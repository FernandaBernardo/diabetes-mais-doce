package br.com.caelum.diabetes.extras;

import android.app.Activity;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import br.com.caelum.diabetes.model.Glicemia;
import jxl.Cell;
import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by Fernanda Bernardo on 26/04/2016.
 */
public class PlanilhaExcel {
    private int COLUNA_OBSERVACAO = 8;
    public File criaArquivo(List<Glicemia> glicemias) {
        File file = new File(Environment.getExternalStorageDirectory(), "TabelaGlicemias.xls");
        try {
            WritableWorkbook wb = Workbook.createWorkbook(file);

            glicemias = ordenaLista(glicemias);

            Calendar dataInicial = glicemias.get(0).getData();
            Calendar dataFinal = glicemias.get(glicemias.size()-1).getData();

            for(int mes = dataInicial.get(Calendar.MONTH); mes <= dataFinal.get(Calendar.MONTH); mes++) {
                WritableSheet sheet = createMonthSheet(glicemias, wb,  mes);

                autoSizeLabel(sheet);
            }
            wb.write();
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    @NonNull
    private WritableSheet createMonthSheet(List<Glicemia> glicemias, WritableWorkbook wb,  int mes) throws WriteException {
        int sheetIndex = wb.getNumberOfSheets();
        WritableSheet sheet = wb.createSheet("Mês " + (mes + 1), sheetIndex);

        criaHeader(sheet);

        int rowDate = 1;
        boolean preencheuData = false;
        int diaGlicemiaAnterior = glicemias.get(0).getData().get(Calendar.DAY_OF_MONTH);

        List<Glicemia> remover = new ArrayList<>();
        for (Glicemia glicemia : glicemias) {
            int excelColumnIndex = glicemia.getTipoRefeicao().getExcelColumnIndex();
            int diaGlicemiaAtual = glicemia.getData().get(Calendar.DAY_OF_MONTH);

            if(glicemia.getData().get(Calendar.MONTH) != mes) break;

            if(diaGlicemiaAnterior != diaGlicemiaAtual) {
                rowDate++;
                diaGlicemiaAnterior = diaGlicemiaAtual;
                preencheuData = false;
            }

            if(!preencheuData) {
                sheet.addCell(new Label(0,
                        rowDate,
                        ParserTools.getParseDate(glicemia.getData())));
                preencheuData = true;
            }

            preecheCelula(sheet, rowDate, glicemia, excelColumnIndex);

            remover.add(glicemia);
        }
        glicemias.removeAll(remover);
        return sheet;
    }

    private void preecheCelula(WritableSheet sheet, int rowDate, Glicemia glicemia, int excelColumnIndex) throws WriteException {
        Cell cell = sheet.getCell(excelColumnIndex, rowDate);
        String valorGlicemiaTexto = Integer.toString(glicemia.getValorGlicemia());

        if(!("".equals(cell.getContents()))) {
            valorGlicemiaTexto = cell.getContents() + "/" + valorGlicemiaTexto;
        }

        sheet.addCell(new Label(excelColumnIndex,
                rowDate,
                valorGlicemiaTexto));

        Cell cellObservacao = sheet.getCell(COLUNA_OBSERVACAO, rowDate);
        String observacao = glicemia.getObservacao();

		if(!(observacao == null || "".equals(observacao))) {
			observacao = glicemia.getTipoRefeicao().getText() + " - " + observacao;
		}

        if(!("".equals(cellObservacao.getContents()))) {
            observacao = cellObservacao.getContents() + " / " + observacao;
        }

        sheet.addCell(new Label(COLUNA_OBSERVACAO,
                rowDate,
                observacao));
    }

    private List<Glicemia> ordenaLista(List<Glicemia> glicemias) {
        Comparator<Glicemia> comparador = new Comparator<Glicemia>() {
            public int compare(Glicemia g1, Glicemia g2) {
                if(g1.getData().getTimeInMillis() > g2.getData().getTimeInMillis()) return 1;
                if(g1.getData().getTimeInMillis() < g2.getData().getTimeInMillis()) return -1;
                return 0;
            }
        };
        Collections.sort(glicemias, comparador);

        return glicemias;
    }

    private void criaHeader(WritableSheet sheet) throws WriteException {
        sheet.addCell(new Label(0, 0, "Data"));
        sheet.addCell(new Label(1, 0, Extras.CAFE_DA_MANHA));
        sheet.addCell(new Label(2, 0, Extras.LANCHE_DA_MANHA));
        sheet.addCell(new Label(3, 0, Extras.ALMOCO));
        sheet.addCell(new Label(4, 0, Extras.LANCHE_DA_TARDE));
        sheet.addCell(new Label(5, 0, Extras.JANTAR));
        sheet.addCell(new Label(6, 0, Extras.CEIA));
        sheet.addCell(new Label(7, 0, Extras.MADRUGADA));
        sheet.addCell(new Label(COLUNA_OBSERVACAO, 0, Extras.OBSERVACAO));
    }

    private void autoSizeLabel(WritableSheet sheet) {
        for (int i = 0; i < sheet.getColumns(); i++) {
            Cell[] cells = sheet.getColumn(i);
            int longestStrLen = -1;

            if (cells.length == 0) continue;

            for (int j = 0; j < cells.length; j++) {
                if ( cells[j].getContents().length() > longestStrLen ) {
                    String str = cells[j].getContents();
                    if (str == null || str.isEmpty()) continue;
                    longestStrLen = str.trim().length();
                }
            }

            if (longestStrLen == -1) continue;

            if (longestStrLen > 255) longestStrLen = 255;

            CellView cv = sheet.getColumnView(i);
            cv.setSize(longestStrLen * 256 + 100);
            sheet.setColumnView(i, cv);
        }
    }
}

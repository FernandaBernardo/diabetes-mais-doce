package br.com.caelum.diabetes.extras;

import android.app.Activity;
import android.os.Environment;

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
    private WritableSheet sheet;

    public File criaArquivo(List<Glicemia> glicemias) {
        File file = new File(Environment.getExternalStorageDirectory(), "TabelaGlicemias.xls");
        try {
            WritableWorkbook wb = Workbook.createWorkbook(file);

            glicemias = ordenaLista(glicemias);

            Calendar dataInicial = glicemias.get(0).getData();
            Calendar dataFinal = glicemias.get(glicemias.size()-1).getData();

            int cont = 0;
            for(int mes = dataInicial.get(Calendar.MONTH); mes <= dataFinal.get(Calendar.MONTH); mes++) {
                wb.createSheet("Mês " + (mes + 1), cont);

                sheet = wb.getSheet(cont);

                criaHeader(sheet);

                int rowDate = 1;
                int dataAtual = glicemias.get(0).getData().get(Calendar.DAY_OF_MONTH);

                List<Glicemia> remover = new ArrayList<>();
                for (Glicemia glicemia : glicemias) {
                    if(glicemia.getData().get(Calendar.MONTH) != mes) break;

                    if(dataAtual != glicemia.getData().get(Calendar.DAY_OF_MONTH)) {
                        rowDate++;
                    }

                    sheet.addCell(new Label(0,
                            rowDate,
                            ParserTools.getParseDate(glicemia.getData())));

                    String valorGlicemia = null;
                    Cell cell = sheet.getCell(glicemia.getTipoRefeicao().getExcelColumnIndex(), rowDate);
                    if(cell.getContents() == "") {
                        valorGlicemia = glicemia.getValorGlicemia() + "";
                    } else {
                        valorGlicemia = cell.getContents() + "/" + glicemia.getValorGlicemia();
                    }

                    sheet.addCell(new Label(glicemia.getTipoRefeicao().getExcelColumnIndex(),
                            rowDate,
                            valorGlicemia));

                    remover.add(glicemia);
                }
                glicemias.removeAll(remover);

                autoSizeLabel(sheet);

                cont++;
            }
            wb.write();
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
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

package br.com.caelum.diabetes.extras;

import android.app.Activity;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
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
    private Activity activity;

    public PlanilhaExcel(Activity activity) {
        this.activity = activity;
    }

    public File criaArquivo(List<Glicemia> glicemias) {
        File file = new File(Environment.getExternalStorageDirectory(), "TabelaGlicemias.xls");
        try {
            WritableWorkbook wb = Workbook.createWorkbook(file);
            wb.createSheet("Glicemias", 0);

            WritableSheet sheet = wb.getSheet(0);

            criaHeader(sheet);

            for (Glicemia glicemia: glicemias) {
                sheet.addCell(new Label(glicemia.getTipoRefeicao().getNum(), glicemia.getData().get(Calendar.DAY_OF_MONTH), glicemia.getValorGlicemia()+""));
                sheet.addCell(new Label(0, glicemia.getData().get(Calendar.DAY_OF_MONTH), Parser.getParseDate(glicemia.getData().get(Calendar.DAY_OF_MONTH), glicemia.getData().get(Calendar.MONTH), glicemia.getData().get(Calendar.YEAR))));
            }

            autoSizeLabel(sheet);

            wb.write();
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private void criaHeader(WritableSheet sheet) throws WriteException {
        sheet.addCell(new Label(0, 0, "Data"));
        sheet.addCell(new Label(1, 0, "Antes do Café"));
        sheet.addCell(new Label(2, 0, "2h depois do Café"));
        sheet.addCell(new Label(3, 0, "Antes do Almoço"));
        sheet.addCell(new Label(4, 0, "2h depois do Almoço"));
        sheet.addCell(new Label(5, 0, "Antes do Jantar"));
        sheet.addCell(new Label(6, 0, "2h depois do Jantar"));
        sheet.addCell(new Label(7, 0, "Madrugada"));
    }

    private void autoSizeLabel(WritableSheet sheet) {
        for (int i = 0; i < sheet.getColumns(); i++) {
            Cell[] cells = sheet.getColumn(i);
            int longestStrLen = -1;

            if (cells.length == 0)
                continue;

            for (int j = 0; j < cells.length; j++) {
                if ( cells[j].getContents().length() > longestStrLen ) {
                    String str = cells[j].getContents();
                    if (str == null || str.isEmpty())
                        continue;
                    longestStrLen = str.trim().length();
                }
            }

            if (longestStrLen == -1)
                continue;

            if (longestStrLen > 255)
                longestStrLen = 255;

            CellView cv = sheet.getColumnView(i);
            cv.setSize(longestStrLen * 256 + 100);
            sheet.setColumnView(i, cv);
        }
    }
}

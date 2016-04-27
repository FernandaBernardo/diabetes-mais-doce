package br.com.caelum.diabetes.extras;

import android.app.Activity;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

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

    public File criaArquivo() {
        File file = new File(Environment.getExternalStorageDirectory(), "TabelaGlicemias.xls");
        try {
            WritableWorkbook wb = Workbook.createWorkbook(file);
            wb.createSheet("Glicemias", 0);

            WritableSheet sheet = wb.getSheet(0);

            Label label = new Label(0, 0, "Primeira célula");
            sheet.addCell(label);

            wb.write();
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}

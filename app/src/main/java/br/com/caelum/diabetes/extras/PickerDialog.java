package br.com.caelum.diabetes.extras;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by Fernanda on 03/07/2015.
 */
public class PickerDialog implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private FragmentManager fragmentManager;
    private Calendar dataSelecionada;
    private TextView dataTextView;
    private TextView horaTextView;

    public PickerDialog(FragmentManager fragmentManager, TextView dataTextView, TextView horaTextView) {
        this(fragmentManager, dataTextView);
        this.horaTextView = horaTextView;
        this.timePickerDialog = TimePickerDialog.newInstance(this, dataSelecionada.get(Calendar.HOUR_OF_DAY) , dataSelecionada.get(Calendar.MINUTE), true);
        setTimeListener();
        setTimeText();
    }

    public PickerDialog(FragmentManager fragmentManager, TextView dataTextView) {
        this.fragmentManager = fragmentManager;
        this.dataSelecionada = Calendar.getInstance();
        this.dataTextView = dataTextView;
        this.datePickerDialog = DatePickerDialog.newInstance(this, dataSelecionada.get(Calendar.YEAR), dataSelecionada.get(Calendar.MONTH), dataSelecionada.get(Calendar.DAY_OF_MONTH));
        setDataListener();
        setDataText();
    }

    public PickerDialog(FragmentManager fragmentManager, TextView dataTextView, Calendar dataInicial) {
        this(fragmentManager, dataTextView);
        this.dataSelecionada = dataInicial;
        this.datePickerDialog = DatePickerDialog.newInstance(this, dataInicial.get(Calendar.YEAR), dataInicial.get(Calendar.MONTH), dataInicial.get(Calendar.DAY_OF_MONTH));
        setDataListener();
        setDataText();
    }

    private void setDataListener () {
        dataTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(true);
                datePickerDialog.show(fragmentManager, "datepicker");
            }
        });
    }

    private void setTimeListener () {
        horaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show(fragmentManager, "timepicker");
                timePickerDialog.setCloseOnSingleTapMinute(true);
            }
        });
    }

    private void setDataText() {
        dataTextView.setText(ParserTools.getParseDate(dataSelecionada));
    }

    private void setTimeText() {
        horaTextView.setText(ParserTools.getParseHour(dataSelecionada));
    }

    public Calendar getDataSelecionada() {
        return dataSelecionada;
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        dataSelecionada.set(year, month, day);
        dataTextView.setText(ParserTools.getParseDate(day, month, year));
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hora, int minuto) {
        dataSelecionada.set(Calendar.HOUR_OF_DAY, hora);
        dataSelecionada.set(Calendar.MINUTE, minuto);

        horaTextView.setText(ParserTools.getParseHour(hora, minuto));
    }
}

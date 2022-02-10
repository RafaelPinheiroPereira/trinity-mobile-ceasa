package br.com.app.rmalimentos.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import br.com.app.rmalimentos.R;
import br.com.app.rmalimentos.model.entity.Sale;
import br.com.app.rmalimentos.utils.DateUtils;
import br.com.app.rmalimentos.viewmodel.ExportViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog.Builder;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExportActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.cv_initial_date)
    CalendarView cvInitialDate;

    @BindView(R.id.cv_final_date)
    CalendarView cvFinalDate;

    @BindView(R.id.btn_export)
    Button btnExport;

    ExportViewModel exportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        ButterKnife.bind(this);
        initViews();
        exportViewModel = new ViewModelProvider(this).get(ExportViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        DateFormat format = DateFormat.getDateInstance(DateFormat.DATE_FIELD);
        try {
            exportViewModel.setInitialDate(format.parse(format.format(cvInitialDate.getDate())));
            exportViewModel.setFinalDate(format.parse(format.format(cvFinalDate.getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.exportViewModel.setContext(this);
        this.exportViewModel.setEmployee(this.exportViewModel.findSessionEmployee());

        cvInitialDate.setOnDateChangeListener(
                (view, year, month, dayOfMonth)->{
                    // display the selected date by using a toast

                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    DateFormat f = DateFormat.getDateInstance(DateFormat.DATE_FIELD);
                    String formatedInitialDate = f.format(c.getTime());
                    try {

                        Date initialDate = f.parse(formatedInitialDate);
                        exportViewModel.setInitialDate(initialDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                });
        cvFinalDate.setOnDateChangeListener(
                (view, year, month, dayOfMonth)->{
                    // display the selected date by using a toast
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    DateFormat f = DateFormat.getDateInstance(DateFormat.DATE_FIELD);
                    String formatedFinalDate = f.format(c.getTime());
                    try {
                        Date finalDate = f.parse(formatedFinalDate);

                        exportViewModel.setFinalDate(finalDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.btn_export)
    public void exportData() {
        if (DateUtils.isValidPeriod(
                this.exportViewModel.getInitialDate(), this.exportViewModel.getFinalDate())) {
            LiveData<List<Sale>> salesListLiveData = this.exportViewModel.searchDataToExportByDate();
            salesListLiveData.observe(this, sales->{
                if (sales.size() > 0) {
                    //vou procurar os itens de cada vendas e preencher
                    exportViewModel.setSales(sales);
                    exportViewModel.loadAllSaleItem();
                    exportViewModel.exportData();
                } else {
                    BottomSheetMaterialDialog mBottomSheetDialog =
                            new Builder(this)
                                    .setTitle("Atenção!")
                                    .setMessage("Não existem dados para serem exportados no período informado!")
                                    .setCancelable(false)
                                    .setPositiveButton(
                                            "OK",
                                            (dialogInterface, which)->{
                                                Toast.makeText(getApplication().getApplicationContext(),
                                                        "Por favor, verifique o período de vendas!",
                                                        Toast.LENGTH_LONG).show();

                                                dialogInterface.dismiss();
                                            })
                                    .build();

                    mBottomSheetDialog.show();

                }
            });


        } else {
            Toast.makeText(
                    this, "Data Inicial dever ser menor ou igual a Data Final!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
    }
}

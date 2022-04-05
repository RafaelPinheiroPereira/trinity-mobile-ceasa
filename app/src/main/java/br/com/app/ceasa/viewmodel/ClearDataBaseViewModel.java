package br.com.app.ceasa.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.Date;

import br.com.app.ceasa.repository.ConfigurationDataRepository;
import br.com.app.ceasa.repository.PaymentRepository;

public class ClearDataBaseViewModel  extends AndroidViewModel {

    PaymentRepository paymentRepository;

    Date initialDate;
    Date finalDate;

    public ClearDataBaseViewModel(@NonNull Application application) {
        super(application);

        paymentRepository = new PaymentRepository(application);
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public void clearDatabase() {
        this.paymentRepository.clearDatabaseByDates(this.getInitialDate(),this.getFinalDate());
    }
}

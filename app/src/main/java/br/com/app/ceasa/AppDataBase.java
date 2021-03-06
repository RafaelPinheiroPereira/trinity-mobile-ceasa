package br.com.app.ceasa;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import br.com.app.ceasa.model.converter.Converters;
import br.com.app.ceasa.model.dao.ClientDAO;
import br.com.app.ceasa.model.dao.ConfigurationDataDAO;
import br.com.app.ceasa.model.dao.HistoricDAO;
import br.com.app.ceasa.model.dao.PaymentDAO;
import br.com.app.ceasa.model.dao.PrinterDPDAO;
import br.com.app.ceasa.model.entity.Client;
import br.com.app.ceasa.model.entity.ConfigurationData;
import br.com.app.ceasa.model.entity.Payment;
import br.com.app.ceasa.model.entity.PrinterDP;

@Database(
    entities = {Client.class, Payment.class, ConfigurationData.class, PrinterDP.class},
    version = 3)
@TypeConverters({Converters.class})
public abstract class AppDataBase extends RoomDatabase {

  private static volatile AppDataBase mAppDataBaseInstance;

  public abstract ClientDAO clientDAO();

  public abstract PaymentDAO paymentDAO();

  public abstract ConfigurationDataDAO configurationDataDAO();

  public abstract HistoricDAO historicDAO();

  public abstract PrinterDPDAO printerDPDAO();

  public static AppDataBase getDatabase(final Context context) {
    if (mAppDataBaseInstance == null) {
      synchronized (AppDataBase.class) {
        if (mAppDataBaseInstance == null) {
          mAppDataBaseInstance =
              Room.databaseBuilder(
                      context.getApplicationContext(), AppDataBase.class, "ceasa")
                  .fallbackToDestructiveMigration()
                  .allowMainThreadQueries()
                  .build();
        }
      }
    }
    return mAppDataBaseInstance;
  }
}

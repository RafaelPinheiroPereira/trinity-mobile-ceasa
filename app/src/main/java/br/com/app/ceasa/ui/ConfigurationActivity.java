package br.com.app.ceasa.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import br.com.app.ceasa.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class ConfigurationActivity extends AppCompatActivity {

  @BindView(R.id.list_view_menu)
  ListView listView;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  private static final String[] menu =
      new String[] {"Data/Valor Base", "Configurar Impressora", "Deletar Dados"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_configuration);
    ButterKnife.bind(this);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    ArrayAdapter<String> adapter =
        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menu);
    this.listView.setAdapter(adapter);
  }

  @OnItemClick({R.id.list_view_menu})
  protected void onListItemClick(int position) {
    switch (position) {
      case 0:
        startActivity(new Intent(this, ConfigurationDataActivity.class));
        break;
      case 1:
        startActivity(new Intent(this, PrinterActivity.class));
        break;
      case 2:
        startActivity(new Intent(this, ClearPaymentActivity.class));
        break;
      default:
        finish();
    }
  }
}

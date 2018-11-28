package de.wullenweber_informatik.micfit.RatesActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import de.wullenweber_informatik.micfit.R;

public class ShowRatesActivity extends AppCompatActivity {

    ListView listrates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rates);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, RatesActivityStarter._theRates);

        listrates = (ListView) findViewById(R.id.listRates);
        listrates.setAdapter(adapter);
    }
}

package ca.ggolda.reference_criminal_code;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcgol on 01/06/2017.
 */

public class ActivitySection extends AppCompatActivity {

    private ArrayList<InstanceProvision> provisions;
    private AdapterProvision mAdapterProvision;
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_section);


        provisions = new ArrayList<>();

        //TODO: get provisions from a source
        InstanceProvision tempProv = new InstanceProvision("YO");
        provisions.add(tempProv);
        provisions.add(tempProv);
        provisions.add(tempProv);
        provisions.add(tempProv);
        provisions.add(tempProv);

        mAdapterProvision = new AdapterProvision(ActivitySection.this, R.layout.card_provision, provisions);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(mAdapterProvision);

    }


}

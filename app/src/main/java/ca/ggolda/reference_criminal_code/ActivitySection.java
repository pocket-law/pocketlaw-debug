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
        InstanceProvision tempProv1 = new InstanceProvision(1, "Possession of weapon for dangerous purpose", "88(1)", "Every person commits an offence who carries or possesses a weapon, an imitation of a weapon, a prohibited device or any ammunition or prohibited ammunition for a purpose dangerous to the public peace or for the purpose of committing an offence.");
        InstanceProvision tempProv2 = new InstanceProvision(1, "Punishment", "(2)", "Every person who commits an offence under subsection (1)");
        InstanceProvision tempSub1 = new InstanceProvision(2, "(a)", "is guilty of an indictable offence and liable to imprisonment for a term not exceeding ten years; or");
        InstanceProvision tempSub2 = new InstanceProvision(2, "(b)", "is guilty of an offence punishable on summary conviction.");
        InstanceProvision tempCite = new InstanceProvision(3, "R.S., 1985, c. C-46, s. 89; 1995, c. 39, s. 139.");

        provisions.add(tempProv1);
        provisions.add(tempProv2);
        provisions.add(tempSub1);
        provisions.add(tempSub2);
        provisions.add(tempCite);




        mAdapterProvision = new AdapterProvision(ActivitySection.this, R.layout.card_provision, provisions);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(mAdapterProvision);

    }


}

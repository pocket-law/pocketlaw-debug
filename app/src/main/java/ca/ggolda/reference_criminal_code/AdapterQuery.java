package ca.ggolda.reference_criminal_code;

/**
 * Created by gcgol on 01/06/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class AdapterQuery extends ArrayAdapter<Section> {


    private Context mContext;

    // Layouts
    private TextView resultLocation;
    private TextView resultText;

    private ListView listViewQuery;
    private ListView listViewSection;


    public AdapterQuery(Context context, int resource, List<Section> objects) {
        super(context, resource, objects);

        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.card_query, parent, false);
        }

        // Heading text/number
        resultLocation = (TextView) convertView.findViewById(R.id.address);
        resultText = (TextView) convertView.findViewById(R.id.text);

        final Section current = getItem(position);

        // TODO: change to full address
        resultLocation.setText(current.getSection());
        resultText.setText(current.getFulltext());

        // Get section and query listViews from main activity
        listViewSection = (ListView) ((ActivityMain) mContext).findViewById(R.id.listview_section);
        listViewQuery = (ListView) ((ActivityMain) mContext).findViewById(R.id.listview_query);


        // TODO: move to corresponding provision on click of query result
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set section listview on basis of TOC selection
                listViewSection.setSelection(current.getID() - 1);

                listViewQuery.setVisibility(View.GONE);

            }
        });

        return convertView;

    }


}
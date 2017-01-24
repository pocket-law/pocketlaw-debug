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

public class AdapterHeading extends ArrayAdapter<Section> {


    private Context mContext;
    private ListView listviewSection;


    public AdapterHeading(Context context, int resource, List<Section> objects) {
        super(context, resource, objects);

        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.card_heading, parent, false);
        }

        final Section current = getItem(position);

        TextView headingOne = (TextView) convertView.findViewById(R.id.marginal_note);
        TextView headingTwo = (TextView) convertView.findViewById(R.id.heading2);

        TextView sectionOne = (TextView) convertView.findViewById(R.id.section);
        TextView sectionTwo = (TextView) convertView.findViewById(R.id.section2);

        LinearLayout levelOne = (LinearLayout) convertView.findViewById(R.id.level1);
        LinearLayout levelTwo = (LinearLayout) convertView.findViewById(R.id.level2);

        listviewSection = (ListView) ((ActivityMain)mContext).findViewById(R.id.listview_section);

        // Set section and heading text
        levelOne.setVisibility(View.VISIBLE);
        headingOne.setText("" + current.getFulltext());
        levelTwo.setVisibility(View.GONE);
        sectionOne.setText("" + current.getSection());


        Log.e("XMLXML1", ""+current.getID());



        //TODO: open corresponding part
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set section listview on basis of TOC selection
                listviewSection.setSelection(current.getID() - 1);
                ActivityMain.partsHideShow();


            }
        });

        return convertView;


    }

}
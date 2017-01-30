package ca.ggolda.reference_criminal_code;

/**
 * Created by gcgol on 01/06/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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

        TextView headingOne = (TextView) convertView.findViewById(R.id.heading);
        TextView sectionOne = (TextView) convertView.findViewById(R.id.section);
        LinearLayout levelOne = (LinearLayout) convertView.findViewById(R.id.level1);


        listviewSection = (ListView) ((ActivityMain)mContext).findViewById(R.id.listview_section);

        // Set section and heading text
        levelOne.setVisibility(View.VISIBLE);
        headingOne.setText("" + current.getFulltext());
        sectionOne.setText("" + current.getSection());

        //Change background color based on heading type
        //TODO: make switch
        if (current.getPinpoint().equals("level1")) {
            headingOne.setBackgroundColor(Color.parseColor("#8C292e34"));
            sectionOne.setBackgroundColor(Color.parseColor("#8C292e34"));
        } else if (current.getPinpoint().equals("level2")) {
            headingOne.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            sectionOne.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        } else if (current.getPinpoint().equals("level3")) {
            headingOne.setBackgroundColor(Color.parseColor("#12FFFFFF"));
            sectionOne.setBackgroundColor(Color.parseColor("#12FFFFFF"));
        } else if (current.getPinpoint().equals("schedules")) {
            headingOne.setBackgroundColor(Color.parseColor("#66e13f0d"));
            sectionOne.setBackgroundColor(Color.parseColor("#66e13f0d"));
        }else if (current.getPinpoint().equals("forms")) {
            headingOne.setBackgroundColor(Color.parseColor("#66e13f0d"));
            sectionOne.setBackgroundColor(Color.parseColor("#66e13f0d"));
        } else if (current.getPinpoint().equals("related_provs")) {
            headingOne.setBackgroundColor(Color.parseColor("#66e13f0d"));
            sectionOne.setBackgroundColor(Color.parseColor("#66e13f0d"));
        } else if (current.getPinpoint().equals("amendments_nif")) {
            headingOne.setBackgroundColor(Color.parseColor("#66e13f0d"));
            sectionOne.setBackgroundColor(Color.parseColor("#66e13f0d"));

        }


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
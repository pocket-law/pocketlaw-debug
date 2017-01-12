package ca.ggolda.reference_criminal_code;

/**
 * Created by gcgol on 01/06/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class AdapterHeading extends ArrayAdapter<Heading> {


    private Context mContext;



    public AdapterHeading(Context context, int resource, List<Heading> objects) {
        super(context, resource, objects);

        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.card_heading, parent, false);
        }

        final Heading current = getItem(position);

        TextView headingOne = (TextView) convertView.findViewById(R.id.heading1);
        TextView headingTwo = (TextView) convertView.findViewById(R.id.heading2);

        TextView sectionOne = (TextView) convertView.findViewById(R.id.section1);
        TextView sectionTwo = (TextView) convertView.findViewById(R.id.section2);

        LinearLayout levelOne = (LinearLayout) convertView.findViewById(R.id.level1);
        LinearLayout levelTwo = (LinearLayout) convertView.findViewById(R.id.level2);

        if (current.getLevel() == 2) {
            levelTwo.setVisibility(View.VISIBLE);
            headingTwo.setText(""+current.getHeading_text());
            levelOne.setVisibility(View.GONE);

            sectionTwo.setText(""+current.getSection());

        } else {
            levelOne.setVisibility(View.VISIBLE);
            headingOne.setText(""+current.getHeading_text());
            levelTwo.setVisibility(View.GONE);

            sectionOne.setText(""+current.getSection());

        }


        //TODO: open corresponding part
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;


    }

}
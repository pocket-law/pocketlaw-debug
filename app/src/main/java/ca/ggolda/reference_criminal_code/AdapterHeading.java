package ca.ggolda.reference_criminal_code;

/**
 * Created by gcgol on 01/06/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        headingOne.setText(current.getHeading_text());
        headingTwo.setText(""+current.getLevel());

        //TODO: open corresponding part
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;


    }

}
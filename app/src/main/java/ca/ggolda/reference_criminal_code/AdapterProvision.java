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

public class AdapterProvision extends ArrayAdapter<Provision> {


    private Context mContext;

    private Provision provision;


    public AdapterProvision(Context context, int resource, List<Provision> objects) {
        super(context, resource, objects);

        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.card_section, parent, false);
        }

        final Provision current = getItem(position);


        LinearLayout provisionLayout = (LinearLayout) convertView.findViewById(R.id.provision_layout);
        TextView provisionName = (TextView) convertView.findViewById(R.id.marginal_note);
        TextView provisionNumber = (TextView) convertView.findViewById(R.id.section);
        TextView provisionText = (TextView) convertView.findViewById(R.id.text);

        LinearLayout subLayout = (LinearLayout) convertView.findViewById(R.id.subsection_layout);
        TextView subNumber = (TextView) convertView.findViewById(R.id.sub_number);
        TextView subText = (TextView) convertView.findViewById(R.id.sub_text);

        TextView citationText = (TextView) convertView.findViewById(R.id.historical_note);


        switch(current.getProvision_type()) {
            case 1:
                provisionLayout.setVisibility(View.VISIBLE);
                subLayout.setVisibility(View.GONE);
                citationText.setVisibility(View.GONE);

                provisionName.setText(current.getProvision_name());
                provisionNumber.setText(current.getProvision_number());
                provisionText.setText(current.getProvision_text());


                break;
            case 2:
                provisionLayout.setVisibility(View.GONE);
                subLayout.setVisibility(View.VISIBLE);
                citationText.setVisibility(View.GONE);

                subNumber.setText(current.getProvision_number());
                subText.setText(current.getProvision_text());

                break;
            case 3:
                provisionLayout.setVisibility(View.GONE);
                subLayout.setVisibility(View.GONE);
                citationText.setVisibility(View.VISIBLE);

                citationText.setText(current.getProvision_text());

                break;

        }







        //TODO: something with?
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;


    }

}
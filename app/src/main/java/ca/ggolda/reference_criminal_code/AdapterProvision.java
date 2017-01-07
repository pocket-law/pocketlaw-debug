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

public class AdapterProvision extends ArrayAdapter<InstanceProvision> {


    private Context mContext;

    private InstanceProvision provision;


    public AdapterProvision(Context context, int resource, List<InstanceProvision> objects) {
        super(context, resource, objects);

        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.card_provision, parent, false);
        }

        TextView provisionName = (TextView) convertView.findViewById(R.id.provision_name);




        //TODO: something with?
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;


    }

}
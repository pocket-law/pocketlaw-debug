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

public class AdapterSection extends ArrayAdapter<Section> {


    private Context mContext;


    public AdapterSection(Context context, int resource, List<Section> objects) {
        super(context, resource, objects);

        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.card_section, parent, false);
        }

        TextView subtext = (TextView) convertView.findViewById(R.id.sub_text);
        TextView subnumber = (TextView) convertView.findViewById(R.id.sub_number);

        TextView text = (TextView) convertView.findViewById(R.id.text_section);
        TextView marginalNote = (TextView) convertView.findViewById(R.id.marginal_note);
        TextView marginalNumber = (TextView) convertView.findViewById(R.id.marginal_number);
        TextView section = (TextView) convertView.findViewById(R.id.section);

        LinearLayout marginalLayout = (LinearLayout) convertView.findViewById(R.id.marginal_layout);
        LinearLayout sectionLayout = (LinearLayout) convertView.findViewById(R.id.section_layout);
        LinearLayout subSectionLayout = (LinearLayout) convertView.findViewById(R.id.subsection_layout);

        TextView historicalNote = (TextView) convertView.findViewById(R.id.historical_note);


        final Section current = getItem(position);

            // Section Marginal Note
        if (current.getType() == 1) {
            marginalNote.setText("" + current.getSectionText());
            marginalNumber.setText("" + current.getSection());
            marginalLayout.setVisibility(View.VISIBLE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.GONE);

            // Section Text
        } else if (current.getType() == 2) {
            text.setText("" + current.getSectionText());
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.VISIBLE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.GONE);

            // Section Subsection
        } else if (current.getType() == 3) {
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.VISIBLE);
            historicalNote.setVisibility(View.GONE);

            subtext.setText(""+current.getSectionText());
            subnumber.setText(""+current.getSection());

            // HistoricalNote
        } else if (current.getType() == 4) {
            historicalNote.setText("" + current.getSectionText());
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.VISIBLE);
        } else {

        }

        section.setText("" + current.getSection());

        //TODO: open corresponding part
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;


    }

}
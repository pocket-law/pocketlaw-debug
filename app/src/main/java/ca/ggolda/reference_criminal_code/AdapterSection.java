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

        // Subsection text/number
        TextView subtext = (TextView) convertView.findViewById(R.id.sub_text);
        TextView subnumber = (TextView) convertView.findViewById(R.id.sub_number);

        // Paragraph text/number
        TextView paratext = (TextView) convertView.findViewById(R.id.para_text);
        TextView paranumber = (TextView) convertView.findViewById(R.id.para_number);

        // Paragraph text/number
        TextView subsectionParatext = (TextView) convertView.findViewById(R.id.subsection_para_text);
        TextView subsectionParanumber = (TextView) convertView.findViewById(R.id.subsection_para_number);


        // Section text/number
        TextView section = (TextView) convertView.findViewById(R.id.section);
        TextView text = (TextView) convertView.findViewById(R.id.text_section);

        // MarginalNote text/number
        TextView marginalNote = (TextView) convertView.findViewById(R.id.marginal_note);
        TextView marginalNumber = (TextView) convertView.findViewById(R.id.marginal_number);

        // SubMarginalNote text/number
        TextView subMarginalNote = (TextView) convertView.findViewById(R.id.submarginal_note);
        TextView subMarginalNumber = (TextView) convertView.findViewById(R.id.submarginal_number);



        LinearLayout subsectionParagraphLayout = (LinearLayout) convertView.findViewById(R.id.subsection_paragraph_layout);
        LinearLayout subMarginalLayout = (LinearLayout) convertView.findViewById(R.id.submarginal_layout);
        LinearLayout marginalLayout = (LinearLayout) convertView.findViewById(R.id.marginal_layout);
        LinearLayout sectionLayout = (LinearLayout) convertView.findViewById(R.id.section_layout);
        LinearLayout subSectionLayout = (LinearLayout) convertView.findViewById(R.id.subsection_layout);
        LinearLayout paragraphLayout = (LinearLayout) convertView.findViewById(R.id.paragraph_layout);


        TextView historicalNote = (TextView) convertView.findViewById(R.id.historical_note);


        final Section current = getItem(position);

        // Section MarginalNote
        if (current.getType() == 1) {
            marginalNote.setText("" + current.getSectionText());
            marginalNumber.setText("" + current.getSection());
            marginalLayout.setVisibility(View.VISIBLE);
            subMarginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.GONE);
            paragraphLayout.setVisibility(View.GONE);
            subsectionParagraphLayout.setVisibility(View.GONE);

            // Section Text
        } else if (current.getType() == 2) {
            text.setText("" + current.getSectionText());
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.VISIBLE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.GONE);
            subMarginalLayout.setVisibility(View.GONE);
            subsectionParagraphLayout.setVisibility(View.GONE);
            paragraphLayout.setVisibility(View.GONE);

            // Section Subsection Text
        } else if (current.getType() == 3) {
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.VISIBLE);
            historicalNote.setVisibility(View.GONE);
            subMarginalLayout.setVisibility(View.GONE);
            subsectionParagraphLayout.setVisibility(View.GONE);
            paragraphLayout.setVisibility(View.GONE);

            subtext.setText("" + current.getSectionText());
            subnumber.setText("" + current.getSection());

            // Section Paragraph
        } else if (current.getType() == 4) {
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.GONE);
            paragraphLayout.setVisibility(View.VISIBLE);
            subsectionParagraphLayout.setVisibility(View.GONE);
            subMarginalLayout.setVisibility(View.GONE);

            paratext.setText("" + current.getSectionText());
            paranumber.setText("" + current.getSection());

            // Subsection MarginalNote
        } else if (current.getType() == 5) {
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.GONE);
            subsectionParagraphLayout.setVisibility(View.GONE);
            paragraphLayout.setVisibility(View.GONE);
            subMarginalLayout.setVisibility(View.VISIBLE);

            subMarginalNote.setText("" + current.getSectionText());
            subMarginalNumber.setText("" + current.getSection());

            // Subsection Paragraph
        } else if (current.getType() == 6) {
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.GONE);
            paragraphLayout.setVisibility(View.GONE);
            subMarginalLayout.setVisibility(View.GONE);
            subsectionParagraphLayout.setVisibility(View.VISIBLE);

            subsectionParatext.setText("" + current.getSectionText());
            subsectionParanumber.setText("" + current.getSection());

            // HistoricalNote
        } else if (current.getType() == 9) {
            historicalNote.setText("" + current.getSectionText());
            marginalLayout.setVisibility(View.GONE);
            sectionLayout.setVisibility(View.GONE);
            subsectionParagraphLayout.setVisibility(View.GONE);
            subMarginalLayout.setVisibility(View.GONE);
            subSectionLayout.setVisibility(View.GONE);
            historicalNote.setVisibility(View.VISIBLE);
            paragraphLayout.setVisibility(View.GONE);
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
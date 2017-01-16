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

    // Layouts
    LinearLayout subsectionParagraphLayout;
    LinearLayout subMarginalLayout;
    LinearLayout marginalLayout;
    LinearLayout sectionLayout;
    LinearLayout subSectionLayout;
    LinearLayout paragraphLayout;
    LinearLayout headingLayout;
    LinearLayout subparagraphLayout;
    LinearLayout subsectionSubparagraphLayout;
    LinearLayout definitionMarginalLayout;
    LinearLayout definitionTextLayout;
    LinearLayout historicalLayout;



    public AdapterSection(Context context, int resource, List<Section> objects) {
        super(context, resource, objects);

        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.card_section, parent, false);
        }

        // Heading text/number
        TextView headingText = (TextView) convertView.findViewById(R.id.heading_text);
        TextView headingNumber = (TextView) convertView.findViewById(R.id.heading_number);

        // Subsection text/number
        TextView subtext = (TextView) convertView.findViewById(R.id.sub_text);
        TextView subnumber = (TextView) convertView.findViewById(R.id.sub_number);

        //Definition MarginalNote
        TextView definitionMarginalNote = (TextView) convertView.findViewById(R.id.defined_marginal);

        //Definition Text
        TextView definitionText = (TextView) convertView.findViewById(R.id.defined_text);

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

        // Subparagraph text/number
        TextView subParaText = (TextView) convertView.findViewById(R.id.subpara_text);
        TextView subParaNumber = (TextView) convertView.findViewById(R.id.subpara_number);

        // Subsection Subparagraph text/number
        TextView subsectionSubParaText = (TextView) convertView.findViewById(R.id.subsection_subpara_text);
        TextView subsectionSubParaNumber = (TextView) convertView.findViewById(R.id.subsection_subpara_number);

        // HistoricalNote
        TextView historicalNote = (TextView) convertView.findViewById(R.id.historical_note);

        subsectionParagraphLayout = (LinearLayout) convertView.findViewById(R.id.subsection_paragraph_layout);
        subMarginalLayout = (LinearLayout) convertView.findViewById(R.id.submarginal_layout);
        marginalLayout = (LinearLayout) convertView.findViewById(R.id.marginal_layout);
        sectionLayout = (LinearLayout) convertView.findViewById(R.id.section_layout);
        subSectionLayout = (LinearLayout) convertView.findViewById(R.id.subsection_layout);
        paragraphLayout = (LinearLayout) convertView.findViewById(R.id.paragraph_layout);
        headingLayout = (LinearLayout) convertView.findViewById(R.id.heading_layout);
        subparagraphLayout = (LinearLayout) convertView.findViewById(R.id.subparagraph_layout);
        subsectionSubparagraphLayout = (LinearLayout) convertView.findViewById(R.id.subsection_subparagraph_layout);
        definitionMarginalLayout = (LinearLayout) convertView.findViewById(R.id.definition_marginal_layout);
        definitionTextLayout = (LinearLayout) convertView.findViewById(R.id.definition_text_layout);
        historicalLayout = (LinearLayout) convertView.findViewById(R.id.historical_layout);




        final Section current = getItem(position);

        // hide all predefined views to allow visibility setting via type
        hideAll();


        // Section Heading
        if (current.getType() == 0) {

            headingText.setText("" + current.getSectionText());
            headingNumber.setText("" + current.getSection());

            headingLayout.setVisibility(View.VISIBLE);

            // Section MarginalNote
        } else if (current.getType() == 1) {

            marginalNote.setText("" + current.getSectionText());
            marginalNumber.setText("" + current.getSection());

            marginalLayout.setVisibility(View.VISIBLE);

            // Section Text
        } else if (current.getType() == 2) {

            text.setText("" + current.getSectionText());

            sectionLayout.setVisibility(View.VISIBLE);

            // Section Subsection Text
        } else if (current.getType() == 3) {

            subtext.setText("" + current.getSectionText());
            subnumber.setText("" + current.getSection());

            subSectionLayout.setVisibility(View.VISIBLE);

            // Section Paragraph
        } else if (current.getType() == 4) {

            paratext.setText("" + current.getSectionText());
            paranumber.setText("" + current.getSection());

            paragraphLayout.setVisibility(View.VISIBLE);

            // Subsection MarginalNote
        } else if (current.getType() == 5) {

            subMarginalNote.setText("" + current.getSectionText());
            subMarginalNumber.setText("" + current.getSection());

            subMarginalLayout.setVisibility(View.VISIBLE);

            // Subsection paragraph
        } else if (current.getType() == 6) {

            subsectionParatext.setText("" + current.getSectionText());
            subsectionParanumber.setText("" + current.getSection());

            subsectionParagraphLayout.setVisibility(View.VISIBLE);

            // Subsection Paragraph
        } else if (current.getType() == 7) {

            subParaText.setText("" + current.getSectionText());
            subParaNumber.setText("" + current.getSection());

            subparagraphLayout.setVisibility(View.VISIBLE);

            // Subsection subParagraph
        } else if (current.getType() == 8) {

            subsectionSubParaText.setText("" + current.getSectionText());
            subsectionSubParaNumber.setText("" + current.getSection());

            subsectionSubparagraphLayout.setVisibility(View.VISIBLE);

            // HistoricalNote
        } else if (current.getType() == 9) {

            historicalNote.setText("" + current.getSectionText());

            historicalLayout.setVisibility(View.VISIBLE);

            // Definition MarginalNote
        } else if (current.getType() == 10) {

            definitionMarginalNote.setText("" + current.getSectionText());

            definitionMarginalLayout.setVisibility(View.VISIBLE);


            // Definition MarginalNote
        } else if (current.getType() == 11) {

            definitionText.setText("" + current.getSectionText());

            definitionTextLayout.setVisibility(View.VISIBLE);


        } else {

        }

        section.setText("" + current.getSection());



        //TODO: do stuff with corresponding provision on click
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;


    }

    private void hideAll() {

        definitionTextLayout.setVisibility(View.GONE);
        definitionMarginalLayout.setVisibility(View.GONE);
        marginalLayout.setVisibility(View.GONE);
        sectionLayout.setVisibility(View.GONE);
        subsectionParagraphLayout.setVisibility(View.GONE);
        subMarginalLayout.setVisibility(View.GONE);
        subSectionLayout.setVisibility(View.GONE);
        historicalLayout.setVisibility(View.GONE);
        paragraphLayout.setVisibility(View.GONE);
        headingLayout.setVisibility(View.GONE);
        subparagraphLayout.setVisibility(View.GONE);
        subsectionSubparagraphLayout.setVisibility(View.GONE);

    }

}
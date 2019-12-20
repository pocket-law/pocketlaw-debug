package ca.ggolda.reference_criminal_code.data_utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ca.ggolda.reference_criminal_code.R;
import ca.ggolda.reference_criminal_code.activities.ActivityPopulate;
import ca.ggolda.reference_criminal_code.objects.Section;

/**
 * Created by gcgol on 01/09/2017.
 */

public class XmlParser {

    private Context mContext;
    private DbHelper dbHelper = DbHelper.getInstance(mContext);

    // We don't use namespaces
    private static final String ns = null;

    private List sections;


    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readStatute(parser);
        } finally {
            in.close();
        }
    }


        public XmlParser(Context context){
            this.mContext = context;
        }

        private void Update(String s){

            final String text = s + "...";
            ActivityPopulate.getHandler().post(new Runnable() {

                public void run() {
                    //ui stuff here :)
                    TextView txtView = (TextView) ((Activity)mContext).findViewById(R.id.txt_log);
                    txtView.setText(text);
                }
            });

        }


    // For skipping.
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        Log.e("SKIP", "SKIP CALLED! can't get rid of yet..." + parser.getName());

        String isSkipping = parser.getName();


        // TODO: this hack seems to get around a bug i cannot understand when
        // TODO: parsing the income tax act after s56(3)(c) -- not sure of any negative impacts on
        // TODO: Income Tax Act or others....
        // TODO: TEST - but seems to work right now
        if (parser.getName().equals("Subsection")) {
            isSkipping = "SAVED";
            String section = "";
            String pinpoint = "";

            readSubsection(parser, section, pinpoint);
        }

        // TODO: for income tax act again, kinda hacky
        if (parser.getName().equals("Section")) {
            isSkipping = "SAVED";
            String section = "";

            readSection(parser, section);
        }


        // TODO: for income tax act again, kinda hacky
        if (parser.getName().equals("ContinuedSubparagraph")) {
            isSkipping = "SAVED";
            String section = "";
            String pinpoint = "";

            readContinuedSubsectionSubparagraph(parser, section, pinpoint);

        } else if (parser.getName().equals("Oath")) {
            isSkipping = "SAVED";
            String section = "Oath";

            readHistorySmallText(parser, section);

        } else if (parser.getName().equals("Footnote")) {
            isSkipping = "SAVED";

            readFootnote(parser);

        } else if (parser.getName().equals("Note")) {
            isSkipping = "SAVED";

            readHistorySmallText(parser, "Note");

        } else if (parser.getName().equals("Provision")) {
            isSkipping = "SAVED";
            String section = "";

            readProvision(parser, section);

        } else if (parser.getName().equals("HistoricalNote")) {
            isSkipping = "SAVED";
            String section = "";

            readHistoricalNote(parser, section);

        } else if (parser.getName().equals("Identification")) {
            isSkipping = "SAVED";
            readIdentification(parser);

        } else if (parser.getName().equals("ReaderNote")) {
            isSkipping = "SAVED";
            readReaderNote(parser);

            //TODO: EXPERIMENT BELOW
        } else if (parser.getName().equals("Definition")) {
            isSkipping = "SAVED";
            readDefinition(parser, "", "");

        } else if (parser.getName().equals("Paragraph")) {
            isSkipping = "SAVED";
            readParagraph(parser, "", "");

        } else if (parser.getName().equals("Subparagraph")) {
            isSkipping = "SAVED";
            readSubparagraph(parser, "", "");

        } else if (parser.getName().equals("Clause")) {
            isSkipping = "SAVED";
            readClause(parser, "", "");

        } else if (parser.getName().equals("Heading")) {
            isSkipping = "SAVED";
            readHeading(parser, "", "");


            //TODO: EXPERIMENT END

        } else {

            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }

        //TODO: go back and make sure you can skip these when you are
        if (!((isSkipping.equals("MarginalNote") || (isSkipping.equals("a")) || (isSkipping.equals("SAVED"))))) {
            Log.e("EEEE", "SKIP: " + isSkipping);
        }
    }

    // Start reading
    private List readStatute(XmlPullParser parser) throws XmlPullParserException, IOException {

        sections = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "Statute");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            // Starts by looking for the Body tag
            if (name.equals("Body")) {
                readBody(parser);
            } else {
                skip(parser);
            }
        }

        // Return array list of all sections
        // TODO: for...?
        return sections;
    }

    // Parses the contents of the body for Heading and Section
    private void readBody(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Body");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Section")) {

                // Get the section number from the Section Code
                String section = "";

                readSection(parser, section);

            } else if (parser.getName().equals("Heading")) {

                // Get the section number from the Heading Code
                String section = "";
                String pinpoint = "";

                // TODO: Because section getting is done in readHeading, I think this if statement can be deleted
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");

                    String[] split_code = code[code.length - 2].split("_");

                    if (split_code[0].equals("l")) {
                        section = "Part " + split_code[split_code.length - 1];
                    } else {
                        section = split_code[split_code.length - 1];
                    }

                    Log.e("EEEE", "to readHeading for section: " + section);

                }


                if ((parser.getAttributeValue(null, "level")) != null) {
                    String[] level = parser.getAttributeValue(null, "level").split("\"");
                    pinpoint = "level" + level[0]; // using pinpoint for level here as no further pinpoint necessary

                    Log.e("XML", "subsectionTrue : " + pinpoint);
                }


                readHeading(parser, section, pinpoint);

            } else {
                skip(parser);
            }

        }

    }

    // Parses the contents of a Heading
    private void readHeading(XmlPullParser parser, String section, String pinpoint) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Heading");


        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("TitleText")) {

//                String regexStr = "^[0-9]*$";
                String regexStr = "(.*)[A-Z](.*)";    //RegEx to attempt override if not already a number and not first word Part

                if ((section.trim().matches(regexStr))) {

                    Log.e("EEEE", "heading section MATCH REGEX: " + section);

                    if ((parser.getAttributeValue(null, "Code")) != null) {
                        String[] code = parser.getAttributeValue(null, "Code").split("\"");

                        String[] split_code = code[code.length - 4].split("_");


                        if (split_code[0].equals("l")) {
                            section = "Part " + split_code[split_code.length - 1];
                        } else {
                            section = split_code[split_code.length - 1];
                        }


                        Log.e("EEEE", "readTitleText for heading section: " + section);

                    }


                } else {
                    Log.e("EEEE", "heading section NO MATCH REGEX: " + section);
                }

                Log.e("PRE-heading", "" + parser.getName() + ", " + section + ", " + pinpoint + ")");

                readTitleText(parser, section, pinpoint);

            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Section.
    private void readSection(XmlPullParser parser, String section) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Section");

        String pinpoint = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("MarginalNote")) {

                readMarginalText(parser, section);

            } else if (parser.getName().equals("Label")) {

                parser.next();

                pinpoint = parser.getText();

                parser.next();

            } else if (parser.getName().equals("Text")) {

                readSectionText(parser, pinpoint);

            } else if (parser.getName().equals("HistoricalNote")) {

                readHistoricalNote(parser, section);

            } else if (parser.getName().equals("Definition")) {

                readDefinition(parser, section, pinpoint);

            } else if (parser.getName().equals("Subsection")) {

                Log.e("EEEE", "readSubsection: " + section + " " + pinpoint);

                readSubsection(parser, section, pinpoint);

            } else if (parser.getName().equals("ContinuedSectionSubsection")) {

                Log.e("EEEE", "readContinuedSectionSubsection(): " + section + " " + section);

                readContinuedSectionSubsection(parser, section, section);


            } else if (parser.getName().equals("Paragraph")) {

                readParagraph(parser, section, pinpoint);


            } else {

                skip(parser);
            }

        }

    }

    // Parses the contents of a Subsection.
    private void readSubsection(XmlPullParser parser, String section, String pinpoint) throws IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Label")) {

                parser.next();

                String localPinpoint = parser.getText();


                if (localPinpoint.equals("(1)")) {
                    Log.e("First Subsection:", "Section: " + pinpoint + " Pinpoint: " + pinpoint + " localPinpoint: " + localPinpoint);


                    pinpoint = pinpoint + localPinpoint;

                    Log.e("First Subsection:", "" + pinpoint);
                } else if (!localPinpoint.equals("")) {
                    pinpoint = localPinpoint;
                } else {
                    Log.e("FIX","Dafix!");
                }

                parser.next();

            } else if (parser.getName().equals("Text")) {

                readSubsectionText(parser, section, pinpoint);

            } else if (parser.getName().equals("MarginalNote")) {

                readSubMarginalText(parser, section, pinpoint);

            } else if (parser.getName().equals("Definition")) {

                readDefinition(parser, section, pinpoint);

            } else if (parser.getName().equals("Paragraph")) {

                Log.e("EEEE", "readSubsectionParagraph(): " + section + " " + pinpoint);

                readSubsectionParagraph(parser, section, pinpoint);

            } else if (parser.getName().equals("ContinuedSectionSubsection")) {

                readContinuedSubsection(parser, section, pinpoint);

            } else {

                skip(parser);
            }
        }
    }

    // Parses the contents of a Subsection.
    private void readContinuedSubsection(XmlPullParser parser, String section, String pinpoint) throws IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                readContinuedSubsectionText(parser, section, pinpoint);

            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Subsection.
    private void readContinuedSectionSubsection(XmlPullParser parser, String section, String pinpoint) throws IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                readContinuedSectionSubsectionText(parser, section, pinpoint);

            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Definition.
    private void readDefinition(XmlPullParser parser, String section, String pinpoint) throws IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }


            if (parser.getName().equals("MarginalNote")) {

                readDefinitionMarginalNoteText(parser, section, pinpoint);

            } else if (parser.getName().equals("Text")) {

                readDefinitionText(parser, section, pinpoint);

                //Recursively call to use the readDefinitionText since the formatting is the same
            } else if (parser.getName().equals("ContinuedDefinition")) {

                readDefinition(parser, section, pinpoint);

            } else if (parser.getName().equals("Paragraph")) {

                readDefinitionParagraph(parser, section, pinpoint);

            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Paragraph.
    private void readParagraph(XmlPullParser parser, String section, String pinpoint) throws IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Label")) {

                parser.next();

                pinpoint = parser.getText();

                parser.next();

            } else if (parser.getName().equals("Text")) {

                readParagraphText(parser, section, pinpoint);

            } else if (parser.getName().equals("Subparagraph")) {

                readSubparagraph(parser, section, pinpoint);

            } else if (parser.getName().equals("ContinuedParagraph")) {

                readContinuedParagraph(parser, section, pinpoint);


            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Definition Paragraph.
    private void readDefinitionParagraph(XmlPullParser parser, String section, String pinpoint) throws IOException, XmlPullParserException {


        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Label")) {

                parser.next();
                pinpoint = parser.getText();
                parser.next();

            } else if (parser.getName().equals("Text")) {

                readParagraphText(parser, section, pinpoint);

            }  else if (parser.getName().equals("Subparagraph")) {

                readSubparagraph(parser, section, pinpoint);

            } else if (parser.getName().equals("ContinuedParagraph")) {

                readContinuedParagraph(parser, section, pinpoint);

            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Subsection Paragraph.
    private void readSubsectionParagraph(XmlPullParser parser, String section, String pinpoint) throws IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Label")) {

                parser.next();

                pinpoint = parser.getText();

                parser.next();

            } else if (parser.getName().equals("Text")) {

                readSubsectionParagraphText(parser, section, pinpoint);

            } else if (parser.getName().equals("Subparagraph")) {

                Log.e("XML", "Subsection Subparagraph");

                readSubsectionSubParagraph(parser, section, pinpoint);

            } else if (parser.getName().equals("ContinuedParagraph")) {

                Log.e("XML", "readContinuedSubsectionParagraph");

                readContinuedSubsectionParagraph(parser, section, pinpoint);

            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Subparagraph.
    private void readSubparagraph(XmlPullParser parser, String section, String pinpoint) throws IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Label")) {

                parser.next();

                pinpoint = parser.getText();

                parser.next();

            } else if (parser.getName().equals("Text")) {

                readSubParagraphText(parser, section, pinpoint);

            } else if (parser.getName().equals("Clause")) {

                Log.e("eeee", "found clause");

                Log.e("eeee", "calling readClause");

                readClause(parser, section, pinpoint);


            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Subsection Subparagraph.
    private void readSubsectionSubParagraph(XmlPullParser parser, String section, String pinpoint) throws IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Label")) {

                parser.next();

                pinpoint = parser.getText();

                parser.next();

            } else if (parser.getName().equals("Text")) {

                readSubsectionSubParagraphText(parser, section, pinpoint);

            } else if (parser.getName().equals("Clause")) {

                Log.e("eeee", "found clause");

                Log.e("eeee", "calling readClause for readSubsectionSubParagraph");

                readClause(parser, section, pinpoint);

            } else if (parser.getName().equals("ContinuedSubparagraph")) {

                readContinuedSubsectionSubparagraph(parser, section, pinpoint);

            } else {
                skip(parser);
            }
        }
    }

    // For Subsection Continued Subsection Paragraph values
    private void readContinuedSubsectionParagraph(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                readContinuedSubsectionParagraphText(parser, section, pinpoint);

            }
        }
    }

    // For Subsection Continued Subsection Paragraph values
    private void readContinuedSubsectionSubparagraph(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                readContinuedSubsectionSubparagraphText(parser, section, pinpoint);

            }
        }
    }

    // For the section HistoricalNote value.
    private void readHistoricalNote(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("ul")) {

                readHistoryListItem(parser, section);

            } else {
                skip(parser);
            }
        }
    }

    // For History List Item
    private void readHistoryListItem(XmlPullParser parser, String section) throws IOException, XmlPullParserException {

        Log.e("EEEEE", "readHistoryListItem");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("li")) {

                readHistorySmallText(parser, section);

            } else {

                skip(parser);

            }
        }
    }


    // For Continued Paragraph
    private void readContinuedParagraph(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                readContinuedParagraphText(parser, section, pinpoint);

            }
        }
    }


    // For Clause in Subparagraph
    private void readClause(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Label")) {

                parser.next();

                pinpoint = parser.getText();

                parser.next();

            } else if (parser.getName().equals("Text")) {

                readSubparagraphClauseText(parser, section, pinpoint);

            } else if (parser.getName().equals("Subclause")) {

                Log.e("eeee", "SKIP -- not -- pinpoit subclause:" + pinpoint);

                readSubclause(parser, section, pinpoint);

            } else {
                skip(parser);
            }
        }
    }

    // For Clause in Subparagraph
    private void readSubclause(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Label")) {

                parser.next();

                pinpoint = parser.getText();

                parser.next();

            } else if (parser.getName().equals("Text")) {

                readSubclauseText(parser, section, pinpoint);

            } else if (parser.getName().equals("Subsubclause")) {

                    readSubsubclause(parser, section, pinpoint);

            } else {
                skip(parser);
            }
        }
    }

    // For Clause in Subparagraph
    private void readSubsubclause(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Label")) {

                parser.next();

                pinpoint = parser.getText();

                parser.next();

            } else if (parser.getName().equals("Text")) {

                readSubsubclauseText(parser, section, pinpoint);


            } else {
                skip(parser);
            }
        }
    }


    // for footnotes
    private void readFootnote(XmlPullParser parser) throws
            IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                readFootnoteText(parser);

            } else {

                skip(parser);
            }
        }
    }

    // for footnotes
    private void readReaderNote(XmlPullParser parser) throws
            IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Note")) {

                readHistorySmallText(parser, "ReaderNote");

            } else {

                skip(parser);
            }
        }
    }

    // for footnotes
    private void readProvision(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                readProvisionText(parser, section);

            } else {

                skip(parser);
            }
        }
    }


    // for <Identification>
    private void readIdentification(XmlPullParser parser) throws
            IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("LongTitle")) {

                readLongTitle(parser);

            } else {

                skip(parser);
            }
        }
    }



















    /////////////////////////////////////
    //
    //   READ TEXT ENDPOINTS BELOW
    //
    /////////////////////////////////////




























    // For the section MarginalNote value.
    private void readMarginalText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Update(text);


        Section resultObject = new Section(1, "marginalNote", section, text);
        dbHelper.insertSectionDetail(resultObject);

        //  Log.e("XML", "db add readMarginalText (  1  , " + "marginalNote" + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For Subsection Continued Subsection text values.
    private void readContinuedSubsectionText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(14, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);

        //   Log.e("XML", "db add readContinuedSubsectionText (  14  , " + pinpoint + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For Subsection Continued Subsection text values.
    private void readContinuedSectionSubsectionText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(14, pinpoint, section, text);

        Log.e("RESULT OBJ", "continuedsst" + resultObject + "" + resultObject.getSection());

        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add readContinuedSectionSubsectionText (  14  , " + pinpoint + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For the subsection MarginalNote value.
    private void readSubMarginalText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();


        Section resultObject = new Section(5, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);

        //   Log.e("XML", "db add readSubMarginalText (  5  , " + pinpoint + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For Subsection text values.
    private void readSubsectionText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(3, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);

        //   Log.e("XML", "db add readSubsectionText (  3  , " + pinpoint + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }


    // For Continued Paragraph text values.
    private void readContinuedParagraphText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(12, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);

        //    Log.e("XML", "db add readContinuedParagraphText (  12  , " + pinpoint + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {

            skip(parser);
        }
    }

    // For Continued Subsection Paragraph text values.
    private void readContinuedSubsectionParagraphText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(13, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);

        //    Log.e("XML", "db add Continued Subsection Paragraph Text (  13  , " + pinpoint + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For Continued Subsection Paragraph text values.
    private void readContinuedSubsectionSubparagraphText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(18, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add readContinuedSubsectionSubparagraphText (  18  , " + pinpoint + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For SubParagraph text values.
    private void readSubParagraphText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(7, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);

        //     Log.e("XML", "db add SubParagraph Text (  7  , " + pinpoint + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For Subsection SubParagraph text values.
    private void readSubsectionSubParagraphText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(8, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);

        //     Log.e("XML", "db add Subsection SubParagraph Text (  8  , " + pinpoint + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

    }


    // For History List Item Text
    private void readHistorySmallText(XmlPullParser parser, String section) throws IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Log.e("EEEEE", "readHistorySmallText parser.getText: " + text);

        if (text != null) {
            // if the history item starts with a space, remove it
            text = text.startsWith(" ") ? text.substring(1) : text;

            Section resultObject = new Section(9, "nopinpoint", section, text);
            dbHelper.insertSectionDetail(resultObject);

//            Log.e("XML", "db add Historical Note (  9  , " + "nopinpoint" + section + " " + text + " )");
        }

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

    }

    // For Section text values.
    private void readSectionText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(2, "section text", section, text);
        dbHelper.insertSectionDetail(resultObject);

        //    Log.e("XML", "db add Section Text (  2  , " + "section text" + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

    }

    // For the Definition MarginalNote value.
    private void readDefinitionMarginalNoteText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        // Skip tags within the marginal note, grab only text
        // TODO: consider a diff approach
        if (text != null) {

            Section resultObject = new Section(10, pinpoint, section, text);
            dbHelper.insertSectionDetail(resultObject);

            //      Log.e("XML", "db add Definition Marginal Note (  10  , " + pinpoint + section + " " + text + " )");

            if (parser.next() == XmlPullParser.START_TAG) {
                skip(parser);
            }
        }

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

    }

    // For definition text values.
    private void readDefinitionText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        //   Log.e("XML", "db add readDefinitionTextBEFORE (  11  , " + pinpoint + section + " " + text + " )");

        Section resultObject = new Section(11, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);


        if (parser.next() == XmlPullParser.START_TAG) {
//            if (!parser.getName().equals("b")) {
                skip(parser);
//            }
        }

    }

    // For Subsection Paragraph text values.
    private void readSubsectionParagraphText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(6, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add readSubsectionParagraphText (  6  , " + pinpoint + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

//     For Paragraph text values.
    private void readParagraphText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {


        parser.next();

        String text = parser.getText();


        Section resultObject = new Section(4, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add readParagraphText (  4  , " + pinpoint + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {


            if (!parser.getName().equals("b")) {
                Log.e("XML", "db add readParagraphText SKIPPING a " + parser.getName());

                skip(parser);
            }
        }

    }



    // For subparagraph clause text values.
    private void readSubparagraphClauseText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(15, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add readSubparagraphClauseText (  15  , " + pinpoint + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For subparagraph clause text values.
    private void readSubclauseText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(17, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add readSubclauseText (  17  , " + pinpoint + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For subsubclause text values.
    private void readSubsubclauseText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(19, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add readSubclauseText (19, " + pinpoint + section + " " + text + ")");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }
    }

    // For the tags TitleText and level values.
    private void readTitleText(XmlPullParser parser, String section, String pinpoint) throws IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(0, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);

        Log.e("XML", "db add heading TitleText (  0  , " + pinpoint + "," + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }


    }

    // For the Footnote value.
    private void readFootnoteText(XmlPullParser parser) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();


        Section resultObject = new Section(9, "Footnote", "Footnote", "* " + text);
        dbHelper.insertSectionDetail(resultObject);


        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

    }

    // For the Footnote value.
    private void readProvisionText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(18, "Provision", section, text);
        dbHelper.insertSectionDetail(resultObject);


        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

    }


    // For the LongTitle value.
    private void readLongTitle(XmlPullParser parser) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();

        Section resultObject = new Section(1, "", "LongTitle", text);
        dbHelper.insertSectionDetail(resultObject);


        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

    }

}


package org.pocketlaw.pocketlaw_debug;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcgol on 01/09/2017.
 */

public class XmlParserFORMULA {

    private Context mContext;
    private DbHelper dbHelper = DbHelper.getInstance(this.mContext);

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


    // For skipping.
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        String isSkipping = parser.getName();

        Log.e("EEEE", "SKIP: " + isSkipping);


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
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    section = code[1];

                }

                readSection(parser, section);


            } else {
                skip(parser);
            }
        }
    }


    // Parses the contents of a Section.
    private void readSection(XmlPullParser parser, String section) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Section");

        String pinpoint = section; // for now pinpoint equals section

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("FormulaGroup")) {

                readFormulaGroup(parser, section, pinpoint);

            } else if (parser.getName().equals("Subsection")) {

                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    pinpoint = "(" + code[(code.length - 1)] + ")";
                }

                readSubsection(parser, section, pinpoint);

            } else {

                skip(parser);
            }
        }
    }

    // Parses the contents of a Section.
    private void readSubsection(XmlPullParser parser, String section, String pinpoint) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Subsection");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("FormulaGroup")) {

                readFormulaGroup(parser, section, pinpoint);

            } else {

                skip(parser);
            }
        }
    }


    // for formula group (in income tax act)
    private void readFormulaGroup(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        Log.e("eeee", "in readFormulaGroup");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            Log.e("eeee", "in while parser.getName()" + parser.getName());


            if (parser.getName().equals("Formula")) {

                Log.e("eeee", "FORMULA  FOUND");

                readFormula(parser, section, pinpoint);

            } else if (parser.getName().equals("FormulaConnector")) {

                Log.e("eeee", "FORMULA CONNECTOR FOUND");

                readDefinitionText(parser, section, pinpoint);


            } else if (parser.getName().equals("FormulaDefinition")) {

                Log.e("eeee", "FORMULA DEFINITION FOUND");

                readFormulaDefinition(parser, section, pinpoint);


            } else {

                skip(parser);
            }
        }
    }

    // for formula
    private void readFormula(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        Log.e("eeee", "in readFormula");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            Log.e("eeee", "in while parser.getName()" + parser.getName());


            if (parser.getName().equals("FormulaText")) {

                Log.e("eeee", "FORMULATEXT FOUND");

                readFormulaText(parser, section, pinpoint);

            } else {

                skip(parser);
            }
        }
    }

    // for formula definition (in income tax act)
    private void readFormulaDefinition(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        Log.e("eeee", "in readFormulaDefinition");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            Log.e("eeee", "in while parser.getName()" + parser.getName());


            if (parser.getName().equals("FormulaTerm")) {

                Log.e("eeee", "FORMULATERM  FOUND");

                readFormulaDefinitionText(parser, section, pinpoint);

            } else if (parser.getName().equals("Text")) {


                Log.e("eeee", "FORMULA DEFINITION TEXT  FOUND");

                readDefinitionText(parser, section, pinpoint);


            } else if (parser.getName().equals("FormulaParagraph")) {


                Log.e("eeee", "FORMULA PARAGRAPH  FOUND");

                readFormulaParagraph(parser, section, pinpoint);

            } else {

                skip(parser);
            }
        }
    }

    // for formula definition (in income tax act)
    private void readFormulaParagraph(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        Log.e("eeee", "in readFormulaParagraph");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            Log.e("eeee", "readFormulaParagraph in while parser.getName()" + parser.getName());


            if (parser.getName().equals("Label")) {

                Log.e("eeee", "Label  FOUND");

                readFormulaParagraphLabel(parser, section);


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


    // For the tags FormulaText
    private void readFormulaText(XmlPullParser parser, String section, String pinpoint) throws IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();
        Log.e("XML", "db add readFormulaText ( 16, " + pinpoint + section + text + ")");
        Section resultObject = new Section(16, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);
//
//        Log.e("XML", "db add readFormulaText (  nothing doing for now )");
//
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
            skip(parser);
        }
    }

    // For the Definition MarginalNote value.
    private void readFormulaDefinitionText(XmlPullParser parser, String section, String pinpoint) throws
            IOException, XmlPullParserException {

        parser.next();

        String text = parser.getText();


        Section resultObject = new Section(10, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);


        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

    }

    // For the tags FormulaText
    private void readFormulaParagraphLabel(XmlPullParser parser, String section) throws IOException, XmlPullParserException {

        Log.e("eeee", "in readFormulaParagraphLabel");

        parser.next();
        String pinpoint = parser.getText();
        String text = "";


        Log.e("eeee", "1 readFormulaParagraphLabel after label .getName()" + parser.getName() + " .getText()" + parser.getText());

        parser.next();

        Log.e("eeee", "2 readFormulaParagraphLabel after label .getName()" + parser.getName() + " .getText()" + parser.getText());

        parser.next();

        Log.e("eeee", "3 readFormulaParagraphLabel after label .getName()" + parser.getName() + " .getText()" + parser.getText());

        parser.next();

        Log.e("eeee", "4 readFormulaParagraphLabel after label .getName()" + parser.getName() + " .getText()" + parser.getText());


        text = parser.getText();


        Log.e("XML", "db add readFormulaParagraphLabel ( 6, " + pinpoint + " " + section + text + ")");
        Section resultObject = new Section(6, pinpoint, section, text);
        dbHelper.insertSectionDetail(resultObject);

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }


    }

}


package ca.ggolda.reference_criminal_code;

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

public class XmlSectionParser {
    // We don't use namespaces
    private static final String ns = null;

    private List sections;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

        sections = new ArrayList();

        Log.e("XML", "readFeed");

        parser.require(XmlPullParser.START_TAG, ns, "Statute");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.e("XML", "getName: " + name);

            // Starts by looking for the Body tag
            if (name.equals("Body")) {
                Log.e("XML", "Body");
                readBody(parser);
            } else {
                skip(parser);
            }
        }

        Log.e("XML ENDSIZE", "" + sections.size());

        return sections;
    }

    // Parses the contents of the body for Heading and Section tags and hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
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

                    Log.e("XML", "sectionTrue : " + section);
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

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("MarginalNote")) {
                Log.e("XML", "MarginalNote");

                readMarginalNote(parser, section);

            } else if (parser.getName().equals("Text")) {
                Log.e("XML", "Text");

                readSectionText(parser, section);

            } else if (parser.getName().equals("HistoricalNote")) {
                Log.e("XML", "HistoricalNote");

                readHistoricalNote(parser);

            } else if (parser.getName().equals("Subsection")) {
                Log.e("XML", "Subsection");

                String subsection = null;

                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    subsection = "(" + code[3] + ")";

                    Log.e("XML", "subsectionTrue : " + subsection);
                }

                readSubsection(parser, subsection);

            } else if (parser.getName().equals("Paragraph")) {
                Log.e("XML", "Paragraph");

                String subsection = null;
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    subsection = "(" + code[3] + ")";

                }

                readParagraph(parser, subsection);


            } else {

                skip(parser);
            }

        }

    }

    // For the section MarginalNote value.
    private List readMarginalNote(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        //TODO: read the documentation man
        //
        parser.next();

        String text = parser.getText();
        Section resultObject = new Section(1, section, text);
        sections.add(resultObject);

        Log.e("XML", "sections.addMargNote( " + section + " , " + text + " )");
        Log.e("XML", "sections.size in parser :" + sections.size());

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

        return sections;
    }


    // Parses the contents of a Subsection.
    private void readSubsection(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        Log.e("XML", "readSubsection, parser.getText: " + parser.getText() + " .getName: " + parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            Log.e("XML", "readSubsection aftwhile" + parser.getName());

            if (parser.getName().equals("Text")) {

                readSubsectionText(parser, subsection);

            } else if (parser.getName().equals("MarginalNote")) {

                readSubMarginalNote(parser, subsection);

            } else if (parser.getName().equals("Paragraph")) {
                Log.e("XML", "Paragraph");

                String subparasection = null;
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    subparasection = "(" + code[5] + ")";

                }

                readSubsectionParagraph(parser, subparasection);


            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Paragraph.
    private void readParagraph(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        Log.e("XML", "readSubsection, parser.getText: " + parser.getText() + " .getName: " + parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                Log.e("XML", "Paragraph Text" + parser.getName());

                readParagraphText(parser, subsection);

            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Subsection Paragraph.
    private void readSubsectionParagraph(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        Log.e("XML", "readSubsectionParagraph, parser.getText: " + parser.getText() + " .getName: " + parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                Log.e("XML", "Subsection Paragraph Text" + parser.getName());

                readSubsectionParagraphText(parser, subsection);

            } else {
                skip(parser);
            }
        }
    }

    // For Subsection text values.
    private List readSubsectionText(XmlPullParser parser, String subsection) throws
            IOException, XmlPullParserException {

        //TODO: read the documentation man
        //
        parser.next();

        String sub_text = parser.getText();

        Log.e("XML", "subsections.addText( " + " 3, " + sub_text + " )" + "VikaWowow");

        Section subsection_text = new Section(3, subsection, sub_text);
        sections.add(subsection_text);

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

        return sections;
    }

    // For the subsection MarginalNote value.
    private List readSubMarginalNote(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        //TODO: read the documentation man
        //
        parser.next();

        String text = parser.getText();
        Section resultObject = new Section(5, section, text);
        sections.add(resultObject);

        Log.e("XML", "sections.addMargNote( " + section + " , " + text + " )");
        Log.e("XML", "sections.size in parser :" + sections.size());

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

        return sections;
    }

    // For Paragraph text values.
    private List readParagraphText(XmlPullParser parser, String subsection) throws
            IOException, XmlPullParserException {

        //TODO: read the documentation man
        //
        parser.next();

        String paratext = parser.getText();

        Log.e("XML", "paragraph.addText( " + " 4, " + paratext + " )");

        Section subsection_text = new Section(4, subsection, paratext);
        sections.add(subsection_text);

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

        return sections;
    }

    // For Subsection Paragraph text values.
    private List readSubsectionParagraphText(XmlPullParser parser, String subsection) throws
            IOException, XmlPullParserException {

        //TODO: read the documentation man
        //
        parser.next();

        String paratext = parser.getText();

        Log.e("XML", "paragraph.addText( " + " 6, " + paratext + " )");

        Section subsection_text = new Section(6, subsection, paratext);
        sections.add(subsection_text);

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

        return sections;
    }

    // For the section HistoricalNote value.
    private List readHistoricalNote(XmlPullParser parser) throws
            IOException, XmlPullParserException {

        Log.e("XML", "readHistoricalNote");

        Section histNote = new Section(9, "historicalnote", "historicalnote");
        sections.add(histNote);

        skip(parser);

        //TODO: do

        return sections;
    }

    // For Section text values.
    private List readSectionText(XmlPullParser parser, String section) throws
            IOException, XmlPullParserException {

        //TODO: read the documentation man
        //
        parser.next();

        String text = parser.getText();
        Section resultObject = new Section(2, section, text);
        sections.add(resultObject);

        Log.e("XML", "sections.addText( " + section + " , " + text + " )");
        Log.e("XML", "sections.size in parser :" + sections.size());

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

        return sections;
    }

    // For skipping.
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
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
}
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

public class SectionXmlParser {
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

            } else if (parser.getName().equals("Heading")) {

                Log.e("HEADING", "HEADING!");


                // Get the section number from the Heading Code
                String section = "";
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    section = code[1];

                    Log.e("XML", "heading sectionTrue : " + section);
                }

                readHeading(parser, section);

            } else {
                skip(parser);
            }

        }

    }

    // Parses the contents of a Heading
    private void readHeading(XmlPullParser parser, String section) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Heading");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("TitleText")) {
                Log.e("XML", "TitleText");

                readTitleText(parser, section);

            } else {
                skip(parser);
            }
        }
    }

    // For the tags TitleText and level values.
    private List readTitleText(XmlPullParser parser, String section) throws IOException, XmlPullParserException {
        Section resultObject = null;

        parser.next();

        String text = parser.getText();

        resultObject = new Section(0, section, text);

        sections.add(resultObject);
        Log.e("XML", "sectionHeading.add( " + 0 + " , " + section + " " + text + " )");

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

        return sections;
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

            } else if (parser.getName().equals("Definition")) {
                Log.e("XML", "Definition");

                String subsection = null;

                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    subsection = code[1];

                    Log.e("XML", "definitionTrue : " + subsection);
                }

                readDefinition(parser, subsection);

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

            } else if (parser.getName().equals("ContinuedSectionSubsection")) {
                Log.e("XML", "ContinuedSectionSubsection");

                readContinuedSubsection(parser, subsection);


            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Subsection.
    private void readContinuedSubsection(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {
                Log.e("XML", "Text");

                readContinuedSubsectionText(parser, subsection);


            } else {
                skip(parser);
            }
        }
    }

    // For Subsection SubParagraph text values.
    private List readContinuedSubsectionText(XmlPullParser parser, String subsection) throws
            IOException, XmlPullParserException {

        //TODO: read the documentation man
        //
        parser.next();

        String paratext = parser.getText();

        Log.e("XML", "subsection subparagraph.addText( " + " 14, " + paratext + " )");

        Section subsection_text = new Section(14, subsection, paratext);
        sections.add(subsection_text);

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

        return sections;
    }

    // Parses the contents of a Subsection.
    private void readDefinition(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        Log.e("XML", "readDefinition, parser.getText: " + parser.getText() + " .getName: " + parser.getName());

        if ((parser.getAttributeValue(null, "Code")) != null) {
            String[] code = parser.getAttributeValue(null, "Code").split("\"");

            //TODO: clean this up, this is horrible :(
            String temp = code[3];
            String[] englishAndFrenchTemp = temp.split("[}]");
            String definedTerm =  englishAndFrenchTemp[0].substring(1);

            Section definedNameSection = new Section(10, code[1], definedTerm);
            sections.add(definedNameSection);
            Log.e("XML", "definedTerm.addText( " + " 10, " + code[1] + " " + definedTerm + " )");

        }


        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            Log.e("XML", "readDefinition aftwhile" + parser.getName());

            if (parser.getName().equals("Text")) {

                readDefinitionText(parser, subsection);

                //Recursively call to use the readDefinitionText since the formatting is the same
            } else if (parser.getName().equals("ContinuedDefinition")) {

                readDefinition(parser, subsection);

            } else if (parser.getName().equals("Paragraph")) {
                Log.e("XML", "Definition Paragraph");

                String definition_section = null;
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    definition_section = "(" + code[5] + ")";

                }

                readDefinitionParagraph(parser, definition_section);


            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Paragraph.
    private void readParagraph(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        Log.e("XML", "readParagraph, parser.getText: " + parser.getText() + " .getName: " + parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                Log.e("XML", "Paragraph Text" + parser.getName());

                readParagraphText(parser, subsection);

            } else if (parser.getName().equals("Subparagraph")) {

                Log.e("XML", "Subparagraph");

                String subpara_section = null;
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");

                    subpara_section = "(" + code[5] + ")";

                }

                readSubparagraph(parser, subpara_section);

            } else if (parser.getName().equals("ContinuedParagraph")) {

                Log.e("XML", "Continued Paragraph");

                readContinuedParagraph(parser, subsection);

            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Definition Paragraph.
    private void readDefinitionParagraph(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        Log.e("XML", "readParagraph, parser.getText: " + parser.getText() + " .getName: " + parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                Log.e("XML", "Paragraph Text" + parser.getName());

                readParagraphText(parser, subsection);

            } else if (parser.getName().equals("Subparagraph")) {

                Log.e("XML", "Subparagraph");

                String subpara_section = null;
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");

                    subpara_section = "(" + code[7] + ")";


                }

                readSubparagraph(parser, subpara_section);

            } else if (parser.getName().equals("ContinuedParagraph")) {

                Log.e("XML", "Continued Paragraph" + parser.getName());

                readContinuedParagraph(parser, subsection);

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

            } else if (parser.getName().equals("Subparagraph")) {

                Log.e("XML", "Subsection Subaragraph");

                String subpara_section = null;
                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");
                    subpara_section = "(" + code[7] + ")";

                }

                readSubsectionSubParagraph(parser, subpara_section);

            } else if (parser.getName().equals("ContinuedParagraph")) {

                Log.e("XML", "Continued Subsection Paragraph");

                readContinuedSubsectionParagraph(parser, subsection);

            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Subparagraph.
    private void readSubparagraph(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        Log.e("XML", "readSubsection, parser.getText: " + parser.getText() + " .getName: " + parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                Log.e("XML", "Paragraph Text" + parser.getName());

                readSubParagraphText(parser, subsection);


            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of a Subsection Subparagraph.
    private void readSubsectionSubParagraph(XmlPullParser parser, String subsection) throws IOException, XmlPullParserException {

        Log.e("XML", "readSubsection, parser.getText: " + parser.getText() + " .getName: " + parser.getName());

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                Log.e("XML", "Paragraph Text" + parser.getName());

                readSubsectionSubParagraphText(parser, subsection);

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

    // For Subsection text values.
    private List readDefinitionText(XmlPullParser parser, String subsection) throws
            IOException, XmlPullParserException {

        //TODO: read the documentation man
        //
        parser.next();

        String sub_text = parser.getText();

        Log.e("XML", "subsections.addText( " + " 11, " + sub_text + " )");

        Section subsection_text = new Section(11, subsection, sub_text);
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

    // For continued paragraph
    private void readContinuedParagraph(XmlPullParser parser, String subsection) throws
            IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                Log.e("XML", "Paragraph Text" + parser.getName());

                readContinuedParagraphText(parser, subsection);

            }
        }
    }

    // For Continued Paragraph text values.
    private List readContinuedParagraphText(XmlPullParser parser, String subsection) throws
            IOException, XmlPullParserException {

        //TODO: read the documentation man
        //
        parser.next();

        String paratext = parser.getText();

        Log.e("XML", "continued paragraph.addText( " + " 12, " + paratext + " )");

        Section subsection_text = new Section(12, subsection, paratext);
        sections.add(subsection_text);

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

        return sections;
    }

    // For Continued Subsection Paragraph text values.
    private List readContinuedSubsectionParagraphText(XmlPullParser parser, String subsection) throws
            IOException, XmlPullParserException {

        //TODO: read the documentation man
        //
        parser.next();

        String paratext = parser.getText();

        Log.e("XML", "continued paragraph.addText( " + " 13, " + paratext + " )");

        Section subsection_text = new Section(13, subsection, paratext);
        sections.add(subsection_text);

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

        return sections;
    }

    // For SubParagraph text values.
    private List readSubParagraphText(XmlPullParser parser, String subsection) throws
            IOException, XmlPullParserException {

        //TODO: read the documentation man
        //
        parser.next();

        String paratext = parser.getText();

        Log.e("XML", "subparagraph.addText( " + " 7, " + paratext + " )");

        Section subsection_text = new Section(7, subsection, paratext);
        sections.add(subsection_text);

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

        return sections;
    }

    // For Subsection SubParagraph text values.
    private List readSubsectionSubParagraphText(XmlPullParser parser, String subsection) throws
            IOException, XmlPullParserException {

        //TODO: read the documentation man
        //
        parser.next();

        String paratext = parser.getText();

        Log.e("XML", "subsection subparagraph.addText( " + " 8, " + paratext + " )");

        Section subsection_text = new Section(8, subsection, paratext);
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

    // For Subsection Paragraph text values.
    private void readContinuedSubsectionParagraph(XmlPullParser parser, String subsection) throws
            IOException, XmlPullParserException {

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("Text")) {

                Log.e("XML", "Continued Subsection Paragraph Text" + parser.getName());

                readContinuedSubsectionParagraphText(parser, subsection);

            }
        }
    }


    //TODO: get more than the first HistoricalNote
    // For the section HistoricalNote value.
    private void readHistoricalNote(XmlPullParser parser) throws
            IOException, XmlPullParserException {

        Log.e("XML", "readHistoricalNote");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("ul")) {
                Log.e("XML", "ul");

                readHistoryListItem(parser);

            } else {
                skip(parser);
            }
        }
    }

    private void readHistoryListItem(XmlPullParser parser) throws IOException, XmlPullParserException {


        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("li")) {

                readHistoryListItemText(parser);

            } else {

                skip(parser);

            }
        }
    }

    private List readHistoryListItemText(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.next();

        String histNote = parser.getText();

        if (histNote != null) {
            Section historicalNotesSection = new Section(9, "historicalnote", histNote);
            sections.add(historicalNotesSection);
            Log.e("XML", "historicalnote.addText( " + " 9, " + histNote + " )");
        }

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }

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
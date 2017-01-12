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
            String name = parser.getName();
            if (name.equals("Section")) {

                // Get the section number from the TitleText code
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

    // Parses the contents of an entry. If it encounters a DefinedTermEn, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private void readSection(XmlPullParser parser, String section) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Section");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("MarginalNote")) {
                Log.e("XML", "TitleText");

                readText(parser, section);
            } else {
                skip(parser);
            }

        }

    }


    // For the section marginal note value.
    private List readText(XmlPullParser parser, String section) throws IOException, XmlPullParserException {
        Section resultObject = null;

        Log.e("XML", "parser next: " + parser.next());

        String text = parser.getText();

        Log.e("XML", "text: " + text);

        resultObject = new Section(section, text);

        sections.add(resultObject);

        Log.e("XML", "sections.add( " + section + " , " + text + " )");

        Log.e("XML", "sections.size in parser :" + sections.size());

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }


        return sections;
    }

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
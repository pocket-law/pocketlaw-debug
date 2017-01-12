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

public class XmlParser {
    // We don't use namespaces
    private static final String ns = null;

    private List headings;
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

        headings = new ArrayList();

        Log.e("XML", "readFeed");

        parser.require(XmlPullParser.START_TAG, ns, "Statute");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.e("XML", "getName" + name);

            // Starts by looking for the Text tag
            // Starts by looking for the entry tag
            if (name.equals("Body")) {
                Log.e("XML", "SectionYO");
                readBody(parser);
            } else {
                skip(parser);
            }
        }

        return headings;
    }

    // Parses the contents of an entry. If it encounters a DefinedTermEn, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private void readBody(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Body");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Heading")) {
                Log.e("XML", "Heading");

                int level = 1;
                String section = "";

                Log.e("XML", "level " + level);

                if ((parser.getAttributeValue(null, "level")) != null) {

                    level = Integer.valueOf(parser.getAttributeValue(null, "level"));

                    Log.e("XML", "levelTrue " + level);

                }


                Log.e("XML", "valof " + parser.getAttributeValue(0));


                readHeading(parser, level, section);
            } else {
                skip(parser);
            }
        }

        Log.e("XML ENDSIZE", "" + headings.size());

    }

    // Parses the contents of an entry. If it encounters a DefinedTermEn, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private void readHeading(XmlPullParser parser, int level, String section) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Heading");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("TitleText")) {
                Log.e("XML", "TitleText");

                if ((parser.getAttributeValue(null, "Code")) != null) {
                    String[] code = parser.getAttributeValue(null, "Code").split("\"");

                    Log.e("XML", "codeTrue" + (parser.getAttributeValue(null, "Code")));

                    Log.e("XML", "codeLength : " + code.length);

                    if (code.length > 5) {
                        section = code[3];
                    } else {
                        section = code[1];
                    }
                    Log.e("XML", "sectionTrue : " + section);

                }

                readText(parser, level, section);
            } else {
                skip(parser);
            }


        }

    }


    // For the tags text and level values.
    private List readText(XmlPullParser parser, int level, String section) throws IOException, XmlPullParserException {
        Heading resultObject = null;

        Log.e("XML", "parser next: " + parser.next());

        String text = parser.getText();

        Log.e("XML", "text: " + text);

        resultObject = new Heading(text, level, section);

        headings.add(resultObject);
        Log.e("XML", "headings.add( " + text + " , " + level + " " + section + " )");

        Log.e("XML", "headings.size in parser :" + headings.size());

        if (parser.next() == XmlPullParser.START_TAG) {
            skip(parser);
        }


        return headings;
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
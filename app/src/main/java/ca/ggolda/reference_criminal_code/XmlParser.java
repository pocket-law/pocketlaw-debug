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
        return sections;
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
                readHeading(parser);
            } else {
                skip(parser);
            }
        }

        Log.e("XML ENDSIZE", ""+sections.size());

    }

    // Parses the contents of an entry. If it encounters a DefinedTermEn, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private void readHeading(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Heading");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("TitleText")) {
                Log.e("XML", "TitleText");
                readText(parser);
            } else {
                skip(parser);
            }




        }

    }


    // For the tags text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();

            sections.add(result);
            Log.e("XML", "sections.add( " + result + " )");

            parser.nextTag();

        }
        return result;
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

    public static class Section {

        public final String DefinedTermEn;

        private Section(String DefinedTermEn) {
            this.DefinedTermEn = DefinedTermEn;
        }
    }
}
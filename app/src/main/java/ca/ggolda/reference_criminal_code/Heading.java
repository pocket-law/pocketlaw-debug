package ca.ggolda.reference_criminal_code;

/**
 * Created by gcgol on 01/11/2017.
 */

public class Heading {

    private String mHeadingText;
    private int mLevel;



    // Constructor for Heading with all attributes
    public Heading(String heading_text, int level) {
        mHeadingText = heading_text;
        mLevel = level;

    }

    public String getHeading_text() {
        return mHeadingText;
    }
    public void setHeading_text(String mHeadingText) {
        this.mHeadingText = mHeadingText;
    }


    public int getLevel() {
        return mLevel;
    }
    public void setLevel(int mLevel) {
        this.mLevel = mLevel;
    }


}

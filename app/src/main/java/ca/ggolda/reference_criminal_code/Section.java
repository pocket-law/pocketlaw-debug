package ca.ggolda.reference_criminal_code;

/**
 * Created by gcgol on 01/11/2017.
 */

public class Section {

    // mType will determine weather:
    // 1: Section MarginalNote
    // 2: Section Text
    // 9: Section HistoricalNote
    private int mType;
    private String mSection;
    private String mSectionText;


    // Constructor for Heading with all attributes
    public Section(int type, String section, String section_text) {
        mType = type;
        mSection = section;
        mSectionText = section_text;

    }

    public int getType() {
        return mType;
    }
    public void setType(int mType) {
        this.mType = mType;
    }

    public String getSection() {
        return mSection;
    }
    public void setSection(String mSection) {
        this.mSection = mSection;
    }

    public String getSectionText() {
        return mSectionText;
    }
    public void setSectionText(String mSectionText) {
        this.mSectionText = mSectionText;
    }


}

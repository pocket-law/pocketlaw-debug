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
    private String mMarginalNote;


    // Constructor for Heading with all attributes
    public Section(int type, String section, String marginalNote) {
        mType = type;
        mSection = section;
        mMarginalNote = marginalNote;

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
        return mMarginalNote;
    }
    public void setMarginalNote(String mMarginalNote) {
        this.mMarginalNote = mMarginalNote;
    }


}

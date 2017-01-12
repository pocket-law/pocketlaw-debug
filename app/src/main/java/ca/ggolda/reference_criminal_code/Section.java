package ca.ggolda.reference_criminal_code;

/**
 * Created by gcgol on 01/11/2017.
 */

public class Section {

    private String mSection;
    private String mMarginalNote;




    // Constructor for Heading with all attributes
    public Section(String section, String marginalNote) {
        mSection = section;
        mMarginalNote = marginalNote;

    }


    public String getSection() {
        return mSection;
    }
    public void setSection(String mSection) {
        this.mSection = mSection;
    }

    public String getMarginalNote() {
        return mMarginalNote;
    }
    public void setMarginalNote(String mMarginalNote) {
        this.mMarginalNote = mMarginalNote;
    }


}

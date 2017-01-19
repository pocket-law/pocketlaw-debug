package ca.ggolda.reference_criminal_code;

import java.io.Serializable;

/**
 * Created by gcgol on 01/11/2017.
 */

public class Section implements Serializable {

    // mType will determine whether:
    private int mType;

    private String mGroup;
    private String mSection;
    private String mFulltext;


    // Constructor for Heading with all attributes
    public Section(int type, String group, String section, String fulltext) {
        mType = type;
        mGroup = group;
        mSection = section;
        mFulltext = fulltext;

    }

    public int getType() {
        return mType;
    }
    public void setType(int mType) {
        this.mType = mType;
    }

    public String getGroup() {
        return mGroup;
    }
    public void setGroup(String mGroup) {
        this.mGroup = mGroup;
    }

    public String getSection() {
        return mSection;
    }
    public void setSection(String mSection) {
        this.mSection = mSection;
    }

    public String getFulltext() {
        return mFulltext;
    }
    public void setFulltext(String mFulltext) {
        this.mFulltext = mFulltext;
    }


}

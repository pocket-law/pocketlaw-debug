package ca.ggolda.reference_criminal_code;

/**
 * Created by gcgol on 11/01/2016.
 */
public class InstanceProvision {


    private int mProvisionType;
    private String mProvisionName;
    private String mProvisionNumber;
    private String mProvisionText;


    // Required for firebase
    public InstanceProvision() {
    }

    // Constructor for InstanceProvision with all attributes (sections)
    public InstanceProvision(int provision_type, String provision_name, String provision_number, String provision_text) {
        mProvisionType = provision_type;
        mProvisionName = provision_name;
        mProvisionNumber = provision_number;
        mProvisionText = provision_text;
    }

    // Constructor for InstanceProvision subs
    public InstanceProvision(int provision_type, String provision_number, String provision_text) {
        mProvisionType = provision_type;
        mProvisionNumber = provision_number;
        mProvisionText = provision_text;
    }

    // Constructor for InstanceProvision citations
    public InstanceProvision(int provision_type, String provision_text) {
        mProvisionType = provision_type;
        mProvisionText = provision_text;
    }


    public int getProvision_type() {
        return mProvisionType;
    }
    public void setProvision_type(int mProvisionType) {
        this.mProvisionType = mProvisionType;
    }

    public String getProvision_name() {
        return mProvisionName;
    }
    public void setProvision_name(String mProvisionName) {
        this.mProvisionName = mProvisionName;
    }

    public String getProvision_number() {
        return mProvisionNumber;
    }
    public void setProvision_number(String mProvisionNumber) {
        this.mProvisionNumber = mProvisionNumber;
    }

    public String getProvision_text() {
        return mProvisionText;
    }
    public void setProvision_text(String mProvisionText) {
        this.mProvisionText = mProvisionText;
    }


}
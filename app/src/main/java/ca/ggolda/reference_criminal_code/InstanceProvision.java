package ca.ggolda.reference_criminal_code;

/**
 * Created by gcgol on 11/01/2016.
 */
public class InstanceProvision {


    private String mProvisionName;


    // Required for firebase
    public InstanceProvision() {
    }

    // Constructor for InstanceGame with all attributes
    public InstanceProvision(String provision_name) {

        mProvisionName = provision_name;

    }


    public String getProvision_name() {
        return mProvisionName;
    }
    public void setProvision_name(String mProvisionName) {
        this.mProvisionName = mProvisionName;
    }


}

package com.android.showmeeapp.model.UserDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Profile {

    @SerializedName("tags")
    @Expose
    private List<String> tags = new ArrayList<String>();
    @SerializedName("excludedClubs")
    @Expose
    private List<Object> excludedClubs = new ArrayList<Object>();
    @SerializedName("school")
    @Expose
    private String school;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    /**
     * No args constructor for use in serialization
     *
     */
    public Profile() {
    }

    /**
     *
     * @param tags
     * @param school
     * @param phoneNumber
     * @param excludedClubs
     */
    public Profile(List<String> tags, List<Object> excludedClubs, String school, String phoneNumber) {
        this.tags = tags;
        this.excludedClubs = excludedClubs;
        this.school = school;
        this.phoneNumber = phoneNumber;
    }

    /**
     *
     * @return
     *     The tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     *     The tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     *     The excludedClubs
     */
    public List<Object> getExcludedClubs() {
        return excludedClubs;
    }

    /**
     *
     * @param excludedClubs
     *     The excludedClubs
     */
    public void setExcludedClubs(List<Object> excludedClubs) {
        this.excludedClubs = excludedClubs;
    }

    /**
     *
     * @return
     *     The school
     */
    public String getSchool() {
        return school;
    }

    /**
     *
     * @param school
     *     The school
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     *
     * @return
     *     The phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @param phoneNumber
     *     The phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}

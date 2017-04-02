
package com.android.showmeeapp.model.UserDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Services {

    @SerializedName("google")
    @Expose
    private Google google;
    @SerializedName("resume")
    @Expose
    private Resume resume;

    /**
     * No args constructor for use in serialization
     *
     */
    public Services() {
    }

    /**
     *
     * @param resume
     * @param google
     */
    public Services(Google google, Resume resume) {
        this.google = google;
        this.resume = resume;
    }

    /**
     *
     * @return
     *     The google
     */
    public Google getGoogle() {
        return google;
    }

    /**
     *
     * @param google
     *     The google
     */
    public void setGoogle(Google google) {
        this.google = google;
    }

    /**
     *
     * @return
     *     The resume
     */
    public Resume getResume() {
        return resume;
    }

    /**
     *
     * @param resume
     *     The resume
     */
    public void setResume(Resume resume) {
        this.resume = resume;
    }

}

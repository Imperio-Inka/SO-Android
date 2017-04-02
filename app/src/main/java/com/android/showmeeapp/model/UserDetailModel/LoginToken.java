
package com.android.showmeeapp.model.UserDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginToken {

    @SerializedName("when")
    @Expose
    private When when;
    @SerializedName("hashedToken")
    @Expose
    private String hashedToken;

    /**
     * No args constructor for use in serialization
     *
     */
    public LoginToken() {
    }

    /**
     *
     * @param when
     * @param hashedToken
     */
    public LoginToken(When when, String hashedToken) {
        this.when = when;
        this.hashedToken = hashedToken;
    }

    /**
     *
     * @return
     *     The when
     */
    public When getWhen() {
        return when;
    }

    /**
     *
     * @param when
     *     The when
     */
    public void setWhen(When when) {
        this.when = when;
    }

    /**
     *
     * @return
     *     The hashedToken
     */
    public String getHashedToken() {
        return hashedToken;
    }

    /**
     *
     * @param hashedToken
     *     The hashedToken
     */
    public void setHashedToken(String hashedToken) {
        this.hashedToken = hashedToken;
    }

}

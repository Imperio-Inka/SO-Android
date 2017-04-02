
package com.android.showmeeapp.model.UserDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Resume {

    @SerializedName("loginTokens")
    @Expose
    private List<LoginToken> loginTokens = new ArrayList<LoginToken>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Resume() {
    }

    /**
     *
     * @param loginTokens
     */
    public Resume(List<LoginToken> loginTokens) {
        this.loginTokens = loginTokens;
    }

    /**
     *
     * @return
     *     The loginTokens
     */
    public List<LoginToken> getLoginTokens() {
        return loginTokens;
    }

    /**
     *
     * @param loginTokens
     *     The loginTokens
     */
    public void setLoginTokens(List<LoginToken> loginTokens) {
        this.loginTokens = loginTokens;
    }

}


package com.android.showmeeapp.model.UserDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Google {

    @SerializedName("accessToken")
    @Expose
    private String accessToken;
    @SerializedName("idToken")
    @Expose
    private String idToken;
    @SerializedName("expiresAt")
    @Expose
    private Integer expiresAt;
    @SerializedName("scope")
    @Expose
    private List<String> scope = new ArrayList<String>();
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("verified_email")
    @Expose
    private Boolean verifiedEmail;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("given_name")
    @Expose
    private String givenName;
    @SerializedName("family_name")
    @Expose
    private String familyName;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;
    @SerializedName("locale")
    @Expose
    private String locale;

    /**
     * No args constructor for use in serialization
     *
     */
    public Google() {
    }

    /**
     *
     * @param scope
     * @param accessToken
     * @param expiresAt
     * @param locale
     * @param familyName
     * @param givenName
     * @param refreshToken
     * @param idToken
     * @param verifiedEmail
     * @param id
     * @param picture
     * @param email
     * @param name
     * @param gender
     */
    public Google(String accessToken, String idToken, Integer expiresAt, List<String> scope, String id, String email, Boolean verifiedEmail, String name, String givenName, String familyName, String picture, String gender, String refreshToken, String locale) {
        this.accessToken = accessToken;
        this.idToken = idToken;
        this.expiresAt = expiresAt;
        this.scope = scope;
        this.id = id;
        this.email = email;
        this.verifiedEmail = verifiedEmail;
        this.name = name;
        this.givenName = givenName;
        this.familyName = familyName;
        this.picture = picture;
        this.gender = gender;
        this.refreshToken = refreshToken;
        this.locale = locale;
    }

    /**
     *
     * @return
     *     The accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     *
     * @param accessToken
     *     The accessToken
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     *
     * @return
     *     The idToken
     */
    public String getIdToken() {
        return idToken;
    }

    /**
     *
     * @param idToken
     *     The idToken
     */
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    /**
     *
     * @return
     *     The expiresAt
     */
    public Integer getExpiresAt() {
        return expiresAt;
    }

    /**
     *
     * @param expiresAt
     *     The expiresAt
     */
    public void setExpiresAt(Integer expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     *
     * @return
     *     The scope
     */
    public List<String> getScope() {
        return scope;
    }

    /**
     *
     * @param scope
     *     The scope
     */
    public void setScope(List<String> scope) {
        this.scope = scope;
    }

    /**
     *
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     *     The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     *     The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     *     The verifiedEmail
     */
    public Boolean getVerifiedEmail() {
        return verifiedEmail;
    }

    /**
     *
     * @param verifiedEmail
     *     The verified_email
     */
    public void setVerifiedEmail(Boolean verifiedEmail) {
        this.verifiedEmail = verifiedEmail;
    }

    /**
     *
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     *     The givenName
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     *
     * @param givenName
     *     The given_name
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     *
     * @return
     *     The familyName
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     *
     * @param familyName
     *     The family_name
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     *
     * @return
     *     The picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     *
     * @param picture
     *     The picture
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     *
     * @return
     *     The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     *     The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     *     The refreshToken
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     *
     * @param refreshToken
     *     The refreshToken
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     *
     * @return
     *     The locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     *
     * @param locale
     *     The locale
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

}


package com.android.showmeeapp.model;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@JsonPropertyOrder({
    "summary",
    "description",
    "start",
    "end",
    "location",
    "visibility",
    "school",
    "club",
    "tags",
    "phoneNumber",
    "displayName",
    "responseStatus",
    "userId"
})
public class EventSO {

    @JsonProperty("summary")
    private String summary;
    @JsonProperty("description")
    private String description;
    @JsonProperty("start")
    private String start;
    @JsonProperty("end")
    private String end;
    @JsonProperty("location")
    private String location;
    @JsonProperty("visibility")
    private String visibility;
    @JsonProperty("school")
    private String school;
    @JsonProperty("club")
    private String club;
    @JsonProperty("tags")
    private List<String> tags = new ArrayList<String>();
//    @JsonProperty("phoneNumber")
//    private String phoneNumber;
//    @JsonProperty("displayName")
//    private String displayName;
//    @JsonProperty("responseStatus")
//    private String responseStatus;
//    @JsonProperty("userId")
//    private String userId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The summary
     */
    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    /**
     *
     * @param summary
     *     The summary
     */
    @JsonProperty("summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     *
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     *     The start
     */
    @JsonProperty("start")
    public String getStart() {
        return start;
    }

    /**
     *
     * @param start
     *     The start
     */
    @JsonProperty("start")
    public void setStart(String start) {
        this.start = start;
    }

    /**
     *
     * @return
     *     The end
     */
    @JsonProperty("end")
    public String getEnd() {
        return end;
    }

    /**
     *
     * @param end
     *     The end
     */
    @JsonProperty("end")
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     *
     * @return
     *     The location
     */
    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location
     *     The location
     */
    @JsonProperty("location")
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return
     *     The visibility
     */
    @JsonProperty("visibility")
    public String getVisibility() {
        return visibility;
    }

    /**
     *
     * @param visibility
     *     The visibility
     */
    @JsonProperty("visibility")
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    /**
     *
     * @return
     *     The school
     */
    @JsonProperty("school")
    public String getSchool() {
        return school;
    }

    /**
     *
     * @param school
     *     The school
     */
    @JsonProperty("school")
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     *
     * @return
     *     The club
     */
    @JsonProperty("club")
    public String getClub() {
        return club;
    }

    /**
     *
     * @param club
     *     The club
     */
    @JsonProperty("club")
    public void setClub(String club) {
        this.club = club;
    }

    /**
     *
     * @return
     *     The tags
     */
    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     *     The tags
     */
    @JsonProperty("tags")
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     *     The phoneNumber
     */
//    @JsonProperty("phoneNumber")
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    /**
//     *
//     * @param phoneNumber
//     *     The phoneNumber
//     */
//    @JsonProperty("phoneNumber")
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }

//    /**
//     *
//     * @return
//     *     The displayName
//     */
//    @JsonProperty("displayName")
//    public String getDisplayName() {
//        return displayName;
//    }
//
//    /**
//     *
//     * @param displayName
//     *     The displayName
//     */
//    @JsonProperty("displayName")
//    public void setDisplayName(String displayName) {
//        this.displayName = displayName;
//    }
//
//    /**
//     *
//     * @return
//     *     The responseStatus
//     */
//    @JsonProperty("responseStatus")
//    public String getResponseStatus() {
//        return responseStatus;
//    }
//
//    /**
//     *
//     * @param responseStatus
//     *     The responseStatus
//     */
//    @JsonProperty("responseStatus")
//    public void setResponseStatus(String responseStatus) {
//        this.responseStatus = responseStatus;
//    }
//
//    /**
//     *
//     * @return
//     *     The userId
//     */
//    @JsonProperty("userId")
//    public String getUserId() {
//        return userId;
//    }
//
//    /**
//     *
//     * @param userId
//     *     The userId
//     */
//    @JsonProperty("userId")
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

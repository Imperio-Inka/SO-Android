package com.android.showmeeapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 17/08/16.
 */
import java.io.Serializable;

/**
 * Created by admin on 17/08/16.
 */
public class AddLocalAttendees implements Serializable{

	@SerializedName("phoneNumber")
	@Expose
	private String phoneNumber;

	@SerializedName("displayName")
	@Expose
	private String displayName;

	@SerializedName("responseStatus")
	@Expose
	private String responseStatus;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}


}


package com.android.showmeeapp.model.UserDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {
	public static UserModel userModel;

	@SerializedName("_id")
	@Expose
	private String id;
	@SerializedName("createdAt")
	@Expose
	private CreatedAt createdAt;
	@SerializedName("services")
	@Expose
	private Services services;
	@SerializedName("profile")
	@Expose
	private Profile profile;

	/**
	 * No args constructor for use in serialization
	 */
	private UserModel() {
	}

	public static UserModel getInstance(){
		if(userModel==null){
			userModel= new UserModel();
		}
		return  userModel;
	}

	/**
	 * @param id
	 * @param services
	 * @param createdAt
	 * @param profile
	 */
	public UserModel(String id, CreatedAt createdAt, Services services, Profile profile) {
		this.id = id;
		this.createdAt = createdAt;
		this.services = services;
		this.profile = profile;
	}

	/**
	 * @return The id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The _id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return The createdAt
	 */
	public CreatedAt getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt The createdAt
	 */
	public void setCreatedAt(CreatedAt createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return The services
	 */
	public Services getServices() {
		return services;
	}

	/**
	 * @param services The services
	 */
	public void setServices(Services services) {
		this.services = services;
	}

	/**
	 * @return The profile
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * @param profile The profile
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

}

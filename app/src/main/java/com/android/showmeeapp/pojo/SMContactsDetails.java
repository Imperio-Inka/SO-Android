package com.android.showmeeapp.pojo;

/**
 * Created by admin on 12/08/16.
 */
public class SMContactsDetails {
	String name;
	String contact;
	String email;
	String image;

	public SMContactsDetails(String name, String contact, String email, String image) {
		this.name = name;
		this.contact = contact;
		this.email = email;
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}

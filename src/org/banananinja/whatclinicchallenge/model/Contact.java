package org.banananinja.whatclinicchallenge.model;

import android.content.Intent;

public class Contact {
	
	public static final String CONTACT_ID = "id";
	public static final String NAME = "name";
	public static final String PHONE = "phone";
	public static final String COUNTRY = "country";
	public static final String EMAIL = "email";
	public static final String POSITION = "position";
	
    //Constructors
	public Contact(String id, String name, String phone, String country, String email) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.country = country;
		this.email = email;
	}
	
	public Contact(Intent data) {
		id = data.getStringExtra(CONTACT_ID);
		name = data.getStringExtra(Contact.NAME);
		phone = data.getStringExtra(Contact.PHONE);
		country = data.getStringExtra(Contact.COUNTRY);
		email = data.getStringExtra(Contact.EMAIL);
	}

	//Instance variables
	private String id;
	
	private String name;
	
	private String phone;
	
	private String country;
	
	private String email;

	//Getters and setters
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	//Other
	//Package a contact instance member variables in an intent
	public static void packageAsIntent(Intent intent, String id, String name, String phone, String country, String email){
		intent.putExtra(CONTACT_ID, id);
		intent.putExtra(Contact.NAME, name);
		intent.putExtra(Contact.COUNTRY, country);
		intent.putExtra(Contact.PHONE, phone);
		intent.putExtra(Contact.EMAIL, email);
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", phone=" + phone
				+ ", country=" + country + ", email=" + email + "]";
	}
	
	
	
}

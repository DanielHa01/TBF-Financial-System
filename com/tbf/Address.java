/**
 * Address object library
 * 
 * @author Pete Tungtweegait, Daniel Ha
 */
package com.tbf;

public class Address {
	public Address(String street, String city, String state, String zip, String country) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
	}
	
	final private String street;
	final private String city;
	final private String state;
	final private String zip;
	final private String country;
	
	public String getStreet() {
		return this.street;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public String getState() {
		return this.state;
	}
	
	public String getZip() {
		return this.zip;
	}
	
	public String getCountry() {
		return this.country;
	}
}

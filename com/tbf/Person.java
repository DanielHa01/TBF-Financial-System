/**
 * Library for Person class (subclass of Persons)
 * 
 * @author Daniel Ha
 */
package com.tbf;

public class Person {
	public Person(String lastName, String firstName) {
		this.lastName = lastName;
		this.firstName = firstName;
	}

	private String lastName;
	private String firstName;

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}

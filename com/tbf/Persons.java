/**
 * Persons object library
 * 
 * @author Pete Tungtweegait, Daniel Ha
 */
package com.tbf;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Persons {
	public Persons(String personCode, BrokerData brokerData, Person person, Address address, String email) {
		this.personCode = personCode;
		this.brokerData = brokerData;
		this.person = person;
		this.address = address;
		this.email = email;
	}

	private String personCode;
	private BrokerData brokerData;
	private Person person;
	private Address address;
	private String email;

	public String getPersonCode() {
		return this.personCode;
	}

	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}

	public BrokerData getBrokerData() {
		return this.brokerData;
	}

	public void setBrokerData(BrokerData brokerData) {
		this.brokerData = brokerData;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public static List<Persons> loadPersonsData() {
		File f = new File("data/Persons.dat");
		Scanner s = null;
		try {
			s = new Scanner(f);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		int personSize = Integer.parseInt(s.nextLine());
		List<Persons> Persons = new ArrayList<>();
		for(int i = 0; i < personSize; i++) {
			String line = s.nextLine();
			Persons p = null;
			String tokens[] = line.split(";", -1);
			String personCode = tokens[0];
			BrokerData bData = null;
			if (tokens[1].contains(",")) {
				String brokerData[] = tokens[1].split(",");
				bData = new BrokerData(brokerData[0], brokerData[1]);
			} else {
				String brokerData = tokens[1];
				bData = new BrokerData(brokerData, brokerData);
			}

			String name[] = tokens[2].split(",");
			Person person = new Person(name[0], name[1]);

			String address[] = tokens[3].split(",");
			Address ad = new Address(address[0], address[1], address[2], address[3], address[4]);

			String emails = tokens[4];

			p = new Persons(personCode, bData, person, ad, emails);
			Persons.add(p);
		}
		s.close();
		return Persons;
	}
	
	public static List<Persons> getPersons() {
		List<Persons> Persons = new ArrayList<>();
		
		String DRIVER_CLASS = "com.mysql.jdbc.Driver";
		try {
			Class.forName(DRIVER_CLASS).newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://cse.unl.edu/dha", "dha", "27JEmKcj");
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		String query = "SELECT " + 
					"p.personCode, b.brokerSEC, b.brokerType, p.firstName, p.lastName, e.email, " +
					"a.street, a.city, a.state, a.zipCode, a.country " +
					"FROM Persons p " + 
					"LEFT JOIN Brokers b ON b.personId = p.personId " + 
					"LEFT JOIN Emails e on e.personId = p.personId " +
					"LEFT JOIN Address a on a.personId = p.personId;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String personCode = rs.getString("personCode");
				String brokerSEC = rs.getString("brokerSEC");
				String brokerType = rs.getString("brokerType");
				BrokerData brokerData = new BrokerData(brokerType, brokerSEC);
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				Person person = new Person(lastName, firstName);
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zip = rs.getString("zipCode");
				String country = rs.getString("country");
				Address address = new Address(street, city, state, zip, country);
				String email = rs.getString("email");
				Persons p = new Persons(personCode, brokerData, person, address, email);
				Persons.add(p);
			}

		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
			return Persons;
	}
}

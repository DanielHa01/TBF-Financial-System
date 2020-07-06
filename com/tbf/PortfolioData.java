package com.tbf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PortfolioData {
	
	static Connection conn = null;
	
	public static void connectToDatabase(){
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
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://cse.unl.edu/dha", "dha", "27JEmKcj");
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
	}
	
	public static void removeAllPersons() {
		
		connectToDatabase();
		
		String query = "DELETE FROM Address;";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "DELETE FROM Emails;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "DELETE FROM Brokers;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "SELECT PortfolioAssetId FROM PortfolioAsset;";
		Integer PortfolioAssetId = null;
		
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				PortfolioAssetId = rs.getInt("PortfolioAssetId"); 
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
	
		if(PortfolioAssetId != null) {
			query = "DELETE FROM PorfolioAsset;";
			
			try {
				ps = conn.prepareStatement(query);
				ps.executeUpdate(query);
			} catch (SQLException sqle) {
				throw new RuntimeException(sqle);
			}
		}
		
		query = "SELECT PortfolioId FROM Portfolios;";
		Integer PortfolioId = null;
		
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				PortfolioId = rs.getInt("PortfolioId"); 
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		if(PortfolioId != null) {
			query = "DELETE FROM Portfolios;";
			
			try {
				ps = conn.prepareStatement(query);
				ps.executeUpdate(query);
			} catch (SQLException sqle) {
				throw new RuntimeException(sqle);
			}
		}
		
		query = "DELETE FROM Persons;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		try {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
	}
	
	public static void removePerson(String personCode) {
		connectToDatabase();
		
		String query = "DELETE " + 
					"FROM Address a " + 
					"LEFT JOIN Persons p " +
					"ON a.personId = p.personId " +
					"WHERE p.personCode = ?;";
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "DELETE " + 
				"FROM Emails e " + 
				"LEFT JOIN Persons p " +
				"ON e.personId = p.personId " +
				"WHERE p.personCode = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "DELETE " + 
				"FROM Brokers b" + 
				"LEFT JOIN Persons p " +
				"ON b.personId = p.personId " +
				"WHERE p.personCode = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "DELETE " + 
				"FROM PortfolioAsset pa " +
				"LEFT JOIN Portfolios p " + 
				"ON pa.portfolioId = p.portfolioId " +
				"LEFT JOIN Persons pe " + 
				"ON pe.personId = p.ownerId " + 
				"WHERE pe.personCode = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "DELETE " + 
				"FROM PortfolioAsset pa " +
				"LEFT JOIN Portfolios p " + 
				"ON pa.portfolioId = p.portfolioId " +
				"LEFT JOIN Persons pe " + 
				"ON pe.personId = p.managerId " + 
				"WHERE pe.personCode = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "DELETE " + 
				"FROM Portfolio p " +
				"LEFT JOIN Persons pe " + 
				"ON pe.personId = p.ownerId " + 
				"WHERE pe.personCode = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "DELETE " + 
				"FROM Portfolio p " +
				"LEFT JOIN Persons pe " + 
				"ON pe.personId = p.managerId " + 
				"WHERE pe.personCode = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "UPDATE " + 
				"Portfolio p " +
				"LEFT JOIN Persons pe " + 
				"ON pe.personId = p.ownerId " + 
				"SET p.beneficiaryId = null " +
				"WHERE pe.personCode = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "DELETE " + 
				"FROM Persons " + 
				"WHERE personCode = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		try {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
	}
	
	public static void addPerson(String personCode, String firstName, String lastName, String street, String city, 
								String state, String zip, String country, String brokerType, String secBrokerId) {
		connectToDatabase();
		
		String query = "INSERT INTO " +
					"Persons (personCode, firstName, lastName) " + 
					"values (?,?,?);";

		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer personId = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "SELECT " +
				"personId " + 
				"FROM Persons " +
				"WHERE personCode = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			rs = ps.executeQuery();
			while(rs.next()) {
				personId = rs.getInt("personId");
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "INSERT INTO " +
				"Address (street, city, state, zipCode, country, personId) " + 
				"values (?, ?, ?, ? ,?, ?);";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, state);
			ps.setString(4, zip);
			ps.setString(5, country);
			ps.setInt(6, personId);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		if(secBrokerId != null) {
			query = "INSERT INTO " +
					"Brokers (brokerSEC, brokerType, personId) " + 
					"values (?, ?, ?);";
			
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, secBrokerId);
				ps.setString(2,  brokerType);
				ps.setInt(3,  personId);
				ps.executeUpdate();
			} catch (SQLException sqle) {
				throw new RuntimeException(sqle);
			}
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
	}
	
	public static void addEmail(String personCode, String email) {
		
		connectToDatabase();
		
		String query = "SELECT " +
				"personId " + 
				"FROM Persons " +
				"WHERE personCode = ?;";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer personId = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			rs = ps.executeQuery();
			while(rs.next()) {
				personId = rs.getInt("personId");
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "INSERT INTO " + 
				"Emails (email, personId) " + 
				"values (?, ?);";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, email);
			ps.setInt(2, personId);
			ps.executeUpdate();
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
	}
	
	public static void removeAllAssets() {
		
		connectToDatabase();
		
		String query = "DELETE FROM PortfolioAsset;";
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "DELETE FROM Assets;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		try {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
	}
	
	public static void removeAsset(String assetCode) {
		
		connectToDatabase();
		
		String query = "SELECT FROM Assets assetId " +
					"WHERE assetCode = ?;";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer assetId = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, assetCode);
			rs = ps.executeQuery();
			while(rs.next()) {
				assetId = rs.getInt("assetId");
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "DELETE FROM PortfolioAsset " + 
				"WHERE assetId = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, assetId);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "DELETE FROM Assets " + 
				"WHERE assetId = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, assetId);
			ps.executeUpdate();
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
	}
	
	public static void addDepositAccount(String assetCode, String label, double apr) {
		
		connectToDatabase();
		
		String query = "INSERT INTO " + 
					"Assets (assetCode, assetType, label, apr) " + 
					"values (?, ?, ?, ?);";
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1,  assetCode);
			ps.setString(2,  "D");
			ps.setString(3, label);
			ps.setDouble(4, apr);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		try {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
	}
	
	public static void addPrivateInvestment(String assetCode, String label, Double quarterlyDividend, 
											Double baseRateOfReturn, Double baseOmega, Double totalValue) {
	
		connectToDatabase();
		
		String query = "INSERT INTO " + 
				"Assets (assetCode, assetType, label, quarterlyDividen, baseROT, baseOmega, totalValue) " + 
				"values (?, ?, ?, ?, ?, ?, ?);";
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1,  assetCode);
			ps.setString(2,  "P");
			ps.setString(3, label);
			ps.setDouble(4, quarterlyDividend);
			ps.setDouble(5, baseRateOfReturn);
			ps.setDouble(6, baseOmega);
			ps.setDouble(7, totalValue);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		try {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
	}
	
	public static void addStock(String assetCode, String label, Double quarterlyDividend, Double baseRateOfReturn, 
								Double beta, String stockSymbol, Double sharePrice) {
		connectToDatabase();
		
		String query = "INSERT INTO " + 
				"Assets (assetCode, assetType,  label, quarterlyDividen, baseROT, betaMeasure, stockSymbol, sharePrice) " + 
				"values (?,?,?,?,?,?,?,?);";
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, assetCode);
			ps.setString(2, "S");
			ps.setString(3, label);
			ps.setDouble(4, quarterlyDividend);
			ps.setDouble(5, baseRateOfReturn);
			ps.setDouble(6, beta);
			ps.setString(7, stockSymbol);
			ps.setDouble(8, sharePrice);
			
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		try {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
	}
	
	public static void removeAllPortfolios() {
		
		connectToDatabase();
		
		String query = "DELETE FROM PortfolioAsset;";
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "DELETE FROM Portfolios;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		try {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
	}
	
	public static void removePortfolio(String portfolioCode) {
		
		connectToDatabase();
		
		String query = "SELECT " + 
					"portfolioId " + 
					"FROM Portfolio " + 
					"WHERE portfolioCode = ?;";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer portfolioId = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, portfolioCode);
			rs = ps.executeQuery();
			while(rs.next()) {
				portfolioId = rs.getInt("portfolioId");
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "DELETE " + 
				"FROM PortfolioAsset " + 
				"WHERE PortfolioId = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, portfolioId);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "DELETE " + 
				"FROM Portfolios " + 
				"WHERE PortfolioId = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, portfolioId);
			ps.executeUpdate();
			
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
	}
	
	public static void addPortfolio(String portfolioCode, String ownerCode, String managerCode, String beneficiaryCode) {
		
		connectToDatabase();
		
		String query = "SELECT " + 
					"personId " +
					"FROM Persons " + 
					"WHERE personCode = ?;";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer ownerId = null;
		Integer managerId = null;
		Integer beneficiaryId = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, ownerCode);
			rs = ps.executeQuery();
			while(rs.next()) {
				ownerId = rs.getInt("personId");
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "SELECT " + 
				"personId " +
				"FROM Persons " + 
				"WHERE personCode = ?;";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, managerCode);
			rs = ps.executeQuery();
			while(rs.next()) {
				managerId = rs.getInt("personId");
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		if(beneficiaryCode == null) {
			beneficiaryId = null;
		} else {
			query = "SELECT " + 
					"personId " +
					"FROM Persons " + 
					"WHERE personCode = ?;";
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, beneficiaryCode);
				rs = ps.executeQuery();
				while(rs.next()) {
					beneficiaryId = rs.getInt("personId");
				}
			} catch (SQLException sqle) {
				throw new RuntimeException(sqle);
			}
		}
		
		query = "INSERT INTO " +
				"Portfolios (portfolioCode,ownerId,managerId,beneficiaryId) " + 
				"value (?,?,?,?);";
		
		if(beneficiaryCode == null) {
			query = "INSERT INTO " +
					"Portfolios (portfolioCode,ownerId,managerId,beneficiaryId) " + 
					"value (?,?,?,null);";
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, portfolioCode);
				ps.setInt(2, ownerId);
				ps.setInt(3, managerId);
				ps.executeUpdate();
			} catch (SQLException sqle) {
				throw new RuntimeException(sqle);
			}
		} else {
			query = "INSERT INTO " +
					"Portfolios (portfolioCode,ownerId,managerId,beneficiaryId) " + 
					"value (?,?,?,?);";
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, portfolioCode);
				ps.setInt(2, ownerId);
				ps.setInt(3, managerId);
				ps.setInt(4, beneficiaryId);
				ps.executeUpdate();
			} catch (SQLException sqle) {
				throw new RuntimeException(sqle);
			}
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
	}
	
	public static void addAsset(String portfolioCode, String assetCode, double value) {
		
		connectToDatabase();
		
		String query = "SELECT " + 
					"portfolioId " + 
					"FROM Portfolios " +
					"WHERE portfolioCode = ?;";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer portfolioId = null;
		Integer assetId = null;
		String assetType = null;
		
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, portfolioCode);
			rs = ps.executeQuery();
			while(rs.next()) {
				portfolioId = rs.getInt("portfolioId");
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		query = "SELECT " + 
				"assetId, assetType " + 
				"FROM Assets " +
				"WHERE assetCode = ?;";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, assetCode);
			rs = ps.executeQuery();
			while(rs.next()) {
				assetId = rs.getInt("assetId");
				assetType = rs.getString("assetType");
			}
		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		
		if(assetType.equals("S")) {
			query = "INSERT INTO " + 
					"PortfolioAsset (portfolioId,assetId,shareCount) " + 
					"values (?,?,?);";
		} else if(assetType.equals("D")) {
			query = "INSERT INTO " + 
					"PortfolioAsset (portfolioId,assetId,totalValue) " + 
					"values (?,?,?);";
		} else {
			query = "INSERT INTO " + 
					"PortfolioAsset (portfolioId,assetId,percentagestake) " + 
					"values (?,?,?);";
		}
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, portfolioId);
			ps.setInt(2, assetId);
			ps.setDouble(3, value);
			ps.executeUpdate();
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
	}
}

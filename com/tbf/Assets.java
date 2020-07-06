/**
 * Assets object library
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

public abstract class Assets <A> {
	
	private String code;
	private String label;
	private String type;
	
	public Assets(String code, String type, String label) {
		this.code = code;
		this.label = label;
		this.type = type;
	}
	
	public String getCode() {
		return this.code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return this.type;
	}


	public void setType(String type) {
		this.type = type;
	}
	
	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}
	
	public abstract double getAnnualReturn();
	public abstract double getRateOfReturn();
	public abstract double getValue();
	
	public static List<Assets> loadAssetsData() {

		List<Assets> A = new ArrayList<>();
		File f = new File("data/Assets.dat");
		Scanner s = null;
		try {
			s = new Scanner(f);
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}

		int AssetsSize = Integer.parseInt(s.nextLine());
		for (int i = 0; i < AssetsSize; i++) {
			String line = s.nextLine();
			if (!line.trim().isEmpty()) {
				DepositAccount D = null;
				Stock S = null;
				PrivateInvestment P = null;
				String tokens[] = line.split(";");
				String code = tokens[0];
				String type = tokens[1];
				String label = tokens[2];
				if (tokens[1].equals("D")) {
					Double apr = Double.parseDouble(tokens[3]);
					D = new DepositAccount(code, type, label, apr);
					A.add(D);
				} else if (tokens[1].equals("S")) {
					Double quartDIV = Double.parseDouble(tokens[3]);
					Double baseROT = Double.parseDouble(tokens[4]);
					Double betaM = Double.parseDouble(tokens[5]);
					String symbols = tokens[6];
					Double sharePrice = Double.parseDouble(tokens[7]);
					S = new Stock(code, type, label, quartDIV, baseROT, betaM, symbols, sharePrice);
					A.add(S);

				} else if (tokens[1].equals("P")) {
					Double quartDIV = Double.parseDouble(tokens[3]);
					Double baseROT = Double.parseDouble(tokens[4]);
					Double baseOM = Double.parseDouble(tokens[5]);
					Double totalValue = Double.parseDouble(tokens[6]);
					P = new PrivateInvestment(code, type, label, quartDIV, baseROT, baseOM, totalValue);
					A.add(P);
				}

			}
		}
		s.close();
		return A;
	}
	
	public static List<Assets> getAssets() {
		List<Assets> Assets = new ArrayList<>();
		
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
					"assetCode, assetType, label, quarterlyDividen, baseROT, betaMeasure, stockSymbol, sharePrice, baseOmega, totalValue, apr " +
					"FROM Assets;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String assetCode = rs.getString("assetCode");
				String assetType = rs.getString("assetType");
				String label = rs.getString("label");
				
				if(assetType.equals("D")) {
					Double apr = rs.getDouble("apr");
					DepositAccount d = new DepositAccount(assetCode, assetType, label, apr);
					Assets.add(d);
				} else if(assetType.contentEquals("S")) {
					Double quarterlyDividen = rs.getDouble("quarterlyDividen");
					Double baseROT = rs.getDouble("baseROT");
					Double betaMeasure = rs.getDouble("betaMeasure");
					String stockSymbol = rs.getString("stockSymbol");
					Double sharePrice = rs.getDouble("sharePrice");
					Stock s = new Stock(assetCode, assetType, label, quarterlyDividen, baseROT, betaMeasure, stockSymbol, sharePrice);
					Assets.add(s);
				} else {
					Double quarterlyDividen = rs.getDouble("quarterlyDividen");
					Double baseROT = rs.getDouble("baseROT");
					Double baseOmega = rs.getDouble("baseOmega");
					Double totalValue = rs.getDouble("totalValue");
					PrivateInvestment p = new PrivateInvestment(assetCode, assetType, label, quarterlyDividen, baseROT, baseOmega, totalValue);
					Assets.add(p);
				}
				
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
		
			return Assets;
	}
}

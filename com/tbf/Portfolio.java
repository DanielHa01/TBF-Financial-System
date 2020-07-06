/**
 * Portfolio object library
 * 
 * @author Pete Tungtweegait, Daniel Ha
 */

package com.tbf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.sql.*;

public class Portfolio {

	private String portfolioCode;
	private String ownerCode;
	private String managerCode;
	private String beneficiaryCode;
	private String assetList;

	public Portfolio(String portfolioCode, String ownerCode, String managerCode, String beneficiaryCode,
			String assetList) {
		this.portfolioCode = portfolioCode;
		this.ownerCode = ownerCode;
		this.managerCode = managerCode;
		this.beneficiaryCode = beneficiaryCode;
		this.assetList = assetList;
	}

	public String getPortfolioCode() {
		return this.portfolioCode;
	}

	public void setPortfolioCode(String portfolioCode) {
		this.portfolioCode = portfolioCode;
	}

	public String getOwnerCode() {
		return this.ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public String getManagerCode() {
		return this.managerCode;
	}

	public void setManagerCode(String managerCode) {
		this.managerCode = managerCode;
	}

	public String getBeneficiaryCode() {
		return this.beneficiaryCode;
	}

	public void setBeneficiaryCode(String beneficiaryCode) {
		this.beneficiaryCode = beneficiaryCode;
	}

	public String getAssetList() {
		return this.assetList;
	}

	public void setAssetList(String assetList) {
		this.assetList = assetList;
	}

	public int getNumAsset() {
		int count = 0;
		String delimit = ",";
		for (int j = 0; j < this.getAssetList().length() - delimit.length(); j++) {
			if (delimit.compareTo(this.getAssetList().substring(j, j + delimit.length())) == 0) {
				count++;
			}
		}
		return count + 1;
	}

	public static Persons codeToPerson(String code, List<Persons> Per) {
		Persons result = null;
		for (Persons p : Per) {
			if (p.getPersonCode().equals(code)) {
				result = p;
			}
		}
		return result;
	}

	public static Integer getFees(Persons p, int numAssets) {

		if (p.getBrokerData().getBrokerType() == null) {
			return 0;
		}

		if (p.getBrokerData().getBrokerType().equals("E")) {
			return 0;
		} else if (p.getBrokerData().getBrokerType().equals("J")) {
			return numAssets * 75;
		} else {
			return 0;
		}
	}

	public static Double getRisk(Assets a, Double totalValue) {
		Double risk = 0.0;
		if (a.getType().equals("D")) {
			risk = 0.0;
		} else if (a.getType().equals("P")) {
			risk = ((PrivateInvestment) a).getOmegaRisk() * (a.getValue() / totalValue);
		} else if (a.getType().equals("S")) {
			risk = ((Stock) a).getBetaM() * (a.getValue() / totalValue);
		}
		return risk;
	}

	public static Double getCommisions(Persons p, Double totalReturn) {

		if (p.getBrokerData().getBrokerType() == null) {
			return 0.0;
		}

		if (p.getBrokerData().getBrokerType().equals("J")) {
			return totalReturn * (1.25 / 100);
		} else if (p.getBrokerData().getBrokerType().equals("E")) {
			return totalReturn * (3.75 / 100);
		} else {
			return 0.0;
		}
	}

	public static List<Portfolio> loadPortfolioData() {
		List<Portfolio> Portfolios = new ArrayList<>();
		File f = new File("data/Portfolios.dat");
		Scanner s = null;
		try {
			s = new Scanner(f);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		int size = Integer.parseInt(s.nextLine());
		for (int i = 0; i < size; i++) {
			String line = s.nextLine();
			Portfolio p = null;
			String tokens[] = line.split(";", -1);
			String portfolioCode = tokens[0];
			String ownerCode = tokens[1];
			String managerCode = tokens[2];
			String beneficiaryCode = tokens[3];
			String assetList = tokens[4];

			p = new Portfolio(portfolioCode, ownerCode, managerCode, beneficiaryCode, assetList);
			Portfolios.add(p);
		}
		s.close();
		return Portfolios;
	}

	public static List<Portfolio> getPortfolio() {
		List<Portfolio> Portfolios = new ArrayList<>();

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

		String query = "SELECT " + "portfolioCode, ownerId, managerId, beneficiaryId " + "FROM Portfolios;";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String portfolioCode = rs.getString("portfolioCode");
				String ownerCode = null;
				String queryOwner = "SELECT " + "p.personCode FROM Persons p "
						+ "JOIN Portfolios po ON p.personId = po.ownerId " + "WHERE ownerId = " + "'"
						+ rs.getString("ownerId") + "'" + ";";
				PreparedStatement psOwner = null;
				ResultSet rsOwner = null;
				try {
					psOwner = conn.prepareStatement(queryOwner);
					rsOwner = psOwner.executeQuery();
					while (rsOwner.next()) {
						ownerCode = rsOwner.getString("personCode");
					}
				} catch (SQLException sqle) {
					throw new RuntimeException(sqle);
				}
				String managerCode = null;
				String queryManager = "SELECT " + "p.personCode FROM Persons p "
						+ "JOIN Portfolios po ON p.personId = po.managerId " + "WHERE managerId = " + "'"
						+ rs.getString("managerId") + "'" + ";";
				PreparedStatement psManager = null;
				ResultSet rsManager = null;
				try {
					psManager = conn.prepareStatement(queryManager);
					rsManager = psManager.executeQuery();
					while (rsManager.next()) {
						managerCode = rsManager.getString("personCode");
					}
				} catch (SQLException sqle) {
					throw new RuntimeException(sqle);
				}
				String beneficiaryCode = null;
				String queryBeneficiary = "SELECT " + "p.personCode FROM Persons p "
						+ "JOIN Portfolios po ON p.personId = po.beneficiaryId " + "WHERE beneficiaryId = " + "'"
						+ rs.getString("beneficiaryId") + "'" + ";";
				PreparedStatement psBeneficiary = null;
				ResultSet rsBeneficiary = null;
				try {
					psBeneficiary = conn.prepareStatement(queryBeneficiary);
					rsBeneficiary = psBeneficiary.executeQuery();
					while (rsBeneficiary.next()) {
						beneficiaryCode = rsBeneficiary.getString("personCode");
					}
				} catch (SQLException sqle) {
					throw new RuntimeException(sqle);
				}
				String query2 = "SELECT "
						+ "pa.sharecount, pa.percentageStake, pa.totalValue, a.assetCode, a.assetType "
						+ "FROM PortfolioAsset pa " + "LEFT JOIN Assets a on pa.assetId = a.assetId "
						+ "LEFT JOIN Portfolios p on p.portfolioId = pa.portfolioId " + "WHERE portfolioCode = " + "'"
						+ portfolioCode + "'" + ";";
				StringBuilder sb = new StringBuilder();
				PreparedStatement ps1 = null;
				ResultSet rs1 = null;
				try {
					ps1 = conn.prepareStatement(query2);
					rs1 = ps1.executeQuery();
					while (rs1.next()) {
						String assetType = rs1.getString("assetType");
						if (assetType.equals("S")) {
							Double stockCount = rs1.getDouble("shareCount");
							String assetCode = rs1.getString("assetCode");
							sb.append(String.format("%s:%f,", assetCode, stockCount));
						} else if (assetType.equals("D")) {
							Double totalValue = rs1.getDouble("totalValue");
							String assetCode = rs1.getString("assetCode");
							sb.append(String.format("%s:%f,", assetCode, totalValue));
						} else {
							Double percentageStake = rs1.getDouble("percentageStake");
							String assetCode = rs1.getString("assetCode");
							sb.append(String.format("%s:%f,", assetCode, percentageStake));
						}
					}
				} catch (SQLException sqle) {
					throw new RuntimeException(sqle);
				}
				try {
					if (rs1 != null && !rs1.isClosed()) {
						rs1.close();
					}
					if (ps1 != null && !ps1.isClosed()) {
						ps1.close();
					}
				} catch (SQLException sqle) {
					throw new RuntimeException(sqle);
				}
				String assetList = sb.toString();
				Portfolio p = new Portfolio(portfolioCode, ownerCode, managerCode, beneficiaryCode, assetList);
				Portfolios.add(p);
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

		return Portfolios;
	}

	public static Double getTotalValue(Portfolio p) {
		List<Assets> As = Assets.getAssets();

		Double totalValue = 0.0;
		int numAssets = p.getNumAsset();

		if (p.getAssetList().equals("")) {
			totalValue = 0.0;
		} else {
			String assets[] = p.getAssetList().split(",");
			for (int i = 0; i < numAssets; i++) {
				String asset[] = assets[i].split(":");
				String code = asset[0];
				Double value = Double.parseDouble(asset[1]);
				for (Assets a : As) {
					if (code.equals(a.getCode())) {
						if (a.getType().equals("D")) {
							((DepositAccount) a).setTotalValue(value);
						} else if (a.getType().equals("S")) {
							((Stock) a).setShareCount(value);
						} else if (a.getType().equals("P")) {
							((PrivateInvestment) a).setPercentageStake(value);
						}
						totalValue += a.getValue();
					}
				}
			}
		}
		return totalValue;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		List<Persons> per = Persons.getPersons();
		sb.append("Owner: \n" + Portfolio.codeToPerson(ownerCode, per).getPerson().getLastName() + ", "
				+ Portfolio.codeToPerson(ownerCode, per).getPerson().getFirstName() + '\n');
		sb.append("Manager: \n" + Portfolio.codeToPerson(managerCode, per).getPerson().getLastName() + ", "
				+ Portfolio.codeToPerson(managerCode, per).getPerson().getFirstName() + '\n');

		sb.append(String.format("Total value: %.2f\n", Portfolio.getTotalValue(this)));

		return sb.toString();
	}

	public static void printSummary(PortfolioList<Portfolio> portfolio) {

		StringBuilder sb = new StringBuilder();

		List<Persons> Per = Persons.getPersons();
		List<Assets> As = Assets.getAssets();

		sb.append(String.format("%-11s %-23s %-26s %-10s %-15s %-22s %-16s %-6s\n", "Portfolio", "Owner", "Manager",
				"Fees", "Commisions", "Weighted Risk", "Return", "Total"));

		System.out.println("Portfolio Summary Report");
		System.out.println(
				"===========================================================================================================================================");
		double totalFees = 0;
		double totalComission = 0;
		double totalR = 0;
		double total = 0;

		for (int i = 0; i < portfolio.getSize(); i++) {
			Portfolio p = portfolio.getPortfolioListNode(i).getItem();
			Persons owner = Portfolio.codeToPerson(p.getOwnerCode(), Per);
			Persons manager = Portfolio.codeToPerson(p.getManagerCode(), Per);
			int numAssets = p.getNumAsset();

			Double totalValue = 0.0;
			Double totalReturn = 0.0;
			Double totalRisk = 0.0;

			if (p.getAssetList().equals("")) {
				totalValue = 0.0;
				totalReturn = 0.0;
				totalRisk = 0.0;
			} else {
				String assets[] = p.getAssetList().split(",");
				for (int j = 0; j < numAssets; j++) {
					String asset[] = assets[j].split(":");
					String code = asset[0];
					Double value = Double.parseDouble(asset[1]);
					for (Assets a : As) {
						if (code.equals(a.getCode())) {
							if (a.getType().equals("D")) {
								((DepositAccount) a).setTotalValue(value);
							} else if (a.getType().equals("S")) {
								((Stock) a).setShareCount(value);
							} else if (a.getType().equals("P")) {
								((PrivateInvestment) a).setPercentageStake(value);
							}
							totalValue += a.getValue();

							totalReturn += a.getAnnualReturn();
						}
					}
				}
				for (int j = 0; j < numAssets; j++) {
					String asset[] = assets[j].split(":");
					String code = asset[0];
					for (Assets a : As) {
						if (code.equals(a.getCode())) {
							totalRisk += Portfolio.getRisk(a, totalValue);
						}
					}
				}
			}

			sb.append(String.format("%-11.5s %-23s %-26s %-10d %-15.2f %-22.4f %-16.2f %-6.2f\n", p.getPortfolioCode(),
					owner.getPerson().getLastName() + "," + owner.getPerson().getFirstName(),
					manager.getPerson().getLastName() + "," + manager.getPerson().getFirstName(),
					Portfolio.getFees(manager, numAssets), Portfolio.getCommisions(manager, totalReturn), totalRisk,
					totalReturn, totalValue));
			totalFees += Portfolio.getFees(manager, numAssets);
			totalComission += Portfolio.getCommisions(manager, totalReturn);
			totalR += totalReturn;
			total += totalValue;
		}

		System.out.println(sb);
		System.out.println(
				"							------------------------------------------------------------------------------------");
		System.out.printf("%50s %20.4f %9.2f %40.2f %17.2f\n", "Totals", totalFees, totalComission, totalR, total);
	}

}

class SortByOwner implements Comparator<Portfolio> {
	public int compare(Portfolio a, Portfolio b) {
		List<Persons> Per = Persons.getPersons();
		Persons x = Portfolio.codeToPerson(a.getOwnerCode(), Per);
		Persons y = Portfolio.codeToPerson(b.getOwnerCode(), Per);
		if (x.getPerson().getLastName().compareTo(y.getPerson().getLastName()) != 0) {
			return x.getPerson().getLastName().compareTo(y.getPerson().getLastName());
		} else {
			return x.getPerson().getFirstName().compareTo(y.getPerson().getFirstName());
		}
	}
}

class SortByValue implements Comparator<Portfolio> {

	public int compare(Portfolio a, Portfolio b) {
		if (Portfolio.getTotalValue(a) - Portfolio.getTotalValue(b) < 0) {
			return 1;
		} else if (Portfolio.getTotalValue(a) - Portfolio.getTotalValue(b) > 0) {
			return -1;
		} else {
			return 0;
		}
	}
}

class SortByManager implements Comparator<Portfolio> {

	public int compare(Portfolio a, Portfolio b) {

		List<Persons> Per = Persons.getPersons();
		Persons x = Portfolio.codeToPerson(a.getManagerCode(), Per);
		Persons y = Portfolio.codeToPerson(b.getManagerCode(), Per);

		if (x.getBrokerData().getBrokerType() == null) {
			return 1;
		}

		if (y.getBrokerData().getBrokerType() == null) {
			return -1;
		}

		String brokerTypeX = x.getBrokerData().getBrokerType();
		String brokerTypeY = y.getBrokerData().getBrokerType();

		if (brokerTypeX.compareTo(brokerTypeY) != 0) {
			return brokerTypeX.compareTo(brokerTypeY);
		} else {
			if (x.getPerson().getLastName().compareTo(y.getPerson().getLastName()) != 0) {
				return x.getPerson().getLastName().compareTo(y.getPerson().getLastName());
			} else {
				return x.getPerson().getFirstName().compareTo(y.getPerson().getFirstName());
			}
		}
	}
}

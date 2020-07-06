/**
 * This program load up Portfolio.dat, Assest.dat, Persons.dat and
 * produce a report of each individual assets along with their information
 * 
 * @author Pete Tungtweegait Daniel Ha
 */
package com.tbf;

import java.util.*;

public class PortfolioReport {
	public static void main(String args[]) {

		List<Portfolio> Por = Portfolio.getPortfolio();
		List<Persons> Per = Persons.getPersons();
		List<Assets> As = Assets.getAssets();
		StringBuilder sb = new StringBuilder();
		PortfolioList<Portfolio> Owner = new PortfolioList<Portfolio>(new SortByOwner());
		PortfolioList<Portfolio> Manager = new PortfolioList<Portfolio>(new SortByManager());
		PortfolioList<Portfolio> Value = new PortfolioList<Portfolio>(new SortByValue());

		for (Portfolio po : Por) {
			Owner.add(po);
			Manager.add(po);
			Value.add(po);
		}
		
		Portfolio.printSummary(Owner);
		Portfolio.printSummary(Value);
		Portfolio.printSummary(Manager);


		StringBuilder sbd = new StringBuilder();
		System.out.println("Portfolio Details");
		System.out.println(
				"===========================================================================================================================================");

		for (Portfolio p : Por) {
			System.out.printf("Portfolio %s\n", p.getPortfolioCode());
			System.out.println("------------------------------------");
			System.out.println("Owner:");
			System.out.printf("%s,%s\n", Portfolio.codeToPerson(p.getOwnerCode(), Per).getPerson().getLastName(),
					Portfolio.codeToPerson(p.getOwnerCode(), Per).getPerson().getFirstName());
			if (Portfolio.codeToPerson(p.getOwnerCode(), Per).getBrokerData().getBrokerType() == null) {
				System.out.println("");
			} else if (Portfolio.codeToPerson(p.getOwnerCode(), Per).getBrokerData().getBrokerType().equals("E")) {
				System.out.println("Expert Broker");
			} else if (Portfolio.codeToPerson(p.getOwnerCode(), Per).getBrokerData().getBrokerType().equals("J")) {
				System.out.println("Junior Broker");
			}
			System.out.printf("[%s]\n", Portfolio.codeToPerson(p.getOwnerCode(), Per).getEmail());
			System.out.printf("%s\n", Portfolio.codeToPerson(p.getOwnerCode(), Per).getAddress().getStreet());
			System.out.printf("%s, %s %s %s\n", Portfolio.codeToPerson(p.getOwnerCode(), Per).getAddress().getCity(),
					Portfolio.codeToPerson(p.getOwnerCode(), Per).getAddress().getState(),
					Portfolio.codeToPerson(p.getOwnerCode(), Per).getAddress().getCountry(),
					Portfolio.codeToPerson(p.getOwnerCode(), Per).getAddress().getZip());
			System.out.println("Manager:");
			System.out.printf("%s,%s\n", Portfolio.codeToPerson(p.getManagerCode(), Per).getPerson().getLastName(),
					Portfolio.codeToPerson(p.getManagerCode(), Per).getPerson().getFirstName());
			System.out.println("Beneficiary:");
			if (p.getBeneficiaryCode() == null) {
				System.out.println("None");
			} else {
				System.out.printf("%s,%s\n", Portfolio.codeToPerson(p.getBeneficiaryCode(), Per).getPerson().getLastName(),
						Portfolio.codeToPerson(p.getBeneficiaryCode(), Per).getPerson().getFirstName());
				if (Portfolio.codeToPerson(p.getOwnerCode(), Per).getBrokerData().getBrokerType() == null) {
					System.out.println("");
				} else if (Portfolio.codeToPerson(p.getOwnerCode(), Per).getBrokerData().getBrokerType().equals("E")) {
					System.out.println("Expert Broker");
				} else if (Portfolio.codeToPerson(p.getOwnerCode(), Per).getBrokerData().getBrokerType().equals("J")) {
					System.out.println("Junior Broker");
				}
				System.out.printf("[%s]\n", Portfolio.codeToPerson(p.getBeneficiaryCode(), Per).getEmail());
				System.out.printf("%s\n", Portfolio.codeToPerson(p.getBeneficiaryCode(), Per).getAddress().getStreet());
				System.out.printf("%s, %s %s %s\n", Portfolio.codeToPerson(p.getBeneficiaryCode(), Per).getAddress().getCity(),
						Portfolio.codeToPerson(p.getBeneficiaryCode(), Per).getAddress().getState(),
						Portfolio.codeToPerson(p.getBeneficiaryCode(), Per).getAddress().getCountry(),
						Portfolio.codeToPerson(p.getBeneficiaryCode(), Per).getAddress().getZip());
				System.out.println("Manager:");
				System.out.printf("%s,%s\n", Portfolio.codeToPerson(p.getBeneficiaryCode(), Per).getPerson().getLastName(),
						Portfolio.codeToPerson(p.getBeneficiaryCode(), Per).getPerson().getFirstName());
			}
			System.out.println("Assets:");
			sbd.append(String.format("%-12s %-32s %-21s %-6s %-24s %-6s\n", "Code", "Asset", "Return Rate", "Risk",
					"Annual Return", "Value"));
			Double totalValue = 0.0;
			Double totalReturn = 0.0;
			Double totalRisk = 0.0;
			String assets[] = p.getAssetList().split(",");
			String code = null;
			if (p.getAssetList().equals("")) {

			} else {
				for (int i = 0; i < p.getNumAsset(); i++) {
					String asset[] = assets[i].split(":");
					code = asset[0];
					Double value = Double.parseDouble(asset[1]);
					for (Assets a : As) {
						if (code.equals(a.getCode())) {
							if (code.equals(a.getCode())) {
								if (a.getType().equals("D")) {
									((DepositAccount) a).setTotalValue(value);
								} else if (a.getType().equals("S")) {
									((Stock) a).setShareCount(value);
								} else if (a.getType().equals("P")) {
									((PrivateInvestment) a).setPercentageStake(value);
								}
							}
							totalValue += a.getValue();

							totalReturn += a.getAnnualReturn();
						}
					}
				}
			}

			for (int i = 0; i < p.getNumAsset(); i++) {
				String asset[] = assets[i].split(":");
				code = asset[0];
				for (Assets b : As) {
					if (code.equals(b.getCode())) {
						totalRisk += Portfolio.getRisk(b, totalValue);
						sbd.append(String.format("%-12s %-32s %-21.2f %-6.4f %-24.2f %-6.2f\n", code, b.getLabel(),
								b.getRateOfReturn() * 100, Portfolio.getRisk(b, totalValue), b.getAnnualReturn(), b.getValue()));
					}
				}
			}
			System.out.println(sbd);
			sbd.delete(0, sbd.length());
			System.out.println("							-----------------------------------------------------");
			System.out.printf("%62s %11.4f %8.2f %25.2f\n", "Totals", totalRisk, totalReturn, totalValue);
		}
	}
}

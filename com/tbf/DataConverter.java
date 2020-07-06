/**
 * This program load data from Persons.dat and Assets.dat and 
 * produce Persons.xml and Assets.xml
 * 
 * @author Daniel Ha, Pete Tungtweegait
 */
package com.tbf;

import java.io.*;
import java.util.*;

public class DataConverter {

	public static void main(String args[]) {
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

		PrintWriter out;
		try {
			out = new PrintWriter("data/Persons.xml");
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		out.println("<?xml version=\"1.0\"?>");
		out.println("<persons>");
		for (Persons p : Persons) {
			if (p.getBrokerData().getBrokerType().equals("E")) {
				out.println("  <com.tbf.person.ExpertBroker>");
				out.printf("    <personCode>%s</personCode>\n", p.getPersonCode());
				out.printf("    <firstName>%s</firstName>\n", p.getPerson().getFirstName());
				out.printf("    <lastName>%s</lastName>\n", p.getPerson().getLastName());
				out.println("    <address>");
				out.printf("      <street>%s</street>\n", p.getAddress().getStreet());
				out.printf("      <city>%s</city>\n", p.getAddress().getCity());
				out.printf("      <state>%s</state>\n", p.getAddress().getState());
				out.printf("      <zipCode>%s</zipCode>\n", p.getAddress().getZip());
				out.printf("      <country>%s</country>\n", p.getAddress().getCountry());
				out.println("    </address>");
				out.println("    <emails>");
				// count the number of email (the number of email = the number of commas + 1)
				String delimit = ",";
				int count = 0;
				for (int j = 0; j < p.getEmail().length() - delimit.length(); j++) {
					if (delimit.compareTo(p.getEmail().substring(j, j + delimit.length())) == 0) {
						count++;
					}
				}
				if (count == 0) {
					out.println("      <com.tbf.person.Email>");
					out.printf("        <address>%s</address>\n", p.getEmail());
					out.println("      </com.tbf.person.Email>");
				} else {
					int emailNum = count + 1;
					String email[] = p.getEmail().split(",");
					for (int j = 0; j < emailNum; j++) {
						out.println("      <com.tbf.person.Email>");
						out.printf("        <address>%s</address>\n", email[j]);
						out.println("      </com.tbf.person.Email>");
					}
				}
				out.println("    </emails>");
				out.printf("    <secCode>%s</secCode>\n", p.getBrokerData().getSEC());
				out.println("  </com.tbf.person.ExpertBroker>");
			} else if (p.getBrokerData().getBrokerType().equals("J")) {
				out.println("  <com.tbf.person.JuniorBroker>");
				out.printf("    <personCode>%s</personCode>\n", p.getPersonCode());
				out.printf("    <firstName>%s</firstName>\n", p.getPerson().getFirstName());
				out.printf("    <lastName>%s</lastName>\n", p.getPerson().getLastName());
				out.println("    <address>");
				out.printf("      <street>%s</street>\n", p.getAddress().getStreet());
				out.printf("      <city>%s</city>\n", p.getAddress().getCity());
				out.printf("      <state>%s</state>\n", p.getAddress().getState());
				out.printf("      <zipCode>%s</zipCode>\n", p.getAddress().getZip());
				out.printf("      <country>%s</country>\n", p.getAddress().getCountry());
				out.println("    </address>");
				out.println("    <emails>");
				// count the number of email (the number of email = the number of commas + 1)
				String delimit = ",";
				int count = 0;
				for (int j = 0; j < p.getEmail().length() - delimit.length(); j++) {
					if (delimit.compareTo(p.getEmail().substring(j, j + delimit.length())) == 0) {
						count++;
					}
				}
				if (count == 0) {
					out.println("      <com.tbf.person.Email>");
					out.printf("        <address>%s</address>\n", p.getEmail());
					out.println("      </com.tbf.person.Email>");
				} else {
					int emailNum = count + 1;
					String email[] = p.getEmail().split(",");
					for (int j = 0; j < emailNum; j++) {
						out.println("      <com.tbf.person.Email>");
						out.printf("        <address>%s</address>\n", email[j]);
						out.println("      </com.tbf.person.Email>");
					}
				}
				out.println("    </emails>");
				out.printf("    <secCode>%s</secCode>\n", p.getBrokerData().getSEC());
				out.println("  </com.tbf.person.JuniorBroker>");
			} else {
				out.println("  <person>");
				out.printf("    <personCode>%s</personCode>\n", p.getPersonCode());
				out.printf("    <firstName>%s</firstName>\n", p.getPerson().getFirstName());
				out.printf("    <lastName>%s</lastName>\n", p.getPerson().getLastName());
				out.println("    <address>");
				out.printf("      <street>%s</street>\n", p.getAddress().getStreet());
				out.printf("      <city>%s</city>\n", p.getAddress().getCity());
				out.printf("      <state>%s</state>\n", p.getAddress().getState());
				out.printf("      <zipCode>%s</zipCode>\n", p.getAddress().getZip());
				out.printf("      <country>%s</country>\n", p.getAddress().getCountry());
				out.println("    </address>");
				out.println("    <emails>");
				// count the number of email (the number of email = the number of commas + 1)
				String delimit = ",";
				int count = 0;
				for (int j = 0; j < p.getEmail().length() - delimit.length(); j++) {
					if (delimit.compareTo(p.getEmail().substring(j, j + delimit.length())) == 0) {
						count++;
					}
				}
				if (count == 0) {
					out.println("      <com.tbf.person.Email>");
					out.printf("        <address>%s</address>\n", p.getEmail());
					out.println("      </com.tbf.person.Email>");
				} else {
					int emailNum = count + 1;
					String email[] = p.getEmail().split(",");
					for (int j = 0; j < emailNum; j++) {
						out.println("      <com.tbf.person.Email>");
						out.printf("        <address>%s</address>\n", email[j]);
						out.println("      </com.tbf.person.Email>");
					}
				}
				out.println("    </emails>");
				out.println("  </person>");
			}
		}
		out.println("</persons>");
		out.close();

		List<Assets<? extends Assets>> A = new ArrayList<>();
		List<DepositAccount> DA = new ArrayList<>();
		List<Stock> ST = new ArrayList<>();
		List<PrivateInvestment> PI = new ArrayList<>();
		f = new File("data/Assets.dat");
		try {
			s = new Scanner(f);
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		
		int AssetsSize = Integer.parseInt(s.nextLine());
		for(int i = 0; i < AssetsSize; i++) {
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

		try {
			out = new PrintWriter("data/Assets.xml");
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		out.printf("<?xml version=\"1.0\"?>\n");
		out.printf("<assets>\n");

		for (DepositAccount da : DA) {
			out.printf("<com.tbf.assets.DepositAccount>\n");
			out.printf(" <assetCode>%s</assetCode>\n", da.getCode());
			out.printf(" <label>%s</label>\n", da.getLabel());
			out.printf(" <apr>%f</apr>\n", da.getApr());
			out.printf("</com.tbf.assets.DepositAccount>\n");
		}

		for (Stock st : ST) {
			out.printf("<com.tbf.assets.Stock>\n");
			out.printf(" <assetCode>%s</assetCode>\n", st.getCode());
			out.printf(" <label>%s</label>\n", st.getLabel());
			out.printf(" <baseQuarterlyDividend>%f</baseQuarterlyDividend>\n", st.getQuartDiv());
			out.printf(" <baseRateOfReturn>%f</baseRateOfReturn>\n", st.getBaseROT());
			out.printf(" <stockSymbol>%s</stockSymbol>\n", st.getSymbol());
			out.printf(" <sharePrice>%f</sharePrice>\n", st.getSharePrice());
			out.printf(" <beta>%f</beta>\n", st.getBetaM());
			out.printf("<com.tbf.assets.Stock>\n");
		}

		for (PrivateInvestment pi : PI) {
			out.printf("<com.tbf.assets.PrivateInvestment>\n");
			out.printf(" <assetCode>%s</assetCode>\n", pi.getCode());
			out.printf(" <label>%s</label>\n", pi.getLabel());
			out.printf(" <baseQuarterlyDividend>%f</baseQuarterlyDividend>\n", pi.getQuartDiv());
			out.printf(" <baseRateOfReturn>%f</baseRateOfReturn>\n", pi.getBaseOM());
			out.printf(" <totalAmount>%f</totalAmount>\n", pi.getTotalValue());
			out.printf("<omega>%f</omega>\n", pi.getBaseOM());
			out.printf("</com.tbf.assets.PrivateInvestment>\n");

		}
		out.printf("</assets>\n");
		out.close();
	}
}

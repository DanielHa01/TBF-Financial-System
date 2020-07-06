package com.tbf;

import java.util.Comparator;
import java.util.List;

public class PortfolioListTester {
	public static void main(String[] args) {

		PortfolioList<Portfolio> owner = new PortfolioList<Portfolio>(new SortByOwner());
		PortfolioList<Portfolio> manager = new PortfolioList<Portfolio>(new SortByManager());
		PortfolioList<Portfolio> value = new PortfolioList<Portfolio>(new SortByValue());

		List<Portfolio> P = Portfolio.getPortfolio();

		for (Portfolio po : P) {
			owner.add(po);
			manager.add(po);
			value.add(po);
		}

		System.out.println("Sort by owner:");
		owner.print();
		System.out.println("Sort by manager:");
		manager.print();
		System.out.println("Sort by value:");
		value.print();
		
	}
}

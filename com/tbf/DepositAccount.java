/**
 * Library for Assets type: Deposit Account 
 * 
 * @author Pete Tungtweegait
 */
package com.tbf;

public class DepositAccount extends Assets<DepositAccount> {

	private Double apr;
	private Double totalValue;
	
	public DepositAccount(String code, String type, String label, Double apr) {
		super(code, type, label);
		this.apr = apr;
	}
	
	public Double getApr() {
		return this.apr;
	}
	
	public void setApr(Double apr) {
		this.apr = apr;
	}
	
	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}
	
	public double getAnnualReturn() {
		return this.totalValue *this.getRateOfReturn();
	}
	
	public double getRateOfReturn() {
		return (Math.exp(apr) - 1.0);
	}
	
	public double getValue() {
		return this.totalValue;
	}
}

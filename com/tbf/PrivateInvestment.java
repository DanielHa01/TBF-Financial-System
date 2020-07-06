/**
 * Library for Assets type: Private Investment
 * 
 * @author Pete Tungtweegait Daniel Ha
 */
package com.tbf;

public class PrivateInvestment extends Assets<PrivateInvestment> {

	private Double quartDiv;
	private Double baseROT;
	private Double baseOM;
	private Double totalValue;
	private Double percentageStake;

	public PrivateInvestment(String code, String type, String label, Double quartDiv, Double baseROT, Double baseOM,
			Double totalValue) {
		super(code, type, label);
		this.quartDiv = quartDiv;
		this.baseROT = baseROT;
		this.baseOM = baseOM;
		this.totalValue = totalValue;
	}

	public Double getQuartDiv() {
		return quartDiv;
	}

	public void setQuartDiv(Double quartDiv) {
		this.quartDiv = quartDiv;
	}

	public Double getBaseROT() {
		return baseROT;
	}

	public void setBaseROT(Double baseROT) {
		this.baseROT = baseROT;
	}

	public Double getBaseOM() {
		return baseOM;
	}

	public void setBaseOM(Double baseOM) {
		this.baseOM = baseOM;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}
	
	public void setPercentageStake(Double percentageStake) {
		this.percentageStake = percentageStake;
	}
	
	public double getOmegaRisk() {
		return baseOM + Math.exp(-125500.0/totalValue);
	}
		
	public double getAnnualReturn() {
		return (((baseROT) * totalValue) + (4 * quartDiv))*percentageStake;
	}
	
	public double getRateOfReturn() {
		return (this.getAnnualReturn()/totalValue);
	}
	
	public double getValue() {
		return this.getTotalValue() * percentageStake;
	}
}

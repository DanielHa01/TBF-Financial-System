/**
 * Library for Assets type: Stock
 * 
 * @author Pete Tungtweegait Daniel Ha
 */
package com.tbf;

public class Stock extends Assets<Stock> {

	private Double quartDiv;
	private Double baseROT;
	private Double betaM;
	private String symbol; 
	private Double sharePrice;
	private Double shareCount;
	
	
	public Stock(String code, String type, String label, Double quartDiv, Double baseROT, Double betaM, String symbol, Double sharePrice) {
		super(code, type,  label);
		this.quartDiv = quartDiv;
		this.baseROT = baseROT;
		this.betaM = betaM;
		this.symbol = symbol;
		this.sharePrice = sharePrice;
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


	public Double getBetaM() {
		return betaM;
	}


	public void setBetaM(Double betaM) {
		this.betaM = betaM;
	}


	public String getSymbol() {
		return symbol;
	}


	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	public Double getSharePrice() {
		return sharePrice;
	}


	public void setSharePrice(Double sharePrice) {
		this.sharePrice = sharePrice;
	}
	
	public void setShareCount(Double shareCount) {
		this.shareCount = shareCount;
	}
	
	public double getAnnualReturn() {
		return ((baseROT)*sharePrice*shareCount)+(4*quartDiv*shareCount);
	}
	
	public double getRateOfReturn() {
		return (this.getAnnualReturn() / (sharePrice*shareCount));
	}
	
	public double getValue() {
		return shareCount * sharePrice;
	}
	
	public Double getShareCount() {
		return this.shareCount;
	}
}

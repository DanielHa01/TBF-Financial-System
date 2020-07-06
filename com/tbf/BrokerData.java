/**
 * BrokerData object library
 * 
 * @author Pete Tungtweegait, Daniel Ha
 */
package com.tbf;

public class BrokerData {
	public BrokerData(String brokerType, String SEC) {
		this.brokerType = brokerType;
		this.SEC = SEC;
	}
	
	final private String brokerType;
	final private String SEC;
	
	public String getBrokerType() {
		return this.brokerType;
	}

	public String getSEC() {
		return this.SEC;
	}

}

package org.ait.producer.consumer.model;

import java.sql.Date;

public class Customer {
	private int customerId;
	private String customerName;
	private Date dateOfActivation;
	private String addressProofId;
	private String addressProofType;
	private String emailId;
	private MobileService mobileService;

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getDateOfActivation() {
		return dateOfActivation;
	}

	public void setDateOfActivation(Date dateOfActivation) {
		this.dateOfActivation = dateOfActivation;
	}

	public String getAddressProofId() {
		return addressProofId;
	}

	public void setAddressProofId(String addressProofId) {
		this.addressProofId = addressProofId;
	}

	public String getAddressProofType() {
		return addressProofType;
	}

	public void setAddressProofType(String addressProofType) {
		this.addressProofType = addressProofType;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public MobileService getMobileService() {
		return mobileService;
	}

	public void setMobileService(MobileService mobileService) {
		this.mobileService = mobileService;
	}
}

package org.ait.producer.consumer.model;

import java.sql.Date;

public class CDRDetail {
	private String fromMobileNo;
	private String toMobileNo;
	private String areaCode;
	private String callType;
	private Date starTime;
	private Date endTime;
	private float cost;
	private long duration;

	public String getFromMobileNo() {
		return fromMobileNo;
	}

	public void setFromMobileNo(String fromMobileNo) {
		this.fromMobileNo = fromMobileNo;
	}

	public String getToMobileNo() {
		return toMobileNo;
	}

	public void setToMobileNo(String toMobileNo) {
		this.toMobileNo = toMobileNo;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public Date getStarTime() {
		return starTime;
	}

	public void setStarTime(Date starTime) {
		this.starTime = starTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}

package com.solitardj9.microiot.filter.routeFilterManager.model;

public class Token {
	//
	private String serviceId;
	
	private String sendQueueEndpoint;
	
	private String recvQueueEndpoint;
	
	public Token() {
	}

	public Token(String serviceId, String sendQueueEndpoint, String recvQueueEndpoint) {
		this.serviceId = serviceId;
		this.sendQueueEndpoint = sendQueueEndpoint;
		this.recvQueueEndpoint = recvQueueEndpoint;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getSendQueueEndpoint() {
		return sendQueueEndpoint;
	}

	public void setSendQueueEndpoint(String sendQueueEndpoint) {
		this.sendQueueEndpoint = sendQueueEndpoint;
	}

	public String getRecvQueueEndpoint() {
		return recvQueueEndpoint;
	}

	public void setRecvQueueEndpoint(String recvQueueEndpoint) {
		this.recvQueueEndpoint = recvQueueEndpoint;
	}

	@Override
	public String toString() {
		return "Token [serviceId=" + serviceId + ", sendQueueEndpoint=" + sendQueueEndpoint + ", recvQueueEndpoint="
				+ recvQueueEndpoint + "]";
	}
}
package com.gionji.gionjihome.device;

public class GHDevice {
	
	private String id;
	private String name;
	private String SSID;
	private String ip;
	
	
	public GHDevice(String id, String name, String sSID, String ip) {
		super();
		this.id = id;
		this.name = name;
		SSID = sSID;
		this.ip = ip;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSSID() {
		return SSID;
	}


	public void setSSID(String sSID) {
		SSID = sSID;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
	static public String getIpAddress(String msg){
		return msg.split("@")[1];
	}
	
	static public String getId(String msg){
		return msg.split("#")[1].split("@")[0];
	}	
	
		
}

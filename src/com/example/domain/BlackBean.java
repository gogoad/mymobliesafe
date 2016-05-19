package com.example.domain;

public class BlackBean {
	private String phone;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	private int mode;
	@Override
	public String toString() {
		return "BlackBean [phone=" + phone + ", mode=" + mode + "]";
	}
	
}

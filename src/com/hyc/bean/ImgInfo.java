package com.hyc.bean;

import java.io.File;

public class ImgInfo {
	int type;
	long cardno;
	String path;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getCardno() {
		return cardno;
	}
	public void setCardno(long cardno) {
		this.cardno = cardno;
	}
	public String getFile() {
		return path;
	}
	public void setFile(String path) {
		this.path = path;
	}
}

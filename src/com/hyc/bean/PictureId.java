package com.hyc.bean;

public class PictureId {
	// 图片下载路径
	String src;
	// 图片添加时间
	String time;
	// 图片播放开始时间
	String stime;
	// 图片播放结束时间
	String etime;
	//文件名字
	String srcname;

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	public void setSrcname(String srcname) {
		this.srcname = srcname;
	}
	public String getSrcname() {
		return srcname;
	}
}

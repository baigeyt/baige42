package com.hyc.bean;

import com.hyc.network.GetDeviceID;

public class InterWeb {
	public String MAC = getMac();
	// public String MAC=Login.accesstoken;
	// private String web = "http://api.360baige.com";
	private String web1 = "http://api.figool.cn";
	// private String web = "http://api.figool.com";
	private String web = "http://api.360baige.cn";

	private String getMac() {
		GetDeviceID get = new GetDeviceID();
		return get.getMacAddress();

	}

	// ѧУ��Ϣ2
	private String URL_SchoolInfoMain = web + "/company/info?accesstoken="
			+ MAC;
	// ��ȡAPK��Ϣ
	private String URL_APKVersion = web + "/apk/listofapk?accesstoken=" + MAC
			+ "&type=1";
	// ��ʱ������
	private String URL_ICCardTime = web + "/iccardtime/info?accesstoken=" + MAC;
	// ���
	private String URL_RecAdvert = web + "/advertise/list?accesstoken=" + MAC;
	// ������
	private String URL_IC = web + "/iccard/list?accesstoken=" + MAC;
	// ��������
	private String URL_ONEIC = web + "/iccard/one?accesstoken=" + MAC
			+ "&physicsno=";
	// ͨ��
	private String URL_NOTICE = web + "/index/notice?accesstoken=" + MAC;
	// ѧУ��Ϣ
	private String URL_SchoolInfo = web + "/company/info?accesstoken=" + MAC;
	// չʾͼƬ
	private String URL_SchoolPicInfo = web + "/campus/list?accesstoken=" + MAC
			+ "&area=";
	// ������Ϣ
	private String URL_Weather = web + "/index/weather?accesstoken=" + MAC
			+ "&city=";
	// �ϴ���Ƭ
	private String URL_UploadFile = web + "/resource/upload?accesstoken=" + MAC
			+ "&type=2";
	// �ϴ���¼
	private String URL_UploadRecord = web + "/icrecord/add?accesstoken=" + MAC;
	// �ϴ���¼
	private String URL_ReUploadRecord = web + "icrecord/update?accesstoken="
			+ MAC;
	// ��ȡ��Դ
	private String URL_RecResource = web + "/resource/get?accesstoken=" + MAC
			+ "&resourceid=";
	// ��ȡ�ֲ�ͼƬ����Ƶ��Դ
	private String URL_RecResourceIMG = web + "/resource/";

	public String getURL_APKVersion() {
		return URL_APKVersion;
	}

	public void setURL_APKVersion(String uRL_APKVersion) {
		URL_APKVersion = uRL_APKVersion;
	}

	public String getURL_RecResourceIMG() {
		return URL_RecResourceIMG;
	}

	public void setURL_RecResourceIMG(String uRL_RecResourceIMG) {
		URL_RecResourceIMG = uRL_RecResourceIMG;
	}

	public String getURL_RecAdvert() {
		return URL_RecAdvert;
	}

	public String getURL_IC() {
		return URL_IC;
	}

	public String getURL_ONEIC() {
		return URL_ONEIC;
	}

	public String getURL_NOTICE() {
		return URL_NOTICE;
	}

	public String getURL_SchoolInfo() {
		return URL_SchoolInfo;
	}

	public String getURL_SchoolPicInfo() {
		return URL_SchoolPicInfo;
	}

	public String getURL_Weather() {
		return URL_Weather;
	}

	public String getURL_UploadFile() {
		return URL_UploadFile;
	}

	public String getURL_UploadRecord() {
		return URL_UploadRecord;
	}

	public String getURL_ReUploadRecord() {
		return URL_ReUploadRecord;
	}

	public String getURL_RecResource() {
		return URL_RecResource;
	}

	public String getURL_ICCardTime() {
		return URL_ICCardTime;
	}

	public void setURL_ICCardTime(String uRL_ICCardTime) {
		URL_ICCardTime = uRL_ICCardTime;
	}

	public String getURL_SchoolInfoMain() {
		return URL_SchoolInfoMain;
	}

	public void setURL_SchoolInfoMain(String uRL_SchoolInfoMain) {
		URL_SchoolInfoMain = uRL_SchoolInfoMain;
	}
}

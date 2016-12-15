package com.hyc.network;

import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hyc.bean.InterWeb;


/**
 * 
 * 通过调用服务器“上传资源”接口，检测网络连接
 * 杨滔
 *2016/3/25
 */

public class IsNetWork {
	InterWeb interWeb = new InterWeb();

	public boolean isNetWork() {
		URL five_url;

		try {
			
			five_url = new URL(interWeb.getURL_UploadRecord());
			HttpURLConnection conn = (HttpURLConnection) five_url
					.openConnection();
			conn.setConnectTimeout(5000);
			if (conn.getResponseCode() == 200) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	
	public boolean isNetworkAvailable(Activity activity){
		Context context = activity.getApplicationContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null)
		{
			return false;
		}
		else
		{
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0)
			{
				for (int i = 0; i < networkInfo.length; i++)
				{

					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

}

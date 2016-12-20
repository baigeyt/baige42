package com.hyc.rec;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.extend.DataString;
import com.extend.DeleteFilePic;
import com.extend.GetFileNum;
import com.hyc.bean.InterWeb;
import com.hyc.bean.PictureId;
import com.hyc.db.DBManagerAdvert;

public class RecAdvert {
	InterWeb interWeb = new InterWeb();
	private String picT_String;
	private final static String ALBUM_PATH = Environment
			.getExternalStorageDirectory() + "/baige/";
	private String mFileName;
	private String time;
	private int n = 1, kk, jj;

	int cont = 0;
	int fileNum = 0;
	JSONArray jsonNow;
	private String src;
	// 客戶添加图片时间
	public static String time_picture;
	// 客户添加视频时间
	public static String time_advert;
	// 视频播放开始时间
	public static String stime_advert;
	// 视频播放开始时间
	public static String etime_advert;
	// 图片播放开始时间
	public String stime_picture = null;
	// 图片播放结束时间
	public String etime_picture = null;
	// 图片存储文件夹

	DBManagerAdvert dBManageradvert = new DBManagerAdvert();
	PictureId pictureId = new PictureId();

	// 获取资源ID
	public int receiveAdvert() {
		URL five_url;
		dBManageradvert.creatDB();
		dBManageradvert.openDB();
		try {
			five_url = new URL(interWeb.getURL_RecAdvert());
			HttpURLConnection five_urlConnection = (HttpURLConnection) five_url
					.openConnection();
			five_urlConnection.setRequestMethod("GET");// 设置请求的方式
			five_urlConnection.setReadTimeout(5000);// 设置超时的时间
			five_urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
			// 设置请求的头
			System.out.println("获取  资源");
			System.out.println("访问连接" + interWeb.getURL_RecAdvert());

			// 获取响应的状态码 404 200 505 302
			if (five_urlConnection.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								five_urlConnection.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					picT_String += line;
				}
				System.out.println(picT_String);
				jsonNow = new JSONObject(picT_String.substring(4))
						.getJSONArray("list");

				System.out.println("资源总数量： " + jsonNow.length());
				for (int i = 0; i < jsonNow.length(); i++) {
					JSONObject object = jsonNow.getJSONObject(i);
					System.out.println("object  " + object);
					if (object.getString("type").equals("2")) {

						jj++;
						stime_picture = object.getString("stime");
						etime_picture = object.getString("etime");
						if (stime_picture != null && etime_picture != null) {

							Log.e("时间", stime_picture);
							Log.e("时间", etime_picture);

							String pic_url = getDir() + "/baige/advertPicFile/"
									+ stime_picture + "_" + etime_picture;
							File pic_file = new File(pic_url);
							if (pic_file.exists()) {
							} else {
								pic_file.mkdirs();
							}
						}
						JSONArray array = object.getJSONArray("resourceids");
						cont += array.length();
						System.out.println("进进进");
						System.out.println("JSONArray" + array.toString());
						System.out.println("JSONArray" + cont);

						for (int j = 0; j < array.length(); j++) {
							System.out.println("这里是图片保存");
							System.out.println("JSONArray" + array.toString());

							n = 1;
							JSONObject ject = array.getJSONObject(j);
							src = ject.getString("src");
							System.out.println("src  " + src);
							// 遍历JSONArray
							time = ject.getString("time");
							mFileName = j + ".jpg";
							if (src != null) {
								Cursor cursor = dBManageradvert.db.query(
										"advert", null, null, null, null, null,
										null);
								if (cursor.moveToFirst()) {
									for (cursor.moveToFirst(); !cursor
											.isAfterLast(); cursor.moveToNext()) {
										if (src.equals(cursor.getString(cursor
												.getColumnIndex("src")))) {
											System.out.println("图片ID一样，图片ID一样");
											if (time.equals(cursor.getString(cursor
													.getColumnIndex("time")))) {
												System.out
														.println("图片time一样，图片time一样");
												n = 0;
												break;
											} else {
												pictureId.setTime(time);
												ContentValues time_values = new ContentValues();
												time_values.put("time",
														pictureId.getTime());
												dBManageradvert.db.update(
														"advert", time_values,
														"src=" + src, null);
												// saveFile("advertPicFile/pic"
												// + jj
												// + mFileName, src);

												saveFile("advertPicFile/"
														+ stime_picture + "_"
														+ etime_picture + "/"
														+ jj + mFileName, src);
												n = 0;
												break;
											}
										}
									}
									if (n == 1) {
										saveFile("advertPicFile/"
												+ stime_picture + "_"
												+ etime_picture + "/" + jj
												+ mFileName, src);
										// saveFile("advertPicFile/pic" + jj
										// + mFileName, src);
										pictureId.setSrc(src);
										pictureId.setTime(time);
										pictureId.setStime(stime_picture);
										pictureId.setEtime(etime_picture);
										pictureId.setSrcname(stime_picture + "_"
												+ etime_picture);

										dBManageradvert.insert(pictureId);
									}

								} else {
									saveFile("advertPicFile/" + stime_picture
											+ "_" + etime_picture + "/" + jj
											+ mFileName, src);
									pictureId.setSrc(src);
									pictureId.setTime(time);
									pictureId.setStime(stime_picture);
									pictureId.setEtime(etime_picture);
									pictureId.setSrcname(stime_picture + "_"
											+ etime_picture);
									dBManageradvert.insert(pictureId);
								}
								cursor.close();
							}
						}
					} else if (object.getString("type").equals("4")) {
						kk++;

						JSONArray array1 = object.getJSONArray("resourceids");

						stime_advert = refFormatNowDate(Integer.parseInt(object
								.getString("stime")));
						etime_advert = refFormatNowDate(Integer.parseInt(object
								.getString("etime")));

						// System.out.println("stime_advert:  " + stime_advert);
						// System.out.println("etime_advert:  " + etime_advert);

						System.out.println("array1  " + array1);
						cont += array1.length();
						System.out.println("视频个数： " + array1.length());
						for (int k = 0; k < array1.length(); k++) {
							System.out.println("这里是视频保存");
							JSONObject mobject = array1.getJSONObject(k);
							String src = mobject.getString("src");
							String time = mobject.getString("time");

							time_advert = refFormatNowDate(Integer
									.parseInt(time));
							// System.out.println("time_advert" + time_advert);

							System.out.println("src  " + src);

							// containsString(str1,str2)判断str1这个字符串里面存不存在str2这个字符
							if (containsString(src, ".mp4")
									&& containsString(src, "/")) {
								mFileName = src.substring(
										src.lastIndexOf("/") + 1,
										src.lastIndexOf(".mp4"))
										+ ".mp4";
							}
							System.out.println("mFileName  " + mFileName);
							if (mFileName != null) {

								saveFile("advertVideoFile/" + "baige" + kk
										+ ".mp4",
										interWeb.getURL_RecResourceIMG()   
												+ "videos/" + mFileName);

							}
						}
					}
				}
				picT_String = null;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}finally{
//			deleteNoUsedFile();
			if(dBManageradvert!=null){
				dBManageradvert.closeDB();	
			}
		}
		return cont;
	}

	/**
	 * 判断当前时间 是否在视频播放开始时间与结束时间之内
	 */

	public String isplay() {
		System.out.println("调用了isplay()");
		List<String> list = getPicFile();
		List<String> paths = new ArrayList<String>();

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String str1 = list.get(i).substring(0,
							list.get(i).lastIndexOf("_"));
					String str2 = list.get(i).substring(
							list.get(i).lastIndexOf("_") + 1,
							list.get(i).length());

					String str11 = refFormatNowDate(Integer.parseInt(str1));
					String str22 = refFormatNowDate(Integer.parseInt(str2));

					Date d1 = df.parse(str11);
					Date d2 = df.parse(str22);

					Date d3 = df.parse(DataString.StringData1());

					if (d2.getTime() > d3.getTime()
							&& d1.getTime() < d3.getTime()) {
						System.out.println("视频在播放时间内");

						paths.add(Environment.getExternalStorageDirectory()
								+ "/baige/advertPicFile/" + list.get(i));
					}else{
						DeleteFilePic.delete(new File(Environment.getExternalStorageDirectory()
								+ "/baige/advertPicFile/" + list.get(i)));
					}
				}
				if (paths.size() > 0) {
					return paths.get(0);
				}
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<String> getPicFile() {
		List<String> list = new ArrayList<String>();
		System.out.println("调用了isplayPic()");
		String path = Environment.getExternalStorageDirectory()
				+ "/baige/advertPicFile/";
		int cc = new GetFileNum().getAllFilesNum(new File(path));

		if (cc > 0) {

			File file = new File(path);
			File files[] = file.listFiles();
			for (int i = 0; i < files.length; i++) {

				list.add(files[i].getName());
				System.out.println("....." + files[i].getName());
			}

		}
		return list;
	}

	/*
	 * 判断字符串是否包含一些字符 contains
	 */
	public static boolean containsString(String src, String dest) {
		boolean flag = false;
		if (src.contains(dest)) {
			flag = true;
		}
		return flag;
	}

	public boolean fileIsExists(String file) {
		try {
			File f = new File(Environment.getExternalStorageDirectory()
					+ "/baige/advertVideoFile/" + file);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}
			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
		}
	}

	// 获取视频时间
	public String refFormatNowDate(int time) {
		SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String times = sdFormatter.format(time * 1000L);
		return times;
	}

	private void saveFile(String filepath, String myurl) {
		try {
			System.out.println("filePath" + filepath);
			URL url = new URL(myurl);
			System.out.println("访问连接filePath：  " + myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			if (conn.getResponseCode() == 200) {
				InputStream inStream = conn.getInputStream();
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = -1;
				while ((len = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				outStream.close();
				inStream.close();
				byte[] data = outStream.toByteArray();
				File file = new File(ALBUM_PATH + filepath);
				FileOutputStream outputStream = new FileOutputStream(file);
				outputStream.write(data);
				outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private File getDir() {
		// 得到SD卡根目录
		File dir = Environment.getExternalStorageDirectory();
		if (dir.exists()) {
			return dir;
		} else {
			dir.mkdirs();
			return dir;
		}
	}
	
	private void deleteNoUsedFile(){
		File picFileSrc = new File(Environment.getExternalStorageDirectory()
					+ "/baige/advertPicFile");
		File picFileSrcs[] = picFileSrc.listFiles();
		for(int i=0;i<picFileSrcs.length;i++){
			if(qureyData(picFileSrcs[i].getPath().substring(picFileSrcs[i].getPath().lastIndexOf("/")+1,picFileSrcs[i].getPath().length())).equals("c")){
				DeleteFilePic.delete(picFileSrcs[i]);
			}
		}
	}
	
	private String qureyData(String picName){
		String srcPath = "c";
		String columns[] = new String[]{"srcname"};
		String wheres[] = new String[]{picName};
		Cursor curAllPic = dBManageradvert.db.query(
				"advert", columns, "srcname=?", wheres, null, null,
				null);
		for(curAllPic.moveToFirst(); !curAllPic
				.isAfterLast(); curAllPic.moveToNext()){
			srcPath = curAllPic.getString(curAllPic.getColumnIndex("srcname"));
		}
		return srcPath;
	}
}

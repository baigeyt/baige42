package com.hyc.baige;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.extend.CardMd5;
import com.extend.DataString;
import com.extend.DeleteFilePic;
import com.extend.GetFileNum;
import com.extend.InstallAPK;
import com.extend.PicDispose;
import com.hyc.bean.APKInfo;
import com.hyc.bean.Company;
import com.hyc.bean.ICCardTime;
import com.hyc.bean.ImgInfo;
import com.hyc.bean.MacEntity;
import com.hyc.bean.NameClass;
import com.hyc.bean.Stu;
import com.hyc.db.DBMacAddress;
import com.hyc.db.DBManagerAdvert;
import com.hyc.db.DBManagerCard;
import com.hyc.db.DBManagerCompany;
import com.hyc.db.DBManagerICCardTime;
import com.hyc.db.DBManagerSchPic;
import com.hyc.db.DBManagerStu;
import com.hyc.db.Db;
import com.hyc.network.GetDeviceID;
import com.hyc.network.IsNetWork;
import com.hyc.network.NetReceiver;
import com.hyc.rec.RecAdvert;
import com.hyc.rec.RecICCardTime;
import com.hyc.rec.RecNotice;
import com.hyc.rec.RecOneCard;
import com.hyc.rec.RecSchoolInfo;
import com.hyc.rec.RecSchoolPicInfo;
import com.hyc.rec.RecVerSionAPK;
import com.hyc.rec.RecWeather;
import com.hyc.rec.RequestDataOSS;
import com.hyc.rec.ResInstallAPK;
import com.hyc.up.OSSSample;
import com.tony.autoscroll.AutoScrollView;
import com.tony.autoscroll.FullScreenVideoView;

public class MainActivity extends Activity {
	// TODO 蓝点是可能需要改的地方
	// ------------------------------------------
	DBManagerStu dbManagerstu = new DBManagerStu();
	DBManagerCard dbManagercard = new DBManagerCard();
	DBManagerAdvert dBManageradvert = new DBManagerAdvert();
	DBManagerSchPic dBManagerSchPic = new DBManagerSchPic();
	private String name = null;
	private String classname = null;
	private Context context;
	private EchoThread echoThread = new EchoThread();
	private int client_num;
	private int allcount = 0;
	private int card_count = 0;
	public static int upallcount = 0;
	private ServerS servers;
	private ServerSocket server;
	private String IP = null;
	private String serverAddress;

	private Db db;
	private SQLiteDatabase dbWriter, dbReader;
	private int flag1 = 0;
	private int flag2 = 0;
	private int flag3 = 0;
	private int flag4 = 0;
	private int flag5 = 0;
	private int flag6 = 0;
	public static int flag7 = 0;
	public static int flag8 = 0;
	public static int flag9 = 0;
	private String city;
	private String district;
	private int SchoolID = 0;
	public static Intent intent;
	private OSS oss = null;
	private List<String> list = new ArrayList<String>();
	public static final String action = "jason.broadcast.action";
	private ImgInfo mImginfo;
	private String parment;
	// ------------------------------------------
	private ImageView imageView1, imageView2, imageView3, imageView4,
			imageView5, imageView6, imageView7, imageView8, infoimg1, infoimg2,
			infoimg3, imageLOGO;
	private TextView textView1_1, textView1_2, textView2_1, textView2_2,
			textView3_1, textView3_2, textView4_1, textView4_2, textView5_1,
			textView5_2, textView6_1, textView6_2, textView7_1, textView7_2,
			textView8_1, textView8_2, textConnect, textIntroduce, textContact,
			textAddress, textAllcount, textAAA, textWeather, textNotice,
			textSSS;
	private Timer timercont, timerAdvert;
	public static Timer timerAd;
	private Timer timer, timercardno,timerTwoUp;
	private TimerTask task, taskcont, taskAdvert,taskTwoUp;
	// ------------------------------------------
	private int imgvCount = 0;
	private String timeName = null;
	private long tt = 4294967295L;
	// ------------------------------------------

	public static double latitude = 39.9;
	public static double longitude = 116.3;
	private IsNetWork work = new IsNetWork();
	public static int state = 0;
	public static TimerTask timerTask;
	private AtomicInteger what1 = new AtomicInteger(0);
	private ViewPager advPager = null;
	private ImageView img1, img2, img3, img4, img5, img6, img7, img8;
	private VideoView videoView;
	private List<View> advPics, school_advPics;
	private int advert = 0;
	private long videoDuration;
	private boolean isContinue = true;
	private ViewPager school_advPager = null;
	private int currentItem = 0; // 当前图片的索引号

	private AutoScrollView scrollView;
	private TextView te;
	private ScheduledExecutorService scheduledExecutorService;
	private LinearLayout linearLayout;

	// 时间的两个变量
	private static final int msgKey1 = 1;
	private TextView mTime;

	// 存储学校信息
	DBManagerCompany dbCompany = new DBManagerCompany();
	Company company = new Company();

	private int videoNum = 1;
	private Thread play;

	// 自定义刷卡时间
	DBManagerICCardTime dbCardTime;
	List<ICCardTime> cardTimelist = null;
	int timeIdentify = 0;

	// 線程池
	private ExecutorService mExecutorService = null;
	private ThreadPoolExecutor threadPoolSoc = null;

	// 自动更新本地学生数据
	private boolean noOss = false;
	private boolean school = true;
	
	private boolean isNetWorkNormal = false;
	private DBMacAddress dbMac;
	public static String accesstoken;

	// 更新数据广播
	private Broad broad;

	// ------------------------------------------
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉TITLE
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// 去掉虚拟键(API必须大于或等于14)
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

		setContentView(R.layout.activity_main);
		registerUpdate();

		db = new Db(MainActivity.this);
		dbWriter = db.getWritableDatabase();
		dbReader = db.getReadableDatabase();
		db.insertTwo("0");
		dbWriter.delete("filepaths", null, null);
		dbWriter.delete("allpaths", null, null);
		dbManagerstu.creatDB();
		dbManagercard.creatDB();
		dBManagerSchPic.creatDB();
		dBManageradvert.creatDB();
		dbCompany.creatDB();
		// 测试更新数据
		// dbManagerstu.insertDate("2016-10-27");

		imageView1 = (ImageView) this.findViewById(R.id.imageView1);
		imageView2 = (ImageView) this.findViewById(R.id.imageView2);
		imageView3 = (ImageView) this.findViewById(R.id.imageView3);
		imageView4 = (ImageView) this.findViewById(R.id.imageView4);
		imageView5 = (ImageView) this.findViewById(R.id.imageView5);
		imageView6 = (ImageView) this.findViewById(R.id.imageView6);
		imageView7 = (ImageView) this.findViewById(R.id.imageView7);
		imageView8 = (ImageView) this.findViewById(R.id.imageView8);

		textView1_1 = (TextView) this.findViewById(R.id.textView1_1);
		textView1_2 = (TextView) this.findViewById(R.id.textView1_2);
		textView2_1 = (TextView) this.findViewById(R.id.textView2_1);
		textView2_2 = (TextView) this.findViewById(R.id.textView2_2);
		textView3_1 = (TextView) this.findViewById(R.id.textView3_1);
		textView3_2 = (TextView) this.findViewById(R.id.textView3_2);
		textView4_1 = (TextView) this.findViewById(R.id.textView4_1);
		textView4_2 = (TextView) this.findViewById(R.id.textView4_2);
		textView5_1 = (TextView) this.findViewById(R.id.textView5_1);
		textView5_2 = (TextView) this.findViewById(R.id.textView5_2);
		textView6_1 = (TextView) this.findViewById(R.id.textView6_1);
		textView6_2 = (TextView) this.findViewById(R.id.textView6_2);
		textView7_1 = (TextView) this.findViewById(R.id.textView7_1);
		textView7_2 = (TextView) this.findViewById(R.id.textView7_2);
		textView8_1 = (TextView) this.findViewById(R.id.textView8_1);
		textView8_2 = (TextView) this.findViewById(R.id.textView8_2);

		textConnect = (TextView) this.findViewById(R.id.textConnect);
		textIntroduce = (TextView) this.findViewById(R.id.textIntroduce);
		textContact = (TextView) this.findViewById(R.id.textContact);
		textAddress = (TextView) this.findViewById(R.id.textAddress);

		textAllcount = (TextView) this.findViewById(R.id.allcount);
		textAAA = (TextView) this.findViewById(R.id.aaaa);
		scrollView = (AutoScrollView) findViewById(R.id.auto_scrollview);
		textWeather = (TextView) this.findViewById(R.id.textWeather);
		textNotice = (TextView) this.findViewById(R.id.textNotice);
		textSSS = (TextView) this.findViewById(R.id.textSSS);
		imageLOGO = (ImageView) this.findViewById(R.id.school_LOGO);
		te = (TextView) this.findViewById(R.id.te);
		infoimg1 = (ImageView) this.findViewById(R.id.infoimg1);
		infoimg2 = (ImageView) this.findViewById(R.id.infoimg2);
		infoimg3 = (ImageView) this.findViewById(R.id.infoimg3);

		// 显示时间，日期，星期
		mTime = (TextView) findViewById(R.id.mytime);
		mTime.setText(DataString.StringData());
		// new TimeThread().start();

		linearLayout = (LinearLayout) this.findViewById(R.id.schoolinfo);

		PicDispose.copyToSD(MainActivity.this);

		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				noOss = true;
				// 获取服务端APK信息
				RecVerSionAPK recVerSionAPK = new RecVerSionAPK();
				APKInfo aInfo = recVerSionAPK.getAPKVersion(MainActivity.this);

				// 下载服务端apk
				if (aInfo != null) {
					if (aInfo.getApkUrl() != null) {
						ResInstallAPK resInstallAPK = new ResInstallAPK();
						String path = aInfo.getApkUrl();
						resInstallAPK.getFileFromServer(path, schooleInfo);
					}
				}

				// 获取自定义刷卡时间
				RecICCardTime recICCardTime = new RecICCardTime();
				recICCardTime.recCardTime(mHandler);

				// te.setVisibility(View.GONE);
				// 获取天气
				// 获取学校文字介绍
				RecSchoolInfo recSchoolInfo = new RecSchoolInfo(weatherHandler);
				recSchoolInfo.receiveSchInfo();
				for (int i = 1; i < 5; i++) {
					// 获取展示图片
					RecSchoolPicInfo recSchoolPicInfo = new RecSchoolPicInfo(i);
					recSchoolPicInfo.receiveWeather();
				}
				// 获取公告
				RecNotice recNotice = new RecNotice();
				recNotice.receiveDate();
				// 通知主线程
				Message wthInfomsg = new Message();
				wthInfomsg.what = 1;
				MainActivity.this.weatherHandler.sendMessageDelayed(wthInfomsg,
						3000);

				// 发消息让屏保隐藏
				Message message = new Message();
				message.what = 0;
				if (scInfoHandler != null) {
					scInfoHandler.sendMessageDelayed(message, 10000);
				}

				try {
					Thread.sleep(5000);
					// 把学校信息存入数据库
					company = dbCompany.query();
					if (company.getName() == null) {
						System.out.println("添加学校信息到数据库");
						if (RecSchoolInfo.schoolName != null) {
							company.setSchoolid(RecSchoolInfo.logoId);
							company.setName(RecSchoolInfo.schoolName);
							company.setQq(RecSchoolInfo.qq);
							company.setMobile(RecSchoolInfo.mobile);
							company.setEmail(RecSchoolInfo.email);
							company.setProvince(RecSchoolInfo.province);
							company.setCity(RecSchoolInfo.city);
							company.setDistrict(RecSchoolInfo.district);
							company.setAddress(RecSchoolInfo.address);
							company.setContent(RecSchoolInfo.content);
							dbCompany.insert(company);
						}
					} else if (RecSchoolInfo.schoolName == null) {
						if (company.getName() != null) {
							Message msg = new Message();
							msg.what = 3;
							MainActivity.this.weatherHandler.sendMessage(msg);
						}
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		timer.schedule(task, 0, 1800000);

		playPicorVid();
		// 断网后数据上传
		initTimer();
		twoUpTimer();
		//
		timercont = new Timer();
		taskcont = new TimerTask() {
			@Override
			public void run() {
				WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifi.getConnectionInfo();
				IP = intToIp(wifiInfo.getIpAddress());

				DeleteFilePic.deletelistFiles(new File(getDir()
						+ "/baige/picFile"));
				DeleteFilePic.deletelistFiles(new File(getDir()
						+ "/baige/twoFile"));
				// System.out.println(IP);
				servers = new ServerS();
				servers.start();

			}
		};
		timercont.schedule(taskcont, 3000);

		// 设置刷卡间隔时间
		timercardno = new Timer();
		TimerTask task_cardno = new TimerTask() {
			@Override
			public void run() {
				try {

					// 判断当前时间是否是在自定义的刷卡时间内
					if (timeIdentify == 0) {
						// 删除刷卡记录表，清除重复刷卡限制
						dbManagercard.delete();
					}
					// -----显示时间，日期，星期-----------
					Message msg = new Message();
					msg.what = msgKey1;
					mHandler.sendMessage(msg);

					// 检索视频播放的结束时间"2016-06-22 11:50"
					if (DataString.StringData1().equals(RecAdvert.etime_advert)) {
						Message etime_msg = new Message();
						etime_msg.what = 234;
						mHandler.sendMessage(etime_msg);
					}
					// 定时清除刷卡数据
					if (DataString.StringData2().equals("23:59")) {
						Message clear_allcount = new Message();
						clear_allcount.what = 456;
						mHandler.sendMessage(clear_allcount);
					}

					// 开机没网络状态下 重新获取OSS凭证
					if (oss == null && new IsNetWork().isNetWork()) {
						Message oss_init = new Message();
						oss_init.what = 4;
						weatherHandler.sendMessage(oss_init);
					}

					// 检测视频播放时间
					String adePicPath = new RecAdvert().isplay();
					if (adePicPath != null) {
						File adePicfile = new File(new RecAdvert().isplay());
						if (adePicfile.exists()) {
							Log.e("src", "有资源文件夹存在");
							if (new GetFileNum().getAllFilesNum(adePicfile) == 4) {
								Log.e("src", "文件夹里有4个");
								if (!adePicPath.equals(timeName)) {
									Log.e("src", "有新的资源");
									// 打开视频播放
									Message msg_pic = new Message();
									msg_pic.what = 100;
									Bundle bPicPath = new Bundle();
									bPicPath.putString("bPicPath", adePicPath);
									msg_pic.setData(bPicPath);
									mHandler.sendMessage(msg_pic);
									timeName = adePicPath;
								}
							}
						}
					}

					// 判断当前时间是否在自定义刷卡时间内
					if (cardTimelist != null) {
						if (cardTimelist.size() > 0) {
							if (new DataString().isSwipingCard(cardTimelist)) {
								// 发消息让socket断开连接，关闭刷卡功能
								Log.v("dd", "打开刷卡");
								mHandler.sendEmptyMessage(110);
							} else {
								// 发消息让socket打开连接，开启刷卡功能
								Log.v("dd", "关闭刷卡");
								mHandler.sendEmptyMessage(111);

							}
						} else {
							Log.v("dd", "打开刷卡aaabbb");
							timeIdentify = 0;
						}
					} else {
						Log.v("dd", "打开刷卡aaa");
						timeIdentify = 0;
					}

				} catch (Exception e) {
					// TODO: handle exception
					Log.e("yichang", e.toString());
					e.printStackTrace();
				}
			}
		};
		timercardno.schedule(task_cardno, 5000, 60000);

		IntentFilter filter = new IntentFilter(action);
		registerReceiver(broadcastReceiver, filter);

		dbCardTime = new DBManagerICCardTime();
		dbCardTime.creatDB();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case msgKey1:
				// 定时跟新显示时间
				mTime.setText(DataString.StringData());
				break;
			case 123:
				if (new File(Environment.getExternalStorageDirectory()
						+ "/baige/advertVideoFile/" + "baige1.mp4").exists()) {
					advert = 1;
					initViewPager(new RecAdvert().isplay());
					Log.v("TimeThread", "开始放视频");
				}
				break;
			case 234:

				advert = 2;
				initViewPager(new RecAdvert().isplay());
				Log.v("TimeThread", "停止放视频");

				break;
			case 456:
				// 定时清除刷卡数跟上传数
				upallcount = 0;
				allcount = 0;
				textAllcount.setText("刷卡数 : " + allcount);
				textAAA.setText("上传数 : " + upallcount);
				break;
			case 99:
				Log.e("99", "进99来了");
				if (msg.getData() != null) {
					if (msg.getData().getString("srcpath") != null) {
						initViewPager(msg.getData().getString("srcpath"));
					} else {
						initViewPager(getDir().getPath());
					}
				} else {
					initViewPager(getDir().getPath());
				}
				break;
			case 100:
				advert = 1;
				if (play != null) {
					if (!play.isInterrupted()) {
						play.interrupt();
						play = null;
					}
				}
				what1 = new AtomicInteger(0);
				initViewPager(msg.getData().getString("bPicPath"));
				break;
			case 111:
				timeIdentify = 1;
				break;
			case 110:
				timeIdentify = 0;
				break;
			case 120:
				textConnect.setText("网络状态：已连接");
				isNetWorkNormal = true;
				break;
			case 121:
				textConnect.setText("网络状态：未连接");
				isNetWorkNormal = false;
				break;
			case 77:
				Toast.makeText(MainActivity.this, "卡号位数不正确", Toast.LENGTH_SHORT)
						.show();
				break;
			case 88:
				Toast.makeText(MainActivity.this, "刷卡设备连接超时或异常！",
						Toast.LENGTH_SHORT).show();
				break;
			case 222:
				cardTimelist = dbCardTime.query();
				break;
			default:
				break;
			}
		}
	};

	// 屏保设置
	@SuppressLint("HandlerLeak")
	Handler scInfoHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				linearLayout.setVisibility(View.VISIBLE);
				break;
			case 1:
				textConnect.setText("连接信息：" + "\n" + "未连接");
				break;
			default:
				break;
			}
		}
	};

	@SuppressLint("HandlerLeak")
	Handler hideHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				linearLayout.setVisibility(View.INVISIBLE);
			}
		}
	};
	@SuppressLint("HandlerLeak")
	Handler weatherHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				Bundle bun = msg.getData();
				city = bun.getString("city");
				district = bun.getString("district");
				if (city.indexOf("市") != -1) {
					city = city.substring(0, city.lastIndexOf("市"));
				}
				if (city.indexOf("地区") != -1) {
					city = city.substring(0, city.lastIndexOf("地区"));
				}
				if (city.indexOf("自治州") != -1) {
					city = city.substring(0, city.lastIndexOf("自治州"));
				}
				if (city.indexOf("自治区") != -1) {
					city = city.substring(0, city.lastIndexOf("自治区"));
				}
				if (district.indexOf("县") != -1) {
					district = district.substring(0, district.lastIndexOf("县"));
				}

				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						RecWeather mGetWeather = new RecWeather();
						mGetWeather.request(city);
						System.out.println(city + "     " + district);
						System.out.println("天气天气天气天气天气天气天气天气");

						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						RecWeather mGetWeather = new RecWeather();
						if (mGetWeather.wendu.equals("暂无")) {
							new AsyncTask<Void, Void, Void>() {

								@Override
								protected Void doInBackground(Void... params) {
									RecWeather mGetWeather = new RecWeather();
									mGetWeather.request(district);
									System.out.println(city + "     "
											+ district);
									System.out.println("天气天气天气天气天气天气天气天气");
									return null;
								}

								protected void onPostExecute(Void result) {
									RecWeather mGetWeather = new RecWeather();
									textWeather.setText("今日天气:"
											+ mGetWeather.type + "\t温度:"
											+ mGetWeather.wendu + "℃" + "\t风力:"
											+ mGetWeather.fengli + "\t风向:"
											+ mGetWeather.fengxiang
											+ "\t♥百鸽提示您♥:"
											+ mGetWeather.ganmao);
								};
							}.execute();
						} else {
							textWeather.setText("今日天气:" + mGetWeather.type
									+ "\t温度:" + mGetWeather.wendu + "℃"
									+ "\t风力:" + mGetWeather.fengli + "\t风向:"
									+ mGetWeather.fengxiang + "\t♥百鸽提示您♥:"
									+ mGetWeather.ganmao);
						}
						super.onPostExecute(result);
					}

				}.execute();

				break;
			case 1:
				// setControls();
				te.setVisibility(View.GONE);
				textIntroduce.setText(RecSchoolInfo.schoolName);
				textContact.setText("联系方式：" + "QQ:" + RecSchoolInfo.qq
						+ "\t电话：" + RecSchoolInfo.mobile + "\tEmail："
						+ RecSchoolInfo.email);
				textAddress.setText("学校地址：" + RecSchoolInfo.province
						+ RecSchoolInfo.city + RecSchoolInfo.district
						+ RecSchoolInfo.address);

				String mContent = RecNotice.content;
				mContent = mContent.replace("&nbsp;", "");
				textNotice.setText("通知公告：" + mContent);

				String mTextSSS = RecSchoolInfo.content;
				mTextSSS = mTextSSS.replace("&nbsp;", "");
				textSSS.setText(mTextSSS);
				// 显示学校LOGO
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/baige/LOGOFile/0.jpg");
				if (file.exists()) {
					imageLOGO.setImageURI(Uri.fromFile(file));
				}
				school_initviewpager();

				scheduledExecutorService = Executors
						.newSingleThreadScheduledExecutor();
				// 当Activity显示出来后，每两秒钟切换一次图片显示
				scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(),
						1, 10, TimeUnit.SECONDS);

				// 文字滚动
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						if (!scrollView.isScrolled()) {
							scrollView.setScrolled(true);
						}
					}
				}, 2000);

				SchoolID = RecSchoolInfo.Id;

				System.out.println("SchoolID：    " + SchoolID);
				// if (String.valueOf(SchoolID) != null && SchoolID != 0) {
				// getOSSCertificate();
				// } else {
				// // 第一次启动apk没网络 从本地数据库获取学校ID
				// if (dbCompany.query() != null) {
				// company = dbCompany.query();
				// int number = 0;
				//
				// number = company.getSchoolid();
				// if (number != 0) {
				// SchoolID = company.getSchoolid();
				// getOSSCertificate();
				// }
				// }
				// }
				break;
			case 3:
				Log.v("dddd", "没网络 去本地找学校信息");
				company = dbCompany.query();

				textIntroduce.setText(company.getName());
				textContact
						.setText("联系方式：" + "QQ:" + company.getQq() + "\t电话："
								+ company.getMobile() + "\tEmail："
								+ company.getEmail());
				textAddress.setText("学校地址：" + company.getProvince()
						+ company.getCity() + company.getDistrict()
						+ company.getAddress());

				textSSS.setText(company.getContent());
				textNotice.setText("通知公告：" + "暂时获取不到公告");

				// textIntroduce.setText(company.getName() + "\r\n联系方式：" + "qq:"
				// + company.getQq() + "\t电话：" + company.getMobile()
				// + "\temail：" + company.getEmail() + "\r\n学校地址："
				// + company.getProvince() + company.getCity()
				// + company.getDistrict() + company.getAddress());
				textWeather.setText("暂时获取不到天气");
				break;

			case 4:
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						RecSchoolInfo recSchoolInfo = new RecSchoolInfo();
						recSchoolInfo.receiveSchInfo();
						return null;
					}

					protected void onPostExecute(Void result) {
						SchoolID = RecSchoolInfo.Id;
						if (SchoolID != 0 && String.valueOf(SchoolID) != null) {

							getOSSCertificate();
						} else {

							// 第一次启动apk没网络 从本地数据库获取学校ID
							if (dbCompany.query() != null) {
								company = dbCompany.query();
								int number = 0;

								number = company.getSchoolid();
								if (number != 0) {
									SchoolID = company.getSchoolid();
									getOSSCertificate();
								}
							}

						}
					};
				}.execute();
				break;
			}
		}
	};

	public void getOSSCertificate() {
		// 初始化OSS
		System.out.println("初始化OSS：   1111 ");
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				System.out.println("初始化OSS：  222 ");

				list = new RequestDataOSS().uploadFileOss(MainActivity.this,
						String.valueOf(SchoolID));
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				new Thread() {
					public void run() {
						// System.out.println("初始化OSS：  333 ");
						// System.out.println("初始化OSS： " + list.size());

						if (work.isNetWork() && list.size() == 3) {

							// System.out.println("初始化OSS：  444 ");
							//
							// System.out.println("oss：    11" + list.get(0));

							OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(
									list.get(0), list.get(1), list.get(2));
							ClientConfiguration ccf = new ClientConfiguration();
							ccf.setConnectionTimeout(3000);
							ccf.setSocketTimeout(3000);
							oss = new OSSClient(MainActivity.this,
									"http://oss-bj.360baige.cn",
									credentialProvider,ccf);
							System.out.println("oss：    22" + oss);
							Timer timer = new Timer();
							TimerTask task = new TimerTask() {
								@Override
								public void run() {
									new AsyncTask<Void, Void, Void>() {

										@Override
										protected Void doInBackground(
												Void... params) {
											list = new RequestDataOSS()
													.uploadFileOss(
															MainActivity.this,
															String.valueOf(SchoolID));
											return null;
										}

										@Override
										protected void onPostExecute(Void result) {
											new Thread() {
												public void run() {
													if (work.isNetWork()
															&& list.size() == 3) {
														OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(
																list.get(0),
																list.get(1),
																list.get(2));

														oss = new OSSClient(
																MainActivity.this,
																"http://oss-bj.360baige.cn",
																credentialProvider);
													}
												};
											}.start();
											super.onPostExecute(result);
										}

									}.execute();
								}
							};
							timer.schedule(task, 120000, 1800000);
							try {
								String url = oss.presignConstrainedObjectURL(
										"sdk-baige", "logo.jpg", 3600);
								System.out
										.println(url
												+ "oooooooooooooooooooooookkkkkkkkkkkkk");
							} catch (ClientException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
				}.start();
				// TODO Auto-generated method stub
				super.onPostExecute(result);
			}

		}.execute();

	}

	// 定时启动二次上传
	private void initTimer() {
		timerAd = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				Log.e("haha", "哈哈哈哈哈哈哈");
				if (work.isNetWork()==true) {
					mHandler.sendEmptyMessage(120);
					if (noOss) {
						weatherHandler.sendEmptyMessage(4);
						noOss = false;
					}
					Log.e("haha", "网络正常");

					if (school) {
						RecSchoolInfo recSchoolInfo = new RecSchoolInfo();
						recSchoolInfo.receiveSchoolInfoMain(schooleInfo,
								getMac(), MainActivity.this);
						school = false;
					}
				} else {
					mHandler.sendEmptyMessage(121);
				}
			}
		};

		timerAd.schedule(timerTask, 0, 3000);
	}
	
	private void twoUpTimer() {
		timerTwoUp = new Timer();
		taskTwoUp = new TimerTask() {
			@Override
			public void run() {
				if (oss!=null&&isNetWorkNormal==true) {
					if (db.queryTwo() != null) {
						if (db.queryTwo().equals("0")) {
							two_compressPic();
						}
					}
				}
			}
		};

		timerTwoUp.schedule(taskTwoUp, 0, 3000);
	}

	private void compressPic(final ImgInfo imginfo) {
		Log.e("haha", "一次上传启动");
		scInfoHandler.removeMessages(0);
		Message hidemsg = new Message();
		hidemsg.what = 1;
		MainActivity.this.hideHandler.sendMessage(hidemsg);

		// Bitmap bitmap = null;
		// FileInputStream fis;
		try {
			// TODO Auto-generated method stub
			try {
				if (oss != null&&isNetWorkNormal==true) {
					System.out.println("22222222222222");
					System.out.println("imginfo" + imginfo.getCardno());
					String testObject = imginfo.getFile().substring(
							imginfo.getFile().lastIndexOf("/") + 1,
							imginfo.getFile().lastIndexOf("."));

					Date nowTime = new Date(System.currentTimeMillis());
					SimpleDateFormat sdFormatter = new SimpleDateFormat(
							"yyyyMMdd");
					String retStrFormatNowDate = sdFormatter.format(nowTime);

					String uploadFilePath = imginfo.getFile();

					parment = "baige2"
							+ "/"
							+ RecSchoolInfo.Id
							+ "/"
							+ retStrFormatNowDate
							+ "/"
							+ new CardMd5().GetMD5Code(testObject
									+ System.currentTimeMillis()) + ".jpg";
					System.out.println(parment);

					new OSSSample(MainActivity.this, uploadFilePath, oss,
							mHandler, imginfo, parment, scInfoHandler, dbWriter)
							.upload();
				} else {
					mHandler.sendEmptyMessage(121);
					ContentValues values = new ContentValues();
					values.put("type", imginfo.getType());
					values.put("cardno", imginfo.getCardno());
					values.put("alluploadpaths", imginfo.getFile());
					values.put("timecode", System.currentTimeMillis() / 1000);
					dbWriter.insert("allpaths", null, values);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.v("tt", "http://" + "sdk-baige."
					+ "oss-cn-beijing.aliyuncs.com/" + serverAddress);

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	private void two_compressPic() {

		ArrayList<String> reup = GetFileName();
		// System.out.println("待传人数" + reup.size());
		db.insertTwo("1");
		try {
			context = MainActivity.this;
			int num = reup.size();
			for (int j = 0; j < num; j++) {
				ArrayList<String> listCode = queryRecode(reup.get(j));
				String content = reup.get(j);
				int type = Integer.parseInt(listCode.get(1));
				long cardno = Long.parseLong(listCode.get(0));
				long time = Long.parseLong(listCode.get(2));

				ImgInfo info = new ImgInfo();
				info.setType(type);
				info.setCardno(cardno);

				String testObject = content.substring(
						content.lastIndexOf("/") + 1, content.lastIndexOf("."));

				Date nowTime = new Date(System.currentTimeMillis());
				SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMdd");
				String retStrFormatNowDate = sdFormatter.format(nowTime);

				String filePath = content;

				parment = "baige2"
						+ "/"
						+ RecSchoolInfo.Id
						+ "/"
						+ retStrFormatNowDate
						+ "/"
						+ new CardMd5().GetMD5Code(testObject
								+ System.currentTimeMillis()) + ".jpg";

				if (oss != null) {
					Log.e("ddd11", "6666666666666666666");
					Log.e("ddd22", info.getCardno() + "");
					Log.e("ddd33", parment);
					Log.e("ddd44", filePath);
					new OSSSample(MainActivity.this, filePath, oss, mHandler,
							info, parment, dbWriter).sycupload(time);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.insertTwo("0");
		}
	}

	public void transImage1(String fromFile, String toFile, int width,
			int height, int quality) {
		try {
			Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
			int bitmapWidth = bitmap.getWidth();
			int bitmapHeight = bitmap.getHeight();
			// 缩放图片的尺寸
			float scaleWidth = (float) width / bitmapWidth;
			float scaleHeight = (float) height / bitmapHeight;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 产生缩放后的Bitmap对象
			Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmapWidth, bitmapHeight, matrix, false);
			// save file
			File myCaptureFile = new File(toFile);
			FileOutputStream out = new FileOutputStream(myCaptureFile);
			if (resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
				out.flush();
				out.close();
			}
			if (!bitmap.isRecycled()) {
				bitmap.recycle();// 记得释放资源，否则会内存溢出
			}
			if (!resizeBitmap.isRecycled()) {
				resizeBitmap.recycle();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// 更新数据广播
	private void registerUpdate() {
		broad = new Broad();
		IntentFilter filter = new IntentFilter();// 创建IntentFilter对象
		filter.addAction("com.baige.ui.service");
		registerReceiver(broad, filter);// 注册Broadcast Receive
	}

	private class Broad extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getStringExtra("miss") != null) {
				if (intent.getStringExtra("miss").equals("2")) {
					timeIdentify = 0;
				}
			}

			if (intent.getStringExtra("reflush") != null) {
				if (intent.getStringExtra("reflush").equals("1")) {
					timeIdentify = 1;
					Toast.makeText(MainActivity.this, "正在更新数据，请不要刷卡！",
							Toast.LENGTH_LONG).show();
				}
			}
		}

	}

	Handler schooleInfo = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				dbManagerstu.delete();
				Log.e("在这下载", "下载数据");
				Log.e("comecome", "进入更新数据了0.6");
				new Thread() {
					public void run() {
						Login l = new Login();
						l.myFun(MainActivity.this);
					};
				}.start();
				break;
			case 3:
				Log.e("update", "日常检查资源文件");
				MainActivity.intent = new Intent(MainActivity.this,
						MyService.class);
				startService(MainActivity.intent);
				break;
			case 5:
				// 没换学校，日常更新数据
				Log.e("comecome", "进入更新数据了5.0");
				dbManagerstu.delete();
				new Thread() {
					public void run() {
						Login l = new Login();
						l.myFun(MainActivity.this);
					};
				}.start();
				break;
			case 6:
				// 静默安装apk
				InstallAPK installAPK = new InstallAPK();
				installAPK.onClick_install();
				break;
			}

		};
	};

	private String getMac() {

		dbMac = new DBMacAddress();
		dbMac.creatDB();
		dbMac.creatDB_ID();
		MacEntity macEntity = new MacEntity();
		macEntity = dbMac.query();
		if (macEntity.getMac() != null) {
			System.out.println("mac:  " + macEntity.getMac());
			accesstoken = macEntity.getMac();
			System.out.println("从数据库获取MAC");
		}
		if (accesstoken == null) {
			GetDeviceID getDeviceID = new GetDeviceID();
			accesstoken = getDeviceID.getMacAddress();
			System.out.println("accesstoken  " + accesstoken);
			if (accesstoken != null) {
				dbMac.insert(accesstoken);
			}
		}
		return accesstoken;
	}

	@SuppressLint("HandlerLeak")
	Handler upHandler = new Handler() {
		public void handleMessage(Message msg) {
			// System.out.println(imgvCount);
			if (msg.what == 1) {
				Bitmap bitmap = BitmapFactory.decodeFile(msg.getData()
						.getString("File"));

				try {
					if (bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {

					} else {
						BufferedOutputStream stream;
						stream = new BufferedOutputStream(new FileOutputStream(
								new File(msg.getData().getString("File"))));
						bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
					}
				} catch (Exception e) {
				} finally {
					if (bitmap != null) {
						bitmap.recycle();
					}
					bitmap = null;
				}
				File file = new File(msg.getData().getString("File"));
				Uri uri = Uri.fromFile(file);
				ImgInfo imginfo = new ImgInfo();
				imginfo.setCardno(msg.getData().getLong("cardno"));
				imginfo.setType(msg.getData().getInt("type"));
				imginfo.setFile(msg.getData().getString("File"));
				mImginfo = imginfo;
				switch (imgvCount) {
				case 0:
					imageView1.setImageURI(uri);
					System.out.println("777777777777777777777");
					imgvCount++;
					break;
				case 1:
					imageView2.setImageURI(uri);
					imgvCount++;
					break;
				case 2:
					imageView3.setImageURI(uri);
					imgvCount++;
					break;
				case 3:
					imageView4.setImageURI(uri);
					imgvCount++;
					break;
				case 4:
					imageView5.setImageURI(uri);
					imgvCount++;
					break;
				case 5:
					imageView6.setImageURI(uri);
					imgvCount++;
					break;
				case 6:
					imageView7.setImageURI(uri);
					imgvCount++;
					break;
				case 7:
					imageView8.setImageURI(uri);
					imgvCount = 0;
					break;
				default:
					break;
				}
				compressPic(imginfo);
			}
		}
	};

	public static byte[] fromHex(String hexString) throws NumberFormatException {
		hexString = hexString.trim();
		String s[] = hexString.split(" ");
		byte ret[] = new byte[s.length];
		for (int i = 0; i < s.length; i++) {
			ret[i] = (byte) Integer.parseInt(s[i], 16);
		}
		return ret;
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				String mClassname = "班级：";
				int mType = 8;
				mType = msg.getData().getInt("type");

				if (mType != 8) {
					mClassname = "职位：";
				}
				name = msg.getData().getString("name");
				classname = msg.getData().getString("classname");
				switch (imgvCount) {
				case 0:
					textView1_1.setText("姓名：" + name);
					textView1_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					break;
				case 1:
					textView2_1.setText("姓名：" + name);
					textView2_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					break;
				case 2:
					textView3_1.setText("姓名：" + name);
					textView3_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					break;
				case 3:
					textView4_1.setText("姓名：" + name);
					textView4_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					break;
				case 4:
					textView5_1.setText("姓名：" + name);
					textView5_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					break;
				case 5:
					textView6_1.setText("姓名：" + name);
					textView6_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					break;
				case 6:
					textView7_1.setText("姓名：" + name);
					textView7_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					break;
				case 7:
					textView8_1.setText("姓名：" + name);
					textView8_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					break;

				default:
					break;
				}
			}

		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (broadcastReceiver != null) {
			unregisterReceiver(broadcastReceiver);
		}
		stopService(intent);

		upallcount = 0;

		Login.accesstoken = null;

		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerAd != null) {
			timerAd.cancel();
			timerAd = null;
		}
		if (timercont != null) {
			timercont.cancel();
			timercont = null;
		}

		if (scrollView.isScrolled()) {
			scrollView.setScrolled(false);
		}

		dbManagercard.closeDB();
		if (dbManagerstu != null) {
			dbManagerstu.closeDB();
		}
		if (dbMac != null) {
			dbMac.closeDBMac();
		}

		dBManageradvert.closeDB();
		dBManagerSchPic.closeDB();

		try {

			if (server != null)
				server.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			echoThread.server_socket.close();
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("关闭------------------");
		// super.onDestroy();
	}

	private class ServerS extends Thread {
		@Override
		public void run() {
			super.run();

			try {
				server = new ServerSocket(3333);
//				mExecutorService = Executors.newCachedThreadPool();// 創建一個線程pool
				threadPoolSoc = new ThreadPoolExecutor(3, 10, 0,TimeUnit.SECONDS,    
		                //缓冲队列为3   
		                new ArrayBlockingQueue<Runnable>(3),   
		                //抛弃旧的任务   
		                new ThreadPoolExecutor.DiscardOldestPolicy());    

			} catch (IOException e1) {
				e1.printStackTrace();
			}
			while (true) {
				try {
					if (server != null) {
						Socket server_socket = server.accept();
						threadPoolSoc.execute(new EchoThread(server_socket));
//						mExecutorService.execute(new EchoThread(server_socket));// 執行里面的線程
						Log.e("线程", "...线程线程...");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	class EchoThread extends Thread {

		private Socket server_socket;
		private BufferedOutputStream outStream = null;
		private String socket_class, socket_name;
		private int type;
		private File pictureFile = null;
		private String picFilename, picFilepath = null;
		private Long cardno;
		private NameClass nameClass;

		private boolean out = true;
		private boolean cardok = true;
		private boolean carbad = true;
		private boolean picok = true;
		private boolean picbad = true;

		private String result = null;
		private int l = 0;
		private int j = 0;

		private int resetTime = 0;

		public EchoThread(Socket server_socket) {
			this.server_socket = server_socket;
		}

		public EchoThread() {

		}

		@Override
		public void run() {
			BufferedInputStream br = null;
			OutputStream server_out = null;
			try {
				server_socket.setSoLinger(true, 0);
				server_socket.setReuseAddress(true);
				server_socket.setSoTimeout(10000);
				br = new BufferedInputStream(server_socket.getInputStream());
				server_out = server_socket.getOutputStream();

				boolean temp = true;
				while (temp) {
					final byte[] buff = new byte[1024];
					int len = -1;

					while ((len = br.read(buff)) != -1) {
						result = new String(buff, 0, len);

						if (result.indexOf(result.valueOf("end")) != -1) {
							// System.out.println(result);
							if (result.indexOf(result.valueOf("cardok")) != -1
									&& cardok) {
								String[] card_string = result.split(":");

								for (int i = 0; i < card_string.length; i++) {

									if (i == 1) {
										byte[] card_data = new byte[len];
										for (int j = 0; j < len; j++) {
											card_data[j] = buff[j];
										}

										nameClass = card_client(server_out,
												card_data);
										long carNun = 0;
										if (nameClass != null) {

											carNun = nameClass.getCardno();
										}

										Date nowTime = new Date(
												System.currentTimeMillis());
										SimpleDateFormat sdFormatter = new SimpleDateFormat(
												"yyyy-MM-dd");

										String cardTime1 = sdFormatter
												.format(nowTime);
										CardMd5 cardMd5 = new CardMd5();

										if (carNun != 0) {
											serverAddress = SchoolID
													+ "/"
													+ cardTime1
													+ "/"
													+ cardMd5
															.GetMD5Code(carNun
																	+ nowTime
																			.toString());
											System.out.println(serverAddress
													+ "0000010");
										}

										if (nameClass != null) {
											Log.e("判断卡号", "en");
											temp = nameClass.getTemp();
											if (temp) {
												Log.e("卡号正确", "en");
												allcount++;
												nameClass.setStuNo(String
														.valueOf(allcount));
											}
										}
									}
								}
								cardok = false;
							} else if (result.indexOf(result
									.valueOf("cardokbad")) != -1 && carbad) {
								String[] cardbad_string = result.split(":");
								for (int i = 0; i < cardbad_string.length; i++) {
									if (i == 1) {
										// System.out.println(cardbad_string[i]);
									}
								}
								// outStream.write(buff, 0, len);
								// outStream.flush();
								temp = false;
								carbad = false;
							} else if (result.indexOf(result
									.valueOf("picturebad")) != -1 && picbad) {

								String[] picturebad_string = result.split(":");
								for (int i = 0; i < picturebad_string.length; i++) {
									if (i == 1) {
										System.out.println("picturebad...."
												+ picturebad_string[i]);
									}
								}
								// outStream.write(buff, 0, len);
								// outStream.flush();
								temp = false;
								picbad = false;
							} else {

								outStream.write(buff, 0, len);
								outStream.flush();
								l++;
								
								//新固件加的协议
								
								if (nameClass.getPath() != null) {
									File f = new File(nameClass.getPath());
									if (f.exists()) {
										String message2 = "recv:ok:end";
										server_out.write(message2
												.getBytes("UTF-8"));
										// System.out.println(message);
										server_out.flush();
									}
								}
							}
						} else {

							if (result.indexOf(result.valueOf("pictureok")) != -1
									&& picok) {
								Log.e("图片过来了", "过来了");
								String[] picture_string = result.split(":");

								int lenght = 0;
								for (int i = 0; i < 4; i++) {
									if (i == 1)
										lenght = picture_string[i].length();

									if ((i == 3) && (nameClass != null)) {

										picFilename = CardMd5.GetMD5Code(String
												.valueOf(System
														.currentTimeMillis()))
												+ nameClass.getStuNo();
										// picFilename
										// =Long.toString(physicsno);

										pictureFile = new File(
												Environment
														.getExternalStorageDirectory()
														+ "/baige/picFile/"
														+ picFilename + ".jpg");
										FileOutputStream outputStream = new FileOutputStream(
												pictureFile);
										outStream = new BufferedOutputStream(
												outputStream);
										outStream.write(buff, (18 + lenght),
												len - (18 + lenght));
										outStream.flush();

										nameClass.setPath(Environment
												.getExternalStorageDirectory()
												+ "/baige/picFile/"
												+ picFilename + ".jpg");
									}
								}
								picok = false;
								j++;
							} else {
								outStream.write(buff, 0, len);
								outStream.flush();

							}
						}

					}
					Log.e("跳出来", 555 + "");
					if (nameClass != null) {
						if (nameClass.getTemp() && nameClass.getPath() == null) {
							mHandler.sendEmptyMessage(88);
							String path = Environment
									.getExternalStorageDirectory()
									+ "/baige/picFile/"
									+ CardMd5.GetMD5Code(String.valueOf(System
											.currentTimeMillis()))
									+ nameClass.getStuNo() + ".jpg";
							if (new File(getDir() + "/baige/LOGOFile/1.jpg")
									.exists()) {
								new OSSSample().copyFile(getDir()
										+ "/baige/LOGOFile/1.jpg", path);
								nameClass.setPath(path);

								while (!new File(nameClass.getPath()).exists()) {

								}
								Message message1 = new Message();
								Bundle bundle = new Bundle();
								bundle.putString("name", nameClass.getName());
								bundle.putString("classname",
										nameClass.getClasses());
								bundle.putInt("type", nameClass.getType());
								System.out.println("刷卡人类型");
								System.out.println(nameClass.getType());

								message1.setData(bundle);
								message1.what = 1;
								MainActivity.this.handler.sendMessage(message1);

								Message message = new Message();
								message.what = 1;
								Bundle bundle1 = new Bundle();
								bundle1.putInt("type", nameClass.getType());
								bundle1.putLong("cardno", nameClass.getCardno());
								bundle1.putString("File", nameClass.getPath());
								message.setData(bundle1);
								MainActivity.this.upHandler
										.sendMessage(message);
								if (nameClass.getType() == 8) {
									dbManagercard.deletePre(Long
											.toString(nameClass.getCardno()));
								}
							}
							temp = false;

						} else {
							temp = false;
						}
					} else {
						temp = false;
					}

					if ((len == -1 && l > 0) || (len == -1 && j > 0)) {
						if (nameClass != null) {
							if (nameClass.getPath() != null) {
								if (new File(nameClass.getPath()).exists()) {
									if (isNoImage(nameClass.getPath())) {
										new OSSSample().copyFile(getDir()
												+ "/baige/LOGOFile/1.jpg",
												nameClass.getPath());
										while (!new File(nameClass.getPath())
												.exists()) {

										}
										if (nameClass.getTemp()) {
											Message message1 = new Message();
											Bundle bundle = new Bundle();
											bundle.putString("name",
													nameClass.getName());
											bundle.putString("classname",
													nameClass.getClasses());
											message1.setData(bundle);
											message1.what = 1;
											MainActivity.this.handler
													.sendMessage(message1);
											Message message = new Message();
											message.what = 1;
											Bundle bundle1 = new Bundle();
											bundle1.putInt("type",
													nameClass.getType());
											bundle1.putLong("cardno",
													nameClass.getCardno());
											bundle1.putString("File",
													nameClass.getPath());
											message.setData(bundle1);
											MainActivity.this.upHandler
													.sendMessage(message);

										}

										temp = false;
									} else {
										if (nameClass.getTemp()) {
											Message message1 = new Message();
											Bundle bundle = new Bundle();
											bundle.putString("name",
													nameClass.getName());
											bundle.putString("classname",
													nameClass.getClasses());
											bundle.putInt("type",
													nameClass.getType());
											System.out.println("刷卡人类型");
											System.out.println(nameClass
													.getType());

											message1.setData(bundle);
											message1.what = 1;

											MainActivity.this.handler
													.sendMessage(message1);
											Message message = new Message();
											message.what = 1;
											Bundle bundle1 = new Bundle();
											bundle1.putInt("type",
													nameClass.getType());
											bundle1.putLong("cardno",
													nameClass.getCardno());
											bundle1.putString("File",
													nameClass.getPath());
											message.setData(bundle1);
											MainActivity.this.upHandler
													.sendMessage(message);

										}

										temp = false;
									}
								} else {
									new OSSSample().copyFile(getDir()
											+ "/baige/LOGOFile/1.jpg",
											nameClass.getPath());
									while (!new File(nameClass.getPath())
											.exists()) {

									}
									if (nameClass.getTemp()) {
										Message message1 = new Message();
										Bundle bundle = new Bundle();
										bundle.putString("name",
												nameClass.getName());
										bundle.putString("classname",
												nameClass.getClasses());
										message1.setData(bundle);
										message1.what = 1;
										MainActivity.this.handler
												.sendMessage(message1);
										Message message = new Message();
										message.what = 1;
										Bundle bundle1 = new Bundle();
										bundle1.putInt("type",
												nameClass.getType());
										bundle1.putLong("cardno",
												nameClass.getCardno());
										bundle1.putString("File",
												nameClass.getPath());
										message.setData(bundle1);
										MainActivity.this.upHandler
												.sendMessage(message);

									}
								}
							}
						}
					}
				}
				result = null;
				if (br != null) {
					br.close();
					br = null;
				}
				if (outStream != null) {
					outStream.close();
					outStream = null;
				}
				if (server_out != null) {
					server_out.close();
					server_out = null;
				}
				if (server_socket != null) {
					server_socket.close();
					server_socket = null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if (out) {
					result = null;
					try {
						if (br != null) {
							br.close();
							br = null;
						}
						if (outStream != null) {
							outStream.close();
							outStream = null;
						}
						if (server_out != null) {
							server_out.close();
							server_out = null;
						}
						if (server_socket != null) {
							server_socket.close();
							server_socket = null;
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (nameClass != null) {
						if (nameClass.getTemp()) {
							mHandler.sendEmptyMessage(99);
							if (nameClass.getPath() != null) {
								if (new File(nameClass.getPath()).exists()) {
									if (isNoImage(nameClass.getPath())) {
										new OSSSample().copyFile(getDir()
												+ "/baige/LOGOFile/1.jpg",
												nameClass.getPath());
										while (!new File(nameClass.getPath())
												.exists()) {

										}
										Message message1 = new Message();
										Bundle bundle = new Bundle();
										bundle.putString("name",
												nameClass.getName());
										bundle.putString("classname",
												nameClass.getClasses());
										message1.setData(bundle);
										message1.what = 1;
										MainActivity.this.handler
												.sendMessage(message1);
										Message message = new Message();
										message.what = 1;
										Bundle bundle1 = new Bundle();
										bundle1.putInt("type",
												nameClass.getType());
										bundle1.putLong("cardno",
												nameClass.getCardno());
										bundle1.putString("File",
												nameClass.getPath());
										message.setData(bundle1);
										MainActivity.this.upHandler
												.sendMessage(message);
									} else {
										Message message1 = new Message();
										Bundle bundle = new Bundle();
										bundle.putString("name",
												nameClass.getName());
										bundle.putString("classname",
												nameClass.getClasses());
										bundle.putInt("type",
												nameClass.getType());
										System.out.println("刷卡人类型");
										System.out.println(nameClass.getType());

										message1.setData(bundle);
										message1.what = 1;

										MainActivity.this.handler
												.sendMessage(message1);
										Message message = new Message();
										message.what = 1;
										Bundle bundle1 = new Bundle();
										bundle1.putInt("type",
												nameClass.getType());
										bundle1.putLong("cardno",
												nameClass.getCardno());
										bundle1.putString("File",
												nameClass.getPath());
										message.setData(bundle1);
										MainActivity.this.upHandler
												.sendMessage(message);

									}
								} else {
									new OSSSample().copyFile(getDir()
											+ "/baige/LOGOFile/1.jpg",
											nameClass.getPath());
									while (!new File(nameClass.getPath())
											.exists()) {

									}
									if (nameClass.getTemp()) {
										Message message1 = new Message();
										Bundle bundle = new Bundle();
										bundle.putString("name",
												nameClass.getName());
										bundle.putString("classname",
												nameClass.getClasses());
										message1.setData(bundle);
										message1.what = 1;
										MainActivity.this.handler
												.sendMessage(message1);
										Message message = new Message();
										message.what = 1;
										Bundle bundle1 = new Bundle();
										bundle1.putInt("type",
												nameClass.getType());
										bundle1.putLong("cardno",
												nameClass.getCardno());
										bundle1.putString("File",
												nameClass.getPath());
										message.setData(bundle1);
										MainActivity.this.upHandler
												.sendMessage(message);

									}
								}
							} else {
								String path = Environment
										.getExternalStorageDirectory()
										+ "/baige/picFile/"
										+ CardMd5.GetMD5Code(String
												.valueOf(System
														.currentTimeMillis()))
										+ nameClass.getStuNo() + ".jpg";
								if (new File(getDir() + "/baige/LOGOFile/1.jpg")
										.exists()) {
									new OSSSample().copyFile(getDir()
											+ "/baige/LOGOFile/1.jpg", path);
									nameClass.setPath(path);
									while (!new File(nameClass.getPath())
											.exists()) {

									}
									Message message1 = new Message();
									Bundle bundle = new Bundle();
									bundle.putString("name",
											nameClass.getName());
									bundle.putString("classname",
											nameClass.getClasses());
									message1.setData(bundle);
									message1.what = 1;
									MainActivity.this.handler
											.sendMessage(message1);

									Message message = new Message();
									message.what = 1;
									Bundle bundle1 = new Bundle();
									bundle1.putInt("type", nameClass.getType());
									bundle1.putLong("cardno",
											nameClass.getCardno());
									bundle1.putString("File",
											nameClass.getPath());
									message.setData(bundle1);
									MainActivity.this.upHandler
											.sendMessage(message);
									if (nameClass.getType() == 8) {
										dbManagercard
												.deletePre(Long
														.toString(nameClass
																.getCardno()));
									}
								}
							}

						}
					}
					out = false;
				}
			}

			// TODO Auto-generated method stub
		}

	}

	private NameClass card_client(OutputStream server_out, byte card_data[]) {

		NameClass nameClass = null;
		StringBuilder sMsg3 = new StringBuilder();
		boolean temp = true;
		try {
			byte[] bRec = null;
			bRec = new byte[card_data.length];
			for (int i = 0; i < card_data.length; i++) {
				bRec[i] = card_data[i];
			}
			sMsg3.append(MyFunc.ByteArrToHex(bRec));
			Log.e("--sMsg3--", sMsg3.toString());
			System.out.println(sMsg3.toString());

			String frcardno_datd = sMsg3.substring(sMsg3.indexOf("3A") + 2,
					sMsg3.lastIndexOf("3A"));
			System.out.println("frcardno_datd[i]" + frcardno_datd);
			System.out.println("frcardno_datd[i].length()"
					+ frcardno_datd.length());
			if (frcardno_datd.length() == 31) {
				String frcardno = MainActivity.fromCardno(frcardno_datd
						.substring(12, frcardno_datd.length() - 7));
				System.out.println(frcardno);
				nameClass = server_read(frcardno);
			} else if (frcardno_datd.length() > 31) {
				String str = frcardno_datd.substring(0, 31);
				String frcardno = MainActivity.fromCardno(str.substring(12,
						str.length() - 7));
				System.out.println(frcardno);
				nameClass = server_read(frcardno);
			}
			sMsg3 = new StringBuilder();
			String message = null;
			if (card_count == 0) {
				if (nameClass != null) {
					if (nameClass.getName() != null) {
						// 判断有没有自定义的刷卡时间
						if (timeIdentify == 0) {
							message = "cardno:" + nameClass.getCardno()
									+ ":contentok:" + nameClass.getClasses()
									+ nameClass.getName() + ":end";
							temp = true;
							nameClass.setTemp(temp);
							Log.v("tt", "正常刷卡時間");

						} else {
							message = "cardno:" + nameClass.getCardno()
									+ ":contentagain:end";
							temp = false;
							nameClass.setTemp(temp);
							Log.v("tt", "不在刷卡时间内");
						}
					} else {
						Log.v("tt", "无效卡");

						message = "cardno:" + nameClass.getCardno()
								+ ":contentbad:end";
						temp = false;
						nameClass.setTemp(temp);
					}
				}
			} else {
				message = "cardno:" + nameClass.getCardno()
						+ ":contentagain:end";
				temp = false;
				nameClass.setTemp(temp);
			}
			if (message != null) {
				server_out.write(message.getBytes("UTF-8"));
				// System.out.println(message);
			}
			server_out.flush();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nameClass;
	}

	private NameClass server_read(String read_string) {
		long physicsno = 0;

		System.out.println(read_string + read_string.length());

		NameClass nameClass = new NameClass();

		physicsno = Long.parseLong(read_string, 16);

		nameClass.setCardno(tt - physicsno);

		dbManagercard.openDB();

		if (!(Long.toString(tt - physicsno).equals(dbManagercard.query(Long
				.toString(tt - physicsno))))) {

			Log.e("MainActivity", physicsno + "");
			card_count = 0;
			Stu stu = dbManagerstu.query(nameClass.getCardno() + "");
			nameClass.setType(stu.getType());
			nameClass.setName(stu.getName());
			nameClass.setClasses(stu.getClassname());
			// System.out.println(server_class);
			if (nameClass.getName() != null) {
				// type=8是学生 插入数据库限制重复刷卡 如果不是学生，就不限制
				if (nameClass.getType() == 8) {
					dbManagercard.insert(Long.toString(nameClass.getCardno()));

					System.out.println("server_name = " + nameClass.getName()
							+ " server_class = " + nameClass.getClasses());
				}
			} else if (nameClass.getName() == null) {
				RecOneCard recOneCard = new RecOneCard();
				stu = recOneCard.receiveDate(physicsno);
				nameClass.setType(stu.getType());
				nameClass.setName(stu.getName());
				nameClass.setClasses(stu.getClassname());
				if (nameClass.getName() != null) {
					// type=8是学生 插入数据库限制重复刷卡 如果不是学生，就不限制
					if (nameClass.getType() == 8) {
						dbManagercard.insert(Long.toString(nameClass
								.getCardno()));

						System.out.println("server_name = "
								+ nameClass.getName() + " server_class = "
								+ nameClass.getClasses());
					}
				} else if (nameClass.getName() == null) {
					nameClass.setCardno(0);
					nameClass.setName(null);
					nameClass.setClasses(null);
				}
			}

		} else {
			card_count = 1;
		}
		physicsno = 0;
		return nameClass;
	}

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + 1;
	}

	/**
	 * 通过读取文件并获取其width及height的方式，来判断判断当前文件是否图片，这是一种非常简单的方式。
	 * 
	 * @param imageFile
	 * @return
	 */
	public static boolean isNoImage(String imagepath) {
		//
		Bitmap bitmap = BitmapFactory.decodeFile(imagepath);

		try {
			if (bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
				return true;
			}
			BufferedOutputStream stream;
			stream = new BufferedOutputStream(new FileOutputStream(new File(
					imagepath)));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
			return false;
		} catch (Exception e) {
			return false;
		} finally {
			if (bitmap != null) {
				bitmap.recycle();
			}
			bitmap = null;
		}
	}

	public void transImage(String fromFile, String toFile, int width,
			int height, int quality) {
		try {
			Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
			int bitmapWidth = bitmap.getWidth();
			int bitmapHeight = bitmap.getHeight();
			// 缩放图片的尺寸
			float scaleWidth = (float) width / bitmapWidth;
			float scaleHeight = (float) height / bitmapHeight;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);

			// 产生缩放后的Bitmap对象
			Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmapWidth, bitmapHeight, matrix, false);
			// save file
			File myCaptureFile = new File(toFile);
			FileOutputStream out = new FileOutputStream(myCaptureFile);
			if (resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
				out.flush();
				out.close();
			}
			if (!bitmap.isRecycled()) {
				bitmap.recycle();// 记得释放资源，否则会内存溢出
			}
			if (!resizeBitmap.isRecycled()) {
				resizeBitmap.recycle();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public boolean fileIsExists(String file) {
		try {
			File f = new File(file);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@SuppressLint("DefaultLocale")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList GetFileName() {
		ArrayList<String> vector = new ArrayList<String>();
//		Cursor c = dbReader.query("allpaths", null, null, null, null, null,
//				null);
		Cursor c  = dbReader.rawQuery("select alluploadpaths from allpaths limit 0,10", null);
		while (c.moveToNext()) {   
			vector.add(c.getString(c.getColumnIndex("alluploadpaths")));  
		}
		c.close();
		return vector;
	}

	@SuppressLint("DefaultLocale")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector GetFileName1(String fileAbsolutePath, String form) {
		Vector vecFile = new Vector();
		File file = new File(fileAbsolutePath);
		if (file.exists() && file.isDirectory()) {
			if (file.listFiles().length > 0) {
				File[] subFile = file.listFiles();
				for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
					// 判断是否为文件夹
					if (!subFile[iFileLength].isDirectory()) {
						String filename = subFile[iFileLength].getName();
						// 判断是否为MP4结尾
						if (filename.trim().toLowerCase().endsWith(form)) {
							vecFile.add(filename);
						}
					}
				}
			}
		}
		return vecFile;
	}

	// ----------------------------------------------------获取文件夹
	public File getDir() {
		// 得到SD卡根目录
		File dir = Environment.getExternalStorageDirectory();
		if (dir.exists()) {
			return dir;
		} else {
			dir.mkdirs();
			return dir;
		}
	}

	// @Override
	// public void setText(String content) {
	// textConnect.setText(content);
	// }

	public static String fromCardno(String hexString) {
		hexString = hexString.trim();
		String s[] = hexString.split(" ");
		StringBuilder builder = new StringBuilder();
		for (int i = s.length - 1; i >= 0; i--) {
			builder.append(s[i]);
		}
		return builder.toString();
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			if (upallcount > allcount) {
				upallcount = allcount;
			}
			if (intent.getAction().equals(action)) {
				textAAA.setText("上传数 : " + upallcount);
			}
		}
	};

	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			// tv_title.setText(titles[position]);
			// dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			// dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	// MP4分辩率：720*480
	private void initView() {
		advPager = (ViewPager) findViewById(R.id.adv_pager);
		// 这里存放的是四张广告背景
		advPics = new ArrayList<View>();
		img1 = new ImageView(this);
		img2 = new ImageView(this);
		img3 = new ImageView(this);
		img4 = new ImageView(this);
		// 添加视频
		videoView = new FullScreenVideoView(this, advPager);
	}

	// ----------------------------------------------------广告
	@SuppressLint("ClickableViewAccessibility")
	private void initViewPager(String mFilePath) {

		switch (advert) {
		case 0:
			initView();
			Log.v("initViewPager()", "case 0");
			img1.setBackgroundResource(R.drawable.first);
			img2.setBackgroundResource(R.drawable.second);
			img3.setBackgroundResource(R.drawable.third);
			img4.setBackgroundResource(R.drawable.four);
			advPics.add(img1);
			advPics.add(img2);
			advPics.add(img3);
			advPics.add(img4);
			break;
		case 1:
			Log.e("initViewPager()  case1", mFilePath);
			initView();
			if (advPics.size() > 0) {
				advPics.clear();
			}
			// videoView.canPause();
			File file = new File(mFilePath);
			if (file.exists()) {
				if (file.isDirectory()) {
					File files[] = file.listFiles();
					if (files.length == 4) {
						for (int i = 0; i < files.length; i++) {
							Log.e("图片路径", files[i].getPath());
							if (files[i].length() / 1024 > 180) {
								if (mFilePath != null) {
									transImage(files[i].getPath(),
											files[i].getPath(), 1366, 768, 40);
								}
							}
							if (i == 0) {
								img1.setImageURI(Uri.fromFile(files[i]));
								advPics.add(img1);
							}
							if (i == 1) {
								img2.setImageURI(Uri.fromFile(files[i]));
								advPics.add(img2);
							}
							if (i == 2) {
								img3.setImageURI(Uri.fromFile(files[i]));
								advPics.add(img3);
							}
							if (i == 3) {
								img4.setImageURI(Uri.fromFile(files[i]));
								advPics.add(img4);
							}
						}
					}
				}
				// if (file.exists()) {
				// if (i == 0) {
				// img1.setImageURI(Uri.fromFile(file));
				// advPics.add(img1);
				// }
				// if (i == 1) {
				// img2.setImageURI(Uri.fromFile(file));
				// advPics.add(img2);
				// }
				// if (i == 2) {
				// img3.setImageURI(Uri.fromFile(file));
				// advPics.add(img3);
				// }
				// if (i == 3) {
				// img4.setImageURI(Uri.fromFile(file));
				// advPics.add(img4);
				// }
				// }
			}

			@SuppressWarnings("rawtypes")
			Vector filename = GetFileName1(
					getDir() + "/baige/advertVideoFile/", ".mp4");
			if (filename.size() != 0) {
				videoView.setVideoPath(getDir() + "/baige/advertVideoFile/"
						+ filename.get(0).toString());
				advPics.add(videoView);
			}
			break;
		default:
			break;
		}
		// 小图标
		advPager.setAdapter(new AdvAdapter(advPics));
		advPager.setOnPageChangeListener(new GuidePageChangeListener());
		advPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					isContinue = false;
					break;
				case MotionEvent.ACTION_UP:
					isContinue = true;
					break;
				default:
					isContinue = true;
					break;
				}
				return false;
			}
		});
		play = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (isContinue) {
						viewHandler.sendEmptyMessage(what1.get());
						whatOption();
					}
				}
			}
		});
		play.start();
	}

	/*---------------------------------------学校图片轮播------------------------------------*/
	/*------------------------------------------------------------------------------------*/
	/*------------------------------------------------------------------------------------*/
	private void school_initviewpager() {

		school_advPager = (ViewPager) findViewById(R.id.vp);
		school_advPics = new ArrayList<View>();
		File file;
		Vector reup = GetFileName1(getDir()
				+ "/baige/campusPicFile/campusPic0/", ".jpg");
		if (reup.size() != 0) {
			for (int i = 0; i < reup.size(); i++) {
				String file_string = Environment.getExternalStorageDirectory()
						+ "/baige/campusPicFile/campusPic0/" + i + ".jpg";
				file = new File(file_string);
				if (i == 0) {
					img5 = new ImageView(this);
					img5.setImageURI(Uri.fromFile(file));
					school_advPics.add(img5);
				}
				if (i == 1) {
					img6 = new ImageView(this);
					img6.setImageURI(Uri.fromFile(file));
					school_advPics.add(img6);
				}
				if (i == 2) {
					img7 = new ImageView(this);
					img7.setImageURI(Uri.fromFile(file));
					school_advPics.add(img7);
				}
				if (i == 3) {
					img8 = new ImageView(this);
					img8.setImageURI(Uri.fromFile(file));
					school_advPics.add(img8);
				}
			}
		} else {
			img5 = new ImageView(this);
			img5.setImageDrawable(getResources().getDrawable(
					R.drawable.school_0));
			school_advPics.add(img5);
		}

		school_advPager.setAdapter(new AdvAdapter(school_advPics));
		school_advPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	@SuppressLint("HandlerLeak")
	private final Handler viewHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 设置显示
			advPager.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}
	};

	private void whatOption() {
		if (what1.get() < 5) {
			what1.incrementAndGet();
		}
		// 自动播放视频
		if (what1.get() == 5) {
			Vector filename = GetFileName1(
					getDir() + "/baige/advertVideoFile/", ".mp4");

			if (filename.size() > 0) {

				System.out.println("视频个数" + filename.size());
				System.out.println("第一个视频文件名" + filename.get(0).toString());

				videoView.start();
				// 视频播放完成监听
				videoView
						.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								// advPics.remove(videoView);

								System.out.println("视频监听，播放成功");

								Vector filename = GetFileName1(getDir()
										+ "/baige/advertVideoFile/", ".mp4");
								if (videoNum == filename.size()) {
									videoView.stopPlayback();
									videoView.setVideoPath(getDir()
											+ "/baige/advertVideoFile/"
											+ filename.get(0).toString());
									what1 = new AtomicInteger(0);
									videoNum = 1;
									synchronized (play) {
										play.notify();
									}
								} else {
									if (filename.size() != 0) {
										videoView.setVideoPath(getDir()
												+ "/baige/advertVideoFile/"
												+ filename.get(videoNum)
														.toString());
										videoView.requestFocus();
										videoView.start();
									}
									videoNum++;
								}
							}
						});
				// 视频播放错误监听
				videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						Log.e("视频播放中出错", extra + "");
						Vector filename = GetFileName1(getDir()
								+ "/baige/advertVideoFile/", ".mp4");
						videoView.stopPlayback();
						videoView.setVideoPath(getDir()
								+ "/baige/advertVideoFile/"
								+ filename.get(0).toString());
						what1 = new AtomicInteger(0);
						videoNum = 1;
						synchronized (play) {
							play.notify();
						}

						return true;
					}
				});
				if (play != null) {
					synchronized (play) {
						try {
							play.wait();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				what1 = new AtomicInteger(0);
			}
		}
		// else if (what1.get() > imageViews.length - 1) {
		// what1.getAndAdd(-6);
		// }
		try {
			if (what1.get() < 5) {
				play.sleep(5000);
			}
		} catch (InterruptedException e) {

		}
	}

	// 切换当前显示的图片
	private Handler school_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			school_advPager.setCurrentItem(currentItem);// 切换当前显示的图片
			Vector reup1 = GetFileName1(getDir()
					+ "/baige/campusPicFile/campusPic1/", ".jpg");
			if (reup1.size() != 0 && reup1.size() >= (currentItem + 1)) {
				File file1 = new File(Environment.getExternalStorageDirectory()
						+ "/baige/campusPicFile/campusPic1/" + currentItem
						+ ".jpg");
				infoimg1.setImageURI(Uri.fromFile(file1));
			} else {
				infoimg1.setImageDrawable(getResources().getDrawable(
						R.drawable.school0));
			}
			Vector reup2 = GetFileName1(getDir()
					+ "/baige/campusPicFile/campusPic2/", ".jpg");
			if (reup2.size() != 0 && reup2.size() >= (currentItem + 1)) {
				File file2 = new File(Environment.getExternalStorageDirectory()
						+ "/baige/campusPicFile/campusPic2/" + currentItem
						+ ".jpg");
				infoimg2.setImageURI(Uri.fromFile(file2));
			} else {
				infoimg2.setImageDrawable(getResources().getDrawable(
						R.drawable.school1));
			}
			Vector reup3 = GetFileName1(getDir()
					+ "/baige/campusPicFile/campusPic3/", ".jpg");
			if (reup3.size() != 0 && reup3.size() >= (currentItem + 1)) {
				File file3 = new File(Environment.getExternalStorageDirectory()
						+ "/baige/campusPicFile/campusPic3/" + currentItem
						+ ".jpg");
				infoimg3.setImageURI(Uri.fromFile(file3));
			} else {
				infoimg3.setImageDrawable(getResources().getDrawable(
						R.drawable.school2));
			}
		};
	};

	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (school_advPager) {
				currentItem = (currentItem + 1) % school_advPics.size();
				school_handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

	}

	// 广告pager适配器
	private final class AdvAdapter extends PagerAdapter {
		private List<View> views = null;

		public AdvAdapter(List<View> views) {
			this.views = views;
		}

		@Override
		public void destroyItem(View view, int arg1, Object object) {
			((ViewPager) view).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View view) {
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(View view, int arg1) {
			((ViewPager) view).addView(views.get(arg1), 0);
			return views.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View view, Object arg1) {
			return view == arg1;
		}

		@Override
		public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View view) {
		}

	}

	private final class GuidePageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			what1.getAndSet(arg0);
		}
	}

	private void playPicorVid() {
		// 定时请求播放视频
		String srcPath = getDir() + "/baige/advertPicFile/";
		File mFile = new File(srcPath);
		GetFileNum gf = new GetFileNum();
		int ll = gf.getAllFilesNum(mFile);
		if (ll > 0) {
			timeName = new RecAdvert().isplay();
			if (timeName != null) {
				if (gf.getAllFilesNum(new File(timeName)) == 4) {
					// 打开视频播放
					Log.e("come", "进来了");
					// numPic = 1;
					advert = 1;
					Message msg_pic = new Message();
					msg_pic.what = 99;
					Bundle bSrcPath = new Bundle();
					bSrcPath.putString("srcpath", timeName);
					msg_pic.setData(bSrcPath);
					mHandler.sendMessage(msg_pic);
				} else {
					DeleteFilePic.delete(new File(timeName));
					dBManageradvert.deleteDir(timeName.substring(
							timeName.lastIndexOf("/") + 1, timeName.length()));
					timeName = "null";
					Message msg_pic = new Message();
					msg_pic.what = 99;
					mHandler.sendMessage(msg_pic);
				}
			} else {
				Message msg_pic = new Message();
				msg_pic.what = 99;
				mHandler.sendMessage(msg_pic);
			}
		} else {
			Message msg_pic = new Message();
			msg_pic.what = 99;
			mHandler.sendMessage(msg_pic);
		}
	}

	private ArrayList<String> queryRecode(String val) {
		ArrayList<String> list = new ArrayList<String>();
		System.out.println(val);
		String cardq = "c";
		String type = "b";
		String time = "d";
		String[] columns = { "type", "cardno", "timecode" };
		String[] selectionArgs = { val };
		Cursor c = dbReader.query("allpaths", columns, "alluploadpaths=?",
				selectionArgs, null, null, null);
		while (c.moveToNext()) {
			cardq = c.getString(c.getColumnIndex("cardno"));
			type = c.getString(c.getColumnIndex("type"));
			time = c.getString(c.getColumnIndex("timecode"));
		}
		c.close();
		list.add(String.valueOf(cardq));
		list.add(String.valueOf(type));
		list.add(String.valueOf(time));
		return list;
	}

}

package com.hyc.baige;

import java.util.List;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.hyc.qidong.Reflesh;

public class BroadCastReceiver extends BroadcastReceiver{
	static final String action_boot="android.intent.action.BOOT_COMPLETED";
	private Context context;
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		this.context = context;
		if (intent.getAction().equals(action_boot)){
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
			        if(getAppSatus(context, "com.hyc.baige")!=1){
			        	Intent ootStartIntent=new Intent(context,Reflesh.class);
	                    ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                    context.startActivity(ootStartIntent);	
			        }
				}
			}, 60000);
        }
	}
	
	private int getAppSatus(Context context, String pageName) {  
		  
	    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  
	    List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(20);  
	  
	    //判断是否在栈顶
	    if (list.get(0).topActivity.getPackageName().equals(pageName)) {  
	        return 1;  
	    } else {  
	        //判断程序是否在栈里
	        for (ActivityManager.RunningTaskInfo info : list) {  
	            if (info.topActivity.getPackageName().equals(pageName)) {  
	                return 1;  
	            }  
	        }  
	        return 3;//栈里找不到 返回3 
	    }  
	}  

}

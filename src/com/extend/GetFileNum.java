package com.extend;

import java.io.File;
import java.util.ArrayList;

import android.os.Environment;

public class GetFileNum {

	private ArrayList<String> list =new ArrayList<String>();

	public  ArrayList<String> getFiles(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					getFiles(files[i]);
				} else {
					list.add(files[i].getName());
				}
			}
		}
		return list;
	}
	//获取第一个子文件的文件名
   public String getFilesName(File root){  
		File files[] = root.listFiles();  	
    if(files != null){  
        for (File f : files){  
            if(f.isDirectory()){  
            	getFilesName(f);
            }else{  
            }  
        }  
      }
	return files[0].getName();
    }
   
   //获取当前文件夹下子文件个数
   public int getAllFilesNum(File root){  
		File files[] = root.listFiles();  	
    if(files != null){  
        for (File f : files){  
            if(f.isDirectory()){  
            	getAllFilesNum(f);
            }else{  
            }  
        }  
        return files.length;
      }
	return 0;
    }
   
   //创建baige文件夹  防止数据库报错
   public void creadFile0(){
	   
	   String pic_url = getDir() + "/baige/db";
		File pic_file = new File(pic_url);
		if (pic_file.exists()) {
		} else {
			pic_file.mkdirs();
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
   
}

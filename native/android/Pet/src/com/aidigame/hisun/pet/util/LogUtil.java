package com.aidigame.hisun.pet.util;

import android.util.Log;

public class LogUtil {
	public static boolean isDebug=true;
	public static void i(String tag,String info){
		if(isDebug){
			Log.i(tag, info);
		}
	}

}

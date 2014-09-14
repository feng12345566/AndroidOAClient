package com.mct.util;

import com.mct.client.R;

import android.app.Activity;

public class MyAnimationUtils {
   public static void inActivity(Activity activity){
	   activity.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
   }
   
   public static void outActivity(Activity activity){
	   activity.overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
   }
}

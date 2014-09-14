package com.mct.util;

import java.io.File;

import com.mct.controls.ChatMessageAdapter;
import com.mct.controls.ChatMessageAdapter.ViewHolder;
import com.mct.controls.ConversationListAdapter;
import com.mct.controls.MyExpandableListAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

public class ImageUtil {
	private Context context;
	private static ImageUtil imageUtil;
	private Drawable drawable = null;
	private String filePath;

	private ImageUtil(Context context) {
		this.context = context;
	}

	public static ImageUtil shareImageUtil(Context context) {
		if (imageUtil == null) {
			imageUtil = new ImageUtil(context);
		}
		return imageUtil;

	}

	public Drawable getImageById(String userId, int imageId, boolean isDown) {
		String SdPath = FileUtil.getSDPath();
		filePath = SdPath + "/mobileOA/image/";
		if (SdPath != null) {
			File file = new File(filePath + userId + ".png");
			if (file.exists()) {
				drawable = BitmapDrawable.createFromPath(filePath);
			} else {
				drawable = context.getResources().getDrawable(imageId);
				if (isDown) {
					FileUtil.mkdir(filePath);
					Intent intent = new Intent();
					intent.putExtra("userid", userId);
					intent.setAction(Common.ACTION_HEADIMAGE);
					context.sendBroadcast(intent);
				}

			}
		} else {
			drawable = context.getResources().getDrawable(imageId);
		}
		return drawable;
	}

}

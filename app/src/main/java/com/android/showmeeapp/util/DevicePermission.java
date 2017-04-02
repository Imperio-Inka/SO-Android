package com.android.showmeeapp.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.showmeeapp.constant.Constant;

import java.io.File;

/**
 * Created by admin on 01/08/16.
 */
public class DevicePermission extends AppCompatActivity {
	public static final int MY_PERMISSIONS = 100;
	private Activity act;

	public DevicePermission() {

	}

	public DevicePermission(Activity activity) {
		this.act = activity;
		checkPermissions();
	}

	boolean checkPermissions() {
		if (ContextCompat.checkSelfPermission(act, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
				ContextCompat.checkSelfPermission(act, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
				ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
				ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
				ActivityCompat.checkSelfPermission(act, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
				ActivityCompat.checkSelfPermission(act, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
				ContextCompat.checkSelfPermission(act, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(act,
					new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, android.Manifest.permission.CAMERA},
					MY_PERMISSIONS);
		} else {
			createDirectry();
		}
		return false;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED && grantResults[4] == PackageManager.PERMISSION_GRANTED && grantResults[5] == PackageManager.PERMISSION_GRANTED && grantResults[6] == PackageManager.PERMISSION_GRANTED) {
					createDirectry();
					System.out.println("Permission Granted");
				} else {
					System.out.println("Permission Denied");
					finish();
				}
				return;
			}
		}
	}

	//////////////////////// Create Directry ////////////////////////////////////

	void createDirectry() {
		Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (isSDPresent) {
			File dir = new File(Constant.DATA_PATH);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v("", "ERROR: Creation of directory " + Constant.DATA_PATH + " on sdcard failed");
					return;
				} else {
					Log.v("", "Created directory " + Constant.DATA_PATH + " on sdcard");
				}
			}
		} else {
			File mydir = new File(Environment.getDataDirectory() + "/" + Constant.FOLDAR_NAME + "/");
			if (!mydir.exists()) {
				mydir.mkdirs();
			}
		}
	}

}

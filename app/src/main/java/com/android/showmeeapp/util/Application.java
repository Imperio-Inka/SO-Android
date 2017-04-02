package com.android.showmeeapp.util;


import android.content.Context;
import android.support.multidex.MultiDex;

public class Application extends android.support.multidex.MultiDexApplication {

	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}

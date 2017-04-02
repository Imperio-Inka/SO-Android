package com.android.showmeeapp.ui.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.showmeeapp.R;
import com.android.showmeeapp.constant.Constant;
import com.android.showmeeapp.gcm.CommonGCMUtility;
import com.android.showmeeapp.gcm.WakeLocker;
import com.android.showmeeapp.util.ConnectionDetector;
import com.android.showmeeapp.util.PreferenceClass;
import com.android.showmeeapp.util.ProgressHUD;
import com.google.android.gcm.GCMRegistrar;

import java.util.ArrayList;
import java.util.List;


public class SMLoginActivity extends Activity implements View.OnClickListener {
	private Context mContext;
	private ProgressHUD progress;
	private ConnectionDetector cd;
	private String item = "";
	private String registrationId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smlogin);
		mContext = this;

		if(PreferenceClass.getBooleanPreferences(mContext,"AtDrawerScreen")){
			startActivity(new Intent(this, DrawerHomeActivity.class));
			finish();
		}else if(PreferenceClass.getBooleanPreferences(mContext,"AtCaledar")){
			startActivity(new Intent(this, CalendarListActivity.class));
			finish();
		}else{
			cd = new ConnectionDetector(mContext);
			init();
			setSpinner();
		}
	}

	private void init() {
		((Button) findViewById(R.id.btn_login_google)).setOnClickListener(this);
		((Button) findViewById(R.id.btn_Get_started)).setOnClickListener(this);
		//((EditText) findViewById(R.id.edt_School_Name)).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_login_google:
				if (cd.isConnectingToInternet()) {
					if(item.equals("Select school")){
						Toast.makeText(mContext, "Please select your school first", Toast.LENGTH_SHORT).show();
					}else {
						startActivity(new Intent(this, GoogleLoginActivity.class));
						finish();
					}

				} else {
					Toast.makeText(mContext, R.string.No_Internet_connection, Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.btn_Get_started:
				if(item.equals("Select school")){
					Toast.makeText(mContext, "Please select your school first", Toast.LENGTH_SHORT).show();
				}else {
					//Toast.makeText(mContext, "In process...", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(this, DrawerHomeActivity.class);
					intent.putExtra("LOGING_TYPE", "get_started");
					startActivity(intent);
					finish();
				}
				break;

			default:
				break;
		}
	}

	private void setSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		// Spinner Drop down elements
		List<String> schoolList = new ArrayList<String>();
		schoolList.add("Select school");
		schoolList.add("Santa Clara University");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, schoolList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				item = parent.getItemAtPosition(position).toString();
				PreferenceClass.setStringPreference(mContext, Constant.SELECTED_SCHOOL, item);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
	}

	private void serverData() {
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(CommonGCMUtility.DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		registrationId = GCMRegistrar.getRegistrationId(this);

		System.out.println("Reg Id is  " + registrationId);
		PreferenceClass.setStringPreference(mContext, Constant.GCM_REGISTER_KEY, registrationId);
		// Check if regid already presents
		if (registrationId.equals("")) {
			GCMRegistrar.register(this, CommonGCMUtility.SENDER_ID);
		}
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(CommonGCMUtility.EXTRA_MESSAGE);
			System.out.println("" + newMessage);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegisterReceiverError", ">" + e.getMessage());
		}
		super.onDestroy();
	}
}

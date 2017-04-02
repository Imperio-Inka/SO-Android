package com.android.showmeeapp.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.showmeeapp.R;
import com.android.showmeeapp.constant.Constant;
import com.android.showmeeapp.ui.activities.ContactsActivity;
import com.android.showmeeapp.ui.activities.DrawerHomeActivity;
import com.android.showmeeapp.ui.activities.UpdateEventActivity;
import com.android.showmeeapp.util.PreferenceClass;
import com.android.showmeeapp.util.ProgressHUD;
import com.android.showmeeapp.util.tagcontainer.TagContainerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by kushahuja on 7/3/16.
 */
public class EventDetailsFragment extends Fragment implements View.OnClickListener {
	public static ArrayList<String> tagList = new ArrayList<>();
	private View rootView;
	private Context mContext;
	private ActionBar actionBar;
	private View mToolbarView;
	private String strJsonObject;
	SimpleDateFormat formatter;
	Calendar calendar;
	ProgressHUD progress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_event_details, container, false);
		mContext = this.getActivity();
		Bundle bundle = this.getArguments();
		strJsonObject = bundle.getString("JsonObject");
		init();

		return rootView;
	}

	private void init() {
		progress = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {

			}
		});

		setActionBar();
		calendar = Calendar.getInstance();
		formatter = new SimpleDateFormat("EEE-MMM,d yyyy'Z'h:mm a");

		((TextView) rootView.findViewById(R.id.txtFragmentTitle)).setVisibility(View.GONE);
		((TextView) rootView.findViewById(R.id.txtDetaisTitle)).setText(mContext.getResources().getString(R.string.EventDETAILS));
		((ImageView) rootView.findViewById(R.id.imgOpenDrawer)).setVisibility(View.GONE);
		((ToggleButton) rootView.findViewById(R.id.toggleButtonPublicEvent)).setEnabled(false);
		((ToggleButton) rootView.findViewById(R.id.toggleButtonInviteOther)).setEnabled(false);

		setEventDetails();
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.imgVwEdit:
				Intent intent = new Intent(mContext, UpdateEventActivity.class);
				intent.putExtra("JSONDATA", strJsonObject);
				startActivity(intent);
				break;
			case R.id.imgAddGuest:
				startActivity(new Intent(mContext, ContactsActivity.class));
				break;

			default:
				break;
		}
	}

	private void setActionBar() {
		mToolbarView = (Toolbar) rootView.findViewById(R.id.toolbar);
		((DrawerHomeActivity) getActivity()).setSupportActionBar((Toolbar) mToolbarView);
		actionBar = ((DrawerHomeActivity) getActivity()).getSupportActionBar();
		actionBar.setDisplayShowCustomEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	private void setEventDetails() {
		try {
			JSONObject jsonObject = new JSONObject(strJsonObject);
			Log.d("Event Details", " :: " + strJsonObject.toString());
			String summary = jsonObject.getString("summary");
			if (jsonObject.has("location")) {
				((TextView) rootView.findViewById(R.id.txtAtLocation)).setText(jsonObject.getString("location"));
			}
			if (jsonObject.has("description")) {
				((TextView) rootView.findViewById(R.id.txtEventDescreption)).setText(jsonObject.getString("description"));
			}
			JSONObject jObjStartTime = jsonObject.getJSONObject("start");
			JSONObject jObjEndTime = jsonObject.getJSONObject("end");
			long start = jObjStartTime.getLong("$date");
			long end = jObjEndTime.getLong("$date");
			String StartDate = getDate(start);
			String EndDate = getDate(end);
			String[] arrayStartDate = StartDate.split("Z");
			String[] arrayEndDate = EndDate.split("Z");
			String strStartDate = arrayStartDate[0];
			String strStartTime = arrayStartDate[1];
			String strEndDate = arrayEndDate[0];
			String strEndTime = arrayEndDate[1];

			((TextView) rootView.findViewById(R.id.txtEventTitle)).setText(summary);

			((TextView) rootView.findViewById(R.id.txtSetStartDate)).setText(strStartDate);
			((TextView) rootView.findViewById(R.id.txtStartTime)).setText(strStartTime);
			((TextView) rootView.findViewById(R.id.txtSetEndDate)).setText(strEndDate);
			((TextView) rootView.findViewById(R.id.txtEndTime)).setText(strEndTime);

			JSONArray jsonArray = jsonObject.getJSONArray("tags");
			tagList.clear();
			for (int i = 0; i < jsonArray.length(); i++) {
				Log.d("Tag is ", " Tags are " + jsonArray.getString(i));
				if (!(jsonArray.getString(i) == "null")) {
					tagList.add(jsonArray.getString(i));
				}
			}
			((TagContainerLayout) rootView.findViewById(R.id.tagcontainerLayout)).setTags(tagList);

			if (jsonObject.has("createdBy")) {
				if (jsonObject.getString("createdBy").equals(PreferenceClass.getStringPreferences(mContext, Constant.CURRENT_USER_ID))) {
					((ImageView) rootView.findViewById(R.id.imgVwEdit)).setVisibility(View.VISIBLE);
					((ImageView) rootView.findViewById(R.id.imgVwEdit)).setOnClickListener(this);
				}
			}
			if (jsonObject.getString("visibility").equals("public")) {
				((RelativeLayout) rootView.findViewById(R.id.relative_guest)).setVisibility(View.VISIBLE);
				((ToggleButton) rootView.findViewById(R.id.toggleButtonPublicEvent)).setChecked(true);
				((ImageView) rootView.findViewById(R.id.imgAddGuest)).setOnClickListener(this);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (progress.isShowing() && progress != null) {
			progress.dismiss();
		}
	}

	private String getDate(long milliSeconds) {
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}
}


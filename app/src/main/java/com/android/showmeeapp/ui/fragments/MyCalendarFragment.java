package com.android.showmeeapp.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.showmeeapp.R;
import com.android.showmeeapp.adapter.RecycleListAdapter;
import com.android.showmeeapp.constant.Constant;
import com.android.showmeeapp.ui.activities.DrawerHomeActivity;
import com.android.showmeeapp.util.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;


/**
 * Created by kushahuja on 7/3/16.
 */
public class MyCalendarFragment extends Fragment implements View.OnClickListener {

	private View rootView;
	private ActionBar actionBar;
	private View mToolbarView;
	private Context mContext;
	private Meteor mMeteor;
	private RecyclerView mRecyclerView;
	private RecycleListAdapter mAdapter;
	private ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_mycalendar, container, false);
		setActionBar();
		((ImageView) rootView.findViewById(R.id.imgOpenDrawer)).setOnClickListener(this);
		((TextView) rootView.findViewById(R.id.txtFragmentTitle)).setText(R.string.MyCalendar);
		mContext = this.getActivity();
		init();

		return rootView;
	}

	private void init() {
		Meteor.setLoggingEnabled(true);
		mMeteor = new Meteor(mContext, Constant.BASE_URL, new InMemoryDatabase());
		mMeteor.connect();
		getImpotedCalendar();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.imgOpenDrawer:
				DrawerHomeActivity.mDrawerLayout.openDrawer(DrawerHomeActivity.mRecyclerView);
				break;

			default:
				break;
		}
	}

	private void setActionBar() {
		mToolbarView = (Toolbar) rootView.findViewById(R.id.toolbar);
		((DrawerHomeActivity) getActivity()).setSupportActionBar((Toolbar) mToolbarView);
		actionBar = ((DrawerHomeActivity) getActivity()).getSupportActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(false);
	}

	private void getImpotedCalendar() {
		final ProgressHUD progress = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {

			}
		});

		Log.d("ALL CALENDARS ::: ","get imported calendar called.");

		mMeteor.call(Constant.METHOD_CALENDAR_LIST, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}
				try {
					JSONArray jsonArray = new JSONArray(result);
					Log.e("ALL CALENDARS ::: ",""+jsonArray);
					for (int i=0;i<jsonArray.length();i++) {
						jsonList.add(jsonArray.getJSONObject(i));
					}
					setCalanderList();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String error, String reason, String details) {
				if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}
			}
		});
	}

	private void setCalanderList() {
		RecyclerView.LayoutManager mLayoutManager;
		mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
		mRecyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(mContext);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mAdapter = new RecycleListAdapter(mContext, Constant.GET_ALL_CALENDAR, jsonList, this);
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}
}

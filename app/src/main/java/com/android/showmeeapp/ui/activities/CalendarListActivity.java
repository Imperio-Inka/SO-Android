package com.android.showmeeapp.ui.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.showmeeapp.R;
import com.android.showmeeapp.adapter.ExpandableListAdapter;
import com.android.showmeeapp.adapter.RecycleListAdapter;
import com.android.showmeeapp.constant.Constant;
import com.android.showmeeapp.util.PreferenceClass;
import com.android.showmeeapp.util.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

public class CalendarListActivity extends AppCompatActivity implements View.OnClickListener {

	public static Dialog dialog;
	public static Button btnSaveTags;
	ArrayList<String> tagsListFinal = new ArrayList<String>();
	int pos;
	boolean isStartHomeActivity = true;
	private Context mContext;
	private Meteor mMeteor;
	private ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();
	private ArrayList<JSONObject> tagList = new ArrayList<>();
	private RecyclerView mRecyclerView;
	private RecyclerView recyclerView_SchoolTags;
	private RecycleListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calander_list);
		mContext = this;
		PreferenceClass.setBooleanPreference(mContext, "AtCaledar", true);
		init();
	}

	private void init() {
		((ImageView) findViewById(R.id.imgOpenDrawer)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.txtFragmentTitle)).setText(R.string.Import_Google_calendar);
		((TextView) findViewById(R.id.txtFragmentTitle)).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_16));
		btnSaveTags = (Button) findViewById(R.id.btn_Done);
		btnSaveTags.setOnClickListener(this);
		((Button) findViewById(R.id.btnSkip)).setOnClickListener(this);

		Meteor.setLoggingEnabled(true);
		mMeteor = new Meteor(this, Constant.BASE_URL, new InMemoryDatabase());
		mMeteor.connect();

		getCalenderList();
		setDailog();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_Done:
				String[] tagArray;
				Log.e("RootjArray in activity ", RecycleListAdapter.RootjArray + "");
				if (RecycleListAdapter.RootjArray.length() == 0) {
					Toast.makeText(mContext, "Please add tags first", Toast.LENGTH_LONG).show();
				} else {
					int length = RecycleListAdapter.RootjArray.length();
					int count_loop = 0;
					String[] Ids = new String[length];
					for (int i = 0; i < RecycleListAdapter.RootjArray.length(); i++) {
						try {
							pos = i;
							count_loop = count_loop + 1;
							JSONObject rootJsonObj = RecycleListAdapter.RootjArray.getJSONObject(i);
							String calendarId = rootJsonObj.getString("id");
							JSONArray jsonArray = rootJsonObj.getJSONArray("tags");
							tagArray = new String[(jsonArray.length()) + 1];
							for (int j = 0; j < jsonArray.length(); j++) {
								JSONObject jsonObject = jsonArray.getJSONObject(j);
								String tag = jsonObject.getString("tag_title");
								tagArray[j] = tag;

							}
							Ids[i] = calendarId;
							setTagsToSever(tagArray, calendarId);

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

//					for (int k = 0; k < Ids.length; k++) {
//						calendarSyncToSever(Ids[k]);
//					}
				}

				break;
			case R.id.imgVwDone:
				if (dialog.isShowing()) {
					dialog.dismiss();
					RecycleListAdapter.TagList();
				}

				break;
			case R.id.btnSkip:
				final ProgressHUD progress = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialogInterface) {

					}
				});
				setTagsByUser(tagsListFinal, progress);

				break;

			default:
				break;
		}
	}

	private void setCalanderList() {
		RecyclerView.LayoutManager mLayoutManager;
		mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		mRecyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(mContext);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mAdapter = new RecycleListAdapter(mContext, Constant.GET_CALENDAR_LIST, jsonList, this);
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	private void setDailog() {
		dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_tag_list);
		dialog.setCancelable(true);

		recyclerView_SchoolTags = (RecyclerView) dialog.findViewById(R.id.recyclerView);
		((ImageView) dialog.findViewById(R.id.imgVwDone)).setOnClickListener(this);
		recyclerView_SchoolTags.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
		getSchoolTags();
	}

	private void getCalenderList() {
		final ProgressHUD progress = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {

			}
		});
		mMeteor.call(Constant.METHOD_CALENDAR_LIST, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}
				Log.e("CALENDER LIST  : ", result);
				try {
					JSONArray rootJsonArray = new JSONArray(result);
					for (int i = 0; i < rootJsonArray.length(); i++) {
						JSONObject jsonObject = rootJsonArray.getJSONObject(i);
						jsonList.add(jsonObject);
						if (i == (rootJsonArray.length() - 1)) {
							PreferenceClass.setStringPreference(mContext, Constant.DEFAULT_CALENDAR_ID, jsonObject.getString("id"));
						}
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
				Log.e("CALENDER error  : ", error + " reason " + reason);
			}
		});
	}

	private void getSchoolTags() {
		final ProgressHUD progress = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {

			}
		});
		mMeteor.call(Constant.METHOD_GET_SCHOOL_TAG, Constant.SCHOOL, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONArray jsonArray = jsonObject.getJSONArray("tags");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jObj = jsonArray.getJSONObject(i);
						JSONArray jsonArrayTags = jsonArray.getJSONObject(i).getJSONArray("tags");
						System.out.println("Final Tag jsonArrayTags: " + jsonArrayTags);
						for (int j = 0; j < jsonArrayTags.length(); j++) {
							tagsListFinal.add(jsonArrayTags.getString(j));
						}
						tagList.add(jObj);
					}
					setTagsByUser(tagsListFinal, progress);

					System.out.println("Final Tag List Is: " + tagsListFinal);
				} catch (JSONException e) {
					e.printStackTrace();
				}catch(NullPointerException e){
					e.printStackTrace();
				}

				ExpandableListAdapter adapter = new ExpandableListAdapter(mContext, tagList,null, 1);
				recyclerView_SchoolTags.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onError(String error, String reason, String details) {
				if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}
				Log.e("Add tag error  : ", error + " reason " + reason);
			}
		});
	}


	private void setTagsToSever(String[] arrayParam, final String calendarId) {
		final ProgressHUD progress = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {

			}
		});
		Log.e("calendarId server :: ", calendarId +" tags are "+arrayParam[0]);
		mMeteor.call(Constant.METHOD_INIT_CALENDAR, new Object[]{calendarId, arrayParam}, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				Log.e("Calendar Init success", result);
				System.out.println("Result for calendar Init: " + result);

//				if (pos == RecycleListAdapter.RootjArray.length() - 1) {
//				}
				calendarSyncToSever(calendarId);


			}

			@Override
			public void onError(String error, String reason, String details) {
				Log.e("Calendar Init error ", error + " reason " + reason);
				System.out.println("Result for calendar Init Faild: " + reason);

				Toast.makeText(mContext, "Tags not added", Toast.LENGTH_LONG).show();
				if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}
			}
		});
	}

	private void calendarSyncToSever(String calendarId) {

		mMeteor.call(Constant.METHOD_CALENDAR_SYNC, new String[]{calendarId}, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				System.out.println("Result for calendar Sync: " + result);
			}

			@Override
			public void onError(String error, String reason, String details) {
				System.out.println("Result error for calendar Sync: " + error + " reason " + reason);
			}
		});
	}

	private void setTagsByUser(ArrayList<String> user_tags, final ProgressHUD progress) {
		System.out.println("user_tags is: " + user_tags);
		String[] tagsArrayUser = user_tags.toArray(new String[user_tags.size()]);
		System.out.println("user_tags is: " + tagsArrayUser);
		mMeteor.call(Constant.METHOD_SET_USERS_TAG, new Object[]{tagsArrayUser}, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}
				Toast.makeText(mContext, "Registration successfully.", Toast.LENGTH_LONG).show();
				if (isStartHomeActivity) {
					isStartHomeActivity = false;
					startActivity(new Intent(mContext, DrawerHomeActivity.class));
					finish();
				}
			}

			@Override
			public void onError(String error, String reason, String details) {
				if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}
				Log.e("Add tag error : ", error + " reason " + reason);
				Toast.makeText(mContext, "Registration failed.", Toast.LENGTH_LONG).show();
			}
		});
	}

}

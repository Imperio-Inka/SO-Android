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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.showmeeapp.R;
import com.android.showmeeapp.adapter.ExpandableListAdapter;
import com.android.showmeeapp.constant.Constant;
import com.android.showmeeapp.model.UserDetailModel.UserModel;
import com.android.showmeeapp.ui.activities.DrawerHomeActivity;
import com.android.showmeeapp.util.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

/**
 * Created by kushahuja on 7/3/16.
 */
public class MyTagsFragment extends Fragment implements View.OnClickListener {
	public static Set<String> selectedTagList = new HashSet<>();
	private View rootView;
	private ActionBar actionBar;
	private View mToolbarView;
	private Context mContext;
	private Meteor mMeteor;
	private RecyclerView recyclerview;
	private ArrayList<JSONObject> tagList = new ArrayList<>();
	private ArrayList<JSONObject> userFollowedTagsList = new ArrayList<>();
	private ArrayList<JSONObject> schoolTagsList= new ArrayList<>();
	public static HashMap<Integer, List<Integer>> hashMap = new HashMap<>();
	Bundle extras;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_my_tegs, container, false);
		setActionBar();
		((ImageView) rootView.findViewById(R.id.imgOpenDrawer)).setOnClickListener(this);
		((TextView) rootView.findViewById(R.id.txtFragmentTitle)).setText(R.string.MyTegs);
		((Button) rootView.findViewById(R.id.btn_Done)).setOnClickListener(this);
		mContext = getActivity();
		init();

		return rootView;
	}

	private void init() {
		recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerView);
		recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
		Meteor.setLoggingEnabled(true);
		mMeteor = new Meteor(mContext, Constant.BASE_URL, new InMemoryDatabase());
		mMeteor.connect();
		extras = this.getArguments();
		String[] school = {"SCU"};

		if (extras == null) {
			getUsersTags();
		}
/*

		mMeteor.call(Constant.METHOD_GET_SCHOOL_TAG,school, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				try {
					JSONObject jsonObject = null;
					jsonObject = new JSONObject(result);
					JSONArray jsonArray = jsonObject.getJSONArray("tags");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jObj = jsonArray.getJSONObject(i);
						schoolTagsList.add(jObj);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (extras == null) {
					getUsersTags();
				}
			}

			@Override
			public void onError(String error, String reason, String details) {
				Log.e("Add tag error : ", error + " reason " + reason);
			}
		});


*/
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.imgOpenDrawer:
				DrawerHomeActivity.mDrawerLayout.openDrawer(DrawerHomeActivity.mRecyclerView);
				break;
			case R.id.btn_Done:
				setTagsByUser();

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

//	private void getUsersTags() {
//		hashMap.clear();
//		final ProgressHUD progress = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
//			@Override
//			public void onCancel(DialogInterface dialogInterface) {
//
//			}
//		});
//		selectedTagList.clear();
//		userFollowedTagsList.clear();
//		mMeteor.call(Constant.METHOD_GET_USERS_TAG, new ResultListener() {
//			@Override
//			public void onSuccess(String result) {
//				try {
//					JSONObject jsonObject = null;
//					jsonObject = new JSONObject(result);
//					JSONArray jsonArray = jsonObject.getJSONArray("tags");
//					for (int i = 0; i < jsonArray.length(); i++) {
//						JSONObject jObj = jsonArray.getJSONObject(i);
//						userFollowedTagsList.add(jObj);
//					}
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				ExpandableListAdapter adapter = new ExpandableListAdapter(mContext, userFollowedTagsList, 2);
//				recyclerview.setAdapter(adapter);
//				adapter.notifyDataSetChanged();
//				if (progress.isShowing() && progress != null) {
//					progress.dismiss();
//				}
//			}
//
//			@Override
//			public void onError(String error, String reason, String details) {
//				if (progress.isShowing() && progress != null) {
//					progress.dismiss();
//				}
//			}
//		});
//	}

	private void setTagsByUser() {
		final ProgressHUD progress = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {

			}
		});

		String[] tagsArrayUser = selectedTagList.toArray(new String[selectedTagList.size()]);

		mMeteor.call(Constant.METHOD_SET_USERS_TAG, new Object[]{tagsArrayUser}, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}
//				Toast.makeText(mContext, "Tags successfully updated.", Toast.LENGTH_LONG).show();
				getUsersTags();
			}

			@Override
			public void onError(String error, String reason, String details) {
				if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}
//				Toast.makeText(mContext, "Tags not updated.", Toast.LENGTH_LONG).show();
			}
		});
	}



	private void getUsersTags() {
		final ProgressHUD progress = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {

			}
		});


		mMeteor.call(Constant.METHOD_GET_SCHOOL_TAG, new Object[]{UserModel.getInstance().getProfile().getSchool()}, new ResultListener() {


			@Override
			public void onSuccess(String result) {
				if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}
				Log.e("School tags list *** ; ", result);
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONArray jsonArray = jsonObject.getJSONArray("tags");
					tagList.clear();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jObj = jsonArray.getJSONObject(i);
						tagList.add(jObj);
					}
					Log.e("School tags list ",tagList.toString() );

					selectedTagList.clear();
					mMeteor.call(Constant.METHOD_GET_USERS_TAG, new ResultListener() {
						@Override
						public void onSuccess(String result) {
							try {
								JSONObject jsonObject = null;
								jsonObject = new JSONObject(result);
								JSONArray jsonArray = jsonObject.getJSONArray("tags");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jObj = jsonArray.getJSONObject(i);
									userFollowedTagsList.add(jObj);
								}
								Log.e("Selected tags list ",userFollowedTagsList.toString() );
								ExpandableListAdapter adapter = new ExpandableListAdapter(mContext,tagList, userFollowedTagsList, 2);
								recyclerview.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						@Override
						public void onError(String error, String reason, String details) {

						}
					});

				} catch (JSONException e) {
					e.printStackTrace();
				}



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
}

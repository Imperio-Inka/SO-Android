package com.android.showmeeapp.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.showmeeapp.R;
import com.android.showmeeapp.adapter.DrawerAdapter;
import com.android.showmeeapp.constant.Constant;
import com.android.showmeeapp.model.UserDetailModel.CreatedAt;
import com.android.showmeeapp.model.UserDetailModel.Google;
import com.android.showmeeapp.model.UserDetailModel.LoginToken;
import com.android.showmeeapp.model.UserDetailModel.Profile;
import com.android.showmeeapp.model.UserDetailModel.Resume;
import com.android.showmeeapp.model.UserDetailModel.Services;
import com.android.showmeeapp.model.UserDetailModel.UserModel;
import com.android.showmeeapp.model.UserDetailModel.When;
import com.android.showmeeapp.pojo.NavDrawerItem;
import com.android.showmeeapp.ui.fragments.CreateEventFragment;
import com.android.showmeeapp.ui.fragments.HomeFragment;
import com.android.showmeeapp.ui.fragments.MyCalendarFragment;
import com.android.showmeeapp.ui.fragments.MyTagsFragment;
import com.android.showmeeapp.ui.fragments.ProfileFragment;
import com.android.showmeeapp.ui.fragments.SettingFragment;
import com.android.showmeeapp.util.DevicePermission;
import com.android.showmeeapp.util.PreferenceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

public class DrawerHomeActivity extends AppCompatActivity implements View.OnClickListener {

	public static DrawerLayout mDrawerLayout;          // Declaring DrawerLayout
	public static RecyclerView mRecyclerView;
	private static Context mContext;
	private static Bundle extras = null;
	private ActionBarDrawerToggle mDrawerToggle;      // Declaring Action Bar Drawer Toggle
	private String[] navMenuTitles;                   // Declaring Adapter For Recycler View
	private ArrayList<NavDrawerItem> navDrawerItems;
	private Meteor mMeteor;

	private TextView userName;

	public static void displayView(int position) {
		Fragment fragment = null;
		switch (position) {
			case 1:
				fragment = new HomeFragment();
				fragment.setArguments(extras);
				break;
			case 2:
				fragment = new MyCalendarFragment();

				break;
			case 3:
				if (extras != null) {
					showMessege(mContext.getResources().getString(R.string.CreateAccountMessege));
				}
				fragment = new MyTagsFragment();
				fragment.setArguments(extras);
				break;
			case 4:
				if (extras != null) {
					showMessege(mContext.getResources().getString(R.string.CreateAccountMessege_Event));
				}
				fragment = new CreateEventFragment();
				break;
			case 5:
				fragment = new SettingFragment();
				break;
			case 6:
				fragment = new ProfileFragment();
				break;
			default:
				break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			try {
				ft.replace(R.id.frame_container, fragment).commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mDrawerLayout.closeDrawer(mRecyclerView);
		}
	}

	private static void showMessege(String messege) {
		final AlertDialog dialog = new AlertDialog.Builder(mContext, R.style.MyDialogTheme)
				.setTitle("Alert !!!")
				.setMessage(messege)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mContext.startActivity(new Intent(mContext, GoogleLoginActivity.class));
						Activity activity = (Activity) mContext;
						activity.finish();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						displayView(1);
					}
				})
				.create();
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new DevicePermission(DrawerHomeActivity.this);

		setContentView(R.layout.activity_drawer_home);
//		final ProgressHUD progress = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
//			@Override
//			public void onCancel(DialogInterface dialogInterface) {
//
//			}
//		});
		mContext = this;
		extras = getIntent().getExtras();
		if (extras == null) {
			PreferenceClass.setBooleanPreference(mContext, "AtDrawerScreen", true);
		}
		mMeteor = new Meteor(mContext, Constant.BASE_URL, new InMemoryDatabase());
		mMeteor.connect();
		mMeteor.call(Constant.METHOD_USER_DETAILS, new ResultListener() {
			@Override
			public void onSuccess(String result) {
//				if (progress.isShowing() && progress != null) {
//					progress.dismiss();
//				}
				try {
					JSONObject jsonObject = new JSONObject(result);
					Log.e("User Details model ::: ", "User Model Initiated" + jsonObject);
					PreferenceClass.setStringPreference(mContext, Constant.CURRENT_USER_ID, jsonObject.getString("_id"));
					String userId = jsonObject.getString("_id");
					UserModel userModel = UserModel.getInstance();
					userModel.setId(userId);

					CreatedAt createdAt = new CreatedAt();
					createdAt.set$date(jsonObject.getJSONObject("createdAt").getLong("$date"));
					userModel.setCreatedAt(createdAt);


					Services services = new Services();
					JSONObject serviceJObj = jsonObject.getJSONObject("services");
					JSONObject googleJObj = serviceJObj.getJSONObject("google");
					Google google = new Google();
					google.setAccessToken(googleJObj.getString("accessToken"));
					google.setIdToken(googleJObj.getString("idToken"));
					google.setExpiresAt(googleJObj.getInt("expiresAt"));
					google.setId(googleJObj.getString("id"));
					google.setEmail(googleJObj.getString("email"));
					google.setVerifiedEmail(googleJObj.getBoolean("verified_email"));
					google.setGivenName(googleJObj.getString("given_name"));
					google.setFamilyName(googleJObj.getString("family_name"));
					google.setPicture(googleJObj.getString("picture"));

					try {
						google.setGender(googleJObj.getString("gender"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					google.setRefreshToken(googleJObj.getString("refreshToken"));
					try {
						google.setLocale(googleJObj.getString("locale"));
					} catch (JSONException e) {

					}

					List<String> scope = new ArrayList<String>();
					JSONArray scopeJArray = googleJObj.getJSONArray("scope");
					for (int i = 0; i < scopeJArray.length(); i++) {
						scope.add(scopeJArray.getString(i));
					}
					google.setScope(scope);
					services.setGoogle(google);


					Resume resume = new Resume();
					LoginToken loginToken = new LoginToken();
					JSONObject resumeJObj = serviceJObj.getJSONObject("resume");
					JSONArray loginTokensJArray = resumeJObj.getJSONArray("loginTokens");
					When when = new When();
					for (int i = 0; i < loginTokensJArray.length(); i++) {
						loginToken.setHashedToken(loginTokensJArray.getJSONObject(i).getString("hashedToken"));
						when.set$date(loginTokensJArray.getJSONObject(i).getJSONObject("when").getInt("$date"));
					}
					loginToken.setWhen(when);
					List<LoginToken> loginTokenArrayList = new ArrayList<LoginToken>();
					loginTokenArrayList.add(loginToken);
					resume.setLoginTokens(loginTokenArrayList);
					services.setResume(resume);
					userModel.setServices(services);


					Profile profile = new Profile();
					JSONObject profileJObj = jsonObject.getJSONObject("profile");
					JSONArray tagsJArray = profileJObj.getJSONArray("tags");
					List<String> tagsList = new ArrayList<String>();
					for (int i = 0; i < tagsJArray.length(); i++) {
						tagsList.add(tagsJArray.getString(i));
					}
					JSONArray excludedClubsJArray = profileJObj.getJSONArray("excludedClubs");
					List<Object> excludedClubsList = new ArrayList<Object>();
					for (int i = 0; i < excludedClubsJArray.length(); i++) {
						excludedClubsList.add(excludedClubsJArray.getString(i));
					}
					profile.setSchool(profileJObj.getString("school"));

					try {

						profile.setPhoneNumber(profileJObj.getString("phoneNumber"));

					} catch (JSONException e) {
					}
					profile.setTags(tagsList);
					profile.setExcludedClubs(excludedClubsList);
					userModel.setProfile(profile);
					init();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String error, String reason, String details) {
//				if (progress.isShowing() && progress != null) {
//					progress.dismiss();
//				}
			}
		});
	}

	private void init() {
		userName = (TextView) findViewById(R.id.txt_user_name);
		navDrawerItems = new ArrayList<NavDrawerItem>();

		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		// adding nav drawer items to array
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], R.drawable.drawer_home_icon));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], R.drawable.drawer_calendar_icon));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], R.drawable.drawer_tag_icon));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], R.drawable.drawer_event_icon));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], R.drawable.ic_setting_light));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], R.drawable.drawer_profile));

		setDrawer();
	}

	private void setDrawer() {
		mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
		mRecyclerView.setHasFixedSize(true);
		RecyclerView.Adapter mAdapter = new DrawerAdapter(mContext, navDrawerItems, DrawerHomeActivity.this);
		mRecyclerView.setAdapter(mAdapter);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}
		}; // Drawer Toggle Object Made
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.syncState();

		//Set Fragment for product
		displayView(1);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.imgIcon:
			case R.id.txtTitle:
				int position = Integer.parseInt(v.getTag().toString());
				displayView(position);

				break;
			default:
				break;
		}
	}
}

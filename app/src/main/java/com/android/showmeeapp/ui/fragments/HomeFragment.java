package com.android.showmeeapp.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
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
import com.android.showmeeapp.adapter.CalendarViewAdapter;
import com.android.showmeeapp.adapter.RecycleListAdapter;
import com.android.showmeeapp.constant.Constant;
import com.android.showmeeapp.model.UserDetailModel.CreatedAt;
import com.android.showmeeapp.model.UserDetailModel.Google;
import com.android.showmeeapp.model.UserDetailModel.LoginToken;
import com.android.showmeeapp.model.UserDetailModel.Profile;
import com.android.showmeeapp.model.UserDetailModel.Resume;
import com.android.showmeeapp.model.UserDetailModel.Services;
import com.android.showmeeapp.model.UserDetailModel.UserModel;
import com.android.showmeeapp.model.UserDetailModel.When;
import com.android.showmeeapp.pojo.CalendarData;
import com.android.showmeeapp.ui.activities.DrawerHomeActivity;
import com.android.showmeeapp.util.PreferenceClass;
import com.android.showmeeapp.util.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;


/**
 * Created by kushahuja on 7/3/16.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

	public static HashMap<String, Integer> hashMap = new HashMap<>();
	private View rootView;
	private ActionBar actionBar;
	private View mToolbarView;
	private Context mContext;
	private RecyclerView mRecyclerViewCalendar;
	private RecyclerView mRecyclerView;
	private LinearLayoutManager layoutManager;
	private int current_month;
	private ArrayList<JSONObject> eventList = new ArrayList<>();
	private Meteor mMeteor;
	private JSONObject jsonObject;
	private boolean loadOnFirstTime = true;
	private SimpleDateFormat dateFormat, dateFormat2;
	Calendar calendar, cal;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home, container, false);
		setActionBar();
		mContext = getActivity();
		init();

		return rootView;
	}

	private void init() {
		dateFormat = new SimpleDateFormat("d-M-yyyy");
		dateFormat2 = new SimpleDateFormat("d-M-yyyy'T'h:mm a");

		((ImageView) rootView.findViewById(R.id.imgOpenDrawer)).setOnClickListener(this);
		((ImageView) rootView.findViewById(R.id.imgPreviusMonth)).setOnClickListener(this);
		((ImageView) rootView.findViewById(R.id.imgNextMonth)).setOnClickListener(this);
		((TextView) rootView.findViewById(R.id.txtFragmentTitle)).setText(R.string.Home);
		mRecyclerViewCalendar = (RecyclerView) rootView.findViewById(R.id.recyclerView);
		mRecyclerViewCalendar.setHasFixedSize(true);
		mRecyclerViewCalendar.setLayoutManager(new GridLayoutManager(mContext, 7));
		mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewEvent);
		mRecyclerView.setHasFixedSize(true);
		layoutManager = new LinearLayoutManager(mContext);
		mRecyclerView.setLayoutManager(layoutManager);

		calendar = Calendar.getInstance(Locale.getDefault());
		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		current_month = calendar.get(Calendar.MONTH);
		setCalendar(day, month, year);

		cal = Calendar.getInstance(Locale.getDefault());
		Date today = cal.getTime();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		Date tommarow = cal.getTime();
		CalendarData.setTodayCompleteDate(dateFormat.format(today));
		CalendarData.setTommarowCompleteDate(dateFormat.format(tommarow));

		Meteor.setLoggingEnabled(true);
		mMeteor = new Meteor(mContext, Constant.BASE_URL, new InMemoryDatabase());
		mMeteor.connect();
//		getUsersDetais();

		eventList.clear();
		Bundle extras = this.getArguments();
		if (extras != null) {
			if (extras.getString("LOGING_TYPE").equals("get_started"))
				getSchoolEvents();
		} else {
			getEvents();
			getEventsCreatedByUser();
		}

		mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				View view = mRecyclerView.getChildAt(1);
				onScrollsetCalendar(view);
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.imgOpenDrawer:
				DrawerHomeActivity.mDrawerLayout.openDrawer(DrawerHomeActivity.mRecyclerView);
				break;
			case R.id.txtDateOfWeek:
				String[] completeDate = (view.getTag().toString().trim()).split("-");
				current_month = Integer.parseInt(completeDate[3]);
				String day = completeDate[0];
				String month = String.valueOf((current_month + 1));
				String year = completeDate[2];

				setCalendar(day, month, year);

				if (getKey(day + "-" + month + "-" + year)) {
					Integer position = hashMap.get(day + "-" + month + "-" + year);
					layoutManager.scrollToPositionWithOffset(position, 1);

				}
				break;
			case R.id.imgPreviusMonth:
				int currentDate = CalendarData.getCurrentDayOfMonth();
				int currentMonth = CalendarData.getCurrentMonth();
				int currentYear = CalendarData.getCurrentYear();
				int previusMonth = CalendarData.getPrevMonth();
				int previusYear = CalendarData.getPrevYear();
				int priviusDay = 0;

				for (int i = 0; i < 7; i++) {
					if (currentDate > 1) {
						currentDate--;
					} else {
						priviusDay++;
					}
				}
				if (priviusDay > 0) {
					currentDate = CalendarData.getNumberOfDaysOfMonth(previusMonth) - priviusDay;
					currentMonth = previusMonth;
					if (currentMonth == 11)
						currentYear = previusYear;
				}
				current_month = currentMonth;
				currentMonth = currentMonth + 1;
				setCalendar(String.valueOf(currentDate), String.valueOf(currentMonth), String.valueOf(currentYear));

				break;
			case R.id.imgNextMonth:
				int current_Date = CalendarData.getCurrentDayOfMonth();
				int current_Month = CalendarData.getCurrentMonth();
				int current_Year = CalendarData.getCurrentYear();
				int nextMonth = CalendarData.getNextMonth();
				int nextYear = CalendarData.getNextYear();
				int currentMonths_days = CalendarData.getNumberOfDaysOfMonth(current_Month);
				int nextMonths_day = 0;

				for (int i = 0; i < 7; i++) {
					if (current_Date <= currentMonths_days) {
						current_Date++;
					} else {
						nextMonths_day++;
					}
				}
				if (nextMonths_day > 0) {
					current_Date = nextMonths_day;
					current_Month = nextMonth;
					if (current_Month == 0)
						current_Year = nextYear;
				}
				current_month = current_Month;
				current_Month = current_Month + 1;
				setCalendar(String.valueOf(current_Date), String.valueOf(current_Month), String.valueOf(current_Year));

				break;
			case R.id.relative_layout2:
				String strJsonObject = view.getTag().toString();
				Log.e("strJsonObject : ", strJsonObject.toString());
				Bundle bundle = new Bundle();
				bundle.putString("JsonObject", strJsonObject);
				EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
				eventDetailsFragment.setArguments(bundle);

				FragmentManager fm = getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.add(R.id.frame_container, eventDetailsFragment);
				fragmentTransaction.commit();

				break;
			case R.id.txtSectionDate:
				int pos = onScrollsetCalendar(view);
				layoutManager.scrollToPositionWithOffset(pos, 1);

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

	private void getSchoolEvents() {
		MeteorCallback callback = new MeteorCallback() {
			@Override
			public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
				Log.e("subscription", "getSchoolEvents ::::::  " + collectionName + " newValuesJson :: " + newValuesJson);
				if (collectionName.equals("events")) {
					if (newValuesJson != null) {
						try {
							JSONObject jsonObject = new JSONObject(newValuesJson);
							if (isEventNotExist(jsonObject)) {
								eventList.add(jsonObject);
							}
							//System.out.println("size list : " + eventList.size());
							//System.out.println("getEvents event list : " + eventList.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						sortToJsonlist(eventList);
						RecycleListAdapter adapter = new RecycleListAdapter(mContext, Constant.GET_EVETN_LIST, eventList, HomeFragment.this);
						adapter.notifyDataSetChanged();
						mRecyclerView.setAdapter(adapter);
						setHashMap();
					}
				}

			}

			@Override
			public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {

			}

			@Override
			public void onDataRemoved(String collectionName, String documentID) {

			}

			@Override
			public void onConnect(boolean signedInAutomatically) {

			}

			@Override
			public void onDisconnect() {
			}

			@Override
			public void onException(Exception e) {

			}
		};
		mMeteor.subscribe(Constant.METHOD_GET_SCHOOL_EVENT, new String[]{"Santa Clara University"});
		mMeteor.addCallback(callback);
	}

	private void getUsersDetais() {
		final ProgressHUD progress = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {

			}
		});
		mMeteor.call(Constant.METHOD_USER_DETAILS, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}
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
					google.setGender(googleJObj.getString("gender"));
					google.setRefreshToken(googleJObj.getString("refreshToken"));
					google.setLocale(googleJObj.getString("locale"));
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
					profile.setPhoneNumber(profileJObj.getString("phoneNumber"));
					profile.setTags(tagsList);
					profile.setExcludedClubs(excludedClubsList);
					userModel.setProfile(profile);

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

	private void getEvents() {
		/*final ProgressHUD progress = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {

			}
		});*/
		MeteorCallback callback = new MeteorCallback() {
			@Override
			public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
				Log.e("subscription", "getEvents ::::::  " + collectionName + " newValuesJson :: " + newValuesJson);
				if (collectionName.equals("events")) {
					if (newValuesJson != null) {
						try {
							JSONObject jsonObject = new JSONObject(newValuesJson);
							if (isEventNotExist(jsonObject)) {
								eventList.add(jsonObject);
							}
							//System.out.println("size list : " + eventList.size());
							//System.out.println("getEvents event list : " + eventList.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						sortToJsonlist(eventList);
						RecycleListAdapter adapter = new RecycleListAdapter(mContext, Constant.GET_EVETN_LIST, eventList, HomeFragment.this);
						adapter.notifyDataSetChanged();
						mRecyclerView.setAdapter(adapter);
						setHashMap();
					}
				}

			}

			@Override
			public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
				//Log.e("subscription", "onDataChanged collectionName " + collectionName + " newValueJason :: " + updatedValuesJson);
				Log.e("subscription", "getEvents ::::::  " + collectionName + " newValuesJson :: " + updatedValuesJson);
				if (collectionName.equals("events")) {
					if (updatedValuesJson != null) {
						try {
							JSONObject jsonObject = new JSONObject(updatedValuesJson);
							if (isEventNotExist(jsonObject)) {
								eventList.add(jsonObject);
							}
							//System.out.println("size list : " + eventList.size());
							//System.out.println("getEvents event list : " + eventList.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						sortToJsonlist(eventList);
						RecycleListAdapter adapter = new RecycleListAdapter(mContext, Constant.GET_EVETN_LIST, eventList, HomeFragment.this);
						adapter.notifyDataSetChanged();
						mRecyclerView.setAdapter(adapter);
						setHashMap();
					}
				}

			}

			@Override
			public void onDataRemoved(String collectionName, String documentID) {

			}

			@Override
			public void onConnect(boolean signedInAutomatically) {
				//Log.e("subscription", "SignedInAutomatically :: " + signedInAutomatically);

			}

			@Override
			public void onDisconnect() {
			/*	if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}*/
			}

			@Override
			public void onException(Exception e) {

			}
		};
		mMeteor.subscribe(Constant.METHOD_EVENT_FEEDS);
		mMeteor.addCallback(callback);
	}

	private void getEventsCreatedByUser() {
		MeteorCallback mCallback = new MeteorCallback() {
			@Override
			public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
				//Log.e("subscription", "getEventsCreatedByUser  ****   " + collectionName + " newValuesJson :: " + newValuesJson);
				if (collectionName.equals("events")) {
					if (newValuesJson != null) {
						try {
							JSONObject jsonObject = new JSONObject(newValuesJson);
							if (isEventNotExist(jsonObject)) {
								eventList.add(jsonObject);
							}
							//System.out.println("size list : " + eventList.size());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						sortToJsonlist(eventList);
						RecycleListAdapter adapter = new RecycleListAdapter(mContext, Constant.GET_EVETN_LIST, eventList, HomeFragment.this);
						adapter.notifyDataSetChanged();
						mRecyclerView.setAdapter(adapter);
						setHashMap();
					}
				}
			}

			@Override
			public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {

			}

			@Override
			public void onDataRemoved(String collectionName, String documentID) {

			}

			@Override
			public void onConnect(boolean signedInAutomatically) {

			}

			@Override
			public void onDisconnect() {
			}

			@Override
			public void onException(Exception e) {

			}
		};
		mMeteor.subscribe(Constant.METHOD_EVENT_CREATED_BY_USER);
		mMeteor.addCallback(mCallback);
	}

	private void setCalendar(String day, String month, String year) {
		CalendarViewAdapter adapter = new CalendarViewAdapter(mContext, day, month, year, this);
		adapter.notifyDataSetChanged();
		mRecyclerViewCalendar.setAdapter(adapter);
		((TextView) rootView.findViewById(R.id.txtMonthDetails)).setText(CalendarData.getMonthAsString(current_month) + "-" + year);
	}

	private JSONArray sortToJsonlist(final ArrayList<JSONObject> list) {
		Collections.sort(list, new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject lhs, JSONObject rhs) {
				Date lId = null;
				Date rId = null;
				try {
					lId = getDate(lhs.getJSONObject("start").getLong("$date"));
					rId = getDate(rhs.getJSONObject("start").getLong("$date"));
					return lId.compareTo(rId);

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NullPointerException e){
					e.printStackTrace();
				}
				return 0;
			}
		});
		return new JSONArray(list);
	}

	private void setHashMap() {
		String priviusDate = null;
		hashMap.clear();
		//Log.e("eventList.size() : ", eventList.size() + "");
		for (int position = 0; position < eventList.size(); position++) {
			String strDate = null;
			try {
				JSONObject rootJsonObj = eventList.get(position);
				JSONObject jObjStartTime = rootJsonObj.getJSONObject("start");
				long start = jObjStartTime.getLong("$date");
				String strStartDate = RecycleListAdapter.getDate(start);
				String[] startDate = strStartDate.split("T");
				strDate = startDate[0];

				if (position == 0) {
					hashMap.put(strDate, position);
				} else {
					if (!strDate.equals(priviusDate)) {
						hashMap.put(strDate, position);
					}
				}
				priviusDate = strDate;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		setTodayDate();
	}

	public boolean getKey(String key) {
		//Log.e("getKey : ", key);
		if (hashMap.containsKey(key)) {
			//Log.e("true : ", "true");
			return true;
		}
		return false;
	}

	private int onScrollsetCalendar(View view) {
		try {
			String[] tag = (view.getTag().toString().trim()).split("-");
			current_month = Integer.parseInt(tag[1]);
			current_month = current_month - 1;
			String date = tag[0];
			String _month = String.valueOf((current_month + 1));
			String _year = tag[2];
			int pos = Integer.parseInt(tag[3]);
			// Log.e("Complete date : ", "day : " + date + " month : " + _month + " year : " + _year);
			setCalendar(date, _month, _year);
			return pos;
		} catch (NullPointerException npe) {
			Log.d("Null pointer exception", "" + npe);
		}
		return 0;
	}

	private void setTodayDate() {
		Date currentDate = getDate(calendar.getTimeInMillis());
		Date eventDate = null;
		for (int i = 0; i < eventList.size(); i++) {
			try {
				eventDate = getDate(eventList.get(i).getJSONObject("start").getLong("$date"));
				if (eventDate.equals(currentDate)) {
					layoutManager.scrollToPositionWithOffset(i, 1);
					cal.setTimeInMillis(eventList.get(i).getJSONObject("start").getLong("$date"));
					setCalendar(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), String.valueOf(cal.get(Calendar.MONTH) + 1), String.valueOf(cal.get(Calendar.YEAR)));
					break;
				} else if (eventDate.after(currentDate)) {
					layoutManager.scrollToPositionWithOffset(i, 1);
					cal.setTimeInMillis(eventList.get(i).getJSONObject("start").getLong("$date"));
					setCalendar(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), String.valueOf(cal.get(Calendar.MONTH) + 1), String.valueOf(cal.get(Calendar.YEAR)));
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
	}
	}

	private boolean isEventNotExist(JSONObject jsonObject) {
		try {
			String id = jsonObject.getString("id");
			for (int i = 0; i < eventList.size(); i++) {
				if (eventList.get(i).getString("id").equals(id)) {
					return false;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return true;
	}

	public Date getDate(long milliSeconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		int currentDate = calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.MONTH) + calendar.get(Calendar.YEAR);
		Date date = calendar.getTime();
		return date;
	}

}

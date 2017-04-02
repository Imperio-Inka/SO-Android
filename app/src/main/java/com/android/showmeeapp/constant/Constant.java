package com.android.showmeeapp.constant;

import android.os.Environment;

/**
 * Created by yuva on 14/7/16.
 */
public class Constant {

	public static final String[] SCHOOL= {"Santa Clara University"};
	//https://loopcowstudio.com/#top
	public static final String BASE_URL = "wss://www.loopcowstudio.com/websocket";
	public static final String GOOGLE_PLACE_KEY = "AIzaSyDb_FJ6Gp7cJ4GWGUU9zU1fopMGt_JFk24";
	public static final String GCM_REGISTER_KEY="gcm_reg_key";

	/*Server Method*/
	public static final String METHOD_CALENDAR_LIST = "calendars.list";
	public static final String METHOD_CALENDAR_SYNC = "calendars.sync";
	public static final String METHOD_USER_DETAILS = "users.currentUser";
	public static final String METHOD_INIT_CALENDAR = "calendars.init";
	public static final String METHOD_GET_ALL_CALENDAR = "calendars.imported";
	public static final String METHOD_GET_SCHOOL_TAG = "tags.school";
	public static final String METHOD_SET_USERS_TAG = "users.setTags";
	public static final String METHOD_GET_USERS_TAG = "tags.user";
	public static final String METHOD_INSERT_EVENT = "events.insert";
	public static final String METHOD_INSERT_EVENT_SO = "events.insert.local";
	public static final String METHOD_UPDATE_EVENT = "events.update";
	public static final String METHOD_UPDATE_EVENT_SO = "events.update.local";
	public static final String METHOD_EVENT_FEEDS = "events.feeds";
	public static final String METHOD_EVENT_SET_TAGS = "events.setTags";
	public static final String METHOD_GET_SCHOOL_EVENT = "events.school";
	public static final String METHOD_EVENT_CREATED_BY_USER = "events.owner";
	public static final String METHOD_CHECKPHONE_NUMBERS = "contacts.checkPhoneNumbers";
	public static final String METHOD_EVENT_INVITATION_WITH_SMS = "invitations.event.sms";
	public static final String METHOD_ADD_LOCAL_ATTANDEES = "contacts.checkPhoneNumbers";
	public static final String METHOD_ADD_ATTENDEE = "attendees.add";


	/*Constant String*/
	public static final String USER_ID = "user_id";
	public static final String DEFAULT_CALENDAR_ID = "default_caledar_id";
	public static final String CURRENT_USER_ID = "current_user_id";
	public static final String SELECTED_SCHOOL = "selected_school_by_user";
	public static final String USER_CONTACT = "user_contact_set";


	/*Constant for list adapter*/
	public static final int GET_CALENDAR_LIST = 101;
	public static final int GET_EVETN_LIST = 102;
	public static final int GET_ALL_CALENDAR = 103;


	// Calendar constants
	public static final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	public static final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December"};
	public static final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	// App folder path in sdcard
	public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/ShowMee/";
	public static final String FOLDAR_NAME = "ShowMee";


}

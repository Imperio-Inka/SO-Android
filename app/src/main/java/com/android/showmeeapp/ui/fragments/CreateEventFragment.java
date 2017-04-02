package com.android.showmeeapp.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.showmeeapp.R;
import com.android.showmeeapp.adapter.ExpandableListAdapter;
import com.android.showmeeapp.constant.Constant;
import com.android.showmeeapp.model.CreateEvent;
import com.android.showmeeapp.model.End;
import com.android.showmeeapp.model.EventSO;
import com.android.showmeeapp.model.EventTags;
import com.android.showmeeapp.model.Start;
import com.android.showmeeapp.model.User;
import com.android.showmeeapp.model.UserDetailModel.UserModel;
import com.android.showmeeapp.ui.activities.ContactsActivity;
import com.android.showmeeapp.ui.activities.DrawerHomeActivity;
import com.android.showmeeapp.util.FileUtils;
import com.android.showmeeapp.util.GPSService;
import com.android.showmeeapp.util.PreferenceClass;
import com.android.showmeeapp.util.tagcontainer.TagContainerLayout;
import com.android.showmeeapp.util.tagcontainer.TagView;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;


/**
 * Created by kushahuja on 7/3/16.
 */
public class CreateEventFragment extends Fragment implements View.OnClickListener {
	private static final int REQUEST_CODE_AUTOCOMPLETE = 111;
	public static Dialog dialog;
	public static ArrayList<String> selectedTagList = new ArrayList<>();
	public static HashMap<Integer, List<Integer>> hashMap = new HashMap<>();
	public static ArrayList<String> selectedSMContactList = new ArrayList<>();
	public static ArrayList<String> selectedMYContactList = new ArrayList<>();
	public static List<Integer> hasList;
	private int start_year = 0, start_month = 0, start_day = 0;
	private int end_year = 0, end_month = 0, end_day = 0;
	private int startTime_hour = 0, startTime_minut = 0;
	private int endTime_hour = 0, endTime_minut = 0;
	private ArrayList<String> tagsListFinal = new ArrayList<String>();
	private String imageName = "";
	private View rootView;
	private Context mContext;
	private ActionBar actionBar;
	private View mToolbarView;
	private Dialog dialogPickImage;
	private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
	private Calendar cal;
	private RecyclerView recyclerView_SchoolTags;
	private DateFormat dateFormat, dateFormatMilliSecond, timeFormatMilliSecond, datetimeFormatMilliSecond, timeFormat;
	private String strVisibilty = "default";
	private boolean inviteOther = false;
	private ArrayList<JSONObject> tagList = new ArrayList<>();
	private Meteor mMeteor;

	private String strDateStartMillisecond = "";
	private String strDateEndMillisecond = "";
	private String strTimeStartMillisecond = "";
	private String strTimeEndMillisecond = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_create_event, container, false);
		mContext = this.getActivity();
		init();

		return rootView;
	}

	private void init() {
		setActionBar();
		((TextView) rootView.findViewById(R.id.txtFragmentTitle)).setText(R.string.CreateEvent);
		((ImageView) rootView.findViewById(R.id.imgOpenDrawer)).setOnClickListener(this);
		((ImageView) rootView.findViewById(R.id.imgEventPicture)).setOnClickListener(this);
		((ImageView) rootView.findViewById(R.id.imgCurrentLocation)).setOnClickListener(this);
		((ImageView) rootView.findViewById(R.id.imgAddGuest)).setOnClickListener(this);
		((Button) rootView.findViewById(R.id.btnCreate)).setOnClickListener(this);

		((TextView) rootView.findViewById(R.id.txtSetStartDate)).setOnClickListener(this);
		((TextView) rootView.findViewById(R.id.txtStartTime)).setOnClickListener(this);
		((TextView) rootView.findViewById(R.id.txtSetEndDate)).setOnClickListener(this);
		((TextView) rootView.findViewById(R.id.txtAtLocation)).setOnClickListener(this);
		((TextView) rootView.findViewById(R.id.txtEndTime)).setOnClickListener(this);
		((TextView) rootView.findViewById(R.id.txtEventTag)).setOnClickListener(this);
		cal = Calendar.getInstance();
		dateFormat = new SimpleDateFormat("EEE-MMM,d yyyy");
		timeFormat = new SimpleDateFormat("h:mm a");

		dateFormatMilliSecond = new SimpleDateFormat("dd-MM-yyyy");
		timeFormatMilliSecond = new SimpleDateFormat("HH:mm");
		datetimeFormatMilliSecond = new SimpleDateFormat("dd-MM-yyyy HH:mm");

		dialogPickImage();


		Meteor.setLoggingEnabled(true);
		mMeteor = new Meteor(mContext, Constant.BASE_URL, new InMemoryDatabase());
		mMeteor.connect();

		//set tags Dialog
		setDailog();

		((ToggleButton) rootView.findViewById(R.id.toggleButtonPublicEvent)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b) {
					strVisibilty = "public";
				} else {
					strVisibilty = "default";
				}
			}
		});
		((ToggleButton) rootView.findViewById(R.id.toggleButtonInviteOther)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b) {
					inviteOther = true;
				} else {
					inviteOther = false;
				}
			}
		});

		((TagContainerLayout) rootView.findViewById(R.id.tagcontainerLayout)).setOnTagClickListener(new TagView.OnTagClickListener() {
			@Override
			public void onTagClick(final int position, String text) {
				AlertDialog dialog = new AlertDialog.Builder(mContext, R.style.MyDialogTheme)
						.setTitle("Delete !!!")
						.setMessage("Are you sure want to delete this tag?")
						.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								((TagContainerLayout) rootView.findViewById(R.id.tagcontainerLayout)).removeTag(position);
								selectedTagList.remove(position);
							}
						})
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						})
						.create();
				if (!dialog.isShowing()) {
					dialog.show();
				}
			}

			@Override
			public void onTagLongClick(int position, String text) {

			}
		});

	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.imgOpenDrawer:
				DrawerHomeActivity.mDrawerLayout.openDrawer(DrawerHomeActivity.mRecyclerView);
				break;
			case R.id.imgEventPicture:
				if (!dialogPickImage.isShowing()) {
					dialogPickImage.show();
				}
				break;
			case R.id.imgCurrentLocation:
				String address = "";
				GPSService mGPSService = new GPSService(getActivity());
				mGPSService.getLocation();

				if (mGPSService.isLocationAvailable == false) {
					Toast.makeText(getActivity(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
					return;
				} else {
					address = mGPSService.getLocationAddress();
					((TextView) rootView.findViewById(R.id.txtAtLocation)).setText(address);
				}
				mGPSService.closeGPS();
				break;
			case R.id.imgAddGuest:
				startActivity(new Intent(mContext, ContactsActivity.class));
				break;
			case R.id.txtSetStartDate:
				setEventDate(1);
				break;
			case R.id.txtStartTime:
				//setEventTime(1);
				break;
			case R.id.txtSetEndDate:
				setEventDate(2);
				break;
			case R.id.txtAtLocation:
				openLocationDialog();
				break;
			case R.id.txtEndTime:
				//setEventTime(2);
				break;
			case R.id.txtEventTag:
				//	Toast.makeText(mContext,"In Process...",Toast.LENGTH_LONG).show();
				if (dialog != null && !dialog.isShowing()) {
					dialog.show();
				}
				break;
			case R.id.imgVwDone:
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				((TagContainerLayout) rootView.findViewById(R.id.tagcontainerLayout)).removeAllTags();
				((TagContainerLayout) rootView.findViewById(R.id.tagcontainerLayout)).setTags(selectedTagList);

				break;
			case R.id.btnCreate:
				getDataFromUser();
				break;

			case R.id.txtTakePhoto:
				if (dialogPickImage.isShowing() && dialogPickImage != null) {
					dialogPickImage.dismiss();
				}
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				imageName = System.currentTimeMillis() + ".png";
				intent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtils.getUriByImageName(Constant.DATA_PATH, Constant.FOLDAR_NAME, imageName));
				if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
					startActivityForResult(intent, REQUEST_CAMERA);
				}
				break;
			case R.id.txtChooseExisting:
				if (dialogPickImage.isShowing() && dialogPickImage != null) {
					dialogPickImage.dismiss();
				}
				Intent intentPickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intentPickImage.setType("image/*");
				intentPickImage.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intentPickImage, "Complete action using"), SELECT_FILE);
				break;
			case R.id.txtCancel:
				if (dialogPickImage.isShowing() && dialogPickImage != null) {
					dialogPickImage.dismiss();
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == getActivity().RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				try {
					Uri uri = FileUtils.getUriByImageName(Constant.DATA_PATH, Constant.FOLDAR_NAME, imageName);
					File file = FileUtils.getFile(mContext, uri);
					BitmapFactory.Options bmOptions = new BitmapFactory.Options();
					Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
					((ImageView) rootView.findViewById(R.id.imgEventPicture)).setImageBitmap(bitmap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
				Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String FilePath = cursor.getString(columnIndex);
				cursor.close();
				File finalFile = new File(FilePath);
				BitmapFactory.Options bmOptions = new BitmapFactory.Options();
				Bitmap bitmap = BitmapFactory.decodeFile(finalFile.getAbsolutePath(), bmOptions);
				((ImageView) rootView.findViewById(R.id.imgEventPicture)).setImageBitmap(bitmap);

			} else if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
				if (resultCode == getActivity().RESULT_OK) {
					// Get the user's selected place from the Intent.
					Place place = PlaceAutocomplete.getPlace(mContext, data);
					((TextView) rootView.findViewById(R.id.txtAtLocation)).setText(place.getName() + " " + place.getAddress());
				}
			} else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
				Status status = PlaceAutocomplete.getStatus(mContext, data);
				Log.e("TAG", "Error: Status = " + status.toString());
			} else if (resultCode == getActivity().RESULT_CANCELED) {
				Status status = PlaceAutocomplete.getStatus(mContext, data);
				Log.e("TAG", "Error: Status = " + status.toString());
			}
		}
	}

	private void setEventDate(int type) {
		switch (type) {
			case 1:
				if (start_year == 0 && start_month == 0 && start_day == 0) {
					start_year = cal.get(Calendar.YEAR);
					start_month = cal.get(Calendar.MONTH);
					start_day = cal.get(Calendar.DAY_OF_MONTH);
				}
				cal.set(start_year, start_month, start_day);
				DatePickerDialog datePicker = new DatePickerDialog(mContext, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						try {
							start_year = year;
							start_month = monthOfYear;
							start_day = dayOfMonth;
							cal.set(start_year, start_month, start_day);
							strDateStartMillisecond = dateFormatMilliSecond.format(cal.getTime());
							Log.e("startDate : ", dateFormat.format(cal.getTime()));
							((TextView) rootView.findViewById(R.id.txtSetStartDate)).setText("" + dateFormat.format(cal.getTime()));
							setEventTime(1);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, start_year, start_month, start_day);
				datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
				datePicker.show();

				break;
			case 2:
				try {
					if (start_day == 0 && start_month == 0 && start_day == 0) {
						Toast.makeText(mContext, "Please select event start date first", Toast.LENGTH_SHORT).show();
					} else if (startTime_hour == 0 && startTime_minut == 0) {
						Toast.makeText(mContext, "Please select event start time first", Toast.LENGTH_SHORT).show();
					} else {
						end_year = start_year;
						end_month = start_month;
						end_day = start_day;
						cal.set(end_year, end_month, end_day);

						DatePickerDialog datePicker2 = new DatePickerDialog(mContext, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								try {
									start_year = year;
									start_month = monthOfYear;
									start_day = dayOfMonth;
									cal.set(start_year, start_month, start_day);
									strDateEndMillisecond = dateFormatMilliSecond.format(cal.getTime());
									Log.e("EndDate : ", dateFormat.format(cal.getTime()));
									((TextView) rootView.findViewById(R.id.txtSetEndDate)).setText("" + dateFormat.format(cal.getTime()));
									setEventTime(2);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}, end_year, end_month, end_day);
						datePicker2.getDatePicker().setMinDate(cal.getTimeInMillis());
						datePicker2.show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
		}
	}

	private void setEventTime(int type) {
		switch (type) {
			case 1:
				if (start_day == 0 && start_month == 0 && start_day == 0) {
					Toast.makeText(mContext, "Please select event start date first", Toast.LENGTH_SHORT).show();
				} else {
					startTime_hour = cal.get(Calendar.HOUR_OF_DAY);
					startTime_minut = cal.get(Calendar.MINUTE);
					cal.set(start_year, start_month, start_day);

					new TimePickerDialog(mContext, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							String strTime = setTimeFormate(hourOfDay, minute);
							//strTimeStartMillisecond = hourOfDay + ":" + minute;
							strTimeStartMillisecond = timeFormatMilliSecond.format(cal.getTime());
							cal.set(start_year, start_month, start_day, hourOfDay, minute);
							startTime_hour = hourOfDay;
							startTime_minut = minute;
							Log.e("startTime : ", timeFormat.format(cal.getTime()));
							Log.e("startTime format Pm : ", strTime);
							((TextView) rootView.findViewById(R.id.txtStartTime)).setText("" + strTime);
						}
					}, startTime_hour, startTime_minut, false).show();
				}
				break;
			case 2:
				if (start_day == 0 && start_month == 0 && start_day == 0) {
					Toast.makeText(mContext, "Please select event start date first", Toast.LENGTH_SHORT).show();
				} else if (startTime_hour == 0 && startTime_minut == 0) {
					Toast.makeText(mContext, "Please select event start time first", Toast.LENGTH_SHORT).show();
				} else if (end_day == 0 && end_month == 0 && end_year == 0) {
					Toast.makeText(mContext, "Please select event end time first", Toast.LENGTH_SHORT).show();
				} else {
					endTime_hour = startTime_hour;
					endTime_minut = startTime_minut;
					cal.set(end_year, end_month, end_day);

					new TimePickerDialog(mContext, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							//strTimeEndMillisecond = hourOfDay + ":" + minute;
							strTimeEndMillisecond = timeFormatMilliSecond.format(cal.getTime());
							String strTime = setTimeFormate(hourOfDay, minute);
							cal.set(end_year, end_month, end_day, hourOfDay, minute);
							endTime_hour = hourOfDay;
							endTime_minut = minute;
							Log.e("endTime : ", timeFormat.format(cal.getTime()));
							Log.e("endTime format Pm : ", strTime);
							((TextView) rootView.findViewById(R.id.txtEndTime)).setText("" + strTime);
						}
					}, endTime_hour, endTime_minut, false).show();
				}
				break;
			default:
				break;
		}
	}

	private String setTimeFormate(int hours, int mins) {

		String timeSet = "";
		if (hours > 12) {
			hours -= 12;
			timeSet = "PM";
		} else if (hours == 0) {
			hours += 12;
			timeSet = "AM";
		} else if (hours == 12)
			timeSet = "PM";
		else
			timeSet = "AM";

		String minutes = "";
		if (mins < 10)
			minutes = "0" + mins;
		else
			minutes = String.valueOf(mins);
		// Append in a StringBuilder
		String aTime = new StringBuilder().append(hours).append(':')
				.append(minutes).append(" ").append(timeSet).toString();

		return aTime;
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

	private void dialogPickImage() {
		dialogPickImage = new Dialog(mContext);
		dialogPickImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogPickImage.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialogPickImage.setContentView(R.layout.dialog_pick_image);
		dialogPickImage.setCancelable(false);

		((TextView) dialogPickImage.findViewById(R.id.txtTakePhoto)).setOnClickListener(this);
		((TextView) dialogPickImage.findViewById(R.id.txtChooseExisting)).setOnClickListener(this);
		((TextView) dialogPickImage.findViewById(R.id.txtCancel)).setOnClickListener(this);
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

	private void getDataFromUser() {
		String strEventTitle = ((EditText) rootView.findViewById(R.id.edtEventTitle)).getText().toString().trim();
		String strEventDescription = ((EditText) rootView.findViewById(R.id.txtEventDescreption)).getText().toString().trim();
		String strStartDateTime = ((TextView) rootView.findViewById(R.id.txtSetStartDate)).getText().toString().trim() + " " + ((TextView) rootView.findViewById(R.id.txtStartTime)).getText().toString().trim();
		String strEndDateTime = ((TextView) rootView.findViewById(R.id.txtSetEndDate)).getText().toString().trim() + " " + ((TextView) rootView.findViewById(R.id.txtEndTime)).getText().toString().trim();
		String strLocation = ((TextView) rootView.findViewById(R.id.txtAtLocation)).getText().toString().trim();

		if (strEventTitle.length() == 0) {
			Toast.makeText(mContext, "Please enter event title.", Toast.LENGTH_LONG).show();
		} else if (strStartDateTime.length() == 0) {
			Toast.makeText(mContext, "Please select start date for event.", Toast.LENGTH_LONG).show();
		} else if (strEndDateTime.length() == 0) {
			Toast.makeText(mContext, "Please select end date for event.", Toast.LENGTH_LONG).show();
		} else {
			try {
				EventSO createEventObject = new EventSO();
				createEventObject.setSummary(strEventTitle);
				createEventObject.setDescription(strEventDescription);
				createEventObject.setLocation(strLocation);
				createEventObject.setVisibility(strVisibilty);
//				createEventObject.setPhoneNumber("4153019886");
				createEventObject.setClub("KA");
				createEventObject.setSchool(UserModel.getInstance().getProfile().getSchool());


				Date mStartDate = datetimeFormatMilliSecond.parse(strDateStartMillisecond + " " + strTimeStartMillisecond);
				long startTimeInMilliseconds = mStartDate.getTime();

//				Start start = new Start();
//				start.setDateTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//						.format(startTimeInMilliseconds));
				createEventObject.setStart(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
						.format(startTimeInMilliseconds));



				Date mEndDate = datetimeFormatMilliSecond.parse(strDateEndMillisecond + " " + strTimeEndMillisecond);
				long endTimeInMilliseconds = mEndDate.getTime();
//				End end = new End();
//				end.setDateTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//						.format(endTimeInMilliseconds));
				createEventObject.setEnd(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
						.format(endTimeInMilliseconds));

				String[] tagsWithEvent = new String[selectedTagList.size()];
				for (int i = 0; i < selectedTagList.size(); i++) {
					tagsWithEvent[i] = selectedTagList.get(i);
				}
				EventTags eventTags = new EventTags();
				eventTags.setTags(tagsWithEvent);
				insertEvent(createEventObject, eventTags);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void insertEvent(EventSO createEventObject, final EventTags eventTags) {
//		final ProgressHUD progress = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
//			@Override
//			public void onCancel(DialogInterface dialogInterface) {
//
//			}
//		});
//		Log.e("CreateEventFragment: ", "Insert Event method called. " + createEventObject.getStart().getDateTime());

		mMeteor.call(Constant.METHOD_INSERT_EVENT_SO, new Object[]{createEventObject}, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				Log.d(" InsertEventResult : ", result.toString());
				selectedTagList.clear();
//				try {
//					JSONObject resultObject = new JSONObject(result);
//					String eventId = resultObject.getString("id");
//					setEventTags(eventId, eventTags);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
			}

			@Override
			public void onError(String error, String reason, String details) {
				Log.e("Add event error : ", error + " reason " + reason);
			}
		});
	}

	private void setEventTags(final String eventId, EventTags tags) {
		mMeteor.call(Constant.METHOD_EVENT_SET_TAGS, new Object[]{eventId, tags.getTags()}, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				Log.e("Event tags updated : ", result);
				selectedTagList.clear();
				inviteBySMS(eventId);
			}

			@Override
			public void onError(String error, String reason, String details) {
				Log.e("Add tag error : ", "There was a problem adding tags to events error :: " + error + " reason :: " + reason);
			}
		});
	}
	private void inviteBySMS(String eventId){
		String contactNumber;
				for(int i=0; i<selectedMYContactList.size();i++) {
					contactNumber = selectedMYContactList.get(i);
					mMeteor.call(Constant.METHOD_EVENT_INVITATION_WITH_SMS, new Object[]{eventId, contactNumber}, new ResultListener() {
						@Override
						public void onSuccess(String result) {
							Log.e("Event tags updated : ", result);
							DrawerHomeActivity.displayView(1);
						}

						@Override
						public void onError(String error, String reason, String details) {
							Log.e("Add tag error : ", "There was a problem adding tags to events error :: " + error + " reason :: " + reason);
						}
					});
				}
	}

	private void getSchoolTags() {
		mMeteor.call(Constant.METHOD_GET_SCHOOL_TAG, Constant.SCHOOL, new ResultListener() {
			@Override
			public void onSuccess(String result) {
				Log.e("getSchoolTags Result : ", result);
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
					System.out.println("Final Tag List Is: " + tagsListFinal);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				ExpandableListAdapter adapter = new ExpandableListAdapter(mContext, tagList,null, 3);
				recyclerView_SchoolTags.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onError(String error, String reason, String details) {
				Log.e("Add tag error  : ", error + " reason " + reason);
			}
		});
	}

	private void openLocationDialog() {
		try {
			Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
			startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
		} catch (GooglePlayServicesRepairableException e) {
			GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(), 0).show();
		} catch (GooglePlayServicesNotAvailableException e) {
			String message = "Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
			Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		}
	}


}


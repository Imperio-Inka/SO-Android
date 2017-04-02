package com.android.showmeeapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.showmeeapp.R;
import com.android.showmeeapp.constant.Constant;
import com.android.showmeeapp.pojo.CalendarData;
import com.android.showmeeapp.ui.activities.CalendarListActivity;
import com.android.showmeeapp.util.tagcontainer.TagContainerLayout;
import com.android.showmeeapp.util.tagcontainer.TagView;
import com.android.showmeeapp.views.ExpandView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by yuva on 15/7/16.
 */
public class RecycleListAdapter extends RecyclerView.Adapter<RecycleListAdapter.ViewHolder> {

	private static final int SECTIONED_STATE = 1;
	private static final int REGULAR_STATE = 2;
	public static JSONArray RootjArray = new JSONArray();
	public static ArrayList<JSONObject> jsonList = new ArrayList<>();
	public static ArrayList<String> selectedTagList = new ArrayList<>();
	public static ViewHolder holder;
	public static int clikedItemPosition;
	public static String strCurrentCalendarId;
	static int requestFor;
	private static Context mContext;
	private static JSONObject RootJobject;
	private static int checkedCount = 0;
	View.OnClickListener onClickListener;
	private int[] mRowStates;
	private String priviusDate;

	public RecycleListAdapter(Context mContext, int requestFor, ArrayList<JSONObject> list, View.OnClickListener onClickListener) {
		this.mContext = mContext;
		this.requestFor = requestFor;
		this.jsonList = list;
		this.onClickListener = onClickListener;
		mRowStates = new int[jsonList.size()];
		Log.e("RecycleListAdapter: ", " Events Recycle adapert called.: ");

	}

	private static void setTeg(final ViewHolder holder, final int pos) {
		try {
			final String summary = jsonList.get(pos).getString("summary");
			holder.checkBox.setText(summary);
			holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
					if (isChecked) {
						clikedItemPosition = holder.getAdapterPosition();
						holder.relativeLayout.setVisibility(View.VISIBLE);
						if (checkedCount == 0) {
							ExpandView.expand(CalendarListActivity.btnSaveTags);
						}
						checkedCount++;
						if (RootjArray.isNull(pos)) {
							holder.isObjectExist = false;
						} else {
							try {
								String calendarId = RootjArray.getJSONObject(pos).getString("id");
								if (calendarId.equals(summary)) {
									holder.isObjectExist = true;
								} else {
									holder.isObjectExist = false;
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

					} else {
						checkedCount--;
						clikedItemPosition = holder.getAdapterPosition();
						if (checkedCount == 0) {
							ExpandView.collapse(CalendarListActivity.btnSaveTags);
						}
						holder.relativeLayout.setVisibility(View.GONE);
						holder.tagContainerLayout.removeAllTags();
						try {
							String listCallId = jsonList.get(clikedItemPosition).getString("id");
							for (int calendarPos = 0; calendarPos < RootjArray.length(); calendarPos++) {
								JSONObject jsonObject = RootjArray.getJSONObject(calendarPos);
								String calId = jsonObject.getString("id");
								Log.e("calId : ", calId + " summary : " + listCallId);
								if (calId.equals(listCallId)) {
									RootjArray.remove(calendarPos);
									break;
								}
							}
							if (holder.listHashMap.containsKey(clikedItemPosition)) {
								HashMap<Integer, List<Integer>> map = holder.listHashMap.get(clikedItemPosition);
								Log.e("map.size() : ", map.size() + "");
								for (int i = 0; i < map.size(); i++) {
									map.get(i).clear();
								}
								map.clear();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
						Log.e("RootjArray ", RootjArray.toString());
						Log.e("listHashMap  ", holder.listHashMap.toString());
					}
				}
			});
			holder.editText.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					clikedItemPosition = holder.getAdapterPosition();
					RecycleListAdapter.holder = holder;
					try {
						strCurrentCalendarId = jsonList.get(clikedItemPosition).getString("id");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					Log.e("clikedItemPosition : ", clikedItemPosition + "");
					if (CalendarListActivity.dialog != null && !CalendarListActivity.dialog.isShowing()) {
						if (ExpandableListAdapter.viewHolder != null) {
							ExpandableListAdapter.viewHolder.notifyDataChange();
						}
						CalendarListActivity.dialog.show();
						selectedTagList.clear();
					}
				}
			});
			holder.tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
				@Override
				public void onTagClick(final int position, final String text) {
					clikedItemPosition = holder.getAdapterPosition();
					RecycleListAdapter.holder = holder;
					AlertDialog dialog = new AlertDialog.Builder(mContext, R.style.MyDialogTheme)
							.setTitle("Delete !!!")
							.setMessage("Are you sure want to delete this tag?")
							.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									holder.tagContainerLayout.removeTag(position);
									try {
										String Id = jsonList.get(clikedItemPosition).getString("id");
										LoopDelArrayItem:
										{
											for (int calendarPos = 0; calendarPos < RootjArray.length(); calendarPos++) {
												JSONObject jsonObject = RootjArray.getJSONObject(calendarPos);
												String calId = jsonObject.getString("id");
												if (calId.equals(Id)) {
													JSONArray jsonArray = jsonObject.getJSONArray("tags");
													for (int k = 0; k < jsonArray.length(); k++) {
														if (jsonArray.getJSONObject(k).getString("tag_title").equals(text)) {
															Log.e("json item remove * ", jsonArray.getJSONObject(k).getString("tag_title"));
															jsonArray.remove(k);
															Log.e("jsonArray atfer remove ", jsonArray.toString());
															break LoopDelArrayItem;
														}
													}
												}
											}
										}

										HashMap<Integer, List<Integer>> map = RecycleListAdapter.holder.listHashMap.get(RecycleListAdapter.clikedItemPosition);
										LoopDelHashItem:
										{
											for (int i = 0; i < ExpandableListAdapter.dataList.size(); i++) {      // i == Expandable titlePosition
												JSONArray jArray = ExpandableListAdapter.dataList.get(i).getJSONArray("tags");
												for (int j = 0; j < jArray.length(); j++) {
													Log.e("jArray.getString(j) ", jArray.getString(j) + " == " + text);
													if (jArray.getString(j).equals(text)) {
														for (int k = 0; k < map.get(i).size(); k++) {
															Log.e("map count ", map.get(i).get(k) + "");
															if (map.get(i).get(k) == j) {
																Log.e("remove item **", map.get(i).get(k) + "");
																map.get(i).remove(k);
																Log.e("map atfer remove ", map.toString());
																break LoopDelHashItem;
															}
														}
													}
												}
											}
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
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
				public void onTagLongClick(final int position, String text) {
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void TagList() {
		LoadTags(holder);
	}

	private static void LoadTags(final ViewHolder holder) {
		final String CallId;
		try {
			//summary = jsonList.get(clikedItemPosition).getString("summary");
			CallId = jsonList.get(clikedItemPosition).getString("id");
			for (int i = 0; i < selectedTagList.size(); i++) {
				try {
					Log.e("isObjectExist ***", holder.isObjectExist + "");
					if (!holder.isObjectExist) {
						RootJobject = new JSONObject();
						RootJobject.put("id", CallId);
						JSONArray jsonArrayTag = new JSONArray();
						JSONObject jsonObjectTag = new JSONObject();
						jsonObjectTag.put("tag_title", selectedTagList.get(i));
						jsonArrayTag.put(jsonObjectTag);

						RootJobject.put("tags", jsonArrayTag);
						RootjArray.put(RootjArray.length(), RootJobject);
						Log.e("RootjArray ", " Create new Object :  " + RootjArray.toString());
						holder.isObjectExist = true;
					} else {
						for (int j = 0; j < RootjArray.length(); j++) {
							JSONObject jsonObject = RootjArray.getJSONObject(j);
							String id = jsonObject.getString("id");
							if (id.equals(CallId)) {
								JSONArray jsonArray = jsonObject.getJSONArray("tags");
								JSONObject tagjObject = new JSONObject();
								tagjObject.put("tag_title", selectedTagList.get(i));
								jsonArray.put(tagjObject);
								Log.e("jsonArray ", "added Object : " + jsonArray.toString());
							}
						}
						Log.e("RootjArray", "complete list : " + RootjArray.toString());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			for (int k = 0; k < RootjArray.length(); k++) {
				JSONObject jsonObject = RootjArray.getJSONObject(k);
				String id = jsonObject.getString("id");
				if (id.equals(CallId)) {
					JSONArray array = jsonObject.getJSONArray("tags");
					if (holder.tagContainerLayout.getChildCount() > 0) {
						holder.tagContainerLayout.removeAllTags();
					}
					for (int l = 0; l < array.length(); l++) {
						JSONObject object = array.getJSONObject(l);
						holder.tagContainerLayout.addTag(object.getString("tag_title"));
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static String getDate(long milliSeconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("d-M-yyyy'T'h:mm a");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		int currentDate = calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.MONTH) + calendar.get(Calendar.YEAR);
		return formatter.format(calendar.getTime());
	}

	@Override
	public RecycleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = null;
		switch (requestFor) {
			case Constant.GET_CALENDAR_LIST:
				v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_calendar_list_item, parent, false);
				break;
			case Constant.GET_ALL_CALENDAR:
				v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_calendar_list_item, parent, false);
				break;
			case Constant.GET_EVETN_LIST:
				v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_event_list_item, parent, false);
				break;

			default:
				break;
		}
		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		switch (requestFor) {
			case Constant.GET_CALENDAR_LIST:
				setTeg(holder, position);

				break;
			case Constant.GET_ALL_CALENDAR:
				try {
					final String summary = jsonList.get(position).getString("summary");
					holder.checkBox.setText(summary);
					boolean isImported = jsonList.get(position).getBoolean("imported");
					if (isImported) {
						holder.checkBox.setChecked(true);
						holder.relativeLayout.setVisibility(View.VISIBLE);
						holder.etdrelativeLayout.setVisibility(View.INVISIBLE);
						clikedItemPosition = holder.getAdapterPosition();
						JSONObject jsonObject = jsonList.get(clikedItemPosition);
						try {
							JSONArray jsonArray = jsonObject.getJSONArray("tags");
							for (int k = 0; k < jsonArray.length(); k++) {
								if (jsonArray.getString(k).contains("null") || jsonArray.getString(k).equalsIgnoreCase("")) {

								} else {
									holder.tagContainerLayout.addTag(jsonArray.getString(k));
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (NullPointerException e) {
							e.printStackTrace();
						}
					}
					holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
							if (isChecked) {
								holder.relativeLayout.setVisibility(View.VISIBLE);
								holder.etdrelativeLayout.setVisibility(View.INVISIBLE);
								clikedItemPosition = holder.getAdapterPosition();
								JSONObject jsonObject = jsonList.get(clikedItemPosition);
								try {
									JSONArray jsonArray = jsonObject.getJSONArray("tags");
									for (int k = 0; k < jsonArray.length(); k++) {
										if (jsonArray.getString(k).contains("null") || jsonArray.getString(k).equalsIgnoreCase("")) {

										} else {
											holder.tagContainerLayout.addTag(jsonArray.getString(k));
										}
									}
								} catch (JSONException e) {
									e.printStackTrace();
								} catch (NullPointerException e) {
									e.printStackTrace();
								}
							} else {
								clikedItemPosition = holder.getAdapterPosition();
								holder.relativeLayout.setVisibility(View.GONE);
								holder.tagContainerLayout.removeAllTags();
							}
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();

				}
				break;
			case Constant.GET_EVETN_LIST:
				boolean showSeparator = false;
				try {
					JSONObject rootJsonObj = jsonList.get(position);
					String summary = rootJsonObj.getString("summary");
					JSONObject jObjStartTime = rootJsonObj.getJSONObject("start");
					JSONObject jObjEndTime = rootJsonObj.getJSONObject("end");
					long start = jObjStartTime.getLong("$date");
					long end = jObjEndTime.getLong("$date");
					String strStartDate = getDate(start);
					String strEndDate = getDate(end);
					String[] startDate = strStartDate.split("T");
					String[] endDate = strEndDate.split("T");
					String strDate = startDate[0];
					String[] spliteDate = strDate.split("-");
					int intMonth = Integer.parseInt(spliteDate[1]);
					String strMonth = CalendarData.getMonthAsString(intMonth - 1);

					holder.textView.setText(summary);
					holder.textView3.setText(startDate[1] + " To " + endDate[1]);
					holder.relativeLayout2.setOnClickListener(onClickListener);
					holder.relativeLayout2.setTag(rootJsonObj.toString());
					holder.rootReletiveLayout.setTag(strDate + "-" + position);

					if (rootJsonObj.has("location")) {
						holder.textView4.setText(rootJsonObj.getString("location"));
					} else {
						//holder.textView4.setVisibility(View.INVISIBLE);
					}

					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(jObjStartTime.getLong("$date"));
					SimpleDateFormat formatter = new SimpleDateFormat("d-M-yyyy");
					String date = formatter.format(calendar.getTime()).trim();

					// Show separator //
					switch (mRowStates[position]) {
						case SECTIONED_STATE:
							showSeparator = true;
							break;
						case REGULAR_STATE:
							showSeparator = false;
							break;
						default:
							if (position == 0) {
								showSeparator = true;
							} else {
								if (!date.equals(priviusDate)) {
									showSeparator = true;
								}
							}
							// Cache it
							mRowStates[position] = showSeparator ? SECTIONED_STATE : REGULAR_STATE;
							break;
					}
					if (showSeparator) {
						if (strDate.equals(CalendarData.getTodayCompleteDate())) {
							holder.textView2.setText("Today, " + strMonth + " " + spliteDate[0]);
						} else if (strDate.equals(CalendarData.getTommarowCompleteDate())) {
							holder.textView2.setText("Tomorrow, " + strMonth + " " + spliteDate[0]);
						} else {
							holder.textView2.setText(strMonth + " " + spliteDate[0]);
						}

						holder.textView2.setOnClickListener(onClickListener);
						holder.textView2.setTag(strDate + "-" + position);
						holder.relativeLayout.setVisibility(View.VISIBLE);
						if (position == 0)
							holder.view.setVisibility(View.GONE);
					} else {
						holder.relativeLayout.setVisibility(View.GONE);
					}
					priviusDate = date;

				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
		}
	}

	@Override
	public int getItemCount() {
		return jsonList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public HashMap<Integer, HashMap> listHashMap = new HashMap<>();
		public HashMap<Integer, List<Integer>> hashMap = new HashMap<>();
		public Map<Integer, List<Integer>> ListMapToCheck = new HashMap<>();
		//public List<Integer> hasList;
		public boolean isHasListExist = false;
		TextView textView, textView2, textView3, textView4;
		CheckBox checkBox;
		RelativeLayout relativeLayout, relativeLayout2, rootReletiveLayout, etdrelativeLayout;
		Button button;
		EditText editText;
		View view;
		TagContainerLayout tagContainerLayout;
		boolean isObjectExist = false;

		public ViewHolder(View itemView) {
			super(itemView);
			switch (requestFor) {
				case Constant.GET_CALENDAR_LIST:
					checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_calendar_list);
					relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout_dropdown_view);
					button = (Button) itemView.findViewById(R.id.btnAddTag);
					editText = (EditText) itemView.findViewById(R.id.edt_Tag);
					tagContainerLayout = (TagContainerLayout) itemView.findViewById(R.id.tagcontainerLayout);

					break;
				case Constant.GET_ALL_CALENDAR:
					checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_calendar_list);
					relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout_dropdown_view);
					etdrelativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout);
					button = (Button) itemView.findViewById(R.id.btnAddTag);
					editText = (EditText) itemView.findViewById(R.id.edt_Tag);
					button.setVisibility(View.INVISIBLE);
					//editText.setVisibility(View.INVISIBLE);
					tagContainerLayout = (TagContainerLayout) itemView.findViewById(R.id.tagcontainerLayout);

					break;
				case Constant.GET_EVETN_LIST:
					textView = (TextView) itemView.findViewById(R.id.txtEventName);
					textView2 = (TextView) itemView.findViewById(R.id.txtSectionDate);
					textView3 = (TextView) itemView.findViewById(R.id.txtEventTime);
					textView4 = (TextView) itemView.findViewById(R.id.txtAtLocation);
					relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout);
					relativeLayout2 = (RelativeLayout) itemView.findViewById(R.id.relative_layout2);
					rootReletiveLayout = (RelativeLayout) itemView.findViewById(R.id.rootReletiveLayout);
					view = (View) itemView.findViewById(R.id.viewheader);

					break;
				default:
					break;
			}
		}
	}
}

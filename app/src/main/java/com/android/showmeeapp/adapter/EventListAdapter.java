package com.android.showmeeapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.showmeeapp.R;
import com.android.showmeeapp.constant.Constant;
import com.android.showmeeapp.pojo.CalendarData;
import com.android.showmeeapp.ui.activities.CalendarListActivity;
import com.android.showmeeapp.util.FilterEventList;
import com.android.showmeeapp.views.ExpandView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * Created by yuva on 15/7/16.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> implements Filterable {

	private Context mContext;
	public ArrayList<JSONObject> jsonList = new ArrayList<>();
	View.OnClickListener onClickListener;
	int requestFor;
	private static final int SECTIONED_STATE = 1;
	private static final int REGULAR_STATE = 2;
	private int[] mRowStates;

	public class ViewHolder extends RecyclerView.ViewHolder {
		TextView textView, textView2;
		RelativeLayout relativeLayout,relativeLayout2;
		View view;

		public ViewHolder(View itemView) {
			super(itemView);

			switch (requestFor) {
				case Constant.GET_EVETN_LIST:
					textView = (TextView) itemView.findViewById(R.id.txtEventName);
					textView2 = (TextView) itemView.findViewById(R.id.txtSectionDate);
					relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout);
					relativeLayout2 = (RelativeLayout) itemView.findViewById(R.id.relative_layout2);
					view = (View) itemView.findViewById(R.id.viewheader);

					break;
				default:
					break;
			}
		}
	}

	public EventListAdapter(Context mContext, int requestFor, ArrayList<JSONObject> list, View.OnClickListener onClickListener) {
		this.mContext = mContext;
		this.requestFor = requestFor;
		this.jsonList = list;
		this.onClickListener = onClickListener;
		mRowStates = new int[jsonList.size()];
	}

	@Override
	public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = null;
		switch (requestFor) {
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
	public void onBindViewHolder(final ViewHolder holder, int position) {
		switch (requestFor) {
			case Constant.GET_EVETN_LIST:
				boolean showSeparator = false;

				if (jsonList.size() != 0) {
					try {
						holder.textView.setText(jsonList.get(position).getString("event"));

						// Show separator //
						String strDate = jsonList.get(position).getString("date");
						int date = Integer.parseInt(strDate);
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
									int previousdate = Integer.parseInt(jsonList.get(position - 1).getString("date"));
									if (date != previousdate) {
										showSeparator = true;
									}
								}
								// Cache it
								mRowStates[position] = showSeparator ? SECTIONED_STATE : REGULAR_STATE;
								break;
						}
						if (showSeparator) {
							if (Integer.parseInt(jsonList.get(position).getString("date")) == (CalendarData.getCurrentDayOfMonth())) {
								Log.e("Todays date : ", "" + jsonList.get(position).getString("date") + " day : " + CalendarData.getCurrentDayOfMonth());
								holder.textView2.setText("TODAY, " + jsonList.get(position).getString("month") + " " + jsonList.get(position).getString("date"));
							} else if (Integer.parseInt(jsonList.get(position).getString("date")) == ((CalendarData.getCurrentDayOfMonth()) + 1)) {
								Log.e("tomorrow date : ", "" + jsonList.get(position).getString("date") + " day : " + CalendarData.getCurrentDayOfMonth() + 1);
								holder.textView2.setText("TOMORROW, " + jsonList.get(position).getString("month") + " " + jsonList.get(position).getString("date"));
							} else {
								Log.e("date : ", "" + jsonList.get(position).getString("date") + " day : " + CalendarData.getCurrentDayOfMonth());
								holder.textView2.setText(jsonList.get(position).getString("month") + " " + jsonList.get(position).getString("date"));
							}
							holder.relativeLayout.setVisibility(View.VISIBLE);
							if (position == 0)
								holder.view.setVisibility(View.GONE);
						} else {
							holder.relativeLayout.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					holder.relativeLayout.setVisibility(View.GONE);
					holder.relativeLayout2.setVisibility(View.VISIBLE);
					holder.textView2.setText("CreateEvent not found !!!");
				}
				break;
			default:
				break;
		}
	}


	@Override
	public Filter getFilter() {
		return new FilterEventList(this, jsonList);
	}

	@Override
	public int getItemCount() {
		return jsonList.size();
	}

}

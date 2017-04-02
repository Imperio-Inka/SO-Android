package com.android.showmeeapp.util;

/**
 * Created by yuva on 25/7/16.
 */

import android.util.Log;
import android.widget.Filter;

import com.android.showmeeapp.adapter.EventListAdapter;
import com.android.showmeeapp.adapter.RecycleListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FilterEventList extends Filter {
	private final EventListAdapter mAdapter;
	ArrayList<JSONObject> oldList;
	ArrayList<JSONObject> filteredList;


	public FilterEventList(EventListAdapter mAdapter, ArrayList<JSONObject> contactList) {
		this.mAdapter = mAdapter;
		this.oldList = contactList;
		filteredList = new ArrayList<>();
	}

	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
		filteredList.clear();
		final FilterResults results = new FilterResults();

		if (constraint.length() == 0) {
			//filteredList.addAll(oldList);
			filteredList.clear();
		} else {
			for (int i = 0; i < oldList.size(); i++) {
				try {
					String date = oldList.get(i).getString("date") + " " + oldList.get(i).getString("month");
					Log.e("oldList date : ",date);
					Log.e("constraint date : ",constraint +"");
					//if (date.contains(constraint)) {
					if (date.equals(constraint)) {
						filteredList.add(oldList.get(i));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		results.values = filteredList;
		results.count = filteredList.size();
		return results;
	}

	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {
		mAdapter.jsonList.clear();
		mAdapter.jsonList.addAll((ArrayList<JSONObject>) results.values);
		mAdapter.notifyDataSetChanged();
	}
}

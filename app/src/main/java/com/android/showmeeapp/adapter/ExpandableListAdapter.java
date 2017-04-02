package com.android.showmeeapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.showmeeapp.R;
import com.android.showmeeapp.ui.activities.UpdateEventActivity;
import com.android.showmeeapp.ui.fragments.CreateEventFragment;
import com.android.showmeeapp.ui.fragments.MyTagsFragment;
import com.android.showmeeapp.util.AnimationUtils;
import com.android.showmeeapp.util.tagcontainer.TagContainerLayout;
import com.android.showmeeapp.util.tagcontainer.TagView;
import com.android.showmeeapp.views.ExpandView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by anandbose on 09/06/15.
 */
public class ExpandableListAdapter extends RecyclerView.Adapter<ExpandableListAdapter.ViewHolder> {
	public static Context mContext;
	public static ViewHolder viewHolder;
	public static ArrayList<JSONObject> dataList = new ArrayList<>();
	public static ArrayList<JSONObject> userTags = new ArrayList<>();
	public static int titlePosition;
	public int unSelectedTag = 100;
	List<Integer> tagPos = new ArrayList<>();
	ArrayList<String> userTagsLst = new ArrayList<>();
	//ArrayList<String> userFollowedTagsLst = new ArrayList<>();
	private int requestFor;

	public ExpandableListAdapter(Context context, ArrayList<JSONObject> data, ArrayList<JSONObject> userTags, int requestFor) {
		this.dataList = data;
		this.mContext = context;
		this.requestFor = requestFor;
		this.userTags = userTags;
		tagPos.clear();
	}

	@Override
	public ExpandableListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
		View v = null;
		v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_expandlist_group_item, parent, false);
		ViewHolder vhItem = new ViewHolder(v);
		return vhItem;
	}

	@Override
	public void onBindViewHolder(final ExpandableListAdapter.ViewHolder holder, final int titlePosition) {
		AnimationUtils.scaleXY(holder);
		userTagsLst.clear();
		this.viewHolder = holder;
		this.titlePosition = titlePosition;
		try {
			holder.header_title.setText(dataList.get(titlePosition).getString("category"));
			JSONArray jArray = dataList.get(titlePosition).getJSONArray("tags");
			for (int j = 0; j < jArray.length(); j++) {
				userTagsLst.add(jArray.getString(j));
			}

			holder.tagContainerLayout.setTags(userTagsLst);

			if (requestFor == 1) {
				if (RecycleListAdapter.holder.listHashMap.containsKey(RecycleListAdapter.clikedItemPosition)) {
					HashMap<Integer, List<Integer>> map = RecycleListAdapter.holder.listHashMap.get(RecycleListAdapter.clikedItemPosition);
					if (map.containsKey(titlePosition)) {
						Log.e("titlePosition  ; ", titlePosition + "");
						for (int i = 0; i < map.get(titlePosition).size(); i++) {
							Log.e("map.get(titlePos) ; ", map.get(titlePosition).get(i) + "");
							Log.e("position selected I ; ", i + "");
							holder.tagContainerLayout.getChildAt(map.get(titlePosition).get(i)).setSelected(true);
							holder.tagContainerLayout.getChildAt(map.get(titlePosition).get(i)).setBackground(mContext.getResources().getDrawable(R.drawable.selected_tag));
						}
					}
				}
			} else if (requestFor == 2) {
				for (int i = 0; i < userTagsLst.size(); i++) {
					Log.e("userTagsList  ", userTagsLst.get(i) + "  "+userTags.toString());

					if (userTags.toString().contains(userTagsLst.get(i)))
					{
						holder.tagContainerLayout.getChildAt(i).setSelected(true);
						holder.tagContainerLayout.getChildAt(i).setBackground(mContext.getResources().getDrawable(R.drawable.selected_tag));
					}

					MyTagsFragment.selectedTagList.add(userTagsLst.get(i));

				}
				if (MyTagsFragment.hashMap.containsKey(titlePosition)) {
					for (int i = 0; i < MyTagsFragment.hashMap.get(titlePosition).size(); i++) {
						holder.tagContainerLayout.getChildAt(MyTagsFragment.hashMap.get(titlePosition).get(i)).setSelected(false);
						holder.tagContainerLayout.getChildAt(MyTagsFragment.hashMap.get(titlePosition).get(i)).setBackgroundColor(mContext.getResources().getColor(R.color.transparant));
					}
				}

			} else if (requestFor == 3) {
				if (CreateEventFragment.hashMap.containsKey(titlePosition)) {
					for (int i = 0; i < CreateEventFragment.hashMap.get(titlePosition).size(); i++) {
						Log.e("position selected I ; ", i + "");
						holder.tagContainerLayout.getChildAt(CreateEventFragment.hashMap.get(titlePosition).get(i)).setSelected(true);
						holder.tagContainerLayout.getChildAt(CreateEventFragment.hashMap.get(titlePosition).get(i)).setBackground(mContext.getResources().getDrawable(R.drawable.selected_tag));
					}
				}
			} else if (requestFor == 4) {
				for (int j = 0; j < UpdateEventActivity.selectedTagList.size(); j++) {
					JSONArray array = dataList.get(titlePosition).getJSONArray("tags");
					for (int k = 0; k < array.length(); k++) {
						if (array.get(k).equals(UpdateEventActivity.selectedTagList.get(j))) {
							if (!holder.isCREATEeventHasListExist) {
								UpdateEventActivity.hasList = new ArrayList<Integer>();
								UpdateEventActivity.hasList.add(k);
								holder.isCREATEeventHasListExist = true;
							} else {
								UpdateEventActivity.hasList.add(k);
							}
							Log.e("titlePosition ***", titlePosition + " position : " + k + " holder.hasList : " + UpdateEventActivity.hasList.size());
							UpdateEventActivity.hashMap.put(titlePosition, UpdateEventActivity.hasList);
						}
					}
				}
				if (UpdateEventActivity.hashMap.containsKey(titlePosition)) {
					for (int i = 0; i < UpdateEventActivity.hashMap.get(titlePosition).size(); i++) {
						Log.e("position selected I ; ", i + "");
						holder.tagContainerLayout.getChildAt(UpdateEventActivity.hashMap.get(titlePosition).get(i)).setSelected(true);
						holder.tagContainerLayout.getChildAt(UpdateEventActivity.hashMap.get(titlePosition).get(i)).setBackground(mContext.getResources().getDrawable(R.drawable.selected_tag));
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		holder.header_title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (holder.isTanShow) {
					holder.isTanShow = false;
					ExpandView.setAnaimation(holder.tagContainerLayout, ExpandView.Expand);
				} else {
					holder.isTanShow = true;
					ExpandView.setAnaimation(holder.tagContainerLayout, ExpandView.Collapse);
				}
			}
		});

		holder.tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

			@Override
			public void onTagClick(int position, String text) {
				Log.e("position  ; ", position + "");
				if (holder.tagContainerLayout.getChildAt(position).isSelected()) {
					holder.tagContainerLayout.getChildAt(position).setBackgroundColor(mContext.getResources().getColor(R.color.transparant));
					holder.tagContainerLayout.getChildAt(position).setSelected(false);
					if (requestFor == 1) {
						HashMap<Integer, List<Integer>> map = RecycleListAdapter.holder.listHashMap.get(RecycleListAdapter.clikedItemPosition);
						for (int i = 0; i < map.get(titlePosition).size(); i++) {
							if (map.get(titlePosition).get(i) == position) {
								Log.e("map.get(title)  ", map.get(titlePosition).get(i) + "");
								map.get(titlePosition).remove(i);
								break;
							}
						}
						for (int j = 0; j < RecycleListAdapter.selectedTagList.size(); j++) {
							if (RecycleListAdapter.selectedTagList.get(j).equals(text)) {
								RecycleListAdapter.selectedTagList.remove(j);
								break;
							}
						}
						for (int k = 0; k < RecycleListAdapter.RootjArray.length(); k++) {
							try {
								JSONObject jsonObject = RecycleListAdapter.RootjArray.getJSONObject(k);
								String calId = jsonObject.getString("id");
								Log.e("calId : ", calId + " RecycleListAdapter.strCurrentCalendarId : " + RecycleListAdapter.strCurrentCalendarId);
								if (calId.equals(RecycleListAdapter.strCurrentCalendarId)) {
									JSONArray jsonArray = jsonObject.getJSONArray("tags");
									Log.e("jsonArray.size  ", jsonArray.length() + "");
									for (int tagPos = 0; tagPos < jsonArray.length(); tagPos++) {
										if (jsonArray.getJSONObject(tagPos).getString("tag_title").equals(text)) {
											Log.e("remove ** ", jsonArray.getJSONObject(tagPos).getString("tag_title"));
											jsonArray.remove(tagPos);
										}
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					} else if (requestFor == 2) {
						if (MyTagsFragment.selectedTagList.contains(text)) {
							MyTagsFragment.selectedTagList.remove(text);
						}
						holder.tagContainerLayout.getChildAt(position).setSelected(false);
						holder.tagContainerLayout.getChildAt(position).setBackgroundColor(mContext.getResources().getColor(R.color.transparant));

						List<Integer> list;
						if (!MyTagsFragment.hashMap.containsKey(titlePosition)) {
							list = new ArrayList<Integer>();
							list.add(position);
						} else {
							list = MyTagsFragment.hashMap.get(titlePosition);
							list.add(position);
						}
						MyTagsFragment.hashMap.put(titlePosition, list);


					} else if (requestFor == 3) {
						for (int j = 0; j < CreateEventFragment.selectedTagList.size(); j++) {
							if (CreateEventFragment.selectedTagList.get(j).equals(text)) {
								CreateEventFragment.selectedTagList.remove(j);
							}
						}
						if (CreateEventFragment.hashMap.containsKey(titlePosition)) {
							List<Integer> list = CreateEventFragment.hashMap.get(titlePosition);
							for (int i = 0; i < list.size(); i++) {
								if (list.get(i) == position) {
									list.remove(i);
									break;
								}
							}
						}
					} else if (requestFor == 4) {
						for (int j = 0; j < UpdateEventActivity.selectedTagList.size(); j++) {
							if (UpdateEventActivity.selectedTagList.get(j).equals(text)) {
								UpdateEventActivity.selectedTagList.remove(j);
							}
						}
						if (UpdateEventActivity.hashMap.containsKey(titlePosition)) {
							List<Integer> list = UpdateEventActivity.hashMap.get(titlePosition);
							for (int i = 0; i < list.size(); i++) {
								if (list.get(i) == position) {
									list.remove(i);
									break;
								}
							}
						}
					}

					//Log.e("listsize after remove: ", RecycleListAdapter.selectedTagList.size() + "");
				} else {
					holder.tagContainerLayout.getChildAt(position).setSelected(true);
					holder.tagContainerLayout.getChildAt(position).setBackground(mContext.getResources().getDrawable(R.drawable.selected_tag));
					if (requestFor == 1) {
						List<Integer> list;
						RecycleListAdapter.selectedTagList.add(text);
						if (!RecycleListAdapter.holder.ListMapToCheck.containsKey(titlePosition)) {
							list = new ArrayList<Integer>();
							list.add(position);
						} else {
							list = RecycleListAdapter.holder.ListMapToCheck.get(titlePosition);
							list.add(position);
						}

						Log.e("titlePosition  :::: ", titlePosition + " position : " + position + " ListsSeze : " + list.size());
						RecycleListAdapter.holder.ListMapToCheck.put(titlePosition, list);
						Log.e("ListMapToCheck ***", RecycleListAdapter.holder.ListMapToCheck.get(titlePosition).toString());
						RecycleListAdapter.holder.hashMap.put(titlePosition, RecycleListAdapter.holder.ListMapToCheck.get(titlePosition));
						RecycleListAdapter.holder.listHashMap.put(RecycleListAdapter.clikedItemPosition, RecycleListAdapter.holder.hashMap);

					} else if (requestFor == 2) {
						if (!MyTagsFragment.selectedTagList.contains(text)) {
							MyTagsFragment.selectedTagList.add(text);
						}

						holder.tagContainerLayout.getChildAt(position).setSelected(true);
						holder.tagContainerLayout.getChildAt(position).setBackground(mContext.getResources().getDrawable(R.drawable.selected_tag));
						if (MyTagsFragment.hashMap.containsKey(titlePosition)) {
							if (MyTagsFragment.hashMap.get(titlePosition).contains(position)) {
								List<Integer> list = MyTagsFragment.hashMap.get(titlePosition);
								for (int i = 0; i < list.size(); i++) {
									if (list.get(i) == position) {
										list.remove(i);
										break;
									}
								}
							}
						}

					} else if (requestFor == 3) {
						CreateEventFragment.selectedTagList.add(text);
						if (!holder.isCREATEeventHasListExist) {
							CreateEventFragment.hasList = new ArrayList<Integer>();
							CreateEventFragment.hasList.add(position);
							holder.isCREATEeventHasListExist = true;
						} else {
							CreateEventFragment.hasList.add(position);
						}
						Log.e("titlePosition ***", titlePosition + " position : " + position + " holder.hasList : " + CreateEventFragment.hasList.size());
						CreateEventFragment.hashMap.put(titlePosition, CreateEventFragment.hasList);
					} else if (requestFor == 4) {
						UpdateEventActivity.selectedTagList.add(text);
						if (!holder.isCREATEeventHasListExist) {
							UpdateEventActivity.hasList = new ArrayList<Integer>();
							UpdateEventActivity.hasList.add(position);
							holder.isCREATEeventHasListExist = true;
						} else {
							UpdateEventActivity.hasList.add(position);
						}
						Log.e("titlePosition ***", titlePosition + " position : " + position + " holder.hasList : " + UpdateEventActivity.hasList.size());
						UpdateEventActivity.hashMap.put(titlePosition, UpdateEventActivity.hasList);
					}
					tagPos.add(position);
				}
			}

			@Override
			public void onTagLongClick(int position, String text) {

			}
		});
	}

	@Override
	public int getItemCount() {
		return dataList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public TextView header_title;
		public TagContainerLayout tagContainerLayout;
		public boolean isTanShow = true;
		public boolean isCREATEeventHasListExist = false;
		public List<Integer> hasList;

		public ViewHolder(View itemView) {
			super(itemView);
			header_title = (TextView) itemView.findViewById(R.id.txt_header_title);
			tagContainerLayout = (TagContainerLayout) itemView.findViewById(R.id.tagcontainerLayout);
			tagContainerLayout.setTheme(unSelectedTag);
		}

		public void notifyDataChange() {
			notifyDataSetChanged();
		}

		public void notifyItemChange(int pos) {
			notifyItemChanged(pos);
		}
	}
}

/*

if (!RecycleListAdapter.holder.isHasListExist) {
		RecycleListAdapter.holder.hasList = new ArrayList<Integer>();
		RecycleListAdapter.holder.hasList.add(position);
		RecycleListAdapter.holder.isHasListExist = true;
		} else {
		RecycleListAdapter.holder.hasList.add(position);
		}
		Log.e("titlePosition ***", titlePosition + " position : " + position + " holder.hasList : " + RecycleListAdapter.holder.hasList.size());
		RecycleListAdapter.holder.hashMap.put(titlePosition, RecycleListAdapter.holder.hasList);
		RecycleListAdapter.holder.listHashMap.put(RecycleListAdapter.clikedItemPosition, RecycleListAdapter.holder.hashMap);
*/

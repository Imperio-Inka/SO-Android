package com.android.showmeeapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.showmeeapp.R;
import com.android.showmeeapp.pojo.MyContactsDetails;
import com.android.showmeeapp.ui.activities.UpdateEventActivity;
import com.android.showmeeapp.ui.fragments.CreateEventFragment;

import java.util.ArrayList;


/**
 * Created by yuva on 15/7/16.
 */
public class MyContactsListAdapter extends RecyclerView.Adapter<MyContactsListAdapter.ViewHolder> {

	public static ArrayList<MyContactsDetails> list = new ArrayList<>();
	private static Context mContext;
	View.OnClickListener onClickListener;
	boolean[] itemChecked;

	public MyContactsListAdapter(Context mContext, ArrayList<MyContactsDetails> list, View.OnClickListener onClickListener) {
		this.mContext = mContext;
		this.list = list;
		this.onClickListener = onClickListener;
		this.itemChecked = new boolean[list.size()];
	}

	@Override
	public MyContactsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = null;
		v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contacts_list_item, parent, false);

		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		if (list.get(position).getName() != (null))
			holder.textView.setText(list.get(position).getName());
		if (list.get(position).getContact() != (null))
			holder.textView2.setText(list.get(position).getContact());
		if (list.get(position).getName() != null)
			holder.textView3.setText(list.get(position).getEmail());
		holder.checkBox.setChecked(false);

		if (itemChecked[position]) {
			holder.checkBox.setChecked(true);
		}

		holder.checkBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (holder.checkBox.isChecked()) {
					itemChecked[position] = true;
					CreateEventFragment.selectedMYContactList.add(list.get(position).getContact());
					UpdateEventActivity.selectedMYContactList.add(list.get(position).getContact());
				} else {
					itemChecked[position] = false;
					for (int i = 0; i < CreateEventFragment.selectedMYContactList.size(); i++) {
						if (CreateEventFragment.selectedMYContactList.get(i).equals(list.get(position).getContact())) {
							CreateEventFragment.selectedMYContactList.remove(i);
						}
					}
					for (int i = 0; i < UpdateEventActivity.selectedMYContactList.size(); i++) {
						if (UpdateEventActivity.selectedMYContactList.get(i).equals(list.get(position).getContact())) {
							UpdateEventActivity.selectedMYContactList.remove(i);
						}
					}
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		TextView textView, textView2, textView3;
		CheckBox checkBox;
		Button button;

		public ViewHolder(View itemView) {
			super(itemView);
			textView = (TextView) itemView.findViewById(R.id.txtContactName);
			textView2 = (TextView) itemView.findViewById(R.id.txtContactNumber);
			textView3 = (TextView) itemView.findViewById(R.id.txtContactEmail);
			checkBox = (CheckBox) itemView.findViewById(R.id.checkBox1);
		}
	}
}

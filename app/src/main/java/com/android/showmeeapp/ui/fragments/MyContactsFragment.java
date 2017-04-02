package com.android.showmeeapp.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.showmeeapp.R;
import com.android.showmeeapp.adapter.MyContactsListAdapter;
import com.android.showmeeapp.pojo.MyContactsDetails;
import com.android.showmeeapp.ui.database.SqliteDatabase;

import java.util.ArrayList;


/**
 * Created by admin on 12/08/16.
 */
public class MyContactsFragment extends Fragment implements View.OnClickListener {

	private View rootView;
	private Context mContext;
	private ArrayList<MyContactsDetails> contactList = new ArrayList<>();
	private MyContactsDetails fatchContacts;
	private RecyclerView mRecyclerView;
	private LinearLayoutManager layoutManager;
	private SqliteDatabase db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_mycontacts, container, false);
		mContext = this.getActivity();
		db = new SqliteDatabase(mContext);
		db.open();
		init();

		return rootView;
	}

	private void init() {
		mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
		mRecyclerView.setHasFixedSize(true);
		layoutManager = new LinearLayoutManager(mContext);
		mRecyclerView.setLayoutManager(layoutManager);

		Cursor cursor = db.getMYContact();
		while (cursor.moveToNext()) {
			fatchContacts = new MyContactsDetails(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
			contactList.add(fatchContacts);
		}

		MyContactsListAdapter myContactsListAdapter = new MyContactsListAdapter(mContext, contactList, this);
		myContactsListAdapter.notifyDataSetChanged();
		mRecyclerView.setAdapter(myContactsListAdapter);

	}

	@Override
	public void onClick(View view) {

	}

}




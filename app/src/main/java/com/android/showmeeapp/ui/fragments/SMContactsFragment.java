package com.android.showmeeapp.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.showmeeapp.R;
import com.android.showmeeapp.adapter.SmContactsListAdapter;
import com.android.showmeeapp.pojo.SMContactsDetails;
import com.android.showmeeapp.ui.database.SqliteDatabase;

import java.util.ArrayList;

/**
 * Created by admin on 12/08/16.
 */
public class SMContactsFragment extends Fragment implements View.OnClickListener {

	private View rootView;
	private Context mContext;
	private ArrayList<SMContactsDetails> contactList = new ArrayList<>();
	private SMContactsDetails fatchContacts;
	private RecyclerView mRecyclerView;
	private LinearLayoutManager layoutManager;
	private SqliteDatabase db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_smcontacts, container, false);
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

		Cursor cursor = db.getSMContact();
		while (cursor.moveToNext()) {fatchContacts = new SMContactsDetails(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
			contactList.add(fatchContacts);
		}

		SmContactsListAdapter smContactsListAdapter = new SmContactsListAdapter(mContext, contactList, this);
		smContactsListAdapter.notifyDataSetChanged();
		mRecyclerView.setAdapter(smContactsListAdapter);

	}

	@Override
	public void onClick(View view) {

	}

}

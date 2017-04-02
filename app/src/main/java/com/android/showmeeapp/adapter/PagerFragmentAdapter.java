package com.android.showmeeapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.showmeeapp.ui.fragments.MyContactsFragment;
import com.android.showmeeapp.ui.fragments.SMContactsFragment;

/**
 * Created by admin on 12/08/16.
 */
public class PagerFragmentAdapter extends FragmentPagerAdapter {

	int tabCount = 2;

	public PagerFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		//Returning the current tabs
		switch (position) {
			case 0:
				SMContactsFragment smContactsFragment = new SMContactsFragment();
				return smContactsFragment;
			case 1:
				MyContactsFragment myContactsFragment = new MyContactsFragment();
				return myContactsFragment;
			default:
				return null;
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case 0:
				return "SM Android";
			case 1:
				return "Invite Friends";
			default:
				return null;
		}
	}

	@Override
	public int getCount() {
		return tabCount;
	}
}

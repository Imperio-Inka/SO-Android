package com.android.showmeeapp.ui.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.showmeeapp.R;
import com.android.showmeeapp.ui.activities.DrawerHomeActivity;
import com.android.showmeeapp.ui.activities.SMLoginActivity;
import com.android.showmeeapp.ui.activities.TermAndConditionActivity;

import java.util.List;


/**
 * Created by kushahuja on 7/3/16.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

	private View rootView;
	private ActionBar actionBar;
	private View mToolbarView;
	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_settings, container, false);
		setActionBar();
		((ImageView) rootView.findViewById(R.id.imgOpenDrawer)).setOnClickListener(this);
		((TextView) rootView.findViewById(R.id.txtFragmentTitle)).setText(R.string.Settings);
		mContext = this.getActivity();
		init();
		return rootView;
	}

	private void init() {
		((TextView) rootView.findViewById(R.id.txtFeedback)).setOnClickListener(this);
		((TextView) rootView.findViewById(R.id.txtRatting)).setOnClickListener(this);
		((TextView) rootView.findViewById(R.id.txtTermAndCondition)).setOnClickListener(this);
		((TextView) rootView.findViewById(R.id.txtDeleteAccount)).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.imgOpenDrawer:
				DrawerHomeActivity.mDrawerLayout.openDrawer(DrawerHomeActivity.mRecyclerView);
				break;
			case R.id.txtFeedback:
				openGmail(new String[]{""}, "", "");
				break;
			case R.id.txtRatting:
				showRateDialogForRate(mContext);
				break;
			case R.id.txtTermAndCondition:
				startActivity(new Intent(getActivity(), TermAndConditionActivity.class));
				break;
			case R.id.txtDeleteAccount:
				Intent i = new Intent(getContext(), SMLoginActivity.class);
				startActivity(i);
				break;
			default:
				break;
		}
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

	public void openGmail(String[] email, String subject, String content) {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		emailIntent.setType("text/plain");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
		final PackageManager pm = getActivity().getPackageManager();
		final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
		ResolveInfo best = null;
		for (final ResolveInfo info : matches)
			if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
				best = info;
		if (best != null)
			emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
		startActivity(emailIntent);
	}

	private void showRateDialogForRate(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme)
				.setTitle("Rate")
				.setMessage("Please, Rate this app")
				.setPositiveButton("RATE", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (context != null) {
							Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
							Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
							// To count with Play market backstack, After pressing back button,
							// to taken back to our application, we need to add following flags to intent.
							goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
									Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
									Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
							try {
								context.startActivity(goToMarket);
							} catch (ActivityNotFoundException e) {
								context.startActivity(new Intent(Intent.ACTION_VIEW,
										Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
							}
						}
					}
				})
				.setNegativeButton("CANCEL", null);
		builder.show();
	}


}

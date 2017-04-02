package com.android.showmeeapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.showmeeapp.R;
import com.android.showmeeapp.constant.Constant;
import com.android.showmeeapp.model.UserDetailModel.UserModel;
import com.android.showmeeapp.ui.activities.DrawerHomeActivity;
import com.android.showmeeapp.ui.activities.GoogleLoginActivity;
import com.android.showmeeapp.ui.activities.SMLoginActivity;
import com.android.showmeeapp.ui.activities.TermAndConditionActivity;
import com.android.showmeeapp.util.PreferenceClass;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kushahuja on 8/14/16.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
	private View rootView;
	private ActionBar actionBar;
	private View mToolbarView;
	private Context mContext;

	String item = "";


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_profile_view, container, false);
		setActionBar();
		((ImageView) rootView.findViewById(R.id.imgOpenDrawer)).setOnClickListener(this);
		((TextView) rootView.findViewById(R.id.txtFragmentTitle)).setText(R.string.Settings);
		mContext = this.getActivity();
		init();
		return rootView;
	}

	private void init() {
		((EditText) rootView.findViewById(R.id.txt_user_name)).setOnClickListener(this);

		((ImageView) rootView.findViewById(R.id.img_profile)).setOnClickListener(this);
		((Button) rootView.findViewById(R.id.btn_login_google)).setOnClickListener(this);
		((Button) rootView.findViewById(R.id.btn_update)).setOnClickListener(this);

		UserModel user = UserModel.getInstance();
		if (UserModel.getInstance().getId() != null || UserModel.getInstance().getId() != "") {
			((Button) rootView.findViewById(R.id.btn_login_google)).setVisibility(View.GONE);
			((EditText) rootView.findViewById(R.id.txt_user_name)).setText(user.getServices().getGoogle().getGivenName() + " " + user.getServices().getGoogle().getFamilyName());
			String image = user.getServices().getGoogle().getPicture();
			image.replace("\\", "");
			new DownloadImageTask((ImageView) rootView.findViewById(R.id.img_profile)).execute(image);
			setSpinner();

		} else {
			((Button) rootView.findViewById(R.id.btn_login_google)).setVisibility(View.VISIBLE);

		}

	}


	@Override
	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.img_profile:
				break;
			case R.id.btn_login_google:
				if (item.equals("Select school")) {
					Toast.makeText(mContext, "Please select your school first", Toast.LENGTH_SHORT).show();
				} else {
					startActivity(new Intent(getContext(), GoogleLoginActivity.class));

				}
				break;
			case R.id.btn_update:
				startActivity(new Intent(getActivity(), TermAndConditionActivity.class));
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

	private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

	private void setSpinner() {
		Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
		// Spinner Drop down elements
		List<String> schoolList = new ArrayList<String>();
		schoolList.add("Select school");
		schoolList.add("Santa Clara University");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, schoolList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);

		spinner.setSelection(1);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				item = parent.getItemAtPosition(position).toString();
				PreferenceClass.setStringPreference(mContext, Constant.SELECTED_SCHOOL, item);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
	}
}

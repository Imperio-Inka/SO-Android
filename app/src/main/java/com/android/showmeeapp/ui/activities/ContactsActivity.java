package com.android.showmeeapp.ui.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.showmeeapp.R;
import com.android.showmeeapp.adapter.PagerFragmentAdapter;
import com.android.showmeeapp.constant.Constant;
import com.android.showmeeapp.pojo.FatchContacts;
import com.android.showmeeapp.ui.database.SqliteDatabase;
import com.android.showmeeapp.util.PreferenceClass;
import com.android.showmeeapp.util.ProgressHUD;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;


public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

	private Context mContext;
	private Meteor mMeteor;
	private ArrayList<FatchContacts> contactList = new ArrayList<>();
	private String[] contactsNumber;
	private SqliteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		setActionBar();
		mContext = this;
		db = new SqliteDatabase(mContext);
		db.open();

		db.deleteAllContact();
		Meteor.setLoggingEnabled(true);
		mMeteor = new Meteor(mContext, Constant.BASE_URL, new InMemoryDatabase());
		mMeteor.connect();

		new AsyncContacts().execute();

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
		ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
		PagerFragmentAdapter adapter = new PagerFragmentAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);

		/*if (!PreferenceClass.getBooleanPreferences(mContext, Constant.USER_CONTACT)) {
>>>>>>> 2aa58d2a5e6bd699882b5f2cbb9a5d6fece57525
			new AsyncContacts().execute();
		} else {
			TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
			tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
			ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
			PagerFragmentAdapter adapter = new PagerFragmentAdapter(getSupportFragmentManager());
			viewPager.setAdapter(adapter);
			tabLayout.setupWithViewPager(viewPager);
<<<<<<< HEAD
		}
			((TextView) findViewById(R.id.txtDone)).setOnClickListener(this);
=======
		}*/

		((TextView) findViewById(R.id.txtDone)).setOnClickListener(this);
	}

	private void setActionBar() {
		Toolbar mToolbarView = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar((Toolbar) mToolbarView);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {
		finish();
	}

	public class AsyncContacts extends AsyncTask<String, String, String> {
		ProgressHUD progress;

		@Override
		protected String doInBackground(String... strings) {
			try {
				StringBuffer sb = new StringBuffer();
				sb.append("......Contact Details.....");
				ContentResolver cr = getContentResolver();
				Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
				String phone = null;
				String emailContact = null;
				String emailType = null;
				String image_uri = "";
				Bitmap bitmap = null;

				if (cur.getCount() > 0) {
					while (cur.moveToNext()) {
						String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
						String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						image_uri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
						if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
							Log.e("name : ", name + ", ID : " + id);
							sb.append("\n Contact Name:" + name);
							Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
							while (pCur.moveToNext()) {
								phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								sb.append("\n Phone number:" + phone);
								Log.e("phone", phone);
							}
							pCur.close();
							Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
							while (emailCur.moveToNext()) {
								emailContact = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
								emailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
								sb.append("\nEmail:" + emailContact + "Email type:" + emailType);
								Log.e("Email ", emailContact + " Email Type : " + emailType);
							}
							emailCur.close();
						}
						if (image_uri != null) {
							//System.out.println(Uri.parse(image_uri));
							try {
								bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(image_uri));
								sb.append("\n Image in Bitmap:" + bitmap);
								System.out.println(bitmap);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						FatchContacts fatchContacts = new FatchContacts(name, phone, emailContact, image_uri);
						contactList.add(fatchContacts);
					}
				}

			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialogInterface) {

				}
			});
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			contactsNumber = new String[contactList.size()];
			for (int i = 0; i < contactList.size(); i++) {
				contactsNumber[i] = contactList.get(i).getContact();
			}
			mMeteor.call(Constant.METHOD_CHECKPHONE_NUMBERS, new Object[]{contactsNumber}, new ResultListener() {
				@Override
				public void onSuccess(String result) {
					if (progress.isShowing() && progress != null) {
						progress.dismiss();
					}
					String[] array = result.replace("[", "").replace("]", "").split(",");
					for (int i = 0; i < array.length; i++) {
						if (array[i].equals("true")) {
							db.insertSMContact(contactList.get(i).getName(), contactList.get(i).getContact(), contactList.get(i).getEmail(), contactList.get(i).getImage());
						} else {
							db.insertMYContact(contactList.get(i).getName(), contactList.get(i).getContact(), contactList.get(i).getEmail(), contactList.get(i).getImage());
						}
					}

					TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
					tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
					ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
					PagerFragmentAdapter adapter = new PagerFragmentAdapter(getSupportFragmentManager());
					viewPager.setAdapter(adapter);
					tabLayout.setupWithViewPager(viewPager);

					//PreferenceClass.setBooleanPreference(mContext, Constant.USER_CONTACT, true);
				}

				@Override
				public void onError(String error, String reason, String details) {
					if (progress.isShowing() && progress != null) {
						progress.dismiss();
					}
					Log.e("error  ::: ", "" + error + " reason " + reason);
				}
			});
		}
	}
}

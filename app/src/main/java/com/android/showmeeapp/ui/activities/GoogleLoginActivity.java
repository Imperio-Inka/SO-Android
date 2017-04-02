package com.android.showmeeapp.ui.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.showmeeapp.R;
import com.android.showmeeapp.constant.Constant;
import com.android.showmeeapp.util.MeteorOAuthServices;
import com.android.showmeeapp.util.PreferenceClass;
import com.android.showmeeapp.util.ProgressHUD;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

public class GoogleLoginActivity extends AppCompatActivity implements MeteorCallback {

	private WebView browser;
	private Meteor mMeteor;
	private GoogleApiClient client;
	private String credentialSecret;
	private String credentialToken;

	private Context mContext;
	private ProgressHUD progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_login);
		mContext = this;
		init();
	}

	private void init() {
		progress = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {

			}
		});

		Meteor.setLoggingEnabled(true);
		mMeteor = new Meteor(this, "wss://www.loopcowstudio.com/websocket", new InMemoryDatabase());
		mMeteor.addCallback(this);
		mMeteor.connect();
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	private class MyBrowser extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@TargetApi(Build.VERSION_CODES.KITKAT)
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			try {
				if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}
			} catch (Exception e) {

			}
			if (url.contains("https://www.loopcowstudio.com/_oauth/google?state")) {
				view.evaluateJavascript("JSON.parse(document.getElementById('config').innerHTML)", new ValueCallback<String>() {
					@Override
					public void onReceiveValue(String value) {
						System.out.println("value received" + value);

						try {
							JSONObject valueJson = new JSONObject(value);
							Log.d("Json", "Conversion completed" + valueJson.toString());
							credentialSecret = valueJson.getString("credentialSecret");
							credentialToken = valueJson.getString("credentialToken");
							Log.d("Json", "credential Secret: " + credentialSecret + " credential Token: " + credentialToken);

						} catch (JSONException e) {
							Log.d("Json", "Conversion failed");
							e.printStackTrace();
						}
					}
				});
			} else if (url.equalsIgnoreCase("https://www.loopcowstudio.com/_oauth/google")) {
				Log.d("matched", "Final url matched" + url);

				mMeteor.login(credentialToken, credentialSecret, new ResultListener() {
					@Override
					public void onSuccess(String result) {
						Log.d("Freaking", "Success off" + result);

						mMeteor.loginWithToken(credentialToken, new ResultListener() {
							@Override
							public void onSuccess(String result) {
								Log.d("Hello", "how are you" + result);
							}

							@Override
							public void onError(String error, String reason, String details) {

								String userId = mMeteor.getUserId();
								Log.d("mMeteor.getUserId() : ", userId + "");
								PreferenceClass.setStringPreference(mContext, Constant.USER_ID, userId);
								startActivity(new Intent(mContext,CalendarListActivity.class));
								finish();
							}
						});
					}

					@Override
					public void onError(String error, String reason, String details) {
						System.out.println("In login mMeteor.getUserId() is:  " + mMeteor.getUserId());
						Log.d("Freaking", "Buzz off " + error + "Reasons: " + reason + " details " + details);
					}
				});
			}
		}
	}

	private class MyChromeBrowser extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
		}
	}


	@Override
	public void onConnect(boolean signedInAutomatically) {
		System.out.println("Connected! Hell YEa");
		System.out.println("Is logged in: " + mMeteor.isLoggedIn());
		System.out.println("User ID: " + mMeteor.getUserId());
		browser = (WebView) findViewById(R.id.webview);

		browser.getSettings().setJavaScriptEnabled(true);
		browser.loadUrl(MeteorOAuthServices.google("asdf"));
//        browser.setWebChromeClient(new WebChromeClient());
		browser.setWebViewClient(new MyBrowser());

	}

	@Override
	public void onDisconnect() {

	}

	@Override
	public void onException(Exception e) {

	}

	@Override
	public void onDataAdded(String collectionName, String documentID, String newValuesJson) {

	}

	@Override
	public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {

	}

	@Override
	public void onDataRemoved(String collectionName, String documentID) {

	}


	@Override
	public void onStart() {
		super.onStart();
		client.connect();
	}

	@Override
	public void onStop() {
		super.onStop();
		client.disconnect();
	}
}

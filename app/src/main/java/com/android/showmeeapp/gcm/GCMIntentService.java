package com.android.showmeeapp.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.showmeeapp.R;
import com.android.showmeeapp.constant.Constant;
import com.android.showmeeapp.ui.activities.DrawerHomeActivity;
import com.android.showmeeapp.util.PreferenceClass;
import com.google.android.gcm.GCMBaseIntentService;


public class GCMIntentService extends GCMBaseIntentService {
static Context appContext;
	private static final String TAG = "GCMIntentService";
	int notificationId=0;
	public GCMIntentService() {
		super(CommonGCMUtility.SENDER_ID);

	}

	/**
	 * Method called on device registered
	 **/
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "My Own Device registered: regId = " + registrationId);
		PreferenceClass.setStringPreference(context, Constant.GCM_REGISTER_KEY, registrationId);
		CommonGCMUtility.registrationSuccess(context, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		CommonGCMUtility.displayMessage(context, getString(R.string.gcm_unregistered));
	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
			Log.i(TAG, "Received message");
			String message = null;
			appContext=context;
			final Bundle bundle = intent.getExtras();

			for (String key : bundle.keySet()) {
			System.out.println("gcm My key: " + key);
			System.out.println("gcm My message value: " + bundle.get(key));
			message=bundle.getString("message");

			generateNotification(context, message);
	    }
	}

	public void deleteLocally(Context context, String orderId) {
		// if ignore delete the db & remove notificaiton
		cancelNotification(context, 1);
		// doPlayStop();
		CommonGCMUtility.displayMessage(context, orderId);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		CommonGCMUtility.displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		CommonGCMUtility.displayMessage(context,getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		CommonGCMUtility.displayMessage(context,getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

    @SuppressWarnings("deprecation")
	private void generateNotification(Context mContext, String message) {


	    // intent triggered, you can add other intent for other actions
		Intent intent = new Intent(mContext, DrawerHomeActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mContext);



/*		.addAction(R.drawable.launcher_icon, "View", pIntent)
				.addAction(R.drawable.launcher_icon, "Remind", pIntent)*/


		Notification mNotification=null;

		mNotification = mBuilder.setSmallIcon(R.drawable.ic_launcher).setTicker(mContext.getResources().getString(R.string.app_name)).setWhen(0)
				.setAutoCancel(true)
				.setContentTitle(mContext.getResources().getString(R.string.app_name))
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
				.setContentIntent(pIntent)
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher))
				.setContentText(message).build();


		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		//notificationManager.notify(Integer.parseInt(status), mNotification);
		notificationManager.notify(100, mNotification);

	}

	public static void cancelNotification(Context context, int notificationId) {
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(notificationId);
	}

}

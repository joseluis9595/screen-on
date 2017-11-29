package com.jlaq.keepscreenon;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import static android.app.Notification.PRIORITY_HIGH;

public class MainActivity extends Activity {
	private Context context = this;

	// Constants for setting the screen timeout
	private final int SCREEN_ON = 1;
	private final int SCREEN_NORMAL = 2;

	// Previous saved screen timeout
	private MySharedPrefsManager mySharedPrefsManager;
	private int previousTimeout;
	private int actualTimeout;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mySharedPrefsManager = new MySharedPrefsManager(this);

		try {
			// Get previous saved timeout
			previousTimeout = mySharedPrefsManager.getTimeout();

			// Find out actual timeout of the device
			actualTimeout = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);

			// If actual timeout is set to MAX and no previous timeout was saved, show error
			if (previousTimeout == -1 && actualTimeout == Integer.MAX_VALUE) {
				Toast.makeText(this, "Screen is already set to ON all time", Toast.LENGTH_SHORT).show();
			} else {
				// Else change screen timeout
				changeScreenTimeout(actualTimeout == Integer.MAX_VALUE ? SCREEN_NORMAL : SCREEN_ON);
			}
		} catch (Settings.SettingNotFoundException e) {
			e.printStackTrace();
		}
		finish();
	}

	/**
	 * Changes the device's screen timeout
	 */
	public void changeScreenTimeout(int state) {
		switch (state) {
			case SCREEN_ON:
				// Save previous value
				mySharedPrefsManager.saveTimeout(actualTimeout);

				// Change screen timeout
				Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, Integer.MAX_VALUE);

				Toast.makeText(context, "Keeping screen on", Toast.LENGTH_SHORT).show();
				// Display notification informing the user
				displayNotification();
				break;

			case SCREEN_NORMAL:
				// Change screen timeout
				Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, previousTimeout);

				Toast.makeText(context, "Back to normal", Toast.LENGTH_SHORT).show();
				// Cancel onGoing notification
				cancelNotification();
				break;
		}
	}

	/**
	 * Displays a notification saying that the screen is on
	 * and when the user clicks it, it restores the user's previous
	 * screen timeout, saved in the constant previousScreenTimeout
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void displayNotification() {
		// Create intent pointing to main activity
		Intent notificationIntent = new Intent(context, MainActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		// Create pending intent for notification
		PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

		// Initialize the notification builder
		Notification.Builder mBuilder = new Notification.Builder(context)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setLargeIcon(bm)
				.setContentTitle("Screen is ON")
				.setContentText("Click here to go back to " + Util.getProperTime(previousTimeout))
				.setContentIntent(intent)
				.setPriority(PRIORITY_HIGH)
				.setOngoing(true);

		// Display notification
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		if (mNotificationManager != null) {
			mNotificationManager.notify(0, mBuilder.build());
		}
	}


	/**
	 * Cancels the notification previously created
	 */
	public void cancelNotification() {
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		if (notificationManager != null)
			notificationManager.cancel(0);
	}


}

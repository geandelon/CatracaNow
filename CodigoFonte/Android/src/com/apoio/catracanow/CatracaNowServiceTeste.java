package com.apoio.catracanow;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class CatracaNowServiceTeste extends IntentService {

	public CatracaNowServiceTeste() {
		super("Teste");
	}

	@Override
	protected void onHandleIntent(Intent intent)  {


		// This example will cause the phone to vibrate "SOS" in Morse Code
		// In Morse Code, "s" = "dot-dot-dot", "o" = "dash-dash-dash"
		// There are pauses to separate dots/dashes, letters, and words
		// The following numbers represent millisecond lengths
		int dot = 200;      // Length of a Morse Code "dot" in milliseconds
		int dash = 500;     // Length of a Morse Code "dash" in milliseconds
		int short_gap = 200;    // Length of Gap Between dots/dashes
		int medium_gap = 500;   // Length of Gap Between Letters
		int long_gap = 1000;    // Length of Gap Between Words
		long[] pattern = {
				0,  // Start immediately
				dot, short_gap, dot, short_gap, dot,    // s
				medium_gap,
				dash, short_gap, dash, short_gap, dash, // o
				medium_gap,
				dot, short_gap, dot, short_gap, dot,    // s
				long_gap,

				dot, short_gap, dot, short_gap, dot,    // s
				medium_gap,
				dash, short_gap, dash, short_gap, dash, // o
				medium_gap,
				dot, short_gap, dot, short_gap, dot,    // s
				long_gap
		};



		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("Passou na Catraca")
		.setContentText("Fulano passou na catraca horário tal.")
		.setVibrate(pattern)
		.setDefaults(Notification.FLAG_SHOW_LIGHTS) // barulho padrão de notifcação
		.setLights(0x00ff0000, 500, 200); // (cor do led, tempo em ms ligado, tempo em ms desligado)
		
		//0xff00ff00

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, mBuilder.build());

	}

}

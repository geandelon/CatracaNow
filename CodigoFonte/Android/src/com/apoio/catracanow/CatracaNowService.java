package com.apoio.catracanow;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class CatracaNowService extends IntentService {
	private static final String _NOME_SERVICE = "CatracaNowService";
	
	public CatracaNowService() {
		super(_NOME_SERVICE);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		// Servi�o processa, exibi notifica��es e
		// retorna a nova lista
		
	      // Normally we would do some work here, like download a file.
	      // For our sample, we just sleep for 5 seconds.
	      long endTime = System.currentTimeMillis() + 5*1000;
	      while (System.currentTimeMillis() < endTime) {
	          synchronized (this) {
	              try {
	                  wait(endTime - System.currentTimeMillis());
	              } catch (Exception e) {
	              }
	          }
	      }
	}
	
	@Override
	public void onCreate() {
		Toast.makeText(this, "CatracaNowService Criado", Toast.LENGTH_LONG).show();
		Log.d(_NOME_SERVICE, "onCreate");
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "CatracaNowService Finalizado", Toast.LENGTH_SHORT).show();
		Log.d(_NOME_SERVICE, "onDestroy");
		super.onDestroy();
	}
}

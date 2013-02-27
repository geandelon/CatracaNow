package com.apoio.catracanow;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.apoio.auxiliar.ControleDeAcesso;
import com.apoio.auxiliar.JSONParser;

public class CatracaNowService2 extends Service {
	private static final String TAG = "CatracaNowService";
	private Context _contexto;
	private ThreadCatracaNowService _sThread;
	private List<ControleDeAcesso> _listaAcessoAtual;
	private static final String _url = "http://10.0.1.81:23020/catracanow/Codigo/Servicos/ServicoControleDeAcesso.asmx/HelloWorld";
	private List<ControleDeAcesso> _listaAcessoAux;

	public CatracaNowService2() {

	}


	@Override
	public void onCreate() {
		Toast.makeText(this, "CatracaNowService Criado", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		_contexto = this;
		_sThread = new ThreadCatracaNowService();
		_listaAcessoAtual = new ArrayList<ControleDeAcesso>();
		_listaAcessoAux = new ArrayList<ControleDeAcesso>();
		_sThread.start();
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "CatracaNowService finalizou", Toast.LENGTH_SHORT).show();
		Log.d(TAG, "onDestroy");
	}

	private class ThreadCatracaNowService extends Thread {		
		@Override
		public void run() {
		
			try {
				_listaAcessoAtual = ControleDeAcesso.parse(consultarAcesso());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			while (true) {				
				try {
					
					Thread.sleep(5000);				
					// analisa se houve modificação
					// caso exista enviar notificação
					_listaAcessoAux = ControleDeAcesso.parse(consultarAcesso());
					
					if (_listaAcessoAtual.size() == 0)
						_listaAcessoAtual = _listaAcessoAux;
					else					
						gerarNotificacao();
					
				} catch (InterruptedException e) {
					Toast.makeText(_contexto, "Problema ao colocar a thread para dormir.", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(_contexto, e.getMessage(), Toast.LENGTH_SHORT).show();					
				}
			}
		}
		
		private void gerarNotificacao() {
			// compara as datas
			
			for (int i = 0; i < _listaAcessoAux.size(); i++) {
				
				//if (_listaAcessoAux.get(i).UltimoAcessoData.compareTo(_listaAcessoAtual.get(i).UltimoAcessoData) > 0)
				//{
					NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(_contexto)
					        .setSmallIcon(R.drawable.ic_launcher)
					        .setContentTitle("Passou na Catraca")
					        .setContentText("Fulano passou na catraca horário tal.");
					
					NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				    // Hide the notification after its selected
				    //noti.flags |= Notification.FLAG_AUTO_CANCEL;

				    notificationManager.notify(0, mBuilder.build());
					
				//}
				
			}
			
		}

		private JSONObject consultarAcesso() {
			try {
				return JSONParser.ObterDados(_url);
				
			} catch (Exception e) {
				Toast.makeText(_contexto, e.getMessage(), Toast.LENGTH_SHORT).show();
				return null;
			}
		}

	}

}

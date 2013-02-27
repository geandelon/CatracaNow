package com.apoio.catracanow;

import java.util.List;

import org.json.JSONException;

import com.apoio.auxiliar.ControleDeAcesso;
import com.apoio.auxiliar.MapeadorControleDeAcesso;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class CatracaNowService extends IntentService {

	private static final String _NOME_SERVICE = "CatracaNowService";
	private List<ControleDeAcesso> _listaAcessoAtual;
	private List<ControleDeAcesso> _listaAcessoAux;
	private MapeadorControleDeAcesso _mapeadorControleDeAcesso;
	private static final String _url = "http://10.0.1.81:23020/catracanow/Codigo/Servicos/ServicoControleDeAcesso.asmx/HelloWorld";


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

		// Servi�o, consulta, analisa, exibi notifica��es e retorna a nova lista

		_mapeadorControleDeAcesso = new MapeadorControleDeAcesso(this);
		try {
			_listaAcessoAux = ControleDeAcesso.parse(_mapeadorControleDeAcesso.Consulte(_url));
		} catch (JSONException e1) {
			Toast.makeText(this, e1.getMessage(), Toast.LENGTH_SHORT).show();		
		}
		
		if (_listaAcessoAtual != null) {
			analisarAcessos();
		}

	}

	private void analisarAcessos() {
		// compara as datas

		if (_listaAcessoAux != null) {



			for (int i = 0; i < _listaAcessoAux.size(); i++) {
				gerarNotificacao();
			}

		}

	}

	private void gerarNotificacao() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("Passou na Catraca")
		.setContentText("Fulano passou na catraca hor�rio tal.");

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Hide the notification after its selected
		//noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, mBuilder.build());
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

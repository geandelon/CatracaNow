package com.apoio.catracanow;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.apoio.auxiliar.ControleDeAcesso;
import com.apoio.auxiliar.MapeadorControleDeAcesso;

public class CatracaNowService extends IntentService {

	private static final String NOME_SERVICE = "CatracaNowService";
	public static final String ATUALIZACAO_ACESSOS_ACTIVITY = "ATUALIZACAO_ACESSOS_ACTIVITY";
	public static final String FINALIZOU_SERVICE = "FINALIZOU_SERVICE";
	public static final String VALOR = "ListaDeAcesso";
	private static final String _url = "http://10.0.1.81:23020/catracanow/Codigo/Servicos/ServicoControleDeAcesso.asmx/HelloWorld";
	private ArrayList<ControleDeAcesso> _listaAcessoAtual;
	private ArrayList<ControleDeAcesso> _listaAcessoAux;
	private MapeadorControleDeAcesso _mapeadorControleDeAcesso;

	public CatracaNowService() {
		super(NOME_SERVICE);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onHandleIntent(Intent intent) {
		_listaAcessoAtual = (ArrayList<ControleDeAcesso>) intent.getSerializableExtra(CatracaNowActivity.VALOR);

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

		enviarAcessos();
	}

	@Override
	public boolean stopService(Intent name) {
		return super.stopService(name);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "CatracaNowService Criado", Toast.LENGTH_LONG).show();
		Log.d(NOME_SERVICE, "onCreate");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "CatracaNowService Finalizado", Toast.LENGTH_SHORT).show();
		Log.d(NOME_SERVICE, "onDestroy");
		
		Intent intent = new Intent(FINALIZOU_SERVICE);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	private void analisarAcessos() {
		if (_listaAcessoAux != null) {
			for (int i = 0; i < _listaAcessoAux.size(); i++) {
				if (_listaAcessoAux.get(i).UltimoAcessoData.compareTo(_listaAcessoAtual.get(i).UltimoAcessoData) > 0) {
					gerarNotificacao();
				}
			}
		}
	}
	
	private void gerarNotificacao() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("Passou na Catraca")
		.setContentText("Fulano passou na catraca hor�rio tal.");

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, mBuilder.build());
	}
	
	private void enviarAcessos() {
		Intent intent = new Intent(ATUALIZACAO_ACESSOS_ACTIVITY);
		intent.putExtra(VALOR, _listaAcessoAux);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}
}

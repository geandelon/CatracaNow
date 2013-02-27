package com.apoio.catracanow;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.apoio.auxiliar.ControleDeAcesso;

public class CatracaNowActivity extends Activity {

	private Context _contexto;	
	private ArrayList<ControleDeAcesso> _listaAcessoAtual;
	private WorkerThread _wT = null;

	public static final String ATUALIZACAO_ACESSOS_SERVICE = "ATUALIZACAO_ACESSOS_SERVICE";
	public static final String VALOR = "ListaDeAcesso";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catraca_now);

		_contexto = this;
		LocalBroadcastManager.getInstance(_contexto).registerReceiver(mMessageReceiver, new IntentFilter(CatracaNowService.ATUALIZACAO_ACESSOS_ACTIVITY));

		// temos que iniciar e finalizar o servidor
		// quando estiver ligado
		// o serviço fica rodando e executa a consulta no servidor
		// a cada x segundos

		//((ToggleButton) findViewById(R.id.toggleButton)).setOnClickListener(btnAcertoOnClickListener);

		ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
		toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// The toggle is enabled
					//startService(new Intent(_contexto, CatracaNowService.class));

					criarWorkerThread();

				} else {
					// The toggle is disabled
					//stopService(new Intent(_contexto, CatracaNowService.class));
					interromperWorkerThread();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_catraca_now, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		interromperWorkerThread();
		LocalBroadcastManager.getInstance(_contexto).unregisterReceiver(mMessageReceiver);
	}

	private void criarWorkerThread() {
		if (_wT == null) {		
			_wT = new WorkerThread();
			_wT.run();
		}
	}

	private void interromperWorkerThread() {
		if (_wT != null) {
			Toast.makeText(this, "Interrompendo a Thread de gerenciamento de consulta.", Toast.LENGTH_LONG).show();
			Log.d("CatracaNow", "Interrompendo a Thread de gerenciamento de consulta.");
			_wT.interrupt();
			_wT = null;	
		}
	}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@SuppressWarnings("unchecked")
		@Override
		public void onReceive(Context context, Intent intent) {
			_listaAcessoAtual = (ArrayList<ControleDeAcesso>) intent.getSerializableExtra(CatracaNowService.VALOR);
		}
	};

	private class WorkerThread extends Thread {		

		@Override
		public void run() {
			
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Intent intent = new Intent(_contexto, CatracaNowService.class);
			intent.putExtra(VALOR, _listaAcessoAtual);
			startService(intent);
		}

		@Override
		protected void finalize() throws Throwable {
			super.finalize();
		}
	}

}

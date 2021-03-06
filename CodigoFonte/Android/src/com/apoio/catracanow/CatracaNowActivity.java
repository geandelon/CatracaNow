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
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

import com.apoio.auxiliar.ControleDeAcesso;

public class CatracaNowActivity extends Activity {

	private Context _contexto;	
	private ArrayList<ControleDeAcesso> _listaAcessoAtual;
	private WorkerThread _wT = null;
	public static final String VALOR = "ListaDeAcesso";
	private boolean _off = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catraca_now);

		_contexto = this;
		((ToggleButton) findViewById(R.id.toggleButton)).setOnClickListener(btnOnOffListener);
	}

	protected void onStart() {
		super.onStart();
		LocalBroadcastManager.getInstance(_contexto).registerReceiver(mMessageReceiver, new IntentFilter(CatracaNowService.ATUALIZACAO_ACESSOS_ACTIVITY));
		LocalBroadcastManager.getInstance(_contexto).registerReceiver(mMessageReceiverFinalizou, new IntentFilter(CatracaNowService.FINALIZOU_SERVICE));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//startActivity(new Intent(this, ConfiguracoesActivity.class));
		return true;
/*		switch (item.getItemId()) {
		case R.id.menu_parada:
			startActivity(new Intent(this, ParadaActivity.class));
			return true;	  
	    case R.id.menu_acerto:
	    	startActivity(new Intent(this, AcertoActivity.class));
	        return true;	  	        
	    case R.id.menu_configuracoes:
	    	Intent intent = new Intent(this, ConfiguracoesActivity.class);
	    	startActivity(intent);
	        return true;	
	    default:
	        return super.onOptionsItemSelected(item);
	    }*/
	}

	View.OnClickListener btnOnOffListener = new View.OnClickListener() {
		public void onClick(View v) {
			boolean on = ((ToggleButton) v).isChecked();

			if (on) {
				_off = false;
				criarWorkerThread();
			} else {
				_off = true;
				interromperWorkerThread();
			}
		}		
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_catraca_now, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		interromperWorkerThread();
		LocalBroadcastManager.getInstance(_contexto).unregisterReceiver(mMessageReceiver);
		LocalBroadcastManager.getInstance(_contexto).unregisterReceiver(mMessageReceiverFinalizou);
	}

	private void criarWorkerThread() {
		if (_wT != null)
			interromperWorkerThread();

		if (!_off) {
			_wT = new WorkerThread();
			_wT.run();
		}
	}

	private void interromperWorkerThread() {
		if (_wT != null) {
			//Toast.makeText(this, "Interrompendo a Thread de gerenciamento de consulta.", Toast.LENGTH_LONG).show();
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

	private BroadcastReceiver mMessageReceiverFinalizou = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			criarWorkerThread();
		}
	};

	private class WorkerThread extends Thread {
		@Override
		public void run() {
			//Intent intent = new Intent(_contexto, CatracaNowService.class);
			//intent.putExtra(VALOR, _listaAcessoAtual);
			//startService(intent);
			startService(new Intent(_contexto, CatracaNowServiceTeste.class));
		}

		@Override
		protected void finalize() throws Throwable {
			super.finalize();
		}
	}
}

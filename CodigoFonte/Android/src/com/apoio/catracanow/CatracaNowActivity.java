package com.apoio.catracanow;

import java.util.List;

import org.json.JSONException;

import com.apoio.auxiliar.ControleDeAcesso;
import com.apoio.auxiliar.MapeadorControleDeAcesso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CatracaNowActivity extends Activity {

	private Context _contexto;	
	private List<ControleDeAcesso> _listaAcessoAtual;
	private MapeadorControleDeAcesso _mapeadorControleDeAcesso;
	private static final String _url = "http://10.0.1.81:23020/catracanow/Codigo/Servicos/ServicoControleDeAcesso.asmx/HelloWorld";
	private WorkerThread _wT = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catraca_now);

		_contexto = this;
		_mapeadorControleDeAcesso = new MapeadorControleDeAcesso(_contexto);

		// temos que iniciar e finalizar o servidor
		// quando estiver ligado
		// o servi�o fica rodando e executa a consulta no servidor
		// a cada x segundos

		//((ToggleButton) findViewById(R.id.toggleButton)).setOnClickListener(btnAcertoOnClickListener);

		ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
		toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// The toggle is enabled
					//startService(new Intent(_contexto, CatracaNowService.class));

					_wT = new WorkerThread();
					_wT.run();

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
		interromperWorkerThread();
		super.onDestroy();
	}

	private void interromperWorkerThread() {
		if (_wT != null) {
			Toast.makeText(this, "Interrompendo a Thread de gerenciamento de consulta.", Toast.LENGTH_LONG).show();
			Log.d("CatracaNow", "Interrompendo a Thread de gerenciamento de consulta.");
			_wT.interrupt();
			_wT = null;	
		}
	}

	private class WorkerThread extends Thread {

		@Override
		public void run() {

			// s� gerencia as chamadas de tempo em tempo
			// e atualiza a listaAcessoAtual

			startService(new Intent(_contexto, CatracaNowService.class));

			/*			try {
				_listaAcessoAtual = ControleDeAcesso.parse(_mapeadorControleDeAcesso.Consulte(_url));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/

			/*			while (true) {				
				try {

					Thread.sleep(5000);				
					// analisa se houve modifica��o
					// caso exista enviar notifica��o
					_listaAcessoAux = ControleDeAcesso.parse(consultarAcesso());

					if (_listaAcessoAtual.size() == 0)
						_listaAcessoAtual = _listaAcessoAux;
					else					
						gerarNotificacao();

				} catch (InterruptedException e) {
					Toast.makeText(_contexto, "Problema ao colocar a thread para dormir.", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(_contexto, e.getMessage(), Toast.LENGTH_SHORT).show();					
				}*/


			super.run();
		}

	}

}

package com.apoio.catracanow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class CatracaNowActivity extends Activity {
	
	private Context contexto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catraca_now);
		
		contexto = this;
		
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
		        	startService(new Intent(contexto, CatracaNowService.class));
		        } else {
		            // The toggle is disabled
		        	stopService(new Intent(contexto, CatracaNowService.class));
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

}

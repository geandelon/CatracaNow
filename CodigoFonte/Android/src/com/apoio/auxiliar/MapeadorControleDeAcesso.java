package com.apoio.auxiliar;

import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

public class MapeadorControleDeAcesso {
	
	private Context _contexto;
	
	public MapeadorControleDeAcesso(Context pContexto) {
		this._contexto = pContexto;
	}
	
	public JSONObject Consulte(String pUrl) {
		try {
			return JSONParser.ObterDados(pUrl);
			
		} catch (Exception e) {
			Toast.makeText(_contexto, e.getMessage(), Toast.LENGTH_SHORT).show();
			return null;
		}
	}

}

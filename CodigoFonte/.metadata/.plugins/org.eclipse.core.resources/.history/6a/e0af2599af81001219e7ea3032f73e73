package com.apoio.auxiliar;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class ControleDeAcesso implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int Empresa;
    public int Filial;
    public int Codigo;
    public String Cracha;
    public String Nome;
    public int UltimoAcesso;
    public Date UltimoAcessoData;
    
	private static final String TAG_EMPRESA = "Empresa";
	private static final String TAG_FILIAL = "Filial";
	private static final String TAG_CODIGO = "Codigo";
	private static final String TAG_CRACHA = "Cracha";
	private static final String TAG_NOME = "Nome";
	private static final String TAG_ULTIMO_ACESSO = "UltimoAcesso";
	private static final String TAG_ULTIMO_ACESSO_DATA = "UltimoAcessoDataString";
	
	private static SimpleDateFormat parseDT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public static ArrayList<ControleDeAcesso> parse(JSONObject obj) throws JSONException {
		
		ArrayList<ControleDeAcesso> lista = new ArrayList<ControleDeAcesso>();
		JSONObject dado;
		ControleDeAcesso controle;
		
		JSONArray arr = obj.getJSONArray("d");
		for (int j = 0; j < arr.length(); j++) {
			dado = arr.getJSONObject(j);			
			controle = new ControleDeAcesso();
	        controle.Empresa = dado.getInt(TAG_EMPRESA);         
	        controle.Filial = dado.getInt(TAG_FILIAL);
	        controle.Codigo = dado.getInt(TAG_CODIGO);
	        controle.Cracha = dado.getString(TAG_CRACHA);
	        controle.Nome = dado.getString(TAG_NOME);
	        controle.UltimoAcesso = dado.getInt(TAG_ULTIMO_ACESSO);
	        try {
				controle.UltimoAcessoData = parseDT.parse(dado.getString(TAG_ULTIMO_ACESSO_DATA));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        lista.add(controle);
		}
		
        
        return lista;
	}
}

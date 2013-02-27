package com.apoio.auxiliar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/*    <customErrors mode="Off"/>
    <webServices>
      <protocols>
        <add name="HttpGet"/>
        <add name="HttpPost"/>
      </protocols>
    </webServices>*/

public class JSONParser {
	
/*	private static int CONNECTION_TIMEOUT = 50000;
	private static int DATARETRIEVAL_TIMEOUT = 50000;

	public static JSONObject ObterDados(String serviceUrl) {

		//disableConnectionReuseIfNecessary();

		HttpURLConnection urlConnection = null;
		try {
			// create connection
			URL urlToRequest = new URL(serviceUrl);
			urlConnection = (HttpURLConnection) 
					urlToRequest.openConnection();
			urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
			urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

			// handle issues
			int statusCode = urlConnection.getResponseCode();
			if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
				// handle unauthorized (if service requires user login)
			} else if (statusCode != HttpURLConnection.HTTP_OK) {
				// handle any other errors, like 404, 500,..
			}

			// create JSON object from content
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			return new JSONObject(getResponseText(in));

		} catch (MalformedURLException e) {
			// URL is invalid
		} catch (SocketTimeoutException e) {
			// data retrieval or connection timed out
		} catch (IOException e) {
			// could not read response body 
			// (could not create input stream)
		} catch (JSONException e) {
			// response body is no valid JSON string
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}		

		return null;
	}
	

    private static String getResponseText(InputStream inStream) {
    	// very nice trick from 
    	// http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
    	return new Scanner(inStream).useDelimiter("\\A").next();
    }*/
    
    
    
    
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
 
    public static JSONObject ObterDados(String url) throws Exception {
    	
		//JSONObject obj =  JSONParser.ObterDados2("http://10.0.1.86:23020/suite/servicos/ServicoPDA_SATAndroid.asmx/GetDelon");
		//String FirstName = obj.getString("FirstName");
 
        // Executando a requisição HTTP
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
                        
            httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
 
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();           
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {       	
            //e.printStackTrace();
        	//Log.e("Sem conexão", "Verifique sua conexão com a rede.");
        	throw new Exception(e);
        	//return null;
        } 
         

        // Convertendo a resposta para String
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Erro no buffer:", "Erro ao converter a resposta numa STRING JSON " + e.toString());
        }
 
        // tentativa de conversão da string num objeto JSON
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Erro ao converter a string num objeto JSON " + e.toString());
        }

        return jObj;
 
    }

}


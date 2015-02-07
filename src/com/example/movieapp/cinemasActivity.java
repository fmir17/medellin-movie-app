package com.example.movieapp;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class cinemasActivity extends Activity {

	JSONArray respJSON;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		obtenerCinemas obtenerPel = new obtenerCinemas();
		obtenerPel.execute(bundle.getString("salas"));
	}

	
	
	
	
	
	private class obtenerCinemas extends AsyncTask<String, Integer, String> {

		Context context;

		public obtenerCinemas(Context context) {
			this.context = context;
		}
		
		public obtenerCinemas() {
		}

		// Se modifica el metodo doInBackground para que retorne un objeto de
		// tipo ItemAdapter
		@Override
		protected String doInBackground(String... params) {
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet del = new HttpGet(
					"https://medellin-movie.herokuapp.com/cinema/" + params[0]);
			del.setHeader("content-type", "application/json");

			try {

				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				respJSON = new JSONArray(respStr);
				JSONObject obj;
				obj = respJSON.getJSONObject(0);
				JSONArray cinema = new JSONArray(obj.getString("cinema"));
				JSONObject obj2 = cinema.getJSONObject(0);
				String dato = obj2.getString("cinemaName");
				Log.i("Servicio cinemas", dato);
				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
			}
			
			
			 return "";

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}
}

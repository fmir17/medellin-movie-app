package com.example.movieapp;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView lblResultado;
	private Button bt1;
	private Button btgetPeliculas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lblResultado = (TextView) findViewById(R.id.lblResultado);
		bt1 = (Button) findViewById(R.id.btnGet2);
		btgetPeliculas = (Button) findViewById(R.id.btnGetPeliculas);

		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				obtenerPelicula obtenerPel = new obtenerPelicula();

				obtenerPel.execute();

			}
		});

		btgetPeliculas.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				obtenerListaPeliculas listaPeliculas = new obtenerListaPeliculas();

				listaPeliculas.execute();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 * @author sebastian.garciav Metodo que permite obtener la información de un
	 *         JSON
	 */
	private class obtenerPelicula extends AsyncTask<String, Integer, String> {
		private String title;
		private String[] peliculas;

		@Override
		protected String doInBackground(String... params) {

			boolean resul = true;
			HttpClient httpClient = new DefaultHttpClient();

			HttpGet del = new HttpGet(
					"https://pescadores-colombia-api.herokuapp.com/fishinglog/jennifer/Inimaginable");
			del.setHeader("content-type", "application/json");

			try {

				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());

				JSONObject respJSON = new JSONObject(respStr);

				title = respJSON.getString("title");

			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}

			return title;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			lblResultado.setText("" + title);
		}
	}

	/**
	 * 
	 * @author sebastian.garciav Metodo que permite Obtener todas las peliculas
	 */
	private class obtenerListaPeliculas extends
			AsyncTask<String, Integer, String> {
		private String title;
		private String[] peliculas;

		@Override
		protected String doInBackground(String... params) {
		
			boolean resul = true;
			HttpClient httpClient = new DefaultHttpClient();
			
			HttpGet del = new HttpGet(
					"https://medellin-movie.herokuapp.com/movie");
			del.setHeader("content-type", "application/json");
			
	

			try {

				
				
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());				
				JSONArray respJSON = new JSONArray(respStr);
				
				 peliculas = new String[respJSON.length()];
				 
				 System.out.println("Tamaño del arreglo="+respJSON.length());
				
				 for(int i=0; i<respJSON.length(); i++)
				 {
				 JSONObject obj = respJSON.getJSONObject(i);
				
				 
				 String title = obj.getString("title");
				 String url = obj.getString("url");
				
				 peliculas[i] = "" + title + "-" + url ;
				 
				 }
				 
			
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}

			return title;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			lblResultado.setText("" + peliculas[0]);
		}
	}

}

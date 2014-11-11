package com.example.movieapp;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoPelicula extends Activity {

	private TextView lblTitle, lblDescription,lblGenre,lblFormat, lblDirector;
	private String descripcion;
	//private WebView webView;
	
    static Point size;
    static float density;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_pelicula);

		lblTitle = (TextView) findViewById(R.id.lblTitle);
		//lblDescription = (TextView) findViewById(R.id.lblDescription);
		lblGenre = (TextView) findViewById(R.id.lblGenre);
		lblFormat = (TextView) findViewById(R.id.lblFormat);
		lblDirector = (TextView) findViewById(R.id.lblDirector);
		//webView = (WebView) findViewById(R.id.webView1);
		      
	
		Bundle bundle = getIntent().getExtras();
		
		obtenerPelicula obtenerPel = new obtenerPelicula();
		obtenerPel.execute(bundle.getString("identidad"));

	}

	/**
	 * 
	 * @author sebastian.garciav Metodo que permite obtener la información de un
	 *         JSON
	 */
	private class obtenerPelicula extends AsyncTask<String, Integer, String> {
		private String title;
		private String[] peliculas;
		JSONObject respJSON;
		private String idPelicula;
		@Override
		protected String doInBackground(String... params) {

			HttpClient httpClient = new DefaultHttpClient();

			idPelicula=  params[0];
			System.out.println(idPelicula);
			HttpGet del = new HttpGet(
					"https://medellin-movie.herokuapp.com/movie/" + idPelicula);
			del.setHeader("content-type", "application/json");

			try {

				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				System.out.println("json"+respStr);
				JSONArray respJSONArray = new JSONArray(respStr);
				respJSON = respJSONArray.getJSONObject(0);

			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);

			}

			return title;
		}
		
		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			try {
				lblTitle.setText("" + respJSON.getString("title"));
				//lblDescription.setText("" + respJSON.getString("description"));
				lblGenre.setText("" + respJSON.getString("genre"));
				lblFormat.setText("" + respJSON.getString("format"));
				lblDirector.setText("" + respJSON.getString("director"));
												
				/*descripcion = "<html><body>"
				+ "<p align=\"justify\">"                
				+ respJSON.getString("description")
				+ "</p> "
				+ "</body></html>";	
				webView.loadData(descripcion, "text/html", "utf-8");*/
				descripcion = respJSON.getString("description");
				JustifiedTextView jtv= new JustifiedTextView(getApplicationContext(), descripcion );
				LinearLayout place = (LinearLayout) findViewById(R.id.LayoutPrincipal);
				place.addView(jtv);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}

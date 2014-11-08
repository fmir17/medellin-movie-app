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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class InfoPelicula extends Activity {

	private TextView lblID, lblTitle, lblDescription, lblUrl, lblGenre,
			lblFormat, lblDirector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_pelicula);

		lblID = (TextView) findViewById(R.id.lblID);
		lblTitle = (TextView) findViewById(R.id.lblTitle);
		lblDescription = (TextView) findViewById(R.id.lblDescription);
		lblUrl = (TextView) findViewById(R.id.lblUrl);
		lblGenre = (TextView) findViewById(R.id.lblGenre);
		lblFormat = (TextView) findViewById(R.id.lblFormat);
		lblDirector = (TextView) findViewById(R.id.lblDirector);

		Bundle bundle = getIntent().getExtras();
		lblID.setText(bundle.getString("identidad"));
		
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
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				lblID.setText("" + respJSON.getString("id"));
				lblTitle.setText("" + respJSON.getString("title"));
				lblDescription.setText("" + respJSON.getString("description"));
				lblUrl.setText("" + respJSON.getString("urlImage"));
				lblGenre.setText("" + respJSON.getString("genre"));
				lblFormat.setText("" + respJSON.getString("format"));
				lblDirector.setText("" + respJSON.getString("director"));

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}

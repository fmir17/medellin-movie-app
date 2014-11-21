package com.example.movieapp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class InfoPelicula extends Activity {

	private TextView lblTitle, lblGenre, lblFormat, lblDirector;
	private String descripcion, link;
	private ImageView imagenPelicula;
	private Bitmap imagen;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_pelicula);

		lblTitle = (TextView) findViewById(R.id.lblTitle);
		lblGenre = (TextView) findViewById(R.id.lblGenre);
		lblFormat = (TextView) findViewById(R.id.lblFormat);
		lblDirector = (TextView) findViewById(R.id.lblDirector);
		imagenPelicula = (ImageView)findViewById(R.id.imagenPelicula);
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

			idPelicula = params[0];
			System.out.println(idPelicula);
			HttpGet del = new HttpGet(
					"https://medellin-movie.herokuapp.com/movie/" + idPelicula);
			del.setHeader("content-type", "application/json");

			try {
				System.out.println("Estoy en el primer try");
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				System.out.println("json" + respStr);
				JSONArray respJSONArray = new JSONArray(respStr);
				respJSON = respJSONArray.getJSONObject(0);
				link = respJSON.getString("urlImage");
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);

			}
			// Para cargar las imagenes con una url
			URL imageUrl = null;
			HttpURLConnection conn = null;
			try {
				System.out.print("Estoy en el ssegundo try");
				imageUrl = new URL(link);
				conn = (HttpURLConnection) imageUrl.openConnection();
				conn.connect();
				imagen = BitmapFactory.decodeStream(conn.getInputStream());


			} catch (IOException e) {

				e.printStackTrace();

			}

			return title;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			try {
				lblTitle.setText("" + respJSON.getString("title"));
				lblGenre.setText("" + respJSON.getString("genre"));
				lblFormat.setText("" + respJSON.getString("format"));
				lblDirector.setText("" + respJSON.getString("director"));
				descripcion = respJSON.getString("description");
				JustifiedTextView jtv = new JustifiedTextView(
						getApplicationContext(), descripcion);
				LinearLayout place = (LinearLayout) findViewById(R.id.LayoutPrincipal);
				place.addView(jtv);
				imagenPelicula.setImageBitmap(imagen);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
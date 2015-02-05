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
import android.graphics.PorterDuff.Mode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class InfoPelicula extends Activity implements OnClickListener{

	private TextView lblTitle, lblGenre, lblFormat, lblDirector,lblCalificacion,lblReparto;
	private String descripcion, link, video;
	private ImageView imagenPelicula;
	private Bitmap imagen;
	private RatingBar stars;
	private Button trailer,salas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_pelicula);

		lblTitle = (TextView) findViewById(R.id.lblTitle);
		lblGenre = (TextView) findViewById(R.id.lblGenre);
		lblFormat = (TextView) findViewById(R.id.lblFormat);
		lblDirector = (TextView) findViewById(R.id.lblDirector);
		imagenPelicula = (ImageView)findViewById(R.id.imagenPelicula);
		lblCalificacion = (TextView)findViewById(R.id.lblCalificacion);
		lblReparto = (TextView)findViewById(R.id.lblReparto);
		trailer = (Button)findViewById(R.id.btnTrailer);
		salas = (Button)findViewById(R.id.btnSalas);
		stars = (RatingBar)findViewById(R.id.ratingbar);
		
		trailer.setOnClickListener(this);
		salas.setOnClickListener(this);
		stars.setEnabled(false);
		imagenPelicula.setImageBitmap(imagen);
		
		Bundle bundle = getIntent().getExtras();
		obtenerPelicula obtenerPel = new obtenerPelicula();
		obtenerPel.execute(bundle.getString("identidad"));
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()== R.id.btnTrailer){
			Log.i("log", "Estoy en el condicional del video");
			Intent intent = new Intent(InfoPelicula.this,Video.class);
			intent.putExtra("video", video);
			startActivity(intent);
		}
	}

	/**
	 * 
	 * @author sebastian.garciav Metodo que permite obtener la información de un
	 *         JSON
	 */
	private class obtenerPelicula extends AsyncTask<String, Integer, String> {
		JSONObject respJSON;
		private String idPelicula;

		@Override
		protected String doInBackground(String... params) {

			HttpClient httpClient = new DefaultHttpClient();
			idPelicula = params[0];
			HttpGet del = new HttpGet(
					"https://medellin-movie.herokuapp.com/movie/" + idPelicula);
			del.setHeader("content-type", "application/json");

			try {
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
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
				imageUrl = new URL(link);
				conn = (HttpURLConnection) imageUrl.openConnection();
				conn.connect();
				imagen = BitmapFactory.decodeStream(conn.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			try {
				lblTitle.setText("" + respJSON.getString("title"));
				lblGenre.setText("" + respJSON.getString("genre"));
				lblFormat.setText("" + respJSON.getString("format"));
				lblDirector.setText("" + respJSON.getString("director"));
				lblReparto.setText("" + respJSON.getString("cast"));
				descripcion = respJSON.getString("description");
				video = respJSON.getString("urlVideo");
				/* La estrellas j valores decimales por lo que se debe
				realizar la conversion de string a float*/
				String estrellas = respJSON.getString("stars");
				stars.setRating(Float.parseFloat(estrellas));
				//---------------------------------------------------------
				lblCalificacion.setText(estrellas);								
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
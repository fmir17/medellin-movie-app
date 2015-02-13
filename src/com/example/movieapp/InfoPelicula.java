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
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

public class InfoPelicula extends Activity implements OnClickListener{

	private TextView lblTitle, lblGenre, lblFormat, lblDirector,lblCalificacion,lblReparto,lblDuracion;
	private String descripcion, link, video;
	private ImageView imagenPelicula;
	private Bitmap imagen;
	private RatingBar stars;
	private Button trailer,salas;
	private String[] cinemas;
	private String[] cinemasID;
	private String idPelicula;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!verificaConexion(this)) {
		    Toast.makeText(getBaseContext(),
		            "Comprueba tu conexión a Internet. Cerrando Aplicación ... ", Toast.LENGTH_LONG)
		            .show();
		    this.finish();
		}
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
		lblDuracion = (TextView)findViewById(R.id.lblDuracion);
		
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

		if(v.getId()== R.id.btnSalas){		 
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Tu Cinema Preferido?");
			builder.setItems(cinemas, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			        dialog.cancel();
					Intent intent = new Intent(InfoPelicula.this,CinemasActivity.class);
					intent.putExtra("IDCinema", cinemasID[item]);
					intent.putExtra("IDPelicula", idPelicula);
					intent.putExtra("nombreCinema", cinemas[item]);
					startActivity(intent);			        
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();
		}				
	}
	
	public static boolean verificaConexion(Context ctx) {
	    boolean bConectado = false;
	    ConnectivityManager connec = (ConnectivityManager) ctx
	            .getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] redes = connec.getAllNetworkInfo();
	    for (int i = 0; i < 2; i++) {
	        if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
	            bConectado = true;
	        }
	    }
	    return bConectado;
	}

	/**
	 * 
	 * @author sebastian.garciav Metodo que permite obtener la información de un
	 *         JSON
	 */
	private class obtenerPelicula extends AsyncTask<String, Integer, String> {
		JSONObject respJSON;

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
			
			
			// Para cargar los cinemas donde se proyecta la pelicula

			HttpGet del2 = new HttpGet(
					"https://medellin-movie.herokuapp.com/cinema/" + idPelicula);
			del2.setHeader("content-type", "application/json");

			try {

				HttpResponse resp = httpClient.execute(del2);
				String respStr = EntityUtils.toString(resp.getEntity());
				JSONArray respuesta = new JSONArray(respStr);
				JSONObject obj;
				obj = respuesta.getJSONObject(0);
				JSONArray cinema = new JSONArray(obj.getString("cinema"));
				JSONObject obj2;
				cinemas = new String[cinema.length()];
				cinemasID = new String[cinema.length()];
				for(int i =0; i < cinema.length();i++){
					obj2 = cinema.getJSONObject(i);
					cinemas[i] = obj2.getString("cinemaName");
					cinemasID[i] = obj2.getString("cinemaId");
				}				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
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
				lblDuracion.setText("" + respJSON.getString("duration") + " minutos");
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
package com.example.movieapp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {

	private ListView listview;
	String identificador;
	JSONArray respJSON; // Arreglo con datos de las peliculas
	private Bitmap[] imagenes; // Arreglo donde se guarda cada una de las
								// imagenes cargadas desde url
	ItemAdapter adaptador;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		obtenerListaPeliculas nuevo = new obtenerListaPeliculas(this);
		nuevo.execute();
		listview = (ListView)findViewById(R.id.listView);		
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                Item dato = (Item) adaptador.getItem(posicion);
                identificador = dato.getId();
				Intent intent = new Intent(MainActivity.this,
						InfoPelicula.class);
				intent.putExtra("identidad", identificador);
				startActivity(intent);
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
	 * @author sebastian.garciav Metodo que permite Obtener todas las peliculas
	 */
	private class obtenerListaPeliculas extends
			AsyncTask<String, Integer, ItemAdapter> {
		private String title;
		private String[] titulos;
		private String[] Ids;
		Context context;
		
		public obtenerListaPeliculas (Context context){
			this.context = context;
		}

		@Override
		protected ItemAdapter doInBackground(String... params) {

			boolean resul = true;
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet del = new HttpGet(
					"https://medellin-movie.herokuapp.com/movie");
			del.setHeader("content-type", "application/json");

			try {

				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				respJSON = new JSONArray(respStr);
				titulos = new String[respJSON.length()];
				Ids = new String[respJSON.length()];
				System.out.println("Tamaño del arreglo=" + respJSON.length());
				JSONObject obj;			
				for (int i = 0; i < respJSON.length(); i++) {
					obj = respJSON.getJSONObject(i);
					titulos[i] = obj.getString("title");
					Ids[i] = obj.getString("id");
				}
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}

			// Para cargar las imagenes con una url y llenar el arreglo
			URL imageUrl = null;
			HttpURLConnection conn = null;
			imagenes = new Bitmap[respJSON.length()];
			try {
				String url;
				JSONObject object;
				for (int k = 0; k < respJSON.length(); k++) {
					object = respJSON.getJSONObject(k);
					url = object.getString("urlImage");
					imageUrl = new URL(url);
					conn = (HttpURLConnection) imageUrl.openConnection();
					conn.connect();
					imagenes[k] = BitmapFactory.decodeStream(conn
							.getInputStream());
				}
			} catch (IOException e) {

				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			ArrayList <Item> items = new ArrayList<Item>();
			for (int i = 0; i < respJSON.length(); i++) {
				items.add(new Item(imagenes[i], titulos[i], Ids[i]));
			}
			 adaptador = new ItemAdapter(context,items);					
			
			 return adaptador;
		}

		@Override
		protected void onPostExecute(ItemAdapter result) {
			super.onPostExecute(result);
			 listview.setAdapter(result);
		}
	}
}

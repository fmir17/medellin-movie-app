package com.example.movieapp;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CinemasActivity extends Activity {

	JSONArray respJSON;
	private String nombreCinema,idPelicula,idCinema;
	private String[] hora;
	private String[] formato;
	private String[] precio;
	private String direccion,ciudad,telefono;
	private TextView empresa;
	private ListView listaCinemas;
	private TextView lblCiudad, lblDireccion, lblTelefono;
	AdapterCinema adaptador;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!verificaConexion(this)) {
		    Toast.makeText(getBaseContext(),
		            "Comprueba tu conexión a Internet. Cerrando Aplicación ... ", Toast.LENGTH_LONG)
		            .show();
		    this.finish();
		}
		setContentView(R.layout.activity_cinemas);
		Bundle bundle = getIntent().getExtras();
		idPelicula = bundle.getString("IDPelicula");
		idCinema = bundle.getString("IDCinema");
		nombreCinema = bundle.getString("nombreCinema");
		lblCiudad = (TextView)findViewById(R.id.lblciudad);
		lblDireccion = (TextView)findViewById(R.id.lbldireccion);
		lblTelefono = (TextView)findViewById(R.id.lbltelefono);
		empresa = (TextView)findViewById(R.id.lblEmpresa);
		empresa.setText(nombreCinema);
		obtenerCinemas obtenerCinemas = new obtenerCinemas(this);
		obtenerCinemas.execute();
		listaCinemas = (ListView)findViewById(R.id.listaCinema);
        listaCinemas.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                ItemCinema dato = (ItemCinema) adaptador.getItem(posicion);
                /*identificador = dato.getId();
				Intent intent = new Intent(MainActivity.this,
						InfoPelicula.class);
				intent.putExtra("identidad", identificador);
				startActivity(intent);*/
            }
        });		
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

		
	private class obtenerCinemas extends AsyncTask<String, Integer, AdapterCinema> {

		Context context;

		public obtenerCinemas(Context context) {
			this.context = context;
		}
		
		// Se modifica el metodo doInBackground para que retorne un objeto de
		// tipo AdapterCinema
		@Override
		protected AdapterCinema doInBackground(String... params) {
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet del = new HttpGet(
					"https://medellin-movie.herokuapp.com/movieTheater/" + idPelicula + "/" + idCinema);
			del.setHeader("content-type", "application/json");

			try {
				String temporal = "";
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				respJSON = new JSONArray(respStr);
				
				JSONObject obj;
				obj = respJSON.getJSONObject(0);

				JSONArray cinema = new JSONArray(obj.getString("functions"));
				
				JSONObject obj2;
				int tamano = cinema.length();
				hora = new String[tamano];
				formato = new String[tamano];
				precio = new String[tamano];
				for(int i = 0; i <  cinema.length(); i++){
					obj2 = cinema.getJSONObject(i);				
					hora[i] = obj2.getString("schedule");
					if(obj2.getString("format").equalsIgnoreCase("3D") || obj2.getString("format").equalsIgnoreCase("2D")){
						temporal = temporal + obj2.getString("format") + "        ";
						formato[i] = temporal;
						temporal = "";
					}else {
						formato[i] = obj2.getString("format");
					}			
					precio[i] = obj2.getString("price");							
				}
				
				JSONObject obj3 = new JSONObject(obj.getString("description"));
				direccion = obj3.getString("address");
				ciudad = obj3.getString("city");
				telefono = obj3.getString("phoneNumber");
				
				ArrayList <ItemCinema> items = new ArrayList<ItemCinema>();
				for (int i = 0; i < cinema.length(); i++) {
					items.add(new ItemCinema(hora[i],formato[i],precio[i]));
				}
				adaptador = new AdapterCinema(context,items);
				
			} catch (Exception ex) {
				Log.e("consumiendo servicio funciones", "Error!", ex);
			}
								
			 return adaptador; 
		}

		@Override
		protected void onPostExecute(AdapterCinema result) {
			super.onPostExecute(result);
			listaCinemas.setAdapter(result);
			lblCiudad.setText(ciudad);
			lblDireccion.setText(direccion);
			lblTelefono.setText(telefono);
		}
	}
}

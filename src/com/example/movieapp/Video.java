package com.example.movieapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class Video extends YouTubeBaseActivity implements OnInitializedListener {

	String IdVideo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video);

		/*
		 * En caso de que el dispositivo no tenga instalado youtube se le
		 * informa al usuario y se redirecciona a playstore para que instale
		 * dicha aplicacion.
		 */
		if (!isAppInstalled("com.google.android.youtube")) {

			AlertDialog.Builder builder = new AlertDialog.Builder(Video.this);
	        builder.setMessage("Para poder reproducir los videos de los trailers es necesario que instale la aplicacion movil de youtube desde  la PlayStore.")
            .setTitle("Información")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
        			
        			try {
        				startActivity(new Intent(Intent.ACTION_VIEW,
        						Uri.parse("market://details?id="
        								+ "com.google.android.youtube")));
        			} catch (android.content.ActivityNotFoundException anfe) {
        				startActivity(new Intent(
        						Intent.ACTION_VIEW,
        						Uri.parse("http://play.google.com/store/apps/details?id="
        								+ "com.google.android.youtube")));
        			}
                }
            });
	        AlertDialog alerta = builder.create();
	        alerta.show();

		}
		// ----------------------------------------------------------------------
		
		YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		youTubeView.initialize("AIzaSyBTcHQMtlTXkVdItfD4HZT_UdtrRQgdAuQ", this); //Clave para Youtube API 
		Bundle bundle = getIntent().getExtras();
		IdVideo = bundle.getString("video");
	}

	@Override
	public void onInitializationFailure(Provider provider,
			YouTubeInitializationResult result) {
		/*
		 * Toast.makeText(this, "Error: " + "No fue posible cargar el video",
		 * Toast.LENGTH_LONG) .show();
		 */
	}

	@Override
	public void onInitializationSuccess(Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {
			player.loadVideo(IdVideo);// LOL Cats
		}
	}

	private boolean isAppInstalled(String uri) {
		PackageManager pm = getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}

}

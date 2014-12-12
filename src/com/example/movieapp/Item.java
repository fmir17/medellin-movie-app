package com.example.movieapp;

import android.graphics.Bitmap;

public class Item {

	private Bitmap icono;
	private String titulo;
	private String id;
	
	public Item(Bitmap icono, String titulo, String id){
		this.icono = icono;
		this.titulo = titulo;
		this.id = id;
	}
	
	public Bitmap getIcono() {
		return icono;
	}
	public void setIcono(Bitmap icono) {
		this.icono = icono;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}

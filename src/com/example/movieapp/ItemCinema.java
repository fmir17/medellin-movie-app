package com.example.movieapp;

public class ItemCinema {

	private String hora;
	private String formato;
	private String precio;
	
	public ItemCinema(){}
	
	public ItemCinema(String hora,String formato, String precio){
		this.hora = hora;
		this.formato = formato;
		this.precio = precio;
	}
	
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getFormato() {
		return formato;
	}
	public void setFormato(String formato) {
		this.formato = formato;
	}
	public String getPrecio() {
		return precio;
	}
	public void setPrecio(String precio) {
		this.precio = precio;
	}
}

package com.example.movieapp;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter{

	protected Context context;
	protected ArrayList<Item> items;
	
	
    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }
	
	@Override
	public int getCount() {
		return this.items.size();
	}

	@Override
	public Object getItem(int position) {
		return this.items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item, null);
        }
        ImageView imagen = (ImageView) rowView.findViewById(R.id.imagenLista);
        TextView titulo = (TextView) rowView.findViewById(R.id.tituloLista);
        Item item = this.items.get(position);
        imagen.setImageBitmap(item.getIcono());
        titulo.setText(item.getTitulo());
        return rowView;
	}
}

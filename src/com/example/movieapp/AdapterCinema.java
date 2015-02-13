package com.example.movieapp;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterCinema extends BaseAdapter{

	protected Context context;
	protected ArrayList<ItemCinema> items;
	
	
    public AdapterCinema(Context context, ArrayList<ItemCinema> items) {
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
            rowView = inflater.inflate(R.layout.item_cinema, null);
        }
        TextView hora = (TextView) rowView.findViewById(R.id.lblhora);
        TextView formato = (TextView) rowView.findViewById(R.id.lblformato);
        TextView precio = (TextView) rowView.findViewById(R.id.lblprecio);
        ItemCinema item = this.items.get(position);
        hora.setText(item.getHora());
        formato.setText(item.getFormato());
        precio.setText(item.getPrecio());
        return rowView;
	}
	
}

package com.developer.avtocorrectapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MyAdapter extends BaseAdapter {
    private ClickListener clickListener;
    Context ctx;
    ArrayList<Slovar> slovars;

    public MyAdapter(Context ctx, ArrayList<Slovar> slovars) {
        this.ctx = ctx;
        this.slovars = slovars;
    }

    @Override
    public int getCount() {
        return slovars.size();
    }

    @Override
    public Object getItem(int i) {
        return slovars.get(i);
    }

    @Override
    public long getItemId(int i) {
        return slovars.get(i).getId();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(ctx, R.layout.list_item, null);
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.linearItem);
        final ListView listView = (ListView) v.findViewById(R.id.list);
        final TextView txru = (TextView) v.findViewById(R.id.tvRU);
        final TextView txen = (TextView) v.findViewById(R.id.tvEN);
        final ImageView update = v.findViewById(R.id.buttonUpdate);
        final ImageView delete = v.findViewById(R.id.buttonDelete);
        txru.setText(slovars.get(i).getRu());
        txen.setText(slovars.get(i).getEn());
        final String str_update = "update", str_delete = "delete";
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {//Обработчик нажатия на удалить
                    clickListener.itemClicked(slovars.get(i), i, str_delete);
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {//Обработчик нажатия на заменить
                    clickListener.itemClicked(slovars.get(i), i, str_update);
                }
            }
        });
        return v;
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    public interface ClickListener{
        void itemClicked(Slovar slovar, int position, String str);
        void itemLongClicked(Slovar slovar, int position);
    }
}

package com.developer.avtocorrectapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MyAdapter.ClickListener{

    private ListView slovarList;
    MyAdapter myAdapter;
    List<Slovar> slovars;
    DatabaseAdapter adapter;
    final  int DIALOG_EXIT = 1;
    int slovaId = 0;
    String ru, en, create_at, update_at;
    String slova_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slovarList = (ListView)findViewById(R.id.list);
        adapter = new DatabaseAdapter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.open();
        slovars = adapter.getSlovars();
        myAdapter = new MyAdapter(this, (ArrayList<Slovar>) slovars);
        myAdapter.setClickListener(this);
        slovarList.setAdapter(myAdapter);
        adapter.close();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu = menu;
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                if (!query.isEmpty()) {
                    slovaSearch(query);
                    slova_search = query;
                }else {
                    onResume();
                }
                return false;
            }
        });

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add){
            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void slovaSearch(String searchText){
        adapter.open();
        slovars = adapter.getSlovarWord(searchText);
        myAdapter = new MyAdapter(this, (ArrayList<Slovar>) slovars);
        myAdapter.setClickListener(this);
        slovarList.setAdapter(myAdapter);
        adapter.close();
        ru = searchText;
    }

    private void openDeleteDialog(final Slovar slovar){
        final DatabaseAdapter adapter = new DatabaseAdapter(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удалить данные");
        builder.setMessage(String.format("Удалить данные %s?", slovar.getRu()));
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = slovar.getId();
                adapter.open();
                adapter.delete(id);
                adapter.close();
                slovaSearch(slova_search);
                Toast.makeText(MainActivity.this, "Данные удалено!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }

    private void openUpdateDialog(final Slovar slovar){
        final DatabaseAdapter adapter = new DatabaseAdapter(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Заменить данные");
        builder.setMessage(String.format("Заменить данные %s?", slovar.getRu()));
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // Текущее время
                Date currentDate = new Date();
                // Форматирование времени как "день.месяц.год"
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String dateText = dateFormat.format(currentDate);
                update_at = dateText;
                adapter.open();
                Slovar slovar = new Slovar(slovaId, ru, en, create_at, update_at);
                adapter.update(slovar);
                adapter.close();
                slovaSearch(slova_search);
                Toast.makeText(MainActivity.this, "Данные заменено!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }

    /*protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_EXIT) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle("Сообщение");
            // сообщение
            adb.setMessage("Заменить или удалить данные?");
            // иконка
            adb.setIcon(android.R.drawable.ic_dialog_info);
            // кнопка Заменить ответа
            adb.setPositiveButton("Заменить", myClickListener);
            // кнопка Удалить ответа
            adb.setNegativeButton("Удалить", myClickListener);
            // кнопка Отменить ответа
            adb.setNeutralButton("Отменить", myClickListener);
            // создаем диалог
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                //  кнопка заменить
                case Dialog.BUTTON_POSITIVE:
                    // Текущее время
                    Date currentDate = new Date();
                    // Форматирование времени как "день.месяц.год"
                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                    String dateText = dateFormat.format(currentDate);
                    update_at = dateText;
                    adapter.open();
                    Slovar slovar = new Slovar(slovaId, ru, en, create_at, update_at);
                    adapter.update(slovar);
                    adapter.close();
                    Toast.makeText(MainActivity.this, "Данные заменено!", Toast.LENGTH_SHORT).show();
                    break;
                //  кнопка удалить
                case Dialog.BUTTON_NEGATIVE:
                    adapter.open();
                    adapter.delete(slovaId);
                    adapter.close();
                    Toast.makeText(MainActivity.this, "Данные удалено!", Toast.LENGTH_SHORT).show();
                    onResume();
                    break;
                //  кнопка отменить
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    };*/

    @Override
    public void itemClicked(Slovar slovar, int position, String str) {
        if (str.equals("update")) {
            slovaId = slovar.getId();
            en = slovar.getEn();
            create_at = slovar.getCreated_at();
            adapter.close();
            if (slovaId > 0) {
                openUpdateDialog(slovar); // show диалог
            }
        }else if (str.equals("delete")){
            if (slovaId > 0) {
                openDeleteDialog(slovar); // show диалог
            }
        }
    }

    @Override
    public void itemLongClicked(Slovar slovar, int position) {

    }
}

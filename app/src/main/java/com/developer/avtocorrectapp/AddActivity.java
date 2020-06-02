package com.developer.avtocorrectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    private EditText ruBox;
    private EditText enBox;
    private Button saveButton;

    private DatabaseAdapter adapter;
    private int slovaId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ruBox = (EditText) findViewById(R.id.ru);
        enBox = (EditText) findViewById(R.id.en);
        saveButton = (Button) findViewById(R.id.saveButton);
        adapter = new DatabaseAdapter(this);
    }

    public void save(View view){
        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);

        String ru = ruBox.getText().toString();
        String en = enBox.getText().toString();
        String create_at = dateText;
        String update_at = dateText;
        Slovar slovar = new Slovar(slovaId, ru, en, create_at, update_at);

        adapter.open();
        adapter.insert(slovar);
        adapter.close();
        goHome();
    }
    private void goHome(){
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}

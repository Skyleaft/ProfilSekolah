package com.example.mzstudio.profilsekolah;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.mzstudio.profilsekolah.Koneksi.koneksi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListSekolah extends AppCompatActivity {

    @BindView(R.id.lv_sekolah) ListView lvsekolah;

    koneksi k = new koneksi();

    private List<String> datasekolah = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sekolah);
        ButterKnife.bind(this);

        String a,b;
        k.sql="select * from sekolah";
        k.ambil();
        try {
            while(k.rs.next()){
                a = k.rs.getString("id_sekolah");
                b = k.rs.getString("nama_sekolah");
                datasekolah.add(a+" - "+b);

            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datasekolah);
        lvsekolah.setAdapter(adapter);



        lvsekolah.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = lvsekolah.getItemAtPosition(position);
                String[] separated = listItem.toString().split("-");
                Intent resultIntent = new Intent();
                resultIntent.putExtra("id_sekolah", separated[0]);
                resultIntent.putExtra("nama_sekolah", separated[1]);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

    }
}

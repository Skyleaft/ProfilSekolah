package com.example.mzstudio.profilsekolah;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mzstudio.profilsekolah.Koneksi.koneksi;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFasilitas extends Fragment {

    @BindView(R.id.btn_pilih) Button bt_pilih;
    @BindView(R.id.btn_baru) FloatingActionButton bt_baru;
    @BindView(R.id.btn_simpan) FloatingActionButton bt_simpan;
    @BindView(R.id.btn_hapus) FloatingActionButton bt_hapus;
    @BindView(R.id.btn_ubah) FloatingActionButton bt_ubah;
    @BindView(R.id.fab_menu)FloatingActionMenu fabmenu;
    @BindView(R.id.txt_kdfasilitas) EditText _txtkdfas;
    @BindView(R.id.txt_namafasilitas) EditText _txtnamafas;
    @BindView(R.id.txt_jenisfasilitas) EditText _jenisfas;
    @BindView(R.id.txt_ketfasilitas) EditText _ketfas;
    @BindView(R.id.tv_namasekolah) TextView _namasklh;
    @BindView(R.id.table_fasilitas) TableView<String[]> tbfas;

    koneksi k = new koneksi();

    private String id_sekolah,nama_sekolah;

    public TabFasilitas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.tab_fasilitas, container, false);
        final CoordinatorLayout rootlayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorlayout);
        ButterKnife.bind(this, v);

        //kodeotomatis();
        tampildata();
        bt_pilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),ListSekolah.class);
                startActivityForResult(i, 1);
            }
        });

        bt_baru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabmenu.close(true);
                bersih();
                k.sql="select *from fasilitas";
                k.ambil();
                try {
                    k.rs.last();
                    int baris = k.rs.getRow();
                    Toast.makeText(getContext(), "TEst : "+ baris, Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1 : {
                if (resultCode == Activity.RESULT_OK) {
                    id_sekolah = data.getStringExtra("id_sekolah");
                    nama_sekolah = data.getStringExtra("nama_sekolah");
                    _namasklh.setText(nama_sekolah);

                }
                break;
            }
        }
    }

    private void bersih(){
        _txtkdfas.setText("");
        nama_sekolah=null;
        _txtnamafas.setText("");
        _namasklh.setText("");
        _jenisfas.setText("");
        _ketfas.setText("");
        id_sekolah=null;

    }

    private void kodeotomatis(){
        try {
            k.sql ="SELECT * FROM fasilitas";
            k.ambil();
            k.rs.last();
            int baris = k.rs.getRow();
            String baru;
            if(baris==0){
                baru = "FS0001";
            }else{
                int tambah = Integer.valueOf(k.rs.getString(0).substring(3,(k.rs.getString(1).length())))+ 1;
                if(tambah < 10){
                    baru = "FS000"+ tambah;
                }else if(tambah < 100){
                    baru = "FS00"+ tambah;
                }else if(tambah < 1000){
                    baru = "FS0"+ tambah;
                }else{
                    baru = "FS"+ tambah;
                }
            }
            _txtkdfas.setText(baru);


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    private String[] fasheader={"ID","Nama Fasilitas"};
    private List<String[]> datafas = new ArrayList<>();

    private void tampildata() {
        datafas.clear();
        try {
            String a, b, c;
            k.sql = "select * from fasilitas";
            k.ambil();
            while (k.rs.next()) {
                a = k.rs.getString("id_fasilitas");
                b = k.rs.getString("nama_fasilitas");
                c = k.rs.getString("id_sekolah");
                datafas.add(new String[]{a, b,c});

            }

            tbfas.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(), fasheader));
            tbfas.setDataAdapter(new SimpleTableDataAdapter(getContext(), datafas));

            TableColumnDpWidthModel tableColumnModel = new TableColumnDpWidthModel( getContext(), 2 );
            tableColumnModel.setColumnWidth( 0, 120 );
            tableColumnModel.setColumnWidth( 1, 300 );
            tbfas.setColumnModel(tableColumnModel);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

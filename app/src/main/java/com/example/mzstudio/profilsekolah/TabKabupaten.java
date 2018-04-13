package com.example.mzstudio.profilsekolah;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabKabupaten extends Fragment {

    @BindView(R.id.btn_baru) FloatingActionButton bt_baru;
    @BindView(R.id.btn_simpan) FloatingActionButton bt_simpan;
    @BindView(R.id.btn_hapus) FloatingActionButton bt_hapus;
    @BindView(R.id.btn_ubah) FloatingActionButton bt_ubah;
    @BindView(R.id.fab_menu)FloatingActionMenu fabmenu;
    @BindView(R.id.txt_kodekab) EditText _txtkdkab;
    @BindView(R.id.txt_namakab) EditText _txtnamakab;
    @BindView(R.id.table_kab) TableView<String[]> tbkab;
    @BindView(R.id.sp_prov) Spinner spprov;

    koneksi k = new koneksi();

    public TabKabupaten() {
        // Required empty public constructor
    }

    private List<String> dataprov = new ArrayList<>();
    private String kodeprov,kodekab;

    private boolean terpilih =false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.tab_kabupaten, container, false);
        final CoordinatorLayout rootlayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorlayout);
        ButterKnife.bind(this, v);
        loadSpinnerData();
        tampildata();

        spprov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String _kdprov = spprov.getSelectedItem().toString();
                String[] separated = _kdprov.split("-");
                kodeprov = separated[0];


                if(terpilih == false){
                    _txtkdkab.setText("");
                    _txtkdkab.append(kodeprov);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bt_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabmenu.close(true);
                String namakab = _txtnamakab.getText().toString();
                String kdkab = _txtkdkab.getText().toString();

                if(namakab.equals("") || kdkab.equals("") || kdkab.length() <4){
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Pemberitahuan");
                    alertDialog.setMessage("Data belum lengkap!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else{
                    try{
                        k.sql ="select *from kabupaten where id_kabupaten='"+kdkab+"' or nama_kabupaten='"+namakab+"'";
                        k.ambil();
                        if(k.rs.next()) {
                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                            alertDialog.setTitle("Pemberitahuan");
                            alertDialog.setMessage("Data Sudah Ada!");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        else{
                            k.sql="insert into kabupaten values('"+kdkab+"','"+namakab+"','"+kodeprov+"')";
                            k.crud();
                            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            Snackbar.make(rootlayout, "Data berhasil Disimpan", Snackbar.LENGTH_LONG).show();
                            bersih();
                            tampildata();
                        }
                    }catch (SQLException e){
                        e.printStackTrace();
                    }


                }

            }
        });

        bt_baru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabmenu.close(true);
                bersih();
            }
        });


        tbkab.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                terpilih = true;
                kodekab = ((String[])clickedData)[0];

                _txtnamakab.setText(((String[])clickedData)[1]);
                kodeprov = ((String[])clickedData)[2];
                String getprov = ((String[])clickedData)[2];
                try{
                    k.sql = "select *from provinsi where id_provinsi='"+((String[])clickedData)[2]+"'";
                    k.ambil();
                    if(k.rs.next()){
                        getprov = getprov +"- " + k.rs.getString("nama_provinsi");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
                setSpinText(spprov,getprov);
                _txtkdkab.setText(((String[])clickedData)[0]);
                //Toast.makeText(getContext(), "Data yang Dipilih : "+((String[])clickedData)[1], Toast.LENGTH_SHORT).show();
            }
        });

        bt_ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabmenu.close(true);
                if(kodekab==null){
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Pemberitahuan");
                    alertDialog.setMessage("Data Belum Dipilih!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Peringatan!");
                    builder.setMessage("Yakin Ubah data Kabupaten : "+kodekab);
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            k.sql="update kabupaten set id_kabupaten='"+_txtkdkab.getText().toString()+"',nama_kabupaten='"+_txtnamakab.getText().toString()+"',id_provinsi='"+kodeprov+"' where id_kabupaten='"+kodekab+"'";
                            k.crud();
                            // Do nothing, but close the dialog
                            dialog.dismiss();
                            bersih();
                            tampildata();
                            Snackbar.make(rootlayout, "Data berhasil Diubah", Snackbar.LENGTH_LONG).show();


                        }
                    });
                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        });

        bt_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabmenu.close(true);
                if(kodekab==null){
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Pemberitahuan");
                    alertDialog.setMessage("Data Belum Dipilih!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Peringatan!");
                    builder.setMessage("Yakin Hapus data Kabupaten : "+kodekab);
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            k.sql="delete from kabupaten where id_kabupaten='"+kodekab+"'";
                            k.crud();
                            // Do nothing, but close the dialog
                            dialog.dismiss();
                            bersih();
                            tampildata();
                            Snackbar.make(rootlayout, "Data berhasil Dihapus", Snackbar.LENGTH_LONG).show();

                        }
                    });
                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });


        return v;
    }


    private void bersih(){
        spprov.setSelection(0);
        _txtkdkab.setText("");
        _txtnamakab.setText("");
        kodekab=null;
        terpilih=false;
    }

    public void setSpinText(Spinner spin, String text)
    {
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {
            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                spin.setSelection(i);

            }
        }

    }

    private void loadSpinnerData(){

        try{
            String a,b;
            k.sql="select * from provinsi";
            k.ambil();
            while(k.rs.next()){
                a = k.rs.getString("id_provinsi");
                b = k.rs.getString("nama_provinsi");
                dataprov.add(a+" - "+b);

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, dataprov);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spprov.setAdapter(adapter);


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private String[] kabheader={"Kode","Nama Kabupaten/Kota"};
    private List<String[]> datakab = new ArrayList<>();

    private void tampildata() {
        datakab.clear();
        try {
            String a, b, c;
            k.sql = "select * from kabupaten";
            k.ambil();
            while (k.rs.next()) {
                a = k.rs.getString("id_kabupaten");
                b = k.rs.getString("nama_kabupaten");
                c = k.rs.getString("id_provinsi");
                datakab.add(new String[]{a, b,c});

            }

            tbkab.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(), kabheader));
            tbkab.setDataAdapter(new SimpleTableDataAdapter(getContext(), datakab));

            TableColumnDpWidthModel tableColumnModel = new TableColumnDpWidthModel( getContext(), 2 );
            tableColumnModel.setColumnWidth( 0, 110 );
            tableColumnModel.setColumnWidth( 1, 300 );
            tbkab.setColumnModel(tableColumnModel);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

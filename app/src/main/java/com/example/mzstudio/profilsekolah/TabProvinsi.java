package com.example.mzstudio.profilsekolah;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mzstudio.profilsekolah.Koneksi.koneksi;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;



/**
 * A simple {@link Fragment} subclass.
 */

public class TabProvinsi extends Fragment {

    @BindView(R.id.btn_baru) FloatingActionButton bt_baru;
    @BindView(R.id.btn_simpan) FloatingActionButton bt_simpan;
    @BindView(R.id.btn_hapus) FloatingActionButton bt_hapus;
    @BindView(R.id.btn_ubah) FloatingActionButton bt_ubah;
    @BindView(R.id.fab_menu)FloatingActionMenu fabmenu;
    @BindView(R.id.txt_kodeprov) EditText _txtkdprov;
    @BindView(R.id.txt_namaprov) EditText _txtnamaprov;
    @BindView(R.id.table_prov)TableView<String[]> tbprov;

    SwipeRefreshLayout swipeLayout;

    koneksi k = new koneksi();
    String kd_prov;

    public TabProvinsi() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.tab_provinsi, container, false);
        final CoordinatorLayout rootlayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorlayout);
        ButterKnife.bind(this, v);

        tampildata();

        bt_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabmenu.close(true);
                String namaprov = _txtnamaprov.getText().toString();
                String kdprov = _txtkdprov.getText().toString();

                if(namaprov.equals("") || kdprov.equals("")){
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
                        k.sql ="select *from provinsi where id_provinsi='"+kdprov+"' or nama_provinsi='"+namaprov+"'";
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
                            k.sql="insert into provinsi values('"+kdprov+"','"+namaprov+"')";
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

        tbprov.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                kd_prov = ((String[])clickedData)[0];
                _txtkdprov.setText(kd_prov);
                _txtnamaprov.setText(((String[])clickedData)[1]);
                //Toast.makeText(getContext(), "Data yang Dipilih : "+((String[])clickedData)[1], Toast.LENGTH_SHORT).show();
            }
        });

        bt_ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabmenu.close(true);
                if(kd_prov==null){
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
                    builder.setMessage("Yakin Ubah data Provinsi : "+kd_prov);
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try{
                                k.sql="select *from provinsi where nama_provinsi='"+_txtnamaprov.getText().toString() +"' and id_provinsi='"+_txtkdprov.getText().toString()+"'";
                                k.ambil();
                                if(k.rs.next()) {
                                    dialog.dismiss();
                                    Snackbar.make(rootlayout, "Data Gagal Diubah cek kembali Datanya", Snackbar.LENGTH_LONG).show();
                                }
                                else{
                                    k.sql="update provinsi set id_provinsi='"+_txtkdprov.getText().toString()+"',nama_provinsi='"+_txtnamaprov.getText().toString()+"' where id_provinsi='"+kd_prov+"'";
                                    k.crud();
                                    // Do nothing, but close the dialog
                                    dialog.dismiss();
                                    bersih();
                                    tampildata();
                                    Snackbar.make(rootlayout, "Data berhasil Diubah", Snackbar.LENGTH_LONG).show();
                                }
                            }catch (SQLException e){
                                e.printStackTrace();
                            }


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
                if(kd_prov==null){
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
                    builder.setMessage("Yakin Hapus data Provinsi : "+kd_prov);
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            k.sql="delete from provinsi where id_provinsi='"+kd_prov+"'";
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
        _txtkdprov.setText("");
        _txtnamaprov.setText("");
        kd_prov=null;
    }

    private String[] provheader={"Kode","Nama Provinsi"};
    private List<String[]> dataprov = new ArrayList<>();

    private void tampildata(){
    dataprov.clear();
        try{

            String a,b;
            k.sql="select * from provinsi";
            k.ambil();
            while(k.rs.next()){
                a = k.rs.getString("id_provinsi");
                b = k.rs.getString("nama_provinsi");
                dataprov.add(new String[]{a,b});

            }

            tbprov.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(),provheader));
            tbprov.setDataAdapter(new SimpleTableDataAdapter(getContext(),dataprov));

            TableColumnDpWidthModel tableColumnModel = new TableColumnDpWidthModel( getContext(), 2 );
            tableColumnModel.setColumnWidth( 0, 90 );
            tableColumnModel.setColumnWidth( 1, 300 );
            tbprov.setColumnModel(tableColumnModel);

            /*final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(2);
            tableColumnWeightModel.setColumnWeight(0, 1);
            tableColumnWeightModel.setColumnWeight(1, 4);
            setColumnModel(tableColumnWeightModel);*/


        }catch (SQLException e){
            e.printStackTrace();
        }



    }



}

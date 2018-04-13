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
public class TabKecamatan extends Fragment {

    @BindView(R.id.btn_baru) FloatingActionButton bt_baru;
    @BindView(R.id.btn_simpan) FloatingActionButton bt_simpan;
    @BindView(R.id.btn_hapus) FloatingActionButton bt_hapus;
    @BindView(R.id.btn_ubah) FloatingActionButton bt_ubah;
    @BindView(R.id.fab_menu)FloatingActionMenu fabmenu;
    @BindView(R.id.txt_kodekec) EditText _txtkdkec;
    @BindView(R.id.txt_namakec) EditText _txtnamakec;
    @BindView(R.id.table_kec) TableView<String[]> tbkec;
    @BindView(R.id.sp_prov) Spinner spprov;
    @BindView(R.id.sp_kab) Spinner spkab;

    private List<String> dataprov = new ArrayList<>();
    private List<String> datakab = new ArrayList<>();
    private String kodeprov,kodekab,kodekec;

    koneksi k = new koneksi();

    String _getkab;

    private boolean terpilih =false;

    public TabKecamatan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.tab_kecamatan, container, false);
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

                datakab.clear();
                String c,d;
                k.sql="select * from kabupaten where id_provinsi='"+kodeprov+"'";
                k.ambil();
                try {
                    while(k.rs.next()){
                        c = k.rs.getString("id_kabupaten");
                        d = k.rs.getString("nama_kabupaten");
                        datakab.add(c+" - "+d);

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datakab);
                adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spkab.setAdapter(adapter2);

                if(terpilih == true){
                    setSpinText(spkab,_getkab);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spkab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String _kdkab = spkab.getSelectedItem().toString();
                String[] separated2 = _kdkab.split("-");
                kodekab = separated2[0];

                if(terpilih == false){
                    _txtkdkec.setText("");
                    _txtkdkec.append(kodekab);

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
                String namakec = _txtnamakec.getText().toString();
                String kdkec = _txtkdkec.getText().toString();

                if(namakec.equals("") || kdkec.equals("") || kdkec.length()<6){
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
                        k.sql ="select *from kecamatan where id_kecamatan='"+kdkec+"' or nama_kecamatan='"+namakec+"'";
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
                            k.sql="insert into kecamatan values('"+kdkec+"','"+namakec+"','"+kodekab+"')";
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


        tbkec.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                terpilih = true;
                kodekec = ((String[])clickedData)[0];
                _txtkdkec.setText(kodekec);
                _txtnamakec.setText(((String[])clickedData)[1]);
                kodekab = ((String[])clickedData)[2];
                String getkab = kodekab;
                String getprov=null;
                try{
                    k.sql = "select *from kabupaten where id_kabupaten='"+kodekab+"'";
                    k.ambil();
                    if(k.rs.next()){
                        getkab = getkab +"- " + k.rs.getString("nama_kabupaten");
                        _getkab = getkab;
                        getprov = k.rs.getString("id_provinsi");
                    }

                    k.sql = "select *from provinsi where id_provinsi='"+getprov+"'";
                    k.ambil();
                    if(k.rs.next()){
                        getprov = getprov +"- " + k.rs.getString("nama_provinsi");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
                setSpinText(spprov,getprov);
                setSpinText(spkab,getkab);


                //Toast.makeText(getContext(), "Data yang Dipilih : "+((String[])clickedData)[1], Toast.LENGTH_SHORT).show();
            }
        });

        bt_ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabmenu.close(true);
                if(kodekec==null){
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
                    builder.setMessage("Yakin Ubah data Kecamatan : "+kodekec);
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            k.sql="update kecamatan set id_kecamatan='"+_txtkdkec.getText().toString()+"',nama_kecamatan='"+_txtnamakec.getText().toString()+"',id_kabupaten='"+kodekab+"' where id_kecamaan='"+kodekec+"'";
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
                if(kodekec==null){
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
                    builder.setMessage("Yakin Hapus data Kecamatan : "+kodekec);
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            k.sql="delete from kecamatan where id_kecamatan='"+kodekec+"'";
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
        spkab.setSelection(-1);
        _txtkdkec.setText("");
        _txtnamakec.setText("");
        kodekec=null;
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

    private String[] kecheader={"Kode","Nama Kecamatan"};
    private List<String[]> datakec = new ArrayList<>();



    private void tampildata() {
        datakec.clear();
        try {
            String a, b, c;
            k.sql = "select * from kecamatan";
            k.ambil();
            while (k.rs.next()) {
                a = k.rs.getString("id_kecamatan");
                b = k.rs.getString("nama_kecamatan");
                c = k.rs.getString("id_kabupaten");
                datakec.add(new String[]{a, b,c});

            }

            tbkec.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(), kecheader));
            tbkec.setDataAdapter(new SimpleTableDataAdapter(getContext(), datakec));

            TableColumnDpWidthModel tableColumnModel = new TableColumnDpWidthModel( getContext(), 2 );
            tableColumnModel.setColumnWidth( 0, 120 );
            tableColumnModel.setColumnWidth( 1, 300 );
            tbkec.setColumnModel(tableColumnModel);



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

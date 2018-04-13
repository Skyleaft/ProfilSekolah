package com.example.mzstudio.profilsekolah;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mzstudio.profilsekolah.Koneksi.koneksi;

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
public class FragmentPencarian extends Fragment {

    @BindView(R.id.table_sklh) TableView<String[]> tbsklh;

    public FragmentPencarian() {
        // Required empty public constructor
    }

    koneksi k = new koneksi();

    private String[] desheader={"NPSN","Nama Sekolah","Alamat","Kelurahan/Desa"};
    private List<String[]> datades = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pencarian, container, false);
        final CoordinatorLayout rootlayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorlayout);
        ButterKnife.bind(this, v);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        return v;
    }

    private void tampildata() {
        datades.clear();
        try {
            String a, b, c;
            k.sql = "select * from desa";
            k.ambil();
            while (k.rs.next()) {
                a = k.rs.getString("id_desa");
                b = k.rs.getString("nama_desa");
                c = k.rs.getString("id_kecamatan");
                datades.add(new String[]{a, b,c});

            }

            tbsklh.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(), desheader));
            tbsklh.setDataAdapter(new SimpleTableDataAdapter(getContext(), datades));

            TableColumnDpWidthModel tableColumnModel = new TableColumnDpWidthModel( getContext(), 2 );
            tableColumnModel.setColumnWidth( 0, 120 );
            tableColumnModel.setColumnWidth( 1, 160 );
            tableColumnModel.setColumnWidth( 2, 200 );
            tableColumnModel.setColumnWidth( 3, 160 );
            tbsklh.setColumnModel(tableColumnModel);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

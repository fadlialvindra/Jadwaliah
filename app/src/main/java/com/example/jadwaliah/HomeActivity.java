package com.example.jadwaliah;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;

    String[] daftar;
    ListView ListView01;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    public static HomeActivity ha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BuatJadwalActivity.class);
                startActivity(intent);
            }
        });

        ha = this;
        dbcenter = new DataHelper(this);
        RefreshList();

        listView = (ExpandableListView)findViewById(R.id.expjadwal);
        initData();
        listAdapter = new ExpandableListAdapter(this,listDataHeader,listHash);
        listView.setAdapter(listAdapter);
    }

    public void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM biodata", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(1).toString();
        }
        ListView01 = (ListView) findViewById(R.id.listView1);
        ListView01.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = daftar[arg2]; //.getItemAtPosition(arg2).toString();
                final CharSequence[] dialogitem = {"Lihat Jadwal", "Update Jadwal", "Hapus Jadwal"};
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent i = new Intent(getApplicationContext(), LihatJadwalActivity.class);
                                i.putExtra("nama", selection);
                                startActivity(i);
                                break;
                            case 1:
                                Intent in = new Intent(getApplicationContext(), UpdateJadwalActivity.class);
                                in.putExtra("nama", selection);
                                startActivity(in);
                                break;
                            case 2:
                                SQLiteDatabase db = dbcenter.getWritableDatabase();
                                db.execSQL("delete from biodata where nama = '" + selection + "'");
                                RefreshList();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        ((ArrayAdapter) ListView01.getAdapter()).notifyDataSetInvalidated();
    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("Senin");
        listDataHeader.add("Selasa");
        listDataHeader.add("Rabu");
        listDataHeader.add("Kamis");
        listDataHeader.add("Jumat");

        List<String> senin = new ArrayList<>();
        senin.add("07.30      Matematika       GIK L1C");
        senin.add("11.10      Grafikom           MIPAT L1A");
        senin.add("13.30      Mobile               GIK L2");

        List<String> selasa = new ArrayList<>();
        selasa.add("09.20     Mobile Prak       LAB");
        selasa.add("15.30     Robotik              MIPAT L1B");

        List<String> rabu = new ArrayList<>();
        rabu.add("07.30       KWU                GIK L1C");
        rabu.add("11.10       Strukdat          LAB");

        List<String> kamis = new ArrayList<>();
        kamis.add("09.20      Web Lanjut          GIK L2");
        kamis.add("11.10      Bahasa Alami      MIPAT L1B");

        List<String> jumat = new ArrayList<>();
        jumat.add("07.30      ManPeng          MIPAT L1A");
        jumat.add("09.20      Pancasila         MIAPT L1B");
        jumat.add("13.30      Basis Data        LAB");


        listHash.put(listDataHeader.get(0),senin);
        listHash.put(listDataHeader.get(1),selasa);
        listHash.put(listDataHeader.get(2),rabu);
        listHash.put(listDataHeader.get(3),kamis);
        listHash.put(listDataHeader.get(4),jumat);
    }

    @Override
    public void onClick(View view) {

    }
}

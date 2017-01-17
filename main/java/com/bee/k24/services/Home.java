package com.bee.k24.services;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bee.k24.R;
import com.bee.k24.adapter.ListViewAdaptersHashmapOrderFinish;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    ProgressDialog mProgressDialog;
    String[] daftar;
    RecyclerView listView;
    protected Cursor cursor;
    Db dbcenter;
    EditText inputSearch;
    private ArrayList<HashMap<String, String>> list;
    ListViewAdaptersHashmapOrderFinish adapter;
    private DataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_main);
        dbcenter = new Db(this);
        setDB();
        listView = (RecyclerView) findViewById(R.id.recyclerView);
        listView.setHasFixedSize(true);
        LinearLayoutManager mlayoutmanager=new LinearLayoutManager(this);
        listView.setLayoutManager(mlayoutmanager);

        list = new ArrayList<HashMap<String, String>>();
        adapter = new ListViewAdaptersHashmapOrderFinish(this, list);
        listView.setAdapter(adapter);
        cekdata();
    }

    private void setDB() {
        mDataSource = new DataSource(this);
        mDataSource.open();
    }

    private void cekdata() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM siswa", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        if (cursor.getCount() <= 0) {
             if(Connectivity.isConnected(getApplicationContext())){
                notifupdate();
             }else{
                Toast.makeText(getApplicationContext(),"Mohon cek koneksi internet anda",Toast.LENGTH_LONG).show();
             }
        } else {
            RefreshList();
        }
    }

    public void RefreshList() {
        list.clear();
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM siswa", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) { // perulangan ini untuk edit dan view data makanan
            cursor.moveToPosition(cc);
            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put("ID", cursor.getString(0).toString());
            temp.put("NAMA", cursor.getString(1).toString());
            temp.put("ALAMAT", cursor.getString(2).toString());
            temp.put("JOIN", cursor.getString(3).toString());
            list.add(temp);
        }
    }

    public void showP() {
        mProgressDialog = new ProgressDialog(Home.this);
        mProgressDialog.setTitle("Update Data");
        mProgressDialog.setIcon(R.drawable.logo);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public void hideP() {
        mProgressDialog.dismiss();
    }

    public void Update() {
        showP();
        mDataSource.updateSiswa();
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://test.k24.co.id/api.php",
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                final JSONArray cek_array = new JSONArray(response);
                                if (cek_array.length() != 0) {
                                    try {
                                        list.clear();
                                        int i = 0;
                                        for (i = 0; i < cek_array.length(); i++) {
                                            JSONObject obj = cek_array.getJSONObject(i);
                                            String ID = obj.getString("ID");
                                            String NAMA = obj.getString("nama");
                                            String ASAL = obj.getString("asal");
                                            String JOIN = obj.getString("join");
                                                mDataSource.addSiswa(ID, NAMA, ASAL, JOIN);
                                        }
                                        Toast.makeText(getApplicationContext(), "Update Data Selesai", Toast.LENGTH_LONG).show();
                                        rest();
                                    } catch (JSONException e) {
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Gagal Update Data", Toast.LENGTH_LONG).show();
                                    hideP();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            hideP();
                        } else {
                            Toast.makeText(getApplicationContext(), "Tidak dapat menemukan server", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 404:
                                    break;
                                default:
                                    hideP();
                                    break;
                            }
                        }
                        hideP();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", "rc2017");
                params.put("name", "fiki");
                return params;
            }
        };

        postRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.random:
                rest();
                break;
            case R.id.update:
                if(Connectivity.isConnected(getApplicationContext())){
                    notifupdateMANUAL();
                }else{
                    Toast.makeText(getApplicationContext(),"Mohon cek koneksi internet anda",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.del:
                dialogdel();
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }


    public void notifupdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setMessage("Data Kosong.! \n\nData akan diupdate, semua data yang lama akan di hapus")
                .setCancelable(false)
                .setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Update();
                            }
                        })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        dialog.cancel();

                    }
                }).show();
    }

    public void notifupdateMANUAL() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setMessage("Data akan diupdate manual, semua data yang lama akan di hapus")
                .setCancelable(false)
                .setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Update();
                            }
                        })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        dialog.cancel();

                    }
                }).show();
    }

    public void dialogdel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setMessage("Yakin akan menghapus data.?")
                .setCancelable(false)
                .setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mDataSource.cleardata();
                                rest();
                            }
                        })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        dialog.cancel();

                    }
                }).show();
    }

    public void rest(){
        Intent intensaya=getIntent();
        finish();
        startActivity(intensaya);
    }
}

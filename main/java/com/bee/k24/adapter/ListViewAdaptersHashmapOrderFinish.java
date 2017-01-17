package com.bee.k24.adapter;
/**
 * Created by om fiki
 */
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bee.k24.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdaptersHashmapOrderFinish extends RecyclerView.Adapter<ListViewAdaptersHashmapOrderFinish.Holder> {
    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    // public HashMap<String, String> mItems;

    public ListViewAdaptersHashmapOrderFinish(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity=activity;
        this.list=list;
    }

    public class Holder extends RecyclerView.ViewHolder {

        Activity activity;
        TextView id,nama,alamat,join;

        public Holder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.id_siswa);
            nama = (TextView) itemView.findViewById(R.id.nama_siswa);
            alamat= (TextView) itemView.findViewById(R.id.alamat_siswa);
            join= (TextView) itemView.findViewById(R.id.join_siswa);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final HashMap<String, String> map=list.get(position);
        holder.id.setText("ID "+map.get("ID"));
        holder.nama.setText(map.get("NAMA"));
        holder.alamat.setText("Alamat "+map.get("ALAMAT"));
        holder.join.setText("Join "+map.get("JOIN"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
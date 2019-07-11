package com.exomatik.kuliner.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.exomatik.kuliner.Model.ModelBerita;
import com.exomatik.kuliner.Model.ModelKuliner;
import com.exomatik.kuliner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerBerita extends RecyclerView.Adapter<RecyclerBerita.bidangViewHolder> {
    private Context context;
    private ArrayList<ModelBerita> dataList;

    public RecyclerBerita(ArrayList<ModelBerita> paramArrayList) {
        this.dataList = paramArrayList;
    }

    public int getItemCount() {
        if (this.dataList != null) {
            return this.dataList.size();
        }
        return 0;
    }

    public void onBindViewHolder(final bidangViewHolder viewHolder, final int position) {
        viewHolder.textNama.setText(dataList.get(position).getNama());
        viewHolder.textDesc.setText("Deskripsi : " + dataList.get(position).getDesc());
        viewHolder.imgKuliner.setImageResource(dataList.get(position).getFoto());

    }

    public bidangViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        View localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.list_berita, paramViewGroup, false);
        this.context = paramViewGroup.getContext();
        return new bidangViewHolder(localView);
    }

    public class bidangViewHolder extends RecyclerView.ViewHolder {
        private TextView textNama, textDesc;
        private ImageView imgKuliner;

        public bidangViewHolder(View paramView) {
            super(paramView);
            textNama = ((TextView) paramView.findViewById(R.id.text_nama));
            textDesc = ((TextView) paramView.findViewById(R.id.text_desc));
            imgKuliner = ((ImageView) paramView.findViewById(R.id.img_kuliner));
        }
    }
}
package com.exomatik.kuliner.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.exomatik.kuliner.Model.ModelKuliner;
import com.exomatik.kuliner.Model.ModelKulinerTerdekat;
import com.exomatik.kuliner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerLihatKulinerTerdekat extends RecyclerView.Adapter<RecyclerLihatKulinerTerdekat.bidangViewHolder> {
    private Context context;
    private ArrayList<ModelKulinerTerdekat> dataList;

    public RecyclerLihatKulinerTerdekat(ArrayList<ModelKulinerTerdekat> paramArrayList) {
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
        viewHolder.textAlamat.setText("Alamat : " + dataList.get(position).getAlamat());
        viewHolder.textJarak.setText("Jarak : " + dataList.get(position).getJarak().substring(0, 4) + " Km");
        viewHolder.rbFavorit.setRating(dataList.get(position).getFavorit());
        Uri localUri = Uri.parse(dataList.get(position).getFoto());
        Picasso.with(context).load(localUri).into(viewHolder.imgKuliner);

    }

    public bidangViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        View localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.list_lihat_kuliner_terdekat, paramViewGroup, false);
        this.context = paramViewGroup.getContext();
        return new bidangViewHolder(localView);
    }

    public class bidangViewHolder extends RecyclerView.ViewHolder {
        private TextView textNama, textDesc, textAlamat, textJarak;
        private RatingBar rbFavorit;
        private CircleImageView imgKuliner;

        public bidangViewHolder(View paramView) {
            super(paramView);
            textNama = ((TextView) paramView.findViewById(R.id.text_nama));
            textDesc = ((TextView) paramView.findViewById(R.id.text_desc));
            textAlamat = ((TextView) paramView.findViewById(R.id.text_alamat));
            textJarak = ((TextView) paramView.findViewById(R.id.text_jarak));
            imgKuliner = ((CircleImageView) paramView.findViewById(R.id.img_kuliner));
            rbFavorit = ((RatingBar) paramView.findViewById(R.id.rb_favorit));
        }
    }
}
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
import android.widget.Toast;

import com.exomatik.kuliner.Activity.ActFormTambah;
import com.exomatik.kuliner.Model.ModelKuliner;
import com.exomatik.kuliner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerLihatKuliner extends RecyclerView.Adapter<RecyclerLihatKuliner.bidangViewHolder> {
    private Context context;
    private ArrayList<ModelKuliner> dataList;

    public RecyclerLihatKuliner(ArrayList<ModelKuliner> paramArrayList) {
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
        viewHolder.rbFavorit.setRating(dataList.get(position).getFavorit());
        Uri localUri = Uri.parse(dataList.get(position).getFoto());
        Picasso.with(context).load(localUri).into(viewHolder.imgKuliner);

    }

    public bidangViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        View localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.list_lihat_kuliner, paramViewGroup, false);
        this.context = paramViewGroup.getContext();
        return new bidangViewHolder(localView);
    }

    public class bidangViewHolder extends RecyclerView.ViewHolder {
        private TextView textNama, textDesc, textAlamat;
        private RatingBar rbFavorit;
        private CircleImageView imgKuliner;

        public bidangViewHolder(View paramView) {
            super(paramView);
            textNama = ((TextView) paramView.findViewById(R.id.text_nama));
            textDesc = ((TextView) paramView.findViewById(R.id.text_desc));
            textAlamat = ((TextView) paramView.findViewById(R.id.text_alamat));
            imgKuliner = ((CircleImageView) paramView.findViewById(R.id.img_kuliner));
            rbFavorit = ((RatingBar) paramView.findViewById(R.id.rb_favorit));
        }
    }
}
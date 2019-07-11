package com.exomatik.kuliner.Activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exomatik.kuliner.Adapter.RecyclerBerita;
import com.exomatik.kuliner.Adapter.SwipeAdapter;
import com.exomatik.kuliner.Model.ModelBerita;
import com.exomatik.kuliner.R;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    private TextView btnSignIn;
    private RelativeLayout btn1, btn2, btn3;
    private SwipeAdapter adapter;
    private ViewPager viewPager;
    private Handler h = new Handler();
    private Runnable runnable;
    private static int currentPage = 0;
    private RecyclerView rcMakanan;
    private ArrayList<ModelBerita> listMakanan = new ArrayList<ModelBerita>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        btnSignIn = (TextView) findViewById(R.id.text_sign_in);
        btn1 = (RelativeLayout) findViewById(R.id.btn_main1);
        btn2 = (RelativeLayout) findViewById(R.id.btn_main2);
        btn3 = (RelativeLayout) findViewById(R.id.btn_main3);
        viewPager = (ViewPager) findViewById(R.id.view_Pager);
        rcMakanan = (RecyclerView) findViewById(R.id.rc_berita);

        setSwipe();
        setMakanan();

        btnSignIn.setPaintFlags(btnSignIn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignIn.setTextColor(getResources().getColor(R.color.green3));
                startActivity(new Intent(MainActivity.this, ActSignIn.class));
                finish();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActKulinerFavorit.class));
                finish();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActKulinerTerdekat.class));
                finish();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActKulinerCari.class));
                finish();
            }
        });
    }

    private void setSwipe() {
        adapter = new SwipeAdapter(MainActivity.this);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);

        final CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }

    private void setMakanan() {
        listMakanan.add(new ModelBerita("Coto Makassar", "Coto makassar atau coto mangkasara adalah makanan tradisional Makassar, Sulawesi Selatan.[1] Makanan ini terbuat dari jeroan (isi perut) sapi yang direbus dalam waktu yang lama. Rebusan jeroan bercampur daging sapi ini kemudian diiris-iris lalu dibumbui dengan bumbu yang diracik secara khusus. Coto dihidangkan dalam mangkuk dan dinikmati dengan ketupat dan \"burasa\" atau yang biasa dikenal sebagai buras, yakni sejenis ketupat yang dibungkus daun pisang.\n" +
                "\n" +
                "Coto makassar diperkirakan telah ada semenjak masa Kerajaan Gowa di abad ke-16.[2][3] Dahulu hidangan coto bagian daging sapi sirloin dan tenderloin hanya disajikan untuk disantap oleh keluarga kerajaan. Sementara bagian jeroan disajikan untuk masyarakat kelas bawah atau abdi dalem pengikut kerajaan.\n" +
                "\n" +
                "Saat ini coto mangkasara sudah menyebar ke berbagai daerah di Indonesia, mulai di warung pinggir jalan hingga restoran. Masyarakat umum juga menyukai bagian daging sapi atau kerbau yang terletak di bagian punggung (sirloin) itu. Sementara beberapa penjual memberi pilihan daging sapi atau jeroan, atau campuran keduanya, untuk dihidangkan.", R.drawable.bg1));
        listMakanan.add(new ModelBerita("Pallubasa", "Pallubasa adalah makanan tradisional Makassar, Sulawesi Selatan. Seperti Coto Mangkasara (Coto Makassar), Pallubasa juga terbuat dari jeroan (isi dalam perut) sapi atau kerbau. Proses memasaknya pun hampir sama dengan Coto Makassar, yaitu direbus dalam waktu lama. Setelah matang, jeroan yang ditambah dengan daging itu diiris-iris, kemudian ditaruh/dihidangkan dalam mangkuk.\n" +
                "\n" +
                "Dahulu, Pallubasa dengan daging sapi sirloin dan tenderloin hanya disajikan untuk disantap oleh keluarga kerajaan, sementara bagian jeroan disajikan untuk masyarakat kelas bawah atau abdi dalem pengikut kerajaan. Kini, penjual-penjual Pallubasa memberikan bermacam-macam pilihan daging sapi atau jeroan untuk dihidangkan.\n" +
                "\n" +
                "Yang membedakan Pallubasa dengan Coto Makassar adalah bumbunya yang diracik khusus. Selain itu, Coto Makassar dimakan bersama ketupat, sementara Pallubasa dimakan bersama nasi putih.", R.drawable.bg2));
        listMakanan.add(new ModelBerita("Es Pisang Ijo", "Pisang ijo atau Es pisang ijo, adalah sejenis makanan khas di Sulawesi Selatan, utamanya di kota Makassar yang terbuat dari bahan utama berupa pisang ijo. Pisang ijo berupa pisang yang dibalut dengan adonan tepung yang berwarna hijau dan cara memasaknya dengan mengkukus di sebuah dandang. Tepung berwarna dibuat dari tepung, air, pewarna hijau atau air daun suji dan air daun pandan.", R.drawable.bg3));
        listMakanan.add(new ModelBerita("Sarabba", "Sarabba merupakan salah satu kuliner khas di nusantara. Cita rasa yang ditawarkan cukup menggoda. Tak ayal salah satu jenis minuman ini paling banyak diburu oleh masyarakat Indonesia. Minuman dengan cita rasa berbeda memang cukup menarik. Rasa yang enak menjadi pelengkap tersendiri. Ini menjadi nilai plus ", R.drawable.bgminum1));

        RecyclerBerita adapterLihatKelas = new RecyclerBerita(listMakanan);
        LinearLayoutManager localLinearLayoutManager = new LinearLayoutManager(MainActivity.this, 0, false);
        rcMakanan.setLayoutManager(localLinearLayoutManager);
        rcMakanan.setNestedScrollingEnabled(false);
        rcMakanan.setAdapter(adapterLihatKelas);
    }

    @Override
    public void onStart() {
        h.postDelayed(new Runnable() {
                          public void run() {
                              if (currentPage == adapter.getCount()) {
                                  currentPage = 0;
                              } else {
                                  currentPage++;
                              }

                              viewPager.setCurrentItem(currentPage);

                              runnable = this;

                              h.postDelayed(runnable, 5000);
                          }
                      }
                , 5000);

        super.onStart();
    }
}

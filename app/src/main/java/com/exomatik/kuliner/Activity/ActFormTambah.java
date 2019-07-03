package com.exomatik.kuliner.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exomatik.kuliner.Featured.FileUtil;
import com.exomatik.kuliner.Model.ModelKuliner;
import com.exomatik.kuliner.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class ActFormTambah extends AppCompatActivity {
    public static ModelKuliner dataKuliner;
    private CircleImageView imgKuliner, imgLocation, imgHapus;
    private EditText etNama, etDesc, etAlamat;
    private ImageView back;
    private String locationPoint;
    private RelativeLayout btnKirim;
    private TextView textTambah;
    private ProgressDialog progressDialog;
    private int PICK_IMAGE = 100;
    private int PLACE_PICKER_REQUEST = 1;
    private File compressedImage, actualImage;
    private StorageReference mStorageRef;
    private Uri imageUri = null;
    private View view;
    private RatingBar rbFavorit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_form_tambah);

        back = (ImageView) findViewById(R.id.back);
        imgKuliner = (CircleImageView) findViewById(R.id.img_user);
        imgLocation = (CircleImageView) findViewById(R.id.img_pick_alamat);
        imgHapus = (CircleImageView) findViewById(R.id.img_hapus);
        etNama = (EditText) findViewById(R.id.et_nama);
        etDesc = (EditText) findViewById(R.id.et_desc);
        etAlamat = (EditText) findViewById(R.id.et_alamat);
        btnKirim = (RelativeLayout) findViewById(R.id.rl_update);
        textTambah = (TextView) findViewById(R.id.text_tambah);
        rbFavorit = (RatingBar) findViewById(R.id.rb_favorit);
        view = (View) findViewById(android.R.id.content);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        setData();

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekEditText();
            }
        });

        imgKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFoto();
            }
        });

        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAlamat.setError(null);
                getLocation();
            }
        });

        imgHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusData();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActFormTambah.this, MainActivity.class));
                finish();
            }
        });
    }

    private void setData() {
        if (dataKuliner != null) {
            Uri localUri = Uri.parse(dataKuliner.getFoto());
            Picasso.with(ActFormTambah.this).load(localUri).into(imgKuliner);
            etNama.setText(dataKuliner.getNama());
            etDesc.setText(dataKuliner.getDesc());
            etAlamat.setText(dataKuliner.getAlamat());
            locationPoint = dataKuliner.getLocationPoint();
            rbFavorit.setRating(dataKuliner.getFavorit());
            imgHapus.setVisibility(View.VISIBLE);
        } else {
            imgKuliner.setImageResource(R.drawable.logo);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActFormTambah.this, MainActivity.class));
        finish();
    }

    private void getLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(ActFormTambah.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Toast.makeText(ActFormTambah.this, "Error " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(ActFormTambah.this, "Error " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getFoto() {
        progressDialog = new ProgressDialog(ActFormTambah.this);
        progressDialog.setMessage(getResources().getString(R.string.progress_title1));
        progressDialog.setTitle(getResources().getString(R.string.progress_text1));
        progressDialog.setCancelable(false);
        progressDialog.show();
        startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.INTERNAL_CONTENT_URI), PICK_IMAGE);
        progressDialog.dismiss();
    }

    private void hapusData() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ActFormTambah.this);

        alert.setTitle("Hapus");
        alert.setMessage("Apakah anda yakin ingin menghapus kelas ini?");
        alert.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                DatabaseReference db_remove_kelas = FirebaseDatabase.getInstance()
                        .getReference("kuliner")
                        .child(dataKuliner.getId());
                db_remove_kelas.removeValue();

                dialog.dismiss();
                startActivity(new Intent(ActFormTambah.this, ActMenuAdmin.class));
                finish();
            }
        });
        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    private void cekEditText() {
        String nama = etNama.getText().toString();
        String desc = etDesc.getText().toString();
        String alamat = etAlamat.getText().toString();

        if (nama.isEmpty() || desc.isEmpty() || alamat.isEmpty() || locationPoint == null || imageUri == null) {
            if (nama.isEmpty()) {
                etNama.setError(getResources().getString(R.string.error_data_kosong));
            }
            if (desc.isEmpty()) {
                etDesc.setError(getResources().getString(R.string.error_data_kosong));
            }
            if (alamat.isEmpty()) {
                etAlamat.setError(getResources().getString(R.string.error_data_kosong));
            }
            if (locationPoint == null) {
                Snackbar snackbar = Snackbar
                        .make(view, getResources().getString(R.string.error_latitude_kosong), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            if (imageUri == null) {
                Snackbar snackbar = Snackbar
                        .make(view, getResources().getString(R.string.error_foto_kosong), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        } else {
            progressDialog = new ProgressDialog(ActFormTambah.this);
            progressDialog.setMessage(getResources().getString(R.string.progress_title1));
            progressDialog.setTitle(getResources().getString(R.string.progress_text1));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();

            String id = null;

            if (dataKuliner != null){
                id = dataKuliner.getId();
            }
            else {
                id = System.currentTimeMillis() + "_" + nama;
            }

            ModelKuliner dataKuliner = new ModelKuliner(nama, desc, alamat, locationPoint, null, id, rbFavorit.getRating());
            uploadMethod(dataKuliner);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, ActFormTambah.this);
                etAlamat.setText(place.getName());
                locationPoint = place.getLatLng().toString();
            }
        }

        if ((resultCode == -1) && (requestCode == PICK_IMAGE)) {
            try {
                actualImage = FileUtil.from(ActFormTambah.this, data.getData());
                compressedImage = new Compressor(ActFormTambah.this).compressToFile(actualImage);
                imageUri = Uri.fromFile(compressedImage);
                Picasso.with(ActFormTambah.this).load(imageUri).into(imgKuliner);
            } catch (IOException e) {
                Toast.makeText(ActFormTambah.this, "Error " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadMethod(ModelKuliner dataUploaded) {
        if (dataUploaded.getFoto() == null) {
            simpanFoto(dataUploaded, imageUri);
        } else {
            if (imageUri == null) {
                simpanData(dataUploaded, null);
            } else {
                hapusFoto(dataUploaded, imageUri);
            }
        }
    }

    private void hapusFoto(final ModelKuliner dataUploaded, final Uri image) {
        StorageReference fotoDelete = getInstance().getReferenceFromUrl(dataKuliner.getFoto());
        fotoDelete.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                simpanFoto(dataUploaded, image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar
                        .make(view, "Error " + e.getMessage().toString(), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void simpanFoto(final ModelKuliner dataUploaded, Uri image) {
        final String file = System.currentTimeMillis() + "_" + image.getLastPathSegment();
        mStorageRef.child("fotoKuliner/" + file).putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            public void onSuccess(UploadTask.TaskSnapshot paramAnonymousTaskSnapshot) {
                simpanData(dataUploaded, paramAnonymousTaskSnapshot);
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception paramAnonymousException) {
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar
                        .make(view, "Error, " + paramAnonymousException.getMessage().toString(), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage(Integer.toString((int) progress) + " %");
                progressDialog.setProgress((int) progress);
                String progressText = taskSnapshot.getBytesTransferred() / 1024 + "KB/" + taskSnapshot.getTotalByteCount() / 1024 + "KB";
                progressDialog.setTitle(progressText);
            }
        });
    }

    private void simpanData(ModelKuliner dataUploaded, UploadTask.TaskSnapshot snapshot) {
        String foto = null;
        if (snapshot == null) {
            foto = dataKuliner.getFoto();
        } else {
            foto = snapshot.getDownloadUrl().toString();
        }
        ModelKuliner data = new ModelKuliner(dataUploaded.getNama(), dataUploaded.getDesc()
                , dataUploaded.getAlamat(), dataUploaded.getLocationPoint(), foto
                , dataUploaded.getId(), dataUploaded.getFavorit());

        DatabaseReference localDatabaseReference = FirebaseDatabase.getInstance().getReference();

        localDatabaseReference
                .child("kuliner")
                .child(dataUploaded.getId())
                .setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> paramAnonymous2Task) {
                        if (paramAnonymous2Task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(ActFormTambah.this, "Berhasil Upload Data", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ActFormTambah.this, ActMenuAdmin.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Snackbar.make(view, "Gagal Upload Data, error tidak diketahui", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Snackbar.make(view, "Error " + e.getMessage().toString(), Snackbar.LENGTH_LONG).show();
            }
        });
    }
}

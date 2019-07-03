package com.exomatik.kuliner.Model;

/**
 * Created by IrfanRZ on 03/07/2019.
 */

public class ModelKuliner {
    String nama, desc, alamat, locationPoint, foto, id;
    float favorit;

    public ModelKuliner() {
    }

    public ModelKuliner(String nama, String desc, String alamat, String locationPoint, String foto, String id, float favorit) {
        this.nama = nama;
        this.desc = desc;
        this.alamat = alamat;
        this.locationPoint = locationPoint;
        this.foto = foto;
        this.id = id;
        this.favorit = favorit;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getLocationPoint() {
        return locationPoint;
    }

    public void setLocationPoint(String locationPoint) {
        this.locationPoint = locationPoint;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getFavorit() {
        return favorit;
    }

    public void setFavorit(float favorit) {
        this.favorit = favorit;
    }
}

package com.exomatik.kuliner.Model;

/**
 * Created by IrfanRZ on 07/07/2019.
 */

public class ModelBerita {
    String nama, desc;
    int foto;

    public ModelBerita() {
    }

    public ModelBerita(String nama, String desc, int foto) {
        this.nama = nama;
        this.desc = desc;
        this.foto = foto;
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

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}

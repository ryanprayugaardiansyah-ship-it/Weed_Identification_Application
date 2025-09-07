package com.example.identifikasigulma;

public class Gulma {
    private String nama;
    private String deskripsi;
    private String namaIlmiah;
    private int imageResourceId;

    public Gulma(String nama, String deskripsi, String namaIlmiah, int imageResourceId) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.namaIlmiah = namaIlmiah;
        this.imageResourceId = imageResourceId;
    }

    public String getNama() {
        return nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getNamaIlmiah() {
        return namaIlmiah;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}

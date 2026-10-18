/**
 * ============================================================
 *  APLIKASI KASIR RESTORAN - DESKTOP (JAVA SWING)
 *  STSI4201 Pemrograman Berbasis Desktop
 *  File : Menu.java
 * ============================================================
 * Class Menu (SAMA dengan versi console, tidak dihapus):
 *   - atribut  : nama, kategori, harga
 *   - constructor, method getter, method cetakMenu, method formatRupiah
 *   - ditambahkan method toString() supaya rapi saat tampil di JComboBox
 * ============================================================
 */
public class Menu {

    // ================= ATRIBUT (VARIABEL INSTANSI) =================
    // tipe data String : menyimpan teks (nama & kategori)
    // tipe data int    : menyimpan angka bulat (harga dalam Rupiah)
    String nama;       // contoh: "Nasi Goreng"
    String kategori;   // contoh: "MAKANAN" atau "MINUMAN"
    int harga;         // contoh: 18000 (artinya Rp18.000)

    // ================= CONSTRUCTOR =================
    // Constructor dipanggil setiap kali kita membuat object baru: new Menu(...)
    public Menu(String nama, String kategori, int harga) {
        this.nama = nama;           // this = menunjuk ke atribut milik object ini
        this.kategori = kategori;
        this.harga = harga;
    }

    // ================= METHOD GETTER =================
    // Method untuk mengambil nilai atribut (dipakai di class LogikaRestoran)
    public String getNama() {
        return nama;
    }

    public String getKategori() {
        return kategori;
    }

    public int getHarga() {
        return harga;
    }

    // ================= METHOD MENAMPILKAN DAFTAR MENU (dipakai console) =================
    // Method void : hanya menampilkan, tidak mengembalikan nilai
    public void cetakMenu(int nomor) {
        System.out.println(nomor + ". " + nama + "   ->   " + formatRupiah(harga)
                + "   (" + kategori + ")");
    }

    // ================= METHOD MENGUBAH ANGKA MENJADI TEKS RUPIAH =================
    // Method static : bisa dipanggil tanpa membuat object (Menu.formatRupiah)
    static String formatRupiah(int nilai) {
        // Locale "id-ID" membuat angka 18000 ditampilkan menjadi 18.000
        return "Rp" + String.format(java.util.Locale.forLanguageTag("id-ID"), "%,d", nilai);
    }

    // ================= METHOD TAMBAHAN UNTUK GUI =================
    // Method toString() dipakai JComboBox agar nama menu tampil rapi
    @Override
    public String toString() {
        return nama + "  [" + kategori + "]  -  " + formatRupiah(harga);
    }
}

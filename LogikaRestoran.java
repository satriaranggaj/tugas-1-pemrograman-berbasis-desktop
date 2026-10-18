/**
 * ============================================================
 *  APLIKASI KASIR RESTORAN - DESKTOP (JAVA SWING)
 *  STSI4201 Pemrograman Berbasis Desktop
 *  File : LogikaRestoran.java
 * ============================================================
 * Class ini berisi SELURUH ATURAN BISNIS aplikasi restoran yang
 * sebelumnya berada di class Main (versi console):
 *   - array data menu (4 makanan + 4 minuman)
 *   - array data pesanan (maksimal 4 item)
 *   - pencarian menu berdasarkan nomor  (SWITCH CASE)
 *   - validasi jumlah pesanan          (IF - ELSE IF - ELSE)
 *   - diskon 10%                       (IF - ELSE)
 *   - promo beli 1 gratis 1            (NESTED IF)
 *
 * Dipakai BERSAMA oleh dua tampilan:
 *   1. Main.java        -> aplikasi console / terminal
 *   2. RestoranApp.java -> aplikasi GUI (Java Swing)
 *
 * TUJUAN: aturan bisnis hanya ada di SATU tempat, tidak ditulis ulang.
 * ============================================================
 */
public class LogikaRestoran {

    // ================= KONSTANTA ATURAN BISNIS =================
    // final = nilainya tidak bisa diubah selama program berjalan
    public static final String NAMA_MINUMAN_PROMO = "Es Teh";   // nama minuman promo
    public static final int HARGA_MINUMAN_PROMO = 6000;         // harga 1 gelas Es Teh
    public static final int BATAS_PESANAN = 4;                  // maksimal 4 item pesanan
    public static final int BATAS_JUMLAH = 10;                  // maksimal 10 porsi per menu
    public static final int BIAYA_PELAYANAN = 20000;            // biaya pelayanan Rp20.000
    public static final int BATAS_DISKON = 100000;              // batas diskon 10%
    public static final int BATAS_PROMO = 50000;                // batas promo

    // garis pemisah struk (dipakai console maupun GUI)
    public static final String GARIS = "------------------------------------------";

    // ================= ARRAY DATA MENU (SAMA DENGAN VERSI CONSOLE) =================
    private final Menu[] daftarMenu = new Menu[8];

    // ================= ARRAY DATA PESANAN (MAKSIMAL 4 ITEM) =================
    private final String[] namaPesanan = new String[BATAS_PESANAN];     // contoh: "Ayam Bakar"
    private final String[] kategoriPesanan = new String[BATAS_PESANAN]; // contoh: "MAKANAN"
    private final int[] jumlahPesanan = new int[BATAS_PESANAN];         // contoh: 2
    private final int[] hargaSatuan = new int[BATAS_PESANAN];           // contoh: 28000
    private final int[] subtotalItem = new int[BATAS_PESANAN];          // harga x jumlah

    // ================= VARIABEL KUMULATIF & HASIL PERHITUNGAN =================
    private int jumlahItem = 0;             // jumlah item yang sudah masuk pesanan
    private int subtotalKeseluruhan = 0;   // jumlah seluruh subtotal item
    private int jumlahBeliPromo = 0;        // banyaknya Es Teh yang dibeli
    private int diskon = 0;
    private int bonusPromo = 0;
    private int pajak = 0;
    private int totalBayar = 0;
    // true = hasil hitungTotal() masih berlaku untuk pesanan saat ini.
    // menjadi false setiap ada perubahan pesanan (lihat tambahPesanan & reset)
    private boolean sudahDihitung = false;

    // ================= CONSTRUCTOR : MENGISI ARRAY MENU =================
    public LogikaRestoran() {

        // 4 menu MAKANAN
        daftarMenu[0] = new Menu("Nasi Goreng", "MAKANAN", 18000);
        daftarMenu[1] = new Menu("Mie Goreng", "MAKANAN", 17000);
        daftarMenu[2] = new Menu("Ayam Bakar", "MAKANAN", 28000);
        daftarMenu[3] = new Menu("Soto Ayam", "MAKANAN", 15000);

        // 4 menu MINUMAN
        daftarMenu[4] = new Menu("Es Teh", "MINUMAN", 6000);
        daftarMenu[5] = new Menu("Es Jeruk", "MINUMAN", 8000);
        daftarMenu[6] = new Menu("Kopi", "MINUMAN", 10000);
        daftarMenu[7] = new Menu("Jus Alpukat", "MINUMAN", 14000);
    }

    // ================= METHOD PENGAMBIL DATA MENU =================
    public Menu[] getDaftarMenu() {
        return daftarMenu;
    }

    public int getJumlahMenu() {
        return daftarMenu.length;
    }

    // ============================================================
    //  METHOD : MENCARI MENU DARI NOMOR YANG DIINPUT  (SWITCH CASE)
    //  Sama dengan method cariMenu() pada versi console.
    //  Return : object Menu, atau null jika nomor di luar 1-8
    // ============================================================
    public Menu cariMenu(int nomor) {

        switch (nomor) {                       // <-- SWITCH CASE
            case 1:
                return daftarMenu[0];
            case 2:
                return daftarMenu[1];
            case 3:
                return daftarMenu[2];
            case 4:
                return daftarMenu[3];
            case 5:
                return daftarMenu[4];
            case 6:
                return daftarMenu[5];
            case 7:
                return daftarMenu[6];
            case 8:
                return daftarMenu[7];
            default:                            // nomor di luar 1-8
                return null;
        }
    }

    // ============================================================
    //  METHOD : VALIDASI JUMLAH PESANAN  (IF - ELSE IF - ELSE)
    //  Sama dengan method validasiJumlah() pada versi console.
    // ============================================================
    public int validasiJumlah(int jumlah) {

        if (jumlah <= 0) {                     // <-- IF
            return 1;
        } else if (jumlah > BATAS_JUMLAH) {     // <-- ELSE IF
            return BATAS_JUMLAH;
        } else {                               // <-- ELSE
            return jumlah;
        }
    }

    // ============================================================
    //  METHOD : MENAMBAHKAN PESANAN (pengganti method catatPesanan)
    //  Menyimpan nama, kategori, jumlah, harga satuan, dan subtotal item
    //  ke dalam array, lalu menjumlahkan subtotal keseluruhan.
    //
    //  Aturan yang dijaga method ini:
    //   - menu tidak boleh null
    //   - maksimal 4 item pesanan (BATAS_PESANAN)
    //   - jumlah 0 / negatif / lebih dari BATAS_JUMLAH DINORMALKAN
    //     memakai validasiJumlah(), sehingga rules hanya ada di satu tempat
    //     dan tetap aman walaupun method ini dipanggil langsung dari luar
    //     GUI atau console
    //
    //  Setiap pesanan baru membuat hasil hitung sebelumnya tidak berlaku
    //  (sudahDihitung = false), sehingga total/struk tidak pernah kedaluwarsa.
    // ============================================================
    public boolean tambahPesanan(Menu pesanan, int jumlah) {

        // IF : menu tidak boleh null
        if (pesanan == null) {
            return false;
        }

        // IF : maksimal 4 item pesanan (sama seperti batas versi console)
        if (jumlahItem >= BATAS_PESANAN) {
            return false;
        }

        // jumlah selalu dinormalkan oleh aturan yang sama (validasiJumlah)
        int jumlahValid = validasiJumlah(jumlah);

        // menyimpan ke array pada indeks sesuai jumlah item
        namaPesanan[jumlahItem] = pesanan.getNama();
        kategoriPesanan[jumlahItem] = pesanan.getKategori();
        jumlahPesanan[jumlahItem] = jumlahValid;
        hargaSatuan[jumlahItem] = pesanan.getHarga();
        subtotalItem[jumlahItem] = pesanan.getHarga() * jumlahValid;      // harga x jumlah

        // menjumlahkan subtotal setiap item menjadi subtotal keseluruhan
        subtotalKeseluruhan = subtotalKeseluruhan + subtotalItem[jumlahItem];

        // IF : menghitung banyaknya Es Teh yang dibeli (untuk promo)
        if (pesanan.getNama().equalsIgnoreCase(NAMA_MINUMAN_PROMO)) {
            jumlahBeliPromo = jumlahBeliPromo + jumlahValid;
        }

        jumlahItem = jumlahItem + 1;

        // hasil hitung sebelumnya menjadi tidak berlaku karena pesanan berubah
        sudahDihitung = false;

        return true;
    }

    // ============================================================
    //  METHOD : MENGHITUNG TOTAL
    //  Berisi IF - ELSE (diskon) dan NESTED IF (promo), sama seperti
    //  perhitungan pada versi console.
    // ============================================================
    public void hitungTotal() {

        // --- DISKON 10% JIKA SUBTOTAL > Rp100.000  (IF - ELSE) ---
        if (subtotalKeseluruhan > BATAS_DISKON) {
            diskon = subtotalKeseluruhan * 10 / 100;
        } else {
            diskon = 0;
        }

        // --- PROMO BELI 1 GRATIS 1  (NESTED IF) ---
        bonusPromo = 0;

        if (subtotalKeseluruhan > BATAS_PROMO) {              // <-- IF LUAR

            if (jumlahBeliPromo >= 2) {                       // <-- IF DALAM (NESTED IF)
                bonusPromo = HARGA_MINUMAN_PROMO;
            } else {
                bonusPromo = 0;
            }
        } else {
            bonusPromo = 0;
        }

        // --- PAJAK 10% DAN BIAYA PELAYANAN ---
        pajak = subtotalKeseluruhan * 10 / 100;
        int biayaPelayanan = BIAYA_PELAYANAN;                // Rp20.000

        // --- TOTAL BAYAR ---
        totalBayar = subtotalKeseluruhan - diskon - bonusPromo + pajak + biayaPelayanan;

        sudahDihitung = true;
    }

    // ================= METHOD MENGHAPUS SEMUA PESANAN =================
    public void reset() {

        jumlahItem = 0;
        subtotalKeseluruhan = 0;
        jumlahBeliPromo = 0;
        diskon = 0;
        bonusPromo = 0;
        pajak = 0;
        totalBayar = 0;
        sudahDihitung = false;

        // IF : array pesanan dikosongkan (maksimal 4 item, ditulis manual)
        if (BATAS_PESANAN > 0) {
            namaPesanan[0] = null;
            kategoriPesanan[0] = null;
            jumlahPesanan[0] = 0;
            hargaSatuan[0] = 0;
            subtotalItem[0] = 0;
        }
        if (BATAS_PESANAN > 1) {
            namaPesanan[1] = null;
            kategoriPesanan[1] = null;
            jumlahPesanan[1] = 0;
            hargaSatuan[1] = 0;
            subtotalItem[1] = 0;
        }
        if (BATAS_PESANAN > 2) {
            namaPesanan[2] = null;
            kategoriPesanan[2] = null;
            jumlahPesanan[2] = 0;
            hargaSatuan[2] = 0;
            subtotalItem[2] = 0;
        }
        if (BATAS_PESANAN > 3) {
            namaPesanan[3] = null;
            kategoriPesanan[3] = null;
            jumlahPesanan[3] = 0;
            hargaSatuan[3] = 0;
            subtotalItem[3] = 0;
        }
    }

    // ================= METHOD PENGAMBIL HASIL PERHITUNGAN =================
    public int getJumlahItem() {
        return jumlahItem;
    }

    public int getSubtotalKeseluruhan() {
        return subtotalKeseluruhan;
    }

    public int getDiskon() {
        return diskon;
    }

    public int getBonusPromo() {
        return bonusPromo;
    }

    public int getPajak() {
        return pajak;
    }

    public int getBiayaPelayanan() {
        return BIAYA_PELAYANAN;
    }

    public int getTotalBayar() {
        return totalBayar;
    }

    public boolean isSudahDihitung() {
        return sudahDihitung;
    }

    // IF ELSE : status diskon untuk panel status di GUI
    public boolean isDiskonAktif() {
        return diskon > 0;
    }

    // IF : status promo untuk panel status di GUI
    public boolean isPromoAktif() {
        return bonusPromo > 0;
    }

    // ================= METHOD PENGAMBIL DATA PESANAN =================
    public String getNamaPesanan(int index) {
        return namaPesanan[index];
    }

    public String getKategoriPesanan(int index) {
        return kategoriPesanan[index];
    }

    public int getJumlahPesanan(int index) {
        return jumlahPesanan[index];
    }

    public int getHargaSatuan(int index) {
        return hargaSatuan[index];
    }

    public int getSubtotalItem(int index) {
        return subtotalItem[index];
    }

    // ============================================================
    //  METHOD : MENGUBAH SATU ITEM PESANAN MENJADI BARIS TABEL
    //  Dipakai JTable daftar pesanan di GUI.
    //  Return : Object[] (nama, jumlah, harga satuan, subtotal item)
    //            atau null jika item tersebut tidak ada
    // ============================================================
    public Object[] getBarisTabel(int index) {

        // IF : item tidak ada -> tidak ada baris yang ditambahkan
        if (namaPesanan[index] == null) {
            return null;
        } else {
            return new Object[]{
                    namaPesanan[index] + " (" + kategoriPesanan[index] + ")",
                    jumlahPesanan[index] + "",
                    Menu.formatRupiah(hargaSatuan[index]),
                    Menu.formatRupiah(subtotalItem[index])
            };
        }
    }

    // ============================================================
    //  METHOD : MENGUBAH SATU ITEM PESANAN MENJADI BARIS STRUK
    //  Dipakai JTextArea struk di GUI maupun cetakan console.
    // ============================================================
    public String getBarisDetail(int index) {

        // IF : item tidak ada -> tampilkan keterangan
        if (namaPesanan[index] == null) {
            return (index + 1) + ". Pesanan ke-" + (index + 1) + " : tidak ada pesanan";
        } else {
            return (index + 1) + ". " + namaPesanan[index] + " (" + kategoriPesanan[index] + ")"
                    + "\n     Jumlah         : " + jumlahPesanan[index]
                    + "\n     Harga Satuan   : " + Menu.formatRupiah(hargaSatuan[index])
                    + "\n     Subtotal Item  : " + Menu.formatRupiah(subtotalItem[index]);
        }
    }

    // ============================================================
    //  METHOD : RINGKASAN STRUK (subtotal, diskon, promo, pajak, biaya)
    // ============================================================
    public String getRingkasanStruk() {
        return "Subtotal Keseluruhan    : " + Menu.formatRupiah(subtotalKeseluruhan)
                + "\nDiskon (10%)           : -" + Menu.formatRupiah(diskon)
                + "\nPromo (Beli 1 Gratis 1): -" + Menu.formatRupiah(bonusPromo)
                + "\nPajak (10%)            : " + Menu.formatRupiah(pajak)
                + "\nBiaya Pelayanan        : " + Menu.formatRupiah(BIAYA_PELAYANAN);
    }

    // ============================================================
    //  METHOD : STRUK LENGKAP SIAP TAMPIL (dipakai GUI dan console)
    //  Maksimal 4 item pesanan ditulis manual, tanpa perulangan.
    // ============================================================
    public String getStrukLengkap() {
        return "==========================================="
                + "\n             STRUK PESANAN"
                + "\n==========================================="
                + "\n" + getBarisDetail(0)
                + "\n" + getBarisDetail(1)
                + "\n" + getBarisDetail(2)
                + "\n" + getBarisDetail(3)
                + "\n" + GARIS
                + "\n" + getRingkasanStruk()
                + "\n" + GARIS
                + "\nTOTAL BAYAR            : " + Menu.formatRupiah(totalBayar)
                + "\n==========================================="
                + "\n   Terima kasih, selamat makan!";
    }

    // ============================================================
    //  METHOD : PESAN INFORMASI UNTUK CONSOLE
    // ============================================================
    public String getInfoDiskon() {
        // IF : diskon aktif atau tidak
        if (isDiskonAktif()) {
            return "[INFO] Diskon 10% aktif, potongan " + Menu.formatRupiah(diskon) + ".";
        } else {
            return "[INFO] Diskon tidak aktif.";
        }
    }

    public String getInfoPromo() {
        // IF ELSE + NESTED IF : sama seperti aturan promo versi console
        if (subtotalKeseluruhan > BATAS_PROMO) {
            if (jumlahBeliPromo >= 2) {
                return "[INFO] Subtotal memenuhi syarat promo (>" + Menu.formatRupiah(BATAS_PROMO) + ")."
                        + "\n[INFO] Promo aktif! Beli 1 gratis 1 untuk " + NAMA_MINUMAN_PROMO
                        + ", bonus " + Menu.formatRupiah(bonusPromo) + ".";
            } else {
                return "[INFO] Subtotal memenuhi syarat promo (>" + Menu.formatRupiah(BATAS_PROMO) + ")."
                        + "\n[INFO] Promo tidak aktif karena " + NAMA_MINUMAN_PROMO
                        + " belum dibeli minimal 2.";
            }
        } else {
            return "[INFO] Promo tidak aktif karena subtotal belum melebihi "
                    + Menu.formatRupiah(BATAS_PROMO) + ".";
        }
    }
}

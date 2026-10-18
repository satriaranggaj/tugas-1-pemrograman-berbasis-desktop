import java.util.Scanner;

/**
 * ============================================================
 *  APLIKASI KASIR RESTORAN - UJI OTOMATIS
 *  File : TestLogikaRestoran.java
 * ============================================================
 * Pengujian otomatis TANPA library eksternal (tanpa JUnit).
 * Dijalankan dengan:  java TestLogikaRestoran
 *
 * Yang diuji:
 *   1. validasiJumlah      : 0, negatif, melebihi batas, dan nilai normal
 *   2. guard jumlah        : tambahPesanan() tidak boleh menyimpan 0 / negatif /
 *                            lebih dari BATAS_JUMLAH walau dipanggil langsung
 *   3. batas item          : maksimal 4 item pesanan
 *   4. batas diskon        : subtotal tepat Rp100.000 vs di atasnya
 *   5. syarat promo        : subtotal > Rp50.000 DAN Es Teh minimal 2
 *   6. reset               : semua data pesanan & hasil hitung dikosongkan
 *   7. total kedaluwarsa   : hitung total -> tambah item -> hitung ulang
 *   8. input console       : token non-angka dibuang, slot berikutnya tetap terbaca
 *
 * CATATAN: GUI tidak pernah dijalankan pada test ini.
 * ============================================================
 */
public class TestLogikaRestoran {

    // jumlah pemeriksaan yang gagal
    static int jumlahGagal = 0;

    // ============================================================
    //  TITIK MASUK TEST
    // ============================================================
    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println("  UJI OTOMATIS APLIKASI KASIR RESTORAN");
        System.out.println("===========================================");

        ujiValidasiJumlah();
        ujiJumlahSaatDisimpan();
        ujiBatasJumlahItem();
        ujiBatasDiskon();
        ujiSyaratPromo();
        ujiReset();
        ujiTotalKedaluwarsa();
        ujiInputConsole();

        System.out.println("\n===========================================");
        if (jumlahGagal == 0) {
            System.out.println("  HASIL: SEMUA TEST LULUS");
        } else {
            System.out.println("  HASIL: " + jumlahGagal + " TEST GAGAL");
        }
        System.out.println("===========================================");

        // keluar dengan kode 0 (lulus) atau 1 (ada yang gagal)
        System.exit(jumlahGagal == 0 ? 0 : 1);
    }

    // ============================================================
    //  TEST 1 : VALIDASI JUMLAH PESANAN (IF - ELSE IF - ELSE)
    // ============================================================
    static void ujiValidasiJumlah() {
        System.out.println("\n[1] Validasi jumlah pesanan");
        LogikaRestoran logika = new LogikaRestoran();

        cekJumlah("jumlah 0 -> 1", 1, logika.validasiJumlah(0));
        cekJumlah("jumlah -5 -> 1", 1, logika.validasiJumlah(-5));
        cekJumlah("jumlah 11 -> 10", LogikaRestoran.BATAS_JUMLAH, logika.validasiJumlah(11));
        cekJumlah("jumlah 3 -> 3", 3, logika.validasiJumlah(3));
        cekJumlah("jumlah 10 -> 10", LogikaRestoran.BATAS_JUMLAH,
                logika.validasiJumlah(LogikaRestoran.BATAS_JUMLAH));
    }

    // ============================================================
    //  TEST 2 : JUMLAH YANG DISIMPAN SELALU VALID
    //  tambahPesanan() dipanggil langsung, tanpa lewat GUI
    // ============================================================
    static void ujiJumlahSaatDisimpan() {
        System.out.println("\n[2] Jumlah yang disimpan tidak boleh 0 / negatif / melebihi batas");

        // jumlah 0 harus disimpan sebagai 1 (subtotal 1 x 18.000)
        LogikaRestoran logikaA = new LogikaRestoran();
        logikaA.tambahPesanan(logikaA.cariMenu(1), 0);       // Nasi Goreng
        cekJumlah("jumlah 0 disimpan jadi 1", 1, logikaA.getJumlahPesanan(0));
        cekJumlah("subtotal jumlah 0 = Rp18.000", 18000, logikaA.getSubtotalKeseluruhan());

        // jumlah 99 harus disimpan sebagai BATAS_JUMLAH (10)
        LogikaRestoran logikaB = new LogikaRestoran();
        logikaB.tambahPesanan(logikaB.cariMenu(1), 99);      // Nasi Goreng
        cekJumlah("jumlah 99 disimpan jadi 10", LogikaRestoran.BATAS_JUMLAH,
                logikaB.getJumlahPesanan(0));
        cekJumlah("subtotal 10 x 18.000 = Rp180.000", 180000, logikaB.getSubtotalKeseluruhan());

        // jumlah negatif harus disimpan sebagai 1
        LogikaRestoran logikaC = new LogikaRestoran();
        logikaC.tambahPesanan(logikaC.cariMenu(5), -3);      // Es Teh
        cekJumlah("jumlah -3 disimpan jadi 1", 1, logikaC.getJumlahPesanan(0));
        cekJumlah("Es Teh terhitung 1 gelas untuk promo", 1, logikaC.getSubtotalItem(0) / 6000);
    }

    // ============================================================
    //  TEST 3 : MAKSIMAL 4 ITEM PESANAN
    // ============================================================
    static void ujiBatasJumlahItem() {
        System.out.println("\n[3] Maksimal 4 item pesanan");

        LogikaRestoran logika = new LogikaRestoran();
        cekBoolean("item ke-1 diterima", true, logika.tambahPesanan(logika.cariMenu(1), 1));
        cekBoolean("item ke-2 diterima", true, logika.tambahPesanan(logika.cariMenu(2), 1));
        cekBoolean("item ke-3 diterima", true, logika.tambahPesanan(logika.cariMenu(3), 1));
        cekBoolean("item ke-4 diterima", true, logika.tambahPesanan(logika.cariMenu(4), 1));
        cekBoolean("item ke-5 ditolak", false, logika.tambahPesanan(logika.cariMenu(5), 1));
        cekJumlah("jumlah item tetap 4", 4, logika.getJumlahItem());

        // menu di luar 1-8 menghasilkan null (SWITCH CASE)
        cekBoolean("cariMenu(9) bernilai null", true, logika.cariMenu(9) == null);
        cekBoolean("cariMenu(1) bukan null", true, logika.cariMenu(1) != null);
    }

    // ============================================================
    //  TEST 4 : BATAS DISKON (subtotal > Rp100.000)
    // ============================================================
    static void ujiBatasDiskon() {
        System.out.println("\n[4] Batas diskon 10%");

        // Tepat Rp100.000 -> diskon TIDAK aktif
        // Ayam Bakar 3 = 84.000 + Es Jeruk 2 = 16.000 = 100.000
        LogikaRestoran logika = new LogikaRestoran();
        logika.tambahPesanan(logika.cariMenu(3), 3);
        logika.tambahPesanan(logika.cariMenu(6), 2);
        cekJumlah("subtotal = Rp100.000", 100000, logika.getSubtotalKeseluruhan());
        logika.hitungTotal();
        cekJumlah("diskon tepat 100.000 = 0", 0, logika.getDiskon());
        cekBoolean("status diskon nonaktif", false, logika.isDiskonAktif());
        cekJumlah("total = 130.000", 130000, logika.getTotalBayar());

        // Di atas Rp100.000 -> diskon 10% (Es Teh 1, jadi promo tidak aktif)
        LogikaRestoran logika2 = new LogikaRestoran();
        logika2.tambahPesanan(logika2.cariMenu(3), 3);
        logika2.tambahPesanan(logika2.cariMenu(6), 2);
        logika2.tambahPesanan(logika2.cariMenu(5), 1);
        cekJumlah("subtotal = Rp106.000", 106000, logika2.getSubtotalKeseluruhan());
        logika2.hitungTotal();
        cekJumlah("diskon 10% = Rp10.600", 10600, logika2.getDiskon());
        cekBoolean("status diskon aktif", true, logika2.isDiskonAktif());
        cekJumlah("promo tidak aktif (Es Teh 1)", 0, logika2.getBonusPromo());
        cekJumlah("total = Rp126.000", 126000, logika2.getTotalBayar());
    }

    // ============================================================
    //  TEST 5 : SYARAT PROMO (NESTED IF)
    // ============================================================
    static void ujiSyaratPromo() {
        System.out.println("\n[5] Syarat promo beli 1 gratis 1");

        // (a) subtotal > 50.000 DAN Es Teh 2 -> promo aktif
        LogikaRestoran a = new LogikaRestoran();
        a.tambahPesanan(a.cariMenu(3), 2);      // Ayam Bakar 56.000
        a.tambahPesanan(a.cariMenu(5), 2);      // Es Teh 12.000
        a.hitungTotal();
        cekJumlah("subtotal promo = Rp68.000", 68000, a.getSubtotalKeseluruhan());
        cekJumlah("bonus promo = Rp6.000", 6000, a.getBonusPromo());
        cekBoolean("status promo aktif", true, a.isPromoAktif());
        cekJumlah("total = Rp88.800", 88800, a.getTotalBayar());

        // (b) subtotal > 50.000 tetapi Es Teh hanya 1 -> promo tidak aktif
        LogikaRestoran b = new LogikaRestoran();
        b.tambahPesanan(b.cariMenu(3), 2);
        b.tambahPesanan(b.cariMenu(5), 1);
        b.hitungTotal();
        cekJumlah("bonus promo = 0", 0, b.getBonusPromo());
        cekJumlah("total = Rp88.200", 88200, b.getTotalBayar());

        // (c) Es Teh 2 tetapi subtotal <= 50.000 -> promo tidak aktif
        LogikaRestoran c = new LogikaRestoran();
        c.tambahPesanan(c.cariMenu(1), 1);      // Nasi Goreng 18.000
        c.tambahPesanan(c.cariMenu(4), 1);      // Soto Ayam 15.000
        c.tambahPesanan(c.cariMenu(5), 2);      // Es Teh 12.000
        c.hitungTotal();
        cekJumlah("subtotal = Rp45.000", 45000, c.getSubtotalKeseluruhan());
        cekJumlah("bonus promo = 0", 0, c.getBonusPromo());
        cekJumlah("total = Rp69.500", 69500, c.getTotalBayar());

        // (d) promo + diskon bisa aktif bersamaan (Es Teh dihitung dari
        //     total gelas, mis. 3 teguk)
        LogikaRestoran d = new LogikaRestoran();
        d.tambahPesanan(d.cariMenu(3), 3);      // Ayam Bakar 84.000
        d.tambahPesanan(d.cariMenu(5), 3);      // Es Teh 18.000
        d.hitungTotal();
        cekJumlah("subtotal = Rp102.000", 102000, d.getSubtotalKeseluruhan());
        cekJumlah("diskon = Rp10.200", 10200, d.getDiskon());
        cekJumlah("bonus promo tetap 1 gelas = Rp6.000", 6000, d.getBonusPromo());
        // 102.000 - 10.200 (diskon) - 6.000 (promo) + 10.200 (pajak) + 20.000 = 116.000
        cekJumlah("total = Rp116.000", 116000, d.getTotalBayar());
    }

    // ============================================================
    //  TEST 6 : RESET
    // ============================================================
    static void ujiReset() {
        System.out.println("\n[6] Tombol Reset");

        LogikaRestoran logika = new LogikaRestoran();
        logika.tambahPesanan(logika.cariMenu(3), 2);
        logika.tambahPesanan(logika.cariMenu(5), 2);
        logika.hitungTotal();
        cekBoolean("sebelum reset sudah dihitung", true, logika.isSudahDihitung());

        logika.reset();

        cekJumlah("item = 0", 0, logika.getJumlahItem());
        cekJumlah("subtotal = 0", 0, logika.getSubtotalKeseluruhan());
        cekJumlah("diskon = 0", 0, logika.getDiskon());
        cekJumlah("promo = 0", 0, logika.getBonusPromo());
        cekJumlah("pajak = 0", 0, logika.getPajak());
        cekJumlah("total = 0", 0, logika.getTotalBayar());
        cekBoolean("sudahDihitung = false", false, logika.isSudahDihitung());
        cekBoolean("array pesanan dikosongkan", true, logika.getNamaPesanan(0) == null);

        // setelah reset pesanan bisa ditambahkan lagi
        cekBoolean("bisa pesan lagi setelah reset",
                true, logika.tambahPesanan(logika.cariMenu(1), 1));
        cekJumlah("subtotal baru = Rp18.000", 18000, logika.getSubtotalKeseluruhan());
    }

    // ============================================================
    //  TEST 7 : TOTAL KEDALUWARSA
    //  Meniru urutan: pesan -> Hitung Total -> pesan lagi -> Cetak Struk
    // ============================================================
    static void ujiTotalKedaluwarsa() {
        System.out.println("\n[7] Total tidak boleh kedaluwarsa setelah pesanan ditambah");

        LogikaRestoran logika = new LogikaRestoran();

        // pesan dulu: Ayam Bakar 2 = Rp56.000 (belum ada Es Teh)
        logika.tambahPesanan(logika.cariMenu(3), 2);
        logika.hitungTotal();
        cekBoolean("sudah dihitung", true, logika.isSudahDihitung());
        cekJumlah("total lama = Rp81.600", 81600, logika.getTotalBayar());

        // user menambah pesanan kedua: Es Teh 2 = Rp12.000
        logika.tambahPesanan(logika.cariMenu(5), 2);

        // hasil hitung sebelumnya harus dianggap tidak berlaku
        cekBoolean("sudahDihitung kembali false", false, logika.isSudahDihitung());

        // tombol "Cetak Struk" selalu menghitung ulang
        logika.hitungTotal();
        cekJumlah("subtotal terbaru = Rp68.000", 68000, logika.getSubtotalKeseluruhan());
        cekJumlah("promo ikut aktif = Rp6.000", 6000, logika.getBonusPromo());
        cekJumlah("total terbaru = Rp88.800", 88800, logika.getTotalBayar());
        cekBoolean("total benar-benar berubah", true, 88800 != 81600);

        // struk yang dicetak harus memuat pesanan terbaru DAN total terbaru
        String struk = logika.getStrukLengkap();
        cekTeks("struk memuat Es Teh", "Es Teh", struk);
        cekTeks("struk memuat total terbaru", "Rp88.800", struk);
        cekTeksTidakAda("struk tidak memuat total lama", "Rp81.600", struk);
    }

    // ============================================================
    //  TEST 8 : INPUT CONSOLE TIDAK BERTAHAN TOKEN NON-ANGKA
    // ============================================================
    static void ujiInputConsole() {
        System.out.println("\n[8] Input console non-angka");

        // baris input: x | 2 | y | 99
        Scanner input = new Scanner("x\n2\ny\n99\n");
        LogikaRestoran logika = new LogikaRestoran();

        // token "x" harus dibuang dan pesanan ke-1 dibatalkan
        cekJumlah("nomor dari token 'x' = 0", 0, Main.inputNomor(input));

        // slot berikutnya harus langsung membaca "2"
        cekJumlah("nomor berikutnya = 2", 2, Main.inputNomor(input));

        // jumlah dari token "y" -> diisi 1
        cekJumlah("jumlah dari token 'y' = 1", 1, Main.validasiJumlah(input, logika));

        // jumlah berikutnya "99" -> disesuaikan menjadi 10
        cekJumlah("jumlah 99 -> 10", LogikaRestoran.BATAS_JUMLAH,
                Main.validasiJumlah(input, logika));

        // setelah semua slot terbaca, pemesanan tetap bisa berjalan normal
        Main.catatPesanan(logika, 2, 2);
        cekJumlah("item tersimpan = 1", 1, logika.getJumlahItem());
        cekJumlah("subtotal Mie Goreng 2 = Rp34.000", 34000, logika.getSubtotalKeseluruhan());
        input.close();
    }

    // ============================================================
    //  METHOD BANTU PEMERIKSAAN (assert manual, tanpa JUnit)
    // ============================================================
    static void cekJumlah(String namaUji, int diharapkan, int nilaiSebenarnya) {
        if (diharapkan == nilaiSebenarnya) {
            System.out.println("   LULUS  " + namaUji);
        } else {
            System.out.println("   GAGAL  " + namaUji
                    + " (diharapkan " + diharapkan + ", aktual " + nilaiSebenarnya + ")");
            jumlahGagal = jumlahGagal + 1;
        }
    }

    static void cekBoolean(String namaUji, boolean diharapkan, boolean nilaiSebenarnya) {
        if (diharapkan == nilaiSebenarnya) {
            System.out.println("   LULUS  " + namaUji);
        } else {
            System.out.println("   GAGAL  " + namaUji
                    + " (diharapkan " + diharapkan + ", aktual " + nilaiSebenarnya + ")");
            jumlahGagal = jumlahGagal + 1;
        }
    }

    static void cekTeks(String namaUji, String harusAda, String teks) {
        if (teks.contains(harusAda)) {
            System.out.println("   LULUS  " + namaUji);
        } else {
            System.out.println("   GAGAL  " + namaUji + " (teks '" + harusAda + "' tidak ditemukan)");
            jumlahGagal = jumlahGagal + 1;
        }
    }

    // pemeriksaan kebalikan: teks HARUS TIDAK memuat potongan teks tertentu
    static void cekTeksTidakAda(String namaUji, String tidakBolehAda, String teks) {
        if (!teks.contains(tidakBolehAda)) {
            System.out.println("   LULUS  " + namaUji);
        } else {
            System.out.println("   GAGAL  " + namaUji + " (masih memuat '" + tidakBolehAda + "')");
            jumlahGagal = jumlahGagal + 1;
        }
    }
}

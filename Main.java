import java.util.Scanner;

/**
 * ============================================================
 *  APLIKASI RESTORAN - VERSI CONSOLE / TERMINAL
 *  STSI4201 Pemrograman Berbasis Desktop
 *  File : Main.java
 * ============================================================
 * Berkas ini adalah APLIKASI CONSOLE (tidak dihapus).
 *
 * Yang berubah: seluruh aturan bisnis (array menu, switch case,
 * validasi jumlah, diskon, pajak, biaya pelayanan, promo, struk)
 * dipindahkan ke class LogikaRestoran.java supaya bisa dipakai
 * bersama oleh versi console (berkas ini) dan versi GUI Swing.
 * Hasil tampilan program ini tetap sama seperti sebelumnya.
 *
 * Untuk menjalankan versi GUI gunakan : java RestoranApp
 * ============================================================
 */
public class Main {

    public static void main(String[] args) {

        // membuat objek Scanner untuk membaca input keyboard
        Scanner input = new Scanner(System.in);

        // objek logika restoran : menyimpan array menu & aturan bisnis
        LogikaRestoran logika = new LogikaRestoran();
        Menu[] daftarMenu = logika.getDaftarMenu();

        System.out.println("===========================================");
        System.out.println("      APLIKASI RESTORAN SEDERHANA");
        System.out.println("===========================================");

        // ============================================================
        //  LANGKAH 1 : MENAMPILKAN DAFTAR MENU DIKELOMPOKKAN
        //  memakai method cetakMenu() dari class Menu
        // ============================================================
        System.out.println("\n=== MAKANAN ===");
        daftarMenu[0].cetakMenu(1);
        daftarMenu[1].cetakMenu(2);
        daftarMenu[2].cetakMenu(3);
        daftarMenu[3].cetakMenu(4);

        System.out.println("\n=== MINUMAN ===");
        daftarMenu[4].cetakMenu(5);
        daftarMenu[5].cetakMenu(6);
        daftarMenu[6].cetakMenu(7);
        daftarMenu[7].cetakMenu(8);

        System.out.println("\nPesanan maksimal " + LogikaRestoran.BATAS_PESANAN + " menu.");

        // ============================================================
        //  LANGKAH 2 : INPUT PESANAN (MAKSIMAL 4 MENU)
        //  Ditulis manual (tanpa perulangan) sesuai aturan tugas.
        // ============================================================

        // ---------- PESANAN KE-1 ----------
        System.out.println("\n--- Pesanan ke-1 ---");
        System.out.print("Nomor menu (1-8) : ");
        int nomor1 = inputNomor(input);

        // IF ELSE : nomor dicek dulu, kalau valid baru ditanyakan jumlah
        if (nomor1 < 1 || nomor1 > 8) {
            System.out.println("Nomor menu tidak valid, pesanan ke-1 dibatalkan.");
        } else {
            System.out.print("Jumlah pesanan   : ");
            int jumlah1 = validasiJumlah(input, logika);
            catatPesanan(logika, nomor1, jumlah1);
        }

        // ---------- PESANAN KE-2 ----------
        System.out.println("\n--- Pesanan ke-2 ---");
        System.out.print("Nomor menu (1-8) : ");
        int nomor2 = inputNomor(input);

        if (nomor2 < 1 || nomor2 > 8) {
            System.out.println("Nomor menu tidak valid, pesanan ke-2 dibatalkan.");
        } else {
            System.out.print("Jumlah pesanan   : ");
            int jumlah2 = validasiJumlah(input, logika);
            catatPesanan(logika, nomor2, jumlah2);
        }

        // ---------- PESANAN KE-3 ----------
        System.out.println("\n--- Pesanan ke-3 ---");
        System.out.print("Nomor menu (1-8) : ");
        int nomor3 = inputNomor(input);

        if (nomor3 < 1 || nomor3 > 8) {
            System.out.println("Nomor menu tidak valid, pesanan ke-3 dibatalkan.");
        } else {
            System.out.print("Jumlah pesanan   : ");
            int jumlah3 = validasiJumlah(input, logika);
            catatPesanan(logika, nomor3, jumlah3);
        }

        // ---------- PESANAN KE-4 ----------
        System.out.println("\n--- Pesanan ke-4 ---");
        System.out.print("Nomor menu (1-8) : ");
        int nomor4 = inputNomor(input);

        if (nomor4 < 1 || nomor4 > 8) {
            System.out.println("Nomor menu tidak valid, pesanan ke-4 dibatalkan.");
        } else {
            System.out.print("Jumlah pesanan   : ");
            int jumlah4 = validasiJumlah(input, logika);
            catatPesanan(logika, nomor4, jumlah4);
        }

        // ============================================================
        //  LANGKAH 3 : MENGECEK APAKAH ADA PESANAN
        //  IF : kalau pelanggan tidak memesan apa pun, program berhenti
        // ============================================================
        if (logika.getJumlahItem() == 0) {
            System.out.println("\nTidak ada pesanan. Terima kasih, sampai jumpa!");
            input.close();
            return;   // keluar dari method main
        }

        // ============================================================
        //  LANGKAH 4 : MENGHITUNG TOTAL (diskon, promo, pajak, biaya)
        //  seluruh perhitungan dilakukan di class LogikaRestoran
        //  (IF ELSE diskon + NESTED IF promo)
        // ============================================================
        logika.hitungTotal();

        System.out.println("\n" + logika.getInfoDiskon());
        System.out.println(logika.getInfoPromo());

        // ============================================================
        //  LANGKAH 5 : MENCETAK STRUK PESANAN
        // ============================================================
        System.out.println("\n" + logika.getStrukLengkap());

        input.close();   // menutup Scanner
    }

    // ============================================================
    //  METHOD : MENCARI MENU (SWITCH CASE) lalu menyimpan pesanan
    // ============================================================
    static void catatPesanan(LogikaRestoran logika, int nomor, int jumlah) {

        // switch case untuk memilih menu berada di logika.cariMenu(nomor)
        Menu pesanan = logika.cariMenu(nomor);

        // IF ELSE : menu harus tidak null
        if (pesanan == null) {
            System.out.println("Nomor menu tidak valid, pesanan dibatalkan.");
        } else {
            logika.tambahPesanan(pesanan, jumlah);
        }
    }

    // ============================================================
    //  METHOD : MENERIMA NOMOR MENU DARI KEYBOARD  (IF - ELSE)
    //
    //  PENTING: input dibaca per TOKEN dengan next(). Dengan begitu token
    //  yang tidak valid (misalnya "x") benar-benar dibuang dari Scanner,
    //  sehingga slot pesanan berikutnya tidak ikut gagal membaca.
    //  (jika memakai hasNextInt()/nextInt(), token non-angka tidak pernah
    //   terrainsi sehingga program tersangkut di slot yang sama)
    // ============================================================
    static int inputNomor(Scanner input) {

        // IF : tidak ada input lagi (mis. EOF atau input dialihkan)
        if (!input.hasNext()) {
            System.out.println("Input selesai, pesanan dibatalkan.");
            return 0;               // nomor 0 otomatis tidak valid
        }

        String token = input.next();        // satu token langsung dibaca & dibuang
        int nomor = 0;
        boolean angka = true;

        // mencoba mengubah token menjadi angka
        try {
            nomor = Integer.parseInt(token.trim());
        } catch (NumberFormatException e) {
            angka = false;
        }

        // IF ELSE : token berupa angka atau bukan
        if (angka) {
            return nomor;
        } else {
            System.out.println("Input bukan angka (" + token + "), pesanan dibatalkan.");
            return 0;
        }
    }

    // ============================================================
    //  METHOD : MENERIMA JUMLAH PESANAN DARI KEYBOARD  (IF - ELSE)
    //  Token non-angka dibuang dengan cara yang sama seperti inputNomor()
    //  supaya pembacaan slot berikutnya tetap bisa dilanjutkan.
    //  Aturan jumlah (0 / negatif / lebih dari batas) TIDAK diulang di sini,
    //  melainkan memakai method milik LogikaRestoran.
    // ============================================================
    static int validasiJumlah(Scanner input, LogikaRestoran logika) {

        // IF : tidak ada input lagi
        if (!input.hasNext()) {
            System.out.println("Input selesai, jumlah diisi 1.");
            return 1;
        }

        String token = input.next();        // token langsung dibuang setelah dibaca
        int jumlahMentah = 0;
        boolean angka = true;

        try {
            jumlahMentah = Integer.parseInt(token.trim());
        } catch (NumberFormatException e) {
            angka = false;
        }

        // IF : token bukan angka -> diisi 1 (sama seperti aturan jumlah minimum)
        if (!angka) {
            System.out.println("Input bukan angka (" + token + "), jumlah diisi 1.");
            return 1;
        }

        // aturan bisnis tetap milik LogikaRestoran (tidak diduplikasi di sini)
        int jumlah = logika.validasiJumlah(jumlahMentah);

        // IF ELSE IF : hanya Pemberitahuan ke user, bukan aturan baru
        if (jumlah != jumlahMentah) {
            if (jumlahMentah <= 0) {
                System.out.println("Jumlah tidak boleh 0 atau negatif, diisi 1.");
            } else {
                System.out.println("Maksimal " + LogikaRestoran.BATAS_JUMLAH
                        + " per menu, diisi " + LogikaRestoran.BATAS_JUMLAH + ".");
            }
        }

        return jumlah;
    }
}

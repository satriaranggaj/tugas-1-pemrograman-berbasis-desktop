import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * ============================================================
 *  APLIKASI KASIR RESTORAN - DESKTOP (JAVA SWING)
 *  File : RestoranFrame.java
 * ============================================================
 * JFrame utama aplikasi kasir restoran.
 *
 * Layout BorderLayout:
 *   NORTH  : judul aplikasi
 *   CENTER : panel utama (kiri daftar menu, tengah daftar pesanan, kanan struk)
 *   SOUTH  : panel tombol aksi (Hitung Total, Cetak Struk, Reset)
 * ============================================================
 */
public class RestoranFrame extends JFrame {

    private final LogikaRestoran logika;      // aturan bisnis (dibagikan ke semua panel)
    private final PanelMenu panelMenu;        // panel A
    private final PanelPesanan panelPesanan;  // panel B + C
    private final PanelStruk panelStruk;      // panel E + F
    private final JLabel lblStatus = new JLabel("  Siap menerima pesanan.");   // baris pesan bawah

    public RestoranFrame() {

        // ---------- 1. PENGATURAN JFRAME ----------
        super("Aplikasi Kasir Restoran - STSI4201 Pemrograman Berbasis Desktop");
        logika = new LogikaRestoran();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ukuran jendela mengikuti area layar yang tersedia (aman untuk laptop kecil)
        java.awt.Rectangle batasLayar = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();
        setSize(Math.min(1120, batasLayar.width - 60), Math.min(680, batasLayar.height - 60));
        setMinimumSize(new Dimension(980, 600));

        // jendela ditampilkan di tengah area layar
        setLocation(batasLayar.x + (batasLayar.width - getWidth()) / 2,
                batasLayar.y + (batasLayar.height - getHeight()) / 2);

        // ---------- 2. MEMBUAT 3 PANEL UTAMA ----------
        panelMenu = new PanelMenu(logika.getDaftarMenu());
        panelPesanan = new PanelPesanan(logika);
        panelStruk = new PanelStruk(logika);

        setLayout(new BorderLayout());

        // ---------- 3. MENGISI TIAP BAGIAN BORDERLAYOUT ----------
        add(buatJudul(), BorderLayout.NORTH);
        add(buatPanelUtama(), BorderLayout.CENTER);
        add(buatPanelBawah(), BorderLayout.SOUTH);

        // ---------- 4. KETIKA BARIS MENU DIKLIK, COMBOBOX PESANAN MENGISI OTOMATIS ----------
        panelMenu.getTabelMenu().getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        // abaikan event saat tabel sedang di-drag
                        if (e.getValueIsAdjusting()) {
                            return;
                        }
                        int barisTampil = panelMenu.getTabelMenu().getSelectedRow();
                        // IF : ada baris yang dipilih
                        if (barisTampil >= 0) {
                            int barisData = panelMenu.getTabelMenu()
                                    .convertRowIndexToModel(barisTampil);
                            panelPesanan.pilihMenu(barisData);
                        }
                    }
                });

        panelStruk.perbaruiStatus();
    }

    // ============================================================
    //  METHOD : MEMBUAT JUDUL APLIKASI (bagian NORTH)
    // ============================================================
    private JPanel buatJudul() {

        JPanel panelJudul = new JPanel(new BorderLayout());
        panelJudul.setBackground(new Color(21, 42, 74));

        JLabel lblNama = new JLabel("   APLIKASI KASIR RESTORAN");
        lblNama.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblNama.setForeground(Color.WHITE);

        JLabel lblKeterangan = new JLabel("Tugas Praktik - Pemrograman Berbasis Desktop (STSI4201)   ");
        lblKeterangan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblKeterangan.setForeground(new Color(180, 200, 230));

        panelJudul.add(lblNama, BorderLayout.WEST);
        panelJudul.add(lblKeterangan, BorderLayout.EAST);
        panelJudul.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));

        return panelJudul;
    }

    // ============================================================
    //  METHOD : PANEL UTAMA (bagian CENTER)
    //  WEST  : daftar menu      CENTER : daftar pesanan      EAST : struk
    // ============================================================
    private JPanel buatPanelUtama() {

        JPanel panelUtama = new JPanel(new BorderLayout(10, 0));
        panelUtama.setBorder(BorderFactory.createEmptyBorder(10, 10, 6, 10));

        // lebar tiap panel agar tabel pesanan tidak tertekan
        panelMenu.setPreferredSize(new Dimension(350, 100));
        panelPesanan.setPreferredSize(new Dimension(400, 100));
        panelStruk.setPreferredSize(new Dimension(340, 100));

        panelUtama.add(panelMenu, BorderLayout.WEST);
        panelUtama.add(panelPesanan, BorderLayout.CENTER);
        panelUtama.add(panelStruk, BorderLayout.EAST);

        return panelUtama;
    }

    // ============================================================
    //  METHOD : PANEL TOMBOL AKSI + BARIS PESAN (bagian SOUTH)
    //  PANEL D : Hitung Total | Cetak Struk | Reset
    // ============================================================
    private JPanel buatPanelBawah() {

        // tombol-tombol aksi (menggunakan GridLayout 1 baris x 3 kolom)
        JButton btnHitung = new JButton("Hitung Total");
        btnHitung.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHitung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                aksiHitungTotal();
            }
        });

        JButton btnCetak = new JButton("Cetak Struk");
        btnCetak.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                aksiCetakStruk();
            }
        });

        JButton btnReset = new JButton("Reset");
        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                aksiReset();
            }
        });

        JPanel panelAksi = new JPanel(new GridLayout(1, 3, 8, 0));
        panelAksi.add(btnHitung);
        panelAksi.add(btnCetak);
        panelAksi.add(btnReset);

        // baris pesan status
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblStatus.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));

        JPanel panelBawah = new JPanel(new BorderLayout());
        panelBawah.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        panelBawah.add(panelAksi, BorderLayout.CENTER);
        panelBawah.add(lblStatus, BorderLayout.EAST);

        return panelBawah;
    }

    // ============================================================
    //  METHOD : AKSI TOMBOL "HITUNG TOTAL"
    //  Menghitung subtotal, diskon, pajak, biaya pelayanan, promo,
    //  dan total bayar melalui class LogikaRestoran.
    // ============================================================
    private void aksiHitungTotal() {

        // IF : belum ada pesanan -> beri tahu user
        if (logika.getJumlahItem() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Belum ada pesanan.\nTambahkan pesanan terlebih dahulu.",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // seluruh perhitungan (IF ELSE diskon + NESTED IF promo) terjadi di logika
        logika.hitungTotal();

        panelStruk.perbaruiStatus();       // update label status diskon & promo
        panelStruk.tampilkanStruk();       // update isi struk
        lblStatus.setText("  Total dihitung. Klik 'Cetak Struk' untuk melihat struk.");
    }

    // ============================================================
    //  METHOD : AKSI TOMBOL "CETAK STRUK"
    // ============================================================
    private void aksiCetakStruk() {

        // IF : belum ada pesanan -> tidak bisa dicetak
        if (logika.getJumlahItem() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Belum ada pesanan untuk dicetak.",
                    "Informasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // total SELALU dihitung ulang supaya struk pasti sesuai pesanan terbaru
        // (dulu hanya dihitung bila belum pernah dihitung, sehingga bisa
        //  menampilkan daftar pesanan baru dengan total yang sudah lama)
        logika.hitungTotal();

        panelStruk.perbaruiStatus();
        panelStruk.tampilkanStruk();

        // menampilkan struk lengkap dalam dialog
        JOptionPane.showMessageDialog(this, panelStruk.getTeksStruk(),
                "Struk Pesanan", JOptionPane.INFORMATION_MESSAGE);
        lblStatus.setText("  Struk ditampilkan.");
    }

    // ============================================================
    //  METHOD : AKSI TOMBOL "RESET"
    // ============================================================
    private void aksiReset() {

        // konfirmasi dulu sebelum menghapus semua pesanan
        int jawaban = JOptionPane.showConfirmDialog(this,
                "Hapus semua pesanan dan mulai dari awal?",
                "Konfirmasi Reset", JOptionPane.YES_NO_OPTION);

        // IF : user menekan tombol Yes
        if (jawaban == JOptionPane.YES_OPTION) {
            logika.reset();
            panelPesanan.perbaruiTabel();
            panelStruk.bersihkan();
            lblStatus.setText("  Semua pesanan telah dihapus.");
        }
    }
}

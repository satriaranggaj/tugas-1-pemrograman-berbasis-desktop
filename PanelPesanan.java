import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * ============================================================
 *  APLIKASI KASIR RESTORAN - DESKTOP (JAVA SWING)
 *  File : PanelPesanan.java
 * ============================================================
 * PANEL B : INPUT PESANAN  +  PANEL C : DAFTAR PESANAN
 *   - JComboBox   : memilih menu
 *   - JTextField  : mengisi jumlah pesanan
 *   - JButton     : Tambah Pesanan
 *   - JTable      : daftar pesanan (nama, jumlah, harga satuan, subtotal item)
 *   - Maksimal 4 item, lewat dari 4 akan muncul JOptionPane peringatan
 * ============================================================
 */
public class PanelPesanan extends JPanel {

    private final LogikaRestoran logika;        // aturan bisnis (bukan tampilan)
    private final JComboBox<Menu> comboMenu;    // pilihan menu
    private final JTextField txtJumlah;         // input jumlah
    private final JButton btnTambah;            // tombol tambah pesanan
    private final JTable tabelPesanan;          // tabel daftar pesanan
    private final DefaultTableModel modelPesanan;
    private final JLabel lblInfo;               // keterangan jumlah item

    public PanelPesanan(LogikaRestoran logika) {

        this.logika = logika;

        // ---------- 1. LAYOUT PANEL (BorderLayout) ----------
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createTitledBorder("PESANAN"));

        // ---------- 2. KETERANGAN (bagian NORTH) ----------
        lblInfo = new JLabel("Belum ada pesanan. Maksimal " + LogikaRestoran.BATAS_PESANAN + " item.");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblInfo.setForeground(new java.awt.Color(90, 90, 90));
        add(lblInfo, BorderLayout.NORTH);

        // ---------- 3. TABEL DAFTAR PESANAN (bagian CENTER) ----------
        modelPesanan = new DefaultTableModel(
                new Object[]{"Nama Menu", "Jumlah", "Harga Satuan", "Subtotal Item"}, 0) {
            @Override
            public boolean isCellEditable(int baris, int kolom) {
                return false;       // isi tabel otomatis dari program
            }
        };

        tabelPesanan = new JTable(modelPesanan);
        tabelPesanan.setRowHeight(24);
        tabelPesanan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelPesanan.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        // kolom 1 (Jumlah), 2 (Harga Satuan), 3 (Subtotal Item) rata kanan
        tabelPesanan.getColumnModel().getColumn(0).setPreferredWidth(155);
        tabelPesanan.getColumnModel().getColumn(1).setPreferredWidth(58);
        tabelPesanan.getColumnModel().getColumn(2).setPreferredWidth(92);
        tabelPesanan.getColumnModel().getColumn(3).setPreferredWidth(95);
        tabelPesanan.getColumnModel().getColumn(1).setCellRenderer(new RendererRupiah());
        tabelPesanan.getColumnModel().getColumn(2).setCellRenderer(new RendererRupiah());
        tabelPesanan.getColumnModel().getColumn(3).setCellRenderer(new RendererRupiah());

        add(new JScrollPane(tabelPesanan), BorderLayout.CENTER);

        // ---------- 4. PANEL INPUT PESANAN (GridLayout 1 baris x 3 kolom) ----------
        comboMenu = new JComboBox<Menu>();
        isiComboMenu();                           // mengisi 8 menu ke combo box
        comboMenu.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtJumlah = new JTextField("1");
        txtJumlah.setHorizontalAlignment(JTextField.CENTER);
        txtJumlah.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        btnTambah = new JButton("+ Tambah Pesanan");
        btnTambah.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                tambahPesanan();                  // isi method aksi tombol
            }
        });

        JPanel panelInput = new JPanel(new GridLayout(1, 3, 8, 0));
        panelInput.add(comboMenu);
        panelInput.add(txtJumlah);
        panelInput.add(btnTambah);
        add(panelInput, BorderLayout.SOUTH);

        // ---------- 5. MENGEMBALIKAN AREA TEKS KOSONG ----------
        perbaruiTabel();
    }

    // ============================================================
    //  METHOD : MENGISI COMBOBOX DENGAN 8 MENU (manual, tanpa perulangan)
    // ============================================================
    private void isiComboMenu() {
        Menu[] daftarMenu = logika.getDaftarMenu();
        comboMenu.addItem(daftarMenu[0]);
        comboMenu.addItem(daftarMenu[1]);
        comboMenu.addItem(daftarMenu[2]);
        comboMenu.addItem(daftarMenu[3]);
        comboMenu.addItem(daftarMenu[4]);
        comboMenu.addItem(daftarMenu[5]);
        comboMenu.addItem(daftarMenu[6]);
        comboMenu.addItem(daftarMenu[7]);
    }

    // ============================================================
    //  METHOD : AKSI TOMBOL "TAMBAH PESANAN"
    //  Berisi IF (batas 4 item), validasi input, dan pemanggilan
    //  aturan bisnis pada class LogikaRestoran.
    // ============================================================
    public void tambahPesanan() {

        // IF : maksimal 4 item pesanan -> muncul peringatan
        if (logika.getJumlahItem() >= LogikaRestoran.BATAS_PESANAN) {
            JOptionPane.showMessageDialog(this,
                    "Maksimal " + LogikaRestoran.BATAS_PESANAN
                            + " item pesanan.\nSilakan klik Reset untuk pesanan baru.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // IF : jumlah harus berupa angka
        int jumlah;
        try {
            jumlah = Integer.parseInt(txtJumlah.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Jumlah pesanan harus berupa angka.",
                    "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
            txtJumlah.setText("1");
            return;
        }

        // aturan jumlah 0 / negatif / lebih dari 10 (IF - ELSE IF - ELSE di logika)
        int jumlahValid = logika.validasiJumlah(jumlah);

        // IF : jumlah diubah oleh validasi -> beri tahu user
        if (jumlahValid != jumlah) {
            JOptionPane.showMessageDialog(this,
                    "Jumlah pesanan disesuaikan menjadi " + jumlahValid + ".",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }

        // menyimpan pesanan ke array milik class logika
        Menu menuDipilih = (Menu) comboMenu.getSelectedItem();
        boolean berhasil = logika.tambahPesanan(menuDipilih, jumlahValid);

        // IF ELSE : berhasil atau tidak
        if (berhasil) {
            perbaruiTabel();
            txtJumlah.setText("1");
            txtJumlah.requestFocusInWindow();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Pesanan tidak bisa ditambahkan (sudah maksimal).",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ============================================================
    //  METHOD : MENGEMBARKAN TABEL PESANAN DARI DATA LOGIKA
    //  Maksimal 4 baris, ditulis manual tanpa perulangan.
    // ============================================================
    public void perbaruiTabel() {

        modelPesanan.setRowCount(0);       // kosongkan tabel
        tambahBaris(0);
        tambahBaris(1);
        tambahBaris(2);
        tambahBaris(3);

        // IF : pesanan masih kosong atau sudah berisi
        if (logika.getJumlahItem() == 0) {
            lblInfo.setText("Belum ada pesanan. Maksimal " + LogikaRestoran.BATAS_PESANAN + " item.");
        } else {
            lblInfo.setText("Item tersimpan: " + logika.getJumlahItem() + " dari "
                    + LogikaRestoran.BATAS_PESANAN + ". Klik 'Hitung Total' untuk melihat struk.");
        }

        txtJumlah.setText("1");
    }

    // ============================================================
    //  METHOD : MENAMBAHKAN 1 BARIS KE TABEL PESANAN
    // ============================================================
    private void tambahBaris(int index) {
        Object[] baris = logika.getBarisTabel(index);
        // IF : baris tidak kosong (item memang ada)
        if (baris != null) {
            modelPesanan.addRow(baris);
        }
    }

    // ============================================================
    //  METHOD : MEMILIH MENU PADA COMBOBOX (dipakai RestoranFrame
    //  saat baris menu pada tabel diklik)
    // ============================================================
    public void pilihMenu(int indexMenu) {
        // IF : nomor menu valid
        if (indexMenu >= 0 && indexMenu < comboMenu.getItemCount()) {
            comboMenu.setSelectedIndex(indexMenu);
        }
    }
}

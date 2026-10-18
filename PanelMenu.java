import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * ============================================================
 *  APLIKASI KASIR RESTORAN - DESKTOP (JAVA SWING)
 *  File : PanelMenu.java
 * ============================================================
 * PANEL A : DAFTAR MENU
 *   - JTable dengan kolom : Nama Menu | Kategori | Harga
 *   - JComboBox untuk menyaring tampilan (SEMUA / MAKANAN / MINUMAN)
 *   - Data diambil dari array menu milik class LogikaRestoran
 *   - Perulangan tetap tidak dipakai, isi tabel ditulis manual
 * ============================================================
 */
public class PanelMenu extends JPanel {

    private final JTable tabelMenu;                       // tabel daftar menu
    private final TableRowSorter<TableModel> penyaring;   // pengurut & penyaring baris

    public PanelMenu(Menu[] daftarMenu) {

        // ---------- 1. LAYOUT PANEL (BorderLayout) ----------
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createTitledBorder("DAFTAR MENU"));
        setPreferredSize(new Dimension(350, 100));

        // ---------- 2. JUDUL + COMBOBOX FILTER (bagian NORTH) ----------
        JPanel panelAtas = new JPanel(new BorderLayout());

        JLabel lblJudul = new JLabel("Saring Kategori:");
        panelAtas.add(lblJudul, BorderLayout.WEST);

        final String[] pilihanFilter = {"SEMUA", "MAKANAN", "MINUMAN"};
        final JComboBox<String> comboFilter = new JComboBox<String>(pilihanFilter);
        panelAtas.add(comboFilter, BorderLayout.EAST);
        add(panelAtas, BorderLayout.NORTH);

        // ---------- 3. MODEL TABEL 3 KOLOM ----------
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Nama Menu", "Kategori", "Harga"}, 0) {
            @Override
            public boolean isCellEditable(int baris, int kolom) {
                return false;       // data menu tidak boleh diubah oleh user
            }
        };

        // ---------- 4. MENGISI 8 BARIS MENU (manual, tanpa perulangan) ----------
        tambahBarisMenu(model, daftarMenu[0]);
        tambahBarisMenu(model, daftarMenu[1]);
        tambahBarisMenu(model, daftarMenu[2]);
        tambahBarisMenu(model, daftarMenu[3]);
        tambahBarisMenu(model, daftarMenu[4]);
        tambahBarisMenu(model, daftarMenu[5]);
        tambahBarisMenu(model, daftarMenu[6]);
        tambahBarisMenu(model, daftarMenu[7]);

        // ---------- 5. MEMBUAT JTABLE ----------
        tabelMenu = new JTable(model);
        tabelMenu.setRowHeight(24);
        tabelMenu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelMenu.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // kolom Harga (index 2) dibuat rata kanan dengan RendererRupiah
        tabelMenu.getColumnModel().getColumn(2).setCellRenderer(new RendererRupiah());

        // ---------- 6. PENYARING BARIS (sorting + filter kategori) ----------
        penyaring = new TableRowSorter<TableModel>(model);
        tabelMenu.setRowSorter(penyaring);
        terapkanFilter("SEMUA");                       // awal: tampilkan semua menu

        // ---------- 7. KETIKA COMBOBOX DIGANTI, FILTER BERUBAH (IF - ELSE) ----------
        comboFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                terapkanFilter((String) comboFilter.getSelectedItem());
            }
        });

        // ---------- 8. TABEL DI DALAM SCROLLPANE (bagian CENTER) ----------
        add(new JScrollPane(tabelMenu), BorderLayout.CENTER);
    }

    // ============================================================
    //  METHOD : MENAMBAHKAN 1 BARIS MENU KE TABEL
    // ============================================================
    private static void tambahBarisMenu(DefaultTableModel model, Menu menu) {
        model.addRow(new Object[]{
                menu.getNama(),
                menu.getKategori(),
                Menu.formatRupiah(menu.getHarga())
        });
    }

    // ============================================================
    //  METHOD : MENYARING TAMPILAN MENU (IF - ELSE)
    //  "SEMUA"   : semua baris ditampilkan
    //  "MAKANAN" : hanya baris dengan Kategori = MAKANAN
    // ============================================================
    private void terapkanFilter(String kategori) {

        // IF : tampilkan semua menu
        if (kategori == null || kategori.equals("SEMUA")) {
            penyaring.setRowFilter(null);                  // tanpa penyaring
        } else {
            // ELSE : saring berdasarkan kolom ke-2 (Kategori)
            penyaring.setRowFilter(RowFilter.regexFilter(kategori, 1));
        }
    }

    // ============================================================
    //  METHOD : MENGEMBALIKAN TABEL (dipakai RestoranFrame)
    //  supaya klik baris menu bisa mengisi JComboBox pesanan
    // ============================================================
    public JTable getTabelMenu() {
        return tabelMenu;
    }
}

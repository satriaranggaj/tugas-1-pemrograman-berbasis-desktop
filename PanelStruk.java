import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * ============================================================
 *  APLIKASI KASIR RESTORAN - DESKTOP (JAVA SWING)
 *  File : PanelStruk.java
 * ============================================================
 * PANEL E : STRUK   dan   PANEL F : STATUS PROMO
 *   - JTextArea menampilkan subtotal, diskon, pajak, biaya pelayanan,
 *     promo, dan total bayar
 *   - Dua label status:
 *       "Diskon 10% Aktif" / "Diskon tidak aktif"
 *       "Promo Beli 1 Gratis 1 Es Teh Aktif" / "Promo tidak aktif"
 * ============================================================
 */
public class PanelStruk extends JPanel {

    private final LogikaRestoran logika;   // sumber data struk
    private final JTextArea areaStruk;     // teks struk
    private final JLabel lblDiskon;         // status diskon
    private final JLabel lblPromo;          // status promo

    public PanelStruk(LogikaRestoran logika) {

        this.logika = logika;

        // ---------- 1. LAYOUT PANEL (BorderLayout) ----------
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createTitledBorder("STRUK PEMBAYARAN"));

        // ---------- 2. STATUS DISKON & STATUS PROMO (GridLayout 2 baris) ----------
        lblDiskon = buatLabelStatus();
        lblPromo = buatLabelStatus();

        JPanel panelStatus = new JPanel(new GridLayout(2, 1, 0, 6));
        panelStatus.add(lblDiskon);
        panelStatus.add(lblPromo);
        add(panelStatus, BorderLayout.NORTH);

        // ---------- 3. JTEXTAREA STRUK DI DALAM SCROLLPANE (bagian CENTER) ----------
        areaStruk = new JTextArea();
        areaStruk.setEditable(false);                       // hanya untuk dibaca
        areaStruk.setLineWrap(true);
        areaStruk.setWrapStyleWord(false);
        areaStruk.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaStruk.setMargin(new Insets(8, 8, 8, 8));
        areaStruk.setBackground(new Color(252, 252, 252));

        add(new JScrollPane(areaStruk), BorderLayout.CENTER);

        bersihkan();
    }

    // ============================================================
    //  METHOD : MEMBUAT LABEL STATUS (kotak berwarna)
    // ============================================================
    private JLabel buatLabelStatus() {
        JLabel label = new JLabel(" ", JLabel.CENTER);
        label.setOpaque(true);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        return label;
    }

    // ============================================================
    //  METHOD : MENAMPILKAN STRUK KE JTEXTAREA
    //  Teks struk diambil dari method milik class LogikaRestoran
    // ============================================================
    public void tampilkanStruk() {
        areaStruk.setText(logika.getStrukLengkap());
        areaStruk.setCaretPosition(0);          // scroll ke atas
    }

    // ============================================================
    //  METHOD : MENGEMBALIKAN TEKS STRUK (dipakai untuk dialog Cetak Struk)
    // ============================================================
    public String getTeksStruk() {
        return logika.getStrukLengkap();
    }

    // ============================================================
    //  METHOD : MENGUBAH WARNA & TEKS LABEL STATUS (IF - IF ELSE)
    // ============================================================
    public void perbaruiStatus() {

        // IF : diskon aktif (hijau) atau tidak (abu-abu)
        if (logika.isDiskonAktif()) {
            lblDiskon.setText("Diskon 10% Aktif  (potongan " + Menu.formatRupiah(logika.getDiskon()) + ")");
            lblDiskon.setBackground(new Color(212, 237, 218));
            lblDiskon.setForeground(new Color(21, 87, 36));
        } else {
            lblDiskon.setText("Diskon 10% tidak aktif");
            lblDiskon.setBackground(new Color(233, 236, 239));
            lblDiskon.setForeground(new Color(73, 80, 87));
        }

        // IF ELSE : promo aktif (hijau) atau tidak (kuning = belum memenuhi syarat)
        if (logika.isPromoAktif()) {
            lblPromo.setText("Promo Beli 1 Gratis 1 " + LogikaRestoran.NAMA_MINUMAN_PROMO
                    + " Aktif  (bonus " + Menu.formatRupiah(logika.getBonusPromo()) + ")");
            lblPromo.setBackground(new Color(212, 237, 218));
            lblPromo.setForeground(new Color(21, 87, 36));
        } else {
            lblPromo.setText("Promo belum aktif");
            lblPromo.setToolTipText(logika.getInfoPromo());   // alasan tidak aktif
            lblPromo.setBackground(new Color(255, 243, 205));
            lblPromo.setForeground(new Color(120, 80, 0));
        }
    }

    // ============================================================
    //  METHOD : MENGOSONGKAN PANEL STRUK (dipakai tombol Reset)
    // ============================================================
    public void bersihkan() {

        areaStruk.setText("");
        areaStruk.setText("Belum ada struk.\n\n"
                + "1. Pilih menu pada tabel Daftar Menu\n"
                + "2. Isi jumlah, klik '+ Tambah Pesanan' (maksimal 4 item)\n"
                + "3. Klik 'Hitung Total'\n"
                + "4. Klik 'Cetak Struk'");

        lblDiskon.setText("Diskon 10% tidak aktif");
        lblDiskon.setBackground(new Color(233, 236, 239));
        lblDiskon.setForeground(new Color(73, 80, 87));

        lblPromo.setText("Promo belum aktif");
        lblPromo.setBackground(new Color(233, 236, 239));
        lblPromo.setForeground(new Color(73, 80, 87));
    }
}

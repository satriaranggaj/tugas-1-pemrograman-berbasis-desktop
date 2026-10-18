import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * ============================================================
 *  APLIKASI KASIR RESTORAN - DESKTOP (JAVA SWING)
 *  File : RendererRupiah.java
 * ============================================================
 * Class kecil untuk merapikan tampilan tabel:
 * kolom angka (jumlah, harga, subtotal) dibuat rata kanan
 * dan diberi jarak kiri-kanan sedikit.
 * ============================================================
 */
public class RendererRupiah extends DefaultTableCellRenderer {

    // Method milik JTable yang mengatur tampilan tiap sel
    @Override
    public Component getTableCellRendererComponent(JTable tabel, Object nilai, boolean sel,
                                                   boolean fokus, int baris, int kolom) {

        Component komponen = super.getTableCellRendererComponent(tabel, nilai, sel, fokus, baris, kolom);

        // kolom yang isinya teks angka dibuat rata kanan
        if (nilai instanceof String) {
            setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
            setHorizontalAlignment(SwingConstants.LEFT);
        }

        // jarak teks dari tepi sel
        setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));

        return komponen;
    }
}

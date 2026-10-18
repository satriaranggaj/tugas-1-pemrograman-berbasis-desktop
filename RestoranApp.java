import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * ============================================================
 *  APLIKASI KASIR RESTORAN - DESKTOP (JAVA SWING)
 *  STSI4201 Pemrograman Berbasis Desktop
 *  File : RestoranApp.java
 * ============================================================
 * TITIK MASUK (main) untuk versi GUI.
 *
 * CARA MENJALANKAN:
 *   1. buka file ini, klik kanan -> Run File
 *   2. atau di terminal : javac *.java  lalu  java RestoranApp
 *
 * CATATAN:
 *   - buka command ini untuk versi terminal (console): java Main
 * ============================================================
 */
public class RestoranApp {

    public static void main(String[] args) {

        // ---------- 1. MEMAASIKAN LOOK AND FEEL (tampilan) ----------
        // Nimbus membuat tampilan lebih modern. Bila gagal, dipakai
        // tampilan bawaan sistem operasi.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e2) {
                // tampilan bawaan tetap dipakai
            }
        }

        // ---------- 2. MENGATUR FONT DASAR APLIKASI ----------
        Font fontDasar = new Font("Segoe UI", Font.PLAIN, 13);
        UIManager.put("Label.font", fontDasar);
        UIManager.put("Button.font", fontDasar);
        UIManager.put("TextField.font", fontDasar);
        UIManager.put("ComboBox.font", fontDasar);
        UIManager.put("TextArea.font", fontDasar);
        UIManager.put("CheckBox.font", fontDasar);
        UIManager.put("TabbedPane.font", fontDasar);
        UIManager.put("Table.font", fontDasar);
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("OptionPane.messageFont", fontDasar);
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 13));

        // warna tampilan JOptionPane agar senada dengan aplikasi
        UIManager.put("OptionPane.background", new Color(250, 250, 250));

        // ---------- 3. MENJALANKAN GUI PADA THREAD KHUSUS SWING ----------
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RestoranFrame frame = new RestoranFrame();
                frame.setVisible(true);       // jendela ditampilkan
            }
        });
    }
}

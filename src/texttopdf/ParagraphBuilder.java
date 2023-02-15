/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texttopdf;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import static com.itextpdf.kernel.pdf.PdfName.Color;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.scene.control.Alert;

/**
 *
 * @author Michel
 */
public class ParagraphBuilder {

    public ParagraphBuilder(File fin, File fout) {
        Scanner sc = null;
        int nacc = 0;
        Color[] tabCol = new Color[]{
            new DeviceRgb(255, 0, 0),
            new DeviceRgb(0, 255, 0),
            new DeviceRgb(0, 0, 255),
            new DeviceRgb(0, 255, 255),
            new DeviceRgb(255, 0, 255),
            new DeviceRgb(255, 255, 0),};
        try {
            sc = new Scanner(fin);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERREUR");
            alert.setHeaderText("une erreur est survenue");
            alert.setContentText("erreur de fichier " + e);
            alert.show();
            return;
        }

        Document doc = null;
        try {

            doc = DocForPDF.getInstance(fout);

            Paragraph para = new Paragraph();
            
            PdfFont font = PdfFontFactory.createFont(StandardFonts.COURIER);
            para.setFont(font);
            Color c = new DeviceRgb(255, 0, 0);
            Text tx = new Text(fin.getName());
            tx.setBackgroundColor(c);
            para.add(tx);

            StringBuilder sb = new StringBuilder(100);

            sb.append("\n");
            
            while (sc.hasNext()) {
                String ph = sc.nextLine();
                sb.append('\u0000');//pour conserver l'indentation, sinon les blancs en tête sont éliminés
                for (int i = 0; i < ph.length(); i++) {
                    char ch = ph.charAt(i);
                    switch (ch) {
                        case '{':
                            tx = new Text(sb.toString());
                            para.add(tx);
                            tx=new Text(""+ch);
                            tx.setBackgroundColor(tabCol[nacc % 6]);
                            para.add(tx);
                            nacc++;
                            sb = new StringBuilder(100);
                            break;

                        case '}':
                            nacc--;
                            if (nacc < 0) {
                                nacc = 0;
                            }
                            tx=new Text(sb.toString());
                            para.add(tx);
                           tx = new Text("" + ch);
                            tx.setBackgroundColor(tabCol[nacc % 6]);
                            para.add(tx);
                            sb = new StringBuilder(100);
                            break;
                        default:
                            sb.append(ch);
                    }
                }
                sb.append("\n");

            }
            tx = new Text(sb.toString());

            para.add(tx);
               
            doc.add(para);
            doc.add(new AreaBreak());
          
            sc.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERREUR");
            alert.setHeaderText("une erreur est survenue");
            alert.setContentText("erreur de création de doc " + e);
            alert.show();

        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texttopdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import java.io.File;


/**
 *
 * @author Michel
 */
public class DocForPDF {
    private static PdfDocument pdfDoc=null;   
    private static Document doc = null;   
    
    private DocForPDF(){
        
    }
    public static Document getInstance(File f) throws Exception{
        if(doc == null) {
            pdfDoc = new PdfDocument(new PdfWriter(f));
            doc=new Document(pdfDoc);
        }
        return doc;
    }    
    
    public static void init(){
        doc=null;
    }
    
}

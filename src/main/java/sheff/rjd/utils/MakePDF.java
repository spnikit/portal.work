package sheff.rjd.utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.pdf.BaseFont;

import ru.aisa.pdf.PDFMaker;

//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.pdf.BaseFont;

public class MakePDF {

	 public static final String RESULT = "/home/vovako/result.pdf";
	 public static final String HTML = "/home/vovako/fpu26.html";
	 private final static String FONT_LOCATION =
			 "/home/vovako/maven_new/portal.iit.work/src/main/resources/fonts/Arial Regular.Ttf";
	public static void main(String[] args) throws  IOException, com.lowagie.text.DocumentException {
		// TODO Auto-generated method stub
		new MakePDF().createPdf(RESULT);
	}
	public void createPdf(String filename)
			throws  IOException, com.lowagie.text.DocumentException {

		
		InputStream is = new FileInputStream(new File(HTML));
		byte[] bytes = null;
		try {
			bytes = new byte[is.available()];
			int len;
			int tot = 0;
			do {
				len = is.read(bytes, tot, bytes.length - tot);
				tot += len;
			} while (len > 0);
		} catch (final Exception e) {
		} finally {
			if (is != null) {
				is.close();
			}
		}

		
		
		OutputStream os = new FileOutputStream(new File(RESULT));
		  
		
//		PDFMaker pdfmaker = new PDFMaker();
//		pdfmaker.generatePDFfromHTML(new String(bytes, "UTF-8"), os);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		HtmlCleaner cleaner = new HtmlCleaner();
		CleanerProperties props = cleaner.getProperties();
		props.setCharset("UTF-8");
		TagNode node = cleaner.clean(new String(bytes, "UTF-8"));
		new PrettyXmlSerializer(props).writeToStream(node, out);
		
		
		ITextRenderer renderer = new ITextRenderer();
		renderer.getFontResolver().addFont(FONT_LOCATION, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		renderer.setDocumentFromString(new String(out.toByteArray(), "UTF-8"));
		renderer.layout();
		renderer.createPDF(os);
		out.flush();
		out.close();
		os.close();

		    }
}

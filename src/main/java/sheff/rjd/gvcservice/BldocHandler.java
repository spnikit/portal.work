package sheff.rjd.gvcservice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import sheff.rjd.utils.Base64Codec;
import sheff.rjd.utils.Base64;

public class BldocHandler {

	private static final String gzipheader = "application/vnd.xfdl;content-encoding=\"base64-gzip\"\n";

	public String open(String tmp) throws UnsupportedEncodingException,
			Exception {

		byte[] bt64 = Base64GzipToBinary(tmp.substring(52).getBytes("UTF-8"));
		InputStream ins = new ByteArrayInputStream(bt64);
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory
				.newInstance();
		documentFactory.setNamespaceAware(true);
		documentFactory.setValidating(false);
		DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(ins);

		try {
			NodeList button = doc.getElementsByTagName("button");

			for (int i = 0; i < button.getLength(); i++) {

				if (((Element) button.item(i)).getElementsByTagName("value")
						.item(0).getTextContent().equalsIgnoreCase("signature")
						|| ((Element) button.item(i))
								.getElementsByTagName("value").item(0)
								.getTextContent().equalsIgnoreCase("Сохранить")
						|| ((Element) button.item(i))
								.getElementsByTagName("value").item(0)
								.getTextContent()
								.equalsIgnoreCase("Сохранить и выйти")
						|| ((Element) button.item(i))
								.getElementsByTagName("value").item(0)
								.getTextContent()
								.equalsIgnoreCase("Просмотрел")
						|| ((Element) button.item(i))
								.getElementsByTagName("value").item(0)
								.getTextContent()
								.equalsIgnoreCase("Отмена / Закрыть")
						|| ((Element) button.item(i))
								.getElementsByTagName("value").item(0)
								.getTextContent()
								.equalsIgnoreCase("Добавить подпись")
						|| ((Element) button.item(i))
								.getElementsByTagName("value").item(0)
								.getTextContent()
								.equalsIgnoreCase("Согласовать РДВ")) {

					if (((Element) button.item(i)).getElementsByTagName(
							"active").getLength() > 0) {
						Element local = (Element) ((Element) button.item(i))
								.getElementsByTagName("active").item(0);
						local.setTextContent("off");
						if (local.hasAttribute("compute")) {
							local.setAttribute("compute", "");
						}
					} else {
						Element active = doc.createElement("active");
						button.item(i).appendChild(active)
								.setTextContent("off");
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		StringWriter stringWriter = new StringWriter();
		Result result = new StreamResult(stringWriter);
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.transform(new DOMSource(doc), result);
		tmp = stringWriter.getBuffer().toString();

		String gblob = Base64.encodeBytes(tmp.getBytes("UTF-8"), Base64.GZIP);
		return gzipheader + gblob;

	}

	private static byte[] Base64GzipToBinary(byte theData[]) throws Exception {
		byte theDecodedBytes[] = null;
		theDecodedBytes = Base64Codec.base64Decode(theData);
		try {
			ByteArrayInputStream theStream = new ByteArrayInputStream(
					theDecodedBytes) {

				public synchronized int read() {
					int test = super.read();
					return test;
				}

			};
			GZIPInputStream theGzipStream = new GZIPInputStream(theStream);
			ByteArrayOutputStream theOutput = new ByteArrayOutputStream(2048);
			byte theBytes[] = new byte[2048];
			do {
				if (theGzipStream.available() != 1) {
					break;
				}
				int numBytes = theGzipStream.read(theBytes);
				if (numBytes < 1) {
					break;
				}
				theOutput.write(theBytes, 0, numBytes);
			} while (true);
			theGzipStream.close();
			theDecodedBytes = theOutput.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return theDecodedBytes;
	}

}

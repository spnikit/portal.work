package com.aisa.portal.invoice.ttk.endpoints;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.xmlbeans.XmlOptions;

import ru.aisa.sfact.ФайлDocument;

public class RunMe {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		FileInputStream in=new  FileInputStream("/home/zpss/work/testSF.xml");
		FileChannel fch = in.getChannel();

		ByteBuffer bf=  ByteBuffer.allocate((int) fch.size());
		fch.read(bf);
		fch.close();
		in.close();
		byte[] xml=bf.array();
String XML=(new String(xml, "windows-1251"));
XmlOptions options = new XmlOptions();
options.setCharacterEncoding("windows-1251");
options.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://aisa.ru/sfact", "Файл")) ;
ФайлDocument fdocument=ФайлDocument.Factory.parse(XML,options);
System.out.println(fdocument.toString());
FileOutputStream outp=new FileOutputStream("/home/zpss/work/testSF_out.xml");
outp.write(("<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n"+fdocument.toString()).getBytes("windows-1251"));
outp.close();
	}

}

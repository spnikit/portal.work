package sheff.rjd.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class BigDocHandler extends DefaultHandler{
	/**
	 * @uml.property  name="inTagDname"
	 */
	boolean inTagDname = false;
	/**
	 * @uml.property  name="inTagRequest"
	 */
	boolean inTagRequest = false;
	/**
	 * @uml.property  name="inTagDoc"
	 */
	boolean inTagDoc = false;
	/**
	 * @uml.property  name="inTagUsername"
	 */
	boolean inTagUsername = false;
	/**
	 * @uml.property  name="inTagCertid"
	 */
	boolean inTagCertid = false;
	/**
	 * @uml.property  name="inTagSign"
	 */
	boolean inTagSign = false;
	/**
	 * @uml.property  name="inTagIdsForDel"
	 */
	boolean inTagIdsForDel = false;
	/**
	 * @uml.property  name="inTagDocId"
	 */
	boolean inTagDocId = false;
	/**
	 * @uml.property  name="inTagBaseDocId"
	 */
	boolean inTagBaseDocId=false;
	/**
	 * @uml.property  name="inTagImageId"
	 */
	boolean inTagImageId = false;
	/**
	 * @uml.property  name="inTagImageBl"
	 */
	boolean inTagImageBl = false;
	/**
	 * @uml.property  name="inTagFname"
	 */
	boolean inTagFname = false;
	/**
	 * @uml.property  name="inTagDataGroupRef"
	 */
	boolean inTagDataGroupRef = false;
	/**
	 * @uml.property  name="imageId"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	public List <String> ImageId= new ArrayList<String>();
	/**
	 * @uml.property  name="imageBl"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	public List <String> ImageBl= new ArrayList<String>();
	/**
	 * @uml.property  name="docId"
	 */
	public String DocId=new String();
	/**
	 * @uml.property  name="username"
	 */
	public String username=new String();
	/**
	 * @uml.property  name="baseDocId"
	 */
	public String BaseDocId=new String();
	/**
	 * @uml.property  name="idsForDel"
	 */
	public String IdsForDel=new String();
	/**
	 * @uml.property  name="sign" multiplicity="(0 -1)" dimension="1"
	 */
	public byte[] sign=new byte[0];
	/**
	 * @uml.property  name="certid"
	 */
	public String certid=new String();
	/**
	 * @uml.property  name="request"
	 */
	public String request=new String();
	/**
	 * @uml.property  name="name"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	public List <String> name = new ArrayList<String>();
	/**
	 * @uml.property  name="blobs"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	public List <String> blobs = new ArrayList<String>();
	/**
	 * @uml.property  name="fname"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	public List <String> fname = new ArrayList<String>();
	/**
	 * @uml.property  name="datagroupref"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	public List <String> datagroupref = new ArrayList<String>();
	String blob=new String();
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (inTagDname || inTagBaseDocId || inTagDoc || inTagRequest || inTagUsername || inTagCertid || inTagSign || inTagIdsForDel || inTagDocId || inTagImageId || inTagImageBl) {
			char[] dch = new char[length];
			System.arraycopy(ch, start, dch, 0, length);
			if (inTagDname) name.add(new String(dch));
			if (inTagImageId) ImageId.add(new String(dch));
			if (inTagImageBl) ImageBl.add(new String(dch));
			if (inTagFname) fname.add(new String(dch));
			if (inTagDataGroupRef) datagroupref.add(new String(dch));
			if (inTagDoc) blob = blob+(new String(dch));
			
			if (inTagRequest) request=new String().valueOf(dch);
			if (inTagUsername) username=new String().valueOf(dch);
			if (inTagCertid) certid=new String().valueOf(dch);
			if (inTagIdsForDel) IdsForDel=new String().valueOf(dch);
			if (inTagDocId) DocId=new String().valueOf(dch);
			if (inTagBaseDocId) BaseDocId=new String().valueOf(dch);
			if (inTagSign) {
				org.apache.commons.codec.binary.Hex a = null;
				try{
				byte[]tmp=a.decodeHex(dch);
				sign = new byte[tmp.length];
				sign=tmp;
				} catch (Exception e){
					
				}
				
			}
		}

	}

	
	public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
		if (name.indexOf("DocumentName") > 0) {
			inTagDname = true;
		}
		if (name.equals("edt:DocumentFname")) {
			inTagFname = true;
		}
		if (name.equals("edt:DocumentDataGroup")) {
			inTagDataGroupRef = true;
		}
		
		if (name.indexOf("IdsForDel") > 0) {
			inTagIdsForDel = true;
		}
		if (name.equals("edt:DocId")) {
			inTagDocId = true;
		}
		if (name.equals("edt:BaseDocId")) {
			inTagBaseDocId = true;
		}
		if (name.indexOf("sign") > 0) {
			inTagSign = true;
		}
		if (name.indexOf("certid") > 0) {
			inTagCertid = true;
		}
		if (name.indexOf("DocumentBl") > 0) {
			inTagDoc = true;
		blob  = new String();
		}
		if (name.indexOf("request") > 0) {
			inTagRequest = true;
		}
		if (name.indexOf("username") > 0) {
			inTagUsername = true;
		}
		if (name.indexOf("ImageId") > 0) {
			inTagImageId = true;
		}
		if (name.indexOf("ImageBl") > 0) {
			inTagImageBl = true;
		}
		
		
	}

	public void endElement(String uri, String localName, String name) throws SAXException {
		if (name.indexOf("DocumentName") > 0) {
			inTagDname = false;
		}
		if (name.equals("edt:DocumentFname") ) {
			inTagFname = false;
		}
		if (name.equals("edt:DocumentDataGroup") ) {
			inTagDataGroupRef = false;
		}
		if (name.indexOf("IdsForDel") > 0) {
			inTagIdsForDel = false;
		}
		if (name.indexOf("sign") > 0) {
			inTagSign = false;
		}
		if (name.equals("edt:DocId")){
			inTagDocId = false;
		}
		if (name.equals("edt:BaseDocId")) {
			inTagBaseDocId = false;
		}
		if (name.indexOf("request") > 0) {
			inTagRequest = false;
		}
		if (name.indexOf("certid") > 0) {
			inTagCertid = false;
		}
		if (name.indexOf("username") > 0) {
			inTagUsername = false;
		}
		if (name.indexOf("DocumentBl") > 0) {
			inTagDname = false;
			blobs.add(blob);
			
		}
		if (name.indexOf("ImageId") > 0) {
			inTagImageId = false;
		}
		if (name.indexOf("ImageBl") > 0) {
			inTagImageBl = false;
		}
	}
	
}
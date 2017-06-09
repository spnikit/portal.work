package ru.aisa.rgd.HTMLGenerator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.aisa.rgd.utils.XMLUtil;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;

public class DocdataParser {
	
  public static List<Map<String,String>> createList(byte[] array) throws Exception {
      List<Map<String, String>> list = new ArrayList<Map<String,String>>();
      List<Map<String, String>> resultList = new ArrayList<Map<String,String>>();
      Document document = null;
	   
	  InputStream stream = new ByteArrayInputStream(array);
	  try {
		  document = XMLUtil.getDOM(stream);
	  } catch (SAXException e) {
		  throw new ServiceException(e, ServiceError.ERR_DOCUMENT_PARSING);
	  } finally {
		  if (stream != null) {
			  stream.close();
		  }
	  }
	  
	  Element root  = document.getDocumentElement();
	  resultList = iterTable(root, list);
	 
	  return resultList;
  }
  
  private static List<Map<String, String>> iterTable(Node node,  List<Map<String, String>> list) {
	  Map<String, String> map;
	  NodeList listNode = node.getChildNodes();
	  
	  for(int i = 0; i < listNode.getLength(); i++) {
		  if(listNode.item(i).hasChildNodes() && listNode.item(i).getChildNodes().item(0).getNodeName().equals("#text")) {		
			  map = new HashMap<String, String>();
			  map.put(listNode.item(i).getNodeName(), listNode.item(i).getTextContent());
			  list.add(map);		 	 
		  }
		  else {
			  map = new HashMap<String, String>();
			  if(listNode.item(i).getChildNodes().getLength() == 0) {
				  map = new HashMap<String, String>(1);
				  map.put(listNode.item(i).getNodeName(), "");
				  list.add(map);
			  } else if(listNode.item(i).getChildNodes().item(0).getNodeName().equals("#text")) {
				  map.put(listNode.item(i).getNodeName(), listNode.item(i).getTextContent());
				  list.add(map);
				  iterTable(listNode.item(i), list);
				  map = new HashMap<String, String>();
				  map.put(listNode.item(i).getNodeName(), "");
				  list.add(map);
			  } else {
				  map.put(listNode.item(i).getNodeName(), "");
				  list.add(map);
				  iterTable(listNode.item(i), list);
				  map = new HashMap<String, String>();
				  map.put(listNode.item(i).getNodeName(), "");
				  list.add(map);
			  }
		  }
	   }
	  return list;
  }
}

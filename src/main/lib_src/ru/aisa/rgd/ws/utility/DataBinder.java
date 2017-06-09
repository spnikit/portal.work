package ru.aisa.rgd.ws.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.aisa.rgd.etd.report.ReportDataMapper;
import ru.aisa.rgd.etd.report.ReportTableData;
import ru.aisa.rgd.utils.XMLUtil;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import sheff.rjd.utils.Base64;

public class DataBinder {
	
	private Document document;
	private Element rootEl;
	
	protected Logger logger	= Logger.getLogger(getClass());
	
	public DataBinder(Document document)
	{
		this.document = document;
		rootEl = document.getDocumentElement();
	}
	public DataBinder(String document)
	{
		this.document = XMLUtil.getDOM(document);
		rootEl = this.document.getDocumentElement();
	}
	
	public void setRootElement(String tagName)
	{
		rootEl = (Element) document.getElementsByTagName(tagName).item(0);
	}
	
	public void setRootLastElement(String tagName)
	{
		
		document.getElementsByTagName(tagName).getLength();
		rootEl = (Element) document.getElementsByTagName(tagName).item(document.getElementsByTagName(tagName).getLength()-1);
	}
	
	
	public void setRootElement(Element element)
	{
		rootEl = element;
	}
	
	
	public void resetRootElement()
	{
		rootEl = document.getDocumentElement();
	}
	
	/**
	 * @param tagName
	 * @return Array of nades with same name
	 */
	public NodeList getNodes(String tagName)
	{
		return rootEl.getElementsByTagName(tagName);
	}
	
	/**
	 * @param tagName
	 * @return First node in document with such name
	 * @throws InternalException 
	 */
	public Node getNode(String tagName) throws InternalException
	{
		NodeList n = rootEl.getElementsByTagName(tagName);
		if (n.getLength() < 1)
			throw new InternalException("Tag with name " + tagName + " doesn't exist");
		return n.item(0);
	
	}
	
	public Node getLastNode(String tagName) throws InternalException
	{
		NodeList n = rootEl.getElementsByTagName(tagName);
		if (n.getLength() < 1)
			throw new InternalException("Tag with name " + tagName + " doesn't exist");
		return n.item(n.getLength()-1);
	
	}
	
	public Element getElementWithAttribute(String tagName, Attribute attr)
	{
		Element element = null;
		NodeList list = getNodes(tagName);
		int lenght = list.getLength();
		for (int i = 0 ; i < lenght; i++)
		{
			if (((Element) list.item(i)).hasAttribute(attr.getName()) 
				&& ((Element) list.item(i)).getAttribute(attr.getName()).equals(attr.getValue()))
			{
				element =(Element) list.item(i);
				break;
			}
		}
		return element;
	}
	
	/**
	 * @param tagName Название элемента
	 * @param attrName Название аттрибута
	 * @return Пара (значение аттрибута, Element)
	 */
	public Map<String,Element> getElementsContainAttribute(String tagName, String attrName)
	{
		Element element;
		NodeList list = getNodes(tagName);
		int lenght = list.getLength();
		Map<String,Element> map = new HashMap<String,Element>();
		
		for (int i = 0 ; i < lenght; i++)
		{
			element = ((Element) list.item(i));
			if (element.hasAttribute(attrName))
			{
				map.put(element.getAttribute(attrName), element);

			}
		}
		return map;
	}
	
	public Node getNodeByHierarchy(String... strings) throws InternalException
	{
		Element node = (Element) document.getFirstChild();
		for (String str : strings)
		{
			node = (Element) node.getElementsByTagName(str).item(0);
			if (node == null) throw new 
				InternalException("Tag with name " + str + " in sequence " 
						+ Arrays.asList(strings).toString() + " doesn't exist");
		}
		return node;
	}
	
	public static Node getNode(Node parent, String tag)
	{
		return ((Element)parent).getElementsByTagName(tag).item(0);
	}
	
	/**
	 * @param tagName
	 * @return Value of first node
	 * @throws InternalException 
	 */
	public String getValue(String tagName) throws InternalException
	{
		Node node = rootEl.getElementsByTagName(tagName).item(0);
		if (node == null) throw new 
		InternalException("Tag with name " + tagName + " in root element  <" 
				+ rootEl.getLocalName() + "> doesn't exist");
		return node.getTextContent();
	}
	
	public String getValue(Element el, String tagName) throws InternalException
	{
		Node node = el.getElementsByTagName(tagName).item(0);
		if (node == null) throw new 
		InternalException("Tag with name " + tagName + " in root element  <" 
				+ rootEl.getLocalName() + "> doesn't exist");
		return node.getTextContent();
	}
	
	/*public static String getValue(Element el, String tagName) throws InternalException
	{
		Node node = el.getElementsByTagName(tagName).item(0);
		if (node == null) throw new 
		InternalException("Tag with name " + tagName + " in root element  <" 
				+ el.getLocalName() + "> doesn't exist");
		return node.getTextContent();
	}*/
	
	/**
	 * @param tagName
	 * @return Numerical value of node
	 * @throws InternalException 
	 * @throws NumberFormatException 
	 */
	/**
	 * @param tagName
	 * @return Numerical value of node
	 * @throws InternalException 
	 * @throws IllegalArgumentException
	 * @throws NumberFormatException
	 */
	public int getInt(String tagName) throws InternalException
	{
		Integer i;
		String str = null;
		try
		{
			str = getValue(tagName);
			i = Integer.parseInt(str);
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Value [" + str + "] of element  <" + tagName + "> is not Integer ");
		}
		return i;
	}

	/**
	 * @param tagName
	 * @return Numerical value of node
	 * @throws InternalException 
	 * @throws IllegalArgumentException
	 * @throws NumberFormatException
	 */
	public long getLong(String tagName) throws InternalException
	{
		Long i;
		String str = null;
		try
		{
			str = getValue(tagName);
			i = Long.parseLong(str);
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Value [" + str + "] of element  <" + tagName + "> is not Long ");
		}
		return i;
	}

	public BigDecimal getBigDecimal(String tagName) throws InternalException
	{
		String value = getValue(tagName);
		return new BigDecimal(value);
	}

	/**
	 *
	 * @param tagName
	 * @return
	 * @throws InternalException 
	 */
	public BigDecimal getBigDecimalIfFilled(String tagName) throws InternalException
	{
		String value = getValue(tagName);
		if (value != null && !value.equals(""))
			return new BigDecimal(value);
		else
			return null;
	}
	
	
	/**
	 * @param tagName
	 * @return Values of nodes
	 */
	public String[] getValuesAsArray(String tagName)
	{	
		NodeList nodeList = rootEl.getElementsByTagName(tagName);
		String[] array = new String[nodeList.getLength()];
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			array[i] = nodeList.item(i).getTextContent();
		}
		return array;
	}
	
	public <T> ArrayList<T> getValuesAsArrayList(String tagName)
	{	
		NodeList nodeList = rootEl.getElementsByTagName(tagName);
		ArrayList<T> array = new ArrayList<T>();
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			
			array.add((T) nodeList.item(i).getTextContent());
		}
		return array;
	}
	
	
	public void setNodeValue(String tag, Object value) throws DOMException, InternalException
	{
		if (value != null)
			getNode(tag).setTextContent(value.toString());
	}
	
	public  static void setNodeValue(Element el, String tag, Object value) throws DOMException, InternalException
	{
		if (value != null)
			getNode(el, tag).setTextContent(value.toString());
	}
	
	
	
	public void setLastNodeValue(String tag, Object value) throws DOMException, InternalException
	{
		if (value != null)
			getLastNode(tag).setTextContent(value.toString());
	}
	/**
	 * Clone given node and append it to parent element
	 * @param node
	 * @return Created node
	 */
	public static Node cloneNode(Node node)
	{	
		Node parent = node.getParentNode();
		Node clone = node.cloneNode(true);
		parent.appendChild(clone);
		return clone;
	}
	
	public Node cloneNode(String tag) throws InternalException
	{	
		Node node = getNode(tag);
		return cloneNode(node);
	}
	
	/*
	 * @param node 
	 * @param data
	 * @param map Mapping <Tag name, Method name>
	 * @deprecated Use fillComplexNode(Node node, HashMap<String, Object> data)
	 
	public static  void fillComplexNode(Node complexNode, Object data, Map<String, String> map)
	{
		Set<String> set = map.keySet();
		Node node;
		String value;
		for (String tag : set)
		{
			try {
				node = getNode(complexNode, tag);
				value = data.getClass().getMethod(map.get(tag)).invoke(data).toString();
				node.setTextContent(value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			
		}
		
	}*/
	
	/**
	 * Fills entire elements of node
	 * @param node
	 * @param data 
	 * 
	 */
	public  static void fillComplexNode(Node node, Map<String, Object> data)
	{
		Set<String> set = data.keySet();
		Node n;
		for (String tag : set)
		{
			n = getNode(node, tag);
			if (data.get(tag) != null) n.setTextContent(data.get(tag).toString());
		}
		
	}
	
	
	/**
	 * @deprecated use
	 *             {@link #fillTable(Object[], RowFiller, String, String, Object)}
	 * @param node
	 * @param list
	 * @param rm
	 */
	public static <T> void fillTable(Node node, List<T> list,
			DataRowMapper<T> rm)
	{
		fillComplexNode(node, rm.mapRow(list.get(0)));
		list.remove(0);
		Node n;
		for (T o : list)
		{
			n = cloneNode(node);
			fillComplexNode(n, rm.mapRow(o));
		}

	}

/**
 *  @deprecated use {@link #fillTable(Object[], RowFiller, Element, Object)
 * @param tag
 * @param list
 * @param rm
 * @throws InternalException
 */
	public <T> void fillTableNew(String tag, List<T> list, DataRowMapper<T> rm)
			throws InternalException
	{
		if (list.isEmpty())
			return;
		Node node = getNode(tag);
		Node parentNode = node.getParentNode();
		for (int i = 1; i < list.size(); i++)
		{
			Node cloneNode = node.cloneNode(true);
			fillComplexNode(cloneNode, rm.mapRow(list.get(i)));
			parentNode.appendChild(cloneNode);
		}
		fillComplexNode(node, rm.mapRow(list.get(0)));
		return;
	}

/**
	 * @deprecated use {@link #fillTable(Object[], RowFiller, Element, Object)
	 * @param tag
	 * @param list
	 * @param rm
	 * @throws InternalException
	 * 
	 */
	public <T> void fillTable(String tag, List<T> list, DataRowMapper<T> rm)
			throws InternalException
	{
		Node node = getNode(tag);
		fillComplexNode(node, rm.mapRow(list.get(0)));
		list.remove(0);
		Node n;
		for (T o : list)
		{
			n = cloneNode(node);
			fillComplexNode(n, rm.mapRow(o));
		}
	}

/**
	 *  @deprecated use {@link #fillTable(Object[], RowFiller, Element, Object)
	 * @param tag
	 * @param list
	 * @param rm
	 * @throws InternalException
	 */
	public <T> void fillTable1(String tag, List<T> list, DataRowMapper<T> rm)
			throws InternalException
	{
		Node node = getNode(tag);
		fillComplexNode(node, rm.mapRow(list.get(0)));
		// list.remove(0);
		Node n;
		for (T o : list)
		{
			if (!o.equals(list.get(0)))
			{
				n = cloneNode(node);
				fillComplexNode(n, rm.mapRow(o));
			}
		}
	}

	/**
	 * 
	 * @param rowsContent
	 * @param filler
	 * @param rowTepmlate
	 * @throws InternalException
	 * @note порядок элементов не соблюдается
	 */
	public <T1, T2> void fillTable(T1[] rowsContent, RowFiller<T1, T2> filler,
			Element rowTepmlate) throws Exception
	{
		fillTable(rowsContent, filler, rowTepmlate, null);
	}

	public <T1, T2> void fillTable(T1[] rowsContent, RowFiller<T1, T2> filler,
			String tableName, String rowName, T2 optionsObj) throws Exception
	{
		fillTable(rowsContent, filler,
				(Element) getNodeByHierarchy(getRootEl(), tableName, rowName),
				optionsObj);
	}

	public <T1, T2> void fillTable(T1[] rowsContent, RowFiller<T1, T2> filler,
			String tableName, String rowName) throws InternalException
	{
		fillTable(rowsContent, filler,
				(Element) getNodeByHierarchy(getRootEl(), tableName, rowName),
				null);
	}

	/**
	 * 
	 * @param rowsContent
	 * @param filler
	 * @param rowTepmlate
	 * @param optionsObj
	 *            - объект дополнительных опций. Будет передан в filler.
	 *            Передайте <b>null</b> если не требуется дополнительных свойств
	 * @throws DOMException 
	 * @throws InternalException
	 * @note потоконебезопасно, порядок перебора элементов не соблюдается
	 */
	
	public <T1, T2> void prettyfillTable(T1[] rowsContent, RowFiller<T1, T2> filler,
			String tableName, String rowName, boolean isUseOldSpace) throws InternalException
	{
		prettyFillTable(rowsContent, filler, (Element) getNodeByHierarchy(getRootEl(), tableName, rowName), isUseOldSpace, null);
	}
	
	
	
	public <T1, T2> void fillTable(T1[] rowsContent, RowFiller<T1, T2> filler,
			Element rowTepmlate, T2 optionsObj) throws DOMException, InternalException
	{
		if (rowsContent.length > 0)
		{
			Element rootEl = this.getRootEl();
			Element tableEl = (Element) rowTepmlate.getParentNode();
			this.setRootElement(tableEl);

			// начинаем со второй строки, чтобы был нетронутый шаблон
			for (int i = 1; i < rowsContent.length; i++)
			{
				Element cloneNode = (Element) rowTepmlate.cloneNode(true);
				tableEl.appendChild(cloneNode);
				this.setRootElement(cloneNode);
				filler.fillRow(this, rowsContent[i], i, optionsObj);
			}

			{// теперь заполняем первую строку
				this.setRootElement(rowTepmlate);
				filler.fillRow(this, rowsContent[0], 0, optionsObj);
			}
			this.setRootElement(rootEl);
		}
	}

	/**
	 * Добавить строки к таблице. Строки будут добавлены в конец
	 * 
	 * @param rowsContent
	 *            - добавляемые строки
	 * @param filler
	 *            - объект-наполнитель
	 * @param rowTepmlate
	 *            - элемент шаблон
	 * @param optionsObj
	 *            объект дополнительных опций. Будет передан в filler. Передайте
	 *            <b>null</b> если не требуется дополнительных свойств
	 * @throws Exception
	 * @note потоконебезопасно, порядок перебора элементов соблюдается. В метод
	 *       {@link RowFiller#fillRow(DataBinder, Object, int, Object)} третьим
	 *       параметром будет передаваться порядковый номер из массива
	 *       <b>rowsContent</b>
	 */
	
	
	
	//tag это название строки таблицы, tag1 название строки внутренней таблицы, tag2 название поля к которому привязывается внутренняя таблица
	public void fillTableOfTable(String tag,String tag1,String tag2, Map<String,List<Map<String,Object>>> list) throws InternalException
	{
		Node row = getNode(tag);
		Node n;
		for(String tag3:list.keySet())
		{
			n=cloneNode(row);
			setRootElement((Element)n);
			setNodeValue(tag2,tag3);
			Node row1=getNode(tag1);
			Node n1;
			for(int t=0;t<list.get(tag3).size();t++)
			{
				n1=cloneNode(row1);
				setRootElement((Element)n1);
				fillComplexNode(n1,list.get(tag3).toArray(new Map[0])[t]);
			}row1.getParentNode().removeChild(row1);
		}row.getParentNode().removeChild(row);
	}
	
	public void fillTableOfTable1(String tag,String tag1,List<String> l, Map<Map<String,Object>,List<Map<String,Object>>> list) throws InternalException
	{
		Node row = getNode(tag);
		Node n;
		int p=0;
		for(Map<String,Object> tag3:list.keySet())
		{
			n=cloneNode(row);
			setRootElement((Element)n);
			for(String l1:l)
			{
				setNodeValue(l1,tag3.get(l1));
			}
			Node row1=getNode(tag1);
			Node n1;
			for(int t=0;t<list.get(tag3).size();t++)
			{
				n1=cloneNode(row1);
				setRootElement((Element)n1);
				fillComplexNode(n1,list.get(tag3).toArray(new Map[0])[t]);
			}row1.getParentNode().removeChild(row1);
			p++;
		}row.getParentNode().removeChild(row);
	}
	
	public <T> void fillTable(List<T> list, DataRowMapper<T> rm, String... strings) throws InternalException
	{
		if (list.size() > 0)
		{
			Node node  = getNodeByHierarchy(strings);
			Node copy = cloneNode(node);
			fillComplexNode(node, rm.mapRow(list.get(0)));
			list.remove(0);
			Node n;
			for (T o : list)
			{
				n = cloneNode(copy);
				fillComplexNode(n, rm.mapRow(o));
			}
			copy.getParentNode().removeChild(copy);
		}
	}
	
	public <T> void fillExistTable(List<T> list, DataRowMapper<T> rm, String... strings) throws InternalException
	{
		Node node  = getNodeByHierarchy(strings);
		Node n;
		for (T o : list)
		{
			n = cloneNode(node);
			fillComplexNode(n, rm.mapRow(o));
		}
	}
	
	public void clearTable(String table, String row) throws InternalException
	{
		setRootElement(table);
		NodeList nl = getNodes(row);
        int nodeLength = nl.getLength();
        for (int i=1; i<nodeLength; i++){
        	resetRootElement();
        	getNode(table).removeChild(nl.item(0));
        }
        nl = nl.item(0).getChildNodes();
        for (int i=0; i<nl.getLength(); i++){
        	nl.item(i).setTextContent("");
        }
	}
	
	public <T> void mkTablesAndFillSingle(T [] singles, DataRowMapper<T> rm, String...strings) throws InternalException
	{
		if (singles.length > 0)
		{
			int i;
			Node node  = getNodeByHierarchy(strings);
			Node copy = cloneNode(node);
			fillComplexNode(node, rm.mapRow(singles[0]));
			Node n;
			for (i=1; i<singles.length; i++)
			{
				T o = singles[i];
				n = cloneNode(copy);
				fillComplexNode(n, rm.mapRow(o));
			}
			copy.getParentNode().removeChild(copy);
		}
	}	
	public <T1, T2> void prettyFillTable(T1[] rowsContent, RowFiller<T1, T2> filler, Element rowTepmlate, boolean isUseOldSpace, T2 optionsObj) throws DOMException, InternalException
	{
		Node dataEl = document.getElementsByTagNameNS("", "data").item(0);
		Node tmpEl = rowTepmlate;

		String space;
		StringBuffer spaceSB = new StringBuffer(30);
		if (isUseOldSpace)
		{
			Node prText = tmpEl.getPreviousSibling();
			while (prText != null)
			{
				spaceSB.append(prText.getNodeValue());
				prText = prText.getPreviousSibling();
			}
		}
		else
		{
			spaceSB.append("\n   ");
			while (tmpEl != dataEl)
			{
				spaceSB.append("   ");
				tmpEl = tmpEl.getParentNode();
			}
		}
		space = spaceSB.toString();
		Node nextNode = rowTepmlate.getNextSibling();

		if (rowsContent.length > 0)
		{
			Element rootEl = this.getRootEl();
			Element tableEl = (Element) rowTepmlate.getParentNode();
			this.setRootElement(tableEl);

			// начинаем со второй строки, чтобы был нетронутый шаблон
			for (int i = 1; i < rowsContent.length; i++)
			{
				Element cloneNode = (Element) rowTepmlate.cloneNode(true);

				if (nextNode != null)
					tableEl.insertBefore(cloneNode, nextNode);
				else
					tableEl.appendChild(cloneNode);
				tableEl.insertBefore(document.createTextNode(space), cloneNode);
				this.setRootElement(cloneNode);
				filler.fillRow(this, rowsContent[i], i, optionsObj);
			}

			{// теперь заполняем первую строку
				this.setRootElement(rowTepmlate);
				filler.fillRow(this, rowsContent[0], 0, optionsObj);
			}
			this.setRootElement(rootEl);
		}
	}
	public <T> void FillMultiTables(T [] singles, DataRowMapper<T> rm, int pos, String...strings) throws InternalException
	{
		if (singles.length > 0)
		{
			int i, z;
			Node node  = getNodeByHierarchy(strings[0]);
			Node tmp;
			NodeList nodelist;
			if (pos>0){
				nodelist = node.getChildNodes();
				i=nodelist.getLength();
				for(z=0; z<i; z++){
					//System.out.println("\n"+node.getNodeName()+"-->ChildNode"+Integer.toString(z)+"name: "+nodelist.item(z).getNodeName());
					tmp=nodelist.item(z);
					if(tmp.getNodeName().equals(strings[1])){
						pos-=1;
						if(pos<=0){
							node=tmp;
							z=i;
						}
					}
				}
				if (node == null) throw new InternalException("DataBinder-->FillMultiTables-->1 Node doesn't exist");
				nodelist=node.getChildNodes();
				i=nodelist.getLength();
				for(z=0; z<i; z++){
					//System.out.println("\n"+node.getNodeName()+"-->ChildNode"+Integer.toString(z)+"name: "+nodelist.item(z).getNodeName());
					tmp=nodelist.item(z);
					if(tmp.getNodeName().equals(strings[2])){
						node=tmp;
						z=i;
					}
				}
				if (node == null) throw new InternalException("DataBinder-->FillMultiTables-->2 Node doesn't exist");
				nodelist=node.getChildNodes();
				i=nodelist.getLength();
				for(z=0; z<i; z++){
					//System.out.println("\n"+node.getNodeName()+"-->ChildNode"+Integer.toString(z)+"name: "+nodelist.item(z).getNodeName());
					tmp=nodelist.item(z);
					if(tmp.getNodeName().equals(strings[3])){
						node=tmp;
						z=i;
					}
				}
				if (node == null) throw new InternalException("DataBinder-->FillMultiTables-->3 Node doesn't exist");
				Node copy = cloneNode(node);
				fillComplexNode(node, rm.mapRow(singles[0]));
				Node n;
				for (i=1; i<singles.length; i++)
				{
					T o = singles[i];
					n = cloneNode(copy);
					fillComplexNode(n, rm.mapRow(o));
				}
				copy.getParentNode().removeChild(copy);				
			}
		}
	}	
	 
	public  Node getNodeInChildsWithPos(Node node, int pos, String NodeName) throws InternalException
	{
		NodeList nodelist=node.getChildNodes();
		int nlLength = nodelist.getLength();
		int i;
		if (nlLength>0){
			int cnt=0;
			Node tmp;
			for(i=0; i<nlLength; i++){
				tmp=nodelist.item(i);
				if (tmp.getNodeName().equals(NodeName)) cnt+=1;
				if ((cnt==pos) && (tmp.getNodeName().equals(NodeName))){
					node=tmp;
					i = nlLength;
				}
			}
			return node;
		} else return null;
	}
	
	public <T> void FillMultiTables(T [] singles, DataRowMapper<T> rm, List<Integer> pos, String...strings) throws InternalException
	{
		if (singles.length > 0)
		{
			int i, z;
			Node node = getNodeByHierarchy(strings[0]);
			z = pos.size();
			//System.out.println("\nStart-->nodename: "+node.getNodeName()+"; strings[0]: "+strings[0]+"; pos.size="+Integer.toString(pos.size()));
				int k;
				for(i=0; i<z; i++){
					k=i*2+1;
					node=getNodeInChildsWithPos(node, pos.get(i), strings[k]);  //выбираем узел по позиции (обычно table<N> --> row)
					//System.out.println("\ni="+Integer.toString(i)+"; node="+node.getNodeName()+"; pos="+Integer.toString(pos.get(i))+"; strings["+Integer.toString(k)+"]="+strings[k]);
					node=getNodeInChildsWithPos(node, 1, strings[k+1]);		//берем первый элемент(обычно узел table<N>)
					//System.out.println("\ni="+Integer.toString(i)+"; node="+node.getNodeName()+"; pos=1; strings["+Integer.toString(k+1)+"]="+strings[k+1]);
				}
				node=getNodeInChildsWithPos(node, 1, strings[strings.length-1]);
				//System.out.println("\niFinal-->node="+node.getNodeName()+"; pos=1; strings["+Integer.toString(strings.length-1)+"]="+strings[strings.length-1]);
				Node copy = cloneNode(node);
				fillComplexNode(node, rm.mapRow(singles[0]));
				Node n;
				for (i=1; i<singles.length; i++)
				{
					T o = singles[i];
					n = cloneNode(copy);
					fillComplexNode(n, rm.mapRow(o));
				}
				copy.getParentNode().removeChild(copy);	
		}
	}	
	
	public  void cloneAndFill(String tag, List list) throws InternalException
	{
		Node node = getNode(tag);
		node.setTextContent(list.get(0).toString());
		list.remove(0);
		Node n;
		for (Object o : list)
		{
			n = cloneNode(node);
			n.setTextContent(o.toString());
		}
	}
	
	public Attribute createAttribute(String name, Object value) {
		return new Attribute(name, value);
	}
	
	public Element createElement(String tag, String parentTag) throws DOMException, InternalException
	{
		Element el = document.createElement(tag);
		getNode(parentTag).appendChild(el);
		return el;
	}
	public Element createElement(String tag) throws DOMException, InternalException
	{
		Element el = document.createElement(tag);
		return el;
	}
	public Element createElement(String tag, Element parentEl) throws DOMException, InternalException
	{
		Element el = document.createElement(tag);
		parentEl.appendChild(el);
		return el;
	}
	
	public Element createElementWithValue(String tag, String value, String parentTag) throws DOMException, InternalException
	{
		Element el = createElement(tag, parentTag);
		el.setTextContent(value);
		return el;
	}
	public Element createElementWithValue(String tag, String value, Element parentEl) throws DOMException, InternalException
	{
		Element el = createElement(tag, parentEl);
		el.setTextContent(value);
		return el;
	}
	
	public void createStageFlag(int stage) throws DOMException, InternalException
	{
		createElementWithValue(ETDForm.STAGE_FLAG, String.valueOf(stage), ETDForm.STAGE_ROOT_ELEMENT);
	}
	
	private static final String 
		X_REPEAT_TAG = "xforms:repeat",
		CLONABLE_TAG = "pane";
	
	/**
	 * @param attribute Аттрибут в xforms:repeat
	 * @param fields Наименования поле
	 * @param data Маппеные данные
	 * @throws DOMException
	 * @throws InternalException
	 */
	public void fillSimpleReportTable(Attribute attribute, List<String> fields, List<Map<String, String>> data) throws DOMException, InternalException
	{
		Element elrepeat = getElementWithAttribute(X_REPEAT_TAG, attribute);
		
		Element current = (Element) elrepeat.getElementsByTagName(CLONABLE_TAG).item(0);
		Element copy =  (Element) current.cloneNode(true);

		for ( int i=0, len = data.size(); i < len ; i++)
		{
			if (i!=0)
			{
				current = (Element) copy.cloneNode(true);
				elrepeat.appendChild(current);
			}
			
			setRootElement(current);
			Map<String, Element> map = getElementsContainAttribute("label", "sid");
			for (String attrValue : fields)
			{
				setNodeValue(map.get(attrValue), "value", data.get(i).get(attrValue));
			}
		}
	}
	

	public Element getRootEl() {
		return rootEl;
	}
	
	private void fillRTIterate(ReportDataMapper mapper, Element start) throws DOMException, InternalException
	{
		setRootElement(start);
		
		Attribute attribute = new Attribute(mapper.idTag, mapper.id);
		Element elrepeat = getElementWithAttribute(mapper.xformsTag, attribute);
		
		Element current = (Element) elrepeat.getElementsByTagName(mapper.repeatTag).item(0);
		Element copy =  (Element) current.cloneNode(true);
		
		List<ReportTableData> rows = mapper.getRows();
		
		for ( int i=0, len = rows.size(); i < len ; i++)
		{
			if (i!=0)
			{
				current = (Element) copy.cloneNode(true);
				elrepeat.appendChild(current);
			}
			
			setRootElement(current);
			Map<String, Element> map = getElementsContainAttribute(mapper.fieldTag, mapper.fieldNameAttr);
			ReportTableData  data = rows.get(i);
			Set<String> fields = data.getFields();
			for (String attrValue : fields)
			{
				setNodeValue(map.get(attrValue), "value", data.get(attrValue));
			};
			
			List<ReportDataMapper> tables = data.getTables();
			for (ReportDataMapper mapper2 : tables)
			{
					fillRTIterate(mapper2, current);
			}
		};
	}
	
	public void fillReportTable(ReportDataMapper mapper) throws DOMException, InternalException
	{
		Element backRoot = rootEl;
		logger.debug("ROOT BEFORE = " + backRoot.getLocalName());
		fillRTIterate(mapper, rootEl);
		setRootElement(backRoot);
		logger.debug("ROOT AFTER = " + backRoot.getLocalName());
		
	}
	
	public void setPicture(String tag, byte[] value) throws DOMException, InternalException, IOException
	{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		GZIPOutputStream gout = new GZIPOutputStream(buffer); 
		gout.write(value);
		gout.close();
		buffer.close();
		final String finalbytes = Base64.encodeBytes(buffer.toByteArray());
		setNodeValue(tag, finalbytes);
	}
	public void clearElements(String... tagsNames) throws InternalException
	{
		for (String tagName : tagsNames)
		{
			NodeList nl = rootEl.getElementsByTagName(tagName);
			for (int i = 0; i < nl.getLength(); i++)
			{
				nl.item(i).setTextContent("");
			}
		}
	}
	public void clearTable41(String table1, String table2, String row)
			throws InternalException
	{
		resetRootElement();
		Node table1node = getNode(table1);
		setRootElement(table1);

		NodeList gg = getNodes(row);
		List<Node> j = getChildNodes(table1node, row);
		for (int i = 1; i < j.size(); i++)
		{
			resetRootElement();
			table1node.removeChild((Node) j.get(i));
		}

		gg = gg.item(0).getChildNodes();

		for (int i = 0; i < gg.getLength(); i++)
		{
			if (gg.item(i) instanceof Element
					&& !gg.item(i).getNodeName().equals(table2))
				gg.item(i).setTextContent("");
			if (gg.item(i).getNodeName().equals(table2))
			{
				// System.out.println("rootEl=" +rootEl.getNodeName());
				// setRootElement(table2);
				// System.out.println("rootEl=" +rootEl.getNodeName());
				//
				// //Node table2node = getNode(table2);
				// List SUKOLIST = getChildNodes(rootEl, row);
				// System.out.println("list="+SUKOLIST);
				// NodeList nl = getNodes(row);
				// int nodeLength = nl.getLength();
				// for (int t=1; t<nodeLength; t++){
				// resetRootElement();
				// getNode(table2).removeChild(nl.item(0));
				// }
				// nl = nl.item(0).getChildNodes();
				// for (int t=0; t<nl.getLength(); t++){
				// nl.item(t).setTextContent("");
				// }
				resetRootElement();
				clearTable(table2, row);
			}
		}

	}
	static List<Node> getChildNodes(Node node, String name)
	{
		ArrayList<Node> r = new ArrayList<Node>();
		NodeList children = node.getChildNodes();
		int l = children.getLength();
		for (int i = 0; i < l; ++i)
		{
			if (name.equals(children.item(i).getNodeName()))
				r.add(children.item(i));
		}
		return r;
	}
	
	
	public static Node getNodeByHierarchy(Element rootEl, String... strings)
			throws InternalException
	{

		Node node = rootEl;
		for (String str : strings)
		{
			NodeList nl = node.getChildNodes();
			boolean flag = true;
			for (int i = 0; i < nl.getLength(); i++)
			{
				Node tmpNode = nl.item(i);
				if (tmpNode.getNodeType() == Node.ELEMENT_NODE)
				{
					if (tmpNode.getLocalName().equals(str))
					{
						node = tmpNode;
						flag = false;
						break;
					}

				}
			}
			if (flag)
//				throw new TagNotFoundException(str, rootEl.getTagName());
			{
			 throw new InternalException("Тэг с именем '" + str
			 + "' не найден в последовательности "
			 + Arrays.asList(strings).toString() + " (rootEl = "
			 + rootEl.getTagName() + ")");
			 }
		}
		return node;
	}
	
	public <T> int handleTable(String tableName, String rowName,
			RowHandler<T> handler, T obj) throws InternalException
	{
		return handleTable(getElement(tableName), new ElementLNameFilter(
				rowName), handler, obj);
	}
	public <T> int handleTable(Element tableEl, NodeFilter<Element> filter,
			RowHandler<T> handler, T obj) throws DOMException, InternalException
	{
		int rowCount = 0;
		Element rootEl = this.getRootEl();
		NodeList nl = tableEl.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++)
		{
			Node thisNode = nl.item(i);
			if (thisNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element thisEl = (Element) thisNode;
				boolean isHanlde;
				if (filter != null)
					isHanlde = filter.accept(thisEl);
				else
					isHanlde = true;
				if (isHanlde)
				{
					setRootElement(thisEl);
					handler.handleRow(this, rowCount, obj);
					rowCount++;
				}
			}
		}
		this.setRootElement(rootEl);
		return rowCount;
	}
	public Element getElement(String tagName) throws InternalException 
	{
		Node node = rootEl.getElementsByTagName(tagName).item(0);
		if (node == null)
		{
			 throw new InternalException("Тэг с именем '" + tagName
			 + "' не найден в последовательности (rootEl = "
			 + rootEl.getTagName() + ")");
			 }
		return (Element) node;
	}
	public String mergeElement(String tag, Object value, Element parentEl)
	{
		String oldValue = null;
		Element mergeEl = (Element) parentEl.getElementsByTagName(tag).item(0);

		if (mergeEl == null || mergeEl.getParentNode() != parentEl)
		{
			mergeEl = document.createElement(tag);
			parentEl.appendChild(mergeEl);
		} else
		{
			oldValue = mergeEl.getTextContent();
		}
		mergeEl.setTextContent(value.toString());
		return oldValue;
	}

	/**
	 * Записать новое значение тэга или добавить тэг с заданым значением если
	 * таковой отсутствовал среди дочерних элементов первого уровня. В качестве
	 * родительского элемента испольуется текущий <b>rootEl</b>
	 *
	 * @param tag
	 *            - имя добавляемого (редактируемого элемента)
	 * @param value
	 *            - новое значение элемента
	 * @return старое значение элемента или <b>null</b> если он отсутсвовал
	 */
	public String mergeElement(String tag, Object value)
	{
		return mergeElement(tag, value, rootEl);
	}
	public String getValueIfTagExists(String tagName)
	{
		Node node = rootEl.getElementsByTagName(tagName).item(0);
		if (node == null)
			return "";
		return node.getTextContent();
	}
	public Document createDocumentByCloningNodes(String sourseDocTagName, String newDocRootTagName, String[] tagsArray)
	{
		DomDocumentCloner cloner = new DomDocumentCloner(document);
		cloner.clone(sourseDocTagName, newDocRootTagName, tagsArray);
		return cloner.getNewDocument();
	}
}

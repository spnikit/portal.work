package ru.aisa.rgd.ws.utility;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sheff.rjd.utils.XMLUtil;

/**
 * Создает новый документ, рекурсивным клонированием перечисленных нод из другого
 * 
 * @author Nikita Karatun
 * 
 */
public class DomDocumentCloner
{
 private Document document;
 private Document newDocument;
 private Set tagsNames;

 /**
  * @param document исходный документ
  */
 public DomDocumentCloner(Document document)
 {
  super();
  this.document = document;
 }

 /**
  * @return исходный документ
  */
 public Document getDocument()
 {
  return document;
 }

 /**
  * @return новый документ
  */
 public Document getNewDocument()
 {
  return newDocument;
 }

 /**
  * Создает документ клонированием из ноды исходного документа списка нод
  * 
  * @param sourseDocTagName
  *            имя ноды исходного документа из которой надо клонировать
  * @param newDocRootTagName
  *            имя корневой ноды создаваемого документа
  * @param tagsArray
  *            массив имен нод, которые надо скопировать
  */
 public void clone(String sourseDocTagName, String newDocRootTagName, String[] tagsArray)
 {
  this.tagsNames = new HashSet(Arrays.asList(tagsArray));
  NodeList nodeList = document.getElementsByTagName(sourseDocTagName).item(0).getChildNodes();

  newDocument = XMLUtil.getDOM("<" + newDocRootTagName + "></" + newDocRootTagName + ">");
  Element parentElement = newDocument.getDocumentElement();
  copyNodes(parentElement, nodeList);
 }

 private void copyNodes(Element parentElement, NodeList nodeList)
 {
  for (int i = 0; i < nodeList.getLength(); i++)
  {
   Node node = nodeList.item(i);
   if (node.getNodeType() != Node.TEXT_NODE)
    if (nodeHasOnlyText(node))
     copyNode(parentElement, node, true, nodeIsNeededToCopy(node));
    else
    {
     Node newNode = copyNode(parentElement, node, false, nodeIsNeededToCopy(node));
     if (newNode != null)
      copyNodes((Element) newNode, node.getChildNodes());
    }
  }
 }

 private boolean nodeIsNeededToCopy(Node node)
 {
  return tagsNames.contains(node.getNodeName());
 }

 private boolean nodeHasOnlyText(Node node)
 {
  NodeList nodeList = node.getChildNodes();
  if (nodeList.getLength() == 1 && nodeList.item(0).getNodeType() == 3)
   return true;
  else
   return false;
 }

 private Node copyNode(Element parentElement, Node node, boolean isDeepCopy, boolean copyCondition)
 {
  if (copyCondition)
  {
   Node newNode = node.cloneNode(isDeepCopy);
   newDocument.adoptNode(newNode);
   parentElement.appendChild(newNode);
   return newNode;
  }
  else
   return null;
 }
}
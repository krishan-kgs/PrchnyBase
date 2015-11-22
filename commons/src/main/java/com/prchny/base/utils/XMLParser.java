/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 13, 2010
 *  @author singla
 */

package com.prchny.base.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {
  
  private Document document;
  
  private final File file;
  
  public XMLParser(final String filePath) {
  
    this(new File(filePath));
  }
  
  public XMLParser(final File file) {
  
    if (!file.exists()) {
      throw new IllegalArgumentException("No such File Exists.File Path :"
          + file.getAbsolutePath());
    }
    this.file = file;
  }
  
  public static Element parse(final String text) {
  
    try {
      final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(false);
      final DocumentBuilder db = dbf.newDocumentBuilder();
      final Document document =
          db.parse(new ByteArrayInputStream(text.getBytes()));
      return new Element(document.getDocumentElement());
    } catch (final ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (final FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (final SAXException e) {
      throw new RuntimeException(e);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public Element parse() {
  
    try {
      final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(false);
      final DocumentBuilder db = dbf.newDocumentBuilder();
      document = db.parse(new FileInputStream(file));
    } catch (final ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (final FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (final SAXException e) {
      throw new RuntimeException(e);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    return getRoot();
  }
  
  public Element getRoot() {
  
    return new Element(document.getDocumentElement());
  }
  
  public static class Element {
    
    private final org.w3c.dom.Element element;
    
    private Element(final org.w3c.dom.Element element) {
    
      this.element = element;
    }
    
    public List<Element> list() {
    
      final NodeList nList = element.getChildNodes();
      return getElementList(nList);
    }
    
    private List<Element> getElementList(final NodeList nList) {
    
      final List<Element> elements = new ArrayList<Element>(nList.getLength());
      for (int i = 0; i < nList.getLength(); i++) {
        final Node nNode = nList.item(i);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
          elements.add(new Element((org.w3c.dom.Element) nNode));
        }
      }
      return elements;
    }
    
    public List<Element> list(final String name) {
    
      return getElementList(element.getElementsByTagName(name));
    }
    
    public String attribute(final String name) {
    
      return element.getAttribute(name);
    }
    
    public Element get(final String name) {
    
      final NodeList nList = element.getElementsByTagName(name);
      for (int i = 0; i < nList.getLength(); i++) {
        final Node nNode = nList.item(i);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
          return new Element((org.w3c.dom.Element) nNode);
        }
      }
      return null;
    }
    
    public Element elementAt(final String nameOrPath) {
    
      final StringTokenizer tokenizer = new StringTokenizer(nameOrPath, "/");
      Element e = this;
      while (tokenizer.hasMoreTokens() && (e != null)) {
        e = e.get(tokenizer.nextToken());
      }
      return e;
    }
    
    public String text(final String nameOrPath) {
    
      final Element e = elementAt(nameOrPath);
      return e != null ? e.text() : null;
    }
    
    public String text() {
    
      return element.getFirstChild() != null ? element.getFirstChild()
          .getNodeValue() : null;
    }
    
    @Override
    public String toString() {
    
      return text();
    }
    
    public String getName() {
    
      return element.getNodeName();
    }
  }
  
  public static void main(final String[] args) {
  
    final XMLParser parser = new XMLParser("/home/kamal/Documents/sample.xml");
    final Element root = parser.parse();
    System.out.println(root.text("status"));
    /*
     * for (Element desciptions :
     * root.getChildElements("/HotelInfo/Address/AddressLine")) { }
     */
    final String response =
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><indus_billing_api><indus_billing_resp><status>failure</status><failure_reason>wrong xml data</failure_reason></indus_billing_resp></indus_billing_api>";
    final Element root1 = XMLParser.parse(response);
    final Element stat = root1.elementAt("status");
    System.out.println("status frm elmtn:" + stat.text());
    final List<Element> dataSets = root1.list();
    for (final Element dataSet : dataSets) {
      final String status = dataSet.text("status");
      System.out.println("fileName: " + status);
    }
  }
}

package com.tum.shakti;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

public class PaymentService implements PaymentWS{

	private static final String FILE_PATH = "../datasource/ds_12_3.xml";

	private int accountID1BeforeBalance;

	private int accountID2BeforeBalance;
	
	private int accountID1AfterBalance;
	
	boolean status;
	
	

	private int accountID2AfterBalance;

	private String accountID;
	private String acccountHolder;
	private String accountBalance;

	public int queryAccount(String accountId) {

		Document doc = readXmlDataSourceToGetDocument();

		// Get the document's root XML node
		NodeList root = doc.getChildNodes();

		// Navigate down the hierarchy to get to the CEO node
		Node comp = getNode("bank", root);

		// NodeList nodes = exec.getChildNodes();
		NodeList nodeList = comp.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				if (currentNode.hasChildNodes()) {

					NodeList ndl1 = currentNode.getChildNodes();
					accountID = getNodeValue("accountID", ndl1);
					acccountHolder = getNodeValue("name", ndl1);
					accountBalance = getNodeValue("amount", ndl1);

					if (accountID.equalsIgnoreCase(accountId)) {

						return Integer.parseInt(accountBalance);

					}

				}
			}
		}
		return 0;

	}

	/**
	 * @return
	 */
	private Document readXmlDataSourceToGetDocument() {
		DOMParser parser = new DOMParser();
		try {
			parser.parse(FILE_PATH);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = parser.getDocument();
		return doc;
	}

	// above function have been overloaded so that the interface is same for all
	// it has been passed with different parameters
	public boolean transfer(String accountId1, String accountId2, int amount) {

		Document doc = readXmlDataSourceToGetDocument();

		// Get the document's root XML node
		NodeList root = doc.getChildNodes();

		// Navigate down the hierarchy to get to the CEO node
		Node comp = getNode("bank", root);

		// NodeList nodes = exec.getChildNodes();
		NodeList nodeList = comp.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				if (currentNode.hasChildNodes()) {

					NodeList ndl1 = currentNode.getChildNodes();
					 accountID = getNodeValue("accountID", ndl1);
					 acccountHolder = getNodeValue("name", ndl1);
					 accountBalance = getNodeValue("amount", ndl1);

					if (accountID.equalsIgnoreCase(accountId1)) {

						setAccountID1BeforeBalance(Integer.parseInt(accountBalance));
						
						NodeList list = currentNode.getChildNodes();

						for (int j = 0; j < list.getLength(); j++) {

							Node node = list.item(j);

							// get the salary element, and update the value
							if ("amount".equals(node.getNodeName())) {
								
								node.setTextContent(Integer
										.toString(getAccountID1BeforeBalance()
												- amount));
								setAccountID1AfterBalance(
										getAccountID1BeforeBalance()
												- amount);
							}
						}
						

					}

					if (accountID.equalsIgnoreCase(accountId2)) {
						
						Node node=null;
						setAccountID2BeforeBalance(Integer.parseInt(accountBalance));
						
						NodeList list = currentNode.getChildNodes();

						for (int j = 0; j < list.getLength(); j++) {

							 node = list.item(j);

							// get the salary element, and update the value
							if ("amount".equals(node.getNodeName())) {
							
								node.setTextContent(Integer
										.toString(getAccountID2BeforeBalance()
												+ amount));
								setAccountID2AfterBalance(getAccountID2BeforeBalance()
												+ amount);
								

							}
						}
						
						
						 try {
							status=checkIfTransactionIsSuccessFul(getAccountID1BeforeBalance(),getAccountID1AfterBalance(),getAccountID2BeforeBalance(),getAccountID2AfterBalance(),amount);
						} catch (TransactionException e) {

							System.out.println(e);
						}
						 
						 if(status)
						 {
						// write the content into xml file
							TransformerFactory transformerFactory = TransformerFactory
									.newInstance();
							Transformer transformer = null;
							try {
								transformer = transformerFactory.newTransformer();
							} catch (TransformerConfigurationException e) {
								e.printStackTrace();
							}
							DOMSource source = new DOMSource(doc);
							StreamResult result = new StreamResult(
									new File(
											FILE_PATH));
							try {
								transformer.transform(source, result);
							} catch (TransformerException e) {
								e.printStackTrace();
							}
						 }
							
						return status;
					}

				}
			}
			
		}
		return false;

	}

	
	/*
	 * 
	 */

	private boolean checkIfTransactionIsSuccessFul(
			int accountID1BeforeBalance2, int accountID1AfterBalance2,
			int accountID2BeforeBalance2, int accountID2AfterBalance2,
			int amount) throws TransactionException {

		
		int amountID1 = accountID1AfterBalance2 + amount;
		int amountID2 = accountID2AfterBalance2 - amount;

		if (amountID1 == accountID1BeforeBalance2) {
			if (amountID2 == accountID2BeforeBalance2) {
				if (accountID1AfterBalance2 > 0) {
					return true;
				}
				else {
					throw new TransactionException("##############Error Occured");
				}
			}
			else{
				throw new TransactionException("##############Error Occured");
			}

		}else {
			throw new TransactionException("#############Error Occured");
		}


	}

	protected static Node getNode(String tagName, NodeList nodes) {
		for (int x = 0; x < nodes.getLength(); x++) {
			Node node = nodes.item(x);
			if (node.getNodeName().equalsIgnoreCase(tagName)) {
				return node;
			}
		}

		return null;
	}

	protected String getNodeValue(Node node) {
		NodeList childNodes = node.getChildNodes();
		for (int x = 0; x < childNodes.getLength(); x++) {
			Node data = childNodes.item(x);
			if (data.getNodeType() == Node.TEXT_NODE)
				return data.getNodeValue();
		}
		return "";
	}

	protected static String getNodeValue(String tagName, NodeList nodes) {
		for (int x = 0; x < nodes.getLength(); x++) {
			Node node = nodes.item(x);
			if (node.getNodeName().equalsIgnoreCase(tagName)) {
				NodeList childNodes = node.getChildNodes();
				for (int y = 0; y < childNodes.getLength(); y++) {
					Node data = childNodes.item(y);
					if (data.getNodeType() == Node.TEXT_NODE)
						return data.getNodeValue();
				}
			}
		}
		return "";
	}

	protected static String getNodeAttr(String attrName, Node node) {
		NamedNodeMap attrs = node.getAttributes();
		for (int y = 0; y < attrs.getLength(); y++) {
			Node attr = attrs.item(y);
			if (attr.getNodeName().equalsIgnoreCase(attrName)) {
				return attr.getNodeValue();
			}
		}
		return "";
	}

	protected String getNodeAttr(String tagName, String attrName, NodeList nodes) {
		for (int x = 0; x < nodes.getLength(); x++) {
			Node node = nodes.item(x);
			if (node.getNodeName().equalsIgnoreCase(tagName)) {
				NodeList childNodes = node.getChildNodes();
				for (int y = 0; y < childNodes.getLength(); y++) {
					Node data = childNodes.item(y);
					if (data.getNodeType() == Node.ATTRIBUTE_NODE) {
						if (data.getNodeName().equalsIgnoreCase(attrName))
							return data.getNodeValue();
					}
				}
			}
		}

		return "";
	}

	/**
	 * @return the accountID1Balance
	 */
	public int getAccountID1BeforeBalance() {
		return accountID1BeforeBalance;
	}

	/**
	 * @return the accountID2Balance
	 */
	public int getAccountID2BeforeBalance() {
		return accountID2BeforeBalance;
	}

	/**
	 * @param accountID1Balance
	 *            the accountID1Balance to set
	 */
	public void setAccountID1BeforeBalance(int accountID1Balance) {
		this.accountID1BeforeBalance = accountID1Balance;
	}

	/**
	 * @param accountID2Balance
	 *            the accountID2Balance to set
	 */
	public void setAccountID2BeforeBalance(int accountID2Balance) {
		this.accountID2BeforeBalance = accountID2Balance;
	}
	
	public int getAccountID1AfterBalance() {
		return accountID1AfterBalance;
	}

	public void setAccountID1AfterBalance(int accountID1AfterBalance) {
		this.accountID1AfterBalance = accountID1AfterBalance;
	}

	public int getAccountID2AfterBalance() {
		return accountID2AfterBalance;
	}

	public void setAccountID2AfterBalance(int accountID2AfterBalance) {
		this.accountID2AfterBalance = accountID2AfterBalance;
	}

}
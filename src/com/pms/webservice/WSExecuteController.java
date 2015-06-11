package com.pms.webservice;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.pms.webservice.service.SyncService;

public class WSExecuteController {

	public String process(String localXml) {
		String resultXml = null;
		try{
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			InputStream inXml = new ByteArrayInputStream(localXml.getBytes("UTF-8")); 
			Document xmlDoc = db.parse(inXml);
			
			Element root = xmlDoc.getDocumentElement();
			if( "MESSAGE".equals(root.getTagName()) ) {
				
				SyncService ss = SyncService.getInstance(root);
				
				resultXml = ss.GetResult();
			}
			else {
				System.out.println("传入数据不正确，未找到message结点");
				resultXml = "<error>" + "传入数据不正确，未找到message结点" + "</error>";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return resultXml;
	}

}

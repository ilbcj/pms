package com.pms.webservice;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Element;

import com.pms.webservice.service.SyncService;

public class WSExecuteController {

	public String process(String localXml) {
		String resultXml = null;
		try{
			SAXBuilder builder=new SAXBuilder();
			InputStream inXml = new ByteArrayInputStream(localXml.getBytes("UTF-8")); 
			Document xmlDoc = builder.build(inXml);
			Element root = xmlDoc.getRootElement();
			
//			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
//			DocumentBuilder db = factory.newDocumentBuilder();
//			InputStream inXml = new ByteArrayInputStream(localXml.getBytes("UTF-8")); 
//			Document xmlDoc = db.parse(inXml);

			if( "MESSAGE".equals(root.getName()) ) {
				
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
			resultXml = e.getMessage();
		}
		return resultXml;
	}

}

package com.pms.webservice.service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class SyncSearchService extends SyncService {

	@Override
	public String GetResult() {
		String result = "";
		try{
			Element message = null, dataset = null, data = null, item = null;
			message = new Element("MESSAGE");
			
			dataset = new Element("DATASET");
			dataset.setAttribute("name", "WA_COMMON_010000");
			Attribute attr = new Attribute("rmk", "数据交互通用信息");
			dataset.setAttribute(attr);
			
			data = new Element("DATA");
			data.setAttribute("test","这是一个测试的文字");
			
			item = new Element("ITEM");
			item.setAttribute("key", "H010006");
			item.setAttribute("val", this.getDci().getFROM());
			item.setAttribute("rmk", "发起节点的标识");
			data.addContent(item);
			
			item = new Element("ITEM");
			item.setAttribute("key", "H010007");
			item.setAttribute("val", this.getDci().getTO());
			item.setAttribute("rmk", "目的节点的标识");
			data.addContent(item);
			
			item = new Element("ITEM");
			item.setAttribute("key", "I010014");
			item.setAttribute("val", this.getDci().getMESSAGE_SEQUENCE());
			item.setAttribute("rmk", "消息流水号");
			data.addContent(item);
			
			item = new Element("ITEM");
			item.setAttribute("key", "I010013");
			item.setAttribute("val", "254141");
			item.setAttribute("rmk", "消息类型（数据存储查询返回结果响应）");
			data.addContent(item);

			item = new Element("ITEM");
			item.setAttribute("key", "I010010");
			item.setAttribute("val", this.getDci().getMESSAGE_TYPE());
			item.setAttribute("rmk", "业务服务类型（数据存储访问服务）");
			data.addContent(item);
			
			dataset.addContent(data);
			message.addContent(dataset);
			Document doc = new Document(message);
			
//			Format format = Format.getCompactFormat();
//			format.setEncoding("UTF-8");
			//format.setIndent("    ");
			XMLOutputter XMLOut = new XMLOutputter(Format.getPrettyFormat().setEncoding("utf-8"));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(baos, "utf-8"); 
			XMLOut.output(doc, writer);
			writer.close();
			result = baos.toString();
			System.out.println(result);
			result = "业务服务类型（数据存储访问服务）";
			String sqlStr = "select ";
			if( this.getSc().getRETURNINFO() == null || this.getSc().getRETURNINFO().size() == 0 ) {
				sqlStr += "* ";
			}
			else {
				// TODO parse return column
			}
			
			if( this.getSc().getTableName() == null || this.getSc().getTableName().length() == 0) {
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

}
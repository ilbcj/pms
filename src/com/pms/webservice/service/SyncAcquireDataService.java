package com.pms.webservice.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.pms.util.ConfigHelper;
import com.pms.util.FtpUtil;
import com.pms.webservice.model.Item;
import com.pms.webservice.model.acquiredata.AcquiredataCondition;

public class SyncAcquireDataService extends SyncService {

	private Log logger = LogFactory.getLog(SyncAcquireDataService.class);
	
	@Override
	public String GetResult() throws Exception {
		if( this.getAdc() == null 
				|| ( (getAdc().getAddDataItems() == null || getAdc().getAddDataItems().size() == 0) && 
					 (getAdc().getAllDataItems() == null || getAdc().getAllDataItems().size() == 0) ) ) {
			return "获取数据文件请求参数不正确。";
		}
		
		AcquiredataCondition files = null;
		files = sendFiles();
		
		String result = generateAcquriedataResponseResult(files);
		
		return result;
	}
	
	
	
	private AcquiredataCondition sendFiles() throws FileNotFoundException {
		AcquiredataCondition files = getAdc();
		AcquiredataCondition result = new AcquiredataCondition();
		List<Item> allDataItems = new ArrayList<Item>();
		List<Item> addDataItems = new ArrayList<Item>();
		
		String localFile = "";//"e:\\5416-010000-User-All-1445941888-00233.zip";
		String exportPath = ConfigHelper.getInstance().getSyncExportPath();
		
		String url = "15.6.22.2";
		int port = 21;
		String username = "jdzy";
		String password = "123456";
		String parentPath = "/home/tong/transfile/pms/send/";
		String path = "";
		String filename = "";//"5416-010000-User-All-1445941888-00233.zip";
		InputStream input = null;
		
//		// get all pms nodes -- this is error????
//		List<String> pmsNodes = new ArrayList<String>();
//		//pmsNodes.add("110000");
//		pmsNodes.add("330200");
		
		// change to get from pms node
		List<String> pmsNodes = new ArrayList<String>();
		String fromPms = getDci().getFROM();
		logger.info("aquire data from " + fromPms);
		pmsNodes.add(fromPms);
		
		if( files.getAllDataItems() != null && files.getAllDataItems().size() > 0 ) {
			for(Item allItem : files.getAllDataItems()) {
				try{
					localFile = exportPath + "/" + allItem.getVal();
					filename = allItem.getVal();
					input = new FileInputStream(new File(localFile));
					
					for(String pmsNode : pmsNodes) {
						path = parentPath + pmsNode;
						FtpUtil.uploadFile(url, port, username, password, path, filename, input);
					}
					allDataItems.add(allItem);
				}
				catch(Exception e) {
					String warnMsg = "[DSA]加载文件失败，文件:" + localFile + ";错误信息:" + e.getMessage() + ".";
					logger.warn(warnMsg);
//					filename = allItem.getVal();
//					input = new FileInputStream(new File(filename));
//					
//					for(String pmsNode : pmsNodes) {
//						path = parentPath + pmsNode;
//						FtpUtil.uploadFile(url, port, username, password, path, filename, input);
//					}
//					allDataItems.add(allItem);
				}
			}
		}
		result.setAllDataItems(allDataItems);
		
		if( files.getAddDataItems() != null && files.getAddDataItems().size() > 0 ) {
			for(Item addItem : files.getAddDataItems()) {
				try{
					localFile = exportPath + "/" + addItem.getVal();
					filename = addItem.getVal();
					input = new FileInputStream(new File(localFile));
					for(String pmsNode : pmsNodes) {
						path = parentPath + pmsNode;
						FtpUtil.uploadFile(url, port, username, password, path, filename, input);
					}
					addDataItems.add(addItem);
				}
				catch(Exception e) {
					String warnMsg = "[DSA]加载文件失败，文件:" + localFile + ";错误信息:" + e.getMessage() + ".";
					logger.warn(warnMsg);
				}
			}
		}
		result.setAddDataItems(addDataItems);
		
		return result;
	}

	private String generateAcquriedataResponseResult(AcquiredataCondition files) throws IOException {
		String result = null;
		Document doc = null;
		try{
			boolean isSuccess = true;
			if( files == null 
					|| ( (files.getAddDataItems() == null || files.getAddDataItems().size() == 0) && 
						 (files.getAllDataItems() == null || files.getAllDataItems().size() == 0) ) ) {
				isSuccess = false;
			}
			
			Element message = null;// dataset = null, data = null, condition = null, item = null;
			message = new Element("MESSAGE");
			doc = new Document(message);
			
			// 1- WA_COMMON_010000
			Element dataset010000 = null;
			dataset010000 = new Element("DATASET");
			message.addContent(dataset010000);
			dataset010000.setAttribute("name", "WA_COMMON_010000");
			dataset010000.setAttribute("rmk", "数据交互通用信息");
			
			Element data010000 = null;
			data010000 = new Element("DATA");
			dataset010000.addContent(data010000);
			
			Element item010000 = null;
			item010000 = new Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "H010006");
			itemSetAttribute(item010000, "val", this.getDci().getTO());
			itemSetAttribute(item010000, "rmk", "发起节点的标识");
			
			item010000 = new Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "H010007");
			itemSetAttribute(item010000, "val", this.getDci().getFROM());
			itemSetAttribute(item010000, "rmk", "目的节点的标识");
			
			item010000 = new Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "I010014");
			itemSetAttribute(item010000, "val", this.getDci().getMESSAGE_SEQUENCE());
			itemSetAttribute(item010000, "rmk", "消息流水号");
			
			item010000 = new Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "I010013");
			itemSetAttribute(item010000, "val", "254151");
			itemSetAttribute(item010000, "rmk", "消息类型（鉴权服务结果）");

			item010000 = new Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "I010010");
			itemSetAttribute(item010000, "val", this.getDci().getBUSINESS_SERVER_TYPE());
			itemSetAttribute(item010000, "rmk", "业务服务类型（鉴权服务）");
			
			// 1- WA_COMMON_010004
			Element dataset010004 = null;
			dataset010004 = new Element("DATASET");
			message.addContent(dataset010004);
			dataset010004.setAttribute("name", "WA_COMMON_010004");
			dataset010004.setAttribute("rmk", "消息返回状态信息");

			Element data010004 = null;
			data010004 = new Element("DATA");
			dataset010004.addContent(data010004);
			
			Element item010004 = null;
			item010004 = new Element("ITEM");
			data010004.addContent(item010004);
			itemSetAttribute(item010004, "key", "I030003");
			if( isSuccess ) {
				itemSetAttribute(item010004, "val", "0");
			}
			else {
				itemSetAttribute(item010004, "val", "1");
			}
			itemSetAttribute(item010004, "rmk", "消息状态");
			
			item010004 = new Element("ITEM");
			data010004.addContent(item010004);
			itemSetAttribute(item010004, "key", "I010015");
			itemSetAttribute(item010004, "val", "" + new Date().getTime()/1000);
			itemSetAttribute(item010004, "rmk", "消息返回时间");
			
			item010004 = new Element("ITEM");
			data010004.addContent(item010004);
			itemSetAttribute(item010004, "key", "I030010");
			if( isSuccess ) {
				itemSetAttribute(item010004, "val", "");
			}
			else {
				itemSetAttribute(item010004, "val", "-100001");
			}
			itemSetAttribute(item010004, "rmk", "业务消息码");
			
			item010004 = new Element("ITEM");
			data010004.addContent(item010004);
			itemSetAttribute(item010004, "key", "I010009");
			if( isSuccess ) {
				itemSetAttribute(item010004, "val", "");
			}
			else {
				itemSetAttribute(item010004, "val", "文件传输未完成");
			}
			itemSetAttribute(item010004, "rmk", "备注");
			
			// 1- WA_COMMON_010220
			Element dataset010220 = null;
			dataset010220 = new Element("DATASET");
			message.addContent(dataset010220);
			dataset010220.setAttribute("name", "WA_COMMON_010220");
			dataset010220.setAttribute("rmk", "已传送文件");

			for( int i = 0; i < files.getAllDataItems().size(); i++ ) {
				Item current = files.getAllDataItems().get(i);
				
				Element data010220 = null;
				data010220 = new Element("DATA");
				dataset010220.addContent(data010220);
				
				Element item010220 = null;
				item010220 = new Element("ITEM");
				data010220.addContent(item010220);
				itemSetAttribute(item010220, "key", current.getKey());
				itemSetAttribute(item010220, "val", current.getVal());
				itemSetAttribute(item010220, "eng", current.getEng());
			}
			
			for( int i = 0; i < files.getAddDataItems().size(); i++ ) {
				Item current = files.getAddDataItems().get(i);
				
				Element data010220 = null;
				data010220 = new Element("DATA");
				dataset010220.addContent(data010220);
				
				Element item010220 = null;
				item010220 = new Element("ITEM");
				data010220.addContent(item010220);
				itemSetAttribute(item010220, "key", current.getKey());
				itemSetAttribute(item010220, "val", current.getVal());
				itemSetAttribute(item010220, "eng", current.getEng());
			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			XMLOutputter XMLOut = new XMLOutputter(Format.getPrettyFormat().setEncoding("utf-8"));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter writer = new PrintWriter(baos);
			XMLOut.output(doc, writer);
			writer.close();
			result = baos.toString();
			System.out.println(result);
		}
		return result;
	}
}

package com.pms.webservice.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.pms.webservice.model.exchange.ExchangeCondition;
import com.pms.webservice.service.dataexchange.DataexchangeService;

public class SyncDataexchangeService extends SyncService {

	private Log logger = LogFactory.getLog(SyncDataexchangeService.class);
	
	@Override
	public String GetResult() throws Exception {
//		//1. check if sync request 
		if( this.getEcs() == null ) {
			return "数据交换请求参数不正确。";
		}
		
		String result = null;
		if( getEcs().get(0).isAsync() ) {
			DataExchangeThread det = new DataExchangeThread(this);
			Thread t1 = new Thread(det);
			t1.start();
		}
		else {
			updateData(this);
		}
		
		result = generateExchangeResponseResult();
		return result;
	}
	
	private void updateData(SyncDataexchangeService sds) throws Exception {
		//ExchangeCondition ec = sds.getEcs();
		List<ExchangeCondition> ecs = sds.getEcs();
		for( int x = 0; x < ecs.size(); x++ ) {
			ExchangeCondition ec = ecs.get(x);
			
			DataexchangeService des = DataexchangeService.getInstance( ec );
			des.ExecuteUpdate();
			
		}// end of loop
		
		return;
	}
	
	private String generateExchangeResponseResult() throws IOException {
		String result = null;
		Document doc = null;
		try{
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
			itemSetAttribute(item010000, "val", this.getDci().getFROM());
			itemSetAttribute(item010000, "rmk", "发起节点的标识");
			
			item010000 = new Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "H010007");
			itemSetAttribute(item010000, "val", this.getDci().getTO());
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
			itemSetAttribute(item010004, "val", "0");
			itemSetAttribute(item010004, "rmk", "消息状态");
			
			item010004 = new Element("ITEM");
			data010004.addContent(item010004);
			itemSetAttribute(item010004, "key", "I010015");
			itemSetAttribute(item010004, "val", "" + new Date().getTime()/1000);
			itemSetAttribute(item010004, "rmk", "消息返回时间");
			
			item010004 = new Element("ITEM");
			data010004.addContent(item010004);
			itemSetAttribute(item010004, "key", "I030010");
			itemSetAttribute(item010004, "val", "");
			itemSetAttribute(item010004, "rmk", "业务消息码");
			
			item010004 = new Element("ITEM");
			data010004.addContent(item010004);
			itemSetAttribute(item010004, "key", "I010009");
			itemSetAttribute(item010004, "val", "");
			itemSetAttribute(item010004, "rmk", "备注");
			
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
	
	private class DataExchangeThread implements Runnable{

		private SyncDataexchangeService sds;
		
		public DataExchangeThread(SyncDataexchangeService service) {
			sds = service;
		}
		
		@Override
		public void run() {
//			System.out.println(sds);
//			if(sds != null ) {
//				System.out.println(sds.getEc().getTable());
//			}
			try{
				updateData(sds);
			}
			catch(Exception e) {
				logger.info(e.getMessage());
			}
			// TODO async response
			
		}

	}

}

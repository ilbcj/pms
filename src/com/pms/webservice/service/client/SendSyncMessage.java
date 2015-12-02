package com.pms.webservice.service.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.List;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cyberpolice.auth.client.AuthService;
import org.cyberpolice.auth.client.AuthServiceStub;
import org.cyberpolice.auth.client.Execute;
import org.cyberpolice.auth.client.ExecuteE;
import org.cyberpolice.auth.client.ExecuteResponse;
import org.cyberpolice.auth.client.ExecuteResponseE;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import com.pms.webservice.model.MessageReturnStatusInfo;

public class SendSyncMessage {
	private static Log logger = LogFactory.getLog(SendSyncMessage.class); 
	private String address;
	//address = "http://15.6.22.2:7879/prjServiceEntry_dplServiceEntry/services/ServiceEntry/";//gab
	private String requestId;
	
	public SendSyncMessage(String address, String requestId) {
		this.address = address;
		this.requestId = requestId;
	}
	
	public String SendMessage(String remote, String message) throws RemoteException {
		
		AuthService as = new AuthServiceStub(address);
		ExecuteE execute0 = new ExecuteE();
		Execute param = new Execute();
		param.setXml(message);
		execute0.setExecute(param);
		
		OMElement header = createHeaderOMElement(address, requestId, remote);
		ExecuteResponseE responseE = as.execute(header, execute0);
//		ExecuteResponseE responseE = as.execute(execute0);
		
		ExecuteResponse response = responseE.getExecuteResponse();
		String result = response.getResult();
		System.out.println(result);
		return result;
	}
	
	public static boolean ParseResponse(String response) {
		boolean result = false;
		
		try{
			SAXBuilder builder=new SAXBuilder();
			InputStream inXml = new ByteArrayInputStream(response.getBytes("UTF-8")); 
			Document xmlDoc = builder.build(inXml);
			Element root = xmlDoc.getRootElement();
			
			if( "MESSAGE".equals(root.getName()) ) {
				List<Element> datasetList = root.getChildren();//getElementsByTagName("DATASET");
				for(int i=0;i<datasetList.size();i++) {
					Element element = datasetList.get(i); //得到"page"的第i+1组标签
					if("DATASET".equals(element.getName())) {
						String name = element.getAttributeValue("name"); //获得name属性
						if("WA_COMMON_010000".equals(name) ) {
							//TODO parse 数据交互通用信息
						} else if ("WA_COMMON_010004".equalsIgnoreCase(name) ) {
							MessageReturnStatusInfo mrsi = parseMessageReturnStatusInfo( element );
							if( "0".equals(mrsi.getBUSINESS_STATUS())) {
								result = true;
							}
						}
					}
				}
			}
			else {
				String warnMsg = "[XP]接收数据不正确，未找到message结点";
				logger.warn(warnMsg);
				result = false;
			}
		}
		catch(Exception e) {
			String warnMsg = "[XP]接收数据不正确，错误信息:" + e.getMessage() + ".";
			logger.warn(warnMsg);
			result = false;
		}

		return result;
	}
	
	
	private static MessageReturnStatusInfo parseMessageReturnStatusInfo(Element element) {
		MessageReturnStatusInfo result = null;
		String name = element.getChildren().get(0).getName();
		if( "DATA".equals(name) ) {
			result = new MessageReturnStatusInfo();
			List<Element> itemList = element.getChildren().get(0).getChildren();
			for(int i=0;i<itemList.size();i++) {
				Element item = itemList.get(i);
				if("ITEM".equals(item.getName())) {
					if( "I030003".equals(item.getAttributeValue("key")) ) {
						result.setBUSINESS_STATUS( item.getAttributeValue("val") );
					} else if ( "I010015".equals(item.getAttributeValue("key"))  ) {
						result.setBUSINESS_TIME( item.getAttributeValue( "val") );
					} else if ( "I030010".equals(item.getAttributeValue("key"))  ) {
						result.setBUSINESS_ERRCODE( item.getAttributeValue( "val") );
					} else if ( "I010009".equals(item.getAttributeValue("key"))  ) {
						result.setMEMO( item.getAttributeValue( "val") );
					}
				}
			}
		}
		return result;
	}	
	
	/** 
	 *  构建同步服务请求的Header 
	 *  
	 *  通过请求ID和服务ID，构建调用Esb服务的请求头，使最终发送的请求头格式为
	 *  <soapenv:Header>
	 *  	<CyberpoliceSBReqHeader xmlns="http://15.9.65.1:7879/prjServiceEntry_dplServiceEntry/services/ServiceEntry/">
	 *  		<rid>Q11000000000000000</rid>
	 *  		<sid>S01000011000000009</sid>
	 *  		<taskid></taskid>
	 *  		<timeout>300000</timeout>
	 *  		<retrynum>10</retrynum>
	 *  	</CyberpoliceSBReqHeader>
	 *  </soapenv:Header>
	 *  
	 *  @return 构建成功的请求头
	*/  
	private OMElement createHeaderOMElement(String nameSpaceAddress, String requestId, String serviceId){   
		OMFactory factory = OMAbstractFactory.getOMFactory();   
	  	OMNamespace SecurityElementNamespace = factory.createOMNamespace(nameSpaceAddress,"");   
        OMElement headerOM = factory.createOMElement("CyberpoliceSBReqHeader",   
                SecurityElementNamespace);   
        OMElement rid = factory.createOMElement("rid", SecurityElementNamespace);
        //rid.setText("Q11000000000000000");
        rid.setText(requestId);
        headerOM.addChild(rid);
        
        OMElement sid = factory.createOMElement("sid", SecurityElementNamespace);
        //sid.setText("S01000011000000009");
        sid.setText(serviceId);
        headerOM.addChild(sid);
        
        OMElement taskid = factory.createOMElement("taskid", SecurityElementNamespace);
        taskid.setText("");
        headerOM.addChild(taskid);
        
        OMElement timeout = factory.createOMElement("timeout", SecurityElementNamespace);
        timeout.setText("300000");
        headerOM.addChild(timeout);
        
        OMElement retrynum = factory.createOMElement("retrynum", SecurityElementNamespace);
        retrynum.setText("10");
        headerOM.addChild(retrynum);
        return headerOM;
	 }   
}

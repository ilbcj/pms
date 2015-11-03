package com.pms.webservice.service.client;

import java.rmi.RemoteException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.cyberpolice.auth.client.AuthService;
import org.cyberpolice.auth.client.AuthServiceStub;
import org.cyberpolice.auth.client.Execute;
import org.cyberpolice.auth.client.ExecuteE;
import org.cyberpolice.auth.client.ExecuteResponse;
import org.cyberpolice.auth.client.ExecuteResponseE;

public class SendSyncMessage {
	
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


/**
 * AuthServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
    package org.cyberpolice.auth;

import com.pms.webservice.WSExecuteController;
    /**
     *  AuthServiceSkeleton java skeleton for the axisService
     */
    public class AuthServiceSkeleton{
        
         
        /**
         * Auto generated method signature
         * 
                                     * @param execute 
             * @return executeResponse 
         */
        
                 public org.cyberpolice.auth.ExecuteResponseE execute
                  (
                  org.cyberpolice.auth.ExecuteE execute
                  )
            {
                
                	 WSExecuteController controller = new WSExecuteController();
                	 String xmldata = execute.localExecute.localXml;
                	 int start = xmldata.indexOf("<!--");
                	 int end = xmldata.indexOf("-->");
                	 String temp = "";
                	 if( -1 != start && -1 != end ) {
                		 temp = xmldata.substring(start, end+3);
                	 }
                	 xmldata = xmldata.replace(temp, "");
                	 String result = controller.process(xmldata);
                	 org.cyberpolice.auth.ExecuteResponseE response = new org.cyberpolice.auth.ExecuteResponseE();
                	 org.cyberpolice.auth.ExecuteResponse executeResponse = new org.cyberpolice.auth.ExecuteResponse();
                	 executeResponse.setResult(result);
                	 response.setExecuteResponse(executeResponse);
                	 return response;
        }
    }
    
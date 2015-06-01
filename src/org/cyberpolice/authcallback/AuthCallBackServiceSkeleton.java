
/**
 * AuthCallBackServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
    package org.cyberpolice.authcallback;

import com.pms.webservice.WSCommitController;
    /**
     *  AuthCallBackServiceSkeleton java skeleton for the axisService
     */
    public class AuthCallBackServiceSkeleton{
        
         
        /**
         * Auto generated method signature
         * 
                                     * @param commit 
             * @return commitResponse 
         */
        
                 public org.cyberpolice.authcallback.CommitResponseE commit
                  (
                  org.cyberpolice.authcallback.CommitE commit
                  )
            {
                	 WSCommitController controller = new WSCommitController();
                	 String result = controller.process(commit.localCommit.localXml);
                	 
                	 org.cyberpolice.authcallback.CommitResponseE response = new org.cyberpolice.authcallback.CommitResponseE();
                	 org.cyberpolice.authcallback.CommitResponse localCommitResponse = new org.cyberpolice.authcallback.CommitResponse();
                	 localCommitResponse.setResult(result);
                	 response.setCommitResponse(localCommitResponse);
                	 return response;
        }
     
    }
    
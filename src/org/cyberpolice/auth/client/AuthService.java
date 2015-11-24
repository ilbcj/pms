

/**
 * AuthService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package org.cyberpolice.auth.client;

import java.rmi.RemoteException;

import org.apache.axiom.om.OMElement;

    /*
     *  AuthService java interface
     */

    public interface AuthService {
          

        /**
          * Auto generated method signature
          * 
                    * @param execute0
                
         */

         
                     public org.cyberpolice.auth.client.ExecuteResponseE execute(

                        org.cyberpolice.auth.client.ExecuteE execute0)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param execute0
            
          */
        public void startexecute(

            org.cyberpolice.auth.client.ExecuteE execute0,

            final org.cyberpolice.auth.client.AuthServiceCallbackHandler callback)

            throws java.rmi.RemoteException;


		public ExecuteResponseE execute(OMElement header, ExecuteE execute0) throws RemoteException;

     

        
       //
       }
    
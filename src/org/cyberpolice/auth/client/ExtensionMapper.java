
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:34:40 IST)
 */

        
            package org.cyberpolice.auth.client;
        
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://cyberpolice.org/auth".equals(namespaceURI) &&
                  "execute".equals(typeName)){
                   
                            return  org.cyberpolice.auth.client.Execute.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://cyberpolice.org/auth".equals(namespaceURI) &&
                  "executeResponse".equals(typeName)){
                   
                            return  org.cyberpolice.auth.client.ExecuteResponse.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    
package com.pms.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.pms.dao.impl.OrganizationDAOImpl;
import com.pms.dao.impl.ResourceDAOImpl;
import com.pms.dao.impl.SystemConfigDAOImpl;
import com.pms.dao.impl.UserDAOImpl;
import com.pms.model.Organization;
import com.pms.model.ResData;
import com.pms.model.ResRole;
import com.pms.model.ResRoleResource;
import com.pms.model.SystemConfig;
import com.pms.model.User;
import com.pms.util.ConfigHelper;
import com.pms.util.MD5Security;
import com.pms.util.ZipUtil;
import com.pms.webservice.service.client.SendSyncMessage;

public class DataSyncService {
	public String DownLoadRes(String amount, List<ResData> items) throws Exception {
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
        Date date = timeFormat.parse("1970-01-01 00:00:00");
        long second = (System.currentTimeMillis() - date.getTime())/1000;
        String rootPath = System.getProperty("java.io.tmpdir")+"/" + second + "/";
        File DirFile = new File(rootPath);		
		if(!DirFile.exists()){
    	    DirFile.mkdir();
        }
    	  
		ResourceDAOImpl dao=new ResourceDAOImpl();
		List<ResData> res = null;
		if( amount == "All" || amount.equals("All") ){
			res = dao.GetAllDatas();
		}else{
			SystemConfigDAOImpl scdao = new SystemConfigDAOImpl();
			List<SystemConfig> SystemConfigList = scdao.GetConfigByType(SystemConfig.SYSTEMCONFIGTYPESYNC);
			
			String LatestModTime = getSysConfigByItem(SystemConfig.SYSTEMCONFIG_ITEM_DATARES, SystemConfigList);
	        res = dao.GetDatasByTime( LatestModTime );
		}
		
		items.addAll(res);
		if( items.size() == 0 )
			return null;
		
		int num = 0;
		int n = 5000;
        int count = res.size()/n;
        if(res.size() % n != 0){
        	count = res.size()/n + 1;
        }
        
        for (int j = 1; j <= count; j++) {
            String str = "resource_type" + "\t"  + 
	    		"RESOURCE_ID" + "\t" + "RESOURCE_STATUS" + "\t" + "RESOURCE_DESCRIBE" + "\t"  + 
				"DATASET_SENSITIVE_LEVEL" + "\t" + "DATA_SET" + "\t" + "SECTION_CLASS" + "\t"  + 
				"ELEMENT" + "\t" + "SECTION_RELATIOIN_CLASS" + "\t" + "OPERATE_SYMBOL" + "\t"  + 
				"ELEMENT_VALUE" + "\t" + "DELETE_STATUS" + "\t" + "DATA_VERSION" + "\t"  + 
				"LATEST_MOD_TIME" + "\t" + "RMK" + "\n";
            for (int i = num; i < res.size(); i++)  {
            	str = str + res.get(i).getResource_type() + "\t"
        			 + nullConvertEmptyStr( res.get(i).getRESOURCE_ID() ) + "\t" 
        			 + res.get(i).getRESOURCE_STATUS() + "\t" 
        			 + nullConvertEmptyStr( res.get(i).getRESOURCE_DESCRIBE() ) + "\t"
            		 + nullConvertEmptyStr( res.get(i).getDATASET_SENSITIVE_LEVEL() ) + "\t" 
        			 + nullConvertEmptyStr( res.get(i).getDATA_SET() ) + "\t" 
            		 + nullConvertEmptyStr( res.get(i).getSECTION_CLASS() ) + "\t"
            		 + nullConvertEmptyStr( res.get(i).getELEMENT() ) + "\t" 
            		 + nullConvertEmptyStr( res.get(i).getSECTION_RELATIOIN_CLASS() ) + "\t" 
            		 + nullConvertEmptyStr( res.get(i).getOPERATE_SYMBOL() ) + "\t"
            		 + nullConvertEmptyStr( res.get(i).getELEMENT_VALUE() ) + "\t" 
            		 + res.get(i).getDELETE_STATUS() + "\t" 
            		 + res.get(i).getDATA_VERSION() + "\t"
            		 + getLongTime(res.get(i).getLATEST_MOD_TIME()) + "\t" 
            		 + nullConvertEmptyStr( res.get(i).getRMK() ) + "\n";
            	num++;
        		if(num >= n*j){
                	break;
                }
            }
            String filename = "wa_authority_data_resource-" + j + ".bcp";
            File file = new File(rootPath + filename);  
            PrintWriter pw = new PrintWriter( new OutputStreamWriter( new FileOutputStream( file ), "utf-8" ) );
            pw.write(str);  
            pw.close();
		}

        Document domIndex = DocumentHelper.createDocument();//创建IndexXml文件
        Element root = domIndex.addElement("MESSAGE");//添加根元素
        Element childNode = root.addElement("DATASET").addAttribute("name", "WA_COMMON_010017").addAttribute("ver", "1.0").addAttribute("rmk", "数据文件索引信息");
        Element childNodes = childNode.addElement("DATA");
        
        Element childNodes_FileFormat = childNodes.addElement("DATASET").addAttribute("name", "WA_COMMON_010013").addAttribute("rmk", "BCP文件描述信息");
        Element FileFormats = childNodes_FileFormat.addElement("DATA");
        FileFormats.addElement("ITEM").addAttribute("key", "I010032").addAttribute("val", "").addAttribute("rmk", "列分隔符（缺少值时默认为制表符\t）");
        FileFormats.addElement("ITEM").addAttribute("key", "I010033").addAttribute("val", "").addAttribute("rmk", "行分隔符（缺少值时默认为换行符\n）");
        FileFormats.addElement("ITEM").addAttribute("key", "A010004").addAttribute("val", "WA_AUTHORITY_DATA_RESOURCE").addAttribute("rmk", "数据集代码");
        FileFormats.addElement("ITEM").addAttribute("key", "F010008").addAttribute("val", "300200").addAttribute("rmk", "数据采集地");
        FileFormats.addElement("ITEM").addAttribute("key", "I010038").addAttribute("val", "2").addAttribute("rmk", "数据起始行，可选项，不填写默认为第１行");
        FileFormats.addElement("ITEM").addAttribute("key", "I010039").addAttribute("val", "UTF-8").addAttribute("rmk", "可选项，默认为UTF-８，BCP文件编码格式（采用不带格式的编码方式，如：UTF-８无BOM）");

        Element childNodes_DataFile = FileFormats.addElement("DATASET").addAttribute("name", "WA_COMMON_010014").addAttribute("rmk", "BCP数据文件信息");
        for (int j = 1; j <= count; j++) {

        	int Record_number = 0;
        	if(res.size() - n * (j - 1) < n){
        		Record_number = res.size() - n * (j - 1);
            }else{
            	Record_number = n;
            }
        	Element DataFiles = childNodes_DataFile.addElement("DATA");
        	DataFiles.addElement("ITEM").addAttribute("key", "H040003").addAttribute("val", "").addAttribute("rmk", "文件路径");
        	DataFiles.addElement("ITEM").addAttribute("key", "H010020").addAttribute("val", "wa_authority_data_resource-" + j+".bcp").addAttribute("rmk", "文件名");
        	DataFiles.addElement("ITEM").addAttribute("key", "I010034").addAttribute("val", String.valueOf( Record_number ) ).addAttribute("rmk", "记录行数");
        }   
        
        Element childNodes_FileStructure = FileFormats.addElement("DATASET").addAttribute("name", "WA_COMMON_010015").addAttribute("rmk", "BCP文件数据结构");
        Element FileStructures = childNodes_FileStructure.addElement("DATA");
//        FileStructures.addElement("ITEM").addAttribute("key", "").addAttribute("eng", "id").addAttribute("val", "").addAttribute("chn", "");
//        FileStructures.addElement("ITEM").addAttribute("key", "").addAttribute("eng", "name").addAttribute("val", "").addAttribute("chn", "");
        
        FileStructures.addElement("ITEM").addAttribute("key", "J030029").addAttribute("eng", "resource_type").addAttribute("val", "").addAttribute("chn", "资源类型");
        FileStructures.addElement("ITEM").addAttribute("key", "J030006").addAttribute("eng", "RESOURCE_ID").addAttribute("val", "").addAttribute("chn", "资源唯一标识");
        FileStructures.addElement("ITEM").addAttribute("key", "J030010").addAttribute("eng", "RESOURCE_STATUS").addAttribute("val", "").addAttribute("chn", "资源状态");
        FileStructures.addElement("ITEM").addAttribute("key", "J030012").addAttribute("eng", "RESOURCE_DESCRIBE").addAttribute("val", "").addAttribute("chn", "资源描述");
        FileStructures.addElement("ITEM").addAttribute("key", "J030003").addAttribute("eng", "DATASET_SENSITIVE_LEVEL").addAttribute("val", "").addAttribute("chn", "数据集敏感度编码（表控）");
        FileStructures.addElement("ITEM").addAttribute("key", "A010004").addAttribute("eng", "DATA_SET").addAttribute("val", "").addAttribute("chn", "数据集编码（表控）");
        FileStructures.addElement("ITEM").addAttribute("key", "J030001").addAttribute("eng", "SECTION_CLASS").addAttribute("val", "").addAttribute("chn", "字段分类编码（列控）");
        FileStructures.addElement("ITEM").addAttribute("key", "J030004").addAttribute("eng", "ELEMENT").addAttribute("val", "").addAttribute("chn", "字段分类编码（列控）");
        FileStructures.addElement("ITEM").addAttribute("key", "J030002").addAttribute("eng", "SECTION_RELATIOIN_CLASS").addAttribute("val", "").addAttribute("chn", "字段分类关系编码（列控）");
        FileStructures.addElement("ITEM").addAttribute("key", "J030021").addAttribute("eng", "OPERATE_SYMBOL").addAttribute("val", "").addAttribute("chn", "操作符");
        FileStructures.addElement("ITEM").addAttribute("key", "J030005").addAttribute("eng", "ELEMENT_VALUE").addAttribute("val", "").addAttribute("chn", "字段值");
        FileStructures.addElement("ITEM").addAttribute("key", "H010029").addAttribute("eng", "DELETE_STATUS").addAttribute("val", "").addAttribute("chn", "删除状态");
        FileStructures.addElement("ITEM").addAttribute("key", "J030017").addAttribute("eng", "DATA_VERSION").addAttribute("val", "").addAttribute("chn", "数据版本号");
        FileStructures.addElement("ITEM").addAttribute("key", "I010005").addAttribute("eng", "LATEST_MOD_TIME").addAttribute("val", "").addAttribute("chn", "最新修改时间");
        FileStructures.addElement("ITEM").addAttribute("key", "J030013").addAttribute("eng", "RMK").addAttribute("val", "").addAttribute("chn", "备注");	

            
        String xmlIndex = domIndex.asXML();
        String name = "DataRes";
        
		return CreateIndexXmlAndZip(xmlIndex, rootPath, name, amount);
	}
	
	public String DownLoadOrg(String amount, List<Organization> items) throws Exception {
       SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
       Date date = timeFormat.parse("1970-01-01 00:00:00");
       long second = (System.currentTimeMillis() - date.getTime())/1000;
       String rootPath = System.getProperty("java.io.tmpdir")+"/" + second + "/";
       File DirFile = new File(rootPath);		
       if(!DirFile.exists()){
    	   DirFile.mkdir();
       }
       
		OrganizationDAOImpl dao = new OrganizationDAOImpl();
		List<Organization> org = null;
		if( amount == "All" || amount.equals("All") ){
			org = dao.GetAllOrgs();
		}else{
			SystemConfigDAOImpl scdao = new SystemConfigDAOImpl();
			List<SystemConfig> SystemConfigList = scdao.GetConfigByType(SystemConfig.SYSTEMCONFIGTYPESYNC);
			
	        String LatestModTime = getSysConfigByItem(SystemConfig.SYSTEMCONFIG_ITEM_ORG, SystemConfigList);
			org = dao.GetOrgsByTime( LatestModTime );
		}	
		
		items.addAll(org);
		if( items.size() == 0 )
			return null;
		
		int num = 0;
		int n = 5000;
        int count = org.size()/n;
        if(org.size()%n != 0){
        	count = org.size()/n + 1;
        }
        
        for (int j = 1; j <= count; j++) {
            String str = "GA_DEPARTMENT" + "\t" + "UNIT" + "\t" + "ORG_LEVEL" + "\t"  + 
	    		"PARENT_ORG" + "\t" + "DELETE_STATUS" + "\t" + "DATA_VERSION" + "\t"  + 
				"LATEST_MOD_TIME" + "\n";
            for (int i = num; i < org.size(); i++)  {
            	str = str + nullConvertEmptyStr( org.get(i).getGA_DEPARTMENT() ) + "\t" 
            			+ nullConvertEmptyStr( org.get(i).getUNIT() ) + "\t" 
            			+ ConvertOrgLevel( nullConvertEmptyStr( org.get(i).getORG_LEVEL() ) ) + "\t"
            			+ nullConvertEmptyStr( org.get(i).getPARENT_ORG() ) + "\t" 
            			+ org.get(i).getDELETE_STATUS() + "\t" 
            			+ org.get(i).getDATA_VERSION() + "\t"
            			+ getLongTime(org.get(i).getLATEST_MOD_TIME()) + "\n";
            	num++;
        		if(num >= n*j){
                	break;
                }
            }
            String filename = "wa_authority_orgnization-" + j + ".bcp";
            File file = new File(rootPath + filename); 
            PrintWriter pw = new PrintWriter( new OutputStreamWriter( new FileOutputStream( file ), "utf-8" ) );
            pw.write(str);  
            pw.close();
		}

        Document domIndex = DocumentHelper.createDocument();//创建IndexXml文件
        Element root = domIndex.addElement("MESSAGE");//添加根元素
        Element childNode = root.addElement("DATASET").addAttribute("name", "WA_COMMON_010017").addAttribute("ver", "1.0").addAttribute("rmk", "数据文件索引信息");
        Element childNodes = childNode.addElement("DATA");
        
        Element childNodes_FileFormat = childNodes.addElement("DATASET").addAttribute("name", "WA_COMMON_010013").addAttribute("rmk", "BCP文件描述信息");
        Element FileFormats = childNodes_FileFormat.addElement("DATA");
        FileFormats.addElement("ITEM").addAttribute("key", "I010032").addAttribute("val", "").addAttribute("rmk", "列分隔符（缺少值时默认为制表符\t）");
        FileFormats.addElement("ITEM").addAttribute("key", "I010033").addAttribute("val", "").addAttribute("rmk", "行分隔符（缺少值时默认为换行符\n）");
        FileFormats.addElement("ITEM").addAttribute("key", "A010004").addAttribute("val", "WA_AUTHORITY_ORGNIZATION").addAttribute("rmk", "数据集代码");
        FileFormats.addElement("ITEM").addAttribute("key", "F010008").addAttribute("val", "300200").addAttribute("rmk", "数据采集地");
        FileFormats.addElement("ITEM").addAttribute("key", "I010038").addAttribute("val", "2").addAttribute("rmk", "数据起始行，可选项，不填写默认为第１行");
        FileFormats.addElement("ITEM").addAttribute("key", "I010039").addAttribute("val", "UTF-8").addAttribute("rmk", "可选项，默认为UTF-８，BCP文件编码格式（采用不带格式的编码方式，如：UTF-８无BOM）");

        Element childNodes_DataFile = FileFormats.addElement("DATASET").addAttribute("name", "WA_COMMON_010014").addAttribute("rmk", "BCP数据文件信息");
        for (int j = 1; j <= count; j++) {
        	
        	int Record_number = 0;
        	if(org.size() - n * (j - 1) < n){
        		Record_number = org.size() - n * (j - 1);
            }else{
            	Record_number = n;
            }
        	Element DataFiles = childNodes_DataFile.addElement("DATA");
        	DataFiles.addElement("ITEM").addAttribute("key", "H040003").addAttribute("val", "").addAttribute("rmk", "文件路径");
        	DataFiles.addElement("ITEM").addAttribute("key", "H010020").addAttribute("val", "wa_authority_orgnization-" + j+".bcp").addAttribute("rmk", "文件名");
        	DataFiles.addElement("ITEM").addAttribute("key", "I010034").addAttribute("val", String.valueOf( Record_number ) ).addAttribute("rmk", "记录行数");
        }   

        Element childNodes_FileStructure = FileFormats.addElement("DATASET").addAttribute("name", "WA_COMMON_010015").addAttribute("rmk", "BCP文件数据结构");
        Element FileStructures = childNodes_FileStructure.addElement("DATA");
        FileStructures.addElement("ITEM").addAttribute("key", "A010001").addAttribute("eng", "GA_DEPARTMENT").addAttribute("chn", "公安组织机构代码");
        FileStructures.addElement("ITEM").addAttribute("key", "E010002").addAttribute("eng", "UNIT").addAttribute("chn", "公安组织机构名称");
        FileStructures.addElement("ITEM").addAttribute("key", "I010052").addAttribute("eng", "ORG_LEVEL").addAttribute("chn", "公安组织机构级别");
        FileStructures.addElement("ITEM").addAttribute("key", "A010005").addAttribute("eng", "PARENT_ORG").addAttribute("chn", "组织机构父节点(上级组织机构)");
        FileStructures.addElement("ITEM").addAttribute("key", "H010029").addAttribute("eng", "DELETE_STATUS").addAttribute("chn", "删除状态");
        FileStructures.addElement("ITEM").addAttribute("key", "J030017").addAttribute("eng", "DATA_VERSION").addAttribute("chn", "数据版本号");
        FileStructures.addElement("ITEM").addAttribute("key", "I010005").addAttribute("eng", "LATEST_MOD_TIME").addAttribute("chn", "最新修改时间");
        
        String xmlIndex = domIndex.asXML();
            
        String name = "Org";
        
        return CreateIndexXmlAndZip(xmlIndex, rootPath, name, amount);
	}
	private String ConvertOrgLevel(String level){
		String res=null;
		if("部".endsWith(level)){
			res="1";
		}else if("省".endsWith(level)){
			res="2";
		}else if("市".endsWith(level)){
			res="3";
		}else if("县".endsWith(level)){
			res="4";
		}else if("基层所队".endsWith(level)){
			res="9";
		}
		return res;
				
	}
	public String DownLoadUser(String amount, List<User> items) throws Exception {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
        Date date = timeFormat.parse("1970-01-01 00:00:00");
        long second = (System.currentTimeMillis() - date.getTime())/1000;
        String rootPath = System.getProperty("java.io.tmpdir")+"/" + second + "/";
        File DirFile = new File(rootPath);		
        if(!DirFile.exists()){
     	   DirFile.mkdir();
        }	
        
		UserDAOImpl dao = new UserDAOImpl();
		List<User> user = null;
		if( amount == "All" || amount.equals("All") ){
			user = dao.GetAllUsers();
		}else{
			SystemConfigDAOImpl scdao = new SystemConfigDAOImpl();
			List<SystemConfig> SystemConfigList = scdao.GetConfigByType(SystemConfig.SYSTEMCONFIGTYPESYNC);
			
	        String LatestModTime = getSysConfigByItem(SystemConfig.SYSTEMCONFIG_ITEM_USER, SystemConfigList);
	        user = dao.GetUsersByTime( LatestModTime );
		}
		
		items.addAll(user);
		if( items.size() == 0 )
			return null;
		
		int num = 0;
		int n = 5000;
        int count = user.size()/n;
        if(user.size()%n != 0){
        	count = user.size()/n + 1;
        }

        for (int j = 1; j <= count; j++) {
            String str = "NAME" + "\t" + "CERTIFICATE_CODE_MD5" + "\t"
            	 + "CERTIFICATE_CODE_SUFFIX" + "\t" + "SEXCODE" + "\t" + "GA_DEPARTMENT" + "\t"
            	 + "UNIT" + "\t"+ "ORG_LEVEL" + "\t" + "POLICE_SORT" + "\t"
            	 + "POLICE_NO" + "\t" + "SENSITIVE_LEVEL" + "\t" + "BUSINESS_TYPE" + "\t"
            	 + "TAKE_OFFICE" + "\t" + "USER_STATUS" + "\t" 
            	 + "DELETE_STATUS" + "\t" + "DATA_VERSION" + "\t"
            	 + "LATEST_MOD_TIME" + "\t"  + "\n";
            for (int i = num; i <user.size(); i++)  {
            	str = str + nullConvertEmptyStr( user.get(i).getNAME() ) + "\t" 
            			+ nullConvertEmptyStr( user.get(i).getCERTIFICATE_CODE_MD5() ) + "\t"
	            		+ nullConvertEmptyStr( user.get(i).getCERTIFICATE_CODE_SUFFIX() ) + "\t" 
	            		+ nullConvertEmptyStr( user.get(i).getSEXCODE() ) + "\t" 
	            		+ nullConvertEmptyStr( user.get(i).getGA_DEPARTMENT() ) + "\t"
	            		+ nullConvertEmptyStr( user.get(i).getUNIT() ) + "\t" 
	            		+ ConvertOrgLevel( nullConvertEmptyStr( user.get(i).getORG_LEVEL() ) ) + "\t" 
	            		+ nullConvertEmptyStr( user.get(i).getPOLICE_SORT() ) + "\t"
	            		+ nullConvertEmptyStr( user.get(i).getPOLICE_NO() ) + "\t" 
	            		+ nullConvertEmptyStr( user.get(i).getSENSITIVE_LEVEL() ) + "\t" 
	            		+ nullConvertEmptyStr( user.get(i).getBUSINESS_TYPE() ) + "\t"
	            		+ nullConvertEmptyStr( user.get(i).getTAKE_OFFICE() ) + "\t" 
	            		+ user.get(i).getUSER_STATUS() + "\t" 
	            		+ user.get(i).getDELETE_STATUS() + "\t" 
	            		+ user.get(i).getDATA_VERSION() + "\t"
	            		+ getLongTime(user.get(i).getLATEST_MOD_TIME()) + "\n";
            	num++;
        		if(num >= n*j){
                	break;
                }
            }
            String filename = "wa_authority_police-" + j + ".bcp";
            File file = new File(rootPath + filename);
            PrintWriter pw = new PrintWriter( new OutputStreamWriter( new FileOutputStream( file ), "utf-8" ) );
            pw.write(str);  
            pw.close();
		}
        
        Document domIndex = DocumentHelper.createDocument();//创建IndexXml文件
        Element root = domIndex.addElement("MESSAGE");//添加根元素
        Element childNode = root.addElement("DATASET").addAttribute("name", "WA_COMMON_010017").addAttribute("ver", "1.0").addAttribute("rmk", "数据文件索引信息");
        Element childNodes = childNode.addElement("DATA");
        
        Element childNodes_FileFormat = childNodes.addElement("DATASET").addAttribute("name", "WA_COMMON_010013").addAttribute("rmk", "BCP文件描述信息");
        Element FileFormats = childNodes_FileFormat.addElement("DATA");
        FileFormats.addElement("ITEM").addAttribute("key", "I010032").addAttribute("val", "").addAttribute("rmk", "列分隔符（缺少值时默认为制表符\t）");
        FileFormats.addElement("ITEM").addAttribute("key", "I010033").addAttribute("val", "").addAttribute("rmk", "行分隔符（缺少值时默认为换行符\n）");
        FileFormats.addElement("ITEM").addAttribute("key", "A010004").addAttribute("val", "WA_AUTHORITY_POLICE").addAttribute("rmk", "数据集代码");
        FileFormats.addElement("ITEM").addAttribute("key", "F010008").addAttribute("val", "300200").addAttribute("rmk", "数据采集地");
        FileFormats.addElement("ITEM").addAttribute("key", "I010038").addAttribute("val", "2").addAttribute("rmk", "数据起始行，可选项，不填写默认为第１行");
        FileFormats.addElement("ITEM").addAttribute("key", "I010039").addAttribute("val", "UTF-8").addAttribute("rmk", "可选项，默认为UTF-８，BCP文件编码格式（采用不带格式的编码方式，如：UTF-８无BOM）"); 
        
        Element childNodes_DataFile = FileFormats.addElement("DATASET").addAttribute("name", "WA_COMMON_010014").addAttribute("rmk", "BCP数据文件信息");
        for (int j = 1; j <= count; j++) {
        	
        	int Record_number = 0;
        	if(user.size() - n * (j - 1) < n){
        		Record_number = user.size() - n * (j - 1);
            }else{
            	Record_number = n;
            }
        	Element DataFiles = childNodes_DataFile.addElement("DATA");
        	DataFiles.addElement("ITEM").addAttribute("key", "H040003").addAttribute("val", "").addAttribute("rmk", "文件路径");
        	DataFiles.addElement("ITEM").addAttribute("key", "H010020").addAttribute("val", "wa_authority_police-" + j+".bcp").addAttribute("rmk", "文件名");
        	DataFiles.addElement("ITEM").addAttribute("key", "I010034").addAttribute("val", String.valueOf( Record_number ) ).addAttribute("rmk", "记录行数");
        }   

        Element childNodes_FileStructure = FileFormats.addElement("DATASET").addAttribute("name", "WA_COMMON_010015").addAttribute("rmk", "BCP文件数据结构");
        Element FileStructures = childNodes_FileStructure.addElement("DATA");
//        FileStructures.addElement("ITEM").addAttribute("key", "").addAttribute("eng", "id").addAttribute("val", "").addAttribute("chn", "");
        
        FileStructures.addElement("ITEM").addAttribute("key", "B010001").addAttribute("eng", "name").addAttribute("val", "").addAttribute("chn", "姓名");
        FileStructures.addElement("ITEM").addAttribute("key", "J030014").addAttribute("eng", "CERTIFICATE_CODE_MD5").addAttribute("val", "").addAttribute("chn", "身份证哈希值");
        FileStructures.addElement("ITEM").addAttribute("key", "J030015").addAttribute("eng", "CERTIFICATE_CODE_SUFFIX").addAttribute("val", "").addAttribute("chn", "身份证后6位");
        FileStructures.addElement("ITEM").addAttribute("key", "B010011").addAttribute("eng", "SEXCODE").addAttribute("val", "").addAttribute("chn", "性别");
        FileStructures.addElement("ITEM").addAttribute("key", "A010001").addAttribute("eng", "GA_DEPARTMENT").addAttribute("val", "").addAttribute("chn", "公安组织机构代码");
        FileStructures.addElement("ITEM").addAttribute("key", "E010002").addAttribute("eng", "UNIT").addAttribute("val", "").addAttribute("chn", "公安组织机构名称");
        FileStructures.addElement("ITEM").addAttribute("key", "I010052").addAttribute("eng", "ORG_LEVEL").addAttribute("val", "").addAttribute("chn", "公安组织机构级别");
        FileStructures.addElement("ITEM").addAttribute("key", "A010003").addAttribute("eng", "POLICE_SORT").addAttribute("val", "").addAttribute("chn", "警种");
        FileStructures.addElement("ITEM").addAttribute("key", "B010028").addAttribute("eng", "POLICE_NO").addAttribute("val", "").addAttribute("chn", "警号");
        FileStructures.addElement("ITEM").addAttribute("key", "H010034").addAttribute("eng", "SENSITIVE_LEVEL").addAttribute("val", "").addAttribute("chn", "最高敏感级别");
        FileStructures.addElement("ITEM").addAttribute("key", "I010031").addAttribute("eng", "BUSINESS_TYPE").addAttribute("val", "").addAttribute("chn", "业务部门类别");
        FileStructures.addElement("ITEM").addAttribute("key", "B030026").addAttribute("eng", "TAKE_OFFICE").addAttribute("val", "").addAttribute("chn", "职务/职称");
        FileStructures.addElement("ITEM").addAttribute("key", "J030016").addAttribute("eng", "USER_STATUS").addAttribute("val", "").addAttribute("chn", "用户状态");
        
//        FileStructures.addElement("ITEM").addAttribute("key", "").addAttribute("eng", "position").addAttribute("val", "").addAttribute("chn", "");
//        FileStructures.addElement("ITEM").addAttribute("key", "").addAttribute("eng", "dept").addAttribute("val", "").addAttribute("chn", "");
        
        FileStructures.addElement("ITEM").addAttribute("key", "H010029").addAttribute("eng", "DELETE_STATUS").addAttribute("val", "").addAttribute("chn", "删除状态");
        FileStructures.addElement("ITEM").addAttribute("key", "J030017").addAttribute("eng", "DATA_VERSION").addAttribute("val", "").addAttribute("chn", "数据版本号");	
        FileStructures.addElement("ITEM").addAttribute("key", "I010005").addAttribute("eng", "LATEST_MOD_TIME").addAttribute("val", "").addAttribute("chn", "最新修改时间");
        
        String xmlIndex = domIndex.asXML();
        String name = "User";
        
        return CreateIndexXmlAndZip(xmlIndex, rootPath, name, amount);
	}
	
	public String DownLoadResRole(String amount, List<ResRoleResource> items) throws Exception {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
        Date date = timeFormat.parse("1970-01-01 00:00:00");
        long second = (System.currentTimeMillis() - date.getTime())/1000;
        String rootPath = System.getProperty("java.io.tmpdir")+"/" + second + "/";
        File DirFile = new File(rootPath);		
        if(!DirFile.exists()){
     	   DirFile.mkdir();
        }	
        
        ResourceDAOImpl dao = new ResourceDAOImpl();
		List<ResRoleResource> resRole = null;
		if( amount == "All" || amount.equals("All") ){
			resRole = dao.GetAllResRoles();
		}else{
			SystemConfigDAOImpl scdao = new SystemConfigDAOImpl();
			List<SystemConfig> SystemConfigList = scdao.GetConfigByType(SystemConfig.SYSTEMCONFIGTYPESYNC);
			
	        String LatestModTime = getSysConfigByItem(SystemConfig.SYSTEMCONFIG_ITEM_RESINROLE, SystemConfigList);
	        resRole = dao.GetResRolesByTime( LatestModTime );
		}
		
		items.addAll(resRole);
		if( items.size() == 0 )
			return null;
		
		int num = 0;
		int n = 5000;
        int count = resRole.size()/n;
        if(resRole.size()%n != 0){
        	count = resRole.size()/n + 1;
        }
        
        for (int j = 1; j <= count; j++) {
            String str = "RESOURCE_ID" + "\t" + "BUSINESS_ROLE" + "\t"
            	 + "DELETE_STATUS" + "\t" + "DATA_VERSION" + "\t"
            	 + "LATEST_MOD_TIME"  + "\n";
            for (int i = num; i <resRole.size(); i++)  {
            	str = str + nullConvertEmptyStr( resRole.get(i).getRESOURCE_ID() ) + "\t" 
            			+ nullConvertEmptyStr( resRole.get(i).getBUSINESS_ROLE() ) + "\t"
            			+ resRole.get(i).getDELETE_STATUS() + "\t" 
            			+ resRole.get(i).getDATA_VERSION() + "\t" 
            			+ getLongTime(resRole.get(i).getLATEST_MOD_TIME()) + "\n";
            	num++;
        		if(num >= n*j){
                	break;
                }
            }
            String filename = "wa_authority_resource_role-" + j + ".bcp";
            File file = new File(rootPath + filename);
            PrintWriter pw = new PrintWriter( new OutputStreamWriter( new FileOutputStream( file ), "utf-8" ) );
            pw.write(str);  
            pw.close();
		}
        
        Document domIndex = DocumentHelper.createDocument();//创建IndexXml文件
        Element root = domIndex.addElement("MESSAGE");//添加根元素
        Element childNode = root.addElement("DATASET").addAttribute("name", "WA_COMMON_010017").addAttribute("ver", "1.0").addAttribute("rmk", "数据文件索引信息");
        Element childNodes = childNode.addElement("DATA");
        
        Element childNodes_FileFormat = childNodes.addElement("DATASET").addAttribute("name", "WA_COMMON_010013").addAttribute("rmk", "BCP文件描述信息");
        Element FileFormats = childNodes_FileFormat.addElement("DATA");
        FileFormats.addElement("ITEM").addAttribute("key", "I010032").addAttribute("val", "").addAttribute("rmk", "列分隔符（缺少值时默认为制表符\t）");
        FileFormats.addElement("ITEM").addAttribute("key", "I010033").addAttribute("val", "").addAttribute("rmk", "行分隔符（缺少值时默认为换行符\n）");
        FileFormats.addElement("ITEM").addAttribute("key", "A010004").addAttribute("val", "WA_AUTHORITY_RESOURCE_ROLE").addAttribute("rmk", "数据集代码");
        FileFormats.addElement("ITEM").addAttribute("key", "F010008").addAttribute("val", "300200").addAttribute("rmk", "数据采集地");
        FileFormats.addElement("ITEM").addAttribute("key", "I010038").addAttribute("val", "2").addAttribute("rmk", "数据起始行，可选项，不填写默认为第１行");
        FileFormats.addElement("ITEM").addAttribute("key", "I010039").addAttribute("val", "UTF-8").addAttribute("rmk", "可选项，默认为UTF-８，BCP文件编码格式（采用不带格式的编码方式，如：UTF-８无BOM）"); 
        
        Element childNodes_DataFile = FileFormats.addElement("DATASET").addAttribute("name", "WA_COMMON_010014").addAttribute("rmk", "BCP数据文件信息");
        for (int j = 1; j <= count; j++) {
        	
        	int Record_number = 0;
        	if(resRole.size() - n * (j - 1) < n){
        		Record_number = resRole.size() - n * (j - 1);
            }else{
            	Record_number = n;
            }
        	Element DataFiles = childNodes_DataFile.addElement("DATA");
        	DataFiles.addElement("ITEM").addAttribute("key", "H040003").addAttribute("val", "").addAttribute("rmk", "文件路径");
        	DataFiles.addElement("ITEM").addAttribute("key", "H010020").addAttribute("val", "wa_authority_resource_role-" + j+".bcp").addAttribute("rmk", "文件名");
        	DataFiles.addElement("ITEM").addAttribute("key", "I010034").addAttribute("val", String.valueOf( Record_number ) ).addAttribute("rmk", "记录行数");
        }   

        Element childNodes_FileStructure = FileFormats.addElement("DATASET").addAttribute("name", "WA_COMMON_010015").addAttribute("rmk", "BCP文件数据结构");
        Element FileStructures = childNodes_FileStructure.addElement("DATA");
//        FileStructures.addElement("ITEM").addAttribute("key", "").addAttribute("eng", "id").addAttribute("val", "").addAttribute("chn", "");

        FileStructures.addElement("ITEM").addAttribute("key", "J030006").addAttribute("eng", "RESOURCE_ID").addAttribute("val", "").addAttribute("chn", "资源唯一标识");
        FileStructures.addElement("ITEM").addAttribute("key", "I010026").addAttribute("eng", "BUSINESS_ROLE").addAttribute("val", "").addAttribute("chn", "角色编码");
        
        FileStructures.addElement("ITEM").addAttribute("key", "J030036").addAttribute("eng", "RESOURCE_CLASS").addAttribute("val", "").addAttribute("chn", "资源分类");
        
        FileStructures.addElement("ITEM").addAttribute("key", "H010029").addAttribute("eng", "DELETE_STATUS").addAttribute("val", "").addAttribute("chn", "删除状态");
        FileStructures.addElement("ITEM").addAttribute("key", "J030017").addAttribute("eng", "DATA_VERSION").addAttribute("val", "").addAttribute("chn", "数据版本号");
        FileStructures.addElement("ITEM").addAttribute("key", "I010005").addAttribute("eng", "LATEST_MOD_TIME").addAttribute("val", "").addAttribute("chn", "最新修改时间");
        
        String xmlIndex = domIndex.asXML();
        String name = "ResInRole";
        
        return CreateIndexXmlAndZip(xmlIndex, rootPath, name, amount);
	}
	
	public String DownLoadRole(String amount, List<ResRole> items) throws Exception {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
        Date date = timeFormat.parse("1970-01-01 00:00:00");
        long second = (System.currentTimeMillis() - date.getTime())/1000;
        String rootPath = System.getProperty("java.io.tmpdir")+"/" + second + "/";
        File DirFile = new File(rootPath);		
        if(!DirFile.exists()){
     	   DirFile.mkdir();
        }	
        
        ResourceDAOImpl dao = new ResourceDAOImpl();
		List<ResRole> role = null;
		if( amount == "All" || amount.equals("All") ){
			role = dao.GetAllRoles();
		}else{
			SystemConfigDAOImpl scdao = new SystemConfigDAOImpl();
			List<SystemConfig> SystemConfigList = scdao.GetConfigByType(SystemConfig.SYSTEMCONFIGTYPESYNC);
			
	        String LatestModTime = getSysConfigByItem(SystemConfig.SYSTEMCONFIG_ITEM_Role, SystemConfigList);
	        role = dao.GetRolesByTime( LatestModTime );
		}
		
		items.addAll(role);
		if( items.size() == 0 )
			return null;
		
		int num = 0;
		int n = 5000;
        int count = role.size()/n;
        if(role.size()%n != 0){
        	count = role.size()/n + 1;
        }
        
        for (int j = 1; j <= count; j++) {
            String str = "BUSINESS_ROLE" + "\t" + "BUSINESS_ROLE_TYPE" + "\t"
            	 + "BUSINESS_ROLE_NAME" + "\t" + "SYSTEM_TYPE" + "\t" + "CLUE_SRC_SYS" + "\t"
            	 + "DELETE_STATUS" + "\t" + "DATA_VERSION" + "\t"
            	 + "LATEST_MOD_TIME"+ "\n";
            
            for (int i = num; i <role.size(); i++)  {
            	str = str + nullConvertEmptyStr( role.get(i).getBUSINESS_ROLE() ) + "\t" 
            			+ role.get(i).getBUSINESS_ROLE_TYPE() + "\t"
            			+ nullConvertEmptyStr( role.get(i).getBUSINESS_ROLE_NAME() ) + "\t" 
            			+ nullConvertEmptyStr( role.get(i).getSYSTEM_TYPE() ) + "\t" 
	            		+ nullConvertEmptyStr( role.get(i).getCLUE_SRC_SYS() ) + "\t"
	            		+ role.get(i).getDELETE_STATUS() + "\t" 
	            		+ role.get(i).getDATA_VERSION() + "\t"
	            		+ getLongTime(role.get(i).getLATEST_MOD_TIME()) + "\n";
            	
            	num++;
        		if(num >= n*j){
                	break;
                }
            }
            String filename = "wa_authority_role-" + j + ".bcp";
            File file = new File(rootPath + filename);
            PrintWriter pw = new PrintWriter( new OutputStreamWriter( new FileOutputStream( file ), "utf-8" ) );
            pw.write(str);  
            pw.close();
		}
        
        Document domIndex = DocumentHelper.createDocument();//创建IndexXml文件
        Element root = domIndex.addElement("MESSAGE");//添加根元素
        Element childNode = root.addElement("DATASET").addAttribute("name", "WA_COMMON_010017").addAttribute("ver", "1.0").addAttribute("rmk", "数据文件索引信息");
        Element childNodes = childNode.addElement("DATA");
        
        Element childNodes_FileFormat = childNodes.addElement("DATASET").addAttribute("name", "WA_COMMON_010013").addAttribute("rmk", "BCP文件描述信息");
        Element FileFormats = childNodes_FileFormat.addElement("DATA");
        FileFormats.addElement("ITEM").addAttribute("key", "I010032").addAttribute("val", "").addAttribute("rmk", "列分隔符（缺少值时默认为制表符\t）");
        FileFormats.addElement("ITEM").addAttribute("key", "I010033").addAttribute("val", "").addAttribute("rmk", "行分隔符（缺少值时默认为换行符\n）");
        FileFormats.addElement("ITEM").addAttribute("key", "A010004").addAttribute("val", "WA_AUTHORITY_ROLE").addAttribute("rmk", "数据集代码");
        FileFormats.addElement("ITEM").addAttribute("key", "F010008").addAttribute("val", "300200").addAttribute("rmk", "数据采集地");
        FileFormats.addElement("ITEM").addAttribute("key", "I010038").addAttribute("val", "2").addAttribute("rmk", "数据起始行，可选项，不填写默认为第１行");
        FileFormats.addElement("ITEM").addAttribute("key", "I010039").addAttribute("val", "UTF-8").addAttribute("rmk", "可选项，默认为UTF-８，BCP文件编码格式（采用不带格式的编码方式，如：UTF-８无BOM）"); 
        
        Element childNodes_DataFile = FileFormats.addElement("DATASET").addAttribute("name", "WA_COMMON_010014").addAttribute("rmk", "BCP数据文件信息");
        for (int j = 1; j <= count; j++) {
        	
        	int Record_number = 0;
        	if(role.size() - n * (j - 1) < n){
        		Record_number = role.size() - n * (j - 1);
            }else{
            	Record_number = n;
            }
        	Element DataFiles = childNodes_DataFile.addElement("DATA");
        	DataFiles.addElement("ITEM").addAttribute("key", "H040003").addAttribute("val", "").addAttribute("rmk", "文件路径");
        	DataFiles.addElement("ITEM").addAttribute("key", "H010020").addAttribute("val", "wa_authority_role-" + j+".bcp").addAttribute("rmk", "文件名");
        	DataFiles.addElement("ITEM").addAttribute("key", "I010034").addAttribute("val", String.valueOf( Record_number ) ).addAttribute("rmk", "记录行数");
        }    

        Element childNodes_FileStructure = FileFormats.addElement("DATASET").addAttribute("name", "WA_COMMON_010015").addAttribute("rmk", "BCP文件数据结构");
        Element FileStructures = childNodes_FileStructure.addElement("DATA");
//        FileStructures.addElement("ITEM").addAttribute("key", "").addAttribute("eng", "id").addAttribute("val", "").addAttribute("chn", "");

        FileStructures.addElement("ITEM").addAttribute("key", "I010026").addAttribute("eng", "BUSINESS_ROLE").addAttribute("val", "").addAttribute("chn", "角色编码");
        FileStructures.addElement("ITEM").addAttribute("key", "I010025").addAttribute("eng", "BUSINESS _ROLE_TYPE").addAttribute("val", "").addAttribute("chn", "角色类型");
        FileStructures.addElement("ITEM").addAttribute("key", "I010054").addAttribute("eng", "BUSINESS _ROLE_NAME").addAttribute("val", "").addAttribute("chn", "角色名称");
        FileStructures.addElement("ITEM").addAttribute("key", "B050016").addAttribute("eng", "SYSTEM_TYPE").addAttribute("val", "").addAttribute("chn", "系统类型");
        
//        FileStructures.addElement("ITEM").addAttribute("key", "").addAttribute("eng", "ROLE_DESC").addAttribute("val", "").addAttribute("chn", "");
        
        FileStructures.addElement("ITEM").addAttribute("key", "H010029").addAttribute("eng", "DELETE_STATUS").addAttribute("val", "").addAttribute("chn", "删除状态");
        FileStructures.addElement("ITEM").addAttribute("key", "J030017").addAttribute("eng", "DATA_VERSION").addAttribute("val", "").addAttribute("chn", "数据版本号");
        FileStructures.addElement("ITEM").addAttribute("key", "I010005").addAttribute("eng", "LATEST_MOD_TIME").addAttribute("val", "").addAttribute("chn", "最新修改时间");
        
        String xmlIndex = domIndex.asXML();
        String name = "Role";
        
        return CreateIndexXmlAndZip(xmlIndex, rootPath, name, amount);
	}
	
	public String CreateIndexXmlAndZip(String xml, String rootPath, String name, String amount) throws Exception {
	     //xml格式化
        Document doc = DocumentHelper.parseText(xml);       
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setNewlines(true);
        format.setTrimText(true);
        format.setPadText(true);
        format.setEncoding("UTF-8");
        String filenameIndex = "GAB_ZIP_INDEX.xml";
        FileOutputStream out = new FileOutputStream(rootPath + filenameIndex);
        XMLWriter xmlWriter = new XMLWriter(out, format);  
        xmlWriter.write(doc);   
        xmlWriter.flush();
        out.close();
        
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
        Date date = timeFormat.parse("1970-01-01 00:00:00");
        long second = (System.currentTimeMillis() - date.getTime())/1000;

        SystemConfigDAOImpl dao = new SystemConfigDAOImpl();
		List<SystemConfig> SystemConfigList = dao.GetConfigByType(SystemConfig.SYSTEMCONFIGTYPESYNC);
		
        String businessType = getSysConfigByItem(SystemConfig.SYSTEMCONFIG_ITEM_BUSINESSTYPE, SystemConfigList);//"5416";
        String dataSource = getSysConfigByItem(SystemConfig.SYSTEMCONFIG_ITEM_DATASOURCE, SystemConfigList);//"010000";
        String sn = getSysConfigByItem(SystemConfig.SYSTEMCONFIG_ITEM_SN, SystemConfigList);//"00001";
        sn = String.format("%05d", Integer.parseInt(sn));
        String exportPath = getSysConfigByItem(SystemConfig.SYSTEMCONFIG_ITEM_EXPORTPATH, SystemConfigList);
        
        String zipNnme = businessType + "-" + dataSource + "-" + name + "-" + amount + "-" + second + "-" + sn + ".zip";
        
//        ZipUtil zs = new  ZipUtil(new FileChooserUtil().fileChooser() + zipNnme);
        ZipUtil zs = new  ZipUtil(exportPath +"/"+ zipNnme);
	    zs.compress(rootPath);

	    UpdateConfig(SystemConfigList, name);
	    
	    broadcastNotice(exportPath, zipNnme);
	    
		return exportPath +"/"+ zipNnme;
	    
	}
	
	private void broadcastNotice(String path, String fileName) throws Exception {
		// 1. calculate checksum
		File file = new File(path + "/" + fileName);
		FileInputStream fi = new FileInputStream(file);
		
		String checksum = MD5Security.md5(fi);
		
		// 2. get notice list
		List<String> sids = new ArrayList<String>();
		//sids.add("S01000011000000009");//gab sync service
		//sids.add("S11000000000000009");//bj sync service
		sids.add("S33020000000000009");//zhejiang_ningbo sync service          
			
		// 3. notice other pms	
		String address = ConfigHelper.getEsbAddr();
		String rid = ConfigHelper.getRequestId();
		
		SendSyncMessage ssm = new SendSyncMessage(address, rid);
//			if(sids == null) {
//				return;
//			}
		for( String sid : sids) {
			// 3.1 generate request content
			String message = generateBroadcastRequestContent(sid, checksum, fileName);
			
			String result = ssm.SendMessage(sid, message);
			System.out.println(result);
		}
		
	
		
	}
	private String generateBroadcastRequestContent(String sid, String checksum, String fileName) throws IOException {
		String result = null;
		org.jdom2.Document doc = null;
		try{
			org.jdom2.Element message = null;// dataset = null, data = null, condition = null, item = null;
			message = new org.jdom2.Element("MESSAGE");
			doc = new org.jdom2.Document(message);
			
			// 1- WA_COMMON_010000
			org.jdom2.Element dataset010000 = null;
			dataset010000 = new org.jdom2.Element("DATASET");
			message.addContent(dataset010000);
			dataset010000.setAttribute("name", "WA_COMMON_010000");
			dataset010000.setAttribute("rmk", "数据交互通用信息");
			
			org.jdom2.Element data010000 = null;
			data010000 = new org.jdom2.Element("DATA");
			dataset010000.addContent(data010000);
			
			org.jdom2.Element item010000 = null;
			item010000 = new org.jdom2.Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "H010006");
			String from = ConfigHelper.getRegion();
			itemSetAttribute(item010000, "val", from);
			itemSetAttribute(item010000, "rmk", "发起节点的标识");
			
			item010000 = new org.jdom2.Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "H010007");
			String to = sid.length() > 7 ? sid.substring(1, 7) : "";
			itemSetAttribute(item010000, "val", to);
			itemSetAttribute(item010000, "rmk", "目的节点的标识");
			
			item010000 = new org.jdom2.Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "I010014");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
			String longtime = ("" + System.currentTimeMillis()).substring(0,10);
			String mesgSn = from + timenow + longtime;
			itemSetAttribute(item010000, "val", mesgSn);
			itemSetAttribute(item010000, "rmk", "消息流水号");
			
			item010000 = new org.jdom2.Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "I010013");
			itemSetAttribute(item010000, "val", "154161");
			itemSetAttribute(item010000, "rmk", "消息类型（鉴权服务结果）");

			item010000 = new org.jdom2.Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "I010010");
			itemSetAttribute(item010000, "val", "07");
			itemSetAttribute(item010000, "rmk", "业务服务类型（鉴权服务）");
			
			// 1- WA_COMMON_010001
			org.jdom2.Element dataset010001 = null;
			dataset010001 = new org.jdom2.Element("DATASET");
			message.addContent(dataset010001);
			dataset010001.setAttribute("name", "WA_COMMON_010001");
			dataset010001.setAttribute("rmk", "用户认证和公共角色信息");

			org.jdom2.Element data010001 = null;
			data010001 = new org.jdom2.Element("DATA");
			dataset010001.addContent(data010001);
			
			org.jdom2.Element item010001 = null;
			item010001 = new org.jdom2.Element("ITEM");
			data010001.addContent(item010001);
			itemSetAttribute(item010001, "key", "A010001");
			itemSetAttribute(item010001, "val", from);
			itemSetAttribute(item010001, "eng", "GA_DEPARTMENT");
			itemSetAttribute(item010001, "chn", "公安机关机构代码");
			
			item010001 = new org.jdom2.Element("ITEM");
			data010001.addContent(item010001);
			itemSetAttribute(item010001, "key", "I010026");
			itemSetAttribute(item010001, "val", "");
			itemSetAttribute(item010001, "eng", "ROLE_ID");
			itemSetAttribute(item010001, "chn", "角色ID");
			
			item010001 = new org.jdom2.Element("ITEM");
			data010001.addContent(item010001);
			itemSetAttribute(item010001, "key", "J030014");
			itemSetAttribute(item010001, "val", "");
			itemSetAttribute(item010001, "eng", "CERTIFICATE_CODE_MD5");
			itemSetAttribute(item010001, "chn", "身份证哈希值");
			
			item010001 = new org.jdom2.Element("ITEM");
			data010001.addContent(item010001);
			itemSetAttribute(item010001, "key", "I010025");
			itemSetAttribute(item010001, "val", "");
			itemSetAttribute(item010001, "eng", "ROLE_TYPE");
			itemSetAttribute(item010001, "chn", "角色类型");
			
			item010001 = new org.jdom2.Element("ITEM");
			data010001.addContent(item010001);
			itemSetAttribute(item010001, "key", "I010027");
			itemSetAttribute(item010001, "val", "");
			itemSetAttribute(item010001, "eng", "COOKIES");
			itemSetAttribute(item010001, "chn", "通行字");
			
			// 1- WA_COMMON_010045
			org.jdom2.Element dataset010045 = null;
			dataset010045 = new org.jdom2.Element("DATASET");
			message.addContent(dataset010045);
			dataset010045.setAttribute("name", "WA_COMMON_010045");
			dataset010045.setAttribute("rmk", "");

			org.jdom2.Element data010045 = null;
			data010045 = new org.jdom2.Element("DATA");
			dataset010045.addContent(data010045);
						
			org.jdom2.Element item010045 = null;
			item010045 = new org.jdom2.Element("ITEM");
			data010045.addContent(item010045);
			String fileKey = fileName.indexOf("All") != -1 ? "H010019" : "H010020";
			itemSetAttribute(item010045, "key", fileKey);
			itemSetAttribute(item010045, "val", fileName);
			itemSetAttribute(item010045, "eng", "FILE_NAME");
			String fileChn = fileName.indexOf("All") != -1 ? "全量数据包文件名称" : "增量数据包文件名称";
			itemSetAttribute(item010045, "chn", fileChn);
			
			item010045 = new org.jdom2.Element("ITEM");
			data010045.addContent(item010045);
			itemSetAttribute(item010045, "key", "H010028");
			itemSetAttribute(item010045, "val", checksum);
			itemSetAttribute(item010045, "eng", "checksum");
			itemSetAttribute(item010045, "chn", "校验和");
			itemSetAttribute(item010045, "fmt", "md5");
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
	
	protected void itemSetAttribute(org.jdom2.Element item, String key, String value) {
		item.setAttribute(key, value == null ? "" : value);
	}

	public SystemConfig UpdateConfig( List<SystemConfig> scList, String name ) throws Exception
	{
	    SystemConfig systemConfig=new SystemConfig();
	    SystemConfigDAOImpl dao = new SystemConfigDAOImpl();
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		
	    for (int i = 0; i < scList.size(); i++) {
	    	systemConfig.setId( scList.get(i).getId() );
	    	systemConfig.setItem( scList.get(i).getItem() );
	    	if( scList.get(i).getItem().equals( SystemConfig.SYSTEMCONFIG_ITEM_SN ) ){		
	    		systemConfig.setValue( String.valueOf( Integer.parseInt( scList.get(i).getValue() ) + 1 ) );
	    	}else if( scList.get(i).getItem().equals( "sync_" + name ) )
	    	{
	    		systemConfig.setValue( timenow );
	    		
	    	}else{
	    		systemConfig.setValue( scList.get(i).getValue() );
	    	}
	    	systemConfig.setRmk( scList.get(i).getRmk() );
	    	systemConfig.setType( scList.get(i).getType() );
			systemConfig.setLATEST_MOD_TIME( timenow );
			
			systemConfig = dao.UpdateConfig(systemConfig);
		} 
	    
		return systemConfig;
	}
	
	private String getSysConfigByItem(String item, List<SystemConfig> scList) throws Exception {
		
		String value = null;
		for (int i = 0; i < scList.size(); i++) {
			if(scList.get(i).getItem().equals(item)){		
				value = scList.get(i).getValue();
				break;
			}
		}
		return value;
	}
	
	private long getLongTime(String time) {
		long longtime = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
        try {
            // 
            // The get the date object from the string just called the 
            // parse method and pass the time string to it. The method 
            // throws ParseException if the time string is in an 
            // invalid format. But remember as we don't pass the date 
            // information this date object will represent the 1st of
            // january 1970.
            Date date = sdf.parse(time);
            longtime = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return longtime;
	}
	
	private String nullConvertEmptyStr(String value){
		 return value == null ? "" : value;
	}
	
}

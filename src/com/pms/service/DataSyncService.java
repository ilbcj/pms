package com.pms.service;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.pms.dao.impl.OrganizationDAOImpl;
import com.pms.dao.impl.ResourceDAOImpl;
import com.pms.dao.impl.SystemConfigDAOImpl;
import com.pms.dao.impl.UserDAOImpl;
import com.pms.model.Organization;
import com.pms.model.ResData;
import com.pms.model.SystemConfig;
import com.pms.model.User;
import com.pms.util.ZipUtil;

public class DataSyncService {
	public void DownLoadRes() throws Exception {
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
        Date date = timeFormat.parse("1970-01-01 00:00:00");
        long second = (System.currentTimeMillis() - date.getTime())/1000;
        String rootPath = "E:/" + second + "/";
        File DirFile = new File(rootPath);		
		if(!DirFile.exists()){
    	    DirFile.mkdir();
        }
    	  
		ResourceDAOImpl dao=new ResourceDAOImpl();
		List<ResData> res =dao.GetAllDatas();
		
		int num = 0;
		int n = 5000;
        int count = res.size()/n;
        if(res.size() % n != 0){
        	count = res.size()/n + 1;
        }
        
        for (int j = 1; j <= count; j++) {
            Document dom = DocumentHelper.createDocument();//创建xml文件 
            Element root = dom.addElement("root");//添加根元素,XValue  
            root.addAttribute("name", "WA_AUTHORITY_RESOURCE");
            String str = "\n" + "id" + "\t" + "name" + "\t" + "resource_type" + "\t"  + 
	    		"RESOURCE_ID" + "\t" + "RESOURCE_STATUS" + "\t" + "RESOURCE_DESCRIBE" + "\t"  + 
				"DATASET_SENSITIVE_LEVEL" + "\t" + "DATA_SET" + "\t" + "SECTION_CLASS" + "\t"  + 
				"ELEMENT" + "\t" + "SECTION_RELATIOIN_CLASS" + "\t" + "OPERATE_SYMBOL" + "\t"  + 
				"ELEMENT_VALUE" + "\t" + "DELETE_STATUS" + "\t" + "DATA_VERSION" + "\t"  + 
				"LATEST_MOD_TIME" + "\t" + "RESOURCE_REMARK" + "\n";
            for (int i = num; i < res.size(); i++)  {
            	str = str + res.get(i).getId() + "\t" + res.get(i).getName() + "\t" + res.get(i).getResource_type() + "\t"
        			 + res.get(i).getRESOURCE_ID() + "\t" + res.get(i).getRESOURCE_STATUS() + "\t" + res.get(i).getRESOURCE_DESCRIBE() + "\t"
            		 + res.get(i).getDATASET_SENSITIVE_LEVEL() + "\t" + res.get(i).getDATA_SET() + "\t" + res.get(i).getSECTION_CLASS() + "\t"
            		 + res.get(i).getELEMENT() + "\t" + res.get(i).getSECTION_RELATIOIN_CLASS() + "\t" + res.get(i).getOPERATE_SYMBOL() + "\t"
            		 + res.get(i).getELEMENT_VALUE() + "\t" + res.get(i).getDELETE_STATUS() + "\t" + res.get(i).getDATA_VERSION() + "\t"
            		 + res.get(i).getLATEST_MOD_TIME() + "\t" + res.get(i).getRESOURCE_REMARK() + "\n";
            	num++;
        		if(num >= n*j){
                	break;
                }
            }
        	root.setText(str);
            String xml = dom.asXML();
            String filename = "wa_authority_resource_" + j + ".xml";
            File file = new File(rootPath + filename);  
            PrintWriter pw = new PrintWriter(file);
            pw.write(xml);  
            pw.close();
		}

        Document domIndex = DocumentHelper.createDocument();//创建IndexXml文件
        Element root = domIndex.addElement("MESSAGE");//添加根元素
        Element childNode = root.addElement("DATASET").addAttribute("name", "WA_COMMON_010017").addAttribute("ver", "1.0").addAttribute("rmk", "数据文件索引信息");
        Element childNodes = childNode.addElement("DATA");
        
        Element childNodes_FileFormat = childNodes.addElement("DATASET").addAttribute("name", "WA_COMMON_010013").addAttribute("rmk", "BCP文件格式信息");
        Element FileFormats = childNodes_FileFormat.addElement("DATA");
        FileFormats.addElement("ITEM").addAttribute("key", "I010032").addAttribute("val", "").addAttribute("rmk", "列分隔符（缺少值时默认为制表符\t）");
        FileFormats.addElement("ITEM").addAttribute("key", "I010033").addAttribute("val", "").addAttribute("rmk", "行分隔符（缺少值时默认为换行符\n）");
        FileFormats.addElement("ITEM").addAttribute("key", "A010004").addAttribute("val", "WA_SOURCE_0002").addAttribute("rmk", "数据集代码");
        FileFormats.addElement("ITEM").addAttribute("key", "F010008").addAttribute("val", "300200").addAttribute("rmk", "数据采集地");
        FileFormats.addElement("ITEM").addAttribute("key", "I010038").addAttribute("val", "1").addAttribute("rmk", "数据集编码（表控）");
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
        	DataFiles.addElement("ITEM").addAttribute("key", "H040003").addAttribute("val", "attach").addAttribute("rmk", "文件路径");
        	DataFiles.addElement("ITEM").addAttribute("key", "H010020").addAttribute("val", "wa_authority_resource_" + j+".xml").addAttribute("rmk", "文件名");
        	DataFiles.addElement("ITEM").addAttribute("key", "I010034").addAttribute("val", String.valueOf( Record_number ) ).addAttribute("rmk", "记录行数");
        }   
        
        Element childNodes_FileStructure = FileFormats.addElement("DATASET").addAttribute("name", "WA_COMMON_010015").addAttribute("rmk", "BCP文件结构信息");
        Element FileStructures = childNodes_FileStructure.addElement("DATA");
        FileStructures.addElement("ITEM").addAttribute("key", "").addAttribute("eng", "id").addAttribute("val", "").addAttribute("chn", "");
        FileStructures.addElement("ITEM").addAttribute("key", "").addAttribute("eng", "name").addAttribute("val", "").addAttribute("chn", "");
        
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
        FileStructures.addElement("ITEM").addAttribute("key", "J030013").addAttribute("eng", "RESOURCE_REMARK").addAttribute("val", "").addAttribute("chn", "备注");	

            
        String xmlIndex = domIndex.asXML();
        
        CreateIndexXmlAndZip(xmlIndex,rootPath);
	}
	
	public void DownLoadOrg() throws Exception {
       SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
       Date date = timeFormat.parse("1970-01-01 00:00:00");
       long second = (System.currentTimeMillis() - date.getTime())/1000;
       String rootPath = "E:/" + second + "/";
       File DirFile = new File(rootPath);		
       if(!DirFile.exists()){
    	   DirFile.mkdir();
       }
       
		OrganizationDAOImpl dao = new OrganizationDAOImpl();
		List<Organization> org = dao.GetAllOrgs();
		
		int num = 0;
		int n = 5000;
        int count = org.size()/n;
        if(org.size()%n != 0){
        	count = org.size()/n + 1;
        }
        
        for (int j = 1; j <= count; j++) {
            Document dom = DocumentHelper.createDocument();//创建xml文件 
            Element root = dom.addElement("root");//添加根元素,XValue  
            root.addAttribute("name", "WA_AUTHORITY_ORGNIZATION");
            String str = "\n" + "GA_DEPARTMENT" + "\t" + "UNIT" + "\t" + "ORG_LEVEL" + "\t"  + 
	    		"PARENT_ORG" + "\t" + "DELETE_STATUS" + "\t" + "DATA_VERSION" + "\t"  + 
				"LATEST_MOD_TIME" + "\n";
            for (int i = num; i < org.size(); i++)  {
            	str = str + org.get(i).getGA_DEPARTMENT() + "\t" + org.get(i).getUNIT() + "\t" + org.get(i).getORG_LEVEL() + "\t"
            		 + org.get(i).getPARENT_ORG() + "\t" + org.get(i).getDELETE_STATUS() + "\t" + org.get(i).getDATA_VERSION() + "\t"
            		 + org.get(i).getLATEST_MOD_TIME() + "\n";
            	num++;
        		if(num >= n*j){
                	break;
                }
            }
        	root.setText(str);
            String xml = dom.asXML();
            String filename = "wa_authority_orgnization_" + j + ".xml";
            File file = new File(rootPath + filename); 
            PrintWriter pw = new PrintWriter(file);
            pw.write(xml);  
            pw.close();
		}

        Document domIndex = DocumentHelper.createDocument();//创建IndexXml文件
        Element root = domIndex.addElement("MESSAGE");//添加根元素
        Element childNode = root.addElement("DATASET").addAttribute("name", "WA_COMMON_010017").addAttribute("ver", "1.0").addAttribute("rmk", "数据文件索引信息");
        Element childNodes = childNode.addElement("DATA");
        
        Element childNodes_FileFormat = childNodes.addElement("DATASET").addAttribute("name", "WA_COMMON_010013").addAttribute("rmk", "BCP文件格式信息");
        Element FileFormats = childNodes_FileFormat.addElement("DATA");
        FileFormats.addElement("ITEM").addAttribute("key", "I010032").addAttribute("val", "").addAttribute("rmk", "列分隔符（缺少值时默认为制表符\t）");
        FileFormats.addElement("ITEM").addAttribute("key", "I010033").addAttribute("val", "").addAttribute("rmk", "行分隔符（缺少值时默认为换行符\n）");
        FileFormats.addElement("ITEM").addAttribute("key", "A010004").addAttribute("val", "WA_SOURCE_0002").addAttribute("rmk", "数据集代码");
        FileFormats.addElement("ITEM").addAttribute("key", "F010008").addAttribute("val", "300200").addAttribute("rmk", "数据采集地");
        FileFormats.addElement("ITEM").addAttribute("key", "I010038").addAttribute("val", "1").addAttribute("rmk", "数据集编码（表控）");
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
        	DataFiles.addElement("ITEM").addAttribute("key", "H040003").addAttribute("val", "attach").addAttribute("rmk", "文件路径");
        	DataFiles.addElement("ITEM").addAttribute("key", "H010020").addAttribute("val", "wa_authority_orgnization_" + j+".xml").addAttribute("rmk", "文件名");
        	DataFiles.addElement("ITEM").addAttribute("key", "I010034").addAttribute("val", String.valueOf( Record_number ) ).addAttribute("rmk", "记录行数");
        }   

        Element childNodes_FileStructure = FileFormats.addElement("DATASET").addAttribute("name", "WA_COMMON_010015").addAttribute("rmk", "BCP文件结构信息");
        Element FileStructures = childNodes_FileStructure.addElement("DATA");
        FileStructures.addElement("ITEM").addAttribute("key", "A010001").addAttribute("eng", "GA_DEPARTMENT").addAttribute("chn", "公安组织机构代码");
        FileStructures.addElement("ITEM").addAttribute("key", "E010002").addAttribute("eng", "UNIT").addAttribute("chn", "公安组织机构名称");
        FileStructures.addElement("ITEM").addAttribute("key", "I010052").addAttribute("eng", "ORG_LEVEL").addAttribute("chn", "公安组织机构级别");
        FileStructures.addElement("ITEM").addAttribute("key", "A010005").addAttribute("eng", "PARENT_ORG").addAttribute("chn", "组织机构父节点(上级组织机构)");
        FileStructures.addElement("ITEM").addAttribute("key", "H010029").addAttribute("eng", "DELETE_STATUS").addAttribute("chn", "删除状态");
        FileStructures.addElement("ITEM").addAttribute("key", "J030017").addAttribute("eng", "DATA_VERSION").addAttribute("chn", "数据版本号");
        FileStructures.addElement("ITEM").addAttribute("key", "I010005").addAttribute("eng", "LATEST_MOD_TIME").addAttribute("chn", "最新修改时间");
        
        String xmlIndex = domIndex.asXML();
            
        CreateIndexXmlAndZip(xmlIndex,rootPath);
	}

	public void DownLoadUser() throws Exception {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
        Date date = timeFormat.parse("1970-01-01 00:00:00");
        long second = (System.currentTimeMillis() - date.getTime())/1000;
        String rootPath = "E:/" + second + "/";
        File DirFile = new File(rootPath);		
        if(!DirFile.exists()){
     	   DirFile.mkdir();
        }	
        
		UserDAOImpl dao = new UserDAOImpl();
		List<User> user = dao.GetAllUsers();
		
		int num = 0;
		int n = 5000;
        int count = user.size()/n;
        if(user.size()%n != 0){
        	count = user.size()/n + 1;
        }

        for (int j = 1; j <= count; j++) {
            Document dom = DocumentHelper.createDocument();//创建xml文件 
            Element root = dom.addElement("root");//添加根元素,XValue  
            root.addAttribute("name", "WA_AUTHORITY_POLICE");
            String str = "\n" + "id" + "\t" + "NAME" + "\t" + "CERTIFICATE_CODE_MD5" + "\t"
            	 + "CERTIFICATE_CODE_SUFFIX" + "\t" + "SEXCODE" + "\t" + "GA_DEPARTMENT" + "\t"
            	 + "UNIT" + "\t"+ "ORG_LEVEL" + "\t" + "POLICE_SORT" + "\t"
            	 + "POLICE_NO" + "\t" + "SENSITIVE_LEVEL" + "\t" + "BUSINESS_TYPE" + "\t"
            	 + "TAKE_OFFICE" + "\t" + "USER_STATUS" + "\t" + "position" + "\t"
            	 + "dept" + "\t" + "DELETE_STATUS" + "\t" + "DATA_VERSION" + "\t"
            	 + "LATEST_MOD_TIME" + "\t"  + "\n";
            for (int i = num; i <user.size(); i++)  {
            	str = str + user.get(i).getId() + "\t" + user.get(i).getNAME() + "\t" + user.get(i).getCERTIFICATE_CODE_MD5() + "\t"
            		 + user.get(i).getCERTIFICATE_CODE_SUFFIX() + "\t" + user.get(i).getSEXCODE() + "\t" + user.get(i).getGA_DEPARTMENT() + "\t"
            		 + user.get(i).getUNIT() + "\t" + user.get(i).getORG_LEVEL() + "\t" + user.get(i).getPOLICE_SORT() + "\t"
            		 + user.get(i).getPOLICE_NO() + "\t" + user.get(i).getSENSITIVE_LEVEL() + "\t" + user.get(i).getBUSINESS_TYPE() + "\t"
            		 + user.get(i).getTAKE_OFFICE() + "\t" + user.get(i).getUSER_STATUS() + "\t" + user.get(i).getPosition() + "\t"
            		 + user.get(i).getDept() + "\t" + user.get(i).getDELETE_STATUS() + "\t" + user.get(i).getDATA_VERSION() + "\t"
            		 + user.get(i).getLATEST_MOD_TIME() + "\n";
            	num++;
        		if(num >= n*j){
                	break;
                }
            }
        	root.setText(str);
            String xml = dom.asXML();
            String filename = "wa_authority_police_" + j + ".xml";
            File file = new File(rootPath + filename);
            PrintWriter pw = new PrintWriter(file);
            pw.write(xml);  
            pw.close();
		}
        
        Document domIndex = DocumentHelper.createDocument();//创建IndexXml文件
        Element root = domIndex.addElement("MESSAGE");//添加根元素
        Element childNode = root.addElement("DATASET").addAttribute("name", "WA_COMMON_010017").addAttribute("ver", "1.0").addAttribute("rmk", "数据文件索引信息");
        Element childNodes = childNode.addElement("DATA");
        
        Element childNodes_FileFormat = childNodes.addElement("DATASET").addAttribute("name", "WA_COMMON_010013").addAttribute("rmk", "BCP文件格式信息");
        Element FileFormats = childNodes_FileFormat.addElement("DATA");
        FileFormats.addElement("ITEM").addAttribute("key", "I010032").addAttribute("val", "").addAttribute("rmk", "列分隔符（缺少值时默认为制表符\t）");
        FileFormats.addElement("ITEM").addAttribute("key", "I010033").addAttribute("val", "").addAttribute("rmk", "行分隔符（缺少值时默认为换行符\n）");
        FileFormats.addElement("ITEM").addAttribute("key", "A010004").addAttribute("val", "WA_SOURCE_0002").addAttribute("rmk", "数据集代码");
        FileFormats.addElement("ITEM").addAttribute("key", "F010008").addAttribute("val", "300200").addAttribute("rmk", "数据采集地");
        FileFormats.addElement("ITEM").addAttribute("key", "I010038").addAttribute("val", "1").addAttribute("rmk", "数据集编码（表控）");
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
        	DataFiles.addElement("ITEM").addAttribute("key", "H040003").addAttribute("val", "attach").addAttribute("rmk", "文件路径");
        	DataFiles.addElement("ITEM").addAttribute("key", "H010020").addAttribute("val", "wa_authority_police_" + j+".xml").addAttribute("rmk", "文件名");
        	DataFiles.addElement("ITEM").addAttribute("key", "I010034").addAttribute("val", String.valueOf( Record_number ) ).addAttribute("rmk", "记录行数");
        }   

        Element childNodes_FileStructure = FileFormats.addElement("DATASET").addAttribute("name", "WA_COMMON_010015").addAttribute("rmk", "BCP文件结构信息");
        Element FileStructures = childNodes_FileStructure.addElement("DATA");
        
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
        
        FileStructures.addElement("ITEM").addAttribute("key", "").addAttribute("eng", "position").addAttribute("val", "").addAttribute("chn", "");
        FileStructures.addElement("ITEM").addAttribute("key", "").addAttribute("eng", "dept").addAttribute("val", "").addAttribute("chn", "");
        
        FileStructures.addElement("ITEM").addAttribute("key", "H010029").addAttribute("eng", "DELETE_STATUS").addAttribute("val", "").addAttribute("chn", "删除状态");
        FileStructures.addElement("ITEM").addAttribute("key", "J030017").addAttribute("eng", "DATA_VERSION").addAttribute("val", "").addAttribute("chn", "数据版本号");	
        FileStructures.addElement("ITEM").addAttribute("key", "I010005").addAttribute("eng", "LATEST_MOD_TIME").addAttribute("val", "").addAttribute("chn", "最新修改时间");
        
        String xmlIndex = domIndex.asXML();
        
        CreateIndexXmlAndZip(xmlIndex,rootPath);
	}
	
	public void CreateIndexXmlAndZip(String xml,String rootPath) throws Exception {
	     //xml格式化
        Document doc = DocumentHelper.parseText(xml);       
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setNewlines(true);
        format.setTrimText(true);
        format.setPadText(true);
        format.setEncoding("UTF-8");
        StringWriter out = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(out, format);   
        xmlWriter.write(doc);   
        xmlWriter.flush();
        
        String filenameIndex = "GAB_ZIP_INDEX.xml";
        File fileIndex = new File(rootPath + filenameIndex);
        PrintWriter pwIndex = new PrintWriter(fileIndex);   
        pwIndex.write(out.toString());
        pwIndex.close();
        
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
        Date date = timeFormat.parse("1970-01-01 00:00:00");
        long second = (System.currentTimeMillis() - date.getTime())/1000;

        SystemConfigDAOImpl dao = new SystemConfigDAOImpl();
		List<SystemConfig> SystemConfigList = dao.GetConfigByType(SystemConfig.SYSTEMCONFIGTYPESYNC);
		
        String businessType = getSysConfigByItem(SystemConfig.SYSTEMCONFIG_ITEM_BUSINESSTYPE, SystemConfigList);//"5416";
        String dataSource = getSysConfigByItem(SystemConfig.SYSTEMCONFIG_ITEM_DATASOURCE, SystemConfigList);//"010000";
        String sn = getSysConfigByItem(SystemConfig.SYSTEMCONFIG_ITEM_SN, SystemConfigList);//"00001";
        sn = String.format("%05d", Integer.parseInt(sn));  

        String zipNnme = businessType + "-" + dataSource + "-All-" + second + "-" + sn + ".zip";

        ZipUtil zs = new  ZipUtil("E:/" + zipNnme);
	    zs.compress(rootPath);

	    UpdateConfig(SystemConfigList);
	    
	}
	
	public SystemConfig UpdateConfig( List<SystemConfig> scList ) throws Exception
	{
	    SystemConfig systemConfig=new SystemConfig();
	    for (int i = 0; i < scList.size(); i++) {
	    	systemConfig.setId( scList.get(i).getId() );
	    	systemConfig.setItem( scList.get(i).getItem() );
	    	if(scList.get(i).getItem().equals( SystemConfig.SYSTEMCONFIG_ITEM_SN )){		
	    		systemConfig.setValue( String.valueOf( Integer.parseInt( scList.get(i).getValue() ) + 1 ));
	    	}
	    	systemConfig.setRmk( scList.get(i).getRmk() );
	    	systemConfig.setType( scList.get(i).getType() );
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
			systemConfig.setLATEST_MOD_TIME( timenow );
		}
	    
	    SystemConfigDAOImpl dao = new SystemConfigDAOImpl();
	    dao.UpdateConfig(systemConfig);
	    
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
	
}

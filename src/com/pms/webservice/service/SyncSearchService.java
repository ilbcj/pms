package com.pms.webservice.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.pms.model.Organization;
import com.pms.model.ResData;
import com.pms.model.ResRole;
import com.pms.model.User;
import com.pms.webservice.dao.SearchDAO;
import com.pms.webservice.dao.impl.SearchDAOImpl;
import com.pms.webservice.model.Condition;

public class SyncSearchService extends SyncService {

	@SuppressWarnings("rawtypes")
	@Override
	public String GetResult() throws IOException {
		String result = "";
		Document doc = null;
		try{
			Element message = null, dataset = null, data = null, item = null;
			message = new Element("MESSAGE");
			doc = new Document(message);
			//WA_COMMON_010000
			dataset = new Element("DATASET");
			message.addContent(dataset);
			dataset.setAttribute("name", "WA_COMMON_010000");
			dataset.setAttribute("rmk", "数据交互通用信息");
			
			data = new Element("DATA");
			dataset.addContent(data);
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "H010006");
			itemSetAttribute(item, "val", this.getDci().getFROM());
			itemSetAttribute(item, "rmk", "发起节点的标识");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "H010007");
			itemSetAttribute(item, "val", this.getDci().getTO());
			itemSetAttribute(item, "rmk", "目的节点的标识");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I010014");
			itemSetAttribute(item, "val", this.getDci().getMESSAGE_SEQUENCE());
			itemSetAttribute(item, "rmk", "消息流水号");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I010013");
			itemSetAttribute(item, "val", "254141");
			itemSetAttribute(item, "rmk", "消息类型（数据存储查询返回结果响应）");

			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I010010");
			itemSetAttribute(item, "val", this.getDci().getBUSINESS_SERVER_TYPE());
			itemSetAttribute(item, "rmk", "业务服务类型（数据存储访问服务）");
			
			//WA_COMMON_010004
			dataset = new Element("DATASET");
			message.addContent(dataset);
			dataset.setAttribute("name", "WA_COMMON_010004");
			dataset.setAttribute("rmk", "消息返回状态信息");

			data = new Element("DATA");
			dataset.addContent(data);
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I030003");
			itemSetAttribute(item, "val", "0");
			itemSetAttribute(item, "rmk", "消息状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I010015");
			itemSetAttribute(item, "val", "" + new Date().getTime());
			itemSetAttribute(item, "rmk", "消息返回时间");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I030010");
			itemSetAttribute(item, "val", "");
			itemSetAttribute(item, "rmk", "业务消息错误码");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I010009");
			itemSetAttribute(item, "val", "");
			itemSetAttribute(item, "rmk", "备注（错误说明）");
			
			//WA_COMMON_010124
			dataset = new Element("DATASET");
			message.addContent(dataset);
			dataset.setAttribute("name", "WA_COMMON_010124");
			dataset.setAttribute("rmk", "查询返回结果");

			data = new Element("DATA");
			dataset.addContent(data);
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "H010005");
			itemSetAttribute(item, "val", "");
			itemSetAttribute(item, "rmk", "查询任务标识（异步查询时必填）");
			
			dataset = new Element("DATASET");
			data.addContent(dataset);
			dataset.setAttribute("name", "WA_COMMON_010125");
			dataset.setAttribute("rmk", "查询返回数据");
			
			if(this.getSc() == null) {
				setError(getDataSet(message, "WA_COMMON_010004"), "-10001", "查询条件不存在");
				throw new Exception("search condition data error");
			}
			
			String sqlStr = "select ";
			if( this.getSc().getRETURNINFO() == null || this.getSc().getRETURNINFO().size() == 0 ) {
				sqlStr += "* ";
			}
			else {
				// TODO parse return column
			}
			
			sqlStr += "from ";
			
			if( this.getSc().getTableName() == null || this.getSc().getTableName().length() == 0) {
				setError(getDataSet(message, "WA_COMMON_010004"), "-10002", "查询条件不正确，表名不存在");
				throw new Exception("search condition data error");
			}
			else {
				sqlStr += this.getSc().getTableName() + " ";
			}
			
			if( this.getSc().getCONDITION() == null || this.getSc().getCONDITION().length() == 0 
					|| this.getSc().getCONDITIONITEMS() == null || this.getSc().getCONDITIONITEMS().size() == 0 ) {
				//no where case
			}
			else {
				sqlStr += "where ";
				List<Condition> cons = this.getSc().getCONDITIONITEMS();
				sqlStr += cons.get(0).getEng() + "='" + cons.get(0).getVal() + "' ";
				for(int i = 1; i<cons.size(); i++) {
					sqlStr += this.getSc().getCONDITION() + " " + cons.get(i).getEng() + "='" + cons.get(i).getVal() + "' ";
				}
			}
				
			System.out.println(sqlStr);
			SearchDAO dao = new SearchDAOImpl();
			int type = getSearchType(this.getSc().getTableName());
			int first = Integer.parseInt(this.getSc().getOnceNum());
			int count = Integer.parseInt(this.getSc().getTotalNum());
			List datas = dao.SqlQueryAllCols(sqlStr, type, first, count);
			
			for( int i = 0; i<datas.size(); i++) {
				data = new Element("DATA");
				dataset.addContent(data);
				
				addDBResultItemToXML(data, datas.get(i), type);
			}
			

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
//			Format format = Format.getCompactFormat();
//			format.setEncoding("UTF-8");
			//format.setIndent("    ");
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
	
	private void itemSetAttribute(Element item, String key, String value) {
		item.setAttribute(key, value == null ? "" : value);
	}
	
	private void addDBResultItemToXML(Element data, Object model, int type) {
		Element item = null;
		if( type == SearchDAO.TYPEUSER ) {
			User user = (User)model;
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030014");
			itemSetAttribute(item, "val", user.getCERTIFICATE_CODE_MD5());
			itemSetAttribute(item, "rmk", "身份证哈希值");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030015");
			itemSetAttribute(item, "val", user.getCERTIFICATE_CODE_SUFFIX());
			itemSetAttribute(item, "rmk", "身份证后6位");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "B010001");
			itemSetAttribute(item, "val", user.getNAME());
			itemSetAttribute(item, "rmk", "姓名");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "B010011");
			itemSetAttribute(item, "val", user.getSEXCODE());
			itemSetAttribute(item, "rmk", "性别");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "A010001");
			itemSetAttribute(item, "val", user.getGA_DEPARTMENT());
			itemSetAttribute(item, "rmk", "公安组织机构代码");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "E010002");
			itemSetAttribute(item, "val", user.getUNIT());
			itemSetAttribute(item, "rmk", "公安组织机构名称");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I010052");
			itemSetAttribute(item, "val", user.getORG_LEVEL());
			itemSetAttribute(item, "rmk", "公安组织机构级别");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "A010003");
			itemSetAttribute(item, "val", user.getPOLICE_SORT());
			itemSetAttribute(item, "rmk", "警种");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "B010028");
			itemSetAttribute(item, "val", user.getPOLICE_NO());
			itemSetAttribute(item, "rmk", "警号");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "H010034");
			itemSetAttribute(item, "val", user.getSENSITIVE_LEVEL());
			itemSetAttribute(item, "rmk", "最高敏感级别");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I010031");
			itemSetAttribute(item, "val", user.getBUSINESS_TYPE());
			itemSetAttribute(item, "rmk", "业务部门类别");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "B030026");
			itemSetAttribute(item, "val", user.getTAKE_OFFICE());
			itemSetAttribute(item, "rmk", "职务/职称");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030016");
			itemSetAttribute(item, "val", ""+user.getUSER_STATUS());
			itemSetAttribute(item, "rmk", "用户状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "H010029");
			itemSetAttribute(item, "val", ""+user.getDELETE_STATUS());
			itemSetAttribute(item, "rmk", "删除状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030017");
			itemSetAttribute(item, "val", ""+user.getDATA_VERSION());
			itemSetAttribute(item, "rmk", "数据版本号");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I010005");
			itemSetAttribute(item, "val", ""+user.getLATEST_MOD_TIME());
			itemSetAttribute(item, "rmk", "最新修改时间");
			
		}
		else if( type == SearchDAO.TYPEORG ) {
			Organization org = (Organization)model;
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "A010001");
			itemSetAttribute(item, "val", org.getGA_DEPARTMENT());
			itemSetAttribute(item, "rmk", "公安组织机构代码");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "E010002");
			itemSetAttribute(item, "val", org.getUNIT());
			itemSetAttribute(item, "rmk", "公安组织机构名称");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I010052");
			itemSetAttribute(item, "val", org.getORG_LEVEL());
			itemSetAttribute(item, "rmk", "公安组织机构级别");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "A010005");
			itemSetAttribute(item, "val", org.getPARENT_ORG());
			itemSetAttribute(item, "rmk", "组织机构父节点(上级组织机构)");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "H010029");
			itemSetAttribute(item, "val", ""+org.getDELETE_STATUS());
			itemSetAttribute(item, "rmk", "删除状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030017");
			itemSetAttribute(item, "val", ""+org.getDATA_VERSION());
			itemSetAttribute(item, "rmk", "数据版本号");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I010005");
			itemSetAttribute(item, "val", ""+org.getLATEST_MOD_TIME());
			itemSetAttribute(item, "rmk", "最新修改时间");
		}
		else if( type == SearchDAO.TYPEROLE ) {
			ResRole role = (ResRole)model;
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I010026");
			itemSetAttribute(item, "val", role.getBUSINESS_ROLE());
			itemSetAttribute(item, "rmk", "角色编码");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I010025");
			itemSetAttribute(item, "val", ""+role.getBUSINESS_ROLE_TYPE());
			itemSetAttribute(item, "rmk", "角色类型");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I010054");
			itemSetAttribute(item, "val", role.getBUSINESS_ROLE_NAME());
			itemSetAttribute(item, "rmk", "角色名称");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "B050016");
			itemSetAttribute(item, "val", role.getSYSTEM_TYPE());
			itemSetAttribute(item, "rmk", "系统类型");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "H010029");
			itemSetAttribute(item, "val", ""+role.getDELETE_STATUS());
			itemSetAttribute(item, "rmk", "删除状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030017");
			itemSetAttribute(item, "val", ""+role.getDATA_VERSION());
			itemSetAttribute(item, "rmk", "数据版本号");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I010005");
			itemSetAttribute(item, "val", ""+role.getLATEST_MOD_TIME());
			itemSetAttribute(item, "rmk", "最新修改时间");
			
		}
		else if( type == SearchDAO.TYPERESDATA ) {
			ResData resData = (ResData)model;
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030006");
			itemSetAttribute(item, "val", resData.getRESOURCE_ID());
			itemSetAttribute(item, "rmk", "资源唯一标识");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030010");
			itemSetAttribute(item, "val", ""+resData.getRESOURCE_STATUS());
			itemSetAttribute(item, "rmk", "资源状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030012");
			itemSetAttribute(item, "val", resData.getRESOURCE_DESCRIBE());
			itemSetAttribute(item, "rmk", "资源描述");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030003");
			itemSetAttribute(item, "val", resData.getDATASET_SENSITIVE_LEVEL());
			itemSetAttribute(item, "rmk", "数据集敏感度编码（表控）");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "A010004");
			itemSetAttribute(item, "val", resData.getDATA_SET());
			itemSetAttribute(item, "rmk", "数据集编码（表控）");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030001");
			itemSetAttribute(item, "val", resData.getSECTION_CLASS());
			itemSetAttribute(item, "rmk", "字段分类编码（列控）");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030004");
			itemSetAttribute(item, "val", resData.getELEMENT());
			itemSetAttribute(item, "rmk", "字段编码（原目标字段列控）");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030002");
			itemSetAttribute(item, "val", resData.getSECTION_RELATIOIN_CLASS());
			itemSetAttribute(item, "rmk", "字段分类关系编码（列控）");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030021");
			itemSetAttribute(item, "val", resData.getOPERATE_SYMBOL());
			itemSetAttribute(item, "rmk", "操作符");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030005");
			itemSetAttribute(item, "val", resData.getELEMENT_VALUE());
			itemSetAttribute(item, "rmk", "字段值");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030013");
			itemSetAttribute(item, "val", resData.getRESOURCE_REMARK());
			itemSetAttribute(item, "rmk", "备注");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "H010029");
			itemSetAttribute(item, "val", ""+resData.getDELETE_STATUS());
			itemSetAttribute(item, "rmk", "删除状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "J030017");
			itemSetAttribute(item, "val", ""+resData.getDATA_VERSION());
			itemSetAttribute(item, "rmk", "数据版本号");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "I010005");
			itemSetAttribute(item, "val", ""+resData.getLATEST_MOD_TIME());
			itemSetAttribute(item, "rmk", "最新修改时间");
		}
		

	}
	
	private int getSearchType(String tableName) {
		int type = -1;
		if( "WA_AUTHORITY_POLICE".equals(tableName) ) {
			type = SearchDAO.TYPEUSER;
		}
		else if ( "WA_AUTHORITY_ORGNIZATION".equals(tableName) ) {
			type = SearchDAO.TYPEORG;
		}
		else if ("WA_AUTHORITY_ROLE".equals(tableName) ) {
			type = SearchDAO.TYPEROLE;
		}
		else if ("WA_AUTHORITY_RESOURCE".equals(tableName) ) {
			type = SearchDAO.TYPERESDATA;
		}
		return type;
	}
	
	private Element getDataSet(Element root, String name) {
		List<Element> items = root.getChildren();
		Element result = null;
		for(int i = 0; i<items.size(); i++){
			Element tmp = items.get(i);
			if("WA_COMMON_010004".equals( tmp.getAttributeValue("name") )) {
				result = tmp;
				break;
			}
		}
		return result;
	}
	
	private void setError(Element dataset, String errorCode, String errorMsg) {
		List<Element> items = dataset.getChild("DATA").getChildren("ITEM");
		for(int i = 0; i<items.size(); i++){
			Element tmp = items.get(i);
			if("I030010".equals( tmp.getAttributeValue("key") )) {
				tmp.setAttribute("val", errorCode);
			}
			else if("I010009".equals( tmp.getAttributeValue("key") )) {
				tmp.setAttribute("val", errorMsg);
			}
		}
	}

}
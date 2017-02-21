package com.pms.webservice.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.pms.model.Organization;
import com.pms.model.ResData;
import com.pms.model.ResFeature;
import com.pms.model.ResRole;
import com.pms.model.ResRoleResource;
import com.pms.model.User;
import com.pms.model.UserRole;
import com.pms.util.DateTimeUtil;
import com.pms.webservice.dao.SearchDAO;
import com.pms.webservice.dao.impl.SearchDAOImpl;
import com.pms.webservice.model.Condition;
import com.pms.webservice.model.Item;
import com.pms.webservice.model.search.Common_010121;
import com.pms.webservice.model.search.Common_010123;
import com.pms.webservice.model.search.NestConditions;

public class SyncComplexSearchService extends SyncService {
	private static Log logger = LogFactory.getLog(SyncComplexSearchService.class);
	
	@SuppressWarnings("rawtypes")
	@Override
	public String GetResult() throws IOException {
		String result = null;
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
			itemSetAttribute(item, "val", "" + new Date().getTime()/1000);
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
			
			if(this.getCsc() == null) {
				setError(getDataSet(message, "WA_COMMON_010004"), "-10001", "查询条件不存在");
				throw new Exception("search condition data error");
			}
			
			String sqlStr = "select distinct ";
			if( this.getCsc().getReturnColumns() == null || this.getCsc().getReturnColumns().size() == 0 ) {
				sqlStr += "* ";
			}
			else {
				// deal with return columns when generating xml result
				sqlStr += "* ";
			}
			
			sqlStr += " from ";
			
			if( this.getCsc().getTableNameJ010002() == null || this.getCsc().getTableNameJ010002().length() == 0) {
				setError(getDataSet(message, "WA_COMMON_010004"), "-10002", "查询条件不正确，表名不存在");
				throw new Exception("search condition data error");
			}
			else {
				sqlStr += this.getCsc().getTableNameJ010002() + " ";
//				if( this.getCsc().getTableAlias() != null ) {
//					sqlStr += getCsc().getTableAlias() + " ";
//				}
			}
			
			// parse join clause and it's alias
			if( this.getCsc().getCommon010123() != null && this.getCsc().getCommon010123().size() > 0 ) {
				try{
					sqlStr += addJoinConditionToSQL();
				}
				catch(Exception e) {
					setError(getDataSet(message, "WA_COMMON_010004"), "-10004", "查询条件不正确，join条件解析失败。");
					throw e;
				}
			}
			
			// parse where clause
			if( this.getCsc().getConditions() != null && this.getCsc().getConditions().getCondition() != null ) {
//				Condition condition = getCsc().getConditions().getCondition();
//				sqlStr += " where (" + parseCondition( condition ) + ") ";
//				if( getCsc().getConditions().getConditions() != null ) {
//					for( int i = 0; i < getCsc().getConditions().getConditions().size(); i++ ) {
//						condition = getCsc().getConditions().getConditions().get(i);
//						sqlStr += " " + getCsc().getConditions().getCondition().getRel() + " (" + parseCondition( condition ) + ") ";
//					}
//				}
				sqlStr += " where (" + parseConditionsObject( getCsc().getConditions() ) + ") ";
			}

			// parse group clause
			List<Item> items = getCsc().getGroupColumns();
			if( items != null && items.size() > 0 ) {
				Item groupItem = items.get(0);
				sqlStr += " group by " + groupItem.getEng() + " ";
				for( int i = 1; i < items.size(); i++ ) {
					groupItem = items.get(i);
					sqlStr += ", " + groupItem.getEng() + " ";
				}
			}
			
			// parse order clause
			try{
				items = getCsc().getOrderColumns();
				if( items != null && items.size() > 0 ) {
					Item orderItem = items.get(0);
					String order = parseOrder(orderItem.getSort());
					sqlStr += " order by " + orderItem.getEng() + " " + order;
					for( int i = 1; i < items.size(); i++ ) {
						orderItem = items.get(i);
						order = parseOrder(orderItem.getSort());
						sqlStr += ", " + orderItem.getEng() + " " + order;
					}
				}
			}
			catch(Exception e) {
				setError(getDataSet(message, "WA_COMMON_010004"), "-10003", "查询条件不正确，排序条件解析失败。");
				throw e;
			}
			
			logger.info(sqlStr);
			
			SearchDAO dao = new SearchDAOImpl();
			int type = getSearchType(this.getCsc().getTableNameJ010002());
//			int first = Integer.parseInt(this.getCsc().getOnceNum());
			//int total = Integer.parseInt(this.getCsc().getTotalNum() == null ? "0" : this.getCsc().getTotalNum());
			int totalNum = this.getCsc().getSearchResultMaxNumI010017();
			int onceNum = this.getCsc().getSearchResultCountI010019();
			int total = Math.min(totalNum, onceNum);
			List datas = null;
			datas = dao.SqlQueryAllCols(sqlStr, type, 0, total);

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
			logger.info(result);
		}
		return result;
	}
	
	private String parseOrder(String sort) throws Exception {
		String order = "";
		if( sort == null || Item.SORTTYPEASC.equalsIgnoreCase(sort)) {
			order = "";
		}
		else if ( Item.SORTTYPEDESC.equalsIgnoreCase(sort) ) {
			order = Item.SORTTYPEDESC;
		}
		else {
			throw new Exception("parse order condition error.");
		}
		return order;
	}

	private String parseConditionsObject(NestConditions conditions) throws Exception {
		String rel = conditions.getCondition().getRel();
		List<NestConditions> subConditions = conditions.getSubConditions();
		String resultTemp = "";
		List<String> parts = new ArrayList<String>();
		if( subConditions != null && !"NOT".equalsIgnoreCase(rel)) {
			for( NestConditions sub : subConditions ) {
				resultTemp = parseConditionsObject(sub);
				if(resultTemp != null && resultTemp.length() > 0) {
					parts.add(resultTemp);
				}
			}
		}
		
		if( "NOT".equals(rel) ) {
			if(subConditions != null && subConditions.size() > 0) {
				if( subConditions.size() != 1 ) {
					throw new Exception("parse where condition(NOT with subconditions'size > 1) error.");
				}
				NestConditions innerCondition = subConditions.get(0);
				if( innerCondition.getCondition() == null
						|| innerCondition.getCondition().getItems() == null
						|| innerCondition.getCondition().getItems().size() != 1 ) {
					throw new Exception("parse where condition(NOT with size of subconditions'items != 1) error.");
				}
				String innerRel = innerCondition.getCondition().getRel();
				Item innerItem = innerCondition.getCondition().getItems().get(0);
				if( "NULL".equalsIgnoreCase(innerRel) ) {
					resultTemp = "(" + innerItem.getEng() + " is not NULL ) ";
				}
				else if ( "IN".equalsIgnoreCase(innerRel) ) {
					String values[] = innerItem.getVal().split(",");
					resultTemp = "(" + innerItem.getEng() + " not in ('" + values[0] + "'";
					for( int j = 1; j < values.length; j++ ){
						resultTemp += " , '" + values[j] + "'"; 
					}
					resultTemp += ") ) ";
				}
				else {
					throw new Exception("parse where condition(NOT without IN or NULL) error.");
				}
				return resultTemp;
			}
		}
		
		List<Item> items = conditions.getCondition().getItems();
		for( int i = 0; i < items.size(); i++ ) {
			Item item = items.get(i);
			if( "NOT".equals(rel) ) {
				if(subConditions != null && subConditions.size() > 0) {
					throw new Exception("parse where condition(NOT with items and subconditions) error.");
				}
				resultTemp = "(" + item.getEng() + " != '" + item.getVal() + "') ";
			}
			else if ( "BTW".equals(rel) && i == 0 ) {
				if( items != null && items.size() == 2 ) {
					String min = items.get(0).getVal();
					String max = items.get(1).getVal();
//					String min = val1.compareTo(val2) >= 0 ? val2 : val1;
//					String max = val1.compareTo(val2) >= 0 ? val1 : val2;
					if( items.get(0).getKey().contains("I010005")) {
						try{
							min = DateTimeUtil.GetTimeStr( Long.parseLong(min) );
							max = DateTimeUtil.GetTimeStr( Long.parseLong(max) );
						}
						catch(Exception e) {
							//val = val;
						}
					}
					resultTemp = "(" + items.get(0).getEng() + " between '" + min + "' and '" + max + "') ";
				}
				else {
					throw new Exception("parse where condition(BTW) error.");
				}
			}
			else if ( "AND".equalsIgnoreCase(rel) 
					|| "OR".equalsIgnoreCase(rel) ) {
				resultTemp = "(" + item.getEng() + " = '" + item.getVal() + "') ";
			}
			else if ( "PBR".equalsIgnoreCase(rel) ) {
				resultTemp = "(" + item.getEng() + " like '%" + item.getVal() +"') ";
			}
			else if ( "EBR".equalsIgnoreCase(rel) ) {
				resultTemp = "(" + item.getEng() + " like '" + item.getVal() +"%') ";
			}
			else if ( "BBR".equalsIgnoreCase(rel) ) {
				resultTemp = "(" + item.getEng() + " like '%" + item.getVal() +"%') ";
			}
			else if ( "GT".equalsIgnoreCase(rel) ) {
				resultTemp = "(" + item.getEng() + " > '" + item.getVal() +"') ";
			}
			else if ( "LT".equalsIgnoreCase(rel) ) {
				resultTemp = "(" + item.getEng() + " < '" + item.getVal() +"') ";
			}
			else if ( "GE".equalsIgnoreCase(rel) ) {
				String val = item.getVal();
				if( item.getKey().contains("I010005") ) {
					try{
						Long longTime = Long.parseLong(val) * 1000;
						val = DateTimeUtil.GetTimeStr(longTime);
					}
					catch(Exception e) {
						logger.debug("parse search condition LATEST_MOD_TIME error,[time:" + item.getVal() + "]");
					}
					
				}
				resultTemp = "(" + item.getEng() + " >= '" + val +"') ";
			}
			else if ( "LE".equalsIgnoreCase(rel) ) {
				resultTemp = "(" + item.getEng() + " <= '" + item.getVal() +"') ";
			}
			else if ( "IN".equalsIgnoreCase(rel) ) {
				if( items != null && items.size() == 1 ) {
					String values[] = item.getVal().split(",");
					resultTemp = "(" + item.getEng() + " in ('" + values[0] + "'";
					for( int j = 1; j < values.length; j++ ){
						resultTemp += " , '" + values[j] + "'"; 
					}
					resultTemp += ") ) ";
				}
				else {
					throw new Exception("parse where condition(IN) error.");
				}
			}
			else if ( "NULL".equalsIgnoreCase(rel) ) {
				if( items != null && items.size() == 1 ) {
					resultTemp = "(" + items.get(0).getEng() + " is null) ";
				}
				else {
					throw new Exception("parse where condition(NULL) error.");
				}
			}
			parts.add(resultTemp);
		}
		
		// build result from parts
		String result = "";
		if( parts.size() > 0 ) {
			result = parts.get(0);
			for(int i = 1; i < parts.size(); i++) {
				if( "AND".equalsIgnoreCase(rel) ) {
					result += " and " + parts.get(i);
				}
				else if( "OR".equalsIgnoreCase(rel) ) {
					result += " or " + parts.get(i);
				}
				else {
					throw new Exception("parse where condition(muti-parts without AND or OR) error.");
				}
			}
		}
		return result;
	}
	
//	private String parseCondition( Condition condition ) throws Exception
//	{
//		String result = "";
//		List<Item> items = condition.getItems();
//		if ( "AND".equalsIgnoreCase(condition.getRel()) ) {
//			if(items != null && items.size()>0) {
//				result += items.get(0).getEng() + " = '" + items.get(0).getVal() + "'";
//				for( int i = 1; i < items.size(); i++ ) {
//					result += " and " + items.get(i).getEng() + " = '" + items.get(i).getVal() + "'";
//				}
//				result += " ";
////				result = "(" + result + ") ";
//			}
//		}
//		else if ( "IN".equalsIgnoreCase(condition.getRel()) ) {
//			if( items != null && items.size() == 1 ) {
//				String values[] = items.get(0).getVal().split(",");
//				result = items.get(0).getEng() + " in ('" + values[0] + "'";
//				for( int i = 1; i < values.length; i++ ){
//					result += " , '" + values[i] + "'"; 
//				}
//				result += ") ";
//			}
//			else {
//				throw new Exception("parse where condition(IN) error.");
//			}
//		}
//		else if ( "NOT IN".equalsIgnoreCase(condition.getRel()) ) {
//			if( items != null && items.size() == 1 ) {
//				String values[] = items.get(0).getVal().split(",");
//				result = items.get(0).getEng() + " not in ('" + values[0] + "'";
//				for( int i = 1; i < values.length; i++ ){
//					result += " , '" + values[i] + "'"; 
//				}
//				result += ") ";
//			}
//			else {
//				throw new Exception("parse where condition(NOT IN) error.");
//			}
//		}
//		else if ( "NULL".equalsIgnoreCase(condition.getRel()) ) {
//			if( items != null && items.size() == 1 ) {
//				result = items.get(0).getEng() + " is null ";
//			}
//			else {
//				throw new Exception("parse where condition(NULL) error.");
//			}
//		}
//		else if ( "NOT NULL".equalsIgnoreCase(condition.getRel()) ) {
//			if( items != null && items.size() == 1 ) {
//				result = items.get(0).getEng() + " is not null ";
//			}
//			else {
//				throw new Exception("parse where condition(NOT NULL) error.");
//			}
//		}
//		else if ( "NOT".equalsIgnoreCase(condition.getRel()) ) {
//			if( items != null && items.size() == 1 ) {
//				String val = items.get(0).getVal();
//				result = items.get(0).getEng() + " != '" + val + "' ";
//			}
//			else {
//				throw new Exception("parse where condition(NOT NULL) error.");
//			}
//		}
//		else if ( "GE".equalsIgnoreCase(condition.getRel()) ) {
//			if( items != null && items.size() == 1 ) {
//				String val = items.get(0).getVal();
//				if( items.get(0).getKey().contains("I010005")) {
//					try{
//						val = DateTimeUtil.GetTimeStr( Long.parseLong(val) );
//					}
//					catch(Exception e) {
//						//val = val;
//					}
//				}
//				result = items.get(0).getEng() + " >= '" + val + "'";
//			}
//			else {
//				throw new Exception("parse where condition(GE) error.");
//			}
//		}
//		else if ( "BTW".equalsIgnoreCase(condition.getRel()) ) {
//			if( items != null && items.size() == 2 ) {
//				String val1 = items.get(0).getVal();
//				String val2 = items.get(1).getVal();
//				String min = val1.compareTo(val2) >= 0 ? val2 : val1;
//				String max = val1.compareTo(val2) >= 0 ? val1 : val2;
//				if( items.get(0).getKey().contains("I010005")) {
//					try{
//						min = DateTimeUtil.GetTimeStr( Long.parseLong(min) );
//						max = DateTimeUtil.GetTimeStr( Long.parseLong(max) );
//					}
//					catch(Exception e) {
//						//val = val;
//					}
//				}
//				result = items.get(0).getEng() + " between '" + min + "' and '" + max + "' ";
//			}
//			else {
//				throw new Exception("parse where condition(BTW) error.");
//			}
//		}
//		return result;
//	}
	
	private String addJoinConditionToSQL() throws Exception {
		String join = "";
		
		if( this.getCsc().getCommon010123() != null && this.getCsc().getCommon010123().size() > 0 ) {
			//1 join case
			// 1.1 deal with join part
			for(Common_010123 common010123 : getCsc().getCommon010123() ) {
				if( Common_010123.JOINTYPEINNER == common010123.getJoinTypeJ010007() ) {
					join += " inner join ";
				}
				else if( Common_010123.JOINTYPELEFT == common010123.getJoinTypeJ010007() ) {
					join += " left join ";
				}
				else if( Common_010123.JOINTYPERIGHT == common010123.getJoinTypeJ010007() ) {
					join += " right join ";
				}
				else {
					throw new Exception("unknow join flag");
				}
				
				if(common010123.getTableNameJ010002() == null || common010123.getTableNameJ010002().isEmpty()) {
					throw new Exception("missing table name parameter");
				}
				else {
					join += common010123.getTableNameJ010002() + " ";
				}
				
				join += " on ";
								
				Condition joinConditions = common010123.getSearch();
				if( joinConditions.getItems().size() > 0 ) {
					Item item = joinConditions.getItems().get(0);
					//where += item.getEng() + "=" + item.getVal() + " ";
					String destColumn = item.getEng().contains(".") ? item.getEng().substring(item.getEng().indexOf(".") + 1) : item.getEng();
					// if join table only contain one column, the request doesn't contain column name, we need add column name after table name.
					if( item.getVal().contains(".") ) {
						join += item.getEng() + "=" + item.getVal() + " ";
					}
					else {
						
						join += item.getEng() + "=" + item.getVal() + "." + destColumn + " ";
					}
					for( int i = 1; i < joinConditions.getItems().size(); i++ ) {
						item = joinConditions.getItems().get(i);
						//where += " " + joinConditions.getRel() + " " + item.getEng() + "=" + item.getVal() + " ";
						if( item.getVal().contains(".") ) { 
							join += " " + joinConditions.getRel() + " " + item.getEng() + "=" + item.getVal() + " ";
						}
						else {
							join += " " + joinConditions.getRel() + " " + item.getEng() + "=" + item.getVal() + "." + destColumn + " ";
						}
					}
				}
			}
			
			// 1.2 deal with alias part which in the join clause
			if( this.getCsc().getCommon010121() != null && this.getCsc().getCommon010121().size() > 0 ) {
				for(Common_010121 common010121 : getCsc().getCommon010121() ) {
					// 1.2.1 : [[( select distinct columns]]
					String aliasSearch = " ( select distinct ";
					List<Item> retCols = common010121.getReturnColumns();
					if( retCols == null || retCols.size() == 0 ) {
						aliasSearch += " * ";
					}
					else {
						aliasSearch += " " + retCols.get(0).getEng();
						for( int i = 1; i < retCols.size(); i++ ) {
							aliasSearch += ", " + retCols.get(i).getEng();
						}
						aliasSearch += " ";
					}
					
					// 1.2.2 : ( select distinct columns [[from]]
					aliasSearch += " from ";
					
					// 1.2.3 : ( select distinct columns from [[table]] 
					if( common010121.getTableNameJ010002() == null || common010121.getTableNameJ010002().length() == 0 ) {
						logger.info("查询条件不正确，表名不存在");
						throw new Exception("search condition data error");
					}
					else {
						aliasSearch += common010121.getTableNameJ010002() + " ";
					}
					
					// 1.2.4 :  ( select distinct columns from table [[join clause]]
					if( common010121.getCommon010123() != null ) {
						for(Common_010123 common010123 : common010121.getCommon010123() ) {
							if( Common_010123.JOINTYPEINNER == common010123.getJoinTypeJ010007() ) {
								aliasSearch += " inner join ";
							}
							else if( Common_010123.JOINTYPELEFT == common010123.getJoinTypeJ010007() ) {
								aliasSearch += " left join ";
							}
							else if( Common_010123.JOINTYPERIGHT == common010123.getJoinTypeJ010007() ) {
								aliasSearch += " right join ";
							}
							else {
								throw new Exception("unknow join flag");
							}
							
							if(common010123.getTableNameJ010002() == null || common010123.getTableNameJ010002().isEmpty()) {
								throw new Exception("missing table name parameter");
							}
							else {
								aliasSearch += common010123.getTableNameJ010002() + " ";
							}
							
							aliasSearch += " on ";
											
							Condition joinConditions = common010123.getSearch();
							if( joinConditions.getItems().size() > 0 ) {
								Item item = joinConditions.getItems().get(0);
								//where += item.getEng() + "=" + item.getVal() + " ";
								String destColumn = item.getEng().contains(".") ? item.getEng().substring(item.getEng().indexOf(".") + 1) : item.getEng();
								// if join table only contain one column, the request doesn't contain column name, we need add column name after table name.
								if( item.getVal().contains(".") ) {
									aliasSearch += item.getEng() + "=" + item.getVal() + " ";
								}
								else {
									
									aliasSearch += item.getEng() + "=" + item.getVal() + "." + destColumn + " ";
								}
								for( int i = 1; i < joinConditions.getItems().size(); i++ ) {
									item = joinConditions.getItems().get(i);
									//where += " " + joinConditions.getRel() + " " + item.getEng() + "=" + item.getVal() + " ";
									if( item.getVal().contains(".") ) { 
										aliasSearch += " " + joinConditions.getRel() + " " + item.getEng() + "=" + item.getVal() + " ";
									}
									else {
										aliasSearch += " " + joinConditions.getRel() + " " + item.getEng() + "=" + item.getVal() + "." + destColumn + " ";
									}
								}
							}
							else {
								throw new Exception("missing join condition parameter");
							}
						}
					}
					
					
					// 1.2.5 : ( select distinct columns from table join clause [[where clause]]
					if( common010121.getConditions() != null && common010121.getConditions().getCondition() != null ) {
//						Condition condition = common010121.getConditions().getCondition();
//						aliasSearch += " where (" + parseCondition( condition ) + ") ";
//						if( common010121.getConditions().getConditions() != null ) {
//							for( int i = 0; i < common010121.getConditions().getConditions().size(); i++ ) {
//								condition = common010121.getConditions().getConditions().get(i);
//								aliasSearch += " " + common010121.getConditions().getCondition().getRel() + " (" + parseCondition( condition ) + ") ";
//							}
//						}
						aliasSearch += " where (" + parseConditionsObject( common010121.getConditions() ) + ") ";
					}

					// 1.2.6 : ( select distinct columns from table join clause where clause ) alias 
					aliasSearch += " ) " + common010121.getTableAliasJ010015() + " ";
					
					// 1.2.6 : put aliasSearch to join clause
//					if( where.contains( "'" + common010121.getAlias() + "'" ) ) {
//						where.replace("'" + common010121.getAlias() + "'", aliasSearch);
//					}
					if( join.contains( common010121.getTableAliasJ010015() ) ) {
						join = join.replaceAll("(^.*?)" + common010121.getTableAliasJ010015() + "(.*$)", "$1" + aliasSearch + "$2");
					}
				}
			}
			
//			// 1.3 deal with where part
//			List<Item> whereItem = getCsc().getCONDITIONITEMS();
//			if( whereItem != null && whereItem.size() > 0 ) {
//				where += " where " + whereItem.get(0).getEng() + " = " + whereItem.get(0).getVal() + " ";
//				for(int i = 1; i< whereItem.size(); i++) {
//					where += " " + getCsc().getCONDITION() + " " + whereItem.get(i).getEng() + " = " + whereItem.get(i).getVal() + " ";
//				}
//			}
		}
		
		return join;
	}
	
//	@SuppressWarnings("rawtypes")
//	private List queryOrgChildrenList(String orgId) throws Exception {
//		OrganizationDAO dao = new OrganizationDAOImpl();
//		Organization base = dao.GetOrgNodeById(orgId);
//		List<Organization> res = dao.GetOrgNodeByParentId(orgId);
//		res.add(0, base);
//		return res;
//	}
//	
//	private String matchSubSearchWithInCondition(Map<String, String> subMap, String columnValue) {
//		String result = null;
//		if( subMap.containsKey(columnValue) ) {
//			result = subMap.get(columnValue);
//		} else {
//			result = columnValue;
//		}
//		return result;	
//	}
//	
//	private String matchSubSearch(Map<String, String> subMap, String columnValue) {
//		String result = null;
//		if( subMap.containsKey(columnValue) ) {
//			result = " ( " + subMap.get(columnValue) + " ) ";
//		} else {
//			result = " '" + columnValue + "' ";
//		}
//		return result;	
//	}
	
	private boolean isReturnColumn(String columnKey) {
		List<Item> columns = getCsc().getReturnColumns();
		boolean isReturn = false;
		if(columns == null || columns.size() == 0) {
			return true;
		}
		
		for(Item column : columns) {
			if( column.getKey().contains( columnKey) ) {
				isReturn = true;
				break;
			}
		}
		return isReturn;
	}
	
	private void addDBResultItemToXML(Element data, Object model, int type) {
		Element item = null;
		
		if( type == SearchDAO.TYPEUSER ) {
			User user = (User)model;
			
			if( isReturnColumn("J030014")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.J030014");
				itemSetAttribute(item, "val", user.getCERTIFICATE_CODE_MD5());
				itemSetAttribute(item, "rmk", "身份证哈希值");
			}
			
			if( isReturnColumn("J030015")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.J030015");
				itemSetAttribute(item, "val", user.getCERTIFICATE_CODE_SUFFIX());
				itemSetAttribute(item, "rmk", "身份证后6位");
			}
			
			if( isReturnColumn("B010001")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.B010001");
				itemSetAttribute(item, "val", user.getNAME());
				itemSetAttribute(item, "rmk", "姓名");
			}
			
			if( isReturnColumn("B010011")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.B010011");
				itemSetAttribute(item, "val", user.getSEXCODE());
				itemSetAttribute(item, "rmk", "性别");
			}
			
			if( isReturnColumn("A010001")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.A010001");
				itemSetAttribute(item, "val", user.getGA_DEPARTMENT());
				itemSetAttribute(item, "rmk", "公安组织机构代码");
			}
			
			if( isReturnColumn("E010002")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.E010002");
				itemSetAttribute(item, "val", user.getUNIT());
				itemSetAttribute(item, "rmk", "公安组织机构名称");
			}
			
			if( isReturnColumn("I010052")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.I010052");
				itemSetAttribute(item, "val", ""+user.getORG_LEVEL());
				itemSetAttribute(item, "rmk", "公安组织机构级别");
			}
			
			if( isReturnColumn("A010003")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.A010003");
				itemSetAttribute(item, "val", user.getPOLICE_SORT());
				itemSetAttribute(item, "rmk", "警种");
			}
			
			if( isReturnColumn("B010028")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.B010028");
				itemSetAttribute(item, "val", user.getPOLICE_NO());
				itemSetAttribute(item, "rmk", "警号");
			}
			
			if( isReturnColumn("H010034")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.H010034");
				itemSetAttribute(item, "val", user.getSENSITIVE_LEVEL());
				itemSetAttribute(item, "rmk", "最高敏感级别");
			}
			
			if( isReturnColumn("I010031")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.I010031");
				itemSetAttribute(item, "val", user.getBUSINESS_TYPE());
				itemSetAttribute(item, "rmk", "业务部门类别");
			}
			
			if( isReturnColumn("B030026")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.B030026");
				itemSetAttribute(item, "val", user.getTAKE_OFFICE());
				itemSetAttribute(item, "rmk", "职务/职称");
			}
			
			if( isReturnColumn("J030016")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.J030016");
				itemSetAttribute(item, "val", ""+user.getUSER_STATUS());
				itemSetAttribute(item, "rmk", "用户状态");
			}
			
			if( isReturnColumn("H010029")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.H010029");
				itemSetAttribute(item, "val", ""+user.getDELETE_STATUS());
				itemSetAttribute(item, "rmk", "删除状态");
			}
			
			if( isReturnColumn("J030017")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.J030017");
				itemSetAttribute(item, "val", ""+user.getDATA_VERSION());
				itemSetAttribute(item, "rmk", "数据版本号");
			}
			
			if( isReturnColumn("I010005")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.I010005");
				itemSetAttribute(item, "val", ""+ getLongTime(user.getLATEST_MOD_TIME()) );
				itemSetAttribute(item, "rmk", "最新修改时间");
			}
		}
		else if( type == SearchDAO.TYPEORG ) {
			Organization org = (Organization)model;
			
			if( isReturnColumn("A010001")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_ORGNIZATION.A010001");
				itemSetAttribute(item, "val", org.getGA_DEPARTMENT());
				itemSetAttribute(item, "rmk", "公安组织机构代码");
			}
			
			if( isReturnColumn("E010002")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_ORGNIZATION.E010002");
				itemSetAttribute(item, "val", org.getUNIT());
				itemSetAttribute(item, "rmk", "公安组织机构名称");
			}
			
			if( isReturnColumn("I010052")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_ORGNIZATION.I010052");
				itemSetAttribute(item, "val", "" + org.getORG_LEVEL());
				itemSetAttribute(item, "rmk", "公安组织机构级别");
			}
			
			if( isReturnColumn("A010005")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_ORGNIZATION.A010005");
				itemSetAttribute(item, "val", org.getPARENT_ORG());
				itemSetAttribute(item, "rmk", "组织机构父节点(上级组织机构)");
			}
			
			if( isReturnColumn("H010029")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_ORGNIZATION.H010029");
				itemSetAttribute(item, "val", ""+org.getDELETE_STATUS());
				itemSetAttribute(item, "rmk", "删除状态");
			}
			
			if( isReturnColumn("J030017")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_ORGNIZATION.J030017");
				itemSetAttribute(item, "val", ""+org.getDATA_VERSION());
				itemSetAttribute(item, "rmk", "数据版本号");
			}
			
			if( isReturnColumn("I010005")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_ORGNIZATION.I010005");
				itemSetAttribute(item, "val", ""+ getLongTime(org.getLATEST_MOD_TIME()) );
				itemSetAttribute(item, "rmk", "最新修改时间");
			}
		}
		else if( type == SearchDAO.TYPEROLE ) {
			ResRole role = (ResRole)model;
		
			if( isReturnColumn("I010026")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_ROLE.I010026");
				itemSetAttribute(item, "val", role.getBUSINESS_ROLE());
				itemSetAttribute(item, "rmk", "角色编码");
			}
			
			if( isReturnColumn("I010025")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_ROLE.I010025");
				itemSetAttribute(item, "val", ""+role.getBUSINESS_ROLE_TYPE());
				itemSetAttribute(item, "rmk", "角色类型");
			}
			
			if( isReturnColumn("I010054")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_ROLE.I010054");
				itemSetAttribute(item, "val", role.getBUSINESS_ROLE_NAME());
				itemSetAttribute(item, "rmk", "角色名称");
			}
			
			if( isReturnColumn("B050016")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_ROLE.B050016");
				itemSetAttribute(item, "val", role.getSYSTEM_TYPE());
				itemSetAttribute(item, "rmk", "系统类型");
			}
			
			if( isReturnColumn("H010029")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_ROLE.H010029");
				itemSetAttribute(item, "val", ""+role.getDELETE_STATUS());
				itemSetAttribute(item, "rmk", "删除状态");
			}
			
			if( isReturnColumn("J030017")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_ROLE.J030017");
				itemSetAttribute(item, "val", ""+role.getDATA_VERSION());
				itemSetAttribute(item, "rmk", "数据版本号");
			}
			
			if( isReturnColumn("I010005")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_ROLE.I010005");
				itemSetAttribute(item, "val", ""+ getLongTime(role.getLATEST_MOD_TIME()) );
				itemSetAttribute(item, "rmk", "最新修改时间");
			}
			
		}
		else if( type == SearchDAO.TYPERESDATA ) {
			ResData resData = (ResData)model;
		
			if( isReturnColumn("J030006")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030006");
				itemSetAttribute(item, "val", resData.getRESOURCE_ID());
				itemSetAttribute(item, "rmk", "资源唯一标识");
			}
			
			if( isReturnColumn("J030029")) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030029");
				itemSetAttribute(item, "val", ""+resData.getResource_type());
				itemSetAttribute(item, "rmk", "资源类型");
			}
			
			if( isReturnColumn("J030010")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030010");
				itemSetAttribute(item, "val", ""+resData.getRESOURCE_STATUS());
				itemSetAttribute(item, "rmk", "资源状态");
			}
			
			if( isReturnColumn("J030012")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030012");
				itemSetAttribute(item, "val", resData.getRESOURCE_DESCRIBE());
				itemSetAttribute(item, "rmk", "资源描述");
			}
			
			if( isReturnColumn("J030003")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030003");
				itemSetAttribute(item, "val", resData.getDATASET_SENSITIVE_LEVEL());
				itemSetAttribute(item, "rmk", "数据集敏感度编码（表控）");
			}
			
			if( isReturnColumn("A010004")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.A010004");
				itemSetAttribute(item, "val", resData.getDATA_SET());
				itemSetAttribute(item, "rmk", "数据集编码（表控）");
			}
			
			if( isReturnColumn("J030001")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030001");
				itemSetAttribute(item, "val", resData.getSECTION_CLASS());
				itemSetAttribute(item, "rmk", "字段分类编码（列控）");
			}
			
			if( isReturnColumn("J030004")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030004");
				itemSetAttribute(item, "val", resData.getELEMENT());
				itemSetAttribute(item, "rmk", "字段编码（原目标字段列控）");
			}
			
			if( isReturnColumn("J030002")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030002");
				itemSetAttribute(item, "val", resData.getSECTION_RELATIOIN_CLASS());
				itemSetAttribute(item, "rmk", "字段分类关系编码（列控）");
			}
			
			if( isReturnColumn("J030021")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030021");
				itemSetAttribute(item, "val", resData.getOPERATE_SYMBOL());
				itemSetAttribute(item, "rmk", "操作符");
			}
			
			if( isReturnColumn("J030005")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030005");
				itemSetAttribute(item, "val", resData.getELEMENT_VALUE());
				itemSetAttribute(item, "rmk", "字段值");
			}
			
			if( isReturnColumn("J030013")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030013");
				itemSetAttribute(item, "val", resData.getRMK());
				itemSetAttribute(item, "rmk", "备注");
			}
			
			if( isReturnColumn("H010029")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.H010029");
				itemSetAttribute(item, "val", ""+resData.getDELETE_STATUS());
				itemSetAttribute(item, "rmk", "删除状态");
			}
			
			if( isReturnColumn("J030017")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030017");
				itemSetAttribute(item, "val", ""+resData.getDATA_VERSION());
				itemSetAttribute(item, "rmk", "数据版本号");
			}
			
			if( isReturnColumn("I010005")) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.I010005");
				itemSetAttribute(item, "val", ""+ getLongTime(resData.getLATEST_MOD_TIME()) );
				itemSetAttribute(item, "rmk", "最新修改时间");
			}
		}
		else if( type == SearchDAO.TYPERESFUN ) {
			ResFeature resFeature = (ResFeature)model;
		
			if( isReturnColumn("J020012") ) {
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J020012");
				itemSetAttribute(item, "val", resFeature.getSYSTEM_TYPE());
				itemSetAttribute(item, "rmk", "系统类型");
			}
			
			if( isReturnColumn("J030006") ) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030006");
				itemSetAttribute(item, "val", resFeature.getRESOURCE_ID());
				itemSetAttribute(item, "rmk", "资源唯一标识");
			}
			
			if( isReturnColumn("J030037") ) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030037");
				itemSetAttribute(item, "val", resFeature.getBUSINESS_FUNCTION_ID());
				itemSetAttribute(item, "rmk", "业务系统功能标识");
			}
			
			if( isReturnColumn("J020013") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J020013");
				itemSetAttribute(item, "val", resFeature.getAPP_ID());
				itemSetAttribute(item, "rmk", "所属业务系统ID");
			}
			
			if( isReturnColumn("J030007") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030007");
				itemSetAttribute(item, "val", resFeature.getRESOUCE_NAME());
				itemSetAttribute(item, "rmk", "名称");
			}
			
			if( isReturnColumn("J030038") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030038");
				itemSetAttribute(item, "val", resFeature.getBUSINESS_FUNCTION_PARENT_ID());
				itemSetAttribute(item, "rmk", "业务系统功能父节点标识");
			}
			
			if( isReturnColumn("J030008") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030008");
				itemSetAttribute(item, "val", resFeature.getPARENT_RESOURCE());
				itemSetAttribute(item, "rmk", "父资源唯一标识");
			}
			
			if( isReturnColumn("G010002") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.G010002");
				itemSetAttribute(item, "val", resFeature.getURL());
				itemSetAttribute(item, "rmk", "URL");
			}
			
			if( isReturnColumn("J030009") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030009");
				itemSetAttribute(item, "val", resFeature.getRESOURCE_ICON_PATH());
				itemSetAttribute(item, "rmk", "图标路径");
			}
			
			if( isReturnColumn("J030010") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030010");
				itemSetAttribute(item, "val", "" + resFeature.getRESOURCE_STATUS());
				itemSetAttribute(item, "rmk", "资源状态");
			}
			
			if( isReturnColumn("J030011") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030011");
				itemSetAttribute(item, "val", resFeature.getRESOURCE_ORDER());
				itemSetAttribute(item, "rmk", "顺序");
			}
			
			if( isReturnColumn("J030012") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030012");
				itemSetAttribute(item, "val", resFeature.getRESOURCE_DESCRIBE());
				itemSetAttribute(item, "rmk", "资源描述");
			}
			
			if( isReturnColumn("J030013") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030013");
				itemSetAttribute(item, "val", resFeature.getRMK());
				itemSetAttribute(item, "rmk", "备注");
			}
			
			if( isReturnColumn("J030035") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030035");
				itemSetAttribute(item, "val", "" + resFeature.getFUN_RESOURCE_TYPE());
				itemSetAttribute(item, "rmk", "功能资源分类");
			}
			
			if( isReturnColumn("H010029") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.H010029");
				itemSetAttribute(item, "val", ""+resFeature.getDELETE_STATUS());
				itemSetAttribute(item, "rmk", "删除状态");
			}
			
			if( isReturnColumn("J030017") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030017");
				itemSetAttribute(item, "val", ""+resFeature.getDATA_VERSION());
				itemSetAttribute(item, "rmk", "数据版本号");
			}
			
			if( isReturnColumn("I010005") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.I010005");
				itemSetAttribute(item, "val", ""+ getLongTime(resFeature.getLATEST_MOD_TIME()) );
				itemSetAttribute(item, "rmk", "最新修改时间");
			}
		}
		else if( type == SearchDAO.TYPEUSER_ROLE ) {
			UserRole userRole = (UserRole)model;

			if( isReturnColumn("J030014") ) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE_ROLE.J030014");
				itemSetAttribute(item, "val", userRole.getCERTIFICATE_CODE_MD5());
				itemSetAttribute(item, "rmk", "身份证哈希值");
			}
			
			if( isReturnColumn("I010026") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE_ROLE.I010026");
				itemSetAttribute(item, "val", userRole.getBUSINESS_ROLE());
				itemSetAttribute(item, "rmk", "角色编码");
			}
			
			if( isReturnColumn("H010029") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE_ROLE.H010029");
				itemSetAttribute(item, "val", ""+userRole.getDELETE_STATUS());
				itemSetAttribute(item, "rmk", "删除状态");
			}
			
			if( isReturnColumn("J030017") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE_ROLE.J030017");
				itemSetAttribute(item, "val", ""+userRole.getDATA_VERSION());
				itemSetAttribute(item, "rmk", "数据版本号");
			}
			
			if( isReturnColumn("I010005") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE_ROLE.I010005");
				itemSetAttribute(item, "val", ""+ getLongTime(userRole.getLATEST_MOD_TIME()) );
				itemSetAttribute(item, "rmk", "最新修改时间");
			}
		}
		else if( type == SearchDAO.TYPEROLE_RESOURCE ) {
			ResRoleResource roleRes = (ResRoleResource)model;

			if( isReturnColumn("J030006") ) {	
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_RESOURCE_ROLE.J030006");
				itemSetAttribute(item, "val", roleRes.getRESOURCE_ID());
				itemSetAttribute(item, "rmk", "资源唯一标识");
			}
			
			if( isReturnColumn("I010026") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_RESOURCE_ROLE.I010026");
				itemSetAttribute(item, "val", roleRes.getBUSINESS_ROLE());
				itemSetAttribute(item, "rmk", "角色编码");
			}
			
			if( isReturnColumn("J030036") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_RESOURCE_ROLE.J030036");
				itemSetAttribute(item, "val", ""+roleRes.getRESOURCE_CLASS());
				itemSetAttribute(item, "rmk", "资源分类");
			}
			
			if( isReturnColumn("H010029") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_RESOURCE_ROLE.H010029");
				itemSetAttribute(item, "val", ""+roleRes.getDELETE_STATUS());
				itemSetAttribute(item, "rmk", "删除状态");
			}
			
			if( isReturnColumn("J030017") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_RESOURCE_ROLE.J030017");
				itemSetAttribute(item, "val", ""+roleRes.getDATA_VERSION());
				itemSetAttribute(item, "rmk", "数据版本号");
			}
			
			if( isReturnColumn("I010005") ) {		
				item = new Element("ITEM");
				data.addContent(item);
				itemSetAttribute(item, "key", "WA_AUTHORITY_RESOURCE_ROLE.I010005");
				itemSetAttribute(item, "val", ""+ getLongTime(roleRes.getLATEST_MOD_TIME()) );
				itemSetAttribute(item, "rmk", "最新修改时间");
			}
		}
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
            //System.out.println("Date and Time: " + date);
            longtime = date.getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return longtime;
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
		else if ("WA_AUTHORITY_DATA_RESOURCE".equals(tableName) ) {
			type = SearchDAO.TYPERESDATA;
		}
		else if ("WA_AUTHORITY_FUNC_RESOURCE".equals(tableName) ) {
			type = SearchDAO.TYPERESFUN;
		}
		else if ("WA_AUTHORITY_POLICE_ROLE".equals(tableName) ) {
			type = SearchDAO.TYPEUSER_ROLE;
		}
		else if ("WA_AUTHORITY_RESOURCE_ROLE".equals(tableName) ) {
			type = SearchDAO.TYPEROLE_RESOURCE;
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
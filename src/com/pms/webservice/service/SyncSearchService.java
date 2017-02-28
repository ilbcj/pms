package com.pms.webservice.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.pms.dao.OrganizationDAO;
import com.pms.dao.impl.OrganizationDAOImpl;
import com.pms.model.Organization;
import com.pms.model.ResData;
import com.pms.model.ResFeature;
import com.pms.model.ResRole;
import com.pms.model.ResRoleResource;
import com.pms.model.User;
import com.pms.model.UserRole;
import com.pms.webservice.dao.SearchDAO;
import com.pms.webservice.dao.impl.SearchDAOImpl;
import com.pms.webservice.model.Common010121;
import com.pms.webservice.model.Common010123;
import com.pms.webservice.model.Condition;
import com.pms.webservice.model.Item;
import com.pms.webservice.model.SearchCondition;

public class SyncSearchService extends SyncService {
	private static Log logger = LogFactory.getLog(SyncSearchService.class);
	
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
			
			String sqlStr = "select distinct ";
			if( this.getSc().getRETURNITEMS() == null || this.getSc().getRETURNITEMS().size() == 0 ) {
				sqlStr += "* ";
			}
			else {
				sqlStr += "* ";
//				// TODO parse return column
//				List<Item> retCols = getSc().getRETURNITEMS();
//				String tableAlias = getSc().getTableAlias();
//				for( int i = 0; i < retCols.size(); i++ ) {
//					if(tableAlias != null && !tableAlias.isEmpty()) {
//						sqlStr += tableAlias + "." + retCols.get(i).getEng() + ", ";
//					}
//					else {
//						sqlStr += retCols.get(i).getEng() + ", ";
//					}
//				}
//				sqlStr = sqlStr.substring(0, sqlStr.length() - 2);
			}
			
			sqlStr += " from ";
			
			if( this.getSc().getTableName() == null || this.getSc().getTableName().length() == 0) {
				setError(getDataSet(message, "WA_COMMON_010004"), "-10002", "查询条件不正确，表名不存在");
				throw new Exception("search condition data error");
			}
			else {
				sqlStr += this.getSc().getTableName() + " ";
				if( this.getSc().getTableAlias() != null ) {
					sqlStr += getSc().getTableAlias() + " ";
				}
			}
			
			if( (this.getSc().getCONDITION() == null || this.getSc().getCONDITION().length() == 0 
						|| this.getSc().getCONDITIONITEMS() == null || this.getSc().getCONDITIONITEMS().size() == 0 ) 
					&& ( this.getSc().getCONDITION_START() == null || this.getSc().getCONDITION_START().length() == 0 
						|| this.getSc().getSTARTITEMS() == null || this.getSc().getSTARTITEMS().size() == 0
						|| this.getSc().getCONDITION_CONNECT() == null || this.getSc().getCONDITION_CONNECT().length() == 0
						|| this.getSc().getCONNECTITEMS() == null || this.getSc().getCONNECTITEMS().size() == 0)
					&& ( this.getSc().getCommon010123() == null || this.getSc().getCommon010123().size() == 0 ) ) {
				//no where case
			}
			else {
				sqlStr += addSearchConditionToSQL();
			}

			System.out.println(sqlStr);
			
			SearchDAO dao = new SearchDAOImpl();
			int type = getSearchType(this.getSc().getTableName());
//			int first = Integer.parseInt(this.getSc().getOnceNum());
			//int total = Integer.parseInt(this.getSc().getTotalNum() == null ? "0" : this.getSc().getTotalNum());
			int totalNum = Integer.parseInt(this.getSc().getTotalNum() == null ? "0" : this.getSc().getTotalNum());
			int onceNum = Integer.parseInt(this.getSc().getOnceNum() == null ? "0" : this.getSc().getOnceNum());
			int total = Math.min(totalNum, onceNum);
			List datas = null;
			if( this.getSc().getCONNECTTYPE() == SearchCondition.CONNECT_TYPE_NO ) {
				datas = dao.SqlQueryAllCols(sqlStr, type, 0, total);
			}
			else if( this.getSc().getCONNECTTYPE() == SearchCondition.CONNECT_TYPE_010117 ) {
				datas = queryOrgChildrenList(this.getSc().getSTARTITEMS().get(0).getVal());
			}

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
	
	@SuppressWarnings("rawtypes")
	private List queryOrgChildrenList(String orgId) throws Exception {
		OrganizationDAO dao = new OrganizationDAOImpl();
		Organization base = dao.GetOrgNodeById(orgId);
		List<Organization> res = dao.GetOrgNodeByParentId(orgId);
		res.add(0, base);
		return res;
	}
	
	@SuppressWarnings("unchecked")
	private String addSearchConditionToSQL() throws Exception {
		Map<String, String> subMap = new HashMap<String, String>();
		String where = "";
		
		if( this.getSc().getCommon010123() != null && this.getSc().getCommon010123().size() > 0 ) {
			//1 join case
			// 1.1 deal with join part
			for(Common010123 common010123 : getSc().getCommon010123() ) {
				if( Common010123.JOINTYPEINNER == common010123.getJoin() ) {
					where += " inner join ";
				}
				else if( Common010123.JOINTYPELEFT == common010123.getJoin() ) {
					where += " left join ";
				}
				else if( Common010123.JOINTYPERIGHT == common010123.getJoin() ) {
					where += " right join ";
				}
				else {
					throw new Exception("unknow join flag");
				}
				
				if(common010123.getTable() == null || common010123.getTable().isEmpty()) {
					throw new Exception("missing table name parameter");
				}
				else {
					where += common010123.getTable() + " ";
				}
				
				if(common010123.getAlias() == null || common010123.getAlias().isEmpty()) {
					//throw new Exception("missing table alias parameter");
					String loginfo = "[WSQ]missing table alias parameter in '" + getDci().getMESSAGE_SEQUENCE() + "' query message.";
					logger.error(loginfo);
					where += " on ";
				}
				else {
					where += common010123.getAlias() + " on ";
				}
				
				Condition joinConditions = common010123.getSearch();
				if( joinConditions.getItems().size() > 0 ) {
					Item item = joinConditions.getItems().get(0);
					//where += item.getEng() + "=" + item.getVal() + " ";
					String destColumn = item.getEng().contains(".") ? item.getEng().substring(item.getEng().indexOf(".") + 1) : item.getEng();
					// if join table only contain one column, the request doesn't contain column name, we need add column name after table name.
					if( item.getVal().contains(".") ) {
						where += item.getEng() + "=" + item.getVal() + " ";
					}
					else {
						
						where += item.getEng() + "=" + item.getVal() + "." + destColumn + " ";
					}
					for( int i = 1; i < joinConditions.getItems().size(); i++ ) {
						item = joinConditions.getItems().get(i);
						//where += " " + joinConditions.getRel() + " " + item.getEng() + "=" + item.getVal() + " ";
						if( item.getVal().contains(".") ) { 
							where += " " + joinConditions.getRel() + " " + item.getEng() + "=" + item.getVal() + " ";
						}
						else {
							where += " " + joinConditions.getRel() + " " + item.getEng() + "=" + item.getVal() + "." + destColumn + " ";
						}
					}
				}
			}
			
			// 1.2 deal with alias part which in the join clause
			if( this.getSc().getCommon010121() != null && this.getSc().getCommon010121().size() > 0 ) {
				for(Common010121 common010121 : getSc().getCommon010121() ) {
					// 1.2.1 : ( select distinct
					String aliasSearch = " ( select distinct ";
					List<Item> retCols = common010121.getRETURNITEMS();
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
					
					// 1.2.2 : ( select distinct columns from
					aliasSearch += " from ";
					
					// 1.2.3 : ( select distinct columns from table 
					if( common010121.getTable() == null || common010121.getTable().length() == 0 ) {
						logger.info("查询条件不正确，表名不存在");
						throw new Exception("search condition data error");
					}
					else {
						aliasSearch += common010121.getTable() + " ";
					}
					
					// 1.2.4 : ( select distinct columns from table where case
					if( common010121.getSearch() != null && common010121.getSearch().getItems() != null && common010121.getSearch().getItems().size() != 0 ) {
						aliasSearch += " where ";
						List<Item> items = common010121.getSearch().getItems();
						if( items.size() == 1 && "IN".equalsIgnoreCase(common010121.getSearch().getRel()) ) {
							aliasSearch += items.get(0).getEng() + " in ( " + items.get(0).getVal() + ") ";
						}
						else {
							aliasSearch += items.get(0).getEng() + " = " + items.get(0).getVal();
							for(int i = 1; i< items.size(); i++) {
								aliasSearch += " " + common010121.getSearch().getRel() + " " + items.get(i).getEng() + " = " + items.get(i).getVal() + " ";
							}
						}
					}

					// 1.2.5 : ( select distinct columns from table where case ) alias 
					aliasSearch += " ) " + common010121.getAlias() + " ";
					
					// 1.2.6 : put aliasSearch to join clause
//					if( where.contains( "'" + common010121.getAlias() + "'" ) ) {
//						where.replace("'" + common010121.getAlias() + "'", aliasSearch);
//					}
					if( where.contains( common010121.getAlias() ) ) {
						where = where.replaceAll("(^.*?)" + common010121.getAlias() + "(.*$)", "$1" + aliasSearch + "$2");

						where.replaceAll(common010121.getAlias(), aliasSearch);
					}
				}
			}
			
			// 1.3 deal with where part
			List<Item> whereItem = getSc().getCONDITIONITEMS();
			if( whereItem != null && whereItem.size() > 0 ) {
				where += " where " + whereItem.get(0).getEng() + " = " + whereItem.get(0).getVal() + " ";
				for(int i = 1; i< whereItem.size(); i++) {
					where += " " + getSc().getCONDITION() + " " + whereItem.get(i).getEng() + " = " + whereItem.get(i).getVal() + " ";
				}
			}
		}
		else {
			//2 subsearch case
			if( this.getSsc() != null ) {
				String subSearch = "";
				subSearch = "select ";
				if(this.getSsc().getRETURNITEMS() == null || this.getSsc().getRETURNITEMS().size() == 0 ) {
					subSearch +=  " * ";
				}
				else {
					for(int i = 0; i< this.getSsc().getRETURNITEMS().size(); i++) {
						subSearch += " " + ((Item)this.getSsc().getRETURNITEMS().get(i)).getEng() + ", ";
					}
					subSearch = subSearch.substring(0,subSearch.lastIndexOf(','));
					subSearch += " ";
				}
				subSearch += "from " + this.getSsc().getTableName() + " ";
				List<Item> subCons = this.getSsc().getCONDITIONITEMS();
				List<Item> subStartCons = this.getSsc().getSTARTITEMS();
				List<Item> subConnectCons = this.getSsc().getCONNECTITEMS();
				if(subCons != null && subCons.size() > 0) {
					subSearch += " where ";
					if( "IN".equalsIgnoreCase(this.getSsc().getCONDITION()) ) {
						subSearch += subCons.get(0).getEng() + " in (" + subCons.get(0).getVal() + ") ";
					}else {
						subSearch += subCons.get(0).getVal().length() == 0 ? subCons.get(0).getEng() + " is null " : subCons.get(0).getEng() + "='" + subCons.get(0).getVal() + "' ";
						for(int i = 1; i<subCons.size(); i++) {
							subSearch += this.getSsc().getCONDITION() + " ";
							subSearch += subCons.get(i).getVal().length() == 0 ? subCons.get(i).getEng() + " is null " : subCons.get(i).getEng() + "='" + subCons.get(i).getVal() + "' ";
						}
					}
				}
				else if (subStartCons != null && subStartCons.size() > 0 && subConnectCons != null && subConnectCons.size() > 0) {
					List<Organization> datas = queryOrgChildrenList(this.getSsc().getSTARTITEMS().get(0).getVal());
					subSearch = "";
					for(int i = 0; i<datas.size(); i++) {
						if( "GA_DEPARTMENT".equals(((Item)this.getSsc().getRETURNITEMS().get(0)).getEng())) {
							subSearch += datas.get(i).getGA_DEPARTMENT() + ", ";
						}
					}
					if(subSearch.length() > 0) {
						subSearch = subSearch.substring(0,subSearch.lastIndexOf(','));
						subSearch += " ";
					}
				}
				subMap.put(this.getSsc().getAlias(), subSearch);
				if( this.getSsc().getRETURNITEMS().size() == 1 ) {
					String retColFlag = ((Item)this.getSsc().getRETURNITEMS().get(0)).getKey();
					subMap.put(this.getSsc().getAlias() + "." + retColFlag, subSearch);
				}
			}
			
			
			where = "where ";
			List<Item> cons = this.getSc().getCONDITIONITEMS();
			List<Item> startCons = this.getSc().getSTARTITEMS();
			List<Item> connectCons = this.getSc().getCONNECTITEMS();
			if(cons != null && cons.size() > 0) {
				if( "IN".equalsIgnoreCase(this.getSc().getCONDITION()) ) {
					//where += cons.get(0).getEng() + " in " + matchSubSearch(subMap, cons.get(0).getVal()) + " ";
					where += cons.get(0).getEng() + " in (" + matchSubSearchWithInCondition(subMap, cons.get(0).getVal()) + ") ";
				}else {
					where += cons.get(0).getVal() == null || cons.get(0).getVal().length() == 0 ? cons.get(0).getEng() + " is null " : cons.get(0).getEng() + "=" + matchSubSearch(subMap, cons.get(0).getVal()) + " ";
					for(int i = 1; i<cons.size(); i++) {
						where += this.getSc().getCONDITION() + " ";
						where += cons.get(i).getVal() == null || cons.get(i).getVal().length() == 0 ? cons.get(i).getEng() + " is null " : cons.get(i).getEng() + "=" + matchSubSearch(subMap, cons.get(i).getVal()) + " ";
					}
				}
			}
			else if( startCons != null && startCons.size() > 0 && connectCons != null && connectCons.size() > 0 ) {
				where = "CONNECT BY PRIOR " + connectCons.get(0).getEng() + " = " + connectCons.get(0).getVal() + " ";
				where += " START WITH " + startCons.get(0).getEng() + " = " + startCons.get(0).getVal();
			}
		}
		
		return where;
	}
	
	private String matchSubSearchWithInCondition(Map<String, String> subMap, String columnValue) {
		String result = null;
		if( subMap.containsKey(columnValue) ) {
			result = subMap.get(columnValue);
		} else {
			result = columnValue;
		}
		return result;	
	}
	
	private String matchSubSearch(Map<String, String> subMap, String columnValue) {
		String result = null;
		if( subMap.containsKey(columnValue) ) {
			result = " ( " + subMap.get(columnValue) + " ) ";
		} else {
			result = " '" + columnValue + "' ";
		}
		return result;	
	}
	
	private void addDBResultItemToXML(Element data, Object model, int type) {
		Element item = null;
		if( type == SearchDAO.TYPEUSER ) {
			User user = (User)model;
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.J030014");
			itemSetAttribute(item, "val", user.getCERTIFICATE_CODE_MD5());
			itemSetAttribute(item, "rmk", "身份证哈希值");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.J030015");
			itemSetAttribute(item, "val", user.getCERTIFICATE_CODE_SUFFIX());
			itemSetAttribute(item, "rmk", "身份证后6位");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.B010001");
			itemSetAttribute(item, "val", user.getNAME());
			itemSetAttribute(item, "rmk", "姓名");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.B010011");
			itemSetAttribute(item, "val", user.getSEXCODE());
			itemSetAttribute(item, "rmk", "性别");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.A010001");
			itemSetAttribute(item, "val", user.getGA_DEPARTMENT());
			itemSetAttribute(item, "rmk", "公安组织机构代码");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.E010002");
			itemSetAttribute(item, "val", user.getUNIT());
			itemSetAttribute(item, "rmk", "公安组织机构名称");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.I010052");
			itemSetAttribute(item, "val", ""+user.getORG_LEVEL());
			itemSetAttribute(item, "rmk", "公安组织机构级别");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.A010003");
			itemSetAttribute(item, "val", user.getPOLICE_SORT());
			itemSetAttribute(item, "rmk", "警种");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.B010028");
			itemSetAttribute(item, "val", user.getPOLICE_NO());
			itemSetAttribute(item, "rmk", "警号");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.H010034");
			itemSetAttribute(item, "val", user.getSENSITIVE_LEVEL());
			itemSetAttribute(item, "rmk", "最高敏感级别");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.I010031");
			itemSetAttribute(item, "val", user.getBUSINESS_TYPE());
			itemSetAttribute(item, "rmk", "业务部门类别");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.B030026");
			itemSetAttribute(item, "val", user.getTAKE_OFFICE());
			itemSetAttribute(item, "rmk", "职务/职称");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.J030016");
			itemSetAttribute(item, "val", ""+user.getUSER_STATUS());
			itemSetAttribute(item, "rmk", "用户状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.H010029");
			itemSetAttribute(item, "val", ""+user.getDELETE_STATUS());
			itemSetAttribute(item, "rmk", "删除状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.J030017");
			itemSetAttribute(item, "val", ""+user.getDATA_VERSION());
			itemSetAttribute(item, "rmk", "数据版本号");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE.I010005");
			itemSetAttribute(item, "val", ""+ getLongTime(user.getLATEST_MOD_TIME()) );
			itemSetAttribute(item, "rmk", "最新修改时间");
			
		}
		else if( type == SearchDAO.TYPEORG ) {
			Organization org = (Organization)model;
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_ORGNIZATION.A010001");
			itemSetAttribute(item, "val", org.getGA_DEPARTMENT());
			itemSetAttribute(item, "rmk", "公安组织机构代码");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_ORGNIZATION.E010002");
			itemSetAttribute(item, "val", org.getUNIT());
			itemSetAttribute(item, "rmk", "公安组织机构名称");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_ORGNIZATION.I010052");
			itemSetAttribute(item, "val", "" + org.getORG_LEVEL());
			itemSetAttribute(item, "rmk", "公安组织机构级别");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_ORGNIZATION.A010005");
			itemSetAttribute(item, "val", org.getPARENT_ORG());
			itemSetAttribute(item, "rmk", "组织机构父节点(上级组织机构)");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_ORGNIZATION.H010029");
			itemSetAttribute(item, "val", ""+org.getDELETE_STATUS());
			itemSetAttribute(item, "rmk", "删除状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_ORGNIZATION.J030017");
			itemSetAttribute(item, "val", ""+org.getDATA_VERSION());
			itemSetAttribute(item, "rmk", "数据版本号");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_ORGNIZATION.I010005");
			itemSetAttribute(item, "val", ""+ getLongTime(org.getLATEST_MOD_TIME()) );
			itemSetAttribute(item, "rmk", "最新修改时间");
		}
		else if( type == SearchDAO.TYPEROLE ) {
			ResRole role = (ResRole)model;
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_ROLE.I010026");
			itemSetAttribute(item, "val", role.getBUSINESS_ROLE());
			itemSetAttribute(item, "rmk", "角色编码");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_ROLE.I010025");
			itemSetAttribute(item, "val", ""+role.getBUSINESS_ROLE_TYPE());
			itemSetAttribute(item, "rmk", "角色类型");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_ROLE.I010054");
			itemSetAttribute(item, "val", role.getBUSINESS_ROLE_NAME());
			itemSetAttribute(item, "rmk", "角色名称");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_ROLE.B050016");
			itemSetAttribute(item, "val", role.getSYSTEM_TYPE());
			itemSetAttribute(item, "rmk", "系统类型");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_ROLE.H010029");
			itemSetAttribute(item, "val", ""+role.getDELETE_STATUS());
			itemSetAttribute(item, "rmk", "删除状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_ROLE.J030017");
			itemSetAttribute(item, "val", ""+role.getDATA_VERSION());
			itemSetAttribute(item, "rmk", "数据版本号");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_ROLE.I010005");
			itemSetAttribute(item, "val", ""+ getLongTime(role.getLATEST_MOD_TIME()) );
			itemSetAttribute(item, "rmk", "最新修改时间");
			
		}
		else if( type == SearchDAO.TYPERESDATA ) {
			ResData resData = (ResData)model;
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030006");
			itemSetAttribute(item, "val", resData.getRESOURCE_ID());
			itemSetAttribute(item, "rmk", "资源唯一标识");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030010");
			itemSetAttribute(item, "val", ""+resData.getRESOURCE_STATUS());
			itemSetAttribute(item, "rmk", "资源状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030012");
			itemSetAttribute(item, "val", resData.getRESOURCE_DESCRIBE());
			itemSetAttribute(item, "rmk", "资源描述");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030003");
			itemSetAttribute(item, "val", resData.getDATASET_SENSITIVE_LEVEL());
			itemSetAttribute(item, "rmk", "数据集敏感度编码（表控）");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.A010004");
			itemSetAttribute(item, "val", resData.getDATA_SET());
			itemSetAttribute(item, "rmk", "数据集编码（表控）");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030001");
			itemSetAttribute(item, "val", resData.getSECTION_CLASS());
			itemSetAttribute(item, "rmk", "字段分类编码（列控）");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030004");
			itemSetAttribute(item, "val", resData.getELEMENT());
			itemSetAttribute(item, "rmk", "字段编码（原目标字段列控）");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030002");
			itemSetAttribute(item, "val", resData.getSECTION_RELATIOIN_CLASS());
			itemSetAttribute(item, "rmk", "字段分类关系编码（列控）");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030021");
			itemSetAttribute(item, "val", resData.getOPERATE_SYMBOL());
			itemSetAttribute(item, "rmk", "操作符");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030005");
			itemSetAttribute(item, "val", resData.getELEMENT_VALUE());
			itemSetAttribute(item, "rmk", "字段值");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030013");
			itemSetAttribute(item, "val", resData.getRMK());
			itemSetAttribute(item, "rmk", "备注");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.H010029");
			itemSetAttribute(item, "val", ""+resData.getDELETE_STATUS());
			itemSetAttribute(item, "rmk", "删除状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.J030017");
			itemSetAttribute(item, "val", ""+resData.getDATA_VERSION());
			itemSetAttribute(item, "rmk", "数据版本号");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_DATA_RESOURCE.I010005");
			itemSetAttribute(item, "val", ""+ getLongTime(resData.getLATEST_MOD_TIME()) );
			itemSetAttribute(item, "rmk", "最新修改时间");
		}
		else if( type == SearchDAO.TYPERESFUN ) {
			ResFeature resFeature = (ResFeature)model;
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J020012");
			itemSetAttribute(item, "val", resFeature.getSYSTEM_TYPE());
			itemSetAttribute(item, "rmk", "系统类型");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030006");
			itemSetAttribute(item, "val", resFeature.getRESOURCE_ID());
			itemSetAttribute(item, "rmk", "资源唯一标识");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J020013");
			itemSetAttribute(item, "val", resFeature.getAPP_ID());
			itemSetAttribute(item, "rmk", "所属业务系统ID");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030007");
			itemSetAttribute(item, "val", resFeature.getRESOUCE_NAME());
			itemSetAttribute(item, "rmk", "名称");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030008");
			itemSetAttribute(item, "val", resFeature.getPARENT_RESOURCE());
			itemSetAttribute(item, "rmk", "父资源唯一标识");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.G010002");
			itemSetAttribute(item, "val", resFeature.getURL());
			itemSetAttribute(item, "rmk", "URL");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030009");
			itemSetAttribute(item, "val", resFeature.getRESOURCE_ICON_PATH());
			itemSetAttribute(item, "rmk", "图标路径");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030010");
			itemSetAttribute(item, "val", "" + resFeature.getRESOURCE_STATUS());
			itemSetAttribute(item, "rmk", "资源状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030011");
			itemSetAttribute(item, "val", resFeature.getRESOURCE_ORDER());
			itemSetAttribute(item, "rmk", "顺序");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030012");
			itemSetAttribute(item, "val", resFeature.getRESOURCE_DESCRIBE());
			itemSetAttribute(item, "rmk", "资源描述");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030013");
			itemSetAttribute(item, "val", resFeature.getRMK());
			itemSetAttribute(item, "rmk", "备注");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030035");
			itemSetAttribute(item, "val", "" + resFeature.getFUN_RESOURCE_TYPE());
			itemSetAttribute(item, "rmk", "功能资源分类");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.H010029");
			itemSetAttribute(item, "val", ""+resFeature.getDELETE_STATUS());
			itemSetAttribute(item, "rmk", "删除状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.J030017");
			itemSetAttribute(item, "val", ""+resFeature.getDATA_VERSION());
			itemSetAttribute(item, "rmk", "数据版本号");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_FUNC_RESOURCE.I010005");
			itemSetAttribute(item, "val", ""+ getLongTime(resFeature.getLATEST_MOD_TIME()) );
			itemSetAttribute(item, "rmk", "最新修改时间");
		}
		else if( type == SearchDAO.TYPEUSER_ROLE ) {
			UserRole userRole = (UserRole)model;
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE_ROLE.J030014");
			itemSetAttribute(item, "val", userRole.getCERTIFICATE_CODE_MD5());
			itemSetAttribute(item, "rmk", "身份证哈希值");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE_ROLE.I010026");
			itemSetAttribute(item, "val", userRole.getBUSINESS_ROLE());
			itemSetAttribute(item, "rmk", "角色编码");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE_ROLE.H010029");
			itemSetAttribute(item, "val", ""+userRole.getDELETE_STATUS());
			itemSetAttribute(item, "rmk", "删除状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE_ROLE.J030017");
			itemSetAttribute(item, "val", ""+userRole.getDATA_VERSION());
			itemSetAttribute(item, "rmk", "数据版本号");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_POLICE_ROLE.I010005");
			itemSetAttribute(item, "val", ""+ getLongTime(userRole.getLATEST_MOD_TIME()) );
			itemSetAttribute(item, "rmk", "最新修改时间");
		}
		else if( type == SearchDAO.TYPEROLE_RESOURCE ) {
			ResRoleResource roleRes = (ResRoleResource)model;
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_RESOURCE_ROLE.J030006");
			itemSetAttribute(item, "val", roleRes.getRESOURCE_ID());
			itemSetAttribute(item, "rmk", "资源唯一标识");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_RESOURCE_ROLE.I010026");
			itemSetAttribute(item, "val", roleRes.getBUSINESS_ROLE());
			itemSetAttribute(item, "rmk", "角色编码");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_RESOURCE_ROLE.J030036");
			itemSetAttribute(item, "val", ""+roleRes.getRESOURCE_CLASS());
			itemSetAttribute(item, "rmk", "资源分类");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_RESOURCE_ROLE.H010029");
			itemSetAttribute(item, "val", ""+roleRes.getDELETE_STATUS());
			itemSetAttribute(item, "rmk", "删除状态");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_RESOURCE_ROLE.J030017");
			itemSetAttribute(item, "val", ""+roleRes.getDATA_VERSION());
			itemSetAttribute(item, "rmk", "数据版本号");
			
			item = new Element("ITEM");
			data.addContent(item);
			itemSetAttribute(item, "key", "WA_AUTHORITY_RESOURCE_ROLE.I010005");
			itemSetAttribute(item, "val", ""+ getLongTime(roleRes.getLATEST_MOD_TIME()) );
			itemSetAttribute(item, "rmk", "最新修改时间");
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
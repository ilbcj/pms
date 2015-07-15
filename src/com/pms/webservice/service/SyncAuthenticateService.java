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

import com.pms.model.ResData;
import com.pms.webservice.model.Condition;
import com.pms.webservice.model.Item;
import com.pms.webservice.model.auth.Common010032;

public class SyncAuthenticateService extends SyncService {

	@Override
	public String GetResult() throws IOException {
		//1. get resource's by user id
		List<ResData> resources =  queryResourceByUser(this.getUa().getUSER_NAME());
		
		//2. compare if seaching data in the query result of step 1.
		String dataset = null;
		for( int i = 0; i < this.getAc().getCommon010032().size(); i++ ) {
			dataset = this.getAc().getCommon010032().get(i).getSourceName();
			//compare subcondition
			List<Condition> subConditions = this.getAc().getCommon010032().get(i).getSubConditions();
			for( int j = 0; j < subConditions.size(); j++ ) {
				Condition condition = subConditions.get(j);
				List<Item> items = condition.getItems();
				for(int k = 0; k < items.size(); k++) {
					Item item = items.get(k);
					if( checkResourceAccessRights(resources, dataset, item.getKey(), item.getVal()) ) {
						item.setHasAccessAuth(true);
					} else {
						item.setHasAccessAuth(false);
					}
				}				
			}
			
			//compare return column
			List<Item> retColumns = this.getAc().getCommon010032().get(i).getItems();
			for(int j = 0; j < retColumns.size(); j++) {
				Item column = retColumns.get(j);
				if( checkResourceAccessRights(resources, dataset, column.getKey(), column.getVal()) ) {
					column.setHasAccessAuth(true);
				} else {
					column.setHasAccessAuth(false);
				}
			}
		}
		
		//3. generate result
		String result = null;
		result = generateAuthResult();
		return result;
	}

	private List<ResData> queryResourceByUser(String userId) {
		// get use's role
		
		// get use's usergroup
		
		// get usegroup's role
		
		// get use's org
		
		// get org's role
		
		// get resource of role
		return null;
	}
	
	private boolean checkResourceAccessRights(List<ResData> resources,
			String dataset, String column, String value) {
		boolean result = false;
		for(int i = 0; i < resources.size(); i++) {
			ResData res = resources.get(i);
			
			if ( dataset == null || dataset.length() == 0 || column == null || column.length() == 0 ) {
				continue;
			}
			
			if ( dataset.equals(res.getDATA_SET()) && column.equals(res.getELEMENT()) ) {		
				if ( value != null && value.length() == 0 ) {
					if ( value.equals(res.getELEMENT_VALUE()) ) {
						result = true;
						break;
					} else {
						continue;
					}
				}
				result = true;
				break;
			} else {
				continue;
			}
		}
		return result;
	}


	private String generateAuthResult() throws IOException {
		String result = null;
		Document doc = null;
		try{
			Element message = null;// dataset = null, data = null, condition = null, item = null;
			message = new Element("MESSAGE");
			doc = new Document(message);
			
			// 1- WA_COMMON_010000
			Element dataset010000 = null;
			dataset010000 = new Element("DATASET");
			message.addContent(dataset010000);
			dataset010000.setAttribute("name", "WA_COMMON_010000");
			dataset010000.setAttribute("rmk", "数据交互通用信息");
			
			Element data010000 = null;
			data010000 = new Element("DATA");
			dataset010000.addContent(data010000);
			
			Element item010000 = null;
			item010000 = new Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "H010006");
			itemSetAttribute(item010000, "val", this.getDci().getFROM());
			itemSetAttribute(item010000, "rmk", "发起节点的标识");
			
			item010000 = new Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "H010007");
			itemSetAttribute(item010000, "val", this.getDci().getTO());
			itemSetAttribute(item010000, "rmk", "目的节点的标识");
			
			item010000 = new Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "I010014");
			itemSetAttribute(item010000, "val", this.getDci().getMESSAGE_SEQUENCE());
			itemSetAttribute(item010000, "rmk", "消息流水号");
			
			item010000 = new Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "I010013");
			itemSetAttribute(item010000, "val", "252011");
			itemSetAttribute(item010000, "rmk", "消息类型（鉴权服务结果）");

			item010000 = new Element("ITEM");
			data010000.addContent(item010000);
			itemSetAttribute(item010000, "key", "I010010");
			itemSetAttribute(item010000, "val", this.getDci().getBUSINESS_SERVER_TYPE());
			itemSetAttribute(item010000, "rmk", "业务服务类型（鉴权服务）");
			
			// 1- WA_COMMON_010004
			Element dataset010004 = null;
			dataset010004 = new Element("DATASET");
			message.addContent(dataset010004);
			dataset010004.setAttribute("name", "WA_COMMON_010004");
			dataset010004.setAttribute("rmk", "消息返回状态信息");

			Element data010004 = null;
			data010004 = new Element("DATA");
			dataset010004.addContent(data010004);
			
			Element item010004 = null;
			item010004 = new Element("ITEM");
			data010004.addContent(item010004);
			itemSetAttribute(item010004, "key", "I030003");
			itemSetAttribute(item010004, "val", "0");
			itemSetAttribute(item010004, "rmk", "消息状态");
			
			item010004 = new Element("ITEM");
			data010004.addContent(item010004);
			itemSetAttribute(item010004, "key", "I010015");
			itemSetAttribute(item010004, "val", "" + new Date().getTime());
			itemSetAttribute(item010004, "rmk", "消息返回时间");
			
			item010004 = new Element("ITEM");
			data010004.addContent(item010004);
			itemSetAttribute(item010004, "key", "I030010");
			itemSetAttribute(item010004, "val", "1036");
			itemSetAttribute(item010004, "rmk", "业务消息码（部分权限）");
			
			item010004 = new Element("ITEM");
			data010004.addContent(item010004);
			itemSetAttribute(item010004, "key", "I010009");
			itemSetAttribute(item010004, "val", "");
			itemSetAttribute(item010004, "rmk", "备注");
			
			// 1- WA_COMMON_010038
			Element dataset010038 = null;
			dataset010038 = new Element("DATASET");
			message.addContent(dataset010038);
			dataset010038.setAttribute("name", "WA_COMMON_010038");
			dataset010038.setAttribute("rmk", "可访问的字段关系，数据级别");
			
			Element data010038 = null;
			data010038 = new Element("DATA");
			dataset010038.addContent(data010038);
			
			Element item010038 = null;
			item010038 = new Element("ITEM");
			data010038.addContent(item010038);
			itemSetAttribute(item010038, "key", "H010034");
			itemSetAttribute(item010038, "eng", "SENSITIVE_LEVEL");
			itemSetAttribute(item010038, "chn", "最高敏感级别");
			itemSetAttribute(item010038, "val", this.getAc().getSensitiveLevel());			
			
			// 1- WA_COMMON_010034
			Element dataset010034 = null;
			dataset010034 = new Element("DATASET");
			message.addContent(dataset010034);
			dataset010034.setAttribute("name", "WA_COMMON_010034");
			dataset010034.setAttribute("rmk", "鉴权结果信息");

			Element data010034 = null;
			data010034 = new Element("DATA");
			dataset010034.addContent(data010034);
			
			// 2-- WA_COMMON_010034 --> data --> CONDITION(STC)
			Element condition010034 = null;
			condition010034 = new Element("CONDITION");
			data010034.addContent(condition010034);
			condition010034.setAttribute("rel", this.getAc().getStc().getRel());
			
			Element itemSTC = null;
			List<Item> items = this.getAc().getStc().getItems();
			for( int i = 0; i < items.size(); i++ ) {
				Item currentItem = items.get(i);
				itemSTC = new Element("ITEM");
				condition010034.addContent(itemSTC);
				itemSetAttribute(itemSTC, "key", currentItem.getKey());
				itemSetAttribute(itemSTC, "eng", currentItem.getEng());
				itemSetAttribute(itemSTC, "val", currentItem.getVal());
			}
			
			// 2-- WA_COMMON_010034 --> data --> WA_COMMON_010036
			Element dataset010036 = null;
			dataset010036 = new Element("DATASET");
			data010034.addContent(dataset010036);
			dataset010036.setAttribute("name", "WA_COMMON_010036");
			dataset010036.setAttribute("rmk", "鉴权详细结果");
			
			// 3--- WA_COMMON_010034 --> data --> WA_COMMON_010036 --> data(loop)
			for( int i = 0; i < this.getAc().getCommon010032().size(); i++ ) {
				Common010032 common010032 = this.getAc().getCommon010032().get(i);
				Element data010036 = null;
				data010036 = new Element("DATA");
				dataset010036.addContent(data010036);
				
				// 4---- WA_COMMON_010034 --> data --> WA_COMMON_010036 --> data(loop) --> dataset(result)
				Element datasetResult = null;
				datasetResult = new Element("DATASET");
				data010036.addContent(datasetResult);
				datasetResult.setAttribute("name", common010032.getSourceName());
				
				// 5----- WA_COMMON_010034 --> data --> WA_COMMON_010036 --> data(loop) --> dataset(result) --> data(result)
				Element dataResult = null;
				dataResult = new Element("DATA");
				datasetResult.addContent(dataResult);

		//parse auth result
				boolean hasAuthCondition = false, hasAuthColumn = false;
				// 6------ WA_COMMON_010034 --> data --> WA_COMMON_010036 --> data(loop) --> dataset(result) --> data(result) --> dataset(success)
				Element datasetSuccess = null;
				datasetSuccess = new Element("DATASET");
				dataResult.addContent(datasetSuccess);
				datasetSuccess.setAttribute("name", "WA_COMMON_010035");
				datasetSuccess.setAttribute("rmk", "鉴权成功部分");
				
				// 7------- traverse conditions, find success record
				Element dataSuccessCondition = null;
				dataSuccessCondition = new Element("DATA");
				
				Element conditionParent = null;
				conditionParent = new Element("CONDITION");
				dataSuccessCondition.addContent(conditionParent);
				conditionParent.setAttribute("rel", common010032.getParentCondition());
				
				List<Condition> subConditions = common010032.getSubConditions();
				for( int j = 0; j < subConditions.size(); j++ ) {
					Condition condition = subConditions.get(j);
					boolean isLink2Parent = false;
					List<Item> subConditionItems = condition.getItems();
					
					Element subCondition = null;
					subCondition = new Element("CONDITION");
					//conditionParent.addContent(subCondition);
					subCondition.setAttribute("rel", condition.getRel());
					
					for( int k = 0; k < subConditionItems.size(); k++ ) {
						Item item = subConditionItems.get(k);
						Element itemCondition = null;
						itemCondition = new Element("ITEM");
						subCondition.addContent(itemCondition);
						itemSetAttribute(itemCondition, "key", item.getKey());
						itemSetAttribute(itemCondition, "eng", item.getEng());
						itemSetAttribute(itemCondition, "val", item.getVal());
						
						if( "H010014".equals(item.getKey()) ) {
							isLink2Parent = true;
						}
						else if( item.isHasAccessAuth() ) {
							isLink2Parent = true;
							hasAuthCondition = true;
						}
						else {
							//if not time, and don't have authenticated condition, then do nothing
						}
					}
					
					if ( isLink2Parent ) {
						conditionParent.addContent(subCondition);
					}
				}
				// 7------- traverse return columns, find success record
				Element dataSuccessColumn = null;
				dataSuccessColumn = new Element("DATA");
				
				List<Item> retColumnItems = common010032.getItems();
				for( int j = 0; j < retColumnItems.size(); j++ ) {
					Item item = retColumnItems.get(j);
					
					Element itemRetColumn = null;
					itemRetColumn = new Element("ITEM");
					
					itemSetAttribute(itemRetColumn, "key", item.getKey());
					itemSetAttribute(itemRetColumn, "eng", item.getEng());
					itemSetAttribute(itemRetColumn, "val", item.getVal());
					
					if( item.isHasAccessAuth() ) {
						hasAuthColumn = true;
						dataSuccessColumn.addContent(itemRetColumn);
					}
				}
				
				if( hasAuthCondition && hasAuthColumn ) {
					datasetSuccess.addContent(dataSuccessCondition);
					datasetSuccess.addContent(dataSuccessColumn);
				}
				else {
					// if there is no authenticated condition or there is no authenticated column ,then nothing success returned.
				}
				
				// 6------ WA_COMMON_010034 --> data --> WA_COMMON_010036 --> data(loop) --> dataset(result) --> data(result) --> dataset(fail)
				Element datasetFail = null;
				datasetFail = new Element("DATASET");
				dataResult.addContent(datasetFail);
				datasetFail.setAttribute("name", "WA_COMMON_010037");
				datasetFail.setAttribute("rmk", "鉴权失败的部分");
				
				// 7------- traverse conditions, find fail record
				Element dataFailCondition = null;
				dataFailCondition = new Element("DATA");
				
				if( !hasAuthCondition ) {
					conditionParent = new Element("CONDITION");
					dataFailCondition.addContent(conditionParent);
					conditionParent.setAttribute("rel", common010032.getParentCondition());
				}
				
			}
			
			
//			
//			
//			item = new Element("ITEM");
//			data.addContent(item);
//			itemSetAttribute(item, "key", "H010005");
//			itemSetAttribute(item, "val", "");
//			itemSetAttribute(item, "rmk", "查询任务标识（异步查询时必填）");
//			
//			dataset = new Element("DATASET");
//			data.addContent(dataset);
//			dataset.setAttribute("name", "WA_COMMON_010125");
//			dataset.setAttribute("rmk", "查询返回数据");
//			
//			if(this.getSc() == null) {
//				setError(getDataSet(message, "WA_COMMON_010004"), "-10001", "查询条件不存在");
//				throw new Exception("search condition data error");
//			}
//			
//			String sqlStr = "select ";
//			if( this.getSc().getRETURNITEMS() == null || this.getSc().getRETURNITEMS().size() == 0 ) {
//				sqlStr += "* ";
//			}
//			else {
//				// TODO parse return column
//			}
//			
//			sqlStr += "from ";
//			
//			if( this.getSc().getTableName() == null || this.getSc().getTableName().length() == 0) {
//				setError(getDataSet(message, "WA_COMMON_010004"), "-10002", "查询条件不正确，表名不存在");
//				throw new Exception("search condition data error");
//			}
//			else {
//				sqlStr += this.getSc().getTableName() + " ";
//			}
//			
//			if( (this.getSc().getCONDITION() == null || this.getSc().getCONDITION().length() == 0 
//						|| this.getSc().getCONDITIONITEMS() == null || this.getSc().getCONDITIONITEMS().size() == 0 ) 
//					&& ( this.getSc().getCONDITION_START() == null || this.getSc().getCONDITION_START().length() == 0 
//						|| this.getSc().getSTARTITEMS() == null || this.getSc().getSTARTITEMS().size() == 0
//						|| this.getSc().getCONDITION_CONNECT() == null || this.getSc().getCONDITION_CONNECT().length() == 0
//						|| this.getSc().getCONNECTITEMS() == null || this.getSc().getCONNECTITEMS().size() == 0) ) {
//				//no where case
//			}
//			else {
//				sqlStr += addSearchConditionToSQL();
//			}
//
//			System.out.println(sqlStr);
//			
//			SearchDAO dao = new SearchDAOImpl();
//			int type = getSearchType(this.getSc().getTableName());
////			int first = Integer.parseInt(this.getSc().getOnceNum());
//			int total = Integer.parseInt(this.getSc().getTotalNum());
//			List datas = null;
//			if( this.getSc().getCONNECTTYPE() == SearchCondition.CONNECT_TYPE_NO ) {
//				datas = dao.SqlQueryAllCols(sqlStr, type, 0, total);
//			}
//			else if( this.getSc().getCONNECTTYPE() == SearchCondition.CONNECT_TYPE_010117 ) {
//				datas = queryOrgChildrenList(this.getSc().getSTARTITEMS().get(0).getVal());
//			}
//			
//			
//			
//			for( int i = 0; i<datas.size(); i++) {
//				data = new Element("DATA");
//				dataset.addContent(data);
//				
//				addDBResultItemToXML(data, datas.get(i), type);
//			}
			
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
}

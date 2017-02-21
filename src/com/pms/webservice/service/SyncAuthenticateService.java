package com.pms.webservice.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.google.common.collect.HashMultimap;
import com.pms.dao.PrivilegeDAO;
import com.pms.dao.ResClassifyResourceDAO;
import com.pms.dao.ResColumnClassifyRelationDAO;
import com.pms.dao.ResourceDAO;
import com.pms.dao.UserDAO;
import com.pms.dao.impl.PrivilegeDAOImpl;
import com.pms.dao.impl.ResClassifyResourceDAOImpl;
import com.pms.dao.impl.ResColumnClassifyRelationDAOImpl;
import com.pms.dao.impl.ResourceDAOImpl;
import com.pms.dao.impl.UserDAOImpl;
import com.pms.model.ResClassifyResource;
import com.pms.model.ResData;
import com.pms.model.ResRelationColumnClassify;
import com.pms.model.RowResourceColumn;
import com.pms.model.User;
import com.pms.model.UserRole;
import com.pms.webservice.model.Condition;
import com.pms.webservice.model.Item;
import com.pms.webservice.model.auth.Common010032;

public class SyncAuthenticateService extends SyncService {

	private Log logger = LogFactory.getLog(SyncAuthenticateService.class);
	
	@Override
	public String GetResult() throws Exception {
		//0. get user by user id
		UserDAO udao = new UserDAOImpl();
		User user = udao.GetUserByCertificateCodeMd5(this.getUa().getCERTIFICATE_CODE_MD5());
		if(user == null) {
			throw new Exception("there is no record returned when query user by certificate_code_md5(" + this.getUa().getCERTIFICATE_CODE_MD5() + ").");
		}
		String level = user.getSENSITIVE_LEVEL() == null || user.getSENSITIVE_LEVEL().length() == 0 ? "0" : user.getSENSITIVE_LEVEL();
		this.getAc().setSensitiveLevel(level);
		
		//1. get resource's by user id
		List<ResData> resources = queryResourceByUser(user.getCERTIFICATE_CODE_MD5());
//		List<ResData> noRightColumnsInClassifyRelation = queryNoRightColumnsInClassifyRelation(resources);
//		List<ResData> returnColumnResources = new ArrayList<ResData>();
//		returnColumnResources.addAll(resources);
//		
//		for(ResData resInNoRight : noRightColumnsInClassifyRelation) {
//			for(int x = 0; x < returnColumnResources.size(); x++) {
//				ResData resInRtn = returnColumnResources.get(x);
//				if( resInNoRight.getDATA_SET().equals(resInRtn.getDATA_SET()) 
//						&& (resInNoRight.getELEMENT().equals(resInRtn.getELEMENT()) && (resInRtn.getELEMENT_VALUE() == null || resInRtn.getELEMENT().length() == 0) ) ) {
//					returnColumnResources.remove(x);
//					x = 0;
//				}
//			}
//		}
//		
		
		//2. compare if seaching data in the query result of step 1.
		String dataset = null;
		Set<String> searchColumns = null;
		for( int i = 0; i < this.getAc().getCommon010032().size(); i++ ) {
			searchColumns = new HashSet<String>();
			dataset = this.getAc().getCommon010032().get(i).getSourceName();
			//compare subcondition
			List<Condition> subConditions = this.getAc().getCommon010032().get(i).getSubConditions();
			List<Item> subItems = this.getAc().getCommon010032().get(i).getSubItems();
			if( (subConditions == null || subConditions.size() == 0) && (subItems == null || subItems.size() == 0) ) {
				//return all privileged conditions
				HashMultimap<String,ResData> privilegedResDatas = HashMultimap.create();
				for(int j = 0; j < resources.size(); j++) {
					ResData resource = resources.get(j);
					if( dataset.equalsIgnoreCase(resource.getDATA_SET()) && resource.getELEMENT() != null && resource.getELEMENT().length() > 0 
							&& resource.getELEMENT_VALUE() != null && resource.getELEMENT_VALUE().length() > 0 
							&& resource.getOPERATE_SYMBOL() != null && resource.getOPERATE_SYMBOL().length() > 0 ) {
						privilegedResDatas.put(resource.getELEMENT(), resource);
						searchColumns.add(resource.getELEMENT());
					}
				}
				
				Set<String> keys = privilegedResDatas.keySet();
				subConditions = new ArrayList<Condition>();
			    //subItems = new ArrayList<Item>();
				for(String key:keys)
				{
				    //String result = String.format("%d:", key);
				    Set<ResData> values = privilegedResDatas.get(key);
				    Condition condition = new Condition();
				    List<Item> items = new ArrayList<Item>();
			    	for(ResData value:values) {
			    		if(condition.getRel() == null || condition.getRel().length() == 0) {
			    			if( "=".equalsIgnoreCase(value.getOPERATE_SYMBOL())) {
			    				condition.setRel("IN");
			    			}
			    		}
			    		Item itme = new Item();
			    		itme.setKey(value.getELEMENT());
			    		itme.setVal(value.getELEMENT_VALUE());
			    		itme.setEng(convertKeyToEngNameWithAlias(value.getELEMENT()));
			    		itme.setHasAccessAuth(true);
			    		items.add(itme);
				    }
			    	condition.setItems(items);				    	
			    	subConditions.add(condition);
			    	this.getAc().getCommon010032().get(i).setSubConditions(subConditions);
				}
			}
			else {
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
						searchColumns.add(item.getKey());
					}
				}
				
				for( int j = 0; j < subItems.size(); j++) {
					Item item = subItems.get(j);
					if( checkResourceAccessRights(resources, dataset, item.getKey(), item.getVal()) ) {
						item.setHasAccessAuth(true);
					} else {
						item.setHasAccessAuth(false);
					}
					searchColumns.add(item.getKey());
				}
			}
			
			//compare return column
			
			List<Item> retColumns = this.getAc().getCommon010032().get(i).getItems();
			if( retColumns == null || retColumns.size() == 0 ) {
				//return all privileged columns 
				//this a simple resource compare, no relation deal
				retColumns = new ArrayList<Item>();
				for(int j = 0; j < resources.size(); j++) {
					ResData resource = resources.get(j);
					if( dataset.equalsIgnoreCase(resource.getDATA_SET()) && resource.getELEMENT() != null && resource.getELEMENT().length() > 0 
							&& (resource.getELEMENT_VALUE() == null || resource.getELEMENT_VALUE().length() == 0) 
							&& (resource.getOPERATE_SYMBOL() == null || resource.getOPERATE_SYMBOL().length() == 0) ) {
						Item item = new Item();
						item.setKey(resource.getELEMENT());
						item.setVal("");
						item.setEng(convertKeyToEngNameWithAlias(resource.getELEMENT()));
			    		if( hasClassifyRelationRight(dataset, item, searchColumns, resources) ) {
			    			item.setHasAccessAuth(true);
			    		}
			    		else {
			    			item.setHasAccessAuth(false);
			    		}
			    		retColumns.add(item);
					}
				}
				this.getAc().getCommon010032().get(i).setItems(retColumns);
			}
			else {
				for(int j = 0; j < retColumns.size(); j++) {
					Item column = retColumns.get(j);
					if( checkResourceAccessRights(resources, dataset, column.getKey(), column.getVal()) ) {
						//column.setHasAccessAuth(true);
						if( hasClassifyRelationRight(dataset, column, searchColumns, resources) ) {
							column.setHasAccessAuth(true);
			    		}
			    		else {
			    			column.setHasAccessAuth(false);
			    		}
					} else {
						column.setHasAccessAuth(false);
					}
				}
				// add unknown column which in the white list to return columns 
				List<Item> whiteListColumns = this.getAc().getCommon010032().get(i).getWhiteListItems();
				if( whiteListColumns != null && whiteListColumns.size() > 0 ) {
					retColumns.addAll(whiteListColumns);
				}
			}
		}
		
		//3. generate result
		String result = null;
		result = generateAuthResult(resources);
		return result;
	}

	private boolean hasClassifyRelationRight(String dataset, Item column, Set<String> searchColumns, List<ResData> resources) throws Exception {
		// 1. check if return column is in a classify
		ResClassifyResourceDAO crdao = new ResClassifyResourceDAOImpl();
		ResClassifyResource classifyResRtn = crdao.QueryResClassifyResource(dataset, column.getKey());
		if(classifyResRtn == null) {
			return true;
		}
		
		for(String searchItem : searchColumns) {
			// 2. check if search column is in a classify
			ResClassifyResource classifyResSearch = crdao.QueryResClassifyResource(dataset, searchItem);
			if(classifyResSearch == null) {
				continue;
			}
			
			// 3. check if classify of 'search-->return' is a classify relation
			ResColumnClassifyRelationDAO ccrdao = new ResColumnClassifyRelationDAOImpl();
			ResRelationColumnClassify ccr = ccrdao.QueryResRelationColumnClassify( classifyResSearch.getSECTION_CLASS(), classifyResRtn.getSECTION_CLASS() );
			if( ccr == null ) {
				continue;
			}
			
			// 4. check if 'search-->return classify relation' is a classify relation resource
			ResourceDAO rdao = new ResourceDAOImpl();
			ResData crr = rdao.GetClassifyRelationResourceByRelationId( dataset, ccr.getSECTION_RELATIOIN_CLASS() );
			if( crr == null ) {
				continue;
			}
			
			// 5. query return column's classify resource and check if user has its privilege
			boolean hasRes = false;
			ResData crRtnCol = rdao.GetClassifyResourceByClassifyAndElement(dataset,  classifyResRtn.getSECTION_CLASS(), column.getKey());
			if( crRtnCol == null ) {
				return false;
			}
			for( ResData res : resources ) {
				if( res.getRESOURCE_ID().equals(crRtnCol.getRESOURCE_ID()) ) {
					hasRes = true;
					break;
				}
			}
			if( !hasRes ) {
				return false;
			}
			
			// 6. query search column's classify resource and check if user has its privilege
			hasRes = false;
			ResData crSearchCol = rdao.GetClassifyResourceByClassifyAndElement(dataset, classifyResSearch.getSECTION_CLASS(), searchItem);
			if( crSearchCol == null ) {
				return false;
			}
			for( ResData res : resources ) {
				if( res.getRESOURCE_ID().equals(crSearchCol.getRESOURCE_ID()) ) {
					hasRes = true;
					break;
				}
			}
			if( !hasRes ) {
				return false;
			}
			
			// 7. check if user has the classifyrelationresource of step 4
			hasRes = false;
			for( ResData res : resources ) {
				if( res.getRESOURCE_ID().equals(crr.getRESOURCE_ID()) ) {
					hasRes = true;
					break;
				}
			}
			if( !hasRes ) {
				return false;
			}
		}
		return true;
	}

//	private List<ResData> queryNoRightColumnsInClassifyRelation(List<ResData> resources) throws Exception {
//		// [-----------------all classify relation columns--------------------------------]
//		// [------user's classify relation resource--------]
//		//                 [-----------user's classify resource---------------------------]
//		//                 [--------user's column----------]
//		//                                                 [---user's no right column-----]
//		
//		// 1. get all classify relation columns
////		ResClassifyResourceDAO rcrdao = new ResClassifyResourceDAOImpl();
////		List<ResClassifyResource> crs = rcrdao.QueryAllResClassifyResource();
//		
//		// 2 get all classify relation resource by user
//		List<ResData> classifyRelationResources = new ArrayList<ResData>();
//		for(ResData res: resources) {
//			if( (res.getELEMENT() == null || res.getELEMENT().length() == 0) && (res.getELEMENT_VALUE() == null || res.getELEMENT_VALUE().length() == 0 )
//					&& (res.getSECTION_RELATIOIN_CLASS() != null && res.getSECTION_RELATIOIN_CLASS().length() > 0) ) {
//				classifyRelationResources.add(res);
//			}
//		}
//		
//		// 3. get all classify resource by user
//		List<ResData> userClassifyResources = new ArrayList<ResData>();
//		for(ResData res: resources) {
//			if( (res.getELEMENT() != null && res.getELEMENT().length() > 0) && (res.getELEMENT_VALUE() == null || res.getELEMENT_VALUE().length() == 0 )
//					&& (res.getSECTION_CLASS() != null && res.getSECTION_CLASS().length() > 0)
//					&& (res.getSECTION_RELATIOIN_CLASS() == null || res.getSECTION_RELATIOIN_CLASS().length() == 0) ) {
//				userClassifyResources.add(res);
//			}
//		}
//		
//		// 4. get intersection of step 2 and step 3
//		// 4.1 get all classify resource by user's classify relation resource
//		List<ResData> classifyResourcesInCRR = new ArrayList<ResData>();
//		ResColumnClassifyRelationDAO ccrdao = new ResColumnClassifyRelationDAOImpl();
//		List<ResRelationColumnClassify> rccs = ccrdao.QueryAllResRelationColumnClassify();
//		ResourceDAO rdao = new ResourceDAOImpl();
//		List<ResData> crsTemp = null;
//		for(ResData res: classifyRelationResources) {
//			for(ResRelationColumnClassify rcc : rccs) {
//				if( rcc.getSECTION_RELATIOIN_CLASS().equals(res.getSECTION_RELATIOIN_CLASS()) ) {
////					crsTemp = rdao.GetDataByRelationColumnClassify(res.getDATA_SET(), rcc.getSRC_CLASS_CODE());
////					for(ResData resInTemp : crsTemp) {
////						boolean isExist = false;
////						for(ResData resInCRR : classifyResourcesInCRR) {
////							if( resInTemp.getRESOURCE_ID().equals(resInCRR.getRESOURCE_ID())) {
////								isExist = true;
////							}
////						}
////						if(!isExist) {
////							classifyResourcesInCRR.add(resInTemp);
////						}
////					}
//					crsTemp = rdao.GetDataByRelationColumnClassify(res.getDATA_SET(), rcc.getDST_CLASS_CODE());
//					for(ResData resInTemp : crsTemp) {
//						boolean isExist = false;
//						for(ResData resInCRR : classifyResourcesInCRR) {
//							if( resInTemp.getRESOURCE_ID().equals(resInCRR.getRESOURCE_ID())) {
//								isExist = true;
//							}
//						}
//						if(!isExist) {
//							classifyResourcesInCRR.add(resInTemp);
//						}
//					}
//					break;
//				}
//			}
//		}
//		
//		// 4.2 get intersection of step 4.1 and step 3
//		List<ResData> classifyResources = new ArrayList<ResData>();
//		for(ResData res3 : userClassifyResources) {
//			for(ResData res4_1 : classifyResourcesInCRR) {
//				if(res3.getRESOURCE_ID().equals(res4_1.getRESOURCE_ID())) {
//					classifyResources.add(res4_1);
//				}
//			}
//		}
//		
//		// 5 get no rights column of classify relation
//		List<ResData> noRightsColumns = new ArrayList<ResData>();
//		for(ResData res3 : userClassifyResources) {
//			boolean isExist = false;
//			for(ResData res4 : classifyResources) {
//				if( res3.getRESOURCE_ID().equals(res4.getRESOURCE_ID()) ) {
//					isExist = true;
//					break;
//				}
//			}
//			if(!isExist) {
//				noRightsColumns.add(res3);
//			}
//		}
//		
//		return noRightsColumns;
//	}

	private List<ResData> queryResourceByUser(String userId) throws Exception {
		// 1. get user's role
		PrivilegeDAO pdao = new PrivilegeDAOImpl();
		List<UserRole> roles = pdao.GetUserRoleByUserId(userId);
		
		// 2. get resource of role
		List<ResData> result = new ArrayList<ResData>();
		ResourceDAO rdao = new ResourceDAOImpl();
		for(int i = 0; i < roles.size(); i++) {
			List<ResData> dataResources = rdao.GetDatasByRole(getBusinessRoleByRoleId( roles.get(i).getBUSINESS_ROLE() ));

			for(int j = 0; j < result.size(); j ++) {
				for(int k = 0; k < dataResources.size(); k ++) {
					if(result.get(j).getId() == dataResources.get(k).getId()) {
						dataResources.remove(k);
						break;
					}
				}
			}
			result.addAll(dataResources);
		}
		
		return result;
	}

//	private List<ResData> queryResourceByUser_1611bak(String userId) throws Exception {
//		// get user's role
//		PrivilegeDAO pdao = new PrivilegeDAOImpl();
//		List<Privilege> privileges = pdao.QueryPrivilegesByOwnerId(userId, Privilege.OWNERTYPEUSER);
//		
//		// get use's usergroup
//		GroupDAO gdao = new GroupDAOImpl();
//		List<GroupUser> ugs = gdao.GetGroupsByUserId(userId);
//		
//		// get usegroup's role
//		for(int i = 0; i < ugs.size(); i++) {
//			List<Privilege> privilegesOfUserGroup = pdao.QueryPrivilegesByOwnerId(ugs.get(i).getGroupid(), Privilege.OWNERTYPEUSERGROUP);
//			privileges.addAll(privilegesOfUserGroup);
//		}
//		
//		// TODO organization id's type has changed to string, so handle it later
//		// get user's org
//		
//		// get org's role
//		
//		// get resource of role
//		List<ResData> result = new ArrayList<ResData>();
//		ResourceDAO rdao = new ResourceDAOImpl();
//		for(int i = 0; i < privileges.size(); i++) {
//			List<ResData> dataResources = rdao.GetDatasByRole(getBusinessRoleByRoleId( privileges.get(i).getRole_id() ));
////			boolean isExist = false;
////			for(int j = 0; j < dataResources.size(); j++) {
////				for(int k = 0; k < result.size(); k++) {
////					if( dataResources.get(j).getId() == result.get(k).getId() ) {
////						isExist = true;
////						break;
////					}
////				}
////				if(isExist) {
////					isExist = false;
////				}
////				else {
////					result.add(dataResources.get(j));
////				}
////			}
//			for(int j = 0; j < result.size(); j ++) {
//				for(int k = 0; k < dataResources.size(); k ++) {
//					if(result.get(j).getId() == dataResources.get(k).getId()) {
//						dataResources.remove(k);
//						break;
//					}
//				}
//			}
//			result.addAll(dataResources);
//		}
//		return result;
//	}
	
	private String getBusinessRoleByRoleId(String role_id) {
		String businessRole = "" + role_id;
		return businessRole;
	}

	private boolean checkResourceAccessRights(List<ResData> resources,
			String dataset, String column, String value) throws Exception {
		boolean result = false;
		boolean isRowColumn = false;
		ResourceDAO rdao = new ResourceDAOImpl();
		RowResourceColumn col = rdao.GetRowResourceColumnsByElement(column);
		if(col != null) {
			isRowColumn = true;
		}
		for(int i = 0; i < resources.size(); i++) {
			ResData res = resources.get(i);
			
			if ( dataset == null || dataset.length() == 0 || column == null || column.length() == 0 ) {
				continue;
			}
			
			if ( dataset.equals(res.getDATA_SET()) && column.equals(res.getELEMENT()) && (res.getSECTION_CLASS() == null || res.getSECTION_CLASS().length() == 0)) {
				if(isRowColumn) {
					//if is row resource column, then check value
					if ( value != null && value.length() != 0 ) {
						if ( value.equals(res.getELEMENT_VALUE()) ) {
							result = true;
							break;
						} else {
							continue;
						}
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

	private String generateAuthResult(List<ResData> userResources) throws IOException {
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
			itemSetAttribute(item010004, "val", "" + new Date().getTime()/1000);
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
			
//			// 1- WA_COMMON_010038
//			Element dataset010038 = null;
//			dataset010038 = new Element("DATASET");
//			message.addContent(dataset010038);
//			dataset010038.setAttribute("name", "WA_COMMON_010038");
//			dataset010038.setAttribute("rmk", "可访问的字段关系，数据级别");
//			
//			Element data010038 = null;
//			data010038 = new Element("DATA");
//			dataset010038.addContent(data010038);
//			
//			Element item010038 = null;
//			item010038 = new Element("ITEM");
//			data010038.addContent(item010038);
//			itemSetAttribute(item010038, "key", "H010034");
//			itemSetAttribute(item010038, "eng", "SENSITIVE_LEVEL");
//			itemSetAttribute(item010038, "chn", "最高敏感级别");
//			itemSetAttribute(item010038, "val", this.getAc().getSensitiveLevel());			
			
			// 1- WA_COMMON_010034
			Element dataset010034 = null;
			dataset010034 = new Element("DATASET");
			message.addContent(dataset010034);
			dataset010034.setAttribute("name", "WA_COMMON_010034");
			dataset010034.setAttribute("rmk", "鉴权结果信息");

			Element data010034 = null;
			data010034 = new Element("DATA");
			dataset010034.addContent(data010034);
//			
//			// 2-- WA_COMMON_010034 --> data --> CONDITION(STC)
//			Element condition010034 = null;
//			condition010034 = new Element("CONDITION");
//			data010034.addContent(condition010034);
//			condition010034.setAttribute("rel", this.getAc().getStc().getRel());
			
//			Element itemSTC = null;
//			List<Item> items = this.getAc().getStc().getItems();
//			for( int i = 0; i < items.size(); i++ ) {
//				Item currentItem = items.get(i);
//				itemSTC = new Element("ITEM");
//				condition010034.addContent(itemSTC);
//				itemSetAttribute(itemSTC, "key", currentItem.getKey());
//				itemSetAttribute(itemSTC, "eng", currentItem.getEng());
//				itemSetAttribute(itemSTC, "val", currentItem.getVal());
//			}
			
			// 2-- WA_COMMON_010034 --> data --> WA_COMMON_010038
			Element dataset010038 = null;
			dataset010038 = new Element("DATASET");
			data010034.addContent(dataset010038);
			dataset010038.setAttribute("name", "WA_COMMON_010038");
			dataset010038.setAttribute("rmk", "数据级别等用户属性和公共属性");
			
			Element data010038 = null;
			data010038 = new Element("DATA");
			dataset010038.addContent(data010038);
			
			Element item010038 = null;
			item010038 = new Element("ITEM");
			data010038.addContent(item010038);
			itemSetAttribute(item010038, "key", "H010034");
			itemSetAttribute(item010038, "eng", "SENSITIVE_LEVEL");
			itemSetAttribute(item010038, "chn", "用户数据敏感级别");
			itemSetAttribute(item010038, "val", this.getAc().getSensitiveLevel());			
	
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
				
				// 4---- WA_COMMON_010034 --> data --> WA_COMMON_010036 --> data(loop) --> ITEM(search-id)
				Element itemSearchID = null;
				itemSearchID = new Element("ITEM");
				data010036.addContent(itemSearchID);
				itemSetAttribute(itemSearchID, "key", "H010005");
				itemSetAttribute(itemSearchID, "eng", "SEARCH_ID");
				itemSetAttribute(itemSearchID, "chn", "查询标识");
				itemSetAttribute(itemSearchID, "val", common010032.getSyncKey());	
				
				// 5---- WA_COMMON_010034 --> data --> WA_COMMON_010036 --> data(loop) --> dataset(result)
				Element datasetResult = null;
				datasetResult = new Element("DATASET");
				data010036.addContent(datasetResult);
				datasetResult.setAttribute("name", common010032.getSourceName());
				
				// 6----- WA_COMMON_010034 --> data --> WA_COMMON_010036 --> data(loop) --> dataset(result) --> data(result)
				Element dataResult = null;
				dataResult = new Element("DATA");
				datasetResult.addContent(dataResult);

		//parse auth result
				boolean hasAuthCondition = false, hasAuthColumn = false, hasUnauthCondition = false, hasUnauthColumn = false;
				// 7------ WA_COMMON_010034 --> data --> WA_COMMON_010036 --> data(loop) --> dataset(result) --> data(result) --> dataset(success)
				Element datasetSuccess = null;
				datasetSuccess = new Element("DATASET");
				dataResult.addContent(datasetSuccess);
				datasetSuccess.setAttribute("name", "WA_COMMON_010035");
				datasetSuccess.setAttribute("rmk", "鉴权成功部分");
				
				// 8------- traverse conditions, find success record
				Element dataSuccessCondition = null;
				dataSuccessCondition = new Element("DATA");
				
				Element conditionParent = null;
				conditionParent = new Element("CONDITION");
				dataSuccessCondition.addContent(conditionParent);
				conditionParent.setAttribute("rel", common010032.getParentCondition());
				
				// 8.1----- deal with subconditions(subconditions in parent condition)
				List<Condition> subConditions = common010032.getSubConditions();
				for( int j = 0; j < subConditions.size(); j++ ) {
					Condition condition = subConditions.get(j);
					boolean isLink2Parent = false;
					List<Item> subConditionItems = condition.getItems();
					
					Element subCondition = null;
					subCondition = new Element("CONDITION");
					//conditionParent.addContent(subCondition);
					subCondition.setAttribute("rel", condition.getRel());
					
					if("IN".equalsIgnoreCase(condition.getRel())) {
						Element itemCondition = null;
						itemCondition = new Element("ITEM");
						String value = "";
						for( int k = 0; k < subConditionItems.size(); k++ ) {
							Item item = subConditionItems.get(k);
							
							itemSetAttribute(itemCondition, "key", item.getKey());
							itemSetAttribute(itemCondition, "eng", item.getEng());
												
							if( item.isHasAccessAuth() ) {
								isLink2Parent = true;
								hasAuthCondition = true;
								value += item.getVal() + ","; 
							}
						}
						
						if(value.length()>0) {
							value = value.substring(0, value.length()-1);//delete the last comma
							itemSetAttribute(itemCondition, "val", value);
							subCondition.addContent(itemCondition);
						}
					}
					else {
						for( int k = 0; k < subConditionItems.size(); k++ ) {
							Item item = subConditionItems.get(k);
							Element itemCondition = null;
							itemCondition = new Element("ITEM");
							
							itemSetAttribute(itemCondition, "key", item.getKey());
							itemSetAttribute(itemCondition, "eng", item.getEng());
							itemSetAttribute(itemCondition, "val", item.getVal());
							
							if( "H010014".equals(item.getKey()) ) {
								isLink2Parent = true;
								subCondition.addContent(itemCondition);
							}
							else if( item.isHasAccessAuth() ) {
								isLink2Parent = true;
								hasAuthCondition = true;
								subCondition.addContent(itemCondition);
							}
							else {
								//if not time, and don't have authenticated condition, then do nothing
							}
						}
					}
					
					if ( isLink2Parent ) {
						conditionParent.addContent(subCondition);
					}
				}
				
				// 8.2----- deal with columns(items in parent condition)
				List<Item> subItems = common010032.getSubItems();
				for( int j = 0; j < subItems.size(); j++ ) {
					Item item = subItems.get(j);
					if(item.isHasAccessAuth()) {
						hasAuthCondition = true;
						Element itemInParentCondition = null;
						itemInParentCondition = new Element("ITEM");
						conditionParent.addContent(itemInParentCondition);
						
						itemSetAttribute(itemInParentCondition, "key", item.getKey());
						itemSetAttribute(itemInParentCondition, "eng", item.getEng());
						itemSetAttribute(itemInParentCondition, "val", item.getVal());
					}
				}
				
				// 9------- traverse return columns, find success record
				Element dataSuccessColumn = null;
				dataSuccessColumn = new Element("DATA");
				
				List<Item> retColumnItems = common010032.getItems();
				if(retColumnItems == null || retColumnItems.size() == 0) {
					for( int j = 0; j < userResources.size(); j++ ) {
						ResData resource = userResources.get(j);
						if( common010032.getSourceName().equals(resource.getDATA_SET()) 
								&& resource.getELEMENT() != null && resource.getELEMENT().length() > 0 
								&& (resource.getELEMENT_VALUE() == null || resource.getELEMENT_VALUE().length() == 0) ) {
							Element itemRetColumn = null;
							itemRetColumn = new Element("ITEM");
							
							itemSetAttribute(itemRetColumn, "key", resource.getELEMENT());
							itemSetAttribute(itemRetColumn, "eng", convertKeyToEngNameWithAlias(resource.getELEMENT()));
							itemSetAttribute(itemRetColumn, "val", "");
							hasAuthColumn = true;
							dataSuccessColumn.addContent(itemRetColumn);
						}
					}
				}
				else {
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
				}
				
				if( (subConditions == null || subConditions.size() == 0) && (subItems == null || subItems.size() == 0) ) {
					if( hasAuthColumn ) {
						datasetSuccess.addContent(dataSuccessCondition);
						datasetSuccess.addContent(dataSuccessColumn);
					}
				}
				else {
					if( hasAuthCondition && hasAuthColumn ) {
						datasetSuccess.addContent(dataSuccessCondition);
						datasetSuccess.addContent(dataSuccessColumn);
					}
					else {
						// if there is no authenticated condition or there is no authenticated column ,then nothing success returned.
					}
				}
				
				// 7------ WA_COMMON_010034 --> data --> WA_COMMON_010036 --> data(loop) --> dataset(result) --> data(result) --> dataset(fail)
				Element datasetFail = null;
				datasetFail = new Element("DATASET");
				dataResult.addContent(datasetFail);
				datasetFail.setAttribute("name", "WA_COMMON_010037");
				datasetFail.setAttribute("rmk", "鉴权失败的部分");
				
				// 8------- traverse conditions, find fail record
				Element dataFailCondition = null;
				
				
				conditionParent = new Element("CONDITION");
//				if( !hasAuthCondition ) {
					dataFailCondition = new Element("DATA");
					dataFailCondition.addContent(conditionParent);
					conditionParent.setAttribute("rel", common010032.getParentCondition());
//				}
				// 8.1------ deal with subconditions(subconditions in parent condition)
				subConditions = common010032.getSubConditions();
				for( int j = 0; j < subConditions.size(); j++ ) {
					Condition condition = subConditions.get(j);
					boolean isLink2Parent = false;
					List<Item> subConditionItems = condition.getItems();
					
					Element subCondition = null;
					subCondition = new Element("CONDITION");
					//conditionParent.addContent(subCondition);
					subCondition.setAttribute("rel", condition.getRel());
					
					if("in".equalsIgnoreCase(condition.getRel())) {
						Element itemCondition = null;
						itemCondition = new Element("ITEM");
						String value = "";
						for( int k = 0; k < subConditionItems.size(); k++ ) {
							Item item = subConditionItems.get(k);
							
							itemSetAttribute(itemCondition, "key", item.getKey());
							itemSetAttribute(itemCondition, "eng", item.getEng());
							
							if( !item.isHasAccessAuth() ) {
								isLink2Parent = true;
								hasUnauthCondition = true;
								value += item.getVal() + ",";
							}
						}
						
						if(value.length()>0) {
							value = value.substring(0, value.length()-1);//delete the last comma
							itemSetAttribute(itemCondition, "val", value);
							subCondition.addContent(itemCondition);
						}
					}else {
						for( int k = 0; k < subConditionItems.size(); k++ ) {
							Item item = subConditionItems.get(k);
							Element itemCondition = null;
							itemCondition = new Element("ITEM");
							
							itemSetAttribute(itemCondition, "key", item.getKey());
							itemSetAttribute(itemCondition, "eng", item.getEng());
							itemSetAttribute(itemCondition, "val", item.getVal());
							
							if( "H010014".equals(item.getKey()) ) {
								if( !hasAuthCondition ) {
									isLink2Parent = true;
									subCondition.addContent(itemCondition);
								}
							}
							else if( !item.isHasAccessAuth() ) {
								isLink2Parent = true;
								hasUnauthCondition = true;
								subCondition.addContent(itemCondition);
							}
							else {
								//if not time, and have authenticated condition, then do nothing
							}
						}
					}
					
					
					if ( isLink2Parent ) {
						if( !hasAuthCondition ) {
							conditionParent.addContent(subCondition);
						}
						else {
							dataFailCondition.addContent(subCondition);
						}
					}
				}
				
				// 8.2----- deal with columns(items in parent condition)
				subItems = common010032.getSubItems();
				for( int j = 0; j < subItems.size(); j++ ) {
					Item item = subItems.get(j);
					if(!item.isHasAccessAuth()) {
						hasUnauthCondition = true;
						Element itemInParentCondition = null;
						itemInParentCondition = new Element("ITEM");
						conditionParent.addContent(itemInParentCondition);
						
						itemSetAttribute(itemInParentCondition, "key", item.getKey());
						itemSetAttribute(itemInParentCondition, "eng", item.getEng());
						itemSetAttribute(itemInParentCondition, "val", item.getVal());
					}
				}
				
				// 9------- traverse return columns, find fail record
				Element dataFailColumn = null;
				dataFailColumn = new Element("DATA");
				
				retColumnItems = common010032.getItems();
				if(retColumnItems == null || retColumnItems.size() == 0) {
					List<ResData> allColumnResource = queryColumnResourceByDataSet(common010032.getSourceName());
					for( int j = 0; j < userResources.size(); j++ ) {
						for( int k = 0; k < allColumnResource.size(); k++ ) {
							if( allColumnResource.get(k).getId() == userResources.get(j).getId()) {
								allColumnResource.remove(k);
								break;
							}
						}
					}
					
					for( int j = 0; j < allColumnResource.size(); j++ ) {
						ResData resource = allColumnResource.get(j);
						
						Element itemRetColumn = null;
						itemRetColumn = new Element("ITEM");
						
						itemSetAttribute(itemRetColumn, "key", resource.getELEMENT());
						itemSetAttribute(itemRetColumn, "eng", convertKeyToEngNameWithAlias(resource.getELEMENT()));
						itemSetAttribute(itemRetColumn, "val", "");
						hasUnauthColumn = true;
						dataFailColumn.addContent(itemRetColumn);
					}
				}
				else {
					for( int j = 0; j < retColumnItems.size(); j++ ) {
						Item item = retColumnItems.get(j);
						
						Element itemRetColumn = null;
						itemRetColumn = new Element("ITEM");
						
						itemSetAttribute(itemRetColumn, "key", item.getKey());
						itemSetAttribute(itemRetColumn, "eng", item.getEng());
						itemSetAttribute(itemRetColumn, "val", item.getVal());
						
						if( !item.isHasAccessAuth() ) {
							hasUnauthColumn = true;
							dataFailColumn.addContent(itemRetColumn);
						}
					}
				}
				
				if( hasUnauthCondition || hasUnauthColumn ) {
					datasetFail.addContent(dataFailCondition);
					datasetFail.addContent(dataFailColumn);
				}
				else {
					// if there isn't any unauthorized condition or unauthorized column ,then nothing fail returned.
				}
				
			}
			
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
			logger.info(result);
		}
		return result;
	}

	private List<ResData> queryColumnResourceByDataSet(String dataSet) throws Exception {
		ResourceDAO rdao = new ResourceDAOImpl();
		List<ResData> result = rdao.GetColumnDatasByDataSet(dataSet);
		return result;
	}
}

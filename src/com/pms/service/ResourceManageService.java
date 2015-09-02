package com.pms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.pms.dao.ResourceDAO;
import com.pms.dao.impl.ResourceDAOImpl;
import com.pms.dto.ResDataListItem;
import com.pms.dto.RoleListItem;
import com.pms.model.AttrDefinition;
import com.pms.model.AttrDictionary;
import com.pms.model.ResData;
import com.pms.model.ResDataOrg;
import com.pms.model.ResFeature;
import com.pms.model.ResRole;
import com.pms.model.ResRoleOrg;
import com.pms.model.ResRoleResource;

public class ResourceManageService {

	public int QueryAllFeatureItems(ResFeature criteria, int page, int rows, List<ResFeature> items) throws Exception {
		criteria.setDelete_status(ResFeature.DELSTATUSNO);
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResFeature> res = dao.GetFeatures( criteria, page, rows );
		items.addAll(res);
		int total = QueryAllFeaturesCount( criteria );
		return total;
	}
	
	private int QueryAllFeaturesCount(ResFeature criteria) throws Exception {
		criteria.setDelete_status(ResFeature.DELSTATUSNO);
		ResourceDAO dao = new ResourceDAOImpl();
		int count = dao.GetFeaturesCount( criteria );
		return count;
	}

	public ResFeature SaveResourceFeature(ResFeature feature) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		feature.setLatest_mod_time(timenow);
		feature = dao.FeatureAdd(feature);
		return feature;
	}

	public void DeleteResourceFeatures(List<Integer> resourceIds) throws Exception {
		if(resourceIds == null)
			return;
		
		ResFeature target;
		for(int i = 0; i< resourceIds.size(); i++) {
			target = new ResFeature();
			target.setId(resourceIds.get(i));

			DeleteResourceFeature(target);
		}
		
		return ;
	}

	public ResFeature DeleteResourceFeature(ResFeature feature) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResFeature> nodes=dao.GetFeatureById(feature.getId());
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());

		for(int i = 0; i< nodes.size(); i++) {
			feature.setName(nodes.get(i).getName());
			feature.setResource_id(nodes.get(i).getResource_id());
			feature.setResource_status(nodes.get(i).getResource_status());
			feature.setResource_describe(nodes.get(i).getResource_describe());
			feature.setResource_remark(nodes.get(i).getResource_remark());
			feature.setDelete_status(ResFeature.DELSTATUSYES);
			feature.setApp_id(nodes.get(i).getApp_id());
			feature.setParent_resource(nodes.get(i).getParent_resource());
			feature.setResource_order(nodes.get(i).getResource_order());
			feature.setSystem_type(nodes.get(i).getSystem_type());
			feature.setLatest_mod_time(timenow);
		
			feature = dao.FeatureAdd(feature);
		}
		
		return feature;
	}
	
	public int QueryAllDataItems(List<String> resource_status, List<String> delete_status, List<String> resource_type,
			List<String> dataset_sensitive_level, List<String> data_set, List<String> section_class, 
			List<String> lement, List<String> section_relatioin_class, 
			ResData criteria, int page, int rows, List<ResDataListItem> items) 
					throws Exception {
		criteria.setDELETE_STATUS(ResData.DELSTATUSNO);
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResData> res = dao.GetDatas( resource_status, delete_status, resource_type, 
				dataset_sensitive_level, data_set, section_class, 
				lement,section_relatioin_class,
				criteria, page, rows );
		ResDataListItem resDataListItem = null;
		for(int i=0; i<res.size(); i++) {
			resDataListItem = ConvertDatasDefinitonToResDataListItem(res.get(i));
			items.add(resDataListItem);
		}
		int total = QueryAllDatasCount( criteria );

		return total;
	}
	
	public ResDataListItem ConvertDatasDefinitonToResDataListItem(ResData attr) throws Exception {
		ResDataListItem item = new ResDataListItem();
		item.setId(attr.getId());
		item.setName(attr.getName());
		item.setRESOURCE_ID(attr.getRESOURCE_ID());
//		item.setRESOURCE_STATUS(attr.getRESOURCE_STATUS());
		item.setRESOURCE_DESCRIBE(attr.getRESOURCE_DESCRIBE());
		item.setRESOURCE_REMARK(attr.getRESOURCE_REMARK());				
//		item.setDELETE_STATUS(attr.getDELETE_STATUS());
//		item.setResource_type(attr.getResource_type());
//		item.setCLUE_DST_SYS(attr.getCLUE_DST_SYS());
		item.setDATASET_SENSITIVE_LEVEL(attr.getDATASET_SENSITIVE_LEVEL());	
		item.setDATA_SET(attr.getDATA_SET());
		item.setSECTION_CLASS(attr.getSECTION_CLASS());
		item.setELEMENT(attr.getELEMENT());
		item.setSECTION_RELATIOIN_CLASS(attr.getSECTION_RELATIOIN_CLASS());
		item.setDATA_VERSION(attr.getDATA_VERSION());
		
		ResourceDAO dao = new ResourceDAOImpl();
		List<AttrDictionary> resStatNode = dao.GetDictionarysNode(AttrDefinition.ATTR_RESOURCEDATA_RESOURCE_STATUS, attr.getRESOURCE_STATUS(), attr.getId());
		for (int i = 0; i < resStatNode.size(); i++) {
			item.setRESOURCE_STATUS(resStatNode.get(i).getValue());
		}
		
		List<AttrDictionary> delStatNode = dao.GetDictionarysNode(AttrDefinition.ATTR_RESOURCEDATA_DELETE_STATUS, attr.getDELETE_STATUS(), attr.getId());
		for (int i = 0; i < delStatNode.size(); i++) {
			item.setDELETE_STATUS(delStatNode.get(i).getValue());
		}
		
		List<AttrDictionary> resTypeNode = dao.GetDictionarysNode(AttrDefinition.ATTR_RESOURCEDATA_RESOURCE_TYPE, attr.getResource_type(), attr.getId());
		for (int i = 0; i < resTypeNode.size(); i++) {
			item.setResource_type(resTypeNode.get(i).getValue());
		}
		
		
		List<ResDataOrg> nodes = dao.GetResDataOrgByResId(attr.getRESOURCE_ID());
		
		OrgManageService oms = new OrgManageService();
		String path = null;
		for (int i = 0; i < nodes.size(); i++) {
			
			path = oms.QueryNodePath(nodes.get(i).getCLUE_DST_SYS());
			item.setPid(nodes.get(i).getCLUE_DST_SYS());
			item.setDest_data_version(nodes.get(i).getDATA_VERSION());
		}
		if(path != null && path.length() > 0){
			int index = path.lastIndexOf('/');
			String pname = "";
			if( -1 == index ) {
				pname = path;
			}
			else {
				pname = path.substring(path.lastIndexOf('/')+1, path.length());
			}
			item.setPname(pname);
		}
		
		List<AttrDictionary> attrDicts = dao.GetDatasDictionarys(attr.getId());
		List data = new ArrayList();
		for(int i = 0; i < attrDicts.size(); i++) {
			AttrDictionary attrDictionary=new AttrDictionary();

			attrDictionary.setId(attrDicts.get(i).getId());
			attrDictionary.setAttrid(attrDicts.get(i).getAttrid());
			attrDictionary.setValue(attrDicts.get(i).getValue());
			attrDictionary.setCode(attrDicts.get(i).getCode());
			attrDictionary.setTstamp(attrDicts.get(i).getTstamp());
			data.add(attrDictionary);
		}
		
		item.setValue(data);

		return item;
	}	
	
	private int QueryAllDatasCount(ResData criteria) throws Exception {
		criteria.setDELETE_STATUS(ResData.DELSTATUSNO);
		ResourceDAO dao = new ResourceDAOImpl();
		int count = dao.GetDatasCount( criteria );
		return count;
	}

	public ResData SaveResourceData(ResData data, ResDataOrg resDataOrg) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		data.setLATEST_MOD_TIME(timenow);
		data.setDATA_VERSION(data.getDATA_VERSION()+1);
		data = dao.DataAdd(data);
		
		if(resDataOrg.getCLUE_DST_SYS() !=null && resDataOrg.getCLUE_DST_SYS().length() != 0){
			List<ResDataOrg> resDataOrgNodes=dao.GetResDataOrgByResId(data.getRESOURCE_ID());
			for (int i = 0; i < resDataOrgNodes.size(); i++) {	
				resDataOrg.setId(resDataOrgNodes.get(i).getId());
			}
			resDataOrg.setRESOURCE_ID(data.getRESOURCE_ID());
			resDataOrg.setCLUE_DST_SYS(resDataOrg.getCLUE_DST_SYS());
			resDataOrg.setDATA_VERSION(resDataOrg.getDATA_VERSION()+1);
			resDataOrg.setLATEST_MOD_TIME(timenow);
			resDataOrg = dao.ResDataOrgAdd(resDataOrg);
		}
		return data;
	}
	
	public void DeleteResourceDatas(List<Integer> resourceIds) throws Exception {
		if(resourceIds == null)
			return;
		
		ResData target;
		for(int i = 0; i< resourceIds.size(); i++) {
			target = new ResData();
			target.setId(resourceIds.get(i));

			DeleteResourceData(target);
		}
		
		return ;
	}

	public ResData DeleteResourceData(ResData data) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResData> nodes=dao.GetDataById(data.getRESOURCE_ID());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		
		for(int i = 0; i< nodes.size(); i++) {
			data.setName(nodes.get(i).getName());
			data.setResource_type(nodes.get(i).getResource_type());
			data.setRESOURCE_ID(nodes.get(i).getRESOURCE_ID());
			data.setRESOURCE_STATUS(nodes.get(i).getRESOURCE_STATUS());
			data.setRESOURCE_DESCRIBE(nodes.get(i).getRESOURCE_DESCRIBE());
			data.setDATASET_SENSITIVE_LEVEL(nodes.get(i).getDATASET_SENSITIVE_LEVEL());
			data.setDATA_SET(nodes.get(i).getDATA_SET());
			data.setSECTION_CLASS(nodes.get(i).getSECTION_CLASS());
			data.setELEMENT(nodes.get(i).getELEMENT());
			data.setSECTION_RELATIOIN_CLASS(nodes.get(i).getSECTION_RELATIOIN_CLASS());
			data.setOPERATE_SYMBOL(nodes.get(i).getOPERATE_SYMBOL());
			data.setELEMENT_VALUE(nodes.get(i).getELEMENT_VALUE());
			data.setDELETE_STATUS(ResData.DELSTATUSYES);
			data.setDATA_VERSION(nodes.get(i).getDATA_VERSION());	
			data.setLATEST_MOD_TIME(timenow);
			data.setRESOURCE_REMARK(nodes.get(i).getRESOURCE_REMARK());
		
			data = dao.DataAdd(data);
			
			List<ResDataOrg> resDataOrgNodes=dao.GetResDataOrgByResId(data.getRESOURCE_ID());
			for (int j = 0; j < resDataOrgNodes.size(); j++) {
				ResDataOrg resDataOrg=new ResDataOrg();
				resDataOrg.setId(resDataOrgNodes.get(j).getId());
				resDataOrg.setRESOURCE_ID(resDataOrgNodes.get(j).getRESOURCE_ID());
				resDataOrg.setCLUE_DST_SYS(resDataOrgNodes.get(j).getCLUE_DST_SYS());
				resDataOrg.setDELETE_STATUS(ResDataOrg.DELSTATUSYES);
				resDataOrg.setDATA_VERSION(resDataOrgNodes.get(j).getDATA_VERSION());
				resDataOrg.setLATEST_MOD_TIME(timenow);
				
				resDataOrg = dao.ResDataOrgAdd(resDataOrg);
			}
		}
		
		return data;
	}

	public int QueryAllRoleItems(ResRole criteria, int page, int rows,
			List<RoleListItem> items) throws Exception {
		criteria.setDELETE_STATUS(ResRole.DELSTATUSNO);
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResRole> res = dao.GetRoles( criteria, page, rows );
		RoleListItem roleItem = null;
		for(int i=0; i<res.size(); i++) {
			roleItem = ConvertRoleToListItem(res.get(i));
			items.add(roleItem);
		}
		int total = QueryAllRolesCount( criteria );
		return total;
	}
	
	public RoleListItem ConvertRoleToListItem(ResRole resRole) throws Exception {
		RoleListItem item = new RoleListItem();
		item.setId(resRole.getId());
		item.setBUSINESS_ROLE(resRole.getBUSINESS_ROLE());
		item.setBUSINESS_ROLE_TYPE(resRole.getBUSINESS_ROLE_TYPE());
		item.setBUSINESS_ROLE_NAME(resRole.getBUSINESS_ROLE_NAME());
		item.setSYSTEM_TYPE(resRole.getSYSTEM_TYPE());
		item.setCLUE_SRC_SYS(resRole.getCLUE_SRC_SYS());				
		item.setROLE_DESC(resRole.getROLE_DESC());
		item.setDELETE_STATUS(resRole.getDELETE_STATUS());
		item.setDATA_VERSION(resRole.getDATA_VERSION());	
		item.setLATEST_MOD_TIME(resRole.getLATEST_MOD_TIME());
//		item.setCLUE_DST_SYS(attr.getCLUE_DST_SYS());
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResRoleOrg> nodes = dao.GetResRoleOrgByRoleid(resRole.getBUSINESS_ROLE());
		
		OrgManageService oms = new OrgManageService();
		String path = null;
		for (int i = 0; i < nodes.size(); i++) {
			
			path = oms.QueryNodePath(nodes.get(i).getCLUE_DST_SYS());
			item.setPid(nodes.get(i).getCLUE_DST_SYS());
			item.setDest_data_version(nodes.get(i).getDATA_VERSION());
		}
		if(path != null && path.length() > 0){
			int index = path.lastIndexOf('/');
			String pname = "";
			if( -1 == index ) {
				pname = path;
			}
			else {
				pname = path.substring(path.lastIndexOf('/')+1, path.length());
			}
			item.setPname(pname);
		}
		
		List<AttrDictionary> attrDicts = dao.GetRolesDictionarys(resRole.getId());
		List data = new ArrayList();
		for(int i = 0; i < attrDicts.size(); i++) {
			AttrDictionary attrDictionary=new AttrDictionary();

			attrDictionary.setId(attrDicts.get(i).getId());
			attrDictionary.setAttrid(attrDicts.get(i).getAttrid());
			attrDictionary.setValue(attrDicts.get(i).getValue());
			attrDictionary.setCode(attrDicts.get(i).getCode());
			attrDictionary.setTstamp(attrDicts.get(i).getTstamp());
			data.add(attrDictionary);
		}
		
		item.setValue(data);

		return item;
	}
	
	private int QueryAllRolesCount(ResRole criteria) throws Exception {
		criteria.setDELETE_STATUS(ResRole.DELSTATUSNO);
		ResourceDAO dao = new ResourceDAOImpl();
		int count = dao.GetRolesCount( criteria );
		return count;
	}
	
	public ResRole SaveResourceRole(ResRole role, ResRoleOrg resRoleOrg, List<String> featureIds, List<String> dataIds) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		
		role.setLATEST_MOD_TIME(timenow);
		role.setDATA_VERSION(role.getDATA_VERSION()+1);
		role = dao.RoleAdd(role);
		
		if(resRoleOrg.getCLUE_DST_SYS() !=null && resRoleOrg.getCLUE_DST_SYS().length() != 0){
			List<ResRoleOrg> resRoleOrgNodes=dao.GetResRoleOrgByRoleid(role.getBUSINESS_ROLE());
			for (int i = 0; i < resRoleOrgNodes.size(); i++) {	
				resRoleOrg.setId(resRoleOrgNodes.get(i).getId());
			}
			resRoleOrg.setBUSINESS_ROLE(role.getBUSINESS_ROLE());
			resRoleOrg.setCLUE_DST_SYS(resRoleOrg.getCLUE_DST_SYS());
			resRoleOrg.setDATA_VERSION(resRoleOrg.getDATA_VERSION()+1);
			resRoleOrg.setLATEST_MOD_TIME(timenow);
			resRoleOrg = dao.ResRoleOrgAdd(resRoleOrg);
		}
		
		dao.UpdateFeatureRoleResource(role.getBUSINESS_ROLE(), featureIds);
		dao.UpdateDataRoleResource(role.getBUSINESS_ROLE(), dataIds);
		return role;
	}

	public void DeleteResourceRoles(List<Integer> roleIds) throws Exception {
		if(roleIds == null)
			return;
		
		ResRole target;
		for(int i = 0; i< roleIds.size(); i++) {
			target = new ResRole();
			target.setId(roleIds.get(i));
			
			DeleteResourceRole(target);
		}
		
		return ;
	}
	
	public ResRole DeleteResourceRole(ResRole role) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResRole> roleNodes=dao.GetRoleById(role.getId());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());

		for(int i = 0; i< roleNodes.size(); i++) {
			role.setBUSINESS_ROLE(roleNodes.get(i).getBUSINESS_ROLE());
			role.setBUSINESS_ROLE_TYPE(roleNodes.get(i).getBUSINESS_ROLE_TYPE());
			role.setBUSINESS_ROLE_NAME(roleNodes.get(i).getBUSINESS_ROLE_NAME());
			role.setSYSTEM_TYPE(roleNodes.get(i).getSYSTEM_TYPE());
			role.setCLUE_SRC_SYS(roleNodes.get(i).getCLUE_SRC_SYS());
			role.setROLE_DESC(roleNodes.get(i).getROLE_DESC());
			role.setDELETE_STATUS(ResRole.DELSTATUSYES);
			role.setDATA_VERSION(roleNodes.get(i).getDATA_VERSION());
			role.setLATEST_MOD_TIME(timenow);
		
			role = dao.RoleAdd(role);
			
			List<ResRoleResource> roleResNodes=dao.GetRoleResourcesByRoleid(roleNodes.get(i).getBUSINESS_ROLE());
			for (int j = 0; j < roleResNodes.size(); j++) {
				ResRoleResource resRole=new ResRoleResource();
				resRole.setRESOURCE_ID(roleResNodes.get(j).getRESOURCE_ID());
				resRole.setBUSINESS_ROLE(roleResNodes.get(j).getBUSINESS_ROLE());
				resRole.setRestype(roleResNodes.get(j).getRestype());
				resRole.setDELETE_STATUS(ResRoleResource.DELSTATUSYES);
				resRole.setDATA_VERSION(roleResNodes.get(j).getDATA_VERSION());
				resRole.setLATEST_MOD_TIME(timenow);
				
				resRole = dao.ResRoleResourceAdd(resRole);
			}
			
			List<ResRoleOrg> resRoleOrgNodes=dao.GetResRoleOrgByRoleid(roleNodes.get(i).getBUSINESS_ROLE());
			for (int j = 0; j < resRoleOrgNodes.size(); j++) {
				ResRoleOrg resRoleOrg=new ResRoleOrg();
				resRoleOrg.setId(resRoleOrgNodes.get(j).getId());
				resRoleOrg.setBUSINESS_ROLE(resRoleOrgNodes.get(j).getBUSINESS_ROLE());
				resRoleOrg.setCLUE_DST_SYS(resRoleOrgNodes.get(j).getCLUE_DST_SYS());
				resRoleOrg.setDELETE_STATUS(ResRoleOrg.DELSTATUSYES);
				resRoleOrg.setDATA_VERSION(resRoleOrgNodes.get(j).getDATA_VERSION());
				resRoleOrg.setLATEST_MOD_TIME(timenow);
				
				resRoleOrg = dao.ResRoleOrgAdd(resRoleOrg);
			}
		}
		
		return role;
	}
	
	public void QueryRoleResource(String id, List<ResFeature> features,
			List<ResDataListItem> items) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResRoleResource> rrs = dao.GetRoleResourcesByRoleid(id);
		
		for(int i=0; i<rrs.size(); i++) {
			if ( rrs.get(i).getRestype() == ResRoleResource.RESTYPEFEATURE ) {
				ResFeature feature = dao.GetFeatureByResId( rrs.get(i).getRESOURCE_ID() );
				features.add(feature);
			}
			else if( rrs.get(i).getRestype() == ResRoleResource.RESTYPEDATA ) {
				List<ResData> data = dao.GetDataById( rrs.get(i).getRESOURCE_ID() );
				ResDataListItem resDataListItem = null;
				for(int j=0; j<data.size(); j++) {
					resDataListItem = ConvertDatasDefinitonToResDataListItem( data.get(j) );
				}
				items.add(resDataListItem);
				
			}
		}
		
	}
}

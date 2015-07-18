package com.pms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.pms.dao.ResourceDAO;
import com.pms.dao.impl.ResourceDAOImpl;
import com.pms.dto.ResDataListItem;
import com.pms.model.AttrDictionary;
import com.pms.model.ResData;
import com.pms.model.ResFeature;
import com.pms.model.ResRole;
import com.pms.model.ResRoleResource;

public class ResourceManageService {

	public int QueryAllFeatureItems(ResFeature criteria, int page, int rows, List<ResFeature> items) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResFeature> res = dao.GetFeatures( criteria, page, rows );
		items.addAll(res);
		int total = QueryAllFeaturesCount( criteria );
		return total;
	}
	
	private int QueryAllFeaturesCount(ResFeature criteria) throws Exception {
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
		ResourceDAO dao = new ResourceDAOImpl();
		for(int i = 0; i< resourceIds.size(); i++) {
			target = new ResFeature();
			target.setId(resourceIds.get(i));
			dao.FeatureDel(target);
		}
		
		return ;
	}
	
	public int QueryAllDataItems(List<String> resource_status, ResData criteria, int page, int rows, List<ResDataListItem> items) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResData> res = dao.GetDatas( resource_status, criteria, page, rows );
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
		item.setRESOURCE_STATUS(attr.getRESOURCE_STATUS());
		item.setRESOURCE_DESCRIBE(attr.getRESOURCE_DESCRIBE());
		item.setRESOURCE_REMARK(attr.getRESOURCE_REMARK());				
		item.setDELETE_STATUS(attr.getDELETE_STATUS());
		item.setResource_type(attr.getResource_type());	
//		item.setCLUE_DST_SYS(attr.getCLUE_DST_SYS());
		item.setDATASET_SENSITIVE_LEVEL(attr.getDATASET_SENSITIVE_LEVEL());	
		item.setDATA_SET(attr.getDATA_SET());
		item.setSECTION_CLASS(attr.getSECTION_CLASS());
		item.setELEMENT(attr.getELEMENT());
		item.setSECTION_RELATIOIN_CLASS(attr.getSECTION_RELATIOIN_CLASS());
		item.setDATA_VERSION(attr.getDATA_VERSION());

		ResourceDAO dao = new ResourceDAOImpl();
		List<AttrDictionary> attrDicts = dao.GetDatasDictionarys(attr.getId());
		List data = new ArrayList();
		for(int i = 0; i < attrDicts.size(); i++) {
			AttrDictionary attrDictionary=new AttrDictionary();

//			attrDictionary.setId(attrDicts.get(i).getId());
			attrDictionary.setAttrid(attrDicts.get(i).getAttrid());
			attrDictionary.setValue(attrDicts.get(i).getValue());
//			attrDictionary.setCode(attrDicts.get(i).getCode());
//			attrDictionary.setTstamp(attrDicts.get(i).getTstamp());
			data.add(attrDictionary);
		}
		
		item.setValue(data);

		return item;
	}	
	
	private int QueryAllDatasCount(ResData criteria) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		int count = dao.GetDatasCount( criteria );
		return count;
	}

	public ResData SaveResourceData(ResData data) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		data.setLATEST_MOD_TIME(timenow);
		data.setDATA_VERSION(data.getDATA_VERSION()+1);
		data = dao.DataAdd(data);
		return data;
	}

	public void DeleteResourceDatas(List<Integer> resourceIds) throws Exception {
		if(resourceIds == null)
			return;
		
		ResData target;
		ResourceDAO dao = new ResourceDAOImpl();
		for(int i = 0; i< resourceIds.size(); i++) {
			target = new ResData();
			target.setId(resourceIds.get(i));
			dao.DataDel(target);
		}
		
		return ;
	}

	public int QueryAllRoleItems(ResRole criteria, int page, int rows,
			List<ResRole> items) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResRole> res = dao.GetRoles( criteria, page, rows );
		items.addAll(res);
		int total = QueryAllRolesCount( criteria );
		return total;
	}

	private int QueryAllRolesCount(ResRole criteria) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		int count = dao.GetRolesCount( criteria );
		return count;
	}
	
	public ResRole SaveResourceRole(ResRole role, List<String> featureIds, List<String> dataIds) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		role.setLATEST_MOD_TIME(timenow);
		role.setDATA_VERSION(role.getDATA_VERSION()+1);
		role = dao.RoleAdd(role);
		
		dao.UpdateFeatureRoleResource(role.getBUSINESS_ROLE(), featureIds);
		dao.UpdateDataRoleResource(role.getBUSINESS_ROLE(), dataIds);
		return role;
	}

	public void DeleteResourceRoles(List<Integer> roleIds) throws Exception {
		if(roleIds == null)
			return;
		
		ResRole target;
		ResourceDAO dao = new ResourceDAOImpl();
		for(int i = 0; i< roleIds.size(); i++) {
			target = new ResRole();
			target.setId(roleIds.get(i));
			dao.RoleDel(target);
		}
		
		return ;
	}

	public void QueryRoleResource(String id, List<ResFeature> features,
			List<ResData> datas) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResRoleResource> rrs = dao.GetRoleResourcesByRoleid(id);
		
		for(int i=0; i<rrs.size(); i++) {
			if ( rrs.get(i).getRestype() == ResRoleResource.RESTYPEFEATURE ) {
				ResFeature feature = dao.GetFeatureById( rrs.get(i).getRESOURCE_ID() );
				features.add(feature);
			}
			else if( rrs.get(i).getRestype() == ResRoleResource.RESTYPEDATA ) {
				ResData data = dao.GetDataById( rrs.get(i).getRESOURCE_ID() );
				datas.add(data);
			}
		}
		
	}
}

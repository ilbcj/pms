package com.pms.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.pms.dao.ResourceDAO;
import com.pms.dao.impl.ResourceDAOImpl;
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
	
	public int QueryAllDataItems(ResData criteria, int page, int rows, List<ResData> items) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResData> res = dao.GetDatas( criteria, page, rows );
		items.addAll(res);
		int total = QueryAllDatasCount( criteria );
		return total;
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

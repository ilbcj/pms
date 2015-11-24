package com.pms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.pms.dao.AttributeDAO;
import com.pms.dao.AuditLogDAO;
import com.pms.dao.AuditLogDescribeDao;
import com.pms.dao.ResourceDAO;
import com.pms.dao.impl.AttributeDAOImpl;
import com.pms.dao.impl.AuditLogDAOImpl;
import com.pms.dao.impl.AuditLogDescribeDAOImpl;
import com.pms.dao.impl.ResourceDAOImpl;
import com.pms.dto.ResDataListItem;
import com.pms.dto.ResDataTemplateListItem;
import com.pms.dto.RoleListItem;
import com.pms.dto.TreeNode;
import com.pms.model.AttrDefinition;
import com.pms.model.AttrDictionary;
import com.pms.model.AuditResLog;
import com.pms.model.AuditResLogDescribe;
import com.pms.model.ResData;
import com.pms.model.ResDataOrg;
import com.pms.model.ResDataTemplate;
import com.pms.model.ResFeature;
import com.pms.model.ResRole;
import com.pms.model.ResRoleOrg;
import com.pms.model.ResRoleResource;

public class ResourceManageService {

	public int QueryAllFeatureItems(String id, ResFeature criteria, int page, int rows, List<ResFeature> items) throws Exception {
		criteria.setDELETE_STATUS(ResFeature.DELSTATUSNO);
		List<ResFeature> nodes = new ArrayList<ResFeature>();
		queryAllChildrenNodesById(id, criteria, nodes);
		int total = nodes.size();
		items.addAll(nodes);
		
		AddResQueryLog(null, criteria, null);
		
		return total;
	}
	
	public List<ResFeature> QueryFeatureChildrenNodes(String id, ResFeature criteria) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResFeature> res = dao.GetFeatureNodeByParentId( id, criteria);
		return res;
	}
	
	public TreeNode ConvertFeatureToTreeNode(ResFeature feature) throws Exception {
		TreeNode node = new TreeNode();
		String state = hasChild(feature.getRESOURCE_ID()) ? "closed" : "open";
		node.setState(state);
		node.setId(feature.getRESOURCE_ID());
		node.setText(feature.getRESOUCE_NAME());
		
		return node;
	}
	
	private boolean hasChild(String pid) throws Exception
	{
		ResourceDAO dao = new ResourceDAOImpl();
		return dao.FeatureHasChild( pid );
	}
	
	public void queryAllChildrenNodesById(String pid, ResFeature criteria, List<ResFeature> children) throws Exception
	{
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResFeature> func = null; 
		if( pid.equals("0") ){	
			func = dao.GetAllFeatures(criteria);
			if(func == null || func.size() == 0) {
				return;
			}
			else {
				children.addAll(func);
			}
			return;
		}else{
			func = dao.GetFeatureNodeByParentId( pid, criteria );
			if(func == null || func.size() == 0) {
				return;
			}
			else {
				children.addAll(func);
			}
				
			for(int i=0; i<func.size(); i++)
			{
				queryAllChildrenNodesById(func.get(i).getRESOURCE_ID(), criteria, children);
			}
		}
		return;
	}
	
	private int QueryAllFeaturesCount(ResFeature criteria) throws Exception {
		criteria.setDELETE_STATUS(ResFeature.DELSTATUSNO);
		ResourceDAO dao = new ResourceDAOImpl();
		int count = dao.GetFeaturesCount( criteria );
		return count;
	}

	public ResFeature SaveResourceFeature(ResFeature feature) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		feature.setLATEST_MOD_TIME(timenow);
		
		AddResAddOrUpdateLog(null, null, feature, null, null);
		
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
			feature.setRESOUCE_NAME(nodes.get(i).getRESOUCE_NAME());
			feature.setRESOURCE_ID(nodes.get(i).getRESOURCE_ID());
			feature.setRESOURCE_STATUS(nodes.get(i).getRESOURCE_STATUS());
			feature.setRESOURCE_DESCRIBE(nodes.get(i).getRESOURCE_DESCRIBE());
			feature.setRMK(nodes.get(i).getRMK());
			feature.setDELETE_STATUS(ResFeature.DELSTATUSYES);
			feature.setAPP_ID(nodes.get(i).getAPP_ID());
			feature.setPARENT_RESOURCE(nodes.get(i).getPARENT_RESOURCE());
			feature.setRESOURCE_ORDER(nodes.get(i).getRESOURCE_ORDER());
			feature.setSYSTEM_TYPE(nodes.get(i).getSYSTEM_TYPE());
			feature.setLATEST_MOD_TIME(timenow);
		
			feature = dao.FeatureAdd(feature);
			AddResDelLog(null, null, feature, null, null);
		}
		
		return feature;
	}
	
	public int QueryAllDataItems(List<String> resource_status, List<String> resource_type,
			List<String> dataset_sensitive_level, List<String> data_set, List<String> section_class, 
			List<String> lement, List<String> section_relatioin_class, 
			ResData criteria, int page, int rows, List<ResDataListItem> items) 
					throws Exception {
		criteria.setDELETE_STATUS(ResData.DELSTATUSNO);
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResData> res = dao.GetDatas( resource_status, resource_type, dataset_sensitive_level,
				data_set, section_class, lement,section_relatioin_class,
				criteria, page, rows );
		ResDataListItem resDataListItem = null;
		for(int i=0; i<res.size(); i++) {
			resDataListItem = ConvertDatasDefinitonToResDataListItem(res.get(i));
			items.add(resDataListItem);
		}
		int total = QueryAllDatasCount( criteria );
		
		AddResQueryLog(criteria, null, null);
		
		return total;
	}
	
	public int QueryAllDataTemplateItems(List<String> resource_status, List<String> resource_type,
			List<String> dataset_sensitive_level, List<String> data_set, List<String> section_class, 
			List<String> lement, List<String> section_relatioin_class, 
			ResDataTemplate criteria, int page, int rows, List<ResDataTemplateListItem> items) 
					throws Exception {
		criteria.setDELETE_STATUS(ResData.DELSTATUSNO);
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResDataTemplate> res = dao.GetDataTemplates( resource_status, resource_type, dataset_sensitive_level,
				data_set, section_class, lement,section_relatioin_class,
				criteria, page, rows );
		ResDataTemplateListItem resDataTemplateListItem = null;
		for(int i=0; i<res.size(); i++) {
			resDataTemplateListItem = ConvertDatasDefinitonToResDataTemplateListItem(res.get(i));
			items.add(resDataTemplateListItem);
		}
		int total = QueryAllDataTemplatesCount( criteria );
		
//		AddResQueryLog(criteria, null, null);
		
		return total;
	}
	
	public ResDataListItem ConvertDatasDefinitonToResDataListItem(ResData attr) throws Exception {
		ResDataListItem item = new ResDataListItem();
		item.setId(attr.getId());
		item.setName(attr.getName());
		item.setRESOURCE_ID(attr.getRESOURCE_ID());
		item.setRESOURCE_DESCRIBE(attr.getRESOURCE_DESCRIBE());
		item.setRMK(attr.getRMK());				
		item.setDATA_VERSION(attr.getDATA_VERSION());
		
		AttributeDAO attrdao = new AttributeDAOImpl();
		List<AttrDictionary> resStatNode = attrdao.GetDictsDatasNode(AttrDefinition.ATTR_RESOURCEDATA_RESOURCE_STATUS, String.valueOf(attr.getRESOURCE_STATUS()), attr.getRESOURCE_ID());
		for (int i = 0; i < resStatNode.size(); i++) {
			item.setRESOURCE_STATUS(resStatNode.get(i).getValue());
		}
		
		List<AttrDictionary> delStatNode = attrdao.GetDictsDatasNode(AttrDefinition.ATTR_RESOURCEDATA_DELETE_STATUS, String.valueOf(attr.getDELETE_STATUS()), attr.getRESOURCE_ID());
		for (int i = 0; i < delStatNode.size(); i++) {
			item.setDELETE_STATUS(delStatNode.get(i).getValue());
		}
		
		List<AttrDictionary> resTypeNode = attrdao.GetDictsDatasNode(AttrDefinition.ATTR_RESOURCEDATA_RESOURCE_TYPE, String.valueOf(attr.getResource_type()), attr.getRESOURCE_ID());
		for (int i = 0; i < resTypeNode.size(); i++) {
			item.setResource_type(resTypeNode.get(i).getValue());
		}
		
		List<AttrDictionary> dslNode = attrdao.GetDictsDatasNode(AttrDefinition.ATTR_RESOURCEDATA_DATASET_SENSITIVE_LEVEL, attr.getDATASET_SENSITIVE_LEVEL(), attr.getRESOURCE_ID());
		for (int i = 0; i < dslNode.size(); i++) {
			item.setDATASET_SENSITIVE_LEVEL(dslNode.get(i).getValue());
		}
		
		List<AttrDictionary> datasetNode = attrdao.GetDictsDatasNode(AttrDefinition.ATTR_RESOURCEDATA_DATA_SET, attr.getDATA_SET(), attr.getRESOURCE_ID());
		for (int i = 0; i < datasetNode.size(); i++) {
			item.setDATA_SET(datasetNode.get(i).getValue());
		}
		
		List<AttrDictionary> sectionclassNode = attrdao.GetDictsDatasNode(AttrDefinition.ATTR_RESOURCEDATA_SECTION_CLASS, attr.getSECTION_CLASS(), attr.getRESOURCE_ID());
		for (int i = 0; i < sectionclassNode.size(); i++) {
			item.setSECTION_CLASS(sectionclassNode.get(i).getValue());
		}
		
		List<AttrDictionary> lementNode = attrdao.GetDictsDatasNode(AttrDefinition.ATTR_RESOURCEDATA_LEMENT, attr.getELEMENT(), attr.getRESOURCE_ID());
		for (int i = 0; i < lementNode.size(); i++) {
			item.setELEMENT(lementNode.get(i).getValue());
		}
		
		List<AttrDictionary> secrelaclsNode = attrdao.GetDictsDatasNode(AttrDefinition.ATTR_RESOURCEDATA_SECTION_RELATION_CLASS, attr.getSECTION_RELATIOIN_CLASS(), attr.getRESOURCE_ID());
		for (int i = 0; i < secrelaclsNode.size(); i++) {
			item.setSECTION_RELATIOIN_CLASS(secrelaclsNode.get(i).getValue());
		}
		
		ResourceDAO dao = new ResourceDAOImpl();
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
		
		List<AttrDictionary> attrDicts = attrdao.GetDatasDictionarys(attr.getRESOURCE_ID());
		List<AttrDictionary> data = new ArrayList<AttrDictionary>();
		for(int i = 0; i < attrDicts.size(); i++) {
			AttrDictionary attrDictionary=new AttrDictionary();

			attrDictionary.setId(attrDicts.get(i).getId());
			attrDictionary.setAttrid(attrDicts.get(i).getAttrid());
			attrDictionary.setValue(attrDicts.get(i).getValue());
			attrDictionary.setCode(attrDicts.get(i).getCode());
			attrDictionary.setTstamp(attrDicts.get(i).getTstamp());
			data.add(attrDictionary);
		}
		
		item.setDictionary(data);

		return item;
	}	
	
	public ResDataTemplateListItem ConvertDatasDefinitonToResDataTemplateListItem(ResDataTemplate attr) throws Exception {
		ResDataTemplateListItem item = new ResDataTemplateListItem();
		item.setId(attr.getId());
		item.setName(attr.getName());
		item.setRESOURCE_ID(attr.getRESOURCE_ID());
		item.setRESOURCE_DESCRIBE(attr.getRESOURCE_DESCRIBE());
		item.setRMK(attr.getRMK());				
		item.setDATA_VERSION(attr.getDATA_VERSION());
		
		AttributeDAO attrdao = new AttributeDAOImpl();
		List<AttrDictionary> resStatNode = attrdao.GetDictsDataTemplatesNode(AttrDefinition.ATTR_RESOURCEDATA_RESOURCE_STATUS, String.valueOf(attr.getRESOURCE_STATUS()), attr.getRESOURCE_ID());
		for (int i = 0; i < resStatNode.size(); i++) {
			item.setRESOURCE_STATUS(resStatNode.get(i).getValue());
		}
		
		List<AttrDictionary> delStatNode = attrdao.GetDictsDataTemplatesNode(AttrDefinition.ATTR_RESOURCEDATA_DELETE_STATUS, String.valueOf(attr.getDELETE_STATUS()), attr.getRESOURCE_ID());
		for (int i = 0; i < delStatNode.size(); i++) {
			item.setDELETE_STATUS(delStatNode.get(i).getValue());
		}
		
		List<AttrDictionary> resTypeNode = attrdao.GetDictsDataTemplatesNode(AttrDefinition.ATTR_RESOURCEDATA_RESOURCE_TYPE, String.valueOf(attr.getResource_type()), attr.getRESOURCE_ID());
		for (int i = 0; i < resTypeNode.size(); i++) {
			item.setResource_type(resTypeNode.get(i).getValue());
		}
		
		List<AttrDictionary> dslNode = attrdao.GetDictsDataTemplatesNode(AttrDefinition.ATTR_RESOURCEDATA_DATASET_SENSITIVE_LEVEL, attr.getDATASET_SENSITIVE_LEVEL(), attr.getRESOURCE_ID());
		for (int i = 0; i < dslNode.size(); i++) {
			item.setDATASET_SENSITIVE_LEVEL(dslNode.get(i).getValue());
		}
		
		List<AttrDictionary> datasetNode = attrdao.GetDictsDataTemplatesNode(AttrDefinition.ATTR_RESOURCEDATA_DATA_SET, attr.getDATA_SET(), attr.getRESOURCE_ID());
		for (int i = 0; i < datasetNode.size(); i++) {
			item.setDATA_SET(datasetNode.get(i).getValue());
		}
		
		List<AttrDictionary> sectionclassNode = attrdao.GetDictsDataTemplatesNode(AttrDefinition.ATTR_RESOURCEDATA_SECTION_CLASS, attr.getSECTION_CLASS(), attr.getRESOURCE_ID());
		for (int i = 0; i < sectionclassNode.size(); i++) {
			item.setSECTION_CLASS(sectionclassNode.get(i).getValue());
		}
		
		List<AttrDictionary> lementNode = attrdao.GetDictsDataTemplatesNode(AttrDefinition.ATTR_RESOURCEDATA_LEMENT, attr.getELEMENT(), attr.getRESOURCE_ID());
		for (int i = 0; i < lementNode.size(); i++) {
			item.setELEMENT(lementNode.get(i).getValue());
		}
		
		List<AttrDictionary> secrelaclsNode = attrdao.GetDictsDataTemplatesNode(AttrDefinition.ATTR_RESOURCEDATA_SECTION_RELATION_CLASS, attr.getSECTION_RELATIOIN_CLASS(), attr.getRESOURCE_ID());
		for (int i = 0; i < secrelaclsNode.size(); i++) {
			item.setSECTION_RELATIOIN_CLASS(secrelaclsNode.get(i).getValue());
		}
		
		ResourceDAO dao = new ResourceDAOImpl();
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
		
		List<AttrDictionary> attrDicts = attrdao.GetDatasDictionarys(attr.getRESOURCE_ID());
		List<AttrDictionary> data = new ArrayList<AttrDictionary>();
		for(int i = 0; i < attrDicts.size(); i++) {
			AttrDictionary attrDictionary=new AttrDictionary();

			attrDictionary.setId(attrDicts.get(i).getId());
			attrDictionary.setAttrid(attrDicts.get(i).getAttrid());
			attrDictionary.setValue(attrDicts.get(i).getValue());
			attrDictionary.setCode(attrDicts.get(i).getCode());
			attrDictionary.setTstamp(attrDicts.get(i).getTstamp());
			data.add(attrDictionary);
		}
		
		item.setDictionary(data);

		return item;
	}	
	
	private int QueryAllDatasCount(ResData criteria) throws Exception {
		criteria.setDELETE_STATUS(ResData.DELSTATUSNO);
		ResourceDAO dao = new ResourceDAOImpl();
		int count = dao.GetDatasCount( criteria );
		return count;
	}
	
	private int QueryAllDataTemplatesCount(ResDataTemplate criteria) throws Exception {
		criteria.setDELETE_STATUS(ResData.DELSTATUSNO);
		ResourceDAO dao = new ResourceDAOImpl();
		int count = dao.GetDataTemplatesCount( criteria );
		return count;
	}
	
	public ResData SaveResourceData(ResData data, ResDataOrg resDataOrg) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		data.setLATEST_MOD_TIME(timenow);
		data.setDATA_VERSION(data.getDATA_VERSION()+1);
//		
//		AddResAddOrUpdateLog(data, resDataOrg, null, null, null);
//		
//		data = dao.DataAdd(data);
//		if(data.getRESOURCE_ID() == null || data.getRESOURCE_ID().length() == 0) {
//			String resId = String.format("%s%010d", ConfigHelper.getRegion(), data.getId());
//			data.setRESOURCE_ID(resId);
//		}
//		data = dao.DataAdd(data);
		ResData resData = dao.GetData(data);
		
		if(resData == null){
			ResDataTemplate resDataTemplate = dao.GetDataTemplate(data);
			data.setRESOURCE_ID(resDataTemplate.getRESOURCE_ID());
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
		}else{
			
			if( resData.getDELETE_STATUS() == ResData.DELSTATUSYES ){
				resData.setRESOURCE_ID(resData.getRESOURCE_ID());
				resData.setDELETE_STATUS(ResData.DELSTATUSNO);
				resData.setDATA_VERSION(resData.getDATA_VERSION()+1);
				data = dao.DataAdd(resData);
			}
			
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
		List<ResData> nodes=dao.GetDataById(data.getId());
		
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
			data.setDATA_VERSION(nodes.get(i).getDATA_VERSION()+1);	
			data.setLATEST_MOD_TIME(timenow);
			data.setRMK(nodes.get(i).getRMK());
		
			data = dao.DataAdd(data);
			
			List<ResDataOrg> resDataOrgNodes=dao.GetResDataOrgByResId(data.getRESOURCE_ID());
			ResDataOrg resDataOrg=new ResDataOrg();
			for (int j = 0; j < resDataOrgNodes.size(); j++) {
				resDataOrg.setId(resDataOrgNodes.get(j).getId());
				resDataOrg.setRESOURCE_ID(resDataOrgNodes.get(j).getRESOURCE_ID());
				resDataOrg.setCLUE_DST_SYS(resDataOrgNodes.get(j).getCLUE_DST_SYS());
				resDataOrg.setDELETE_STATUS(ResDataOrg.DELSTATUSYES);
				resDataOrg.setDATA_VERSION(resDataOrgNodes.get(j).getDATA_VERSION()+1);
				resDataOrg.setLATEST_MOD_TIME(timenow);
				
				resDataOrg = dao.ResDataOrgAdd(resDataOrg);
			}
			AddResDelLog(data, resDataOrg, null, null, null);
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
		
		AddResQueryLog(null, null, criteria);
		
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
		
		AttributeDAO attrdao = new AttributeDAOImpl();
		List<AttrDictionary> attrDicts = attrdao.GetRolesDictionarys(resRole.getId());
		List<AttrDictionary> data = new ArrayList<AttrDictionary>();
		for(int i = 0; i < attrDicts.size(); i++) {
			AttrDictionary attrDictionary=new AttrDictionary();

			attrDictionary.setId(attrDicts.get(i).getId());
			attrDictionary.setAttrid(attrDicts.get(i).getAttrid());
			attrDictionary.setValue(attrDicts.get(i).getValue());
			attrDictionary.setCode(attrDicts.get(i).getCode());
			attrDictionary.setTstamp(attrDicts.get(i).getTstamp());
			data.add(attrDictionary);
		}
		
		item.setDictionary(data);

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
		
		AddResAddOrUpdateLog(null, null, null, role, resRoleOrg);
		
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
//		dao.UpdateDataResource(dataIds);
		
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
		List<ResRole> roleNodes=dao.GetRoleById(role.getBUSINESS_ROLE());

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
			ResRoleResource resRole=new ResRoleResource();
			for (int j = 0; j < roleResNodes.size(); j++) {
				resRole.setRESOURCE_ID(roleResNodes.get(j).getRESOURCE_ID());
				resRole.setBUSINESS_ROLE(roleResNodes.get(j).getBUSINESS_ROLE());
				resRole.setRESOURCE_CLASS(roleResNodes.get(j).getRESOURCE_CLASS());
				resRole.setDELETE_STATUS(ResRoleResource.DELSTATUSYES);
				resRole.setDATA_VERSION(roleResNodes.get(j).getDATA_VERSION());
				resRole.setLATEST_MOD_TIME(timenow);
				
				resRole = dao.ResRoleResourceAdd(resRole);
			}
			
			List<ResRoleOrg> resRoleOrgNodes=dao.GetResRoleOrgByRoleid(roleNodes.get(i).getBUSINESS_ROLE());
			ResRoleOrg resRoleOrg=new ResRoleOrg();
			for (int j = 0; j < resRoleOrgNodes.size(); j++) {
				resRoleOrg.setId(resRoleOrgNodes.get(j).getId());
				resRoleOrg.setBUSINESS_ROLE(resRoleOrgNodes.get(j).getBUSINESS_ROLE());
				resRoleOrg.setCLUE_DST_SYS(resRoleOrgNodes.get(j).getCLUE_DST_SYS());
				resRoleOrg.setDELETE_STATUS(ResRoleOrg.DELSTATUSYES);
				resRoleOrg.setDATA_VERSION(resRoleOrgNodes.get(j).getDATA_VERSION());
				resRoleOrg.setLATEST_MOD_TIME(timenow);
				
				resRoleOrg = dao.ResRoleOrgAdd(resRoleOrg);
			}
			AddResDelLog(null, null, null, role, resRoleOrg);
		}
		
		return role;
	}
	
	public void QueryRoleResource(String id, List<ResFeature> features,
			List<ResDataListItem> items) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		List<ResRoleResource> rrs = dao.GetRoleResourcesByRoleid(id);
		
		for(int i=0; i<rrs.size(); i++) {
			if ( rrs.get(i).getRESOURCE_CLASS() == ResRoleResource.RESCLASSFEATURE ) {
				ResFeature feature = dao.GetFeatureByResId( rrs.get(i).getRESOURCE_ID() );
				features.add(feature);
			}
			else if( rrs.get(i).getRESOURCE_CLASS() == ResRoleResource.RESCLASSDATA ) {
//				List<ResDataTemplate> dataTemplate = dao.GetDataTemplateByResId( rrs.get(i).getRESOURCE_ID() );
//				ResDataTemplateListItem resDataTemplateListItem = null;
//				for(int j=0; j<dataTemplate.size(); j++) {
//					resDataTemplateListItem = ConvertDatasDefinitonToResDataTemplateListItem( dataTemplate.get(j) );
//				}
//				items.add(resDataTemplateListItem);
				
				List<ResData> data = dao.GetDataByResId( rrs.get(i).getRESOURCE_ID() );
				ResDataListItem resDataListItem = null;
				for(int j=0; j<data.size(); j++) {
					resDataListItem = ConvertDatasDefinitonToResDataListItem( data.get(j) );
				}
				items.add(resDataListItem);
			}
		}
		
	}
	
	private void AddResQueryLog(ResData resData, ResFeature resFeature, ResRole resRole) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		
		AuditResLog auditResLog = new AuditResLog();
		AuditLogDAO logdao = new AuditLogDAOImpl();
		AuditLogService als = new AuditLogService();
		
		auditResLog.setAdminId(als.adminLogin());
		auditResLog.setIpAddr("");
		auditResLog.setFlag(AuditResLog.LOGFLAGQUERY);
		auditResLog.setResult(AuditResLog.LOGRESULTSUCCESS);
		auditResLog.setLATEST_MOD_TIME(timenow);
		auditResLog = logdao.AuditResLogAdd(auditResLog);
		
		if( resData != null || resFeature != null || resRole != null) {
			AuditResLogDescribe auditResLogDescribe = new AuditResLogDescribe();
			AuditLogDescribeDao logDescdao = new AuditLogDescribeDAOImpl();
			
			auditResLogDescribe.setLogid(auditResLog.getId());
			String str="";
			if( resData != null){
				if(resData.getName() != null && resData.getName().length() > 0) {
					str += resData.getName()+";";
				}
				if(resData.getRESOURCE_ID() != null && resData.getRESOURCE_ID().length() > 0) {
					str += resData.getRESOURCE_ID();
				}
			}
			if( resFeature != null){
				if(resFeature.getRESOUCE_NAME() != null && resFeature.getRESOUCE_NAME().length() > 0) {
					str += resFeature.getRESOUCE_NAME()+";";
				}
				if(resFeature.getRESOURCE_ID() != null && resFeature.getRESOURCE_ID().length() > 0) {
					str += resFeature.getRESOURCE_ID();
				}
			}
			if( resRole != null){
				if(resRole.getBUSINESS_ROLE_NAME() != null && resRole.getBUSINESS_ROLE_NAME().length() > 0) {
					str += resRole.getBUSINESS_ROLE_NAME()+";";
				}
				if(resRole.getBUSINESS_ROLE() != null && resRole.getBUSINESS_ROLE().length() > 0) {
					str += resRole.getBUSINESS_ROLE();
				}
			}
			auditResLogDescribe.setDescrib(str);
			
			auditResLogDescribe.setLATEST_MOD_TIME(timenow);
//			auditResLogDescribe = logDescdao.AuditResLogDescribeAdd(auditResLogDescribe);
		}
	}
	
	private void AddResAddOrUpdateLog(ResData resData, ResDataOrg resDataOrg, ResFeature resFeature, ResRole role, ResRoleOrg resRoleOrg) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		AuditResLog auditResLog = new AuditResLog();
		AuditLogDAO logdao = new AuditLogDAOImpl();
		AuditLogService als = new AuditLogService();
		
		auditResLog.setAdminId(als.adminLogin());
		auditResLog.setIpAddr("");
		if( resData != null) {
			if(resData.getId() == 0 ){
				auditResLog.setFlag(AuditResLog.LOGFLAGADD);
			}else{
				auditResLog.setFlag(AuditResLog.LOGFLAGUPDATE);
			}
		}
		if( resFeature != null) {
			if(resFeature.getId() == 0 ){
				auditResLog.setFlag(AuditResLog.LOGFLAGADD);
			}else{
				auditResLog.setFlag(AuditResLog.LOGFLAGUPDATE);
			}
		}
		if( role != null) {
			if(role.getId() == 0 ){
				auditResLog.setFlag(AuditResLog.LOGFLAGADD);
			}else{
				auditResLog.setFlag(AuditResLog.LOGFLAGUPDATE);
			}
		}
		auditResLog.setResult(AuditResLog.LOGRESULTSUCCESS);
		auditResLog.setLATEST_MOD_TIME(timenow);
		auditResLog = logdao.AuditResLogAdd(auditResLog);
		
		AuditResLogDescribe auditResLogDescribe = new AuditResLogDescribe();
		AuditLogDescribeDao logDescdao = new AuditLogDescribeDAOImpl();
		
		auditResLogDescribe.setLogid(auditResLog.getId());
		String str="";
		if( resData != null){
			if(resData.getName() != null && resData.getName().length() > 0) {
				str += resData.getName()+";";
			}
			if(resData.getRESOURCE_ID() != null && resData.getRESOURCE_ID().length() > 0) {
				str += resData.getRESOURCE_ID()+";";
			}
			str += resData.getRESOURCE_STATUS()+";";
			if(resData.getRESOURCE_DESCRIBE() != null && resData.getRESOURCE_DESCRIBE().length() > 0) {
				str += resData.getRESOURCE_DESCRIBE()+";";
			}
			if(resData.getRMK() != null && resData.getRMK().length() > 0) {
				str += resData.getRMK()+";";
			}
			str += resData.getDELETE_STATUS()+";"+resData.getResource_type()+";";
			if(resDataOrg.getCLUE_DST_SYS() !=null && resDataOrg.getCLUE_DST_SYS().length() != 0){
				str += resDataOrg.getCLUE_DST_SYS()+";";
			}
			if(resData.getDATASET_SENSITIVE_LEVEL() != null && resData.getDATASET_SENSITIVE_LEVEL().length() > 0) {
				str += resData.getDATASET_SENSITIVE_LEVEL()+";";
			}
			if(resData.getDATA_SET() != null && resData.getDATA_SET().length() > 0) {
				str += resData.getDATA_SET()+";";
			}
			if(resData.getELEMENT() != null && resData.getELEMENT().length() > 0) {
				str += resData.getELEMENT()+";";
			}
			if(resData.getSECTION_RELATIOIN_CLASS() != null && resData.getSECTION_RELATIOIN_CLASS().length() > 0) {
				str += resData.getSECTION_RELATIOIN_CLASS()+";";
			}
			if(resData.getSECTION_CLASS() != null && resData.getSECTION_CLASS().length() > 0) {
				str += resData.getSECTION_CLASS();
			}
		}
		if( resFeature != null){
			if(resFeature.getRESOUCE_NAME() != null && resFeature.getRESOUCE_NAME().length() > 0) {
				str += resFeature.getRESOUCE_NAME()+";";
			}
			if(resFeature.getRESOURCE_ID() != null && resFeature.getRESOURCE_ID().length() > 0) {
				str += resFeature.getRESOURCE_ID()+";";
			}
			str += resFeature.getRESOURCE_STATUS()+";";
			if(resFeature.getRESOURCE_DESCRIBE() != null && resFeature.getRESOURCE_DESCRIBE().length() > 0) {
				str += resFeature.getRESOURCE_DESCRIBE()+";";
			}
			if(resFeature.getRMK() != null && resFeature.getRMK().length() > 0) {
				str += resFeature.getRMK()+";";
			}
			str += resFeature.getDELETE_STATUS()+";";
			if(resFeature.getAPP_ID() != null && resFeature.getAPP_ID().length() > 0) {
				str += resFeature.getAPP_ID()+";";
			}
			if(resFeature.getPARENT_RESOURCE() != null && resFeature.getPARENT_RESOURCE().length() > 0) {
				str += resFeature.getPARENT_RESOURCE()+";";
			}
			if(resFeature.getRESOURCE_ORDER() != null && resFeature.getRESOURCE_ORDER().length() > 0) {
				str += resFeature.getRESOURCE_ORDER()+";";
			}
			if(resFeature.getSYSTEM_TYPE() != null && resFeature.getSYSTEM_TYPE().length() > 0) {
				str += resFeature.getSYSTEM_TYPE();
			}
		}	
		if( role != null){
			if(role.getBUSINESS_ROLE_NAME() != null && role.getBUSINESS_ROLE_NAME().length() > 0) {
				str += role.getBUSINESS_ROLE_NAME()+";";
			}
			if(role.getBUSINESS_ROLE() != null && role.getBUSINESS_ROLE().length() > 0) {
				str += role.getBUSINESS_ROLE()+";";
			}
			str += role.getBUSINESS_ROLE_TYPE()+";";
			if(resRoleOrg.getCLUE_DST_SYS() !=null && resRoleOrg.getCLUE_DST_SYS().length() != 0){
				str += resRoleOrg.getCLUE_DST_SYS()+";";
			}
			if(role.getSYSTEM_TYPE() != null && role.getSYSTEM_TYPE().length() > 0) {
				str += role.getSYSTEM_TYPE()+";";
			}
			if(role.getCLUE_SRC_SYS() != null && role.getCLUE_SRC_SYS().length() > 0) {
				str += role.getCLUE_SRC_SYS()+";";
			}
			str += role.getDELETE_STATUS()+";";
			if(role.getROLE_DESC() != null && role.getROLE_DESC().length() > 0) {
				str += role.getROLE_DESC();
			}
			
			ResourceDAO dao = new ResourceDAOImpl();
			List<ResRoleResource> rrs = dao.GetRoleResourcesByRoleid(role.getBUSINESS_ROLE());
			
			for(int i=0; i<rrs.size(); i++) {
				if ( rrs.get(i).getRESOURCE_CLASS() == ResRoleResource.RESCLASSFEATURE ) {
					ResFeature feature = dao.GetFeatureByResId( rrs.get(i).getRESOURCE_ID() );
					str += ";"+feature.getRESOUCE_NAME()+";"+feature.getRESOURCE_ID();
				}
				else if( rrs.get(i).getRESOURCE_CLASS() == ResRoleResource.RESCLASSDATA ) {
					List<ResData> data = dao.GetDataByResId( rrs.get(i).getRESOURCE_ID() );
					for(int j=0; j<data.size(); j++) {
						str += ";"+data.get(j).getName()+";"+data.get(j).getRESOURCE_ID();
					}
					
				}
			}
		}
		auditResLogDescribe.setDescrib(str);
		
		auditResLogDescribe.setLATEST_MOD_TIME(timenow);
//		auditResLogDescribe = logDescdao.AuditResLogDescribeAdd(auditResLogDescribe);
	}
	
	private void AddResDelLog(ResData resData, ResDataOrg resDataOrg, ResFeature resFeature, ResRole role, ResRoleOrg resRoleOrg) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		
		AuditResLog auditResLog = new AuditResLog();
		AuditLogDAO logdao = new AuditLogDAOImpl();
		AuditLogService als = new AuditLogService();
		
		auditResLog.setAdminId(als.adminLogin());
		auditResLog.setIpAddr("");
		auditResLog.setFlag(AuditResLog.LOGFLAGDELETE);
		auditResLog.setResult(AuditResLog.LOGRESULTSUCCESS);
		auditResLog.setLATEST_MOD_TIME(timenow);
		auditResLog = logdao.AuditResLogAdd(auditResLog);
		
		AuditResLogDescribe auditResLogDescribe = new AuditResLogDescribe();
		AuditLogDescribeDao logDescdao = new AuditLogDescribeDAOImpl();
		
		auditResLogDescribe.setLogid(auditResLog.getId());
		
		String str="";
		
		if( resData != null){
			if(resData.getName() != null && resData.getName().length() > 0) {
				str += resData.getName()+";";
			}
			if(resData.getRESOURCE_ID() != null && resData.getRESOURCE_ID().length() > 0) {
				str += resData.getRESOURCE_ID()+";";
			}
			str += resData.getRESOURCE_STATUS()+";";
			if(resData.getRESOURCE_DESCRIBE() != null && resData.getRESOURCE_DESCRIBE().length() > 0) {
				str += resData.getRESOURCE_DESCRIBE()+";";
			}
			if(resData.getRMK() != null && resData.getRMK().length() > 0) {
				str += resData.getRMK()+";";
			}
			str += resData.getDELETE_STATUS()+";"+resData.getResource_type()+";";
			if(resDataOrg.getCLUE_DST_SYS() !=null && resDataOrg.getCLUE_DST_SYS().length() != 0){
				str += resDataOrg.getCLUE_DST_SYS()+";";
			}
			if(resData.getDATASET_SENSITIVE_LEVEL() != null && resData.getDATASET_SENSITIVE_LEVEL().length() > 0) {
				str += resData.getDATASET_SENSITIVE_LEVEL()+";";
			}
			if(resData.getDATA_SET() != null && resData.getDATA_SET().length() > 0) {
				str += resData.getDATA_SET()+";";
			}
			if(resData.getELEMENT() != null && resData.getELEMENT().length() > 0) {
				str += resData.getELEMENT()+";";
			}
			if(resData.getSECTION_RELATIOIN_CLASS() != null && resData.getSECTION_RELATIOIN_CLASS().length() > 0) {
				str += resData.getSECTION_RELATIOIN_CLASS()+";";
			}
			if(resData.getSECTION_CLASS() != null && resData.getSECTION_CLASS().length() > 0) {
				str += resData.getSECTION_CLASS();
			}
		}
		
		if( resFeature != null){
			if(resFeature.getRESOUCE_NAME() != null && resFeature.getRESOUCE_NAME().length() > 0) {
				str += resFeature.getRESOUCE_NAME()+";";
			}
			if(resFeature.getRESOURCE_ID() != null && resFeature.getRESOURCE_ID().length() > 0) {
				str += resFeature.getRESOURCE_ID()+";";
			}
			str += resFeature.getRESOURCE_STATUS()+";";
			if(resFeature.getRESOURCE_DESCRIBE() != null && resFeature.getRESOURCE_DESCRIBE().length() > 0) {
				str += resFeature.getRESOURCE_DESCRIBE()+";";
			}
			if(resFeature.getRMK() != null && resFeature.getRMK().length() > 0) {
				str += resFeature.getRMK()+";";
			}
			str += resFeature.getDELETE_STATUS()+";";
			if(resFeature.getAPP_ID() != null && resFeature.getAPP_ID().length() > 0) {
				str += resFeature.getAPP_ID()+";";
			}
			if(resFeature.getPARENT_RESOURCE() != null && resFeature.getPARENT_RESOURCE().length() > 0) {
				str += resFeature.getPARENT_RESOURCE()+";";
			}
			if(resFeature.getRESOURCE_ORDER() != null && resFeature.getRESOURCE_ORDER().length() > 0) {
				str += resFeature.getRESOURCE_ORDER()+";";
			}
			if(resFeature.getSYSTEM_TYPE() != null && resFeature.getSYSTEM_TYPE().length() > 0) {
				str += resFeature.getSYSTEM_TYPE();
			}
		}
		
		if( role != null){
			if(role.getBUSINESS_ROLE_NAME() != null && role.getBUSINESS_ROLE_NAME().length() > 0) {
				str += role.getBUSINESS_ROLE_NAME()+";";
			}
			if(role.getBUSINESS_ROLE() != null && role.getBUSINESS_ROLE().length() > 0) {
				str += role.getBUSINESS_ROLE()+";";
			}
			str += role.getBUSINESS_ROLE_TYPE()+";";
			if(resRoleOrg.getCLUE_DST_SYS() !=null && resRoleOrg.getCLUE_DST_SYS().length() != 0){
				str += resRoleOrg.getCLUE_DST_SYS()+";";
			}
			if(role.getSYSTEM_TYPE() != null && role.getSYSTEM_TYPE().length() > 0) {
				str += role.getSYSTEM_TYPE()+";";
			}
			if(role.getCLUE_SRC_SYS() != null && role.getCLUE_SRC_SYS().length() > 0) {
				str += role.getCLUE_SRC_SYS()+";";
			}
			str += role.getDELETE_STATUS()+";";
			if(role.getROLE_DESC() != null && role.getROLE_DESC().length() > 0) {
				str += role.getROLE_DESC();
			}
			
			ResourceDAO dao = new ResourceDAOImpl();
			List<ResRoleResource> rrs = dao.GetRoleResourcesByRoleid(role.getBUSINESS_ROLE());
			
			for(int i=0; i<rrs.size(); i++) {
				if ( rrs.get(i).getRESOURCE_CLASS() == ResRoleResource.RESCLASSFEATURE ) {
					ResFeature feature = dao.GetFeatureByResId( rrs.get(i).getRESOURCE_ID() );
					str += ";"+feature.getRESOUCE_NAME()+";"+feature.getRESOURCE_ID();
				}
				else if( rrs.get(i).getRESOURCE_CLASS() == ResRoleResource.RESCLASSDATA ) {
					List<ResData> data = dao.GetDataByResId( rrs.get(i).getRESOURCE_ID() );
					for(int j=0; j<data.size(); j++) {
						str += ";"+data.get(j).getName()+";"+data.get(j).getRESOURCE_ID();
					}
					
				}
			}
		}
		auditResLogDescribe.setDescrib(str);
		
		auditResLogDescribe.setLATEST_MOD_TIME(timenow);
//		auditResLogDescribe = logDescdao.AuditResLogDescribeAdd(auditResLogDescribe);
	}
	
}

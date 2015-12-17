package com.pms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.pms.dao.AttributeDAO;
import com.pms.dao.AuditLogDAO;
import com.pms.dao.AuditLogDescribeDao;
import com.pms.dao.impl.AttributeDAOImpl;
import com.pms.dao.impl.AuditLogDAOImpl;
import com.pms.dao.impl.AuditLogDescribeDAOImpl;
import com.pms.dto.AttrDictItem;
import com.pms.model.AttrDefinition;
import com.pms.model.AttrDictionary;
import com.pms.model.AuditSystemLog;
import com.pms.model.AuditSystemLogDescribe;

public class AttrDictionaryService {
	
	public int QueryAttrDictionaryOfUser(AttrDefinition criteria, int page,
			int rows, ArrayList<AttrDictItem> items) throws Exception {
		criteria.setType(AttrDefinition.ATTRTYPEUSER);
		AttributeDAO dao = new AttributeDAOImpl();
		List<AttrDefinition> res = dao.GetAttrDefinitions( criteria, page, rows );
		AttrDictItem attrDictItem = null;
		for(int i=0; i<res.size(); i++) {
			attrDictItem = ConvertAttrDefinitonToAttrDictItem(res.get(i));
			items.add(attrDictItem);
		}
		int total = dao.GetAttrDefinitionsCount( criteria );
		
		AddSystemQueryLog(criteria);
		return total;
	}
	
	public int QueryAttrDictionaryOfResourceData(AttrDefinition criteria, int page,
			int rows, ArrayList<AttrDictItem> items) throws Exception {
		criteria.setType(AttrDefinition.ATTRTYPERESOURCEDATA);
		AttributeDAO dao = new AttributeDAOImpl();
		List<AttrDefinition> res = dao.GetAttrDefinitions( criteria, page, rows );
		AttrDictItem attrDictItem = null;
		for(int i=0; i<res.size(); i++) {
			attrDictItem = ConvertAttrDefinitonToAttrDictItem(res.get(i));
			items.add(attrDictItem);
		}
		int total = dao.GetAttrDefinitionsCount( criteria );
		
		AddSystemQueryLog(criteria);
		return total;
	}
	
	public int QueryAttrDictionaryOfResourceFeature(AttrDefinition criteria, int page,
			int rows, ArrayList<AttrDictItem> items) throws Exception {
		criteria.setType(AttrDefinition.ATTRTYPEFUNCDATA);
		AttributeDAO dao = new AttributeDAOImpl();
		List<AttrDefinition> res = dao.GetAttrDefinitions( criteria, page, rows );
		AttrDictItem attrDictItem = null;
		for(int i=0; i<res.size(); i++) {
			attrDictItem = ConvertAttrDefinitonToAttrDictItem(res.get(i));
			items.add(attrDictItem);
		}
		int total = dao.GetAttrDefinitionsCount( criteria );
		
		AddSystemQueryLog(criteria);
		return total;
	}
	
	public int QueryAttrDictionaryOfOrg(AttrDefinition criteria, int page,
			int rows, ArrayList<AttrDictItem> items) throws Exception {
		criteria.setType(AttrDefinition.ATTRTYPEORG);
		AttributeDAO dao = new AttributeDAOImpl();
		List<AttrDefinition> res = dao.GetAttrDefinitions( criteria, page, rows );
		AttrDictItem attrDictItem = null;
		for(int i=0; i<res.size(); i++) {
			attrDictItem = ConvertAttrDefinitonToAttrDictItem(res.get(i));
			items.add(attrDictItem);
		}
		int total = dao.GetAttrDefinitionsCount( criteria );
		
		AddSystemQueryLog(criteria);
		return total;
	}
	
	public int QueryAttrDictionaryOfRole(AttrDefinition criteria, int page,
			int rows, ArrayList<AttrDictItem> items) throws Exception {
		criteria.setType(AttrDefinition.ATTRTYPEROLE);
		AttributeDAO dao = new AttributeDAOImpl();
		List<AttrDefinition> res = dao.GetAttrDefinitions( criteria, page, rows );
		AttrDictItem attrDictItem = null;
		for(int i=0; i<res.size(); i++) {
			attrDictItem = ConvertAttrDefinitonToAttrDictItem(res.get(i));
			items.add(attrDictItem);
		}
		int total = dao.GetAttrDefinitionsCount( criteria );
		
		AddSystemQueryLog(criteria);
		return total;
	}
	
	public AttrDictItem ConvertAttrDefinitonToAttrDictItem(AttrDefinition attr) throws Exception {

		AttrDictItem item = new AttrDictItem();
		item.setId(attr.getId());
		item.setName(attr.getName());
		item.setCode(attr.getCode());
		
		AttributeDAO dao = new AttributeDAOImpl();
		List<AttrDictionary> attrDicts = dao.GetAttrDictionarysByAttrId(attr.getId());
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

	public void SaveAttrDictionary(AttrDictItem attrItem) throws Exception {
		AttributeDAO dao = new AttributeDAOImpl();
		dao.UpdateAttrDictionary(attrItem.getId(), attrItem.getDictionary());
		
		AddSystemAddOrUpdateLog(attrItem);
	}
	
	private void AddSystemQueryLog(AttrDefinition criteria) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		
		AuditSystemLog auditSystemLog = new AuditSystemLog();
		AuditLogDAO logdao = new AuditLogDAOImpl();
		AuditLogService als = new AuditLogService();
		
		auditSystemLog.setAdminId(als.adminLogin());
		auditSystemLog.setIpAddr("");
		auditSystemLog.setFlag(AuditSystemLog.LOGFLAGQUERY);
		auditSystemLog.setResult(AuditSystemLog.LOGRESULTSUCCESS);
		auditSystemLog.setLATEST_MOD_TIME(timenow);
		auditSystemLog = logdao.AuditSystemLogAdd(auditSystemLog);
		
		if( criteria != null ) {
			AuditSystemLogDescribe auditSystemLogDescribe = new AuditSystemLogDescribe();
			AuditLogDescribeDao logDescdao = new AuditLogDescribeDAOImpl();
			
			auditSystemLogDescribe.setLogid(auditSystemLog.getId());
			String str="";
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				str += criteria.getName()+";";
			}
			if(criteria.getCode() != null && criteria.getCode().length() > 0) {
				str += criteria.getCode()+";";
			}
			auditSystemLogDescribe.setDescrib(str);
			
			auditSystemLogDescribe.setLATEST_MOD_TIME(timenow);
			auditSystemLogDescribe = logDescdao.AuditSystemLogDescribeAdd(auditSystemLogDescribe);
		}
	}
	
	private void AddSystemAddOrUpdateLog(AttrDictItem attrItems) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		AuditSystemLog auditSystemLog = new AuditSystemLog();
		AuditLogDAO logdao = new AuditLogDAOImpl();
		AuditLogService als = new AuditLogService();
		
		auditSystemLog.setAdminId(als.adminLogin());
		auditSystemLog.setIpAddr("");
		
		auditSystemLog.setFlag(AuditSystemLog.LOGFLAGUPDATE);
		auditSystemLog.setResult(AuditSystemLog.LOGRESULTSUCCESS);
		auditSystemLog.setLATEST_MOD_TIME(timenow);
		auditSystemLog = logdao.AuditSystemLogAdd(auditSystemLog);
		
		AuditSystemLogDescribe auditSystemLogDescribe = new AuditSystemLogDescribe();
		AuditLogDescribeDao logDescdao = new AuditLogDescribeDAOImpl();
		
		auditSystemLogDescribe.setLogid(auditSystemLog.getId());
		String str="";
	
		if(attrItems.getName() != null && attrItems.getName().length() > 0) {
			str += attrItems.getName()+";";
		}
		if(attrItems.getCode() != null && attrItems.getCode().length() > 0) {
			str += attrItems.getCode()+";";
		}
		for(int i=0;i<attrItems.getDictionary().size(); i++) {
			str += attrItems.getDictionary().get(i).getValue()+":"+attrItems.getDictionary().get(i).getCode()+";";
		}
		auditSystemLogDescribe.setDescrib(str);
		
		auditSystemLogDescribe.setLATEST_MOD_TIME(timenow);
		auditSystemLogDescribe = logDescdao.AuditSystemLogDescribeAdd(auditSystemLogDescribe);
		
		return ;
	}
	
}

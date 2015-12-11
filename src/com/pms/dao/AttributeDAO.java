package com.pms.dao;

import java.util.List;

import com.pms.model.AttrDefinition;
import com.pms.model.AttrDictionary;

public interface AttributeDAO {

	public List<AttrDefinition> GetAttrDefinitions(AttrDefinition criteria, int page, int rows) throws Exception;
	public int GetAttrDefinitionsCount(AttrDefinition criteria) throws Exception;
	public List<AttrDictionary> GetAttrDictionarysByAttrId(int attrId) throws Exception;
	public void UpdateAttrDictionary(int attrId, List<AttrDictionary> dictionary) throws Exception;
	public List<AttrDictionary> GetRolesDictionarys(int id) throws Exception;
	public List<AttrDictionary> GetDatasDictionarys(String id) throws Exception;
	List<AttrDictionary> GetFeaturesDictionarys(String id) throws Exception;
	public List<AttrDictionary> GetUsersDictionarys(int id) throws Exception;
	public List<AttrDictionary> GetDictsDatasNode(String name, String code, String id) throws Exception;
	public List<AttrDictionary> GetDictsDataTemplatesNode(String name, String code, String id) throws Exception;
	public List<AttrDictionary> GetOrgsDictionarys(String id) throws Exception;
	public AttrDictionary GetAttrDictionarysByAttrIdAndCode(int attrId, String code) throws Exception;
	
	public AttrDefinition GetAttrDefinitionByCode(String code) throws Exception;
	public AttrDictionary AttrDictionaryAdd(AttrDictionary ad) throws Exception;
}

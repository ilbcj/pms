package com.pms.dao;

import java.util.List;

import com.pms.model.AttrDefinition;
import com.pms.model.AttrDictionary;

public interface AttributeDAO {

	List<AttrDefinition> GetAttrDefinitions(AttrDefinition criteria, int page, int rows) throws Exception;
	int GetAttrDefinitionsCount(AttrDefinition criteria) throws Exception;
	List<AttrDictionary> GetAttrDictionarysByAttrId(int attrId) throws Exception;
	void UpdateAttrDictionary(int attrId, List<AttrDictionary> dictionary) throws Exception;
	List<AttrDictionary> GetRolesDictionarys(int id) throws Exception;
	List<AttrDictionary> GetDatasDictionarys(String id) throws Exception;
	List<AttrDictionary> GetUsersDictionarys(int id) throws Exception;
	List<AttrDictionary> GetDictsDatasNode(String name, String code, int id) throws Exception;
	List<AttrDictionary> GetOrgsDictionarys(String id) throws Exception;
}

package com.pms.dao;

import java.util.List;

import com.pms.model.AttrDefinition;
import com.pms.model.AttrDictionary;

public interface AttributeDAO {

	List<AttrDefinition> GetAttrDefinitions(AttrDefinition criteria, int page, int rows) throws Exception;
	int GetAttrDefinitionsCount(AttrDefinition criteria) throws Exception;
	List<AttrDictionary> GetAttrDictionarysByAttrId(int attrId) throws Exception;
}

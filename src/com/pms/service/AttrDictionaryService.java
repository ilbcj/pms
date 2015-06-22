package com.pms.service;

import java.util.ArrayList;
import java.util.List;

import com.pms.dao.AttributeDAO;
import com.pms.dao.impl.AttributeDAOImpl;
import com.pms.dto.AttrDictItem;
import com.pms.model.AttrDefinition;
import com.pms.model.AttrDictionary;

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
		return total;
	}
	
	public AttrDictItem ConvertAttrDefinitonToAttrDictItem(AttrDefinition attr) throws Exception {
		AttrDictItem item = new AttrDictItem();
		item.setId(attr.getId());
		item.setName(attr.getName());
		item.setCode(attr.getCode());
		
		AttributeDAO dao = new AttributeDAOImpl();
		List<AttrDictionary> attrDicts = dao.GetAttrDictionarysByAttrId(attr.getId());
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

		item.setDictionary(data);
		return item;
	}

	public void SaveAttrDictionary(AttrDictItem attrItem) throws Exception {
		AttributeDAO dao = new AttributeDAOImpl();
		dao.UpdateAttrDictionary(attrItem.getId(), attrItem.getDictionary());
	}
	
}

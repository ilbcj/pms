package com.pms.service;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.pms.dao.AttributeDAO;
import com.pms.dao.impl.AttributeDAOImpl;
import com.pms.dao.impl.ResourceDAOImpl;
import com.pms.dto.AttrDictItem;
import com.pms.model.AttrDefinition;
import com.pms.model.AttrDictionary;
import com.pms.model.ResData;

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
	
	public void downLoad() throws Exception {
		ResourceDAOImpl dao=new ResourceDAOImpl();
		List<ResData> res =dao.GetAllDatas();
        
        Document dom=DocumentHelper.createDocument();//创建xml文件 
        Element root=dom.addElement("XValue");//添加根元素,XValue  
        root.addAttribute("X", "X字符串的值");
        String str="\nid\tname\tresource_type\t" +
        		"resource_id\tresource_Status\tresource_Describe\t" +
				"Dataset_Sensitive_Level\tData_Set\tSection_Class\t" +
				"Element\tSection_Relatioin_Class\tOperate_Symbol\t" +
				"Element_Value\tDelete_Status\tData_Version\t" +
				"Latest_Mod_Time\tResource_Remark\n";
        for (int i = 0; i < res.size(); i++)  {  
            str=str+res.get(i).getId()+"\t"+res.get(i).getName()+"\t"+res.get(i).getResource_type()+"\t"+res.get(i).getRESOURCE_ID()+"\t"+res.get(i).getRESOURCE_STATUS()+"\t"+res.get(i).getRESOURCE_DESCRIBE()+"\t"
    		+res.get(i).getDATASET_SENSITIVE_LEVEL()+"\t"+res.get(i).getDATA_SET()+"\t"+res.get(i).getSECTION_CLASS()+"\t"
    		+res.get(i).getELEMENT()+"\t"+res.get(i).getSECTION_RELATIOIN_CLASS()+"\t"+res.get(i).getOPERATE_SYMBOL()+"\t"
    		+res.get(i).getELEMENT_VALUE()+"\t"+res.get(i).getDELETE_STATUS()+"\t"+res.get(i).getDATA_VERSION()+"\t"
    		+res.get(i).getLATEST_MOD_TIME()+"\t"+res.get(i).getRESOURCE_REMARK()+"\n";
        }
        root.setText(str);
        String xml=dom.asXML();
        String filename="wa_authority_resource"+".xml";
        File f1=new File("E:/downLoad/"+filename);  
        PrintWriter pw=new PrintWriter(f1);
        pw.write(xml);  
        pw.close();
        
        Document domIndex=DocumentHelper.createDocument();//创建xml文件
        String xmlIndex=domIndex.asXML(); 
        String filenameIndex="GAB_ZIP_INDEX.xml";
        File f2=new File("E:/downLoad/"+filenameIndex);  
        PrintWriter pwIndex=new PrintWriter(f2);
        pwIndex.write(xmlIndex);  
        pwIndex.close();  
      
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");  
	    String zipNnme = timeFormat.format(new Date())+".zip";

	    ZipService zc = new  ZipService("E:/"+zipNnme);
	    zc.compress("E:/downLoad/");
//        zc.compress(filename);
//        zc.compress(filenameIndex);

	} 
}

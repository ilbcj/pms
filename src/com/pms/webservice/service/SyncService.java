package com.pms.webservice.service;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.pms.webservice.model.DataCommonInfo;
import com.pms.webservice.model.SearchCondition;
import com.pms.webservice.model.UserAuth;

public abstract class SyncService {
	private DataCommonInfo dci;
	private UserAuth ua;
	private SearchCondition sc;
	
	public static SyncService getInstance(Element root)
	{
		NodeList datasetList = root.getElementsByTagName("DATASET");
		for(int i=0;i<datasetList.getLength();i++){
			Element element = (Element)datasetList.item(i); //得到"page"的第i+1组标签
			String name = element.getAttribute("name");  //获得ID属性
			
			//
			//得到标签title的列表
			NodeList titleList = element.getElementsByTagName("title");
			//得到"title"的第1组标签，事实上也只有一组标签
			Element titleElement = (Element)titleList.item(0);
			//获得title元素的第一个值
			String title = titleElement.getFirstChild().getNodeValue();
		}
		return null;
	}
	
	public abstract String GetResult();
}

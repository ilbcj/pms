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
	
	public DataCommonInfo getDci() {
		return dci;
	}

	public void setDci(DataCommonInfo dci) {
		this.dci = dci;
	}

	public UserAuth getUa() {
		return ua;
	}

	public void setUa(UserAuth ua) {
		this.ua = ua;
	}

	public SearchCondition getSc() {
		return sc;
	}

	public void setSc(SearchCondition sc) {
		this.sc = sc;
	}

	public static SyncService getInstance(Element root)
	{
		SyncService result = null;
		DataCommonInfo dci = null;
		UserAuth ua = null;
		SearchCondition sc = null;
		try{
			
			NodeList datasetList = root.getElementsByTagName("DATASET");
			for(int i=0;i<datasetList.getLength();i++) {
				Element element = (Element)datasetList.item(i); //得到"page"的第i+1组标签
				String name = element.getAttribute("name"); //获得name属性
				if("WA_COMMON_010117".equals(name) ) {
					result = new SyncSearchService();
				}
			}
			
			if( result != null ) {
				for(int i=0;i<datasetList.getLength();i++){
					Element element = (Element)datasetList.item(i); //得到"page"的第i+1组标签
					String name = element.getAttribute("name");  //获得ID属性
					if("WA_COMMON_010000".equals(name) ) {
						dci = parseDataCommonInfo( element );
						result.setDci(dci);
					} else if ("WA_COMMON_010001".equals(name) ) {
						ua = parseUserAuth( element );
						result.setUa(ua);
					} else if("WA_COMMON_010117".equals(name) ) {
						sc = parseSearchCondition( element );
						result.setSc(sc);
					}
				}
			}
		}
		catch( Exception e ) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}
	
	private static SearchCondition parseSearchCondition(Element element) {
		// TODO Auto-generated method stub
		return null;
	}

	private static UserAuth parseUserAuth(Element element) {
		// TODO Auto-generated method stub
		return null;
	}

	private static DataCommonInfo parseDataCommonInfo(Element element) {
		// TODO Auto-generated method stub
		return null;
	}

	public abstract String GetResult();
}

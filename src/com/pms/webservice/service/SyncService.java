package com.pms.webservice.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;

import com.pms.webservice.model.Condition;
import com.pms.webservice.model.DataCommonInfo;
import com.pms.webservice.model.SearchCondition;
import com.pms.webservice.model.SubSearchCondition;
import com.pms.webservice.model.UserAuth;

public abstract class SyncService {
	private DataCommonInfo dci;
	private UserAuth ua;
	private SearchCondition sc;
	private SubSearchCondition ssc;
	
	public SubSearchCondition getSsc() {
		return ssc;
	}

	public void setSsc(SubSearchCondition ssc) {
		this.ssc = ssc;
	}

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

	public static SyncService getInstance(Element root) throws Exception {
		SyncService result = null;
		DataCommonInfo dci = null;
		UserAuth ua = null;
		SearchCondition sc = null;
		SubSearchCondition ssc = null;
		
		List<Element> datasetList = root.getChildren();//getElementsByTagName("DATASET");
		for(int i=0;i<datasetList.size();i++) {
			Element element = datasetList.get(i); //得到"page"的第i+1组标签
			if("DATASET".equals(element.getName())) {
				String name = element.getAttributeValue("name"); //获得name属性
				if("WA_COMMON_010117".equals(name) ) {
					result = new SyncSearchService();
				}
			}
		}
		
		if( result != null ) {
			for(int i=0;i<datasetList.size();i++){
				Element element = datasetList.get(i); //得到"page"的第i+1组标签
				if("DATASET".equals(element.getName())) {
					String name = element.getAttributeValue("name");  //获得ID属性
					if("WA_COMMON_010000".equals(name) ) {
						dci = parseDataCommonInfo( element );
						result.setDci(dci);
					} else if ("WA_COMMON_010001".equals(name) ) {
						ua = parseUserAuth( element );
						result.setUa(ua);
					} else if("WA_COMMON_010117".equals(name) ) {
						sc = parseSearchCondition( element );
						result.setSc(sc);
					} else if("WA_COMMON_010121".equals(name) ) {
						ssc = parseSubSearchCondition( element );
						result.setSsc(ssc);
					}
				}
			}
		}
		
		return result;
	}
	
	private static SubSearchCondition parseSubSearchCondition(Element element) throws Exception {
		SubSearchCondition result = null;
		String name = element.getChildren().get(0).getName();
		if( "DATA".equals(name) ) {
			result = new SubSearchCondition();
			List<Element> itemList = element.getChildren().get(0).getChildren();
			for(int i=0; i<itemList.size(); i++) {
				Element item = itemList.get(i);
				if("ITEM".equals(item.getName())) {
				    if( "J010015".equals(item.getAttributeValue("key")) ) {
				    	result.setAlias( item.getAttributeValue("val") );
				    } else if( "J010002".equals(item.getAttributeValue("key")) ) {
						result.setTableName( item.getAttributeValue("val") );
					} 
				} else if ( "CONDITION".equals(item.getName()) ) {
					result.setCONDITION( item.getAttributeValue("rel") );
					List<Condition> conditions = new ArrayList<Condition>();
					List<Element> conditionList = item.getChildren();
					for(int j=0; j<conditionList.size(); j++) {
						Element condition = conditionList.get(j);
						Condition con = new Condition();
						con.setKey( condition.getAttributeValue("key") );
						con.setEng( convertKeyToTableColumnName( condition.getAttributeValue("key") ) );
						con.setVal( condition.getAttributeValue("val") );
						if(con.getEng() == null || con.getEng().length() == 0) {
							throw new Exception("unsupport search column key:" + con.getKey());
						}
						conditions.add(con);
					}
					result.setCONDITIONITEMS(conditions);
				} else if ( "DATASET".equals(item.getName()) ) {
					if( "WA_COMMON_010118".equals( item.getAttributeValue("name")) ) {
						List<Element> children = item.getChildren();
						if( children.size() > 0 ) {
							String childName = children.get(0).getName();
							if( "DATA".equals(childName) ) {
								List<Element> retColList = children.get(0).getChildren();
								List<Condition> retCols = new ArrayList<Condition>();
								for(int k=0; k<retColList.size(); k++) {
									
									
									Element condition = retColList.get(k);
									Condition con = new Condition();
									con.setKey( condition.getAttributeValue("key") );
									con.setEng( convertKeyToTableColumnName( condition.getAttributeValue("key") ) );
									con.setVal( condition.getAttributeValue("val") );
									if(con.getEng() == null || con.getEng().length() == 0) {
										throw new Exception("unsupport search column key:" + con.getKey());
									}
									retCols.add(con);
								}
								result.setRETURNITEMS(retCols);
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	private static SearchCondition parseSearchCondition(Element element) throws Exception {
		SearchCondition result = null;
		String name = element.getChildren().get(0).getName();
		if( "DATA".equals(name) ) {
			result = new SearchCondition();
			List<Element> itemList = element.getChildren().get(0).getChildren();
			for(int i=0; i<itemList.size(); i++) {
				Element item = itemList.get(i);
				if("ITEM".equals(item.getName())) {
					if( "J010002".equals(item.getAttributeValue("key")) ) {
						result.setTableName( item.getAttributeValue("val") );
					} else if ( "I010017".equals(item.getAttributeValue("key"))  ) {
						result.setTotalNum( item.getAttributeValue("val") );
					} else if ( "I010019".equals(item.getAttributeValue("key"))  ) {
						result.setOnceNum( item.getAttributeValue("val") );
					} else if ( "I010018".equals(item.getAttributeValue("key"))  ) {
						result.setIsAsyn( item.getAttributeValue("val") );
					}
				} else if ( "CONDITION".equals(item.getName()) ) {
					result.setCONDITION( item.getAttributeValue("rel") );
					List<Condition> conditions = new ArrayList<Condition>();
					List<Element> conditionList = item.getChildren();
					for(int j=0; j<conditionList.size(); j++) {
						Element condition = conditionList.get(j);
						Condition con = new Condition();
						con.setKey( condition.getAttributeValue("key") );
						con.setEng( convertKeyToTableColumnName( condition.getAttributeValue("key") ) );
						con.setVal( condition.getAttributeValue("val") );
						if(con.getEng() == null || con.getEng().length() == 0) {
							throw new Exception("unsupport search column key:" + con.getKey());
						}
						conditions.add(con);
					}
					result.setCONDITIONITEMS(conditions);
				} else if ( "DATASET".equals(item.getName()) ) {
					if( "WA_COMMON_010118".equals( item.getAttributeValue("name")) ) {
						List<Element> children = item.getChildren();
						if( children.size() > 0 ) {
							String childName = children.get(0).getName();
							if( "DATA".equals(childName) ) {
								List<Element> retColList = children.get(0).getChildren();
								List<Condition> retCols = new ArrayList<Condition>();
								for(int k=0; k<retColList.size(); k++) {
									
									
									Element condition = retColList.get(k);
									Condition con = new Condition();
									con.setKey( condition.getAttributeValue("key") );
									con.setEng( convertKeyToTableColumnName( condition.getAttributeValue("key") ) );
									con.setVal( condition.getAttributeValue("val") );
									if(con.getEng() == null || con.getEng().length() == 0) {
										throw new Exception("unsupport search column key:" + con.getKey());
									}
									retCols.add(con);
								}
								result.setRETURNITEMS(retCols);
							}
						}
					}
				}
			}
		}
		return result;
	}

	private static String convertKeyToTableColumnName(String key) {
		Map<String, String> keyColumnMap = new HashMap<String, String>();
		
		keyColumnMap.put("A010001", "GA_DEPARTMENT");
		keyColumnMap.put("A010004", "DATA_SET");
		
		keyColumnMap.put("J030001", "SECTION_CLASS");
		keyColumnMap.put("J030002", "SECTION_RELATIOIN_CLASS");
		keyColumnMap.put("J030003", "DATASET_SENSITIVE_LEVEL");
		keyColumnMap.put("J030006", "RESOURCE_ID");
		keyColumnMap.put("J030010", "RESOURCE_STATUS");
		keyColumnMap.put("J030014", "CERTIFICATE_CODE_MD5");
		keyColumnMap.put("J030016", "USER_STATUS");
		keyColumnMap.put("J030029", "RESOURCE_TYPE");
		
		keyColumnMap.put("I010026", "BUSINESS_ROLE");
		
		if(key.contains(".")) {
			key = key.substring(key.indexOf('.')+1);
		}
		
		return keyColumnMap.get(key);
	}
	
	private static UserAuth parseUserAuth(Element element) {
		UserAuth result = null;
		String name = element.getChildren().get(0).getName();
		if( "DATA".equals(name) ) {
			result = new UserAuth();
			List<Element> itemList = element.getChildren().get(0).getChildren();
			for(int i=0;i<itemList.size();i++) {
				Element item = itemList.get(i);
				if("ITEM".equals(item.getName())) {
					if( "A010001".equals(item.getAttributeValue("key")) ) {
						result.setGA_DEPARTMENT( item.getAttributeValue("val") );
					} else if ( "I010026".equals(item.getAttributeValue("key"))  ) {
						result.setUSER_NAME( item.getAttributeValue( "val") );
					} else if ( "I010024".equals(item.getAttributeValue("key"))  ) {
						result.setROLE_ID( item.getAttributeValue( "val") );
					} else if ( "I010025".equals(item.getAttributeValue("key"))  ) {
						result.setROLE_TYPE( item.getAttributeValue( "val") );
					} else if ( "I010027".equals(item.getAttributeValue("key"))  ) {
						result.setCOOKIES( item.getAttributeValue( "val") );
					}
				}
			}
		}
		return result;
	}

	private static DataCommonInfo parseDataCommonInfo(Element element) {
		DataCommonInfo result = null;
		String name = element.getChildren().get(0).getName();
		if( "DATA".equals(name) ) {
			result = new DataCommonInfo();
			List<Element> itemList = element.getChildren().get(0).getChildren();
			for(int i=0;i<itemList.size();i++) {
				Element item = itemList.get(i);
				if("ITEM".equals(item.getName())) {
					if( "H010006".equals(item.getAttributeValue("key")) ) {
						result.setFROM( item.getAttributeValue("val") );
					} else if ( "H010007".equals(item.getAttributeValue("key"))  ) {
						result.setTO( item.getAttributeValue( "val") );
					} else if ( "I010014".equals(item.getAttributeValue("key"))  ) {
						result.setMESSAGE_SEQUENCE( item.getAttributeValue( "val") );
					} else if ( "I010013".equals(item.getAttributeValue("key"))  ) {
						result.setMESSAGE_TYPE( item.getAttributeValue( "val") );
					} else if ( "I010010".equals(item.getAttributeValue("key"))  ) {
						result.setBUSINESS_SERVER_TYPE( item.getAttributeValue( "val") );
					}
				}
			}
		}
		return result;
	}
	
	public abstract String GetResult() throws IOException;

}

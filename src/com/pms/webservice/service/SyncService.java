package com.pms.webservice.service;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;

import com.pms.webservice.model.Condition;
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

	public static SyncService getInstance(Element root) {
		SyncService result = null;
		DataCommonInfo dci = null;
		UserAuth ua = null;
		SearchCondition sc = null;
		try{
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
						}
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
						con.setEng( condition.getAttributeValue("eng") );
						con.setVal( condition.getAttributeValue("val") );
						conditions.add(con);
					}
					result.setCONDITIONITEMS(conditions);
				} else if ( "DATASET".equals(item.getName()) ) {
					if( "WA_COMMON_010118".equals( item.getAttributeValue("name")) ) {
						// TODO add some deal process.
						List<String> retCols = new ArrayList<String>();
						List<Element> retColList = item.getChildren();
						for(int k=0; k<retColList.size(); k++) {
							Element retCol = retColList.get(k);
							retCols.add(retCol.getName());
						}
						result.setRETURNINFO(retCols);
					}
				}
			}
		}
		return result;
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
	
//	public static SyncService getInstance_bak(root)
//	{
//		SyncService result = null;
//		DataCommonInfo dci = null;
//		UserAuth ua = null;
//		SearchCondition sc = null;
//		try{
//			NodeList test = root.getChildNodes();
//			Node n = test.item(0);
//			n.getNodeName();
//			NodeList datasetList = root.getElementsByTagName("DATASET");
//			for(int i=0;i<datasetList.getLength();i++) {
//				Element element = (Element)datasetList.item(i); //得到"page"的第i+1组标签
//				String name = element.getAttribute("name"); //获得name属性
//				if("WA_COMMON_010117".equals(name) ) {
//					result = new SyncSearchService();
//				}
//			}
//			
//			if( result != null ) {
//				for(int i=0;i<datasetList.getLength();i++){
//					Element element = (Element)datasetList.item(i); //得到"page"的第i+1组标签
//					String name = element.getAttribute("name");  //获得ID属性
//					if("WA_COMMON_010000".equals(name) ) {
//						dci = parseDataCommonInfo( element );
//						result.setDci(dci);
//					} else if ("WA_COMMON_010001".equals(name) ) {
//						ua = parseUserAuth( element );
//						result.setUa(ua);
//					} else if("WA_COMMON_010117".equals(name) ) {
//						sc = parseSearchCondition( element );
//						result.setSc(sc);
//					}
//				}
//			}
//		}
//		catch( Exception e ) {
//			e.printStackTrace();
//			result = null;
//		}
//		return result;
//	}
	
//	private static SearchCondition parseSearchCondition(Element element) {
//		SearchCondition result = null;
//		String name = element.getFirstChild().getNodeName();
//		if( "DATA".equals(name) ) {
//			result = new SearchCondition();
//			NodeList itemList = element.getFirstChild().getChildNodes();
//			for(int i=0; i<itemList.getLength(); i++) {
//				Element item = (Element)itemList.item(i);
//				if("ITEM".equals(item.getNodeName())) {
//					if( "J010002".equals(item.getAttribute("key")) ) {
//						result.setTableName( item.getAttribute("val") );
//					} else if ( "I010017".equals(item.getAttribute("key"))  ) {
//						result.setTotalNum( item.getAttribute( "val") );
//					} else if ( "I010019".equals(item.getAttribute("key"))  ) {
//						result.setOnceNum( item.getAttribute( "val") );
//					} else if ( "I010018".equals(item.getAttribute("key"))  ) {
//						result.setIsAsyn( item.getAttribute( "val") );
//					}
//				} else if ( "CONDITION".equals(item.getNodeName()) ) {
//					result.setCONDITION( item.getAttribute("rel") );
//					List<Condition> conditions = new ArrayList<Condition>();
//					NodeList conditionList = item.getChildNodes();
//					for(int j=0; j<conditionList.getLength(); j++) {
//						Element condition = (Element)conditionList.item(j);
//						Condition con = new Condition();
//						con.setKey( condition.getAttribute("key") );
//						con.setEng( condition.getAttribute("eng") );
//						con.setVal( condition.getAttribute("val") );
//						conditions.add(con);
//					}
//					result.setCONDITIONITEMS(conditions);
//				} else if ( "DATASET".equals(item.getNodeName()) ) {
//					if( "WA_COMMON_010118".equals( item.getAttribute("name")) ) {
//						// TODO add some deal process.
//						List<String> retCols = new ArrayList<String>();
//						NodeList retColList = item.getChildNodes();
//						for(int k=0; k<retColList.getLength(); k++) {
//							Element retCol = (Element)retColList.item(k);
//							retCols.add(retCol.getNodeName());
//						}
//						result.setRETURNINFO(retCols);
//					}
//				}
//			}
//		}
//		return result;
//	}
//
//	private static UserAuth parseUserAuth(Element element) {
//		UserAuth result = null;
//		String name = element.getFirstChild().getNodeName();
//		if( "DATA".equals(name) ) {
//			result = new UserAuth();
//			NodeList itemList = element.getFirstChild().getChildNodes();
//			for(int i=0;i<itemList.getLength();i++) {
//				Element item = (Element)itemList.item(i);
//				if("ITEM".equals(item.getNodeName())) {
//					if( "A010001".equals(item.getAttribute("key")) ) {
//						result.setGA_DEPARTMENT( item.getAttribute("val") );
//					} else if ( "I010026".equals(item.getAttribute("key"))  ) {
//						result.setUSER_NAME( item.getAttribute( "val") );
//					} else if ( "I010024".equals(item.getAttribute("key"))  ) {
//						result.setROLE_ID( item.getAttribute( "val") );
//					} else if ( "I010025".equals(item.getAttribute("key"))  ) {
//						result.setROLE_TYPE( item.getAttribute( "val") );
//					} else if ( "I010027".equals(item.getAttribute("key"))  ) {
//						result.setCOOKIES( item.getAttribute( "val") );
//					}
//				}
//			}
//		}
//		return result;
//	}
//
//	private static DataCommonInfo parseDataCommonInfo(Element element) {
//		DataCommonInfo result = null;
//		String name = element.getFirstChild().getNodeName();
//		if( "DATA".equals(name) ) {
//			result = new DataCommonInfo();
//			NodeList itemList = element.getFirstChild().getChildNodes();
//			for(int i=0;i<itemList.getLength();i++) {
//				Element item = (Element)itemList.item(i);
//				if("ITEM".equals(item.getNodeName())) {
//					if( "H010006".equals(item.getAttribute("key")) ) {
//						result.setFROM( item.getAttribute("val") );
//					} else if ( "H010007".equals(item.getAttribute("key"))  ) {
//						result.setTO( item.getAttribute( "val") );
//					} else if ( "I010014".equals(item.getAttribute("key"))  ) {
//						result.setMESSAGE_SEQUENCE( item.getAttribute( "val") );
//					} else if ( "I010013".equals(item.getAttribute("key"))  ) {
//						result.setMESSAGE_TYPE( item.getAttribute( "val") );
//					} else if ( "I010010".equals(item.getAttribute("key"))  ) {
//						result.setBUSINESS_SERVER_TYPE( item.getAttribute( "val") );
//					}
//				}
//			}
//		}
//		return result;
//	}

	public abstract String GetResult();

}

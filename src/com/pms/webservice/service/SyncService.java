package com.pms.webservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Element;

//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;

import com.pms.webservice.model.Condition;
import com.pms.webservice.model.Item;
import com.pms.webservice.model.DataCommonInfo;
import com.pms.webservice.model.SearchCondition;
import com.pms.webservice.model.SubSearchCondition;
import com.pms.webservice.model.UserAuth;
import com.pms.webservice.model.auth.AuthCondition;
import com.pms.webservice.model.auth.Common010032;

public abstract class SyncService {
	private static Log logger = LogFactory.getLog(SyncService.class);
	private DataCommonInfo dci;
	private UserAuth ua;
	private SearchCondition sc;
	private SubSearchCondition ssc;
	private AuthCondition ac;
	
	public AuthCondition getAc() {
		return ac;
	}

	public void setAc(AuthCondition ac) {
		this.ac = ac;
	}

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
		AuthCondition ac = null;
		
		List<Element> datasetList = root.getChildren();//getElementsByTagName("DATASET");
		for(int i=0;i<datasetList.size();i++) {
			Element element = datasetList.get(i); //得到"page"的第i+1组标签
			if("DATASET".equals(element.getName())) {
				String name = element.getAttributeValue("name"); //获得name属性
				if("WA_COMMON_010117".equals(name) ) {
					result = new SyncSearchService();
				} else if ("WA_COMMON_010031".equals(name) ) {
					result = new SyncAuthenticateService();
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
					} else if("WA_COMMON_010031".equals(name) ) {
						ac = parseAuthCondition( element );
						result.setAc(ac);
					}
				}
			}
		}
		
		return result;
	}
	
	private static AuthCondition parseAuthCondition(Element root) throws Exception {
		AuthCondition result = null;
		String name = root.getChildren().get(0).getName();
		if( "DATA".equals(name) ) {
			result = new AuthCondition();
			List<Element> elementList = root.getChildren().get(0).getChildren();
			for(int i=0; i<elementList.size(); i++) {
				Element element = elementList.get(i);
				if ( "CONDITION".equals(element.getName()) || "STC".equalsIgnoreCase( element.getAttributeValue("rel") ) ) {
					Condition stc = new Condition();
					stc.setRel("STC");
					List<Item> items = new ArrayList<Item>();
					List<Element> itemList = element.getChildren();
					for(int j=0; j<itemList.size(); j++) {
						Element current = itemList.get(j);
						Item item = new Item();
						item.setKey( current.getAttributeValue("key") );
						item.setEng( convertKeyToEngName( current.getAttributeValue("key") ) );
						item.setVal( current.getAttributeValue("val") );
						if(item.getEng() == null || item.getEng().length() == 0) {
							throw new Exception("unsupport stc item key:" + item.getKey());
						}
						items.add(item);
					}
					stc.setItems(items);
					result.setStc(stc);
				} else if ( "DATASET".equals(element.getName()) ) {
					if( "WA_COMMON_010032".equals( element.getAttributeValue("name")) ) {
						List<Common010032> common010032 = parse010032(element);
						result.setCommon010032(common010032);
					}
				}
			}
		}
		return result;
	}
	
//<DATASET name="WA_COMMON_010032">
//	<DATA>
//		<DATASET name="WA_SOURCE_0001">
//			<DATA>
//				<CONDITION rel="AND">
//					<CONDITION rel="BTW">
//						<ITEM key="H010014" eng="CAPTURE_TIME" val="1426694400"/>
//						<ITEM key="H010014" eng="CAPTURE_TIME" val="1434556799"/>
//					</CONDITION>
//					<CONDITION rel="IN">
//						<ITEM key="B020001" eng="ISP_ID" val="01,02,03,04,05,06"/>
//					</CONDITION>
//					<CONDITION rel="IN">
//						<ITEM key="H010001" eng="HTTP_TYPE" val="1000001,1000002,1000003,1000004,1009996,1009997,1009999"/>
//					</CONDITION>
//					<CONDITION rel="IN">
//						<ITEM key="B050016" eng="DATA_SOURCE" val="111,120,123,124"/>
//					</CONDITION>
//				</CONDITION>
//			</DATA>
//			<DATA/>
//		</DATASET>
//	</DATA>
//</DATASET>
	private static List<Common010032> parse010032(Element root) throws Exception {
		List<Common010032> result = new ArrayList<Common010032>();
		List<Element> xmlChildren = root.getChildren();
		for(int i = 0; i < xmlChildren.size(); i++) {
			Common010032 common010032 = new Common010032();
			String name = xmlChildren.get(i).getName();
			if( "DATA".equals(name) ) {
				Element xmlSourceDataSet = xmlChildren.get(i).getChildren().get(0);
				common010032.setSourceName(xmlSourceDataSet.getAttributeValue("name"));
				
				//parse condition
				Element xmlConditionData = xmlSourceDataSet.getChildren().get(0);
				if(xmlConditionData == null || !"DATA".equals(xmlConditionData.getName())) {
					logger.info("parse 'WA_COMMON_010032 --> data --> dataset --> data(0)' error.");
					continue;
				}
				else {
					Element xmlParentCondition = xmlConditionData.getChildren().get(0);
					if(xmlParentCondition == null || !"CONDITION".equals(xmlParentCondition.getName())) {
						logger.info("parse 'WA_COMMON_010032 --> data --> dataset --> data(0) --> CONDITION' error.");
						continue;
					}
					else {
						common010032.setParentCondition(xmlParentCondition.getAttributeValue("rel"));
						List<Element> xmlSubConditions = xmlParentCondition.getChildren();
						List<Condition> conditions = new ArrayList<Condition>();
						for(int j = 0; j < xmlSubConditions.size(); j++) {
							Condition condition = new Condition();
							Element xmlCurrentCondition = xmlSubConditions.get(j);
							if( !"CONDITION".equals(xmlCurrentCondition.getName()) ) {
								logger.info("parse 'WA_COMMON_010032 --> data --> dataset --> data(0) --> CONDITION --> CONDITION(" + j + ")' error.");
								continue;
							}
							else {
								condition.setRel(xmlCurrentCondition.getAttributeValue("rel"));
								List<Element> xmlItems = xmlCurrentCondition.getChildren();
								List<Item> items = new ArrayList<Item>();
								for(int k = 0; k < xmlItems.size(); k++) {
									Element xmlCurrentItem = xmlItems.get(k);
									String[] values = xmlCurrentItem.getAttributeValue("val").split(",");
									for(int x = 0; x < values.length; x++) {
										Item item = new Item();
										item.setKey( xmlCurrentItem.getAttributeValue("key") );
										item.setEng( convertKeyToEngName( xmlCurrentItem.getAttributeValue("key") ) );
										item.setVal( values[x] );
										if(item.getEng() == null || item.getEng().length() == 0) {
											throw new Exception("unsupport search column key:" + item.getKey());
										}
										items.add(item);
									}
								}
								condition.setItems(items);
							}
							conditions.add(condition);
						}
						common010032.setSubConditions(conditions);
					}
				}
				
				//parse return columns
				Element xmlRetColumnsData = xmlSourceDataSet.getChildren().get(1);
				if(xmlRetColumnsData == null || !"DATA".equals(xmlRetColumnsData.getName()) ) {
					logger.info("parse 'WA_COMMON_010032 --> data --> dataset --> data(1)' error.");
					continue;
				}
				else {
					List<Element> xmlItems = xmlRetColumnsData.getChildren();
					List<Item> items = new ArrayList<Item>();
					for(int j = 0; j < xmlItems.size(); j++) {
						Element xmlCurrentItem = xmlItems.get(j);
						Item item = new Item();
						item.setKey( xmlCurrentItem.getAttributeValue("key") );
						item.setEng( convertKeyToEngName( xmlCurrentItem.getAttributeValue("key") ) );
						item.setVal( xmlCurrentItem.getAttributeValue("val") );
						if(item.getEng() == null || item.getEng().length() == 0) {
							throw new Exception("unsupport search column key:" + item.getKey());
						}
						items.add(item);
					}
					common010032.setItems(items);
				}
			}
			else {
				logger.info("parse 'WA_COMMON_010032 --> data' error.");
				continue;
			}
			result.add(common010032);
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
					List<Item> conditions = new ArrayList<Item>();
					List<Element> conditionList = item.getChildren();
					for(int j=0; j<conditionList.size(); j++) {
						Element condition = conditionList.get(j);
						Item con = new Item();
						con.setKey( condition.getAttributeValue("key") );
						con.setEng( convertKeyToEngName( condition.getAttributeValue("key") ) );
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
								List<Item> retCols = new ArrayList<Item>();
								for(int k=0; k<retColList.size(); k++) {
									
									Element condition = retColList.get(k);
									Item con = new Item();
									con.setKey( condition.getAttributeValue("key") );
									con.setEng( convertKeyToEngName( condition.getAttributeValue("key") ) );
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
					else if( "WA_COMMON_010143".equals( item.getAttributeValue("name")) ) {
						parse010143(item, result);
						result.setCONNECTTYPE(SearchCondition.CONNECT_TYPE_010121);
					}
				}
			}
		}
		return result;
	}
	
//	<DATASET name="WA_COMMON_010143" rmk="查询要求返回数据列">
//		<DATA>
//			<!--层次化查询起点-->
//			<CONDITION rel="AND">
//				<ITEM key="A010001" val="320100" chn="公安组织机构代码"/>
//			</CONDITION>
//			<!--父子之间关联关系-->
//			<CONDITION rel="AND">
//				<ITEM key="A010001" dstkey="E010011" chn="父子之间关联关系"/>
//			</CONDITION>
//		</DATA>
//	</DATASET>
	private static void parse010143( Element element, SearchCondition sc ) throws Exception{
		String name = element.getChildren().get(0).getName();
		if( "DATA".equals(name) ) {
			List<Element> itemList = element.getChildren().get(0).getChildren();
			Element item = itemList.get(0);
			sc.setCONDITION_START(item.getAttributeValue("rel"));
			List<Element> conditionList = item.getChildren();
			List<Item> conditions = new ArrayList<Item>();
			for(int i=0; i<conditionList.size(); i++) {
				Element condition = conditionList.get(i);
				Item con = new Item();
				con.setKey( condition.getAttributeValue("key") );
				con.setEng( convertKeyToEngName( condition.getAttributeValue("key") ) );
				con.setVal( condition.getAttributeValue("val") );
				if(con.getEng() == null || con.getEng().length() == 0) {
					throw new Exception("unsupport search column key:" + con.getKey());
				}
				conditions.add(con);
			}
			sc.setSTARTITEMS(conditions);
			
			item = itemList.get(1);
			sc.setCONDITION_CONNECT(item.getAttributeValue("rel"));
			conditionList = item.getChildren();
			conditions = new ArrayList<Item>();
			for(int i=0; i<conditionList.size(); i++) {
				Element condition = conditionList.get(i);
				Item con = new Item();
				con.setKey( condition.getAttributeValue("key") );
				con.setEng( convertKeyToEngName( condition.getAttributeValue("key") ) );
				con.setVal( convertKeyToEngName( condition.getAttributeValue("dstkey") ) );
				if(con.getEng() == null || con.getEng().length() == 0) {
					throw new Exception("unsupport search column key:" + con.getKey());
				}
				conditions.add(con);
			}
			sc.setCONNECTITEMS(conditions);
		}
		return;
	}
	
	private static SearchCondition parseSearchCondition(Element element) throws Exception {
		SearchCondition result = new SearchCondition();
		List<Element> rootChildren = element.getChildren();
		for(int k = 0; k<rootChildren.size(); k++) {
			String name = rootChildren.get(k).getName();
			if( "DATA".equals(name) ) {
				List<Element> itemList = rootChildren.get(k).getChildren();
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
						List<Item> conditions = new ArrayList<Item>();
						List<Element> conditionList = item.getChildren();
						for(int j=0; j<conditionList.size(); j++) {
							Element condition = conditionList.get(j);
							Item con = new Item();
							con.setKey( condition.getAttributeValue("key") );
							con.setEng( convertKeyToEngName( condition.getAttributeValue("key") ) );
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
									List<Item> retCols = new ArrayList<Item>();
									for(int j=0; j<retColList.size(); j++) {
										
										
										Element condition = retColList.get(j);
										Item con = new Item();
										con.setKey( condition.getAttributeValue("key") );
										con.setEng( convertKeyToEngName( condition.getAttributeValue("key") ) );
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
			else if ( "DATASET".equals(name)  ) {
				String datasetName = rootChildren.get(k).getAttributeValue("name"); //获得name属性
				if("WA_COMMON_010143".equals(datasetName) ) {
					parse010143(rootChildren.get(k), result);
					result.setCONNECTTYPE(SearchCondition.CONNECT_TYPE_010117);
				}
				
			}
		}
		
		return result;
	}

	private static String convertKeyToEngName(String key) {
		Map<String, String> keyColumnMap = new HashMap<String, String>();
		
		keyColumnMap.put("A010001", "GA_DEPARTMENT");
		keyColumnMap.put("A010004", "DATA_SET");
		keyColumnMap.put("A010005", "PARENT_ORG");
		
		keyColumnMap.put("J030001", "SECTION_CLASS");
		keyColumnMap.put("J030002", "SECTION_RELATIOIN_CLASS");
		keyColumnMap.put("J030003", "DATASET_SENSITIVE_LEVEL");
		keyColumnMap.put("J030006", "RESOURCE_ID");
		keyColumnMap.put("J030010", "RESOURCE_STATUS");
		keyColumnMap.put("J030014", "CERTIFICATE_CODE_MD5");
		keyColumnMap.put("J030016", "USER_STATUS");
		keyColumnMap.put("J030029", "RESOURCE_TYPE");
		
		keyColumnMap.put("I010026", "BUSINESS_ROLE");
		
		keyColumnMap.put("H010001", "H010001");//??????
		keyColumnMap.put("H010005", "SEARCH_ID");
		keyColumnMap.put("H010014", "H010014");//??????
		keyColumnMap.put("H010034", "SENSITIVE_LEVEL");
		
		keyColumnMap.put("B020001", "B020001");//??????
		keyColumnMap.put("B050016", "SYSTEM_TYPE");
		keyColumnMap.put("B030005", "CERTIFICATE_CODE");
		
		
		keyColumnMap.put("G010002", "URL");
		
		keyColumnMap.put("220040B", "AUTH_ACCOUNT");
		keyColumnMap.put("B020001", "ISP_ID");
		keyColumnMap.put("B020005", "BUYPHONE");
		keyColumnMap.put("B020007", "IMSI");
		keyColumnMap.put("B040005", "PASSWORD");
		keyColumnMap.put("B040021", "AUTH_TYPE");
		keyColumnMap.put("B040022", "AUTH_ACCOUNT");
		
		keyColumnMap.put("C040003", "HARDWARE_SIGNATURE");
		keyColumnMap.put("C050001", "EQUIPMENT_ID");
		
		keyColumnMap.put("F010008", "COLLECT_PLACE");
		keyColumnMap.put("F020004", "SRC_IP");
		keyColumnMap.put("F020005", "DST_IP");
		keyColumnMap.put("F020006", "SRC_PORT");
		keyColumnMap.put("F020007", "DST_PORT");
		keyColumnMap.put("F020010", "SRC_IPV6");
		keyColumnMap.put("F020011", "DST_IPV6");
		keyColumnMap.put("F020016", "SRC_IP_AREA");
		keyColumnMap.put("F020017", "DST_IP_AREA");
		keyColumnMap.put("F030002", "BASE_STATION_ID");
		
		keyColumnMap.put("G010003", "TopDomain");
		keyColumnMap.put("G020004", "SERVICECODE");
		
		keyColumnMap.put("H010001", "VOICE_TYPE");
		keyColumnMap.put("H010013", "SESSIONID");
		keyColumnMap.put("H010014", "CAPTURE_TIME");
		keyColumnMap.put("H010018", "DEVICE_ID");
		keyColumnMap.put("H010019", "MAINFILE");
		keyColumnMap.put("H010021", "FILESIZE");
		keyColumnMap.put("H010035", "OWNERSHIP_LAND");
		keyColumnMap.put("H010036", "INTERNET_LAND");
		
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
	
	protected void itemSetAttribute(Element item, String key, String value) {
		item.setAttribute(key, value == null ? "" : value);
	}
	
	public abstract String GetResult() throws Exception;

}

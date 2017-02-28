/**   
 * @ClassName:     ${SyncService}   
 * @Description:   ${鉴权服务父类}   
 * 
 * @ProductName:   ${中盈集中授权平台}
 * @author         ${北京中盈网信科技有限公司}  
 * @version        V1.0     
 * @Date           ${2014.9.16} 
*/
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

//import com.pms.webservice.model.Common010121;
//import com.pms.webservice.model.Common010123;
import com.pms.webservice.model.Condition;
import com.pms.webservice.model.Item;
import com.pms.webservice.model.DataCommonInfo;
import com.pms.webservice.model.SearchCondition;
import com.pms.webservice.model.SubSearchCondition;
import com.pms.webservice.model.UserAuth;
import com.pms.webservice.model.acquiredata.AcquiredataCondition;
import com.pms.webservice.model.acquiredata.Feedback;
import com.pms.webservice.model.acquiredata.FeedbackCondition;
import com.pms.webservice.model.auth.AuthCondition;
import com.pms.webservice.model.auth.Common010032;
import com.pms.webservice.model.exchange.Common010131;
import com.pms.webservice.model.exchange.ExchangeCondition;
import com.pms.webservice.model.search.Common_010121;
import com.pms.webservice.model.search.Common_010123;
import com.pms.webservice.model.search.ComplexSearchCondition;
import com.pms.webservice.model.search.NestConditions;

public abstract class SyncService {
	private static Log logger = LogFactory.getLog(SyncService.class);
	private DataCommonInfo dci;
	private UserAuth ua;
	private SearchCondition sc;
	private ComplexSearchCondition csc;
	private SubSearchCondition ssc;
	private AuthCondition ac;
	private ExchangeCondition ec;
	private AcquiredataCondition adc;
	private FeedbackCondition fc;
	
	public FeedbackCondition getFc() {
		return fc;
	}

	public void setFc(FeedbackCondition fc) {
		this.fc = fc;
	}

	public AcquiredataCondition getAdc() {
		return adc;
	}

	public void setAdc(AcquiredataCondition adc) {
		this.adc = adc;
	}

	public ExchangeCondition getEc() {
		return ec;
	}

	public void setEc(ExchangeCondition ec) {
		this.ec = ec;
	}

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
	
	public ComplexSearchCondition getCsc() {
		return csc;
	}

	public void setCsc(ComplexSearchCondition csc) {
		this.csc = csc;
	}

	public static SyncService getInstance(Element root) throws Exception {
		SyncService result = null;
		DataCommonInfo dci = null;
		UserAuth ua = null;
		//SearchCondition sc = null;
		ComplexSearchCondition csc = null;
		SubSearchCondition ssc = null;
		AuthCondition ac = null;
		ExchangeCondition ec = null;
		AcquiredataCondition adc = null;
		FeedbackCondition fc = null;
		
		List<Element> datasetList = root.getChildren();//getElementsByTagName("DATASET");
		for(int i=0;i<datasetList.size();i++) {
			Element element = datasetList.get(i); //得到"page"的第i+1组标签
			if("DATASET".equals(element.getName())) {
				String name = element.getAttributeValue("name"); //获得name属性
				if("WA_COMMON_010117".equals(name) ) {
					//result = new SyncSearchService();
					result = new SyncComplexSearchService();
				} else if ("WA_COMMON_010031".equalsIgnoreCase(name) ) {
					result = new SyncAuthenticateService();
				} else if ("WA_COMMON_010130".equalsIgnoreCase(name) ) {
					result = new SyncDataexchangeService();
				} else if ("WA_COMMON_010220".equalsIgnoreCase(name)) {
					result = new SyncAcquireDataService();
				} else if ("WA_COMMON_010046".equalsIgnoreCase(name)) {
					result = new SyncFeedbackService();
				}				
			}
		}
		
		if( result != null ) {
			for(int i=0;i<datasetList.size();i++){
				Element element = datasetList.get(i); //得到"page"的第i+1组标签
				if("DATASET".equals(element.getName())) {
					String name = element.getAttributeValue("name");  //获得ID属性
					if("WA_COMMON_010000".equalsIgnoreCase(name) ) {
						dci = parseDataCommonInfo( element );
						result.setDci(dci);
					} else if ("WA_COMMON_010001".equalsIgnoreCase(name) ) {
						ua = parseUserAuth( element );
						result.setUa(ua);
					} else if("WA_COMMON_010117".equalsIgnoreCase(name) ) {
//						sc = parseSearchCondition( element );
//						result.setSc(sc);
						csc = parseComplexSearchCondition( element );
						result.setCsc( csc );
					} else if("WA_COMMON_010121".equalsIgnoreCase(name) ) {
						ssc = parseSubSearchCondition( element );
						result.setSsc(ssc);
					} else if("WA_COMMON_010031".equalsIgnoreCase(name) ) {
						ac = parseAuthCondition( element );
						result.setAc(ac);
					} else if("WA_COMMON_010130".equalsIgnoreCase(name) ) {
						ec = parseExchangeCondition( element );
						result.setEc(ec);
					} else if ("WA_COMMON_010220".equalsIgnoreCase(name)) {
						adc = parseAcquriedataCondition( element );
						result.setAdc(adc);
					} else if ("WA_COMMON_010046".equalsIgnoreCase(name)) {
						fc = parseFeedbackCondition( element );
						result.setFc(fc);
					}	
				}
			}
		}
		
		return result;
	}
	
	private static FeedbackCondition parseFeedbackCondition( Element root ) throws Exception {
		FeedbackCondition result = new FeedbackCondition();
		List<Feedback> fbs = new ArrayList<Feedback>();
		
		List<Element> elementList = root.getChildren();
		if(elementList == null) {
			return result;
		}
		
		for(int i = 0; i < elementList.size(); i++) {
			Element element = elementList.get(i);
			if( "DATA".equals(element.getName()) ) {
				List<Element> itemList = element.getChildren();
				Feedback fb = new Feedback();
				for( int j = 0; j < itemList.size(); j++ ) {
					Element current = itemList.get(j);
					Item item = new Item();
					item.setKey( current.getAttributeValue("key") );
					item.setEng( convertKeyToEngNameWithAlias( current.getAttributeValue("key") ) );
					item.setVal( current.getAttributeValue("val") );
					if(item.getEng() == null || item.getEng().length() == 0) {
						String loginfo = "unsupport item key:" + item.getKey();
						logger.error(loginfo);
						throw new Exception(loginfo);
					}
					if( "H010019".equalsIgnoreCase( item.getKey() ) || "H010020".equalsIgnoreCase( item.getKey() ) ) {
						fb.setFile(item);
					}
					else if( "I030003".equalsIgnoreCase( item.getKey() )) {
						fb.setStatus(item);
					}
					else {
						String loginfo = "unknow key in feedback request's item:" + item.getKey();
						logger.error(loginfo);
						throw new Exception(loginfo);
						
					}
				}
				fbs.add(fb);				
			}
		}
		result.setResults(fbs);
		return result;
	}
	
	private static AcquiredataCondition parseAcquriedataCondition( Element root) throws Exception {
		AcquiredataCondition result = new AcquiredataCondition();
		List<Item> all = new ArrayList<Item>();
		List<Item> add = new ArrayList<Item>();
		
		List<Element> elementList = root.getChildren();
		if(elementList == null) {
			return result;
		}
		for(int i = 0; i < elementList.size(); i++) {
			Element element = elementList.get(i);
			if( "DATA".equals(element.getName()) ) {
				Element current = element.getChildren().get(0);
				Item item = new Item();
				item.setKey( current.getAttributeValue("key") );
				item.setEng( convertKeyToEngNameWithAlias( current.getAttributeValue("key") ) );
				item.setVal( current.getAttributeValue("val") );
				if(item.getEng() == null || item.getEng().length() == 0) {
					throw new Exception("unsupport item key:" + item.getKey());
				}
				if( "H010019".equalsIgnoreCase( item.getKey() )) {
					all.add(item);
				}
				else if( "H010020".equalsIgnoreCase( item.getKey() )) {
					add.add(item);
				}
			}
		}
		result.setAddDataItems(add);
		result.setAllDataItems(all);
		return result;
	}
	
	private static ExchangeCondition parseExchangeCondition( Element root ) throws Exception {
		ExchangeCondition result = null;
		String name = root.getChildren().get(0).getName();
		if( "DATA".equals(name) ) {
			result = new ExchangeCondition();
			List<Element> elementList = root.getChildren().get(0).getChildren();
			for(int i=0; i<elementList.size(); i++) {
				Element element = elementList.get(i);
				if ( "CONDITION".equals(element.getName()) ) {
					Condition condition = new Condition();
					condition.setRel(element.getAttributeValue("rel"));
					List<Item> items = new ArrayList<Item>();
					List<Element> itemList = element.getChildren();
					for(int j=0; j<itemList.size(); j++) {
						Element current = itemList.get(j);
						Item item = new Item();
						item.setKey( current.getAttributeValue("key") );
						item.setEng( convertKeyToEngNameWithAlias( current.getAttributeValue("key") ) );
						item.setVal( current.getAttributeValue("val") );
						if(item.getEng() == null || item.getEng().length() == 0) {
							throw new Exception("unsupport item key:" + item.getKey());
						}
						items.add(item);
					}
					condition.setItems(items);
					result.setCondition(condition);
				} else if ( "DATASET".equals(element.getName()) ) {
					if( "WA_COMMON_010131".equals( element.getAttributeValue("name")) ) {
						
						Common010131 common010131 = parse010131(element);
						result.setCommon010131(common010131);
					}
				} else if("ITEM".equals(element.getName())) {
				    if( "J010002".equals(element.getAttributeValue("key")) ) {
				    	result.setTable( element.getAttributeValue("val") );
				    } else if( "I010018".equals(element.getAttributeValue("key")) ) {
						result.setAsync( "YES".equalsIgnoreCase(element.getAttributeValue("val") ) );
					} else if( "H010005".equals(element.getAttributeValue("key")) ) {
						result.setSyncKey( element.getAttributeValue("val") );
					} else if( "I010017".equals(element.getAttributeValue("key")) ) {
						result.setAllReturnCount( element.getAttributeValue("val") == null || element.getAttributeValue("val").trim().length() == 0 ? 0 :Integer.parseInt(element.getAttributeValue("val")) );
					} else if( "I010019".equals(element.getAttributeValue("key")) ) {
						result.setCurrentReturnCount( element.getAttributeValue("val") == null || element.getAttributeValue("val").trim().length() == 0 ? 0 :Integer.parseInt(element.getAttributeValue("val")) );
					} 
				}
			}
		}
		return result;
	}
	
//	private static AuthCondition parseAuthCondition(Element root) throws Exception {
//		AuthCondition result = null;
//		String name = root.getChildren().get(0).getName();
//		if( "DATA".equals(name) ) {
//			result = new AuthCondition();
//			List<Element> elementList = root.getChildren().get(0).getChildren();
//			for(int i=0; i<elementList.size(); i++) {
//				Element element = elementList.get(i);
//				if ( "CONDITION".equals(element.getName()) && "STC".equalsIgnoreCase( element.getAttributeValue("rel") ) ) {
//					Condition stc = new Condition();
//					stc.setRel("STC");
//					List<Item> items = new ArrayList<Item>();
//					List<Element> itemList = element.getChildren();
//					for(int j=0; j<itemList.size(); j++) {
//						Element current = itemList.get(j);
//						Item item = new Item();
//						item.setKey( current.getAttributeValue("key") );
//						item.setEng( convertKeyToEngNameWithAlias( current.getAttributeValue("key") ) );
//						item.setVal( current.getAttributeValue("val") );
//						if(item.getEng() == null || item.getEng().length() == 0) {
//							throw new Exception("unsupport stc item key:" + item.getKey());
//						}
//						items.add(item);
//					}
//					stc.setItems(items);
//					result.setStc(stc);
//				} else if ( "DATASET".equals(element.getName()) ) {
//					if( "WA_COMMON_010032".equals( element.getAttributeValue("name")) ) {
//						List<Common010032> common010032 = parse010032(element);
//						result.setCommon010032(common010032);
//					}
//				}
//			}
//		}
//		return result;
//	}
	private static AuthCondition parseAuthCondition(Element root) throws Exception {
		AuthCondition result = null;
		//String name = root.getChildren().get(0).getName();
		//if( "DATA".equals(name) ) {
			result = new AuthCondition();
			List<Common010032> common010032s = new ArrayList<Common010032>();
			List<Element> elementList = root.getChildren();
			for(int i=0; i<elementList.size(); i++) {
				Element element = elementList.get(i);
//				if ( "CONDITION".equals(element.getName()) && "STC".equalsIgnoreCase( element.getAttributeValue("rel") ) ) {
//					Condition stc = new Condition();
//					stc.setRel("STC");
//					List<Item> items = new ArrayList<Item>();
//					List<Element> itemList = element.getChildren();
//					for(int j=0; j<itemList.size(); j++) {
//						Element current = itemList.get(j);
//						Item item = new Item();
//						item.setKey( current.getAttributeValue("key") );
//						item.setEng( convertKeyToEngNameWithAlias( current.getAttributeValue("key") ) );
//						item.setVal( current.getAttributeValue("val") );
//						if(item.getEng() == null || item.getEng().length() == 0) {
//							throw new Exception("unsupport stc item key:" + item.getKey());
//						}
//						items.add(item);
//					}
//					stc.setItems(items);
//					result.setStc(stc);
//				} else if ( "DATASET".equals(element.getName()) ) {
					//if( "WA_COMMON_010032".equals( element.getAttributeValue("name")) ) {
						Common010032 common010032 = parse010032(element);
						if( null != common010032 ) {
							common010032s.add(common010032);
						}
					//}
				//}
			}
			result.setCommon010032(common010032s);
		//}
		return result;
	}
	
//<!-- <DATASET name="WA_COMMON_010032" rmk="单个请求条件的对象及数据项"> -->
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
//					<ITEM key="F020004" eng="SRC_IP" val="167842562"/>
//				</CONDITION>
//			</DATA>
//			<DATA>
//				<ITEM key="B020007" eng="IMSI" val=""/>
//				<ITEM key="F020004" eng="SRC_IP" val=""/>
//				<ITEM key="G010002" eng="URL" val=""/>
//			</DATA>
//		</DATASET>
//	</DATA>
//<!-- </DATASET> -->
	private static Common010032 parse010032(Element root) throws Exception {
		//Common010032 result = new Common010032();
		//List<Element> xmlChildren = root.getChildren();
		//for(int i = 0; i < xmlChildren.size(); i++) {
			Common010032 common010032 = new Common010032();
			String name = root.getName();
			if( "DATA".equals(name) ) {
				for(int i = 0; i < root.getChildren().size(); i++) {
					Element rootChild = root.getChildren().get(i);
					if( "ITEM".equalsIgnoreCase(rootChild.getName()) ) {
						if( "H010005".equals(rootChild.getAttributeValue("key")) ) {
							common010032.setSyncKey( rootChild.getAttributeValue("val") );
						}						
					}
					else if( "DATASET".equalsIgnoreCase(rootChild.getName()) ) {
						Element xmlSourceDataSet = rootChild;
						common010032.setSourceName(xmlSourceDataSet.getAttributeValue("name"));
						
						//parse condition
						Element xmlConditionData = xmlSourceDataSet.getChildren().get(0);
						if(xmlConditionData == null || !"DATA".equals(xmlConditionData.getName())) {
							logger.info("parse 'WA_COMMON_010032[remove] --> data --> dataset --> data(0)' error.");
							return null;
						}
						else {
							Element xmlParentCondition = xmlConditionData.getChildren().get(0);
							if(xmlParentCondition == null || !"CONDITION".equals(xmlParentCondition.getName())) {
								logger.info("parse 'WA_COMMON_010032[remove] --> data --> dataset --> data(0) --> CONDITION' error.");
								return null;
							}
							else {
								common010032.setParentCondition(xmlParentCondition.getAttributeValue("rel"));
								List<Element> xmlSubConditions = xmlParentCondition.getChildren();
								List<Condition> conditions = new ArrayList<Condition>();
								List<Item> subItems = new ArrayList<Item>();
								for(int j = 0; j < xmlSubConditions.size(); j++) {
									Condition condition = new Condition();
									Item subItem = new Item();
									Element xmlCurrentCondition = xmlSubConditions.get(j);
									if( "ITEM".equalsIgnoreCase(xmlCurrentCondition.getName()) ) {
										subItem.setKey( xmlCurrentCondition.getAttributeValue("key") );
										subItem.setEng( convertKeyToEngNameWithAlias( xmlCurrentCondition.getAttributeValue("key") ) );
										subItem.setVal( xmlCurrentCondition.getAttributeValue("val") );
										if(subItem.getEng() == null || subItem.getEng().length() == 0) {
											throw new Exception("unsupport search column key:" + subItem.getKey());
										}
										subItems.add(subItem);
									}
									else if( "CONDITION".equalsIgnoreCase(xmlCurrentCondition.getName()) ) {
										condition.setRel(xmlCurrentCondition.getAttributeValue("rel"));
										List<Element> xmlItems = xmlCurrentCondition.getChildren();
										List<Item> items = new ArrayList<Item>();
										for(int k = 0; k < xmlItems.size(); k++) {
											Element xmlCurrentItem = xmlItems.get(k);
											String[] values = xmlCurrentItem.getAttributeValue("val").split(",");
											for(int x = 0; x < values.length; x++) {
												Item item = new Item();
												item.setKey( xmlCurrentItem.getAttributeValue("key") );
												item.setEng( convertKeyToEngNameWithAlias( xmlCurrentItem.getAttributeValue("key") ) );
												item.setVal( values[x] );
												if(item.getEng() == null || item.getEng().length() == 0) {
													throw new Exception("unsupport search column key:" + item.getKey());
												}
												items.add(item);
											}
										}
										condition.setItems(items);
										conditions.add(condition);
									}
									else {
										logger.info("parse 'WA_COMMON_010032[remove] --> data --> dataset --> data(0) --> CONDITION --> CONDITION(" + j + ")' error.");
										continue;
									}
								}
								common010032.setSubConditions(conditions);
								common010032.setSubItems(subItems);
							}
						}
						
						//parse return columns
						Element xmlRetColumnsData = xmlSourceDataSet.getChildren().get(1);
						if(xmlRetColumnsData == null || !"DATA".equals(xmlRetColumnsData.getName()) ) {
							logger.info("parse 'WA_COMMON_010032[remove] --> data --> dataset --> data(1)' error.");
							return null;
						}
						else {
							List<Element> xmlItems = xmlRetColumnsData.getChildren();
							List<Item> items = new ArrayList<Item>();
							for(int j = 0; j < xmlItems.size(); j++) {
								Element xmlCurrentItem = xmlItems.get(j);
								Item item = new Item();
								item.setKey( xmlCurrentItem.getAttributeValue("key") );
								item.setEng( convertKeyToEngNameWithAlias( xmlCurrentItem.getAttributeValue("key") ) );
								item.setVal( xmlCurrentItem.getAttributeValue("val") );
								if(item.getEng() == null || item.getEng().length() == 0) {
									throw new Exception("unsupport search column key:" + item.getKey());
								}
								items.add(item);
							}
							common010032.setItems(items);
						}
						
						
					}
					
				}

			}
			else {
				logger.info("parse 'WA_COMMON_010032[remove] --> data' error.");
				return null;
			}
			//result.add(common010032);
			return common010032;
		//}
		
		//return result;
	}

//<DATASET rmk="要更新的字段信息" name="WA_COMMON_010131">
//    <DATA>
//        <ITEM val="刘霞" key="B010001" chn=""/>
//        <ITEM val="0" key="B010011" chn=""/>
//    </DATA>
//</DATASET>	
	private static Common010131 parse010131(Element root) throws Exception {
		Common010131 result = new Common010131();
		Element child = root.getChildren().get(0);
		String name = child.getName();
		if( "DATA".equals(name) ) {
			List<Element> xmlItems = child.getChildren();
			List<Item> items = new ArrayList<Item>();
			for(int i = 0; i < xmlItems.size(); i++) {
				Element xmlCurrentItem = xmlItems.get(i);
				Item item = new Item();
				item.setKey( xmlCurrentItem.getAttributeValue("key") );
				item.setEng( convertKeyToEngNameWithAlias( xmlCurrentItem.getAttributeValue("key") ) );
				item.setVal( xmlCurrentItem.getAttributeValue("val") );
				if(item.getEng() == null || item.getEng().length() == 0) {
					throw new Exception("unsupport value column key:" + item.getKey());
				}
				items.add(item);
			}
			result.setItems(items);
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
						con.setEng( convertKeyToEngNameWithAlias( condition.getAttributeValue("key") ) );
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
									con.setEng( convertKeyToEngNameWithAlias( condition.getAttributeValue("key") ) );
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
				con.setEng( convertKeyToEngNameWithAlias( condition.getAttributeValue("key") ) );
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
				con.setEng( convertKeyToEngNameWithAlias( condition.getAttributeValue("key") ) );
				con.setVal( convertKeyToEngNameWithAlias( condition.getAttributeValue("dstkey") ) );
				if(con.getEng() == null || con.getEng().length() == 0) {
					throw new Exception("unsupport search column key:" + con.getKey());
				}
				conditions.add(con);
			}
			sc.setCONNECTITEMS(conditions);
		}
		return;
	}
	
	private static ComplexSearchCondition parseComplexSearchCondition(Element element) throws Exception {
		ComplexSearchCondition result = new ComplexSearchCondition();
		Element rootChild = element.getChildren().get(0);
		String name = rootChild.getName();
		if ( !"DATA".equals(name)  ) {
			String loginfo = "[WSQ] format incorrect: <DATASET name=\"WA_COMMON_010117\"> -- <" + name +">";
			logger.error(loginfo);
			throw new Exception(loginfo);
		}
		
		//<DATASET name="WA_COMMON_010117">--<DATA>
		List<Element> itemList = rootChild.getChildren();
		for(int i=0; i<itemList.size(); i++) {
			Element item = itemList.get(i);
			//<DATASET name="WA_COMMON_010117">--<DATA>--<ITEM>
			if("ITEM".equals(item.getName())) {
				String valTmp = item.getAttributeValue("val");
				if( "J010002".equals(item.getAttributeValue("key")) ) {
					result.setTableNameJ010002( valTmp );
				} else if ( "I010017".equals(item.getAttributeValue("key"))  ) {
					result.setSearchResultMaxNumI010017( Integer.parseInt(valTmp) );
				} else if ( "H010005".equals(item.getAttributeValue("key"))  ) {
					result.setSearchIDH010005( valTmp );
				} else if ( "I010019".equals(item.getAttributeValue("key"))  ) {
					result.setSearchResultCountI010019( Integer.parseInt(valTmp) );
				} else if ( "I010018".equals(item.getAttributeValue("key"))  ) {
					result.setSearchAllowAsynI010018( valTmp );
				}
			}
			else if ( "CONDITION".equalsIgnoreCase(item.getName()) ) {
				NestConditions conditions = parseCONDITIONS( item );
				result.setConditions(conditions);
			}
			else if ( "DATASET".equals(item.getName()) ) {
				//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010118">
				if( "WA_COMMON_010118".equals( item.getAttributeValue("name")) ) {
					List<Element> children = item.getChildren();
					if( children.size() > 0 ) {
						String childName = children.get(0).getName();
						if ( !"DATA".equals(childName)  ) {
							String loginfo = "[WSQ] format incorrect: <DATASET name=\"WA_COMMON_010117\"> -- <DATA> -- <DATASET name=\"WA_COMMON_010118\"> -- <" + childName + ">"; 
							logger.error(loginfo);
							throw new Exception(loginfo);
						}

						List<Element> retColList = children.get(0).getChildren();
						List<Item> retCols = new ArrayList<Item>();
						for(int j=0; j<retColList.size(); j++) {
							Element condition = retColList.get(j);
							Item con = new Item();
							con.setKey( condition.getAttributeValue("key") );
							con.setEng( convertKeyToEngNameWithAlias( condition.getAttributeValue("key") ) );
							con.setVal( condition.getAttributeValue("val") );
							if(con.getEng() == null || con.getEng().length() == 0) {
								throw new Exception("[WSQ] unsupport search column key:" + con.getKey());
							}
							retCols.add(con);
						}
						result.setReturnColumns(retCols);
					}
				}
				//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010119">
				else if( "WA_COMMON_010119".equals( item.getAttributeValue("name")) ) {
					List<Element> children = item.getChildren();
					if( children.size() > 0 ) {
						String childName = children.get(0).getName();
						if ( !"DATA".equals(childName)  ) {
							String loginfo = "[WSQ] format incorrect: <DATASET name=\"WA_COMMON_010117\"> -- <DATA> -- <DATASET name=\"WA_COMMON_010119\"> -- <" + childName + ">"; 
							logger.error(loginfo);
							throw new Exception(loginfo);
						}

						List<Element> orderColList = children.get(0).getChildren();
						List<Item> orderCols = new ArrayList<Item>();
						for(int j=0; j<orderColList.size(); j++) {
							Element condition = orderColList.get(j);
							Item con = new Item();
							con.setKey( condition.getAttributeValue("key") );
							con.setEng( convertKeyToEngNameWithAlias( condition.getAttributeValue("key") ) );
							con.setVal( condition.getAttributeValue("val") );
							con.setSort( condition.getAttributeValue("sort") );
							if(con.getEng() == null || con.getEng().length() == 0) {
								throw new Exception("[WSQ] unsupport search column key:" + con.getKey());
							}
							orderCols.add(con);
						}
						result.setOrderColumns(orderCols);
					}
				}
				//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010120">
				else if( "WA_COMMON_010120".equals( item.getAttributeValue("name")) ) {
					List<Element> children = item.getChildren();
					if( children.size() > 0 ) {
						String childName = children.get(0).getName();
						if ( !"DATA".equals(childName)  ) {
							String loginfo = "[WSQ] format incorrect: <DATASET name=\"WA_COMMON_010117\"> -- <DATA> -- <DATASET name=\"WA_COMMON_010120\"> -- <" + childName + ">"; 
							logger.error(loginfo);
							throw new Exception(loginfo);
						}

						List<Element> groupColList = children.get(0).getChildren();
						List<Item> groupCols = new ArrayList<Item>();
						for(int j=0; j<groupColList.size(); j++) {
							Element condition = groupColList.get(j);
							Item con = new Item();
							con.setKey( condition.getAttributeValue("key") );
							con.setEng( convertKeyToEngNameWithAlias( condition.getAttributeValue("key") ) );
							con.setVal( condition.getAttributeValue("val") );
							if(con.getEng() == null || con.getEng().length() == 0) {
								throw new Exception("[WSQ] unsupport search column key:" + con.getKey());
							}
							groupCols.add(con);
						}
						result.setGroupColumns(groupCols);
					}
				}
				//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010123">
				else if ("WA_COMMON_010123".equalsIgnoreCase( item.getAttributeValue("name")) ) {
					List<Element> children = item.getChildren();
					if( children.size() > 0 ) {
						List<Common_010123> common010123s = new ArrayList<Common_010123>();

						for(int o = 0; o < children.size(); o++) {
							String childName = children.get(o).getName();
							//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010123">--<DATA>
							if ( !"DATA".equals(childName)  ) {
								String loginfo = "[WSQ] format incorrect: <DATASET name=\"WA_COMMON_010117\"> -- <DATA> -- <DATASET name=\"WA_COMMON_010123\"> -- <" + childName + ">"; 
								logger.error(loginfo);
								throw new Exception(loginfo);
							}
							List<Element> _010123ItemList = children.get(o).getChildren();
							Common_010123 common010123 = new Common_010123();
							
							for(int p=0; p < _010123ItemList.size(); p++) {	
								Element _010123Item = _010123ItemList.get(p);
								//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010123">--<DATA>--<ITEM>
								if("ITEM".equals(_010123Item.getName())) {
									String valTmp = _010123Item.getAttributeValue("val");
									if( "J010002".equals(_010123Item.getAttributeValue("key")) ) {
										common010123.setTableNameJ010002( valTmp );
									} else if ( "J010007".equals(_010123Item.getAttributeValue("key"))  ) {
										common010123.setJoinTypeJ010007( Integer.parseInt(valTmp) );
									}
								}
								//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010123">--<DATA>--<CONDITION>
								else if ( "CONDITION".equals(_010123Item.getName()) ) {
									Condition joinCondition = new Condition();
									joinCondition.setRel( _010123Item.getAttributeValue("rel") );
									List<Item> joinConditionItems = new ArrayList<Item>();
									List<Element> joinConditionList = _010123Item.getChildren();
									for(int q=0; q<joinConditionList.size(); q++) {
										Element joinConditionItem = joinConditionList.get(q);
										Item con = new Item();
										con.setKey( joinConditionItem.getAttributeValue("key") );
										con.setEng( convertKeyToEngNameWithAlias( joinConditionItem.getAttributeValue("key") ) );
										if( joinConditionItem.getAttributeValue("val") == null || joinConditionItem.getAttributeValue("val").isEmpty() ) {
											con.setVal( convertKeyToEngNameWithAlias(joinConditionItem.getAttributeValue("dstkey")) );
										}
										else {
											String value = joinConditionItem.getAttributeValue("val");
											try{
												Integer.parseInt(value);
											}
											catch(Exception e) {
												//value = "'" + value + "'";
												if(value.contains(".")) {
													value = convertKeyToEngNameWithAlias( value );
												}
											}
											con.setVal( value );
										}
										if(con.getEng() == null || con.getEng().length() == 0) {
											throw new Exception("unsupport search column key: " + con.getKey());
										}
										joinConditionItems.add(con);
									}
									joinCondition.setItems(joinConditionItems);
									common010123.setSearch(joinCondition);
								}
							}
							common010123s.add(common010123);
						}
						result.setCommon010123(common010123s);
					}
				}
				//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010121">
				else if ("WA_COMMON_010121".equalsIgnoreCase( item.getAttributeValue("name")) ) {
					List<Element> children = item.getChildren();
					if( children.size() > 0 ) {
						List<Common_010121> common010121s = new ArrayList<Common_010121>();
						
						for(int o = 0; o < children.size(); o++) {
							String childName = children.get(o).getName();
							//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010121">--<DATA>
							if ( !"DATA".equals(childName)  ) {
								String loginfo = "[WSQ] format incorrect: <DATASET name=\"WA_COMMON_010117\"> -- <DATA> -- <DATASET name=\"WA_COMMON_010121\"> -- <" + childName + ">"; 
								logger.error(loginfo);
								throw new Exception(loginfo);
							}
							
							List<Element> _010121ItemList = children.get(o).getChildren();
							Common_010121 common010121 = new Common_010121();
								
							for(int p=0; p < _010121ItemList.size(); p++) {
								Element _010121Item = _010121ItemList.get(p);
								//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010121">--<DATA>--<ITEM>
								if("ITEM".equals(_010121Item.getName())) {
									String varTmp = _010121Item.getAttributeValue("val");
									if( "J010002".equals(_010121Item.getAttributeValue("key")) ) {
										common010121.setTableNameJ010002( varTmp );
									} 
									else if ( "J010015".equals(_010121Item.getAttributeValue("key"))  ) {
										common010121.setTableAliasJ010015( varTmp );
									}
								}
								//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010121">--<DATA>--<CONDITION>
								else if ( "CONDITION".equals(_010121Item.getName()) ) {
								
//									Conditions conditions = new Conditions();
//									List<Condition> subConditions = new ArrayList<Condition>();
//									Condition condition = new Condition();
//									List<Item> conditionItems = new ArrayList<Item>();
//									condition.setRel( _010121Item.getAttributeValue("rel") );
//									List<Element> conditionList = _010121Item.getChildren();
//									
//									for(int q = 0; q < conditionList.size(); q++) {
//										if( "CONDITION".equalsIgnoreCase(conditionList.get(q).getName()) ) {
//											Condition con = new Condition();
//											Element conTag = conditionList.get(q);
//											con.setRel( conTag.getAttributeValue("rel") );
//											
//											List<Item> items = new ArrayList<Item>();
//											List<Element> conTagChildren = conTag.getChildren();
//											for(int k = 0; k < conTagChildren.size(); k++) {
//												Element itemTag = conTagChildren.get(k);
//												Item innerItem = new Item();
//												innerItem.setKey( itemTag.getAttributeValue("key") );
//												innerItem.setEng( convertKeyToEngNameWithAlias( itemTag.getAttributeValue("key") ) );
//												innerItem.setVal( itemTag.getAttributeValue("val") );
//												if(innerItem.getEng() == null || innerItem.getEng().length() == 0) {
//													throw new Exception("unsupport search column key:" + innerItem.getKey());
//												}
//												items.add(innerItem);
//											}
//											if( items.size() > 0 ) {
//												con.setItems(items);
//											}
//											subConditions.add(con);
//										}
//										else if ( "ITEM".equalsIgnoreCase(conditionList.get(q).getName()) ) {
//											Item innerItem = new Item();
//											innerItem.setEng( convertKeyToEngNameWithAlias( conditionList.get(q).getAttributeValue("key") ) );
//											innerItem.setVal( conditionList.get(q).getAttributeValue("val") );
//											if(innerItem.getEng() == null || innerItem.getEng().length() == 0) {
//												throw new Exception("unsupport search column key:" + innerItem.getKey());
//											}
//											conditionItems.add(innerItem);
//										}
//									}
//									
//									if( conditionItems.size() > 0 ) {
//										condition.setItems(conditionItems);
//									}
//									conditions.setCondition(condition);
//
//									if( subConditions.size() > 0 ) {
//										conditions.setConditions(subConditions);
//									}
									NestConditions conditions = parseCONDITIONS( _010121Item );
									common010121.setConditions(conditions);
								}
								else if( "DATASET".equals(_010121Item.getName()) ) {
									//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010121">--<DATA>--<DATASET name="WA_COMMON_010118">
									if( "WA_COMMON_010118".equals( _010121Item.getAttributeValue("name")) ) {
										List<Element> _010121_010118_children = _010121Item.getChildren();
										if( _010121_010118_children.size() > 0 ) {
											String _010121_010118_childrenFirstItemName = _010121_010118_children.get(0).getName();
											
											if ( !"DATA".equals(_010121_010118_childrenFirstItemName)  ) {
												String loginfo = "[WSQ] format incorrect: <DATASET name=\"WA_COMMON_010117\"> -- <DATA> -- <DATASET name=\"WA_COMMON_010121\"> -- <DATA> -- <DATASET name=\"WA_COMMON_010118\"> -- <" + _010121_010118_childrenFirstItemName + ">"; 
												logger.error(loginfo);
												throw new Exception(loginfo);
											}
											
											
											List<Element> retColList = _010121_010118_children.get(0).getChildren();
											List<Item> retCols = new ArrayList<Item>();
											for(int j=0; j<retColList.size(); j++) {
												
												
												Element condition = retColList.get(j);
												Item con = new Item();
												con.setKey( condition.getAttributeValue("key") );
												con.setEng( convertKeyToEngNameWithAlias( condition.getAttributeValue("key") ) );
												con.setVal( condition.getAttributeValue("val") );
												if(con.getEng() == null || con.getEng().length() == 0) {
													throw new Exception("[WSQ] unsupport search column key:" + con.getKey());
												}
												retCols.add(con);
											}
											common010121.setReturnColumns(retCols);
										}
									}// end of [010121] WA_COMMON_010118
									//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010121">--<DATA>--<DATASET name="WA_COMMON_010123">
									else if ("WA_COMMON_010123".equalsIgnoreCase( _010121Item.getAttributeValue("name")) ) {
										List<Element> _010121_010123_children = _010121Item.getChildren();
										if( _010121_010123_children.size() > 0 ) {
											List<Common_010123> common010123s = new ArrayList<Common_010123>();

											for(int x = 0; x < _010121_010123_children.size(); x++) {
												String _010121_010123_childrenName = _010121_010123_children.get(x).getName();
												//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010121">--<DATA>--<DATASET name="WA_COMMON_010123">--<DATA>
												if ( !"DATA".equals(_010121_010123_childrenName)  ) {
													String loginfo = "[WSQ] format incorrect: <DATASET name=\"WA_COMMON_010117\"> -- <DATA> -- <DATASET name=\"WA_COMMON_010121\"> -- <DATA> -- <DATASET name=\"WA_COMMON_010123\"> -- <" + _010121_010123_childrenName + ">"; 
													logger.error(loginfo);
													throw new Exception(loginfo);
												}
												List<Element> _010123ItemList = _010121_010123_children.get(x).getChildren();
												Common_010123 common010123 = new Common_010123();
												
												for(int y=0; y < _010123ItemList.size(); y++) {	
													Element _010123Item = _010123ItemList.get(y);
													//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010121">--<DATA>--<DATASET name="WA_COMMON_010123">--<DATA>--<ITEM>
													if("ITEM".equals(_010123Item.getName())) {
														String valTmp = _010123Item.getAttributeValue("val");
														if( "J010002".equals(_010123Item.getAttributeValue("key")) ) {
															common010123.setTableNameJ010002( valTmp );
														} else if ( "J010007".equals(_010123Item.getAttributeValue("key"))  ) {
															common010123.setJoinTypeJ010007( Integer.parseInt(valTmp) );
														}
													}
													//<DATASET name="WA_COMMON_010117">--<DATA>--<DATASET name="WA_COMMON_010121">--<DATA>--<DATASET name="WA_COMMON_010123">--<DATA>--<CONDITION>
													else if ( "CONDITION".equals(_010123Item.getName()) ) {
														Condition joinCondition = new Condition();
														joinCondition.setRel( _010123Item.getAttributeValue("rel") );
														List<Item> joinConditionItems = new ArrayList<Item>();
														List<Element> joinConditionList = _010123Item.getChildren();
														for(int z=0; z<joinConditionList.size(); z++) {
															Element joinConditionItem = joinConditionList.get(z);
															Item con = new Item();
															con.setKey( joinConditionItem.getAttributeValue("key") );
															con.setEng( convertKeyToEngNameWithAlias( joinConditionItem.getAttributeValue("key") ) );
															if( joinConditionItem.getAttributeValue("val") == null || joinConditionItem.getAttributeValue("val").isEmpty() ) {
																con.setVal( convertKeyToEngNameWithAlias(joinConditionItem.getAttributeValue("dstkey")) );
															}
															else {
																String value = joinConditionItem.getAttributeValue("val");
																try{
																	Integer.parseInt(value);
																}
																catch(Exception e) {
																	//value = "'" + value + "'";
																	if( value.contains(".") ) {
																		value = convertKeyToEngNameWithAlias( value );
																	}
																}
																con.setVal( value );
															}
															if(con.getEng() == null || con.getEng().length() == 0) {
																throw new Exception("unsupport search column key: " + con.getKey());
															}
															joinConditionItems.add(con);
														}
														joinCondition.setItems(joinConditionItems);
														common010123.setSearch(joinCondition);
													}
												}
												common010123s.add(common010123);
											}
											common010121.setCommon010123(common010123s);
										}
									}
								}// end of [010121] DATASET
							}
							common010121s.add(common010121);
						}
						result.setCommon010121(common010121s);
					}
				}// end of WA_COMMON_010121
			}// end of DATASET
		}// end of for loop
		return result;
	}
	
	
	//<DATASET name="WA_COMMON_010117">--<DATA>--<CONDITION>
	//
	//  <CONDITION rel="AND">
	//		<CONDITION rel="NOT">
	//			<CONDITION rel="IN">
	//				<ITEM key="WA_AUTHORITY_DATA_RESOURCE.A010004" eng="" val="VOIP,SMS,MBLOG,MMS"/>
	//			</CONDITION>
	//		</CONDITION>
	//		<ITEM key="WA_AUTHORITY_DATA_RESOURCE.J030010" eng="" val="1"/>
	//		<ITEM key="WA_AUTHORITY_DATA_RESOURCE.H010029" eng="" val="0"/>
	//	</CONDITION>
	//
	private static NestConditions parseCONDITIONS(Element element) throws Exception {
		NestConditions conditions = new NestConditions();
		List<NestConditions> subConditions = new ArrayList<NestConditions>();
		Condition condition = new Condition();
		List<Item> conditionItems = new ArrayList<Item>();
		condition.setRel( element.getAttributeValue("rel") );
		
		List<Element> conditionList = element.getChildren();
		for(int j = 0; j < conditionList.size(); j++) {
			Element conditionListItem = conditionList.get(j);
			if( "CONDITION".equalsIgnoreCase(conditionListItem.getName()) ) {
				NestConditions tmp = parseCONDITIONS( conditionListItem );
				subConditions.add(tmp);
			}
			else if ( "ITEM".equalsIgnoreCase(conditionListItem.getName()) ) {
				Item innerItem = new Item();
				innerItem.setEng( convertKeyToEngNameWithAlias( conditionListItem.getAttributeValue("key") ) );
				innerItem.setVal( conditionListItem.getAttributeValue("val") );
				innerItem.setKey(conditionListItem.getAttributeValue("key"));
				if(innerItem.getEng() == null || innerItem.getEng().length() == 0) {
					throw new Exception("unsupport search column key:" + innerItem.getKey());
				}
				conditionItems.add(innerItem);
			}
		}
		
		if( conditionItems.size() > 0 ) {
			condition.setItems(conditionItems);
		}
		conditions.setCondition(condition);
		
		if( subConditions.size() > 0 ) {
			conditions.setSubConditions(subConditions);
		}
		return conditions;
	}
	
//	private static SearchCondition parseSearchCondition(Element element) throws Exception {
//		SearchCondition result = new SearchCondition();
//		List<Element> rootChildren = element.getChildren();
//		for(int k = 0; k<rootChildren.size(); k++) {
//			String name = rootChildren.get(k).getName();
//			if( "DATA".equals(name) ) {
//				List<Element> itemList = rootChildren.get(k).getChildren();
//				for(int i=0; i<itemList.size(); i++) {
//					Element item = itemList.get(i);
//					if("ITEM".equals(item.getName())) {
//						if( "J010002".equals(item.getAttributeValue("key")) ) {
//							result.setTableName( item.getAttributeValue("val") );
//							result.setTableAlias( item.getAttributeValue("alias") );
//						} else if ( "I010017".equals(item.getAttributeValue("key"))  ) {
//							result.setTotalNum( item.getAttributeValue("val") );
//						} else if ( "H010005".equals(item.getAttributeValue("key"))  ) {
//							result.setSearchId( item.getAttributeValue("val") );
//						} else if ( "I010019".equals(item.getAttributeValue("key"))  ) {
//							result.setOnceNum( item.getAttributeValue("val") );
//						} else if ( "I010018".equals(item.getAttributeValue("key"))  ) {
//							result.setIsAsyn( item.getAttributeValue("val") );
//						}
//					} else if ( "CONDITION".equals(item.getName()) ) {
//						result.setCONDITION( item.getAttributeValue("rel") );
//						List<Item> conditions = new ArrayList<Item>();
//						List<Element> conditionList = item.getChildren();
//						for(int j=0; j<conditionList.size(); j++) {
//							Element condition = conditionList.get(j);
//							if ( "CONDITION".equals(condition.getName()) ) {
//								//TODO deal with time condition like:
//								//  <CONDITION rel="BTW">
//								//  <ITEM key="WA_AUTHORITY_POLICE.I010005" eng="" val="0"/>
//								//  <ITEM key="WA_AUTHORITY_POLICE.I010005" eng="" val="1459132363"/>
//								//  </CONDITION>
//							} else {
//								Item con = new Item();
//								con.setEng( convertKeyToEngNameWithAlias( condition.getAttributeValue("key") ) );
//								con.setVal( condition.getAttributeValue("val") );
//								if(con.getEng() == null || con.getEng().length() == 0) {
//									throw new Exception("unsupport search column key:" + con.getKey());
//								}
//								conditions.add(con);
//							}
//						}
//						result.setCONDITIONITEMS(conditions);
//					} else if ( "DATASET".equals(item.getName()) ) {
//						if( "WA_COMMON_010118".equals( item.getAttributeValue("name")) ) {
//							List<Element> children = item.getChildren();
//							if( children.size() > 0 ) {
//								String childName = children.get(0).getName();
//								if( "DATA".equals(childName) ) {
//									List<Element> retColList = children.get(0).getChildren();
//									List<Item> retCols = new ArrayList<Item>();
//									for(int j=0; j<retColList.size(); j++) {
//										
//										
//										Element condition = retColList.get(j);
//										Item con = new Item();
//										con.setKey( condition.getAttributeValue("key") );
//										con.setEng( convertKeyToEngNameWithAlias( condition.getAttributeValue("key") ) );
//										con.setVal( condition.getAttributeValue("val") );
//										if(con.getEng() == null || con.getEng().length() == 0) {
//											throw new Exception("unsupport search column key:" + con.getKey());
//										}
//										retCols.add(con);
//									}
//									result.setRETURNITEMS(retCols);
//								}
//							}
//						}
//						else if ("WA_COMMON_010123".equalsIgnoreCase( item.getAttributeValue("name")) ) {
//							List<Element> children = item.getChildren();
//							if( children.size() > 0 ) {
//								List<Common010123> common010123s = new ArrayList<Common010123>();
//								for(int o = 0; o < children.size(); o++) {
//									String childName = children.get(o).getName();
//									if( "DATA".equals(childName) ) {
//										List<Element> _010123ItemList = children.get(o).getChildren();
//										Common010123 common010123 = new Common010123();
//										
//										for(int p=0; p < _010123ItemList.size(); p++) {
//											
//											Element _010123Item = _010123ItemList.get(p);
//											if("ITEM".equals(_010123Item.getName())) {
//												if( "J010002".equals(_010123Item.getAttributeValue("key")) ) {
//													common010123.setTable( _010123Item.getAttributeValue("val") );
//													common010123.setAlias( _010123Item.getAttributeValue("alias") );
//												} else if ( "J010007".equals(_010123Item.getAttributeValue("key"))  ) {
//													common010123.setJoin( Integer.parseInt(_010123Item.getAttributeValue("val")) );
//												}
//											} else if ( "CONDITION".equals(_010123Item.getName()) ) {
//												Condition joinCondition = new Condition();
//												joinCondition.setRel( _010123Item.getAttributeValue("rel") );
//												List<Item> joinConditionItems = new ArrayList<Item>();
//												List<Element> joinConditionList = _010123Item.getChildren();
//												for(int q=0; q<joinConditionList.size(); q++) {
//													Element joinConditionItem = joinConditionList.get(q);
//													Item con = new Item();
//													con.setKey( joinConditionItem.getAttributeValue("key") );
//													con.setEng( convertKeyToEngNameWithAlias( joinConditionItem.getAttributeValue("key") ) );
//													if( joinConditionItem.getAttributeValue("val") == null || joinConditionItem.getAttributeValue("val").isEmpty() ) {
//														con.setVal( convertKeyToEngNameWithAlias(joinConditionItem.getAttributeValue("dstkey")) );
//													}
//													else {
//														String value = joinConditionItem.getAttributeValue("val");
//														try{
//															Integer.parseInt(value);
//														}
//														catch(Exception e) {
//															//value = "'" + value + "'";
//														}
//														con.setVal( value );
//													}
//													if(con.getEng() == null || con.getEng().length() == 0) {
//														throw new Exception("unsupport search column key: " + con.getKey());
//													}
//													joinConditionItems.add(con);
//												}
//												joinCondition.setItems(joinConditionItems);
//												common010123.setSearch(joinCondition);
//											}
//										}
//										common010123s.add(common010123);
//									}
//								}
//								result.setCommon010123(common010123s);
//							}
//						}
//						else if ("WA_COMMON_010121".equalsIgnoreCase( item.getAttributeValue("name")) ) {
//							List<Element> children = item.getChildren();
//							if( children.size() > 0 ) {
//								List<Common010121> common010121s = new ArrayList<Common010121>();
//								for(int o = 0; o < children.size(); o++) {
//									String childName = children.get(o).getName();
//									if( "DATA".equals(childName) ) {
//										List<Element> _010121ItemList = children.get(o).getChildren();
//										Common010121 common010121 = new Common010121();
//										
//										for(int p=0; p < _010121ItemList.size(); p++) {
//											
//											Element _010121Item = _010121ItemList.get(p);
//											if("ITEM".equals(_010121Item.getName())) {
//												if( "J010002".equals(_010121Item.getAttributeValue("key")) ) {
//													common010121.setTable( _010121Item.getAttributeValue("val") );
//												} else if ( "I010018".equals(_010121Item.getAttributeValue("key"))  ) {
//													common010121.setIsAsyn(_010121Item.getAttributeValue("val") );
//												} else if ( "J010015".equals(_010121Item.getAttributeValue("key"))  ) {
//													common010121.setAlias( _010121Item.getAttributeValue("val") );
//												}
//											} 
//											else if ( "CONDITION".equals(_010121Item.getName()) ) {
//												Condition searchCondition = new Condition();
//												searchCondition.setRel( _010121Item.getAttributeValue("rel") );
//												List<Item> searchConditionItems = new ArrayList<Item>();
//												List<Element> searchConditionList = _010121Item.getChildren();
//												for(int q=0; q<searchConditionList.size(); q++) {
//													Element searchConditionItem = searchConditionList.get(q);
//													Item con = new Item();
//													con.setKey( searchConditionItem.getAttributeValue("key") );
//													con.setEng( convertKeyToEngNameWithAlias( searchConditionItem.getAttributeValue("key") ) );
//													con.setVal( searchConditionItem.getAttributeValue("val") );
//													
//													if(con.getEng() == null || con.getEng().length() == 0) {
//														throw new Exception("unsupport search column key: " + con.getKey());
//													}
//													searchConditionItems.add(con);
//												}
//												searchCondition.setItems(searchConditionItems);
//												common010121.setSearch(searchCondition);
//											}
//											else if( "DATASET".equals(_010121Item.getName()) ) {
//												if( "WA_COMMON_010118".equals( _010121Item.getAttributeValue("name")) ) {
//													List<Element> _010121_010118_children = _010121Item.getChildren();
//													if( _010121_010118_children.size() > 0 ) {
//														String _010121_010118_childrenFirstItemName = _010121_010118_children.get(0).getName();
//														if( "DATA".equals(_010121_010118_childrenFirstItemName) ) {
//															List<Element> retColList = _010121_010118_children.get(0).getChildren();
//															List<Item> retCols = new ArrayList<Item>();
//															for(int j=0; j<retColList.size(); j++) {
//																
//																
//																Element condition = retColList.get(j);
//																Item con = new Item();
//																con.setKey( condition.getAttributeValue("key") );
//																con.setEng( convertKeyToEngNameWithAlias( condition.getAttributeValue("key") ) );
//																con.setVal( condition.getAttributeValue("val") );
//																if(con.getEng() == null || con.getEng().length() == 0) {
//																	throw new Exception("unsupport search column key:" + con.getKey());
//																}
//																retCols.add(con);
//															}
//															common010121.setRETURNITEMS(retCols);
//														}
//													}
//												}// end of [010121] WA_COMMON_010118
//											}
//										}
//										common010121s.add(common010121);
//									}
//								}
//								result.setCommon010121(common010121s);
//							}
//						}
//					}
//				}
//			}
//			else if ( "DATASET".equals(name)  ) {
//				String datasetName = rootChildren.get(k).getAttributeValue("name"); //获得name属性
//				if("WA_COMMON_010143".equals(datasetName) ) {
//					parse010143(rootChildren.get(k), result);
//					result.setCONNECTTYPE(SearchCondition.CONNECT_TYPE_010117);
//				}
//				
//			}
//		}
//		
//		return result;
//	}

//	protected static String convertKeyToEngName(String key) {
//		Map<String, String> keyColumnMap = new HashMap<String, String>();
//		
//		keyColumnMap.put("A010001", "GA_DEPARTMENT");
//		keyColumnMap.put("A010004", "DATA_SET");
//		keyColumnMap.put("A010005", "PARENT_ORG");
//		
//		keyColumnMap.put("J010019", "COLUMN_NAME");
//		
//		keyColumnMap.put("J020012", "SYSTEM_TYPE");
//		keyColumnMap.put("J020013", "APP_ID");
//		keyColumnMap.put("J030001", "SECTION_CLASS");
//		keyColumnMap.put("J030002", "SECTION_RELATIOIN_CLASS");
//		keyColumnMap.put("J030003", "DATASET_SENSITIVE_LEVEL");
//		keyColumnMap.put("J030004", "ELEMENT");
//		keyColumnMap.put("J030005", "ELEMENT_VALUE");
//		keyColumnMap.put("J030006", "RESOURCE_ID");
//		keyColumnMap.put("J030007", "RESOUCE_NAME");
//		keyColumnMap.put("J030008", "PARENT_RESOURCE");
//		keyColumnMap.put("J030009", "RESOURCE_ICON_PATH");
//		keyColumnMap.put("J030010", "RESOURCE_STATUS");
//		keyColumnMap.put("J030011", "RESOURCE_ORDER");
//		keyColumnMap.put("J030012", "RESOURCE_DESCRIBE");
//		keyColumnMap.put("J030014", "CERTIFICATE_CODE_MD5");
//		keyColumnMap.put("J030016", "USER_STATUS");
//		keyColumnMap.put("J030018", "DATASET_SENSITIVE_NAME");
//		keyColumnMap.put("J030013", "RMK");
//		keyColumnMap.put("J030019", "DATASET_NAME");
//		keyColumnMap.put("J030020", "CLASSIFY_NAME");
//		keyColumnMap.put("J030022", "COLUMU_CN");
//		keyColumnMap.put("J030023", "VALUE_SENSITIVE_ID");
//		keyColumnMap.put("J030024", "VALUE_SENSITIVE_NAME");
//		keyColumnMap.put("J030025", "ID");
//		keyColumnMap.put("J030026", "VALUE_NAME");
//		keyColumnMap.put("J030027", "DST_CLASS_CODE");
//		keyColumnMap.put("J030028", "SRC_CLASS_CODE");
//		keyColumnMap.put("J030029", "RESOURCE_TYPE");
//		keyColumnMap.put("J030035", "FUN_RESOURCE_TYPE");
//		keyColumnMap.put("J030036", "RESOURCE_CLASS");
//		
//		
//		keyColumnMap.put("I010025", "BUSINESS_ROLE_TYPE");
//		keyColumnMap.put("I010026", "BUSINESS_ROLE");
//		keyColumnMap.put("I010054", "BUSINESS_ROLE_NAME");
//		keyColumnMap.put("I030003", "BUSINESS_STATUS");
//		
//		keyColumnMap.put("H010005", "SEARCH_ID");
//		keyColumnMap.put("H010034", "SENSITIVE_LEVEL");
//		
//		keyColumnMap.put("B050016", "SYSTEM_TYPE");
//		keyColumnMap.put("B030005", "CERTIFICATE_CODE");
//		
//		
//		keyColumnMap.put("G010002", "URL");
///////////////////////////////////////////////////////////////////////////////		
//		keyColumnMap.put("220040B", "AUTH_ACCOUNT");
//		keyColumnMap.put("B010001", "NAME");
//		keyColumnMap.put("B010011", "SEXCODE");
//		keyColumnMap.put("B020001", "ISP_ID");
//		keyColumnMap.put("B020005", "BUYPHONE");
//		keyColumnMap.put("B020007", "IMSI");
//		keyColumnMap.put("B030001", "COUNTRY_TYPE");
//		keyColumnMap.put("B030002", "AREA_CODE");
//		keyColumnMap.put("B030004", "CERTIFICATE_TYPE");
//		keyColumnMap.put("B030020", "LANGUAGES");
//		keyColumnMap.put("B030021", "CHINESE_LANGUAGES");
//		keyColumnMap.put("B040002", "USERNAME");
//		keyColumnMap.put("B040003", "ACCOUNT_ID");
//		keyColumnMap.put("B040005", "PASSWORD");
//		keyColumnMap.put("B040014", "PASSWORD_HASH_STRING");
//		keyColumnMap.put("B040021", "AUTH_TYPE");
//		keyColumnMap.put("B040022", "AUTH_ACCOUNT");
//		
//		keyColumnMap.put("C020011", "TERMINAL_MODEL");
//		keyColumnMap.put("C020005", "TERMINAL_OS_VERSION");
//		keyColumnMap.put("C020006", "BROWSE_TYPE");
//		keyColumnMap.put("C020007", "BROWSE_VERSION");
//		keyColumnMap.put("C020009", "TERMINAL_OS_TYPE");
//		keyColumnMap.put("C020017", "TERMINAL_TYPE");
//		keyColumnMap.put("C020021", "SOFTWARE_NAME");
//		keyColumnMap.put("C020022", "SOFTWARE_VERSION");
//		keyColumnMap.put("C040002", "MAC");
//		keyColumnMap.put("C040003", "HARDWARE_SIGNATURE");
//		keyColumnMap.put("C040004", "HARDWARESTRING_TYPE");
//		keyColumnMap.put("C050001", "EQUIPMENT_ID");
//		keyColumnMap.put("C110008", "COOKIE");
//		
//		keyColumnMap.put("E010002", "UNIT");
//		
//		keyColumnMap.put("F010001", "LONGITUDE");
//		keyColumnMap.put("F010002", "LATITUDE");
//		keyColumnMap.put("F010008", "COLLECT_PLACE");
//		keyColumnMap.put("F010009", "ABOVE_SEALEVEL");
//		keyColumnMap.put("F020001", "CLINET_IP");
//		keyColumnMap.put("F020004", "SRC_IP");
//		keyColumnMap.put("F020005", "DST_IP");
//		keyColumnMap.put("F020006", "SRC_PORT");
//		keyColumnMap.put("F020007", "DST_PORT");
//		keyColumnMap.put("F020010", "SRC_IPV6");
//		keyColumnMap.put("F020011", "DST_IPV6");
//		keyColumnMap.put("F020012", "SRC_PORT_V6");
//		keyColumnMap.put("F020013", "DST_PORT_V6");
//		keyColumnMap.put("F020016", "SRC_IP_AREA");
//		keyColumnMap.put("F020017", "DST_IP_AREA");
//		keyColumnMap.put("F030002", "BASE_STATION_ID");
//		keyColumnMap.put("F030013", "UP_TEID");
//		keyColumnMap.put("F030014", "DOWN_TEID");
//		keyColumnMap.put("F040007", "ROOM_ID");
//		
//		keyColumnMap.put("G010003", "TopDomain");
//		keyColumnMap.put("G010004", "CONTENT_TYPE");
//		keyColumnMap.put("G010002", "URL");
//		keyColumnMap.put("G010005", "REFERER");
//		
//		keyColumnMap.put("G020004", "SERVICECODE");
//		keyColumnMap.put("G020013", "SECURITY_SOFTWARE_ORGCODE");
//		keyColumnMap.put("G020014", "COMPANY_NAME");
//		keyColumnMap.put("G020036", "ICP_PROVIDER");
//		
//		keyColumnMap.put("H010001", "VOICE_TYPE");
//		keyColumnMap.put("H010002", "APP_TYPE");
//		keyColumnMap.put("H010006", "CLUE_SRC_SYS");
//		keyColumnMap.put("H010007", "CLUE_DST_SYS");
//		keyColumnMap.put("H010013", "SESSIONID");
//		keyColumnMap.put("H010014", "CAPTURE_TIME");
//		keyColumnMap.put("H010018", "DEVICE_ID");
//		keyColumnMap.put("H010019", "MAINFILE");
//		keyColumnMap.put("H010020", "OTHER_FILE");
//		keyColumnMap.put("H010021", "FILESIZE");
//		keyColumnMap.put("H010028", "FILE_MD5");
//		keyColumnMap.put("H010029", "DELETE_STATUS");
//		keyColumnMap.put("H010031", "PRXOY_TOOLE_TYPE");
//		keyColumnMap.put("H010032", "PROXY_ADDRESS");
//		keyColumnMap.put("H010033", "PROXY_PROVIDER");
//		keyColumnMap.put("H010035", "OWNERSHIP_LAND");
//		keyColumnMap.put("H010036", "INTERNET_LAND");
//		keyColumnMap.put("H010037", "INTERRUPT_FILE");
//		keyColumnMap.put("H010038", "OTHER_FILE_MD5");
//		keyColumnMap.put("H010041", "APPLICATION_LAYER_PROTOCOL");
//		keyColumnMap.put("H010042", "RECORD_ID");
//		keyColumnMap.put("H020002", "TITLE");
//		keyColumnMap.put("H040002", "LOCAL_ACTION");
//		keyColumnMap.put("H070003", "TOOLTYPE");
//		
//		keyColumnMap.put("I010009", "CONTEXT");
//		
//		if(key.contains(".")) {
//			key = key.substring(key.indexOf('.')+1);
//		}
//		
//		return keyColumnMap.get(key);
//	}
	
	protected static String convertKeyToEngNameWithAlias(String key) {
		Map<String, String> keyColumnMap = new HashMap<String, String>();
		
		keyColumnMap.put("A010001", "GA_DEPARTMENT");
		keyColumnMap.put("A010003", "GA_DEPARTMENT");
		keyColumnMap.put("A010004", "DATA_SET");
		keyColumnMap.put("A010005", "PARENT_ORG");
		
		keyColumnMap.put("J010019", "COLUMN_NAME");
		
		keyColumnMap.put("J020012", "SYSTEM_TYPE");
		keyColumnMap.put("J020013", "APP_ID");
		keyColumnMap.put("J030001", "SECTION_CLASS");
		keyColumnMap.put("J030002", "SECTION_RELATIOIN_CLASS");
		keyColumnMap.put("J030003", "DATASET_SENSITIVE_LEVEL");
		keyColumnMap.put("J030004", "ELEMENT");
		keyColumnMap.put("J030005", "ELEMENT_VALUE");
		keyColumnMap.put("J030006", "RESOURCE_ID");
		keyColumnMap.put("J030007", "RESOUCE_NAME");
		keyColumnMap.put("J030008", "PARENT_RESOURCE");
		keyColumnMap.put("J030009", "RESOURCE_ICON_PATH");
		keyColumnMap.put("J030010", "RESOURCE_STATUS");
		keyColumnMap.put("J030011", "RESOURCE_ORDER");
		keyColumnMap.put("J030012", "RESOURCE_DESCRIBE");
		keyColumnMap.put("J030014", "CERTIFICATE_CODE_MD5");
		keyColumnMap.put("J030016", "USER_STATUS");
		keyColumnMap.put("J030018", "DATASET_SENSITIVE_NAME");
		keyColumnMap.put("J030013", "RMK");
		keyColumnMap.put("J030019", "DATASET_NAME");
		keyColumnMap.put("J030020", "CLASSIFY_NAME");
		keyColumnMap.put("J030021", "OPERATE_SYMBOL");
		keyColumnMap.put("J030022", "COLUMU_CN");
		keyColumnMap.put("J030023", "VALUE_SENSITIVE_ID");
		keyColumnMap.put("J030024", "VALUE_SENSITIVE_NAME");
		keyColumnMap.put("J030025", "ID");
		keyColumnMap.put("J030026", "VALUE_NAME");
		keyColumnMap.put("J030027", "DST_CLASS_CODE");
		keyColumnMap.put("J030028", "SRC_CLASS_CODE");
		keyColumnMap.put("J030029", "RESOURCE_TYPE");
		keyColumnMap.put("J030035", "FUN_RESOURCE_TYPE");
		keyColumnMap.put("J030036", "RESOURCE_CLASS");
		
		
		keyColumnMap.put("I010025", "BUSINESS_ROLE_TYPE");
		keyColumnMap.put("I010026", "BUSINESS_ROLE");
		keyColumnMap.put("I010054", "BUSINESS_ROLE_NAME");
		keyColumnMap.put("I030003", "BUSINESS_STATUS");
		
		keyColumnMap.put("H010005", "SEARCH_ID");
		keyColumnMap.put("H010034", "SENSITIVE_LEVEL");
		
		keyColumnMap.put("B050016", "SYSTEM_TYPE");
		keyColumnMap.put("B030005", "CERTIFICATE_CODE");
		
		
		keyColumnMap.put("G010002", "URL");
/////////////////////////////////////////////////////////////////////////////		
		keyColumnMap.put("220040B", "AUTH_ACCOUNT");
		keyColumnMap.put("B010001", "NAME");
		keyColumnMap.put("B010011", "SEXCODE");
		keyColumnMap.put("B010028", "POLICE_NO");
		keyColumnMap.put("B020001", "ISP_ID");
		keyColumnMap.put("B020005", "BUYPHONE");
		keyColumnMap.put("B020007", "IMSI");
		keyColumnMap.put("B030001", "COUNTRY_TYPE");
		keyColumnMap.put("B030002", "AREA_CODE");
		keyColumnMap.put("B030004", "CERTIFICATE_TYPE");
		keyColumnMap.put("B030020", "LANGUAGES");
		keyColumnMap.put("B030021", "CHINESE_LANGUAGES");
		keyColumnMap.put("B040002", "USERNAME");
		keyColumnMap.put("B040003", "ACCOUNT_ID");
		keyColumnMap.put("B040005", "PASSWORD");
		keyColumnMap.put("B040014", "PASSWORD_HASH_STRING");
		keyColumnMap.put("B040021", "AUTH_TYPE");
		keyColumnMap.put("B040022", "AUTH_ACCOUNT");
		
		keyColumnMap.put("C020011", "TERMINAL_MODEL");
		keyColumnMap.put("C020005", "TERMINAL_OS_VERSION");
		keyColumnMap.put("C020006", "BROWSE_TYPE");
		keyColumnMap.put("C020007", "BROWSE_VERSION");
		keyColumnMap.put("C020009", "TERMINAL_OS_TYPE");
		keyColumnMap.put("C020017", "TERMINAL_TYPE");
		keyColumnMap.put("C020021", "SOFTWARE_NAME");
		keyColumnMap.put("C020022", "SOFTWARE_VERSION");
		keyColumnMap.put("C040002", "MAC");
		keyColumnMap.put("C040003", "HARDWARE_SIGNATURE");
		keyColumnMap.put("C040004", "HARDWARESTRING_TYPE");
		keyColumnMap.put("C050001", "EQUIPMENT_ID");
		keyColumnMap.put("C110008", "COOKIE");
		
		keyColumnMap.put("E010002", "UNIT");
		
		keyColumnMap.put("F010001", "LONGITUDE");
		keyColumnMap.put("F010002", "LATITUDE");
		keyColumnMap.put("F010008", "COLLECT_PLACE");
		keyColumnMap.put("F010009", "ABOVE_SEALEVEL");
		keyColumnMap.put("F020001", "CLINET_IP");
		keyColumnMap.put("F020004", "SRC_IP");
		keyColumnMap.put("F020005", "DST_IP");
		keyColumnMap.put("F020006", "SRC_PORT");
		keyColumnMap.put("F020007", "DST_PORT");
		keyColumnMap.put("F020010", "SRC_IPV6");
		keyColumnMap.put("F020011", "DST_IPV6");
		keyColumnMap.put("F020012", "SRC_PORT_V6");
		keyColumnMap.put("F020013", "DST_PORT_V6");
		keyColumnMap.put("F020016", "SRC_IP_AREA");
		keyColumnMap.put("F020017", "DST_IP_AREA");
		keyColumnMap.put("F030002", "BASE_STATION_ID");
		keyColumnMap.put("F030013", "UP_TEID");
		keyColumnMap.put("F030014", "DOWN_TEID");
		keyColumnMap.put("F040007", "ROOM_ID");
		
		keyColumnMap.put("G010003", "TopDomain");
		keyColumnMap.put("G010004", "CONTENT_TYPE");
		keyColumnMap.put("G010002", "URL");
		keyColumnMap.put("G010005", "REFERER");
		
		keyColumnMap.put("G020004", "SERVICECODE");
		keyColumnMap.put("G020013", "SECURITY_SOFTWARE_ORGCODE");
		keyColumnMap.put("G020014", "COMPANY_NAME");
		keyColumnMap.put("G020036", "ICP_PROVIDER");
		
		keyColumnMap.put("H010001", "VOICE_TYPE");
		keyColumnMap.put("H010002", "APP_TYPE");
		keyColumnMap.put("H010006", "CLUE_SRC_SYS");
		keyColumnMap.put("H010007", "CLUE_DST_SYS");
		keyColumnMap.put("H010013", "SESSIONID");
		keyColumnMap.put("H010014", "CAPTURE_TIME");
		keyColumnMap.put("H010018", "DEVICE_ID");
		keyColumnMap.put("H010019", "MAINFILE");
		keyColumnMap.put("H010020", "OTHER_FILE");
		keyColumnMap.put("H010021", "FILESIZE");
		keyColumnMap.put("H010028", "FILE_MD5");
		keyColumnMap.put("H010029", "DELETE_STATUS");
		keyColumnMap.put("H010031", "PRXOY_TOOLE_TYPE");
		keyColumnMap.put("H010032", "PROXY_ADDRESS");
		keyColumnMap.put("H010033", "PROXY_PROVIDER");
		keyColumnMap.put("H010035", "OWNERSHIP_LAND");
		keyColumnMap.put("H010036", "INTERNET_LAND");
		keyColumnMap.put("H010037", "INTERRUPT_FILE");
		keyColumnMap.put("H010038", "OTHER_FILE_MD5");
		keyColumnMap.put("H010041", "APPLICATION_LAYER_PROTOCOL");
		keyColumnMap.put("H010042", "RECORD_ID");
		keyColumnMap.put("H020002", "TITLE");
		keyColumnMap.put("H040002", "LOCAL_ACTION");
		keyColumnMap.put("H070003", "TOOLTYPE");
		
		
		keyColumnMap.put("I010005", "LATEST_MOD_TIME");
		keyColumnMap.put("I010009", "CONTEXT");
		keyColumnMap.put("I010031", "BUSINESS_TYPE");
		
		String result = "";
		if(key.contains(".")) {
			String[] aliasAndKey = key.split("\\.");
			if( aliasAndKey.length == 2 ) {
				result = aliasAndKey[0] + "." + keyColumnMap.get(aliasAndKey[1]);
			}
			else {
				logger.info("unknow column with alias format.[" + key + "]");
			}
		}
		else {
			result = keyColumnMap.get(key);
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
					} else if ( "J030014".equals(item.getAttributeValue("key"))  ) {
						result.setCERTIFICATE_CODE_MD5( item.getAttributeValue( "val") );
					} else if ( "I010026".equals(item.getAttributeValue("key"))  ) {
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

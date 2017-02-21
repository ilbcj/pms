package com.pms.webservice.service.dataexchange;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pms.dao.ResourceDAO;
import com.pms.dao.impl.ResourceDAOImpl;
import com.pms.model.ResFeature;
import com.pms.util.DateTimeUtil;
import com.pms.webservice.dao.ExchangeDAO;
import com.pms.webservice.dao.impl.ExchangeDAOImpl;
import com.pms.webservice.model.Item;
import com.pms.webservice.model.exchange.ExchangeCondition;

public class DataexchangeOfResFeatureService extends DataexchangeService {

	private Log logger = LogFactory.getLog(DataexchangeOfResFeatureService.class);
	
	protected DataexchangeOfResFeatureService(ExchangeCondition ec) {
		super(ec);
	}
	
	public void ExecuteUpdate() throws Exception {
		if( ec == null || ec.getCommon010131() == null || ec.getCommon010131().getItems() == null || ec.getCommon010131().getItems().size() == 0 ) {
			String message = "[DESIFE]更新功能资源数据操作的条件不完整.";
			logger.info(message);
			throw new Exception(message);
		}
		
		if( ec.getCondition() == null || ec.getCondition().getItems() == null || ec.getCondition().getItems().size() == 0 ) {
			ResFeature feature = new ResFeature();
			String importData = "";
			for(int i = 0; i < ec.getCommon010131().getItems().size(); i++) {
				Item item = ec.getCommon010131().getItems().get(i);
				if ( "J020012".equalsIgnoreCase(item.getKey()) ) {
					feature.setSYSTEM_TYPE(item.getVal());
					importData += "J020012[system_type:" + item.getVal() + "]; ";
				}
				else if ( "J030037".equalsIgnoreCase(item.getKey()) ) {
					feature.setBUSINESS_FUNCTION_ID(item.getVal());
					importData += "J030037[business_function_id:" + item.getVal() + "]; ";
				}
				else if ( "J020013".equalsIgnoreCase(item.getKey()) ) {
					feature.setAPP_ID(item.getVal());
					importData += "J020013[app_id:" + item.getVal() + "]; ";
				}
				else if ( "J030007".equalsIgnoreCase(item.getKey()) ) {
					feature.setRESOUCE_NAME(item.getVal());
					importData += "J030007[resource_name:" + item.getVal() + "]; ";
				}
				else if ( "J030038".equalsIgnoreCase(item.getKey()) ) {
					feature.setBUSINESS_FUNCTION_PARENT_ID(item.getVal());
					importData += "J030038[business_function_parent_id:" + item.getVal() + "]; ";
				}
				else if ( "G010002".equalsIgnoreCase(item.getKey()) ) {
					feature.setURL(item.getVal());
					importData += "G010002[url:" + item.getVal() + "]; ";
				}
				else if ( "J030009".equalsIgnoreCase(item.getKey()) ) {
					feature.setRESOURCE_ICON_PATH(item.getVal());
					importData += "J030009[resource_icon_path:" + item.getVal() + "]; ";
				}
				else if ( "J030035".equalsIgnoreCase(item.getKey()) ) {
					feature.setFUN_RESOURCE_TYPE(Integer.parseInt("".equals(item.getVal())?"0":item.getVal()));
					importData += "J030035[resource_type:" + item.getVal() + "]; ";
				}
				else if ( "J030010".equalsIgnoreCase(item.getKey()) ) {
					feature.setRESOURCE_STATUS(Integer.parseInt("".equals(item.getVal())?"0":item.getVal()));
					importData += "J030010[resource_status:" + item.getVal() + "]; ";
				}
				else if ( "J030011".equalsIgnoreCase(item.getKey()) ) {
					feature.setRESOURCE_ORDER(item.getVal());
					importData += "J030011[resource_order:" + item.getVal() + "]; ";
				}
				else if ( "J030012".equalsIgnoreCase(item.getKey()) ) {
					feature.setRESOURCE_DESCRIBE(item.getVal());
					importData += "J030012[resource_describe:" + item.getVal() + "]; ";
				}
				else if ( "J030013".equalsIgnoreCase(item.getKey()) ) {
					feature.setRMK(item.getVal());
					importData += "J030013[resource_remark:" + item.getVal() + "]; ";
				}
			}
			
			// add H010029\J030017\I010005
			String timenow = DateTimeUtil.GetCurrentTime();
			
			feature.setDELETE_STATUS(ResFeature.DELSTATUSNO);
			feature.setDATA_VERSION(1);
			feature.setLATEST_MOD_TIME(timenow);
			
			if(feature.isValid()) {
				ResourceDAO dao = new ResourceDAOImpl();
        		dao.FeatureAdd(feature);
        		logger.info("[DESIFE]appserver insert function resource finish.[importdata:" + importData + "]");
        	}
			else {
				String message = "[DESIFE]function resource data invalid.[importdata:" + importData + "]";
				logger.info(message);
				throw new Exception(message);
			}
		}
		else {			
			if("IN".equalsIgnoreCase(ec.getCondition().getRel())) {
				throw new Exception("can not deal with \"IN\" condition in update data process's condition");
			}
			String search = "select * from wa_authority_func_resource where ";
			for(int j = 0; j < ec.getCondition().getItems().size(); j++) {
				Item item = ec.getCondition().getItems().get(j);
				search += item.getEng() + "='" + item.getVal() + "' " + ec.getCondition().getRel() + " ";
			}
			search = search.substring(0, search.length() - ec.getCondition().getRel().length() - 1);
			
			ExchangeDAO dao = new ExchangeDAOImpl();
			List<ResFeature> features = dao.SearchResFeatureData(search);
			if(features == null || features.size() == 0){
				String message = "[DESIFE]no function resource has been found when update function resource info.[sql:" + search + "]";
				logger.info(message);
				throw new Exception(message);
			}
			else {
				for(ResFeature feature : features) {
					String update = "update wa_authority_func_resource set ";
					for(int i = 0; i < ec.getCommon010131().getItems().size(); i++) {
						Item item = ec.getCommon010131().getItems().get(i);
						if ("J020012".equalsIgnoreCase(item.getKey()) || "J030037".equalsIgnoreCase(item.getKey()) 
								|| "J020013".equalsIgnoreCase(item.getKey()) || "J030007".equalsIgnoreCase(item.getKey())
								|| "J030038".equalsIgnoreCase(item.getKey()) || "G010002".equalsIgnoreCase(item.getKey())
								|| "J030009".equalsIgnoreCase(item.getKey()) || "J030035".equalsIgnoreCase(item.getKey())
								|| "J030010".equalsIgnoreCase(item.getKey()) || "J030011".equalsIgnoreCase(item.getKey())
								|| "J030012".equalsIgnoreCase(item.getKey()) || "J030013".equalsIgnoreCase(item.getKey()) 
								|| "H010029".equalsIgnoreCase(item.getKey()) ) {
							update += item.getEng() + "='" + item.getVal() + "', ";
						}
					}
					update += " data_version = " + (feature.getDATA_VERSION() + 1) + ", " ;
					update += " latest_mod_time = '" + DateTimeUtil.GetCurrentTime() + "'";
					update += " where id = " + feature.getId() + " ";
					logger.info("[DESIFE]update function resource info.[sql:" + update + "]");
					int rs = dao.SqlExchangeData(update);
					logger.info("[DESIFE]update function resource info finish.[sql:" + update + "; count:" + rs + "]");
				}
			}
		}
	}

}

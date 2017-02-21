package com.pms.webservice.service.dataexchange;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pms.dao.ResourceDAO;
import com.pms.dao.impl.ResourceDAOImpl;
import com.pms.model.ResRole;
import com.pms.util.ConfigHelper;
import com.pms.util.DateTimeUtil;
import com.pms.webservice.dao.ExchangeDAO;
import com.pms.webservice.dao.impl.ExchangeDAOImpl;
import com.pms.webservice.model.Item;
import com.pms.webservice.model.exchange.ExchangeCondition;

public class DataexchangeOfRoleService extends DataexchangeService {

	private Log logger = LogFactory.getLog(DataexchangeOfRoleService.class);
	
	protected DataexchangeOfRoleService(ExchangeCondition ec) {
		super(ec);
	}
	
	public void ExecuteUpdate() throws Exception {
		if( ec == null || ec.getCommon010131() == null || ec.getCommon010131().getItems() == null || ec.getCommon010131().getItems().size() == 0 ) {
			String message = "[DESIRO]更新角色数据操作的条件不完整.";
			logger.info(message);
			throw new Exception(message);
		}
		
		if( ec.getCondition() == null || ec.getCondition().getItems() == null || ec.getCondition().getItems().size() == 0 ) {
			ResRole role = new ResRole();
			String importData = "";
			for(int i = 0; i < ec.getCommon010131().getItems().size(); i++) {
				Item item = ec.getCommon010131().getItems().get(i);
				if ("I010026".equalsIgnoreCase(item.getKey()) ) {
					role.setBUSINESS_ROLE(item.getVal());
					importData += "I010026[BUSINESS_ROLE:" + item.getVal() + "]; ";
				}
				else if ( "I010054".equalsIgnoreCase(item.getKey()) ) {
					role.setBUSINESS_ROLE_NAME(item.getVal());
					importData += "I010054[BUSINESS _ROLE_NAME:" + item.getVal() + "]; ";
				}
				else if ( "B050016".equalsIgnoreCase(item.getKey()) ) {
					role.setSYSTEM_TYPE(item.getVal());
					importData += "B050016[SYSTEM_TYPE:" + item.getVal() + "]; ";
				}
			}
			
			// add I010025\H010006\H010029\J030017\I010005
			String timenow = DateTimeUtil.GetCurrentTime();
			role.setBUSINESS_ROLE_TYPE(ResRole.RESROLETYPELOCAL);
			role.setCLUE_SRC_SYS(ConfigHelper.getInstance().getRegion());
			role.setDELETE_STATUS(ResRole.DELSTATUSNO);
			role.setDATA_VERSION(1);
			role.setLATEST_MOD_TIME(timenow);
			
			if(role.getBUSINESS_ROLE_NAME() == null || role.getBUSINESS_ROLE_NAME().length() == 0 
					|| role.getBUSINESS_ROLE() == null || role.getBUSINESS_ROLE().length() == 0) {
				String message = "[DESIRO]local role data invalid.[importdata:" + importData + "]";
				logger.info(message);
				throw new Exception(message);
        	}
			else {
				ResourceDAO dao = new ResourceDAOImpl();
				List<ResRole> pmsRole = dao.GetRoleById(role.getBUSINESS_ROLE());
            	if( pmsRole != null && pmsRole.size() > 0) {
            		String message = "[DESIRO]local role already exist.[importdata:" + importData + "]";
            		logger.info(message);
            		throw new Exception(message);
            	}
        		dao.RoleAdd(role);
        		logger.info("[DESIRO]appserver insert local role finish.[importdata:" + importData + "]");
			}
			
		}
		else {			
			if("IN".equalsIgnoreCase(ec.getCondition().getRel())) {
				throw new Exception("can not deal with \"IN\" condition in update data process's condition");
			}
			String search = "select * from wa_authority_role where ";
			for(int j = 0; j < ec.getCondition().getItems().size(); j++) {
				Item item = ec.getCondition().getItems().get(j);
				search += item.getEng() + "='" + item.getVal() + "' " + ec.getCondition().getRel() + " ";
			}
			search = search.substring(0, search.length() - ec.getCondition().getRel().length() - 1);
			
			ExchangeDAO dao = new ExchangeDAOImpl();
			List<ResRole> roles = dao.SearchRoleData(search);
			if(roles == null || roles.size() == 0){
				String message = "[DESIRO]no role has been found when update role info.[sql:" + search + "]";
				logger.info(message);
				throw new Exception(message);
			}
			else {
				for(ResRole role : roles) {
					String update = "update wa_authority_role set ";
					for(int i = 0; i < ec.getCommon010131().getItems().size(); i++) {
						Item item = ec.getCommon010131().getItems().get(i);
						if( "I010026".equalsIgnoreCase(item.getKey()) ) {
							throw new Exception("[DESIRO] business_role of role cann't update.");
						}
						if ("I010054".equalsIgnoreCase(item.getKey()) 
								|| "B050016".equalsIgnoreCase(item.getKey()) || "H010029".equalsIgnoreCase(item.getKey())) {
							update += item.getEng() + "='" + item.getVal() + "', ";
						}
					}
					update += " data_version = " + (role.getDATA_VERSION() + 1) + ", " ;
					update += " latest_mod_time = '" + DateTimeUtil.GetCurrentTime() + "'";
					update += " where id = " + role.getId() + " ";
					logger.info("[DESIRO]update role info.[sql:" + update + "]");
					int rs = dao.SqlExchangeData(update);
					logger.info("[DESIRO]update role info finish.[sql:" + update + "; count:" + rs + "]");
				}
			}
		}
	}

}

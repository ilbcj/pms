package com.pms.webservice.service.dataexchange;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pms.model.User;
import com.pms.util.DateTimeUtil;
import com.pms.webservice.dao.ExchangeDAO;
import com.pms.webservice.dao.impl.ExchangeDAOImpl;
import com.pms.webservice.model.Item;
import com.pms.webservice.model.exchange.ExchangeCondition;

public class DataexchangeOfUserService extends DataexchangeService {

	private Log logger = LogFactory.getLog(DataexchangeOfUserService.class);
	
	protected DataexchangeOfUserService(ExchangeCondition ec) {
		super(ec);
	}
	
	public void ExecuteUpdate() throws Exception {
		if( ec == null || ec.getCommon010131() == null || ec.getCommon010131().getItems() == null || ec.getCommon010131().getItems().size() == 0 ) {
			logger.info("[DESIU]更新用户数据操作的条件不完整.");
			return;
		}
		
		if( ec.getCondition() == null || ec.getCondition().getItems() == null || ec.getCondition().getItems().size() == 0 ) {
			//insert into `pms`.`systemdata` ('item', 'value', 'rmk') values ( 'item','value','rmk');
			String tablename = ec.getTable();
			//String sqlString = "insert into " + ec.getTable() + " ( ";
			String columns = " ( ";
			String values = " values ( ";
			for(int i = 0; i < ec.getCommon010131().getItems().size(); i++) {
				Item item = ec.getCommon010131().getItems().get(i);
				if("J030037".equalsIgnoreCase(item.getKey()) ){
					if("1".equals(item.getVal()) ) {
						tablename += "_private";
					}
				} else if (!"H010029".equalsIgnoreCase(item.getKey())) {
					columns += item.getEng() + ", ";
					values += "'" + item.getVal() + "', ";
				}
			}
			
			// add H010029\J030017\I010005
			String timenow = DateTimeUtil.GetCurrentTime();
			columns += "DELETE_STATUS, DATA_VERSION, LATEST_MOD_TIME ) ";
			values += "0, 1, '" + timenow + "' ) ";
			
			String sqlString = "insert into " + tablename + " ";
			sqlString += columns + values;
			logger.info("[DESIU]remote pms node required insert user.[" + sqlString + "]");
		}
		else {			
			if("IN".equalsIgnoreCase(ec.getCondition().getRel())) {
			throw new Exception("can not deal with \"IN\" condition in update data process's condition");
			}
			String search = "select * from wa_authority_police where ";
			for(int j = 0; j < ec.getCondition().getItems().size(); j++) {
				Item item = ec.getCondition().getItems().get(j);
				search += item.getEng() + "='" + item.getVal() + "' " + ec.getCondition().getRel() + " ";
			}
			search = search.substring(0, search.length() - ec.getCondition().getRel().length() - 1);
			
			ExchangeDAO dao = new ExchangeDAOImpl();
			List<User> users = dao.SearchUserData(search);
			if(users == null){
				logger.info("[DESIU]no user has been found when update user info.[sql:" + search + "]");
			}
			else {
				for(User user : users) {
					String update = "update wa_authority_police set ";
					for(int i = 0; i < ec.getCommon010131().getItems().size(); i++) {
						Item item = ec.getCommon010131().getItems().get(i);
						update += item.getEng() + "='" + item.getVal() + "', ";
					}
					update += " data_version = " + (user.getDATA_VERSION() + 1) + ", " ;
					update += " latest_mod_time = '" + DateTimeUtil.GetCurrentTime() + "'";
					update += " where id = " + user.getId() + " ";
					logger.info("[DESIU]update user info.[sql:" + update + "]");
					int rs = dao.SqlExchangeData(update);
					logger.info("[DESIU]update user info finish.[sql:" + update + "; count:" + rs + "]");
				}
			}
		}
	}

}

package com.pms.webservice.service.dataexchange;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pms.util.DateTimeUtil;
import com.pms.webservice.dao.ExchangeDAO;
import com.pms.webservice.dao.impl.ExchangeDAOImpl;
import com.pms.webservice.model.Item;
import com.pms.webservice.model.exchange.ExchangeCondition;

public class DataexchangeService {
	private static Log logger = LogFactory.getLog(DataexchangeService.class);
	
	protected ExchangeCondition ec;
	protected DataexchangeService(ExchangeCondition ec)
	{
		this.ec = ec;
	}
	
	public static DataexchangeService getInstance(ExchangeCondition ec) {
		DataexchangeService des = null;
		if( ec != null && ec.getTable() != null && ec.getTable().length() > 0 ) {
			String tablename = ec.getTable();
			if(tablename.equalsIgnoreCase("wa_authority_police")) {
				des = new DataexchangeOfUserService(ec);
			}
			else if(tablename.equalsIgnoreCase("wa_authority_role")) {
				des = new DataexchangeOfRoleService(ec);
			}
			else if(tablename.equalsIgnoreCase("wa_authority_func_resource")) {
				des = new DataexchangeOfResFeatureService(ec);
			}
			else if(tablename.equalsIgnoreCase("wa_authority_resource_role")) {
				des = new DataexchangeOfResAndRoleService(ec);
			}
			else if(tablename.equalsIgnoreCase("WA_DATASET")) {
				//des = new ANewClass();
			}
			else {
				// default update operation;
				des = new DataexchangeService(ec);
			}
		}
		else {
			des = new DataexchangeService(ec);
		}
		return des;
	}

	public void ExecuteUpdate() throws Exception {
		if( ec == null || ec.getCommon010131() == null || ec.getCommon010131().getItems() == null || ec.getCommon010131().getItems().size() == 0 ) {
			logger.info("[DESIS]更新数据操作的条件不完整.");
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
			System.out.println(sqlString);
			
			ExchangeDAO dao = new ExchangeDAOImpl();
			int rs = dao.SqlExchangeData(sqlString);
			System.out.println(rs);
		}
		else {
		
			if("IN".equalsIgnoreCase(ec.getCondition().getRel())) {
				throw new Exception("can not deal with \"IN\" condition in update data process's condition");
			}
			
			String sqlString = "update " + ec.getTable() + " set ";
			for(int i = 0; i < ec.getCommon010131().getItems().size(); i++) {
				Item item = ec.getCommon010131().getItems().get(i);
				sqlString += item.getEng() + "='" + item.getVal() + "', ";
			}
			
			sqlString = sqlString.substring(0, sqlString.length() - 2);
			
			sqlString += " where " ;
			for(int j = 0; j < ec.getCondition().getItems().size(); j++) {
				Item item = ec.getCondition().getItems().get(j);
				sqlString += item.getEng() + "='" + item.getVal() + "' " + ec.getCondition().getRel() + " ";
			}
			
			sqlString = sqlString.substring(0, sqlString.length() - ec.getCondition().getRel().length() - 1);
			System.out.println(sqlString);
			
			ExchangeDAO dao = new ExchangeDAOImpl();
			int rs = dao.SqlExchangeData(sqlString);
			System.out.println(rs);
		}
	}
	
	
}

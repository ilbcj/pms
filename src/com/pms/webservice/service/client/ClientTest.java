package com.pms.webservice.service.client;

import com.pms.util.ConfigHelper;

public class ClientTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			String address = "http://15.6.22.2:7879/prjServiceEntry_dplServiceEntry/services/ServiceEntry/";
			address = ConfigHelper.getInstance().getEsbAddr();
			String rid = "Q11000000000000000";
			rid = ConfigHelper.getInstance().getRequestId();
			
			SendSyncMessage ssm = new SendSyncMessage(address, rid);
			String sid = "S01000011000000009";
			String message = "<name>test user</name>";
			
			String result = ssm.SendMessage(sid, message);
			System.out.println(result);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}

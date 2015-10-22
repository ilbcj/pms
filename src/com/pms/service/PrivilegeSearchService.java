package com.pms.service;

import java.util.ArrayList;
import java.util.List;

import com.pms.dao.GroupDAO;
import com.pms.dao.PrivilegeDAO;
import com.pms.dao.impl.GroupDAOImpl;
import com.pms.dao.impl.PrivilegeDAOImpl;
import com.pms.dto.UserPrivListItem;
import com.pms.model.GroupUser;
import com.pms.model.ResRole;

public class PrivilegeSearchService {
	
	public List<UserPrivListItem> QueryUserPrivileges(String userid) throws Exception {
		List<UserPrivListItem> res = new ArrayList<UserPrivListItem>();
		//query user priv
		QueryPrivsByUserid(userid, res);
		
		//query org priv
		QueryPrivsByOrg(userid, res);
				
		//query usergroup priv
		QueryPrivsByGroup(userid, res);
		
		return res;
	}
	
	private void QueryPrivsByUserid(String userid, List<UserPrivListItem> privItems) throws Exception
	{
		PrivilegeDAO dao = new PrivilegeDAOImpl();
		List<ResRole> res = dao.QueryPrivInfosByUserid(userid);
		
		UserPrivListItem item = null;
		for(int i = 0; i<res.size(); i++) {
			item = existItem(privItems, res.get(i).getId());
			if( item != null ) {
				List<String> source = item.getSource();
				if(source == null) {
					source = new ArrayList<String>();
				}
				source.add(UserPrivListItem.SOURCETYPEUSER);
				item.setSource(source);
			}
			else {
				item = new UserPrivListItem();
				item.setRoleid(res.get(i).getId());
				item.setRolename(res.get(i).getBUSINESS_ROLE_NAME());
				item.setRolecode(res.get(i).getBUSINESS_ROLE());
				List<String> source = new ArrayList<String>();
				source.add(UserPrivListItem.SOURCETYPEUSER);
				item.setSource(source);
				privItems.add(item);
			}
		}
		return;
	}
	
	private void QueryPrivsByOrg(String userid, List<UserPrivListItem> privItems) throws Exception
	{
		PrivilegeDAO dao = new PrivilegeDAOImpl();
		List<ResRole> res = dao.QueryPrivInfosByUsersOrg(userid);
		
		UserPrivListItem item = null;
		for(int i = 0; i<res.size(); i++) {
			item = existItem(privItems, res.get(i).getId());
			if( item != null ) {
				List<String> source = item.getSource();
				if(source == null) {
					source = new ArrayList<String>();
				}
				source.add(UserPrivListItem.SOURCETYPEORG);
				item.setSource(source);
			}
			else {
				item = new UserPrivListItem();
				item.setRoleid(res.get(i).getId());
				item.setRolename(res.get(i).getBUSINESS_ROLE_NAME());
				item.setRolecode(res.get(i).getBUSINESS_ROLE());
				List<String> source = new ArrayList<String>();
				source.add(UserPrivListItem.SOURCETYPEORG);
				item.setSource(source);
				privItems.add(item);
			}
		}
		return;
	}
	
	private void QueryPrivsByGroup(String userid, List<UserPrivListItem> privItems) throws Exception
	{
		GroupDAO gdao = new GroupDAOImpl();
		List<GroupUser> groups = gdao.GetGroupsByUserId(userid);
		
		PrivilegeDAO pdao = new PrivilegeDAOImpl();
		
		for(int i = 0; i<groups.size(); i++) {
			
			List<ResRole> res = pdao.QueryPrivInfosByUsersGroup( groups.get(i).getGroupid() );

			UserPrivListItem item = null;
			for(int j = 0; j<res.size(); j++) {
				item = existItem(privItems, res.get(j).getId());
				if( item != null ) {
					List<String> source = item.getSource();
					if(source == null) {
						source = new ArrayList<String>();
					}
					source.add(UserPrivListItem.SOURCETYPEGROUP);
					item.setSource(source);
				}
				else {
					item = new UserPrivListItem();
					item.setRoleid(res.get(j).getId());
					item.setRolename(res.get(j).getBUSINESS_ROLE_NAME());
					item.setRolecode(res.get(j).getBUSINESS_ROLE());
					List<String> source = new ArrayList<String>();
					source.add(UserPrivListItem.SOURCETYPEGROUP);
					item.setSource(source);
					privItems.add(item);
				}
			}
		}
		
		return;
	}

	private UserPrivListItem existItem(List<UserPrivListItem> privItems, int id) {
		for(int i=0; i<privItems.size(); i++) {
			if( privItems.get(i).getRoleid() == id) {
				return privItems.get(i);
			}
		}
		return null;
	}
}

package com.pms.webservice.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pms.model.HibernateUtil;
import com.pms.model.Organization;
import com.pms.model.ResData;
import com.pms.model.ResFeature;
import com.pms.model.ResRole;
import com.pms.model.ResRoleResource;
import com.pms.model.User;
import com.pms.model.UserRole;
import com.pms.webservice.dao.SearchDAO;

public class SearchDAOImpl implements SearchDAO {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List SqlQueryAllCols(String sql, int type, int first, int count) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<User> rs = null;
		//String sqlString = "select * from WA_AUTHORITY_POLICE where GA_DEPARTMENT = :GA_DEPARTMENT ";
		
		try {
			Query q = session.createSQLQuery(sql);
			if(type == TYPEUSER) {
				q = ((SQLQuery)q).addEntity(User.class);
			}
			else if(type == TYPEORG) {
				q = ((SQLQuery)q).addEntity(Organization.class);
			}
			else if(type == TYPEROLE) {
				q = ((SQLQuery)q).addEntity(ResRole.class);
			}
			else if(type == TYPERESDATA) {
				q = ((SQLQuery)q).addEntity(ResData.class);
			}
			else if(type == TYPERESFUN) {
				q = ((SQLQuery)q).addEntity(ResFeature.class);
			}
			else if(type == TYPEUSER_ROLE) {
				q = ((SQLQuery)q).addEntity(UserRole.class);
			}
			else if(type == TYPEROLE_RESOURCE) {
				q = ((SQLQuery)q).addEntity(ResRoleResource.class);
			}
			if(count != 0)
			{
				q.setFirstResult(first);
				q.setMaxResults(count);
			}
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

}

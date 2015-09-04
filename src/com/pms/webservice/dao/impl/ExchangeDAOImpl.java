package com.pms.webservice.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pms.model.HibernateUtil;
import com.pms.webservice.dao.ExchangeDAO;

public class ExchangeDAOImpl implements ExchangeDAO {


	@Override
	public int SqlExchangeData(String sql) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs = 0;
		
		try {
			Query q = session.createSQLQuery(sql);
			rs = q.executeUpdate();
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

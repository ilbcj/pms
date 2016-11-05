package com.pms.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResValueSensitiveDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResValueSensitive;
import com.pms.util.DateTimeUtil;

public class ResValueSensitiveDAOImpl implements ResValueSensitiveDAO {

	@Override
	public ResValueSensitive ResValueSensitiveSave(ResValueSensitive vs)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResValueSensitive rs = null;
		String sqlString = "select * from WA_VALUE_SENSITIVE where VALUE_SENSITIVE_ID = :VALUE_SENSITIVE_ID ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResValueSensitive.class);
			q.setString("VALUE_SENSITIVE_ID", vs.getVALUE_SENSITIVE_ID());
			rs = (ResValueSensitive) q.uniqueResult();
			
			if(rs != null) {
				vs.setId(rs.getId());
				vs.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				vs.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			vs.setLATEST_MOD_TIME(timenow);
			
			vs = (ResValueSensitive) session.merge(vs);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名字段值敏感度。");
		}
		catch(org.hibernate.exception.SQLGrammarException e)
		{
			tx.rollback();
			System.out.println(e.getSQLException().getMessage());
			throw e.getSQLException();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return vs;
	}

}

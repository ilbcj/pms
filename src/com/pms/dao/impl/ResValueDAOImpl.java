package com.pms.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResValueDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResValue;

public class ResValueDAOImpl implements ResValueDAO {

	@Override
	public ResValue ResValueSave(ResValue val)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResValue rs = null;
		String sqlString = "select * from WA_VALUE where ELEMENT_VALUE = :ELEMENT_VALUE ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResValue.class);
			q.setString("ELEMENT_VALUE", val.getELEMENT_VALUE());
			rs = (ResValue) q.uniqueResult();
			
			if(rs != null) {
				val.setId(rs.getId());
				val.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				val.setDATA_VERSION( 1 );
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
			val.setLATEST_MOD_TIME(timenow);
			
			val = (ResValue) session.merge(val);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名字段值。");
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
		return val;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResValue> QueryRowResValue(String dataSet, String element)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
				 
		List<ResValue> rs = null;
		String sqlString = "SELECT DISTINCT a.* FROM WA_VALUE a, WA_ROW_RELATION b WHERE a.ELEMENT_VALUE = b.ELEMENT_VALUE AND a.ELEMENT=b.ELEMENT AND b.ELEMENT =:ELEMENT AND b.DATA_SET =:DATA_SET ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResValue.class);
			q.setString("DATA_SET", dataSet);
			q.setString("ELEMENT", element);
			rs = q.list();
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
}

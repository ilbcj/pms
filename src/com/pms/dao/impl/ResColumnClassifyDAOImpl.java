package com.pms.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResColumnClassifyDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResColumnClassify;
import com.pms.util.DateTimeUtil;

public class ResColumnClassifyDAOImpl implements ResColumnClassifyDAO {

	@Override
	public ResColumnClassify ResColumnClassifySave(ResColumnClassify cc)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResColumnClassify rs = null;
		String sqlString = "select * from WA_COLUMN_CLASSIFY where SECTION_CLASS = :SECTION_CLASS ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResColumnClassify.class);
			q.setString("SECTION_CLASS", cc.getSECTION_CLASS());
			rs = (ResColumnClassify) q.uniqueResult();
			
			if(rs != null) {
				cc.setId(rs.getId());
				cc.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				cc.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			cc.setLATEST_MOD_TIME(timenow);
			
			cc = (ResColumnClassify) session.merge(cc);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名字段分类。");
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
		return cc;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResColumnClassify> QueryColumnClassify(String dataSet)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
				 
		List<ResColumnClassify> rs = null;
		String sqlString = "SELECT * FROM WA_COLUMN_CLASSIFY WHERE SECTION_CLASS IN (SELECT DISTINCT SECTION_CLASS FROM WA_COLUMN_RELATION WHERE DATA_SET = :DATA_SET ) ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResColumnClassify.class);
			q.setString("DATA_SET", dataSet);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<ResColumnClassify> QueryAllColumnClassify() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResColumnClassify> rs = null;
		String sqlString = "select * from WA_COLUMN_CLASSIFY ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResColumnClassify.class);
			rs = q.list();
			
			tx.commit();
		} catch(org.hibernate.exception.SQLGrammarException e) {
			tx.rollback();
			System.out.println(e.getSQLException().getMessage());
			throw e.getSQLException();
		} catch(Exception e) {
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

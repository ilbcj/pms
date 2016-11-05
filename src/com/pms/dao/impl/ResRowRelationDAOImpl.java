package com.pms.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResRowRelationDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResRelationRow;
import com.pms.model.ResRelationRowPrivate;
import com.pms.util.DateTimeUtil;

public class ResRowRelationDAOImpl implements ResRowRelationDAO {

	@Override
	public ResRelationRow ResRelationRowSave(ResRelationRow rr)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResRelationRow rs = null;
		String sqlString = "select * from WA_ROW_RELATION where DATA_SET = :DATA_SET and ELEMENT = :ELEMENT and ELEMENT_VALUE = :ELEMENT_VALUE ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRelationRow.class);
			q.setString("DATA_SET", rr.getDATA_SET());
			q.setString("ELEMENT", rr.getELEMENT());
			q.setString("ELEMENT_VALUE", rr.getELEMENT_VALUE());
			rs = (ResRelationRow) q.uniqueResult();
			
			if(rs != null) {
				rr.setId(rs.getId());
				rr.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				rr.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			rr.setLATEST_MOD_TIME(timenow);
			
			rr = (ResRelationRow) session.merge(rr);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名数据集-字段-字段值。");
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
		return rr;
	}

	@Override
	public ResRelationRowPrivate ResRelationRowPrivateSave(ResRelationRowPrivate rrrp) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResRelationRowPrivate rs = null;
		String sqlString = "select * from WA_ROW_RELATION_PRIVATE where DATA_SET = :DATA_SET and ELEMENT = :ELEMENT and ELEMENT_VALUE = :ELEMENT_VALUE ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRelationRowPrivate.class);
			q.setString("DATA_SET", rrrp.getDATA_SET());
			q.setString("ELEMENT", rrrp.getELEMENT());
			q.setString("ELEMENT_VALUE", rrrp.getELEMENT_VALUE());
			rs = (ResRelationRowPrivate) q.uniqueResult();
			
			if(rs != null) {
				rrrp.setId(rs.getId());
				rrrp.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				rrrp.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			rrrp.setLATEST_MOD_TIME(timenow);
			
			rrrp = (ResRelationRowPrivate) session.merge(rrrp);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名数据集-字段-字段值。");
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
		return rrrp;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResRelationRow> QueryAllResRelationRow() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResRelationRow> rs = null;
		String sqlString = "select * from WA_ROW_RELATION ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRelationRow.class);
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
	public List<ResRelationRowPrivate> QueryAllResRelationRowPrivate() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResRelationRowPrivate> rs = null;
		String sqlString = "select * from WA_ROW_RELATION_PRIVATE ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRelationRowPrivate.class);
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

	@Override
	public int ResRowRelationImportClear() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		int rs = 0;
		String sqlString = "delete from WA_ROW_RELATION ";
		try {
			Query q = session.createSQLQuery(sqlString);
			rs = q.executeUpdate();
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

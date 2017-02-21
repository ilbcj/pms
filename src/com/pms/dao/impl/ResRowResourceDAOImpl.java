package com.pms.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResRowResourceDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResRowResource;
import com.pms.model.ResRowResourcePrivate;
import com.pms.util.DateTimeUtil;

public class ResRowResourceDAOImpl implements ResRowResourceDAO {

	@Override
	public ResRowResource ResRelationRowSave(ResRowResource rr)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResRowResource rs = null;
		String sqlString = "select * from WA_ROW_RESOURCE where DATA_SET = :DATA_SET and ELEMENT = :ELEMENT and ELEMENT_VALUE = :ELEMENT_VALUE ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRowResource.class);
			q.setString("DATA_SET", rr.getDATA_SET());
			q.setString("ELEMENT", rr.getELEMENT());
			q.setString("ELEMENT_VALUE", rr.getELEMENT_VALUE());
			rs = (ResRowResource) q.uniqueResult();
			
			if(rs != null) {
				rr.setId(rs.getId());
				rr.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				rr.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			rr.setLATEST_MOD_TIME(timenow);
			
			rr = (ResRowResource) session.merge(rr);
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
	public ResRowResourcePrivate ResRelationRowPrivateSave(ResRowResourcePrivate rrrp) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResRowResourcePrivate rs = null;
		String sqlString = "select * from WA_ROW_RESOURCE_PRIVATE where DATA_SET = :DATA_SET and ELEMENT = :ELEMENT and ELEMENT_VALUE = :ELEMENT_VALUE ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRowResourcePrivate.class);
			q.setString("DATA_SET", rrrp.getDATA_SET());
			q.setString("ELEMENT", rrrp.getELEMENT());
			q.setString("ELEMENT_VALUE", rrrp.getELEMENT_VALUE());
			rs = (ResRowResourcePrivate) q.uniqueResult();
			
			if(rs != null) {
				rrrp.setId(rs.getId());
				rrrp.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				rrrp.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			rrrp.setLATEST_MOD_TIME(timenow);
			
			rrrp = (ResRowResourcePrivate) session.merge(rrrp);
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
	public List<ResRowResource> QueryAllResRelationRow() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResRowResource> rs = null;
		String sqlString = "select * from WA_ROW_RELATION ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRowResource.class);
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
	public List<ResRowResourcePrivate> QueryAllResRelationRowPrivate() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResRowResourcePrivate> rs = null;
		String sqlString = "select * from WA_ROW_RELATION_PRIVATE ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRowResourcePrivate.class);
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

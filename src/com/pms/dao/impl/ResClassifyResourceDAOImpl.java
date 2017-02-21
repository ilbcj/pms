package com.pms.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResClassifyResourceDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResClassifyResource;
import com.pms.util.DateTimeUtil;

public class ResClassifyResourceDAOImpl implements ResClassifyResourceDAO {

	@Override
	public ResClassifyResource ResClassifyResourceSave(ResClassifyResource rc)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResClassifyResource rs = null;
		String sqlString = "select * from WA_CLASSIFY_RESOURCE where DATA_SET = :DATA_SET and SECTION_CLASS = :SECTION_CLASS and ELEMENT = :ELEMENT ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResClassifyResource.class);
			q.setString("DATA_SET", rc.getDATA_SET());
			q.setString("SECTION_CLASS", rc.getSECTION_CLASS());
			q.setString("ELEMENT", rc.getELEMENT());
			rs = (ResClassifyResource) q.uniqueResult();
			
			if(rs != null) {
				rc.setId(rs.getId());
				rc.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				rc.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			rc.setLATEST_MOD_TIME(timenow);
			
			rc = (ResClassifyResource) session.merge(rc);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名数据集-字段分类-字段。");
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
		return rc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResClassifyResource> QueryAllResClassifyResource() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResClassifyResource> rs = null;
		String sqlString = "select * from wa_classify_resource ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResClassifyResource.class);
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
	public ResClassifyResource QueryResClassifyResource(String dataset, String element) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		ResClassifyResource rs = null;
		String sqlString = "select * from wa_classify_resource where DATA_SET = :DATASET and ELEMENT = :ELEMENT ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResClassifyResource.class);
			q.setString("DATASET", dataset);
			q.setString("ELEMENT", element);
			rs = (ResClassifyResource) q.uniqueResult();
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
	public int ResClassifyResourceImportClear() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		int rs = 0;
		String sqlString = "delete from wa_classify_resource ";
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

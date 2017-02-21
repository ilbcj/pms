package com.pms.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResClassifyRelationResourceDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResClassifyRelationResource;
import com.pms.util.DateTimeUtil;

public class ResClassifyRelationResourceDAOImpl implements ResClassifyRelationResourceDAO {

	@Override
	public ResClassifyRelationResource ResRelationClassifySave(ResClassifyRelationResource rc)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResClassifyRelationResource rs = null;
		String sqlString = "select * from WA_CLASSIFY_RELATION_RESOURCE where DATA_SET = :DATA_SET and SECTION_RELATIOIN_CLASS = :SECTION_RELATIOIN_CLASS";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResClassifyRelationResource.class);
			q.setString("DATA_SET", rc.getDATA_SET());
			q.setString("SECTION_RELATIOIN_CLASS", rc.getSECTION_RELATIOIN_CLASS());
			rs = (ResClassifyRelationResource) q.uniqueResult();
			
			if(rs != null) {
				rc.setId(rs.getId());
				rc.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				rc.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			rc.setLATEST_MOD_TIME(timenow);
			
			rc = (ResClassifyRelationResource) session.merge(rc);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名数据集-字段分类关系。");
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
	public List<ResClassifyRelationResource> QueryAllResRelationClassify()
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResClassifyRelationResource> rs = null;
		String sqlString = "select * from wa_classify_relation_resource ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResClassifyRelationResource.class);
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
	public List<ResClassifyRelationResource> QueryResRelationClassify(String dataSet)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResClassifyRelationResource> rs = null;
		String sqlString = "select * from wa_classify_relation_resource WHERE DATA_SET = :DATA_SET ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResClassifyRelationResource.class);
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

	@Override
	public int ResClassifyRelationImportClear() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		int rs = 0;
		String sqlString = "delete from wa_classify_relation_resource ";
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

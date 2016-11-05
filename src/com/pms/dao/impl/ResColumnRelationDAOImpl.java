package com.pms.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResColumnRelationDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResRelationColumn;
import com.pms.util.DateTimeUtil;

public class ResColumnRelationDAOImpl implements ResColumnRelationDAO {

	@Override
	public ResRelationColumn ResRelationColumnSave(ResRelationColumn rc)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResRelationColumn rs = null;
		String sqlString = "select * from WA_COLUMN_RELATION where ID = :ID ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRelationColumn.class);
			q.setInteger("ID", rc.getId());
			rs = (ResRelationColumn) q.uniqueResult();
			
			if(rs != null) {
				rc.setId(rs.getId());
				rc.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				rc.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			rc.setLATEST_MOD_TIME(timenow);
			
			rc = (ResRelationColumn) session.merge(rc);
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
	public List<ResRelationColumn> QueryAllResRelationColumn() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResRelationColumn> rs = null;
		String sqlString = "select * from WA_COLUMN_RELATION ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRelationColumn.class);
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
	public int ResColumnRelationImportClear() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		int rs = 0;
		String sqlString = "delete from WA_COLUMN_RELATION ";
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

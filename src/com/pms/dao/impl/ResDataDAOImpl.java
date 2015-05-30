package com.pms.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResDataDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResData;
import com.pms.model.ResDataOrg;

public class ResDataDAOImpl implements ResDataDAO {

	@SuppressWarnings("unchecked")
	@Override
	public ResData ResDataOfColumnSave(ResData rd, String orgid)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		rd.setDATA_VERSION(1);
		
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_RESOURCE where DATA_SET = :DATA_SET and ELEMENT = :ELEMENT and ELEMENT_VALUE is null ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("DATA_SET", rd.getDATA_SET());
			q.setString("ELEMENT", rd.getELEMENT());
			rs = q.list();
			
			ResDataOrg rdo = null;
			sqlString = "select * from WA_AUTHORITY_RESOURCE_ORG where RESOURCE_ID = :RESOURCE_ID ";
			q = session.createSQLQuery(sqlString).addEntity(ResDataOrg.class);
			for(int i = 0; i<rs.size(); i++) {
				q.setString("RESOURCE_ID", rs.get(i).getRESOURCE_ID());
				rdo = (ResDataOrg) q.uniqueResult();
				if( rdo.getCLUE_DST_SYS().equals( orgid ) ) {
					rd.setId(rs.get(i).getId());
					rd.setRESOURCE_ID(rs.get(i).getRESOURCE_ID());
					rd.setDATA_VERSION(rs.get(i).getDATA_VERSION() + 1);
					break;
				} else {
					continue;
				}
			}
						
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
			rd.setLATEST_MOD_TIME(timenow);
			
			rd = (ResData) session.merge(rd);
			if( rd.getRESOURCE_ID() == null || rd.getRESOURCE_ID().length() == 0 ) {
				rd.setRESOURCE_ID( new Integer(rd.getId()).toString() );
				session.merge(rd);
			}
			
			if(rdo == null) {
				rdo = new ResDataOrg();
				rdo.setRESOURCE_ID(rd.getRESOURCE_ID());
				rdo.setCLUE_DST_SYS(orgid);
				rdo.setDELETE_STATUS(ResDataOrg.DELSTATUSNO);
				rdo.setDATA_VERSION(1);
				rdo.setLATEST_MOD_TIME(timenow);
				session.merge(rdo);
			}
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名资源。");
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
		return rd;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void ResDataOfRelationColumnSave(ResData rd, String orgid)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
				
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_RESOURCE where DATA_SET = :DATA_SET and ELEMENT = :ELEMENT and ELEMENT_VALUE is null ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("DATA_SET", rd.getDATA_SET());
			q.setString("ELEMENT", rd.getELEMENT());
			rs = q.list();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
			
			ResDataOrg rdo = null;
			sqlString = "select * from WA_AUTHORITY_RESOURCE_ORG where RESOURCE_ID = :RESOURCE_ID ";
			q = session.createSQLQuery(sqlString).addEntity(ResDataOrg.class);
			for(int i = 0; i<rs.size(); i++) {
				ResData dataResource = rs.get(i);
				q.setString("RESOURCE_ID", dataResource.getRESOURCE_ID());
				rdo = (ResDataOrg) q.uniqueResult();
				if( rdo.getCLUE_DST_SYS().equals( orgid ) ) {
					dataResource.setRESOURCE_DESCRIBE(rd.getRESOURCE_DESCRIBE());
					dataResource.setDATA_VERSION(dataResource.getDATA_VERSION() + 1);
					dataResource.setSECTION_CLASS(rd.getSECTION_CLASS());
					dataResource.setLATEST_MOD_TIME(timenow);
					session.merge(dataResource);
					break;
				} else {
					continue;
				}
			}
						
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名资源。");
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
		return;		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void ResDataOfRelationRowSave(ResData rd, String orgid)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		rd.setDATA_VERSION(1);
		
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_RESOURCE where DATA_SET = :DATA_SET and ELEMENT = :ELEMENT and ELEMENT_VALUE =:ELEMENT_VALUE ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("DATA_SET", rd.getDATA_SET());
			q.setString("ELEMENT", rd.getELEMENT());
			q.setString("ELEMENT_VALUE", rd.getELEMENT_VALUE());
			rs = q.list();
			
			ResDataOrg rdo = null;
			sqlString = "select * from WA_AUTHORITY_RESOURCE_ORG where RESOURCE_ID = :RESOURCE_ID ";
			q = session.createSQLQuery(sqlString).addEntity(ResDataOrg.class);
			for(int i = 0; i<rs.size(); i++) {
				q.setString("RESOURCE_ID", rs.get(i).getRESOURCE_ID());
				rdo = (ResDataOrg) q.uniqueResult();
				if( rdo.getCLUE_DST_SYS().equals( orgid ) ) {
					rd.setId(rs.get(i).getId());
					rd.setRESOURCE_ID(rs.get(i).getRESOURCE_ID());
					rd.setDATA_VERSION(rs.get(i).getDATA_VERSION() + 1);
					break;
				} else {
					continue;
				}
			}
						
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
			rd.setLATEST_MOD_TIME(timenow);
			
			rd = (ResData) session.merge(rd);
			if( rd.getRESOURCE_ID() == null || rd.getRESOURCE_ID().length() == 0 ) {
				rd.setRESOURCE_ID( new Integer(rd.getId()).toString() );
				session.merge(rd);
			}
			
			if(rdo == null) {
				rdo = new ResDataOrg();
				rdo.setRESOURCE_ID(rd.getRESOURCE_ID());
				rdo.setCLUE_DST_SYS(orgid);
				rdo.setDELETE_STATUS(ResDataOrg.DELSTATUSNO);
				rdo.setDATA_VERSION(1);
				rdo.setLATEST_MOD_TIME(timenow);
				session.merge(rdo);
			}
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名资源。");
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
		return;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void ResDataOfRelationClassifySave(ResData rd, String orgid)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		rd.setDATA_VERSION(1);
		
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_RESOURCE where DATA_SET = :DATA_SET and SECTION_RELATIOIN_CLASS = :SECTION_RELATIOIN_CLASS ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("DATA_SET", rd.getDATA_SET());
			q.setString("SECTION_RELATIOIN_CLASS", rd.getSECTION_RELATIOIN_CLASS());
			rs = q.list();
			
			ResDataOrg rdo = null;
			sqlString = "select * from WA_AUTHORITY_RESOURCE_ORG where RESOURCE_ID = :RESOURCE_ID ";
			q = session.createSQLQuery(sqlString).addEntity(ResDataOrg.class);
			for(int i = 0; i<rs.size(); i++) {
				q.setString("RESOURCE_ID", rs.get(i).getRESOURCE_ID());
				rdo = (ResDataOrg) q.uniqueResult();
				if( rdo.getCLUE_DST_SYS().equals( orgid ) ) {
					rd.setId(rs.get(i).getId());
					rd.setRESOURCE_ID(rs.get(i).getRESOURCE_ID());
					rd.setDATA_VERSION(rs.get(i).getDATA_VERSION() + 1);
					break;
				} else {
					continue;
				}
			}
						
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
			rd.setLATEST_MOD_TIME(timenow);
			
			rd = (ResData) session.merge(rd);
			if( rd.getRESOURCE_ID() == null || rd.getRESOURCE_ID().length() == 0 ) {
				rd.setRESOURCE_ID( new Integer(rd.getId()).toString() );
				session.merge(rd);
			}
			
			if(rdo == null) {
				rdo = new ResDataOrg();
				rdo.setRESOURCE_ID(rd.getRESOURCE_ID());
				rdo.setCLUE_DST_SYS(orgid);
				rdo.setDELETE_STATUS(ResDataOrg.DELSTATUSNO);
				rdo.setDATA_VERSION(1);
				rdo.setLATEST_MOD_TIME(timenow);
				session.merge(rdo);
			}
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名资源。");
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
		return;
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public List<ResColumn> QueryAllColumn() throws Exception {
//		Session session = HibernateUtil.currentSession();
//		Transaction tx = session.beginTransaction();
//		
//		List<ResColumn> rs = null;
//		String sqlString = "select * from WA_COLUMN ";
//		try {
//			Query q = session.createSQLQuery(sqlString).addEntity(ResColumn.class);
//			rs = q.list();
//			tx.commit();
//		} catch(Exception e) {
//			e.printStackTrace();
//			tx.rollback();
//			System.out.println(e.getMessage());
//			throw e;
//		}
//		finally
//		{
//			HibernateUtil.closeSession();
//		}
//		return rs;
//	}

}

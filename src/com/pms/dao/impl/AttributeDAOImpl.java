package com.pms.dao.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pms.dao.AttributeDAO;
import com.pms.model.AttrDefinition;
import com.pms.model.AttrDictionary;
import com.pms.model.HibernateUtil;

public class AttributeDAOImpl implements AttributeDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<AttrDefinition> GetAttrDefinitions(AttrDefinition criteria,
			int page, int rows) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AttrDefinition> rs = null;
		String sqlString = "select * from attrdef where 0 = 0 ";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getCode() != null && criteria.getCode().length() > 0) {
				sqlString += " and code like :code ";
			}
			if(criteria.getType() != 0) {
				sqlString += " and type = :type ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AttrDefinition.class);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getCode() != null && criteria.getCode().length() > 0) {
					q.setString( "code", "%" + criteria.getCode() + "%" );
				}
				if(criteria.getType() != 0) {
					q.setInteger( "type", criteria.getType() );
				}
			}
			if( page > 0 && rows >0 ) {
				q.setFirstResult((page-1) * rows);   
				q.setMaxResults(rows);   
			}
			rs = q.list();
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

	@Override
	public int GetAttrDefinitionsCount(AttrDefinition criteria)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from attrdef where 0 = 0 ";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getCode() != null && criteria.getCode().length() > 0) {
				sqlString += " and code like :code ";
			}
			if(criteria.getType() != 0) {
				sqlString += " and type = :type ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getCode() != null && criteria.getCode().length() > 0) {
					q.setString( "code", "%" + criteria.getCode() + "%" );
				}
				if(criteria.getType() != 0) {
					q.setInteger( "type", criteria.getType() );
				}
			}
			rs = ((BigInteger)q.uniqueResult()).intValue();
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AttrDictionary> GetAttrDictionarysByAttrId(int attrId) throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AttrDictionary> rs = null;
		String sqlString = "select * from attrdict where attrid = :attrid ";
	
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AttrDictionary.class);
			q.setInteger("attrid", attrId);
			rs = q.list();
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
	
	@Override
	public void UpdateAttrDictionary(int attrId, List<String> dictionary) throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String sqlString = "delete from attrdict where attrid = :attrid ";
	
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("attrid", attrId);
			q.executeUpdate();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
			for(int i=0;i<dictionary.size(); i++) {
				AttrDictionary attrDict = new AttrDictionary();
				attrDict.setAttrid(attrId);
				attrDict.setValue(dictionary.get(i));
				attrDict.setTstamp(timenow);
				session.merge(attrDict);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return;
	}

}

package com.pms.dao.impl;

import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.AttributeDAO;
import com.pms.model.AttrDefinition;
import com.pms.model.AttrDictionary;
import com.pms.model.HibernateUtil;
import com.pms.util.DateTimeUtil;

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
				sqlString += " and code = :code ";
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
					q.setString( "code", criteria.getCode());
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
				sqlString += " and code = :code ";
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
					q.setString( "code", criteria.getCode());
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
	public void UpdateAttrDictionary(int attrId, List<AttrDictionary> dictionary) throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String sqlString = "delete from attrdict where attrid = :attrid ";
	
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("attrid", attrId);
			q.executeUpdate();
			
			String timenow = DateTimeUtil.GetCurrentTime();

			for(int i=0;i<dictionary.size(); i++) {
				AttrDictionary attrDict = new AttrDictionary();
				attrDict.setAttrid(attrId);
				attrDict.setValue(dictionary.get(i).getValue().toString());
				attrDict.setCode(dictionary.get(i).getCode().toString());
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AttrDictionary> GetDatasDictionarys(String id) throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AttrDictionary> rs = null;
		String sqlString = "SELECT b.* " +
				" FROM WA_AUTHORITY_DATA_RESOURCE a,attrdict b,attrdef c " +
				" WHERE b.attrid=c.id and c.type =:type and a.RESOURCE_ID=:RESOURCE_ID ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AttrDictionary.class);
			q.setInteger("type", AttrDefinition.ATTRTYPERESOURCEDATA);
			q.setString("RESOURCE_ID", id);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AttrDictionary> GetFeaturesDictionarys(String id) throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AttrDictionary> rs = null;
		String sqlString = "SELECT b.* " +
				" FROM WA_AUTHORITY_FUNC_RESOURCE a,attrdict b,attrdef c " +
				" WHERE b.attrid=c.id and c.type =:type and a.RESOURCE_ID=:RESOURCE_ID ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AttrDictionary.class);
			q.setInteger("type", AttrDefinition.ATTRTYPEFUNCDATA);
			q.setString("RESOURCE_ID", id);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AttrDictionary> GetRolesDictionarys(int id) throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AttrDictionary> rs = null;
		String sqlString = "SELECT b.* " +
				" FROM WA_AUTHORITY_ROLE a,attrdict b,attrdef c " +
				" WHERE b.attrid=c.id and c.type =:type and a.id=:id ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AttrDictionary.class);
			q.setInteger("type", AttrDefinition.ATTRTYPEROLE);
			q.setInteger("id", id);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<AttrDictionary> GetUsersDictionarys(int id) throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AttrDictionary> rs = null;
		String sqlString = "SELECT b.* " +
				" FROM WA_AUTHORITY_POLICE a,attrdict b,attrdef c " +
				" WHERE b.attrid=c.id and c.type =:type and a.id=:id ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AttrDictionary.class);
			q.setInteger("type", AttrDefinition.ATTRTYPEUSER);
			q.setInteger("id", id);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AttrDictionary> GetOrgsDictionarys(String id) throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AttrDictionary> rs = null;
		String sqlString = "SELECT b.* " +
				" FROM WA_AUTHORITY_ORGNIZATION a,attrdict b,attrdef c " +
				" WHERE b.attrid=c.id and c.type =:type and a.GA_DEPARTMENT=:GA_DEPARTMENT ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AttrDictionary.class);
			q.setInteger("type", AttrDefinition.ATTRTYPEORG);
			q.setString("GA_DEPARTMENT", id);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AttrDictionary> GetDictsDatasNode(String name, String code, String id) throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AttrDictionary> rs = null;
		String sqlString = "SELECT b.* " +
				" FROM WA_AUTHORITY_DATA_RESOURCE a,attrdict b,attrdef c " +
				" WHERE b.attrid=c.id and c.name=:name and b.code=:code and a.RESOURCE_ID=:RESOURCE_ID ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AttrDictionary.class);
			q.setString("name", name);
			q.setString("code", code);
			q.setString("RESOURCE_ID", id);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AttrDictionary> GetDictsDataTemplatesNode(String name, String code, String id) throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AttrDictionary> rs = null;
		String sqlString = "SELECT b.* " +
				" FROM WA_AUTHORITY_DATA_RESOURCE_Template a,attrdict b,attrdef c " +
				" WHERE b.attrid=c.id and c.name=:name and b.code=:code and a.RESOURCE_ID=:RESOURCE_ID ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AttrDictionary.class);
			q.setString("name", name);
			q.setString("code", code);
			q.setString("RESOURCE_ID", id);
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
	public AttrDictionary GetAttrDictionarysByAttrIdAndCode(int attrId, String code) throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		AttrDictionary rs = null;
		String sqlString = "SELECT * FROM attrdict WHERE attrid =:attrid AND code =:code";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AttrDictionary.class);
			q.setInteger("attrid", attrId);
			q.setString("code", code);
			rs = (AttrDictionary) q.uniqueResult();
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
	public AttrDefinition GetAttrDefinitionByCode(String code) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		AttrDefinition rs = null;
		String sqlString = "select * from attrdef where code = :code ";
				
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AttrDefinition.class);
			q.setString( "code", code );
			
			rs = (AttrDefinition)q.uniqueResult();
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
	public AttrDictionary AttrDictionaryAdd(AttrDictionary ad) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
			
		AttrDictionary rs = null;
		String sqlString = "select * from attrdict where attrid = :attrid and value = :value and code = :code ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AttrDictionary.class);
			q.setInteger("attrid", ad.getAttrid());
			q.setString("value", ad.getValue());
			q.setString("code", ad.getCode());
			rs = (AttrDictionary)q.uniqueResult();
			
			if( rs != null ) {
				if( rs.getAttrid() == ad.getAttrid() && rs.getValue().equals(ad.getValue()) && rs.getCode().equals(ad.getCode()) ) {
					return rs;
				}
				else {
					ad.setId(rs.getId());
				}
			} 
			
			
			
			String timenow = DateTimeUtil.GetCurrentTime();
			ad.setTstamp(timenow);
			
			ad = (AttrDictionary) session.merge(ad);
			tx.commit();	
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名属性。");
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
		return ad;
	}
	
}

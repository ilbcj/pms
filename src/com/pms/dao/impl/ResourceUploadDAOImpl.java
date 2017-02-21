package com.pms.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResourceUploadDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResData;
import com.pms.model.ResRole;
import com.pms.model.ResRoleResource;
import com.pms.model.RowResourceColumn;
import com.pms.util.ConfigHelper;
import com.pms.util.DateTimeUtil;

public class ResourceUploadDAOImpl implements ResourceUploadDAO {

	private static Log logger = LogFactory.getLog(ResourceUploadDAOImpl.class);
	
	@Override
	public ResRole DefaultRoleOfDataSetAdd(ResRole role) throws Exception {
		//compare business_role_type and role_desc( which is data set code ), if record exist, then update, else create.
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResRole rs = null;
		String sqlString = "select * from wa_authority_role where role_desc = :role_desc and  business_role_type = :business_role_type";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRole.class);
			q.setString("role_desc", role.getROLE_DESC());
			q.setInteger("business_role_type", role.getBUSINESS_ROLE_TYPE());
			rs = (ResRole) q.uniqueResult();
			
			if(rs != null) {
				role.setId(rs.getId());
				role.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				role.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			role.setLATEST_MOD_TIME(timenow);
			
			role = (ResRole) session.merge(role);
			if( role.getBUSINESS_ROLE() == null || role.getBUSINESS_ROLE().length() == 0 ) {
				role.setBUSINESS_ROLE( String.format("%s%010d", ConfigHelper.getInstance().getRegion(), role.getId()) );
				role = (ResRole)session.merge(role);
			}
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名角色。");
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
		return role;
	}

	@SuppressWarnings("unchecked")
	public ResData ImportResDataOfColumn(ResData rd) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		rd.setDATA_VERSION(1);
		
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where DATA_SET = :DATA_SET and ELEMENT = :ELEMENT and SECTION_CLASS is null and SECTION_RELATIOIN_CLASS is null and ELEMENT_VALUE is null and resource_type=:resource_type ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("DATA_SET", rd.getDATA_SET());
			q.setString("ELEMENT", rd.getELEMENT());
			//q.setString("SECTION_CLASS", rd.getSECTION_CLASS());
			q.setInteger("resource_type", rd.getResource_type());
			rs = q.list();
			
			if( rs != null && rs.size() > 1) {
				String warnMesg = "[IRD]duplicate record found when search data resource of column by condition of dataset:'" + rd.getDATA_SET() + "', element:'" + rd.getELEMENT() + "', sectionClass:'" + rd.getSECTION_CLASS() + "' ";
				logger.info(warnMesg);
				throw new Exception(warnMesg);
			} else if ( rs != null && rs.size() == 1) {
				ResData exist = rs.get(0);
				if( exist.getDELETE_STATUS() != ResData.DELSTATUSNO ) {
					exist.setDATA_VERSION(exist.getDATA_VERSION() + 1);
					exist.setDELETE_STATUS(ResData.DELSTATUSNO);
					session.merge(exist);
				}
				rd = exist;
			} else {
				String timenow = DateTimeUtil.GetCurrentTime();
				rd.setLATEST_MOD_TIME(timenow);
				
				rd = (ResData)session.merge(rd);
				if( rd.getRESOURCE_ID() == null || rd.getRESOURCE_ID().length() == 0 ) {
					rd.setRESOURCE_ID( String.format("%s%010d", ConfigHelper.getInstance().getRegion(), rd.getId()) );
					rd = (ResData)session.merge(rd);
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
		return rd;		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ResRoleResource ResRoleResourceAdd(ResRoleResource resRoleResource) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String timenow = DateTimeUtil.GetCurrentTime();
		
		List<ResRoleResource> rs = null;
		String sqlString = "SELECT * FROM wa_authority_resource_role WHERE business_role=:business_role AND resource_id =:resource_id ";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRoleResource.class);
			q.setString("business_role", resRoleResource.getBUSINESS_ROLE());
			q.setString("resource_id", resRoleResource.getRESOURCE_ID());
			rs = q.list();

			resRoleResource.setLATEST_MOD_TIME(timenow);
			if(rs.size() == 0) {
				resRoleResource = (ResRoleResource) session.merge(resRoleResource);
			}
			else if(rs.size() == 1) {
				if(rs.get(0).getDELETE_STATUS() != ResRoleResource.DELSTATUSNO) {
					resRoleResource.setId(rs.get(0).getId());
					resRoleResource.setDATA_VERSION(rs.get(0).getDATA_VERSION() + 1);
					resRoleResource = (ResRoleResource) session.merge(resRoleResource);
				}
			}
			else {
				String warnMesg = "[IRRARD]duplicate record found when search role and resource relation by condition of business_role:'" + resRoleResource.getBUSINESS_ROLE() + "', resource_id:'" + resRoleResource.getRESOURCE_ID() + "' ";
				logger.info(warnMesg);
				throw new Exception(warnMesg);
			}
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名的角色资源对应关系。");
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
		return resRoleResource;
	}

	@Override
	public ResRole GetDefaultRoleByDataSet(String dataSet, int roleType)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResRole rs = null;
		String sqlString = "select * from wa_authority_role where role_desc = :role_desc and  business_role_type = :business_role_type";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRole.class);
			q.setString("role_desc", dataSet);
			q.setInteger("business_role_type", roleType);
			rs = (ResRole) q.uniqueResult();
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名角色。");
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
		return rs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResData ImportResDataOfRow(ResData rd) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		rd.setDATA_VERSION(1);
		
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where DATA_SET = :DATA_SET and ELEMENT = :ELEMENT and ELEMENT_VALUE =:ELEMENT_VALUE and resource_type=:resource_type ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("DATA_SET", rd.getDATA_SET());
			q.setString("ELEMENT", rd.getELEMENT());
			q.setString("ELEMENT_VALUE", rd.getELEMENT_VALUE());
			q.setInteger("resource_type", rd.getResource_type());
			rs = q.list();
			
			if( rs != null && rs.size() > 1) {
				String warnMesg = "[IRD]duplicate record found when search data resource of row relation by condition of dataset:'" + rd.getDATA_SET() + "', element:'" + rd.getELEMENT() + "', element_value:'" + rd.getELEMENT_VALUE() + "' ";
				logger.info(warnMesg);
				throw new Exception(warnMesg);
			} else if ( rs != null && rs.size() == 1) {
				ResData exist = rs.get(0);
				if( exist.getDELETE_STATUS() != ResData.DELSTATUSNO ) {
					exist.setDATA_VERSION(exist.getDATA_VERSION() + 1);
					exist.setDELETE_STATUS(ResData.DELSTATUSNO);
					session.merge(exist);
				}
				rd = exist;
			} else {
				String timenow = DateTimeUtil.GetCurrentTime();
				rd.setLATEST_MOD_TIME(timenow);
				
				rd = (ResData) session.merge(rd);
				if( rd.getRESOURCE_ID() == null || rd.getRESOURCE_ID().length() == 0 ) {
					rd.setRESOURCE_ID( String.format("%s%010d", ConfigHelper.getInstance().getRegion(), rd.getId()) );
					rd = (ResData)session.merge(rd);
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
		return rd;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResData ImportResDataOfClassify(ResData rd) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		rd.setDATA_VERSION(1);
		
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where DATA_SET = :DATA_SET and ELEMENT = :ELEMENT and SECTION_CLASS = :SECTION_CLASS and ELEMENT_VALUE is null and resource_type=:resource_type ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("DATA_SET", rd.getDATA_SET());
			q.setString("ELEMENT", rd.getELEMENT());
			q.setString("SECTION_CLASS", rd.getSECTION_CLASS());
			q.setInteger("resource_type", rd.getResource_type());
			rs = q.list();
			
			if( rs != null && rs.size() > 1) {
				String warnMesg = "[IRD]duplicate record found when search data resource of column relation by condition of dataset:'" + rd.getDATA_SET() + "', element:'" + rd.getELEMENT() + "', sectionClass:'" + rd.getSECTION_CLASS() + "' ";
				logger.info(warnMesg);
				throw new Exception(warnMesg);
			} else if ( rs != null && rs.size() == 1) {
				ResData exist = rs.get(0);
				if( exist.getDELETE_STATUS() != ResData.DELSTATUSNO ) {
					exist.setDATA_VERSION(exist.getDATA_VERSION() + 1);
					exist.setDELETE_STATUS(ResData.DELSTATUSNO);
					session.merge(exist);
				}
				rd = exist;
			} else {
				String timenow = DateTimeUtil.GetCurrentTime();
				rd.setLATEST_MOD_TIME(timenow);
				
				rd = (ResData) session.merge(rd);
				if( rd.getRESOURCE_ID() == null || rd.getRESOURCE_ID().length() == 0 ) {
					rd.setRESOURCE_ID( String.format("%s%010d", ConfigHelper.getInstance().getRegion(), rd.getId()) );
					rd = (ResData)session.merge(rd);
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
		return rd;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResData ImportResDataOfClassifyRelation(ResData rd)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		rd.setDATA_VERSION(1);
		
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where DATA_SET = :DATA_SET and SECTION_RELATIOIN_CLASS = :SECTION_RELATIOIN_CLASS and resource_type=:resource_type";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("DATA_SET", rd.getDATA_SET());
			q.setString("SECTION_RELATIOIN_CLASS", rd.getSECTION_RELATIOIN_CLASS());
			q.setInteger("resource_type", rd.getResource_type());
			rs = q.list();
			
			if( rs != null && rs.size() > 1) {
				String warnMesg = "[IRD]duplicate record found when search data resource of classify relation by condition of dataset:'" + rd.getDATA_SET() + "', sectionRelationClass:'" + rd.getSECTION_RELATIOIN_CLASS() + "' ";
				logger.info(warnMesg);
				throw new Exception(warnMesg);
			} else if ( rs != null && rs.size() == 1) {
				ResData exist = rs.get(0);
				if( exist.getDELETE_STATUS() != ResData.DELSTATUSNO ) {
					exist.setDATA_VERSION(exist.getDATA_VERSION() + 1);
					exist.setDELETE_STATUS(ResData.DELSTATUSNO);
					session.merge(exist);
				}
				rd = exist;
			} else {
				String timenow = DateTimeUtil.GetCurrentTime();
				rd.setLATEST_MOD_TIME(timenow);
				
				rd = (ResData) session.merge(rd);
				if( rd.getRESOURCE_ID() == null || rd.getRESOURCE_ID().length() == 0 ) {
					rd.setRESOURCE_ID( String.format("%s%010d", ConfigHelper.getInstance().getRegion(), rd.getId()) );
					rd = (ResData) session.merge(rd);
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
		return rd;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResData> GetDataResourcesOfClassify(String dataSet, String sectionClass, String element, int resourceType ) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where DELETE_STATUS=:DELETE_STATUS and resource_type=:resource_type  and ELEMENT_VALUE is null and SECTION_RELATIOIN_CLASS is null and SECTION_CLASS is not null ";
		
		
		if(dataSet != null && dataSet.length() > 0) {
			sqlString += " and DATA_SET=:DATA_SET ";
		}
		if(sectionClass != null && sectionClass.length() > 0) {
			sqlString += " and SECTION_CLASS=:SECTION_CLASS ";
		}
		if(element != null && element.length() > 0) {
			sqlString += " and ELEMENT=:ELEMENT ";
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setInteger("DELETE_STATUS", ResData.DELSTATUSNO);
			q.setInteger("resource_type", resourceType);
			if(dataSet != null && dataSet.length() > 0) {
				q.setString("DATA_SET", dataSet);
			}
			if(sectionClass != null && sectionClass.length() > 0) {
				q.setString("SECTION_CLASS", sectionClass);
			}
			if(element != null && element.length() > 0) {
				q.setString("ELEMENT", element);
			}

			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			logger.info(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResData> GetDataResourcesOfColumn(String dataSet,
			String element, int resourceType) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where DELETE_STATUS=:DELETE_STATUS and resource_type=:resource_type and SECTION_CLASS is null and ELEMENT_VALUE is null and SECTION_RELATIOIN_CLASS is null ";
		
		if(dataSet != null && dataSet.length() > 0) {
			sqlString += " and DATA_SET=:DATA_SET ";
		}
		if(element != null && element.length() > 0) {
			sqlString += " and ELEMENT=:ELEMENT ";
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setInteger("DELETE_STATUS", ResData.DELSTATUSNO);
			q.setInteger("resource_type", resourceType);
			if(dataSet != null && dataSet.length() > 0) {
				q.setString("DATA_SET", dataSet);
			}
			if(element != null && element.length() > 0) {
				q.setString("ELEMENT", element);
			}

			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			logger.info(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResData> GetDataResourcesOfRow(String dataSet, String element, String elemnetValue, int resourceType) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where DELETE_STATUS=:DELETE_STATUS and resource_type=:resource_type and SECTION_CLASS is null and SECTION_RELATIOIN_CLASS is null and ELEMENT_VALUE is not null ";
		
		if(dataSet != null && dataSet.length() > 0) {
			sqlString += " and DATA_SET=:DATA_SET ";
		}
		if(element != null && element.length() > 0) {
			sqlString += " and ELEMENT=:ELEMENT ";
		}
		if(elemnetValue != null && elemnetValue.length() > 0) {
			sqlString += " and ELEMENT_VALUE=:ELEMENT_VALUE ";
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setInteger("DELETE_STATUS", ResData.DELSTATUSNO);
			q.setInteger("resource_type", resourceType);
			if(dataSet != null && dataSet.length() > 0) {
				q.setString("DATA_SET", dataSet);
			}
			if(element != null && element.length() > 0) {
				q.setString("ELEMENT", element);
			}
			if(element != null && element.length() > 0) {
				q.setString("ELEMENT_VALUE", elemnetValue);
			}
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			logger.info(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void RowResourceColumnAdd(RowResourceColumn column) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<RowResourceColumn> rs = null;
		String sqlString = "select * from row_resource_column where element = :element";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(RowResourceColumn.class);
			q.setString("element", column.getElement());
			rs = q.list();
			
			if( rs != null && rs.size() > 1) {
				String warnMesg = "[IRD]duplicate record found when search res resource column by condition of element:'" + column.getElement() + "' ";
				logger.info(warnMesg);
				throw new Exception(warnMesg);
			} else if ( rs != null && rs.size() == 1) {
				// already exist, nothing to do
			} else {
				session.merge(column);
			}
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名行控资源列。");
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
	public List<RowResourceColumn> GetAllRowResourceColumns() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<RowResourceColumn> rs = null;
		String sqlString = "select * from row_resource_column ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(RowResourceColumn.class);
			rs = q.list();
			
			tx.commit();
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
		return rs;
	}
}

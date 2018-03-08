package power.ejb.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.form.MaterialNameQueryInfo;
import power.ejb.workticket.business.RunJWorkticketMap;

/**
 * Facade for entity InvJMaterialAttachment.
 * 
 * @see power.ejb.resource.InvJMaterialAttachment
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvJMaterialAttachmentFacade implements
		InvJMaterialAttachmentFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved InvJMaterialAttachment
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            InvJMaterialAttachment entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvJMaterialAttachment entity) {
		LogUtil.log("saving InvJMaterialAttachment instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent InvJMaterialAttachment entity.
	 * 
	 * @param entity
	 *            InvJMaterialAttachment entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvJMaterialAttachment entity) {
		LogUtil.log("deleting InvJMaterialAttachment instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(InvJMaterialAttachment.class,
					entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved InvJMaterialAttachment entity and return it or
	 * a copy of it to the sender. A copy of the InvJMaterialAttachment entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            InvJMaterialAttachment entity to update
	 * @return InvJMaterialAttachment the persisted InvJMaterialAttachment
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvJMaterialAttachment update(InvJMaterialAttachment entity) {
		LogUtil.log("updating InvJMaterialAttachment instance", Level.INFO,
				null);
		try {
			InvJMaterialAttachment result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvJMaterialAttachment findById(Long id) {
		LogUtil.log("finding InvJMaterialAttachment instance with id: " + id,
				Level.INFO, null);
		try {
			InvJMaterialAttachment instance = entityManager.find(
					InvJMaterialAttachment.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvJMaterialAttachment entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvJMaterialAttachment property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvJMaterialAttachment> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvJMaterialAttachment> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding InvJMaterialAttachment instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from InvJMaterialAttachment model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvJMaterialAttachment entities.
	 * 
	 * @return List<InvJMaterialAttachment> all InvJMaterialAttachment entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvJMaterialAttachment> findAll() {
		LogUtil.log("finding all InvJMaterialAttachment instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from InvJMaterialAttachment model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvJMaterialAttachment entities.
	 * 
	 * @return List<InvJMaterialAttachment> all InvJMaterialAttachment entities
	 */
	@SuppressWarnings("unchecked")
	public InvJMaterialAttachment getMaterialMap(String enterpriseCode,
			String strdocNo) {		
		// 查询sql
		String sql = "SELECT\n"
			+ "A.ID, \n"
			+ "A.DOC_CODE, \n"
			+ "A.ORI_DOC_NAME, \n"
			+ "A.ORI_DOC_EXT, \n"
			+ "A.DOC_CONTENT, \n"
			+ "A.LAST_MODIFIED_BY, \n"
			+ "A.LAST_MODIFIED_DATE, \n"
			+ "A.ENTERPRISE_CODE, \n"
			+ "A.IS_USE \n"
			+ "FROM\n"
			+ "INV_J_MATERIAL_ATTACHMENT A \n" + "WHERE\n"
			+ "A.IS_USE = 'Y' AND\n" + "A.ENTERPRISE_CODE ='"
			+ enterpriseCode + "' AND\n" + "A.DOC_CODE ='" + strdocNo
			+ "' \n";
		List<InvJMaterialAttachment> list = bll.queryByNativeSQL(sql,
				InvJMaterialAttachment.class);		
		if (list != null) {
			if (list.size() > 0) {
				return (InvJMaterialAttachment) list.get(0);
			}
		}
		return null;

	}

}
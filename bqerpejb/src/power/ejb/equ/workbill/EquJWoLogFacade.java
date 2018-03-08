package power.ejb.equ.workbill;

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

/**
 * Facade for entity EquJWoLog.
 * 
 * @see power.ejb.equ.workbill.EquJWoLog
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJWoLogFacade implements EquJWoLogFacadeRemote {
	// property constants


	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * Perform an initial save of a previously unsaved EquJWoLog entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJWoLog entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquJWoLog entity) {
		LogUtil.log("saving EquJWoLog instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void save(List<EquJWoLog> addList,
			List<EquJWoLog> updateList, String delIds) {
		try {
			if (addList != null && addList.size() > 0) {
				Long id = dll.getMaxId("EQU_J_WO_LOG", "ID");
				int i = 0;
				for (EquJWoLog entity : addList) {
					entity.setId(id + (i++));
					this.save(entity);
				}
			}
			for (EquJWoLog entity : updateList) {
				this.update(entity);
			}
			if (delIds != null && !delIds.trim().equals("")) {
				this.delete(delIds);
			}
//			return true;
		} catch (RuntimeException re) {
			throw re;
//			return false;
		}
	}
	/**
	 * Delete a persistent EquJWoLog entity.
	 * 
	 * @param entity
	 *            EquJWoLog entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	
	
	public void delete(EquJWoLog entity) {
		LogUtil.log("deleting EquJWoLog instance", Level.INFO, null);
		try {
			entity = entityManager
					.getReference(EquJWoLog.class, entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	public boolean delete(String ids) {
		LogUtil.log("deleting EquJWoLog instance", Level.INFO, null);
		try {
			String[] temp=ids.split(",");
		
			for(String data:temp){
				long i=Long.parseLong(data);
				delete(findById(i));
			}
			
			return true;
		
		} catch (RuntimeException re) {
		
			return false;
		
		}
		
	}
	/**
	 * Persist a previously saved EquJWoLog entity and return it or a copy of it
	 * to the sender. A copy of the EquJWoLog entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            EquJWoLog entity to update
	 * @return EquJWoLog the persisted EquJWoLog entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJWoLog update(EquJWoLog entity) {
		LogUtil.log("updating EquJWoLog instance", Level.INFO, null);
		try {
			EquJWoLog result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJWoLog findById(Long id) {
		LogUtil.log("finding EquJWoLog instance with id: " + id, Level.INFO,
				null);
		try {
			EquJWoLog instance = entityManager.find(EquJWoLog.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquJWoLog entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJWoLog property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquJWoLog> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquJWoLog> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding EquJWoLog instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquJWoLog model where model."
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
	 * Find all EquJWoLog entities.
	 * 
	 * @return List<EquJWoLog> all EquJWoLog entities
	 */
	@SuppressWarnings("unchecked")
public PageObject findAll(String woCode,int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquJWoLog instances", Level.INFO, null);
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.*\n" + "  	FROM EQU_J_WO_LOG t\n"
					 + "		 where t.WO_CODE='"+ woCode + "'\n"
					  + " ORDER BY t.id\n";

//			String sqlCount = "SELECT count(*)\n"
//					+ "  		 FROM EQU_J_ORDERSTEP t\n"
//					+ " 		WHERE t.if_use = 'Y'\n" + "		 	  AND t.WO_CODE='"
//					+ woCode + "'\n" + "   		  AND t.enterprisecode = '"
//					+ enterpriseCode + "'";

			List<EquJWoLog> list = dll.queryByNativeSQL(sql,
					EquJWoLog.class,rowStartIdxAndCount);
		
//			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			Long count=(long)(list.size());
			result.setList(list);
			result.setTotalCount(count);
			return result;
//			Query query = entityManager.createQuery(queryString);
//			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}
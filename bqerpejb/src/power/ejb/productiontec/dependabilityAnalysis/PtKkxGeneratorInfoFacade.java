package power.ejb.productiontec.dependabilityAnalysis;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity PtKkxGeneratorInfo.
 * 
 * @see power.ejb.productiontec.dependabilityAnalysis.PtKkxGeneratorInfo
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtKkxGeneratorInfoFacade implements PtKkxGeneratorInfoFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtKkxGeneratorInfo
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            PtKkxGeneratorInfo entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public PtKkxGeneratorInfo save(PtKkxGeneratorInfo entity) {
		try {
			entity.setIsUse("Y");
			entity.setGeneratorId(bll.getMaxId("PT_KKX_GENERATOR_INFO",
					"GENERATOR_ID"));
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		
	}

	/**
	 * Delete a persistent PtKkxGeneratorInfo entity.
	 * 
	 * @param entity
	 *            PtKkxGeneratorInfo entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtKkxGeneratorInfo entity) {
		LogUtil.log("deleting PtKkxGeneratorInfo instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtKkxGeneratorInfo.class,
					entity.getGeneratorId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PtKkxGeneratorInfo entity and return it or a
	 * copy of it to the sender. A copy of the PtKkxGeneratorInfo entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            PtKkxGeneratorInfo entity to update
	 * @return PtKkxGeneratorInfo the persisted PtKkxGeneratorInfo entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtKkxGeneratorInfo update(PtKkxGeneratorInfo entity) {
		try {
			PtKkxGeneratorInfo result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtKkxGeneratorInfo findById(Long id) {
		LogUtil.log("finding PtKkxGeneratorInfo instance with id: " + id,
				Level.INFO, null);
		try {
			PtKkxGeneratorInfo instance = entityManager.find(
					PtKkxGeneratorInfo.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PtKkxGeneratorInfo entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtKkxGeneratorInfo property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtKkxGeneratorInfo> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtKkxGeneratorInfo> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PtKkxGeneratorInfo instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtKkxGeneratorInfo model where model."
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
	 * Find all PtKkxGeneratorInfo entities.
	 * 
	 * @return List<PtKkxGeneratorInfo> all PtKkxGeneratorInfo entities
	 */
	@SuppressWarnings("unchecked")
	public List<PtKkxGeneratorInfo> findInfoByBlockId(String blockId) {
		try {
			String sql = "select t.* from pt_kkx_generator_info t where t.block_id = '"
					+ blockId + "'";
			List<PtKkxGeneratorInfo> list = bll.queryByNativeSQL(sql,
					PtKkxGeneratorInfo.class);
			return list;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}
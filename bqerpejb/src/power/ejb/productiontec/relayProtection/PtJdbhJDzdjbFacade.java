package power.ejb.productiontec.relayProtection;

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
 * Facade for entity PtJdbhJDzdjb.
 * 
 * @see power.ejb.productiontec.relayProtection.PtJdbhJDzdjb
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJdbhJDzdjbFacade implements PtJdbhJDzdjbFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public void save(PtJdbhJDzdjb entity) {
		LogUtil.log("saving PtJdbhJDzdjb instance", Level.INFO, null);
		try {
			entity.setDzdjbId(bll.getMaxId("PT_JDBH_J_DZDJB", "dzdjb_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(PtJdbhJDzdjb entity) {
		LogUtil.log("deleting PtJdbhJDzdjb instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJdbhJDzdjb.class, entity
					.getDzdjbId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJdbhJDzdjb update(PtJdbhJDzdjb entity) {
		LogUtil.log("updating PtJdbhJDzdjb instance", Level.INFO, null);
		try {
			PtJdbhJDzdjb result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJdbhJDzdjb findById(Long id) {
		LogUtil.log("finding PtJdbhJDzdjb instance with id: " + id, Level.INFO,
				null);
		try {
			PtJdbhJDzdjb instance = entityManager.find(PtJdbhJDzdjb.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PtJdbhJDzdjb> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtJdbhJDzdjb instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJdbhJDzdjb model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PtJdbhJDzdjb> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtJdbhJDzdjb instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtJdbhJDzdjb model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}
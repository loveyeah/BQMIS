package power.ejb.productiontec.dependabilityAnalysis.business;

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
 * 锅炉信息
 * @author liuyi 091019
 */
@Stateless
public class PtKkxBoilerInfoFacade implements PtKkxBoilerInfoFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	/**
	 * 新增一条锅炉信息数据
	 */
	public void save(PtKkxBoilerInfo entity) {
		LogUtil.log("saving PtKkxBoilerInfo instance", Level.INFO, null);
		try {
			entity.setBoilerId(bll.getMaxId("PT_KKX_BOILER_INFO", "BOILER_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(PtKkxBoilerInfo entity) {
		LogUtil.log("deleting PtKkxBoilerInfo instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtKkxBoilerInfo.class, entity
					.getBoilerId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public PtKkxBoilerInfo update(PtKkxBoilerInfo entity) {
		LogUtil.log("updating PtKkxBoilerInfo instance", Level.INFO, null);
		try {
			PtKkxBoilerInfo result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtKkxBoilerInfo findById(Long id) {
		LogUtil.log("finding PtKkxBoilerInfo instance with id: " + id,
				Level.INFO, null);
		try {
			PtKkxBoilerInfo instance = entityManager.find(
					PtKkxBoilerInfo.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<PtKkxBoilerInfo> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtKkxBoilerInfo instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtKkxBoilerInfo model where model."
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
	public List<PtKkxBoilerInfo> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtKkxBoilerInfo instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtKkxBoilerInfo model";
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
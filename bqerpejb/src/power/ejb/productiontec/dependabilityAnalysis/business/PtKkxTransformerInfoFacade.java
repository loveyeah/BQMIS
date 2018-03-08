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
 * 主变压器信息
 * @author liuyi 091020
 */
@Stateless
public class PtKkxTransformerInfoFacade implements
		PtKkxTransformerInfoFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;
	
	public void save(PtKkxTransformerInfo entity) {
		LogUtil.log("saving PtKkxTransformerInfo instance", Level.INFO, null);
		try {
			entity.setTransformerId(bll.getMaxId("PT_KKX_TRANSFORMER_INFO", "TRANSFORMER_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(PtKkxTransformerInfo entity) {
		LogUtil.log("deleting PtKkxTransformerInfo instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtKkxTransformerInfo.class,
					entity.getTransformerId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public PtKkxTransformerInfo update(PtKkxTransformerInfo entity) {
		LogUtil.log("updating PtKkxTransformerInfo instance", Level.INFO, null);
		try {
			PtKkxTransformerInfo result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtKkxTransformerInfo findById(Long id) {
		LogUtil.log("finding PtKkxTransformerInfo instance with id: " + id,
				Level.INFO, null);
		try {
			PtKkxTransformerInfo instance = entityManager.find(
					PtKkxTransformerInfo.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<PtKkxTransformerInfo> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtKkxTransformerInfo instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtKkxTransformerInfo model where model."
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
	public List<PtKkxTransformerInfo> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtKkxTransformerInfo instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from PtKkxTransformerInfo model";
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
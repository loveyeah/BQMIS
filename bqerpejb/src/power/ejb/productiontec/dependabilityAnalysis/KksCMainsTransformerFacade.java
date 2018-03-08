package power.ejb.productiontec.dependabilityAnalysis;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity KksCMainsTransformer.
 * 
 * @see power.ejb.productiontec.dependabilityAnalysis.KksCMainsTransformer
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class KksCMainsTransformerFacade implements
		KksCMainsTransformerFacadeRemote {


	@PersistenceContext
	private EntityManager entityManager;

	
	public void save(KksCMainsTransformer entity) {
		LogUtil.log("saving KksCMainsTransformer instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(KksCMainsTransformer entity) {
		LogUtil.log("deleting KksCMainsTransformer instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(KksCMainsTransformer.class,
					entity.getZbyqNumber());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}


	public KksCMainsTransformer update(KksCMainsTransformer entity) {
		LogUtil.log("updating KksCMainsTransformer instance", Level.INFO, null);
		try {
			KksCMainsTransformer result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public KksCMainsTransformer findById(String id) {
		LogUtil.log("finding KksCMainsTransformer instance with id: " + id,
				Level.INFO, null);
		try {
			KksCMainsTransformer instance = entityManager.find(
					KksCMainsTransformer.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<KksCMainsTransformer> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all KksCMainsTransformer instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from KksCMainsTransformer model";
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
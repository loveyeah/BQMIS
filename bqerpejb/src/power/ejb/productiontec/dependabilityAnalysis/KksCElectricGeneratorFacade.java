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
 * Facade for entity KksCElectricGenerator.
 * 
 * @see power.ejb.productiontec.dependabilityAnalysis.KksCElectricGenerator
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class KksCElectricGeneratorFacade implements
		KksCElectricGeneratorFacadeRemote {
	

	@PersistenceContext
	private EntityManager entityManager;

	
	public void save(KksCElectricGenerator entity) {
		LogUtil.log("saving KksCElectricGenerator instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(KksCElectricGenerator entity) {
		LogUtil
				.log("deleting KksCElectricGenerator instance", Level.INFO,
						null);
		try {
			entity = entityManager.getReference(KksCElectricGenerator.class,
					entity.getFdjNumber());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public KksCElectricGenerator update(KksCElectricGenerator entity) {
		LogUtil
				.log("updating KksCElectricGenerator instance", Level.INFO,
						null);
		try {
			KksCElectricGenerator result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public KksCElectricGenerator findById(String id) {
		LogUtil.log("finding KksCElectricGenerator instance with id: " + id,
				Level.INFO, null);
		try {
			KksCElectricGenerator instance = entityManager.find(
					KksCElectricGenerator.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<KksCElectricGenerator> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all KksCElectricGenerator instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from KksCElectricGenerator model";
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
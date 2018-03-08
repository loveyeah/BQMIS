package power.ejb.equ.technology;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquCTechnoAttribute.
 * 
 * @see power.ejb.equ.technology.EquCTechnoAttribute
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCTechnoAttributeFacade implements
		EquCTechnoAttributeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	
	public void save(EquCTechnoAttribute entity) {
		LogUtil.log("saving EquCTechnoAttribute instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(EquCTechnoAttribute entity) {
		LogUtil.log("deleting EquCTechnoAttribute instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(EquCTechnoAttribute.class,
					entity.getAttrId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public EquCTechnoAttribute update(EquCTechnoAttribute entity) {
		LogUtil.log("updating EquCTechnoAttribute instance", Level.INFO, null);
		try {
			EquCTechnoAttribute result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquCTechnoAttribute findById(Long id) {
		LogUtil.log("finding EquCTechnoAttribute instance with id: " + id,
				Level.INFO, null);
		try {
			EquCTechnoAttribute instance = entityManager.find(
					EquCTechnoAttribute.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	



	@SuppressWarnings("unchecked")
	public List<EquCTechnoAttribute> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquCTechnoAttribute instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from EquCTechnoAttribute model";
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
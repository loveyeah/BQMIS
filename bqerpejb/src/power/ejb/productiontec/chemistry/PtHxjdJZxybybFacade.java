package power.ejb.productiontec.chemistry;

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
 * 化学在线仪表月报明细表
 */
@Stateless
public class PtHxjdJZxybybFacade implements PtHxjdJZxybybFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 *增加一条化学在线仪表月报明细表记录
	 */
	public PtHxjdJZxybyb save(PtHxjdJZxybyb entity) {
		LogUtil.log("saving PtHxjdJZxybyb instance", Level.INFO, null);
		try {
			entity.setZxybybId(bll.getMaxId("PT_HXJD_J_ZXYBYB", "ZXYBYB_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		return entity;
	}

	/**
	 * 删除一条化学在线仪表月报明细表记录
	 */
	public void delete(PtHxjdJZxybyb entity) {
		LogUtil.log("deleting PtHxjdJZxybyb instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtHxjdJZxybyb.class, entity
					.getZxybybId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条或多条记录
	 */
	public void deleteMulti(String ids) {
		String sql = "delete from  "+
		"PT_HXJD_J_ZXYBYB a\n"
	    + " where a.ZXYBYB_ID in (" + ids
	   + ")\n" ;
       bll.exeNativeSQL(sql);
		
	}
	/**
	 * 更新一条化学在线仪表月报明细表记录
	 */
	public PtHxjdJZxybyb update(PtHxjdJZxybyb entity) {
		LogUtil.log("updating PtHxjdJZxybyb instance", Level.INFO, null);
		try {
			PtHxjdJZxybyb result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过id查找到一条化学在线仪表月报明细表记录
	 */
	public PtHxjdJZxybyb findById(Long id) {
		LogUtil.log("finding PtHxjdJZxybyb instance with id: " + id,
				Level.INFO, null);
		try {
			PtHxjdJZxybyb instance = entityManager
					.find(PtHxjdJZxybyb.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}


	/**
	 * Find all PtHxjdJZxybyb entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtHxjdJZxybyb> all PtHxjdJZxybyb entities
	 */
	@SuppressWarnings("unchecked")
	public List<PtHxjdJZxybyb> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtHxjdJZxybyb instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtHxjdJZxybyb model";
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
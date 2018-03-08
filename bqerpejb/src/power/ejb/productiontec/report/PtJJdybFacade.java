package power.ejb.productiontec.report;

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
 * 继电月报表（主表）
 * @author liuyi 091010
 */
@Stateless
public class PtJJdybFacade implements PtJJdybFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	/**
	 * 新增一条继电月报表（主表）数据
	 */
	public PtJJdyb save(PtJJdyb entity) {
		LogUtil.log("saving PtJJdyb instance", Level.INFO, null);
		try {
			entity.setJdybId(bll.getMaxId("PT_J_JDYB", "JDYB_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		
	}

	/**
	 * 删除一条继电月报表（主表）数据
	 */
	public void delete(PtJJdyb entity) {
		LogUtil.log("deleting PtJJdyb instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJJdyb.class, entity
					.getJdybId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新一条继电月报表（主表）数据
	 */
	public PtJJdyb update(PtJJdyb entity) {
		LogUtil.log("updating PtJJdyb instance", Level.INFO, null);
		try {
			PtJJdyb result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJJdyb findById(Long id) {
		LogUtil
				.log("finding PtJJdyb instance with id: " + id, Level.INFO,
						null);
		try {
			PtJJdyb instance = entityManager.find(PtJJdyb.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<PtJJdyb> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtJJdyb instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJJdyb model where model."
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
	public List<PtJJdyb> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtJJdyb instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtJJdyb model";
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

	public List<PtJJdyb> findSeasonRec(String fromMonth, String toMonth) {
		String sql = "select * from PT_J_JDYB a \n"
			+ "where a.month<='" + toMonth + "' \n"
			+ "and a.month>='" + fromMonth + "' \n";
		List<PtJJdyb> list = bll.queryByNativeSQL(sql, PtJJdyb.class);
		return list;
	}

}
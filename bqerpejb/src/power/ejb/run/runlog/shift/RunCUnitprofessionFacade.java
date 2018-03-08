package power.ejb.run.runlog.shift;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.base.EquCBlock;
import power.ejb.run.runlog.RunCSpecials;

/**
 * Facade for entity RunCUnitprofession.
 * 
 * @see power.ejb.run.runlog.shift.RunCUnitprofession
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCUnitprofessionFacade implements RunCUnitprofessionFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 根据专业编码判断是否已经存在
	 * 
	 * @param code
	 * @param enterpriseCode
	 * @return
	 */
	public boolean existsByCode(String code, String enterpriseCode) {
		String strSql = "select count(1)\n" +
			"  from run_c_unitprofession t\n" + 
			" where t.speciality_code = '"+code+"'\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'";

		Object obj = bll.getSingal(strSql);
		int count = 0;
		count = Integer.parseInt(obj.toString());
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Perform an initial save of a previously unsaved RunCUnitprofession
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            RunCUnitprofession entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunCUnitprofession entity) {
		LogUtil.log("saving RunCUnitprofession instance", Level.INFO, null);
		try {
			if (entity.getId() == null) {
				entity.setId(bll.getMaxId("run_c_unitprofession", "id"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunCUnitprofession entity.
	 * 
	 * @param entity
	 *            RunCUnitprofession entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCUnitprofession entity) {
		LogUtil.log("deleting RunCUnitprofession instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunCUnitprofession.class,
					entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunCUnitprofession entity and return it or a
	 * copy of it to the sender. A copy of the RunCUnitprofession entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCUnitprofession entity to update
	 * @returns RunCUnitprofession the persisted RunCUnitprofession entity
	 *          instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCUnitprofession update(RunCUnitprofession entity) {
		LogUtil.log("updating RunCUnitprofession instance", Level.INFO, null);
		try {
			RunCUnitprofession result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCUnitprofession findById(Long id) {
		LogUtil.log("finding RunCUnitprofession instance with id: " + id,
				Level.INFO, null);
		try {
			RunCUnitprofession instance = entityManager.find(
					RunCUnitprofession.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunCUnitprofession entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCUnitprofession property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunCUnitprofession> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunCUnitprofession> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCUnitprofession instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCUnitprofession model where model."
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

	/**
	 * Find all RunCUnitprofession entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCUnitprofession> all RunCUnitprofession entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunCUnitprofession> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCUnitprofession instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from RunCUnitprofession model";
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

	
	@SuppressWarnings("unchecked")
	public List findUnitList(String enterpriseCode) {
		String sql ="select r.id,\n" +
			"       r.speciality_code,\n" + 
			"       r.speciality_name,\n" + 
			"       r.h_speciality_code,\n" + 
			"       r.display_no,\n" + 
			"       getspecialname(r.h_speciality_code)\n" + 
			"  from run_c_unitprofession r\n" + 
			" where r.is_use = 'Y'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"' order by r.display_no";
		return bll.queryByNativeSQL(sql);
	}
	public List<RunCSpecials> findUnitPList(String enterprisecode){
		String sql ="select r.*\n" +
		"  from run_c_specials r\n" + 
		" where r.is_use = 'Y'\n" + 
		"   and r.enterprise_code = '"+enterprisecode+"' and r.speciality_type in (2,3) order by r.display_no";
	return bll.queryByNativeSQL(sql,RunCSpecials.class);
	}
	public List<RunCUnitprofession> findUnitExp(String code,String enterpriseCode){
		String sql="select r.*\n" +
			"  from run_c_unitprofession r\n" + 
			" where r.speciality_code <> '"+code+"'\n" + 
			"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and r.is_use = 'Y' order by r.display_no";
		return bll.queryByNativeSQL(sql, RunCUnitprofession.class);
	}


}
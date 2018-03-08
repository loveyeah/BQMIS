package power.ejb.run.runlog;

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
import power.ejb.equ.base.EquCLocation;

/**
 * Facade for entity RunCSpecials.
 * 
 * @see power.ejb.run.runlog.RunCSpecials
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCSpecialsFacade implements RunCSpecialsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved RunCSpecials entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCSpecials entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunCSpecials entity) {
		LogUtil.log("saving RunCSpecials instance", Level.INFO, null);
		try {
			if (entity.getSpecialityId() == null) {
				entity.setSpecialityId(bll.getMaxId("RUN_C_SPECIALS",
						"speciality_id"));
				entity.setIsUse("Y");
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunCSpecials entity.
	 * 
	 * @param entity
	 *            RunCSpecials entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCSpecials entity) {
		LogUtil.log("deleting RunCSpecials instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunCSpecials.class, entity
					.getSpecialityId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunCSpecials entity and return it or a copy of
	 * it to the sender. A copy of the RunCSpecials entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            RunCSpecials entity to update
	 * @return RunCSpecials the persisted RunCSpecials entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCSpecials update(RunCSpecials entity) {
		LogUtil.log("updating RunCSpecials instance", Level.INFO, null);
		try {
			RunCSpecials result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCSpecials findById(Long id) {
		LogUtil.log("finding RunCSpecials instance with id: " + id, Level.INFO,
				null);
		try {
			RunCSpecials instance = entityManager.find(RunCSpecials.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunCSpecials entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCSpecials property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunCSpecials> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunCSpecials> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCSpecials instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCSpecials model where model."
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
	 * Find all RunCSpecials entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCSpecials> all RunCSpecials entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunCSpecials> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCSpecials instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCSpecials model";
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

	public PageObject findList(String strWhere,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select * from  run_c_specials \n";
			if (strWhere != "") {
				sql = sql + " where  " + strWhere;
			}
			List<RunCSpecials> list = bll.queryByNativeSQL(sql,
					RunCSpecials.class, rowStartIdxAndCount);
			String sqlCount = "select count(*)　from run_c_specials \n";
			if (strWhere != "") {
				sqlCount = sqlCount + " where  " + strWhere;
			}
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}

	}

	@SuppressWarnings("unchecked")
	public List<RunCSpecials> getListByParent(String speciality_code,
			String enterpriseCode) {
		String strWhere = " p_speciality_code='" + speciality_code + "'\n"
				+ "and  enterprise_code='" + enterpriseCode + "'\n"
				+ "and is_use='Y' order by display_no";

		PageObject result = findList(strWhere);
		return result.getList();

	}

	public RunCSpecials findByCode(String specialityCode, String enterpriseCode) {
		String strWhere = "  speciality_code='" + specialityCode + "'\n"
				+ "and enterprise_code='" + enterpriseCode + "'\n"
				+ "and is_use='Y'";
		PageObject result = findList(strWhere);
		if (result.getList() != null) {
			if (result.getList().size() > 0) {
				return (RunCSpecials) result.getList().get(0);
			}
		}
		return null;
	}

	/**
	 * 根据类型查找专业列表
	 * @param specialityType
	 * @param enterpriseCode
	 * @return RunCSpecials
	 */
	public List<RunCSpecials> findByType(String specialityType, String enterpriseCode) {
		String strWhere = "  speciality_type in (" + specialityType + ")\n"
				+ "and enterprise_code='" + enterpriseCode + "'\n"
				+ "and is_use='Y' and p_speciality_code <>'0' order by display_no ";
		PageObject result = findList(strWhere);
	    return result.getList();
	}

	public boolean isParentNode(String specialCode, String enterpriseCode) {
		String sql = "select count(*)\n" + "   from run_c_specials t\n"
				+ "  where t.p_speciality_code = '" + specialCode + "'\n"
				+ "    and t.is_use = 'Y'\n" + "    and t.enterprise_code = '"
				+ enterpriseCode + "'";
		int count = Integer.parseInt(bll.getSingal(sql).toString());
		if (count == 0) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RunCSpecials> findSpeList(String enterpriseCode) {
		String sql = "select l.*\n" + "  from run_c_specials l\n"
				+ " where l.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and l.is_use = 'Y'\n" + " order by l.display_no";
		return bll.queryByNativeSQL(sql, RunCSpecials.class);
	}

	public boolean existsByCode(String code, String enterpriseCode,Long... specialId) {
		String strSql = "select count(1)\n" + "  from run_c_specials t\n"
				+ " where t.speciality_code = '" + code + "'\n"
				+ "   and t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterpriseCode + "'";

		  if(specialId !=null&& specialId.length>0){
			  strSql += "  and t.speciality_id <> " + specialId[0];
		    } 
		Object obj = bll.getSingal(strSql);
		int count = 0;
		count = Integer.parseInt(obj.toString());
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

}
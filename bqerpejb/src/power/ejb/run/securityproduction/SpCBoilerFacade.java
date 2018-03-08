package power.ejb.run.securityproduction;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.base.EquCEquipments;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity SpCBoiler.
 * 
 * @see power.ejb.run.securityproduction.SpCBoiler
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpCBoilerFacade implements SpCBoilerFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(SpCBoiler entity) {
		if (entity.getBoilerId() == null) {
			entity.setBoilerId(bll.getMaxId("sp_c_boiler", "boiler_id"));
			entity.setIsUse("Y");
		}
		entityManager.persist(entity);
		return;
	}

	public void delete(SpCBoiler entity) {
		LogUtil.log("deleting SpCBoiler instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(SpCBoiler.class, entity
					.getBoilerId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean update(SpCBoiler entity) {
		entityManager.merge(entity);
		LogUtil.log("update successful", Level.INFO, null);
		return true;
	}

	public SpCBoiler findById(Long id) {
		LogUtil.log("finding SpCBoiler instance with id: " + id, Level.INFO,
				null);
		try {
			SpCBoiler instance = entityManager.find(SpCBoiler.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<SpCBoiler> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding SpCBoiler instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from SpCBoiler model where model."
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
	public List<SpCBoiler> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all SpCBoiler instances", Level.INFO, null);
		try {
			final String queryString = "select model from SpCBoiler model";
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
	public PageObject findList(String strWhere,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select * from  sp_c_boiler \n";
			if (strWhere != "") {
				sql = sql + " where  " + strWhere;
			}
			List<SpCBoiler> list = bll.queryByNativeSQL(sql, SpCBoiler.class,
					rowStartIdxAndCount);
			String sqlCount = "select count(*)　from sp_c_boiler \n";
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
	public List<SpCBoiler> getListByParent(String equCode, String enterpriseCode) {
		String strWhere = "";
		if (equCode.equals("root")) {
			strWhere = "   f_boiler_id = 0  \n";
		} else {
			strWhere = " f_boiler_id='" + equCode + "' \n";
		}
		strWhere = strWhere + "and  enterprise_code='" + enterpriseCode + "'\n"
				+ "and is_use='Y' order by boiler_id";
		PageObject result = findList(strWhere);
		return result.getList();

	}

	public boolean IfHasChild(Long equCode, String enterpriseCode) {
		boolean isSame = false;
		String strWhere = "";
		if (equCode.equals("root")) {
			strWhere = "   f_boiler_id = 0  \n";
		} else {
			strWhere = " f_boiler_id='" + equCode + "' \n";
		}
		strWhere = strWhere + "and  enterprise_code='" + enterpriseCode + "'\n"
				+ "and is_use='Y' order by boiler_id";
		String sql = "select count(1)\n" + "  from sp_c_boiler t\n" + " where "
				+ strWhere;
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

	public SpCBoiler findByCode(String boilerId, String enterpriseCode) {
		String strWhere = "  boiler_id='" + boilerId + "' \n"
				+ " and  enterprise_code='" + enterpriseCode + "' \n"
				+ " and is_use='Y'";
		PageObject result = findList(strWhere);
		if (result.getList() != null) {
			if (result.getList().size() > 0) {
				return (SpCBoiler) result.getList().get(0);
			}
		}
		return null;
	}

}
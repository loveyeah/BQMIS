package power.ejb.manage.system;

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

/**
 * Facade for entity BpCMeasureUnit.
 * 
 * @see power.ejb.manage.system.BpCMeasureUnit
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCMeasureUnitFacade implements BpCMeasureUnitFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public int save(BpCMeasureUnit entity) {

		if (!this.CheckUnitNameSame(entity.getUnitName(), entity
				.getEnterpriseCode())) {
			if (entity.getUnitId() == null) {
				entity.setUnitId(bll.getMaxId("bp_c_measure_unit", "unit_id"));
				entity.setIsUsed("Y");

			}
			entityManager.persist(entity);
			return Integer.parseInt(entity.getUnitId().toString());

		} else

		{
			return -1;
		}
	}

	public void delete(Long unitId) {
		BpCMeasureUnit entity = this.findById(unitId);
		entity.setIsUsed("N");
		this.update(entity);
	}

	public boolean update(BpCMeasureUnit entity) {
		try {
			if (!this.CheckUnitNameSame(entity.getUnitName(), entity
					.getEnterpriseCode(), entity.getUnitId())) {
				entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return true;
			} else {
				return false;
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCMeasureUnit findById(Long id) {
		LogUtil.log("finding BpCMeasureUnit instance with id: " + id,
				Level.INFO, null);
		try {
			BpCMeasureUnit instance = entityManager.find(BpCMeasureUnit.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<BpCMeasureUnit> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all BpCMeasureUnit instances", Level.INFO, null);
		try {
			final String queryString = "select model from BpCMeasureUnit model";
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

	public boolean CheckUnitNameSame(String unitName, String enterpriseCode,
			Long... unitId) {
		boolean isSame = false;
		String sql = "select count(1) from bp_c_measure_unit t\n"
				+ "where t.unit_name='" + unitName
				+ "' and t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.is_used='Y'";

		if (unitId != null && unitId.length > 0) {
			sql += "  and t.unit_id <> " + unitId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

	@SuppressWarnings("unchecked")
	public PageObject findUnitList(String fuzzy, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select * from bp_c_measure_unit t\n"
					+ "where (t.unit_name like '%" + fuzzy + "%'\n"
					+ "or t.retrieve_code like '%" + fuzzy + "%')\n"
					+ "and t.enterprise_code='" + enterpriseCode + "'\n"
					+ "and t.is_used='Y'";

			List<BpCMeasureUnit> list = bll.queryByNativeSQL(sql,
					BpCMeasureUnit.class, rowStartIdxAndCount);
			String sqlCount = "select count(*) from bp_c_measure_unit t\n"
					+ "where t.unit_name like '%" + fuzzy + "%'\n"
					+ "and t.retrieve_code like '%" + fuzzy + "%'\n"
					+ "and t.enterprise_code='" + enterpriseCode + "'\n"
					+ "and t.is_used='Y'";

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

}
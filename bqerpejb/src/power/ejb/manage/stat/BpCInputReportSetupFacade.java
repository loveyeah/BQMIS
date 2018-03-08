package power.ejb.manage.stat;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity BpCInputReportSetup.
 * 
 * @see power.ejb.manage.stat.BpCInputReportSetup
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCInputReportSetupFacade implements
		BpCInputReportSetupFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * 保存所选择的时间类型信息
	 * 
	 * @param entity
	 */
	public void save(BpCInputReportSetup entity) {
		LogUtil.log("saving BpCInputReportSetup instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 批量保存所选择的时间类型信息
	 * 
	 * @param list
	 */
	public void save(List<BpCInputReportSetup> list) {
		for (BpCInputReportSetup entity : list) {
			this.save(entity);
		}
	}

	/**
	 * 判断是否更新时间类型
	 * 
	 * @param reportCode
	 * @param enterpriseCode
	 * @return 大于0 表示更新，不大于0 表示新增
	 */
	public Long ifUpdate(String reportCode, String enterpriseCode) {
		Long count;
		String sql = "select count(*) from BP_C_INPUT_REPORT_SETUP t"
				+ "  where t.REPORT_CODE='" + reportCode + "'"
				+ " and t.enterprise_code='" + enterpriseCode + "'";
		count = Long.valueOf(dll.getSingal(sql).toString());

		return count;
	}

	/**
	 * 变更时间类型时清空原时间类型数据
	 * 
	 * @param entity
	 * 
	 */
	public void delete(String reportCode) {
		LogUtil.log("deleting BpCInputReportSetup instance", Level.INFO, null);
		try {
			String sql = "delete from BP_C_INPUT_REPORT_SETUP t where t.REPORT_CODE= '"
					+ reportCode + "'";
			dll.exeNativeSQL(sql);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据报表编码查询对应的报表时间类型信息
	 * 
	 * @param propertyName
	 *            对应的表的列名
	 * @param value
	 *            报表编码
	 * @param rowStartIdxAndCount
	 * @return List 报表时间类型信息
	 */
	@SuppressWarnings("unchecked")
	public List<BpCInputReportSetup> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpCInputReportSetup instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpCInputReportSetup model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

}
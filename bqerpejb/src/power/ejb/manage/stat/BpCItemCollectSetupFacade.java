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
 * 指标时间类型设置
 * 
 * @author ywliu
 * 
 */
@Stateless
public class BpCItemCollectSetupFacade implements
		BpCItemCollectSetupFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * 批量保存所设置的时间
	 * 
	 * @param entity
	 */
	public void save(List<BpCItemCollectSetup> list) {
		for (BpCItemCollectSetup entity : list) {
			this.save(entity);
		}
	}

	/**
	 * 保存一条时间类型信息
	 * 
	 * @param entity
	 */
	public void save(BpCItemCollectSetup entity) {
		LogUtil.log("saving BpCItemCollectSetup instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 判断是否更新
	 * 
	 * @param itemCode
	 * @param enterpriseCode
	 * @return Long 大于0 表示更新，小于0表示新增
	 */
	public Long ifUpdate(String itemCode, String enterpriseCode) {
		Long count;
		String sql = "select count(*) from bp_c_item_collect_setup t"
				+ "  where t.item_code='" + itemCode + "'"
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
	public Boolean delete(String itemCode) {
		LogUtil.log("deleting BpCItemCollectSetup instance", Level.INFO, null);
		try {
			String sql = "delete from bp_c_item_collect_setup t where t.item_code= '"
					+ itemCode + "'";
			int exeRow = dll.exeNativeSQL(sql);
			if (exeRow > 0) {
				LogUtil.log("delete successful", Level.INFO, null);
				return true;
			} else {
				return false;
			}

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
	 * @return List 报表时间类型信息列表
	 */
	@SuppressWarnings("unchecked")
	public List<BpCItemCollectSetup> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpCItemCollectSetup instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpCItemCollectSetup model where model."
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
/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
 * Facade for entity HrCStatitem.
 * 
 * @see power.ejb.hr.HrCStatitem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCStatitemFacade implements HrCStatitemFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/** 是使用("Y") */
	private static String STR_IS_USE_Y = "Y";
	/** 合计项类型:加班 code: "1" */
	private static String STR_TYPE_CODE_1 = "1";
	/** 合计项类型:运行班 code: "2" */
	private static String STR_TYPE_CODE_2 = "2";
	/** 合计项类型:请假 code: "3" */
	private static String STR_TYPE_CODE_3 = "3";

	/**
	 * Perform an initial save of a previously unsaved HrCStatitem entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCStatitem entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCStatitem entity) {
		LogUtil.log("saving HrCStatitem instance", Level.INFO, null);
		try {
			// 获得流水号
			Long statId = bll.getMaxId("HR_C_STATITEM", "STAT_ID");
			// 设定流水号
			entity.setStatId(statId);
			// 设定修改时间
			entity.setLastModifiyDate(new Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCStatitem entity.
	 * 
	 * @param entity
	 *            HrCStatitem entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCStatitem entity) {
		LogUtil.log("deleting HrCStatitem instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCStatitem.class, entity
					.getStatId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCStatitem entity and return it or a copy of
	 * it to the sender. A copy of the HrCStatitem entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            HrCStatitem entity to update
	 * @return HrCStatitem the persisted HrCStatitem entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCStatitem update(HrCStatitem entity) {
		LogUtil.log("updating HrCStatitem instance", Level.INFO, null);
		try {
			// 设定修改时间
			entity.setLastModifiyDate(new Date());
			HrCStatitem result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCStatitem findById(Long id) {
		LogUtil.log("finding HrCStatitem instance with id: " + id, Level.INFO,
				null);
		try {
			HrCStatitem instance = entityManager.find(HrCStatitem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCStatitem entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCStatitem property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCStatitem> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCStatitem> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCStatitem instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCStatitem model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCStatitem entities.
	 * 
	 * @return List<HrCStatitem> all HrCStatitem entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCStatitem> findAll() {
		LogUtil.log("finding all HrCStatitem instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCStatitem model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 获得考勤合计项信息
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getStatItemList(String enterpriseCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			StringBuilder sql = new StringBuilder();
			StringBuilder sqlCount = new StringBuilder();
			sql.append(" select * from HR_C_STATITEM t ");
			sql.append(" where t.IS_USE = ? and t.ENTERPRISE_CODE = ? ");
			sql.append(" ORDER BY t.STAT_ITEM_TYPE, t.STAT_ITEM_ID ");
			sqlCount
					.append(" select count(distinct t.STAT_ID) from HR_C_STATITEM t ");
			sqlCount.append(" where t.IS_USE = ? and t.ENTERPRISE_CODE = ? ");

			// 查询参数数组
			Object[] params = new Object[2];
			params[0] = STR_IS_USE_Y;
			params[1] = enterpriseCode;
			LogUtil.log("EJB:考勤合计项维护初始化开始。", Level.INFO, null);
			LogUtil.log("EJB:SQL= " + sql.toString(), Level.INFO, null);
			List<StatItemNameInfo> arrList = bll
					.queryByNativeSQL(sql.toString(), params,
							HrCStatitem.class, rowStartIdxAndCount);
			// 按查询结果集设置返回结果
			if (arrList.size() == 0) {
				// 设置查询结果集
				result.setList(null);
				// 设置行数
				result.setTotalCount(Long.parseLong("0"));
			} else {
				result.setList(arrList);
				result.setTotalCount(Long.parseLong(bll.getSingal(
						sqlCount.toString(), params).toString()));
			}
			LogUtil.log("EJB:考勤合计项维护初始化结束。", Level.INFO, null);
			return result;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:考勤合计项维护初始化失败。", Level.SEVERE, e);
			throw e;
		}
	}

	/**
	 * 通过合计项类型code获得合计项名称list
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param typeCode
	 *            合计项类型code
	 * @return PageObject
	 */
	@SuppressWarnings( { "unchecked", "unchecked" })
	public PageObject getStatNameList(String enterpriseCode, String typeCode) {
		try {
			LogUtil.log("EJB:下拉框列表[合计项名称]初始化开始。", Level.SEVERE, null);
			PageObject result = new PageObject();
			List<StatItemNameInfo> arrList = new ArrayList<StatItemNameInfo>();

			// 表名:HR_C_OVERTIME 解释:加班类别维护
			StringBuilder sqlOverTime = new StringBuilder();
			sqlOverTime.append(" select t.OVERTIME_TYPE_ID, ");
			sqlOverTime.append(" t.OVERTIME_TYPE from HR_C_OVERTIME t ");

			// 表名:HR_C_WORKSHIFT 解释:运行班类别维护表
			StringBuilder sqlWorkShift = new StringBuilder();
			sqlWorkShift.append(" select t.WORK_SHIFT_ID, ");
			sqlWorkShift.append(" t.WORK_SHIFT from HR_C_WORKSHIFT t ");

			// 表名:HR_C_VACATIONTYPE 解释:假别编码表
			StringBuilder sqlVacationType = new StringBuilder();
			sqlVacationType.append(" select t.VACATION_TYPE_ID, ");
			sqlVacationType
					.append(" t.VACATION_TYPE from HR_C_VACATIONTYPE t ");

			String sqlWhere = " where t.IS_USE = ?  and t.ENTERPRISE_CODE = ? ";
			// 查询参数数组
			Object[] params = new Object[2];
			params[0] = STR_IS_USE_Y;
			params[1] = enterpriseCode;
			StringBuilder sql = new StringBuilder();
			// 当类别为：加班,运行班,假别时的sql
			if (STR_TYPE_CODE_1.equals(typeCode)) {
				sql = sqlOverTime;
			} else if (STR_TYPE_CODE_2.equals(typeCode)) {
				sql = sqlWorkShift;
			} else if (STR_TYPE_CODE_3.equals(typeCode)) {
				sql = sqlVacationType;
			}

			sql.append(sqlWhere);
			LogUtil.log("下拉框列表[合计项名称]EJB:sql=" + sql, Level.INFO, null);
			List list = bll.queryByNativeSQL(sql.toString(), params);
			Iterator it = list.iterator();
			while (it.hasNext()) {
				StatItemNameInfo statItemNameBeen = new StatItemNameInfo();
				Object[] data = (Object[]) it.next();
				if (data[0] != null) {
					statItemNameBeen.setStatItemId(data[0].toString());
				}
				if (data[1] != null) {
					statItemNameBeen.setStatItemName(data[1].toString());
				}
				arrList.add(statItemNameBeen);
			}

			StatItemNameInfo nullBeen = new StatItemNameInfo();
			// 在第一行增加空行
			nullBeen.setStatItemId("");
			nullBeen.setStatItemName("");
			arrList.add(0, nullBeen);
			result.setList(arrList);
			LogUtil.log("EJB:下拉框列表[合计项名称]初始化结束。", Level.SEVERE, null);
			return result;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:下拉框列表[合计项名称]初始化失败。", Level.SEVERE, e);
			throw e;
		}
	}
}
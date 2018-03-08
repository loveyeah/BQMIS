package power.ejb.hr;

import java.util.ArrayList;
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
import power.ejb.hr.form.DrpCommBeanInfo;

/**
 * Facade for entity HrCIntype.
 *
 * @see power.ejb.hr.HrCIntype
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCIntypeFacade implements HrCIntypeFacadeRemote {
	// property constants
	public static final String IN_TYPE = "inType";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String INSERTBY = "insertby";
	public static final String ORDER_BY = "orderBy";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved HrCIntype entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCIntype entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCIntype entity) {
		LogUtil.log("saving HrCIntype instance", Level.INFO, null);
		try {
			//采番处理
			entity.setInTypeId(bll.getMaxId("HR_C_INTYPE", "IN_TYPE_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCIntype entity.
	 *
	 * @param entity
	 *            HrCIntype entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCIntype entity) {
		LogUtil.log("deleting HrCIntype instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCIntype.class, entity
					.getInTypeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCIntype entity and return it or a copy of it
	 * to the sender. A copy of the HrCIntype entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCIntype entity to update
	 * @return HrCIntype the persisted HrCIntype entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCIntype update(HrCIntype entity) {
		LogUtil.log("updating HrCIntype instance", Level.INFO, null);
		try {
			HrCIntype result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCIntype findById(Long id) {
		LogUtil.log("finding HrCIntype instance with id: " + id, Level.INFO,
				null);
		try {
			HrCIntype instance = entityManager.find(HrCIntype.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCIntype entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCIntype property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCIntype> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCIntype> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCIntype instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCIntype model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrCIntype> findByInType(Object inType) {
		return findByProperty(IN_TYPE, inType);
	}

	public List<HrCIntype> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<HrCIntype> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<HrCIntype> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<HrCIntype> findByInsertby(Object insertby) {
		return findByProperty(INSERTBY, insertby);
	}

	public List<HrCIntype> findByOrderBy(Object orderBy) {
		return findByProperty(ORDER_BY, orderBy);
	}

	/**
	 * Find all HrCIntype entities.
	 *
	 * @return List<HrCIntype> all HrCIntype entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCIntype> findAll() {
		LogUtil.log("查找所有进厂类别id和名称开始。", Level.INFO, null);
		try {
			final String queryString = "select model from HrCIntype model";
			Query query = entityManager.createQuery(queryString);
			LogUtil.log("查找所有进厂类别和名称结束。", Level.INFO, null);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("查找所有进厂类别和名称失败。", Level.INFO, null);
			throw re;
		}
	}

	/**
	 * 按进厂类别ID，是否使用和企业编码检索
	 * @param inTypeId,进厂类别ID
	 * @param isUse ,是否使用
	 * @param enterprisecode,企业编码
	 * @return Boolean
	 */
	public Boolean findByIdIsuseCode(Long inTypeId, String isUse,
			String enterpriseCode) {
		String sql = "select t.* from hr_c_intype t where t.IN_TYPE_ID=? and t.ENTERPRISE_CODE=? and t.IS_USE=?";
		Object[] params = new Object[3];
		params[0] = inTypeId;
		params[1] = enterpriseCode;
		params[2] = isUse;
		List<HrCIntype> instance = bll.queryByNativeSQL(sql, params);
		if (instance.size() == 0)
			return false;
		return true;
	}

	/**
	 * 查询所有员工进厂类别
	 * @param isUse ,是否使用
	 * @param enterprisecode,企业编码
	 * @param rowStartIdxAndCount,分页
	 * @return List<HrCInType>
	 */
	@SuppressWarnings("unchecked")
	public List<HrCIntype> queryAllInType(String isUse, String enterpriseCode,int... rowStartIdxAndCount) {
		try {
			LogUtil.log("查找所有进厂类别id和名称开始。", Level.INFO, null);
			String sql = "select t.* from hr_c_intype t where t.ENTERPRISE_CODE=? and t.IS_USE=? order by t.order_by";
			Object[] params = new Object[2];
			params[0] = enterpriseCode;
			params[1] = isUse;
			List<HrCIntype> list = bll.queryByNativeSQL(sql, params,
					HrCIntype.class,rowStartIdxAndCount);
			LogUtil.log("查找所有进厂类别id和名称结束。", Level.INFO, null);
			return list;
		} catch (RuntimeException re) {
			LogUtil.log("查找所有进厂类别id和名称失败。", Level.INFO, null);
			throw re;
		}
	}
	/**
	 * 查找所有进厂类别
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllInTypes(String enterpriseCode){
		try{
			String sql = "SELECT I.IN_TYPE_ID,I.IN_TYPE  FROM HR_C_INTYPE I " +
					"  WHERE I.IS_USE = ? AND I.ENTERPRISE_CODE = ?";
			LogUtil.log("所有进厂类别id和名称开始。", Level.INFO, null);
			List list = bll.queryByNativeSQL(sql,new Object[]{"Y",enterpriseCode});
			List<DrpCommBeanInfo> arraylist = new ArrayList<DrpCommBeanInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DrpCommBeanInfo model = new DrpCommBeanInfo();
				if (data[0] != null) {
					model.setId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					model.setText(data[1].toString());
				}
				arraylist.add(model);
			}
			PageObject result = new PageObject();
			result.setList(arraylist);
			LogUtil.log("查找所有进厂类别id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有进厂类别id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}
}
/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.DataChangeException;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 加班统计方法类
 * 
 * @author zhouxu
 * @version 1.0
 */
@Stateless
public class HrDOvertimetotalFacade implements HrDOvertimetotalFacadeRemote {
    // property constants
    public static final String DEPT_ID = "deptId";
    public static final String DAYS = "days";
    public static final String LAST_MODIFIY_BY = "lastModifiyBy";
    public static final String IS_USE = "isUse";
    public static final String ENTERPRISE_CODE = "enterpriseCode";
    public static final String ISUSE_Y = "Y";
	/** 日期形式字符串 DATE_FORMAT_YYYYMMDD_HHMM */
	private static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

    @PersistenceContext
    private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

    /**
     * Perform an initial save of a previously unsaved HrDOvertimetotal entity.
     * All subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            HrDOvertimetotal entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrDOvertimetotal entity) {
        LogUtil.log("saving HrDOvertimetotal instance", Level.INFO, null);
        try {
            entityManager.persist(entity);
            LogUtil.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Delete a persistent HrDOvertimetotal entity.
     * 
     * @param entity
     *            HrDOvertimetotal entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrDOvertimetotal entity) {
        LogUtil.log("deleting HrDOvertimetotal instance", Level.INFO, null);
        try {
            entity = entityManager.getReference(HrDOvertimetotal.class, entity.getId());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 更新一个加班表bean
     * 
     * @param entity
     *            HrDOvertimetotal 加班表实体
     * @return HrDOvertimetotal 
     *            HrDOvertimetotal 成功后的实体
     * @throws RuntimeException
     *             操作失败的例外
     * @throws DataChangeException
     *             排他例外
     */
    public HrDOvertimetotal update(HrDOvertimetotal entity)
    throws DataChangeException {
		LogUtil.log("EJB:更新加班统计信息开始。", Level.INFO, null);
		try {
			HrDOvertimetotal lastBean = this.findById(
					entity.getId(), entity.getEnterpriseCode());
			HrDOvertimetotal result;
			SimpleDateFormat sdfFrom = new SimpleDateFormat(
					DATE_FORMAT_YYYYMMDD_HHMMSS);
			String dtNowTime = sdfFrom.format(lastBean.getLastModifiyDate());
			String dtOldTime = sdfFrom.format(entity.getLastModifiyDate());
			if (dtNowTime.equals(dtOldTime)) {
				// 排他成功
			    lastBean.setDeptId(entity.getDeptId());
				lastBean.setDays(entity.getDays());
				lastBean.setLastModifiyBy(entity.getLastModifiyBy());
				lastBean.setLastModifiyDate(new Date());
				lastBean.setIsUse(entity.getIsUse());
				lastBean.setAttendanceDeptId(entity.getAttendanceDeptId());
				result = entityManager.merge(lastBean);
			} else {
				// 排他失败
				throw new DataChangeException(null);
			}
			LogUtil.log("EJB:更新加班统计信息结束。", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:更新加班统计信息失败。", Level.SEVERE, re);
			throw re;
		} catch (DataChangeException e) {
			LogUtil.log("EJB:更新加班统计信息失败。", Level.SEVERE, e);
			throw e;
		}
    }

    /**
     * 由ID获得实体
     * 
     * @param id 加班ID
     * @param enterpriseCode 企业编码
     * 
     * @return HrDOvertimetotal 加班实体
     */
    @SuppressWarnings("unchecked")
	public HrDOvertimetotal findById(HrDOvertimetotalId id, String enterpriseCode) {
		LogUtil.log("EJB:按加班id查找加班信息开始", Level.INFO, null);
		try {
			// 查询sql
			HrDOvertimetotal tempBean = new HrDOvertimetotal();
			String sql = "SELECT * " + 
				"FROM HR_D_OVERTIMETOTAL A " + 
				"WHERE A.ATTENDANCE_YEAR = ?  " + 
				    "AND A.ATTENDANCE_MONTH = ?  " + 
				    "AND A.EMP_ID = ?  " + 
				    "AND A.OVERTIME_TYPE_ID = ? " + 
				    "AND A.IS_USE = ? " + 
				    "AND A.ENTERPRISE_CODE = ? ";
			LogUtil.log("SQL:" + sql, Level.INFO, null);
			List<HrDOvertimetotal> list = bll.queryByNativeSQL(sql,
					new Object[] {id.getAttendanceYear(),
					id.getAttendanceMonth(), id.getEmpId(), 
					id.getOvertimeTypeId(), ISUSE_Y, 
					enterpriseCode },
					HrDOvertimetotal.class);
			if (null == list || 0 == list.size()) {
				tempBean = null;
			} else {
				tempBean = list.get(0);
			}
			LogUtil.log("EJB:按加班id查找加班信息结束", Level.INFO, null);
			return tempBean;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:按加班id查找加班信息错误", Level.SEVERE, e);
			throw e;
		}
	}

    /**
     * Find all HrDOvertimetotal entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrDOvertimetotal property to query
     * @param value
     *            the property value to match
     * @return List<HrDOvertimetotal> found by query
     */
    @SuppressWarnings("unchecked")
    public List<HrDOvertimetotal> findByProperty(String propertyName, final Object value) {
        LogUtil.log("finding HrDOvertimetotal instance with property: " + propertyName + ", value: " + value,
                Level.INFO, null);
        try {
            final String queryString = "select model from HrDOvertimetotal model where model." + propertyName
                    + "= :propertyValue";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find by property name failed", Level.SEVERE, re);
            throw re;
        }
    }

    public List<HrDOvertimetotal> findByDeptId(Object deptId) {
        return findByProperty(DEPT_ID, deptId);
    }

    public List<HrDOvertimetotal> findByDays(Object days) {
        return findByProperty(DAYS, days);
    }

    public List<HrDOvertimetotal> findByLastModifiyBy(Object lastModifiyBy) {
        return findByProperty(LAST_MODIFIY_BY, lastModifiyBy);
    }

    public List<HrDOvertimetotal> findByIsUse(Object isUse) {
        return findByProperty(IS_USE, isUse);
    }

    public List<HrDOvertimetotal> findByEnterpriseCode(Object enterpriseCode) {
        return findByProperty(ENTERPRISE_CODE, enterpriseCode);
    }

    /**
     * Find all HrDOvertimetotal entities.
     * 
     * @return List<HrDOvertimetotal> all HrDOvertimetotal entities
     */
    @SuppressWarnings("unchecked")
    public List<HrDOvertimetotal> findAll() {
        LogUtil.log("finding all HrDOvertimetotal instances", Level.INFO, null);
        try {
            final String queryString = "select model from HrDOvertimetotal model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }

}
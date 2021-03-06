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
 * 运行班统计方法实现
 * 
 * @author huangweijie
 * @version 1.0
 */
@Stateless
public class HrDWorkshifttotalFacade implements HrDWorkshifttotalFacadeRemote {
    // property constants
    public static final String DEPT_ID = "deptId";
    public static final String DAYS = "days";
    public static final String MONEY = "money";
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
     * Perform an initial save of a previously unsaved HrDWorkshifttotal entity.
     * All subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            HrDWorkshifttotal entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrDWorkshifttotal entity) {
        LogUtil.log("saving HrDWorkshifttotal instance", Level.INFO, null);
        try {
            entityManager.persist(entity);
            LogUtil.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Delete a persistent HrDWorkshifttotal entity.
     * 
     * @param entity
     *            HrDWorkshifttotal entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrDWorkshifttotal entity) {
        LogUtil.log("deleting HrDWorkshifttotal instance", Level.INFO, null);
        try {
            entity = entityManager.getReference(HrDWorkshifttotal.class, entity.getId());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 运行班实体更新方法
     * 
     * @param entity
     *            运行班实体
     * @return 成功后的实体
     * @throws RuntimeException
     *             运行时错误异常
     * @throws DataChangeException 
     *             排他异常
     */
    public HrDWorkshifttotal update(HrDWorkshifttotal entity) throws DataChangeException {
		LogUtil.log("EJB:更新运行班统计信息开始。", Level.INFO, null);
		try {
			HrDWorkshifttotal lastBean = this.findById(
					entity.getId(), entity.getEnterpriseCode());
			HrDWorkshifttotal result;
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
				result = entityManager.merge(lastBean);
			} else {
				// 排他失败
				throw new DataChangeException(null);
			}
			LogUtil.log("EJB:更新运行班统计信息结束。", Level.INFO, null);
			return result;
		} catch (DataChangeException e) {
			LogUtil.log("EJB:更新运行班统计信息失败。", Level.SEVERE, e);
			throw e;
		} catch (RuntimeException re) {
            LogUtil.log("EJB:更新运行班统计信息失败。", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 由运行班统计ID获得运行班统计实体
     * 
     * @param id 运行班统计ID
     * @param enterpriseCode 企业编码
     * 
     * @return 运行班统计实体
     */
    @SuppressWarnings("unchecked")
	public HrDWorkshifttotal findById(HrDWorkshifttotalId id, String enterpriseCode) {
		LogUtil.log("EJB:按运行班id查找运行班信息开始", Level.INFO, null);
		try {
			// 查询sql
			HrDWorkshifttotal tempBean = new HrDWorkshifttotal();
			String sql = "SELECT * " + 
				"FROM HR_D_WORKSHIFTTOTAL A " + 
				"WHERE A.ATTENDANCE_YEAR = ?  " + 
				    "AND A.ATTENDANCE_MONTH = ?  " + 
				    "AND A.EMP_ID = ?  " + 
				    "AND A.WORK_SHIFT_ID = ? " + 
				    "AND A.IS_USE = ? " + 
				    "AND A.ENTERPRISE_CODE = ? ";
			LogUtil.log("SQL:" + sql, Level.INFO, null);
			List<HrDWorkshifttotal> list = bll.queryByNativeSQL(sql,
					new Object[] {id.getAttendanceYear(),
					id.getAttendanceMonth(), id.getEmpId(), 
					id.getWorkShiftId(), ISUSE_Y, 
					enterpriseCode},
					HrDWorkshifttotal.class);
			if (null == list || 0 == list.size()) {
				tempBean = null;
			} else {
				tempBean = list.get(0);
			}
			LogUtil.log("EJB:按运行班id查找运行班信息结束", Level.INFO, null);
			return tempBean;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:按运行班id查找运行班信息错误", Level.SEVERE, e);
			throw e;
		}
	}

    /**
     * Find all HrDWorkshifttotal entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrDWorkshifttotal property to query
     * @param value
     *            the property value to match
     * @return List<HrDWorkshifttotal> found by query
     */
    @SuppressWarnings("unchecked")
    public List<HrDWorkshifttotal> findByProperty(String propertyName, final Object value) {
        LogUtil.log("finding HrDWorkshifttotal instance with property: " + propertyName + ", value: " + value,
                Level.INFO, null);
        try {
            final String queryString = "select model from HrDWorkshifttotal model where model." + propertyName
                    + "= :propertyValue";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find by property name failed", Level.SEVERE, re);
            throw re;
        }
    }

    public List<HrDWorkshifttotal> findByDeptId(Object deptId) {
        return findByProperty(DEPT_ID, deptId);
    }

    public List<HrDWorkshifttotal> findByDays(Object days) {
        return findByProperty(DAYS, days);
    }

    public List<HrDWorkshifttotal> findByMoney(Object money) {
        return findByProperty(MONEY, money);
    }

    public List<HrDWorkshifttotal> findByLastModifiyBy(Object lastModifiyBy) {
        return findByProperty(LAST_MODIFIY_BY, lastModifiyBy);
    }

    public List<HrDWorkshifttotal> findByIsUse(Object isUse) {
        return findByProperty(IS_USE, isUse);
    }

    public List<HrDWorkshifttotal> findByEnterpriseCode(Object enterpriseCode) {
        return findByProperty(ENTERPRISE_CODE, enterpriseCode);
    }

    /**
     * Find all HrDWorkshifttotal entities.
     * 
     * @return List<HrDWorkshifttotal> all HrDWorkshifttotal entities
     */
    @SuppressWarnings("unchecked")
    public List<HrDWorkshifttotal> findAll() {
        LogUtil.log("finding all HrDWorkshifttotal instances", Level.INFO, null);
        try {
            final String queryString = "select model from HrDWorkshifttotal model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }

}
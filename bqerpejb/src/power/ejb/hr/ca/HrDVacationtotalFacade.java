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
 * 加班统计方法实现
 * 
 * @author huangweijie
 * @version 1.0
 */
@Stateless
public class HrDVacationtotalFacade implements HrDVacationtotalFacadeRemote {
    // property constants
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
     * Perform an initial save of a previously unsaved HrDVacationtotal entity.
     * All subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            HrDVacationtotal entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrDVacationtotal entity) {
        LogUtil.log("saving HrDVacationtotal instance", Level.INFO, null);
        try {
            entityManager.persist(entity);
            LogUtil.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Delete a persistent HrDVacationtotal entity.
     * 
     * @param entity
     *            HrDVacationtotal entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrDVacationtotal entity) {
        LogUtil.log("deleting HrDVacationtotal instance", Level.INFO, null);
        try {
            entity = entityManager.getReference(HrDVacationtotal.class, entity.getId());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 请假实体更新方法
     * 
     * @param entity
     *            请假实体
     * @return 成功后的实体
     * @throws RuntimeException
     *             运行时错误异常
     * @throws DataChangeException 
     *             排他异常
     */
    public HrDVacationtotal update(HrDVacationtotal entity) 
    throws DataChangeException{
		LogUtil.log("EJB:更新请假统计信息开始。", Level.INFO, null);
		try {
			HrDVacationtotal lastBean = this.findById(
					entity.getId(), entity.getEnterpriseCode());
			HrDVacationtotal result;
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
			LogUtil.log("EJB:更新请假统计信息结束。", Level.INFO, null);
			return result;
		} catch (DataChangeException e) {
			LogUtil.log("EJB:更新请假统计信息失败。", Level.SEVERE, e);
			throw e;
		} catch (RuntimeException re) {
            LogUtil.log("EJB:更新请假统计信息失败。", Level.SEVERE, re);
            throw re;
        } 
    }

    /**
     * 由请假统计ID获得请假统计实体
     * 
     * @param id 请假统计ID
     * @param enterpriseCode 企业编码
     * 
     * @return 请假统计实体
     */
    @SuppressWarnings("unchecked")
	public HrDVacationtotal findById(HrDVacationtotalId id, String enterpriseCode) {
		LogUtil.log("EJB:按请假id查找请假信息开始", Level.INFO, null);
		try {
			// 查询sql
			HrDVacationtotal tempBean = new HrDVacationtotal();
			String sql = "SELECT * " + 
				"FROM HR_D_VACATIONTOTAL A " + 
				"WHERE A.ATTENDANCE_YEAR = ?  " + 
				    "AND A.ATTENDANCE_MONTH = ?  " + 
				    "AND A.EMP_ID = ?  " + 
				    "AND A.VACATION_TYPE_ID = ? " + 
				    "AND A.IS_USE = ? " + 
				    "AND A.ENTERPRISE_CODE = ? ";
			LogUtil.log("SQL:" + sql, Level.INFO, null);
			List<HrDVacationtotal> list = bll.queryByNativeSQL(sql,
					new Object[] {id.getAttendanceYear(),
					id.getAttendanceMonth(), id.getEmpId(), 
					id.getVacationTypeId(), ISUSE_Y, 
					enterpriseCode },
					HrDVacationtotal.class);
			if (null == list || 0 == list.size()) {
				tempBean = null;
			} else {
				tempBean = list.get(0);
			}
			LogUtil.log("EJB:按请假id查找请假信息结束", Level.INFO, null);
			return tempBean;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:按请假id查找请假信息错误", Level.SEVERE, e);
			throw e;
		}
	}

    /**
     * Find all HrDVacationtotal entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrDVacationtotal property to query
     * @param value
     *            the property value to match
     * @return List<HrDVacationtotal> found by query
     */
    @SuppressWarnings("unchecked")
    public List<HrDVacationtotal> findByProperty(String propertyName, final Object value) {
        LogUtil.log("finding HrDVacationtotal instance with property: " + propertyName + ", value: " + value,
                Level.INFO, null);
        try {
            final String queryString = "select model from HrDVacationtotal model where model." + propertyName
                    + "= :propertyValue";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find by property name failed", Level.SEVERE, re);
            throw re;
        }
    }

    public List<HrDVacationtotal> findByDays(Object days) {
        return findByProperty(DAYS, days);
    }

    public List<HrDVacationtotal> findByLastModifiyBy(Object lastModifiyBy) {
        return findByProperty(LAST_MODIFIY_BY, lastModifiyBy);
    }

    public List<HrDVacationtotal> findByIsUse(Object isUse) {
        return findByProperty(IS_USE, isUse);
    }

    public List<HrDVacationtotal> findByEnterpriseCode(Object enterpriseCode) {
        return findByProperty(ENTERPRISE_CODE, enterpriseCode);
    }

    /**
     * Find all HrDVacationtotal entities.
     * 
     * @return List<HrDVacationtotal> all HrDVacationtotal entities
     */
    @SuppressWarnings("unchecked")
    public List<HrDVacationtotal> findAll() {
        LogUtil.log("finding all HrDVacationtotal instances", Level.INFO, null);
        try {
            final String queryString = "select model from HrDVacationtotal model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }

}
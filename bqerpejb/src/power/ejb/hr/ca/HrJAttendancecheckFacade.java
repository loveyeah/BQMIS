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
 * 考勤审核操作方法类
 * 
 * @author zhouxu
 */
@Stateless
public class HrJAttendancecheckFacade implements HrJAttendancecheckFacadeRemote {
    // property constants
    public static final String DEP_CHARGE1 = "depCharge1";
    public static final String DEP_CHARGE2 = "depCharge2";
    public static final String DEP_CHARGE3 = "depCharge3";
    public static final String DEP_CHARGE4 = "depCharge4";
    public static final String LAST_MODIFIY_BY = "lastModifiyBy";
    public static final String IS_USE = "isUse";
    public static final String ENTERPRISE_CODE = "enterpriseCode";

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    /** 日期形式字符串 DATE_FORMAT_YYYYMMDD_HHMM */
    private static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

    /**
     * Perform an initial save of a previously unsaved HrJAttendancecheck
     * entity. All subsequent persist actions of this entity should use the
     * #update() method.
     * 
     * @param entity
     *            HrJAttendancecheck entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrJAttendancecheck entity) {
        LogUtil.log("saving HrJAttendancecheck instance", Level.INFO, null);
        try {
            entityManager.persist(entity);
            LogUtil.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Delete a persistent HrJAttendancecheck entity.
     * 
     * @param entity
     *            HrJAttendancecheck entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrJAttendancecheck entity) {
        LogUtil.log("deleting HrJAttendancecheck instance", Level.INFO, null);
        try {
            entity = entityManager.getReference(HrJAttendancecheck.class, entity.getId());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 更新考勤审核
     * 
     * @param entity
     *            需要被更新的bean的内容
     * @throws RuntimeException
     *             更新失败
     * @throws DataChangeException
     *             排他失败
     */
    public HrJAttendancecheck update(HrJAttendancecheck entity) throws DataChangeException {
        LogUtil.log("updating HrJAttendancecheck instance", Level.INFO, null);
        try {
            HrJAttendancecheck lastBean = this.findById(entity.getId(), entity.getEnterpriseCode());
            SimpleDateFormat sdfFrom = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD_HHMMSS);
            String dtNowTime = sdfFrom.format(lastBean.getLastModifiyDate());
            String dtOldTime = sdfFrom.format(entity.getLastModifiyDate());
            if (dtNowTime.equals(dtOldTime)) {
                // 排他成功
                entity.setLastModifiyDate(new Date());
                entityManager.merge(entity);
            } else {
                // 排他失败
                throw new DataChangeException(null);
            }
            HrJAttendancecheck result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (DataChangeException e) {
            throw e;
        } catch (RuntimeException re) {
            LogUtil.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 根据id查询考勤审核记录
     * 
     * @param id
     * @param enterpriseCode
     * @return
     */
    @SuppressWarnings("unchecked")
    public HrJAttendancecheck findById(HrJAttendancecheckId id, String enterpriseCode) {
        LogUtil.log("EJB:根据ID查找考勤审核记录开始。", Level.INFO, null);
        try {
            HrJAttendancecheck bean = new HrJAttendancecheck();
            // sql
            StringBuffer sbSql = new StringBuffer();
            sbSql.append("SELECT * ");
            sbSql.append("FROM ");
            sbSql.append("  HR_J_ATTENDANCECHECK A ");
            sbSql.append("WHERE ");
            sbSql.append("  A.ATTENDANCE_YEAR = ? AND ");
            sbSql.append("  A.ATTENDANCE_MONTH = ? AND ");
            sbSql.append("  A.ATTENDANCE_DEP = ? AND ");
            sbSql.append("  A.IS_USE = ? AND ");
            sbSql.append("  A.ENTERPRISE_CODE = ? ");
            // 查询
            List<HrJAttendancecheck> list = bll.queryByNativeSQL(sbSql.toString(), new Object[] {
                    id.getAttendanceYear(), id.getAttendanceMonth(), id.getAttendanceDep(), "Y", enterpriseCode },
                    HrJAttendancecheck.class);
            if (list.size()==0) {
                bean = null;
            } else {
                bean = list.get(0);
            }
            LogUtil.log("EJB:根据ID查找考勤审核记录结束。", Level.INFO, null);
            return bean;
        } catch (RuntimeException re) {
            LogUtil.log("EJB:根据ID查找考勤审核记录失败。", Level.SEVERE, null);
            throw re;
        }
    }

    /**
     * Find all HrJAttendancecheck entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrJAttendancecheck property to query
     * @param value
     *            the property value to match
     * @return List<HrJAttendancecheck> found by query
     */
    @SuppressWarnings("unchecked")
    public List<HrJAttendancecheck> findByProperty(String propertyName, final Object value) {
        LogUtil.log("finding HrJAttendancecheck instance with property: " + propertyName + ", value: " + value,
                Level.INFO, null);
        try {
            final String queryString = "select model from HrJAttendancecheck model where model." + propertyName
                    + "= :propertyValue";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find by property name failed", Level.SEVERE, re);
            throw re;
        }
    }

    public List<HrJAttendancecheck> findByDepCharge1(Object depCharge1) {
        return findByProperty(DEP_CHARGE1, depCharge1);
    }

    public List<HrJAttendancecheck> findByDepCharge2(Object depCharge2) {
        return findByProperty(DEP_CHARGE2, depCharge2);
    }

    public List<HrJAttendancecheck> findByDepCharge3(Object depCharge3) {
        return findByProperty(DEP_CHARGE3, depCharge3);
    }

    public List<HrJAttendancecheck> findByDepCharge4(Object depCharge4) {
        return findByProperty(DEP_CHARGE4, depCharge4);
    }

    public List<HrJAttendancecheck> findByLastModifiyBy(Object lastModifiyBy) {
        return findByProperty(LAST_MODIFIY_BY, lastModifiyBy);
    }

    public List<HrJAttendancecheck> findByIsUse(Object isUse) {
        return findByProperty(IS_USE, isUse);
    }

    public List<HrJAttendancecheck> findByEnterpriseCode(Object enterpriseCode) {
        return findByProperty(ENTERPRISE_CODE, enterpriseCode);
    }

    /**
     * Find all HrJAttendancecheck entities.
     * 
     * @return List<HrJAttendancecheck> all HrJAttendancecheck entities
     */
    @SuppressWarnings("unchecked")
    public List<HrJAttendancecheck> findAll() {
        LogUtil.log("finding all HrJAttendancecheck instances", Level.INFO, null);
        try {
            final String queryString = "select model from HrJAttendancecheck model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }

}
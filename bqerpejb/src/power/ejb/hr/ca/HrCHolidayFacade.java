package power.ejb.hr.ca;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity HrCHoliday.
 * 
 * @see power.ejb.hr.ca.HrCHoliday
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCHolidayFacade implements HrCHolidayFacadeRemote {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    /** 是否使用 */
    private String IS_USE_Y = "Y";
    /** 空 */
    private static String STR_NULL = "";

    /**
     * Perform an initial save of a previously unsaved HrCHoliday entity. All
     * subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            HrCHoliday entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrCHoliday entity) {
        LogUtil.log("节假日维护新增开始", Level.INFO, null);
        try {
            // 获得流水号
            Long holidayId = bll.getMaxId("HR_C_HOLIDAY", "HOLIDAY_ID");
            // 设定流水号
            entity.setHolidayId(holidayId);
            // 设定修改时间
            entity.setLastModifiyDate(new Date());
            entityManager.persist(entity);
            LogUtil.log("节假日维护新增结束", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("节假日维护新增失败", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Delete a persistent HrCHoliday entity.
     * 
     * @param entity
     *            HrCHoliday entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrCHoliday entity) {
        LogUtil.log("deleting HrCHoliday instance", Level.INFO, null);
        try {
            entity = entityManager.getReference(HrCHoliday.class, entity
                    .getHolidayId());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Persist a previously saved HrCHoliday entity and return it or a copy of
     * it to the sender. A copy of the HrCHoliday entity parameter is returned
     * when the JPA persistence mechanism has not previously been tracking the
     * updated entity.
     * 
     * @param entity
     *            HrCHoliday entity to update
     * @return HrCHoliday the persisted HrCHoliday entity instance, may not be
     *         the same
     * @throws RuntimeException
     *             if the operation fails
     */
    public HrCHoliday update(HrCHoliday entity) {
        LogUtil.log("EJB:节假日维护更新开始", Level.INFO, null);
        try {
            // 设定修改时间
            entity.setLastModifiyDate(new Date());
            HrCHoliday result = entityManager.merge(entity);
            LogUtil.log("EJB:节假日维护更新结束", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("EJB:节假日维护更新失败", Level.SEVERE, re);
            throw re;
        }
    }

    public HrCHoliday findById(Long id) {
        LogUtil.log("finding HrCHoliday instance with id: " + id, Level.INFO,
                null);
        try {
            HrCHoliday instance = entityManager.find(HrCHoliday.class, id);
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Find all HrCHoliday entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrCHoliday property to query
     * @param value
     *            the property value to match
     * @return List<HrCHoliday> found by query
     */
    @SuppressWarnings("unchecked")
    public List<HrCHoliday> findByProperty(String propertyName,
            final Object value) {
        LogUtil.log("finding HrCHoliday instance with property: "
                + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from HrCHoliday model where model."
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
     * Find all HrCHoliday entities.
     * 
     * @return List<HrCHoliday> all HrCHoliday entities
     */
    @SuppressWarnings("unchecked")
    public List<HrCHoliday> findAll() {
        LogUtil.log("finding all HrCHoliday instances", Level.INFO, null);
        try {
            final String queryString = "select model from HrCHoliday model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 节假日天数获取
     * 
     * @param argStartDate
     *            开始日期
     * @param argEndDate
     *            结束日期
     * @param strEnterpriseCode
     *            企业代码
     * @return 节假日天数
     * @throws SQLException
     */
    public long getHolidayDays(String argStartDate, String argEndDate,
            String strEnterpriseCode) throws SQLException {
        LogUtil.log("EJB:节假日天数获取正常开始", Level.INFO, null);
        long totalCount = 0;
        try {
            // 查询sql
            StringBuffer sbd = new StringBuffer();
            // SELECT文
            sbd.append("SELECT ");
            sbd.append("COUNT(A.HOLIDAY_ID) ");

            // FROM文
            sbd.append("FROM HR_C_HOLIDAY A ");
            sbd.append("WHERE ");
            sbd.append("A.ENTERPRISE_CODE = ? ");
            sbd.append("AND A.IS_USE = ? ");
            sbd.append("AND TO_CHAR(A.HOLIDAY_DATE,'yyyy-mm-dd') >= ? ");
            sbd.append("AND TO_CHAR(A.HOLIDAY_DATE,'yyyy-mm-dd') <= ? ");

            // 查询参数数组
            Object[] params = new Object[4];
            int i = 0;
            params[i++] = strEnterpriseCode;
            params[i++] = IS_USE_Y;
            params[i++] = argStartDate;
            params[i++] = argEndDate;
            totalCount = Long.parseLong(bll.getSingal(sbd.toString(), params)
                    .toString());
            LogUtil.log("EJB: SQL =" + sbd.toString(), Level.INFO, null);
            LogUtil.log("EJB:节假日天数获取正常结束", Level.INFO, null);
        } catch (RuntimeException e) {
            LogUtil.log("EJB:节假日天数获取失败", Level.SEVERE, e);
            throw new SQLException();
        }
        return totalCount;
    }

    /**
     * 查询节假日信息
     * 
     * @param strEnterpriseCode
     *            企业代码
     * @return PageObject 节假日信息
     * @throws SQLException
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public PageObject findHoliday(String strEnterpriseCode)
            throws SQLException, ParseException {
        LogUtil.log("EJB:节假日信息查询正常开始", Level.INFO, null);
        PageObject pobj = new PageObject();
        try {
            // 查询sql
            StringBuffer sbd = new StringBuffer();
            // SELECT文
            sbd.append("SELECT ");
            sbd.append("A.HOLIDAY_ID , ");
            sbd.append("A.HOLIDAY_DATE, ");
            sbd.append("A.HOLIDAY_TYPE ");
            int intLength = sbd.length();

            // FROM文
            sbd.append("FROM HR_C_HOLIDAY A ");
            sbd.append("WHERE ");
            sbd.append("A.ENTERPRISE_CODE = ? ");
            sbd.append("AND A.IS_USE = ? ");

            // 查询参数数组
            Object[] params = new Object[2];
            int i = 0;
            params[i++] = strEnterpriseCode;
            params[i++] = IS_USE_Y;

            // 记录数
            String sqlCount = "SELECT " + " COUNT(A.HOLIDAY_ID) "
                    + sbd.substring(intLength, sbd.length());
            List<HrCHoliday> list = bll
                    .queryByNativeSQL(sbd.toString(), params);
            LogUtil.log("EJB: SQL =" + sbd.toString(), Level.INFO, null);
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
                    .toString());
            LogUtil.log("EJB: SQL =" + sqlCount, Level.INFO, null);
            List<HrCHoliday> arrList = new ArrayList<HrCHoliday>();
            if (list != null) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    HrCHoliday hrCHoliday = new HrCHoliday();
                    Object[] data = (Object[]) it.next();
                    // 节假日id
                    if (data[0] != null) {
                        hrCHoliday.setHolidayId(Long.parseLong(data[0]
                                .toString()));
                    }
                    // 节假日
                    if (data[1] != null) {
                        SimpleDateFormat sdbForm = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        hrCHoliday.setHolidayDate(sdbForm.parse(data[1]
                                .toString()));
                    }
                    // 节假日类别
                    if (data[2] != null) {
                        hrCHoliday.setHolidayType(data[2].toString());
                    }
                    arrList.add(hrCHoliday);
                }
            }
            pobj.setList(arrList);
            pobj.setTotalCount(totalCount);
            LogUtil.log("EJB:节假日信息查询正常结束", Level.INFO, null);
        } catch (RuntimeException e) {
            LogUtil.log("EJB:节假日信息查询失败", Level.SEVERE, e);
            throw new SQLException();
        } catch (ParseException e) {
            LogUtil.log("EJB:节假日信息查询失败", Level.SEVERE, e);
            throw new ParseException("节假日", 0);
        }
        return pobj;
    }

    /**
     * 非节假日，周末上班天数获取
     * 
     * @param argStartDate
     *            开始日期
     * @param argEndDate
     *            结束日期
     * @param strEnterpriseCode
     *            企业代码
     * @return 节假日天数
     * @throws SQLException
     */
    public long getHolidayWeekendDays(String argStartDate, String argEndDate,
            String strEnterpriseCode) throws SQLException {
        LogUtil.log("EJB:非节假日，周末上班天数获取正常开始", Level.INFO, null);
        long totalCount = 0;
        try {
            // 查询sql
            StringBuffer sbd = new StringBuffer();
            // SELECT文
            sbd.append("SELECT ");
            sbd.append("COUNT(A.HOLIDAY_ID) ");

            // FROM文
            sbd.append("FROM HR_C_HOLIDAY A ");
            sbd.append("WHERE ");
            sbd.append("A.ENTERPRISE_CODE = ? ");
            sbd.append("AND A.IS_USE = ? ");
            sbd.append("AND TO_CHAR(A.HOLIDAY_DATE,'yyyy-mm-dd') >= ? ");
            sbd.append("AND TO_CHAR(A.HOLIDAY_DATE,'yyyy-mm-dd') <= ? ");
            sbd.append("AND A.HOLIDAY_TYPE = ? ");

            // 查询参数数组
            Object[] params = new Object[5];
            int i = 0;
            params[i++] = strEnterpriseCode;
            params[i++] = IS_USE_Y;
            params[i++] = argStartDate;
            params[i++] = argEndDate;
            params[i++] = "2";
            totalCount = Long.parseLong(bll.getSingal(sbd.toString(), params)
                    .toString());
            LogUtil.log("EJB: SQL =" + sbd.toString(), Level.INFO, null);
            LogUtil.log("EJB:非节假日，周末上班天数获取正常结束", Level.INFO, null);
        } catch (RuntimeException e) {
            LogUtil.log("EJB:非节假日，周末上班天数获取失败", Level.SEVERE, e);
            throw new SQLException();
        }
        return totalCount;
    }

    /**
     * 查询节假日信息
     * 
     * @param enterpriseCode
     *            企业编码
     * @param dateType
     *            节假日类别
     * @param year
     *            年份
     * @param rowStartIdxAndCount
     * @return PageObject
     * @throws SQLException
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public PageObject getHolidayDateList(String enterpriseCode,
            String dateType, String year, int... rowStartIdxAndCount)
            throws SQLException, ParseException {
        LogUtil.log("EJB:节假日维护查询开始", Level.INFO, null);
        PageObject pobj = new PageObject();
        try {
            // 查询sql
            StringBuffer sql = new StringBuffer();
            StringBuffer sqlCount = new StringBuffer(
                    "SELECT COUNT(A.HOLIDAY_ID) ");
            // SELECT文
            sql.append("SELECT ");
            sql.append("A.HOLIDAY_ID , ");
            sql.append("A.HOLIDAY_DATE, ");
            sql.append("A.HOLIDAY_TYPE ");

            // FROM文
            StringBuffer sqlFrom = new StringBuffer();
            sqlFrom.append("FROM HR_C_HOLIDAY A ");
            sqlFrom.append("WHERE ");
            sqlFrom.append("A.ENTERPRISE_CODE = ? ");
            sqlFrom.append("AND A.IS_USE = ? ");
            sqlFrom.append("AND A.HOLIDAY_TYPE = ? ");
            // 参数个数
            int intCount = 3;
            if (year != null && !STR_NULL.equals(year)) {
                intCount++;
            }
            // 查询参数数组
            Object[] params = new Object[intCount];
            int i = 0;
            params[i++] = enterpriseCode;
            params[i++] = IS_USE_Y;
            params[i++] = dateType;
            // 年度
            if (year != null && !STR_NULL.equals(year)) {
                sqlFrom.append(" AND extract(year from A.HOLIDAY_DATE) = ? ");
                params[i++] = year;
            }
            sqlFrom.append(" ORDER BY A.HOLIDAY_DATE ");
            sql.append(sqlFrom.toString());
            // 记录数
            sqlCount.append(sqlFrom.toString());

            LogUtil.log("EJB: 节假日维护查询SQL =" + sql.toString(), Level.INFO, null);
            List list = bll.queryByNativeSQL(sql.toString(), params,
                    rowStartIdxAndCount);
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount.toString(),
                    params).toString());
            List<HrCHoliday> arrList = new ArrayList<HrCHoliday>();
            if (list != null) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    HrCHoliday hrCHoliday = new HrCHoliday();
                    Object[] data = (Object[]) it.next();
                    // 节假日id
                    if (data[0] != null) {
                        hrCHoliday.setHolidayId(Long.parseLong(data[0]
                                .toString()));
                    }
                    // 节假日
                    if (data[1] != null) {
                        SimpleDateFormat sdbForm = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        hrCHoliday.setHolidayDate(sdbForm.parse(data[1]
                                .toString()));
                    }
                    // 节假日类别
                    if (data[2] != null) {
                        hrCHoliday.setHolidayType(data[2].toString());
                    }
                    arrList.add(hrCHoliday);
                }
            }
            pobj.setList(arrList);
            pobj.setTotalCount(totalCount);
            LogUtil.log("EJB:节假日维护查询结束", Level.INFO, null);
            return pobj;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:节假日维护查询失败", Level.SEVERE, e);
            throw new SQLException();
        } catch (ParseException e) {
            LogUtil.log("EJB:节假日维护查询失败", Level.SEVERE, e);
            throw new ParseException("节假日", 0);
        }
    }

    /**
     * 时间重复check
     * 
     * @param entity
     *            节假日实体
     */
    public PageObject isDateExist(HrCHoliday entity) {
        PageObject result = new PageObject();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 节假日时间
        String holidayDate = format.format(entity.getHolidayDate());
        // 企业编码
        String enterpriseCode = entity.getEnterpriseCode();
        StringBuffer sql = new StringBuffer(
                "SELECT A.HOLIDAY_ID FROM HR_C_HOLIDAY A ");
        StringBuffer sqlCount = new StringBuffer(
                "SELECT COUNT(A.HOLIDAY_ID) FROM HR_C_HOLIDAY A ");
        StringBuffer sqlWhere = new StringBuffer();
        sqlWhere.append(" WHERE A.IS_USE = ? ");
        sqlWhere.append(" AND A.ENTERPRISE_CODE = ? ");
        sqlWhere.append(" AND to_char(A.HOLIDAY_DATE,'yyyy-mm-dd') = ? ");
        sql.append(sqlWhere.toString());
        sqlCount.append(sqlWhere.toString());
        // 查询参数数组
        Object[] params = new Object[3];
        int i = 0;
        params[i++] = IS_USE_Y;
        params[i++] = enterpriseCode;
        params[i++] = holidayDate;
        Long count = Long.parseLong(bll.getSingal(sqlCount.toString(), params)
                .toString());
        result.setTotalCount(count);
        String oldIdStr = STR_NULL;
        if (entity.getHolidayId() != null) {
            oldIdStr = entity.getHolidayId().toString();
        }
        // 判断修改时是否重复
        if (count == 1 && !STR_NULL.equals(oldIdStr)) {
            List list = bll.queryByNativeSQL(sql.toString(), params);
            Iterator it = list.iterator();
            String idStr = STR_NULL;
            if (it.hasNext()) {
                Object data = (Object) it.next();
                idStr = data.toString();
                if (idStr.equals(oldIdStr)) {
                    result.setTotalCount(0L);
                }
            }
        }
        return result;
    }
}
/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.hr.ca;
    
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.CodeConstants;

/**
 * Facade for entity HrCYearplan.
 * 
 * @see power.ejb.hr.ca.HrCYearplan
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCYearplanFacade implements HrCYearplanFacadeRemote {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    /**是否使用*/
    private String IS_USE_Y = "Y";

    /**
     * Perform an initial save of a previously unsaved HrCYearplan entity. All
     * subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            HrCYearplan entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrCYearplan entity) {
        LogUtil.log("saving HrCYearplan instance", Level.INFO, null);
        try {
            entityManager.persist(entity);
            LogUtil.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Delete a persistent HrCYearplan entity.
     * 
     * @param entity
     *            HrCYearplan entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrCYearplan entity) {
        LogUtil.log("deleting HrCYearplan instance", Level.INFO, null);
        try {
            entity = entityManager.getReference(HrCYearplan.class, entity
                    .getId());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Persist a previously saved HrCYearplan entity and return it or a copy of
     * it to the sender. A copy of the HrCYearplan entity parameter is returned
     * when the JPA persistence mechanism has not previously been tracking the
     * updated entity.
     * 
     * @param entity
     *            HrCYearplan entity to update
     * @return HrCYearplan the persisted HrCYearplan entity instance, may not be
     *         the same
     * @throws RuntimeException
     *             if the operation fails
     */
    public HrCYearplan update(HrCYearplan entity) {
        LogUtil.log("updating HrCYearplan instance", Level.INFO, null);
        try {
            HrCYearplan result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    public HrCYearplan findById(HrCYearplanId id) {
        LogUtil.log("finding HrCYearplan instance with id: " + id, Level.INFO,
                null);
        try {
            HrCYearplan instance = entityManager.find(HrCYearplan.class, id);
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Find all HrCYearplan entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrCYearplan property to query
     * @param value
     *            the property value to match
     * @return List<HrCYearplan> found by query
     */
    @SuppressWarnings("unchecked")
    public List<HrCYearplan> findByProperty(String propertyName,
            final Object value) {
        LogUtil.log("finding HrCYearplan instance with property: "
                + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from HrCYearplan model where model."
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
     * Find all HrCYearplan entities.
     * 
     * @return List<HrCYearplan> all HrCYearplan entities
     */
    @SuppressWarnings("unchecked")
    public List<HrCYearplan> findAll() {
        LogUtil.log("finding all HrCYearplan instances", Level.INFO, null);
        try {
            final String queryString = "select model from HrCYearplan model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }
     /**
     * 查询某一部门下的所有员工
     * @param enterpriseCode 企业编码 ,year 年份,deptId 部门id
     * @param rowStartIdxAndCount 分页
     * @return PageObject  查询结果
     */
    @SuppressWarnings({ "unchecked", "unchecked", "unchecked" })
    public HrCYearPlanFields findAllVacation(String year ,String deptId,boolean isSelectAllDept,String enterpriseCode) throws SQLException{
        LogUtil.log("Action:年初计划登记查询开始。", Level.INFO, null);
        HrCYearPlanFields result = new HrCYearPlanFields();
        String empIdNum = null; 
        try {
            int paramsNum = 10;
            
            // 查询sql
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT  D.EMP_ID  \n");
            sql.append("        ,D.VACATION_TYPE_ID  \n");
            sql.append("        ,D.VACATION_TYPE \n");
            sql.append("         ,D.CHS_NAME  \n");
            sql.append("         ,D.DEPT_NAME  \n");
            sql.append("         ,E.DAYS  \n");
            sql.append("         ,E.HOURS  \n");
            sql.append("         ,E.SIGN_STATE \n");
            sql.append(" FROM (             \n");
            sql.append("   SELECT A.VACATION_TYPE_ID VACATION_TYPE_ID \n");
            sql.append("       ,A.VACATION_TYPE VACATION_TYPE \n");
            sql.append("       ,B.EMP_ID EMP_ID    \n");
            sql.append("        ,B.CHS_NAME CHS_NAME \n");
            sql.append("         ,C.DEPT_ID DEPT_ID    \n");
            sql.append("       ,C.DEPT_NAME DEPT_NAME \n");
            // 部门id
            if (deptId != null && !"".equals(deptId)) {
            	sql.append("   FROM  HR_C_VACATIONTYPE A ,HR_J_EMP_INFO B ,HR_C_DEPT C  \n");
            }else {
            	sql.append("   FROM  HR_C_VACATIONTYPE A ,HR_J_EMP_INFO B  \n");
            	sql.append("      LEFT JOIN HR_C_DEPT C ON ( B.DEPT_ID = C.DEPT_ID AND C.IS_USE = ? AND C.ENTERPRISE_CODE = ?)");
            }
            sql.append("   WHERE A.IF_CYCLE = ?  \n");
            sql.append("         AND A.IS_USE= ?  \n");
            sql.append("         AND A.ENTERPRISE_CODE = ?  \n");
            sql.append("           AND B.IS_USE = ?           \n");
            sql.append("          AND B.ENTERPRISE_CODE = ?  \n");
            
            // 部门id
            if (deptId != null && !"".equals(deptId)) {
                 paramsNum = 11;
                // 判断是否要选择下级部门
                if(isSelectAllDept){
                    sql.append("         AND B.DEPT_ID IN(          \n");
                    sql.append("                SELECT T.DEPT_ID   \n");
                    sql.append("                FROM HR_C_DEPT T \n");
                    sql.append("                WHERE T.ENTERPRISE_CODE = ?  AND T.IS_USE = ?  \n");
                    sql.append("                      START WITH T.DEPT_ID = ? CONNECT BY PRIOR T.DEPT_ID = T.PDEPT_ID) \n");
                }else {
                    sql.append("         AND B.DEPT_ID = C.DEPT_ID \n");
                    sql.append("         AND C.ENTERPRISE_CODE = ? \n");
                    sql.append("         AND C.IS_USE = ? \n");
                    sql.append("         AND B.DEPT_ID = ? \n");
                }
            }
            // 部门id
            if (deptId == null || "".equals(deptId)) {
            	sql.append(")D \n");
            }else {
            sql.append("        AND B.DEPT_ID = C.DEPT_ID)D \n");
            }
            sql.append("   LEFT OUTER JOIN HR_C_YEARPLAN E ON D.VACATION_TYPE_ID = E.VACATION_TYPE_ID \n");
            sql.append("        AND D.EMP_ID = E.EMP_ID  \n");
            sql.append("        AND E.PLAN_YEAR = ? \n");
            sql.append("        AND E.IS_USE = ?    \n");
            sql.append("        AND E.ENTERPRISE_CODE = ? \n");
            // 排序，部门id,人员id,假别id
            sql.append("   ORDER BY D.DEPT_ID,    \n");
            sql.append("            D.EMP_ID,     \n");
            sql.append("            D.VACATION_TYPE_ID  \n");
            
            // 查询参数数组
            Object[] params = new Object[paramsNum];
            int i =0;
            //部门
            if (deptId == null || "".equals(deptId)) {
                params[i++] = IS_USE_Y;
                params[i++] = enterpriseCode;
            }
            params[i++] = "1";
            params[i++] = IS_USE_Y;
            params[i++] = enterpriseCode;
            params[i++] = IS_USE_Y;
            params[i++] = enterpriseCode;
            //部门
            if (deptId != null && !"".equals(deptId)) {
                params[i++] = enterpriseCode;
                params[i++] = "Y";//update by sychen 20100831
//                params[i++] = "U";
                params[i++] = deptId;
            }
            //部门
            params[i++] = year;
            params[i++] = IS_USE_Y;
            params[i++] = enterpriseCode;
            LogUtil.log("EJB: SQL =" + sql.toString(), Level.INFO, null);
            List  list = bll.queryByNativeSQL(sql.toString(), params);
            List<HrCYearPlanTwo> arrList = new ArrayList<HrCYearPlanTwo>();
            List<HrCVacationtype> vacationList = new ArrayList<HrCVacationtype>();
            
            if (list !=null) {
                Iterator it = list.iterator();
                while(it.hasNext()) {
                    HrCYearPlanTwo empVacationInfo = new HrCYearPlanTwo();
                    Object[] data = (Object[]) it.next();
                    // 人员id
                    if (data[0] != null) {
                        empVacationInfo.setEmpId(data[0].toString());
                        // 记录下第一个人的员工id,根据第一个人记录有多少种假别和假别名称
                        if(empIdNum == null || "".equals(empIdNum )) {
                            empIdNum = data[0].toString();
                        }
                        // 判断员工id是否有所改变
                        if(empIdNum.equals(data[0].toString())) {
                            // 无改变时，则还是同一个员工
                            HrCVacationtype vacation = new HrCVacationtype();
                            vacation.setVacationTypeId(Long.parseLong(data[1].toString()));
                            vacation.setVacationType(data[2].toString());
                            vacationList.add(vacation);
                        }
                    }
                    // 假别id
                    if(data[1] != null) {
                        empVacationInfo.setVacationTypeId(data[1].toString());
                    }
                    // 假别
                    if (data[2] != null) {
                        empVacationInfo.setVacationType(data[2].toString());
                    }
                    // 中文名
                    if(data[3] != null) {
                        empVacationInfo.setChsName(data[3].toString());
                    }
                    // 部门名称
                    if(data[4] != null) {
                        empVacationInfo.setDeptName(data[4].toString());
                    }
                    // 天数
                    if (data[5] != null) {
                        empVacationInfo.setDays(data[5].toString());
                    }
                    // 时长
                    if (data[6] != null) {
                        empVacationInfo.setHours(data[6].toString());
                    }
                    // 签字状态
                    if (data[7] != null) {
                        empVacationInfo.setSignState(data[7].toString());
                    }
                    arrList.add(empVacationInfo);
                }
            }
            result.setYearPlanList(arrList);
            result.setVacationType(vacationList);
            LogUtil.log("Action:年初计划登记查询结束。", Level.INFO, null);
            return result;
            
        }catch(RuntimeException e){
            LogUtil.log("Action:年初计划登记查询结束。", Level.SEVERE, e);
            throw new SQLException();
            
        }
        }
    
    /**
     * 查询某一员工在加班登记表中上一年的总共换休时间
     * @param enterpriseCode 企业编码 ,empId 员工ID ,year 上一年份
     * @return result  查询结果
     */
    @SuppressWarnings({ "unchecked", "unchecked", "unchecked" })
    public double findExchangeTime(String lastYear ,String enterpriseCode,String empId) throws SQLException{
         LogUtil.log("Action:查询某一员工在加班登记表中上一年的总共换休时间 开始。", Level.INFO, null);
         try {
             // 查询sql
             StringBuffer sql = new StringBuffer();
             sql.append(" SELECT SUM(EXCHANGEREST_HOURS)  \n");
             sql.append(" FROM HR_C_EXCHANGETOREST E      \n");
             sql.append(" WHERE ATTENDANCE_YEAR = ?       \n");
             sql.append("       AND E.EMP_ID = ?          \n");
             sql.append("       AND E.IS_USE = ?          \n");
             sql.append("       AND E.ENTERPRISE_CODE = ? \n");
             // 设置参数
             Object[] params = new Object[4];
             int i =0;
             params[i++] = lastYear;
             params[i++] = empId;
             params[i++] = IS_USE_Y;
             params[i++] = enterpriseCode;
             // 打印sql文
             LogUtil.log("EJB: SQL =" + sql.toString(), Level.INFO, null);
             // 进行查询
             List list = bll.queryByNativeSQL(sql.toString(), params);
             LogUtil.log("Action:查询某一员工在加班登记表中上一年的总共换休时间 结束。", Level.INFO, null);
             if (list !=null && list.size()!=0) {
                 if(list.get(0)==null) return 0;
                 Double result =Double.parseDouble(list.get(0).toString());
                 return result;
             }
             return 0;
            
         } catch(RuntimeException e){
             LogUtil.log("Action:查询某一员工在加班登记表中上一年的总共换休时间 失败。", Level.SEVERE, e);
             throw new SQLException();
         }
         
    }
    /**
     * 查询某一部门的标准出勤时间
     * @param enterpriseCode 企业编码 ,empId 员工ID ,year 上一年份
     * @return result  查询结果
     */
    @SuppressWarnings({ "unchecked", "unchecked", "unchecked" })
    public double findStandardTime(String year ,String enterpriseCode,String empId) throws SQLException{
        LogUtil.log("Action:查询某一部门的标准出勤时间 开始。", Level.INFO, null);
        try {
             // 查询sql
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT DISTINCT  A.STANDARD_TIME  \n");
            sql.append(" FROM HR_C_ATTENDANCESTANDARD A     \n");
            sql.append("      ,HR_J_EMP_INFO I      \n");
            sql.append(" WHERE I.EMP_ID = ?         \n");
            sql.append("       AND A.ATTENDANCE_YEAR = ? \n");
            sql.append("       AND A.IS_USE = ? \n");
            sql.append("       AND A.ENTERPRISE_CODE = ? \n");
            sql.append("       AND I.IS_USE = ? \n");
            sql.append("       AND I.ENTERPRISE_CODE = ? \n");
            sql.append("       AND NVL(I.ATTENDANCE_DEPT_ID,I.DEPT_ID) = A.ATTENDANCE_DEPT_ID         \n");
            // 设置参数
            Object[] params = new Object[6];
            int i =0;
            params[i++] = empId;
            params[i++] = year;
            params[i++] = IS_USE_Y;
            params[i++] = enterpriseCode;
            params[i++] = IS_USE_Y;
            params[i++] = enterpriseCode;
           // 打印sql文
            LogUtil.log("EJB: SQL =" + sql.toString(), Level.INFO, null);
            // 进行查询
            List list = bll.queryByNativeSQL(sql.toString(), params);
            LogUtil.log("Action:查询某一部门的标准出勤时间 结束。", Level.INFO, null);
            if (list !=null && list.size()!=0) {
                if(list.get(0)==null) return 0;
                 Double result =Double.parseDouble(list.get(0).toString());
                 return result;
            }
            return 0;
        }catch (Exception e) {
            LogUtil.log("Action:查询某一部门的标准出勤时间 失败。", Level.SEVERE, e);
               throw new SQLException();
        }
        
    }
 
    /**
     * 查询假别的时长信息
     * @param strEmpId 人员id
     * @param strStartTime 开始时间
     * @param strVacationTypeId 假别id
     * @param strEnterpriseCode 企业代码
     * @return PageObject 假别的时长信息
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public PageObject findVacationHours(String strEmpId,
            String strStartTime, String strVacationTypeId, String strEnterpriseCode,
            final int... rowStartIdxAndCount) throws SQLException {
        LogUtil.log("EJB:假别的时长信息查询正常开始", Level.INFO, null);
        PageObject pobj = new PageObject();
        try {

            int paramsNum = 6;
            // 查询sql
            StringBuffer sbd = new StringBuffer();
            // SELECT文
            sbd.append("SELECT ");
            sbd.append("A.HOURS ");
            int intLength = sbd.length();

            // FROM文
            sbd.append("FROM HR_C_YEARPLAN A ");
            sbd.append("WHERE ");
            sbd.append("A.ENTERPRISE_CODE = ? ");
            sbd.append("AND A.IS_USE = ? ");
            sbd.append("AND A.EMP_ID = ? ");
            sbd.append("AND A.PLAN_YEAR  = ? ");
            sbd.append("AND A.VACATION_TYPE_ID = ? ");
            sbd.append("AND A.SIGN_STATE = ? ");

            // 查询参数数组
            Object[] params = new Object[paramsNum];
            int i =0;
            params[i++] = strEnterpriseCode;
            params[i++] = IS_USE_Y;
            params[i++] = strEmpId;
            params[i++] = strStartTime.substring(0,4);
            params[i++] = strVacationTypeId;
            params[i++] = "2";
            // 记录数
            String sqlCount = "SELECT " +
                    " COUNT(A.PLAN_YEAR) " +sbd.substring(intLength, sbd.length());
            List<HrCYearplan> list = bll.queryByNativeSQL(sbd.toString(), params, rowStartIdxAndCount);
            LogUtil.log("EJB: SQL =" + sbd.toString(), Level.INFO, null);
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
                    .toString());
            LogUtil.log("EJB: SQL =" + sqlCount, Level.INFO, null);
            List<HrCYearplan> arrList = new ArrayList<HrCYearplan>();
            if (list !=null) {
                Iterator it = list.iterator();
                while(it.hasNext()) {
                    HrCYearplan hrCYearplan = new HrCYearplan();
                    Object data = (Object) it.next();
                    // 计划时长
                    if(data != null) {
                        hrCYearplan.setHours(Double.parseDouble(data.toString()));
                    }
                    arrList.add(hrCYearplan);
                }
            }
            pobj.setList(arrList);
            pobj.setTotalCount(totalCount);
            LogUtil.log("EJB:假别的时长信息查询正常结束", Level.INFO, null);
        } catch (RuntimeException e) {
            LogUtil.log("EJB:假别的时长信息查询失败", Level.SEVERE, e);
            throw new SQLException();
        }
        return pobj;
    }
    
     /**
     * 根据逻辑主键查询数据信息
     * @param  empId 员工ID ,year 年份，假别id,enterpriseCode 企业编码
     * @return result  查询结果
     */
    @SuppressWarnings({ "unchecked", "unchecked", "unchecked" })
    public List<HrCYearplan> searchMsgByLogicId(String year ,String empId,String vacationTypeId,String enterpriseCode) throws SQLException{
        LogUtil.log("EJB:根据逻辑主键查询数据信息开始", Level.INFO, null);
         try {
             // 查询sql
             StringBuffer sql = new StringBuffer().append(
                    "SELECT * \n" + "FROM HR_C_YEARPLAN Y \n ").append(
                    " WHERE Y.PLAN_YEAR = ? \n").append(
                    "     AND Y.EMP_ID = ? \n").append(
                    "     AND Y.VACATION_TYPE_ID = ? \n").append(
                    "AND Y.ENTERPRISE_CODE = ?").append("AND Y.IS_USE = ?");
             LogUtil.log("SQL=" + sql.toString(), Level.INFO, null);
             List<HrCYearplan> list=bll.queryByNativeSQL(sql.toString(),new Object[]{year,empId,vacationTypeId,enterpriseCode,IS_USE_Y}, HrCYearplan.class);
             LogUtil.log("Action:根据逻辑主键查询数据信息结束。", Level.INFO, null);
             if (list.size() < 1) {
                return null;
            } else {
                return list;
            }
         } catch (RuntimeException re) {
             LogUtil.log("Action:根据逻辑主键查询数据信息失败。", Level.SEVERE, re);
             throw new SQLException();
         }
    }
    /**
     * 增加和插入数据 
     *
     * @param entity  要修改的记录
     * @return void  
     * @throws SQLException
     */
    public void updateAndInsertYearPlan(List<HrCYearplan> updateList,List<HrCYearplan> insertList)throws SQLException {
        LogUtil.log("EBJ:保存年初计划登记表信息开始", Level.INFO, null);
        try {
            if (updateList != null && insertList != null) {
                // 更新updateList中的数据
                for (int i = 0; i < updateList.size(); i++) {
                    updateYearPlan(updateList.get(i));
                }
                // 插入数据
                for (int j = 0; j < insertList.size(); j++) {
                    addYearPlan(insertList.get(j));
                }
            }
            LogUtil.log("EBJ：保存年初计划登记表信息结束", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("EBJ：保存年初计划登记表信息失败", Level.SEVERE, re);
            throw new SQLException();
        }
    }
    
    /**
     * 修改一条记录
     * 
     * @param entity
     *            要修改的记录
     * @return void
     * @throws SQLException
     */
    public void updateYearPlan(HrCYearplan entity)throws SQLException {
        LogUtil.log("EBJ:修改年初计划登记表信息开始", Level.INFO, null);
        try {
            // 修改时间
            entity.setLastModifiyDate(new Date());
            entityManager.merge(entity);
            LogUtil.log("EBJ：修改年初计划登记表信息结束", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("EBJ：修改年初计划登记表信息失败", Level.SEVERE, re);
            throw new SQLException();
        }
    }
    
    /**
     * 增加一条记录
     *
     * @param entity 要增加的记录
     * @throws SQLException
     * @return void  
     */
    public void addYearPlan(HrCYearplan entity)throws SQLException{
        LogUtil.log("EJB:新增年初计划登记表信息开始", Level.INFO, null);
        try {
            // 设定修改时间
            entity.setLastModifiyDate(new java.util.Date());
            // 设定是否使用
            entity.setIsUse(IS_USE_Y);
            // 保存
            entityManager.persist(entity);
            LogUtil.log("EJB:新增年初计划登记表信息结束", Level.INFO, null);

        } catch (RuntimeException re) {
            LogUtil.log("EJB:新增年初计划登记表信息失败", Level.SEVERE, re);
            throw new SQLException();
        }
    }
    /**
     * 保存上报数据
     *
     * @param reportList 要上报的记录
     * @param saveList 要保存的纪录
     * @throws SQLException
     * @return void  
     */
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveAndReportYearPlan(List<Map<String, Object>> reportList,List<List> saveList) throws SQLException {
        try {
            if (saveList != null) {
                // 保存数据
                updateAndInsertYearPlan(saveList.get(0), saveList.get(1));
            }
            // 上报数据
            for (int i = 0; i < reportList.size(); i++) {
                reportYearPlan(reportList.get(i));
            }
        } catch (RuntimeException e) {
            throw new SQLException();
        }
    }
    /**
     * 上报一条数据
     * 
     * @param  mapList
     *            要上报的记录
     * @throws SQLException
     * @return void
     */
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void reportYearPlan( Map<String, Object> mapList)throws SQLException{
        LogUtil.log("EJB:上报年初计划登记表信息开始", Level.INFO, null);
        // 要上报的人员id
        String empId = mapList.get("empId").toString();
        // 年份
        String year = mapList.get("year").toString();
        // 企业代码
        String enterpriseCode = mapList.get("enterpriseCode").toString();
        // 上次修改人
        String lastModifiyBy = mapList.get("lastModifiyBy").toString();
        // 根据人员id和年份查询人员信息
        StringBuffer sql = new StringBuffer().append(
               "SELECT * \n" + "FROM HR_C_YEARPLAN Y \n ").append(
               " WHERE Y.PLAN_YEAR = ? \n").append(
               "     AND Y.EMP_ID = ? \n").append(
               "AND Y.ENTERPRISE_CODE = ?").append("AND Y.IS_USE = ?").append(
               "       AND ( Y.SIGN_STATE = ? OR Y.SIGN_STATE = ?  ) ");
        LogUtil.log("SQL=" + sql.toString(), Level.INFO, null);
        try {  
        	List<HrCYearplan> result = bll.queryByNativeSQL(sql.toString(),new Object[]{year,empId,enterpriseCode,IS_USE_Y,CodeConstants.FROM_STATUS_0,CodeConstants.FROM_STATUS_3},HrCYearplan.class);
        	for(int i = 0;i<result.size();i++){
        		// 设置签字状态
        		result.get(i).setSignState(CodeConstants.FROM_STATUS_1);
        		// 设置上次修改人
        		result.get(i).setLastModifiyBy(lastModifiyBy);
        		// 设置修改时间
        		result.get(i).setLastModifiyDate(new Date());
        		// 修改
        		update(result.get(i));
        	}
            LogUtil.log("EJB:上报年初计划登记表信息结束" + result.size(), Level.INFO, null);

        } catch (Exception re) {
            LogUtil.log("EJB:上报年初计划登记表信息失败", Level.SEVERE, re);
            throw new SQLException();
        }
    }
}
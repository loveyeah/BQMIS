package power.ejb.hr;

import java.sql.SQLException;
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

/**
 * Facade for entity HrJDepstationcorrespond.
 * 
 * @see power.ejb.hr.HrJDepstationcorrespond
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJDepstationcorrespondFacade implements
        HrJDepstationcorrespondFacadeRemote {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    /**是否使用*/
    private String IS_USE_Y = "Y";

    /**
     * Perform an initial save of a previously unsaved HrJDepstationcorrespond
     * entity. All subsequent persist actions of this entity should use the
     * #update() method.
     * 
     * @param entity
     *            HrJDepstationcorrespond entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrJDepstationcorrespond entity) {
        LogUtil
                .log("saving HrJDepstationcorrespond instance", Level.INFO,
                        null);
        try {
            entityManager.persist(entity);
            LogUtil.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Delete a persistent HrJDepstationcorrespond entity.
     * 
     * @param entity
     *            HrJDepstationcorrespond entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrJDepstationcorrespond entity) {
        LogUtil.log("deleting HrJDepstationcorrespond instance", Level.INFO,
                null);
        try {
            entity = entityManager.getReference(HrJDepstationcorrespond.class,
                    entity.getDepstationcorrespondid());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Persist a previously saved HrJDepstationcorrespond entity and return it
     * or a copy of it to the sender. A copy of the HrJDepstationcorrespond
     * entity parameter is returned when the JPA persistence mechanism has not
     * previously been tracking the updated entity.
     * 
     * @param entity
     *            HrJDepstationcorrespond entity to update
     * @return HrJDepstationcorrespond the persisted HrJDepstationcorrespond
     *         entity instance, may not be the same
     * @throws RuntimeException
     *             if the operation fails
     */
    public HrJDepstationcorrespond update(HrJDepstationcorrespond entity) {
        LogUtil.log("updating HrJDepstationcorrespond instance", Level.INFO,
                null);
        try {
            HrJDepstationcorrespond result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    public HrJDepstationcorrespond findById(Long id) {
        LogUtil.log("finding HrJDepstationcorrespond instance with id: " + id,
                Level.INFO, null);
        try {
            HrJDepstationcorrespond instance = entityManager.find(
                    HrJDepstationcorrespond.class, id);
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Find all HrJDepstationcorrespond entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrJDepstationcorrespond property to query
     * @param value
     *            the property value to match
     * @return List<HrJDepstationcorrespond> found by query
     */
    @SuppressWarnings("unchecked")
    public List<HrJDepstationcorrespond> findByProperty(String propertyName,
            final Object value) {
        LogUtil.log("finding HrJDepstationcorrespond instance with property: "
                + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from HrJDepstationcorrespond model where model."
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
     * Find all HrJDepstationcorrespond entities.
     * 
     * @return List<HrJDepstationcorrespond> all HrJDepstationcorrespond
     *         entities
     */
    @SuppressWarnings("unchecked")
    public List<HrJDepstationcorrespond> findAll() {
        LogUtil.log("finding all HrJDepstationcorrespond instances",
                Level.INFO, null);
        try {
            final String queryString = "select model from HrJDepstationcorrespond model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }
    
    /**
     *  根据部门查找岗位
     * @param deptId
     * @param enterpriseCode
     * @return
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public PageObject findByDeptId(String deptId,String enterpriseCode) throws SQLException {
        LogUtil.log("根据部门查找岗位信息开始: ",Level.INFO, null);
        try {
            PageObject result = new PageObject();
            Object[] params = new Object[4];
            params[0] = enterpriseCode;
            params[1] = IS_USE_Y;
            params[2] = "Y";//update by sychen 20100901
//            params[2] = "U";
//            params[3] = enterpriseCode;
            params[3] = deptId;
            // 查询sql
            String sql=
                "select distinct n.* from HR_J_DEPSTATIONCORRESPOND  t,\n" +
                " HR_C_STATION n \n" +
                " where  t.enterprise_code= ? \n" +
                " and t.STATION_ID = n.STATION_ID " + 
                " and t.is_use= ? \n" +
                " and n.is_use= ? \n" +
                // modify by liuyi 090911 该表中无此属性
//               // " and n.enterprise_code= ? \n" +

                //update by sychen 20100728
                " and t.DEPT_ID IN (select t.dept_id\n" +
                "                    from hr_c_dept t\n" + 
                "                     where t.is_use = 'Y'\n" + //update by sychen 20100901
//                "                     where t.is_use = 'U'\n" +
                "                     and t.enterprise_code = 'hfdc'\n" + 
                "                     START WITH t.dept_id = ?\n" + 
                "                    CONNECT BY PRIOR t.dept_id = t.pdept_id)\n" +
//                " and t.DEPT_ID= ? \n" +
                //update by sychen 20100728 end 
                " order by n.STATION_ID";
            // 打印sql文
            LogUtil.log("sql 文："+sql, Level.INFO, null);
            // 执行查询
            List<HrCStation> list = bll.queryByNativeSQL(sql,params, HrCStation.class);
            // 查询sql
            String sqlCount =
                "select count(distinct n.STATION_ID) from HR_J_DEPSTATIONCORRESPOND  t,\n" +
                 " HR_C_STATION n \n" +
                 " where  t.enterprise_code= ? \n" +
                 " and t.STATION_ID = n.STATION_ID " + 
                 " and t.is_use= ? \n" +
                 " and n.is_use= ? \n" +
              // modify by liuyi 090911 该表中无此属性
//                 " and n.enterprise_code= ? \n" +
                 //update by sychen 20100728
                 " and t.DEPT_ID IN (select t.dept_id\n" +
                 "                    from hr_c_dept t\n" + 
                 "                     where t.is_use = 'Y'\n" + //update by sychen 20100901
//                 "                     where t.is_use = 'U'\n" + 
                 "                     and t.enterprise_code = 'hfdc'\n" + 
                 "                     START WITH t.dept_id = ?\n" + 
                 "                    CONNECT BY PRIOR t.dept_id = t.pdept_id)\n" +
//                 " and t.DEPT_ID= ? \n" +
                 //update by sychen 20100728 end 
                 " order by t.STATION_ID";
            // 执行查询
            Long totalCount = Long
                .parseLong(bll.getSingal(sqlCount,params).toString());
            // 设置PageObject
            result.setList(list);
            result.setTotalCount(totalCount);
            // 返回
            LogUtil.log("根据部门查找岗位信息结束: ",Level.INFO, null);
            return result;
        }catch(RuntimeException e){
            LogUtil.log("根据部门查找岗位信息失败: ",Level.INFO, null);
            throw new SQLException(e.getMessage());
        }
    }
    
    /**
     * 班组联动查询  add by sychen 20100721
     * @param deptId
     * @param enterpriseCode
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public PageObject getBeforeBanZuList(String deptId,String enterpriseCode) throws SQLException {
        try {
            PageObject result = new PageObject();
          
            String sql=
            	"SELECT *\n" +
            	"  FROM hr_c_dept t\n" + 
            	" WHERE t.is_banzu = 1\n" + 
            	"   AND t.dept_status <> 1\n "+
            	"   AND t.is_use = 'Y'\n" +  //update by sychen 20100901
//            	"   AND t.is_use = 'U'\n" + 
            	"   AND t.enterprise_code = '"+enterpriseCode+"'\n" + 
            	"   START WITH t.dept_id= "+deptId+"\n"+ 
            	"   CONNECT BY PRIOR t.dept_id = t.pdept_id";

            // 执行查询
            List<HrCDept> list = bll.queryByNativeSQL(sql, HrCDept.class);
            // 查询sql
            String sqlCount =
                "select count(t.dept_id) \n"+
            	"  FROM hr_c_dept t\n" + 
            	" WHERE t.is_banzu = 1\n" + 
            	"   AND t.dept_status <> 1\n "+
            	"   AND t.is_use = 'Y'\n" + //update by sychen 20100901
//            	"   AND t.is_use = 'U'\n" + 
            	"   AND t.enterprise_code = '"+enterpriseCode+"'\n" + 
            	"   START WITH t.dept_id= "+deptId+"\n"+ 
            	"   CONNECT BY PRIOR t.dept_id = t.pdept_id";
            // 执行查询
            Long totalCount = Long .parseLong(bll.getSingal(sqlCount).toString());
            // 设置PageObject
            result.setList(list);
            result.setTotalCount(totalCount);
            return result;
        }catch(RuntimeException e){
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * 查询部门岗位信息
     * @param strDeptID 部门id
     * @param strEmpID 人员id
     * @param strEnterpriseCode 企业代码
     * @return 部门岗位信息
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public PageObject findStationMaintain(String strDeptID, String strEmpID,
            String strEnterpriseCode, final int... rowStartIdxAndCount)
        throws SQLException {
        LogUtil.log("查询部门岗位信息开始: ", Level.INFO, null);
        try {
            PageObject pobj = new PageObject();
            StringBuffer sbd = new StringBuffer();
            // SELECT 文
            sbd.append("SELECT DISTINCT ");
            sbd.append("B.STATION_ID, ");
            sbd.append("B.STATION_NAME, ");
            sbd.append("D.STATION_LEVEL_NAME ");
            // SELECT的长度
            int intSbdSelect = sbd.length();
            // FROM文
            sbd.append("FROM HR_J_DEPSTATIONCORRESPOND A, ");
            sbd.append("HR_C_STATION B ");
            sbd.append("LEFT JOIN HR_C_STATION_LEVEL D ");
            sbd.append("ON D.STATION_LEVEL_ID = B.STATION_LEVEL_ID ");
            sbd.append("AND D.ENTERPRISE_CODE = ? ");
            sbd.append("AND D.IS_USE = ?  ");
            // WHERE文
            sbd.append("WHERE A.DEPT_ID = ? ");
            sbd.append("AND A.STATION_ID = B.STATION_ID ");
            sbd.append("AND A.ENTERPRISE_CODE = ? ");
            sbd.append("AND A.IS_USE = ? ");
            sbd.append("AND B.ENTERPRISE_CODE = ? ");
            sbd.append("AND B.IS_USE = ? ");
            sbd.append("AND A.STATION_ID NOT IN ( ");
            sbd.append("SELECT ");
            sbd.append("C.STATION_ID ");
            sbd.append("FROM HR_J_EMP_STATION C ");
            sbd.append("WHERE C.EMP_ID = ? ");
            sbd.append("AND C.ENTERPRISE_CODE = ? ");
            sbd.append("AND C.IS_USE = ? )");
            Object[] params = new Object[10];
            int i =0;
            params[i++] = strEnterpriseCode;
            // modified by liuyi 091205 
//            params[i++] =IS_USE_Y;
            params[i++] ="Y";//update by sychen 20100901
//            params[i++] ="U";
            params[i++] = strDeptID;
            params[i++] = strEnterpriseCode;
            params[i++] =IS_USE_Y;
            params[i++] = strEnterpriseCode;
         // modified by liuyi 091205 
//            params[i++] =IS_USE_Y;
            params[i++] ="Y";//update by sychen 20100901
//            params[i++] ="U";
            params[i++] = strEmpID;
            params[i++] = strEnterpriseCode;
            params[i++] =IS_USE_Y;
            // 执行查询
            List<EmpStationMaintainInfo> list = bll.queryByNativeSQL(
                    sbd.toString(), params, rowStartIdxAndCount);
            // 打印sql文
            LogUtil.log("sql 文：" + sbd.toString(), Level.INFO, null);
            // SqlCount文
            StringBuffer sbdCount = new StringBuffer();
            sbdCount.append("SELECT ");
            sbdCount.append("COUNT(B.STATION_ID) ");
            //sqlCount
            String sqlCount = sbdCount.toString()+sbd.substring(intSbdSelect, sbd.length());
            // 执行查询
            Long totalCount = Long
                .parseLong(bll.getSingal(sqlCount,params).toString());
            // 打印sql文
            LogUtil.log("sql 文：" + sqlCount.toString(), Level.INFO, null);
            List<EmpStationMaintainInfo> arrList = new ArrayList<EmpStationMaintainInfo>();
            if (list !=null) {
                Iterator it = list.iterator();
                while(it.hasNext()) {
                    EmpStationMaintainInfo empStationMaintainInfo = new EmpStationMaintainInfo();
                    Object[] data = (Object[]) it.next();
                    // 岗位id
                    if(data[0] != null) {
                        empStationMaintainInfo.setStationId(data[0].toString());
                    }
                    // 岗位名称
                    if (data[1] != null) {
                        empStationMaintainInfo.setStationName(data[1].toString());
                    }
                    // 岗位级别
                    if (data[2] != null) {
                        empStationMaintainInfo.setStationLevel(data[2].toString());
                    }
                    arrList.add(empStationMaintainInfo);
                }
            }
            // 设置PageObject
            pobj.setList(arrList);
            pobj.setTotalCount(totalCount);
            // 返回
            LogUtil.log("查询部门岗位信息结束: ",Level.INFO, null);
            return pobj;
        } catch (RuntimeException e) {
            LogUtil.log("查询部门岗位信息失败: ",Level.INFO, null);
            throw new SQLException(e.getMessage());
        }
        
    }
}
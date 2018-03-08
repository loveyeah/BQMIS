package power.ejb.hr;

import java.sql.SQLException;
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

import power.ear.comm.DataChangeException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity HrJStationremove.
 * 
 * @see power.ejb.hr.HrJStationremove
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJStationremoveFacade implements HrJStationremoveFacadeRemote {

    @PersistenceContext
    private EntityManager entityManager;
    //fields
    @EJB(beanName ="NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    
    /** 字符串: 空字符串 */
    public final String STRING_BLANK = "";
    /** 是否使用:是 */
    public final String IS_USE_Y = "Y";
    /** 是否使用：否 */
    public final String IS_USE_N = "N";
    /** 岗位调动类别ID=‘0’（班组间） */
    public final String STATION_MOVE_TYPE_ID_0 = "0";
    /** 岗位调动类别ID=‘1’（部门间） */
    public final String STATION_MOVE_TYPE_ID_1 = "1";
    // 单据状态
    /** 单据状态0：未上报 */
    public final String DCM_STATUS_0 = "0";
    /** 单据状态1：已上报 */
    public final String DCM_STATUS_1 = "1";
    /** 单据状态2：已终结 */
    public final String DCM_STATUS_2 = "2";
    /** 单据状态3：已退回 */
    public final String DCM_STATUS_3 = "3";
    // 单据状态对应文字说明
    /** 字符串单据状态0：未上报 */
    public final String STRING_DCM_STATUS_0 = "未上报";
    /** 字符串单据状态1：已上报 */
    public final String STRING_DCM_STATUS_1 = "已上报";
    /** 字符串单据状态2：已终结 */
    public final String STRING_DCM_STATUS_2 = "已终结";
    /** 字符串单据状态3：已退回 */
    public final String STRING_DCM_STATUS_3 = "已退回";
    // 是否已回
    /** 是否已回0：否 */
    public final String IFBACK_0 = "0";
    /** 是否已回1：是 */
    public final String IFBACK_1 = "1";
    // 是否已回对应文字说明
    /** 字符串是否已回0：否 */
    public final String STRING_IFBACK_0 = "否";
    /** 字符串是否已回1：是 */
    public final String STRING_IFBACK_1 = "是";

    /**
     * Perform an initial save of a previously unsaved HrJStationremove entity.
     * All subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *                HrJStationremove entity to persist
     * @throws RuntimeException
     *                 when the operation fails
     */
    public HrJStationremove save(HrJStationremove entity) {
        LogUtil.log("saving HrJStationremove instance", Level.INFO, null);
        try {
            // 采番处理
            entity.setStationremoveid(bll.getMaxId("HR_J_STATIONREMOVE", "STATIONREMOVEID"));
//            if(entity.getRequisitionNo()==null||entity.getRequisitionNo().equals(""))
//            {
//            	entity.setRequisitionNo(this.getMaxRequisitionNo(entity.getEnterpriseCode()));
//            }
            // 修改时间
            entity.setLastModifiedDate(new Date());
            entity.setInsertdate(new Date());
            entity.setIsUse(IS_USE_Y);
            LogUtil.log("EJB:岗位调动单保存开始。", Level.INFO, null);
            entityManager.persist(entity);
            LogUtil.log("EJB:岗位调动单保存结束。", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("EJB:岗位调动单保存失败。", Level.SEVERE, re);
            throw re;
        }
        return entity;
        }
    
    
    public String getMaxRequisitionNo(String enterpriseCode)
    {
    	String sql=
    		"\n" +
    		"select nvl(max(to_number(t.requisition_no)),0)+1 from HR_J_STATIONREMOVE t\n" + //modify by wpzhu 20100730
    		"where t.is_use='Y'\n" + 
    		"and t.enterprise_code='"+enterpriseCode+"'";
    	Object obj=bll.getSingal(sql);
    	if(obj!=null&&!obj.equals(""))
    	{
    		return obj.toString();
    	}
    	else
    	{
    		return "1";
    	}

    	
    }

    /**
     * Delete a persistent HrJStationremove entity.
     * 
     * @param entity
     *                HrJStationremove entity to delete
     * @throws SQLException 
     * @throws DataChangeException 
     * @throws RuntimeException
     *                 when the operation fails
     */
    public void delete(HrJStationremove entity,String date) throws SQLException, DataChangeException {
         LogUtil.log("EJB:删除岗位调动单保存开始。", Level.INFO, null);
    try {
        String dbUpdateTime = entity.getLastModifiedDate().toString().substring(0, 19);
        if (!date.equals(dbUpdateTime)) {
            throw new DataChangeException("");
        }else{
            entity.setLastModifiedDate(new Date());
            entity.setIsUse(IS_USE_N);
            entityManager.merge(entity);
        }
        LogUtil.log("EJB:删除岗位调动单保存结束。", Level.INFO, null);
    } catch (RuntimeException re) {
         LogUtil.log("EJB:删除岗位调动单保存失败。", Level.INFO, null);
        throw new SQLException(re.getMessage());
    }
    }
     
    /**
     * 上报岗位调动单
     * @param entity 岗位调动单bean
     * @throws SQLException
     * @throws DataChangeException 
     */
    public void report(HrJStationremove entity,String  date) throws SQLException, DataChangeException {
             LogUtil.log("EJB:上报岗位调动单开始。", Level.INFO, null);
        try {
            String dbUpdateTime = entity.getLastModifiedDate().toString().substring(0, 19);
            if (!date.equals(dbUpdateTime)) {
                throw new DataChangeException("");
            }else{
                entity.setLastModifiedDate(new Date());
                entity.setDcmState(DCM_STATUS_1);
                entityManager.merge(entity);
            }
            LogUtil.log("EJB:上报岗位调动单结束。", Level.INFO, null);
        } catch (RuntimeException re) {
             LogUtil.log("EJB:上报岗位调动单失败。", Level.INFO, null);
            throw new SQLException(re.getMessage());
        }
        }
    /**
     * Persist a previously saved HrJStationremove entity and return it or a
     * copy of it to the sender. A copy of the HrJStationremove entity parameter
     * is returned when the JPA persistence mechanism has not previously been
     * tracking the updated entity.
     * 
     * @param entity
     *                HrJStationremove entity to update
     * @return HrJStationremove the persisted HrJStationremove entity instance,
     *         may not be the same
     * @throws DataChangeException 
     * @throws RuntimeException
     *                 if the operation fails
     */

    public void update(HrJStationremove entity,String date) throws SQLException, DataChangeException {
         LogUtil.log("EJB:更新岗位调动单开始。", Level.INFO, null);
    try {
        String dbUpdateTime = entity.getLastModifiedDate().toString().substring(0, 19);
        if (!date.equals(dbUpdateTime)) {
            throw new DataChangeException("");
        }else{
            entity.setLastModifiedDate(new Date());
            entityManager.merge(entity);
        }
        LogUtil.log("EJB:更新岗位调动单结束。", Level.INFO, null);
    } catch (RuntimeException re) {
         LogUtil.log("EJB:更新岗位调动单失败。", Level.INFO, null);
        throw new SQLException(re.getMessage());
    }
   }
    public HrJStationremove update(HrJStationremove entity) {
    LogUtil.log("updating HrJStationremove instance", Level.INFO, null);
    try {
        HrJStationremove result = entityManager.merge(entity);
        LogUtil.log("update successful", Level.INFO, null);
        return result;
    } catch (RuntimeException re) {
        LogUtil.log("update failed", Level.SEVERE, re);
        throw re;
    }
    }
    public HrJStationremove findById(Long id) {
    LogUtil.log("finding HrJStationremove instance with id: " + id,
        Level.INFO, null);
    try {
        HrJStationremove instance = entityManager.find(
            HrJStationremove.class, id);
        return instance;
    } catch (RuntimeException re) {
        LogUtil.log("find failed", Level.SEVERE, re);
        throw re;
    }
    }

    /**
     * Find all HrJStationremove entities with a specific property value.
     * 
     * @param propertyName
     *                the name of the HrJStationremove property to query
     * @param value
     *                the property value to match
     * @return List<HrJStationremove> found by query
     */
    @SuppressWarnings("unchecked")
    public List<HrJStationremove> findByProperty(String propertyName,
        final Object value) {
    LogUtil.log("finding HrJStationremove instance with property: "
        + propertyName + ", value: " + value, Level.INFO, null);
    try {
        final String queryString = "select model from HrJStationremove model where model."
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
     * Find all HrJStationremove entities.
     * 
     * @return List<HrJStationremove> all HrJStationremove entities
     */
    @SuppressWarnings("unchecked")
    public List<HrJStationremove> findAll() {
    LogUtil.log("finding all HrJStationremove instances", Level.INFO, null);
    try {
        final String queryString = "select model from HrJStationremove model";
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    } catch (RuntimeException re) {
        LogUtil.log("find all failed", Level.SEVERE, re);
        throw re;
    }
    }
    
    /**
     * 查询所有的岗位调动类别维护信息
     * @param enterpriseCode 企业编码
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public PageObject getStationRemove(String enterpriseCode) throws SQLException {
        LogUtil.log("查询所有的岗位调动类别维护信息开始: ",Level.INFO, null);
        try {
            PageObject result = new PageObject();
            Object[] params = new Object[2];
            params[0] = enterpriseCode;
            params[2] = 'Y';
            // 查询sql
            String sql=
                "select  * from HR_C_STATIONMOVETYPE  t\n" +
                "where  t.enterprise_code= ? \n" +
                "and t.is_use= ? \n" +
                " order by t.STATION_MOVE_TYPE_ID";
            // 执行查询
            List<HrJStationremove> list = bll.queryByNativeSQL(sql,params, HrJStationremove.class);
            // 查询sql
            String sqlCount =
                "select count(*) from HR_C_STATIONMOVETYPE  t\n" +
                "where  t.enterprise_code= ? \n" +
                "and t.is_use= ? \n" +
                " order by t.STATION_MOVE_TYPE_ID";;
            // 执行查询
            Long totalCount = Long
                .parseLong(bll.getSingal(sqlCount,params).toString());
            // 设置PageObject
            result.setList(list);
            result.setTotalCount(totalCount);
            // 返回
            LogUtil.log("查询所有的岗位调动类别维护信息结束: ",Level.INFO, null);
            return result;
        }catch(RuntimeException e){
            LogUtil.log("查询所有的岗位调动类别维护信息失败: ",Level.INFO, null);
            throw new SQLException(e.getMessage());
        }
    }
    
      /**
     *  根据人员code查找部门岗位级别信息
     * @param enterpriseCode 企业编码
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public PageObject getDeptStationLevel(String enterpriseCode) throws SQLException {
        LogUtil.log("查询所有的岗位调动类别维护信息开始: ",Level.INFO, null);
        try {
            PageObject result = new PageObject();
            Object[] params = new Object[2];
            params[0] = enterpriseCode;
            params[2] = 'Y';
            // 查询sql
            String sql=
                "select  * from HR_C_STATIONMOVETYPE  t\n" +
                "where  t.enterprise_code= ? \n" +
                "and t.is_use= ? \n" +
                " order by t.STATION_MOVE_TYPE_ID";
            // 执行查询
            List<HrJStationremove> list = bll.queryByNativeSQL(sql,params, HrJStationremove.class);
            // 查询sql
            String sqlCount =
                "select count(*) from HR_C_STATIONMOVETYPE  t\n" +
                "where  t.enterprise_code= ? \n" +
                "and t.is_use= ? \n" +
                " order by t.STATION_MOVE_TYPE_ID";
            // 执行查询
            Long totalCount = Long
                .parseLong(bll.getSingal(sqlCount,params).toString());
            // 设置PageObject
            result.setList(list);
            result.setTotalCount(totalCount);
            // 返回
            LogUtil.log("查询所有的岗位调动类别维护信息结束: ",Level.INFO, null);
            return result;
        }catch(RuntimeException e){
            LogUtil.log("查询所有的岗位调动类别维护信息失败: ",Level.INFO, null);
            throw new SQLException(e.getMessage());
        }
    }

    /************员工调动查询用*开始**********************/
    /**
     * 员工调动查询之
     * 班组调动查询
     * @param startDate 调动日期上限
     * @param endDate 调动日期下限
     * @param deptBFCode 调动前部门
     * @param deptAFCode 调动后部门
     * @param enterpriseCode 企业编码
     * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
     * @return PageObject 查询结果
     */
    @SuppressWarnings("unchecked")
    public PageObject getBanZuList(String startDate, String endDate,
            String deptBFCode, String deptAFCode, String enterpriseCode, 
            final int... rowStartIdxAndCount) {
        // Log开始
        LogUtil.log("EJB:员工调动查询之班组调动查询开始。", Level.INFO, null);
        try {
            PageObject object = new PageObject();
            // 参数List
            List listParams = new ArrayList();
            // 查询sql
            String sql = 
                "SELECT DISTINCT " +
                    "C.CHS_NAME, " + 
                    "TO_CHAR(A.REMOVE_DATE, 'YYYY-MM-DD'), " + 
                    "B1.DEPT_NAME AS DEPT1, " + 
                    "B2.DEPT_NAME AS DEPT2, " + 
                    "TO_CHAR(A.DO_DATE, 'YYYY-MM-DD'), " + 
                    "A.MEMO, " +
                	"A.STATIONREMOVEID ";
            String sqlWhere =
                "FROM " +
                    "HR_J_STATIONREMOVE A LEFT JOIN HR_C_DEPT B1 ON A.OLD_DEP_ID = B1.DEPT_ID " +
                    "AND B1.ENTERPRISE_CODE = ? " +
                    "LEFT JOIN HR_C_DEPT B2 ON A.NEW_DEP_ID = B2.DEPT_ID " +
                    "AND B2.ENTERPRISE_CODE = ? " +
                    "LEFT JOIN HR_J_EMP_INFO C ON A.EMP_ID = C.EMP_ID " +
                    "AND C.ENTERPRISE_CODE = ? " +
                "WHERE  " +
                    "A.STATION_MOVE_TYPE_ID = ? AND  " +
                    "A.ENTERPRISE_CODE = ? AND  " +
                    "A.IS_USE = ? ";
            // 调动时间from
            if(startDate != null && !(STRING_BLANK.equals(startDate))) {
                sqlWhere += "AND TO_CHAR(A.REMOVE_DATE, 'YYYY-MM-DD') >= ? ";
                listParams.add(startDate);
            }
            // 调动时间to
            if(endDate != null && !(STRING_BLANK.equals(endDate))) {
                sqlWhere += "AND TO_CHAR(A.REMOVE_DATE, 'YYYY-MM-DD') <= ? ";
                listParams.add(endDate);
            }
            // 调动前部门
            if(deptBFCode != null && !(STRING_BLANK.equals(deptBFCode))) {
                sqlWhere += "AND A.OLD_DEP_ID = ? ";
                listParams.add(deptBFCode);
            }
            // 调动后部门
            if(deptAFCode != null && !(STRING_BLANK.equals(deptAFCode))) {
                sqlWhere += "AND A.NEW_DEP_ID = ? ";
                listParams.add(deptAFCode);
            }
            sqlWhere +=
                "ORDER BY TO_CHAR(A.REMOVE_DATE, 'YYYY-MM-DD') DESC ";
            sql += sqlWhere;
            String sqlCount = "SELECT COUNT(1) FROM (" + sql + ")";
            // 生成查询参数
            Object[] params = listParams.toArray();
            Object[] paramsNew = new Object[params.length + 6];
            // 企业编码
            for(int i = 0; i < 3; i++) {
                paramsNew[i] = enterpriseCode;
            }
            // 岗位调动类别（班组间）
            paramsNew[3] = STATION_MOVE_TYPE_ID_0;
            // 企业编码
            paramsNew[4] = enterpriseCode;
            // 是否使用
            paramsNew[5] = IS_USE_Y;
            // 其他参数
            for(int i = 0; i < params.length; i++) {
                paramsNew[6 + i] = params[i];
            }
            List list = bll.queryByNativeSQL(sql,
                    paramsNew, rowStartIdxAndCount);
            Iterator it = list.iterator();
            List<EmpMoveQueryBean> arrList = new ArrayList<EmpMoveQueryBean>();
            while (it.hasNext()) {
                EmpMoveQueryBean info = new EmpMoveQueryBean();
                Object[] data = (Object[]) it.next();
                if(null != data[0]) {
                    info.setChsName(data[0].toString());
                }
                if(null != data[1]) {
                    info.setRemoveDate(data[1].toString());
                }
                if(null != data[2]) {
                    info.setDeptNameFirst(data[2].toString());
                }
                if(null != data[3]) {
                    info.setDeptNameSecond(data[3].toString());
                }
                if(null != data[4]) {
                    info.setDoDate(data[4].toString());
                }
                if(null != data[5]) {
                    info.setMemoRemove(data[5].toString());
                }
                arrList.add(info);
            }
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
                    paramsNew).toString());
            object.setList(arrList);
            object.setTotalCount(totalCount);
            // Log结束
            LogUtil.log("EJB:员工调动查询之班组调动查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:员工调动查询之班组调动查询失败。", Level.SEVERE, e);
            throw e;
        }
    }
    
    /**
     * 员工调动查询之
     * 员工调动查询
     * @param startDate 调动日期上限
     * @param endDate 调动日期下限
     * @param dcmState 单据状态
     * @param deptBFCode 调动前部门
     * @param deptAFCode 调动后部门
     * @param enterpriseCode 企业编码
     * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
     * @return PageObject 查询结果
     */
    @SuppressWarnings("unchecked")
    public PageObject getEmpMoveList(String startDate, String endDate,
            String dcmState, String deptBFCode, String deptAFCode, 
            String enterpriseCode, final int... rowStartIdxAndCount) {
        // Log开始
        LogUtil.log("EJB:员工调动查询之员工调动查询开始。", Level.INFO, null);
        try {
            PageObject object = new PageObject();
            // 参数List
            List listParams = new ArrayList();
            // 查询sql
            String sql = 
                "SELECT  DISTINCT " + 
                    "E.CHS_NAME, " +
                    "TO_CHAR(A.REMOVE_DATE, 'YYYY-MM-DD'), " +
                    "TO_CHAR(A.DO_DATE2, 'YYYY-MM-DD'), " +
                    "A.REQUISITION_NO, " +
                    "B1.DEPT_NAME AS DEPT1, " +
                    "B2.DEPT_NAME AS DEPT2, " +
                    "C1.STATION_NAME AS STATION1, " +
                    "C2.STATION_NAME AS STATION2, " +
                    "D1.STATION_LEVEL_NAME AS LEVEL1, " +
                    "D2.STATION_LEVEL_NAME AS LEVEL2, " +
                    "DECODE(A.DCM_STATE, '" +
                    DCM_STATUS_0 + "', '" + STRING_DCM_STATUS_0 + "', '" +
                    DCM_STATUS_1 + "', '" + STRING_DCM_STATUS_1 + "', '" +
                    DCM_STATUS_2 + "', '" + STRING_DCM_STATUS_2 + "', '" +
                    DCM_STATUS_3 + "', '" + STRING_DCM_STATUS_3 + "'), " +
                    "A.MEMO, " +
                    "A.STATIONREMOVEID ";
            String sqlWhere =
                "FROM " + 
                    "HR_J_STATIONREMOVE A LEFT JOIN HR_C_DEPT B1 ON A.OLD_DEP_ID = B1.DEPT_ID " + 
                    "AND B1.ENTERPRISE_CODE = ? " + 
                    "LEFT JOIN HR_C_STATION C1 ON A.OLD_STATION_ID = C1.STATION_ID " +  
                    "AND C1.ENTERPRISE_CODE = ? " + 
                    "LEFT JOIN HR_C_STATION_LEVEL D1 ON C1.STATION_LEVEL_ID = D1.STATION_LEVEL_ID " +  
                    "AND D1.ENTERPRISE_CODE = ? " + 
                    "LEFT JOIN HR_C_DEPT B2 ON A.NEW_DEP_ID = B2.DEPT_ID " +  
                    "AND B2.ENTERPRISE_CODE = ? " + 
                    "LEFT JOIN HR_C_STATION C2 ON A.NEW_STATION_ID = C2.STATION_ID " +  
                    "AND C2.ENTERPRISE_CODE = ? " + 
                    "LEFT JOIN HR_C_STATION_LEVEL D2 ON C2.STATION_LEVEL_ID = D2.STATION_LEVEL_ID " +  
                    "AND D2.ENTERPRISE_CODE = ? " + 
                    "LEFT JOIN HR_J_EMP_INFO E ON A.EMP_ID = E.EMP_ID " +  
                    "AND E.ENTERPRISE_CODE = ? " + 
                "WHERE " + 
                    "A.STATION_MOVE_TYPE_ID = ? AND " + 
                    "A.ENTERPRISE_CODE = ? AND " + 
                    "A.IS_USE = ? ";
            // 调动时间from
            if(startDate != null && !(STRING_BLANK.equals(startDate))) {
                sqlWhere += "AND TO_CHAR(A.REMOVE_DATE, 'YYYY-MM-DD') >= ? ";
                listParams.add(startDate);
            }
            // 调动时间to
            if(endDate != null && !(STRING_BLANK.equals(endDate))) {
                sqlWhere += "AND TO_CHAR(A.REMOVE_DATE, 'YYYY-MM-DD') <= ? ";
                listParams.add(endDate);
            }
            // 单据状态
            if(dcmState != null && !(STRING_BLANK.equals(dcmState))) {
                sqlWhere += "AND A.DCM_STATE = ? ";
                listParams.add(dcmState);
            }
            // 调动前部门
        	  if(deptBFCode != null && !(STRING_BLANK.equals(deptBFCode))) {
                  sqlWhere += "AND A.OLD_DEP_ID = ? ";
                  listParams.add(deptBFCode);
              }
            // 调动后部门
            if(deptAFCode != null && !(STRING_BLANK.equals(deptAFCode))) {
                sqlWhere += "AND A.NEW_DEP_ID = ? ";
                listParams.add(deptAFCode);
            }
            sqlWhere +=
                "ORDER BY TO_CHAR(A.REMOVE_DATE, 'YYYY-MM-DD') DESC ";
            sql += sqlWhere;
            String sqlCount = "SELECT COUNT(1) FROM (" + sql + ")";
            // 生成查询参数
            Object[] params = listParams.toArray();
            Object[] paramsNew = new Object[params.length + 10];
            // 企业编码
            for(int i = 0; i < 7; i++) {
                paramsNew[i] = enterpriseCode;
            }
            // 岗位调动类别（部门间）
            paramsNew[7] = STATION_MOVE_TYPE_ID_1;
            // 企业编码
            paramsNew[8] = enterpriseCode;
            // 是否使用
            paramsNew[9] = IS_USE_Y;
            // 其他参数
            for(int i = 0; i < params.length; i++) {
                paramsNew[10 + i] = params[i];
            }
            List list = bll.queryByNativeSQL(sql,
                    paramsNew, rowStartIdxAndCount);
            Iterator it = list.iterator();
            List<EmpMoveQueryBean> arrList = new ArrayList<EmpMoveQueryBean>();
            while (it.hasNext()) {
                EmpMoveQueryBean info = new EmpMoveQueryBean();
                Object[] data = (Object[]) it.next();
                if(null != data[0]) {
                    info.setChsName(data[0].toString());
                }
                if(null != data[1]) {
                    info.setRemoveDate(data[1].toString());
                }
                if(null != data[2]) {
                    info.setDo2Date(data[2].toString());
                }
                if(null != data[3]) {
                    info.setRequisitionNo(data[3].toString());
                }
                if(null != data[4]) {
                    info.setDeptNameFirst(data[4].toString());
                }
                if(null != data[5]) {
                    info.setDeptNameSecond(data[5].toString());
                }
                if(null != data[6]) {
                    info.setStationNameBefore(data[6].toString());
                }
                if(null != data[7]) {
                    info.setStationNameAfter(data[7].toString());
                }
                if(null != data[8]) {
                    info.setStationLevelNameBefore(data[8].toString());
                }
                if(null != data[9]) {
                    info.setStationLevelNameAfter(data[9].toString());
                }
                if(null != data[10]) {
                    info.setDcmState(data[10].toString());
                }
                if(null != data[11]) {
                    info.setMemoRemove(data[11].toString());
                }
                arrList.add(info);
            }
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
                    paramsNew).toString());
            object.setList(arrList);
            object.setTotalCount(totalCount);
            // Log结束
            LogUtil.log("EJB:员工调动查询之员工调动查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:员工调动查询之员工调动查询失败。", Level.SEVERE, e);
            throw e;
        }
    }
    /**
     * 员工调动查询之
     * 员工借调查询
     * @param startDate 借调日期上限
     * @param endDate 借调日期下限
     * @param deptBFCode 所属部门
     * @param deptAFCode 借调部门
     * @param ifBack 是否已回
     * @param dcmStatus 单据状态
     * @param enterpriseCode 企业编码
     * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
     * @return PageObject 查询结果
     */
    @SuppressWarnings("unchecked")
    public PageObject getEmpBorrowList(String startDate, String endDate,
            String deptBFCode, String deptAFCode, String ifBack, 
            String dcmStatus, String enterpriseCode, 
            final int... rowStartIdxAndCount) {
        // Log开始
        LogUtil.log("EJB:员工调动查询之员工借调查询开始。", Level.INFO, null);
        try {
            PageObject object = new PageObject();
            // 参数List
            List listParams = new ArrayList();
            // 查询sql
            String sql = 
                "SELECT DISTINCT  " + 
                    "A.EMP_ID, " + 
                    "C.EMP_CODE, " + 
                    "C.CHS_NAME, " + 
                    "B1.DEPT_NAME AS DPT_BELONG, " +  
                    "D.STATION_NAME AS STA_BELONG, " + 
                    "B2.DEPT_NAME AS DPT_BORROW, " + 
                    "A.START_DATE, " + 
                    "A.END_DATE, " + 
                    "DECODE(A.IF_BACK, '" +
                    IFBACK_0 + "', '" + STRING_IFBACK_0 + "', '" + 
                    IFBACK_1 + "', '" + STRING_IFBACK_1 + "'), " + 
                    "DECODE(A.DCM_STATUS, '" + 
                    DCM_STATUS_0 + "', '" + STRING_DCM_STATUS_0 + "', '" + 
                    DCM_STATUS_1 + "', '" + STRING_DCM_STATUS_1 + "', '" + 
                    DCM_STATUS_2 + "', '" + STRING_DCM_STATUS_2 + "', '" + 
                    DCM_STATUS_3 + "', '" + STRING_DCM_STATUS_3 + "'), " + 
                    "A.MEMO ";
            String sqlWhere =
                "FROM " + 
                    "HR_J_EMPLOYEEBORROW_IN A LEFT JOIN HR_C_DEPT B2 ON A.BORROW_DEPT_ID = B2.DEPT_ID " + 
                    "AND B2.ENTERPRISE_CODE = ? " + 
                    "LEFT JOIN HR_J_EMP_INFO C ON A.EMP_ID = C.EMP_ID " + 
                    "AND C.ENTERPRISE_CODE = ? " + 
                    "LEFT JOIN HR_C_DEPT B1 ON C.DEPT_ID = B1.DEPT_ID " + 
                    "AND B1.ENTERPRISE_CODE = ? " + 
                    "LEFT JOIN HR_C_STATION D ON C.STATION_ID = D.STATION_ID " + 
                    "AND C.ENTERPRISE_CODE = ? " + 
                "WHERE " + 
                    "A.ENTERPRISE_CODE = ? " +
                    "AND A.IS_USE = ? ";
            // 借调时间from
            if(startDate != null && !(STRING_BLANK.equals(startDate))) {
                sqlWhere += "AND TO_CHAR(A.START_DATE, 'YYYY-MM-DD') >= ? ";
                listParams.add(startDate);
            }
            // 借调时间to
            if(endDate != null && !(STRING_BLANK.equals(endDate))) {
                sqlWhere += "AND TO_CHAR(A.START_DATE, 'YYYY-MM-DD') <= ? ";
                listParams.add(endDate);
            }
            // 所属部门
            if(deptBFCode != null && !(STRING_BLANK.equals(deptBFCode))) {
                sqlWhere += "AND B1.DEPT_ID = ? ";
                listParams.add(deptBFCode);
            }
            // 借调部门
            if(deptAFCode != null && !(STRING_BLANK.equals(deptAFCode))) {
                sqlWhere += "AND B2.DEPT_ID = ? ";
                listParams.add(deptAFCode);
            }
            // 是否已回
            if(ifBack != null && !(STRING_BLANK.equals(ifBack))) {
                sqlWhere += "AND A.IF_BACK = ? ";
                listParams.add(ifBack);
            }
            // 单据状态
            if(dcmStatus != null && !(STRING_BLANK.equals(dcmStatus))) {
                sqlWhere += "AND A.DCM_STATUS = ? ";
                listParams.add(dcmStatus);
            }
            sqlWhere +=
                "ORDER BY C.EMP_CODE ";
            sql += sqlWhere;
            String sqlCount = "SELECT COUNT(1)  FROM (" + sql + ")";
            // 生成查询参数
            Object[] params = listParams.toArray();
            Object[] paramsNew = new Object[params.length + 6];
            // 企业编码
            for(int i = 0; i < 4; i++) {
                paramsNew[i] = enterpriseCode;
            }
            // 企业编码
            paramsNew[4] = enterpriseCode;
            // 是否使用
            paramsNew[5] = IS_USE_Y;
            // 其他参数
            for(int i = 0; i < params.length; i++) {
                paramsNew[6 + i] = params[i];
            }
            List list = bll.queryByNativeSQL(sql,
                    paramsNew, rowStartIdxAndCount);
            Iterator it = list.iterator();
            List<EmpMoveQueryBean> arrList = new ArrayList<EmpMoveQueryBean>();
            while (it.hasNext()) {
                EmpMoveQueryBean info = new EmpMoveQueryBean();
                Object[] data = (Object[]) it.next();
                if(null != data[0]) {
                    info.setHrJEmployeeborrowInId(data[0].toString());
                }
                if(null != data[1]) {
                    info.setEmpCode(data[1].toString());
                }
                if(null != data[2]) {
                    info.setChsName(data[2].toString());
                }
                if(null != data[3]) {
                    info.setDeptNameFirst(data[3].toString());
                }
                if(null != data[4]) {
                    info.setStationNameBefore(data[4].toString());
                }
                if(null != data[5]) {
                    info.setDeptNameSecond(data[5].toString());
                }
                if(null != data[6]) {
                    info.setStartDate(data[6].toString());
                }
                if(null != data[7]) {
                    info.setEndDate(data[7].toString());
                }
                if(null != data[8]) {
                    info.setIfBack(data[8].toString());
                }
                if(null != data[9]) {
                    info.setDcmStatus(data[9].toString());
                }
                if(null != data[10]) {
                    info.setMemoBorrow(data[10].toString());
                }
                arrList.add(info);
            }
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
                    paramsNew).toString());
            object.setList(arrList);
            object.setTotalCount(totalCount);
            // Log结束
            LogUtil.log("EJB:员工调动查询之员工借调查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:员工调动查询之员工借调查询失败。", Level.SEVERE, e);
            throw e;
        }
    }
    
    /************员工调动查询用*结束**********************/
}
package power.ejb.hr;

import java.sql.SQLException;
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

import power.ear.comm.DataChangeException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity HrJEmpStation.
 * 
 * @see power.ejb.hr.HrJEmpStation
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJEmpStationFacade implements HrJEmpStationFacadeRemote {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

    /**是否使用*/
    private String IS_USE_Y = "Y";
    /** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
    private static final String DATE_FORMAT_YYYYMMDD_TIME_SEC = "yyyy-MM-dd HH:mm:ss";
    /** 是否主岗位 */
    private static final String IS_MAIN_STATION = "1";
    
    
    /**
     * 新增职工岗位
     * 
     * @param entity 职工岗位
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrJEmpStation entity) throws SQLException {
        LogUtil.log("EJB:新增职工岗位开始", Level.INFO, null);
        try {
            if (entity.getEmpStationId() == null) {
                entity.setEmpStationId(bll.getMaxId("HR_J_EMP_STATION",
                        "EMP_STATION_ID"));
            }
            entityManager.persist(entity);
            LogUtil.log("EJB:新增职工岗位正常结束", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("EJB:新增职工岗位异常结束", Level.SEVERE, re);
            throw new SQLException();
        }
    }

	/**
	 * 新增职工岗位(多次增加时用)
	 *
	 * @param entity 职工岗位
	 * @param argId 上次增加记录的流水号
	 * @return Long 增加后记录的流水号
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public Long save(HrJEmpStation entity, Long argId) throws SQLException {
		LogUtil.log("EJB:新增职工岗位开始", Level.INFO, null);
		try {
			Long lngId = argId;
			if (lngId == null || lngId.longValue() < 1L) {
				// 流水号自动采番
				lngId = bll.getMaxId("HR_J_EMP_STATION", "EMP_STATION_ID");
			} else {
				lngId = Long.valueOf(argId.longValue() + 1L);
			}
			
			// 流水号
			entity.setEmpStationId(lngId);
			entityManager.persist(entity);
			LogUtil.log("EJB:新增职工岗位正常结束", Level.INFO, null);
			return lngId;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:新增职工岗位异常结束", Level.SEVERE, re);
			throw new SQLException();
		}
	}
	
    /**
     * Delete a persistent HrJEmpStation entity.
     * 
     * @param entity
     *            HrJEmpStation entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrJEmpStation entity) {
        LogUtil.log("deleting HrJEmpStation instance", Level.INFO, null);
        try {
            entity = entityManager.getReference(HrJEmpStation.class, entity
                    .getEmpStationId());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 修改职工岗位
     * 
     * @param entity 职工岗位
     * @return HrJEmpStation 修改后的职工岗位
     * @throws RuntimeException
     *             if the operation fails
     */
    public HrJEmpStation update(HrJEmpStation entity)
            throws SQLException, DataChangeException {
        LogUtil.log("EJB:修改职工岗位开始", Level.INFO, null);

        // 得到数据库中的这个记录
        HrJEmpStation empStation = findById(entity.getEmpStationId());

        // 排他
        if (!formatDate(empStation.getLastModifiedDate(),
                DATE_FORMAT_YYYYMMDD_TIME_SEC).equals(
                formatDate(entity.getLastModifiedDate(),
                        DATE_FORMAT_YYYYMMDD_TIME_SEC))) {
            throw new DataChangeException("排它处理");
        }

        try {
            // 设置修改日期
            entity.setLastModifiedDate(new Date());
            HrJEmpStation result = entityManager.merge(entity);
            LogUtil.log("EJB:修改职工岗位正常结束", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("EJB:修改职工岗位异常结束", Level.SEVERE, re);
            throw re;
        }
    }

    public HrJEmpStation findById(Long id) {
        LogUtil.log("finding HrJEmpStation instance with id: " + id,
                Level.INFO, null);
        try {
            HrJEmpStation instance = entityManager
                    .find(HrJEmpStation.class, id);
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Find all HrJEmpStation entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrJEmpStation property to query
     * @param value
     *            the property value to match
     * @return List<HrJEmpStation> found by query
     */
    @SuppressWarnings("unchecked")
    public List<HrJEmpStation> findByProperty(String propertyName,
            final Object value) {
        LogUtil.log("finding HrJEmpStation instance with property: "
                + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from HrJEmpStation model where model."
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
     * Find all HrJEmpStation entities.
     * 
     * @return List<HrJEmpStation> all HrJEmpStation entities
     */
    @SuppressWarnings("unchecked")
    public List<HrJEmpStation> findAll() {
        LogUtil.log("finding all HrJEmpStation instances", Level.INFO, null);
        try {
            final String queryString = "select model from HrJEmpStation model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 根据日期和形式返回日期字符串
     * @param argDate 日期
     * @param argFormat 日期形式字符串
     * @return 日期字符串
     */
    private String formatDate(Date argDate, String argFormat) {
        if (argDate == null) {
            return "";
        }
        
        // 日期形式
        SimpleDateFormat sdfFrom = null;
        // 返回字符串
        String strResult = null;

        try {
            sdfFrom = new SimpleDateFormat(argFormat);
            // 格式化日期
            strResult = sdfFrom.format(argDate).toString();
        } catch (Exception e) {
            strResult = "";
        } finally {
            sdfFrom = null;
        }

        return strResult;
    }

    /**
     * 查询职工岗位信息
     * @param strEmpID 人员id
     * @param strEnterpriseCode 企业代码
     * @return 职工岗位信息
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public PageObject findEmpStationInfo(String strEmpID,
            String strEnterpriseCode, final int... rowStartIdxAndCount)
        throws SQLException {
        LogUtil.log("查询职工岗位信息开始: ", Level.INFO, null);
        try {
            PageObject pobj = new PageObject();
            StringBuffer sbd = new StringBuffer();
            // SELECT 文
            sbd.append("SELECT ");
            sbd.append("A.STATION_ID, ");
            sbd.append("B.STATION_NAME, ");
            sbd.append("A.IS_MAIN_STATION, ");
            sbd.append("A.EMP_STATION_ID, ");
            sbd.append("A.MEMO, ");
            sbd.append("D.STATION_LEVEL_NAME, ");
            sbd.append("TO_CHAR(A.LAST_MODIFIED_DATE, 'yyyy-MM-DD HH24:mi:ss') ");
            // SELECT的长度
            int intSbdSelect = sbd.length();
            // FROM文
            sbd.append("FROM HR_J_EMP_STATION A, ");
            sbd.append("HR_C_STATION B ");
            sbd.append("LEFT JOIN HR_C_STATION_LEVEL D ");
            sbd.append("ON D.STATION_LEVEL_ID = B.STATION_LEVEL_ID ");
            sbd.append("AND D.ENTERPRISE_CODE = ? ");
            sbd.append("AND D.IS_USE = ?  ");
            // WHERE文
            sbd.append("WHERE A.EMP_ID = ? ");
            sbd.append("AND A.STATION_ID = B.STATION_ID ");
            sbd.append("AND A.ENTERPRISE_CODE = ? ");
            sbd.append("AND A.IS_USE = ? ");
            sbd.append("AND B.ENTERPRISE_CODE = ? ");
            sbd.append("AND B.IS_USE = ? ");
            Object[] params = new Object[7];
            int i =0;
            params[i++] = strEnterpriseCode;
            // modified by liuyi 091205
//            params[i++] =IS_USE_Y;
            params[i++] ="Y"; //update by sychen 20100901 
//            params[i++] ="U"; 
            params[i++] = strEmpID;
            params[i++] = strEnterpriseCode;
            params[i++] =IS_USE_Y;
            params[i++] = strEnterpriseCode;
         // modified by liuyi 091205
//            params[i++] =IS_USE_Y;
            params[i++] ="Y"; //update by sychen 20100901 
//            params[i++] ="U"; 
            // 执行查询
            List<EmpStationMaintainInfo> list = bll.queryByNativeSQL(
                    sbd.toString(), params, rowStartIdxAndCount);
            // 打印sql文
            LogUtil.log("sql 文：" + sbd.toString(), Level.INFO, null);
            // SqlCount文
            StringBuffer sbdCount = new StringBuffer();
            sbdCount.append("SELECT ");
            sbdCount.append("COUNT(A.EMP_STATION_ID) ");
            //sqlCount
            String sqlCount = sbdCount.toString()+sbd.substring(intSbdSelect, sbd.length());
            // 执行查询
            Long totalCount = Long
                .parseLong(bll.getSingal(sqlCount,params).toString());
            // 打印sql文
            LogUtil.log("sql 文：" + sqlCount, Level.INFO, null);
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
                    // 主岗位
                    if (data[2] != null) {
                        empStationMaintainInfo.setMainStation(data[2].toString());
                    }
                    // 职工岗位id
                    if(data[3] != null) {
                        empStationMaintainInfo.setEmpStationId(data[3].toString());
                    }
                    // 备注
                    if(data[4] != null) {
                        empStationMaintainInfo.setMemo(data[4].toString());
                    }
                    // 岗位级别
                    if (data[5] != null) {
                        empStationMaintainInfo.setStationLevel(data[5].toString());
                    }
                    // 修改时间
                    if (data[6] != null) {
                        empStationMaintainInfo.setLastModifyDate(data[6].toString());
                    }
                    // 判断DB中是否存在的flag
                    empStationMaintainInfo.setDbFlag("true");
                    arrList.add(empStationMaintainInfo);
                }
            }
            // 设置PageObject
            pobj.setList(arrList);
            pobj.setTotalCount(totalCount);
            // 返回
            LogUtil.log("查询职工岗位信息结束: ",Level.INFO, null);
            return pobj;
        } catch (RuntimeException e) {
            LogUtil.log("查询职工岗位信息失败: ",Level.INFO, null);
            throw new SQLException(e.getMessage());
        }
        
    }
    
    /**
	 * 查询职工岗位信息
	 * 
	 * @param empId 人员id
	 * @param enterpriseCode 企业代码
	 * @return PageObject
     * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findEmpStationInfoByEmpId(String empId,
			String enterpriseCode) throws SQLException {
		LogUtil.log("EJB:查询职工岗位信息开始。", Level.INFO, null);
		try {
			PageObject object = new PageObject();
			// sql
			String sql =
				"SELECT * " +
				"FROM HR_J_EMP_STATION A " +
				"WHERE " +
					"A.EMP_ID = ? AND " +
					"A.IS_MAIN_STATION = ? AND " +
					"A.IS_USE = ? AND " +
					"A.ENTERPRISE_CODE = ? ";
			List<HrJEmpStation> list = bll.queryByNativeSQL(sql,
        			new Object[] {empId, IS_MAIN_STATION,
					IS_USE_Y, enterpriseCode}, HrJEmpStation.class);
        	if(list == null) {
        		list = new ArrayList<HrJEmpStation>();
        	}
            object.setList(list);
            object.setTotalCount((long)list.size());
			LogUtil.log("EJB:查询职工岗位信息结束。", Level.INFO, null);
			return object;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:查询职工岗位信息失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}
}
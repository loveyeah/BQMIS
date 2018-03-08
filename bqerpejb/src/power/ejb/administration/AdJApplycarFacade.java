package power.ejb.administration;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
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
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.CarApplyInfo;
import power.ejb.administration.form.CarUseApplyInfo;
import power.ejb.administration.form.VehicleDispatchBean;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJApplycar.
 * 
 * @see power.ejb.administration.AdJApplycar
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJApplycarFacade implements AdJApplycarFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "AdJCarfileFacade")
	AdJCarfileFacadeRemote adjfileremote;

	/**
	 *增加用车申请信息
	 * @author Li Chensheng
	 */
	public void save(AdJApplycar entity) throws SQLException {
		LogUtil.log("EJB:增加用车申请信息开始。", Level.INFO, null);
		try {
			Long id = bll.getMaxId("AD_J_APPLYCAR", "id");
			entity.setId(id);
			entityManager.persist(entity);
			LogUtil.log("EJB:增加用车申请信息成功", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("EJB:增加用车申请信息失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * Delete a persistent AdJApplycar entity.
	 * 
	 * @param entity
	 *            AdJApplycar entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJApplycar entity) {
		LogUtil.log("deleting AdJApplycar instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdJApplycar.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 *  更新用车申请信息
	 * @author Li Chensheng	 
	 */
	public AdJApplycar update(AdJApplycar entity) throws SQLException {
		LogUtil.log("EJB：更新用车申请信息开始。", Level.INFO, null);
		try {
			AdJApplycar result = entityManager.merge(entity);				
			LogUtil.log("EJB：更新用车申请信息成功。", Level.INFO, null);
			return result;		
		} catch (Exception e) {
			LogUtil.log("EJB：更新用车申请信息失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 *  更新用车申请信息
	 * @author xstan
	 * @param entity 用车申请实体
	 * @param strUpdateTime 用车申请实体取得时的更新时间
	 * @throws SQLException
	 * @throws DataChangeException
	 */
	public void update(AdJApplycar entity, Long updateTime) throws SQLException, DataChangeException {
		LogUtil.log("EJB：更新用车申请信息开始。", Level.INFO, null);
		try {
			AdJApplycar objNew = this.findById(entity.getId());
			// 取得DB中原数据的更新时间
			Long lngUpdateTimeOld = objNew.getUpdateTime().getTime();
			// 排他处理
			if (!lngUpdateTimeOld.equals(updateTime)) {
				throw new DataChangeException("");
			}
			// 设置更新时间
			entity.setUpdateTime(new Date());
			AdJApplycar result = entityManager.merge(entity);				
			LogUtil.log("EJB：更新用车申请信息成功。", Level.INFO, null);
		} catch (DataChangeException de) {
			LogUtil.log("EJB：更新用车申请信息失败", Level.SEVERE, de);
			throw de;
		} catch (Exception e) {
			LogUtil.log("EJB：更新用车申请信息失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	public AdJApplycar findById(Long id) throws SQLException {
		LogUtil.log("finding AdJApplycar instance with id: " + id, Level.INFO,
				null);
		try {
			AdJApplycar instance = entityManager.find(AdJApplycar.class, id);
			return instance;
		} catch (Exception e) {
			LogUtil.log("find failed", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * Find all AdJApplycar entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJApplycar property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJApplycar> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJApplycar> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJApplycar instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJApplycar model where model."
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
	 * Find all AdJApplycar entities.
	 * 
	 * @return List<AdJApplycar> all AdJApplycar entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJApplycar> findAll() {
		LogUtil.log("finding all AdJApplycar instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJApplycar model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 从申请单号查询派车单
	 *
	 * @param strApplyNo 申请单号
	 * @param strEnterpriseCode 企业代码
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 派车单
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByApplyNo(String strApplyNo, String strEnterpriseCode, final int... rowStartIdxAndCount)
	throws ParseException {
		LogUtil.log("EJB:查询派车单实例开始", Level.INFO, null);
		PageObject pobj = new PageObject();
		// 数字输出格式化
		String patternNumber = "###,###,###,###,##0.00";
		DecimalFormat dfNumber = new DecimalFormat(patternNumber);
		String patternNum = "###,###,###,###,##0";
		DecimalFormat dfNum = new DecimalFormat(patternNum);
		String sql;
		Object[] params = new Object[6];
		params[0] = strEnterpriseCode;
		params[1] = strEnterpriseCode;
		params[2] = strEnterpriseCode;
		params[3] = 'Y';
		params[4] = strApplyNo;
		params[5] = strEnterpriseCode;
		sql = "SELECT D.DEPT_NAME, " +
		"C.CHS_NAME AS APPLYMAN, " +
		"A.USE_NUM, " +
		"A.REASON, " +
		"A.AIM, " +
		"TO_CHAR(A.USE_DATE,'yyyy-mm-dd'), " +
		"DECODE(A.IF_OUT,'Y','是','N','否'), " +
		"A.USE_DAYS, " +
		"A.DEP_IDEA, " +
		"A.DEP_BOSS_CODE, " +
		"TO_CHAR(A.DEP_SIGN_DATE,'yyyy-mm-dd hh24:mi'), " +
		"A.XZ_BOSS_IDEA, " +
		"A.XZ_BOSS_CODE, " +
		"TO_CHAR(A.XZ_SIGN_DATE,'yyyy-mm-dd hh24:mi'), " +
		"A.BIG_BOSS_IDEA, " +
		"A.BIG_BOSS_CODE, " +
		"TO_CHAR(A.BIG_BOSS_SIGN_DATE,'yyyy-mm-dd hh24:mi'), " +
		"A.GO_MILE, " +
		"A.COME_MILE, " +
		"A.DISTANCE, " +
		"A.CAR_NO, " +
		"B.CHS_NAME AS DRIVER, " +
		"A.LQ_PAY, " +
		"A.USE_OIL " +
		"FROM  AD_J_APPLYCAR A " +
		"LEFT JOIN HR_J_EMP_INFO B " +
		"ON A.DRIVER = B.EMP_CODE " +
		"AND B.ENTERPRISE_CODE = ? " +
		"LEFT JOIN HR_J_EMP_INFO C " +
		"ON A.APPLY_MAN = C.EMP_CODE  " +
		"AND C.ENTERPRISE_CODE = ? " +
		"LEFT JOIN HR_C_DEPT D " +
		"ON C.DEPT_ID = D.DEPT_ID " +
		"AND D.ENTERPRISE_CODE = ? " +
		"WHERE A.IS_USE = ? AND " +
		// xsTan 修改开始 2009-2-3 车辆派遣单报表检索条件修正A.ID改为A.CAR_APPLY_ID
		"A.CAR_APPLY_ID = ? AND " +
		// xsTan 修改结束 2009-2-3
		"A.ENTERPRISE_CODE = ? ";
		List<VehicleDispatchBean> list = bll.queryByNativeSQL(sql, params);
		List<VehicleDispatchBean> lstVD= new ArrayList<VehicleDispatchBean>();
		if (list != null) {
			Iterator it = list.iterator();
			while (it.hasNext()) { 
				Object[] data = (Object[]) it.next();
				VehicleDispatchBean vehicleDispatchBean = new VehicleDispatchBean();
				// 用车部门
				if (data[0] != null) {
					vehicleDispatchBean.setDep(data[0].toString());
				}
				// 申请人
				if (data[1] != null) {
					vehicleDispatchBean.setApplyMan(data[1].toString());
				}
				// 用车人数
				if (data[2] != null) {
					vehicleDispatchBean.setUseNum(data[2].toString());
				}
				// 用车事由
				if (data[3] != null) {
					vehicleDispatchBean.setReason(data[3].toString());
				}
				// 到达地点
				if (data[4] != null) {
					vehicleDispatchBean.setAim(data[4].toString());
				}
				// 用车时间
				if (data[5] != null) {
					vehicleDispatchBean.setUseDate(data[5].toString());
				}
				// 是否出省
				if (data[6] != null) {
					vehicleDispatchBean.setIfOut(data[6].toString());
				}
				// 用车天数
				if (data[7] != null) {
					vehicleDispatchBean.setUseDays(dfNum.format(data[7]));
				}
				// 部门意见
				if (data[8] != null) {
					vehicleDispatchBean.setDepIDEA(data[8].toString());
				}
				// 部门经理姓名
				if (data[9] != null) {
					vehicleDispatchBean.setDepBossCode(data[9].toString());
				}
				// 部门经理签字时间
				if (data[10] != null) {
					vehicleDispatchBean.setDepSignDate(data[10].toString());
				}
				// 行政部意见
				if (data[11] != null) {
					vehicleDispatchBean.setXzBossIDEA(data[11].toString());
				}
				// 行政部经理姓名
				if (data[12] != null) {
					vehicleDispatchBean.setXzBossCode(data[12].toString());
				}
				// 行政部经理签字时间
				if (data[13] != null) {
					vehicleDispatchBean.setXzSignDate(data[13].toString());
				}
				// 总经理意见
				if (data[14] != null) {
					vehicleDispatchBean.setBigBossIDEA(data[14].toString());
				}
				// 总经理姓名
				if (data[15] != null) {
					vehicleDispatchBean.setBigBossCode(data[15].toString());
				}
				// 总经理签字时间
				if (data[16] != null) {
					vehicleDispatchBean.setBigBossSignDate(data[16].toString());
				}
				// 发车里程
				if (data[17] != null) {
					vehicleDispatchBean.setGoMile(dfNum.format(data[17]));
				}
				// 收车里程
				if (data[18] != null) {
					vehicleDispatchBean.setComeMile(dfNum.format(data[18]));
				}
				// 行车里程
				if (data[19] != null) {
					vehicleDispatchBean.setDistance(dfNumber.format(data[19]));
				}
				// 车牌号
				if (data[20] != null) {
					vehicleDispatchBean.setCarNo(data[20].toString());
				}
				// 司机
				if (data[21] != null) {
					vehicleDispatchBean.setDriver(data[21].toString());
				}
				
				// 路桥费
				if (data[22] != null) {
					vehicleDispatchBean.setLqPay(dfNumber.format(data[22]));
				}
				// 油费
				if (data[23] != null) {
					vehicleDispatchBean.setUseOil(dfNumber.format(data[23]));
				}
				
				lstVD.add(vehicleDispatchBean);
			}
		}
		if(lstVD.size()>0)
		{
			// 符合条件的派车单
			pobj.setList(lstVD);
			// 符合条件的派车单的总数 
			pobj.setTotalCount(Long.parseLong(lstVD.size() + ""));
		}	
		LogUtil.log("EJB:查询派车单实例结束", Level.INFO, null);
		return pobj;
	}

	/**
	 * 从查询用车申请
	 *
	 * @param strStartDate 用车开始时间
	 * @param strEndDate 用车结束时间
	 * @param strdeptCode 用车部门
	 * @param strWorkerCode 申请人
	 * @param strDriver 司机
	 * @param strDcmStatus 上报状态
	 * @param strEnterpriseCode 企业代码
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 派车单
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getApplyCarInfo(String strStartDate,String strEndDate,String strDeptCode,String strWorkerCode,
			String strDriver, String strDcmStatus, String strEnterpriseCode,
	        final int ...rowStartIdxAndCount) throws SQLException {
		LogUtil.log("EJB:查询用车申请实例开始", Level.INFO, null);
		PageObject pobj = new PageObject();
		String strSql;
		strSql = "SELECT " +
		"DECODE(A.DCM_STATUS, '0','未上报','1','已上报','2','已终结','3','已退回'), " +
		"D.CHS_NAME AS APPLYMAN, " +
		"B.DEPT_NAME, " +
		"TO_CHAR(A.USE_DATE,'yyyy-mm-dd'), " +
		"A.USE_NUM, " +
		"DECODE(A.IF_OUT,'Y','是','N','否')," +
		"A.LQ_PAY, " +
		"A.USE_OIL, " +
		"A.DISTANCE, " +
		"A.GO_MILE, " +
		"A.COME_MILE, " +
		"A.REASON, " +
		"TO_CHAR(A.START_TIME,'YYYY-mm-dd hh24:mi'), " +
		"TO_CHAR(A.END_TIME,'YYYY-mm-dd hh24:mi'), " +
		"A.AIM, " +
		"A.CAR_NO, " +
		"C.CHS_NAME AS DRIVER " +
		"FROM  AD_J_APPLYCAR A " +
		"LEFT JOIN HR_J_EMP_INFO C " +
		"ON A.DRIVER = C.EMP_CODE  " +
		"AND C.ENTERPRISE_CODE = ? " +
		"LEFT JOIN HR_J_EMP_INFO D " +
		"ON A.APPLY_MAN = D.EMP_CODE " +
		"AND D.ENTERPRISE_CODE = ? " +
		"LEFT JOIN HR_C_DEPT B " +
		"ON D.DEPT_ID = B.DEPT_ID " +
		"AND B.ENTERPRISE_CODE = ? " ;
		String strSqlWhere = "WHERE " +
		"A.IS_USE = ? AND " +
		"A.ENTERPRISE_CODE = ? ";
		String sqlCount = "SELECT" 
	            + " COUNT(A.ID)"
	    		+"FROM  AD_J_APPLYCAR A " +
	    		"LEFT JOIN HR_J_EMP_INFO C " +
	    		"ON A.DRIVER = C.EMP_CODE  " +
	    		"AND C.ENTERPRISE_CODE = ? " +
	    		"LEFT JOIN HR_J_EMP_INFO D " +
	    		"ON A.APPLY_MAN = D.EMP_CODE " +
	    		"AND D.ENTERPRISE_CODE = ? " +
	    		"LEFT JOIN HR_C_DEPT B " +
	    		"ON D.DEPT_ID = B.DEPT_ID " +
	    		"AND B.ENTERPRISE_CODE = ? " ;         
		// 查询参数数量
		int paramsCnt = 5;
		if(strStartDate != null && !"".equals(strStartDate)){
			paramsCnt++;
		}
		if(strEndDate != null && !"".equals(strEndDate)){
			paramsCnt++;
		}
		if(strDeptCode != null && !"".equals(strDeptCode)){
			paramsCnt++;
		}
		if(strWorkerCode != null && !"".equals(strWorkerCode)){
			paramsCnt++;
		}
		if(strDriver != null && !"".equals(strDriver)){
			paramsCnt++;
		}
		if(strDcmStatus != null && !"".equals(strDcmStatus)){
			paramsCnt++;
		}
		Object[] params = new Object[paramsCnt];
		int i = 0;
		params[i++] = strEnterpriseCode;
		params[i++] = strEnterpriseCode;
		params[i++] = strEnterpriseCode;
		params[i++] = 'Y';
		params[i++] = strEnterpriseCode;
		// 用车开始时间
		if(strStartDate != null && !"".equals(strStartDate)){
			strSqlWhere += "  AND TO_CHAR(A.USE_DATE,'yyyy-mm-dd') >= ? ";
			params[i++] = strStartDate;
		}
		// 用车结束时间
		if(strEndDate != null && !"".equals(strEndDate)){
			strSqlWhere += "  AND TO_CHAR(A.USE_DATE,'yyyy-mm-dd') <= ? ";
			params[i++] = strEndDate;
		}
		// 用车部门
		if(strDeptCode != null && !"".equals(strDeptCode)){
			strSqlWhere += "  AND B.DEPT_CODE like ? ";
			params[i++] = strDeptCode +"%";
		}
		// 申请人
		if(strWorkerCode != null && !"".equals(strWorkerCode)){
			strSqlWhere += "  AND A.APPLY_MAN = ? ";
			params[i++] = strWorkerCode;
		}
		// 司机
		if(strDriver != null && !"".equals(strDriver)){
			strSqlWhere += "  AND A.DRIVER = ? ";
			params[i++] = strDriver;
		}
		// 上报状态
		if(strDcmStatus != null && !"".equals(strDcmStatus)){
			strSqlWhere += "  AND A.DCM_STATUS= ? ";
			params[i++] = strDcmStatus;
		}
		strSql =strSql + strSqlWhere;
		sqlCount = sqlCount + strSqlWhere;
        LogUtil.log("SQL：" + strSql, Level.INFO, null);
		List<VehicleDispatchBean> list = bll.queryByNativeSQL(strSql, params);
		Object objCount = bll.getSingal(sqlCount,params);
		long lngSize = Long.parseLong(objCount.toString());
		List<VehicleDispatchBean> lstVD= new ArrayList<VehicleDispatchBean>();
		if (list != null) {
			Iterator it = list.iterator();
			while (it.hasNext()) { 
				Object[] data = (Object[]) it.next();
				VehicleDispatchBean vehicleDispatchBean = new VehicleDispatchBean();
				// 上报状态
				if (data[0] != null) {
					vehicleDispatchBean.setDcmStatus(data[0].toString());
				}
				// 申请人
				if (data[1] != null) {
					vehicleDispatchBean.setApplyMan(data[1].toString());
				}
				// 用车部门
				if (data[2] != null) {
					vehicleDispatchBean.setDep(data[2].toString());
				}
				// 用车时间
				if (data[3] != null) {
					vehicleDispatchBean.setUseDate(data[3].toString());
				}
				// 用车人数
				if (data[4] != null) {
					vehicleDispatchBean.setUseNum(data[4].toString());
				}
				// 是否出省
				if (data[5] != null) {
					vehicleDispatchBean.setIfOut(data[5].toString());
				}
				// 路桥费
				if (data[6] != null) {
					vehicleDispatchBean.setLqPay(data[6].toString());
				}
				// 油费
				if (data[7] != null) {
					vehicleDispatchBean.setUseOil(data[7].toString());
				}
				// 行车里程
				if (data[8] != null) {
					vehicleDispatchBean.setDistance(data[8].toString());
				}
				// 发车里程
				if (data[9] != null) {
					vehicleDispatchBean.setGoMile(data[9].toString());
				}
				// 收车里程
				if (data[10] != null) {
					vehicleDispatchBean.setComeMile(data[10].toString());
				}
				// 用车事由
				if (data[11] != null) {
					vehicleDispatchBean.setReason(data[11].toString());
				}
				// 发车时间
				if (data[12] != null) {
					vehicleDispatchBean.setStartTime(data[12].toString());
				}
				// 收车时间
				if (data[13] != null) {
					vehicleDispatchBean.setEndTime(data[13].toString());
				}
				// 到达地点
				if (data[14] != null) {
					vehicleDispatchBean.setAim(data[14].toString());
				}
				// 车牌号
				if (data[15] != null) {
					vehicleDispatchBean.setCarNo(data[15].toString());
				}
				// 司机
				if (data[16] != null) {
					vehicleDispatchBean.setDriver(data[16].toString());
				}
				lstVD.add(vehicleDispatchBean);
			}
		}
		if(lstVD.size()>0)
		{
			// 符合条件的用车申请
			pobj.setList(lstVD);
			// 符合条件的用车申请的总数 
			pobj.setTotalCount(lngSize);
		}	
		LogUtil.log("EJB:查询用车申请实例结束", Level.INFO, null);
		return pobj;
	}
	/**
     * 用车申请：查询用车申请信息
     * @author Li Chensheng
     * @param 
     * @return PageObject  查询结果
     */
    @SuppressWarnings("unchecked")
	public  PageObject findCarApply(String workerCode,String enterpriseCode,final int... rowStartIdxAndCount)
    	throws SQLException {
    	LogUtil.log("EJB:查询用车申请信息开始。", Level.INFO, null);
		try {
            PageObject  result = new PageObject();
            Object[] params = new Object[5];
            params[0] = enterpriseCode;
            params[1] = enterpriseCode;
            params[2] = "Y";
			params[3] = workerCode;
			params[4] = enterpriseCode;
            // 查询sql
            String sql = "SELECT"
            	         + " A.ID,"
            	         + " C.CHS_NAME,"
            	         + " B.DEPT_NAME,"
            	         + " A.IF_OUT,"
            	         + " A.USE_NUM,"
            	         + " A.USE_DATE,"
            	         + " A.USE_DAYS,"
            	         + " A.AIM,"
            	         + " A.REASON,"
            	         + " A.APPLY_MAN,"
            	         + " TO_CHAR(A.UPDATE_TIME, 'YYYY-MM-DD HH24:MI:SS')"
            	         + " FROM AD_J_APPLYCAR A "
            	         + " LEFT JOIN HR_J_EMP_INFO C ON A.APPLY_MAN = C.EMP_CODE AND C.ENTERPRISE_CODE = ? "
            	         + " LEFT JOIN HR_C_DEPT B ON C.DEPT_ID = B.DEPT_ID AND B.ENTERPRISE_CODE = ? "
            	         + " WHERE  A.DCM_STATUS IN ('0','3')"
            	         + " AND A.IS_USE = ? "
                         + " AND A.APPLY_MAN = ? "
                         + " AND A.ENTERPRISE_CODE = ? ";
            	     

            String sqlCount = "SELECT" 
            	            + " COUNT(A.ID)"
            	            + " FROM AD_J_APPLYCAR A "
               	            + " LEFT JOIN HR_J_EMP_INFO C ON A.APPLY_MAN = C.EMP_CODE AND C.ENTERPRISE_CODE = ? "
               	            + " LEFT JOIN HR_C_DEPT B ON C.DEPT_ID = B.DEPT_ID AND B.ENTERPRISE_CODE = ?"
               	            + " WHERE  A.DCM_STATUS IN ('0','3')"
               	            + " AND A.IS_USE = ? "
                            + " AND A.APPLY_MAN = ? "
                            + " AND A.ENTERPRISE_CODE = ? ";	         
            LogUtil.log("SQL：" + sql, Level.INFO, null);
            //获得记录数
			Object objCount = bll.getSingal(sqlCount,params);
			long lngSize = Long.parseLong(objCount.toString());
			//获得记录
			List lstRecord = bll.queryByNativeSQL(sql,params,rowStartIdxAndCount);
			List<CarUseApplyInfo> arrList = new ArrayList<CarUseApplyInfo>();
			Iterator it = lstRecord.iterator();			
			//对数据记录进行循环得到
			while(it.hasNext()){				
				Object[] data = (Object[]) it.next();
				CarUseApplyInfo info = new CarUseApplyInfo();
				//设置CarPassSearchInfo信息
				//插入流水号
				if(data[0]!=null)
					info.setId(Long.parseLong(data[0].toString()));
				//申请人
				if (data[1] != null)
					info.setName(data[1].toString());
				//申请人部门
				if (data[2] != null)
					info.setDepName(data[2].toString());
				// 是否出省
				if (data[3] != null)
					info.setIfOut(data[3].toString());
				// 用车人数
				if (data[4] != null)
					info.setUseNum(Long.parseLong(data[4].toString()));
				// 用车时间
				if (data[5] != null)
					info.setUseDate(data[5].toString());
				// 用车天数
				if (data[6] != null)
					info.setUseDays(Double.parseDouble(data[6].toString()));
				// 到达目的
				if (data[7] != null)
					info.setAim(data[7].toString());
				// 用车理由
				if (data[8] != null)
					info.setReason(data[8].toString());
				// 申请人编码
				if (data[9] != null)
					info.setApplyMan(data[9].toString());
				// 更新时间
				if (data[10] != null)
					info.setUpdateTime(data[10].toString());				
				arrList.add(info);
				}
			result.setList(arrList);
			result.setTotalCount(lngSize);
			LogUtil.log("EJB:查询用车申请信息结束。", Level.INFO, null);
			return result;			
		} catch (Exception e) {
			LogUtil.log("EJB:查询用车申请信息失败。", Level.SEVERE, e);
			throw new SQLException();
        }
    }
    /**
	 *  删除一条用车申请信息
	 * @author Li Chensheng
	 * @throws  SQLException
	 * 
	 */
    public void delete(Long id, String workerCode)throws SQLException{
    	LogUtil.log("EJB：删除用车申请信息开始。", Level.INFO, null);
		try {
			// 取得原数据
			AdJApplycar objBean = this.findById(id);
			// 取得原数据的更新时间
			objBean.setUpdateTime(new Date());
			objBean.setUpdateUser(workerCode);
			objBean.setIsUse("N");
			entityManager.merge(objBean);
			LogUtil.log("EJB：删除用车申请信息成功", Level.INFO, null);			
		} catch (Exception e) {
			LogUtil.log("EJB：删除用车申请信息失败", Level.SEVERE, e);
			throw new SQLException();
		}
    }
    /**
	 * 上报用车申请
	 */
	public void updateState(Long id,String workerCode) 
	    throws SQLException{
		try {
			// 取得用车申请 Bean
			AdJApplycar objBean = this.findById(id);
			objBean.setUpdateTime(new Date());
			objBean.setUpdateUser(workerCode);
			// 上报状态
			objBean.setDcmStatus("1");
			entityManager.merge(objBean);
			LogUtil.log("EJB:上报时修改单据状态信息结束。" , Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("EJB:上报时修改单据状态信息失败。" , Level.SEVERE, e);
			throw new SQLException();
		}
	}
	
	/**
	 * 用车管理：条件查询用车申请详情
	 * @author Li Chensheng
	 * @throws  SQLException
	 */
	public PageObject findCarApplyBy(String startDate,String endDate,
            String depCode,String workerCode,
            String driverCode,String drpCarStatus,String enterpriseCode,
            final int ...rowStartIdxAndCount)
	       throws SQLException{
		try{
			LogUtil.log("EJB:查询用车申请实例开始", Level.INFO, null);
			PageObject result = new PageObject();
			Object[] params = new Object[7];
			params[0] = enterpriseCode;
			params[1] = enterpriseCode;
			params[2] = enterpriseCode;
			params[3] = enterpriseCode;
			params[4] = "2";
			params[5] = "Y";
			params[6] = enterpriseCode;
			String strSql;
			// SQL
			strSql = "SELECT " 
				  +" C.ID,"
			      +" A.CHS_NAME as reqName,"
			      +" D.DEPT_NAME,"
			      +" C.AIM,"
			      +" B.CHS_NAME as driverName,"
			      +" C.USE_DATE,"
			      +" TO_CHAR(C.START_TIME,'YYYY-MM-DD HH24:MI'), "
			      +" TO_CHAR(C.END_TIME,'YYYY-MM-DD HH24:MI'), "
			      +" C.Use_NUM,"
			      +" C.IF_OUT,"
			      +" C.USE_DAYS,"
			      +" C.REASON,"
			      +" C.GO_MILE,"		      
			      +" C.CAR_NO,"
			      +" C.COME_MILE,"
			      +" C.USE_OIL,"
			      +" C.LQ_PAY,"		      
			      +" E.ID as carId,"
			      +" C.DRIVER as driverCode,"
			      +" C.DISTANCE as distance,"
			      +" TO_CHAR(C.UPDATE_TIME,'YYYY-MM-DD HH24:mi:SS') AS CARAPPUPDATETIME," 
			      +" TO_CHAR(E.UPDATE_TIME,'YYYY-MM-DD HH24:mi:SS') AS CARFILEUPDATETIME,"
			      +" C.CAR_APPLY_ID "
//			      +" E.IS_USE"
			      // 左连接
			      + " FROM AD_J_APPLYCAR C "
      	          + " LEFT JOIN HR_J_EMP_INFO B ON C.DRIVER = B.EMP_CODE AND B.ENTERPRISE_CODE = ? "
        	      + " LEFT JOIN HR_J_EMP_INFO A ON C.APPLY_MAN = A.EMP_CODE AND A.ENTERPRISE_CODE = ? "
        	      + " LEFT JOIN HR_C_DEPT D ON A.DEPT_ID = D.DEPT_ID AND D.ENTERPRISE_CODE = ? "
			      + " LEFT JOIN AD_J_CARFILE E ON C.CAR_NO = E.CAR_NO AND E.IS_USE = 'Y' AND E.ENTERPRISE_CODE = ? "	
			      + " WHERE C.DCM_STATUS = ? "
			      + " AND C.IS_USE = ? "
			      + " AND C.ENTERPRISE_CODE = ? "
			      + " AND C.End_Time IS   NULL ";
			//查询sql文,其结果的数量
			String strSqlCount = "SELECT COUNT(C.ID) "
				                // 左连接
			                   + " FROM AD_J_APPLYCAR C "
    	                       + " LEFT JOIN HR_J_EMP_INFO B ON C.DRIVER = B.EMP_CODE AND B.ENTERPRISE_CODE = ? "
      	                       + " LEFT JOIN HR_J_EMP_INFO A ON C.APPLY_MAN = A.EMP_CODE AND A.ENTERPRISE_CODE = ? "
      	                       + " LEFT JOIN HR_C_DEPT D ON A.DEPT_ID = D.DEPT_ID AND D.ENTERPRISE_CODE = ? "
			                   + " LEFT JOIN AD_J_CARFILE E ON C.CAR_NO = E.CAR_NO AND E.IS_USE = 'Y'  AND E.ENTERPRISE_CODE = ? "	      
			                   + " WHERE C.DCM_STATUS = ? "
			                   + " AND C.IS_USE = ? "
			                   + " AND C.ENTERPRISE_CODE = ? "
			                   + " AND C.End_Time IS   NULL ";
			//查询sql文的条件拼接
			String strSqlWhere ="";
			// 用车开始时间
			if((null!=startDate) && (!"".equals(startDate)))
				strSqlWhere += "  AND '"
					     +  startDate
				         +"'<= TO_CHAR(C.Use_Date,'YYYY-MM-DD') ";
			// 用车结束时间
			if ((endDate != null) && (!endDate.equals(""))) {
				strSqlWhere += "  AND '"
				     +  endDate
			         +"'>= TO_CHAR(C.Use_Date,'YYYY-MM-DD') ";
			}
			// 用车部门
			if((depCode != null) && (!"".equals(depCode)))
			{
				strSqlWhere +="  AND D.DEPT_CODE LIKE '"+depCode+"%' ";
			}
			// 申请人
			if((workerCode != null) && (!"".equals(workerCode)))
			{
				strSqlWhere +="  AND C.APPLY_MAN = '"+workerCode+"' ";
			}
			// 司机
			if((driverCode != null) && (!"".equals(driverCode)))
			{
				strSqlWhere +="  AND C.DRIVER = '"+driverCode+"' ";
			}
			// 出车状态
			if((drpCarStatus != null) &&  ("1".equals(drpCarStatus)))
			{
				strSqlWhere +="  AND C.START_TIME IS NULL ";
			}
			if((drpCarStatus != null) && ("2".equals(drpCarStatus)))
			{
				strSqlWhere +="  AND C.START_TIME IS  NOT NULL ";
				strSqlWhere +="  AND C.End_Time IS   NULL ";
			}
//			if((drpCarStatus != null) && ("3".equals(drpCarStatus)))
//			{
//				strSqlWhere +="  AND C.START_TIME IS  NOT NULL ";
//				strSqlWhere +="  AND C.End_Time IS NOT NULL ";
//			}
			//拼接SQL文
			strSql =strSql+strSqlWhere;
			strSqlCount = strSqlCount+strSqlWhere;
			//log
			LogUtil.log("EJB: 条件查询用车申请详情SQL="+strSql,
	                Level.INFO, null);
			//获得记录数
			List lstCount = bll.queryByNativeSQL(strSqlCount,params);
			long lngSize = Long.parseLong(lstCount.get(0).toString());
			//获得记录
			List lstRecord = bll.queryByNativeSQL(strSql,params,rowStartIdxAndCount);

			List<CarApplyInfo> arrList = new ArrayList<CarApplyInfo>();
			Iterator it = lstRecord.iterator();
			
			//对数据记录进行循环得到
			while(it.hasNext()){
				
				Object[] data = (Object[]) it.next();
				CarApplyInfo info = new CarApplyInfo();
				//设置CarPassSearchInfo信息
				if(data[0]!=null)
					info.setId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					info.setReqName(data[1].toString());
				if (data[2] != null)
					info.setDeptName(data[2].toString());
				if (data[3] != null)
					info.setAim(data[3].toString());
				if (data[4] != null)
					info.setDriverName(data[4].toString());
				if (data[5] != null)
					info.setUseDate(data[5].toString());
				if (data[6] != null)
					info.setStartTime(data[6].toString());
				if (data[7] != null)
					info.setEndTime(data[7].toString());
				if (data[8] != null)
					info.setUseNum(Long.parseLong(data[8].toString()));
				if (data[9] != null)
					info.setIfOut(data[9].toString());
				if (data[10] != null)
					info.setUseDays(Double.parseDouble(data[10].toString()));
				if (data[11] != null)
					info.setReason(data[11].toString());
				if (data[12] != null)
					info.setGoMile(data[12].toString());
				if (data[13] != null)
					info.setCarNo(data[13].toString());
				if (data[14] != null)
					info.setComeMile(data[14].toString());
				if (data[15] != null)
					info.setUseOil(data[15].toString());
				if (data[16] != null)
					info.setLqPay(data[16].toString());				
				if (data[17]!=null)
					info.setCarId(Long.parseLong(data[17].toString()));
				if (data[18]!=null)
					info.setDriverCode(data[18].toString());
				if (data[19]!=null)
					info.setDistance(data[19].toString());
				if (data[20] != null){
					AdJApplycar entity = findById(new Long(data[0].toString()));
					info.setUpdateTime(entity.getUpdateTime().getTime());
				}
				if (data[21] != null){				
					AdJCarfile entity = adjfileremote.findById(new Long(data[17].toString()));
					info.setCarFileUpdateTime(entity.getUpdateTime().getTime());
				}
				if (data[22]!=null){
					info.setCarApplyId(data[22].toString());
				}
//				if (data[23]!=null){
//					info.setIsUse(data[23].toString());
//				}
				arrList.add(info);
				}
			result.setList(arrList);
			result.setTotalCount(lngSize);
			LogUtil.log("EJB: 进出车辆查询正常结束。", Level.INFO, null);
			return result;
		}catch(Exception  e) {
			LogUtil.log("EJB:查询车辆信息失败。", Level.SEVERE, null);
			throw new SQLException();
        }
	}
	
	/**
	 *  用车管理：删除一条用车申请信息
	 * @author Li Chensheng
	 * @throws DataChangeException, SQLException
	 * 
	 */
    public void delete(Long id, String workerCode, Long updateTime)throws SQLException, DataChangeException{
    	LogUtil.log("EJB：删除用车申请信息开始。", Level.INFO, null);
		try {
			// 取得原数据
			AdJApplycar objBean = this.findById(id);
			// 取得原数据的更新时间
			Long lngUpdateTimeNew = objBean.getUpdateTime().getTime();
			if (!updateTime.equals(lngUpdateTimeNew)) {
				throw new DataChangeException("");
			}
			objBean.setUpdateTime(new Date());
			objBean.setUpdateUser(workerCode);
			objBean.setIsUse("N");
			entityManager.merge(objBean);
			LogUtil.log("EJB：删除用车申请信息成功", Level.INFO, null);			
		} catch (DataChangeException de) {
			LogUtil.log("EJB：删除用车申请信息失败", Level.SEVERE, de);
			throw de;
		} catch (Exception e) {
			LogUtil.log("EJB：删除用车申请信息失败", Level.SEVERE, e);
			throw new SQLException();
		}
    }
	
}
/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.ReceptionQueryInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJReception.
 * 
 * @see power.ejb.administration.AdJReception
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJReceptionFacade implements AdJReceptionFacadeRemote {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

    /**
     * 插入来宾接待审批单数据
     * 
     * @param entity
     *            AdJReception entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(AdJReception entity) throws SQLException{
        LogUtil.log("插入来宾接待审批单数据开始", Level.INFO, null);
        try {
            entityManager.persist(entity);
            LogUtil.log("插入来宾接待审批单数据正常结束", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("插入来宾接待审批单数据异常结束", Level.SEVERE, re);
            throw new SQLException();
        }
    }

    /**
     * Delete a persistent AdJReception entity.
     * 
     * @param entity
     *            AdJReception entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(AdJReception entity) {
        LogUtil.log("deleting AdJReception instance", Level.INFO, null);
        try {
            entity = entityManager.getReference(AdJReception.class, entity
                    .getId());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 更新来宾接待审批单数据
     * 
     * @param entity
     *            AdJReception entity to update
     * @return AdJReception the persisted AdJReception entity instance, may not
     *         be the same
     * @throws RuntimeException
     *             if the operation fails
     */
    public AdJReception update(AdJReception entity) throws SQLException{
        LogUtil.log("更新来宾接待审批单数据开始", Level.INFO, null);
        try {
        	entity.setUpdateTime(new java.util.Date());
            AdJReception result = entityManager.merge(entity);
            LogUtil.log("更新来宾接待审批单数据正常结束", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("更新更新来宾接待审批单数据据异常结束", Level.SEVERE, re);
            throw new SQLException();
        }
    }
    /**
     * 跟具id查找更新来宾接待审批单数据
     */
    public AdJReception findById(Long id) {
        LogUtil.log("跟具id查找更新来宾接待审批单数据开始", Level.INFO,
                null);
        try {
            AdJReception instance = entityManager.find(AdJReception.class, id);
            LogUtil.log("跟具id查找更新来宾接待审批单数据正常结束", Level.INFO, null);
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("跟具id查找更新来宾接待审批单数据异常结束", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Find all AdJReception entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the AdJReception property to query
     * @param value
     *            the property value to match
     * @return List<AdJReception> found by query
     */
    @SuppressWarnings("unchecked")
    public List<AdJReception> findByProperty(String propertyName,
            final Object value) {
        LogUtil.log("finding AdJReception instance with property: "
                + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from AdJReception model where model."
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
     * Find all AdJReception entities.
     * 
     * @return List<AdJReception> all AdJReception entities
     */
    @SuppressWarnings("unchecked")
    public List<AdJReception> findAll() {
        LogUtil.log("finding all AdJReception instances", Level.INFO, null);
        try {
            final String queryString = "select model from AdJReception model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }
    /**
     * 通过查询条件获得相应的接待信息数据
     * @param strStartDate
     * @param strEndDate
     * @param strDeptCode
     * @param strWorkerCode
     * @param strIsOver
     * @param strDcmStatus
     * @param rowStartIdxAndCount
     * @return PageObject
     */
	public PageObject getReceptionQueryInfo(String strStartDate,String strEndDate,String strDeptCode,
			String strWorkerCode,String strIsOver, String strDcmStatus, final int ...rowStartIdxAndCount)
	{
		try{
			PageObject pobj = new PageObject();	
			// 查询sql
			String strSql = "select r.APPLY_ID, "
				          + "  w.NAME, "
				          + "  d.DEP_NAME, "
						  + "  to_char(r.LOG_DATE,'yyyy-mm-dd'), "
						  + "  to_char(r.MEET_DATE,'yyyy-mm-dd'), "
						  + "  r.REPAST_NUM, "
						  + "  r.ROOM_NUM, "
						  + "  r.MEET_NOTE, "
						  + "  r.REPAST_BZ, "
						  + "  r.ROOM_BZ, "
						  + "  r.REPAST_PLAN, "						  
						  + "  r.ROOM_PLAN, "
						  + "  r.OTHER, "
						  + "  r.PAYOUT_BZ, "
						  + "  r.PAYOUT, "
						  + "  r.BALANCE, "
						  + "  r.DCM_STATUS "						  
						  + "from "
						  + "  AD_J_RECEPTION      r, "
						  + "  HR_C_WORKER         w, "
						  + "  HR_C_DEPARTMENT     d, "
						  + "  HR_J_EMPLOYEEINFORM e ";

			String strSqlWhere = "where r.APPLY_MAN = w.WORKER_CODE "
			                   + "  and r.APPLY_MAN = e.WORKER_CODE "
			                   + "  and e.DEP_CODE = d.DEP_CODE "
			                   + "  and r.IS_USE = ? "
			                   + "  and e.IS_USE = ? "
			                   + "  and d.IF_LOGOF = ? "
			                   + "  and w.IS_USE = ? ";

			// 查询参数数量
			int paramsCnt = 4;
			if(checkIsNotNull(strStartDate)){
				paramsCnt++;
			}
			if(checkIsNotNull(strEndDate)){
				paramsCnt++;
			}
			if(checkIsNotNull(strDeptCode)){
				paramsCnt++;
			}
			if(checkIsNotNull(strWorkerCode)){
				paramsCnt++;
			}
			if(checkIsNotNull(strDcmStatus)){
				paramsCnt++;
			}
			// 查询参数数组
			Object[] params = new Object[paramsCnt];
			int i = 0;
			params[i++] = "Y";
			params[i++] = "Y";
			params[i++] = "Y";
			params[i++] = "Y";
            // 开始日期
			if(checkIsNotNull(strStartDate)){
				strSqlWhere += "  and to_char(r.MEET_DATE,'yyyy-mm-dd') >= ? ";
				params[i++] = strStartDate;
			}
			// 结束日期
			if(checkIsNotNull(strEndDate)){
				strSqlWhere += "  and to_char(r.MEET_DATE,'yyyy-mm-dd') <= ? ";
				params[i++] = strEndDate;
			}
			// 部门编码
			if(checkIsNotNull(strDeptCode)){
				strSqlWhere += "  and e.DEP_CODE = ? ";
				params[i++] = strDeptCode;
			}
			// 人员编码
			if(checkIsNotNull(strWorkerCode)){
				strSqlWhere += "  and r.APPLY_MAN = ? ";
				params[i++] = strWorkerCode;
			}
			// 是否超支
			if(checkIsNotNull(strIsOver)){
				if("Y".equals(strIsOver)){
				    strSqlWhere += "  and r.BALANCE < 0 ";
				}else if("N".equals(strIsOver)){
				    strSqlWhere += "  and r.BALANCE >= 0 ";
				}
			}
			// 单据状态
			if(checkIsNotNull(strDcmStatus)){
				strSqlWhere += "  and r.DCM_STATUS = ? ";
				params[i++] = strDcmStatus;
			}          
			//拼接查询sql
			strSql += strSqlWhere;
			
			LogUtil.log("EJB:接待审批查询开始。SQL=" + strSql, Level.INFO, null);
			//list
			List list = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			List<ReceptionQueryInfo> arrList = new ArrayList<ReceptionQueryInfo>();
			Iterator it = list.iterator();
			while(it.hasNext()){
				Object[] data = (Object[])it.next();
				ReceptionQueryInfo info = new ReceptionQueryInfo();
				if(data[0] != null){
					info.setApplyId(data[0].toString());
				}
				if(data[1] != null){
					info.setApplyManName(data[1].toString());
				}
				if(data[2] != null){
					info.setApplyDeptName(data[2].toString());
				}
				if(data[3] != null){
					info.setLogDate(data[3].toString());
				}
				if(data[4] != null){
					info.setMeetDate(data[4].toString());
				}
				if(data[5] != null){
					info.setRepastNum(data[5].toString());
				}
				if(data[6] != null){
					info.setRoomNum(data[6].toString());
				}
				if(data[7] != null){
					info.setMeetNote(data[7].toString());
				}
				if(data[8] != null){
					info.setRepastBz(data[8].toString());
				}
				if(data[9] != null){
					info.setRoomBz(data[9].toString());
				}
				if(data[10] != null){
					info.setRepastPlan(data[10].toString());
				}
				if(data[11] != null){
					info.setRoomPlan(data[11].toString());
				}
				if(data[12] != null){
					info.setOther(data[12].toString());
				}
				if(data[13] != null){
					info.setPayoutBz(data[13].toString());
				}
				if(data[14] != null){
					info.setPayout(data[14].toString());
				}
				if(data[15] != null){
					info.setBalance(data[15].toString());
				}
				if(data[16] != null){
					info.setDcmStatus(data[16].toString());
				}
				arrList.add(info);
			}
			// 按查询结果集设置返回结果
			if (arrList.size() == 0) {
				// 设置查询结果集
				pobj.setList(null);
				// 设置行数
				pobj.setTotalCount(Long.parseLong(0 + ""));
			} else {
				pobj.setList(arrList);
				pobj.setTotalCount(Long.parseLong(arrList.size()
						+ ""));
			}
			LogUtil.log("EJB:接待审批查询结束。", Level.INFO, null);
			//返回PageObject
			return pobj;
		}catch(RuntimeException e){
			LogUtil.log("EJB:接待审批查询失败。", Level.SEVERE, e);
			throw e;
		}
	}
	private boolean checkIsNotNull(String value){
		if(value != null && !"".equals(value)){
			return true;
		}else {
			return false;
		}
	}

    /**
     * 通过接待审批单号获得相应的接待信息数据
     * @param strApplyNo 接待审批单号
     * @param strEnterpriseCode 企业代码
     * @param rowStartIdxAndCount
     * @return PageObject
     */
	public PageObject findByApplyNo(String strApplyNo, String strEnterpriseCode, final int ...rowStartIdxAndCount) {
		LogUtil.log("EJB:查询接待审批实例开始", Level.INFO, null);
		PageObject pobj = new PageObject();
		// 数字输出格式化
		String patternNumber = "###,###,###,###,##0.00";
		DecimalFormat dfNumber = new DecimalFormat(patternNumber);
		String sql;
		Object[] params = new Object[5];
		params[0] = 'Y';
		params[1] = strApplyNo;
		params[2] = strEnterpriseCode;
		params[3] = strEnterpriseCode;
		params[4] = strEnterpriseCode;
		sql = "SELECT B.CHS_NAME, " +
				"C.DEPT_NAME, " +
				"TO_CHAR(A.LOG_DATE,'YYYY-mm-dd'), " +
				"TO_CHAR(A.MEET_DATE,'YYYY-mm-dd'), " +
				"A.REPAST_NUM, " +
				"A.ROOM_NUM, " +
				"A.MEET_NOTE, " +
				"A.REPAST_BZ, " +
				"A.ROOM_BZ, " +
				"A.REPAST_PLAN, " +
				"A.ROOM_PLAN, " +
				"A.OTHER, " +
				"A.PAYOUT_BZ, " +
				"A.PAYOUT, " +
				"A.BALANCE " +
				"FROM AD_J_RECEPTION A " +
				"LEFT JOIN HR_J_EMP_INFO B " +
				"ON A.APPLY_MAN = B.EMP_CODE " +
				"LEFT JOIN HR_C_DEPT C " +
				"ON B.DEPT_ID = C.DEPT_ID " +
				"WHERE A.IS_USE = ? AND " +
				"A.APPLY_ID = ? AND " +
				"A.ENTERPRISE_CODE = ? AND " +
				"B.ENTERPRISE_CODE = ? AND " +
				"C.ENTERPRISE_CODE = ? ";
		List<ReceptionQueryInfo> list = bll.queryByNativeSQL(sql, params);
		List<ReceptionQueryInfo> lstRQ= new ArrayList<ReceptionQueryInfo>();
		if (list != null) {
			Iterator it = list.iterator();
			while (it.hasNext()) { 
				Object[] data = (Object[]) it.next();
				ReceptionQueryInfo receptionQueryInfo = new ReceptionQueryInfo();
				// 申请人
				if (data[0] != null) {
					receptionQueryInfo.setApplyManName(data[0].toString());
				}
				// 申请部门
				if (data[1] != null) {
					receptionQueryInfo.setApplyDeptName(data[1].toString());
				}
				// 填表日期
				if (data[2] != null) {
					receptionQueryInfo.setLogDate(data[2].toString());
				}
				// 接待日期
				if (data[3] != null) {
					receptionQueryInfo.setMeetDate(data[3].toString());
				}
				// 就餐人数
				if (data[4] != null) {
					receptionQueryInfo.setRepastNum(data[4].toString());
				}
				// 住宿人数
				if (data[5] != null) {
					receptionQueryInfo.setRoomNum(data[5].toString());
				}
				// 接待说明
				if (data[6] != null) {
					receptionQueryInfo.setMeetNote(data[6].toString());
				}
				// 就餐标准
				if (data[7] != null) {
					receptionQueryInfo.setRepastBz(dfNumber.format(data[7]));
				}
				// 住宿标准
				if (data[8] != null) {
					receptionQueryInfo.setRoomBz(dfNumber.format(data[8]));
				}
				// 就餐安排
				if (data[9] != null) {
					receptionQueryInfo.setRepastPlan(data[9].toString());
				}
				// 住宿安排
				if (data[10] != null) {
					receptionQueryInfo.setRoomPlan(data[10].toString());
				}
				// 其他
				if (data[11] != null) {
					receptionQueryInfo.setOther(data[11].toString());
				}
				// 标准支出
				if (data[12] != null) {
					receptionQueryInfo.setPayoutBz(dfNumber.format(data[12]));
				}
				// 实际支出
				if (data[13] != null) {
					receptionQueryInfo.setPayout(dfNumber.format(data[13]));
				}
				// 差额
				if (data[14] != null) {
					receptionQueryInfo.setBalance(dfNumber.format(data[14]));
				}
				
				lstRQ.add(receptionQueryInfo);
			}
		}
		if(lstRQ.size()>0)
		{
			// 符合条件的接待申请单
			pobj.setList(lstRQ);
			// 符合条件的接待申请单的总数 
			pobj.setTotalCount(Long.parseLong(lstRQ.size() + ""));
		}	
		LogUtil.log("EJB:查询接待审批实例结束", Level.INFO, null);
		return pobj;
	}
}
/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJReception;
import power.ejb.administration.AdJReceptionFacadeRemote;
import power.ejb.administration.form.ReceptionChargeInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 接待费用管理.
 * 
 * @author wangyun
 */
@Stateless
public class ReceptionChargeFacade implements ReceptionChargeFacadeRemote {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/** 来宾接待审批单接口 */
	@EJB(beanName = "AdJReceptionFacade")
	protected AdJReceptionFacadeRemote receptionRemote;
	/** IS_USE为Y */
	private static final String IS_USE_Y = "Y";
	/** 单据状态 */
	private static final String STATE = "2";

	/**
	 * 接待费用管理一览
	 * 
	 * @param argEnterpriseCode 企业编码
	 * @param rowStartIdxAndCount 分页
	 * 
	 * @throws SQLException 
	 */
	public PageObject getReceptionCharge(String argEnterpriseCode,
			final int... rowStartIdxAndCount) throws SQLException {
		try {
			PageObject pobj = new PageObject();
			String strSql = "";
			strSql = "SELECT "
					+ "A.ID, "
					+ "C.CHS_NAME, "
					+ "B.DEPT_NAME, "
					+ "TO_CHAR(A.LOG_DATE, 'yyyy-mm-dd'), "
					+ "TO_CHAR(A.MEET_DATE, 'yyyy-mm-dd'), "
					+ "A.REPAST_NUM, "
					+ "A.ROOM_NUM, "
					+ "A.MEET_NOTE, "
					+ "A.REPAST_BZ, "
					+ "A.ROOM_BZ, "
					+ "A.REPAST_PLAN, "
					+ "A.ROOM_PLAN, "
					+ "A.OTHER, "
					+ "A.PAYOUT_BZ, "
					+ "A.PAYOUT, "
					+ "A.BALANCE, "
					+ "A.APPLY_ID, "
					+ "TO_CHAR(A.UPDATE_TIME, 'yyyy-mm-dd hh24:mi:ss') "
					+ "FROM AD_J_RECEPTION A "
					+ "LEFT JOIN "
					+ "HR_J_EMP_INFO C "
					+ "ON A.APPLY_MAN = C.EMP_CODE "
					+ "AND C.ENTERPRISE_CODE = ? "
					+ "LEFT JOIN "
					+ "HR_C_DEPT B "
					+ "ON C.DEPT_ID = B.DEPT_ID "
					+ "AND B.ENTERPRISE_CODE = ? "
					+ "WHERE "
					+ "A.DCM_STATUS = ? AND "
					+ "A.IS_USE = ? AND "
					+ "A.ENTERPRISE_CODE = ?";
			String strSqlCount = "SELECT "
					+ "COUNT(A.ID) "
					+ "FROM AD_J_RECEPTION A "
					+ "LEFT JOIN "
					+ "HR_J_EMP_INFO C "
					+ "ON A.APPLY_MAN = C.EMP_CODE "
					+ "AND C.ENTERPRISE_CODE = ? "
					+ "LEFT JOIN "
					+ "HR_C_DEPT B "
					+ "ON C.DEPT_ID = B.DEPT_ID "
					+ "AND B.ENTERPRISE_CODE = ? "
					+ "WHERE "
					+ "A.DCM_STATUS = ? AND "
					+ "A.IS_USE = ? AND "
					+ "A.ENTERPRISE_CODE = ?";
			Object[] objParams = new Object[5];
			objParams[0] = argEnterpriseCode;
			objParams[1] = argEnterpriseCode;
			objParams[2] = STATE;
			objParams[3] = IS_USE_Y;
			objParams[4] = argEnterpriseCode;
			
			LogUtil.log("EJB:取得接待费用管理开始。SQL=" + strSql, Level.INFO, null);

			List list = bll.queryByNativeSQL(strSql, objParams, rowStartIdxAndCount);
			Long totalCount = Long
					.parseLong(bll.getSingal(strSqlCount, objParams).toString());
			List<ReceptionChargeInfo> arrlist = new ArrayList<ReceptionChargeInfo>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				ReceptionChargeInfo receptionInfo = new ReceptionChargeInfo();
				Object[] data = (Object[]) it.next();
				// 序列号
				if (null != data[0]) {
					receptionInfo.setId(Long.parseLong(data[0].toString()));
				}
				// 姓名
				if (null != data[1]) {
					receptionInfo.setName(data[1].toString());
				}
				// 部门名称
				if (null != data[2]) {
					receptionInfo.setDepName(data[2].toString());
				}
				// 填表日期
				if (null != data[3]) {
					receptionInfo.setLogDate(data[3].toString());
				}
				// 接待日期
				if (null != data[4]) {
					receptionInfo.setMeetDate(data[4].toString());
				}
				// 就餐人数
				if (null != data[5]) {
					receptionInfo.setRepastNum(Long.parseLong(data[5]
							.toString()));
				}
				// 住宿人数
				if (null != data[6]) {
					receptionInfo
							.setRoomNum(Long.parseLong(data[6].toString()));
				}
				// 接待说明
				if (null != data[7]) {
					receptionInfo.setMeetNote(data[7].toString());
				}
				// 就餐标准
				if (null != data[8]) {
					receptionInfo.setRepastBz(Double
							.parseDouble(data[8].toString()));
				}
				// 住宿标准
				if (null != data[9]) {
					receptionInfo.setRoomBz(Double.parseDouble(data[9].toString()));
				}
				// 就餐安排
				if (null != data[10]) {
					receptionInfo.setRepastPlan(data[10].toString());
				}
				// 住宿安排
				if (null != data[11]) {
					receptionInfo.setRoomPlan(data[11].toString());
				}
				// 其他
				if (null != data[12]) {
					receptionInfo.setOther(data[12].toString());
				}
				// 标准支出
				if (null != data[13]) {
					receptionInfo.setPayoutBz(Double.parseDouble(data[13]
							.toString()));
				}
				// 实际支出
				if (null != data[14]) {
					receptionInfo
							.setPayout(Double.parseDouble(data[14].toString()));
				}
				// 差额
				if (null != data[15]) {
					receptionInfo.setBalance(Double.parseDouble(data[15].toString()));
				}
				// 审批单号
				if (null != data[16]) {
					receptionInfo.setApplyId(data[16].toString());
				}
				// 修改时间
				if (null != data[17]) {
					receptionInfo.setUpdateTime(data[17].toString());
				}
				arrlist.add(receptionInfo);
			}
			if (arrlist.size() > 0) {
				pobj.setList(arrlist);
				pobj.setTotalCount(totalCount);
			}
			LogUtil.log("EJB:取得接待费用管理结束。", Level.INFO, null);
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:取得接待费用管理失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 来宾接待审批单更新
	 * 
	 * @param entity
	 * @param strUpdateTime
	 * @throws SQLException 
	 */
	public void update(AdJReception entity, String strUpdateTime)
			throws SQLException, DataChangeException{
		try {
			String strNowDate = entity.getUpdateTime().toString();
			if (!strNowDate.substring(0, 19).equals(strUpdateTime)) {
				throw new DataChangeException(null);
			}
			if (entity != null) {
				LogUtil.log("EJB:更新接待费用管理开始。", Level.INFO, null);
				receptionRemote.update(entity);
				LogUtil.log("EJB:更新接待费用管理结束。", Level.INFO, null);
			}
		} catch(DataChangeException e) {
			LogUtil.log("EJB:更新接待费用管理失败。", Level.SEVERE, e);
			throw e; 
		}
	}
}

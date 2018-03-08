/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.movebackwarehouse.action;

import java.util.Date;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.resource.business.IssueManage;
import power.web.comm.AbstractAction;
import power.ear.comm.CodeRepeatException;
import power.ejb.resource.business.MovebackWarehouse;
import power.ejb.resource.*;
import power.web.comm.Constants;

public class MovebackWarehouseAction extends AbstractAction {

	/** 定义接口 */
	MovebackWarehouse impl;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 领料单明细bean */
	InvJIssueDetails issueDetails;
	/** 物料需求计划明细表bean */
	MrpJPlanRequirementDetail planRequirementDetail;
	/** 事务历史表表bean */
	InvJTransactionHis transactionHis;
	/** 库存物料记录bean */
	InvJWarehouse jWarehouse;
	/** 库位物料记录bean */
	InvJLocation jLocation;
	/** 批号记录表bean */
	InvJLot jLot;
	/** 事物作用信息 */
	Object[] data;
	public static final String MESSAGE = "该记录已被他人使用";
	/** 常量Y */
	public static final String TEMP_Y = "Y";

	/**
	 * 构造函数
	 */
	public MovebackWarehouseAction() {
		impl = (MovebackWarehouse) factory
				.getFacadeRemote("MovebackWarehouseImpl");

	}

	/**
	 * 通过领料单号查找领料单信息
	 */
	public void findByReceiveNo() throws JSONException {
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		String strReceive_No = request.getParameter("receiveNo");
		String strEnterPrise_Code = employee.getEnterpriseCode();
		Object obj = impl.findByReceiveNo(strReceive_No, strEnterPrise_Code,
				start, limit);
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 查找退库仓库信息
	 */
	public void getWareHouse() throws JSONException {
		String strOrder_No = request.getParameter("order_No");
		Long lngSequence_No = Long.valueOf(request.getParameter("sequence_No"));
		Long lngMaterial_Id = Long.valueOf(request.getParameter("material_Id"));
		Object obj = impl.getWareHouse(strOrder_No, lngSequence_No,
				lngMaterial_Id, employee.getEnterpriseCode());
		write(JSONUtil.serialize(obj));
	}

	public void getMoveBackReason() throws JSONException {
		String strEnterpriseCode = employee.getEnterpriseCode();
		Object obj = impl.getMoveBackReason(strEnterpriseCode);
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 查找退库库位信息
	 */
	public void getLocationName() throws JSONException {
		String strOrder_No = request.getParameter("order_No");
		Long lngSequence_No = Long.valueOf(request.getParameter("sequence_No"));
		Long lngMaterial_Id = Long.valueOf(request.getParameter("material_Id"));
		String strFrom_Whs_Id = request.getParameter("fromWhsId");
		Object obj = impl.getLocationName(strOrder_No, lngSequence_No,
				lngMaterial_Id, strFrom_Whs_Id, employee.getEnterpriseCode());
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 退库更新
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void updateMovebackWarehouse() throws JSONException {
		try {
			String rec = request.getParameter("rec");

			Map map = (Map) JSONUtil.deserialize(rec);
			String strWarehouseInvId = request.getParameter("warehouseInvId");
			String strLocationInvId = request.getParameter("locationInvId");
			String strMoveBackReason = request.getParameter("moveBackReason");
			Double lngMoveBackNumber = Double.parseDouble(request
					.getParameter("moveBackNumber"));
			String strRemark = request.getParameter("remark");
			Long lngSequence_No = Long.parseLong(map.get("issueDetailsId")
					.toString());
			Long lngMaterial_Id = Long.parseLong(map.get("materialId")
					.toString());
			String lotNo = impl.getLotNo(map.get("issueNo").toString(),
					lngSequence_No, lngMaterial_Id, strWarehouseInvId,
					strLocationInvId, employee.getEnterpriseCode());
			data = impl.getTransId("R", employee.getEnterpriseCode());
			if (isBlance()) {
				write("{flag:'1'}");
			} else {

				UserTransaction tx = (UserTransaction) new InitialContext()
						.lookup("java:comp/UserTransaction");
				try {
					tx.begin();
					
					// 领料单明细更新
					updateIssueDetails(map, lngMoveBackNumber);
					// 物料需求计划明细表更新
					updatePlanRequirementDetail(lngMoveBackNumber, map);
					// 事务历史表添加
					addTransactionHis(map, lngMoveBackNumber,
							strMoveBackReason, strWarehouseInvId,
							strLocationInvId, strRemark, lotNo);
					// 更新库存物料记录
					updateJWarehouse(map, lngMoveBackNumber, strWarehouseInvId);
					if (!"".equals(strLocationInvId)
							&& strLocationInvId != null) {
						// 更新库位物料记录
						updateJLocation(map, lngMoveBackNumber,
								strWarehouseInvId, strLocationInvId);
					}
					// 更新批号记录表
					updateJLot(map, lngMoveBackNumber, strWarehouseInvId,
							strLocationInvId, lotNo);
					
					tx.commit();
				} catch (Exception e) {
					if(MESSAGE.equals(e.getMessage())){
						write("{flag:'4'}");
						return ;
					}
					tx.rollback();
					throw e;
				}
				write("{flag:'2'}");
			}
		} catch (Exception e) {
			write("{flag:'3'}");
		}
	}

	public boolean isBlance() {
		IssueManage issueRemote = (IssueManage) factory
				.getFacadeRemote("IssueManageImpl");
		return issueRemote.isBalanceNow("R");
	}

	/**
	 * 领料单明细更新
	 * 
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings( { "unchecked", "unused" })
	private void updateIssueDetails(Map map, Double lngMoveBackNumber)
			throws CodeRepeatException {
		// 修改记录的map
		String issueDetailsId = (String) map.get("issueDetailsId");
		// 领料单明细信息
		InvJIssueDetails entity = impl.getIssueDetails(issueDetailsId);
		// 实际数量
		entity
				.setActIssuedCount(entity.getActIssuedCount()
						- lngMoveBackNumber);
		// 操作人
		entity.setLastModifiedBy(employee.getWorkerCode());
		// 取得上次修改时间
		Long lastModifiedDate = Long.parseLong(map.get("lastModifiedDate")
				.toString());
		// 保存
		try {
			if (entity.getIsUse().equals(Constants.IS_USE_Y)
					&& lastModifiedDate.equals(entity.getLastModifiedDate()
							.getTime())) {
				impl.updateIssueDetails(entity);
			} else {
				throw new CodeRepeatException(MESSAGE);
			}
		} catch (CodeRepeatException e) {
			throw e;
		}
	}

	/**
	 * 物料需求计划明细表更新
	 * 
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings( { "unused", "unchecked" })
	private void updatePlanRequirementDetail(Double lngMoveBackNumber, Map map)
			throws CodeRepeatException {
		// 修改记录的map
		String requirementDetailId = getStringToString(map
				.get("requirementDetailId"));
		if("".equals(requirementDetailId)){
			return ;
		}
		// 物料需求计划明细信息
		MrpJPlanRequirementDetail entity = impl
				.getPlanRequirementDetail(requirementDetailId);
		// 已领数量
		entity.setIssQty(entity.getIssQty() - lngMoveBackNumber);
		// 上次修改人
		entity.setLastModifiedBy(employee.getWorkerCode());
		try {
			impl.updatePlanRequirementDetail(entity);
		} catch (CodeRepeatException e) {
			throw e;
		}
	}

	/**
	 * 事务历史表添加
	 * 
	 * @param map
	 * @param moveBackNumber
	 * @param moveBackReason
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings( { "unchecked", "unused" })
	private void addTransactionHis(Map map, Double lngMoveBackNumber,
			String strMoveBackReason, String strWarehouseInvId,
			String strLocationInvId, String strRemark, String strLotNo)
			throws CodeRepeatException {
		transactionHis = new InvJTransactionHis();
		// 单号
		transactionHis.setOrderNo(map.get("issueNo").toString());
		// 序号
		transactionHis.setSequenceId(Long.parseLong(map.get("issueDetailsId")
				.toString()));
		// 事务作用类型ID
		transactionHis.setTransId(Long.parseLong(data[0].toString()));
		// 事务作用原因ID
		transactionHis.setReasonId(Long.parseLong(strMoveBackReason));
		// 物料ID
		transactionHis.setMaterialId(Long.parseLong(map.get("materialId")
				.toString()));
		// 批号
		transactionHis.setLotNo(strLotNo);
		// 异动数量
		transactionHis.setTransQty(Double.valueOf(lngMoveBackNumber));
		// 领用人
		transactionHis.setReceiveMan((String) map.get("receiptBy"));
		// 领用部门
		transactionHis.setReceiveDept((String) map.get("receiptDept"));
		// 归口人
		transactionHis.setCostMan((String) map.get("receiptBy"));
		// 归口部门
		transactionHis.setCostDept((String) map.get("feeByDep"));
		// 计量单位
		transactionHis.setTransUmId(Long.valueOf(getStringToDouble(map
				.get("stockUmId"))));
		// 操作仓库
		transactionHis.setFromWhsNo(strWarehouseInvId);
		// 操作库位
		transactionHis.setFromLocationNo(strLocationInvId);
		// 上次修改人
		transactionHis.setLastModifiedBy(employee.getWorkerCode());
		// 操作时间
		transactionHis.setLastModifiedDate(new Date());
		// 备注
		transactionHis.setMemo(strRemark);
		// 企业代码
		transactionHis.setEnterpriseCode(employee.getEnterpriseCode());
		// 是否使用
		transactionHis.setIsUse(Constants.IS_USE_Y);
		try {
			impl.saveTransactionHis(transactionHis);
		} catch (CodeRepeatException e) {
			throw e;
		}
	}

	/**
	 * 更新库存物料记录
	 * 
	 * @param map
	 * @param moveBackNumber
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings( { "unused", "unchecked" })
	private void updateJWarehouse(Map map, Double lngMoveBackNumber,
			String strWarehouseInvId) throws CodeRepeatException {
		String strEnterpriseCode = employee.getEnterpriseCode();
		InvJWarehouse entity = impl.getJWarehouse((String) map
				.get("materialId"), strWarehouseInvId, strEnterpriseCode);
		if (TEMP_Y.equals(data[1].toString())) {
			// 本期初始
			entity.setOpenBalance(entity.getOpenBalance() + lngMoveBackNumber);
		}
		if (TEMP_Y.equals(data[2].toString())) {
			// 本期接收
			entity.setReceipt(entity.getReceipt() + lngMoveBackNumber);
		}
		if (TEMP_Y.equals(data[3].toString())) {
			// 本期调整
			entity.setAdjust(entity.getAdjust() + lngMoveBackNumber);
		}
		if (TEMP_Y.equals(data[4].toString())) {
			// 本期出库
			entity.setIssue(entity.getIssue() + lngMoveBackNumber);
		}
		// 上次修改人
		entity.setLastModifiedBy(employee.getWorkerCode());
		// 上次修改时间
		entity.setLastModifiedDate(new Date());
		// 保存
		try {
			impl.updateJWarehouse(entity);
		} catch (CodeRepeatException e) {
			throw e;
		}
	}

	/**
	 * 更新库位物料记录
	 * 
	 * @param map
	 * @param moveBackNumber
	 * @param warehouseInvId
	 * @param locationInvId
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings( { "unchecked", "unused" })
	private void updateJLocation(Map map, Double lngMoveBackNumber,
			String strWarehouseInvId, String strLocationInvId)
			throws CodeRepeatException {
		String strEnterpriseCode = employee.getEnterpriseCode();
		InvJLocation entity = impl.getJLocation((String) map.get("materialId"),
				strWarehouseInvId, strEnterpriseCode, strLocationInvId);
		if (TEMP_Y.equals(data[1].toString())) {
			// 本期初始
			entity.setOpenBalance(entity.getOpenBalance() + lngMoveBackNumber);
		}
		if (TEMP_Y.equals(data[2].toString())) {
			// 本期接收
			entity.setReceipt(entity.getReceipt() + lngMoveBackNumber);
		}
		if (TEMP_Y.equals(data[3].toString())) {
			// 本期调整
			entity.setAdjust(entity.getAdjust() + lngMoveBackNumber);
		}
		if (TEMP_Y.equals(data[4].toString())) {
			// 本期出库
			entity.setIssue(entity.getIssue() + lngMoveBackNumber);
		}
		// 上次修改人
		entity.setLastModifiedBy(employee.getWorkerCode());
		// 上次修改时间
		entity.setLastModifiedDate(new Date());
		// 保存
		try {
			impl.updateJLocation(entity);
		} catch (CodeRepeatException e) {
			throw e;
		}
	}

	/**
	 * 更新批号记录表
	 * 
	 * @param map
	 * @param moveBackNumber
	 * @param warehouseInvId
	 * @param locationInvId
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings("unchecked")
	private void updateJLot(Map map, Double lngMoveBackNumber,
			String strWarehouseInvId, String strLocationInvId, String lotNo)
			throws CodeRepeatException {
		String strEnterpriseCode = employee.getEnterpriseCode();
		InvJLot entity = impl.getJLot((String) map.get("materialId"),
				strWarehouseInvId, strEnterpriseCode, strLocationInvId, lotNo);
		// 批号
		entity.setLotNo(lotNo);
		// 仓库编码
		entity.setWhsNo(strWarehouseInvId);
		// 库位编码
		entity.setLocationNo(strLocationInvId);
		// 物料ID
		entity.setMaterialId(Long.parseLong(map.get("materialId").toString()));
		if (TEMP_Y.equals(data[1].toString())) {
			// 本期初始
			entity.setOpenBalance(entity.getOpenBalance() + lngMoveBackNumber);
		}
		if (TEMP_Y.equals(data[2].toString())) {
			// 本期接收
			entity.setReceipt(entity.getReceipt() + lngMoveBackNumber);
		}
		if (TEMP_Y.equals(data[3].toString())) {
			// 本期调整
			entity.setAdjust(entity.getAdjust() + lngMoveBackNumber);
		}
		if (TEMP_Y.equals(data[4].toString())) {
			// 本期出库
			entity.setIssue(entity.getIssue() + lngMoveBackNumber);
		}
		// 上次修改人
		entity.setLastModifiedBy(employee.getWorkerCode());
		// 上次修改时间
		entity.setLastModifiedDate(new Date());
		// 保存
		try {
			impl.updateJLot(entity);
		} catch (CodeRepeatException e) {
			throw e;
		}
	}

	private String getStringToDouble(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "0";
		}
	}

	private String getStringToString(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	/**
	 * @return the issueDetails
	 */
	public InvJIssueDetails getIssueDetails() {
		return issueDetails;
	}

	/**
	 * @param issueDetails
	 *            the issueDetails to set
	 */
	public void setIssueDetails(InvJIssueDetails issueDetails) {
		this.issueDetails = issueDetails;
	}

	/**
	 * @return the planRequirementDetail
	 */
	public MrpJPlanRequirementDetail getPlanRequirementDetail() {
		return planRequirementDetail;
	}

	/**
	 * @param planRequirementDetail
	 *            the planRequirementDetail to set
	 */
	public void setPlanRequirementDetail(
			MrpJPlanRequirementDetail planRequirementDetail) {
		this.planRequirementDetail = planRequirementDetail;
	}

	/**
	 * @return the transactionHis
	 */
	public InvJTransactionHis getTransactionHis() {
		return transactionHis;
	}

	/**
	 * @param transactionHis
	 *            the transactionHis to set
	 */
	public void setTransactionHis(InvJTransactionHis transactionHis) {
		this.transactionHis = transactionHis;
	}

	/**
	 * @return the jWarehouse
	 */
	public InvJWarehouse getJWarehouse() {
		return jWarehouse;
	}

	/**
	 * @param warehouse
	 *            the jWarehouse to set
	 */
	public void setJWarehouse(InvJWarehouse warehouse) {
		jWarehouse = warehouse;
	}

	/**
	 * @return the jLocation
	 */
	public InvJLocation getJLocation() {
		return jLocation;
	}

	/**
	 * @param location
	 *            the jLocation to set
	 */
	public void setJLocation(InvJLocation location) {
		jLocation = location;
	}
}

/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.storechange.action;

import java.util.List;
import power.ear.comm.CodeRepeatException;
import power.ejb.resource.InvCTransactionFacadeRemote;
import power.ejb.resource.InvCTransaction;
import power.ejb.resource.InvJLocationFacadeRemote;
import power.ejb.resource.InvJLocation;
import power.ejb.resource.InvJLotFacadeRemote;
import power.ejb.resource.InvJLot;
import power.ejb.resource.InvJTransactionHis;
import power.ejb.resource.InvJWarehouse;
import power.ejb.resource.InvJWarehouseFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 仓库基础资料维护
 * 
 * @author qhhu
 * @version 1.0
 */
public class StoreChangeAction extends AbstractAction {
	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 库存物料记录remote */
	private InvJWarehouseFacadeRemote warehouseMaterielRemote;
	/** 库位物料记录remote */
	private InvJLocationFacadeRemote locationFacadeRemote;
	/** 批号记录表remote */
	private InvJLotFacadeRemote lotFacadeRemote;
	/** 事务作用remote */
	private InvCTransactionFacadeRemote transactionFacadeRemote;
	/** 调出仓库的事务类型的事务作用bean */
	private InvCTransaction outTranctionBean;
	/** 调出仓库的事务类型的事务作用bean */
	private InvCTransaction inTranctionBean;
	/** 移库的事务类型的事务作用bean */
	private InvCTransaction ttTranctionBean;
	/** 常量调出仓库的事务类型 */
	private static final String IN_TRANCTION_TYPE = "I";
	/** 常量调入仓库的事务类型 */
	private static final String OUT_TRANCTION_TYPE = "P";
	/** 常量移库的事务类型 */
	private static final String TT_TRANCTION_TYPE = "TT";
	/** 企业编码 */
	private String enterpriseCode;
	/** 常量Y */
	private static final String CONSTANT_Y = "Y";
	/** 常量排他 */
	private static final String CONSTANT_PAITA = "A";
	/** 画面用的Bean */
	private StoreChangeBean storeChangeBean;
	/** 比较用的日期格式 */
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 构造函数
	 */
	public StoreChangeAction() {
		// 仓库物料记录remote
		warehouseMaterielRemote = (InvJWarehouseFacadeRemote) factory
				.getFacadeRemote("InvJWarehouseFacade");
		// 库位物料记录remote
		locationFacadeRemote = (InvJLocationFacadeRemote) factory
				.getFacadeRemote("InvJLocationFacade");
		// 批号记录表remote
		lotFacadeRemote = (InvJLotFacadeRemote) factory
				.getFacadeRemote("InvJLotFacade");
		// 事务作用remote
		transactionFacadeRemote = (InvCTransactionFacadeRemote) factory
				.getFacadeRemote("InvCTransactionFacade");
	}

	/**
	 * 确认移库的ＤＢ更新
	 * 
	 * @throws ParseException
	 * @throws JSONException
	 */
	public void updateStore() throws ParseException, JSONException {
		// 获取事物信息
		getTransactionInfo();
		// 企业编码
		enterpriseCode = employee.getEnterpriseCode();
		// 画面用的Bean的实例化
		storeChangeBean = new StoreChangeBean();
		// Bean的内容设定
		setStoreChangeBean();
		// materialId
		Long materialId = storeChangeBean.getMaterialId();
		// 操作仓库
		String fromWhsNo = storeChangeBean.getFromWhsNo();
		// 调入仓库
		String toWhsNo = storeChangeBean.getToWhsId();
		// 操作库位
		String fromLocationId = storeChangeBean.getFromLocationId();
		if ("".equals(storeChangeBean.getFromLocationId())) {
			fromLocationId = null;
		}
		// 批号
		String lotNo = storeChangeBean.getLotNo();
		// 库存物料记录.上次修改时间
		String lastModifiedDateWhs = storeChangeBean.getLastModifiedDateWhs();
		// 库位物料记录.上次修改时间
		String lastModifiedDateLocation = storeChangeBean
				.getLastModifiedDateLocation();
		// 批号记录.上次修改时间
		String lastModifiedDateLot = storeChangeBean.getLastModifiedDateLot();
		// 日期格式
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		// 排他处理
		try {
			// 调出仓库的库存物料记录的排他处理
			// ＤＢ中存在性确认
			if (!toWhsNo.equals(fromWhsNo)) {
				List<InvJWarehouse> resultWarehouse = warehouseMaterielRemote
						.findByWHandMaterial(enterpriseCode, fromWhsNo,
								materialId);
				// 不存在或者更新时间不相等的时候，报异常
				if (resultWarehouse == null || resultWarehouse.size() == 0) {
					throw new Exception(CONSTANT_PAITA);
				} else if (!dateFormat.format(
						resultWarehouse.get(0).getLastModifiedDate()).trim()
						.equals(lastModifiedDateWhs)) {
					throw new Exception(CONSTANT_PAITA);
				}
				// 调出库位的库位物料记录的排他处理
				// 当对应的仓库有库位的时候，做相应处理否则不做
				if (storeChangeBean.getToLocationId() != null) {
					List<InvJLocation> resultLocation = locationFacadeRemote
							.findByWHLM(enterpriseCode, fromWhsNo,
									fromLocationId, materialId);

					// 如果ＤＢ中不存在新增加一条记录
					if (resultLocation == null || resultLocation.size() == 0) {
						throw new Exception(CONSTANT_PAITA);
					} else if (!dateFormat.format(
							resultLocation.get(0).getLastModifiedDate())
							.equals(lastModifiedDateLocation)) {
						throw new Exception(CONSTANT_PAITA);
					}
				}
			}

			// 调出仓库的批号记录表的排他处理
			List<InvJLot> resultInvJLot = lotFacadeRemote.findByLWHLM(
					enterpriseCode, lotNo, fromWhsNo, fromLocationId,
					materialId);
			// 不存在的时候，报异常
			if (resultInvJLot == null || resultInvJLot.size() == 0) {
				throw new Exception(CONSTANT_PAITA);
			} else if (!dateFormat.format(
					resultInvJLot.get(0).getLastModifiedDate()).equals(
					lastModifiedDateLot)) {
				throw new Exception(CONSTANT_PAITA);
			}

		} catch (Exception e) {
			write(JSONUtil.serialize(CONSTANT_PAITA));
			return;
		}

		// 数据库的数据整理
		try {
			// 新增事务历史表记录
			InvJTransactionHis entityInvJTransactionHis = new InvJTransactionHis();
			entityInvJTransactionHis = addTransactionHis(entityInvJTransactionHis);
			InvJWarehouse entityInInvJWarehouse = null;
			InvJWarehouse entityInOutInvJWarehouse = null;
			// 调入仓库的库存物料记录的处理
		    entityInInvJWarehouse = operateToInvJWarehouse();
			// 调出仓库的库存物料记录的处理
			entityInOutInvJWarehouse = updateFromInvJWarehouse();	
			if(toWhsNo.equals(fromWhsNo)){
				entityInOutInvJWarehouse.setReceipt(entityInInvJWarehouse.getReceipt());
			}
			// 调入仓库的库位物料记录的处理
			InvJLocation entityInInvJLocatione = operateToInvJLocation();
			// 调出库位的库位物料记录的处理
			InvJLocation entityOutInvJLocatione = updateFromInvJLocation();
			// 调入仓库的批号记录表的处理
			InvJLot entityInInvJLot = operateToInvJLot();
			// 调出仓库的批号记录表的处理
			InvJLot entityOutInvJLot = updateFromInvJLot();
			lotFacadeRemote.updateInvLotRelatedInfor(entityInvJTransactionHis,
					entityInInvJWarehouse, entityInOutInvJWarehouse,
					entityInInvJLocatione, entityOutInvJLocatione,
					entityInInvJLot, entityOutInvJLot);
			// 登陆成功的时候
			write("true");
		} catch (Exception e) {
			// 异常的时候，返回失败字符
			write("false");
		}

	}

	/**
	 * 获取画面的信息
	 * 
	 * @throws
	 */
	private void setStoreChangeBean() throws ParseException {
		// 物料ID
		storeChangeBean.setMaterialId(Long.parseLong(request
				.getParameter("materialId")));
		// 批号
		storeChangeBean.setLotNo(request.getParameter("lotNo"));
		// 异动数量
		storeChangeBean.setNumChange(Double.parseDouble(request
				.getParameter("numChange")));
		// 操作仓库
		storeChangeBean.setFromWhsNo(request.getParameter("fromWhsNo"));
		// 操作库位
		storeChangeBean.setFromLocationId(request
				.getParameter("fromLocationId"));
		// 调入仓库
		storeChangeBean.setToWhsId(request.getParameter("toWhsId"));
		// 调入库位
		storeChangeBean.setToLocationId(request.getParameter("toLocationId"));
		// 库存物料记录.上次修改时间
		storeChangeBean.setLastModifiedDateWhs(request
				.getParameter("lastModifiedDateWhs"));
		// 库位物料记录.上次修改时间
		storeChangeBean.setLastModifiedDateLocation(request
				.getParameter("lastModifiedDateLocation"));
		// 批号记录.上次修改时间
		storeChangeBean.setLastModifiedDateLot(request
				.getParameter("lastModifiedDateLot"));
	}

	/**
	 * 获取登陆用户的信息
	 */
	public void getUserName() {
		// 获取事务信息
		getTransactionInfo();
		// 返回登陆者用户名和事务名称
		if (ttTranctionBean != null && ttTranctionBean.getTransName() != null) {
			write(employee.getWorkerName() + "TT:"
					+ ttTranctionBean.getTransName());
		} else {
			write(employee.getWorkerName() + "TT:" + "");
		}

	}

	/**
	 * 获取事务类型的信息
	 */
	private void getTransactionInfo() {
		// 调出仓库的事务类型的事务作用bean
		outTranctionBean = transactionFacadeRemote.findByTransCode(employee
				.getEnterpriseCode(), IN_TRANCTION_TYPE);
		// 调入仓库的事务类型的事务作用bean
		inTranctionBean = transactionFacadeRemote.findByTransCode(employee
				.getEnterpriseCode(), OUT_TRANCTION_TYPE);
		// 移库事务类型的事务作用bean
		ttTranctionBean = transactionFacadeRemote.findByTransCode(employee
				.getEnterpriseCode(), TT_TRANCTION_TYPE);
	}

	/**
	 * 事务历史记录
	 * 
	 * @throws CodeRepeatException
	 */
	private InvJTransactionHis addTransactionHis(InvJTransactionHis entity) {
		// 单号
		entity.setOrderNo("0");
		// 序号
		entity.setSequenceId(new Long(0));
		// 事务类型
		entity.setTransId(ttTranctionBean.getTransId());
		// 物料ID
		entity.setMaterialId(storeChangeBean.getMaterialId());
		// 批号
		entity.setLotNo(storeChangeBean.getLotNo());
		// 异动数量
		entity.setTransQty(storeChangeBean.getNumChange());
		// 操作仓库
		entity.setFromWhsNo(storeChangeBean.getFromWhsNo());
		// 操作库位
		entity.setFromLocationNo(storeChangeBean.getFromLocationId());
		// 调入仓库
		entity.setToWhsNo(storeChangeBean.getToWhsId());
		// 调入库位
		entity.setToLocationNo(storeChangeBean.getToLocationId());
		// 是否使用
		entity.setIsUse(Constants.IS_USE_Y);
		// 企业代码
		entity.setEnterpriseCode(employee.getEnterpriseCode());
		// 操作人
		entity.setLastModifiedBy(employee.getWorkerCode());
		return entity;
	}

	/**
	 * 调入仓库的库存物料记录
	 * 
	 * @return
	 * 
	 * @throws CodeRepeatException
	 */
	private InvJWarehouse operateToInvJWarehouse() {
		// 库存物料记录集合
		List<InvJWarehouse> result;
		// 库存物料记录
		InvJWarehouse entity;
		// 物料ID
		Long materialId = storeChangeBean.getMaterialId();
		// 仓库编码
		String whsNo = storeChangeBean.getToWhsId();
		if (materialId != null && whsNo != null) {
			result = warehouseMaterielRemote.findByWHandMaterial(
					enterpriseCode, whsNo, materialId);
			// 如果ＤＢ中不存在该记录则新增加一条记录
			if (result.size() == 0 || result == null) {
				entity = new InvJWarehouse();
				return insertInvJWarehouse(entity);
			} else {
				// 更新记录
				entity = result.get(0);
				return updateToInvJWarehouse(entity);
			}
		} else {
			return null;
		}
	}

	/**
	 * 修改调入仓库的库存物料信息
	 * 
	 * @param entity
	 *            库存物料记录
	 * @return
	 * @throws CodeRepeatException
	 */
	private InvJWarehouse updateToInvJWarehouse(InvJWarehouse entity) {
		// 调拨数量
		double numChange = storeChangeBean.getNumChange();
		// [调入仓库的事务类型]的[是否影响期初]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsOpenBalance())) {
			entity.setOpenBalance(entity.getOpenBalance() + numChange);
		}

		// [调入仓库的事务类型]的[是否影响接受]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsReceive())) {
			entity.setReceipt(entity.getReceipt() + numChange);
		}

		// [调入仓库的事务类型]的[是否影响调整]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsAdjust())) {
			entity.setAdjust(entity.getAdjust() + numChange);
		}

		// [调入仓库的事务类型]的[是否影响出库]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsIssues())) {
			entity.setIssue(entity.getIssue() + numChange);
		}

		// 上次修改人
		entity.setLastModifiedBy(employee.getWorkerCode());
		return entity;
	}

	/**
	 * 增加库存物料信息
	 * 
	 * @param entity
	 *            库存物料记录
	 * @throws CodeRepeatException
	 */
	private InvJWarehouse insertInvJWarehouse(InvJWarehouse entity) {
		// 物料ID
		Long materialId = storeChangeBean.getMaterialId();
		// 仓库编码
		String whsNo = storeChangeBean.getToWhsId();
		// 调拨数量
		double numChange = storeChangeBean.getNumChange();

		// 物料ID设定
		entity.setMaterialId(materialId);
		// 调入仓库设定
		entity.setWhsNo(whsNo);
		// [调入仓库的事务类型]的[是否影响期初]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsOpenBalance())) {
			entity.setOpenBalance(numChange);
		} else {
			entity.setOpenBalance(new Double(0));
		}
		// [调入仓库的事务类型]的[是否影响接受]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsReceive())) {
			entity.setReceipt(numChange);
		} else {
			entity.setReceipt(new Double(0));
		}
		// [调入仓库的事务类型]的[是否影响调整]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsAdjust())) {
			entity.setAdjust(numChange);
		} else {
			entity.setAdjust(new Double(0));
		}
		// [调入仓库的事务类型]的[是否影响出库]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsIssues())) {
			entity.setIssue(numChange);
		} else {
			entity.setIssue(new Double(0));
		}
		// 是否使用
		entity.setIsUse(Constants.IS_USE_Y);
		// 企业代码
		entity.setEnterpriseCode(employee.getEnterpriseCode());
		// 上次修改人
		entity.setLastModifiedBy(employee.getWorkerCode());
		return entity;
	}

	/**
	 * 修改调出仓库的库存物料信息
	 * 
	 * @throws CodeRepeatException
	 */
	private InvJWarehouse updateFromInvJWarehouse() throws Exception {

		// 物料ID
		Long materialId = storeChangeBean.getMaterialId();
		// 仓库编码
		String whsNo = storeChangeBean.getFromWhsNo();
		// 库存记录
		InvJWarehouse entity;

		// ＤＢ中存在性确认
		List<InvJWarehouse> result = warehouseMaterielRemote
				.findByWHandMaterial(enterpriseCode, whsNo, materialId);

		// 存在的话，更新记录
		entity = result.get(0);
		// 调拨数量
		double numChange = storeChangeBean.getNumChange();
		// [调出仓库的事务类型]的[是否影响期初]=Y的情况
		if (CONSTANT_Y.equals(outTranctionBean.getIsOpenBalance())) {
			entity.setOpenBalance(entity.getOpenBalance() + numChange);
		}

		// [调出仓库的事务类型]的[是否影响接受]=Y的情况
		if (CONSTANT_Y.equals(outTranctionBean.getIsReceive())) {
			entity.setReceipt(entity.getReceipt() + numChange);
		}

		// [调出仓库的事务类型]的[是否影响调整]=Y的情况
		if (CONSTANT_Y.equals(outTranctionBean.getIsAdjust())) {
			entity.setAdjust(entity.getAdjust() + numChange);
		}

		// [调出仓库的事务类型]的[是否影响出库]=Y的情况
		if (CONSTANT_Y.equals(outTranctionBean.getIsIssues())) {
			entity.setIssue(entity.getIssue() + numChange);
		}

		// 上次修改人
		entity.setLastModifiedBy(employee.getWorkerCode());
		return entity;
	}

	/**
	 * 库位物料记录
	 * 
	 * @throws CodeRepeatException
	 */
	private InvJLocation operateToInvJLocation() {
		// 库位物料集合
		List<InvJLocation> result;
		// 库位物料记录
		InvJLocation entity;
		// 物料ID
		Long materialId = storeChangeBean.getMaterialId();
		// 仓库编码
		String whsNo = storeChangeBean.getToWhsId();
		// 库位编码
		String locationNo = storeChangeBean.getToLocationId();

		// 对应的仓库没有库位的时候，不更新对应的库位物料记录表
		if (locationNo == null || "".equals(locationNo)) {
			return null;
		}

		if (materialId != null && whsNo != null && locationNo != null) {
			result = locationFacadeRemote.findByWHLM(enterpriseCode, whsNo,
					locationNo, materialId);

			// 如果ＤＢ中不存在新增加一条记录
			if (result.size() == 0 || result == null) {
				entity = new InvJLocation();
				return insertInvJLocation(entity);
			} else {
				// 该记录则更新
				entity = result.get(0);
				return updateToInvJLocation(entity);
			}
		} else {
			return null;
		}
	}

	/**
	 * 增加库位物料信息
	 * 
	 * @param entity
	 *            库存物料记录
	 * @throws CodeRepeatException
	 */
	private InvJLocation insertInvJLocation(InvJLocation entity) {
		// 物料ID
		Long materialId = storeChangeBean.getMaterialId();
		// 仓库编码
		String whsNo = storeChangeBean.getToWhsId();
		// 库位编码
		String locationNo = storeChangeBean.getToLocationId();
		// 调拨数量
		double numChange = storeChangeBean.getNumChange();

		// 物料ID设定
		entity.setMaterialId(materialId);
		// 调入仓库设定
		entity.setWhsNo(whsNo);
		// 调入库位编码设定
		entity.setLocationNo(locationNo);
		// [调入仓库的事务类型]的[是否影响期初]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsOpenBalance())) {
			entity.setOpenBalance(numChange);
		} else {
			entity.setOpenBalance(new Double(0));
		}
		// [调入仓库的事务类型]的[是否影响接受]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsReceive())) {
			entity.setReceipt(numChange);
		} else {
			entity.setReceipt(new Double(0));
		}
		// [调入仓库的事务类型]的[是否影响调整]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsAdjust())) {
			entity.setAdjust(numChange);
		} else {
			entity.setAdjust(new Double(0));
		}
		// [调入仓库的事务类型]的[是否影响出库]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsIssues())) {
			entity.setIssue(numChange);
		} else {
			entity.setIssue(new Double(0));
		}
		// 是否使用
		entity.setIsUse(Constants.IS_USE_Y);
		// 企业代码
		entity.setEnterpriseCode(employee.getEnterpriseCode());
		// 上次修改人
		entity.setLastModifiedBy(employee.getWorkerCode());

		return entity;
	}

	/**
	 * 修改调入仓库的库位物料记录表
	 * 
	 * @param entity
	 *            库位物料记录表
	 * @throws CodeRepeatException
	 */
	private InvJLocation updateToInvJLocation(InvJLocation entity) {
		// 调拨数量
		double numChange = storeChangeBean.getNumChange();
		// [调入仓库的事务类型]的[是否影响期初]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsOpenBalance())) {
			entity.setOpenBalance(entity.getOpenBalance() + numChange);
		}

		// [调入仓库的事务类型]的[是否影响接受]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsReceive())) {
			entity.setReceipt(entity.getReceipt() + numChange);
		}

		// [调入仓库的事务类型]的[是否影响调整]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsAdjust())) {
			entity.setAdjust(entity.getAdjust() + numChange);
		}

		// [调入仓库的事务类型]的[是否影响出库]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsIssues())) {
			entity.setIssue(entity.getIssue() + numChange);
		}

		// 上次修改人
		entity.setLastModifiedBy(employee.getWorkerCode());
		return entity;
	}

	/**
	 * 修改调出库位的库位物料信息
	 * 
	 * @throws CodeRepeatException
	 */
	private InvJLocation updateFromInvJLocation() throws Exception {

		// 物料ID
		Long materialId = storeChangeBean.getMaterialId();
		// 仓库编码
		String whsNo = storeChangeBean.getFromWhsNo();
		// 库位编码
		String locationNo = storeChangeBean.getFromLocationId();

		// 如果没有对应库位，则不更新对应库位物料信息
		if (locationNo == null || "".equals(locationNo)) {
			return null;
		}

		// 库位记录
		InvJLocation entity;

		// ＤＢ中存在性确认
		List<InvJLocation> result = locationFacadeRemote.findByWHLM(
				enterpriseCode, whsNo, locationNo, materialId);

		// 存在的话，更新记录
		entity = result.get(0);
		// 调拨数量
		double numChange = storeChangeBean.getNumChange();
		// [调出仓库的事务类型]的[是否影响期初]=Y的情况
		if (CONSTANT_Y.equals(outTranctionBean.getIsOpenBalance())) {
			entity.setOpenBalance(entity.getOpenBalance() + numChange);
		}

		// [调出仓库的事务类型]的[是否影响接受]=Y的情况
		if (CONSTANT_Y.equals(outTranctionBean.getIsReceive())) {
			entity.setReceipt(entity.getReceipt() + numChange);
		}

		// [调出仓库的事务类型]的[是否影响调整]=Y的情况
		if (CONSTANT_Y.equals(outTranctionBean.getIsAdjust())) {
			entity.setAdjust(entity.getAdjust() + numChange);
		}

		// [调出仓库的事务类型]的[是否影响出库]=Y的情况
		if (CONSTANT_Y.equals(outTranctionBean.getIsIssues())) {
			entity.setIssue(entity.getIssue() + numChange);
		}

		// 上次修改人
		entity.setLastModifiedBy(employee.getWorkerCode());
		return entity;
	}

	/**
	 * 批号记录表记录
	 * 
	 * @throws CodeRepeatException
	 */
	private InvJLot operateToInvJLot() {
		// 批号记录表集合
		List<InvJLot> result;
		// 批号记录
		InvJLot entity;
		// 物料ID
		Long materialId = storeChangeBean.getMaterialId();
		// 仓库编码
		String whsNo = storeChangeBean.getToWhsId();
		// 调入库位
		String locationNo = storeChangeBean.getToLocationId();
		// 批号
		String lotNo = storeChangeBean.getLotNo();

		if (lotNo != null && materialId != null && whsNo != null) {
			result = lotFacadeRemote.findByLWHLM(enterpriseCode, lotNo, whsNo,
					locationNo, materialId);
			// 如果ＤＢ中不存在新增加一条记录
			if (result.size() == 0 || result == null) {
				entity = new InvJLot();
				return insertInvJLot(entity);
			} else {
				// 该记录则更新
				entity = result.get(0);
				return updateToInvJLot(entity);
			}
		} else {
			return null;
		}
	}

	/**
	 * 修改调入仓库的批号记录表信息
	 * 
	 * @param entity
	 *            批号记录表记录
	 * @throws CodeRepeatException
	 */
	private InvJLot updateToInvJLot(InvJLot entity) {
		// 调拨数量
		double numChange = storeChangeBean.getNumChange();
		// [调入仓库的事务类型]的[是否影响期初]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsOpenBalance())) {
			entity.setOpenBalance(entity.getOpenBalance() + numChange);
		}

		// [调入仓库的事务类型]的[是否影响接受]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsReceive())) {
			entity.setReceipt(entity.getReceipt() + numChange);
		}

		// [调入仓库的事务类型]的[是否影响调整]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsAdjust())) {
			entity.setAdjust(entity.getAdjust() + numChange);
		}

		// [调入仓库的事务类型]的[是否影响出库]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsIssues())) {
			entity.setIssue(entity.getIssue() + numChange);
		}

		// 上次修改人
		entity.setLastModifiedBy(employee.getWorkerCode());
		return entity;
	}

	/**
	 * 增加批号记录表信息
	 * 
	 * @param entity
	 *            批号记录表
	 * @throws CodeRepeatException
	 */
	private InvJLot insertInvJLot(InvJLot entity) {
		// 物料ID
		Long materialId = storeChangeBean.getMaterialId();
		// 仓库编码
		String whsNo = storeChangeBean.getToWhsId();
		// 库位编码
		String locationNo = storeChangeBean.getToLocationId();
		// 批号
		String lotNo = storeChangeBean.getLotNo();
		// 调拨数量
		double numChange = storeChangeBean.getNumChange();

		// 物料ID设定
		entity.setMaterialId(materialId);
		// 调入仓库设定
		entity.setWhsNo(whsNo);
		// 批号
		entity.setLotNo(lotNo);
		// 库位编码
		entity.setLocationNo(locationNo);
		// [调入仓库的事务类型]的[是否影响期初]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsOpenBalance())) {
			entity.setOpenBalance(numChange);
		} else {
			entity.setOpenBalance(new Double(0));
		}
		// [调入仓库的事务类型]的[是否影响接受]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsReceive())) {
			entity.setReceipt(numChange);
		} else {
			entity.setReceipt(new Double(0));
		}
		// [调入仓库的事务类型]的[是否影响调整]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsAdjust())) {
			entity.setAdjust(numChange);
		} else {
			entity.setAdjust(new Double(0));
		}
		// [调入仓库的事务类型]的[是否影响出库]=Y的情况
		if (CONSTANT_Y.equals(inTranctionBean.getIsIssues())) {
			entity.setIssue(numChange);
		} else {
			entity.setIssue(new Double(0));
		}
		// 是否使用
		entity.setIsUse(Constants.IS_USE_Y);
		// 企业代码
		entity.setEnterpriseCode(employee.getEnterpriseCode());
		// 上次修改人
		entity.setLastModifiedBy(employee.getWorkerCode());

		return entity;
	}

	/**
	 * 修改调出仓库的批号记录表
	 * 
	 * @throws CodeRepeatException
	 */
	private InvJLot updateFromInvJLot() throws Exception {

		// 物料ID
		Long materialId = storeChangeBean.getMaterialId();
		// 仓库编码
		String whsNo = storeChangeBean.getFromWhsNo();
		// 库位编码
		String locationNo = storeChangeBean.getFromLocationId();
		// 批号
		String lotNo = storeChangeBean.getLotNo();
		// 批号记录
		InvJLot entity;

		// ＤＢ中存在性确认
		List<InvJLot> result = lotFacadeRemote.findByLWHLM(enterpriseCode,
				lotNo, whsNo, locationNo, materialId);

		// 存在的话，更新记录
		entity = result.get(0);
		// 调拨数量
		double numChange = storeChangeBean.getNumChange();
		// [调出仓库的事务类型]的[是否影响期初]=Y的情况
		if (CONSTANT_Y.equals(outTranctionBean.getIsOpenBalance())) {
			entity.setOpenBalance(entity.getOpenBalance() + numChange);
		}

		// [调出仓库的事务类型]的[是否影响接受]=Y的情况
		if (CONSTANT_Y.equals(outTranctionBean.getIsReceive())) {
			entity.setReceipt(entity.getReceipt() + numChange);
		}

		// [调出仓库的事务类型]的[是否影响调整]=Y的情况
		if (CONSTANT_Y.equals(outTranctionBean.getIsAdjust())) {
			entity.setAdjust(entity.getAdjust() + numChange);
		}

		// [调出仓库的事务类型]的[是否影响出库]=Y的情况
		if (CONSTANT_Y.equals(outTranctionBean.getIsIssues())) {
			entity.setIssue(entity.getIssue() + numChange);
		}

		// 上次修改人
		entity.setLastModifiedBy(employee.getWorkerCode());
		return entity;
	}

}

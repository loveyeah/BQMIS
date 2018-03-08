/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.orderregister.action;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.MrpJPlanRequirementDetail;
import power.ejb.resource.MrpJPlanRequirementDetailFacadeRemote;
import power.ejb.resource.PurJOrder;
import power.ejb.resource.PurJOrderDetails;
import power.ejb.resource.PurJOrderDetailsFacadeRemote;
import power.ejb.resource.PurJOrderFacadeRemote;
import power.ejb.resource.PurJOrderRegister;
import power.ejb.resource.PurJPlanOrder;
import power.ejb.resource.PurJPlanOrderFacadeRemote;
import power.ejb.resource.PurJQuotation;
import power.ejb.resource.SysCCurrency;
import power.ejb.resource.SysCCurrencyFacadeRemote;
import power.ejb.resource.form.InquirePriceBean;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 采购单登记处理Action
 * 
 * @author huyou
 * 
 */
public class OrderRegisterAction extends AbstractAction {

	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 日期形式字符串 yyyy-MM-dd */
	private static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	/** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
	private static final String DATE_FORMAT_YYYYMMDD_TIME = "yyyy-MM-dd HH:mm:ss";
	/** 字符串Key: 采购单 order */
	private static final String KEY_ORDER = "order";
	/** 字符串Key: 采购单明细 orderDetails */
	private static final String KEY_ORDER_DETAILS = "orderDetails";
	/** 字符串Key: 采购单明细(需求单) plan */
	private static final String KEY_ORDER_DETAILS_PLAN = "plan";
	/** 字符串Key: 采购单明细(行政单) unplan */
	private static final String KEY_ORDER_DETAILS_UNPLAN = "unplan";
	/** 字符串Key: 采购订单与需求计划关联 planRelated */
	private static final String KEY_ORDER_DETAILS_RELATE = "planRelated";
	/** 字符串Key: 采购单明细 detail */
	private static final String KEY_DETAIL = "detail";
	/** 字符串Key: 需求物资 planDatas */
	private static final String KEY_PLANS = "planDatas";

	/** 字符串Key: 增加 add */
	private static final String KEY_ADD = "add";
	/** 字符串Key: 修改 update */
	private static final String KEY_UPDATE = "update";
	/** 字符串Key: 删除 delete */
	private static final String KEY_DELETE = "delete";

	/** 采购单处理远程接口 */
	private PurJOrderFacadeRemote orderRemote;
	/** 采购单明细处理远程接口 */
	private PurJOrderDetailsFacadeRemote orderDetailRemote;
	/** 采购订单与需求计划关联处理远程接口 */
	private PurJPlanOrderFacadeRemote planOrderRemote;
	/** 物料需求计划明细处理远程接口 */
	private MrpJPlanRequirementDetailFacadeRemote planRemote;

	/** 采购单登记对象 */
	private PurJOrderRegister order;

	/**
	 * 构造函数
	 */
	public OrderRegisterAction() {
		// 采购单处理远程接口
		orderRemote = (PurJOrderFacadeRemote) factory
				.getFacadeRemote("PurJOrderFacade");
		// 采购单明细处理远程接口
		orderDetailRemote = (PurJOrderDetailsFacadeRemote) factory
				.getFacadeRemote("PurJOrderDetailsFacade");
		// 采购订单与需求计划关联处理远程接口
		planOrderRemote = (PurJPlanOrderFacadeRemote) factory
				.getFacadeRemote("PurJPlanOrderFacade");
		// 物料需求计划明细处理远程接口
		planRemote = (MrpJPlanRequirementDetailFacadeRemote) factory
				.getFacadeRemote("MrpJPlanRequirementDetailFacade");
	}

	/**
	 * 获取登录用户
	 */
	public void getCurrentWorker() {
		// 为空则返回
		if (employee == null) {
			write(Constants.BLANK_STRING);
			return;
		}
		// 返回登录用户的工号
		write("{\"code\": \"" + employee.getWorkerCode() + "\",\"name\": \""
				+ employee.getWorkerName() + "\"}");
	}

	/**
	 * 通过采购单流水号得到采购单
	 * 
	 * @throws JSONException
	 */
	public void getOrderById() throws JSONException {
		// 从请求中获得参数
		String strOrderId = request.getParameter("orderId");

		PurJOrderRegister result = orderRemote.findByOrderId(strOrderId,
				Constants.ENTERPRISE_CODE);
		// 返回客户端
		write(JSONUtil.serialize(result));
	}

	/**
	 * 通过模糊查询得到采购单
	 * 
	 * @throws JSONException
	 */
	public void getOrdersByFuzzy() throws JSONException {
		// 从请求中获得参数
		String strFuzzy = request.getParameter("fuzzy");
		// 是否显示所有订单
		String strShowAll = request.getParameter("isShowAll");
		// 最小行数
		String strStart = request.getParameter("start");
		// 最大行数
		String strLimit = request.getParameter("limit");

		// 模糊查询采购单
		PageObject result = orderRemote.findByFuzzy(strFuzzy,
				Constants.ENTERPRISE_CODE, Boolean.parseBoolean(strShowAll),
				Integer.parseInt(strStart), Integer.parseInt(strLimit));
		// 返回客户端
		write(JSONUtil.serialize(result));
	}

	/**
	 * 通过时间，采购人和采购单编号得到采购单
	 * 
	 * @throws JSONException
	 * @author yiliu
	 */
	public void getOrdersByQuery() throws JSONException {
		// 从请求中获得参数
		String strQueryTimeFrom = request.getParameter("queryTimeFrom");
	
		String strQueryTimeTo = request.getParameter("queryTimeTo");
		
		// 采购人员]
		String buyer = request.getParameter("buyer");
		
		// 采购单编号
		String purNo = request.getParameter("purNo");
		

		// 最小行数
		String strStart = request.getParameter("start");
		
		// 最大行数
		String strLimit = request.getParameter("limit");

		// 模糊查询采购单
		PageObject result = orderRemote.findByQuery(strQueryTimeFrom,
				strQueryTimeTo, buyer, purNo, Constants.ENTERPRISE_CODE,
				Integer.parseInt(strStart), Integer.parseInt(strLimit));
		// 返回客户端
		write(JSONUtil.serialize(result));
	}

	/**
	 * 采购信息查询 通过物资编码，物资名称，物资规格，采购员，采购日期得到采购单物资明细
	 * 
	 * @throws JSONException
	 * @author ywliu
	 */
	public void findOrdersMaterial() throws JSONException {
		// 供应商 add by fyyang 20100114
		String supplyName = request.getParameter("supplyName");
		// 从请求中获得参数
		String strQueryTimeFrom = request.getParameter("startdate");
		String strQueryTimeTo = request.getParameter("enddate");
		// 采购人员]
		String buyer = request.getParameter("buyer");
		if (buyer == null || "".equals(buyer)) {
			// buyer = employee.getWorkerName();
			buyer = ""; // modify by lding
		}
		// 物资编码
		String materialNo = request.getParameter("materialNo");
		// 物资编码
		String materialName = request.getParameter("materialName");
		// 物资编码
		String specNo = request.getParameter("specNo");
		// 最小行数
		String strStart = request.getParameter("start");
		// 最大行数
		String strLimit = request.getParameter("limit");

		// // 模糊查询采购单
		PageObject result = new PageObject();
		if (strStart != null && strLimit != null)
			result = orderRemote.getOrdersMaterial(strQueryTimeFrom,
					strQueryTimeTo, buyer, materialNo, materialName, specNo,
					Constants.ENTERPRISE_CODE, supplyName, Integer
							.parseInt(strStart), Integer.parseInt(strLimit));
		else
			result = orderRemote.getOrdersMaterial(strQueryTimeFrom,
					strQueryTimeTo, buyer, materialNo, materialName, specNo,
					Constants.ENTERPRISE_CODE, supplyName);
		// 返回客户端
		write(JSONUtil.serialize(result));
	}

	/**
	 * 查询所有币种信息
	 * 
	 * @throws JSONException
	 */
	public void getCurrencyList() throws JSONException {
		// 币种信息处理远程接口
		SysCCurrencyFacadeRemote currencyRemote = (SysCCurrencyFacadeRemote) factory
				.getFacadeRemote("SysCCurrencyFacade");
		// 查询所有币种信息
		String strResult = JSONUtil.serialize(currencyRemote
				.findAllAdd(Constants.ENTERPRISE_CODE));
		write(strResult);
	}

	/**
	 * 通过采购单编号取得采购单明细
	 * 
	 * @throws JSONException
	 */
	public void getOrderDetailsByNo() throws JSONException {
		// 从请求中获得参数
		String strPurNo = request.getParameter("purNo");
		// 供应商编号
		String strSupplier = request.getParameter("supplier");

		// 取得采购单明细
		PageObject result = orderDetailRemote.findByPurNo(
				Constants.ENTERPRISE_CODE, strPurNo, strSupplier);
		// 返回客户端
		write(JSONUtil.serialize(result));
	}

	/**
	 * 通过采购单编号取得采购单明细，不要求供应商
	 * 
	 * @throws JSONException
	 * @author yiliu
	 */
	public void getPurchaseDetailList() throws JSONException {
		// 从请求中获得参数
		String purNo = request.getParameter("purNo");
		System.out.println(purNo);
		// 供应商编号
		// String strSupplier = request.getParameter("supplier");

		// 取得采购单明细
		PageObject result = orderDetailRemote.findDetailByPurNo(
				Constants.ENTERPRISE_CODE, purNo);
		// 返回客户端
		write(JSONUtil.serialize(result));
	}

	/**
	 * 通过采购单编号取得采购单明细维护数据
	 * 
	 * @throws JSONException
	 */
	public void getMeasureByPurNo() throws JSONException {
		// 从请求中获得参数
		String strPurNo = request.getParameter("purNo");
		// 供应商编号
		String strSupplier = request.getParameter("supplier");

		// 取得采购单明细
		List<PurJOrderRegister> result = orderDetailRemote.getMeasureByPurNo(
				Constants.ENTERPRISE_CODE, strPurNo, strSupplier);
		// 返回客户端
		write(JSONUtil.serialize(result));
	}

	/**
	 * 通过币别Id取得币别名称
	 */
	public void getCurrencyNameById() {
		// 币种信息处理远程接口
		SysCCurrencyFacadeRemote currencyRemote = (SysCCurrencyFacadeRemote) factory
				.getFacadeRemote("SysCCurrencyFacade");
		// 从请求中获得参数
		String strCurrencyId = request.getParameter("currencyId");
		// 查询所有币种信息
		SysCCurrency currency = currencyRemote.findById(Long
				.parseLong(strCurrencyId));
		write(currency.getCurrencyName());
	}

	/**
	 * 通过币别和供应商编号取得汇率
	 */
	public void getExchangeRate() {
		// 从请求中获得参数
		String strCurrencyId = request.getParameter("currencyId");
		// 供应商编号
		String strSupplier = request.getParameter("supplier");

		// 通过币别和供应商编号取得汇率
		String result = orderDetailRemote.getExchangeRate(
				Constants.ENTERPRISE_CODE, strCurrencyId, strSupplier);
		// 返回客户端
		write(result);
	}

	/**
	 * 通过物料ID和供应商编号取得报价
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getUnitPrice() throws JSONException {
		// 从请求中获得参数
		String strMaterialId = request.getParameter("materialId");
		// 供应商编号
		String strSupplier = request.getParameter("supplier");
		// 最小行数
		String strStart = request.getParameter("start");
		// 最大行数
		String strLimit = request.getParameter("limit");

		// 通过物料ID和供应商编号取得报价
		PageObject result = orderDetailRemote.getUnitPrice(
				Constants.ENTERPRISE_CODE, strMaterialId, strSupplier, Integer
						.parseInt(strStart), Integer.parseInt(strLimit));

		StringBuilder sbd = new StringBuilder();
		sbd.append("{\"list\":[");
		List<InquirePriceBean> lstResult = result.getList();
		if (lstResult != null && lstResult.size() > 0) {
			// modify by ywliu 2009/5/26
			// BpCMeasureUnitFacadeRemote unitRemote =
			// (BpCMeasureUnitFacadeRemote) factory
			// .getFacadeRemote("BpCMeasureUnitFacade");
			// BpCMeasureUnit unit = null;
			InquirePriceBean quot = null;

			for (Iterator<InquirePriceBean> it = lstResult.iterator(); it
					.hasNext();) {
				quot = it.next();
				// 计量单位名称取得
				// unit = unitRemote.findById(quot.getUnitId());

				sbd.append("{\"unitId\":" + quot.getUnitId()
						+ ",\"unitName\":\"" + quot.getUnitName()
						+ "\",\"quotedQty\":" + quot.getInquireQty()
						+ ",\"quotedPrice\":" + quot.getUnitPrice() + "},");
			}

			if (sbd.toString().endsWith(",")) {
				sbd.deleteCharAt(sbd.lastIndexOf(","));
			}
		}

		sbd.append("],\"totalCount\":");
		sbd.append(result.getTotalCount());
		sbd.append("}");
		// 返回客户端
		write(sbd.toString());
	}

	/**
	 * 得到物料的当前库存
	 * 
	 * @throws JSONException
	 */
	public void getCurrentMatCounts() throws JSONException {
		// 从请求中获得参数
		String strMaterialIds = request.getParameter("materialIds");
		// 返回客户端
		Map<String, Double> result = orderDetailRemote.getCurrentMatCounts(
				Constants.ENTERPRISE_CODE, strMaterialIds);
		StringBuilder sbd = new StringBuilder();
		if (result != null) {
			sbd.append("[");
			for (Iterator<String> it = result.keySet().iterator(); it.hasNext();) {
				String strKey = it.next();
				sbd.append("{\"key\":\"" + strKey + "\",\"value\":"
						+ result.get(strKey) + "},");
			}

			if (sbd.length() > 1) {
				sbd.deleteCharAt(sbd.lastIndexOf(","));
			}
			sbd.append("]");
		}
		write(sbd.toString());
	}

	/**
	 * 通过采购员ID和供应商编号取得需求计划明细数据
	 * 
	 * @throws JSONException
	 */
	public void getPlans() throws JSONException {
		// 从请求中获得参数
		String strBuyer = request.getParameter("buyer");
		// 供应商编号
		String strSupplier = request.getParameter("supplier");

		// 通过物料ID和供应商编号取得需求计划明细数据
		List<PurJOrderRegister> result = orderDetailRemote.getPlans(
				Constants.ENTERPRISE_CODE, strBuyer, strSupplier, true);
		// 返回客户端
	
		write(JSONUtil.serialize(result));
	}

	/**
	 * 通过采购员ID和供应商编号取得其它采购员的需求计划明细数据
	 * 
	 * @throws JSONException
	 */
	public void getOtherPlans() throws JSONException {
		// 从请求中获得参数
		String strBuyer = request.getParameter("buyer");
		// 供应商编号
		String strSupplier = request.getParameter("supplier");

		// 通过物料ID和供应商编号取得其它采购员的需求计划明细数据
		List<PurJOrderRegister> result = orderDetailRemote.getPlans(
				Constants.ENTERPRISE_CODE, strBuyer, strSupplier, false);
		// 返回客户端
		write(JSONUtil.serialize(result));
	}

	/**
	 * 上报采购单数据
	 */
	public void reportOrder() {
		// 从请求中获得参数
		String strOrderId = request.getParameter("orderId");
		// 通过采购单流水号得到采购单
		PurJOrder order = orderRemote.findById(Long.parseLong(strOrderId));

		// 单据状态 = ‘1’（1：审批中状态）
		order.setPurStatus("2"); // modify by fyyang 090415
		// 上次修改人 = 登陆者ID
		order.setLastModifiedBy(employee.getWorkerCode());

		// 更新采购单
		orderRemote.update(order);
	}

	/**
	 * 更新采购单数据
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void saveOrderData() throws Exception {
		// 从请求中获得参数
		String strSaveInfo = request.getParameter("saveInfo");
		Object objSaveInfo = JSONUtil.deserialize(strSaveInfo);
		Map mapSaveInfo = (Map) objSaveInfo;

		try {
			// 得到采购单对象
			PurJOrderRegister order = (PurJOrderRegister) getDescribeData(getStringKeyMap((Map) mapSaveInfo
					.get(KEY_ORDER)));

			// 采购单明细
			Map<String, Object> mapDetails = getStringKeyMap((Map) mapSaveInfo
					.get(KEY_ORDER_DETAILS));
			// 得到采购单明细对象(需求单)
			Map<String, List<Map<String, Object>>> planedDetail = new HashMap<String, List<Map<String, Object>>>();
			Map<String, Object> mapPlanedDetail = getStringKeyMap((Map) mapDetails
					.get(KEY_ORDER_DETAILS_PLAN));
			// 得到增加的采购单明细对象(需求单)
			List<Map<String, Object>> addPlaneds = new ArrayList<Map<String, Object>>();
			List lstAddPlaneds = (List) mapPlanedDetail.get(KEY_ADD);
			planedDetail.put(KEY_ADD, addPlaneds);
			for (int intCnt = 0; intCnt < lstAddPlaneds.size(); intCnt++) {
				Map mapPlaned = (Map) lstAddPlaneds.get(intCnt);
				// 增加的采购单明细对象(需求单)
				addPlaneds.add(getDescribePlanData(mapPlaned, KEY_ADD));
			}

			// 得到修改的采购单明细对象(需求单)
			List<Map<String, Object>> updatePlaneds = new ArrayList<Map<String, Object>>();
			List lstUpdatePlaneds = (List) mapPlanedDetail.get(KEY_UPDATE);
			planedDetail.put(KEY_UPDATE, updatePlaneds);
			for (int intCnt = 0; intCnt < lstUpdatePlaneds.size(); intCnt++) {
				Map mapPlaned = (Map) lstUpdatePlaneds.get(intCnt);
				// 修改的采购单明细对象(需求单)
				updatePlaneds.add(getDescribePlanData(mapPlaned, KEY_UPDATE));
			}

			// 得到删除的采购单明细对象(需求单)
			List<Map<String, Object>> deletePlaneds = new ArrayList<Map<String, Object>>();
			List lstDeletePlaneds = (List) mapPlanedDetail.get(KEY_DELETE);
			planedDetail.put(KEY_DELETE, deletePlaneds);
			for (int intCnt = 0; intCnt < lstDeletePlaneds.size(); intCnt++) {
				Map mapPlaned = (Map) lstDeletePlaneds.get(intCnt);
				// 删除的采购单明细对象(需求单)
				deletePlaneds.add(getDescribePlanData(mapPlaned, KEY_DELETE));
			}

			// 得到采购单明细对象(行政单)
			Map<String, List<PurJOrderDetails>> unplanedDetail = new HashMap<String, List<PurJOrderDetails>>();
			Map<String, Object> mapUnplanedDetail = getStringKeyMap((Map) mapDetails
					.get(KEY_ORDER_DETAILS_UNPLAN));
			// 得到增加的采购单明细对象(行政单)
			List<PurJOrderDetails> addUnPlaneds = new ArrayList<PurJOrderDetails>();
			List lstAddUnPlaneds = (List) mapUnplanedDetail.get(KEY_ADD);
			unplanedDetail.put(KEY_ADD, addUnPlaneds);
			for (int intCnt = 0; intCnt < lstAddUnPlaneds.size(); intCnt++) {
				Map mapUnPlaned = (Map) lstAddUnPlaneds.get(intCnt);
				// 增加的采购单明细对象
				addUnPlaneds.add(getAddDetail(getDescribeData(mapUnPlaned)));
			}

			// 得到修改的采购单明细对象(行政单)
			List<PurJOrderDetails> updateUnPlaneds = new ArrayList<PurJOrderDetails>();
			List lstUpdateUnPlaneds = (List) mapUnplanedDetail.get(KEY_UPDATE);
			unplanedDetail.put(KEY_UPDATE, updateUnPlaneds);
			for (int intCnt = 0; intCnt < lstUpdateUnPlaneds.size(); intCnt++) {
				Map mapUnPlaned = (Map) lstUpdateUnPlaneds.get(intCnt);
				// 修改的采购单明细对象
				updateUnPlaneds
						.add(getUpdateDetail(getDescribeData(mapUnPlaned)));
			}

			// 得到删除的采购单明细对象(行政单)
			List<PurJOrderDetails> deleteUnPlaneds = new ArrayList<PurJOrderDetails>();
			List lstDeleteUnPlaneds = (List) mapUnplanedDetail.get(KEY_DELETE);
			unplanedDetail.put(KEY_DELETE, deleteUnPlaneds);
			for (int intCnt = 0; intCnt < lstDeleteUnPlaneds.size(); intCnt++) {
				Map mapUnPlaned = (Map) lstDeleteUnPlaneds.get(intCnt);
				// 删除的采购单明细对象
				deleteUnPlaneds
						.add(getDeleteDetail(getDescribeData(mapUnPlaned)));
			}

			// 得到需求物资对象
			Map<String, List<MrpJPlanRequirementDetail>> plans = new HashMap<String, List<MrpJPlanRequirementDetail>>();
			Map<String, Object> mapPlan = getStringKeyMap((Map) mapSaveInfo
					.get(KEY_PLANS));
			// 得到增加的需求物资对象
			List<MrpJPlanRequirementDetail> addPlanDatas = new ArrayList<MrpJPlanRequirementDetail>();
			List lstAddPlanDatas = (List) mapPlan.get(KEY_ADD);
			plans.put(KEY_ADD, addPlanDatas);
			for (int intCnt = 0; intCnt < lstAddPlanDatas.size(); intCnt++) {
				Map mapPlanDatas = (Map) lstAddPlanDatas.get(intCnt);
				// 增加的需求物资对象
				addPlanDatas.add(getUpdatePlan(getDescribeData(mapPlanDatas)));
			}

			// 得到修改的需求物资对象
			List<MrpJPlanRequirementDetail> updatePlanDatas = new ArrayList<MrpJPlanRequirementDetail>();
			List lstUpdatePlanDatas = (List) mapPlan.get(KEY_UPDATE);
			plans.put(KEY_UPDATE, updatePlanDatas);
			for (int intCnt = 0; intCnt < lstUpdatePlanDatas.size(); intCnt++) {
				Map mapPlanDatas = (Map) lstUpdatePlanDatas.get(intCnt);
				// 修改的需求物资对象
				updatePlanDatas
						.add(getUpdatePlan(getDescribeData(mapPlanDatas)));
			}

			// 得到删除的需求物资对象
			List<MrpJPlanRequirementDetail> deletePlanDatas = new ArrayList<MrpJPlanRequirementDetail>();
			List lstDeletePlanDatas = (List) mapPlan.get(KEY_DELETE);
			plans.put(KEY_DELETE, deletePlanDatas);
			for (int intCnt = 0; intCnt < lstDeletePlanDatas.size(); intCnt++) {
				Map mapPlanDatas = (Map) lstDeletePlanDatas.get(intCnt);
				// 删除的需求物资对象
				deletePlanDatas
						.add(getDeletePlan(getDescribeData(mapPlanDatas)));
			}

			// 从请求中获得参数
			String strIsEdit = request.getParameter("isEdit");
			String strIsDelete = request.getParameter("isDelete");
			if (Boolean.parseBoolean(strIsDelete)) {
				// 得到删除采购单数据
				PurJOrder orderData = getDeleteOrderData(order);
				// 删除采购单
				orderRemote.deleteOrderData(orderData, planedDetail,
						unplanedDetail, plans);
				return;
			}

			if (Boolean.parseBoolean(strIsEdit)) {
				// 得到修改的采购单
				PurJOrder orderData = getUpdateOrderData(order);
				// 修改采购单
				orderRemote.updateOrderData(orderData, planedDetail,
						unplanedDetail, plans);
			} else {
				// 得到增加的采购单
				PurJOrder orderData = getAddOrderData(order);
				// 增加采购单
				PurJOrder orderResult = orderRemote.addOrderData(orderData,
						planedDetail, unplanedDetail, plans);
				write("{\"orderId\": \"" + orderResult.getId()
						+ "\",\"purNo\": \"" + orderResult.getPurNo() + "\"}");
			}
		} catch (UsingException e) {
			// 排他
			write(Constants.DATA_USING);
			throw e;
		}
	}

	/**
	 * 设置采购单登记对象
	 * 
	 * @param argOrder
	 *            采购单登记对象
	 */
	public void setOrder(PurJOrderRegister argOrder) {
		order = argOrder;
	}

	/**
	 * 取得采购单登记对象
	 * 
	 * @return 采购单登记对象
	 */
	public PurJOrderRegister getOrder() {
		return order;
	}

	/**
	 * 根据Map得到采购单明细(需求单)对象
	 * 
	 * @param argMap
	 *            Map
	 * @return Map<String, Object> 采购单明细(需求单)对象
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getDescribePlanData(Map<String, Object> argMap,
			String argUpdateFlag) throws Exception {
		if (argMap == null) {
			return null;
		}

		// 采购单登记保存信息
		Map<String, Object> result = new HashMap<String, Object>();
		// 设置采购订单明细
		PurJOrderDetails detail = null;
		if (KEY_ADD.equals(argUpdateFlag)) {
			// 增加的采购单明细
			detail = getAddDetail(getDescribeData(argMap));
		} else if (KEY_UPDATE.equals(argUpdateFlag)) {
			// 修改的采购单明细
			detail = getUpdateDetail(getDescribeData(argMap));
		} else if (KEY_DELETE.equals(argUpdateFlag)) {
			// 删除的采购单明细
			detail = orderDetailRemote.findById(getDescribeData(argMap)
					.getOrderDetailsId());
		}
		result.put(KEY_DETAIL, detail);

		// 设置采购订单与需求计划关联项目
		Map<String, Object> mapPlanRelates = getStringKeyMap((Map) argMap
				.get(KEY_ORDER_DETAILS_RELATE));
		Map<String, List<PurJPlanOrder>> planRelates = new HashMap<String, List<PurJPlanOrder>>();
		result.put(KEY_ORDER_DETAILS_RELATE, planRelates);
		if (mapPlanRelates != null && mapPlanRelates.size() > 0) {
			// 得到增加的采购订单与需求计划关联对象
			List<PurJPlanOrder> addPlanRelates = new ArrayList<PurJPlanOrder>();
			List lstAddPlanRelates = (List) mapPlanRelates.get(KEY_ADD);
			planRelates.put(KEY_ADD, addPlanRelates);
			for (int intCnt = 0; intCnt < lstAddPlanRelates.size(); intCnt++) {
				Map mapPlans = (Map) lstAddPlanRelates.get(intCnt);
				// 增加的采购订单与需求计划关联对象
				addPlanRelates.add(getAddPlanRelate(getDescribeData(mapPlans)));
			}

			// 得到修改的采购订单与需求计划关联对象
			List<PurJPlanOrder> updatePlanRelates = new ArrayList<PurJPlanOrder>();
			List lstUpdatePlanRelates = (List) mapPlanRelates.get(KEY_UPDATE);
			planRelates.put(KEY_UPDATE, updatePlanRelates);
			for (int intCnt = 0; intCnt < lstUpdatePlanRelates.size(); intCnt++) {
				Map mapPlans = (Map) lstUpdatePlanRelates.get(intCnt);
				// 修改的采购订单与需求计划关联对象
				updatePlanRelates
						.add(getUpdatePlanRelate(getDescribeData(mapPlans)));
			}

			// 得到删除的采购订单与需求计划关联对象
			List<PurJPlanOrder> deleteRelates = new ArrayList<PurJPlanOrder>();
			List lstDeletePlanRelates = (List) mapPlanRelates.get(KEY_DELETE);
			planRelates.put(KEY_DELETE, deleteRelates);
			for (int intCnt = 0; intCnt < lstDeletePlanRelates.size(); intCnt++) {
				Map mapPlans = (Map) lstDeletePlanRelates.get(intCnt);
				// 删除的采购订单与需求计划关联对象
				deleteRelates
						.add(getDeletePlanRelate(getDescribeData(mapPlans)));
			}
		}

		return result;
	}

	/**
	 * 得到增加的采购单
	 * 
	 * @param argOrder
	 *            需要增加的采购单信息
	 * @return 采购单
	 */
	private PurJOrder getAddOrderData(PurJOrderRegister argOrder) {
		// 采购单信息
		PurJOrder order = new PurJOrder();
		// 采购人员
		order.setBuyer(argOrder.getBuyer());
		// 合同编号
		order.setContractNo(argOrder.getContractNo());
		// 供应商编号
		order.setSupplier(argOrder.getSupplier());
		// 交期
		order.setDueDate(argOrder.getOrderDueDate());
		// 上次修改人 = 当前用户
		order.setLastModifiedBy(employee.getWorkerCode());
		// 上次修改日期 = 系统时间
		order.setLastModifiedDate(new Date());
		// 币别
		order.setCurrencyId(argOrder.getCurrencyId());
		// 税率
		order.setTaxRate(argOrder.getOrderTaxRate());
		// 备注
		order.setMemo(argOrder.getOrderMemo());
		// 企业代码 ＝ 画面．企业代码(session)
		order.setEnterpriseCode(employee.getEnterpriseCode());

		return order;
	}

	/**
	 * 得到修改的采购单
	 * 
	 * @param argOrder
	 *            需要修改的采购单信息
	 * @return 采购单信息
	 */
	private PurJOrder getUpdateOrderData(PurJOrderRegister argOrder) {
		// 采购单信息
		PurJOrder order = null;
		if (argOrder.getOrderId() != null) {
			// 通过采购单流水号得到采购单
			order = orderRemote.findById(argOrder.getOrderId());

			// 排它处理
			if (order.getLastModifiedDate().getTime() != formatStringToDate(
					argOrder.getOrderModifyDate()).getTime()) {
				throw new UsingException("排它处理");
			}

			// 交期
			order.setDueDate(argOrder.getOrderDueDate());
			// 发票号
			order.setInvoiceNo(argOrder.getInvoiceNo());
			// 合同号
			order.setContractNo(argOrder.getContractNo());
			// 上次修改人 = 当前用户
			order.setLastModifiedBy(employee.getWorkerCode());
			// 币别
			order.setCurrencyId(argOrder.getCurrencyId());
			// 税率
			order.setTaxRate(argOrder.getOrderTaxRate());
			// 备注
			order.setMemo(argOrder.getOrderMemo());
		} else {
			order = new PurJOrder();
			// 设置订单编号
			order.setPurNo(argOrder.getPurNo());
		}

		return order;
	}

	/**
	 * 得到删除的采购单
	 * 
	 * @param argOrder
	 *            需要删除的采购单信息
	 * @return 采购单信息
	 */
	private PurJOrder getDeleteOrderData(PurJOrderRegister argOrder) {
		if (argOrder == null) {
			return null;
		}
		// 采购单信息
		PurJOrder order = orderRemote.findById(argOrder.getOrderId());

		// 排它处理
		if (order.getLastModifiedDate().getTime() != formatStringToDate(
				argOrder.getOrderModifyDate()).getTime()) {
			throw new UsingException("排它处理");
		}

		// 上次修改人 = 当前用户
		order.setLastModifiedBy(employee.getWorkerCode());

		return order;
	}

	/**
	 * 得到增加的采购单明细对象
	 * 
	 * @param argDetail
	 *            自定义Bean
	 * @return 增加的采购单明细对象
	 */
	private PurJOrderDetails getAddDetail(PurJOrderRegister argDetail) {
		// 采购单明细对象
		PurJOrderDetails detail = new PurJOrderDetails();
		// 物料ID
		detail.setMaterialId(argDetail.getMaterialId());
		// 采购数量
		detail.setPurQty(argDetail.getPurQty());
		// 单价
		detail.setUnitPrice(argDetail.getQuotedPrice());
		// 币别
		detail.setCurrencyId(argDetail.getCurrencyId());
		// 税率
		detail.setTaxRate(argDetail.getOrderDetailsTaxRate());
		//税额add by drdu 20100407
		detail.setTaxCount(argDetail.getPurQty()*argDetail.getQuotedPrice()*argDetail.getOrderDetailsTaxRate());
		// 汇率
		detail.setExchageRate(argDetail.getRate());
		// 单位 = 画面.采购计量单位
		detail.setPurUmId(argDetail.getPurUmId());
		// 交期
		detail.setDueDate(argDetail.getOrderDetailsDueDate());
		// 是否免检
		detail.setQaControlFlag(argDetail.getQaControlFlag());
		// 备注
		detail.setMemo(argDetail.getOrderDetailsMemo());
		// 上次修改人
		detail.setLastModifiedBy(employee.getWorkerCode());
		// 企业编码
		detail.setEnterpriseCode(employee.getEnterpriseCode());

		return detail;
	}

	/**
	 * 得到修改的采购单明细对象
	 * 
	 * @param argDetail
	 *            自定义Bean
	 * @return 修改的采购单明细对象
	 */
	private PurJOrderDetails getUpdateDetail(PurJOrderRegister argDetail) {
		if (argDetail == null) {
			return null;
		}
		// 采购单明细对象
		PurJOrderDetails detail = orderDetailRemote.findById(argDetail
				.getOrderDetailsId());

		// 排它处理
		if (detail.getLastModifiedDate().getTime() != formatStringToDate(
				argDetail.getOrderDetailsModifyDate()).getTime()) {
			throw new UsingException("排它处理");
		}

		// 采购数量
		detail.setPurQty(argDetail.getPurQty());
		// 已收数量
		detail.setRcvQty(argDetail.getRcvQty());
		// 单价
		detail.setUnitPrice(argDetail.getQuotedPrice());
		// 币别
		detail.setCurrencyId(argDetail.getCurrencyId());
		// 税率
		detail.setTaxRate(argDetail.getOrderDetailsTaxRate());
		//税额add by drdu 20100407
    	detail.setTaxCount(argDetail.getPurQty()*argDetail.getQuotedPrice()*argDetail.getOrderDetailsTaxRate());
		// 汇率
		detail.setExchageRate(argDetail.getRate());
		// 单位 = 画面.采购计量单位
		detail.setPurUmId(argDetail.getPurUmId());
		// 交期
		detail.setDueDate(argDetail.getOrderDetailsDueDate());
		// 是否免检
		detail.setQaControlFlag(argDetail.getQaControlFlag());
		// 备注
		detail.setMemo(argDetail.getOrderDetailsMemo());
		// 上次修改人
		detail.setLastModifiedBy(employee.getWorkerCode());

		return detail;
	}

	/**
	 * 得到删除的采购单明细对象
	 * 
	 * @param argDetail
	 *            自定义Bean
	 * @return 删除的采购单明细对象
	 */
	private PurJOrderDetails getDeleteDetail(PurJOrderRegister argDetail) {
		if (argDetail == null) {
			return null;
		}
		// 采购单明细对象
		PurJOrderDetails detail = orderDetailRemote.findById(argDetail
				.getOrderDetailsId());

		// 排它处理
		if (detail.getLastModifiedDate().getTime() != formatStringToDate(
				argDetail.getOrderDetailsModifyDate()).getTime()) {
			throw new UsingException("排它处理");
		}

		// 上次修改人
		detail.setLastModifiedBy(employee.getWorkerCode());

		return detail;
	}

	/**
	 * 得到增加的采购订单与需求计划关联对象
	 * 
	 * @param argPlanRelate
	 *            自定义Bean
	 * @return 增加的采购订单与需求计划关联对象
	 */
	private PurJPlanOrder getAddPlanRelate(PurJOrderRegister argPlanRelate) {
		// 采购订单与需求计划关联对象
		PurJPlanOrder planRelate = new PurJPlanOrder();
		// 申请单项次号
		planRelate.setRequirementDetailId(argPlanRelate
				.getRequirementDetailId());
		// 从计划分拆或合并的数量
		planRelate.setMrQty(argPlanRelate.getNeedQty());
		// 上次修改人
		planRelate.setLastModifiedBy(employee.getWorkerCode());
		// 企业编码
		planRelate.setEnterpriseCode(employee.getEnterpriseCode());

		return planRelate;
	}

	/**
	 * 得到修改的采购订单与需求计划关联对象
	 * 
	 * @param argPlanRelate
	 *            自定义Bean
	 * @return 修改的采购订单与需求计划关联对象
	 */
	private PurJPlanOrder getUpdatePlanRelate(PurJOrderRegister argPlanRelate) {
		// 采购订单与需求计划关联对象
		PurJPlanOrder planRelate = planOrderRemote.findById(argPlanRelate
				.getPlanOrderId());

		// 排它处理
		if (planRelate.getLastModifiedDate().getTime() != formatStringToDate(
				argPlanRelate.getPlanRelateModifyDate()).getTime()) {
			throw new UsingException("排它处理");
		}

		// 采购订单号
		planRelate.setPurOrderDetailsId(argPlanRelate.getOrderDetailsId());
		// 从计划分拆或合并的数量
		planRelate.setMrQty(argPlanRelate.getNeedQty());
		// 上次修改人
		planRelate.setLastModifiedBy(employee.getWorkerCode());

		return planRelate;
	}

	/**
	 * 得到删除的采购订单与需求计划关联对象
	 * 
	 * @param argPlanRelate
	 *            自定义Bean
	 * @return 删除的采购订单与需求计划关联对象
	 */
	private PurJPlanOrder getDeletePlanRelate(PurJOrderRegister argPlanRelate) {
		// 采购订单与需求计划关联对象
		PurJPlanOrder planRelate = planOrderRemote.findById(argPlanRelate
				.getPlanOrderId());

		// 排它处理
		if (planRelate.getLastModifiedDate().getTime() != formatStringToDate(
				argPlanRelate.getPlanRelateModifyDate()).getTime()) {
			throw new UsingException("排它处理");
		}

		// 上次修改人
		planRelate.setLastModifiedBy(employee.getWorkerCode());

		return planRelate;
	}

	/**
	 * 得到修改的物料需求计划明细对象
	 * 
	 * @param argPlan
	 *            自定义Bean
	 * @return 修改的物料需求计划明细对象
	 */
	private MrpJPlanRequirementDetail getUpdatePlan(PurJOrderRegister argPlan) {
		// 物料需求计划明细
		MrpJPlanRequirementDetail plan = planRemote.findById(argPlan
				.getRequirementDetailId(), Constants.ENTERPRISE_CODE);

		// 排它处理
		if (plan.getLastModifiedDate().getTime() != formatStringToDate(
				argPlan.getPlanModifyDate()).getTime()) {
			throw new UsingException("排它处理");
		}

		// 已生成采购的数量
		plan.setPurQty(argPlan.getApprovedQty() - argPlan.getNeedQty());
		// 是否已生成采购单
		plan.setIsGenerated("N");
		// 上次修改人
		plan.setLastModifiedBy(employee.getWorkerCode());

		return plan;
	}

	/**
	 * 得到删除的物料需求计划明细对象
	 * 
	 * @param argPlan
	 *            自定义Bean
	 * @return 删除的物料需求计划明细对象
	 */
	private MrpJPlanRequirementDetail getDeletePlan(PurJOrderRegister argPlan) {
		// 物料需求计划明细
		MrpJPlanRequirementDetail plan = planRemote.findById(argPlan
				.getRequirementDetailId(), Constants.ENTERPRISE_CODE);

		// 排它处理
		if (plan.getLastModifiedDate().getTime() != formatStringToDate(
				argPlan.getPlanModifyDate()).getTime()) {
			throw new UsingException("排它处理");
		}

		// 已生成采购的数量
		plan.setPurQty(argPlan.getApprovedQty());
		// 是否已生成采购单
		plan.setIsGenerated("Y");
		// 上次修改人
		plan.setLastModifiedBy(employee.getWorkerCode());

		return plan;
	}

	/**
	 * 根据Map得到一个PurJOrderRegister对象
	 * 
	 * @param argMap
	 *            Map
	 * @return PurJOrderRegister对象
	 * @throws Exception
	 */
	private PurJOrderRegister getDescribeData(Map<String, Object> argMap)
			throws Exception {
		if (argMap == null) {
			return null;
		}

		PurJOrderRegister result = new PurJOrderRegister();
		Field[] fields = PurJOrderRegister.class.getDeclaredFields();
		String strFieldName = "";
		for (int intCnt = 0; intCnt < fields.length; intCnt++) {
			Field field = fields[intCnt];
			// 是否是静态字段
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			if (Class.class == field.getType()) {
				continue;
			}
			strFieldName = field.getName();

			// 如果包括该值
			if (argMap.containsKey(strFieldName)) {
				// 设置为可访问
				field.setAccessible(true);
				// 根据类型得到该类型的值
				field.set(result, getValueByType(argMap.get(strFieldName),
						field.getType()));
			}
		}

		return result;
	}

	/**
	 * 根据类型得到该类型的值
	 * 
	 * @param argObject
	 *            值
	 * @param argType
	 *            类型
	 * @return 该类型的值
	 */
	public Object getValueByType(Object argObject, Class<?> argType) {
		if (argObject == null) {
			return null;
		}
		String strValue = argObject.toString();

		if (void.class == argType) {
			return null;
		} else if (Integer.class == argType) {
			if (strValue.trim().length() < 1) {
				return null;
			}
			return new Integer(strValue);
		} else if (Short.class == argType) {
			if (strValue.trim().length() < 1) {
				return null;
			}
			return new Short(strValue);
		} else if (Long.class == argType) {
			if (strValue.trim().length() < 1) {
				return null;
			}
			return new Long(strValue);
		} else if (Double.class == argType) {
			if (strValue.trim().length() < 1) {
				return null;
			}
			return new Double(strValue);
		} else if (Float.class == argType) {
			if (strValue.trim().length() < 1) {
				return null;
			}
			return new Float(strValue);
		} else if (Boolean.class == argType) {
			return new Boolean(strValue);
		} else if (Byte.class == argType) {
			return new Byte(strValue);
		} else if (Character.class == argType) {
			char ch = '\0';
			if (strValue.trim().length() > 0) {
				ch = strValue.charAt(0);
			} else {
				return null;
			}
			return new Character(ch);
		} else if (Date.class == argType) {
			// 根据日期日期字符串和形式返回日期
			return formatStringToDate(strValue);
		}

		return argObject;
	}

	/**
	 * 转换为字符串为Key的Map
	 * 
	 * @param argObjectMap
	 *            以Object为Key的Map
	 * @return 字符串为Key的Map
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getStringKeyMap(Map argObjectMap) {
		if (argObjectMap == null) {
			return null;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		for (Iterator it = argObjectMap.keySet().iterator(); it.hasNext();) {
			Object objKey = it.next();
			result.put((String) objKey, argObjectMap.get(objKey));
		}

		return result;
	}

	/**
	 * 根据日期日期字符串和形式返回日期
	 * 
	 * @param argDateStr
	 *            日期字符串
	 * @return 日期
	 */
	private Date formatStringToDate(String argDateStr) {
		if (argDateStr == null || argDateStr.trim().length() < 1) {
			return null;
		}

		// 日期形式
		SimpleDateFormat sdfFrom = null;
		// 返回日期
		Date result = null;

		try {
			String strFormat = DATE_FORMAT_YYYYMMDD;
			if (argDateStr.length() > 10) {
				strFormat = DATE_FORMAT_YYYYMMDD_TIME;
			}
			sdfFrom = new SimpleDateFormat(strFormat);
			// 格式化日期
			result = sdfFrom.parse(argDateStr);
		} catch (Exception e) {
			result = null;
		} finally {
			sdfFrom = null;
		}

		return result;
	}

	/**
	 * 排它异常
	 * 
	 * @author huyou
	 * 
	 */
	private class UsingException extends RuntimeException {
		/**
		 * serialVersion
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 构造函数
		 */
		public UsingException() {
			super();
		}

		/**
		 * 构造函数
		 * 
		 * @param argMsg
		 *            消息
		 */
		public UsingException(String argMsg) {
			super(argMsg);
		}
	}

}

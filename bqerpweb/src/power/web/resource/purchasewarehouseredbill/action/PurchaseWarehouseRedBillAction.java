package power.web.resource.purchasewarehouseredbill.action;


import java.util.Date;

import power.ear.comm.ejb.PageObject;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.ejb.resource.InvJLocation;
import power.ejb.resource.InvJLocationFacadeRemote;
import power.ejb.resource.InvJLot;
import power.ejb.resource.InvJLotFacadeRemote;
import power.ejb.resource.InvJTransactionHis;
import power.ejb.resource.InvJTransactionHisFacadeRemote;
import power.ejb.resource.InvJWarehouse;
import power.ejb.resource.InvJWarehouseFacadeRemote;
import power.ejb.resource.PurJArrival;
import power.ejb.resource.PurJArrivalDetails;
import power.ejb.resource.PurJArrivalDetailsFacadeRemote;
import power.ejb.resource.PurJArrivalFacadeRemote;
import power.ejb.resource.business.IssueManage;
import power.ejb.resource.business.PurchaseWarehouse;
import power.ejb.resource.business.PurchaseWarehouseRedBill;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class PurchaseWarehouseRedBillAction extends AbstractAction {

	private PurchaseWarehouseRedBill redBillRemote;
	/** 到货登记/验收表远程对象 */
	protected PurJArrivalFacadeRemote arrivalRemote;
	/** 到货登记/验收明细表远程对象 */
	protected PurJArrivalDetailsFacadeRemote arrivalDetailsRemote;
	/** 事务历史表远程对象 */
	protected InvJTransactionHisFacadeRemote transHisRemote;
	protected PurchaseWarehouse remote;

	private static final String TRANS_NAME = "P";

	private static final String MESSAGE2 = "{flag:'0',msg:'正在结账，此业务无法进行。'}";
	private static final String MASSAGE3 = "{flag:'0',msg:'操作数据库过程中异常终了。'}";
	private static final String MASSAGE4 = "{flag:'0',msg:'他人使用中。'}";
	private static final String EMPTY = "";

	/**
	 * 判断是否结账
	 * 
	 * @return
	 */
	public boolean isBlance() {

		IssueManage issueRemote = (IssueManage) factory
				.getFacadeRemote("IssueManageImpl");

		return issueRemote.isBalanceNow(TRANS_NAME);
	}

	public PurchaseWarehouseRedBillAction() {
		redBillRemote = (PurchaseWarehouseRedBill) factory
				.getFacadeRemote("PurchaseWarehouseRedBillImpl");
		// 到货登记/验收表远程对象
		arrivalRemote = (PurJArrivalFacadeRemote) factory
				.getFacadeRemote("PurJArrivalFacade");
		arrivalDetailsRemote = (PurJArrivalDetailsFacadeRemote) factory
				.getFacadeRemote("PurJArrivalDetailsFacade");
		transHisRemote = (InvJTransactionHisFacadeRemote) factory
				.getFacadeRemote("InvJTransactionHisFacade");
		remote = (PurchaseWarehouse) factory
				.getFacadeRemote("PurchaseWarehouseImpl");
	}

	public void getPurchasehouseCheckList() throws JSONException {
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		String materialNo = request.getParameter("materialNo");
		String materialName = request.getParameter("materialName");
		String specNo = request.getParameter("specNo");
		String warehouseName = request.getParameter("warehouseName");
		String supplyName = request.getParameter("supplyName");
		String whsBy = request.getParameter("whsBy");
		String purBy = request.getParameter("purBy");
		// 添加红单查询 add by ywliu 20100128
		String isRedBill = request.getParameter("redBill");
		int start = 0;
		int limit = 99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject obj = redBillRemote.findPurchasehouseCheckList(employee
				.getEnterpriseCode(), startdate, enddate, materialNo,
				materialName, warehouseName, specNo, supplyName, purBy, whsBy,isRedBill,
				start, limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"
				+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}

	public void addPurchaseWarehouseRedBill() {
		try {
			if (!isBlance()) {
				
				String arrivalNo = request.getParameter("arrivalNo");
				String rcvQty = request.getParameter("rcvQty");
				String materialId = request.getParameter("materialId");
				String id = request.getParameter("id");
				String arrivalDetailID = request
						.getParameter("arrivalDetailID");
				//add by fyyang 091201 单价
				Double price=Double.parseDouble(request.getParameter("unitPrice"));
				InvCMaterialFacadeRemote materialRemote=(InvCMaterialFacadeRemote)factory.getFacadeRemote("InvCMaterialFacade");
			    Double nowStdCost=materialRemote.updateStdCost(Long.parseLong(materialId), employee.getEnterpriseCode(), Double.parseDouble(rcvQty), price);
				// red 为红单，blue 为蓝单
			    String flag=request.getParameter("flag");
				//----add end------------
				PurJArrival arrivalBean = new PurJArrival();
				PurJArrivalDetails arrivalDetailsBean = new PurJArrivalDetails();
				InvJTransactionHis entity = new InvJTransactionHis();
				InvJWarehouse warehouseBean = new InvJWarehouse();
				InvJLocation locationBean = new InvJLocation();
				InvJLot lotBean = new InvJLot();
				if (id != null && !"".equals(id)) {
					arrivalBean = arrivalRemote.findById(Long.valueOf(id));
					arrivalBean.setRelationArrivalNo(arrivalNo);
					arrivalBean.setCheckState("B");
					arrivalBean.setArrivalState("N");
					// modify by ywliu 20100126
					arrivalBean.setLastModifiedBy(employee.getWorkerCode());
					arrivalBean.setCheckBy("");
					arrivalBean.setCheckDate(null);
					arrivalBean.setLastModifiedDate(new Date());
				}
				if (arrivalDetailID != null && !"".equals(arrivalDetailID)) {
					arrivalDetailsBean = arrivalDetailsRemote.findById(Long
							.valueOf(arrivalDetailID));
					//arrivalDetailsBean.setRecQty(Double.valueOf(rcvQty));
					arrivalDetailsBean.setRcvQty(Double.valueOf(rcvQty));//add by fyyang 090804
					arrivalDetailsBean.setTheQty(Double.valueOf(rcvQty));//add by fyyang 090804
					arrivalDetailsBean.setRecQty(Double.valueOf(rcvQty)); //modify by fyyang 090804
					arrivalBean.setLastModifiedDate(new Date());
				}
				if (arrivalNo != null && !"".equals(arrivalNo)
						&& materialId != null
						&& !"".equals(materialId)) {
					entity = redBillRemote.getTransactionHisBean(arrivalNo,
							materialId);
					entity.setReasonId(1l);
					//entity.setTransQty(-Double.valueOf(rcvQty));
					entity.setTransQty(Double.valueOf(rcvQty));
					entity.setLastModifiedDate(new Date());
					//add by fyyang 091201
					entity.setStdCost(nowStdCost);
					entity.setLastModifiedBy(employee.getWorkerCode());
				}

				if (entity.getFromWhsNo() != null
						&& !"".equals(entity.getFromWhsNo())
						&& materialId != null && !"".equals(materialId)) {
					String warehouseInvId = remote.findInvJWarehouse(Long.valueOf(materialId), entity
							.getFromWhsNo(), employee.getEnterpriseCode());
					if (!EMPTY.equals(warehouseInvId)) {
						// 库存物料记录的远程对象
						InvJWarehouseFacadeRemote invJWarehouseRemote = (InvJWarehouseFacadeRemote) factory
								.getFacadeRemote("InvJWarehouseFacade");
						warehouseBean = invJWarehouseRemote.findById(Long
								.parseLong(warehouseInvId));
						//warehouseBean.setReceipt(warehouseBean.getReceipt()-Double.valueOf(rcvQty));//modify by fyyang 090804
						warehouseBean.setReceipt(warehouseBean.getReceipt()+Double.valueOf(rcvQty));
						warehouseBean.setLastModifiedDate(new Date());
					}
					if (entity.getFromLocationNo() != null
							&& !"".equals(entity.getFromLocationNo())) {
						String locationInvId = remote.findInvJLocation(Long.valueOf(materialId),
								entity.getFromWhsNo(), entity
										.getFromLocationNo(), employee
										.getEnterpriseCode());
						if (!"".equals(locationInvId)) {
							InvJLocationFacadeRemote invJLocationRemote = (InvJLocationFacadeRemote) factory
									.getFacadeRemote("InvJLocationFacade");
							locationBean = invJLocationRemote.findById(Long
									.parseLong(locationInvId));
							//locationBean.setReceipt(locationBean.getReceipt()-Double.valueOf(rcvQty));//modify by fyyang 090804
							locationBean.setReceipt(locationBean.getReceipt()+Double.valueOf(rcvQty));
							locationBean.setLastModifiedDate(new Date());
						}
						String lotInvId = remote.findInvJLotId(Long
								.valueOf(materialId), entity.getFromWhsNo(),
								entity.getFromLocationNo(), employee
										.getEnterpriseCode());
						if (!EMPTY.equals(lotInvId)) {
							InvJLotFacadeRemote invLotRemote = (InvJLotFacadeRemote) factory
									.getFacadeRemote("InvJLotFacade");
							lotBean = invLotRemote
									.findById(Long.parseLong(lotInvId));
							//lotBean.setReceipt(lotBean.getReceipt()-Double.valueOf(rcvQty));//modify by fyyang 090804
							lotBean.setReceipt(lotBean.getReceipt()+Double.valueOf(rcvQty));
							lotBean.setLastModifiedDate(new Date());
						}
					}
				}
				// modify by ywliu 20100129
				if("blue".equals(flag)) {
					entity.setPrice(price);
				} 
				redBillRemote.addPurchaseWarehouseRedBill(arrivalBean,arrivalDetailsBean,entity,warehouseBean,locationBean,lotBean);
			} else {
				write(MESSAGE2);
			}
		} catch (RuntimeException e) {
			write(MASSAGE3);
		} catch (Exception e) {
			write(MASSAGE4);
		}
	}
}

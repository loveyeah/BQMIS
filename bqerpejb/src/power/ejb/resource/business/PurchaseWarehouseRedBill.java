package power.ejb.resource.business;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.resource.InvCMaterial;
import power.ejb.resource.InvJLocation;
import power.ejb.resource.InvJLot;
import power.ejb.resource.InvJTransactionHis;
import power.ejb.resource.InvJWarehouse;
import power.ejb.resource.PurJArrival;
import power.ejb.resource.PurJArrivalDetails;
import power.ejb.resource.PurJOrderDetails;

/**
 * 入库红单红冲
 * 
 * @author ywliu
 * 
 */
@Remote
public interface PurchaseWarehouseRedBill {

	/**
	 * 入库单查询
	 * 
	 * @param enterpriseCode
	 * @param sDate
	 * @param eDate
	 * @param materialNo
	 * @param materialName
	 * @param whsName
	 * @param specNo
	 * @param supplierName
	 * @param purBy
	 * @param whsBy
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findPurchasehouseCheckList(String enterpriseCode,
			String sDate, String eDate, String materialNo, String materialName,
			String whsName, String specNo, String supplierName, String purBy,
			String whsBy,String isRedBill, final int... rowStartIdxAndCount);

	/**
	 * 增加一条入库红单,并且更新库存数量
	 * 
	 * @param arrivalNo
	 */
	public void addPurchaseWarehouseRedBill(PurJArrival purjArrivalBean,
			PurJArrivalDetails purJArrivalDetailBean,
			InvJTransactionHis transactionHisBean, InvJWarehouse warehouseBean,
			InvJLocation locationBean, InvJLot lotBean);

	/**
	 * 获取一条相关的历史事务表记录，用于保存事务操作
	 * 
	 * @param arrivalNo
	 * @param arrivalDetailID
	 * @return
	 */
	public InvJTransactionHis getTransactionHisBean(String arrivalNo,
			String arrivalDetailID);
}

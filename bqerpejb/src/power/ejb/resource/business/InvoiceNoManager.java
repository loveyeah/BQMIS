package power.ejb.resource.business;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

@Remote 
public interface InvoiceNoManager {
	
/**
 * 查询已入库未审核且尚未填发票的到货单信息列表
 * modify by drdu 091124 新增参数 buyer采购员
 * @param start
 * @param limit
 * @param enterpriseCode
 * @param sDate
 * @param eDate
 * @param buyer
 * @return
 */
	public PageObject findArrivalBillList(int start, int limit, String enterpriseCode, String buyer,String purNo,String supplyName, String materialName);
	
	/**
	 * 查询已入库未审核且尚未填发票的到货单物资明细信息列表
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findArrivalBillDetailList(Long id,int start,int limit,String enterpriseCode);

}

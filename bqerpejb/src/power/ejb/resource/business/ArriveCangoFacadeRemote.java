package power.ejb.resource.business;

import java.text.ParseException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.resource.InvJTransactionHis;
import power.ejb.resource.PurJArrival;
import power.ejb.resource.PurJArrivalDetails;
import power.ejb.resource.PurJOrderDetails;

/**
 * 到货登记interface
 * @author zhaozhijie
 */
@Remote 
public interface ArriveCangoFacadeRemote {

	/**
	 * 查询物资详细单
	 *
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 物资详细单
	 * @throws ParseException
	 */
	public PageObject findMaterialDetails(String purNo, String  enterpriseCode,  final int... rowStartIdxAndCount)
	  throws ParseException;

	/**
	 * 登记tab数据检索
	 *
	 * @param operateBy 操作人
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
	 * @return PageObject 登记单
	 */
	public PageObject getArriveTabData(String operateBy, String enterpriseCode, final int... rowStartIdxAndCount)
	  throws ParseException;

	/**
	 * 从到货单查询物资详细单
	 *
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 物资详细单
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAMaterialDetails(Long id, String  enterpriseCode, final int... rowStartIdxAndCount)
	  throws ParseException;
	/**
	 * 模糊查询
	 *
	 * @param queryString 采货单号/供应商/物资名称
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
	 * @return PageObject 采购单
	 * @throws ParseException
	 */
	public PageObject getstockData(String queryString, String enterpriseCode,  final int... rowStartIdxAndCount)
	  throws ParseException ; 

	/**
	 * 获取事务流水号
	 * 
	 */
	public String findTransId(String transCode,String enterpriseCode);

	/**
	 * 获取合作伙伴id
	 *
	 */
	@SuppressWarnings("unchecked")
	public String findClientId(String ClientName, String  enterpriseCode);

	/**
	 * 从采购单查询未上报到货单
	 *
     * @return PageObject 未上报到货单
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findReportArrivalDetails(String purNo, String  enterpriseCode)
	throws ParseException ;

	/**
	 *  到货登记保存操作
	 *
	 * @param lstUpdatePurJArrivaInfo 更新到货登记/验收表
	 * @param lstUpdatePurJArrivalDetailsInfo 更新到货登记/验收明细表
	 * @param lstSavePurJArrivalInfo 保存到货登记/验收表
	 * @param lstSavePurJArrivalDetailsInfo 保存到货登记/验收明细表
	 * @throws CodeRepeatException 
	 * @throws ParseException
	 */
	public void saveRegister(List<PurJArrival> lstUpdatePurJArrivaInfo,
			List<PurJArrivalDetails> lstUpdatePurJArrivalDetailsInfo,
			List<PurJArrival> lstSavePurJArrivalInfo,
			List<PurJArrivalDetails> lstSavePurJArrivalDetailsInfo) throws CodeRepeatException;

	/**
	 *  到货登记删除操作
	 *
	 * @param lstDeletePurJArrivalInfo 删除到货登记/验收表
	 * @param lstDeletePurJArrivalDetailsInfo 删除到货登记/验收明细表
	 * @throws CodeRepeatException 
	 * @throws ParseException
	 */
	public void deleteRegister(
			List<PurJArrival> lstDeletePurJArrivalInfo,
			List<PurJArrivalDetails> lstDeletePurJArrivalDetailsInfo) throws CodeRepeatException;

	/**
	 *  到货登记上报操作
	 *
	 * @param lstRptUpdatePurJArrivaInfo 上报中更新到货登记/验收表
	 * @param lstRptUpdatePurJArrivalDetailsInfo 上报中更新到货登记/验收明细表
	 * @param lstUpdatePurJOrderDetailsInfo 更新采购订单明细表
	 * @param lstSaveInvJTransactionHisInfo 更新事务历史表
	 * @throws CodeRepeatException 
	 * @throws ParseException
	 */
	public void reportRegister(
			List<PurJArrival> lstRptUpdatePurJArrivaInfo,
			List<PurJArrivalDetails> lstRptUpdatePurJArrivalDetailsInfo,
			List<PurJOrderDetails> lstUpdatePurJOrderDetailsInfo,
			List<InvJTransactionHis> lstSaveInvJTransactionHisInfo) throws CodeRepeatException;
}

/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 劳务派遣登记Remote
 * 
 * @author zhaomingjian
 * @version 1.0
 */
@Remote
public interface LaborBorrowRegisterQueryFacadeRemote {
	
	/**
	 * 
	 * @param strSignatureDateFrom
	 * @param strSignatureDateTo
	 * @param cooperateUnitId
	 * @param dcmStatus
	 * @param strEnterpriseCode
	 * @param rowStartIdxAndCount
	 * @return @return 劳务派遣登记信息
	 */
	public PageObject getLaborBorrowRegisterInfo(String strSignatureDateFrom,
			          String strSignatureDateTo,String cooperateUnitId,
			          String dcmStatus,String strEnterpriseCode,String strTransferType,
			          final int...rowStartIdxAndCount);
	/**
	 * 派遣人员信息
	 * @param strBorrowContractId
	 * @param strEnterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getBorrowEmployeeInfo(String strBorrowContractId,
			          String strEnterpriseCode,
			          final int...rowStartIdxAndCount);
    
}

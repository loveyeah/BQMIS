/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.hr;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for WorkContractFacade.
 * 
 * @author sufeiyu
 */
@Remote
public interface WorkContractFacadeRemote {

	/**
	 * 取得指定的合同信息
	 * @param strEnterpriseCode
	 * @param workcontractid
	 * @return
	 * @throws SQLException 
	 */
    @SuppressWarnings("unchecked")
	public PageObject getContractDetail(String strEnterpriseCode, Long empId) throws SQLException;
    
    /**
     * 根据部门ID取得部门名 
     * @param strEnterpriseCode
     * @param fristDepId
     * @return
     */
    public String getFirstdeptName(String strEnterpriseCode, Long fristDepId) ;
    
    /**
     * 取得人员部门和岗位
     * @param strEnterpriseCode
     * @param lngEmpId
     * @return
     */
    public Object[] getDeptAndStation(String strEnterpriseCode, Long lngEmpId);
}

/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 新进/离职员工统计查询接口
 * 
 * @author zhouxu
 * @version 1.0
 */
@Remote 
public interface HrJNewAndDimissionFacadeRemote {

    /**
     * 新进/离职员工统计查询
     * 
     * @param argYear
     *            年份
     * @param argDeptId
     *            部门id
     * @param argEnterpriseCode
     *            企业编码
     * @return pageobject类型
     */
    public PageObject findTotal(String argYear, String argDeptId, String argEnterpriseCode,
            final int... rowStartIdxAndCount);

    /**
     * 新进员工统计查询
     * 
     * @param argYear
     *            年份
     * @param argDeptId
     *            部门id
     * @param argEnterpriseCode
     *            企业编码
     * @return pageobject类型
     */
    public PageObject findNew(String argYear, String argDeptId, String argEnterpriseCode,
            final int... rowStartIdxAndCount);

    /**
     * 离职员工统计查询
     * 
     * @param argYear
     *            年份
     * @param argDeptId
     *            部门id
     * @param argEnterpriseCode
     *            企业编码
     * @return pageobject类型
     */
    public PageObject findDimission(String argYear, String argDeptId, String argEnterpriseCode,
    		String typeId,String advicenoteNo,
            final int... rowStartIdxAndCount);
}

/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 全公司考勤查询接口
 * 
 * @author huangweijie
 */
@Remote
public interface AllAttendanceQueryFacadeRemote {
    /**
     * 全公司考勤查询方法
     * @param examineDate 审核年月
     * @param argDeptId 审核部门ID
     * @param argEnterpriseCode 企业编码
     */
    public TimeKeeperExamineForm getExamineInfo(String examineDate,
            String argDeptId, String argEnterpriseCode, Properties p, String empDeptId, String webOrRpt)
            throws Exception;

    /**
     * 通过上级审核部门取得考勤部门
     * 
     * @param pid
     * @param enterpriseCode
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageObject getDeptsByTopDeptid(String enterpriseCode) ;

    /**
     * 查询职工考勤记录
     * @param empId
     * @param enterpriseCode
     * @param strStartDate
     * @param strEndDate
     * @return
     */
    public PageObject getEmpAttendance(String empId, String enterpriseCode,
            String strStartDate, String strEndDate, String checkFlag,
            List<Map<String, Object>> workOrRestList, String attendanceDeptId, String empDeptId)
            throws Exception;

    /**
     * 得到审核记录信息
     * @param examineDate 审核年月
     * @param argDeptId 审核部门ID
     * @param argEnterpriseCode 企业编码
     * @return PageObject 审核记录信息
     * @throws Exception
     */
    public PageObject getAuditeList(String examineDate, String argDeptId,
            String argEnterpriseCode) throws Exception;
}

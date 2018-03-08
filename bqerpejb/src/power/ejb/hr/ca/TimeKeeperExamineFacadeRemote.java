/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.DataFormatException;

import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * 考勤员审核接口
 * 
 * @author zhouxu
 */
@Remote
public interface TimeKeeperExamineFacadeRemote {
    /**
     * 考勤员审核查询
     * 
     * @param examineDate
     * @param examineDept
     * @param enterpriseCode
     * @return 动态表头的store
     * @throws ParseException
     * @throws DataChangeException
     * @throws Exception
     */
    public TimeKeeperExamineForm getExamineInfo(String examineDate, String examineDept, String enterpriseCode,
            Properties p) throws ParseException, DataChangeException, Exception;

    /**
     * 通过上级审核部门取得考勤部门
     * 
     * @param pid
     * @param enterpriseCode
     * @return
     */
    public PageObject getDeptsByTopDeptid(String enterpriseCode, Long loginEmp);

    /**
     * 查询职工考勤记录
     * 
     * @param empId
     * @param enterpriseCode
     * @param strStartDate
     * @param strEndDate
     * @return
     * @throws ParseException
     * @throws Exception
     */
    public TimeKeeperExamineStandardTime getEmpAttendance(String empId, String enterpriseCode, String strStartDate,
            String strEndDate, String checkFlag, List<Map<String, Object>> workOrRestList, String attendanceDeptId,
            String deptId) throws ParseException, Exception;

    /**
     * 批量保存员工考勤记录
     * 
     * @param records
     * @param enterpriseCode
     * @param workerCode
     * @param empId
     * @throws DataChangeException
     * @throws DataFormatException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveEmpAttendance(ArrayList<Map<String, Object>> records, String enterpriseCode, String workerCode,
            String empId) throws DataChangeException, DataFormatException;

    /**
     * 撤销前回审核
     * 
     * @param attendanceDeptId
     * @param checkYear
     * @param checkMonth
     * @throws DataChangeException
     * @throws DataFormatException
     */
    public void cancelLastCheck(ArrayList<Map<String, Object>> arrlist, String enterpriseCode, String workerCode)
            throws DataChangeException, DataFormatException;

    /**
     * 考勤审核
     * 
     * @param deptIds
     * @param strStartDate
     * @param strEndDate
     * @param argEnterpriseCode
     * @param p
     * @param workerCode
     * @param attendanceDeptId
     * @param checkEmpId
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveTotalCountByEmp(List<String> deptIds, String strStartDate, String strEndDate,
            String argEnterpriseCode, Properties p, String workerCode, String attendanceDeptId, Long checkEmpId)
            throws Exception;
}

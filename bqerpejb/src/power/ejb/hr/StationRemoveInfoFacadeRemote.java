/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
/**
 * 班组人员调动Remote
 * 
 * @author zhengzhipeng
 * @version 1.0
 */
@Remote
public interface StationRemoveInfoFacadeRemote {
    /**
     * 获得班组人员调动信息
     * 
     * @param startDate 调动开始日期
     * @param endDate 调动结束日期
     * @param oldDeptId 调动前部门ID
     * @param newDeptId 调动后部门ID
     * @param enterpriseCode 企业编码
     * @param rowStartIdxAndCount
     * @return PageObject
     */
    public PageObject getStationRemoveInfoList(String startDate, String endDate, 
	        String oldDeptId, String newDeptId, String enterpriseCode,
		final int... rowStartIdxAndCount);
    /**
     * 下拉框列表[员工姓名]
     */
    public PageObject getEmpNameList(String enterpriseCode, String deptId);
    /**
     * 新增保存班组人员调动信息
     * 
     * @param entity
     * @throws Exception 
     */
    public void save(HrJStationremove entity) throws Exception;
    /**
     * 修改保存班组人员调动信息
     * 
     * @param entity
     * @throws Exception 
     */
    public void update(HrJStationremove entity) throws SQLException, DataChangeException;
    
}
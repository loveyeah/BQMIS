/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * 员工离职登记Remote
 * 
 * @author zhengzhipeng
 * @version 1.0
 */
@Remote
public interface DismissionInfoFacadeRemote {
    /**
     * 获得员工离职登记信息
     * 
     * @param startDate
     *                年度
     * @param endDate
     *                部门ID
     * @param oldDeptId
     *                离职类别ID
     * @param enterpriseCode
     *                企业编码
     * @param rowStartIdxAndCount
     * @return PageObject
     */
    public PageObject getDismissionInfoList(String flag,String year, String deptId,
	    String typeId, String advicenoteNo, String enterpriseCode,
	    final int... rowStartIdxAndCount);
    /**
     * 删除员工离职登记信息(未存档的)
     * 
     * @param dimissionid 离职人员id
     * @param enterpriseCode 企业编码
     * @param lastModifiedDate
     * @throws DataChangeException 
     */
    public void delete(String dimissionid, String workerCode,
	    String enterpriseCode, String lastModifiedDate) throws DataChangeException;
    /**
     * 修改(保存or存档)离职员工登记信息(含排他)
     * 
     * @param entity 实体信息
     * @param lastModifiedDate 画面端的上次修改时间
     * @param empLastModifiedDate 画面端的上次修改时间（人员基本信息）
     * @return
     * @throws DataChangeException 
     * @throws SQLException 
     */
    public HrJDimission update(HrJDimission entity, String lastModifiedDate,
	    String empLastModifiedDate) throws DataChangeException, SQLException;
    /**
     * 新增(保存or存档)离职员工登记信息(含排他)
     * 
     * @param entity 实体信息
     * @param lastModifiedDate 画面端的上次修改时间
     * @param empLastModifiedDate 画面端的上次修改时间（人员基本信息）
     * @return
     * @throws DataChangeException
     * @throws SQLException
     * @throws CodeRepeatException
     */
    public HrJDimission save(HrJDimission entity, String lastModifiedDate,
	    String empLastModifiedDate) throws DataChangeException, SQLException,
	    CodeRepeatException;
    /**
     * 通过人员id获得基本信息
     * 
     * @param enterpriseCode 企业编码
     * @param dimissionid 人员id
     * @return PageObject
     */
    public PageObject getEmpInfo(String enterpriseCode, String dimissionid);
}
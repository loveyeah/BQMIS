/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.text.ParseException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * 劳动合同登记EJB接口
 * 
 * @see power.ejb.hr.HrJWorkcontract
 * @author zhouxu
 */
@Remote
public interface HrJWorkcontractFacadeRemote {
    /**
     * Perform an initial save of a previously unsaved HrJWorkcontract entity.
     * All subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            HrJWorkcontract entity to persist
     * @throws DataChangeException 
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrJWorkcontract entity) throws DataChangeException;

    /**
     * Delete a persistent HrJWorkcontract entity.
     * 
     * @param entity
     *            HrJWorkcontract entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrJWorkcontract entity);

    /**
     * 更新劳动合同返回该合同
     * 
     * @param entity
     *            需要被更新的bean的内容
     * 
     * @throws RuntimeException
     *             更新失败
     * 
     * @throws DataChangeException
     *             排他失败
     */
    public HrJWorkcontract update(HrJWorkcontract entity) throws DataChangeException;
    
    /**
     * 更新劳动合同返回该合同
     * 
     * @param entity
     *            需要被更新的bean的内容
     * 
     * @throws RuntimeException
     *             更新失败
     * 
     * @throws DataChangeException
     *             排他失败
     */
    public void update1(HrJWorkcontract entity) throws DataChangeException;

    /**
     * 按合同id查找合同信息
     * 
     * @param id
     *            合同id
     * @param enterpriseCode
     *            企业代码
     * @return 合同bean
     */
    public HrJWorkcontract findById(Long id, String enterpriseCode);

    /**
     * Find all HrJWorkcontract entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrJWorkcontract property to query
     * @param value
     *            the property value to match
     * @return List<HrJWorkcontract> found by query
     */
    public List<HrJWorkcontract> findByProperty(String propertyName, Object value);

    public PageObject findByEmpId(Long empId, String enterpriseCode);

    public List<HrJWorkcontract> findByDeptId(Object deptId);

    public List<HrJWorkcontract> findByStationId(Object stationId);

    public List<HrJWorkcontract> findByWrokContractNo(Object wrokContractNo);

    public List<HrJWorkcontract> findByFristDepId(Object fristDepId);

    public List<HrJWorkcontract> findByFristAddrest(Object fristAddrest);

    public List<HrJWorkcontract> findByContractTermId(Object contractTermId);

    public List<HrJWorkcontract> findByIfExecute(Object ifExecute);

    public List<HrJWorkcontract> findByContractContinueMark(Object contractContinueMark);

    public List<HrJWorkcontract> findByMemo(Object memo);

    public List<HrJWorkcontract> findByInsertby(Object insertby);

    public List<HrJWorkcontract> findByEnterpriseCode(Object enterpriseCode);

    public List<HrJWorkcontract> findByIsUse(Object isUse);

    public List<HrJWorkcontract> findByLastModifiedBy(Object lastModifiedBy);

    /**
     * Find all HrJWorkcontract entities.
     * 
     * @return List<HrJWorkcontract> all HrJWorkcontract entities
     */
    public List<HrJWorkcontract> findAll();

    /**
     * 根据人员ID查找部门,岗位,合同信息
     * 
     * @param empId
     *            人员Id
     * @param enterpriseCode
     *            企业编码
     * @return
     * @throws RuntimeException
     * @throws ParseException
     */
    public PageObject getWorkContractInfo(Long empId, String enterpriseCode) throws RuntimeException, ParseException;

    /**
     * 批量保存合同信息
     * 
     * @param list
     * @throws DataChangeException 
     */
    public void saveBat(List<HrJWorkcontract> list) throws DataChangeException;

    /**
     * 批量查找合同信息
     * 
     * @param empIds
     * @param enterpriseCode
     * @return
     * @throws ParseException
     */
    public PageObject getWorkContractInfos(List<Long> empIds, String enterpriseCode) throws ParseException;

    public HrJWorkcontract findByEmpId(Long empId,Long contractId);
}
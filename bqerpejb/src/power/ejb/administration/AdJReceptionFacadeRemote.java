/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for AdJReceptionFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJReceptionFacadeRemote {
    /**
     * 插入来宾接待审批单数据
     * 
     * @param entity
     *            AdJReception entity to persist
     * @throws SQLException 
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(AdJReception entity) throws SQLException;

    /**
     * Delete a persistent AdJReception entity.
     * 
     * @param entity
     *            AdJReception entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(AdJReception entity);

    /**
     * 更新来宾接待审批单数据
     * 
     * @param entity
     *            AdJReception entity to update
     * @return AdJReception the persisted AdJReception entity instance, may not
     *         be the same
     * @throws SQLException 
     * @throws RuntimeException
     *             if the operation fails
     */
    public AdJReception update(AdJReception entity) throws SQLException;

    public AdJReception findById(Long id);

    /**
     * Find all AdJReception entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the AdJReception property to query
     * @param value
     *            the property value to match
     * @return List<AdJReception> found by query
     */
    public List<AdJReception> findByProperty(String propertyName, Object value);

    /**
     * Find all AdJReception entities.
     * 
     * @return List<AdJReception> all AdJReception entities
     */
    public List<AdJReception> findAll();
    /**
     * 通过查询条件获得相应的接待信息数据
     * @param strStartDate
     * @param strEndDate
     * @param strdeptCode
     * @param strWorkerCode
     * @param strIsOver
     * @param strDcmStatus
     * @param rowStartIdxAndCount
     * @return
     */
    public PageObject getReceptionQueryInfo(String strStartDate,String strEndDate,String strdeptCode,String strWorkerCode,String strIsOver, String strDcmStatus,
                final int ...rowStartIdxAndCount);

    /**
     * 通过接待审批单号获得相应的接待信息数据
     * @param strApplyNo 接待审批单号
     * @param strEnterpriseCode 企业代码
     * @param rowStartIdxAndCount
     * @return PageObject
     */
	public PageObject findByApplyNo(String strApplyNo, String strEnterpriseCode, final int ...rowStartIdxAndCount) ;
    
}
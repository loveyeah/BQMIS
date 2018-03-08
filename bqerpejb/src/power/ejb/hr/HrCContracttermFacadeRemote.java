/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCContracttermFacade.
 * 
 * @author zhouxu，jincong
 */
@Remote
public interface HrCContracttermFacadeRemote {
	/**
	 * 保存合同有效期，自动设置最后修改时间
	 * 
	 * @param entity
	 *            HrCContractterm entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCContractterm entity) throws RuntimeException;

	/**
	 * Delete a persistent HrCContractterm entity.
	 * 
	 * @param entity
	 *            HrCContractterm entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCContractterm entity);

	/**
	 * 修改合同有效期，自动设置最后修改时间
	 * 
	 * @param entity
	 *            HrCContractterm entity to update
	 * @return HrCContractterm the persisted HrCContractterm entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCContractterm update(HrCContractterm entity) throws RuntimeException;

	public HrCContractterm findById(Long id);

	/**
	 * Find all HrCContractterm entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCContractterm property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCContractterm> found by query
	 */
	public List<HrCContractterm> findByProperty(String propertyName,
			Object value);

	public List<HrCContractterm> findByContractTerm(Object contractTerm);

	public List<HrCContractterm> findByContractDisplayNo(
			Object contractDisplayNo);

	public List<HrCContractterm> findByEnterpriseCode(Object enterpriseCode);

	public List<HrCContractterm> findByIsUse(Object isUse);

	public List<HrCContractterm> findByLastModifiedBy(Object lastModifiedBy);

	public List<HrCContractterm> findByInsertby(Object insertby);
	//add by wpzhu 20100613
	public  Long  findcontractTermId(String contractterm);
   
	/**
	 * 查找所有记录
	 * 
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
	 * @return PageObject 所有记录
	 * @throws RuntimeException 
	 */
	public PageObject findAll(String enterpriseCode, final int... rowStartIdxAndCount) throws RuntimeException;
}
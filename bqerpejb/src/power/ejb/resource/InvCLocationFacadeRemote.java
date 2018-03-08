package power.ejb.resource;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for InvCLocationFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvCLocationFacadeRemote {
/**
     * 增加一条记录
     *
     * @param entity
     * @throws CodeRepeatException
     */
    public InvCLocation save(InvCLocation entity) throws CodeRepeatException;


 /**
     * 删除一条记录
     *
     * @param locationId 库位流水号
     * @throws CodeRepeatException
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(Long locationId) throws CodeRepeatException ;


    /**
     * 删除对应一个仓库的所有记录
     *
     * @param whsNo 库位流水号
     * @param workerCode 登录者id
     * @throws CodeRepeatException
     * @throws RuntimeException
     *             when the operation fails
     */
    public void deleteByWhsNo(String whsNo, String workerCode) throws CodeRepeatException;

 /**
     * 修改一条记录
     *
     * @param entity  要修改的记录
     * @return InvCLocation  修改的记录
     * @throws CodeRepeatException
     */
    public InvCLocation update(InvCLocation entity) throws CodeRepeatException;
/**
     * 查询
     * @param enterpriseCode 企业编码
     * @param whsNo 仓库编号
     * @param rowStartIdxAndCount 分页
     * @return PageObject  查询结果
     */
    @SuppressWarnings("unchecked")
    public PageObject findAll(String enterpriseCode, String whsNo, final int... rowStartIdxAndCount) ;
    /**
     * 根据主键查找记录
     * @param locationId 库位流水号
     */
    public InvCLocation findById(Long locationId) ;
	/**
	 * Find all InvCLocation entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvCLocation property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCLocation> found by query
	 */
	public List<InvCLocation> findByProperty(String propertyName, Object value);

	public List<InvCLocation> findByWhsNo(Object whsNo);

	public List<InvCLocation> findByLocationNo(Object locationNo);

	public List<InvCLocation> findByLocationName(Object locationName);

	public List<InvCLocation> findByLocationDesc(Object locationDesc);

	public List<InvCLocation> findByLocationZone(Object locationZone);

	public List<InvCLocation> findByIsAllocatableLocations(
			Object isAllocatableLocations);

	public List<InvCLocation> findByIsDefault(Object isDefault);

	public List<InvCLocation> findByLastModifiedBy(Object lastModifiedBy);

	public List<InvCLocation> findByEnterpriseCode(Object enterpriseCode);

	public List<InvCLocation> findByIsUse(Object isUse);

	/**
	 * Find all InvCLocation entities.
	 * 
	 * @return List<InvCLocation> all InvCLocation entities
	 */
	public List<InvCLocation> findAll();
}
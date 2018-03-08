package power.ejb.hr;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCLbgzbmFacade.
 *
 * @author drdu 091125
 */
@Remote
public interface HrCLbgzbmFacadeRemote {

	/**
	 * 增加一条劳保工种记录
	 * @param entity
	 * @throws CodeRepeatException
	 */
	public HrCLbgzbm save(HrCLbgzbm entity) throws CodeRepeatException;

	/**
	 * 删除一条或多条劳保工种记录
	 * @param ids
	 */
	public void deletes(String ids);

	/**
	 * 修改一条劳保工种记录详细信息
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public HrCLbgzbm update(HrCLbgzbm entity) throws CodeRepeatException;

	/**
	 * 根据ID查找一条劳保工种详细信息
	 * @param id
	 * @return
	 */
	public HrCLbgzbm findById(Long id);

	public List<HrCLbgzbm> findByProperty(String propertyName, Object value);

	/**
	 * 根据劳保名称查找列表信息
	 * @param typeName
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String typeName,String enterpriseCode, final int... rowStartIdxAndCount);
	
	/**
	 * 查找所有劳保工种
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllLbgzbms(String enterpriseCode);
}
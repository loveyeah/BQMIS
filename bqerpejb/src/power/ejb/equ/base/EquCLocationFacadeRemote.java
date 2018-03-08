package power.ejb.equ.base;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for EquCLocationFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCLocationFacadeRemote {

	/**
	 * 增加一条位置信息
	 * @param entity
	 * @return
	 */
	public int save(EquCLocation entity);


	/**
	 * 根据主键删除一条位置信息
	 * @param locationId 位置id
	 */
	public boolean delete(Long locationId);

	/**
	 * 修改一条位置信息
	 * @param entity
	 * @return
	 */
	public boolean update(EquCLocation entity);

	/**
	 * 根据id获得位置信息
	 * @param id
	 * @return
	 */
	public EquCLocation findById(Long id);

	/**
	 * 根据位置码获得一条位置信息
	 * @param locationCode
	 * @param enterpriseCode
	 * @return
	 */
	public EquCLocation findByCode(String locationCode,String enterpriseCode);

	/**
	 * 获得所有位置列表
	 * @param rowStartIdxAndCount
	 * @return List<EquCLocation> all EquCLocation entities
	 */
	public List<EquCLocation> findAll(int... rowStartIdxAndCount);
	
	/**
	 * 根据父编码查询位置信息（位置树）
	 * @param locationCode 位置码
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	public List<EquCLocation> getListByParent(String locationCode,String enterpriseCode);
	
	/**
	 * 是否有子节点
	 * @param locationCode
	 * @param enterpriseCode
	 * @return
	 */
	public boolean ifHasChild(String locationCode,String enterpriseCode);
}
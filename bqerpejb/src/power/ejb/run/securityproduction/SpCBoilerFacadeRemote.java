package power.ejb.run.securityproduction;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for SpCBoilerFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpCBoilerFacadeRemote {
	/**
	 * 增加设备信息
	 * 
	 * @param entity
	 * @throws RuntimeException
	 */
	public void save(SpCBoiler entity);

	public void delete(SpCBoiler entity);

	/**
	 * 修改设备信息
	 * 
	 * @param entity
	 * @return
	 */
	public boolean update(SpCBoiler entity);

	public SpCBoiler findById(Long id);

	public List<SpCBoiler> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<SpCBoiler> findAll(int... rowStartIdxAndCount);

	/**
	 * 根据父id查询设备信息（用于锅炉设备树）
	 * 
	 * @param equCode
	 * @param enterpriseCode
	 * @return
	 */
	public List<SpCBoiler> getListByParent(String equCode, String enterpriseCode);

	/**
	 * 判断是否有子节点
	 * 
	 * @param equCode
	 * @param enterpriseCode
	 * @return
	 */
	public boolean IfHasChild(Long equCode, String enterpriseCode);

	/**
	 * 根据ID获得设备信息
	 * 
	 * @param boilerId
	 * @param enterpriseCode
	 * @return
	 */
	public SpCBoiler findByCode(String boilerId, String enterpriseCode);
}
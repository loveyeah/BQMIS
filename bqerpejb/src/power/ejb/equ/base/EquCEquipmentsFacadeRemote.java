package power.ejb.equ.base;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCEquipmentsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCEquipmentsFacadeRemote {
	/**
	 * 增加设备信息
	 * @param entity
	 * @throws RuntimeException
	 */
	public int save(EquCEquipments entity);

	/**
	 * 删除设备信息
	 * @param entity
	 * @throws RuntimeException
	 */
	public void delete(EquCEquipments entity);


	/**
	 * 修改设备信息
	 * @param entity
	 * @return
	 */
	public boolean update(EquCEquipments entity);

	/**
	 * 根据id获得设备信息
	 * @param id
	 * @return
	 */
	public EquCEquipments findById(Long id);

	

	/**
	 * 查找所有设备信息
	 * @param rowStartIdxAndCount
	 * @return List<EquCEquipments> all EquCEquipments entities
	 */
	public List<EquCEquipments> findAll(int... rowStartIdxAndCount);
	
	/**
	 * 根据父编码查询设备信息（用于设备树）
	 * @param equCode
	 * @param enterpriseCode
	 * @return
	 */
	public List<EquCEquipments> getListByParent(String equCode,String enterpriseCode);
	
	/**
	 * 根据功能码获得设备信息
	 * @param attributeCode
	 * @param enterpriseCode
	 * @return
	 */
	public EquCEquipments findByCode(String attributeCode,String enterpriseCode);
	
	
  /**
   * 模糊查询设备列表
   * @param NameOrCode 设备名称或功能码
   * @param enterpriseCode
   * @param start
   * @param limit
   * @return
   */
	public PageObject findListByNameOrCode(String NameOrCode,String enterpriseCode,int start,int limit);

	/**
	 * 根据位置编码查询设备列表
	 * @param locationCode
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findListByLocationCode(String locationCode,String enterpriseCode,int start,int limit);

	/**
	 * 删除位置码时将设备的位置码置为空
	 * @param locationCode
	 * @param enterpriseCode
	 */
	public void deleteLocationCode(String locationCode,String enterpriseCode);
	/**
	 * 根据安装点码查询设备列表
	 * @param installCode
	 * @param enterpriseCode
	 * @param start
	 * @param limit
	 * @return
	 */
	public PageObject findListByInstallCode(String installCode,String enterpriseCode,int start,int limit);

	/**
	 *  删除安装点码时将设备的安装点码置为空
	 * @param installCode
	 * @param enterpriseCode
	 */
	public void deleteInstallationCode(String installCode,String enterpriseCode);
	
	/**
	 * 判断是否有子节点
	 * @param equCode
	 * @param enterpriseCode
	 * @return
	 */
	public boolean IfHasChild(String equCode,String enterpriseCode);

}
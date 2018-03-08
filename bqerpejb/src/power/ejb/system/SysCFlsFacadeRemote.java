package power.ejb.system;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote; 

import power.ear.comm.ejb.PageObject;
import power.ejb.system.form.Menu;
 

/**
 * 功能模块管理
 * @author wzhyan  
 */
@Remote

public interface SysCFlsFacadeRemote {

	/**
	 * 保存
	 * @param entity
	 */
	public SysCFls save(SysCFls entity);
    /**
     * 删除
     * @param entity
     * @return Long
     */
    public void delete(SysCFls entity);
   /**
    * 修改
    * @param entity
    * @return SysCFls
    */
	public SysCFls update(SysCFls entity);
	/**
	 * 由主键查找
	 * @param id
	 * @return SysCFls
	 */
	public SysCFls findById( Long id); 
	/**
	 * 查找当前目录下的所有功能模块和目录
	 */
	public List<SysCFls> findAll(Long parentFileId,int...rowStartIdxAndCount);	 
	/**
	 * 取得用户对应的功能模块
	 * @param parentFileId     目录id
	 * @param workerCode       工号
	 * @return List<SysCFls>
	 */
	public List<SysCFls> findFilesByWorkerId(Long parentFileId, Long workerId);
	@SuppressWarnings("unchecked")
	/**
	 * 
	 */
	public void saveCatalog(List<Map> catalog);
	/**
	 * 取得角色对应的功能模块
	 * @param roleId     角色编号
	 * @param propertyName      属性名称
	 * @param propertyValue      属性名称
	 * @return List<SysCFls>
	 */
	public List<SysCFls> findByroleIdP(Long roleId,String propertyName,String propertyValue,boolean iswait);
	/**
	 * 根据用户Id查找所有的权限
	 * @param roleId     角色编号
	 * @return List<SysCFls>
	 */
	public List<SysCFls> findFileBywId(Long workerId);
	/**
	 * 根据RoleId与parentId查找File
	 * @param roleId     角色编号
	 * @param parentFileId     父ID
	 * @param iswait     是否为已选模块
	 * @return List<SysCFls>
	 */
	public List<SysCFls> findFileByPRoleId(Long roleId,Long parentFileId,boolean iswait);
	/**
	 * 根据parentId查找File
	 * @param parentFileId     父ID
	 * @return boolean
	 */
	public boolean findFilesByPId(Long parentFileId);
	
	public Menu findMenusByWorkerId(String enterpriseCode,Long workerId);
}
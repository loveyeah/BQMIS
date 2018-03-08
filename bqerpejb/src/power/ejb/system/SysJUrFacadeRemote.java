package power.ejb.system;

import java.util.List;

import javax.ejb.Remote;


/**
 * 用户角色关联实体
 * @author wzhyan  
 */
@Remote
public interface SysJUrFacadeRemote {
	/**增加用户角色关联
	 *根据传用户角色关联实体 
	 *@param SysJUr 用户角色关联实体
	 *@return void
	 */
	public void save(SysJUr entity);

	/**物理删除用户角色关联
	 *根据传用户角色关联实体 
	 *@param SysJUr 用户角色关联实体
	 *@return void
	 */
	public void delete(SysJUr entity);

	/**
	 * 修改用户角色关联
	 */
	public SysJUr update(SysJUr entity);
	/**
	 * 根据角色Id与工号查找有效角色
	 * @param Long roleId
	 * @param Long workerId
	 * @return SysJUr
	 */
	public SysJUr findURByURId(Long roleId,Long workerId,boolean isuse);	
	
	 
	/**
	 * 验证功能模块权限
	 * @param workerId  工号
	 * @param fileId    模块ID
	 * @param filePath  文件路径
	 * @return boolean
	 */
	public boolean validateFileRight(Long workerId,String fileId,String filePath);
	
	/**
	 * 从某个角色中删除某个用户
	 *  add by fyyang 091102
	 * @param workCode
	 * @param roleId
	 */
	public void deleteByWorkCodeAndRoleId(String workCode,Long roleId);
}
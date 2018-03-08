package power.ejb.system;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;


/**
 * 功能模块管理
 * @author wzhyan  
 */
@Remote

public interface SysCRolesFacadeRemote {
	/**
	 * 增加角色
	 * @param entity
	 */
    public SysCRoles save(SysCRoles entity) throws CodeRepeatException;
    /**
	 * 增加角色并赋予权限
	 * @param entity
	 * @param rolefile
	 * @param fileIds
	 */
    public SysCRoles saveRF(SysCRoles entity,String fileIds,SysJRrs rolefile) throws CodeRepeatException;
    /**
     * 删除角色
     * @param entity
     */
    public void delete(SysCRoles entity);
    /**
     * 保存角色
     * @param entity
     * @return SysCRoles
     */
	public SysCRoles update(SysCRoles entity) throws CodeRepeatException;
	/**
	 * 由主键查找角色
	 * @param id
	 * @return SysCRoles
	 */
	public SysCRoles findById(Long id);
	/**
	 * 由角色属性查找角色
	 * @param propertyName
	 * @param propertyValue
	 * @return SysCRoles
	 */
	public PageObject findByProperty(String propertyName,String propertyValue,int...rowStartIdxAndCount);
	/**
	 * 查找角色列表
	 * @param roleType 角色类型
	 * @param roleCodeOrName 角色名称
	 * @param rowStartIdxAndCount 
	 * @return List<SysCRoles>
	 */
	public List<SysCRoles> findAll(String roleType,String roleCodeOrName, int...rowStartIdxAndCount );	
	/**
	 * 查找有效的角色列表
	 *@param isUse 是否有效
	 */
	public List<SysCRoles> findByisUse(String isUse,int...rowStartIdxAndCount );
	/**
	 * 角色名称匹配查找有效的角色列表
	 *@param String roleName 角色名称
	 *@return List<SysCRoles> 角色对象列表
	 */
	public List<SysCRoles> findByRoleName(String roleName);
	/**
	 * 取得角色中包含的用户组
	 * 返回列序:  0,关联主键  1,用户主键   2,用户编码(工号)  3,用户名称
	 * @param roleId 角色主键
	 * @return List  
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List findUsersByRole(Long roleId);
	/**
	 * 增加角色的用户组
	 * @param roleUsers 角色用户关联列表
	 */
	public void grantRoleUsers(List<SysJUr> roleUsers);
	/**
	 * 将用户从角色中去除
	 * @param roleUserIds 关联主键值中间以逗号分隔 形如 1,2,3
	 */
	public void revokeRoleUsers(String roleUserIds);
	/**
	 * 增加角色的功能模块组
	 * @param roleFiles 角色功能模块关联列表
	 */
	public void grantRoleFiles(List<SysJRrs> roleFiles);
	/**
	 * 将功能模块从角色中移除
	 * @param roleFileIds 关联主键值中间以逗号分隔 形如 1,2,3
	 */
	public void revokeRoleFiles(String roleFileIds);
	/**
	 * 根据用户ID查找该用户对应的角色列表
	 * @param workerid
	 */
	public List<SysCRoles> findRolesBywId(Long workerId);
	/**
	 * 根据fileID查找角色列表
	 * @param fileID
	 */
	public List<SysCRoles> findRolesByfileId(Long fileId);
}
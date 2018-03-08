package power.ejb.system;

import java.util.List;

import javax.ejb.Remote;


/**
 * 角色与功能模块关联实体
 * @author wzhyan  
 */
@Remote
public interface SysJRrsFacadeRemote {
	/**
	 * 增加角色与功能模块关联
	 */
	public void save(SysJRrs entity);

	/**
	 * 物理删除角色与功能模块关联
	 */
	public void delete(SysJRrs entity); 
	/**
	 * 修改角色与功能模块关联
	 * @param entity
	 * @return SysJRrs
	 */
	public SysJRrs update(SysJRrs entity); 
	/**
	 * 根据角色名称查找功能模块
	 * @param roleId
	 * @param iswait
	 * @return SysCFls
	 */
	public List<SysCFls> findFilesByRoleId(Long roleId,boolean iswait);
	/**
	 * 根据角色编号与功能编号查找功能角色列表
	 * @param roleId
	 * @param fileId
	 * @return SysJRrs
	 */
	public SysJRrs findByRoleIdFileId(Long roleId,Long fileId);
}
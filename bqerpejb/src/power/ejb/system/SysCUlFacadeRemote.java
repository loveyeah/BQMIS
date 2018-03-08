package power.ejb.system;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.system.SysCUlFacade.workerObj;

/**
 * 用户管理
 * 
 * @author wzhyan
 */
@Remote
public interface SysCUlFacadeRemote {
	/**
	 * 增加用户
	 * 
	 * @param entity
	 */
	public SysCUl save(SysCUl entity) throws CodeRepeatException;

	/**
	 * 删除用户
	 */
	public void delete(SysCUl entity);

	/**
	 * 修改用户
	 */
	public SysCUl update(SysCUl entity) throws CodeRepeatException;

	/**
	 * 由主键查找用户
	 * 
	 * @param id
	 *            用户主键
	 * @return SysCUl
	 */
	public SysCUl findById(Long id);

	/**
	 * 查找用户所在的角色组
	 * 
	 * @param workerId
	 * @return List column: 0:关联主键 1:角色主键 2:角色名称
	 */
	@SuppressWarnings("unchecked")
	public List findRolesByUser(Long workerId);

	/**
	 * 判断用户登录权限
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param workerCode
	 *            工号
	 * @param password
	 *            密码
	 * @return boolean
	 */
	public boolean checkUserLoninRight(String enterpriseCode,
			String workerCode, String password);

	/**
	 * 判断用户登录权限并且返回用户主键
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param workerCode
	 *            工号
	 * @param password
	 *            密码
	 * @return Long
	 */
	public SysCUl checkUserRightAndReturnWorkerId(String enterpriseCode,
			String workerCode, String password);

	/**
	 * 根据用户属性模糊查找用户
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性值
	 * @return PageObject
	 */
	public PageObject findUserByProperty(String propertyName,
			String propertyValue, int... rowStartIdxAndCount);
	
	/**
	 * 由企业编码,工号或姓名模糊查找用户列表
	 * @param enterpriseCode      企业编码
	 * @param workerCodeOrName    工号或姓名
	 * @param rowStartIdxAndCount 页码
	 * @return PageObject
	 */
	public PageObject getUsersBy(String enterpriseCode,String workerCodeOrName, int... rowStartIdxAndCount);

	/**
	 * 根据角色编号与用户编号或者姓名查找已选与未选用户
	 * 
	 * @param Long
	 *            roleId 角色编号
	 * @param boolean
	 *            iswait 用户是否存在(true:未选用户,false:已选用户)
	 * @param String
	 *            workerCodeOrName 工号或者名称
	 * @param int
	 *            rowStartIdxAndCount 分页数量
	 * @return PageObject 用户对象列表
	 */
	public PageObject findByroleIdAndCodeOrName(Long roleId,
			String workerCodeOrName, boolean iswait, int... rowStartIdxAndCount);

	/**
	 * 保存用户同时保存角色
	 * 
	 * @param user
	 *            用户实例
	 * @param roleIds
	 *            角色id
	 * @return SysCUl
	 */
	public SysCUl saveUR(SysCUl entity, String roleIds, SysJUr roleuser)
			throws CodeRepeatException;

	/**
	 * 根据fileId查找用户列表
	 * 
	 * @param fileId
	 * @return PageObject
	 */
	public PageObject findUsersByfileId(long fileId, int... rowStartIdxAndCount);

	public List<SysCUl> findUserByWorkerCode(String workerCode);

	/**
	 * 验证用户权限
	 * 
	 * @param enterpriseCode
	 * @param workerCode
	 * @param password
	 * @return boolean
	 */
	public boolean checkUserRight(String enterpriseCode, String workerCode,
			String password);

	public workerObj checkUserRightoutName(String enterpriseCode,
			String workerCode, String password);
	/**
	 * 根据角色编号查找在某个部门下的未选用户
	 * 
	 * @param Long
	 *            roleId 角色编号
	 *  @param Long
	 *  		  deptId 部门ID          
	 * @param int
	 *            rowStartIdxAndCount 分页数量
	 * @return PageObject 用户对象列表
	 */
	public PageObject findUserByDeptAndRole(Long roleId,String fuzzy,Long deptId,int... rowStartIdxAndCount);
	
	
	public String findWorkerCodeByName(String workerName);
	
	public PageObject findWorkerByRoleId(String roleId,String deptCode);
	
}
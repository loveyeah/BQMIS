/**
 * 
 */
package power.ejb.hr.dao;

/**
 * 用户拥有的角色信息
 * @author Administrator
 *
 */
public class SysVUserRole implements java.io.Serializable{
	private Long roleId;
	private String roleName;
	/**
	 * 
	 */
	public SysVUserRole() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param roleId
	 * @param roleName
	 */
	public SysVUserRole(Long roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}
	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}
	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}

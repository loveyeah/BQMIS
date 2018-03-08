/**
 * 
 */
package power.ejb.hr.dao;


/**
 * Remote interface for SysVUserLoginFacade.
 * 
 * @author MyEclipse Persistence Tools
 */

public interface ISysVUserLogin {
	/**
	 * 
	 * @param username
	 *            登录账号
	 * @param password
	 *            登录密码
	 * @return SysVUserLogin
	 * @throws Exception
	 */
	public SysVUserLogin CheckRight(String username, String password)
			throws Exception;
	
}
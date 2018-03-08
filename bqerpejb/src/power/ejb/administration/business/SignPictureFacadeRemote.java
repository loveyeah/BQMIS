/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJEmployeesignpicture;

/**
 * 个性签名维护Remote
 * 
 * @author jincong
 * @version 1.0
 */
@Remote
public interface SignPictureFacadeRemote {

	/**
	 * 查找人员签名信息
	 * 
	 * @param enterpriseCode
	 * 查询员工姓名
	 * @param queryName 
	 *            企业代码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return 查找人员签名信息
	 * @throws SQLException
	 */
	public PageObject findPersonSignInfo(String queryName,String enterpriseCode,
			final int... rowStartIdxAndCount) throws SQLException;

	/**
	 * 根据id查找图片信息
	 * 
	 * @param id
	 * @return 图片信息
	 * @throws SQLException
	 */
	public AdJEmployeesignpicture findPictrueById(String id)
			throws SQLException;

	/**
	 * 根据人员编码查找图片信息
	 * 
	 * @param workerCode 人员编码
	 * @param enterpriseCode 企业编码
	 * @return 图片信息
	 * @throws SQLException
	 */
	public PageObject findPictrueByCode(String workerCode, String enterpriseCode) throws SQLException;
}

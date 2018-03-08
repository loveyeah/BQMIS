/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.business;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface MaterialNameQuery {
	/**
	 * 物料查询
	 * modify by fyyang 090624 增加按物料类别查询
	 * @param fuzzy
	 *            查询字符串
	 * @param rowStartIdxAndCount
	 *            分页
	 * @return PageObject 
	 * issue 区分是否为领料单中的物资查询 1：是 modified by liuyi 20100504 
	 */
//	public PageObject getMaterialList(String enterpriseCode, String fuzzy,String materialClassCode,String flag,
//			final int... rowStartIdxAndCount);
	public PageObject getMaterialList(String issue,String enterpriseCode, String fuzzy,String materialClassCode,String flag,
			final int... rowStartIdxAndCount);
	
	/**
	 * 查询该用户对应的查询权限
	 * add by fyyang 091112
	 * @param workId
	 * @param fileAddr
	 * @param enterpriseCode
	 * @return
	 */
	public String getQueryRightByWorkId(Long workId, String fileAddr,String enterpriseCode);

}

/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.business;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 库存物料查询及选择Interface
 * 
 * @author jincong
 * @version 1.0
 */
@Remote
public interface MaterialQueryAndSelect {

	/**
	 * 查询库存物料
	 * modify by fyyang 090623 增加物料类别查询条件
	 * @param fuzzy 模糊查询参数
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数（开始行数和查询行数）
	 * @return 库存物料
	 */
	public PageObject findMaterial(String fuzzy, String enterpriseCode,String materialClassCode, final int... rowStartIdxAndCount);
	
	/**
	 * 查询某个物资的库存信息
	 * add by fyyang 090424
	 * @param materialNo 物资编码
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findMaterialByMaterialNo(String materialNo, String enterpriseCode, final int... rowStartIdxAndCount);
}

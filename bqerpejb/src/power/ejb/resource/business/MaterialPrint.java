/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.business;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.resource.InvJBook;
import power.ejb.resource.MaterialCheckPrintReportBean;
import power.ejb.resource.form.LotMaterialInfo;

/**
 * 物料盘点打印Interface
 * 
 * @author chenshoujiang
 * @version 1.0
 */
@Remote
public interface MaterialPrint {

	/**
	 * 
	 * @param entity 
	 * 				物料盘点主表entity
	 * @param list 
	 * 				物料盘点明细表list
	 * @throws CodeRepeatException 
	 */
	 @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveMaterial(InvJBook entity,List<LotMaterialInfo> list) throws CodeRepeatException;
	/**
	 * 批号记录数据查询
	 * 
	 * @param rowStartIdxAndCount
	 *            分页
	 * @param    delayStore
	 *            待盘仓库
	 * @param    delayLocation
	 *            待盘库位
	 * @param    delayMaterial
	 *            待盘物料
	 *  @param   planer
	 *            采购员id
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getMaterialList(String delayStore,String delayLocation,String list1,
				String list2,String delayMaterial,String planer,String materialSortId,
				String enterpriseCode,final int... rowStartIdxAndCount);
	
	/**
	 * 物料盘点打印表
	 *
	 * @param delayStore 待盘仓库
	 * @param delayLocation 待盘库位
	 * @param list1 待盘物料分类1
	 * @param list2 待盘物料分类2
	 * @param delayMaterial 待盘物料
	 * @param planer 计划员
	 * @param enterpriseCode 企业编码
     * @return MaterialCheckPrintReportBean 物料盘点打印表明细
	 */
	public MaterialCheckPrintReportBean findAllForMaterialCheckReport(String delayStore,String delayLocation,String list1,
			String list2,String delayMaterial,String planer,String materialSortId,
			String enterpriseCode);
}
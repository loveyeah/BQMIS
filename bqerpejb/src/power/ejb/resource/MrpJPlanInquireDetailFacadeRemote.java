package power.ejb.resource;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for MrpJPlanInquireDetailFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface MrpJPlanInquireDetailFacadeRemote {
	
	
	/**
	 * 增加
	 * @param entity
	 * @return
	 */
	public MrpJPlanInquireDetail save(MrpJPlanInquireDetail entity);

	/**
	 * 删除单条记录
	 * @param id
	 */
	public void delete(Long id);

	/**
	 * 删除多条记录
	 * @param ids
	 */
	public void deleteMulti(String ids);
	
	public MrpJPlanInquireDetail update(MrpJPlanInquireDetail entity);

	public MrpJPlanInquireDetail findById(Long id);

	public List<MrpJPlanInquireDetail> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<MrpJPlanInquireDetail> findAll(int... rowStartIdxAndCount);
	
	/**
	 * 询价明细查询
	 * @param strWhere
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAllList(String strWhere,final int... rowStartIdxAndCount);
	
	/**
	 * 询价单打印页面的
	 * @param gatherIds
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findInquireDetailByGatherId(String gatherIds,final int... rowStartIdxAndCount);
	
	/**
	 * 询价单报表打印数据
	 * @param inquireDetailId
	 * @param gatherIds
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public List getPrintModel(String gatherIds,String inquireDetailId );
	
	/**
	 * 获得报价信息列表
	 * @param materialName
	 * @param buyer
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findQuotedPriceList(String materialName,String buyer,String enterpriseCode,final int... rowStartIdxAndCount);
	
	/**
	 * 批量保存询价内容修改
	 * @param list   增加和修改的数据
	 * @param delIds 删除的主键
	 */
	public void modifyRecords(List<MrpJPlanInquireDetail> list,String delIds);
	
	/**
	 * 确定供应商
	 * add by fyyang 
	 * @param detailId
	 */
	public void chooseSupplier(Long detailId);
	
	/**
	 * 需求计划综合查询获得报价信息列表
	 * @param materialName
	 * @param buyer
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 *  add by ywliu 2009/7/3
	 */
	public PageObject getInquirePriceInfo(String materialName,String buyer,String requirementDetailId,String enterpriseCode,final int... rowStartIdxAndCount);
}
package power.ejb.manage.reportitem;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
/**
 * @author liuyi 20100121
 *
 */
@Remote
public interface BpReportItemManage 
{
	/**
	 * 查找所有的报表维护数据
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	PageObject findAllReportRec(String enterpriseCode, final int... rowStartIdxAndCount);
	
	/**
	 * 批量保存修改的报表维护数据
	 * @param addList
	 * @param updateList
	 * @param ids
	 */
	void saveModiReportEntity(List<BpCCbmReportName> addList,List<BpCCbmReportName> updateList,String ids);
	
	/**
	 * 通过条件查询报表指标维护数据列表
	 * @param theme 主题
	 * @param queryText 模糊查询条件 指标编码 指标别名
	 * @param enterpriseCode
	 * 	 * @param rowStartIdxAndCount
	 * @return
	 */
	PageObject getAllReportItemList(String reportId,String theme,String queryText,String enterpriseCode,final int... rowStartIdxAndCount);
	
	
	/**
	 * 批量保存报表指标数据
	 * @param addList
	 * @param updateList
	 * @param ids
	 */
	void saveReportItemModi(List<BpCCbmReportItem> addList,List<BpCCbmReportItem> updateList,String ids);
	
	
	/**
	 * 通过报表id查找该报表机组
	 * @param reportId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	PageObject getReportBlockList(String reportId,String enterpriseCode,final int... rowStartIdxAndCount);
	
	/**
	 * 批量保存或删除报表机组数据
	 * @param addList
	 * @param updateList
	 * @param ids
	 */
	void saveReportBlockModi(List<BpCCbmReportBlock> addList,List<BpCCbmReportBlock> updateList,String ids);
	
	/**
	 * 查询报表指标录入数据 元素0，列表dataIndex数据；元素1，列表header数据；其余为数据
	 * @param reportId
	 * @param yearMonth
	 * @param enterpriseCode
	 * @return
	 */
	List getReportItemInputValue(String reportId,String yearMonth,String topicId,String enterpriseCode);
	
	/**
	 * 判断该年月的数据是新增还是修改
	 * @param reportId
	 * @param yearMonth
	 * @return
	 */
	public int judgeAddOrUpdate(String reportId,String yearMonth);
	
	/**
	 * 清空该年月的数据
	 * @param reportId
	 * @param yearMonth
	 * @param enterpriseCode
	 */
	public void clearReportItemInput(String reportId,String yearMonth,String enterpriseCode);
	
	/**
	 * 保存该年月的数据
	 * @param modList
	 * @param method
	 */
	public void saveReportItemInput(List<BpJCbmReport>modList,String method);
	
	
}
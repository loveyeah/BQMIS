package power.ejb.manage.stat;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.form.BpCSmallitemReportForm;
import power.ejb.manage.stat.form.SmallReportForm;

@Remote
public interface BpCSmallitemReportFacadeRemote {
	/**
	 * 增加指标到报表
	 * 
	 * @param items
	 */
	public void addItemsToReport(List<BpCSmallitemRelation> items);

	/**
	 * 修改报表指标信息
	 * 
	 * @param updateList
	 */
	public void updateItemsToReport(List<BpCSmallitemRelation> updateList);

	/**
	 * 从报表中删除指标
	 * 
	 * @param ids
	 * @return boolean
	 */
	public boolean deleteItemsFromReport(String ids);

	/**
	 * 取得小指标报表中关联的指标 List<BpCSmallitemRelation>
	 * 
	 * @return
	 */
	public List<BpCSmallitemRelation> getRelationItems(Long reportId);

	/**
	 * 单个保存小指标数据
	 * 
	 * @param model
	 */
	public void saveItemToReport(BpCSmallitemRelation model);

	/**
	 * 相同的行列只能设置一个指标检查
	 * 
	 * @param model
	 * @return
	 */
	public boolean checkSame(BpCSmallitemRelation model);

	/*----------------------------------小指标报表基本信息维护----------------------------------------------------------*/
	/**
	 * 根据小指标报表id查询报表基本信息
	 * 
	 * @param id
	 * @return
	 */
	public BpCSmallitemReport findById(Long id);

	/**
	 * 判断小指标报表名称是否重复
	 * 
	 * @param reportName
	 *            报表名称
	 * @return Long类型 大于0 表示名称重复 不大于0 表示名称不重复
	 */
	public Long checkReportName(String reportName, String enterpriseCode);

	/**
	 * 查询所有小指标报表列表
	 * 
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findSmallItemReportList(String typeCode,
			String enterpriseCode,String workerCode, int... rowStartIdxAndCount);

	/**
	 * 批量增加小指标报表数据
	 * 
	 * @param addList
	 *            报表数据
	 */
	public void save(List<BpCSmallitemReportForm> addList);

	/**
	 * 批量删除小指标报表数据
	 * 
	 * @param ids
	 * @return true 删除成功 false 删除不成功
	 */
	public boolean delete(String ids);

	/**
	 * 批量更新小指标报表数据
	 * 
	 * @param updateList
	 *            报表数据
	 */
	public void update(List<BpCSmallitemReportForm> updateList);

	/*----------------------------------小指标报表对应的行名称维护----------------------------------------------------------*/
	/**
	 * 通过行名称设置查询对应的行设置信息
	 * 
	 * @param id
	 *            行id
	 * @return BpCSmallitemRowtype 行设置信息
	 */
	public BpCSmallitemRowtype findByRowId(Long id);

	/**
	 * 判断行设置名称是否重复
	 * 
	 * @param reportId
	 *            报表id,rowName 行设置名称
	 * @return Long类型 大于0 表示名称重复 不大于0 表示名称不重复
	 */
	public Long checkRowName(Long reportId, String rowName);

	/**
	 * 查询小指标报表对应的行设置列表
	 */
	public List<BpCSmallitemRowtype> findSmallReportRowSetList(Long reportId);

	/**
	 * 保存报表行设置
	 * 
	 * @param entity
	 */
	public void saveRowSet(BpCSmallitemRowtype entity);

	/**
	 * 批量增加小指标报表行设置数据
	 * 
	 * @param addList
	 *            报表数据
	 */
	public void saveRow(List<BpCSmallitemRowtype> addList);

	/**
	 * 批量删除小指标报表行设置数据
	 * 
	 * @param ids
	 * @return true 删除成功 false 删除不成功
	 */
	public boolean deleteRow(String ids);

	/**
	 * 批量更新小指标报表行设置数据
	 * 
	 * @param updateList
	 *            行设置列表
	 */
	public void updateRow(List<BpCSmallitemRowtype> updateList);

	/*----------------------------------小指标报表查询----------------------------------------------------------*/
	/**
	 * 取grid列
	 */
	public List<SmallReportForm> getSamallReportHeader(Long reportId);

	/**
	 * 取小指标数据
	 * 
	 * @param date
	 * @param dateType
	 * @param reportId
	 * @param rowid
	 * @return
	 */
	public List<SmallReportForm> getSamallReportData(String date,
			String quarter, String dateType, Long reportId, Long rowid);

}

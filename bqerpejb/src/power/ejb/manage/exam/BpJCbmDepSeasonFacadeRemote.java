package power.ejb.manage.exam;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ejb.manage.exam.form.BpCCbmDepForm;
import power.ejb.manage.exam.form.BpJCbmDepSeasonValue;

/**
 * Remote interface for BpJCbmDepSeasonFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpJCbmDepSeasonFacadeRemote {
	 
	public void save(BpJCbmDepSeason entity);
 
	public void delete(BpJCbmDepSeason entity);

	 
	public BpJCbmDepSeason update(BpJCbmDepSeason entity);

	public BpJCbmDepSeason findById(Long id);
 
 
	@SuppressWarnings("unchecked")
	public BpJCbmDepSeasonValue getDataInputList(String deptId,
			String itemCode, String yearStr, String enterpriseCode);
	/**
	 * 保存职能部门管理费用季度分解数据
	 * @param addlist
	 * @param updatelist
	 * @param enterpriseCode  企业编码
	 * @return
	 */
	public boolean saveDataInputList(List<BpJCbmDepSeason> addlist,
			List<BpJCbmDepSeason> updatelist, String enterpriseCode);
	/**
	 * 取得职能部门列表
	 * @param enterpriseCode 企业编码
	 * @return List<BpCCbmDepForm>
	 */
	public  List<BpCCbmDepForm> getBpDeptList(String enterpriseCode);
	/**
	 * 修改职能部门
	 * @param addList 要增加的职能部门列表
	 * @param updateList 要修改的职能部门
	 * @param delIds 要删除的职能部门的主键
	 * @return boolean
	 */
	public boolean saveBpDeptList(List<BpCCbmDep> addList,
				List<BpCCbmDep> updateList, String delIds);
	/**
	 * 删除职能部门
	 * @param ids 主键id
	 * @return boolean
	 */
	public boolean deleteBpCCbmDep(String ids);
	/**
	 * 主键查找
	 * @param id 管理费用指标主键
	 * @return BpCCbmOverheadItem
	 */
	public BpCCbmOverheadItem findBpCCbmOverheadItemById(Long id);
	/**
	 * 查询管理费用指标
	 * @param enterpriseCode 企业编码
	 * @return List<BpCCbmOverheadItem>
	 */
	public List<BpCCbmOverheadItem> getOverheadList(String enterpriseCode);
	/**
	 * 保存管理费用指标
	 * @param list   修改的记录
	 * @param delIds 删除的主键
	 * @param enterpriseCode 企业编码
	 */ 
	@SuppressWarnings("unchecked")
	public void saveBpCCbmOverheadItem(List<Map> list,String delIds,String enterpriseCode);
}
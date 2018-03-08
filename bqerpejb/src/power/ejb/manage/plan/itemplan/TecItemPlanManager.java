package power.ejb.manage.plan.itemplan;

import java.text.ParseException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

@Remote
public interface TecItemPlanManager {

	//维护页面
	/**
	 * 月度全厂技术指标计划维护
	 * @param addList
	 * @param updateList
	 * @param ids
	 */
	public void saveTecItem(List<BpCItemplanTecItem> addList,List<BpCItemplanTecItem> updateList,String ids);
	
	/**
	 * 月度全厂技术部门维护
	 * @param addList
	 * @param updateList
	 * @param ids
	 */
	public void saveTecDept(List<BpCItemplanTecDep> addList,List<BpCItemplanTecDep> updateList,String ids);
	
	/**
	 * 月度全厂技术指标计划列表
	 * @return
	 */
	public PageObject getTecItemList(int... rowStartIdxAndCount);
	
	/**
	 * 月度全厂技术部门列表
	 * @return
	 */
	public PageObject getTecDeptList(int... rowStartIdxAndCount);
	
	//月度全厂技术指标
	
	/**
	 * 技术指标计划填写
	 * 明细&主表
	 * @param addList
	 * @param updateList
	 * @param month
	 * @param ids
	 */
	public void saveTecDetail(List<BpJItemplanTecDetail> addList,List<BpJItemplanTecDetail> updateList,String month,String ids,String enterpriseCode) throws ParseException;
	/**
	 * 
	 * @param month
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getTecDeptItemList(String entryIds,String month,String status,String deptId,int... rowStartIdxAndCount);
}

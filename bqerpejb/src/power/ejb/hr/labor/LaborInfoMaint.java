package power.ejb.hr.labor;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.plan.itemplan.BpCItemplanEcoItem;
import power.ejb.manage.plan.trainplan.BpCTrainingType;

@Remote
public interface LaborInfoMaint {

    ///劳保用品分类维护
	
	/**
	 * 增加劳保用品分类信息
	 * @param entity
	 * @return
	 */
	public HrCLaborClass saveLaborClass(HrCLaborClass entity);
	
	/**
	 * 修改劳保用品分类信息
	 * @param entity
	 *
	 **/
	public HrCLaborClass updateLaborClass(HrCLaborClass entity);
	
	/**
	 * 批量删除劳保用品分类信息
	 * @param ids
	 */
	
	public HrCLaborClass findByClassId(Long id);
	
	public boolean checkClassInput(HrCLaborClass entity);
	
	public boolean checkDelete(String ids);
	
	public void deleteLaborClass(String ids);
	/**
	 * 查询劳保用品分类列表
	 * @param codeOrName
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public  PageObject findLaborClassList(String codeOrName,String enterpriseCode,int... rowStartIdxAndCount);
	
	/**
	 * 增加劳保用品信息
	 * @param entity
	 * @return
	 */
	public HrCLaborMaterial saveLaborMaterial(HrCLaborMaterial entity);
	
	/**
	 * 修改劳保用品信息
	 * @param entity
	 * @return
	 */
	public HrCLaborMaterial updateLaborMaterial(HrCLaborMaterial entity);
	
	/**
	 * 删除劳保用品信息
	 * @param ids
	 */
	public void deleteLaborMaterial(String ids);
	
	public HrCLaborMaterial findByMaterialId(Long id);
	
	
	public boolean checkMaterialInput(HrCLaborMaterial entity);
	/**
	 * 查询劳保用品列表
	 * @param laborMaterialName
	 * @param laborClass
	 * @param recieveKind
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findLaborMaterialList(String laborMaterialName,String laborClass,String recieveKind,String enterpriseCode,int... rowStartIdxAndCount);
	
	/**
	 * 保存增加修改或删除的劳保标准信息
	 * @param entity 增加或修改的list
	 * @param ids 
	 */
	public void saveLaborStandard(List<HrJLaborStandard> addList,List<HrJLaborStandard> updateList,String ids);
	/**
	 * 查询该劳保工种对应的劳保用品信息列表
	 * @param laborWorkId
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findLaborStandardList(Long laborWorkId,String enterpriseCode,int... rowStartIdxAndCount);
	
	
	/**
	 * 查询改劳保工种未选的劳保用品信息列表
	 * @param laborWorkId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findNoSelectLaborStandardList(Long laborWorkId,String enterpriseCode,int... rowStartIdxAndCount);
	
	/**
	 * 增加劳保工种变更信息
	 * @param entity
	 * @return
	 * /**
	 * 修改劳保工种变更信息
	 * @param entity
	 * @return
	 */
	
	public HrJLaborChange saveLaborChange(List<HrJLaborChange> list);
	
	

	/**
	 * 删除劳保工种变更信息
	 * @param ids
	 */
	public void deleteLaborChange(String ids);
	/**
	 * 查询劳保工作变更信息列表
	 * @param workCode
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject  findLaborChangeList(String workCode,String enterpriseCode,int... rowStartIdxAndCount);
}

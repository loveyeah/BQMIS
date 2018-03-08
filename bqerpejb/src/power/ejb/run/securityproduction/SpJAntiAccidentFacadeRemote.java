package power.ejb.run.securityproduction;

import java.util.List;

import javax.ejb.Remote;

import power.ejb.run.securityproduction.form.SpJAntiAccidentForm;

/**
 * 25项反事故措施管理.
 * 
 * @author slTang
 */
@Remote
public interface SpJAntiAccidentFacadeRemote {
	/**
	 * 增加一项反事故措施,根据父编码保存
	 * @param entity
	 * @param measureCode 父编码
	 * @return
	 */
	public String save(SpJAntiAccident entity,String measureCode);

	/**
	 * 删除一项反事故措施
	 */
	public void delete(SpJAntiAccident entity);

	/**
	 * 修改一项反事故措施
	 */
	public SpJAntiAccident update(SpJAntiAccident entity);

	public SpJAntiAccident findById(String id);
	
	/**
	 * 保存所有的修改
	 * @param entity 增加，修改，删除的25项反事故措施记录
	 * @param opFlag 增加，修改，删除操作标识 a父编码:增加，u：修改，d:删除（父编码为0，01-25）
	 * @param addList 新增的25项反事故措施详细记录
	 * @param updateList 修改的25项反事故措施详细记录
	 * @param deleteCode 删除的25项反事故措施详细记录
	 */
	@SuppressWarnings("unchecked")
	public void saveModified(SpJAntiAccident entity,String opFlag,List addList,List updateList,String deleteCode);
	
	/**
	 * 根据父编码查找其所有子反事故措施
	 * @param parentCode
	 * @param check 值1时，执行检查页面的显示树
	 * @return
	 */
	public List<SpJAntiAccidentForm> findByParentCode(String check,String parentCode,String fdManager);
	
	/**
	 * 查询所有的反措信息
	 * add by fyyang 090920
	 * @param deptCode
	 * @param enterpriseCode
	 * @return
	 */
	public List<SpJAntiAccidentForm> findAllAntiAccidentByParent(String deptCode,String enterpriseCode);
	public List<SpJAntiAccidentForm> findByCode(String deptCode);
}
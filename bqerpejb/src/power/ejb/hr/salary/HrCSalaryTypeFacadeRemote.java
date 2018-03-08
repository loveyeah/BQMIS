package power.ejb.hr.salary;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 工资类别维护
 * 090927
 * @author fyyang 
 */
@Remote
public interface HrCSalaryTypeFacadeRemote {
	
	/**
	 * 增加一条工资类别信息
	 * @param entity
	 * @throws CodeRepeatException 
	 */
	public HrCSalaryType save(HrCSalaryType entity) throws CodeRepeatException;
   /**
    * 删除一条或多条工资类别信息
    * @param ids
    */
	public void delete(String ids);
   /**
    * 修改一条工资类别信息
    * @param entity
    * @return
 * @throws CodeRepeatException 
    */
	public HrCSalaryType update(HrCSalaryType entity) throws CodeRepeatException;
    /**
     * 查找一条工资类别信息
     * @param id
     * @return
     */
	public HrCSalaryType findById(Long id);
	
	/**
	 * 查询工资类别信息列表
	 * @param isBasicData 是否基础数据
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String isBasicData,String enterpriseCode,final int... rowStartIdxAndCount);
	
	/**
	 * 查询在使用的所有工资类别
	 * @param enterpriseCode
	 * @return
	 */
	public List<HrCSalaryType> findUseSalaryTypeList(String enterpriseCode);
}
package power.ejb.run.securityproduction;

import java.util.List;

import javax.ejb.Remote;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.form.EmployeeInfo;

/**
 * 职业病检查
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJDiseasesFacadeRemote {

	/**
	 * 增加一条职业病检查信息
	 * @param entity
	 * @return
	 */
	public SpJDiseases save(SpJDiseases entity);

	
	/**
	 * 删除一条或者多条职业病信息
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids);

	
	/**
	 * 修改一条职业病检查信息
	 * @param entity
	 * @return
	 */
	public SpJDiseases update(SpJDiseases entity);

	/**
	 * 查找一条职业病检查信息
	 * @param id
	 * @return
	 */
	public SpJDiseases findById(Long id);


	/**
	 * 查找职业病信息列表
	 * @param workName 姓名
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String workName,String enterpriseCode,final int... rowStartIdxAndCount);
	
	/**
	 * 根据工号取人员信息
	 */
	public  EmployeeInfo getEmpInfoDetail(String workerCode);
}
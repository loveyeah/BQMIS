package power.ejb.run.securityproduction.danger;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

@Remote
public interface SpJDangerDeptRegisterFacadeRemote {
	/**
	 * 保存 
	 * @param entity
	 * @throws RuntimeException
	 */
	public void save(SpJDangerDeptRegister entity);
	/**
	 * 修改
	 * @param entity
	 * @return SpJDangerDeptRegister 
	 * @throws RuntimeException
	 */
	public SpJDangerDeptRegister update(SpJDangerDeptRegister entity);

	public void delete(String ids);


	/**
	 * 按照Id查询
	 * @param dangerId
	 * @return SpJDangerDeptRegister
	 */
	public SpJDangerDeptRegister findById(Long dangerId);

	/**
	 * 按照年度查询
	 * 
	 * @param dangerYear 年度
	 * @param status 状态
	 * @param deptCode 部门Code
	 * @param enterpriseCode 企业编码
	 * @param start	
	 * @param limit
	 * @return List<SpJDangerDeptRegister> 
	 */
	public List findByDangerYear(String dangerYear,String status,String deptCode,
			String enterpriseCode,Integer start,Integer limit);
	/**
	 * 获得记录条数
	 * @param dangerYear	年度
	 * @param status 状态
	 * @param deptCode 部门Code
	 * @param enterpriseCode	企业编码
	 * @return	Long 	条数
	 */
	public Long getTotalCount(String dangerYear,String status,String deptCode,
			String enterpriseCode);
	/**
	 * 更改状态
	 * @param status
	 * @param ids
	 * @param enterpriseCode
	 */
	public void changeStatus(String status, String ids, String enterpriseCode);
	/**
	 * 检查输入的部门名称是否存在
	 * @param dutyNames
	 * @param superNames
	 * @return
	 */
	
	public String checkInputDeptName(String deptName,String deptCr);
	/**
	 * 检查输入的人员名称是否存在
	 * @param dutyNames
	 * @param superNames
	 * @return
	 */
	public String  checkInputManName(String dutyNames,String dutyCr);
	/**
	 * 保存导入的数据
	 * add by fyyang 20100514
	 * @param mainEntity
	 * @param detailList
	 */
	public void importDangerDeptValue(String dangerNames,List<SpJDangerDeptRegister> ddrlist);
	/**
	 * 功能：查询责任人自查报告回录信息
	 * @author add by qxjiao 20100804
	 * @param year 
	 * @param enterprise_code
	 */
	public List<SpJDangerDeptRegister> findResultList(String year,String enterprise_code,String worker,String status,int start,int limit);
	
	public int getCount(String year,String enterprise_code,String worker,String status);
	/**
	 * 功能：查询危险源L,B2,D值
	 * @author add by qxjiao 20100804
	 * @param year 年度
	 * @param type 查询类型（L,B2,D）
	 * @param enterprise_code 企业编码
	 * @param start ,limit 分页条件
	 */
	public List findDangerValue(String year ,String type,String enterprise_code,int start,int limit);
	
	public int getValueCount(String year ,String type,String enterprise_code);
	/**
	 * 功能：导出数据
	 */
	public List getExportList(String type,String year,String enterprise_code,String worker);
	public void reportRecord(String worker);
	public void confirmReport();
	public List checkLBValue(String year,String worker,String enterprise_code);
	public List checkNullBValue();
	public PageObject queryDangerList(String year ,String status ,String name ,String enterprise_code,int start ,int limit );
	
	/**
	 * 获得当前登陆人所要处理的事务
	 * @param workId
	 * @param deptId
	 * @param workCode
	 * @return
	 */
	public List getApproveWork(Long workId,Long deptId,String workCode);
	/**
	 * 重大危险源落实部门录入页面
	 * 添加，导入按钮是否可用
	 * add by kzhang 20100811
	 * @param year
	 * @param enterpriseCode
	 * @return Long 大于0可用
	 */
	public Long checkIsEditable(String year,String enterpriseCode);
}
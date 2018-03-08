package power.ejb.run.securityproduction;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 特种作业人员管理
 * 
 * @author fyyang 090602
 */
@Remote
public interface SpJSpecialoperatorsFacadeRemote {
	
	/**
	 * 增加一条特种作业人员信息
	 * @param entity
	 * @return
	 */
	public SpJSpecialoperators save(SpJSpecialoperators entity);
	
	/**
	 * 修改一条特种作业人员信息
	 * @param entity
	 * @return
	 */
	public SpJSpecialoperators update(SpJSpecialoperators entity);
	
	/**
	 * 删除一条或多条特种作业人员信息
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids);
	

	/**
	 * 查找一条特种作业人员信息
	 * @param id
	 * @return
	 */
	public SpJSpecialoperators findById(Long id);

	
	/**
	 * 查询特种人员信息列表
	 * @param workName 姓名
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String workName,String enterpriseCode,int... rowStartIdxAndCount);
}
package power.ejb.productiontec.insulation;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 绝缘仪器仪表台帐
 * @author liuyi 090706
 */
@Remote
public interface PtJyjdJYqybtzlhFacadeRemote {
	/**
	 * 增加一条绝缘仪器仪表信息
	 * @param entity
	 */
	public PtJyjdJYqybtzlh save(PtJyjdJYqybtzlh entity);

	/**
	 * 删除一条或多条绝缘仪器仪表信息
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids);
	
	
	/**
	 * 删除一条绝缘仪器仪表信息
	 * @param entity
	 */
	public void delete(PtJyjdJYqybtzlh entity);

	/**
	 * 修改一条绝缘仪器仪表信息
	 * @param entity
	 * @return
	 */
	public PtJyjdJYqybtzlh update(PtJyjdJYqybtzlh entity);

	/**
	 * 查找一条绝缘仪器仪表信息
	 * @param id
	 * @return
	 */
	public PtJyjdJYqybtzlh findById(Long id);

	

	/**
	 * 查询绝缘仪器仪表信息列表
	 * @param name 仪器仪表名称
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String name, String enterpriseCode,int... rowStartIdxAndCount);
	
	
	/**
	 * 查询绝缘仪器仪表信息列表以及预试计划
	 * @param name 仪器仪表名称
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findPjjTryAll(String name, String enterpriseCode,int... rowStartIdxAndCount);
	
	/**
	 * 对绝缘仪器仪表台帐和预试计划表中的一条信息进行编辑
	 */
	public boolean editPjjTryInfo(Long id,String operateBy,String operateDate,String memo,int testCycle);
}
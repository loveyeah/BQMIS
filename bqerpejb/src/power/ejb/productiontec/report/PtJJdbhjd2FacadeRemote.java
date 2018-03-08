package power.ejb.productiontec.report;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 大唐集团继电保护监督报表2AND4(升压站继电保护及安全自动装置校验计划完成情况月报表)
 * @author liuyi 091013
 */
@Remote
public interface PtJJdbhjd2FacadeRemote {
	/**
	 * 新增一条大唐集团继电保护监督报表2AND4(升压站继电保护及安全自动装置校验计划完成情况月报表)数据
	 */
	public void save(PtJJdbhjd2 entity);

	/**
	 * 删除一条大唐集团继电保护监督报表2AND4(升压站继电保护及安全自动装置校验计划完成情况月报表)数据
	 */
	public void delete(PtJJdbhjd2 entity);

	/**
	 * 更新一条大唐集团继电保护监督报表2AND4(升压站继电保护及安全自动装置校验计划完成情况月报表)数据
	 */
	public PtJJdbhjd2 update(PtJJdbhjd2 entity);

	public PtJJdbhjd2 findById(Long id);

	
	public List<PtJJdbhjd2> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<PtJJdbhjd2> findAll(int... rowStartIdxAndCount);
	
	public PageObject findAllByMonthAndFlag(String month,String tabelFlag,String enterpriseCode,int...rowStartIdxAndCount);
	
	public void saveModiRec(List<PtJJdbhjd2> addList,List<PtJJdbhjd2> updateList);
}
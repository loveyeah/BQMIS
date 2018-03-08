package power.ejb.productiontec.metalSupervise;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 生产技术文本报表
 * 
 * @author dswang 090707
 */
@SuppressWarnings("serial")
@Remote
public interface PtJReportFacadeRemote {
	
	/**
	 * 增加一条报表记录
	 * @param entity
	 * @return
	 */
	public void save(PtJReport entity);
	
	/**
	 * 删除一条或多条报表记录
	 * @param ids
	 */
	public void deleteMulti(String ids);
	
	/**
	 * 修改一条报表记录
	 * @param entity
	 * @return
	 */
	public PtJReport update(PtJReport entity);
	
	/**
	 * 查找一条报表记录
	 * @param id
	 * @return
	 */
	public PtJReport findById(Long id);
	
	/**
	 * 查询报表记录列表
	 * @param year
	 * @param smartDate
	 * @param reportType
	 * @param timeType
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String year,String smartDate,String reportType,String timeType,String enterpriseCode,final int... rowStartIdxAndCount);
}
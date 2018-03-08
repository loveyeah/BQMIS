package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for ZbJReportLoadFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
/**
 * @author code_cj
 *
 */
@Remote
public interface ZbJReportLoadFacadeRemote {
	
	public void save(ZbJReportLoad entity) throws CodeRepeatException;

	public void delete(ZbJReportLoad entity);

	public ZbJReportLoad update(ZbJReportLoad entity);

	public ZbJReportLoad findById(Long id);

	public List<ZbJReportLoad> findByProperty(String propertyName, Object value);
	/**删除一条或者多条报表记录
	 * @param ids
	 */
	public void  deleteReportLoad(String ids,String deleteCode); 
	public PageObject findAllByReportCode(String startTime,String endTime,String loadName,String reportCode,String enterpriseCode,int... rowStartIdxAndCount);
}
package power.ejb.productiontec.dependabilityAnalysis;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.dependabilityAnalysis.form.PtKkxJSjybForm;

/**
 * Remote interface for PtKkxJSjybFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtKkxJSjybFacadeRemote {
	
	public void save(PtKkxJSjyb entity);

	
	public void delete(PtKkxJSjyb entity);

	
	public PtKkxJSjyb update(PtKkxJSjyb entity);

	public PtKkxJSjyb findById(Long id);



	
	public PageObject findAll(String month,String enterpriseCode, int... rowStartIdxAndCount);

	public PageObject getMonthInput(String month, String enterpriseCode,
			int... rowStartIdxAndCount);

	public List<PtKkxJSjybForm> getcomputelist(String month, String enterpriseCode);
	
	/**
	 * 批量修改可靠性数据月报数据
	 * @param list
	 */
	public void modifyRecords(List<PtKkxJSjyb> list);
}
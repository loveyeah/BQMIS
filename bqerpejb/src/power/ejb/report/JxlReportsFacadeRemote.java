package power.ejb.report;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for JxlReportsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface JxlReportsFacadeRemote { 
	public void save(JxlReports entity) throws CodeRepeatException; 
	public void delete(JxlReports entity); 
	public JxlReports update(JxlReports entity); 
	public JxlReports findById(String id); 
	public List<JxlReports> findAll();
	public PageObject findReportModelList(String workerCode, int... rowStartIdxAndCount);
}
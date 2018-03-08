package power.ejb.manage.project;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PrjJStartReportFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PrjJStartReportFacadeRemote {
	/**
	 * 功能：新增开工报告
	 * add by qxjiao 20100812
	 * @param entity
	 * @return
	 */
	public PrjJStartReport save(PrjJStartReport entity);

	/**
	 * 功能：删除开工报告
	 * add by qxjiao 20100812
	 * @param entity
	 */
	public void delete(String ids);

	
	public PrjJStartReport update(PrjJStartReport entity);
	

    //modify by ypan 20100915
	public PageObject findProjectList(String workerCode,String prjSort,String start_date,String end_date,String enterprise_code,String reportType,String workCode,int... rowStartIdxAndCount);
	
	public String findProjectNo(String dept ,String date,String reporttype);
	public PrjJStartReport findReportById(Long id);
	
	public PrjJStartReport findByCon_no(String con_no);
	
}
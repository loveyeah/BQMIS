package power.ejb.report;

import java.util.List;

import javax.ejb.Remote;

import power.ejb.report.form.JxtReportNewEmpForm;

@Remote
public interface JxtReportNewEmpFacadeRemote {

	public List<JxtReportNewEmpForm> getNewEmpInfo(String newEmpids); 
	public List<Object[]>  linZhiOrder(String empids);
	public List<Object[]>  deptInMoveOrder(String stationRemoveIds);
	public List<Object[]>  deptBetMoveOrder(String stationRemoveIds);
	public List<Object[]>  neiBuJieDiaoOrder(String stationRemoveIds);
	

}
package power.web.birt.action.bqmis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.equ.failure.EquFailuresQueryForm;
import power.ejb.equ.failure.EquJFailuresQueryRemote;
import power.ejb.equ.failure.EquYearReportForm;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;

public class EquJFailuresQuery {
	
	private EquJFailuresQueryRemote remote;
	private RunCSpecialsFacadeRemote spremote;
    
	public static Ejb3Factory factory;

	static {
		factory = Ejb3Factory.getInstance();
	}

	/**
	 * 构造函数
	 */
	public EquJFailuresQuery() {
		remote = (EquJFailuresQueryRemote) factory
				.getFacadeRemote("EquJFailuresQuery");
		spremote = (RunCSpecialsFacadeRemote) factory
		.getFacadeRemote("RunCSpecialsFacade");
	}
    
	public List<EquYearReportForm> getEquFailuresQueryForm(String year ,String type) {
		List<EquYearReportForm> list =new ArrayList<EquYearReportForm>();
		if("1".equals(type)) {
		 list = remote.yearReportQuery(year,"hfdc");
		}
		else {
			list=remote.yearReportQueryByDept(year,"hfdc");
		}
//		Iterator<EquYearReportForm> it = list.iterator();
//		while(it.hasNext()) {
//			EquYearReportForm ejbFrom = it.next();
//			int size = ejbFrom.getList().size();
//			for(int i = size;i < 8;i++) {
//				EquFailuresQueryForm birtForm = new EquFailuresQueryForm();
//				birtForm.setM1count(0);
//				birtForm.setM2count(0);
//				birtForm.setM3count(0);
//				birtForm.setM4count(0);
//				birtForm.setM5count(0);
//				birtForm.setM6count(0);
//				birtForm.setM7count(0);
//				birtForm.setM8count(0);
//				birtForm.setM9count(0);
//				birtForm.setM10count(0);
//				birtForm.setM11count(0);
//				birtForm.setM12count(0);
//				ejbFrom.getList().add(birtForm);
//			}
//		}
		return list;
	}
	public EquFailuresQueryForm getYearRate(String year)
	{
		EquFailuresQueryForm model=remote.zongYearEliRate(year, "hfdc");
		return model;
	}
	public List<EquYearReportForm> getEquFailuresBugQuery(String year) {
		List<EquYearReportForm> list =new ArrayList<EquYearReportForm>();
		list=remote.bugReportQuery(year,"hfdc");
		return list;
	}
	public EquFailuresQueryForm getZongRate(String year)
	{
		EquFailuresQueryForm model=remote.zongBugEliRate(year, "hfdc");
		return model;
	}
//	public List<FailureYearReportForm> getDominationProfessionList() {
//		List<RunCSpecials> spList = spremote.findByType("0", "hfdc");
//		List<RunCSpecials> rpList = spremote.findByType("2", "hfdc");
//		List<RunCSpecials> list = new ArrayList<RunCSpecials>();
//		List<FailureYearReportForm> reportList = new ArrayList<FailureYearReportForm>();
//		for (RunCSpecials tmp : spList) {
//			if (!list.contains(tmp)) {
//				list.add(tmp);
//			}
//		}
//		for (RunCSpecials tmp : rpList) {
//			if (!list.contains(tmp)) {
//				list.add(tmp);
//			}
//		}
//		
//		Iterator<RunCSpecials> it = list.iterator();
//		while(it.hasNext()) {
//			RunCSpecials bean = it.next();
//			List<EquFailuresQueryForm> xx = remote.failureYearReport("2009", "hfdc", bean.getSpecialityCode());
//			Iterator<EquFailuresQueryForm> xxIt = xx.iterator();
//			while(xxIt.hasNext()) {
//				EquFailuresQueryForm ejbForm = xxIt.next();
//				FailureYearForm birtForm = new FailureYearForm();
//				birtForm.set
//			}
//			FailureYearReportForm failureYearReportForm = new FailureYearReportForm();
//			failureYearReportForm.setEquFailureList(xx);
//			reportList.add(failureYearReportForm);
//		}
//		return reportList;
//	}
//	public List<EquFailuresQueryForm> failureYearReportQuery() {
//		List<EquFailuresQueryForm> yearList=new ArrayList();
////		List<FailureYearReportForm> list=this.getDominationProfessionList();
////		if(list.size() >0)
////		{
////			for(int i=0;i<list.size();i++)
////			{
////				RunCSpecials model=list.get(i);
////				List<EquFailuresQueryForm> xx = remote.failureYearReport("2009", "hfdc", model.getSpecialityCode());
////				for (EquFailuresQueryForm tmp : xx) {
////					yearList.add(tmp);
////				}
////			}
////		}
////		return yearList;
//	}
}

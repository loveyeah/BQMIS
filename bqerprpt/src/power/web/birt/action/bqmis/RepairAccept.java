package power.web.birt.action.bqmis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.run.repair.RunJRepairProjectAccept;
import power.ejb.run.repair.RunJRepairProjectAcceptFacadeRemote;
import power.ejb.run.repair.form.RepairAcceptForm;
import power.web.birt.bean.bqmis.RepairAcceptBean;

public class RepairAccept {
	
	private RunJRepairProjectAcceptFacadeRemote remote;
	public RepairAccept()
	{
		 remote = (RunJRepairProjectAcceptFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunJRepairProjectAcceptFacade");
	}
	
	@SuppressWarnings("unchecked")
	public RepairAcceptBean getRepairAcceptBase(String acceptId) throws ParseException
	{
		RepairAcceptBean bean  = new RepairAcceptBean();
		List<RepairAcceptForm> formList = remote.getRepairAcceptBase(acceptId);
		if(formList.size()>0)
		{
			bean.setAcceptId(formList.get(0).getAcceptId());
			bean.setPlanStartEndDate(formList.get(0).getPlanStartEndDate());
			bean.setStartEndTime(formList.get(0).getStartEndTime());
			bean.setRepairProjectName(formList.get(0).getRepairProjectName());
			bean.setCompletion(formList.get(0).getCompletion());
			bean.setMemo(formList.get(0).getMemo());
		}
		
		RunJRepairProjectAccept repair = remote.findById(Long.parseLong(acceptId));
		if(!repair.getWorkflowStatus().equals("5")&&!repair.getWorkflowStatus().equals("0"))
		{
			List<ConApproveForm> hisList = remote.getRepairApprove(Long.parseLong(acceptId));
			for(int i =0;i<hisList.size();i++) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
				SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
				ConApproveForm from = hisList.get(i);
				if(from.getId()==4l) {
					bean.setOneAcceptName(hisList.get(i).getCaller());
					bean.setOneAcceptDate(sf.format(format.parse(hisList.get(i).getOpinionTime())));
					bean.setOneApproveMemo(hisList.get(i).getOpinion());
				}
				if(from.getId()==5l)
				{
					bean.setTwoAcceptName(hisList.get(i).getCaller());
					bean.setTwoAcceptDate(sf.format(format.parse(hisList.get(i).getOpinionTime())));
					bean.setTwoAcceptMemo(hisList.get(i).getOpinion());
				}
				if(from.getId()==6l){
					bean.setThreeAcceptName(hisList.get(i).getCaller());
					bean.setThreeAcceptDate(sf.format(format.parse(hisList.get(i).getOpinionTime())));
					 bean.setThreeAcceptMemo(hisList.get(i).getOpinion());
				}
			 }
		}
		
		return bean;
	}
}

package power.web.operateticket.stat;

import java.util.List;
import java.util.Map;

import power.ejb.opticket.form.OptickectQuyStat;
import power.ejb.opticket.form.OptickectStatuStat;
import power.ejb.opticket.stat.OptickectStatInterf;
import power.ejb.opticket.stat.RunJOpticketStat;
import power.ejb.opticket.stat.RunJOpticketStatDetail;
import power.ejb.opticket.stat.RunJOpticketStatFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class StatAction extends AbstractAction {
	public void stat() {

	}

	public void getStatData() throws JSONException {
		RunJOpticketStatFacadeRemote remote = (RunJOpticketStatFacadeRemote) factory
				.getFacadeRemote("RunJOpticketStatFacade");
		String date = request.getParameter("date");
		String specialCode = request.getParameter("specialCode");
		String shiftId = request.getParameter("shiftId");
		Map<String, Object> map = remote.getStatData(employee.getWorkerName(),
				"300MW机组电气专业"+date.replace("-", "").substring(4)+"月份操作票执行统计情况", Long.parseLong(date
						.replace("-", "")), specialCode, employee
						.getEnterpriseCode());
		Object obj1 = map.get("model");
		Object obj2 = map.get("list");
		write("{list:" + JSONUtil.serialize(obj2) + ",model:{data:"
				+ JSONUtil.serialize(obj1) + "}}");
	}

	public void clearStat()
	{
		RunJOpticketStatFacadeRemote remote = (RunJOpticketStatFacadeRemote) factory
		.getFacadeRemote("RunJOpticketStatFacade");
		String str=request.getParameter("str");
		Object object=new Object();
		try {
			object = JSONUtil.deserialize(str);
			Map map=(Map)object;
			RunJOpticketStatDetail model=new RunJOpticketStatDetail();
			if(map!=null){
				Object id=map.get("id");
				Object opticketCount=map.get("opticketCount");
				Object opticketItemCount=map.get("opticketItemCount");
				Object noProblemOpticketCount=map.get("noProblemOpticketCount");
				Object noProblemOpticketItemCount=map.get("noProblemOpticketItemCount");
				Object workcode=map.get("workcode");
				Object isclear=map.get("isclear");
				Object orderBy=map.get("orderBy");
				if(id!=null && !"".equals(id.toString())){
					model.setId(Long.parseLong(id.toString()));
				}
//				if(shiftId!=null && !"".equals(shiftId.toString()))
//					model.setShiftId(Long.parseLong(shiftId.toString()));
//				if(reportId!=null && !"".equals(reportId.toString()))
//					model.setReportId(Long.parseLong(reportId.toString()));
				if(workcode!=null)
					model.setWorkcode(workcode.toString());
				if(opticketCount!=null && !"".equals(opticketCount.toString()))
					model.setOpticketCount(Long.parseLong(opticketCount.toString()));
				if(opticketItemCount!=null && !"".equals(opticketItemCount.toString()))
					model.setOpticketItemCount(Long.parseLong(opticketItemCount.toString()));
				if(noProblemOpticketCount!=null && !"".equals(noProblemOpticketCount.toString()))
					model.setNoProblemOpticketCount(Long.parseLong(noProblemOpticketCount.toString()));
				if(noProblemOpticketItemCount!=null && !"".equals(noProblemOpticketItemCount.toString()))
					model.setNoProblemOpticketItemCount(Long.parseLong(noProblemOpticketItemCount.toString()));
				if(isclear!=null)
					model.setIsclear(isclear.toString());
				if(orderBy!=null && !"".equals(orderBy.toString()))
					model.setOrderBy(Long.parseLong(orderBy.toString()));
				remote.clearStat(model);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}

	public void saveReportInfo() {
		RunJOpticketStatFacadeRemote remote = (RunJOpticketStatFacadeRemote) factory
		.getFacadeRemote("RunJOpticketStatFacade");
		String statId = request.getParameter("statId");
		String statBy=request.getParameter("statBy");
		String deptCharge=request.getParameter("deptCharge");
		String memo=request.getParameter("memo");
		RunJOpticketStat model=remote.findById(Long.parseLong(statId));
		model.setStatBy(statBy);
		model.setDeptCharge(deptCharge);
		model.setMemo(memo);
		remote.update(model);
	}
	
	
	public void getOptickectStatuStat() throws JSONException{
		OptickectStatInterf inf=(OptickectStatInterf)factory.getFacadeRemote("OptickectStatInImp");
		String date = request.getParameter("date");
		String specialCode = request.getParameter("specialCode");
		List<OptickectStatuStat> list=inf.getOptickectStatuStat(date, specialCode, employee.getEnterpriseCode());
		String str=JSONUtil.serialize(list);
		write(str);		
	}
	
	public void getOptickectQuyStat() throws JSONException{
		OptickectStatInterf inf=(OptickectStatInterf)factory.getFacadeRemote("OptickectStatInImp");
		String date = request.getParameter("date");
		String specialCode = request.getParameter("specialCode");
		List<OptickectQuyStat> list=inf.getOptickectQuyStat(date, specialCode, employee.getEnterpriseCode());
		String str=JSONUtil.serialize(list);
		write(str);
	}
}

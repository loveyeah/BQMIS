package power.web.birt.action.bqmis;

import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.opticket.stat.RunJOpticketStat;
import power.ejb.opticket.stat.RunJOpticketStatDetail;
import power.ejb.opticket.stat.RunJOpticketStatFacadeRemote;
import power.web.birt.bean.bqmis.OpticketCountModel;

public class opticketCount {
	
	private RunJOpticketStatFacadeRemote statRemote;
	
	public opticketCount() {
		statRemote = (RunJOpticketStatFacadeRemote) Ejb3Factory.getInstance().getFacadeRemote("RunJOpticketStatFacade");
	}
	
	@SuppressWarnings("unchecked")
	public OpticketCountModel getOpticketCountModel(String statBy,String title,Long yearMonth ,String specialCode){
		Map statMap = statRemote.getStatDataPrint(statBy,title,yearMonth,specialCode,"hfdc");
		OpticketCountModel model = new OpticketCountModel();
		RunJOpticketStat baseModel = (RunJOpticketStat)statMap.get("model");
		model.setBaseModel(baseModel);
		List<RunJOpticketStatDetail> list1 = (List<RunJOpticketStatDetail>) statMap.get("29");
		model.setDeptOnelist(list1);
		List<RunJOpticketStatDetail> list2 = (List<RunJOpticketStatDetail>) statMap.get("30");
		model.setDeptTwolist(list2);
		List<RunJOpticketStatDetail> list3 = (List<RunJOpticketStatDetail>) statMap.get("31");
		model.setDeptThreelist(list3);
		List<RunJOpticketStatDetail> list4 = (List<RunJOpticketStatDetail>) statMap.get("32");
		model.setDeptFourlist(list4);
		List<RunJOpticketStatDetail> list5 = (List<RunJOpticketStatDetail>) statMap.get("33");
		model.setDeptFivelist(list5);
		return model;
	}

}

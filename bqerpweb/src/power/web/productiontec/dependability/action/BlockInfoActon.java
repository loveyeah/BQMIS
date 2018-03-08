package power.web.productiontec.dependability.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxCBlockInfo;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxCBlockInfoFacadeRemote;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxJSjlr;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxJSjlrFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BlockInfoActon extends AbstractAction {
	private PtKkxCBlockInfoFacadeRemote remote;

	public BlockInfoActon() {
		remote = (PtKkxCBlockInfoFacadeRemote) factory
				.getFacadeRemote("PtKkxCBlockInfoFacade");

	}
	@SuppressWarnings("unchecked")
	public void saveBlockInfo() throws JSONException, ParseException {
		String str = request.getParameter("isUpdate");
		String deleteId = request.getParameter("isDelete");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<PtKkxCBlockInfo> addList = new ArrayList<PtKkxCBlockInfo>();
		List<PtKkxCBlockInfo> updateList = new ArrayList<PtKkxCBlockInfo>();
		for (Map data : list) {
			String blockInfoId = null;
			String blockCode = null;
			String mgCode = null;
			String capacity = null;
			String fuelName = null;
			String businessServiceDate = null;
			String statBeginDate = null;
			String boilerName = null;
			String steamerMachine = null;
			String generationName = null;
			String primaryTransformer = null;
			String mgType = null;
			if (data.get("blockInfoId") != null)
				blockInfoId = data.get("blockInfoId").toString();
			if (data.get("blockCode") != null)
				blockCode = data.get("blockCode").toString();
			if (data.get("mgCode") != null)
				mgCode = data.get("mgCode").toString();
			if (data.get("capacity") != null)
				capacity = data.get("capacity").toString();
			if (data.get("fuelName") != null)
				fuelName = data.get("fuelName").toString();
			if (data.get("businessServiceDate") != null)
				businessServiceDate = data.get("businessServiceDate").toString();
			if (data.get("statBeginDate") != null)
				statBeginDate = data.get("statBeginDate").toString();
			if (data.get("boilerName") != null)
				boilerName = data.get("boilerName").toString();
			if (data.get("steamerMachine") != null)
				steamerMachine = data.get("steamerMachine").toString();
			if (data.get("generationName") != null)
				generationName = data.get("generationName").toString();
			if (data.get("primaryTransformer") != null)
				primaryTransformer = data.get("primaryTransformer").toString();
			if (data.get("mgType") != null)
				mgType = data.get("mgType").toString();
			
			PtKkxCBlockInfo model = new PtKkxCBlockInfo();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (blockInfoId == null) {
				model.setBlockCode(blockCode);
				model.setMgCode(mgCode);
				if(!capacity.equals(""))
				model.setCapacity(Double.valueOf(capacity));
				model.setFuelName(fuelName);
				model.setBusinessServiceDate(format.parse(businessServiceDate));
				model.setStatBeginDate(format.parse(statBeginDate));
				model.setBoilerName(boilerName);
				model.setSteamerMachine(steamerMachine);
				model.setGenerationName(generationName);
				model.setPrimaryTransformer(primaryTransformer);
				model.setMgType(mgType);
				model.setEnterpriseCode(employee.getEnterpriseCode());
				addList.add(model);
			} else {
				model = remote.findById(Long.parseLong(blockInfoId));
				model.setBlockCode(blockCode);
				model.setMgCode(mgCode);
				if(capacity !=null && !capacity.equals(""))
				model.setCapacity(Double.valueOf(capacity));
				model.setFuelName(fuelName);
				model.setBusinessServiceDate(format.parse(businessServiceDate.replaceAll("T"," ")));
				model.setStatBeginDate(format.parse(statBeginDate.replaceAll("T"," ")));
				model.setBoilerName(boilerName);
				model.setSteamerMachine(steamerMachine);
				model.setGenerationName(generationName);
				model.setPrimaryTransformer(primaryTransformer);
				model.setMgType(mgType);
				updateList.add(model);
			}

		}
		if (addList.size() > 0 || updateList.size() > 0
				|| deleteId.length() > 0)
			remote.save(addList, updateList, deleteId);
	}
	
	public void getBlockInfoList() throws JSONException{
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject object = remote.findAll(employee.getEnterpriseCode(),Integer.parseInt(start),Integer.parseInt(limit));
		write(JSONUtil.serialize(object));
	}
}

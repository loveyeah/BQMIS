/**
 * 
 */
package power.web.hr.station;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.naming.NamingException;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCStation;
import power.ejb.hr.HrCStationFacadeRemote;
import power.ejb.hr.HrCStationLevel;
import power.ejb.hr.HrCStationLevelFacadeRemote;
import power.ejb.hr.HrCStationType;
import power.ejb.hr.HrCStationTypeFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.ListCombox;
import power.web.comm.ListRange;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * @author wzhyan
 */
public class StationAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	HrCStationFacadeRemote remote;
	private HrCStationLevelFacadeRemote levelRemote;
	private StationInfo staInfo;

	public StationAction() {
		remote = (HrCStationFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("HrCStationFacade");
		levelRemote = (HrCStationLevelFacadeRemote)factory.getFacadeRemote("HrCStationLevelFacade");
	}

	public void GetData() throws Exception {
		String method = request.getParameter("method").toString();
		if (method.equals("getlist")) {
			StationList();
		} else if (method.equals("delete")) {
			StationDelete();
		} else if (method.equals("add")) {
			StationAdd();
		} else if (method.equals("update")) {
			StationUpdate();
		} else if (method.equals("getdata")) {
			getDataForUpdate();
		}
	}

	public void getAvailableStationList() {

	}

	@SuppressWarnings("unchecked")
	public void StationList() throws Exception {
		@SuppressWarnings("unused")
		Integer[] counts = { 0 };
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		String stationName=request.getParameter("stationName"); //add by fyyang 20100806
		PageObject result = remote.GetStationList(stationName,start, limit);

		Iterator it = result.getList().iterator();
		List staList = new ArrayList();
		String use = "";
		String work = "";
		while (it.hasNext()) {
			StationInfo model = new StationInfo();
			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				model.setStationId(data[0].toString());
			}
			if (data[1] != null) {
				model.setStationTypeId(data[1].toString());
			}
			if (data[2] != null) {
				model.setStationCode(data[2].toString());
			}
			if (data[3] != null) {
				model.setStationName(data[3].toString());
			}
			if (data[4] != null) {
				model.setStationDuty(data[4].toString());
			}
			if (data[5] != null) {
				if (data[5].toString().equals("Y")) {  //update by sychen 20100902
//					if (data[5].toString().equals("U")) {
					use = "使用";
				} else if (data[5].toString().equals("N")) {
					use = "停用";
				} else if (data[5].toString().equals("L")) {
					use = "注销";
				} else {
					use = "";
				}
				model.setIsUse(use);
			}
			if (data[6] != null) {
				model.setRetrieveCode(data[6].toString());
			}

			if (data[7] != null) {
				model.setMemo(data[7].toString());
			}
			//add by drdu 090911
			if(data[8] != null)
			{
				if(data[8].toString().equals("1"))
				{
					work = "常白班";
				}else if(data[8].toString().equals("2"))
				{
					work = "运行班";
				}else{
					work = "";
				}
				model.setWorkKind(work);
			}//add by drdu 090929
			if(data[9] != null){ 
				model.setStationLevelId(Long.parseLong(data[9].toString()));
			}
			if(data[10] != null)
			{
				model.setStationLevelName(data[10].toString());
			}
			staList.add(model);
		}
		String str = "{total :" + result.getTotalCount() + ", root:"
				+ JSONUtil.serialize(staList) + "}";
		//System.out.println(str);
		write(str);
	}

	public void StationAdd() throws NamingException, IOException, JSONException {
		ListRange<HrCStation> tlistTitles = new ListRange<HrCStation>();
		HrCStation model = new HrCStation();
		try {
			if (staInfo != null) {
				model.setStationTypeId(Long.parseLong(staInfo.getStationTypeId()));
				model.setStationCode(staInfo.getStationCode());
				model.setStationName(staInfo.getStationName());
				model.setStationDuty(staInfo.getStationDuty());
				model.setIsUse(staInfo.getIsUse());
				model.setMemo(staInfo.getMemo());
				model.setRetrieveCode(staInfo.getRetrieveCode());
				model.setWorkKind(staInfo.getWorkKind());//add by drdu 090911
				model.setStationLevelId(staInfo.getStationLevelId());//add by drdu090923
				model.setEnterpriseCode(employee.getEnterpriseCode());
				remote.save(model);
			}
			tlistTitles.setSuccess(true);
			tlistTitles.setMessage("添加成功！");
			String jsonstr = JSONUtil.serialize(tlistTitles);
			write(jsonstr);
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}

	public void getDataForUpdate() throws Exception {
		HrCStation model = new HrCStation();
		HrCStationTypeFacadeRemote typebll = (HrCStationTypeFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCStationTypeFacade");
		HrCStationType modeltype = new HrCStationType();
		
		HrCStationLevel level = new HrCStationLevel();
		
		String name = "";

		String levelName = "";
		
		long id = 0;
		Object obj = request.getParameter("id");
		id = Long.parseLong(obj.toString());
		model = remote.findById(id);
		if (model.getStationTypeId()!=null){
		name = model.getStationTypeId().toString();
		}else{
			name=null;
		}
		if (name != null) {
			modeltype = typebll.findById(model.getStationTypeId());
			if (modeltype != null) {
				name = modeltype.getStationTypeName();
			}
		}
	
		if(model.getStationLevelId() != null)
		{
			levelName = model.getStationLevelId().toString();
		}else{
			levelName = null;
		}
		if(levelName != null)
		{
			level = levelRemote.findById(model.getStationLevelId());
			if(level != null)
				levelName = level.getStationLevelName();
		}
		
		String str = JSONUtil.serialize(model);
		str = str.substring(0, str.length() - 1);
		str += ",\"stationTypeName\":\"" + name + "\",\"stationLevelName\":\"" + levelName + "\"}";

		write("{success: true,data:" + str + "}");
	}

	public void StationUpdate() throws Exception {
		ListRange<HrCStation> tlistType = new ListRange<HrCStation>();
		try {
			if (staInfo != null) {
				HrCStation model = new HrCStation();
				model.setStationId(Long.parseLong(staInfo.getStationId()));
				model.setStationTypeId(Long.parseLong(staInfo
						.getStationTypeId()));
				model.setStationCode(staInfo.getStationCode());
				model.setStationName(staInfo.getStationName());
				model.setStationDuty(staInfo.getStationDuty());
				model.setMemo(staInfo.getMemo());
				model.setIsUse(staInfo.getIsUse());
				model.setRetrieveCode(staInfo.getRetrieveCode());
				model.setWorkKind(staInfo.getWorkKind());//add by drdu 090911
				model.setStationLevelId(staInfo.getStationLevelId());//add by drdu 090923
				model.setEnterpriseCode(employee.getEnterpriseCode());//add by sychen 20100809
				remote.update(model);
			}
			tlistType.setSuccess(true);
			tlistType.setMessage("修改成功！");
			String jsonstr = JSONUtil.serialize(tlistType);
			write(jsonstr);
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}

	public void StationDelete() throws Exception {
		String ids = request.getParameter("ids");
		if (ids != null && ids.length() > 0) {
			remote.deletes(ids);
			String str = "{success:true,msg:\'ok\'}";
			write(str);
		}
	}

	/*
	 * 岗位名称下拉列表数据源
	 */

	public void getStationList() throws NamingException, JSONException,
			IOException {
		List<ListCombox> typecombox = new ArrayList<ListCombox>();
		HrCStationTypeFacadeRemote bll = (HrCStationTypeFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCStationTypeFacade");
		List<HrCStationType> typelist = bll.findAll();
		if (typelist.size() > 0) {
			for (HrCStationType o : typelist) {
				ListCombox lCombox = new ListCombox();
				lCombox.setId(Long.parseLong(o.getStationTypeId().toString()));
				lCombox.setName(o.getStationTypeName());
				typecombox.add(lCombox);
			}
		}
		String strlist = JSONUtil.serialize(typecombox);
		write(strlist);
	}

	public StationInfo getStaInfo() {
		return staInfo;
	}

	public void setStaInfo(StationInfo staInfo) {
		this.staInfo = staInfo;
	}

}

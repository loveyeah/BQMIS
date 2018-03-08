package power.web.hr.stationLevel;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCStationLevel;
import power.ejb.hr.HrCStationLevelFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.ListCombox;

@SuppressWarnings("serial")
public class StationLevelAction extends AbstractAction{

	private HrCStationLevelFacadeRemote remote;
	private HrCStationLevel level;
	
	public StationLevelAction()
	{
		remote = (HrCStationLevelFacadeRemote)factory.getFacadeRemote("HrCStationLevelFacade");
	}
	
	/**
	 * 增加一条岗位级别记录
	 */
	public void addStationLevel()
	{
		level.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			remote.save(level);
			write("{success:true,msg:'增加成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	/**
	 * 修改一条岗位级别记录信息
	 */
	public void updateStationLevel()
	{
		HrCStationLevel model = remote.findById(level.getStationLevelId());
		model.setStationLevel(level.getStationLevel());
		model.setStationLevelName(level.getStationLevelName());
		model.setRetrieveCode(level.getRetrieveCode());
		try {
			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	/**
	 * 删除一条或多条岗位级别记录
	 */
	public void deleteStationLevel()
	{
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 查找岗位级别记录列表信息 
	 * @throws JSONException
	 */
	public void findStationLevelList() throws JSONException
	{
		String stationLevelName = request.getParameter("stationLevelName");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findStationLevelList(employee.getEnterpriseCode(), stationLevelName, start,limit);
		} else {
			object = remote.findStationLevelList(employee.getEnterpriseCode(), null);
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}

	/**
	 * 取岗位级别下拉列表
	 * @throws JSONException
	 */
	public void findAllList() throws JSONException
	{
		List<ListCombox> typecombox = new ArrayList<ListCombox>();
		
		List<HrCStationLevel> typelist = remote.findAllList(employee.getEnterpriseCode());
		if (typelist.size() > 0) {
			for (HrCStationLevel o : typelist) {
				ListCombox lCombox = new ListCombox();
				lCombox.setId(Long.parseLong(o.getStationLevelId().toString()));
				lCombox.setName(o.getStationLevelName());
				typecombox.add(lCombox);
			}
		}
		String strlist = JSONUtil.serialize(typecombox);
		write(strlist);
	}
	
	public HrCStationLevel getLevel() {
		return level;
	}

	public void setLevel(HrCStationLevel level) {
		this.level = level;
	}
}

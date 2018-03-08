package power.web.run.runlog.action;

import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.run.runlog.RunCEarthtar;
import power.ejb.run.runlog.RunCEarthtarFacadeRemote;
import power.ejb.run.runlog.RunCEqustatusFacadeRemote;
import power.ejb.run.runlog.RunJRunlogWorker;
import power.web.comm.AbstractAction;

public class EarthMaintAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private RunCEarthtarFacadeRemote remote;
	private RunCEarthtar earth;
	//构造函数
	public EarthMaintAction()
	{
		remote = (RunCEarthtarFacadeRemote)factory.getFacadeRemote("RunCEarthtarFacade");
	}
	/*
	 * 查询列表
	 */
	public void getEarthList() throws JSONException{
		List<RunCEarthtar> list=remote.findList(employee.getEnterpriseCode());
		write(JSONUtil.serialize(list));
	}
	/**
	 * 增加地线
	 */
	public void addEarth(){
		earth.setEnterpriseCode(employee.getEnterpriseCode());
		earth.setIsUse("Y");
		remote.save(earth);
		write("{success:true,data:'数据保存成功！'}");
	}
	/**
	 * 修改地线
	 */
	public void updateEarth(){
		RunCEarthtar model=remote.findById(earth.getEarthId());
		model.setEarthName(earth.getEarthName());
		model.setMemo(earth.getMemo());
		model.setDisplayNo(earth.getDisplayNo());
		remote.update(model);
		write("{success:true,data:'数据保存成功！'}");
	}
	/**
	 * 删除地线
	 */
	public void delteEarth(){
		String ids= request.getParameter("ids");
		String [] earths= ids.split(",");
		for(int i=0;i<earths.length;i++)
		{
			RunCEarthtar model=remote.findById(Long.parseLong(earths[i]));
			model.setIsUse("N");
			remote.update(model);
		}
	}
	public RunCEarthtar getEarth() {
		return earth;
	}
	public void setEarth(RunCEarthtar earth) {
		this.earth = earth;
	}
}

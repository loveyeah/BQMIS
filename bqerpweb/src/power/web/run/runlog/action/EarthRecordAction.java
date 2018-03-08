package power.web.run.runlog.action;

import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.runlog.RunCEarthtarFacadeRemote;
import power.ejb.run.runlog.RunJEarthtar;
import power.ejb.run.runlog.RunJEarthtarFacadeRemote;
import power.web.comm.AbstractAction;

public class EarthRecordAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	private RunJEarthtarFacadeRemote recordremote;
	private RunCEarthtarFacadeRemote remote;
	private String specialcode;
	private Long runlogid;
	private Long earthid;
	private Long earthRecordId;
	private String installPlace;
	//构造函数
	public EarthRecordAction()
	{
		recordremote = (RunJEarthtarFacadeRemote)factory.getFacadeRemote("RunJEarthtarFacade");
		remote = (RunCEarthtarFacadeRemote)factory.getFacadeRemote("RunCEarthtarFacade");
	}
	/*
	 * 查询地线安装未拆除列表
	 */
	public void findEarthRecordList() throws JSONException{
		int start=0;
		int limit=99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			 start = Integer.parseInt(request.getParameter("start"));
			 limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject obj=recordremote.findInstallListBySpecial(specialcode, employee.getEnterpriseCode(),start,limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}
	/*
	 * 查询地线列表
	 */
	public void queryEarthRecordList() throws JSONException{
//		List<Object[]> list=recordremote.queryInstallListBySpecial(specialcode, employee.getEnterpriseCode());
//		StringBuffer sb = new StringBuffer();
//		sb.append("[");
//		for(Object[] o : list)
//		{
//			String earthName="";
//			if(o[1] != null)
//			{
//				earthName=remote.findById(Long.parseLong(o[1].toString())).getEarthName();
//			}
//			sb.append("{");
//			sb.append("\"earthRecordId\":"+o[0]+",");
//			sb.append("\"earthId\":\""+(o[1]==null?"":o[1])+"\",");
//			sb.append("\"earthName\":\""+(earthName==null?"":earthName)+"\",");
//			sb.append("\"specialName\":\""+(o[2]==null?"":o[2])+"\",");
//			sb.append("\"installMan\":\""+(o[3]==null?"":o[3])+"\",");
//			sb.append("\"installTime\":\""+(o[4]==null?"":o[4])+"\",");
//			sb.append("\"installPlace\":\""+(o[5]==null?"":o[5])+"\",");
//			sb.append("\"backoutMan\":\""+(o[7]==null?"":o[7])+"\",");
//			sb.append("\"backoutTime\":\""+(o[8]==null?"":o[8])+"\"");
//			sb.append("},");
//		}
//		if(sb.length()>1)
//		{
//			sb.deleteCharAt(sb.lastIndexOf(","));
//		}
//		sb.append("]");
//		write(sb.toString());
		int start=0;
		int limit=99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			 start = Integer.parseInt(request.getParameter("start"));
			 limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject obj=recordremote.queryInstallListBySpecial(specialcode, employee.getEnterpriseCode(),start,limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}
	/*
	 * 安装地线
	 */
	public void installEarthRecord(){
		RunJEarthtar model=new RunJEarthtar();
		model.setEarthId(earthid);
		model.setInstallLogid(runlogid);
		model.setSpecialityCode(specialcode);
		model.setInstallMan(employee.getWorkerCode());
		model.setInstallTime(new Date());
		model.setInstallPlace(installPlace);
		model.setIsBack("N");
		model.setEnterpriseCode(employee.getEnterpriseCode());
		model.setIsUse("Y");
		recordremote.save(model);
		write("{success:true,data:'数据保存成功！'}");
	}
	/*
	 * 拆除地线
	 */
	public void backEarthRecord(){
		RunJEarthtar model=recordremote.findById(earthRecordId);
		model.setBackoutLogid(runlogid);
		model.setBackoutMan(employee.getWorkerCode());
		model.setBackoutTime(new Date());
		model.setIsBack("Y");
		recordremote.update(model);
		write("{success:true,data:'数据保存成功！'}");
	}
	/*
	 * 删除记录
	 */
	public void deleteEarthRecord(){
		RunJEarthtar model=recordremote.findById(earthRecordId);
		model.setIsUse("N");
		recordremote.update(model);
		write("{success:true,data:'数据保存成功！'}");
	}
	public String getSpecialcode() {
		return specialcode;
	}
	public void setSpecialcode(String specialcode) {
		this.specialcode = specialcode;
	}

	public Long getEarthRecordId() {
		return earthRecordId;
	}
	public void setEarthRecordId(Long earthRecordId) {
		this.earthRecordId = earthRecordId;
	}
	public String getInstallPlace() {
		return installPlace;
	}
	public void setInstallPlace(String installPlace) {
		this.installPlace = installPlace;
	}
	public Long getRunlogid() {
		return runlogid;
	}
	public void setRunlogid(Long runlogid) {
		this.runlogid = runlogid;
	}
	public Long getEarthid() {
		return earthid;
	}
	public void setEarthid(Long earthid) {
		this.earthid = earthid;
	}
	
}

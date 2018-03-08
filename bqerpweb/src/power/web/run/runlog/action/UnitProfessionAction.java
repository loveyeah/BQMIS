package power.web.run.runlog.action;

import java.util.Date;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.runlog.RunCMainItem;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftTimeFacadeRemote;
import power.ejb.run.runlog.shift.RunCUnitprofession;
import power.ejb.run.runlog.shift.RunCUnitprofessionFacadeRemote;
import power.ejb.system.SysJRrs;
import power.web.comm.AbstractAction;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class UnitProfessionAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	RunCUnitprofessionFacadeRemote remote;
	RunCSpecialsFacadeRemote specialremote;
	private RunCUnitprofession unitProfession;
	private String ids;
	private String code;
	/**
	 * 构造函数
	 */
	public UnitProfessionAction() {
		remote = (RunCUnitprofessionFacadeRemote) factory
				.getFacadeRemote("RunCUnitprofessionFacade");
		specialremote = (RunCSpecialsFacadeRemote) factory
		.getFacadeRemote("RunCSpecialsFacade");
	}

	public void findUintProfessionList() throws JSONException {
		List<Object[]> list = remote.findUnitList(employee
				.getEnterpriseCode());
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(Object[] o : list)
		{
			sb.append("{");
			sb.append("\"id\":"+o[0]+",");
			sb.append("\"specialityCode\":\""+(o[1]==null?"":o[1])+"\",");
			sb.append("\"specialityName\":\""+(o[2]==null?"":o[2])+"\",");
			sb.append("\"HSpecialityCode\":\""+(o[3]==null?"":o[3])+"\",");
			sb.append("\"displayNo\":"+(o[4]==null?"''":o[4])+",");
			sb.append("\"HSpecialityName\":\""+(o[5]==null?"":o[5])+"\"");
			sb.append("},");
		}
		if(sb.length()>1)
		{
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		sb.append("]");
		write(sb.toString());
	}

	public void addUnitProfession() {
		String codes=request.getParameter("codes");
		
		  String [] units= codes.split(",");
			for(int i=0;i<units.length;i++)
			{
				RunCSpecials spmodel=specialremote.findByCode(units[i], employee.getEnterpriseCode());
				RunCUnitprofession model =new RunCUnitprofession();
				  if (!remote.existsByCode(units[i],employee.getEnterpriseCode())) {		
				   model.setSpecialityCode(spmodel.getSpecialityCode());
				   model.setSpecialityName(spmodel.getSpecialityName());
				   model.setDisplayNo(spmodel.getDisplayNo());
				   model.setEnterpriseCode(spmodel.getEnterpriseCode());	
				   model.setIsUse("Y");
				   remote.save(model);
				   write("{success:true,msg:'保存成功!'}");
				  }
				  else{
						write("{success:true,msg:'编码重复!'}");
					}			
			}	
	}

	public void deleteUnitProfession() {
		RunCUnitprofession model = remote.findById(unitProfession.getId());
		model.setIsUse("N");
		remote.update(model);
		write("{success:true}");
	}

	public void updateUnitProfession() {
		RunCUnitprofession model = remote.findById(unitProfession.getId());
		model.setHSpecialityCode(unitProfession.getHSpecialityCode());
		model.setDisplayNo(unitProfession.getDisplayNo());
		remote.update(model);
		write("{success:true}");
	}
	public void getSpecialExp() throws JSONException{
		List<RunCUnitprofession> list = remote.findUnitExp(code,employee.getEnterpriseCode());
		String str=JSONUtil.serialize(list);
		str=str.substring(0,str.length()-1);
		str=str+",{'specialityCode':'','specialityName':'无'}";
		str=str+"]";
		write(str);
	}
	public RunCUnitprofession getUnitProfession() {
		return unitProfession;
	}

	public void setUnitProfession(RunCUnitprofession unitProfession) {
		this.unitProfession = unitProfession;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}

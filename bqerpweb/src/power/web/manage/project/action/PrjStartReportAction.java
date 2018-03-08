package power.web.manage.project.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;


import power.ear.comm.ejb.PageObject;
import power.ejb.manage.project.PrjJStartReport;
import power.ejb.manage.project.PrjJStartReportFacadeRemote;
import power.web.comm.AbstractAction;

public class PrjStartReportAction extends AbstractAction {
	private PrjJStartReportFacadeRemote remote ;
	private PrjJStartReport model ;
	public PrjStartReportAction(){
		remote = (PrjJStartReportFacadeRemote)factory.getFacadeRemote("PrjJStartReportFacade");
	}
	
	public void findProjectNo(){
		String type=request.getParameter("type");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		String date  = format.format(new Date());
		date = date.substring(2,date.length());
		String prjNo = remote.findProjectNo(employee.getDeptId().toString(), date,type);
		this.write("{prjNo:'"+prjNo+"'}");
	}
	
	public void findPrjReportList(){
		String start_date = request.getParameter("start_date");
		String end_date = request.getParameter("end_date");
		String prj_type = request.getParameter("prj_type");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String reportType = request.getParameter("type");
		String flag=request.getParameter("flag");//1 登记查询 
		String workerCode=!"1".equals(flag)?null:employee.getWorkerCode();
		    //modify by ypan 20100915
			PageObject obj = remote.findProjectList(employee.getWorkerCode(),prj_type, start_date, end_date, employee.getEnterpriseCode(), reportType,workerCode,Integer.parseInt(start),Integer.parseInt(limit));
			try {
				this.write(JSONUtil.serialize(obj));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void addPrjReport(){
		String type = request.getParameter("type");
		model.setEnterpriseCode(employee.getEnterpriseCode());
		model.setIsUse("Y");
		model.setIsBackEntry("N");
		model.setReportType(type);
		PrjJStartReport entity =null;
		String opr = "";
		if(model.getReportId()==null){
			entity = remote.save(model);
			opr = "保存";
		}else{
			PrjJStartReport report = remote.findReportById(model.getReportId());
			if(model.getPrjTypeId()==null||"".equals(model.getPrjTypeId())){
				model.setPrjTypeId(report.getPrjTypeId());
			}
			entity = remote.update(model);
			opr = "修改";
		}
		
		if(entity!=null){
			this.write("{success:true,status:'"+opr+"成功！',reportId:'"+entity.getReportId()+"'}");
		}else{
			this.write("{success:false,status:'数据库中已存在该记录！'}");
		}
	}
	
	public void findEndPrjByCon(){
		String con_no = request.getParameter("con_no");
		model = remote.findByCon_no(con_no);
		try {
			this.write(JSONUtil.serialize(model));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deletePrjReport(){
		String id = request.getParameter("ids");
		remote.delete(id);
		this.write("{success:true,status:'删除成功！'}");
	}
	public PrjJStartReportFacadeRemote getRemote() {
		return remote;
	}

	public void setRemote(PrjJStartReportFacadeRemote remote) {
		this.remote = remote;
	}

	public PrjJStartReport getModel() {
		return model;
	}

	public void setModel(PrjJStartReport model) {
		this.model = model;
	} 
}

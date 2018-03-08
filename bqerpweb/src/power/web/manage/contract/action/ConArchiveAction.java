package power.web.manage.contract.action;


import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.business.ConJContractInfo;
import power.ejb.manage.contract.business.ConJContractInfoFacadeRemote;
import power.ejb.manage.contract.business.ConJFileOpinion;
import power.ejb.manage.contract.business.ConJFileOpinionFacadeRemote;
import power.ejb.manage.contract.business.ConJModify;
import power.ejb.manage.contract.business.ConJModifyFacadeRemote;
import power.ejb.manage.contract.form.ConFileOpinionForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ConArchiveAction extends AbstractAction{
	private ConJContractInfoFacadeRemote remote;
	private ConJFileOpinionFacadeRemote fileremote;
	private ConJModifyFacadeRemote modifyremote;
	private String fuzzy;
	private String startdate;
	private String enddate;
	private Long conId;
	private Long conTypeId;
	private int start;
	private int limit;
	private ConJContractInfo con;
	private ConJFileOpinion file;
	public ConArchiveAction()
	{
		remote=(ConJContractInfoFacadeRemote) factory.getFacadeRemote("ConJContractInfoFacade");
		fileremote=(ConJFileOpinionFacadeRemote) factory.getFacadeRemote("ConJFileOpinionFacade");
		modifyremote=(ConJModifyFacadeRemote) factory.getFacadeRemote("ConJModifyFacade");
	}
	//查询合同归档申请列表
	public void getArchiveList() throws JSONException{
		//add bjxu
		Long conTypeId = Long.parseLong(request.getParameter("conTypeId"));
		
		int start=0;
		int limit=99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			 start = Integer.parseInt(request.getParameter("start"));
			 limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject obj=remote.findContractArchiveList(conTypeId,startdate, enddate, employee.getEnterpriseCode(), fuzzy, start,limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}
	//查询合同归档确认列表
	public void getArchiveConfirmList() throws JSONException{
		String conNo= request.getParameter("conNo");
		String conName= request.getParameter("conName");
		String clientName= request.getParameter("clientName");
		String status="confirm";
		//String conTypeId = request.getParameter("conTypeId");
		PageObject obj=remote.findArchiveList(startdate, enddate, employee.getEnterpriseCode(),status,conNo,conName,clientName,null,conTypeId,start,limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}
	//查询合同已归档列表
	public void queryArchiveOkList() throws JSONException{
		String conNo= request.getParameter("conNo");
		String conName= request.getParameter("conName");
		String clientName= request.getParameter("clientName");
		String fileNo=request.getParameter("fileNo");
		String status="file";
		//String conTypeId = request.getParameter("conTypeId");
		PageObject obj=remote.findArchiveList(startdate, enddate, employee.getEnterpriseCode(),status,conNo,conName,clientName,fileNo,conTypeId,start,limit);
		String str = "{root:"+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}
	//合同归档申请
	public void contractArchive() throws CodeRepeatException
	{
		String typeChooseId= request.getParameter("typeChooseId");
		if(typeChooseId!=null && typeChooseId.equals("2")){
			ConJModify model_1 = modifyremote.findById(conId);
			model_1.setFileStatus("PRE");
			modifyremote.update(model_1);
		} else{
		ConJContractInfo model=remote.findById(conId);
		model.setFileStatus("PRE");
		remote.update(model, null,null);
		}
		write("{success:true,data:'操作成功!'}");
	}
	//查询变更合同的原合同归档信息
	public void getConModel() throws JSONException
	{
		ConJContractInfo conmodel=remote.findById(conId);
		write("{success:true,data:"+JSONUtil.serialize(conmodel)+"}");
	}
	//合同归档确认
	public void conArchiveConfirm() throws CodeRepeatException
	{
		String modifyId=request.getParameter("modifyId");
		if((modifyId != null) && !("".equals(modifyId)) && con.getConttreesNo().length()>19 )
		{
			ConJModify fmodel=modifyremote.findById(Long.parseLong(modifyId));
			fmodel.setFileNo(con.getFileNo());
			fmodel.setPageCount(con.getPageCount());
			fmodel.setFileCount(con.getFileCount());
			fmodel.setFileMemo(con.getFileMemo());
			fmodel.setFileBy(employee.getWorkerCode());
			fmodel.setFileDate(new Date());
			fmodel.setFileStatus("OK");
			modifyremote.update(fmodel);
		}
		else
		{
			ConJContractInfo model=remote.findById(con.getConId());
			model.setFileNo(con.getFileNo());
			model.setPageCount(con.getPageCount());
			model.setFileCount(con.getFileCount());
			model.setFileMemo(con.getFileMemo());
			model.setFileBy(employee.getWorkerCode());
			model.setFileDate(new Date());
			model.setFileStatus("OK");
			remote.update(model, null,null);
		}
		write("{success:true,data:'操作成功！'}");
	}
	//合同归档退回
	public void conArchiveBack() throws CodeRepeatException
	{
		String modifyId=request.getParameter("modifyId");
		if((modifyId != null) && !("".equals(modifyId)))
		{
			file.setFileType("CMOD");
		}
		else
		{
			file.setFileType("CON");
		}
		file.setGdWorker(employee.getWorkerCode());
		file.setEnterpriseCode(employee.getEnterpriseCode());
		file.setWithdrawalTime(new Date());
		file.setIsUse("Y");
		fileremote.save(file);
		write("{success:true,data:'操作成功！'}");
	}
	//获取合同归档退回信息
	public void getFileOpinionList() throws JSONException{
		String type_temp=request.getParameter("type");
		String type ="";
		if(type_temp != null && !type_temp.equals("")){
			type = type_temp;
		} else {
			type="CON";
		}
//		String type="CON";
		List<ConFileOpinionForm> list=fileremote.findFileOList(conId, type, employee.getEnterpriseCode());
		write(JSONUtil.serialize(list));
	}
	public String getFuzzy() {
		return fuzzy;
	}
	public void setFuzzy(String fuzzy) {
		this.fuzzy = fuzzy;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	public ConJContractInfo getCon() {
		return con;
	}
	public void setCon(ConJContractInfo con) {
		this.con = con;
	}
	public ConJFileOpinion getFile() {
		return file;
	}
	public void setFile(ConJFileOpinion file) {
		this.file = file;
	}
	public Long getConTypeId() {
		return conTypeId;
	}
	public void setConTypeId(Long conTypeId) {
		this.conTypeId = conTypeId;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
}

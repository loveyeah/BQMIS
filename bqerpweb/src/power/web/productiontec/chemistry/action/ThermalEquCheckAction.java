package power.web.productiontec.chemistry.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.chemistry.PtHxjdJRlsbjcqk;
import power.ejb.productiontec.chemistry.PtHxjdJRlsbjcqkDetail;
import power.ejb.productiontec.chemistry.PtHxjdJRlsbjcqkDetailFacadeRemote;
import power.ejb.productiontec.chemistry.PtHxjdJRlsbjcqkFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ThermalEquCheckAction extends AbstractAction{

	private PtHxjdJRlsbjcqk equ;
	private PtHxjdJRlsbjcqkFacadeRemote remote;
	private PtHxjdJRlsbjcqkDetailFacadeRemote detailRemote;
	
	public ThermalEquCheckAction()
	{
		remote = (PtHxjdJRlsbjcqkFacadeRemote)factory.getFacadeRemote("PtHxjdJRlsbjcqkFacade");
		detailRemote = (PtHxjdJRlsbjcqkDetailFacadeRemote)factory.getFacadeRemote("PtHxjdJRlsbjcqkDetailFacade");
	}

	/**
	 * 增加一条热力设备检查情况记录
	 */
	public void addThermalEquCheck()
	{
		equ.setFillBy(employee.getWorkerCode());
		equ.setEnterpriseCode(employee.getEnterpriseCode());
		equ = remote.save(equ);
		write("{success : true,msg :'增加成功！'}");
	}
	
	/**
	 * 修改一条热力设备检查情况记录
	 */
	public void updateThermalEquCheck()
	{
		PtHxjdJRlsbjcqk model = remote.findById(equ.getRlsbjcId());
		model.setDeviceCode(equ.getDeviceCode());
		model.setTestType(equ.getTestType());
		model.setStartDate(equ.getStartDate());
		model.setEndDate(equ.getEndDate());
		model.setExamineBy(equ.getExamineBy());
		model.setChargeBy(equ.getChargeBy());
		model.setContent(equ.getContent());
		model.setFillBy(employee.getWorkerCode());
		model.setDepCode(employee.getDeptCode());
		model.setFillDate(new Date());
		
		remote.update(model);
		write("{success : true,msg :'修改成功！'}");
	}
	
	/**
	 * 删除一条或多条热力设备检查情况记录
	 */
	public void deleteThermalEquCheck()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 查找热力设备检查情况记录列表
	 * @throws JSONException 
	 */
	public void findThermalEquCheckList() throws JSONException
	{
		String deviceCode = request.getParameter("deviceCode");
		String sDate = request.getParameter("sDate");
		String eDate =  request.getParameter("eDate");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) 
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findThermalEquCheckList(employee.getEnterpriseCode(), deviceCode, sDate, eDate, start,limit);			
		}
		else {
			obj = remote.findThermalEquCheckList(employee.getEnterpriseCode(), deviceCode, sDate, eDate);
		}
		String strOutput = "";
		if (obj == null || obj.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(obj);
		}
		write(strOutput);
	}
	
	/**
	 * 增加，修改，删除热力设备检查情况明细记录
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveAndUpdateRecord() throws Exception
	{
		try{
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			
			List<PtHxjdJRlsbjcqkDetail> addList = new ArrayList<PtHxjdJRlsbjcqkDetail>();
			List<PtHxjdJRlsbjcqkDetail> updateList = new ArrayList<PtHxjdJRlsbjcqkDetail>();
			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
				PtHxjdJRlsbjcqkDetail model = new PtHxjdJRlsbjcqkDetail();
				model.setEnterpriseCode(employee.getEnterpriseCode());
				if (data.get("rlsbjcDetailId") != null && !"".equals(data.get("rlsbjcDetailId"))) {
					model.setRlsbjcDetailId(Long.parseLong(data.get("rlsbjcDetailId").toString()));
				}
				if(data.get("rlsbjcId") != null && !"".equals(data.get("rlsbjcId")))
				{
					model.setRlsbjcId(Long.parseLong(data.get("rlsbjcId").toString()));
				}
				if(data.get("equCode") != null && !"".equals(data.get("equCode")))
				{
					model.setEquCode(data.get("equCode").toString());
				}
				if(data.get("repairDate") != null && !"".equals(data.get("repairDate")))
				{
					model.setRepairDate(sdf.parse(data.get("repairDate").toString()));
				}
				if(data.get("courseNumber") != null && !"".equals(data.get("courseNumber")))
				{
					model.setCourseNumber(Double.parseDouble(data.get("courseNumber").toString()));
				}
				if(data.get("repairType") != null && !"".equals(data.get("repairType")))
				{
					model.setRepairType(data.get("repairType").toString());
				}
				if(data.get("repairNumber") != null && !"".equals(data.get("repairNumber")))
				{
					model.setRepairNumber(Long.parseLong(data.get("repairNumber").toString()));
				}
				if(data.get("checkHigh") != null && !"".equals(data.get("checkHigh")))
				{
					model.setCheckHigh(data.get("checkHigh").toString());
				}
				if(data.get("checkName") != null && !"".equals(data.get("checkName")))
				{
					model.setCheckName(data.get("checkName").toString());
				}
				if(data.get("checkPart") != null && !"".equals(data.get("checkPart")))
				{
					model.setCheckPart(data.get("checkPart").toString());
				}
				if(data.get("dirtyCapacity") != null && !"".equals(data.get("dirtyCapacity")))
				{
					model.setDirtyCapacity(data.get("dirtyCapacity").toString());
				}
				if(data.get("sedimentQuantity") !=null && !"".equals(data.get("sedimentQuantity")))
				{
					model.setSedimentQuantity(data.get("sedimentQuantity").toString());
				}
				if(data.get("memo") != null && !"".equals(data.get("memo")))
				{
					model.setMemo(data.get("memo").toString());
				}
				
				if((model.getRlsbjcDetailId() == null || "".equals(model.getRlsbjcDetailId())))
				{
					addList.add(model);
				}else{
					updateList.add(model);
				}
			}
			if(addList.size() > 0)
			{
				detailRemote.save(addList);
			}
			if(updateList.size() > 0)
			{
				detailRemote.save(updateList);
			}
			if (deleteIds != null && !deleteIds.trim().equals(""))
			{
				detailRemote.delete(deleteIds);
			}
			write("{success: true,msg:'保存成功！'}");
			
		}catch (Exception exc) {
			write("{success: false,msg:'操作失败'}");
			throw exc;
		}
	}
	
	/**
	 * 根据热力设备检查主表ID查询热力设备检查情况明细记录
	 * @throws JSONException
	 */
	public void findEquCheckDetailList() throws JSONException
	{
		String rlsbjcId = request.getParameter("rlsbjcId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) 
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = detailRemote.findEquCheckDetailList(rlsbjcId, employee.getEnterpriseCode(), start,limit);
		}
		else {
			obj = detailRemote.findEquCheckDetailList(rlsbjcId, employee.getEnterpriseCode());
		}
		String strOutput = "";
		if (obj == null || obj.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(obj);
		}
		write(strOutput);
	}
	
	public PtHxjdJRlsbjcqk getEqu() {
		return equ;
	}

	public void setEqu(PtHxjdJRlsbjcqk equ) {
		this.equ = equ;
	}
}

package power.web.productiontec.chemistry.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.chemistry.PtHxjdCHxzxybwh;
import power.ejb.productiontec.chemistry.PtHxjdCHxzxybwhFacadeRemote;
import power.ejb.productiontec.chemistry.PtHxjdJZxybybzb;
import power.ejb.productiontec.chemistry.PtHxjdJZxybybzbFacadeRemote;
import power.ejb.productiontec.chemistry.form.ChemistryReportForm;
import power.ejb.productiontec.insulation.PtJyjdJYqybtzlh;
import power.ejb.productiontec.insulation.PtJyjdJYqybtzlhFacadeRemote;
import power.ejb.run.securityproduction.SpJSpecialoperators;
import power.ejb.run.securityproduction.SpJSpecialoperatorsFacadeRemote;
import power.ejb.workticket.business.RunJWorkticketSafety;
import power.ejb.workticket.business.RunJWorkticketSafetyFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 化学在线仪器名称维护
 * 
 * @author liuyi 090708
 * 
 */
public class ChemistryAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 146465464L;
	private PtHxjdCHxzxybwh phc;
	private PtHxjdCHxzxybwhFacadeRemote remote;
	private PtHxjdJZxybybzb phj;
	private PtHxjdJZxybybzbFacadeRemote rem;

	public PtHxjdCHxzxybwh getPhc() {
		return phc;
	}

	public void setPhc(PtHxjdCHxzxybwh phc) {
		this.phc = phc;
	}

	public ChemistryAction() {
		remote = (PtHxjdCHxzxybwhFacadeRemote) factory
				.getFacadeRemote("PtHxjdCHxzxybwhFacade");
		rem = (PtHxjdJZxybybzbFacadeRemote) factory
		.getFacadeRemote("PtHxjdJZxybybzbFacade");
	}

	/**
	 * 增加一条化学在线仪表信息
	 */
	public void addPtHxjdCHxzxybwh() {
		 phc.setEnterpriseCode(employee.getEnterpriseCode());
		phc = remote.save(phc);
		if (phc == null) {
			write("{failure:true,msg:'该化学在线仪表名称已存在！'}");
		} else
			write("{success:true,id:'" + phc.getMeterId() + "',msg:'数据增加成功！'}");
	}

	/**
	 * 修改一条化学在线仪表信息
	 */
	public void updatePtHxjdCHxzxybwh() {
		PtHxjdCHxzxybwh temp = remote.findById(phc.getMeterId());
		temp.setMeterName(phc.getMeterName());
		temp.setEnterpriseCode(employee.getEnterpriseCode());

		phc = remote.update(temp);
		if (phc == null) {
			write("{failure:true,msg:'该化学在线仪表名称已存在！'}");
		} else
			write("{success:true,msg:'数据修改成功！'}");
	}

	/**
	 * 删除一条或多条化学在线仪表信息
	 */
	public void deletePtHxjdCHxzxybwh() {
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'数据删除成功！'}");
	}

	/**
	 * 根据姓名和企业编码查询绝缘仪器仪表信息列表
	 * 
	 * @throws JSONException
	 */
	public void findPtHxjdCHxzxybwhList() throws JSONException {
		PageObject obj = new PageObject();

		String name = request.getParameter("name");

		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findAll(name, employee.getEnterpriseCode(), start,
					limit);
		} else {
			obj = remote.findAll(name, employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 根据机组名、月份和企业编码查询化学在线仪表月报列表
	 * 
	 * @throws JSONException
	 */
	public void findPtHxjdJZxybybzbList() throws JSONException {
		PageObject obj = new PageObject();

		String name = request.getParameter("name");
		String month = request.getParameter("month");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = rem.findAll(name,month, employee.getEnterpriseCode(), start,
					limit);
		} else {
			obj = rem.findAll(name,month, employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}
	

	/**
	 * 根据化学在线仪表月报主表的id和企业编码查询化学在线仪表月报主表、明细表和化学在线仪器维护表
	 * 
	 * @throws JSONException
	 */
	public void findChemistryDetailsList() throws JSONException {
		PageObject obj = new PageObject();

		String zxybybzbId = request.getParameter("zxybybzbId");
		if(zxybybzbId == null)
			zxybybzbId = "";
		System.out.println(zxybybzbId);
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = rem.findDetailsAll(zxybybzbId, employee.getEnterpriseCode(), start,
					limit);
		} else {
			obj = rem.findDetailsAll(zxybybzbId, employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		System.out.println(str+" ddddddd");
		write(str);
	}
	
	
	/**
	 * 删除化学在线仪器月报信息
	 * @return
	 */
	public void deletePtHxjdJZxybybzb() {
		String ids = request.getParameter("ids");
		rem.deleteMulti(ids);
		write("{success:true,msg:'数据删除成功！'}");
	}
	public PtHxjdJZxybybzb getPhj() {
		return phj;
	}

	public void setPhj(PtHxjdJZxybybzb phj) {
		this.phj = phj;
	}
	
	
	/**
	 * 批量安措修改
	 */
	public void modifyChemistryReportDetails()
	{
		try {
			PtHxjdJZxybybzbFacadeRemote chemRemote;
		    chemRemote=(PtHxjdJZxybybzbFacadeRemote)factory.getFacadeRemote("PtHxjdJZxybybzbFacade");
		    SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM");
			String addOrUpdateRecords = request.getParameter("addOrUpdateRecords"); 
			String zxybybzbId = request.getParameter("zxybybzbId");
			String month = request.getParameter("month");
			PtHxjdJZxybybzb tempPhj = phj;
			tempPhj.setEnterpriseCode(employee.getEnterpriseCode());
			if(month != null && !(month.equals("")))
				tempPhj.setReportTime(sbf.parse(month));
			if (addOrUpdateRecords != null) {
				List<ChemistryReportForm> list = parseContent(addOrUpdateRecords,tempPhj);
				chemRemote.modifyRecords(list, zxybybzbId);
				write("{success:true}");
			} 
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success:false}");
		}
	}
	@SuppressWarnings("unchecked")
	private List<ChemistryReportForm> parseContent(String records,PtHxjdJZxybybzb tempPhj) throws JSONException
	{
		List<ChemistryReportForm> result = new ArrayList<ChemistryReportForm>();
		Object object = JSONUtil.deserialize(records);
		List list=(List)object;
		int intLen = list.size();
		for(int i=0;i<intLen;i++){
			Map map=(Map)list.get(i);
			ChemistryReportForm m = this.parseContentModal(map, tempPhj);
			result.add(m);
		}
		return result;
	}
	@SuppressWarnings("unchecked")
	private ChemistryReportForm parseContentModal(Map map,PtHxjdJZxybybzb tempPhj)
	{
		ChemistryReportForm model=new ChemistryReportForm();
		PtHxjdJZxybybzb flagPhj = new PtHxjdJZxybybzb();
		SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM");
		
		Object zxybybzbId = map.get("phj.zxybybzbId");
		if(zxybybzbId != null && !(zxybybzbId.equals("")))
			flagPhj.setZxybybzbId(Long.parseLong(zxybybzbId.toString()));
		Object deviceCode = map.get("phj.deviceCode");
		if(deviceCode != null && !(deviceCode.equals("")))
			flagPhj.setDeviceCode(deviceCode.toString());
		
		
		Object memo = map.get("phj.memo");
		if(memo != null && !(memo.equals("")))
			flagPhj.setMemo(memo.toString());
		Object fillBy = map.get("phj.fillBy");
		if(fillBy != null && !(fillBy.equals("")))
			flagPhj.setFillBy(fillBy.toString());
		Object workFlowNo = map.get("phj.workFlowNo");
		if(workFlowNo != null && !(workFlowNo.equals("")))
			flagPhj.setWorkFlowNo(Long.parseLong(workFlowNo.toString()));
		Object workFlowStatus = map.get("phj.workFlowStatus");
		if(workFlowStatus != null && !(workFlowStatus.equals("")))
			flagPhj.setWorkFlowStatus(Long.parseLong(workFlowStatus.toString()));
		Object enterpriseCode = map.get("phj.enterpriseCode");
		if(enterpriseCode != null && !(enterpriseCode.equals("")))
			flagPhj.setEnterpriseCode(enterpriseCode.toString());
				
		Object deviceName = map.get("deviceName");
		Object month = map.get("month");
		Object fillName = map.get("fillName");
		Object fillDate = map.get("fillDate");
		Object zxybybId = map.get("zxybybId");
		Object meterId = map.get("meterId");
		Object mustThrowNum = map.get("mustThrowNum");
		Object equipNum = map.get("equipNum");
		Object throwNum = map.get("throwNum");
		Object equipRate = map.get("equipRate");
		Object throwRate = map.get("throwRate");
		Object searchRate = map.get("searchRate");
		Object meterName = map.get("meterName");

//		if(flagPhj.getZxybybzbId() != null && !(flagPhj.getZxybybzbId().toString().equals("")) )
//		{
////			model.setPhj(flagPhj);
//			model.setPhj(tempPhj);
//		}
//		else
//		{
			model.setPhj(tempPhj);
//		}
		if(deviceName != null)
			model.setDeviceName(deviceName.toString());
		if(month != null)
		{
			try {
				flagPhj.setReportTime((sbf.parse(month.toString())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			model.setMonth(month.toString());
		}
			
		if(fillName != null)
			model.setFillName(fillName.toString());
		if(fillDate != null)
		{
			try {
				flagPhj.setFillDate(sbf.parse(fillDate.toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			model.setFillDate(fillDate.toString());
		}		
		if(zxybybId != null)
			model.setZxybybId(zxybybId.toString());
		if(meterId != null)
			model.setMeterId(meterId.toString());
		if(mustThrowNum != null)
			model.setMustThrowNum(mustThrowNum.toString());
		if(equipNum != null)
			model.setEquipNum(equipNum.toString());
		if(throwNum != null)
			model.setThrowNum(throwNum.toString());
		if(equipRate != null)
			model.setEquipRate(equipRate.toString());
		if(throwRate != null )
			model.setThrowRate(throwRate.toString());
		if(searchRate != null)
			model.setSearchRate(searchRate.toString());
		if(meterName != null)
			model.setMeterName(meterName.toString());
		return model;
	}

}

package power.web.equ.workbill.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.workbill.EquJManplan;
import power.ejb.equ.workbill.EquJManplanFacadeRemote;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.shift.RunCUnitprofession;
import power.ejb.run.runlog.shift.RunCUnitprofessionFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class EquJManpowerAction extends AbstractAction{
	private EquJManplanFacadeRemote remote;
	protected RunCUnitprofessionFacadeRemote dll;
	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public EquJManpowerAction() {
		remote = (EquJManplanFacadeRemote) factory
				.getFacadeRemote("EquJManplanFacade");
		dll = (RunCUnitprofessionFacadeRemote) factory
				.getFacadeRemote("RunCUnitprofessionFacade");
	}

	/**
	 * 取工种专业
	 * 
	 */

	public void useprolist() throws Exception {
		List<RunCSpecials> list = dll.findUnitPList(employee
				.getEnterpriseCode());
		String str = "[";
		int i = 0;
		for (RunCSpecials model : list) {
			i++;
			str += "[\"" + model.getSpecialityName() + "\",\""
					+ model.getSpecialityCode() + "\"]";
			if (i < list.size()) {
				str += ",";
			}
		}
		str += "]";
		write(str);
	}

	/**
	 * 增加新记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveEquJManplan() {

		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			String woCode = request.getParameter("woCode");
			Object obj = JSONUtil.deserialize(str);

			List<EquJManplan> addList = new ArrayList<EquJManplan>();
			List<EquJManplan> updateList = new ArrayList<EquJManplan>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
				
				
				String operationStep = null;
				String planLaborCode = null;
				String planLaborQty = null;
				String planLaborHrs = null;
				String planFee = null;
				String id = null;
				String factLaborCode = null;
				String factLaborQty = null;
				String factLaborHrs = null;
				String factFee = null;
				
						
	
				if(data.get("id")!=null){
					id = data.get("id").toString();
				}
			
				if(data.get("operationStep")!=null){
					operationStep = data.get("operationStep").toString();
				}
				if(data.get("planLaborCode")!=null){
					planLaborCode  = data.get("planLaborCode").toString();
				}
			
				if(data.get("planLaborQty")!=null){
					planLaborQty = data.get("planLaborQty").toString();
				}
				if(data.get("planLaborHrs")!=null){
					planLaborHrs = data.get("planLaborHrs").toString();
					}
				if(data.get("planFee")!=null){
					planFee = data.get("planFee").toString();
					}
				
				if(data.get("factLaborCode")!=null){
					factLaborCode  = data.get("factLaborCode").toString();
				}
			
				if(data.get("factLaborQty")!=null){
					factLaborQty = data.get("factLaborQty").toString();
				}
				if(data.get("factLaborHrs")!=null){
					factLaborHrs = data.get("factLaborHrs").toString();
					}
				if(data.get("factFee")!=null){
					factFee = data.get("factFee").toString();
					}



				EquJManplan model = new EquJManplan();
				// 增加
				if (id==null) {
					model.setWoCode(woCode);
					model.setOperationStep(operationStep);
					if (planLaborQty!=null&&!planLaborQty.equals("")) {
						model.setPlanLaborQty(Long.parseLong(planLaborQty));
					}
					if (planLaborHrs!=null&&!planLaborHrs.equals("")) {
						// modified by liuyi 20100519
//						model.setPlanLaborHrs(Long.parseLong(planLaborHrs));
						model.setPlanLaborHrs(Double.parseDouble(planLaborHrs));
					}
					model.setPlanLaborCode(planLaborCode);
					
					
					if (planFee!=null&&!planFee.equals("")) {
						model.setPlanFee(Double.parseDouble(planFee));
					}
				
				
					model.setEnterprisecode(employee.getEnterpriseCode());

					addList.add(model);
				} else {
					model = remote.findById(Long.parseLong(id));
					
				
					if (planLaborQty!=null&&!planLaborQty.equals("")) {
						model.setPlanLaborQty(Long.parseLong(planLaborQty));
					}
					if (planLaborHrs!=null&&!planLaborHrs.equals("")) {
						// modified by liuyi 20100519
//						model.setPlanLaborHrs(Long.parseLong(planLaborHrs));
						model.setPlanLaborHrs(Double.parseDouble(planLaborHrs));
					}
					model.setPlanLaborCode(planLaborCode);
					
					if (planFee!=null&&!planFee.equals("")) {
						model.setPlanFee(Double.parseDouble(planFee));
					}
					if (factLaborQty!=null&&!factLaborQty.equals("")) {
						model.setFactLaborQty(Long.parseLong(factLaborQty));
					}
					if (factLaborHrs!=null&&!factLaborHrs.equals("")) {
						model.setFactLaborHrs(Long.parseLong(factLaborHrs));
					}
					model.setFactLaborCode(factLaborCode);			
					
					if (factFee!=null&&!factFee.equals("")) {
						model.setFactFee(Double.parseDouble(factFee));
					}
				
					
					
					updateList.add(model);
				}
			}

		
			if ((addList.size() > 0 || updateList.size() > 0)
					|| (deleteIds != null && !deleteIds.trim().equals(""))) {
				remote.save(addList, updateList, deleteIds);
			}
			

			write("{success: true,msg:'保存成功！'}");
			}catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}

	// /**
	// * 修改记录
	// *
	// */
	// public void updateEquCStandardManplan() {
	// try {
	// EquCStandardManplan model = new EquCStandardManplan();
	// model = remote.findById(baseInfo.getId());
	// baseInfo.setEnterprisecode(model.getEnterprisecode());
	// baseInfo.setIfUse(model.getIfUse());
	// baseInfo.setOperationStep(model.getOperationStep());
	// if (baseInfo.getOrderby() == null)
	// baseInfo.setOrderby(model.getOrderby());
	// baseInfo.setWoCode(model.getWoCode());
	// remote.update(baseInfo);
	// write("{success:true,msg:'更新成功'}");
	// } catch (Exception e) {
	// write("{success:false,msg:'更新失败'}");
	// }
	// }
	//
	// /**
	// * 删除记录
	// *
	// */
	// public void deleteEquCStandardManplan() {
	// try {
	// if (remote.delete(ids))
	// write("{success:true,msg:'删除成功'}");
	// else
	// write("{success:false,msg:'删除失败'}");
	// } catch (Exception e) {
	// write("{success:false,msg:'删除失败'}");
	// }
	// }

	/**
	 * 取列表
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getEquJManplan() throws JSONException {
		String woCode = request.getParameter("woCode");
		String opCode = request.getParameter("opCode");
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), woCode,
				opCode, start, limit);
		write(JSONUtil.serialize(obj));
	}
	
	public void getEquCManplan() throws JSONException {
		String woCode = request.getParameter("woCode");
		String opCode = request.getParameter("opCode");
		PageObject obj = remote.getEquCManplan(employee.getEnterpriseCode(), woCode,
				opCode, start, limit);
		write(JSONUtil.serialize(obj));
	}
    
	
	// /**
	// * @return the baseInfo
	// */
	// public EquCStandardManplan getBaseInfo() {
	// return baseInfo;
	// }
	//
	// /**
	// * @param baseInfo
	// * the baseInfo to set
	// */
	// public void setBaseInfo(EquCStandardManplan baseInfo) {
	// this.baseInfo = baseInfo;
	// }
	//
	// /**
	// * @return the ids
	// */
	// public String getIds() {
	// return ids;
	// }
	//
	// /**
	// * @param ids
	// * the ids to set
	// */
	// public void setIds(String ids) {
	// this.ids = ids;
	// }

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	

}

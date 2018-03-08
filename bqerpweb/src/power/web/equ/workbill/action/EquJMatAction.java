package power.web.equ.workbill.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;

import power.ejb.equ.workbill.EquJMainmat;
import power.ejb.equ.workbill.EquJMainmatFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class EquJMatAction extends AbstractAction{
	private EquJMainmatFacadeRemote remote;

	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public EquJMatAction() {
		remote = (EquJMainmatFacadeRemote) factory
				.getFacadeRemote("EquJMainmatFacade");
	}

	/**
	 * 增加新记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveEquJMainmat() {

		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			String woCode = request.getParameter("woCode");
			
			Object obj = JSONUtil.deserialize(str);

			List<EquJMainmat> addList = new ArrayList<EquJMainmat>();
			List<EquJMainmat> updateList = new ArrayList<EquJMainmat>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
		
				
				String operationStep =null;
				String planMaterialCode =null;
				String planLocationId =null;
				String planItemQty =null;
				String planUnit =null;
				String planMaterialPrice =null;
				
				String factMaterialCode =null;
				String factLocationId =null;
				String factItemQty =null;
				String factUnit =null;
				String factMaterialPrice =null;
			
				String id=null;
//				String orderby=null;
				
//				String woCode=data.get("baseInfo.woCode").toString();
				
	
		
			
				if(data.get("baseInfo.operationStep")!=null){
					operationStep = data.get("baseInfo.operationStep").toString();
				}
				if(data.get("baseInfo.planMaterialCode")!=null){
					planMaterialCode  = data.get("baseInfo.planMaterialCode").toString();
				}
			
				if(data.get("baseInfo.planLocationId")!=null){
					planLocationId = data.get("baseInfo.planLocationId").toString();
				}
				if(data.get("baseInfo.planItemQty")!=null){
					planItemQty = data.get("baseInfo.planItemQty").toString();
					}
				if(data.get("baseInfo.planUnit")!=null){
					planUnit = data.get("baseInfo.planUnit").toString();
					}

				if(data.get("baseInfo.planMaterialPrice")!=null){
					planMaterialPrice = data.get("baseInfo.planMaterialPrice").toString();
					}
				if(data.get("baseInfo.factMaterialCode")!=null){
					factMaterialCode  = data.get("baseInfo.factMaterialCode").toString();
				}
			
				if(data.get("baseInfo.factLocationId")!=null){
					factLocationId = data.get("baseInfo.factLocationId").toString();
				}
				if(data.get("baseInfo.factItemQty")!=null){
					factItemQty = data.get("baseInfo.factItemQty").toString();
					}
				if(data.get("baseInfo.factUnit")!=null){
					factUnit = data.get("baseInfo.factUnit").toString();
					}

				if(data.get("baseInfo.factMaterialPrice")!=null){
					factMaterialPrice = data.get("baseInfo.factMaterialPrice").toString();
					}
				
				if(data.get("baseInfo.id")!=null){
					id = data.get("baseInfo.id").toString();
					}

				EquJMainmat model = new EquJMainmat();
				// 增加
				if (id==null) {
					model.setWoCode(woCode);
					model.setOperationStep(operationStep);
					if (planItemQty!=null&&!planItemQty.equals("")) {
						model.setPlanItemQty(Double.parseDouble(planItemQty));
					}
					model.setPlanMaterialCode(planMaterialCode);
					model.setPlanLocationId(planLocationId);
					
					if (planMaterialPrice!=null&&!planMaterialPrice.equals("")) {
						model.setPlanMaterialPrice(Double.parseDouble(planMaterialPrice));
					}
					
					model.setPlanUnit(planUnit);
					model.setEnterprisecode(employee.getEnterpriseCode());

					addList.add(model);
				} 
				//修改
				else {
					model = remote.findById(Long.parseLong(id));
					
				
					if (planItemQty!=null&&!planItemQty.equals("")) {
						model.setPlanItemQty(Double.parseDouble(planItemQty));
					}
					if (planMaterialCode!=null&&!planMaterialCode.equals(""))
					model.setPlanMaterialCode(planMaterialCode);
					
					if (planLocationId!=null&&!planLocationId.equals(""))
					model.setPlanLocationId(planLocationId);
					
					if (planMaterialPrice!=null&&!planMaterialPrice.equals("")) {
						model.setPlanMaterialPrice(Double.parseDouble(planMaterialPrice));
					}
					if (planUnit!=null&&!planUnit.equals(""))
					model.setPlanUnit(planUnit);
					
					if (factItemQty!=null&&!factItemQty.equals("")) {
						model.setFactItemQty(Double.parseDouble(factItemQty));
					}
					if (factMaterialCode!=null&&!factMaterialCode.equals(""))
					model.setFactMaterialCode(factMaterialCode);
					
					if (factLocationId!=null&&!factLocationId.equals(""))
					model.setFactLocationId(factLocationId);
					
					if (factMaterialPrice!=null&&!factMaterialPrice.equals("")) {
						model.setFactMaterialPrice(Double.parseDouble(factMaterialPrice));
					}
					if (factUnit!=null&&!factUnit.equals(""))
					model.setFactUnit(factUnit);
					
					updateList.add(model);
				}
			}

			
			if ((addList.size() > 0 || updateList.size() > 0)
					|| (deleteIds != null && !deleteIds.trim().equals(""))) {
				remote.save(addList, updateList, deleteIds);
			}
			
				

			

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}	
		
	}



	/**
	 * 取列表
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getEquJMainmat() throws JSONException {
		String woCode = request.getParameter("woCode");
		String opCode = request.getParameter("opCode");
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), woCode,
				opCode, start, limit);
		write(JSONUtil.serialize(obj));
	}
	public void getEquCMainmat() throws JSONException {
		String woCode = request.getParameter("woCode");
		String opCode = request.getParameter("opCode");
		PageObject obj = remote.getEquCMainmat(employee.getEnterpriseCode(), woCode,
				opCode, start, limit);
		write(JSONUtil.serialize(obj));
	}


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

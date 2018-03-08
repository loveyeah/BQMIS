package power.web.manage.plan.trainplan;


import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;

import power.ejb.manage.plan.trainplan.BpCTrainingType;
import power.ejb.manage.plan.trainplan.TrainPlanManager;
import power.ejb.manage.plan.trainplan.TrainPlanManagerImpl;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class TrainPlanAction extends AbstractAction{
	 private  TrainPlanManager  remoteA ;
	 private  BpCTrainingType  type ;
	 private  String passmethod;
	 private  String ids;
	 
	 
	 public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getPassmethod() {
		return passmethod;
	}
	public void setPassmethod(String passmethod) {
		this.passmethod = passmethod;
	}
	public TrainPlanAction(){
		remoteA = (TrainPlanManager) factory
		.getFacadeRemote("TrainPlanManagerImpl");
	 }
	/**
	 * 增加
	 */
	public void addTrainPlanType()throws Exception{
		
		boolean existFlag=false;
		existFlag=remoteA.checkinput(type);
		if(existFlag==true){
			write("{success:true,existFlag:"+existFlag+"}");
		}else{
		if("add".equals(passmethod))
		{
//			System.out.println(type.getTrainingTypeName());
			type.setEnterpriseCode(employee.getEnterpriseCode());  
			type.setIsUse("Y");
			boolean 
			flag = remoteA.addTrainPlanType(type);
			if(flag==true){
			 write("{success:true}");
			}
	   }
		
			
		
		else { if ("update".equals(passmethod)){
//			System.out.println("in update");
			boolean flag=false;
			type.setIsUse("Y");
			flag= remoteA.updateTrainPlanType(type.getTrainingTypeId(),type);
			
			write("{success:true}");}
		else  write("success:false");
		
		}
		}
	}
			 
	

	/**
	 * 删除
	 */
	public void delTrainPlanType() throws Exception{
		//System.out.println("in delTrainPlanType");
		boolean deleteFlag=false;
		deleteFlag=remoteA.delTrainPlanType(ids);
		if(deleteFlag==true)
		  write("{success:true}");
		
		
	} 
	/**
	 * 取得
	 * @throws JSONException 
	 */
	public void getTrainPlanList() throws JSONException{
		System.out.println(employee.getEnterpriseCode());
		System.out.println(remoteA);
	
			
		// 取得查询条件: 开始行
			Object objstart = request.getParameter("start");
			// 取得查询条件：结束行
			Object objlimit = request.getParameter("limit");
			PageObject object = null;
			// 判断是否为null
			if (objstart != null && objlimit != null) {
				int start = Integer.parseInt(request.getParameter("start")); 
				int limit = Integer.parseInt(request.getParameter("limit"));
				// 查询
				object = remoteA.getTrainPlanList( employee.getEnterpriseCode(),start, limit);
			} else {
				// 查询
				object =remoteA.getTrainPlanList(employee.getEnterpriseCode() );
			}
			// 输出结果
			String strOutput = "";
			// 查询结果为null
			if (object == null || object.getList() == null) {
				strOutput = "{\"list\":[],\"totalCount\":0}";  
				// 不为null
			} else {
				strOutput = "{\"list\":"+JSONUtil.serialize(object.getList())+",\"totalCount\":"+JSONUtil.serialize(object.getTotalCount())+"}";
				//strOutput = JSONUtil.serialize(object);
			}
			System.out.println(strOutput);
			write(strOutput);
		}
	
		
		
		
		
	
	public BpCTrainingType getType() {
		return type;
	}
	public void setType(BpCTrainingType type) {
		this.type = type;
	}



	
	
}

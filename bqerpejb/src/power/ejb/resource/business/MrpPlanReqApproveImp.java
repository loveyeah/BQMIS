package power.ejb.resource.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.basedata.BaseDataManager;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.resource.MrpJPlanRequirementHead;
import power.ejb.resource.MrpJPlanRequirementHeadFacadeRemote;
import power.ejb.resource.MrpJPlanRequirementHis;
import power.ejb.resource.MrpJPlanRequirementHisFacadeRemote;
import power.ejb.resource.form.PlanApproveStatus;

@Stateless
public class MrpPlanReqApproveImp implements MrpPlanReqApprove{
	@SuppressWarnings("unused")
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	private MrpJPlanRequirementHeadFacadeRemote mrpPlanHeadRemote;
	private MrpJPlanRequirementHisFacadeRemote hisRemote;
	private BaseDataManager commRemote;
	WorkflowService service;
	
	
	public MrpPlanReqApproveImp()
	{
		mrpPlanHeadRemote=(MrpJPlanRequirementHeadFacadeRemote)Ejb3Factory.getInstance()
		.getFacadeRemote("MrpJPlanRequirementHeadFacade");
		hisRemote=(MrpJPlanRequirementHisFacadeRemote)Ejb3Factory.getInstance()
		.getFacadeRemote("MrpJPlanRequirementHisFacade");
		commRemote=(BaseDataManager)Ejb3Factory.getInstance()
		.getFacadeRemote("BaseDataManagerImpl");
		service = new WorkflowServiceImpl();
	}
	/**
	 * 查找所有状态（用于查询）
	 * @return
	 */
	public List<PlanApproveStatus> findAllPlanBusi()
	{
		return this.findNotInBusiStatus(null);
	}
	/**
	 * 查找审批页面所用的状态
	 * @return
	 */
	public List<PlanApproveStatus> findPlanBusiForApprove()
	{
		return this.findNotInBusiStatus("'0','2','9'"); //除去未上报，已退回和已结束的
	}
	

	
	//上报
	public void reportTo(String mrNo,String workflowType,String workerCode,Long actionId,String approveText,String nextRoles,String eventIdentify,String dateMemo)
	{
		List<MrpJPlanRequirementHead> list=mrpPlanHeadRemote.findByMrNo(mrNo);
		if(list!=null&&list.size()>0)
		{
			MrpJPlanRequirementHead model=list.get(0);
//			if(model.getWfNo()!=null)
//			{
//				//已退回的票
//				this.changeWfInfo(model.getWfNo(), workerCode, actionId, approveText, nextRoles);
//			}
//			else
//			{
//				//未上报的票
//				long entryId = service.doInitialize(workflowType,workerCode,mrNo );
//				service.doAction(entryId, workerCode, actionId,approveText, null,nextRoles); 
//				model.setWfNo(entryId);
//			}
			if(model.getWfNo()!=null)
			{
				BaseDataManager commRemote=(BaseDataManager)Ejb3Factory.getInstance()
				.getFacadeRemote("BaseDataManagerImpl");
				commRemote.deleteWf(model.getWfNo());
			}
			long entryId = service.doInitialize(workflowType,workerCode,mrNo );
			service.doAction(entryId, workerCode, actionId,approveText, null,nextRoles); 
			model.setWfNo(entryId);
			model.setMrStatus("1");
			model.setMrDate(new Date());
			model.setPlanDateMemo(dateMemo);
			
			mrpPlanHeadRemote.update(model);
//			String sql=
//				"update mrp_j_plan_requirement_detail t\n" +
//				"set t.approved_qty=t.applied_qty\n" + 
//				"where  t.is_use='Y'  and  t.requirement_head_id="+model.getRequirementHeadId();
//
//			bll.exeNativeSQL(sql);
		}
				
	}
	
	@SuppressWarnings("unchecked")
	public void planReqSign(String mrNo,String approveText,
			String workerCode,Long actionId,String responseDate,
			String nextRoles,String eventIdentify,String itemCode)
	{
		String busiStatus="";
		List<MrpJPlanRequirementHead> list=mrpPlanHeadRemote.findByMrNo(mrNo);
		if(list!=null&&list.size()>0)
		{
			MrpJPlanRequirementHead model=list.get(0);
			String planType="";
			boolean isNew=this.checkIsNew(model.getWfNo());
			if(model.getMrStatus().equals("3")&&isNew==false)
			{
				
				planType=commRemote.checkDeptType(model.getMrDept());
				if(model.getPlanOriginalId()==4||model.getPlanOriginalId()==10)
				{
					//#5生产类
					planType+="M";
				}
				else if(model.getPlanOriginalId()==5||model.getPlanOriginalId()==11)
				{
					//老厂生产类
					planType+="M";
				}
				else if(model.getPlanOriginalId()==6||model.getPlanOriginalId()==7)
				{
					//行政类
					planType=commRemote.checkDeptType(model.getMrDept())+"O";
				}
				else if(model.getPlanOriginalId()==3)
				{
					//固定资产
					planType+="GD";
				}
				else if(model.getPlanOriginalId()==15)
				{
					//劳保类 add by fyyang 20100203
					planType+="LB";
				}
				
				busiStatus=this.getBusiStatus(model.getMrStatus(), eventIdentify, planType);
				
			}
			else
			{
				if(model.getPlanOriginalId()==4||model.getPlanOriginalId()==10)
				{
					//#5生产类
					planType="M";
				}
				else if(model.getPlanOriginalId()==5||model.getPlanOriginalId()==11)
				{
					//老厂生产类
					planType="M";
				}
				else if(model.getPlanOriginalId()==6||model.getPlanOriginalId()==7)
				{
					//行政类
					planType="O";
				}
				else if(model.getPlanOriginalId()==3)
				{
					//固定资产
					planType="GD";
				}
				else if(model.getPlanOriginalId()==15)
				{
					//劳保类 add by fyyang 20100203
					planType="LB";
				}
				
				if(isNew)
				{
					//新流程
					busiStatus=this.getNewBusiStatus(model.getMrStatus(), eventIdentify, planType);
				}
				else
				{
					//老流程
				busiStatus=this.getBusiStatus(model.getMrStatus(), eventIdentify, planType);
				}
			}
			
			//-------add by fyyang 20100318 首次审批时改变核准数量----------
			if(model.getMrStatus().equals("1"))
			{
				String sql=
				"update mrp_j_plan_requirement_detail t\n" +
				"set t.approved_qty=t.applied_qty\n" + 
				"where  t.is_use='Y'  and nvl(t.approved_qty,0)=0  and  t.requirement_head_id="+model.getRequirementHeadId();

			bll.exeNativeSQL(sql);
			}
			//-----------------
			//改变主表业务状态
			model.setMrStatus(busiStatus);
			model.setItemCode(itemCode);//add by fyyang 20100325
			mrpPlanHeadRemote.update(model);
			
			//保存审批信息
			MrpJPlanRequirementHis hisModel=new MrpJPlanRequirementHis();
			hisModel.setApproveBy(workerCode);
			hisModel.setApproveStatus(busiStatus);
			hisModel.setApproveText(approveText);
			hisModel.setMrNo(mrNo);
			hisModel.setEnterpriseCode(model.getEnterpriseCode());
			hisRemote.save(hisModel);
			//改变工作流
			if(model.getMrStatus().equals("3"))
			{
				String deptType=commRemote.checkDeptType(model.getMrDept());
				Map m = new java.util.HashMap();
				if ("FD".equals(deptType)) {
					m.put("isFD", true);
					m.put("isJX", false);
					m.put("isSY", false);
					planType="FD";
				} else if ("JX".equals(deptType)) {
					m.put("isFD", false);
					m.put("isJX", true);
					m.put("isSY", false);
					planType="JX";
				} else {
					m.put("isFD", false);
					m.put("isJX", false);
					m.put("isSY", true);
					planType="SY";
				}
				service.doAction(model.getWfNo(), workerCode, actionId, approveText, m,nextRoles,""); 
			}
			else
			{
			this.changeWfInfo(model.getWfNo(), workerCode, actionId, approveText, nextRoles);
			}
		}
	}
	
	
	
	
	/**
	 * 执行
	 * @param entryId 实例编号
	 * @param workerCode 审批人
	 * @param actionId   动作
	 * @param approveText 意见
	 */
	private void changeWfInfo(Long entryId,String workerCode,Long actionId,String approveText,String nextRoles)
	{
		service.doAction(entryId, workerCode, actionId, approveText, null,nextRoles,""); 
	}
	private String getBusiStatus(String busiStatus,String eventIdentify,String planType)
	{
		if(eventIdentify.equals("TH")) return "9";
		else
		{
		   if(busiStatus.equals("1")) return "3"; //本部门领导已审批
		   else if(busiStatus.equals("3"))
		   {
			   if(planType.equals("FDM")||planType.equals("FDGD")) return "6"; //发电安生部领导已审批 
			   else if(planType.equals("JXM")||planType.equals("JXGD")) return "5";//检修安生部领导已审批
			   else if(planType.equals("SYM")||planType.equals("SYGD")) return "4";//实业安生部领导已审批
				 
			   else if(planType.equals("FDO")) return "8";//发电综合部领导已审批 行政类 发电
			   else if(planType.equals("SYO")) return "A"; //实业综合部领导已审批 行政类 实业
			   else if(planType.equals("JXO")) return "B"; //检修综合部领导已审批 行政类 检修
			   else if(planType.equals("FDLB")) return "Q"; //发电人力资源部已审批 劳保类 发电 add by fyyang 20100203
			   else if(planType.equals("SYLB")) return "R"; //实业人力资源部已审批 劳保类 实业
			   else if(planType.equals("JXLB")) return "S"; //检修人力资源部已审批 劳保类 检修
			   else return "P";
		   }
		   
		   if(busiStatus.equals("R")||busiStatus.equals("S"))
		   {
			   return "Q";
		   }
		   if(busiStatus.equals("Q"))
		   {
			   return "2";
		   }
		   //add by fyyang 091026
		   if(busiStatus.equals("P"))
		   {
			   return "F";
		   }
		   if(busiStatus.equals("F"))
		   {
			   return "2";
		   }
		   //add end
		   //生成类
		   else if(busiStatus.equals("4"))
		   {
			   if(planType.equals("M")) return "6";  //生产类
			   //if(planType.equals("GD"))  return "G";// 固定资产类  商务部领导已审批
			   if(planType.equals("GD"))  return "6";// 固定资产类  modify by fyyang 091026
		   }
		   else if(busiStatus.equals("5"))
		   {
			   if(planType.equals("M")) return "6";  //生产类
			 //  if(planType.equals("GD"))  return "G";// 固定资产类  商务部领导已审批
			   if(planType.equals("GD"))  return "6";// 固定资产类  modify by fyyang 091026
		   }
		   else if(busiStatus.equals("6"))
		   {
			   if(planType.equals("M")) return "2"; //生产类
			   if(planType.equals("GD")) return "G";// 固定资产类  商务部领导已审批
		   }
		   //行政类
		   else if(busiStatus.equals("8"))
		   {
			   return "2";//已结束
		   }
		   else if(busiStatus.equals("A"))
		   {
			   return "2";//已结束
		   }

		   else if(busiStatus.equals("B"))
		   {
			   return "2";//已结束
		   }

//		   //固定资产类

		   else if(busiStatus.equals("G"))
		   {
			   return "H";//物管中心已审批
		   }
		   else if(busiStatus.equals("H"))
		   {
			   return "I";//财务部已审批
		   }
		   else if(busiStatus.equals("I"))
		   {
			   return "J";//监察审计科已审批
		   }
		   else if(busiStatus.equals("J"))
		   {
			   return "K";//分管副总已审批
		   }
		   else if(busiStatus.equals("K"))
		   {
			   return "2";//已结束
		   }

			   
	   }
		return "";
	}
	
	//add by fyyang 20100428
	 private String getNewBusiStatus(String busiStatus,String eventIdentify,String planType)
	 {
		 if(eventIdentify.equals("TH")) return "9";
		 else
		 {
			 if(planType.equals("M"))
			   {
				 //生产类
				 if(busiStatus.equals("1")) return "3"; //本部门领导已审批
				 else if(busiStatus.equals("3")) return "T"; //技术支持部领导已审批
				 else if(busiStatus.equals("T")) return "U"; //发电安监部领导已审批
				 else if(busiStatus.equals("U")) return "2"; //已审批结束
					
			   }
			 else  if(planType.equals("GD"))
			 {
				 //固定资产类
				 if(busiStatus.equals("1")) return "3"; //本部门领导已审批
				 else if(busiStatus.equals("3")) return "T"; //技术支持部领导已审批
				 else if(busiStatus.equals("T")) return "U"; //发电安监部领导已审批
				 else if(busiStatus.equals("U")) return "H"; //物管中心已审批
				 else if(busiStatus.equals("H")) return "I"; //财务部已审批
				 else if(busiStatus.equals("I")) return "K"; //分管副总已审批
				 else if(busiStatus.equals("K")) return "2"; //已审批结束
				 
			 }
			 else  if(planType.equals("LB"))
			 {
				 //劳保类
				 
				 if(busiStatus.equals("1")) return "3"; //本部门领导已审批
				 else if(busiStatus.equals("3")) return "Q"; //发电人资部领导已审批
				 else if(busiStatus.equals("Q")) return "2"; //已审批结束
				 
			 }
			 else return "";
		 }
		
		 return "";
	 }
	 
	 //add by fyyang 20100428
	 private boolean checkIsNew(Long entryId)
	 {
		 String sql=
			 "select trim(substr(trim(t.flow_type),\n" +
			 "                   length(trim(t.flow_type)) - 3,\n" + 
			 "                   length(trim(t.flow_type))))\n" + 
			 "  from wf_c_entry t\n" + 
			 " where t.entry_id = "+entryId+"\n" + 
			 "   and rownum = 1";
		 if(("v1.0").equals(bll.getSingal(sql).toString()))
		 {
			 return false;
		 }
		 else
		 {
			 return true;
		 }

	 }
	
	/**
	 * 查找除statusCode外的所有状态
	 * @param statusCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<PlanApproveStatus> findNotInBusiStatus(String statusCode) 
	{
		String sql="select * from MRP_C_PLAN_REQUIREMENT_STATUS t\n";
		if(statusCode==null||statusCode.equals(""))
		{
			sql=sql+
			" order by t.plan_status_code";
		}
		else
		{
			sql=sql+
			"where t.plan_status_code not in ("+statusCode+")\n" + 
			" order by t.plan_status_code";
		}
		List list=bll.queryByNativeSQL(sql);
		List arraylist = new ArrayList();
		Iterator it=list.iterator();
		while(it.hasNext())
		{
			Object[] data=(Object[])it.next();
			PlanApproveStatus model=new PlanApproveStatus();
			if(data[0]!=null)
			model.setStatusCode(data[0].toString());
			if(data[1]!=null)
			model.setStatusName(data[1].toString());
			arraylist.add(model);
		}
       return arraylist;
	}
	
	public String  findDeptType(String deptCode)
	{
		return commRemote.checkDeptType(deptCode);
	}
}

package power.ejb.workticket.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.workticket.form.WorkticketBusiStatus;
import power.ejb.workticket.form.WorkticketHisForPrint;

@Stateless
public class BqWorkticketApproveImpl implements BqWorkticketApprove{
	@SuppressWarnings("unused")
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	protected RunJWorkticketsFacadeRemote baseRemote;
	protected RunJWorktickethisFacadeRemote hisRemote;
	WorkflowService service;
	public BqWorkticketApproveImpl()
	{
		baseRemote = (RunJWorkticketsFacadeRemote) Ejb3Factory.getInstance()
		.getFacadeRemote("RunJWorkticketsFacade");
		hisRemote = (RunJWorktickethisFacadeRemote) Ejb3Factory.getInstance()
		.getFacadeRemote("RunJWorktickethisFacade");
		service = new WorkflowServiceImpl();
	}
	
	//审批列表页面的状态
	public List<WorkticketBusiStatus>  findBusiStatusForApprove()
	{
		return this.findBusiStatus("1,8,9,14");
	}
	
	/**
	 * 查找状态不在statuIds里面的所有记录
	 * @param statuIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<WorkticketBusiStatus> findBusiStatus(String statuIds)
	{
		String sql="";
		if(statuIds!=null&&!statuIds.equals(""))
		{
			sql="select t.workticket_staus_id,t.workticket_status_name   from run_c_workticket_busi_status t  where t.workticket_staus_id not in  ("+statuIds+")  order by t.workticket_staus_id";
		}
		else
		{
			sql="select t.workticket_staus_id,t.workticket_status_name  from run_c_workticket_busi_status t order by t.workticket_staus_id";
		}
			List list=bll.queryByNativeSQL(sql);
		List arraylist = new ArrayList();
		Iterator it=list.iterator();
		while(it.hasNext())
		{
			Object[] data=(Object[])it.next();
			WorkticketBusiStatus model=new WorkticketBusiStatus();
			if(data[0]!=null)
			{
				model.setWorkticketStausId(Long.parseLong(data[0].toString()));
			}
			if(data[1]!=null)
			{
				model.setWorkticketStatusName(data[1].toString());
			}
			arraylist.add(model);
			
		}
		return arraylist;
	}
	
	 //上报
		public void reportTo(String workticketNo,String workflowType,String workerCode,Long actionId,String approveText,String nextRoles,String eventIdentify)
		{
			RunJWorktickets entity=baseRemote.findById(workticketNo);
//			String workflowType="";
//			String typeCode=entity.getWorkticketTypeCode();
//			if(typeCode.equals("1"))
//			{
//				//电气一种票
//				workflowType="bqWorkticket1";
//			}
//			else if(typeCode.equals("2"))
//			{
//				//电气二种票
//				workflowType="bqWorkticket2";
//			}
//			else if(typeCode.equals("3"))
//			{
//				//热力机械一票
//				workflowType="bqWorkticketRJ1";
//			}
//			else if(typeCode.equals("4"))
//			{
//				//一级动火票
//				workflowType="bqWorkticketDH1";
//			}
//			else if(typeCode.equals("5"))
//			{
//				//热控一票
//				workflowType="bqWorkticketRK1";
//			}
//			else if(typeCode.equals("6"))
//			{
//				//二级动火票
//				workflowType="bqWorkticketDH2";
//			}
//			else if(typeCode.equals("7"))
//			{
//				//二级热力机械票
//				workflowType="bqWorkticketRJ2";
//			}
//			else if(typeCode.equals("8"))
//			{
//				//二级热控票
//				workflowType="bqWorkticketRK2";
//			}
//			else
//			{
//				workflowType="";
//			}
			if(entity.getWorkFlowNo()==null)
			{
				//未上报的票
			if(!workflowType.equals(""))
			{
			long entryId = service.doInitialize(workflowType,workerCode,workticketNo );
			service.doAction(entryId, workerCode, actionId,approveText, null,nextRoles); 
			entity.setWorkFlowNo(entryId);
			
			}
			}
			else
			{
				//已退回的票
				this.changeWfInfo(entity.getWorkFlowNo(), workerCode, actionId, approveText, nextRoles);
			}
			if(eventIdentify.equals("SB"))
			{
			 entity.setIsEmergency("N");
			}
			if(eventIdentify.equals("JJSB"))
			{
				 entity.setIsEmergency("Y");
			}
			entity.setWorkticketStausId(2l);
			
			baseRemote.update(entity);
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
		
		
		private String check(RunJWorktickets model)
		{
			//add 090227
			if(model.getWorkticketTypeCode().equals("2")&&model.getWorkticketStausId()==4)
			{
			 //电二票许可时办理安措
				int num=this.ifSafetyAllExe(model.getWorkticketNo());
				if(num>0)
				{
					return "还有"+num+"条安措未执行，请先执行";
				}
				else
				{
					String safetyNo=this.ifSafetyConflict(model.getWorkticketNo());
				
					if(!safetyNo.equals(""))
					{
						return "安措与工作票号为"+safetyNo+"的安措冲突";
					}
				}
				return "";
			}
			//-------------------------
			//modify by fyyang 090304
			if(model.getWorkticketStausId()==2&&!model.getWorkticketTypeCode().equals("4"))
			{
				//签发时就判断是否冲突
				String chargeNo=this.ifChargerConflict(model.getWorkticketNo());
				if(!chargeNo.equals(""))
				{
					return "工作负责人与工作票号为"+chargeNo+"的负责人冲突！";
				}
			}
			if(model.getWorkticketStausId()==6)
			{
				//许可开工
				//判断安措是否冲突
				
				String safetyNo=this.ifSafetyConflict(model.getWorkticketNo());
			
				
				if(!safetyNo.equals(""))
				{
					return "安措与工作票号为"+safetyNo+"的安措冲突";
				}
				
			}
			
			return "";
		}
		
		public void doEquOp(RunJWorktickets model,String eventIdentify)
		{
			if(model.getWorkticketStausId()==6)
			{
				//许可开工
				if(!eventIdentify.equals("ZF")&&!eventIdentify.equals("TH"))
				{
				  this.addEquOpStatus(model.getWorkticketNo());
				}
			}
			if(model.getWorkticketStausId()==7)
			{
				//终结
				this.deleteEquOpStatus(model.getWorkticketNo());
			}
		}
		
		// 通用的签字审批
		public String workticketSign(String workticketNo,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles,String eventIdentify,String workticketMemo) {
			RunJWorktickets model=baseRemote.findById(workticketNo);
			String msg="";
			if(!eventIdentify.equals("TH")&&!eventIdentify.equals("ZF"))
			{
				msg=this.check(model);
			}
			if(!msg.equals(""))
			{
				return msg;
			}
		
			if(model!=null)
			{
			Long entryId=model.getWorkFlowNo();
			if(model.getWorkticketTypeCode().equals("4")&&model.getFirelevelId()==2)
			{
				//动火票时分一级动火和二级动火
				//二级动火时
				model.setWorkticketStausId(this.getBusiStatus("6",model.getWorkticketStausId(), eventIdentify));
			}
			else
			{
			model.setWorkticketStausId(this.getBusiStatus(model.getWorkticketTypeCode(),model.getWorkticketStausId(), eventIdentify,model.getIsEmergency()));
			}
			model.setWorkticketMemo(workticketMemo);
			baseRemote.update(model);
			RunJWorktickethis hismodel=new RunJWorktickethis();
			hismodel.setWorkticketNo(workticketNo);
			hismodel.setApproveBy(workerCode);
			hismodel.setApproveText(approveText);
			hismodel.setApproveDate(new java.util.Date());
			hismodel.setApproveStatus(model.getWorkticketStausId().toString());
			hisRemote.save(hismodel);
			this.doEquOp(model,eventIdentify);
			this.changeWfInfo(entryId, workerCode, actionId, approveText,nextRoles);
			
			}
			return "审批成功！";
			
		}
		
		//批准工作时间 
		public void workticketDutyChargeConfirm(String workticketNo,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles,String eventIdentify,String approveFinishDate) throws ParseException
		{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			RunJWorktickets model=baseRemote.findById(workticketNo);
			if(model!=null)
			{
			Long entryId=model.getWorkFlowNo();
			model.setWorkticketStausId(this.getBusiStatus(model.getWorkticketTypeCode(),model.getWorkticketStausId(), eventIdentify));
			model.setApprovedFinishDate(df.parse(approveFinishDate));
			baseRemote.update(model);
			RunJWorktickethis hismodel=new RunJWorktickethis();
			hismodel.setWorkticketNo(workticketNo);
			hismodel.setApproveBy(workerCode);
			hismodel.setApproveText(approveText);
			hismodel.setApproveDate(new java.util.Date());
			hismodel.setApproveStatus(model.getWorkticketStausId().toString());
			hisRemote.save(hismodel);
			this.changeWfInfo(entryId, workerCode, actionId, approveText,nextRoles);
			}
		}
		
		//安措办理
		public String workticketSafetyExe(String workticketNo,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles,String eventIdentify,String workticketMemo) throws ParseException
		{
			
			RunJWorktickets model=baseRemote.findById(workticketNo);
			int num=0;
			if(!eventIdentify.equals("ZF")&&!eventIdentify.equals("TH"))
			{
				num=this.ifSafetyAllExe(model.getWorkticketNo());
			}
			if(num>0)
			{
				return "还有"+num+"条安措未执行，请先执行";
			}
			else
			{
			if(model!=null)
			{
			Long entryId=model.getWorkFlowNo();
			model.setWorkticketStausId(this.getBusiStatus(model.getWorkticketTypeCode(),model.getWorkticketStausId(), eventIdentify));
			model.setWorkticketMemo(workticketMemo);
			baseRemote.update(model);
			RunJWorktickethis hismodel=new RunJWorktickethis();
			hismodel.setWorkticketNo(workticketNo);
			hismodel.setApproveBy(workerCode);
			hismodel.setApproveText(approveText);
			hismodel.setApproveDate(new java.util.Date());
			hismodel.setApproveStatus(model.getWorkticketStausId().toString());
			hisRemote.save(hismodel);
			this.changeWfInfo(entryId, workerCode, actionId, approveText,nextRoles);
			}
			}
			return "审批成功！";
		}
		
		//终结
		public String workticketEndApprove(RunJWorktickethis endHisModel,RunJWorktickethis pemitHisModel,RunJWorktickethis changeChargeModel,RunJWorktickethis delayHisModel,RunJWorktickethis safetyExeHisModel,Long actionId,String nextRoles,String eventIdentify,String workticketMemo,String responseDate)
		{
			if(actionId==0l) actionId=103l;
			RunJWorktickets model=baseRemote.findById(endHisModel.getWorkticketNo());
			if(model!=null)
			{
		   //回写主表
			Long entryId=model.getWorkFlowNo();
			model.setWorkticketStausId(this.getBusiStatus(model.getWorkticketTypeCode(),model.getWorkticketStausId(), eventIdentify));
			model.setWorkticketMemo(workticketMemo);
			baseRemote.update(model);
			//4-----许可开工 2------工作负责人变更 1-----------延期  7----安措办理
			//安措办理
			safetyExeHisModel.setChangeStatus("7");
			hisRemote.save(safetyExeHisModel);
			entityManager.flush();
			//许可开工
			
			pemitHisModel.setChangeStatus("4");
			hisRemote.save(pemitHisModel);
			entityManager.flush();
			if(changeChargeModel!=null)
			{
			//工作负责人变更
			changeChargeModel.setChangeStatus("2");
			hisRemote.save(changeChargeModel);
			entityManager.flush();
			}
			if(delayHisModel!=null)
			{
			//延期
			delayHisModel.setChangeStatus("1");
			hisRemote.save(delayHisModel);
			entityManager.flush();
			}
			//终结model
			endHisModel.setApproveStatus(model.getWorkticketStausId().toString());
			hisRemote.save(endHisModel);
			this.changeWfInfo(entryId, endHisModel.getApproveBy(), actionId, endHisModel.getApproveText(),nextRoles);
			}
			return "审批成功！";
		}
		
		//交回/恢复列表  5交回  6恢复
		@SuppressWarnings("unchecked")
		public List<WorkticketHisForPrint> findReturnBackList(String workticketNo) throws ParseException
		{
			//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List hisList = new ArrayList();
			String sql=
				"select t.id,t.workticket_no,\n" +
				"t.old_charge_by,\n" + 
				"t.new_charge_by,\n" + 
				"to_char(t.old_approved_finish_date,'yyyy-mm-dd hh24:mi:ss'),\n" + 
				"t.change_status," +
				"nvl(GETWORKERNAME(t.old_charge_by),t.old_charge_by),\n" + 
				"nvl(GETWORKERNAME(t.new_charge_by),t.new_charge_by)\n" + 
				"from run_j_worktickethis t\n" + 
				"where t.workticket_no='"+workticketNo+"'\n" + 
				"and t.change_status in (5,6)\n" + 
				"order by t.approve_date";

			List list = bll.queryByNativeSQL(sql);
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				WorkticketHisForPrint model=new WorkticketHisForPrint();
				if(data[0]!=null)
				{
					model.setId(data[0].toString());
				}
				if(data[1]!=null)
				{
					model.setWorkticketNo(data[1].toString());
				}
				if(data[2]!=null)
				{
					model.setOldChargeBy(data[2].toString());
				}
				if(data[3]!=null)
				{
					model.setNewChargeBy(data[3].toString());
				}
				if(data[4]!=null)
				{
					model.setOldApprovedFinishDate(data[4].toString());
				}
				if(data[5]!=null)
				{
					model.setChangeStatus(data[5].toString());
				}
				if(data[6]!=null)
				{
					model.setOldChargeByName(data[6].toString());
				}
				if(data[7]!=null)
				{
					model.setNewChargeByName(data[7].toString());
				}
				hisList.add(model);
			}
           return hisList;
		}
		
	//动火票安措办理	
	public String  fireSafetyExeApprove(String safetyType, String workticketNo,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles,String eventIdentify)
	{
		RunJWorktickets model=baseRemote.findById(workticketNo);
		int num=0;
		if(!eventIdentify.equals("ZF")&&!eventIdentify.equals("TH"))
		{
		num=this.fireSafetyNoExeCount(workticketNo, safetyType);
		}
		if(num>0)
		{
			return "还有"+num+"条安措未执行，请先执行";
		}
//		else 
//		{
//			if(model.getFirelevelId()==1&&safetyType.equals("fire"))
//			{
//				if((model.getPrintFlag()==null)||(!model.getPrintFlag().equals("1")))
//					return "请先打印安措执行卡";
//			}
//			if(model.getFirelevelId()==2&&safetyType.equals("run"))
//			{
//			if((model.getPrintFlag()==null)||(!model.getPrintFlag().equals("1")))
//			return "请先打印安措执行卡";
//			}
//		}
		
		if(model!=null)
		{
		Long entryId=model.getWorkFlowNo();
		if(model.getFirelevelId()==1)
		{
		model.setWorkticketStausId(this.getBusiStatus(model.getWorkticketTypeCode(),model.getWorkticketStausId(), eventIdentify));
		}
		else
		{
			model.setWorkticketStausId(this.getBusiStatus("6",model.getWorkticketStausId(), eventIdentify));
		}
		baseRemote.update(model);
		RunJWorktickethis hismodel=new RunJWorktickethis();
		hismodel.setWorkticketNo(workticketNo);
		hismodel.setApproveBy(workerCode);
		hismodel.setApproveText(approveText);
		hismodel.setApproveDate(new java.util.Date());
		hismodel.setApproveStatus(model.getWorkticketStausId().toString());
		hisRemote.save(hismodel);
		this.changeWfInfo(entryId, workerCode, actionId, approveText,nextRoles);
		}
		return "审批成功";
	}
	
	//动火票允许动火时间
	public void firePemitStartApprove(String workticketNo,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles,String eventIdentify,String permitStartDate) throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		RunJWorktickets model=baseRemote.findById(workticketNo);
		if(model!=null)
		{
		Long entryId=model.getWorkFlowNo();
		if(model.getFirelevelId()==1)
		{
		model.setWorkticketStausId(this.getBusiStatus(model.getWorkticketTypeCode(),model.getWorkticketStausId(), eventIdentify));
		}
		else
		{
			model.setWorkticketStausId(this.getBusiStatus("6",model.getWorkticketStausId(), eventIdentify));
		}

		model.setActualStartDate(df.parse(permitStartDate));
		baseRemote.update(model);
		RunJWorktickethis hismodel=new RunJWorktickethis();
		hismodel.setWorkticketNo(workticketNo);
		hismodel.setApproveBy(workerCode);
		hismodel.setApproveText(approveText);
		hismodel.setApproveDate(new java.util.Date());
		hismodel.setApproveStatus(model.getWorkticketStausId().toString());
		hismodel.setOldApprovedFinishDate(df.parse(permitStartDate));
		hisRemote.save(hismodel);
		this.changeWfInfo(entryId, workerCode, actionId, approveText,nextRoles);
		}
	}
	
	//动火票终结
	public void fireEndApprove(RunJWorktickethis hisModel,Long actionId,String responseDate,String nextRoles,String eventIdentify) throws ParseException
	{
		if(actionId==0l) actionId=223l;
		RunJWorktickets model=baseRemote.findById(hisModel.getWorkticketNo());
		if(model!=null)
		{
		Long entryId=model.getWorkFlowNo();
		if(model.getFirelevelId()==1)
		{
		model.setWorkticketStausId(this.getBusiStatus(model.getWorkticketTypeCode(),model.getWorkticketStausId(), eventIdentify));
		}
		else
		{
			model.setWorkticketStausId(this.getBusiStatus("6",model.getWorkticketStausId(), eventIdentify));
		}
		//动火结束时间
		model.setActualEndDate(hisModel.getOldApprovedFinishDate());
		baseRemote.update(model);
		hisModel.setApproveDate(new java.util.Date());
		hisModel.setApproveStatus(model.getWorkticketStausId().toString());
	
		hisRemote.save(hisModel);
		this.changeWfInfo(entryId, hisModel.getApproveBy(), actionId, hisModel.getApproveText(),nextRoles);
		}
	}
	//动火一票测量
	public void fireMeasureApprove(RunJWorkticketMeasure measureModel,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles,String eventIdentify)
	{
		RunJWorkticketMeasureFacadeRemote measureRemote=(RunJWorkticketMeasureFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunJWorkticketMeasureFacade");
		RunJWorktickets model=baseRemote.findById(measureModel.getWorkticketNo());
		if(model!=null)
		{
		Long entryId=model.getWorkFlowNo();
		model.setWorkticketStausId(this.getBusiStatus(model.getWorkticketTypeCode(),model.getWorkticketStausId(), eventIdentify));
     	baseRemote.update(model);
		RunJWorktickethis hismodel=new RunJWorktickethis();
		hismodel.setWorkticketNo(measureModel.getWorkticketNo());
		hismodel.setApproveBy(workerCode);
		hismodel.setApproveText(approveText);
		hismodel.setApproveDate(new java.util.Date());
		hismodel.setApproveStatus(model.getWorkticketStausId().toString());
		hisRemote.save(hismodel);
		//测量信息
		measureRemote.save(measureModel);
		this.changeWfInfo(entryId, workerCode, actionId, approveText,nextRoles);
		}
	}
	
//----------------------------------------------------------------		
		private String ifChargerConflict(String workticketNo)
		{
			String sql=
				"select b.workticket_no from run_j_worktickets b\n" +
				"where b.charge_by=(select c.charge_by  from  run_j_worktickets c where c.workticket_no='"+workticketNo+"' and c.is_use='Y')\n" + 
				"and b.is_use='Y'\n" + 
				"and b.workticket_staus_id=7  and b.workticket_type_code<>'4'";
		 Object obj=bll.getSingal(sql);
		 if(obj==null)
		 {
			 return "";
		 }
		 else
		 {
			 return obj.toString();
		 }
		}
		
		public String ifSafetyConflict(String workticketNo)
		{
			String sql=
				"select m.workticket_no\n" +
				" from RUN_J_WORKTICKET_EQUOP m,RUN_J_WORKTICKET_SAFETY tt,RUN_C_WORKTICKET_SAFETY_KEY n\n" + 
				"where  tt.attribute_code=m.attribute_code and tt.front_key_id=n.safety_key_id\n" + 
				"and tt.attribute_code<> 'temp'\n" + 
				"and m.safety_key_id=n.reverse_key_id\n" + 
				"and tt.workticket_no='"+workticketNo+"'\n" + 
				"and tt.is_use='Y'\n" + 
				"and n.is_use='Y'";
			   List list=bll.queryByNativeSQL(sql);
			   String no="";
			   if(list==null)
			   {
				   return "";
			   }
			   else
			   {
			   for(int i=0;i<list.size();i++)
			   {
				   Object obj = (Object) list.get(i);
				   if(no.equals(""))
				   {
					   no=no+obj.toString();
				   }
				   else
				   {
					   no=no+","+obj.toString();
				   }
			   }
			   return no;
			   }
		}
		
		
		
		
		
		private Long getBusiStatus(String workticketTypeCode,Long busiStatus,String eventIdentify,String ...isEmergency) 
		{
			if("1,2,3,5,7,8".indexOf(workticketTypeCode)!=-1)
			{
				if(eventIdentify.equals("TH")||eventIdentify.equals("FH"))
				{
					return 9l; //已退回
				}
				else if(eventIdentify.equals("ZF"))
				{
					return 14l;//已作废
				}
				
				else
				{
				  
				    if(busiStatus==2)
				    {
				    	if(isEmergency!=null&&isEmergency.length>0)
				    	{
				    		if(isEmergency[0].equals("Y"))
				    		{
				    			return 4l;
				    		}
				    		else
				    		{
				    			 return 3l; //已签发
				    		}
				    	}
				    	else
				    	{
				      //工作票签发人签发
					   return 3l; //已签发	
				    	}
				    }
				    else if(busiStatus==3)
				    {   // 热控票没有点检签发，直接值长审核 modify by ywliu 20100122
				    	if("5".equals(workticketTypeCode) || "8".equals(workticketTypeCode)) {
				    		return 18l;//值长审核
				    	}
				    	else {
				    		return 17l;//点检已审核签发
				    	}
					
				    }
				    else if(busiStatus==17) 
				    {
				    	//值长审核
				    	return 18l;
				    }
				    else if(busiStatus==18)
				    {
				    	//值班人员接票
				    	return 4l;//已接票
					
				    }
				    else if(busiStatus==4)
				    {
				    	if("1,3,5".indexOf(workticketTypeCode)!=-1)
				    	{
				    	//值长批准
				    	return 5l;//已批准
				    	}
				    	if("2,7,8".indexOf(workticketTypeCode)!=-1)
				    	{
				    	//许可开工
				    	return 7l;//已许可
				    	}
				    	
					
				    }
				    else if(busiStatus==5)
				    {
				    	//值班负责人安措办理
				    	return 6l;//已办理
					
				    }
				    else if(busiStatus==6)
				    {
				    	//工作许可人许可开工
				    	return 7l;//已许可
					
				    }
				    else if(busiStatus==7)
				    {
				    	//工作负责人终结
				    	return 8l;//已结束
					
				    }
				    else
				    {
				    	return 0l;
				    }
				    
				}
			}
			if("4,6".indexOf(workticketTypeCode)!=-1)
			{
				if(eventIdentify.equals("TH"))
				{
					return 9l; //已退回
				}
				else if(eventIdentify.equals("ZF"))
				{
					return 14l;//已作废
				}
				else
				{
					if(workticketTypeCode.equals("4"))
					{
							if(busiStatus==2) return 3l; //已签发
							else if(busiStatus==3) return 20l; //许可人安措已填写
							else if(busiStatus==20) 
							{
								if(eventIdentify.equals("YCL")) return 29l;  //测量人已测量
								else return 18l; }//值长已审核
							else if(busiStatus==29) return 18l;//值长已审核
							else if(busiStatus==18) return 22l;//消防监护人审批
							else if(busiStatus==22) return 10l;//消防部门负责人已审批
							else if(busiStatus==10) return 11l;//安监部门负责人已审批
							else if(busiStatus==11) return 23l;//领导已批准
							else if(busiStatus==23) return 24l; //工作负责人安措已办理
							else if(busiStatus==24) return 25l;  //许可人安措已办理
							else if(busiStatus==25) return 26l; //消防监护人已审批
							else if(busiStatus==26) return 27l;  //值班负责人已批准
							else if(busiStatus==27) return 30l; //工作负责人已终结
							else if(busiStatus==30) return 28l; //工作负责人已终结
							else return 0l;
					}
					if(workticketTypeCode.equals("6"))
					{
						if(busiStatus==2) return 3l; //已签发
						else if(busiStatus==3) return 20l; //许可人安措已填写
						else if(busiStatus==20) return 21l;//班长已审核
						else if(busiStatus==21) return 22l;//消防人员已审核
						else if(busiStatus==22) return 11l;//安监部门负责人已审批
						else if(busiStatus==11) return 23l;//领导已批准
						else if(busiStatus==23) return 24l; //工作负责人安措已办理
						else if(busiStatus==24) return 25l;  //许可人安措已办理
						else if(busiStatus==25) return 26l; //消防监护人已审批
						else if(busiStatus==26) return 27l;  //值班负责人已批准
						else if(busiStatus==27) return 28l; //工作负责人已终结
						else return 0l;
					}
				}
			}
			
			return 0l;
		}
		
		//未执行的安措数
		public int ifSafetyAllExe(String workticketNo)
		{
			String sql=
				"select count(*) from RUN_J_WORKTICKET_SAFETY tt\n" +
				" where tt.workticket_no='"+workticketNo+"'\n" + 
				" and (tt.safety_execute_code is null or tt.safety_execute_code='NON')\n" + 
				" and tt.is_use='Y'";
			 int num=Integer.parseInt(bll.getSingal(sql).toString());
			return num;
			
		}
		
		//动火票某种安措未执行的安措总数
		public int fireSafetyNoExeCount(String workticketNo,String safetyType)
		{
			String sql=
				"select count(*) from RUN_J_WORKTICKET_SAFETY tt\n" +
				" where tt.workticket_no='"+workticketNo+"'\n" + 
				" and (tt.safety_execute_code is null or tt.safety_execute_code='NON')\n" + 
				" and tt.is_use='Y'   and GETWTSAFETYTYPE(tt.safety_code)='"+safetyType+"' ";
			 int num=Integer.parseInt(bll.getSingal(sql).toString());
			return num;
		}
		
		
		
		private void addEquOpStatus(String workticketNo)
		{
			String sql=
				"insert into RUN_J_WORKTICKET_EQUOP c(c.workticket_no,c.attribute_code,c.safety_key_id)\n" +
				"select distinct '"+workticketNo+"',a.attribute_code,a.front_key_id from run_j_workticket_safety a\n" + 
				"where a.workticket_no='"+workticketNo+"'\n" + 
				//----修改-----
				"and trim(a.attribute_code)   is not null\n" +
				"       and  a.attribute_code <> 'temp'  \n"+
				//------------
				"and a.front_key_id in (select d.safety_key_id\n" + 
				"                        from RUN_C_WORKTICKET_SAFETY_KEY d\n" + 
				"                         where d.reverse_key_id>0)";
			bll.exeNativeSQL(sql);
		}
		
		private void deleteEquOpStatus(String workticketNo)
		{
			String sql=
				"delete RUN_J_WORKTICKET_EQUOP c\n" +
				"where c.workticket_no='"+workticketNo+"'";
	       bll.exeNativeSQL(sql);
			
		}
		
		@SuppressWarnings("unchecked")
		public List<WorkticketBusiStatus> findBusiStatusList() {
			String sql = "select t.workticket_staus_id,t.workticket_status_name  from run_c_workticket_busi_status t order by t.workticket_staus_id";
			List list = bll.queryByNativeSQL(sql);
			List arraylist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				WorkticketBusiStatus model = new WorkticketBusiStatus();
				if (data[0] != null) {
					model.setWorkticketStausId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					model.setWorkticketStatusName(data[1].toString());
				}
				arraylist.add(model);

			}
			return arraylist;

		} 
		public List<WorkticketBusiStatus> findBusiStatuForReport() {
			return this.findBusiStatusIn("1,9");
		}
		
		
		/**
		 * 查找状态在statuIds里面的所有记录
		 * @param statuIds
		 * @return
		 */
		@SuppressWarnings("unchecked")
		private List<WorkticketBusiStatus> findBusiStatusIn(String statuIds)
		{
			String sql="";
			if(statuIds!=null&&!statuIds.equals(""))
			{
				sql="select t.workticket_staus_id,t.workticket_status_name   from run_c_workticket_busi_status t  where t.workticket_staus_id  in  ("+statuIds+")  order by t.workticket_staus_id";
			}
			else
			{
				sql="select t.workticket_staus_id,t.workticket_status_name  from run_c_workticket_busi_status t order by t.workticket_staus_id";
			}
				List list=bll.queryByNativeSQL(sql);
			List arraylist = new ArrayList();
			Iterator it=list.iterator();
			while(it.hasNext())
			{
				Object[] data=(Object[])it.next();
				WorkticketBusiStatus model=new WorkticketBusiStatus();
				if(data[0]!=null)
				{
					model.setWorkticketStausId(Long.parseLong(data[0].toString()));
				}
				if(data[1]!=null)
				{
					model.setWorkticketStatusName(data[1].toString());
				}
				arraylist.add(model);
				
			}
			return arraylist;
		}
		
		
	
}

package power.ejb.run.protectinoutapply;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.run.protectinoutapply.form.ProtectinoutapplyPrintModel;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

@Stateless
public class ProtectApplyApproveImp implements ProtectApplyApprove{
	@SuppressWarnings("unused")
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	protected RunJProtectinoutApproveFacadeRemote hisRemote;
	protected RunJProtectinoutapplyFacadeRemote protectRemote;
	WorkflowService service;
	public ProtectApplyApproveImp()
	{
		protectRemote=(RunJProtectinoutapplyFacadeRemote)Ejb3Factory.getInstance()
		.getFacadeRemote("RunJProtectinoutapplyFacade");
		hisRemote=(RunJProtectinoutApproveFacadeRemote)Ejb3Factory.getInstance()
		.getFacadeRemote("RunJProtectinoutApproveFacade");
		service = new WorkflowServiceImpl();
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
	
	private Long getBusiStatus(Long busiStatus,String eventIdentify)
	{
		//1--未上报 2---已退回 3---已上报 4----设备部专工已签发 5----设备部主任已签发 6----厂领导已批准 7-----已结束
		//8---当值值长已审批 9---当值班长已许可开工
		if(eventIdentify.equals("TH"))
		{
			return 2l;
		}
		else if(eventIdentify.equals("ZJ"))
		{
			return 7l;
		}
		else
		{
			if(busiStatus==3) return 4l;
			else if(busiStatus==4) return 5l;
			else if(busiStatus==5) return 6l;
			else if(busiStatus==9) return 7l;
			else if(busiStatus==6) return 8l;
			else if(busiStatus==8) return 9l;
			else return 0l;	
			
			
		}
	}
	
	
	public void reportTo(String protectNo,String workflowType,
			String workerCode,Long actionId,String approveText,
			String nextRoles,String eventIdentify,String enterpriseCode)
	{
		RunJProtectinoutapply entity=protectRemote.findByProtectNo(protectNo,enterpriseCode);
		if(entity.getWorkFlowNo()!=null)
		{
			//已退回的票
			this.changeWfInfo(entity.getWorkFlowNo(), workerCode, actionId, approveText, nextRoles);
		}
		else
		{
			//未上报的票
			if(!workflowType.equals(""))
			{
				long entryId = service.doInitialize(workflowType,workerCode,protectNo);
				service.doAction(entryId, workerCode, actionId,approveText, null,nextRoles); 
				entity.setWorkFlowNo(entryId);
			}
		}
		
		entity.setStatusId(3l); //3---已上报
		protectRemote.update(entity);
	}
	
	public String protectApplySign(RunJProtectinoutapply model,RunJProtectinoutApprove hisModel,Long actionId,String responseDate,String nextRoles,String eventIdentify)
	{
		
	
		Long statusId=this.getBusiStatus(model.getStatusId(), eventIdentify);
		model.setStatusId(statusId);
		model.setLastModifyBy(hisModel.getApproveBy());
		hisModel.setApproveStatus(statusId);
		hisModel.setApproveDate(new java.util.Date());
		protectRemote.update(model);
		hisRemote.save(hisModel);
		this.changeWfInfo(model.getWorkFlowNo(), hisModel.getApproveBy(), actionId, hisModel.getApproveText(),nextRoles);
		return "审批成功！";
	}
	////////////////
	
	public ProtectinoutapplyPrintModel getModel(String protectNo) {
		ProtectinoutapplyPrintModel model = new ProtectinoutapplyPrintModel();
		model.setBaseList(this.getBaseModel(protectNo));
		List<RunJProtectinoutapply> baseList = model.getBaseList();
		Iterator<RunJProtectinoutapply> it = baseList.iterator();
		while(it.hasNext()) {
			RunJProtectinoutapply bean = it.next();
			if("Y".equals(bean.getIsIn())) {
				model.setApproveInList(this.findApproveHisList(bean.getProtectNo()));
			} else if("N".equals(bean.getIsIn())) {
				model.setApproveOutList(this.findApproveHisList(bean.getProtectNo()));
			}
		}
		return model;
	}

	@SuppressWarnings("unchecked")
	private List<RunJProtectinoutapply> getBaseModel(String protectNo) {
		List<RunJProtectinoutapply> baseList = new ArrayList<RunJProtectinoutapply>();
		String sql = "select t.protect_no ,nvl(GETDEPTNAME(t.apply_dept),t.apply_dept) apply_dept, nvl(GETWORKERNAME(t.apply_by),t.apply_by) apply_by,\n" +
			"        to_char(t.apply_date, 'yyyy-MM-dd hh24:mi:ss') ,nvl(getequnamebycode(t.equ_code),t.equ_code) equname,t.protect_name,\n" + 
			"        t.protect_reason, t.equ_effect , t.out_safety ,to_char(t.apply_start_date, 'yyyy-MM-dd hh24:mi:ss') ,\n" + 
			"        to_char(t.apply_end_date, 'yyyy-MM-dd hh24:mi:ss') ,nvl(GETWORKERNAME(t.execute_by),t.execute_by) execute_by ,\n" + 
			"        nvl(GETWORKERNAME(t.keeper),t.keeper) keeper ,nvl(GETWORKERNAME(t.permit_by),t.permit_by) permit_by,\n" + 
			"        nvl(GETWORKERNAME(t.checkup_by),t.checkup_by) checkup_by,to_char(t.protect_in_date, 'yyyy-MM-dd hh24:mi:ss') ,\n" + 
			"        to_char(t.protect_out_date, 'yyyy-MM-dd hh24:mi:ss') ,t.memo ,to_char(t.approve_start_date, 'yyyy-MM-dd hh24:mi:ss') ,\n" + 
			"        to_char(t.approve_end_date, 'yyyy-MM-dd hh24:mi:ss') ,t.status_id ,t.is_in from RUN_J_PROTECTINOUTAPPLY t\n" + 
			"        where t.is_use = 'Y'and t.enterprise_code = 'hfdc' and t.protect_no = '"+protectNo+"' or t.relative_no = '"+protectNo+"'";
		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		while (it.hasNext()) {
			RunJProtectinoutapply baseModel = new RunJProtectinoutapply();
			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				baseModel.setProtectNo(data[0].toString());
			}
			if (data[1] != null) {
				baseModel.setApplyDept(data[1].toString());
			}
			if (data[2] != null) {
				baseModel.setApplyBy(data[2].toString());
			}
			if (data[3] != null) {
				try {
					String applyDate = data[3].toString();
					Date date = df.parse(applyDate);
					baseModel.setApplyDate(sf.parse(sf.format(date)));
				} catch (ParseException e) { 
					e.printStackTrace();
				}
			}
			if (data[4] != null) {
				baseModel.setEquCode(data[4].toString());
			}
			if (data[5] != null) {
				baseModel.setProtectName(data[5].toString());
			}
			if (data[6] != null) {
				baseModel.setProtectReason(data[6].toString());
			}
			if (data[7] != null) {
				baseModel.setEquEffect(data[7].toString());
			}
			if (data[8] != null) {
				baseModel.setOutSafety(data[8].toString());
			}
			if (data[9] != null) {
				try {
					baseModel.setApplyStartDate(sf.parse(sf.format(df.parse(data[9].toString()))));
				} catch (ParseException e) { 
					e.printStackTrace();
				}
			}
			if (data[10] != null) {
				try {
					baseModel.setApplyEndDate(sf.parse(sf.format(df.parse(data[10].toString()))));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (data[11] != null) {
				baseModel.setExecuteBy(data[11].toString());
			}
			if (data[12] != null) {
				baseModel.setKeeper(data[12].toString());
			}
			if (data[13] != null) {
				baseModel.setPermitBy(data[13].toString());
			}
			if (data[14] != null) {
				baseModel.setCheckupBy(data[14].toString());
			}
			if (data[15] != null) {
				try {
					baseModel.setProtectInDate(sf.parse(sf.format(df.parse(data[15].toString()))));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (data[16] != null) {
				try {
					baseModel.setProtectOutDate(sf.parse(sf.format(df.parse(data[16].toString()))));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (data[17] != null) {
				baseModel.setMemo(data[17].toString());
			}
			if (data[18] != null) {
				try {
					baseModel.setApproveStartDate(sf.parse(sf.format(df.parse(data[18].toString()))));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (data[19] != null) {
				try {
					baseModel.setApproveEndDate(sf.parse(sf.format(df.parse(data[19].toString()))));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (data[20] != null) {
				String statusId = data[20].toString();
				baseModel.setStatusId(Long.valueOf(statusId));
			}
			if (data[21] != null) {
				baseModel.setIsIn(data[21].toString());
			}
			baseList.add(baseModel);
		}
		return baseList;
		
	}

	private List<RunJProtectinoutApprove> findApproveHisList(String protectNo) {
		String sql = "select nvl(GETWORKERNAME(t.approve_by),t.approve_by) approve_by ,t.approve_status ,to_char(t.approve_date, 'yyyy-MM-dd hh24:mi:ss') from run_j_protectinout_approve t where t.enterprise_code = 'hfdc'and t.protect_no = '"+protectNo+"'";
		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		List<RunJProtectinoutApprove> approveList = new ArrayList<RunJProtectinoutApprove>();
		while(it.hasNext()) {
			Object[] data = (Object[]) it.next();
			RunJProtectinoutApprove approveBean = new RunJProtectinoutApprove();
			if (data[0] != null) {
				approveBean.setApproveBy(data[0].toString());
			}
			if (data[1] != null) {
				approveBean.setApproveStatus(Long.valueOf(data[1].toString()));
			}
			if (data[2] != null) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
				try {
					approveBean.setApproveDate(sf.parse(sf.format(df.parse(data[2].toString()))));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			approveList.add(approveBean);
		}
		return approveList;
	}
	
//	@SuppressWarnings({ "unused", "unchecked" })
//	private RunJProtectinoutapply getBaseModel(String sql) {
//		
//		
//	}
}

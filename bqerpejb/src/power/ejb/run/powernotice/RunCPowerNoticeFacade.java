package power.ejb.run.powernotice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.run.powernotice.form.PowerNoticeInfo;
import power.ejb.run.powernotice.RunJPowerNoticeApprove;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

/**
 * Facade for entity RunCPowerNotice.
 * 
 * @see power.ejb.run.powernotice.RunCPowerNotice
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCPowerNoticeFacade implements RunCPowerNoticeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RunCPowerNotice save(RunCPowerNotice entity) {
		LogUtil.log("saving RunCPowerNotice instance", Level.INFO, null);
		try {
			entity.setNoticeId(bll.getMaxId("run_c_power_notice", "notice_id"));
			entity.setNoticeNo(this.createNoticeNo());
			entity.setIsUse("Y");
			entity.setModifyDate(new java.util.Date());
			entity.setBusiState(1l);//未上报
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(Long noticeId) {
		RunCPowerNotice entity=this.findById(noticeId);
		if(entity!=null)
		{
			entity.setIsUse("N");
			this.update(entity);
		}
	}
	
	public void deleteMulti(String noticeIds)
	{
		String sql=
			"update run_c_power_notice t\n" +
			"set t.is_use='N'\n" + 
			"where t.notice_id in ("+noticeIds+")\n" + 
			"and t.is_use='Y'";
		bll.exeNativeSQL(sql);

	}
	
	public RunCPowerNotice update(RunCPowerNotice entity) {
		LogUtil.log("updating RunCPowerNotice instance", Level.INFO, null);
		try {
			entity.setModifyDate(new java.util.Date());
			RunCPowerNotice result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
    
		public RunCPowerNotice findById(Long id) {
		LogUtil.log("finding RunCPowerNotice instance with id: " + id,
				Level.INFO, null);
		try {
			RunCPowerNotice instance = entityManager.find(
					RunCPowerNotice.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	private PageObject findAll(String enterpriseCode,String sdate,
			String edate,String contactDept,String busiState,String teamValue,String apply,String entryIds,final int... rowStartIdxAndCount)
	{
		PageObject	result = new PageObject();
		//modify by ypan 20100809
		String sql=
			"select distinct t.notice_id,\n" +
			"t.notice_no,\n" + 
			"t.contact_dept,\n" + 
			"GETDEPTNAME (t.contact_dept) deptname,\n" + 
			"t.contact_monitor,\n" + 
			"GETWORKERNAME(t.contact_monitor) monitorname,\n" + 
			"to_char(t.contact_date, 'yyyy-MM-dd hh24:mi:ss'),\n" + 
			"t.contact_content,\n" + 
			"t.memo,\n" + 
			"to_char(t.modify_date, 'yyyy-MM-dd hh24:mi:ss'),\n" + 
			"t.modify_by,\n" + 
			"GETWORKERNAME(t.modify_by) modifyman,\n" + 
			"t.enterprise_code,\n" + 
			"t.is_use,\n" + 
			"t.work_flow_no,\n" + 
			"t.wf_state,\n" + 
			"t.busi_state,\n" + 
			"decode(t.busi_state,1,'未上报',2,'已上报',3,'值班负责人已审批','4','已退回','') status,\n" + 
			" t.notice_sort,\n" + 
			"s.receive_team\n"+
			"from run_c_power_notice t,run_j_power_notice_approve s\n" + 
			"where   s.notice_no(+)=t.notice_no and  t.is_use='Y'  and t.enterprise_code='"+enterpriseCode+"'";
		String sqlCount=
			"select count(1)\n" +
			" from run_c_power_notice t,run_j_power_notice_approve s\n" + 
			"where t.notice_no=s.notice_no(+) and t.is_use='Y'  and t.enterprise_code='"+enterpriseCode+"'";
		String strWhere="";
	  //add by ypan 20100809
		if(teamValue!=null&&!teamValue.equals(""))
		{
			if(teamValue.equals("所有")){
				//modify by fyyang 20100820
				//strWhere=" and s.receive_team in ('一值','二值','三值','四值','五值')";
			}
			else{
				strWhere=" and s.receive_team in ('"+teamValue+"')";
			}
			sql=sql+strWhere;
			sqlCount=sqlCount+strWhere;
		}
		if(apply!=null&&!apply.equals(""))
		{
			strWhere="  and GETWORKERNAME(t.contact_monitor) like '%"+apply+"%'";
			sql=sql+strWhere;
			sqlCount=sqlCount+strWhere;
		}
		if(sdate!=null&&!sdate.equals(""))
		{
			strWhere =  "  and t.modify_date >=to_date('" + sdate
			+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
			sql=sql+strWhere;
			sqlCount=sqlCount+strWhere;
		}
		if(edate!=null&&!edate.equals(""))
		{
			strWhere =  "  and t.modify_date <=to_date('" + edate
			+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
			sql=sql+strWhere;
			sqlCount=sqlCount+strWhere;
		}
		if(contactDept!=null&&!contactDept.equals(""))
		{
			strWhere="  and t.contact_dept like '"+contactDept+"'";
			sql=sql+strWhere;
			sqlCount=sqlCount+strWhere;
		}
		if(busiState!=null&&!busiState.equals(""))
		{
			strWhere="  and t.busi_state in ("+busiState+")";
			sql=sql+strWhere;
			sqlCount=sqlCount+strWhere;
		}
		if(entryIds!=null)
		{
			
			strWhere="  and t.work_flow_no in ("+entryIds+")";
			sql=sql+strWhere;
			sqlCount=sqlCount+strWhere;
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		result.setTotalCount(totalCount);
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		if(list !=null && list.size()>0)
		{
			Iterator it = list.iterator();
			while (it.hasNext()) 
			{  
				//RunJPowerNoticeApprove powerApprove=new RunJPowerNoticeApprove();
				PowerNoticeInfo entity=new PowerNoticeInfo();
				RunCPowerNotice model=new RunCPowerNotice();
				Object[] data = (Object[]) it.next();
				model.setNoticeId(Long.parseLong(data[0].toString()));
				if(data[1]!=null)
				{
					model.setNoticeNo(data[1].toString());
				}
				if(data[2]!=null)
				{
					model.setContactDept(data[2].toString());
				}
				if(data[3]!=null)
				{
					entity.setDeptName(data[3].toString());
				}
				if(data[4]!=null)
				{
					model.setContactMonitor(data[4].toString());
				}
				if(data[5]!=null)
				{
					entity.setMonitorName(data[5].toString());
				}
				if(data[6]!=null)
				{
					entity.setContactDate(data[6].toString());
				}
				if(data[7]!=null)
				{
				model.setContactContent(data[7].toString());	
				}
				if(data[8]!=null)
				{
					model.setMemo(data[8].toString());
				}
				if(data[9]!=null)
				{
					entity.setModifyDate(data[9].toString());
				}
				if(data[10]!=null)
				{
					model.setModifyBy(data[10].toString());
				}
				if(data[11]!=null)
				{
					entity.setModifyMan(data[11].toString());
				}
				if(data[12]!=null)
				{
					model.setEnterpriseCode(data[12].toString());
				}
				if(data[13]!=null)
				{
					model.setIsUse(data[13].toString());
				}
				if(data[14]!=null)
				{
					model.setWorkFlowNo(Long.parseLong(data[14].toString()));
				}
				if(data[15]!=null)
				{
					model.setWfState(Long.parseLong(data[15].toString()));
				}
				if(data[16]!=null)
				{
					model.setBusiState(Long.parseLong(data[16].toString()));
				}
				if(data[17]!=null)
				{
					entity.setBusiStateName(data[17].toString());
					System.out.println(data[17].toString());
				}
				if(data[18] != null)
				{
					model.setNoticeSort(data[18].toString());
				}
				if(data[19] != null)
				{
					entity.setReceiveTeam(data[19].toString());
				}
				entity.setPower(model);
//				powerApprove.setPowerInfo(entity);
				arrlist.add(entity);
				
			}
		}
		result.setList(arrlist);
		return result;
	}
	//add by ypan 20100810
	public PageObject findData(final int... rowStartIdxAndCount){
		PageObject	result = new PageObject();
		String sql="select func_jhtj_runlog.getClassId(4,sysdate,t.shift_serial),\n"+
		"func_jhtj_runlog.getClassId(4,sysdate,t.shift_serial)\n"+ 
        "from run_c_shift_time t\n"+
       "where t.initial_no = 4\n"+ 
       "and t.is_use = 'Y'\n"+
       "order by t.shift_serial";
		String sqlCount=
			"select count(1)\n" +
			"from run_c_shift_time t\n";
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		result.setTotalCount(totalCount);
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		//List arrlist = new ArrayList();
		result.setList(list);
		return result;

	}

	public PageObject findNoReportList(String enterpriseCode,String contactDept,String busiState,final int... rowStartIdxAndCount)
	{
		return this.findAll(enterpriseCode, null, null, contactDept, busiState,null,null,null,rowStartIdxAndCount);
	}
	
	public PageObject findApproveList(String enterpriseCode,String contactDept,String entryIds,final int... rowStartIdxAndCount)
	{
		if(entryIds==null)
		{
			entryIds="''";
		}
		//modify by ypan 20100809
		return this.findAll(enterpriseCode, null, null, contactDept,"2",null,null,entryIds,rowStartIdxAndCount);
	}
	
	public PageObject findQueryList(String enterpriseCode,String sdate,String edate,String contactDept,String busiState,String teamValue,String apply,final int... rowStartIdxAndCount)
	{
		//modify by ypan 20100809

		return this.findAll(enterpriseCode, sdate, edate, contactDept, busiState,teamValue,apply, null, rowStartIdxAndCount);
	}
	
	/**
	 * 上报
	 * @param workticketNo
	 * @param workerCode
	 * @param actionId
	 */
	public void reportTo(String busitNo,String flowCode,String workerCode,Long actionId)
	{
		WorkflowService service = new WorkflowServiceImpl();
		long entryId = service.doInitialize(flowCode,workerCode,busitNo);
		service.doAction(entryId, workerCode, actionId, "上报", null); 
		String sql=" update run_c_power_notice  set busi_state=2,work_flow_no="+entryId+"  where notice_no='"+busitNo+"'";
		bll.exeNativeSQL(sql);
	}
	//审批签字
	public void approveSign(RunJPowerNoticeApprove his,String noticeNo,String workflowNo,String workerCode,
			Long actionId,String responseDate,String nextRoles,String eventIdentify) throws ParseException 
	{
		RunJPowerNoticeApproveFacadeRemote remote=(RunJPowerNoticeApproveFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunJPowerNoticeApproveFacade");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String status="";
		if(eventIdentify.equals("TH"))
		{
			his.setApproveStatus("4");
			status="4";
		}
		else
		{
			his.setApproveStatus("3");
			status="3";
		}
		his.setNoticeNo(noticeNo);
		his.setApproveBy(workerCode);
		his.setReceiveTeam(this.getNowShiftName()); //add by fyyang 20100805
		remote.save(his);
		String sql=
			"update run_c_power_notice t\n" +
			"set t.busi_state="+status+"\n" + 
			"where t.notice_no='"+noticeNo+"'";
         bll.exeNativeSQL(sql);
		this.changeWfInfo(Long.parseLong(workflowNo), workerCode, actionId, his.getApproveText(),nextRoles);
	 
	
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
		WorkflowService service = new WorkflowServiceImpl();
		service.doAction(entryId, workerCode, actionId, approveText, null,nextRoles,""); 
	}
	
	
	
	/**
	 * 生成停送电通知单号
	 * @return
	 */
	private String createNoticeNo()
	{
		String mymonth = "";
		java.text.SimpleDateFormat tempDate = new java.text.SimpleDateFormat(
				"yyyy-MM-dd" + " " + "hh:mm:ss");
		mymonth = tempDate.format(new java.util.Date());
		mymonth = mymonth.substring(0, 4) + mymonth.substring(5, 7);
		String no ="T"+mymonth;
		String sql=
			"select '"+no+"' ||\n" +
			"       (select Trim(case\n" + 
			"                 when max(t.notice_no) is null then\n" + 
			"                  '001'\n" + 
			"                 else\n" + 
			"                  to_char(to_number(substr(max(t.notice_no), 8, 3) + 1),\n" + 
			"                          '000')\n" + 
			"               end)\n" + 
			"          from run_c_power_notice t\n" + 
			"         where t.is_use = 'Y'\n" + 
			"           and substr(t.notice_no, 0, 7) = '"+no+"')\n" + 
			"  from dual";
		no= bll.getSingal(sql).toString().trim();
		return no;
	}
	
	
	private String getNowShiftName()
	{
		String name="";
		String sql=
			"select func_jhtj_runlog.getClassId(4,sysdate,t.shift_serial)\n" +
			"　from run_c_shift_time t\n" + 
			" where t.initial_no = 4\n" + 
			" and t.is_use = 'Y'\n" + 
			" and sysdate>=to_date((to_char(sysdate, 'yyyy-mm-dd')||to_char(t.on_duty_time, ' hh24:mm:ss')),'yyyy-mm-dd hh24:mi:ss')\n" + 
			" and sysdate<to_date((to_char(sysdate, 'yyyy-mm-dd')||to_char(t.off_duty_time, ' hh24:mm:ss')),'yyyy-mm-dd hh24:mi:ss')\n" + 
			" and rownum=1";
         Object obj=bll.getSingal(sql);
         if(obj!=null&&!obj.equals(""))
         {
        	 name=obj.toString();
         }
         return name;
		
	}
	
	

	
	
	
}
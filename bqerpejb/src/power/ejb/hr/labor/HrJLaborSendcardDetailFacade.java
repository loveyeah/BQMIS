package power.ejb.hr.labor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.plan.BpJPlanRepairDep;
import power.ejb.manage.plan.BpJPlanRepairDetail;

/**
 * Facade for entity HrJLaborSendcardDetail.
 * 
 * @see power.ejb.hr.labor.HrJLaborSendcardDetail
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJLaborSendcardDetailFacade implements
		HrJLaborSendcardDetailFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;
	@EJB(beanName = "HrJLaborSendcardFacade")
	private HrJLaborSendcardFacadeRemote remote;
	WorkflowService service;
	public  HrJLaborSendcardDetailFacade(){
		service = new WorkflowServiceImpl();
	}
	public HrJLaborSendcardDetail save(HrJLaborSendcardDetail entity) {
		LogUtil.log("saving HrJLaborSendcardDetail instance", Level.INFO, null);
		try {
			Long detailId=bll.getMaxId("HR_J_LABOR_SENDCARD_DETAIL", "detail_id");
			entity.setDetailId(detailId);
			entity.setIsUse("Y");
		
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public  void findByMainIdandSave(Long mainId,Long ID,String workCode,String enterpriseCode)
	{
		String sql=
			"select * from " +
			"HR_J_LABOR_SENDCARD_DETAIL  b\n" +
			"where b.sendcard_id='"+mainId+"'\n" + 
			"and b.is_use='Y'";
//		System.out.println("the sql"+sql);
    List<HrJLaborSendcardDetail>  list=bll.queryByNativeSQL(sql, HrJLaborSendcardDetail.class);
    if(list!=null&&list.size()>0)
    {
    	for(int i=0;i<list.size();i++)
    	{
    		HrJLaborSendcardDetail 	entity=list.get(i);
    		entity.setSendcardId(ID);
    		entity.setModifyBy(workCode);
    		entity.setModifyDate(new Date());
    		entity.setIsUse("Y");
    		entity.setEnterpriseCode(enterpriseCode);
    		this.save(entity);
    		entityManager.flush();
    	}
    }
	}
	

	//update by sychen 20100712 
    public Long    findMaxYear(String sendYear,String sendKind,String enterpriseCode)
    {
    	Long mainId=0l;
    	String sql="SELECT a.sendcard_id\n" +
    		"  FROM HR_J_LABOR_SENDCARD a\n" + 
    		" WHERE a.send_year = (SELECT nvl(MAX(a.send_year), 0)\n" + 
    		"                        FROM HR_J_LABOR_SENDCARD a\n" + 
    		"                       WHERE a.is_use = 'Y'\n" + 
    		"                         AND a.enterprise_code ='"+enterpriseCode+"'\n" + 
    		"                         AND (a.send_year < '"+sendYear+"'))\n" + 
    		"   AND a.send_kind ='"+sendKind+"'";
	
//    public Long    findMaxYear(String sendKind,String enterpriseCode)
//    {
//    	Long mainId=0l;

//    	String sql=	
//    		"select a.sendcard_id\n " +
//    		"from HR_J_LABOR_SENDCARD  a\n" +
//    		"where\n  " +
//    		"a.send_year=(select nvl(max(a.send_year),0)\n " +
//    		" from HR_J_LABOR_SENDCARD  a where a.is_use='Y'\n" +
//    		" and a.enterprise_code='"+enterpriseCode+"')\n" +
//    		"and a.send_kind='"+sendKind+"'";
//      System.out.println("the sql"+sql);

//update by sychen 20100712 end
    	Object  obj=bll.getSingal(sql);
    	if(obj!=null)
    	{
    		mainId=Long.parseLong(obj.toString());
    		return  mainId;
    	}
		return 0l;
    
	}

    public Long  autoReport(Long mainID,String workerCode,String workflowType)
	{
		    Long entryId;
			entryId = service.doInitialize(workflowType, workerCode, mainID
					.toString());
		
		service.doAction(entryId, workerCode, 24l,"SB", null,"", workerCode );
	
		return entryId;
		
	}
	public void saveMainAndDetail(String flag,String sendYear, String sendKind,String costItem,String enterpriseCode,String workCode,
			List<HrJLaborSendcardDetail> addlist,
			List<HrJLaborSendcardDetail> updatelist) {
	
			Long sendCardId=0l;  
			HrJLaborSendcard  mod=remote.findMainInfo(sendYear, sendKind, enterpriseCode);
			if(mod!=null)
			{
			sendCardId=mod.getSendcardId();
			}
			if (mod==null) {
				// 增加主表
				HrJLaborSendcard model = new HrJLaborSendcard();
				sendCardId = bll.getMaxId("HR_J_LABOR_SENDCARD", "sendcard_id");
				
				model.setSendcardId(sendCardId);
				model.setSendYear(sendYear);
				model.setSendKind(sendKind);
				model.setIsUse("Y");
				model.setEnterpriseCode(enterpriseCode);
				model.setEntryDate(new Date());
				model.setEntryBy(workCode); 
				model.setSendState("0");
				model.setCostItem(costItem);
				if(flag!=null&&flag.equals("approve"))
					
				{
				Long 	workNO=this.autoReport(sendCardId, workCode, "bqSendCard");
				     model.setSendState("1");
				     model.setWorkFlowNo(workNO);
				}
				  
				entityManager.persist(model);
			} else { //修改主表 的制表人和制表时间
				
			/*mod.setEntryDate(new Date());
			mod.setEntryBy(workCode);
			mod.setEnterpriseCode(enterpriseCode);
			mod.setIsUse("Y");
			entityManager.merge(mod);*/
				
			}

			if (addlist != null &&addlist.size() > 0) {

				for (HrJLaborSendcardDetail entity : addlist) {
					entity.setSendcardId(sendCardId);
					this.save(entity);
					entityManager.flush();
			  }
			}
			if (updatelist!=null && updatelist.size() > 0) {
				for (HrJLaborSendcardDetail entity : updatelist) {
					this.update(entity);
					
				}
			}
			
		}
	
		

	

	public void delete(String ids) {
		String sql = "update HR_J_LABOR_SENDCARD_DETAIL  b\n"
				+ "set  b.is_use='N' \n"
				+ "where b.detail_id  in(" + ids + ")";
		bll.exeNativeSQL(sql);

	}

	public HrJLaborSendcardDetail update(HrJLaborSendcardDetail entity) {
		LogUtil.log("updating HrJLaborSendcardDetail instance", Level.INFO,
				null);
		try {
			HrJLaborSendcardDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJLaborSendcardDetail findById(Long id) {
		LogUtil.log("finding HrJLaborSendcardDetail instance with id: " + id,
				Level.INFO, null);
		try {
			HrJLaborSendcardDetail instance = entityManager.find(
					HrJLaborSendcardDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJLaborSendcardDetail getSendCardInfo(Long mainId, String signName) {
		String sql = "select *" + "from HR_J_LABOR_SENDCARD_DETAIL  b \n"
				+ "where b.is_use='Y'\n" + "and b.sendcard_id='" + mainId
				+ "'\n " + "and b.sign_name='" + signName + "'\n";
		List<HrJLaborSendcardDetail> list = bll.queryByNativeSQL(sql,
				HrJLaborSendcardDetail.class);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	public void insertSendcardDetail(HrJLaborSendcard mainModel,
			List<HrJLaborSendcardDetail> detailList) {
		boolean flag = true;
		HrJLaborSendcard model = remote.findMainInfo(mainModel.getSendYear(),
				mainModel.getSendKind(), mainModel.getEnterpriseCode());
		if (model == null) {
			model = remote.save(mainModel);
			flag = true;
		} else {
			remote.update(model);
			flag = false;
		}
		Long mainId = model.getSendcardId();
		for (HrJLaborSendcardDetail detailModel : detailList) {
			if (flag) {
				detailModel.setSendcardId(mainId);
				this.save(detailModel);
				entityManager.flush();
			} else {
				HrJLaborSendcardDetail detailInfo = this.getSendCardInfo(
						mainId, detailModel.getSignName());
				if (detailInfo == null) {
					detailModel.setSendcardId(mainId);
					this.save(detailModel);
					entityManager.flush();
				} else {
					detailModel.setSendcardId(mainId);
					detailModel.setDetailId(detailInfo.getDetailId());
					detailModel.setEnterpriseCode(detailInfo
							.getEnterpriseCode());
					detailModel.setIsUse(detailInfo.getIsUse());
					this.update(detailModel);
				}
			}
		}
	}

	public PageObject findSendcardDetailList(String flag,String sendYear, String sendKind,
			int... rowStartIdxAndCount) {
		PageObject result = new PageObject();
		String sql = "select\n" 
			+ "a.sendcard_id,\n"
			+ "b.detail_id,"
			+ "b.dept_name," 
			+ "b.fact_num,"
			+ "b.send_standard,"			
			+ "(b.fact_num*b.send_standard) as money,\n"
			+ "b.sign_name,"
			+ "b.memo ," 
			+"a.work_flow_no,\n" 
			+"a.entry_by," 
			+"getworkername(a.entry_by)," 
			+"to_char(a.entry_date,'yyyy-MM-dd'),\n" 
			+"a.send_year," 
			+"a.send_kind,\n" 
			+"a.send_state\n"
			+ "from  HR_J_LABOR_SENDCARD  a,"
			+ "HR_J_LABOR_SENDCARD_DETAIL  b\n"
			+ "where\n"
			+" a.sendcard_id=b.sendcard_id\n"
			+ "and a.is_use='Y'\n"
			+ "and b.is_use='Y'";
		if (sendYear != null && !sendYear.equals("")) {
			sql += "and a.send_year='" + sendYear + "'\n";
		}
		if (sendKind != null && !sendKind.equals("")) {
			sql += "and a.send_kind='" + sendKind + "'\n";
		}
		if(flag!=null&&!flag.equals(""))
		{
			if(flag.equals("approve"))
			{
				sql+="and a.send_state ='1'";
			}else  if(flag.equals("report"))
			{
				sql+="and a.send_state='0'";
			}else 
			{
				
			}
		}
//		System.out.println("the sql"+sql);
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long count=0l;
		
		result.setList(list);
		return result;
	}

}
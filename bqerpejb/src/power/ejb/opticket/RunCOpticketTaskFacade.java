package power.ejb.opticket;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;


@Stateless
public class RunCOpticketTaskFacade implements RunCOpticketTaskFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public RunCOpticketTask save(RunCOpticketTask entity) { 
		try {
			if(entity.getOperateTaskId()==null){
				entity.setOperateTaskId(bll.getMaxId("RUN_C_OPTICKET_TASK","operate_task_id"));
			}
			entity.setOperateTaskCode(createOpticketTaskCode(entity.getParentOperateTaskId()));
			entity.setIsUse("Y");
			entity.setModifyDate(new Date());
			entityManager.persist(entity); 
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public RunCOpticketTask saveCopyTicket(RunCOpticketTask entity) { 
		try {
				entity.setOperateTaskId(bll.getMaxId("RUN_C_OPTICKET_TASK","operate_task_id"));
			entity.setOperateTaskCode(createOpticketTaskCode(entity.getParentOperateTaskId()));
			entity.setIsUse("Y");
			entity.setModifyDate(new Date());
			entityManager.persist(entity); 
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private String createOpticketTaskCode(Long pId){
		String sql="";
		Object o=new Object();
		if(pId==-1){
			sql=
				"select replace(nvl(lpad(to_char(max(t.operate_task_code) + 1), 2, '0'),\n" +
				"                   '01'),\n" + 
				"               '')\n" + 
				"  from run_c_opticket_task t\n" + 
				" where length(t.operate_task_code) = 2";
				o = bll.getSingal(sql); 
		}
		else{
			sql = 
				"select replace(decode(length(t.operate_task_code),\n" +
				"              2,\n" + 
				"              (select to_char(nvl(max(a.operate_task_code),t.operate_task_code||'00')+1||'','0000')   from run_c_opticket_task a where a.parent_operate_task_id=t.operate_task_id),\n" + 
				"               4,\n" + 
				"              (select to_char(nvl(max(a.operate_task_code),t.operate_task_code||'00')+1||'','000000')   from run_c_opticket_task a where a.parent_operate_task_id=t.operate_task_id),\n" + 
				"               6,\n" + 
				"              (select to_char(nvl(max(a.operate_task_code),t.operate_task_code||'00')+1||'','00000000')   from run_c_opticket_task a where a.parent_operate_task_id=t.operate_task_id),\n" + 
				"               8,\n" + 
				"              (select to_char(nvl(max(a.operate_task_code),t.operate_task_code||'00')+1||'','0000000000')   from run_c_opticket_task a where a.parent_operate_task_id=t.operate_task_id),\n" + 
				"               10,\n" + 
				"              (select to_char(nvl(max(a.operate_task_code),t.operate_task_code||'000')+1||'','0000000000000')   from run_c_opticket_task a where a.parent_operate_task_id=t.operate_task_id)\n" + 
				"             ,''),' ','')\n" + 
				"  from run_c_opticket_task t\n" + 
				" where t.operate_task_id = ?";
			o = bll.getSingal(sql,new Object[]{pId}); 
		}		
		return (o==null?"":o.toString()); 
	}

	
	public void delete(RunCOpticketTask entity) {
		entity.setIsUse("N");
		this.update(entity);		
	}

	
	public RunCOpticketTask update(RunCOpticketTask entity) {
		LogUtil.log("updating RunCOpticketTask instance", Level.INFO, null);
		try {
			entity.setModifyDate(new Date());
			RunCOpticketTask result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCOpticketTask findById(Long id) {
		LogUtil.log("finding RunCOpticketTask instance with id: " + id,
				Level.INFO, null);
		try {
			RunCOpticketTask instance = entityManager.find(
					RunCOpticketTask.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	
	@SuppressWarnings("unchecked")
	public List<RunCOpticketTask> findAll(String enterpriseCode) {
		LogUtil.log("finding all RunCOpticketTask instances", Level.INFO, null);
		try {
			final String sql="select * from run_c_opticket_task t where t.is_use='Y' and  t.enterprise_code=?";
			return bll.queryByNativeSQL(sql, new Object[]{enterpriseCode},RunCOpticketTask.class);
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	@SuppressWarnings("unchecked")
	public List<RunCOpticketTask> findByParentOperateTaskId(String enterpriseCode,Long parentOperateTaskId){
		final String sql="select * from run_c_opticket_task t where t.is_use='Y' and t.enterprise_code=? and t.parent_operate_task_id=? order by t.display_no";
		return bll.queryByNativeSQL(sql, new Object[]{enterpriseCode,parentOperateTaskId},RunCOpticketTask.class);
	}
	@SuppressWarnings("unchecked")
	public List<RunCOpticketTask> findByOTaskCodeLength(String enterpriseCode){
		String sql=
			"select *\n" +
			"  from run_c_opticket_task t\n" + 
			" where t.is_use = 'Y'\n" + 
			"   and t.enterprise_code = ?\n" + 
			"   and length(t.operate_task_code)=4";
		return bll.queryByNativeSQL(sql, new Object[]{enterpriseCode}, RunCOpticketTask.class);
	}
	/**
	 * 由操作操作票生成操作步骤
	 * @param ticketNos 标准操作票号
	 * @param enterpriseCode 企业编码
	 * @param exeBy 执行人
	 */
	public void addByStandTickets(String enterpriseCode,Long parentId,String ticketNos,String exeBy)
	{
		String[] nos = ticketNos.split(",");
		Long i=1l;
		for(String no : nos)
		{
			String sql = "select a.opticket_name from run_j_opticket a where a.opticket_code=?";
			Object o = bll.getSingal(sql, new Object[]{no});
			//如果操作步骤不为空
			if(o!=null && !"".equals(o.toString().trim()))
			{
				RunCOpticketTask  entity = new RunCOpticketTask();
				entity.setOperateTaskName(o.toString());
			    entity.setModifyBy(exeBy);
			    entity.setIsTask("Y");
			    entity.setEnterpriseCode(enterpriseCode);
			    entity.setDisplayNo(i++);
			    entity.setParentOperateTaskId(parentId);
			    entity = save(entity);
			    sql =  "insert into run_c_opticketstep\n" +
			    	"  (operate_step_id,\n" + 
			    	"   operate_task_id,\n" + 
			    	"   operate_step_name,\n" + 
			    	"   display_no,\n" + 
			    	"   is_main,\n" + 
			    	"   enterprise_code,\n" + 
			    	"   is_use,\n" + 
			    	"   modify_by,\n" + 
			    	"   modify_date)\n" + 
			    	"  select (select nvl(max(operate_step_id),0) from run_c_opticketstep) + row_number() over(order by b.display_no),\n" + 
			    	"         ?,\n" + 
			    	"         b.operate_step_name,\n" + 
			    	"         row_number() over(order by b.display_no),\n" + 
			    	"         'Y',\n" + 
			    	"         ?,\n" + 
			    	"         'Y',\n" + 
			    	"         ?,\n" + 
			    	"         sysdate\n" + 
			    	"    from run_j_opticketstep b\n" + 
			    	"   where b.opticket_code = ?";
			    bll.exeNativeSQL(sql, new Object[]{entity.getOperateTaskId(),enterpriseCode,exeBy,no});
			    entityManager.flush(); 
			}
		}
	}
	/**
	 * 复制操作票步骤
	 */
	public void copyTicket(Long sourceId, Long destId) {
		String sql = "SELECT *\n" +
			"  FROM RUN_C_OPTICKETSTEP t\n" + 
			" WHERE t.operate_task_id ="+sourceId;
		List<RunCOpticketstep> result = bll.queryByNativeSQL(sql, RunCOpticketstep.class);
		for(RunCOpticketstep o :result){
			Long stepId = bll.getMaxId("RUN_C_OPTICKETSTEP",
			"operate_step_id");
			o.setOperateTaskId(destId);
			o.setOperateStepId(stepId);
			entityManager.persist(o);
			entityManager.flush();
		}

	}
	
	
	
}
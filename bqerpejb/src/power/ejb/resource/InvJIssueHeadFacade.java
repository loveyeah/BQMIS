package power.ejb.resource;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.basedata.BaseDataManager;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.workbill.EquJOtmaFacadeRemote;
import power.ejb.hr.LogUtil;

/**
 * 领料单表头的facade.
 * 
 * @see power.ejb.resource.InvJIssueHead
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvJIssueHeadFacade implements InvJIssueHeadFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "EquJOtmaFacade")
	protected EquJOtmaFacadeRemote mabll;
	/**
	 * 更新领料单表头
	 * 
	 * @param entity
	 *            领料单
	 * @return InvJIssueHead 领料单
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvJIssueHead update(InvJIssueHead entity) {
		LogUtil.log("updating InvJIssueHead instance", Level.INFO, null);
		try {
			// 修改时间
			entity.setLastModifiedDate(new Date());
			InvJIssueHead result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 新增领料单表头数据
	 * 
	 * @param entity
	 *            InvJLocation entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvJIssueHead entity) {
		LogUtil.log("saving InvJIssueHead instance", Level.INFO, null);
		try {
			// 设定主键值
			if (entity.getIssueHeadId() == null) {
				entity.setIssueHeadId(bll.getMaxId("INV_J_ISSUE_HEAD",
						"ISSUE_HEAD_ID"));
			}
			// 设定修改时间
			entity.setLastModifiedDate(new java.util.Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
/**
 * 
 * @param head
 * @return
 * add bjxu
 */
	public InvJIssueHead addIssueHead(InvJIssueHead head) {
		try {
			// 流水号
			Long id = bll.getMaxId("INV_J_ISSUE_HEAD", "ISSUE_HEAD_ID");
			// 新增领料单表头
			head.setIssueNo(getIssueNo(id));
			head.setIsUse("Y");
			head.setIssueStatus("0");
			this.save(head);
			return head;
		} catch (RuntimeException e) {
			throw e;
		}

	}
	/**
	 * 生成领料单编号
	 * @param issueId 流水号
	 * add bjxu
	 */
	private String  getIssueNo(Long issueId){
		String issueNo = "LL";
		String id = String.valueOf(issueId);
		if(id.length() > 6){
			issueNo += id.substring(0,6);
		}else{
			String pad = "000000";
			issueNo +=pad.substring(0, 6 - id.length()) + id;
		}
		return issueNo;
	}
	
	/**
	 * 根据领料单表头的流水号查找领料单表头数据
	 * 
	 * @param issueHeadId
	 *            领料单表头流水号
	 * @return InvJIssueHead 领料单
	 */
	public InvJIssueHead findById(Long issueHeadId) {
		LogUtil.log("finding InvJIssueHead instance with issueHeadId: "
				+ issueHeadId, Level.INFO, null);
		try {
			InvJIssueHead instance = entityManager.find(InvJIssueHead.class,
					issueHeadId);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvJIssueHead findByMatCode(String matCode, String enterpriseCode) {
		String sql = "select t.* from INV_J_ISSUE_HEAD t where t.ISSUE_NO='"
				+ matCode +"' and t.is_use='Y'";
		List<InvJIssueHead> list = bll.queryByNativeSQL(sql,
				InvJIssueHead.class);
		if (list != null || list.size() > 0) {
			InvJIssueHead entity = list.get(0);
			return entity;
		} else {
			return null;
		}
	}
	
	/**
	 * 领料单审核
	 * add by fyyang 090619
	 * modify by fyyang 20100304
	 * @param ids
	 * @param checkBy
	 */
	@SuppressWarnings("unchecked")
	public void issueCheckOp(String ids,String checkBy,List<Map> list)
	{
		String[] strArray = ids.split(",");
		String issueNo = "";
		String lastModifiedDate = "";
		if(strArray[0] != null ) {
			issueNo = strArray[0].toString();
		}
		if(strArray[1] != null) {
			lastModifiedDate = strArray[1].toString();
		}
		
//		String sql=
//			"update INV_J_TRANSACTION_HIS t\n" + // modify by ywliu 20100201
//			"set t.CHECK_STATUS='C',t.check_by='"+checkBy+"',t.check_date=sysdate\n" + 
//			"where t.LAST_MODIFIED_DATE = to_date('"+lastModifiedDate+"','yyyy-mm-dd hh24:mi:ss')\n" +
//			" and t.ORDER_NO = '"+issueNo+"'";
		
		//modify by fyyang 20100226
		String sql=
	"update INV_J_TRANSACTION_HIS tt\n" +
	"set tt.CHECK_STATUS='C',tt.check_by='"+checkBy+"',tt.check_date=sysdate\n" + 
	"where tt.trans_his_id in (\n"+
	"select b.trans_his_id\n" +
	"          from inv_j_transaction_his t, resourse_v_rollback_arrival a,inv_j_transaction_his b\n" + 
	"         where t.order_no = '"+issueNo+"'\n" + 
	"           and t.trans_his_id = a.trans_his_id(+)\n" + 
	"           and ((t.trans_qty > 0 and t.trans_qty + nvl(a.sl, 0) <> 0) or\n" + 
	"               (t.trans_qty < 0 and t.roll_back_id is null))\n" + 
	"           and t.is_use = 'Y'   and b.is_use='Y'\n" + 
	"           and t.last_modified_date=to_date('"+lastModifiedDate+"','yyyy-mm-dd hh24:mi:ss')\n" + 
	"           and ( b.trans_his_id=t.trans_his_id or b.roll_back_id=t.trans_his_id) \n"+
	")";

//		"select b.trans_his_id\n" +
//		"  from inv_j_transaction_his b\n" + 
//		" where b.trans_his_id in\n" + 
//		"       (select t.trans_his_id\n" + 
//		"          from inv_j_transaction_his t, resourse_v_rollback_arrival a\n" + 
//		"         where t.order_no = '"+issueNo+"'\n" + 
//		"           and t.trans_his_id = a.trans_his_id(+)\n" + 
//		"           and ((t.trans_qty > 0 and t.trans_qty + nvl(a.sl, 0) <> 0) or\n" + 
//		"               (t.trans_qty < 0 and t.roll_back_id is null))\n" + 
//		"           and t.is_use = 'Y'\n" + 
//		"           and t.last_modified_date=to_date('"+lastModifiedDate+"','yyyy-mm-dd hh24:mi:ss')\n" + 
//		"\n" + 
//		"        )\n" + 
//		"    or b.roll_back_id in\n" + 
//		"       (select t.trans_his_id\n" + 
//		"          from inv_j_transaction_his t, resourse_v_rollback_arrival a\n" + 
//		"         where t.order_no = '"+issueNo+"'\n" + 
//		"           and t.trans_his_id = a.trans_his_id(+)\n" + 
//		"           and ((t.trans_qty > 0 and t.trans_qty + nvl(a.sl, 0) <> 0) or\n" + 
//		"               (t.trans_qty < 0 and t.roll_back_id is null))\n" + 
//		"           and t.is_use = 'Y'\n" + 
//		"           and t.last_modified_date=to_date('"+lastModifiedDate+"','yyyy-mm-dd hh24:mi:ss')\n" + 
//		
//		"\n" + 
//		"        ))";

		bll.exeNativeSQL(sql);
		
		try {
			// add by drdu 20100408 ---------------------------
			for (Map data : list) {
				InvJTransactionHis model = entityManager.find(InvJTransactionHis.class, Long.parseLong(data.get("id").toString()));
				if (data.get("tc") != null) {
					model.setTaxCount(Double.parseDouble(data.get("tc").toString()));
				} else {
					model.setTaxCount(0.0000);
				}
				entityManager.merge(model);
			}
		} catch (RuntimeException re) {
			throw re;
		}
		//----------------------------------------------------
	}
	
	/**
	 * 取消领料单审核
	 * add by drdu 091103
	 * modify by fyyang 20100304
	 * @param ids
	 * @param checkBy
	 */
	public void issueCheckCancel(String ids,String checkBy)
	{
		
		// modify by ywliu 20100202
		String[] strArray = ids.split(",");
		String issueNo = "";
		String lastModifiedDate = "";
		if(strArray[0] != null ) {
			issueNo = strArray[0].toString();
		}
		if(strArray[1] != null) {
			lastModifiedDate = strArray[1].toString();
		}
		String sql=
			"update INV_J_TRANSACTION_HIS t\n" + // modify by ywliu 20100201
			"set t.CHECK_STATUS='',t.check_by='"+checkBy+"',t.check_date=sysdate\n" + 
            "where t.trans_his_id in ("+
			"select b.trans_his_id\n" +
			"  from inv_j_transaction_his       t,\n" + 
			"       resourse_v_rollback_arrival a,\n" + 
			"       inv_j_transaction_his       b\n" + 
			" where t.order_no = '"+issueNo+"'\n" + 
			"   and t.trans_his_id = a.trans_his_id(+)\n" + 
			"   and ((t.trans_qty > 0 and t.trans_qty + nvl(a.sl, 0) <> 0) or\n" + 
			"       (t.trans_qty < 0 and t.roll_back_id is null))\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and b.is_use = 'Y'\n" + 
			"   and t.last_modified_date =\n" + 
			"       to_date('"+lastModifiedDate+"', 'yyyy-mm-dd hh24:mi:ss')\n" + 
			"   and (b.trans_his_id = t.trans_his_id or b.roll_back_id = t.trans_his_id) \n"+
		    ")";

		bll.exeNativeSQL(sql); 
	}
	
	/**
	 *  删除领料单 (同时删除其工作流信息)
	 *  add by fyyang 
	 * @param entryId 工作流实例号
	 * @param issueId 领料单id
	 */
	public void deleteIssueHead(Long entryId,Long issueId)
	{
		String sqlHead=
			"update inv_j_issue_head t\n" +
			"set t.is_use='N'\n" + 
			"where t.issue_head_id="+issueId;
		bll.exeNativeSQL(sqlHead);
		String sqlHeadDetail=
			"update inv_j_issue_details a\n" +
			"set a.is_use='N'\n" + 
			"where a.issue_head_id="+issueId;
		bll.exeNativeSQL(sqlHeadDetail);
		if(entryId!=null&&!entryId.equals(""))
		{
		BaseDataManager commRemote=(BaseDataManager)Ejb3Factory.getInstance()
		.getFacadeRemote("BaseDataManagerImpl");
		commRemote.deleteWf(entryId);
		}
	}
	

}
package power.ejb.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.form.MRPIssueMaterialDetailInfo;
import power.ejb.resource.form.SendMaterialsAccountForm;

/**
 * 领料单明细
 *
 * @see power.ejb.resource.InvJIssueDetails
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvJIssueDetailsFacade implements InvJIssueDetailsFacadeRemote {
	// property constants
	public static final String ISSUE_HEAD_ID = "issueHeadId";
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
	/**
	 * 更新领料单明细
	 * @param entity 领料单明细entity
	 * @return InvJIssueDetails 领料单明细entity
	 * @throws RuntimeException  if the operation fails
	 */
	public InvJIssueDetails update(InvJIssueDetails entity){
		LogUtil.log("updating InvJIssueDetails instance", Level.INFO, null);
		try {
			// 修改时间
            entity.setLastModifiedDate(new Date());
			InvJIssueDetails result = entityManager.merge(entity);
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
	 * @param entity InvJIssueDetails entity to persist
	 * @throws RuntimeException when the operation fails
	 */
	public void save(InvJIssueDetails entity) {
		LogUtil.log("saving InvJIssueDetails instance", Level.INFO, null);
		try {
		    // 设定主键值
			if(entity.getIssueDetailsId() == null){
				entity.setIssueDetailsId(bll.getMaxId("INV_J_ISSUE_DETAILS", "ISSUE_DETAILS_ID"));
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
	 * 根据领料单明细的流水号查找领料单明细数据
	 * @param issueDetailsId 领料单明细流水号
	 * @return InvJIssueDetails 领料单明细
	 */
	public InvJIssueDetails findById(Long issueDetailsId){
		LogUtil.log("finding InvJIssueDetails instance with issueDetailsId: " + issueDetailsId,
				Level.INFO, null);
		try {
			InvJIssueDetails instance = entityManager.find(
					InvJIssueDetails.class, issueDetailsId);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * Find all InvJIssueDetails entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the InvJIssueDetails property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvJIssueDetails> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvJIssueDetails> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding InvJIssueDetails instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from InvJIssueDetails model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}
	public List<InvJIssueDetails> findByIssueHeadId(Long issueHeadId) {
		return findByProperty(ISSUE_HEAD_ID, issueHeadId);
	}
	/**
	 * 根据需求计划单明细id查询领料单明细数据
	 * @param requirementDetailId
	 * @return MRPIssueMaterialDetailInfo
	 */
	public MRPIssueMaterialDetailInfo findIssueDetailByDetailID(
			Long requirementDetailId) {
		MRPIssueMaterialDetailInfo model = new MRPIssueMaterialDetailInfo();
		String sql = "select distinct f.issue_no, i.applied_count,i.approved_count,i.act_issued_count\n" +
			"from mrp_j_plan_requirement_detail a ,INV_J_ISSUE_DETAILS i ,Inv_j_Issue_Head f\n" + 
			"where a.requirement_detail_id = i.requirement_detail_id and f.issue_head_id = i.issue_head_id and a.requirement_detail_id = '"
            + requirementDetailId +"'";
		List result = bll.queryByNativeSQL(sql);
		Iterator it = result.iterator();
		while(it.hasNext()) {
			Object[] data = (Object[]) it.next();
			if(data[0]!= null) {
				model.setIssueNo(data[0].toString());
			}
			if(data[1]!= null) {
				model.setAppliedCount(data[1].toString());
			}
			if(data[2]!= null) {
				model.setApprovedCount(data[2].toString());
			}
			if(data[3]!= null) {
				model.setActIssuedCount(data[3].toString());
			}
		}
		return model;
	}

/**取工单对应物资数量
 * add bjxu
 * 根据领单InvJIssueHeadid
 */
	public int getMcount(Long IssueHeadId){
		String sql = "select count(1) from inv_j_issue_details t where t. issue_head_id='"+IssueHeadId+"'" +
				" and t.is_use='Y'";
		int count = Integer.parseInt(bll.getSingal(sql).toString());
		return count;
	}
	/**
	 * add bjxu
	 * 取一条工单对应物资
	 */
	public InvJIssueDetails getDetails(Long IssueHeadId,Long materialId){
		String sql ="select t.* from inv_j_issue_details t\n" +
			"where t.issue_head_id='"+IssueHeadId+"'and t.material_id='"+materialId+"'";
		List<InvJIssueDetails> list = bll.queryByNativeSQL(sql,
				InvJIssueDetails.class);
		if (list != null || list.size() > 0) {
			InvJIssueDetails entity = list.get(0);
			return entity;
		} else {
			return null;
		}
	}
	public PageObject getSendMaterialAccoun(String sdate,String edate,int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql =
//			"select tt1.*, \n"
//			+ "       nvl(tt2.qty2, 0) - nvl(tt3.qty3, 0) + nvl(tt4.qty4, 0) openbalance, \n"
//			+ "    nvl(tt5.qty5, 0) - nvl(tt6.qty6, 0) + nvl(tt7.qty7, 0) as thisqty, \n"
//			+ "    tt8.material_no, \n"
//			+ "   tt8.material_name, \n"
//			+ "   tt8.spec_no \n"
//			+ "  from (select a.material_id, \n"
//			+ "               sum(a.trans_qty * a.std_cost) price, \n"
//			+ "  sum(a.trans_qty) as qty, \n"
//			+ "   sum(a.trans_qty * a.std_cost) / sum(a.trans_qty) as stdCost \n"
//			+ "          from inv_j_transaction_his a, inv_j_issue_head b \n"
//			+ "where a.trans_id = 4 \n"
//			+ "  and b.checked_date > \n"
//			+ "         (select max(c.last_modified_date) from inv_j_balance c) \n"
//			+ " and a.order_no = b.issue_no \n"
//			+ " and b.issue_status = 'C' \n"
//			+ "         group by a.material_id) tt1, \n"
//			+ "       (select t2.material_id, sum(t2.trans_qty * t2.std_cost) qty2 \n"
//			+ "from inv_j_transaction_his t2 \n"
//			+ "         where t2.trans_id = 6 \n"
//			+ "           and t2.trans_his_id >(select max(c.trans_his_maxid) from inv_j_balance c) \n"
//			+ "group by t2.material_id) tt2, \n"
//			+ "(select t5.material_id, sum(t5.trans_qty * t5.price) qty3 \n"
//			+ "from inv_j_transaction_his t5, pur_j_arrival t55 \n"
//			+ "         where t5.trans_id = 1 \n"
//			+ "           and t5.arrival_no = t55.arrival_no \n"
//			+ "and ((t55.check_date < \n"
//			+ "    (select c.balance_date \n"
//			+ "                    from inv_j_balance c \n"
//			+ "                   where c.balance_id = (select max(c.balance_id)-1 from inv_j_balance c)) and t55.check_state <> '2' and \n"
//			+ "t55.check_state <> 'N') or \n"
//			+ "(t55.check_date > \n"
//			+ "               (select c.balance_date \n"
//			+ "                    from inv_j_balance c \n"
//			+ "                   where c.balance_id = (select max(c.balance_id)-1 from inv_j_balance c)) and \n"
//			+ "(t55.check_state = '2' or t55.check_state = 'N'))) \n"
//			+ "         group by t5.material_id \n"
//			+ ") tt3, \n"
//			+ "       (select t6.material_id, sum(t6.trans_qty * t6.std_cost) qty4 \n"
//			+ "from inv_j_transaction_his t6, inv_j_issue_head t66 \n"
//			+ "         where t6.trans_id = 4 \n"
//			+ "           and t6.order_no = t66.issue_no \n"
//			+ "and ((t66.checked_date < \n"
//			+ "    (select c.balance_date \n"
//			+ "                    from inv_j_balance c \n"
//			+ "                   where c.balance_id = (select max(c.balance_id)-1 from inv_j_balance c)) and t66.issue_status <> 'C') or \n"
//			+ "(t66.checked_date > \n"
//			+ "(select c.balance_date \n"
//			+ "                    from inv_j_balance c \n"
//			+ "                   where c.balance_id = (select max(c.balance_id)-1 from inv_j_balance c)) and t66.issue_status = 'C')) \n"
//			+ "group by t6.material_id \n"
//			+ ") tt4, \n"
//			+ "       (select t7.material_id, \n"
//			+ "               sum(t7.open_balance + t7.receipt + t7.adjust - t7.issue) qty5 \n"
//			+ "from inv_j_warehouse t7 \n"
//			+ "         group by t7.material_id) tt5, \n"
//			+ "       (select t8.material_id, sum(t8.trans_qty) qty6 \n"
//			+ "          from inv_j_transaction_his t8, pur_j_arrival t88 \n"
//			+ "where t8.arrival_no = t88.arrival_no \n"
//			+ "  and t8.trans_id = 1 \n"
//			+ "           and t88.check_state <> '2' \n"
//			+ "and t88.check_state <> 'N' \n"
//			+ "         group by t8.material_id) tt6, \n"
//			+ "       (select t9.material_id, sum(t9.trans_qty) qty7 \n"
//			+ "          from inv_j_transaction_his t9, inv_j_issue_head t99 \n"
//			+ "where t9.trans_id = 4 \n"
//			+ "  and t9.order_no = t99.issue_no \n"
//			+ "and t99.issue_status <> 'C' \n"
//			+ "         group by t9.material_id) tt7, \n"
//			+ "inv_c_material tt8 \n"
//			+ " where tt1.material_id = tt2.material_id(+) \n"
//			+ "and tt1.material_id = tt3.material_id(+) \n"
//			+ "and tt1.material_id = tt4.material_id(+) \n"
//			+ "and tt1.material_id = tt5.material_id(+) \n"
//			+ "and tt1.material_id = tt6.material_id(+) \n"
//			+ "and tt1.material_id = tt7.material_id(+) \n"
//			+ " and tt1.material_id = tt8.material_id(+) \n";
	//		modify by fyyang 091223 按审核时间查询
			
			"select tt1.*,\n" +
			"       nvl(tt2.qty2, 0) - nvl(tt3.qty3, 0) + nvl(tt4.qty4, 0) openbalance,\n" + 
			"       nvl(tt5.qty5, 0) - nvl(tt6.qty6, 0) + nvl(tt7.qty7, 0) as thisqty,\n" + 
			"       tt8.material_no,\n" + 
			"       tt8.material_name,\n" + 
			"       tt8.spec_no\n" + 
			"  from (select a.material_id,\n" + 
			"               sum(a.trans_qty * a.std_cost) price,\n" + 
			"               sum(a.trans_qty) as qty,\n" + 
			"               decode(sum(a.trans_qty),0,0, sum(a.trans_qty * a.std_cost) / sum(a.trans_qty)) as stdCost\n" + 
			"          from inv_j_transaction_his a, inv_j_issue_head b\n" + 
			"         where a.trans_id = 4\n" + 
			"  and  b.checked_date>=to_date('"+sdate+"'||' 00:00:00','yyyy-MM-dd hh24:mi:ss')"+
			"  and  b.checked_date<=to_date('"+edate+"'||' 23:59:59','yyyy-MM-dd hh24:mi:ss')"+
//			"           and b.checked_date >\n" +  //modify by fyyang 091223 已审核，未结账查询条件去掉
//			"               (select max(c.last_modified_date) from inv_j_balance c)\n" + 
			"           and a.order_no = b.issue_no\n" + 
//			"           and b.issue_status = 'C'\n" + 
			"         group by a.material_id) tt1,\n" + 
			"\n" + 
			"       (select t2.material_id, sum(t2.trans_qty * t2.std_cost) qty2\n" + 
			"          from inv_j_transaction_his t2\n" + 
			"         where t2.trans_id = 6\n" + 
			"           and t2.trans_his_id >(select max(c.trans_his_maxid) from inv_j_balance c)\n" + 
			"         group by t2.material_id) tt2,\n" + 
			"\n" + 
			"       (select t5.material_id, sum(t5.trans_qty * t5.price) qty3\n" + 
			"          from inv_j_transaction_his t5, pur_j_arrival t55\n" + 
			"         where t5.trans_id = 1\n" + 
			"           and t5.arrival_no = t55.arrival_no\n" + 
			"           and ((t55.check_date <\n" + 
			"               (select c.balance_date\n" + 
			"                    from inv_j_balance c\n" + 
			"                   where c.balance_id = (select max(c.balance_id)-1 from inv_j_balance c)) and ((t55.check_state <> '2' and\n" + 
			"               t55.check_state <> 'N') or t55.check_state is null)) or\n" + 
			"               (t55.check_date >\n" + 
			"               (select c.balance_date\n" + 
			"                    from inv_j_balance c\n" + 
			"                   where c.balance_id = (select max(c.balance_id)-1 from inv_j_balance c)) and\n" + 
			"               (t55.check_state = '2' or t55.check_state = 'N')))\n" + 
			"         group by t5.material_id\n" + 
			"\n" + 
			"        ) tt3,\n" + 
			"       (select t6.material_id, sum(t6.trans_qty * t6.std_cost) qty4\n" + 
			"          from inv_j_transaction_his t6, inv_j_issue_head t66\n" + 
			"         where t6.trans_id = 4\n" + 
			"           and t6.order_no = t66.issue_no\n" + 
			"           and ((t66.checked_date <\n" + 
			"               (select c.balance_date\n" + 
			"                    from inv_j_balance c\n" + 
			"                   where c.balance_id = (select max(c.balance_id)-1 from inv_j_balance c)) and (t66.issue_status <> 'C' or t66.issue_status is null)) or\n" + 
			"               (t66.checked_date >\n" + 
			"               (select c.balance_date\n" + 
			"                    from inv_j_balance c\n" + 
			"                   where c.balance_id = (select max(c.balance_id)-1 from inv_j_balance c)) and t66.issue_status = 'C'))\n" + 
			"         group by t6.material_id\n" + 
			"\n" + 
			"        ) tt4,\n" + 
			"       (select t7.material_id,\n" + 
			"               sum(t7.open_balance + t7.receipt + t7.adjust - t7.issue) qty5\n" + 
			"          from inv_j_warehouse t7\n" + 
			"         group by t7.material_id) tt5,\n" + 
			"       (select t8.material_id, sum(t8.trans_qty) qty6\n" + 
			"          from inv_j_transaction_his t8, pur_j_arrival t88\n" + 
			"         where t8.arrival_no = t88.arrival_no\n" + 
			"           and t8.trans_id = 1\n" + 
			"           and ((t88.check_state <> '2'\n" + 
			"           and t88.check_state <> 'N') or t88.check_state is null)\n" + 
			"         group by t8.material_id) tt6,\n" + 
			"       (select t9.material_id, sum(t9.trans_qty) qty7\n" + 
			"          from inv_j_transaction_his t9, inv_j_issue_head t99\n" + 
			"         where t9.trans_id = 4\n" + 
			"           and t9.order_no = t99.issue_no\n" + 
			"           and (t99.issue_status <> 'C' or t99.issue_status is null)\n" + 
			"         group by t9.material_id) tt7,\n" + 
			"          inv_c_material tt8\n" + 
			"\n" + 
			" where tt1.material_id = tt2.material_id(+)\n" + 
			"   and tt1.material_id = tt3.material_id(+)\n" + 
			"   and tt1.material_id = tt4.material_id(+)\n" + 
			"   and tt1.material_id = tt5.material_id(+)\n" + 
			"   and tt1.material_id = tt6.material_id(+)\n" + 
			"    and tt1.material_id = tt7.material_id(+)\n" + 
			"     and tt1.material_id = tt8.material_id(+)";

		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		List<SendMaterialsAccountForm> arrlist = new ArrayList<SendMaterialsAccountForm>();
		if(list != null && list.size() > 0){
			Iterator it = list.iterator();
			while(it.hasNext()){
				Object[] da = (Object[])it.next();
				SendMaterialsAccountForm form = new SendMaterialsAccountForm();
				if(da[0] != null)
					form.setMaterialId(Long.parseLong(da[0].toString()));
				if(da[1] != null)
					form.setPrice(Double.parseDouble(da[1].toString()));
				else 
					form.setPrice(0.0);
				if(da[2] != null)
					form.setQty(Double.parseDouble(da[2].toString()));
				else 
					form.setQty(0.0);
				if(da[3] != null)
					form.setStdCost(Double.parseDouble(da[3].toString()));
				else
					form.setStdCost(0.0);
				if(da[4] != null)
					form.setOpenBalance(Double.parseDouble(da[4].toString()));
				else
					form.setOpenBalance(0.0);
				if(da[5] != null)
					form.setThisQty(Double.parseDouble(da[5].toString()));
				else
					form.setThisQty(0.0);
				if(da[6] != null)
					form.setMaterialNo(da[6].toString());
				if(da[7] != null)
					form.setMaterialName(da[7].toString());
				if(da[8] != null)
					form.setSpecNo(da[8].toString());
				arrlist.add(form);
			}
		}
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}
}
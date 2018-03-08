package power.ejb.manage.budget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;

/**
 * Facade for entity CbmCItemtx.
 * 
 * @see power.ejb.manage.budget.CbmCItemtx
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCItemtxFacade implements CbmCItemtxFacadeRemote {
	// property constants
	public static final String ZBBMTX_CODE = "zbbmtxCode";
	public static final String ZBBMTX_NAME = "zbbmtxName";
	public static final String ITEM_ID = "itemId";
	public static final String IS_ITEM = "isItem";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "CbmCItemFacade")
	protected CbmCItemFacadeRemote itemRemote;

	/**
	 * @param entity
	 *            CbmCItemtx entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public CbmCItemtx save(CbmCItemtx entity) {
		try {
			entity.setZbbmtxId(bll.getMaxId("CBM_C_ITEMTX", "ZBBMTX_ID"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent CbmCItemtx entity.
	 */
	public void delete(CbmCItemtx entity) {
		try {
			entity.setIsUse("N");
			if (entity.getItemId() != null) {
				CbmCItem model = itemRemote.findById(entity.getItemId());
				itemRemote.delete(model);
			}
			this.update(entity);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * updated entity.
	 * 
	 * @param entity
	 *            CbmCItemtx entity to update
	 */
	public CbmCItemtx update(CbmCItemtx entity) {
		try {
			CbmCItemtx result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public CbmCItemtx findByItemtxId(Long systemId) {
		try {
			CbmCItemtx model = entityManager.find(CbmCItemtx.class, systemId);
			return model;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CbmCItemtx> findById(Long id) {
		try {
			List list = new ArrayList();
			String sql = "select a.zbbmtx_id,a.zbbmtx_name,a.item_id,a.is_item,b.item_code,b.unit_code from cbm_c_itemtx a , cbm_c_item b\n"
					+ "where a.zbbmtx_id ='"
					+ id
					+ "'\n"
					+ "and a.item_id = b.item_id\n"
					+ "and a.is_use ='Y'\n"
					+ "and b.is_use = 'Y'";

			list = bll.queryByNativeSQL(sql);
			List<CbmCItemtx> arraylist = new ArrayList<CbmCItemtx>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				CbmCItemtx model = new CbmCItemtx();
				if (data[0] != null)
					model.setZbbmtxId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setZbbmtxName(data[1].toString());
				if (data[2] != null)
					model.setItemId(Long.parseLong(data[2].toString()));
				if (data[3] != null)
					model.setIsItem(data[3].toString());
				if (data[4] != null)
					model.setZbbmtxCode(data[4].toString());
				if (data[5] != null)
					model.setEnterpriseCode(data[5].toString());

				arraylist.add(model);
			}
			return arraylist;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	// 体系编码
	public String creatCode(String itemFCode) {
		String temp = "";
		if (itemFCode != null && !itemFCode.equals("000"))
			temp = itemFCode;
		String sql = "select '"
				+ temp
				+ "'"
				+ " || nvl(trim(to_char(max(substr(t.zbbmtx_code, length(t.zbbmtx_code) - 1)) + 1,'000')),\n"
				+ "           '001') from cbm_c_itemtx t"
				+ " where is_use = 'Y'\n";
		if (itemFCode != null && !itemFCode.equals("000")) {
			sql += " and t.zbbmtx_code like '" + itemFCode
					+ "%' and t.zbbmtx_code !='" + itemFCode + "'";
		}
		String Code = bll.getSingal(sql).toString();
		CbmCItemtx model = new CbmCItemtx();
		model.setZbbmtxCode(Code);
		return Code;
	}

	@SuppressWarnings("unchecked")
	public List<TreeNode> findBudgetTreeList(String year,String itemCode,
			String enterpriseCode) {
		List<TreeNode> res = null;
		String sql;
		Integer size;
		Integer maxsize;
		if(year!=null&&!"".equals(year)){
			size = itemCode.length();
			maxsize = size + 4;
			if (itemCode != null && itemCode.equals("000")) {
				sql = "select t.* from cbm_c_itemtx t,\n"+
				"CBM_C_ITEM t2,CBM_C_CENTER_ITEM t3,\n " +
				"CBM_J_BUDGET_ITEM t4,CBM_J_BUDGET_MAKE t5 \n " +
				"  where t.item_id = t2.item_id \n" +
				" and length(t.zbbmtx_code) > '" + size + "'"+
				" and length(t.zbbmtx_code) < '" + maxsize + "'"+
				"  AND t.item_id = t3.item_id \n" +
				"  AND t3.center_item_id = t4.center_item_id \n" +
				"  AND t4.budget_make_id = t5.budget_make_id \n" +
				"  AND t5.budget_time = '"+year+"' \n" +
				"  AND t.is_use = 'Y' \n" +
				"  AND t2.is_use = 'Y' \n" +
				"  AND t3.is_use = 'Y' \n" +
				"  AND t4.is_use = 'Y' \n" +
				"  AND t5.is_use = 'Y'\n"+
				" order by t2.order_by,t.zbbmtx_code asc ";
			} else {
				size = itemCode.length();
				maxsize = size + 4;
				if(year==null||"".equals(year)){
					sql = "select t.*\n" +
					"  from cbm_c_itemtx t, cbm_c_item b\n" + 
					" where t.item_id = b.item_id(+)\n" + 
					"   and t.zbbmtx_code like '" + itemCode + "%'\n" + 
					"   and length(t.zbbmtx_code) > '" + size + "'\n" + 
					"   and length(t.zbbmtx_code) < '" + maxsize + "'\n" + 
					"   and t.is_use = 'Y'\n" + 
					" order by b.order_by,t.zbbmtx_code asc";
				}else{
					sql = "select * from cbm_c_itemtx t,\n"+
						"CBM_C_ITEM t2,CBM_C_CENTER_ITEM t3,\n " +
						"CBM_J_BUDGET_ITEM t4,CBM_J_BUDGET_MAKE t5 \n " +
						" where t.zbbmtx_code like'" + itemCode + "%'\n"+
						" and length(t.zbbmtx_code) > '" + size + "'"+
						" and length(t.zbbmtx_code) < '" + maxsize + "'"+
						"  AND t.item_id = t2.item_id \n" +
						"  AND t.item_id = t3.item_id \n" +
						"  AND t3.center_item_id = t4.center_item_id \n" +
						"  AND t4.budget_make_id = t5.budget_make_id \n" +
						"  AND t5.budget_time = '"+year+"' \n" +
						"  AND t.is_use = 'Y' \n" +
						"  AND t2.is_use = 'Y' \n" +
						"  AND t3.is_use = 'Y' \n" +
						"  AND t4.is_use = 'Y' \n" +
						"  AND t5.is_use = 'Y'\n"+
						" order by t2.order_by,t.zbbmtx_code asc ";
				}
			}
			}else{
				if (itemCode != null && itemCode.equals("000")) {
					sql = " select * from cbm_c_itemtx t where length(t.zbbmtx_code)=3 and is_use = 'Y'  order by t.zbbmtx_code asc ";
				} else {
					size = itemCode.length();
					maxsize = size + 4;
					sql = "select t.*\n" +
					"  from cbm_c_itemtx t, cbm_c_item b\n" + 
					" where t.item_id = b.item_id(+)\n" + 
					"   and t.zbbmtx_code like '" + itemCode + "%'\n" + 
					"   and length(t.zbbmtx_code) > '" + size + "'\n" + 
					"   and length(t.zbbmtx_code) < '" + maxsize + "'\n" + 
					"   and t.is_use = 'Y'\n" + 
					" order by b.order_by,t.zbbmtx_code asc";
				}
			}
		
		List<Object[]> list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			res = new ArrayList<TreeNode>();
			Integer count = 0;
			for (Object[] o : list) {
				TreeNode n = new TreeNode();
				n.setId(o[0].toString());
				if (o[1] != null) {
					n.setCode(o[1].toString());
					Integer size1 = o[1].toString().length();
					Integer maxSize1 =size1+4;
					String sqlcount = "select count(1) from cbm_c_itemtx t\n"
							+ " where t.zbbmtx_code like'" + o[1].toString()
							+ "%'\n" + " and length(t.zbbmtx_code) > " + size1
							+ "  and length(t.zbbmtx_code) < " + maxSize1 
							+ "  and is_use = 'Y'";
					count = Integer
							.parseInt(bll.getSingal(sqlcount).toString());
					if (count > 0) {
						n.setLeaf(false);
					} else {
						n.setLeaf(true);
					}
				}
				if (o[2] != null)
					n.setText(o[2].toString());
				if (o[3] != null)
					n.setOpenType(o[3].toString());
				String isItem = "N";
				if (o[4] != null)
					isItem = o[4].toString();
				n.setDescription(isItem);
				res.add(n);
			}
			return res;
		} else {
			return null;
		}
	}
	
	//add by fyyang 20100311
	@SuppressWarnings("unchecked")
	public List<TreeNode> findBudgetTreeListForWz(String itemCode,
			String enterpriseCode,String deptCode,String mrDept,String flag) {
		List<TreeNode> res = null;
		String sql;
		//1----需求计划审批 2---领料单审批 3---出库物资对账查询
	// add by sychen 20100511-------------------//
		String strWhere = "";
		String  st="";
		if (flag != null && !flag.equals("") && (flag.equals("1")||flag.equals("2"))) {
			if (mrDept != null && !mrDept.equals("")) {
				strWhere += " \n and   d.dep_code='"+mrDept+"' \n";
			}
		}
		else if(flag != null && !flag.equals("") &&flag.equals("3"))
		{
			
		}
		else if(flag == null || flag.equals("")||flag.equals("null"))
		{
			
		}
		else {
			strWhere += " \n and   d.dep_code='"+deptCode+"' \n"; 
		}	
	//add end--------------------------------------------//
		
		if (itemCode != null && itemCode.equals("000")) {
			sql = " select * from cbm_c_itemtx t where length(t.zbbmtx_code)=3 and is_use = 'Y'";
		} else {
			Integer size = itemCode.length();
			Integer maxsize = size + 4;
			sql = "select * from cbm_c_itemtx t\n"
					+ " where t.zbbmtx_code like'" + itemCode + "%'\n"
					+ " and length(t.zbbmtx_code) > '" + size + "'"
					+ " and length(t.zbbmtx_code) < '" + maxsize + "'"
					+ " and is_use = 'Y' \n";
		}
    // add by sychen 20100511-------------------//	
//		st+="select count(1)\n" +
//			"  from CBM_C_CENTER a, CBM_C_CENTER_TOPIC b, CBM_C_CENTER_ITEM c,cbm_c_itemtx d\n" + 
//			" where a.center_id = b.center_id\n" + 
//			"   and b.center_topic_id = c.center_topic_id\n" + 
//			"   and a.is_use='Y'\n" + 
//			"   and b.is_use='Y'\n" + 
//			"   and c.is_use='Y'\n" + 
//			"   and a.enterprise_code='"+enterpriseCode+"'\n" + 
//			"   and b.enterprise_code='"+enterpriseCode+"'\n" + 
//			"   and c.enterprise_code='"+enterpriseCode+"' \n"+
//			"and c.item_id=d.item_id\n" +
//			"and d.is_use='Y'\n" + 
//			"and d.enterprise_code='"+enterpriseCode+"' \n" +
//			"and d.zbbmtx_code like t.zbbmtx_code||'%'    ";
		//modify by wpzhu 20100608 ---------------------------------------------
    st+=
    	"select count(1)" +
    	" from CBM_J_BUDGET_MAKE a," +
    	"CBM_J_BUDGET_ITEM b,CBM_J_BUDGET_GATHER c,CBM_C_CENTER d,cbm_c_itemtx  e\n" +
    	"where a.budget_make_id=b.budget_make_id\n" + 
    	"and a.budget_gather_id=c.budget_gather_id\n" + 
    	"and a.center_id=d.center_id\n" + 
    	"and c.gather_status='2' " +
    	"and d.is_use='Y' \n" + 
    	"and e.is_use='Y' \n" + 
    	"and  a.enterprise_code='"+enterpriseCode+"'\n" + 
    	"and  b.enterprise_code='"+enterpriseCode+"'\n" + 
    	"and  c.enterprise_code='"+enterpriseCode+"'\n" + 
    	"and  d.enterprise_code='"+enterpriseCode+"'\n" + 
    	"and  e.enterprise_code='"+enterpriseCode+"'\n" + 
    	"and b.item_id=e.item_id\n" + 
    	"and e.zbbmtx_code like t.zbbmtx_code || '%'\n" ;

		st += strWhere;	
		
		sql+=
			" and( t.zbbmtx_code like '002000%'\n" +
			"    or t.zbbmtx_code like '002021%'\n" + 
			"    or t.zbbmtx_code like '002037%'\n" + //add by lding20100819 增2×125MW机组拆除
			"    or t.zbbmtx_code like '002001%')   \n";
		if(!"3".equals(flag))
		{
		 sql+=	" and  ("+st+")<>0    ";
		}
	//------------end ---------------------------------------------------------------------------------------------	
		//add end--------------------------------------------//
		
//		sql+=
//			" and( t.zbbmtx_code like '002000%'\n" +
//			"    or t.zbbmtx_code like '002021%'\n" + 
//			"    or t.zbbmtx_code like '002001%')   \n"+
//			" and  (select count(1)\n" +
//			"  from CBM_C_CENTER a, CBM_C_CENTER_TOPIC b, CBM_C_CENTER_ITEM c,cbm_c_itemtx d\n" + 
//			" where a.center_id = b.center_id\n" + 
//			"   and a.dep_code = '"+deptCode+"'\n" + 
//			"   and b.center_topic_id = c.center_topic_id\n" + 
//			"   and a.is_use='Y'\n" + 
//			"   and b.is_use='Y'\n" + 
//			"   and c.is_use='Y'\n" + 
//			"   and a.enterprise_code='"+enterpriseCode+"'\n" + 
//			"   and b.enterprise_code='"+enterpriseCode+"'\n" + 
//			"   and c.enterprise_code='"+enterpriseCode+"' \n"+
//			"and c.item_id=d.item_id\n" +
//			"and d.is_use='Y'\n" + 
//			"and d.enterprise_code='"+enterpriseCode+"' \n" +
//			"and d.zbbmtx_code like t.zbbmtx_code||'%'    )<>0    "+
//			"order by t.zbbmtx_code";

		sql+="\n order by t.zbbmtx_code";
//     System.out.println("the sql"+sql);
		List<Object[]> list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			res = new ArrayList<TreeNode>();
			Integer count = 0;
			for (Object[] o : list) {
				TreeNode n = new TreeNode();
				n.setId(o[0].toString());
				if (o[1] != null) {
					n.setCode(o[1].toString());
					Integer size1 = o[1].toString().length();
					//add by sychen 20100511--------------------//
//					String stcount="select count(1)\n" +
//						"  from CBM_C_CENTER a, CBM_C_CENTER_TOPIC b, CBM_C_CENTER_ITEM c,cbm_c_itemtx d\n" + 
//						" where a.center_id = b.center_id\n" + 
//						"   and b.center_topic_id = c.center_topic_id\n" + 
//						"   and a.is_use='Y'\n" + 
//						"   and b.is_use='Y'\n" + 
//						"   and c.is_use='Y'\n" + 
//						"   and a.enterprise_code='"+enterpriseCode+"'\n" + 
//						"   and b.enterprise_code='"+enterpriseCode+"'\n" + 
//						"   and c.enterprise_code='"+enterpriseCode+"' \n"+
//						"and c.item_id=d.item_id\n" +
//						"and d.is_use='Y'\n" + 
//						"and d.enterprise_code='"+enterpriseCode+"' \n" +
//						"and d.zbbmtx_code like t.zbbmtx_code||'%'   ";
                    String stcount="select count(1)" +
                	" from CBM_J_BUDGET_MAKE a," +
                	"CBM_J_BUDGET_ITEM b,CBM_J_BUDGET_GATHER c,CBM_C_CENTER d,cbm_c_itemtx  e\n" +
                	"where a.budget_make_id=b.budget_make_id\n" + 
                	"and a.budget_gather_id=c.budget_gather_id\n" + 
                	"and a.center_id=d.center_id\n" + 
                	"and c.gather_status='2' " +
                	"and d.is_use='Y' \n" + 
                	"and e.is_use='Y' \n" + 
                	"and  a.enterprise_code='"+enterpriseCode+"'\n" + 
                	"and  b.enterprise_code='"+enterpriseCode+"'\n" + 
                	"and  c.enterprise_code='"+enterpriseCode+"'\n" + 
                	"and  d.enterprise_code='"+enterpriseCode+"'\n" + 
                	"and  e.enterprise_code='"+enterpriseCode+"'\n" + 
                	"and b.item_id=e.item_id\n" + 
                	"and e.zbbmtx_code like t.zbbmtx_code || '%'\n" ;

					stcount += strWhere;
					
					String sqlcount = "select count(1) from cbm_c_itemtx t\n"
						+ " where t.zbbmtx_code like'" + o[1].toString()
						+ "%'\n" + " and length(t.zbbmtx_code) > '" + size1
						+ "'" + " and is_use = 'Y'"
						+" and( t.zbbmtx_code like '002000%'\n" +
						"    or t.zbbmtx_code like '002021%'\n" + 
						"    or t.zbbmtx_code like '002037%'\n" + //add by lding20100819 增2×125MW机组拆除
						"    or t.zbbmtx_code like '002001%')   \n"+
						" and  ("+stcount+" )<>0    \n";
				//add end--------------------------------------------//
					
//					String sqlcount = "select count(1) from cbm_c_itemtx t\n"
//							+ " where t.zbbmtx_code like'" + o[1].toString()
//							+ "%'\n" + " and length(t.zbbmtx_code) > '" + size1
//							+ "'" + " and is_use = 'Y'"
//							+" and( t.zbbmtx_code like '002000%'\n" +
//							"    or t.zbbmtx_code like '002021%'\n" + 
//							"    or t.zbbmtx_code like '002001%')   \n"+
//							" and  (select count(1)\n" +
//							"  from CBM_C_CENTER a, CBM_C_CENTER_TOPIC b, CBM_C_CENTER_ITEM c,cbm_c_itemtx d\n" + 
//							" where a.center_id = b.center_id\n" + 
////							"   and a.dep_code = '"+deptCode+"'\n" + 
//							"   and b.center_topic_id = c.center_topic_id\n" + 
//							"   and a.is_use='Y'\n" + 
//							"   and b.is_use='Y'\n" + 
//							"   and c.is_use='Y'\n" + 
//							"   and a.enterprise_code='"+enterpriseCode+"'\n" + 
//							"   and b.enterprise_code='"+enterpriseCode+"'\n" + 
//							"   and c.enterprise_code='"+enterpriseCode+"' \n"+
//							"and c.item_id=d.item_id\n" +
//							"and d.is_use='Y'\n" + 
//							"and d.enterprise_code='"+enterpriseCode+"' \n" +
//							"and d.zbbmtx_code like t.zbbmtx_code||'%'    )<>0    \n";
							
					count = Integer
							.parseInt(bll.getSingal(sqlcount).toString());
					if (count > 0) {
						n.setLeaf(false);
					} else {
						n.setLeaf(true);
					}
				}
				if (o[2] != null)
					n.setText(o[2].toString());
				if (o[3] != null)
					n.setOpenType(o[3].toString());
				String isItem = "N";
				if (o[4] != null)
					isItem = o[4].toString();
				n.setDescription(isItem);
				res.add(n);
			}
			return res;
		} else {
			return null;
		}
	}
	
	//add by fyyang 20100311 通过指标编码获得指标名称
	public String getItemNameByCode(String itemCode,String enterpriseCode,String  deptCode)
	{ 
		String subitemCode=itemCode;
		if(itemCode.length()>=15){
		 subitemCode = itemCode.substring(0,15);
		if (subitemCode.equals("002021007003015")||subitemCode.equals("002021007003001"))
			subitemCode="002021007003001"; //材料费修理费下的子节点均取材料费预算
		
		else if(itemCode.length()>=12){
			 subitemCode = itemCode.substring(0,12);
				if (subitemCode.equals("002021001007"))
					subitemCode="002021001007"; //化学药品费下的子节点均取化学药品费预算
		}else
			subitemCode=itemCode;
		}
		
//		if(itemCode.length()>=12){
//			 subitemCode = itemCode.substring(0,12);
//			if (subitemCode.equals("002021001007"))
//				subitemCode="002021001007"; //化学药品费下的子节点均取化学药品费预算
//			
//			else
//				subitemCode=itemCode;
//			}
		
		String sql=
			"select (select w.zbbmtx_name from cbm_c_itemtx w where w.zbbmtx_code='"+itemCode+"' and w.is_use='Y') zbbmtx_name," +
            " (nvl(m.ensure_budget,0)+nvl(m.budget_add,0)) * 10000,"+//update by sychen 20100623
//			"nvl(m.advice_budget, 0)*10000, " +
			"nvl(m.fact_happen, 0)*10000\n" +
			"       from cbm_c_itemtx t,\n" + 
			"            (select b.advice_budget, " +
			"                    b.budget_add,"+// add by sychen 20100623
			"                    b.fact_happen, b.item_id,b.ensure_budget\n" + 
			"               from CBM_J_BUDGET_MAKE a,\n" + 
			"                    CBM_J_BUDGET_ITEM b,\n" + 
			"                    CBM_C_CENTER      c\n" + 
			"              where a.budget_make_id = b.budget_make_id\n" + 
			"                and a.center_id = c.center_id\n" + 
			"                and c.dep_code = '"+deptCode+"'\n" + 
			"                and a.budget_time = to_char(sysdate,'yyyy')\n" + 
			"                and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"                and b.enterprise_code = '"+enterpriseCode+"'\n" + 
			"                and c.is_use='Y'\n" + 
			"                and c.enterprise_code='"+enterpriseCode+"'\n" + 
			"                ) m\n" + 
			"      where t.item_id = m.item_id(+)\n" + 
			"        and t.zbbmtx_code = '"+subitemCode+"'\n" + 
			"        and t.is_use = 'Y'\n" + 
			"        and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"        and rownum = 1";
		
		Object [] obj=(Object [])bll.getSingal(sql);
		if(obj!=null&&obj.length>0)
		{
			String data=obj[0].toString()+","+obj[1].toString()+","+obj[2].toString();
			return data;
			
		}
		else
		{
			return itemCode+",0,0";
		}
		

	}
/**
 * 功能：指标预算值和实际值统计查询
 * add by qxjiao 20100907
 */
	public List findBudgetCount(String year ,String id) {
		String sql =
			/*"SELECT t2.item_id,\n" +
			"       t2.item_name,\n" + 
			"       getunitname(t2.unit_code) AS unit_name,\n" + 
			"       t2.item_explain AS ys_formula,\n" + 
			"       t2.fact_explain AS sj_formula,\n" + 
			"       nvl(t2.firstclass_value, 0),\n" + 
			"       nvl(t2.create_value, 0),\n" + 
			"       nvl((SELECT SUM(a.trans_qty * a.std_cost)\n" + 
			"             FROM inv_j_transaction_his a,\n" + 
			"                  inv_j_issue_details   b,\n" + 
			"                  inv_j_issue_head      c\n" + 
			"            WHERE a.order_no = c.issue_no\n" + 
			"              AND c.issue_head_id = b.issue_head_id\n" + 
			"              AND c.issue_status = '2'\n" + 
			"              AND c.item_code LIKE '||t2.item_code||%'\n" + 
			"              AND a.is_use = 'Y'\n" + 
			"              AND b.is_use = 'Y'\n" + 
			"              AND c.is_use = 'Y'\n" + 
			"              AND to_char(a.last_modified_date, 'yyyy') = '"+year+"'\n" + 
			"            GROUP BY c.item_code), 0) AS ll_fee,\n" + 
			"       nvl((SELECT SUM(con.act_amount)\n" + 
			"             FROM con_j_contract_info con\n" + 
			"            WHERE con.con_type_id = 2\n" + 
			"              AND con.item_id LIKE '||t2.item_code||%'\n" + 
			"              AND con.workflow_status = 2\n" + 
			"              AND con.is_use = 'Y'\n" + 
			"              AND con.con_year='"+year+"'\n" + 
			"            GROUP BY con.item_id), 0) AS contract_fee,\n" + 
			"       nvl((SELECT t7.advice_budget\n" + 
			"             FROM CBM_C_CENTER_ITEM t5,\n" + 
			"                  CBM_J_BUDGET_MAKE t6,\n" + 
			"                  CBM_J_BUDGET_ITEM t7\n" + 
			"            WHERE t5.center_item_id = t7.center_item_id\n" + 
			"              AND t6.budget_time = '"+year+"'\n" + 
			"              AND t5.item_id = t2.item_id\n" + 
			"              AND t6.budget_make_id = t7.budget_make_id\n" + 
			"              AND t6.is_use = 'Y'\n" + 
			"              AND t7.is_use = 'Y'\n" + 
			"              AND rownum = 1), 0) AS wq_value,\n" + 
			"       nvl(a.money, 0) AS ww_fee,\n" + 
			"       nvl(b.report_money, 0) AS zb_fee,\n" + 
			"       t2.item_code\n" + 
			"  FROM CBM_C_ITEMTX t1,\n" + 
			"       CBM_C_ITEM t2,\n" + 
			"       CBM_C_CENTER_ITEM t5,\n" + 
			"       CBM_J_BUDGET_MAKE t6,\n" + 
			"       CBM_J_BUDGET_ITEM t7,\n" + 
			"       (SELECT t8.item_id,\n" + 
			"               SUM(t8.estimate_money) money\n" + 
			"          FROM CBM_J_ASSIGNED_FILL t8\n" + 
			"         WHERE t8.is_use = 'Y'\n" + 
			"           AND t8.work_flow_status not in ('0','6')\n" + 
			"           AND to_char(t8.apply_date, 'yyyy') = '"+year+"'\n" + 
			"         GROUP BY t8.item_id) a,\n" + 
			"       (SELECT t9.item_id,\n" + 
			"               SUM(t9.report_money_lower) report_money\n" + 
			"          FROM CBM_J_COST_REPORT t9\n" + 
			"         WHERE t9.is_use = 'Y'\n" + 
			"           AND t9.work_flow_status not in ('0','10')\n" + 
			"           AND to_char(t9.report_date, 'yyyy') = '"+year+"'\n" + 
			"         GROUP BY t9.item_id) b\n" + 
			" WHERE t1.zbbmtx_id = '"+id+"'\n" + 
			"   AND t1.item_id = t2.item_id\n" + 
			"   AND t1.item_id = t5.item_id\n" + 
			"   AND t5.center_item_id = t7.center_item_id\n" + 
			"   AND t6.budget_make_id = t7.budget_make_id\n" + 
			"   AND t1.is_use = 'Y'\n" + 
			"   AND t2.is_use = 'Y'\n" + 
			"   AND t5.is_use = 'Y'\n" + 
			"   AND t6.is_use = 'Y'\n" + 
			"   AND t7.is_use = 'Y'\n" + 
			"   AND t2.item_id = a.item_id(+)\n" + 
			"   AND t2.item_id = b.item_id(+)";*/
		"SELECT t2.item_id,\n" +
		"       t2.item_name,\n" + 
		"       getunitname(t2.unit_code) AS unit_name,\n" + 
		"       t2.item_explain AS ys_formula,\n" + 
		"       t2.fact_explain AS sj_formula,\n" + 
		"       nvl(t2.firstclass_value, 0),\n" + 
		"       nvl(t2.create_value, 0),\n" + 
		"       nvl((SELECT SUM(a.trans_qty * a.std_cost)\n" + 
		"             FROM inv_j_transaction_his a,\n" + 
		"                  inv_j_issue_details   b,\n" + 
		"                  inv_j_issue_head      c\n" + 
		"            WHERE a.order_no = c.issue_no\n" + 
		"              AND c.issue_head_id = b.issue_head_id\n" + 
		"              AND c.issue_status = '2'\n" + 
		"              AND c.item_code LIKE '||t2.item_code||%'\n" + 
		"              AND a.is_use = 'Y'\n" + 
		"              AND b.is_use = 'Y'\n" + 
		"              AND c.is_use = 'Y'\n" + 
		"              AND to_char(a.last_modified_date, 'yyyy') = '"+year+"'\n" + 
		"            GROUP BY c.item_code), 0) AS ll_fee,\n" + 
		"       nvl((SELECT SUM(con.act_amount)\n" + 
		"             FROM con_j_contract_info con\n" + 
		"            WHERE con.con_type_id = 2\n" + 
		"              AND con.item_id LIKE '||t2.item_code||%'\n" + 
		"              AND con.workflow_status = 2\n" + 
		"              AND con.is_use = 'Y'\n" + 
		"              AND con.con_year = '"+year+"'\n" + 
		"            GROUP BY con.item_id), 0) AS contract_fee,\n" + 
		"       nvl((SELECT t7.advice_budget\n" + 
		"             FROM CBM_C_CENTER_ITEM t5,\n" + 
		"                  CBM_J_BUDGET_MAKE t6,\n" + 
		"                  CBM_J_BUDGET_ITEM t7\n" + 
		"            WHERE t5.center_item_id = t7.center_item_id\n" + 
		"              AND t6.budget_time = '"+year+"'\n" + 
		"              AND t5.item_id = t2.item_id\n" + 
		"              AND t6.budget_make_id = t7.budget_make_id\n" + 
		"              AND t6.is_use = 'Y'\n" + 
		"              AND t7.is_use = 'Y'\n" + 
		"              AND rownum = 1), 0) AS wq_value,\n" + 

		"nvl((SELECT SUM(t8.estimate_money)\n" +
		"          FROM CBM_J_ASSIGNED_FILL t8,\n" + 
		"               CBM_C_ITEMTX          it\n" + 
		"         WHERE t8.item_id = it.item_id\n" + 
		"           AND it.zbbmtx_code LIKE t1.zbbmtx_code||'%'\n" + 
		"           AND t8.is_use = 'Y'\n" + 
		"           AND t8.work_flow_status NOT IN ('0', '6')\n" + 
		"           AND to_char(t8.apply_date, 'yyyy') = '2010'\n" +
		"			AND t8.item_id IN (SELECT c.item_id\n" +
		"                                  FROM CBM_J_BUDGET_MAKE a,\n" + 
		"                                       CBM_J_BUDGET_ITEM b,\n" + 
		"                                       CBM_C_CENTER_ITEM c\n" + 
		"                                 WHERE a.budget_make_id = b.budget_make_id\n" + 
		"                                   AND b.center_item_id = c.center_item_id\n" + 
		"                                   AND a.budget_time = '2010'\n" + 
		"                                   AND a.is_use = 'Y'\n" + 
		"                                   AND b.is_use = 'Y'\n" + 
		"                                   AND c.is_use = 'Y')\n"+
		"         ),0) AS ww_fee,\n" + 


		"nvl((SELECT SUM(t9.report_money_lower)\n" +
		"          FROM CBM_J_COST_REPORT t9,\n" + 
		"               CBM_C_ITEMTX        it\n" + 
		"         WHERE t9.item_id = it.item_id\n" + 
		"           AND it.zbbmtx_code LIKE t1.zbbmtx_code||'%'\n" + 
		"           AND t9.is_use = 'Y'\n" + 
		"           AND t9.work_flow_status NOT IN ('0', '10')\n" + 
		"           AND to_char(t9.report_date, 'yyyy') = '2010'\n" + 
		"         ),0) AS zb_fee,\n"+
		"       t2.item_code\n" + 
		"  FROM CBM_C_ITEMTX      t1,\n" + 
		"       CBM_C_ITEM        t2,\n" + 
		"       CBM_C_CENTER_ITEM t5,\n" + 
		"       CBM_J_BUDGET_MAKE t6,\n" + 
		"       CBM_J_BUDGET_ITEM t7\n" + 
		" WHERE t1.zbbmtx_id = '"+id+"'\n" + 
		"   AND t1.item_id = t2.item_id\n" + 
		"   AND t1.item_id = t5.item_id\n" + 
		"   AND t5.center_item_id = t7.center_item_id\n" + 
		"   AND t6.budget_make_id = t7.budget_make_id\n" + 
		"   AND t1.is_use = 'Y'\n" + 
		"   AND t2.is_use = 'Y'\n" + 
		"   AND t5.is_use = 'Y'\n" + 
		"   AND t6.is_use = 'Y'\n" + 
		"   AND t7.is_use = 'Y'";


		List res = bll.queryByNativeSQL(sql);
		return res;
	}
/**
 * 合同费用明细查询
 * add by qxjiao 20100908
 */
public PageObject findContractFeeDetail(String itemCode, String startTime,String endTime,
		String contractName,int start,int limit) {
	String sql = "SELECT t.conttrees_no, \n"+
						"  t.contract_name, \n"+
						"  t.act_amount, \n"+
						" (select a.item_name from CBM_C_ITEM a where a.item_code = t.item_id) as item_name, \n"+
						"  GETWORKERNAME(t.operate_by), \n"+
						"  t.start_date, \n"+
						" t.end_date \n"+
						" FROM CON_J_CONTRACT_INFO t \n"+
						" WHERE t.con_type_id = 2 \n"+
						"  AND t.item_id = '"+itemCode+"' \n"+
						"  AND t.workflow_status = 2 \n"+
						"  AND t.is_use = 'Y'";
	if(contractName!=null&&!"".equals(contractName)){
		sql +="  AND t.contract_name like '%"+contractName+"%'";
	}
	if(startTime!=null&&!"".equals(startTime)){
		sql +=" AND to_char(t.start_date,'yyyy-MM-dd') >='"+startTime+"'";
	}if(endTime!=null&&!"".equals(endTime)){
		sql +=" AND to_char(t.end_date,'yyyy-MM-dd') <='"+endTime+"'";
	}
	
	String sqlCount = "select count(*) from CON_J_CONTRACT_INFO t" +
										"  WHERE t.con_type_id = 2 \n"+
										"  AND t.item_id = '"+itemCode+"' \n"+
										"  AND t.workflow_status = 2 \n"+
										"  AND t.is_use = 'Y'";
	sql +=" order by t.start_date desc";
	List result = bll.queryByNativeSQL(sql, start,limit);
	Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
	PageObject obj = new PageObject();
	obj.setList(result);
	obj.setTotalCount(count);
	return obj;
}
/**
 * 物料费用明细查询
 * add by qxjiao 20100908
 */
public PageObject findLlFeeDetail(String itemCode, String startTime,String endTime, String wlName,int start,int limit) {
	String sql = " SELECT DISTINCT a.trans_his_id, \n"+
					"  a.order_no, \n"+
					"  (a.trans_qty * a.std_cost) AS ll_fee, \n"+
					"  d.material_no, \n"+
					"  d.material_name, \n"+
					"  d.spec_no, \n"+
					"   b.applied_count, \n"+
					"     b.act_issued_count, \n"+
					"  (SELECT t.item_name \n"+
					"    FROM CBM_C_ITEM t \n"+
					"   WHERE t.item_code = c.item_code) AS item_name, \n"+
					"  GETWORKERNAME(a.last_modified_by), \n"+
					" a.last_modified_date \n"+
					" FROM inv_j_transaction_his a, \n"+
					" inv_j_issue_details   b, \n"+
					" inv_j_issue_head      c, \n"+
					" INV_C_MATERIAL        d ";
	String sqlWhere = " WHERE a.order_no = c.issue_no \n"+
						" AND c.issue_head_id = b.issue_head_id \n"+
						" AND b.material_id = d.material_id \n"+
						" AND c.issue_status = '2' \n"+
						" AND c.item_code = '"+itemCode+"' \n" +
						" AND a.is_use = 'Y' \n"+
						" AND b.is_use = 'Y' \n"+
						" AND c.is_use = 'Y'" ;
	String sqlCount = "select count(*) from inv_j_transaction_his a, \n"+
												" inv_j_issue_details   b, \n"+
												" inv_j_issue_head      c, \n"+
												" INV_C_MATERIAL        d ";
	if(wlName!=null&&!"".equals(wlName)){
		sqlWhere +=" AND d.material_name like '%"+wlName+"%'" ;
	}
	if(startTime!=null&&!"".equals(startTime)){
		sqlWhere +=" AND to_char(a.last_modified_date,'yyyy-MM-dd') >='"+startTime+"'";
	}if(endTime!=null&&!"".equals(endTime)){
		sqlWhere +=" AND to_char(a.last_modified_date,'yyyy-MM-dd') <='"+endTime+"'";
	}
	Long count = Long.parseLong(bll.getSingal(sqlCount+sqlWhere).toString());
	List result = bll.queryByNativeSQL(sql+sqlWhere+" order by a.last_modified_date desc", start,limit);
	
	PageObject obj = new PageObject();
	obj.setList(result);
	obj.setTotalCount(count);
	return obj;
}
/**
 * 外委费用明细查询
 * add by qxjiao 20100908
 */
public PageObject findWWFeeDetail(String itemId, String wwName, String approver,
		 String startTime,String endTime,int start,int limit) {
	String sql = " SELECT t.assign_name, \n"+
						"  (SELECT a.item_name \n"+
						" FROM CBM_C_ITEM a \n"+
						"  WHERE a.item_id = t.item_id) AS item_name, \n"+
						"   t.estimate_money, \n"+
						"  t.assign_function, \n"+
						"  GETWORKERNAME(t.apply_by),\n" +
						"  t.work_flow_no \n"+
						" FROM CBM_J_ASSIGNED_FILL t ";
	String sqlWhere = " WHERE t.item_id = '"+itemId+"' and t.work_flow_status = '5'";
	if(wwName!=null&&!"".equals(wwName)){
		sqlWhere += " and t.assign_name like '%"+wwName+"%'" ;
	}
	if(approver!=null&&!"".equals(approver)){
		sqlWhere += " and GETWORKERNAME(t.apply_by) like '%"+approver+"%'";
	}
	if(startTime!=null&&!"".equals(startTime)){
		sqlWhere +=" AND to_char(t.APPLY_DATE,'yyyy-MM-dd') >='"+startTime+"'";
	}if(endTime!=null&&!"".equals(endTime)){
		sqlWhere +=" AND to_char(t.APPLY_DATE,'yyyy-MM-dd') <='"+endTime+"'";
	}
	
	String sqlCount = "select count(*) from CBM_J_ASSIGNED_FILL t";
	sql = sql + sqlWhere + " order by t.apply_date desc";
	List result = bll.queryByNativeSQL(sql, start,limit);
	Long count = Long.parseLong(bll.getSingal(sqlCount+sqlWhere).toString());
	PageObject obj = new PageObject();
	obj.setList(result);
	obj.setTotalCount(count);
	return obj;
}
/**
 * 费用直报明细查询
 * add by qxjiao 20100908
 */
public PageObject findZbFeeDetail(String itemId, String reportBy,  String startTime,String endTime,int start,int limit) {
	String sql = " SELECT t.report_money_lower, \n"+
							" t.report_use, \n"+
							" t.memo, \n"+
							" GETWORKERNAME(t.report_by), \n"+
							" to_char(t.report_date,'yyyy-MM-dd'), \n"+
							" t.work_flow_status, \n" +
							" t.work_flow_no \n"+
							" FROM CBM_J_COST_REPORT t ";
	String sqlWhere = " where t.item_id = '"+itemId+"' and t.work_flow_status = '9'";
	if(reportBy!=null&&!"".equals(reportBy)){
		sqlWhere +=" and GETWORKERNAME(t.report_by) like '%"+reportBy+"%'";
	}
	if(startTime!=null&&!"".equals(startTime)){
		sqlWhere +=" AND to_char(t.report_date,'yyyy-MM-dd') >='"+startTime+"'";
	}if(endTime!=null&&!"".equals(endTime)){
		sqlWhere +=" AND to_char(t.report_date,'yyyy-MM-dd') <='"+endTime+"'";
	}
	String sqlCount = "select count(*) from CBM_J_COST_REPORT t ";
	sql = sql+sqlWhere +" order by t.report_date desc";
	List result = bll.queryByNativeSQL(sql, start,limit);
	Long count = Long.parseLong(bll.getSingal(sqlCount+sqlWhere).toString());
	
	PageObject obj = new PageObject();
	obj.setList(result);
	obj.setTotalCount(count);
	return obj;
}
}
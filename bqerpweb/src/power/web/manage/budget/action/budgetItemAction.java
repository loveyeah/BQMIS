package power.web.manage.budget.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.manage.budget.CbmCItem;
import power.ejb.manage.budget.CbmCItemFacadeRemote;
import power.ejb.manage.budget.CbmCItemtx;
import power.ejb.manage.budget.CbmCItemtxFacadeRemote;
import power.ejb.manage.budget.CbmJBudgetItemFacadeRemote;
import power.ejb.manage.budget.form.CbmItemForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class budgetItemAction extends AbstractAction {
	CbmCItemFacadeRemote itemRemote;
	CbmCItemtxFacadeRemote itemtxRemote;
	CbmJBudgetItemFacadeRemote remote;
	private CbmCItem bubgetItem;
	// 财务对应
	private Long financeItemId;
	// 指标体系名称(无)
	private String zbbmtxName;
	// 父指标编码
	private String itemFCode;
	// 有无数据
	private String isItem;
	// 借方贷方
	private String debitCredit;
	// 体系id
	private Long systemId;

	private String method;

	public budgetItemAction() {
		itemRemote = (CbmCItemFacadeRemote) factory
				.getFacadeRemote("CbmCItemFacade");
		itemtxRemote = (CbmCItemtxFacadeRemote) factory
				.getFacadeRemote("CbmCItemtxFacade");
		remote = (CbmJBudgetItemFacadeRemote) factory
				.getFacadeRemote("CbmJBudgetItemFacade");
	}

	// 保存修改预算指标
	public void saveOrupdateBudgetItem() {
		String code = itemtxRemote.creatCode(itemFCode);
		CbmCItemtx itemtx = new CbmCItemtx();
		CbmCItem item = new CbmCItem();
		if (isItem.equals("N")) {
			if (method.equals("add")) {
				itemtx.setEnterpriseCode(employee.getEnterpriseCode());
				itemtx.setZbbmtxCode(code);
				itemtx.setZbbmtxName(zbbmtxName);
				itemtx.setIsItem("N");
				itemtx=itemtxRemote.save(itemtx);
			} else {
				systemId = Long.parseLong(request.getParameter("systemId"));
				itemtx = itemtxRemote.findByItemtxId(systemId);
				itemtx.setZbbmtxName(zbbmtxName);
				itemtx=itemtxRemote.update(itemtx);
			}
		} else {
			if (method.equals("add")) {
				boolean ifitem = itemRemote
						.findByCode(bubgetItem.getItemCode());
				if (ifitem) {
					write("{failure:true,msg : '该指标已经存在！' }");
				} else {
					bubgetItem.setEnterpriseCode(employee.getEnterpriseCode());
					item = itemRemote.save(bubgetItem);
					itemtx.setItemId(item.getItemId());
					itemtx.setIsItem("Y");
					itemtx.setEnterpriseCode(employee.getEnterpriseCode());
					itemtx.setZbbmtxName(item.getItemName());
					itemtx.setZbbmtxCode(code);
					itemtx=itemtxRemote.save(itemtx);
				}
			} else {
				Long itemId = Long.parseLong(request.getParameter("itemId"));
				item = itemRemote.findById(itemId);
				if (item != null) {
					bubgetItem.setItemId(itemId);
					bubgetItem.setIsUse(item.getIsUse());
					bubgetItem.setAccountOrder(item.getAccountOrder());
					bubgetItem.setFactOrder(item.getFactOrder());
					bubgetItem.setEnterpriseCode(item.getEnterpriseCode());
					bubgetItem=itemRemote.update(bubgetItem);
				}
			}
		}
		write("{success:true,id:'"+itemtx.getZbbmtxId()+"'}");
	}

	// 删除预算指标
	public void deleteBudgetItem() {
		Long id = Long.parseLong(request.getParameter("id"));
		CbmCItemtx model = itemtxRemote.findByItemtxId(id);
		itemtxRemote.delete(model);
	}

	// 指标树
	public void findBudgetTree() {
		String pid = request.getParameter("pid");
		String year = request.getParameter("year");
		try {
			List<TreeNode> list = itemtxRemote.findBudgetTreeList(year,pid, employee
					.getEnterpriseCode());
			//System.out.println(JSONUtil.serialize(list));
			write(JSONUtil.serialize(list));
		} catch (Exception exc) {
			exc.printStackTrace();
			write("[]");
		}
	}

	// 预算指树标公用
	public void getBudgetItemtxInfo() throws JSONException {
		if (request.getParameter("id").equals("ynode-7")) {
			write("{[]}");
		} else {
			Long id = Long.parseLong(request.getParameter("id"));
			List<CbmCItemtx> itemtx = itemtxRemote.findById(id);
			if (itemtx.size() > 0) {
				write(JSONUtil.serialize(itemtx));
			} else {
				write("{[]}");
			}
		}
	}

	// 预算指标
	public void getbudgetItemInfo() throws JSONException {
		String id = request.getParameter("id");
		List<CbmItemForm> itemtx = itemRemote.findByItemId(id);
		if (itemtx.size() > 0) {
			write(JSONUtil.serialize(itemtx));
		} else {
			write("{[]}");
		}
	}

	/**
	 * 获得计算等级
	 */
	public void geAccountorFactOrder() throws JSONException {
		Long id = null;
		if (request.getParameter("itemId") != null) {
			id = Long.parseLong(request.getParameter("itemId"));
		}
		CbmCItem model = itemRemote.findById(id);
		// 预算公式等级
		Long accountOrder = itemRemote.getaccountOrder(id);
		// 实际公式等级
		Long factOrder = itemRemote.getfactOrder(id);
		model.setAccountOrder(accountOrder);
		model.setFactOrder(factOrder);
		itemRemote.update(model);

	}
	
	
	/**
	 * 物资模块费用来源用到的指标树
	 * add by fyyang 20100311
	 */
	public void findBudgetTreeForWz() {
		String pid = request.getParameter("pid");
		//增加申请人部门、参数flag add by sychen 20100511
		String mrDept = request.getParameter("mrDept");
		String flag = request.getParameter("flag");
		
		try {
			List<TreeNode> list = itemtxRemote.findBudgetTreeListForWz(pid, employee
					.getEnterpriseCode(),employee.getDeptCode(),mrDept,flag);
		//	System.out.println(JSONUtil.serialize(list));
			write(JSONUtil.serialize(list));
		} catch (Exception exc) {
			exc.printStackTrace();
			write("[]");
		}
	}
	
	/**
	 * 物资模块费用来源（得到指标名称和年预算及实际费用）
	 * add by fyyang 20100311
	 */
	public void getItemNameByItemCode()
	{

		String itemCode = request.getParameter("itemCode");
		String deptCode = request.getParameter("deptCode");
		String data=itemtxRemote.getItemNameByCode(itemCode, employee.getEnterpriseCode(),deptCode);
	
		write(data);
		
	}
	/**
	 * 指标统计查询
	 * add by qxjiao 20100907
	 */
	public void findBudgetCount(){
		String id = request.getParameter("id");
		String year = request.getParameter("year");
		List<Object[]> objList = itemtxRemote.findBudgetCount(year,id);
		Object[] obj  = null;
		if(objList.size()>0){
			obj = objList.get(0);
			StringBuffer result = new StringBuffer();
			result.append("{itemId:'").append(obj[0]==null?"":obj[0]).append("',itemName:'").append(obj[1]==null?"":obj[1]).append("',unitName:'")
					.append(obj[2]==null?"":obj[2]).append("',ysFormula:'").append(obj[3]==null?"":obj[3]).append("',sjFormula:'")
					.append(obj[4]==null?"":obj[4]).append("',fValue:'").append(obj[5]==null?"":obj[5])
					.append("',cValue:'").append(obj[6]==null?"":obj[6]).append("',llFee:'").append(obj[7]==null?"":obj[7]).append("',contractFee:'")
					.append(obj[8]==null?"":obj[8]).append("',wqValue:'").append(obj[9]==null?"":obj[9]).append("',wwFee:'").append(obj[10]==null?"":obj[10])
					.append("',zbFee:'").append(obj[11]==null?"":obj[11]).append("',itemCode:'").append(obj[12]==null?"":obj[12]).append("'}");
					write(result.toString());
		}else{
			write("{status:'非预算指标！'}");
		}
	}
	/**
	 * 指标统计查询下费用明细查询
	 * add by qxjiao 20100907
	 */
	public void findFeeDetail(){
		String flag = request.getParameter("flag");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		PageObject obj = null;
		if(flag.equals("ll")){//领料费用
			String itemCode = request.getParameter("itemCode");
			String wlName = request.getParameter("wlName");
			obj = itemtxRemote.findLlFeeDetail(itemCode, startTime, endTime,wlName,start, limit);
		}else if(flag.equals("contract")){//合同费用
			String itemCode = request.getParameter("itemCode");
			String contractName = request.getParameter("contractName");
			obj = itemtxRemote.findContractFeeDetail(itemCode, startTime, endTime, contractName, start, limit);
			
		}else if(flag.equals("ww")){//外委费用
			String itemId = request.getParameter("itemId");
			String wwName = request.getParameter("wwName");
			String applyBy = request.getParameter("applyBy");
			obj = itemtxRemote.findWWFeeDetail(itemId, wwName, applyBy, startTime, endTime, start, limit);
			
		}else if(flag.equals("zb")){//费用直报
			String itemId = request.getParameter("itemId");
			String reportBy = request.getParameter("reportBy");
			obj = itemtxRemote.findZbFeeDetail(itemId, reportBy, startTime, endTime, start, limit);
		}
			try {
				write(JSONUtil.serialize(obj));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	public Long getFinanceItemId() {
		return financeItemId;
	}

	public void setFinanceItemId(Long financeItemId) {
		this.financeItemId = financeItemId;
	}

	public String getZbbmtxName() {
		return zbbmtxName;
	}

	public void setZbbmtxName(String zbbmtxName) {
		this.zbbmtxName = zbbmtxName;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getItemFCode() {
		return itemFCode;
	}

	public void setItemFCode(String itemFCode) {
		this.itemFCode = itemFCode;
	}

	public String getIsItem() {
		return isItem;
	}

	public void setIsItem(String isItem) {
		this.isItem = isItem;
	}

	public CbmCItem getBubgetItem() {
		return bubgetItem;
	}

	public void setBubgetItem(CbmCItem bubgetItem) {
		this.bubgetItem = bubgetItem;
	}

	public String getDebitCredit() {
		return debitCredit;
	}

	public void setDebitCredit(String debitCredit) {
		this.debitCredit = debitCredit;
	}
}

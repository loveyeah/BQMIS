/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.resource.quotation.action;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.resource.MrpJPlanGather;
import power.ejb.resource.MrpJPlanGatherFacadeRemote;
import power.ejb.resource.PurJQuotation;
import power.ejb.resource.business.PurJQuotationQuery;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 物料询价管理的Action
 * @author sufeiyu
 * 
 */
public class QuotationAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	// 获得当前用户ID
	Employee employee;
	PurJQuotation quotation;
	String enterpriseCode;
	
	private PurJQuotationQuery remote;
	// modify BY ywliu 09/04/29
	private MrpJPlanGatherFacadeRemote gatherRemeto;

	public QuotationAction() {
		remote = (PurJQuotationQuery) factory
				.getFacadeRemote("PurJQuotationQueryImp");
		// modify BY ywliu 09/04/29
		gatherRemeto = (MrpJPlanGatherFacadeRemote) factory.getFacadeRemote("MrpJPlanGatherFacade");
	}

	/**
	 * 取得物料询价管理grid的数据
	 */
	public void getQuotationList() {
		employee =(Employee) session.getAttribute("employee");
		enterpriseCode = employee.getEnterpriseCode();
		PageObject result = new PageObject();
		String fuzzy = "";

		Object fu = request.getParameter("fuzzy");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");

		if (fu != null) {
			fuzzy = fu.toString();
		}
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			result = remote.getDetail(enterpriseCode, fuzzy, start, limit);
		} else {
			result = remote.getDetail(enterpriseCode, fuzzy);
		}
		try {
			String str = JSONUtil.serialize(result);
			if (str.equals("null")) {
				str = "{\"list\":[],\"totalCount\":null}";
			}
			write(str);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
    
	/**
	 * 取得币种信息
	 */
	public void getCurrency() {
		employee =(Employee) session.getAttribute("employee");
		enterpriseCode = employee.getEnterpriseCode();
		PageObject result = remote.getAllCurrency(enterpriseCode);
		try {
			String str = JSONUtil.serialize(result);
			if (str.equals(null)) {
				str = "{\"list\":[],\"totalCount\":null}";
			}
			write(str);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
    
	/**
	 * 删除数据
	 */
	public void deleteData() {
		employee =(Employee) session.getAttribute("employee");
		enterpriseCode = employee.getEnterpriseCode();
		// 取得参数-询价流水号
		Long quotationId = Long.parseLong(request.getParameter("quotationId")
				.toString());
		// 取得参数-上次修改时间
		Long lastmodifiedDate = Long.parseLong(request.getParameter("lastModifiedDate").toString());
		
		if (quotationId != null) {
			PurJQuotation test = new PurJQuotation();
			test = remote.findById(quotationId);
			Long testLastmodifiedDate = test.getLastModifiedDate().getTime();
			//排他检查
			if ((test.getIsUse().equals("Y")) && (testLastmodifiedDate.equals(lastmodifiedDate))) {
				String strEmployee = employee.getWorkerCode().toString();
				// 删除询价明细
				remote.delete(quotationId, strEmployee);
				// 显示删除成功信息
				write(Constants.DELETE_SUCCESS);
			} else {
				write(Constants.DATA_USING);
			}
		}
	}

	/**
	 * 增加信息
	 */
	public void addData() {
		employee =(Employee) session.getAttribute("employee");
		enterpriseCode = employee.getEnterpriseCode();
		// 企业编码
		quotation.setEnterpriseCode(enterpriseCode);
		// 添加人
		quotation.setLastModifiedBy(employee.getWorkerCode().toString());
		// 流水号
		quotation.setQuotationId(null);
		// 是否使用
		quotation.setIsUse("Y");
		
		// modify BY ywliu 09/04/29
		String gatherId = request.getParameter("gatherId");
		// modify BY ywliu 09/04/29
		if(gatherId != null && !"".equals(gatherId)) {
			MrpJPlanGather entity = gatherRemeto.findById(Long.valueOf(gatherId));
			entity.setIsEnquire("Y");
			gatherRemeto.update(entity);
		}
		try {
			// 增加一条标点符号记录
			remote.save(quotation);
			// 显示成功信息
			write(Constants.ADD_SUCCESS);
		} catch (CodeRepeatException ce) {
			// 显示错误信息
			write(Constants.DATE_REPEAT);
		} finally {
			quotation.setQuotationId(new Long(-1));
		}
	}

	/**
	 * 修改信息
	 */
	public void updateData() {
		employee =(Employee) session.getAttribute("employee");
		enterpriseCode = employee.getEnterpriseCode();
		// 企业编码
		quotation.setEnterpriseCode(enterpriseCode);
		// 添加人
		quotation.setLastModifiedBy(employee.getWorkerCode().toString());
		// 是否使用
		quotation.setIsUse("Y");
		// 取得参数-上次修改时间
		Long lastmodifiedDate = Long.parseLong(request.getParameter("lastModifiedDate").toString());
		
		if(remote.isValid(quotation)){
			PurJQuotation test = new PurJQuotation();
			test = remote.findById(quotation.getQuotationId());
			Long testLastmodifiedDate = test.getLastModifiedDate().getTime();
			//排他检查
			if ((test.getIsUse().equals("Y"))
					&& (testLastmodifiedDate.equals(lastmodifiedDate))) {
				// 增加一条标点符号记录
				remote.update(quotation);
				// 显示成功信息
				write(Constants.ADD_SUCCESS);
			} else {
				write(Constants.DATA_USING);
			}
		} else {
			write(Constants.DATE_REPEAT);
		}
	}

	/**
	 * @return the quotation
	 */
	public PurJQuotation getQuotation() {
		return quotation;
	}

	/**
	 * @param quotation the quotation to set
	 */
	public void setQuotation(PurJQuotation quotation) {
		this.quotation = quotation;
	}
}

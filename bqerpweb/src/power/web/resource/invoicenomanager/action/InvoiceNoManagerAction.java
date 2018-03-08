package power.web.resource.invoicenomanager.action;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.resource.PurJArrival;
import power.ejb.resource.PurJArrivalFacadeRemote;
import power.ejb.resource.PurJOrder;
import power.ejb.resource.PurJOrderDetails;
import power.ejb.resource.PurJOrderDetailsFacade;
import power.ejb.resource.PurJOrderDetailsFacadeRemote;
import power.ejb.resource.PurJOrderFacadeRemote;
import power.ejb.resource.business.InvoiceNoManager;
import power.ejb.resource.business.PurchaseWarehouse;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class InvoiceNoManagerAction extends AbstractAction {

	private InvoiceNoManager remote;
	/** 到货登记/验收表远程对象 */
	protected PurJArrivalFacadeRemote purJArrivalRemote;
	/** */
	private PurJOrderFacadeRemote orderRemote;
	private PurJOrderDetailsFacadeRemote mainRemote;

	public InvoiceNoManagerAction() {
		remote = (InvoiceNoManager) factory
				.getFacadeRemote("InvoiceNoManagerImpl");
		// 到货登记/验收表远程对象
		purJArrivalRemote = (PurJArrivalFacadeRemote) factory
				.getFacadeRemote("PurJArrivalFacade");
		orderRemote = (PurJOrderFacadeRemote) factory
				.getFacadeRemote("PurJOrderFacade");
		mainRemote = (PurJOrderDetailsFacadeRemote) factory
				.getFacadeRemote("PurJOrderDetailsFacade");
	}

	/**
	 * 查询已入库未审核且尚未填发票的到货单信息列表
	 * 
	 * @throws JSONException
	 */
	public void getArrivalListByNoInvoiceNo() throws JSONException {
		String purNo = request.getParameter("purNo");
		String supplyName = request.getParameter("supplyName");
		String materialName = request.getParameter("materialName");

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		// String startDate = request.getParameter("startDate");
		// String endDate = request.getParameter("endDate");
		String buyer = request.getParameter("buyer");// add by drdu 091124
		// 企业编码
		String enterpriseCode = employee.getEnterpriseCode();

		PageObject obj = remote.findArrivalBillList(Integer.parseInt(start),
				Integer.parseInt(limit), enterpriseCode, buyer, purNo,
				supplyName, materialName);
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 查询已入库未审核且尚未填发票的到货单物资明细信息列表
	 * 
	 * @throws JSONException
	 */
	public void getArrivalDetailListByNoInvoiceNo() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String id = request.getParameter("id");
		PageObject obj = remote.findArrivalBillDetailList(Long.parseLong(id),
				Integer.parseInt(start), Integer.parseInt(limit), employee
						.getEnterpriseCode());
		write(JSONUtil.serialize(obj));
	}

	@SuppressWarnings("unchecked")
	public void updateArrivalHead() throws JSONException {
		String invoiceNo = request.getParameter("invoiceNo");

		// ---------add by sychen 20100427--------------//
		String str = request.getParameter("updateData");
		Object object = JSONUtil.deserialize(str);
		// -------------add end -----------------------//
		String id = request.getParameter("id");
		
		// ------add by ltong------------
		if (str != null && !str.equals(""))
			mainRemote.updatePurQty((List<Map>) object);

		// ------add by ltong----end--------
		if (id != null && !"".equals(id)) {
			// modified by liuyi 091209
			// PurJArrival purjArrivalBean =
			// purJArrivalRemote.findById(Long.valueOf(id));
			// purjArrivalBean.setInvoiceNo(invoiceNo);
			// PurJArrival entity = purJArrivalRemote.update(purjArrivalBean);
			PurJOrder orderBean = orderRemote.findById(Long.valueOf(id));
			orderBean.setInvoiceNo(invoiceNo);
			PurJOrder entity = orderRemote.update(orderBean);

			if (entity != null) {
				write("{success:true,msg:'补录发票成功！'}");
			} else {
				write("{success:true,msg:'补录发票失败！'}");
			}
		}

	}

}

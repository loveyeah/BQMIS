package power.web.manage.stat.action;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpCExtendFormulaFacadeRemote;
import power.web.comm.AbstractAction;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class BpCReportFormulaRelationAction extends AbstractAction {
	private BpCExtendFormulaFacadeRemote remote;
	private int start;
	private int limit;
	/**
	 * 
	 * 构造方法
	 * 
	 */
	public BpCReportFormulaRelationAction() {
		remote = (BpCExtendFormulaFacadeRemote) factory
				.getFacadeRemote("BpCExtendFormulaFacade");
	}

	public void getAllStatItemaList() throws JSONException {

		String argFuzzy = request.getParameter("argFuzzy");
		String dataTimeType = request.getParameter("dataTimeType");
		String itemType = request.getParameter("itemType");
		PageObject obj = remote.findAllStatItem(argFuzzy, dataTimeType,
				itemType, start, limit);

		write(JSONUtil.serialize(obj));
	}

	// ******************************************get/set变量方法******************************************

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

}

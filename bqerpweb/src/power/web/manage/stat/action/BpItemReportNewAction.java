package power.web.manage.stat.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpCItemReportNew;
import power.ejb.manage.stat.BpCItemReportNewFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BpItemReportNewAction extends AbstractAction {

	protected BpCItemReportNewFacadeRemote remote;
	private int start;
	private int limit;
	private long code;

	public BpItemReportNewAction() {
		remote = (BpCItemReportNewFacadeRemote) factory
				.getFacadeRemote("BpCItemReportNewFacade");
	}

	/**
	 * 保存操作
	 */
	@SuppressWarnings("unchecked")
	public void addReprotItemNew() {
		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			String code = request.getParameter("isCode");

			Object obj = JSONUtil.deserialize(str);

			List<BpCItemReportNew> addList = new ArrayList<BpCItemReportNew>();
			List<BpCItemReportNew> updateList = new ArrayList<BpCItemReportNew>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
				Long id = null;
				String itemCode = null;
				String reportCode = null;
				String displayNo = null;
				String dataType = null;
				String dataTimeType = null;
				String itemAlias = null;
				String itemBaseName = null;
				String itemSecondName = null;
				String conkersNo = null;
				// add by liuyi 20100524 
				String isShowZero = null;
				if (data.get("model.id") != null
						&& !data.get("model.id").equals(""))
					id = Long.parseLong(data.get("model.id").toString());
				if (data.get("model.reportCode") != null
						&& !data.get("model.reportCode").equals("")) {
					reportCode = data.get("model.reportCode").toString();
				}
				if (data.get("model.itemCode") != null
						&& !data.get("model.itemCode").equals("")) {
					itemCode = data.get("model.itemCode").toString();
				}
				if (data.get("model.itemAlias") != null
						&& !data.get("model.itemAlias").equals("")) {
					itemAlias = data.get("model.itemAlias").toString();
				}
				if (data.get("model.displayNo") != null
						&& !data.get("model.displayNo").equals("")) {
					displayNo = data.get("model.displayNo").toString();
				}
				if (data.get("model.dataType") != null
						&& !data.get("model.dataType").equals("")) {
					dataType = data.get("model.dataType").toString();
				}
				if (data.get("model.dataTimeType") != null
						&& !data.get("model.dataTimeType").equals("")) {
					dataTimeType = data.get("model.dataTimeType").toString();
				}
				if (data.get("model.itemBaseName") != null
						&& !data.get("model.itemBaseName").equals("")) {
					itemBaseName = data.get("model.itemBaseName").toString();
				}
				if (data.get("model.itemSecondName") != null
						&& !data.get("model.itemSecondName").equals("")) {
					itemSecondName = data.get("model.itemSecondName")
							.toString();
				}
				if (data.get("model.conkersNo") != null
						&& !data.get("model.conkersNo").equals("")) {
					conkersNo = data.get("model.conkersNo").toString();
				}
				// add by liuyi 20100524 
				if(data.get("model.isShowZero") != null && !data.get("model.isShowZero").toString().equals("")){
					isShowZero = data.get("model.isShowZero").toString();
				}
				BpCItemReportNew model = new BpCItemReportNew();
				// 增加
				if (id == null) {
					model.setReportCode(Long.parseLong(reportCode));
					model.setItemCode(itemCode);
					model.setDataType(dataType);
					model.setItemAlias(itemAlias);
					model.setItemBaseName(itemBaseName);
					model.setItemSecondName(itemSecondName);
					model.setDataTimeType(dataTimeType);
					// add by liuyi 20100524 
					model.setIsShowZero(isShowZero);
					if (displayNo != null && !displayNo.equals("")) {
						model.setDisplayNo(Long.parseLong(displayNo));
					}
					if (conkersNo != null && !conkersNo.equals("")) {
						model.setConkersNo(Long.parseLong(conkersNo));
					}
					model.setEnterpriseCode(employee.getEnterpriseCode());
					addList.add(model);
				} else {
					model = remote.findById(id);
					model.setReportCode(Long.parseLong(reportCode));
					model.setItemCode(itemCode);
					model.setItemAlias(itemAlias);
					// add by liuyi 20100524 
					model.setIsShowZero(isShowZero);
					if (dataType != null && !dataType.equals("")) {
						model.setDataType(dataType);
					}
					if (displayNo != null && !displayNo.equals("")) {
						model.setDisplayNo(Long.parseLong(displayNo));
					}
					
					if (conkersNo != null && !conkersNo.equals("")) {
						model.setConkersNo(Long.parseLong(conkersNo));
					}
					model.setItemBaseName(itemBaseName);
					model.setItemSecondName(itemSecondName);
					model.setDataTimeType(dataTimeType);
					updateList.add(model);
				}
			}

			if (addList.size() > 0)
				remote.save(addList);

			if (updateList.size() > 0)
				remote.update(updateList);

			// String [] ids=deleteIds.split(",");

			if (deleteIds != null && !deleteIds.trim().equals(""))

				// remote.delete(deleteIds);

				remote.deleteMuti(deleteIds, Long.parseLong(code));

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}

	/**
	 * 根据录入报表编码查找列表信息
	 * 
	 * @throws JSONException
	 */
	public void findReprotItemNewList() throws JSONException {
		String reportCode = request.getParameter("reportCode");
		PageObject obj = remote.findAll(reportCode, employee
				.getEnterpriseCode(), start, limit);

		write(JSONUtil.serialize(obj));

	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}
}

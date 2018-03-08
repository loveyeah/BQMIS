package power.web.manage.stat.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpCAnalyseAccountItem;
import power.ejb.manage.stat.BpCAnalyseAccountItemFacadeRemote;
import power.ejb.manage.stat.BpCAnalyseAccountItemId;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class BpCAnalyseAccountItemAction extends AbstractAction {
	private BpCAnalyseAccountItemFacadeRemote remote;
	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public BpCAnalyseAccountItemAction() {
		remote = (BpCAnalyseAccountItemFacadeRemote) factory
				.getFacadeRemote("BpCAnalyseAccountItemFacade");
	}

	/**
	 * 增加新记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveBpCAnalyseAccountItem() {

		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete"); 
			Object obj = JSONUtil.deserialize(str); 
			List<BpCAnalyseAccountItem> addList = new ArrayList<BpCAnalyseAccountItem>();
			List<BpCAnalyseAccountItem> updateList = new ArrayList<BpCAnalyseAccountItem>();
            List<Map> list = (List<Map>) obj;
			for (Map data : list) { 
				String itemCode = null;
				String accountCode = null;
				String displayNo = null;
				String dataType = null;
				String itemAlias = null;
				String itemBaseName = null;

				if (data.get("baseInfo.id.accountCode") != null) {
					accountCode = data.get("baseInfo.id.accountCode")
							.toString();
				}

				if (data.get("baseInfo.id.itemCode") != null) {
					itemCode = data.get("baseInfo.id.itemCode").toString();
				}
				if (data.get("baseInfo.displayNo") != null) {
					displayNo = data.get("baseInfo.displayNo").toString();
				}
				if (data.get("baseInfo.dataType") != null) {
					dataType = data.get("baseInfo.dataType").toString();
				}
				if (data.get("baseInfo.itemAlias") != null) {
					itemAlias = data.get("baseInfo.itemAlias").toString();
				}
				if (data.get("baseInfo.itemBaseName") != null) {
					itemBaseName = data.get("baseInfo.itemBaseName").toString();
				}

				BpCAnalyseAccountItem model = new BpCAnalyseAccountItem();
				BpCAnalyseAccountItemId id = new BpCAnalyseAccountItemId();
				// 增加
				if (remote.isNew(accountCode, itemCode) == 0) {
					id.setItemCode(itemCode);
					id.setAccountCode(Long.parseLong(accountCode));

					model.setId(id);
					model.setDataType(dataType);
					model.setItemAlias(itemAlias);
					model.setItemBaseName(itemBaseName);
					model.setEnterpriseCode(employee.getEnterpriseCode());

					if (displayNo != null && !displayNo.equals(""))
						model.setDisplayNo(Long.parseLong(displayNo));

					addList.add(model);
				} else {
					id.setAccountCode(Long.parseLong(accountCode));
					id.setItemCode(itemCode);

					model = remote.findById(id);

					if (displayNo != null && !displayNo.equals(""))
						model.setDisplayNo(Long.parseLong(displayNo));
					model.setDataType(dataType);
					model.setItemAlias(itemAlias);
					model.setItemBaseName(itemBaseName);
					updateList.add(model);
				}
			}

			if (addList.size() > 0) {
				remote.save(addList);
			}

			if (updateList.size() > 0) {
				remote.update(updateList);
			}

			if (deleteIds != null && !deleteIds.trim().equals(""))
				remote.delete(deleteIds);

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}

	public void getBpCAnalyseAccountItem() throws JSONException {
		String accountCode = request.getParameter("accountCode");
		PageObject obj = remote.findAll(employee.getEnterpriseCode(),
				accountCode, start, limit);

		write(JSONUtil.serialize(obj));
	}

	// public void getAllStatItemaList() throws JSONException {
	// String argFuzzy = request.getParameter("argFuzzy");
	// PageObject obj = remote.findAllStatItem(argFuzzy, start,
	// limit);
	// System.out.print(JSONUtil.serialize(obj));
	// write(JSONUtil.serialize(obj));
	// }

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

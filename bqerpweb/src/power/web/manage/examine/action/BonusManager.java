package power.web.manage.examine.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.exam.BpJCbmAwardDetail;
import power.ejb.manage.exam.BpJCbmAwardDetailFacadeRemote;
import power.ejb.manage.exam.BpJCbmAwardMainFacadeRemote;
import power.ejb.manage.exam.BpJCbmDepSeason;
import power.ejb.manage.exam.BpJCbmExecutionFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 奖金申报，审批Action
 * 
 * @author ywliu
 * 
 */
@SuppressWarnings("serial")
public class BonusManager extends AbstractAction {

	private BpJCbmAwardMainFacadeRemote mainRemote;
	private BpJCbmAwardDetailFacadeRemote detailRemote;

	public BonusManager() {
		mainRemote = (BpJCbmAwardMainFacadeRemote) factory
				.getFacadeRemote("BpJCbmAwardMainFacade");
		detailRemote = (BpJCbmAwardDetailFacadeRemote) factory
				.getFacadeRemote("BpJCbmAwardDetailFacade");
	}

	/**
	 * 奖金申报查询
	 * 
	 * @throws JSONException
	 */
	public void getBonusApplyList() throws JSONException {
		String dateTime = request.getParameter("dateTime");
		if (dateTime == null)
			dateTime = "2009-11";
		write(JSONUtil.serialize(detailRemote.getDeptCash(dateTime, "hfdc")));
	}

	@SuppressWarnings("unchecked")
	public void saveBonusApplyList() throws JSONException {
		String str = request.getParameter("addOrUpdateRecords");
		Object obj = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) obj;
		List<BpJCbmAwardDetail> addlist = new ArrayList<BpJCbmAwardDetail>();
		List<BpJCbmAwardDetail> updatelist = new ArrayList<BpJCbmAwardDetail>();
		for (Map data : list) {
			BpJCbmAwardDetail model = new BpJCbmAwardDetail();
			if (data.get("cashvalue") != null)
				model.setCashBonus(Double.parseDouble(data.get("cashvalue")
						.toString()));
			/*if (data.get("did") != null)
				model.setDeclareId(Long.parseLong(data.get("did").toString()));*/
			if (data.get("affid") != null)
				model.setAffiliatedId(Long.parseLong(data.get("affid")
						.toString()));
			if (data.get("memo") != null)
				model.setMemo(data.get("memo").toString());
			if ("null".equals(data.get("affjid").toString())
					|| "".equals(data.get("affjid").toString())) {
				addlist.add(model);
			} else {
				updatelist.add(model);
			}
		}
		if (detailRemote.saveDeptCash(addlist, updatelist, "hfdc"))
			write("{success:true,msg:'操 作 成 功 ！'}");
		else
			write("{success:false,msg:'操 作 失 败 ！'}");
	}
}

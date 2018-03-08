package power.web.productiontec.relayProtection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.relayProtection.PtJdbhCSydwh;
import power.ejb.productiontec.relayProtection.PtJdbhCSydwhFacadeRemote;
import power.ejb.productiontec.relayProtection.PtJdbhCSyxmwh;
import power.ejb.productiontec.relayProtection.PtJdbhCSyxmwhFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class ExperimentItemAction extends AbstractAction {
	/** 试验项目接口 */
	private PtJdbhCSyxmwhFacadeRemote expItemRemote;
	/** 试验点接口 */
	private PtJdbhCSydwhFacadeRemote expPointRemote;
	
	private PtJdbhCSyxmwh model;
	private int start;
	private int limit;
	
	public ExperimentItemAction() {
		expItemRemote = (PtJdbhCSyxmwhFacadeRemote) factory.getFacadeRemote("PtJdbhCSyxmwhFacade");
		expPointRemote = (PtJdbhCSydwhFacadeRemote) factory.getFacadeRemote("PtJdbhCSydwhFacade");
	}
	
	public void saveSyxmwh() throws CodeRepeatException {
		model.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			expItemRemote.save(model);
		} catch (Exception e) {
			write("{success:true,msg:'增加失败:试验名称重复！'}");
		}
		write("{success:true}");
	}
	
	public void updateSyxmwh() {
		String syxmId = request.getParameter("syxmId");
		if(syxmId != null && !"".equals(syxmId)) {
			PtJdbhCSyxmwh entity = expItemRemote.findById(Long.valueOf(syxmId));
			entity.setSyxmName(model.getSyxmName());
			entity.setDisplayNo(model.getDisplayNo());
			try {
				expItemRemote.update(entity);
			} catch (Exception e) {
				write("{success:true,msg:'更新失败:试验名称重复！'}");
			}
			write("{success:true}");
		}
	}
	
	public void deleteSyxmwh() {
		String idString = request.getParameter("ids");
		expItemRemote.delete(idString);
//		expPointRemote.delete(ids);
	}
	
	public void getSyxmwhList() throws JSONException {
		PageObject object = expItemRemote.findPtJdbhCSyxmwhList(employee.getEnterpriseCode(), start, limit);
		write(JSONUtil.serialize(object));
	}
	
	public void findSydwhListBySyxmId() throws JSONException {
		String syxmId = request.getParameter("syxmId");
		Long id =  Long.valueOf(syxmId);
		List<PtJdbhCSydwh> list = expPointRemote.findBySyxmId(id);
		write(JSONUtil.serialize(list));
		//System.out.println(JSONUtil.serialize(list)+syxmId+"000000");
	}
	
	@SuppressWarnings("unchecked")
	public void saveSydwh() throws Exception {
		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<PtJdbhCSydwh> addList = new ArrayList<PtJdbhCSydwh>();
			List<PtJdbhCSydwh> updateList = new ArrayList<PtJdbhCSydwh>();

			List<Map> list = (List<Map>) obj;

			for (Map data : list) {

				Long sydId = null;
				Long syxmId = null;
				String sydName = "";
				Long unitId = null;
				Double maximum = null;
				Double minimum = null;
				Long displayNo = null;
				String memo = "";

				if (data.get("sydId") != null) {
					sydId = Long.valueOf(data.get("sydId").toString());
				}
				if (data.get("syxmId") != null) {
					syxmId = Long.valueOf(data.get("syxmId").toString());
				}
				if (data.get("sydName") != null) {
					sydName = data.get("sydName").toString();
				}
				if (data.get("unitId") != null) {
					unitId = Long.valueOf(data.get("unitId").toString());
				}

				if (data.get("maximum") != null) {
					maximum = Double.valueOf(data.get("maximum").toString());
				}
				if (data.get("minimum") != null) {
					minimum = Double.valueOf(data.get("minimum").toString());
				}
				if (data.get("displayNo") != null) {
					displayNo = Long.valueOf(data.get("displayNo").toString());
				}
				if (data.get("memo") != null) {
					memo = data.get("memo").toString();
				}

				PtJdbhCSydwh model = new PtJdbhCSydwh();

				// 增加
				if (sydId == null) {
					model.setSyxmId(syxmId);
					model.setSydName(sydName);
					model.setUnitId(unitId);
					model.setMaximum(maximum);
					model.setMinimum(minimum);
					model.setDisplayNo(displayNo);
					model.setMemo(memo);
					model.setEnterpriseCode(employee.getEnterpriseCode());

					addList.add(model);

				} else {
					model = expPointRemote.findById(sydId);

					model.setSyxmId(syxmId);
					model.setSydName(sydName);
					model.setUnitId(unitId);
					model.setMaximum(maximum);
					model.setMinimum(minimum);
					model.setDisplayNo(displayNo);
					model.setMemo(memo);
					model.setEnterpriseCode(employee.getEnterpriseCode());

					updateList.add(model);

				}

			}

			if (addList.size() > 0 || updateList.size() > 0
					|| (deleteIds != null && !deleteIds.trim().equals(""))) {
				try {
					expPointRemote.save(addList,updateList,deleteIds);
					write("{success:true,msg:'保存成功！'}");
				} catch (Exception e) {
					write("{success:true,msg:'更新失败:试验名称重复！'}");
				}
				
			}
		} catch (Exception exc) {
			write("{success:false,msg:'操作失败'}");
			throw exc;

		}
	}

	/**
	 * @return the model
	 */
	public PtJdbhCSyxmwh getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(PtJdbhCSyxmwh model) {
		this.model = model;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
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
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

}

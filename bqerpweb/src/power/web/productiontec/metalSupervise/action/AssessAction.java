package power.web.productiontec.metalSupervise.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.productiontec.metalSupervise.PtJsjdJHgjlkhxm;
import power.ejb.productiontec.metalSupervise.PtJsjdJHgjlkhxmFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class AssessAction extends AbstractAction {
	private PtJsjdJHgjlkhxm model;
	private PtJsjdJHgjlkhxmFacadeRemote jlkhxmRemote;
	private int start;
	private int limit;

	public PtJsjdJHgjlkhxm getModel() {
		return model;
	}

	public void setModel(PtJsjdJHgjlkhxm model) {
		this.model = model;
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

	public AssessAction() {
		jlkhxmRemote = (PtJsjdJHgjlkhxmFacadeRemote) factory
				.getFacadeRemote("PtJsjdJHgjlkhxmFacade");
	}

	/**
	 * 获取焊工技能考核项目表信息
	 */
	public void findJnKhxmListByhgjnkhId()throws JSONException {
		String hgjnkhId = request.getParameter("hgjnkhId");
		if(hgjnkhId != null){
			List<PtJsjdJHgjlkhxm> list = jlkhxmRemote.findByHgjnkhId(Long.parseLong(hgjnkhId));
			write(JSONUtil.serialize(list));
		}else
			write("{success:false,msg:' 操 作 失 败 !'}");
	}

	/**
	 * 保存修改了的焊工技能考核项目表信息
	 */
	@SuppressWarnings("unchecked")
	public void saveJnKhxm() throws Exception {
		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);
			List<PtJsjdJHgjlkhxm> addList = new ArrayList<PtJsjdJHgjlkhxm>();
			List<PtJsjdJHgjlkhxm> updateList = new ArrayList<PtJsjdJHgjlkhxm>();

			List<Map> list = (List<Map>) obj;

			for (Map data : list) {

				String hgjnkhxmId = null;
				String hgjnkhId = null;
				String examName = null;
				String material = null;
				String sizes = null;
				String method = null;
				String results = null;
				String allowWork = null;

				if (data.get("hgjnkhxmId") != null) {
					hgjnkhxmId = data.get("hgjnkhxmId").toString();
				}
				if (data.get("hgjnkhId") != null) {
					hgjnkhId = data.get("hgjnkhId").toString();
				}
				if (data.get("examName") != null) {
					examName = data.get("examName").toString();
				}

				if (data.get("material") != null) {
					material = data.get("material").toString();
				}

				if (data.get("sizes") != null) {
					sizes = data.get("sizes").toString();
				}
				if (data.get("method") != null) {
					method = data.get("method").toString();
				}
				if (data.get("results") != null) {
					results = data.get("results").toString();
				}
				if (data.get("allowWork") != null) {
					allowWork = data.get("allowWork").toString();
				}

				PtJsjdJHgjlkhxm model = new PtJsjdJHgjlkhxm();

				// 增加
				if (hgjnkhxmId == null) {

					model.setHgjnkhId(Long.parseLong(hgjnkhId));
					model.setExamName(examName);
					model.setMaterial(material);
					model.setSizes(sizes);
					model.setMethod(method);
					model.setResults(results);
					model.setAllowWork(allowWork);
					addList.add(model);

				} else {
					model = jlkhxmRemote.findById(Long.parseLong(hgjnkhxmId));
					model.setHgjnkhId(Long.parseLong(hgjnkhId));
					model.setExamName(examName);
					model.setMaterial(material);
					model.setSizes(sizes);
					model.setMethod(method);
					model.setResults(results);
					model.setAllowWork(allowWork);

					updateList.add(model);

				}

			}

			if (addList.size() > 0 || updateList.size() > 0
					|| (deleteIds != null && !deleteIds.trim().equals("")))
				jlkhxmRemote.save(addList, updateList, deleteIds);
			System.out.println(deleteIds);
			write("{success:true,msg:' 保 存 成 功 ！'}");

		} catch (Exception exc) {
			write("{success:false,msg:' 操 作 失 败 !'}");
			throw exc;

		}
	}
}

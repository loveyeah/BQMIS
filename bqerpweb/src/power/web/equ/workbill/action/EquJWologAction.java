package power.web.equ.workbill.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;

import power.ejb.equ.workbill.EquJWoLog;
import power.ejb.equ.workbill.EquJWoLogFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EquJWologAction extends AbstractAction{
	
	protected int start;
	protected int limit;

	
	private String woCode;
	
	protected EquJWoLogFacadeRemote remote;

	public EquJWologAction() {
		remote = (EquJWoLogFacadeRemote) factory
				.getFacadeRemote("EquJWoLogFacade");

	}
	/**
	 * 增加新记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveEquJWoLog() {

		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<EquJWoLog> addList = new ArrayList<EquJWoLog>();
			List<EquJWoLog> updateList = new ArrayList<EquJWoLog>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {

				String logContent = null;	
				String id = null;
				String woCode = data.get("woCode").toString();

				if (data.get("logContent") != null) {
					logContent = data.get("logContent")
							.toString();
				}

				if (data.get("id") != null)
					id = data.get("id").toString();

				EquJWoLog model = new EquJWoLog();
				
				// 增加
				if (id == null) {
					model.setWoCode(woCode);
					model.setLogContent(logContent);	
					addList.add(model);
				} 
				//修改
				else {
					model = remote.findById(Long.parseLong(id));

				

					if (logContent != null
							&& !logContent.equals(""))
						model.setLogContent(logContent);	


					updateList.add(model);
				}
			}

			if ((addList.size() > 0 || updateList.size() > 0)
					|| (deleteIds != null && !deleteIds.trim().equals(""))) {
				remote.save(addList, updateList, deleteIds);
			}

			write("{success: true,msg:'保存成功！'}");
		
		}catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}

	/**
	 * 获取记录列表
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getEquJWoLog() throws JSONException {
		// String woCode=request.getParameter("woCode");
		// String operationStep=request.getParameter("operationStep");

		PageObject obj = remote.findAll( woCode,
				 start, limit);
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

	public String getWoCode() {
		return woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}



}

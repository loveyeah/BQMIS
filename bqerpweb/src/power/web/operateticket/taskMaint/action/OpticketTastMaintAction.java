/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.operateticket.taskMaint.action;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.comm.TreeNode;
import power.ejb.opticket.RunCOpticketTask;
import power.ejb.opticket.RunCOpticketTaskFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

/**
 * 操作任务维护
 * 
 * @author wangyun
 */
public class OpticketTastMaintAction extends AbstractAction {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 操作任务维护接口 */
	private RunCOpticketTaskFacadeRemote runCOpticketTaskRemote;

	/** 操作票任务维护bean */
	private RunCOpticketTask opticketTask;

	/** 任务维护节点ID */
	private Long node;

	/**
	 * 构造函数
	 */
	public OpticketTastMaintAction() {
		runCOpticketTaskRemote = (RunCOpticketTaskFacadeRemote) factory
				.getFacadeRemote("RunCOpticketTaskFacade");
	}

	/**
	 * 根据父操作票任务id, 获得其子操作票任务
	 */
	public void getOpTickTaskTreeNode() {
		// 序列化为JSON对象的字符串形式
		String str = toJSONStr(runCOpticketTaskRemote
				.findByParentOperateTaskId(employee.getEnterpriseCode(), node));
		// 以html方式输出字符串
		write(str);
	}

	/**
	 * 增加操作票任务
	 * 
	 */
	public void addOpTickTask() {
		String addByStand = request.getParameter("addByStand");
		//由标准操作票生成
		if("Y".equals(addByStand))
		{
			String ticketNos = request.getParameter("ticketNos");
			String parentId = request.getParameter("parentId");
			runCOpticketTaskRemote.addByStandTickets(employee.getEnterpriseCode(), Long.parseLong(parentId), ticketNos, employee.getWorkerCode());
		}
		else
		{ 
			opticketTask.setEnterpriseCode(employee.getEnterpriseCode()); 
			// 填写人
			opticketTask.setModifyBy(employee.getWorkerCode());
			// 设置成叶子节点
			opticketTask.setIsTask("Y"); 
			// 增加一条工作票任务记录
			runCOpticketTaskRemote.save(opticketTask); 
//			opticketTask.setOperateTaskId(new Long(-1));
		}
		write(Constants.ADD_SUCCESS);
	}
	
	public void pasteNode(){
		String copyTaskId = request.getParameter("copyTaskId");
		String pasteTaskId = request.getParameter("pasteTaskId");
		RunCOpticketTask copyModel = runCOpticketTaskRemote.findById(Long.parseLong(copyTaskId));
		RunCOpticketTask pasteModel = runCOpticketTaskRemote.findById(Long.parseLong(pasteTaskId));
		copyModel.setParentOperateTaskId(pasteModel.getOperateTaskId());
		copyModel.setEnterpriseCode(employee.getEnterpriseCode());
		copyModel.setModifyBy(employee.getWorkerCode());
		copyModel.setIsTask("Y");
		RunCOpticketTask newModel  = runCOpticketTaskRemote.saveCopyTicket(copyModel); 
		if(newModel!=null){
			runCOpticketTaskRemote.copyTicket(copyModel.getOperateTaskId(), newModel.getOperateTaskId());
			this.write("{id:'"+newModel.getOperateTaskId()+"'}");
		}
	}

	/**
	 * 修改操作票任务
	 */
	public void updateOpTickTask() {
		// 查找这条操作票任务记录
		RunCOpticketTask model = runCOpticketTaskRemote.findById(opticketTask
				.getOperateTaskId());
		// 操作票任务编号
		model.setOperateTaskCode(opticketTask.getOperateTaskCode());
		// 操作票任务名称
		model.setOperateTaskName(opticketTask.getOperateTaskName());
		// 操作票任务说明
		model.setOperateTaskExplain(opticketTask.getOperateTaskExplain());
		// 显示顺序
		model.setDisplayNo(opticketTask.getDisplayNo());

		// if (model.getDisplayNo() == null) {
		// // 若排序字段为空，在库中排序字段存入操作票任务id
		// model.setDisplayNo(opticketTask.getOperateTaskId());
		// }
		model.setIsTask(opticketTask.getIsTask());

		// 修改这条操作票任务记录
		runCOpticketTaskRemote.update(model);

		write(Constants.MODIFY_SUCCESS);
	}

	/**
	 * 删除操作票任务
	 */
	public void deleteOpTickTask() {
		// 从请求中获得删除的ID
		String strId = request.getParameter("id");

		// 删除操作票任务记录
		runCOpticketTaskRemote.delete(runCOpticketTaskRemote.findById(Long
				.parseLong(strId)));
		write(Constants.DELETE_SUCCESS);
	}

	/**
	 * 将list转换为json格式数据
	 * 
	 * @param argOptickTaskList
	 *            操作票任務
	 * @return json格式数据
	 */
	private String toJSONStr(List<RunCOpticketTask> argOptickTaskList) {
		// StringBuffer sbf = new StringBuffer();
		// sbf.append("[");
		//
		// RunCOpticketTask opticketTask = null;
		// for (int intCnt = 0; intCnt < argOptickTaskList.size(); intCnt++) {
		// // 操作票任务数据对象
		// opticketTask = argOptickTaskList.get(intCnt);
		// // 节点ID
		// String strOpticketTaskId = getEmptyString(opticketTask
		// .getOperateTaskId());
		// // 是否是叶子节点
		// boolean isLeaf =
		// getEmptyString(opticketTask.getOperateTaskCode()).length() == 8;
		//
		// sbf.append("{\"text\":\"" + opticketTask.getOperateTaskName()
		// + "\",\"id\":\"" + strOpticketTaskId + "\",\"leaf\":"
		// + isLeaf + ",\"explain\":\""
		// + opticketTask.getOperateTaskExplain()
		// + "\",\"operateTaskCode\":\""
		// + opticketTask.getOperateTaskCode() + "\",\"displayNo\":\""
		// + getEmptyString(opticketTask.getDisplayNo()) + "\"},");
		// }
		//
		// if (sbf.length() > 1) {
		// sbf.deleteCharAt(sbf.lastIndexOf(","));
		// }
		//
		// sbf.append("]");
		// return sbf.toString();
		if (argOptickTaskList != null && argOptickTaskList.size() > 0) {
			List<TreeNode> list = new ArrayList();
			try {
				for (int i = 0; i < argOptickTaskList.size(); i++) {
					TreeNode treeNode = new TreeNode();
					treeNode.setText(argOptickTaskList.get(i)
							.getOperateTaskName());
					treeNode.setId(argOptickTaskList.get(i).getOperateTaskId()
							.toString());
					treeNode.setCode(argOptickTaskList.get(i)
							.getOperateTaskCode());
					if (argOptickTaskList.get(i).getDisplayNo() != null) {
						treeNode.setOpenType(argOptickTaskList.get(i)
								.getDisplayNo().toString());
					}
					treeNode.setLeaf(getEmptyString(
							argOptickTaskList.get(i).getOperateTaskCode())
							.length() == 13 || getEmptyString(
									argOptickTaskList.get(i).getOperateTaskCode())
									.length() == 12);
					treeNode
							.setDescription(argOptickTaskList.get(i)
									.getOperateTaskExplain() != null ? argOptickTaskList
									.get(i).getOperateTaskExplain()
									: "");
					list.add(treeNode);
				}
				return JSONUtil.serialize(list);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "[]";

			}
		} else {
			return "[]";
		}
	}

	/**
	 * 返回非NULL字符串
	 * 
	 * @param argObj
	 *            对象
	 * @return 非NULL字符串
	 */
	private String getEmptyString(Object argObj) {
		return argObj == null ? "" : argObj.toString();
	}

	/**
	 * @return the opticketTask
	 */
	public RunCOpticketTask getOpticketTask() {
		return opticketTask;
	}

	/**
	 * @param opticketTask
	 *            the opticketTask to set
	 */
	public void setOpticketTask(RunCOpticketTask opticketTask) {
		this.opticketTask = opticketTask;
	}

	/**
	 * @return the node
	 */
	public Long getNode() {
		return node;
	}

	/**
	 * @param node
	 *            the node to set
	 */
	public void setNode(Long node) {
		this.node = node;
	}
}

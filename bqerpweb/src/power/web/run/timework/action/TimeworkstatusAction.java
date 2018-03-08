package power.web.run.timework.action;

import com.googlecode.jsonplugin.JSONUtil;
import power.web.comm.AbstractAction;
import power.ejb.run.timework.RunCTimeworkstatus;
import power.ejb.run.timework.RunCTimeworkstatusFacadeRemote;

@SuppressWarnings("serial")
public class TimeworkstatusAction extends AbstractAction {
	protected RunCTimeworkstatus timeworkstatusInfo;
	protected String id;
	protected String ids;
	protected RunCTimeworkstatusFacadeRemote remote;

	public TimeworkstatusAction() {
		remote = (RunCTimeworkstatusFacadeRemote) factory
				.getFacadeRemote("RunCTimeworkstatusFacade");
	}

	// 取定期工作运行列表
	public void getlistTimeworkstatus() throws Exception {
		write(JSONUtil.serialize(remote.findByIsUse((Object) ("Y"))));
	}

	// 添加记录
	protected boolean insert() throws Exception {
		try {
			timeworkstatusInfo.setStatus("C");// 状态
			timeworkstatusInfo.setEnterprisecode(employee.getEnterpriseCode());// 企业编码
			timeworkstatusInfo.setIsUse("Y");// 删除控制
			remote.save(timeworkstatusInfo);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加操作
	public void addTimeworkstatus() throws Exception {
		if (insert()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'添 加 失 败 !'}");
		}
	}

	// 修改记录
	protected boolean update() {
		try {
			RunCTimeworkstatus model = new RunCTimeworkstatus();
			model = remote.findById(Long.parseLong(id));
			model.setName(timeworkstatusInfo.getName());// 名称
			model.setMemo(timeworkstatusInfo.getMemo());// 备注
			model.setOrderby(timeworkstatusInfo.getOrderby());// 排序号
			remote.update(model);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 修改操作
	public void updateTimeworkstatus() {
		if (update()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'修 改 失 败 !'}");
		}
	}

	// 锁定记录
	protected boolean lock() {
		try {
			String[] str = ids.split(",");
			Long[] id = new Long[str.length];
			for (int i = 0; i < str.length; i++) {
				id[i] = Long.parseLong(str[i]);
				RunCTimeworkstatus model = new RunCTimeworkstatus();
				model = remote.findById(id[i]);
				model.setStatus("L");
				remote.update(model);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 锁定操作
	public void lockTimeworkstatus() {
		if (lock()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'锁 定 失 败 !'}");
		}
	}

	// 解锁记录
	protected boolean unlock() {
		try {
			String[] str = ids.split(",");
			Long[] id = new Long[str.length];
			for (int i = 0; i < str.length; i++) {
				id[i] = Long.parseLong(str[i]);
				RunCTimeworkstatus model = new RunCTimeworkstatus();
				model = remote.findById(id[i]);
				model.setStatus("C");
				remote.update(model);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 解锁操作
	public void unlockTimeworkstatus() {
		if (unlock()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'解 锁 失 败 !'}");
		}
	}

	// 删除记录
	protected boolean delete() {
		try {
			String[] str = ids.split(",");
			Long[] id = new Long[str.length];
			for (int i = 0; i < str.length; i++) {
				id[i] = Long.parseLong(str[i]);
				RunCTimeworkstatus model = new RunCTimeworkstatus();
				model = remote.findById(id[i]);
				model.setIsUse("N");
				remote.update(model);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 删除操作
	public void deleteTimeworkstatus() {
		if (delete()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'删 除 失 败 !'}");
		}
	}

	/**
	 * @return the timeworkstatusInfo
	 */
	public RunCTimeworkstatus gettimeworkstatusInfo() {
		return timeworkstatusInfo;
	}

	/**
	 * @param timeworkstatusInfo
	 *            the timeworkstatusInfo to set
	 */
	public void settimeworkstatusInfo(RunCTimeworkstatus timeworkstatusInfo) {
		this.timeworkstatusInfo = timeworkstatusInfo;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the ids
	 */
	public String getIds() {
		return ids;
	}

	/**
	 * @param ids
	 *            the ids to set
	 */
	public void setIds(String ids) {
		this.ids = ids;
	}
}
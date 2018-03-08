package power.web.run.timework.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONUtil;
import power.web.comm.AbstractAction;
import power.ejb.run.timework.RunCTimeworktype;
import power.ejb.run.timework.RunCTimeworktypeFacadeRemote;

@SuppressWarnings("serial")
public class TimeworktypeAction extends AbstractAction {

	protected RunCTimeworktype timeworktypeInfo;
	protected String id;
	protected String ids;
	protected RunCTimeworktypeFacadeRemote remote;

	public TimeworktypeAction() {
		remote = (RunCTimeworktypeFacadeRemote) factory
				.getFacadeRemote("RunCTimeworktypeFacade");
	}

	// 取定期工作运行列表
	public void getlistTimeworktype() throws Exception {
		write(JSONUtil.serialize(remote.findByIsUse((Object) ("Y"))));
	}

	// 维护时候显示
	public void uselistTimeworktype() throws Exception {
		List<RunCTimeworktype> list = remote.findForManage();
		String str = "";
		int i = 0;
		for (RunCTimeworktype model : list) {
			i++;
			str += "[\"" + model.getName() + "\",\"" + model.getCode() + "\"]";
			if (i < list.size()) {
				str += ",";
			}
		}
		write(str);
	}

	// 添加记录
	protected boolean insert() throws Exception {
		try {
			timeworktypeInfo.setStatus("C");// 状态
			timeworktypeInfo.setEnterprisecode(employee.getEnterpriseCode());// 企业编码
			timeworktypeInfo.setShowtype("Y");// 暂时没用
			timeworktypeInfo.setIsUse("Y");// 删除控制
			remote.save(timeworktypeInfo);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加操作
	public void addTimeworktype() throws Exception {
		if (insert()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'添 加 失 败 !'}");
		}
	}

	// 修改记录
	protected boolean update() {
		try {
			RunCTimeworktype model = new RunCTimeworktype();
			model = remote.findById(Long.parseLong(id));
			model.setName(timeworktypeInfo.getName());// 名称
			model.setMemo(timeworktypeInfo.getMemo());// 备注
			model.setKeywordsinsql(timeworktypeInfo.getKeywordsinsql());// 搜索关键字
			model.setOrderby(timeworktypeInfo.getOrderby());// 排序号
			remote.update(model);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 修改操作
	public void updateTimeworktype() {
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
				RunCTimeworktype model = new RunCTimeworktype();
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
	public void lockTimeworktype() {
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
				RunCTimeworktype model = new RunCTimeworktype();
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
	public void unlockTimeworktype() {
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
				RunCTimeworktype model = new RunCTimeworktype();
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
	public void deleteTimeworktype() {
		if (delete()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'删 除 失 败 !'}");
		}
	}

	/**
	 * @return the timeworktypeInfo
	 */
	public RunCTimeworktype getTimeworktypeInfo() {
		return timeworktypeInfo;
	}

	/**
	 * @param timeworktypeInfo
	 *            the timeworktypeInfo to set
	 */
	public void setTimeworktypeInfo(RunCTimeworktype timeworktypeInfo) {
		this.timeworktypeInfo = timeworktypeInfo;
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

package power.web.run.timework.action;

import com.googlecode.jsonplugin.JSONUtil;
import power.web.comm.AbstractAction;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.timework.RunCTimeworkd;
import power.ejb.run.timework.RunCTimeworkdFacadeRemote;

public class TimeworkdAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String method;
	protected String classSequence;
	protected String id;
	protected String codes;
	protected RunCTimeworkd timeworkdInfo;
	protected String ids;
	protected String workitemcode;
	protected String workitemname;
	protected String startime;

	/**
	 * @return 传输过来的方法
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the classSequence
	 */
	public String getClassSequence() {
		return classSequence;
	}

	/**
	 * @param classSequence
	 *            the classSequence to set
	 */
	public void setClassSequence(String classSequence) {
		this.classSequence = classSequence;
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
	 * @return the codes
	 */
	public String getCodes() {
		return codes;
	}

	/**
	 * @param codes
	 *            the codes to set
	 */
	public void setCodes(String codes) {
		this.codes = codes;
	}

	/**
	 * @return the ids
	 */
	public String getIds() {
		return ids;
	}

	/**
	 * @param ids
	 *            the codes to set
	 */
	public void setIds(String ids) {
		this.ids = ids;
	}

	/**
	 * @return the workitemcode
	 */
	public String getWorkitemcode() {
		return workitemcode;
	}

	/**
	 * @param workitemcode
	 *            the workitemcode to set
	 */
	public void setWorkitemcode(String workitemcode) {
		this.workitemcode = workitemcode;
	}

	/**
	 * @return the workitemname
	 */
	public String getWorkitemname() {
		return workitemname;
	}

	/**
	 * @param workitemname
	 *            the workitemname to set
	 */
	public void setWorkitemname(String workitemname) {
		this.workitemname = workitemname;
	}

	/**
	 * @return the startime
	 */
	public String getStartime() {
		return startime;
	}

	/**
	 * @param startime
	 *            the startime to set
	 */
	public void setStartime(String startime) {
		this.startime = startime;
	}

	/**
	 * @return the timeworkdInfo
	 */
	public RunCTimeworkd getTimeworkdInfo() {
		return timeworkdInfo;
	}

	/**
	 * @param timeworkdInfo
	 *            the timeworkdInfo to set
	 */
	public void setTimeworkdInfo(RunCTimeworkd timeworkdInfo) {
		this.timeworkdInfo = timeworkdInfo;
	}

	protected RunCTimeworkdFacadeRemote remote;

	public TimeworkdAction() {
		remote = (RunCTimeworkdFacadeRemote) factory
				.getFacadeRemote("RunCTimeworkdFacade");
	}

	// 生成周期描述
	protected String cycleSequence() {
		String str = "";
		String classSequencestr = getClassSequence();
		if (timeworkdInfo.getMonth() > 0) {
			str += timeworkdInfo.getMonth().toString() + "月 ";
		} else {
			str += "每月 ";
		}
		if (timeworkdInfo.getWeekNo() == null || timeworkdInfo.getWeekNo() == 0) {
			str += timeworkdInfo.getTestDay().toString() + "号 ";
		} else {
			str += "第" + timeworkdInfo.getWeekNo().toString() + "周 ";
			if (!timeworkdInfo.getTestDay().toString().equals("1")) {
				str += "周"
						+ (Integer.parseInt(timeworkdInfo.getTestDay()
								.toString()) - 1) + " ";
			} else {
				str += "周日 ";
			}
		}
		str += classSequencestr;
		return str;
	}

	// 添加记录
	protected int insert() throws Exception {
		try {
			int i, j = 0;
			String str[] = codes.split(",");
			for (i = 0; i < str.length; i++) {
				timeworkdInfo.setEnterprisecode(employee.getEnterpriseCode());
				timeworkdInfo.setIsUse("Y");
				timeworkdInfo.setStatus("C");
				timeworkdInfo.setCycleSequence(cycleSequence());
				timeworkdInfo.setWorkItemCode(str[i]);
				j += (remote.save(timeworkdInfo)) ? 1 : 0;
			}
			return j;
		} catch (Exception e) {
			return 0;
		}
	}

	// 添加操作
	public void addTimeworkd() throws Exception {
		int i = insert();
		if (i > 0) {
			write("{success:true,eMsg:'成功添加" + i + "条记录'}");
		} else {
			write("{success:false,eMsg:'添 加 失 败 !'}");
		}
	}

	// 修改记录
	protected boolean update() {
		try {
			RunCTimeworkd model = new RunCTimeworkd();
			model = remote.findById(Long.parseLong(id));
			model.setClassSequence(timeworkdInfo.getClassSequence());
			model.setCycleSequence(cycleSequence());
			model.setMonth(timeworkdInfo.getMonth());
			model.setTestDay(timeworkdInfo.getTestDay());
			model.setWeekNo(timeworkdInfo.getWeekNo());
			remote.update(model);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 修改操作
	public void updateTimeworkd() {
		if (update()) {
			write("{success:true,eMsg:'',eMsg:''}");
		} else {
			write("{success:false,eMsg:'修 改 失 败 !'}");
		}
	}

	// 锁定记录
	protected boolean lock() {
		try {
			String[] str = ids.split(",");
			Long[] id = new Long[str.length];
			for (int i = 0; i < str.length; i++) {
				id[i] = Long.parseLong(str[i]);
				RunCTimeworkd model = new RunCTimeworkd();
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
	public void lockTimeworkd() {
		if (lock()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{success:false,eMsg:'锁 定 失 败 !'}");
		}
	}

	// 解锁记录
	protected boolean unlock() {
		try {
			String[] str = ids.split(",");
			Long[] id = new Long[str.length];
			for (int i = 0; i < str.length; i++) {
				id[i] = Long.parseLong(str[i]);
				RunCTimeworkd model = new RunCTimeworkd();
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
	public void unlockTimeworkd() {
		if (unlock()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{success:false,eMsg:'解 锁 失 败 !'}");
		}
	}

	// 删除记录
	protected boolean delete() {
		try {
			String[] str = ids.split(",");
			Long[] id = new Long[str.length];
			for (int i = 0; i < str.length; i++) {
				id[i] = Long.parseLong(str[i]);
				RunCTimeworkd model = new RunCTimeworkd();
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
	public void deleteTimeworkd() {
		if (delete()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{success:false,eMsg:'删 除 失 败 !'}");
		}
	}

	// 取定期工作列表
	public void getlistTimeworkd() throws Exception {
		PageObject obj = remote.findByIsUse((Object) ("Y"), workitemcode);
		for (Object m : obj.getList()) {
			RunCTimeworkd o = (RunCTimeworkd) m;
			o.setWorkItemCode(workitemname);
			o.setEnterprisecode(startime);
		}
		write(JSONUtil.serialize(obj));
	}

}

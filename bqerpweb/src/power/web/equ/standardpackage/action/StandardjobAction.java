package power.web.equ.standardpackage.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.web.comm.AbstractAction;
import power.ear.comm.ejb.PageObject;
import power.ejb.equ.standardpackage.EquCStandardjob;
import power.ejb.equ.standardpackage.EquCStandardjobFacadeRemote;
import power.ejb.equ.standardpackage.EquCStandardjobout;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;

@SuppressWarnings("serial")
public class StandardjobAction extends AbstractAction {
	protected String id;
	protected String ids;
	private int start;
	private int limit;
	protected EquCStandardjob standardInfo;

	protected EquCStandardjobFacadeRemote remote;
	protected RunCSpecialsFacadeRemote dll;

	private String code;

	protected String eMsg = "操 作 失 败 !";

	public StandardjobAction() {
		remote = (EquCStandardjobFacadeRemote) factory
				.getFacadeRemote("EquCStandardjobFacade");
		dll = (RunCSpecialsFacadeRemote) factory
				.getFacadeRemote("RunCSpecialsFacade");
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

	/**
	 * @return the standardInfo
	 */
	public EquCStandardjob getStandardInfo() {
		return standardInfo;
	}

	/**
	 * @param standardInfo
	 *            the standardInfo to set
	 */
	public void setStandardInfo(EquCStandardjob standardInfo) {
		this.standardInfo = standardInfo;
	}

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

	// 添加标准工作包
	protected boolean insert() {
		try {
			standardInfo.setCalnum(null);// 日历编码(备用）
			standardInfo.setCrewId(null);// 班组
			// standardInfo.setDescription(standardInfo.getDescription());// 描述
			standardInfo.setDownTime(null);// 是否必须为此工单停设备(备用）
			standardInfo.setEnterprisecode(employee.getEnterpriseCode());// 企业编码
			standardInfo.setInterruptable(null);// 能否中断(备用）
			standardInfo.setIsUse("Y");
			standardInfo.setStatus("C");
			// standardInfo.setJobCode(standardInfo.getJobCode());// 标准工作指令编码
			// standardInfo.setJopDuration(standardInfo.getJobCode());// 总工时
			// standardInfo.setLaborCode(null);// 工种代码(备用）
			// standardInfo.setMaintDep(null);// 检修单位
			// standardInfo.setPriority(standardInfo.getPriority());// 排序
			// standardInfo.setSpeciality(standardInfo.getSpeciality());// 专业
			// standardInfo.setSupervisor(null);// 主管人(备用）
			long i = remote.save(standardInfo);
			if (i > 0) {
				return true;
			} else if (i == 0) {
				eMsg = "编 码 重 复 !";
				return false;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	// 操作添加标准工作包
	public void addStandardjob() {
		if (insert()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'" + eMsg + "'}");
		}
	}

	// 修改标准工作包
	protected boolean update() {
		try {
			EquCStandardjob model = new EquCStandardjob();
			model = remote.findById(Long.parseLong(id));
			model.setDescription(standardInfo.getDescription());
			model.setJobCode(standardInfo.getJobCode());
			model.setSpeciality(standardInfo.getSpeciality());
			model.setJopDuration(standardInfo.getJopDuration());
			model.setPriority(standardInfo.getPriority());
			long i = remote.update(model);
			if (i > 0) {
				return true;
			} else if (i == 0) {
				eMsg = "编 码 重 复 !";
				return false;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	// 操作修改标准工作包
	public void editStandardjob() {
		if (update()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'" + eMsg + "'}");
		}
	}

	// 删除
	protected boolean delete() {
		try {
			String[] str = ids.split(",");
			Long[] id = new Long[str.length];
			for (int i = 0; i < str.length; i++) {
				id[i] = Long.parseLong(str[i]);
				EquCStandardjob model = new EquCStandardjob();
				model = remote.findById(id[i]);
				model.setIsUse("N");
				remote.update(model);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 操作删除标准工作包
	public void delStandardjob() {
		if (delete()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'删 除 失 败 !'}");
		}
	}

	// 锁定
	protected boolean lock() {
		try {
			String[] str = ids.split(",");
			Long[] id = new Long[str.length];
			for (int i = 0; i < str.length; i++) {
				id[i] = Long.parseLong(str[i]);
				EquCStandardjob model = new EquCStandardjob();
				model = remote.findById(id[i]);
				model.setStatus("L");
				remote.update(model);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 操作锁定标准工作包
	public void lockStandardjob() {
		if (lock()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'锁 定 失 败 !'}");
		}
	}

	// 解锁
	protected boolean unlock() {
		try {
			String[] str = ids.split(",");
			Long[] id = new Long[str.length];
			for (int i = 0; i < str.length; i++) {
				id[i] = Long.parseLong(str[i]);
				EquCStandardjob model = new EquCStandardjob();
				model = remote.findById(id[i]);
				model.setStatus("C");
				remote.update(model);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 操作解锁标准工作包
	public void unlockStandardjob() {
		if (unlock()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'解 锁 失 败 !'}");
		}
	}

	// 取标准工作包列表
	@SuppressWarnings("unchecked")
	public void getStandardjobList() throws JSONException {
		PageObject result = new PageObject();
		PageObject obj = remote.findByIsuse((Object) ("Y"), start, limit);
		List list = obj.getList();
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			EquCStandardjob model = new EquCStandardjob();
			EquCStandardjobout omodel = new EquCStandardjobout();
			Object[] data = (Object[]) it.next();
			model.setJobId(Long.parseLong(data[0].toString()));
			model.setJobCode(data[1].toString());
			model.setCode(data[2].toString());
			if (data[3] != null) {
				model.setDescription(data[3].toString());
			}
			if (data[4] != null) {
				model.setPriority(Long.parseLong(data[4].toString()));
			}
			if (data[5] != null) {
				model.setJopDuration(Double.parseDouble(data[5].toString()));
			}
			if (data[6] != null) {
				model.setMaintDep(data[6].toString());
			}
			if (data[7] != null) {
				model.setSpeciality(data[7].toString());
			}
			if (data[8] != null) {
				model.setCrewId(data[8].toString());
			}
			if (data[9] != null) {
				model.setInterruptable(Long.parseLong(data[9].toString()));
			}
			if (data[10] != null) {
				model.setDownTime(Long.parseLong(data[10].toString()));
			}
			if (data[11] != null) {
				model.setSupervisor(data[11].toString());
			}
			if (data[12] != null) {
				model.setLaborCode(data[12].toString());
			}
			if (data[13] != null) {
				model.setCalnum(data[13].toString());
			}
			if (data[14] != null) {
				model.setStatus(data[14].toString());
			}
			if (data[15] != null) {
				model.setEnterprisecode(data[15].toString());
			}
			if (data[16] != null) {
				model.setIsUse(data[16].toString());
			}
			if (data[17] != null) {
				omodel.setSpecialityName(data[17].toString());
			}
			omodel.setStandardInfo(model);
			arrlist.add(omodel);
		}
		result.setList(arrlist);
		result.setTotalCount(obj.getTotalCount());

		write(JSONUtil.serialize(result));
	}

	public void findRunSpecialsByCode() throws JSONException {
		String enterpriseCode = "hfdc";
		RunCSpecials model = dll.findByCode("ZZ", enterpriseCode);
		String str = JSONUtil.serialize(model);
		System.out.println(str);
		write(str);
	}

	// 生成专业树
	public void findRunSpecials() throws JSONException {
		String code = request.getParameter("id");
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("[");
		// List<RunCSpecials> list = dll.getListByParent(code, employee
		// .getEnterpriseCode());
		List<RunCSpecials> list = dll.getListByParent(code, "hfdc");

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				RunCSpecials spe = list.get(i);
				boolean isLeaf = dll.isParentNode(spe.getSpecialityCode(),
						"hfdc");
				String icon = isLeaf ? "file" : "folder";
				JSONStr.append("{id:'" + spe.getSpecialityCode() + "',text:'"
						+ spe.getSpecialityName() + "',leaf:" + isLeaf
						+ ",iconCls:'" + icon + "'},");
			}
			if (JSONStr.length() > 1) {
				JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
			}
		}
		JSONStr.append("]");
		System.out.println(JSONStr);
		write(JSONStr.toString());
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

}

package power.web.equ.standardpackage.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.standardpackage.EquCRealatequ;
import power.ejb.equ.standardpackage.EquCRealatequFacadeRemote;
import power.ejb.equ.standardpackage.EquCRealatequout;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class RealequAction extends AbstractAction {
	protected String ids;
	protected String code;
	protected String eMsg = "操 作 失 败 !";
	protected EquCRealatequFacadeRemote remote;

	public RealequAction() {
		remote = (EquCRealatequFacadeRemote) factory
				.getFacadeRemote("EquCRealatequFacade");
	}

	@SuppressWarnings("unchecked")
	public void getRealequList() throws JSONException {
		try {
			PageObject result = new PageObject();
			PageObject obj = remote.findByIsuse((Object) ("Y"), code);
			List list = obj.getList();
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				EquCRealatequ model = new EquCRealatequ();
				EquCRealatequout omodel = new EquCRealatequout();
				Object[] data = (Object[]) it.next();
				model.setEquId(Long.parseLong(data[0].toString()));
				model.setCode(data[1].toString());
				model.setAttributeCode(data[2].toString());
				model.setStatus(data[3].toString());
				model.setEnterprisecode(data[4].toString());
				model.setIsUse(data[5].toString());
				omodel.setEquname(data[6].toString());
				omodel.setRealatequInfo(model);
				arrlist.add(omodel);
			}
			result.setList(arrlist);
			result.setTotalCount(obj.getTotalCount());
			write(JSONUtil.serialize(result));
		} catch (Exception e) {
		}
	}

	// 添加标准工作包
	protected boolean insert() {
		try {
			String[] str = ids.split(",");
			for (int i = 0; i < str.length; i++) {
				EquCRealatequ model = new EquCRealatequ();
				model.setAttributeCode(str[i]);// 存入的设备码
				model.setCode(code);// 存入关联码
				model.setIsUse("Y");// 删除
				model.setStatus("C");// 状态
				model.setEnterprisecode(employee.getEnterpriseCode());// 企业编码
				remote.save(model);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void addRealequ() {
		if (insert()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'添 加 失 败 !'}");
		}
	}

	protected boolean delete() {
		try {
			String[] str = ids.split(",");
			for (int i = 0; i < str.length; i++) {
				EquCRealatequ model = new EquCRealatequ();
				model = remote.findById(Long.parseLong(str[i]));
				model.setIsUse("N");// 删除
				remote.update(model);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void delRealequ() {
		if (delete()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'删 除 失 败 !'}");
		}
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
	 * @return the remote
	 */
	public EquCRealatequFacadeRemote getRemote() {
		return remote;
	}

	/**
	 * @param remote
	 *            the remote to set
	 */
	public void setRemote(EquCRealatequFacadeRemote remote) {
		this.remote = remote;
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

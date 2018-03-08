package power.web.run.securityproduction.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.comm.TreeNode;
import power.ejb.equ.base.EquCEquipments;
import power.ejb.equ.base.EquCInstallation;
import power.ejb.equ.base.EquCInstallationFacadeRemote;
import power.ejb.equ.base.EquCLocation;
import power.ejb.equ.base.EquCLocationFacadeRemote;
import power.ejb.run.securityproduction.SpCBoiler;
import power.ejb.run.securityproduction.SpCBoilerFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.UploadFileAbstractAction;

@SuppressWarnings("serial")
public class SafeSuperviseAction extends UploadFileAbstractAction {
	protected SpCBoilerFacadeRemote remote;
	@SuppressWarnings("unused")
	private SpCBoiler boiler;
	private File annex;

	/**
	 * 构造函数
	 */
	public SafeSuperviseAction() {
		remote = (SpCBoilerFacadeRemote) factory
				.getFacadeRemote("SpCBoilerFacade");
	}

	/**
	 * 获得设备树
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getTreeListForBoiler() throws JSONException {
		String code = "";
		code = request.getParameter("id");
		List<TreeNode> arrayList = new ArrayList();
		List<SpCBoiler> list = remote.getListByParent(code, employee
				.getEnterpriseCode());
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				SpCBoiler boiler = list.get(i);
				TreeNode model = new TreeNode();
				boolean isLeaf = !remote.IfHasChild(boiler.getBoilerId(),
						employee.getEnterpriseCode());
				model.setId(boiler.getBoilerId().toString());
				model.setText(/*boiler.getBoilerId() + " "	+*/ boiler.getBoilerName());//modify by wpzhu 100426
				model.setLeaf(isLeaf);
				model.setCode(boiler.getBoilerType());// add by wpzhu 100420

				arrayList.add(model);
			}
		}
		write(JSONUtil.serialize(arrayList));
	}

	public void getTreeForBoiler() {
	}

	public void getInfoBoilerByCode() throws JSONException {
		String code = "";
		code = request.getParameter("id");
		boiler = new SpCBoiler();
		boiler = remote.findByCode(code, employee.getEnterpriseCode());
		write(JSONUtil.serialize(boiler));
	}

	public void addOrUpdateBoilerInfo() {
		String methode = request.getParameter("method");
		if (methode.equals("add")) {
			addBoilerInfo();
		} else {
			updateBoilerInfo();
		}

	}

	public void addBoilerInfo() {
		String filePath = request.getParameter("filePath");
		String flag = request.getParameter("flag");
		if (!filePath.equals("")) {
			String result = filePath.substring(filePath.lastIndexOf("\\") + 1);
			String fileName = result.replaceAll(" ", "");
			String[] filetemp = fileName.split("\\.");
			if (filetemp[1].equals("txt")) {
				filetemp[1] = ".doc";
				fileName = filetemp[0] + filetemp[1];
			}
			String Temp = uploadFile(annex, fileName, "project");
			boiler.setAnnex(Temp);
		}
		if(flag != null && flag.equals("root"))
		{
			boiler.setFBoilerId(0l);
		}
		boiler.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(boiler);
		write("{success:true,msg:'增加成功！'}");
	}

	public void updateBoilerInfo() {
		String filePath = request.getParameter("filePath");
		SpCBoiler model = new SpCBoiler();
		model = remote.findById(Long.parseLong(boiler.getBoilerId().toString()));
		if (!filePath.equals("")) {
			if (!(model.getAnnex()!=null&& filePath.equals(model.getAnnex()))) {
				String result = filePath
						.substring(filePath.lastIndexOf("\\") + 1);
				String fileName = result.replaceAll(" ", "");
				String[] filetemp = fileName.split("\\.");
				if (filetemp[1].equals("txt")) {
					filetemp[1] = ".doc";
					fileName = filetemp[0] + filetemp[1];
				}
				String Temp = uploadFile(annex, fileName, "project");
				model.setAnnex(Temp);
			}
		}
		model.setBoilerName(boiler.getBoilerName());
		model.setBoilerType(boiler.getBoilerType());
		model.setManufacturer(boiler.getManufacturer());
		model.setBoilerNumber(boiler.getBoilerNumber());
		if (remote.update(model)) {
			write("{success:true,msg:'修改成功！'}");
		} else {
			write("{success:true,msg:'修改失败:编码重复！'}");
		}
	}

	public void deleteBoilerInfoById() {
		SpCBoiler model = new SpCBoiler();
		String code = "";
		code = request.getParameter("id");
		if (!remote.IfHasChild(Long.parseLong(code), employee
				.getEnterpriseCode())) {
			model = remote.findByCode(code, employee.getEnterpriseCode());
			model.setIsUse("N");
			remote.update(model);
			write("{success:true,msg:'删除成功！'}");
		} else {
			write("{success:true,msg:'目录不能直接删除！'}");
		}
	}

	public void saveBoilerInfo() {
	}

	public SpCBoiler getBoiler() {
		return boiler;
	}

	public void setBoiler(SpCBoiler boiler) {
		this.boiler = boiler;
	}

	public File getAnnex() {
		return annex;
	}

	public void setAnnex(File annex) {
		this.annex = annex;
	}

}

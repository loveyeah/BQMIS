/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.materialqueryandselect.action;

import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.business.MaterialQueryAndSelect;
import power.ejb.resource.form.MaterialQueryAndSelectInfo;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 库存物料查询及选择
 * 
 * @author jincong
 * @version 1.0
 */
public class MaterialQueryAndSelectAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 库存物料查询 */
	private MaterialQueryAndSelect remote;
	/** 单位接口 */
	private BpCMeasureUnitFacadeRemote unitRemote;
	
	/**
	 * 构造函数
	 */
	public MaterialQueryAndSelectAction() {
		// 库存物料查询
		remote = (MaterialQueryAndSelect) factory
				.getFacadeRemote("MaterialQueryAndSelectImpl");
		// 单位接口
		unitRemote = (BpCMeasureUnitFacadeRemote) factory
				.getFacadeRemote("BpCMeasureUnitFacade");
	}

	/**
	 * 库存物料查询
	 * modify by fyyang 090629 增加物料类别查询条件
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	public void getMaterialQueryAndSelect() throws JSONException {
		PageObject object = new PageObject();
		String materialClassCode=request.getParameter("materialClassCode");
		// 取得查询参数: 模糊查询字段
		String strFuzzyText = request.getParameter("fuzzyText");
		// 取得查询参数: 开始行数
		int intStart = Integer.parseInt(request.getParameter("start"));
		// 取得查询参数: 查询行数
		int intLimit = Integer.parseInt(request.getParameter("limit"));
		// 查询
		object = remote.findMaterial(strFuzzyText, employee.getEnterpriseCode(),materialClassCode,
				intStart, intLimit);
		// 输出
		String strOutput = "";
		if(object != null && (object.getList().size() > 0)) {
			List<MaterialQueryAndSelectInfo> list = object.getList();
			for(int i = 0; i < list.size(); i++) {
				String id = list.get(i).getStockUmId();
				if(id == null) {
					list.get(i).setStockUmId("");
				} else {
					BpCMeasureUnit bpCMeasureUnit = unitRemote.findById(Long.parseLong(id));
					if(bpCMeasureUnit != null) {
						list.get(i).setStockUmId(bpCMeasureUnit.getUnitName());
					} else {
						list.get(i).setStockUmId("");
					}
				}
			}
			strOutput = JSONUtil.serialize(object);
		} else {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		}
		write(strOutput);
	}
	
	/**
	 * 库存物料查询
	 * 
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	public void getMaterialQueryByMaterialNo() throws JSONException {
		PageObject object = new PageObject();
		// 取得查询参数: 物资编码
		String materialNo = request.getParameter("materialNo");
		// 取得查询参数: 开始行数
		int intStart = Integer.parseInt(request.getParameter("start"));
		// 取得查询参数: 查询行数
		int intLimit = Integer.parseInt(request.getParameter("limit"));
		// 查询
		object = remote.findMaterialByMaterialNo(materialNo, employee.getEnterpriseCode(),
				intStart, intLimit);
		// 输出
		String strOutput = "";
		if(object != null && (object.getList().size() > 0)) {
			List<MaterialQueryAndSelectInfo> list = object.getList();
			for(int i = 0; i < list.size(); i++) {
				String id = list.get(i).getStockUmId();
				if(id == null) {
					list.get(i).setStockUmId("");
				} else {
					BpCMeasureUnit bpCMeasureUnit = unitRemote.findById(Long.parseLong(id));
					if(bpCMeasureUnit != null) {
						list.get(i).setStockUmId(bpCMeasureUnit.getUnitName());
					} else {
						list.get(i).setStockUmId("");
					}
				}
			}
			strOutput = JSONUtil.serialize(object);
		} else {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		}
		write(strOutput);
	}
	
	
}

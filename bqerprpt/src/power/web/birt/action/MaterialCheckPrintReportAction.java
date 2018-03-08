/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.birt.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.InvCMaterial;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.ejb.resource.MaterialCheckPrintReportBean;
import power.ejb.resource.MaterialCheckPrintReportListBean;
import power.ejb.resource.PurCPlaner;
import power.ejb.resource.PurCPlanerFacadeRemote;
import power.ejb.resource.business.MaterialPrint;
import power.web.birt.constant.Constant;
import power.web.birt.constant.commUtils;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;


/**
 * 物料盘点打印表Action
 * @author zhujie
 *
 */
public class MaterialCheckPrintReportAction extends AbstractAction{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 远程 */
	private MaterialPrint remote;
	/** 计划员维护Remote */
	private PurCPlanerFacadeRemote planerRemote;
	/** 物料Remote */
	private InvCMaterialFacadeRemote materialRemote;
	/** 远程 */
	private BpCMeasureUnitFacadeRemote remoteComm;

	/**
	 * 构造函数
	 */
	public MaterialCheckPrintReportAction(){
		remote = (MaterialPrint)factory.getFacadeRemote("MaterialPrintImp");
		// 计划员维护Remote
		planerRemote = (PurCPlanerFacadeRemote) factory
				.getFacadeRemote("PurCPlanerFacade");
		// 物料Remote
		materialRemote = (InvCMaterialFacadeRemote) factory
				.getFacadeRemote("InvCMaterialFacade");
		remoteComm = (BpCMeasureUnitFacadeRemote)factory.getFacadeRemote("BpCMeasureUnitFacade");
	}

	/**
	 * 物料盘点打印表
	 *
	 * @param delayStore 待盘仓库
	 * @param delayLocation 待盘库位
	 * @param list1 待盘物料分类1
	 * @param list2 待盘物料分类2
	 * @param delayMaterial 待盘物料
	 * @param planer 计划员
	 * @param enterpriseCode 企业编码
     * @return MaterialCheckPrintReportBean 物料盘点打印表明细
	 * @throws JSONException
	 * @throws JSONException
	 */
	public MaterialCheckPrintReportBean getMaterialCheckPrintReportBean(String delayStore,String delayLocation,
			String delayMaterial,String planer,String materialSortId,
			String enterpriseCode) throws JSONException {
		MaterialCheckPrintReportBean entity = new MaterialCheckPrintReportBean();
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		isMaterialSort(sb1,sb2,planer,materialSortId,enterpriseCode);
		entity = remote.findAllForMaterialCheckReport(delayStore,delayLocation, sb1.toString(),
				sb2.toString(), delayMaterial, planer, materialSortId, enterpriseCode);

		List<MaterialCheckPrintReportListBean> lstMaterial = entity.getMaterialCheckPrintReportList();
		if((lstMaterial != null)&&(lstMaterial.size() > 0)){
			MaterialCheckPrintReportListBean lstBean = null;
			// 详细中添加行数计数
			for(int cnt = 0; cnt < lstMaterial.size(); cnt++){
				int countRow = 0;
		    	List<String> strList = new ArrayList();
		    	lstBean = lstMaterial.get(cnt);
		    	// 处理物料名称
		    	String strNewMaterialName = commUtils.addBrByByteLengthForMaterial(lstBean.getMaterialName(),
		    			Constant.EIGHTPOINTPERSETY16);
		    	lstBean.setMaterialName(strNewMaterialName);
		    	strList.add(strNewMaterialName);
		    	// 处理物料编码
		    	String strNewMaterialNo = commUtils.addBrByByteLengthForMaterial(lstBean.getMaterialNo(),
		    			Constant.EIGHTPOINTPERSETY12);
		    	lstBean.setMaterialNo(strNewMaterialNo);
		    	strList.add(strNewMaterialNo);
		    	// 处理规格型号
		    	String strNewMaterialSpecNo = commUtils.addBrByByteLengthForMaterial(lstBean.getSpecNo(),
		    			Constant.EIGHTPOINTPERSETY11);
		    	lstBean.setSpecNo(strNewMaterialSpecNo);
		    	strList.add(strNewMaterialSpecNo);
		    	// 处理单位
		    	if(lstBean.getStockUmID()!=null&&!"".equals(lstBean.getStockUmID())){
		    		BpCMeasureUnit bcmu = remoteComm.findById(Long.parseLong(lstBean.getStockUmID()));
		    		if(bcmu!=null){
		    			String stockUmID = bcmu.getUnitName();
		    			String strNewMaterialStockUmID = commUtils.addBrByByteLengthForMaterial(stockUmID,
		    					Constant.EIGHTPOINTPERSETY6);
		    			lstBean.setStockUmID(strNewMaterialStockUmID);
		    			strList.add(strNewMaterialStockUmID);
		    		}
		    	}
		    	// 处理批号
		    	String strNewMaterialLotNo = commUtils.addBrByByteLengthForMaterial(lstBean.getLotNo(),
		    			Constant.EIGHTPOINTPERSETY8);
		    	lstBean.setLotNo(strNewMaterialLotNo);
		    	strList.add(strNewMaterialLotNo);
		    	// 处理仓库
		    	String strNewMaterialWasName = commUtils.addBrByByteLengthForMaterial(lstBean.getWhsName(),
		    			Constant.EIGHTPOINTPERSETY10);
		    	lstBean.setWhsName(strNewMaterialWasName);
		    	strList.add(strNewMaterialWasName);
		    	// 处理库位
		    	String strNewMaterialLocationName = commUtils.addBrByByteLengthForMaterial(lstBean.getLocationName(),
		    			Constant.EIGHTPOINTPERSETY10);
		    	lstBean.setLocationName(strNewMaterialLocationName);
		    	strList.add(strNewMaterialLocationName);
		    	// 叠加后的计数
		    	lstBean.setCntRow(commUtils.countMaxContain(strList, Constant.HTML_CHANGE_LINE) + 1);
			}
		}
		entity.setMaterialCheckPrintReportList(lstMaterial);
		// 设置当前日期
		SimpleDateFormat mFormatTimeOnly = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		Date dDate = new Date();
		String strTime = mFormatTimeOnly.format(dDate);
		String chinaTime = commUtils.formatTimeYMD(strTime);
		entity.setNowDate(chinaTime);
		return entity;
	}

	/**
	 * @param sb1
	 * @param sb2
	 * @param planer
	 * @param materialSortId
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void isMaterialSort(StringBuilder sb1,StringBuilder sb2,String planer,String materialSortId,String enterpriseCode) throws JSONException {
		if(!"".equals(planer)) {
			// 创建一个对象
			PageObject object = new PageObject();
			// 调用函数
			object = planerRemote.findByPlaner(planer, enterpriseCode);
			// 获取信息
			List<PurCPlaner> list = object.getList();
			int i = 0;
			if(list != null && list.size() > 0) {
				// 把list里的数据取出
				for (PurCPlaner model : list) {
					i++;
					// 如果是否物料分类是"N",就直接把编码放到list中
					if(model.getIsMaterialClass().equals("N")) {
						sb1.append("'");
						sb1.append(model.getMaterialOrClassNo());
						sb1.append("'");
					} else if(model.getIsMaterialClass().equals("Y")){
						// 如果是否物料分类是"Y",就要先进行查询
						List<InvCMaterial> list1 = materialRemote.findAllChildrenNode(model.getMaterialOrClassNo(),
								enterpriseCode);
						int j = 0;
						for (InvCMaterial model1 : list1) {
							j++;
							if(model1.getMaterialNo() != null) {
								sb1.append("'");
								sb1.append(model1.getMaterialNo());
								sb1.append("'");
							}
							if (j < list1.size()) {
								sb1.append(",");
							}
						}
					}
					if (i < list.size()) {
						sb1.append(",");
					}
				}
			}
		}
		// 如果待盘物料类别的编码不为空的话
		if(!"".equals(materialSortId)) {
			int k = 0;
			List<InvCMaterial> list2 = materialRemote.findAllChildrenNode(materialSortId,
					enterpriseCode);

			for (InvCMaterial model2 : list2) {
				k++;
				if(model2.getMaterialNo() != null) {
					sb2.append("'");
					sb2.append(model2.getMaterialNo());
					sb2.append("'");
				}
				if (k < list2.size()) {
					sb2.append(",");
				}
			}
		}
	}
}

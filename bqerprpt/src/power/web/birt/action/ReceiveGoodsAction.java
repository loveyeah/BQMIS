/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.birt.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.PurJArrivalDetailsFacadeRemote;
import power.ejb.resource.ReceiveGoodsBean;
import power.ejb.resource.ReceiveGoodsListBean;
import power.web.birt.constant.Constant;
import power.web.birt.constant.commUtils;
import power.web.comm.AbstractAction;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * 到货登记表Action
 * @author zhujie
 *
 */
public class ReceiveGoodsAction extends AbstractAction{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 远程 */
	private PurJArrivalDetailsFacadeRemote remote;
	/** 远程 */
	private BpCMeasureUnitFacadeRemote remoteComm;

	/**
	 * 构造函数
	 */
	public ReceiveGoodsAction(){
		remote = (PurJArrivalDetailsFacadeRemote)factory.getFacadeRemote("PurJArrivalDetailsFacade");
		remoteComm = (BpCMeasureUnitFacadeRemote)factory.getFacadeRemote("BpCMeasureUnitFacade");
	}

	/**
	 * 取得到货单明细
	 *
	 * @param
	 * @return electricOneBean 到货单明细
	 */
	public ReceiveGoodsBean setReceiveGoodsBean(String mifNo,String enterpriseCode){
		ReceiveGoodsBean entity = new ReceiveGoodsBean();
		entity = remote.findForReceiveGoods(mifNo, enterpriseCode);

		List<ReceiveGoodsListBean> lstReceiveGoods = entity.getReceiveGoodsList();

		if((lstReceiveGoods != null)&&(lstReceiveGoods.size() > 0)){
			ReceiveGoodsListBean lstBean = null;
		    // 详细中添加行数计数

		    for(int cnt = 0; cnt < lstReceiveGoods.size(); cnt++){
		    	int countRow = 0;
		    	List<String> strList = new ArrayList();
		    	lstBean = lstReceiveGoods.get(cnt);
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
		    	String strNewMaterialbatchNumber = commUtils.addBrByByteLengthForMaterial(lstBean.getBatchNumber(),
		    			Constant.EIGHTPOINTPERSETY8);
		    	lstBean.setBatchNumber(strNewMaterialbatchNumber);
		    	strList.add(strNewMaterialbatchNumber);
		    	// 叠加后的计数
		    	lstBean.setCntRow(commUtils.countMaxContain(strList, Constant.HTML_CHANGE_LINE) + 1);
		    }
		}
		entity.setReceiveGoodsList(lstReceiveGoods);

		// 设置当前日期
		SimpleDateFormat mFormatTimeOnly = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		Date dDate = new Date();
		String strTime = mFormatTimeOnly.format(dDate);
		String chinaTime = commUtils.formatTimeYMD(strTime);
		entity.setNowDate(chinaTime);
		// 设置事务作用码
		entity.setBusinessCode(Constant.RECEIVEGOODS_BUISENESSCODE);
		// 设置事务作用名称
		entity.setBusinessName(Constant.RECEIVEGOODS_BUISENESSNAME);
		return entity;
	}
}

/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.hotelRoomPrice.action;

import java.text.ParseException;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJRoomPrice;
import power.ejb.administration.AdJRoomPriceFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 宾馆房间价格维护
 * 
 * @author zhaozhijie
 * @version 1.0
 */
public class HotelRoomPriceAction  extends AbstractAction{

	/** 宾馆房间价格remote */
	private AdJRoomPriceFacadeRemote remote;
	/** 宾馆房间价格bean */
	private AdJRoomPrice adJRoomPrice;
	/** 房间类别编码 */
	private String roomTypeCode;


	/**
	 * 构造函数
	 */
	public HotelRoomPriceAction() {
		// remote
		remote = (AdJRoomPriceFacadeRemote) factory.getFacadeRemote("AdJRoomPriceFacade");
	}

	/**
	 * 检索房间价格
	 * 
	 * @exception JSONException 
	 * @throws ParseException 
	 */
	public void getRoomPrice() throws JSONException, ParseException {
		LogUtil.log("Action:宾馆房间价格维护检索房间价格加载正常开始", Level.INFO, null);
		PageObject pobj = new PageObject();
		AdJRoomPrice adJRoomPrice = new AdJRoomPrice();
		try {
			pobj = remote.findByRoomTypeCode(roomTypeCode);
		} catch (ParseException e) {
			LogUtil.log("Action:宾馆房间价格维护检索房间价格加载未知例外", Level.INFO, null);
			throw e;
		}	

		// 解析字符串
		String string = null;
		 if(pobj == null) {
			 string = "";
		 } else {
			 if (pobj.getList() != null) {
				 adJRoomPrice = (AdJRoomPrice)pobj.getList().get(0);
				 string = JSONUtil.serialize(adJRoomPrice);
			 } else {
				 string = "";
			 }
		 }
		LogUtil.log("Action:宾馆房间价格维护检索房间价格加载正常结束", Level.INFO, null);
 		write(string);

	}

	/**
	 * 保存房间价格
	 * 
	 * @exception JSONException 
	 */
	public void updateRoomPrice() throws JSONException {

		LogUtil.log("Action:宾馆房间价格维护更新房间价格加载正常开始", Level.INFO, null);
		AdJRoomPrice adjRoomPrice = new AdJRoomPrice();
		Long lngId = adJRoomPrice.getId();
		if (lngId != null) {
			adjRoomPrice = remote.findById(lngId);	
			if(adjRoomPrice!= null) {
				// 修改时间
				adjRoomPrice.setUpdateTime(new java.util.Date());
				// 修改用户
				adjRoomPrice.setUpdateUser(employee.getWorkerCode());
				// 是否可用
				adjRoomPrice.setIsUse("N");
				remote.update(adjRoomPrice);
				LogUtil.log("Action:宾馆房间价格维护删除房间价格加载正常结束", Level.INFO, null);
			} 
		}

		AdJRoomPrice saveAdJRoomPrice = new AdJRoomPrice();

		// 房间类别编码
		saveAdJRoomPrice.setRoomTypeCode(adJRoomPrice.getRoomTypeCode());
		// 房间类别名称
		saveAdJRoomPrice.setRoomTypeName(adJRoomPrice.getRoomTypeName());
		// 价格
		if (adJRoomPrice.getPrice() != null) {
			saveAdJRoomPrice.setPrice(adJRoomPrice.getPrice());
		} else {
			saveAdJRoomPrice.setPrice(Double.parseDouble("0"));
		}

		// 修改时间
		saveAdJRoomPrice.setUpdateTime(new java.util.Date());
		// 修改人
		saveAdJRoomPrice.setUpdateUser(employee.getWorkerCode());
		// 是否可用
		saveAdJRoomPrice.setIsUse("Y");
		remote.save(saveAdJRoomPrice);
		LogUtil.log("Action:宾馆房间价格维护更新房间价格加载正常开始", Level.INFO, null);
 		write(Constants.ADD_SUCCESS);
	}

	/**
	 * @return 价格
	 */
	public AdJRoomPrice getAdJRoomPrice() {
		return adJRoomPrice;
	}

	/**
	 * @param 价格
	 */
	public void setAdJRoomPrice(AdJRoomPrice adJRoomPrice) {
		this.adJRoomPrice = adJRoomPrice;
	}

	/**
	 * @return 房间类别编码
	 */
	public String getRoomTypeCode() {
		return roomTypeCode;
	}

	/**
	 * @param 房间类别编码
	 */
	public void setRoomTypeCode(String roomTypeCode) {
		this.roomTypeCode = roomTypeCode;
	}


}

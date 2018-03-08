package power.web.productiontec.relayProtection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.relayProtection.PtJdbhJDzdjb;
import power.ejb.productiontec.relayProtection.PtJdbhJDztzd;
import power.ejb.productiontec.relayProtection.PtJdbhJDztzdFacadeRemote;
import power.ejb.productiontec.relayProtection.form.ProtectNoticeForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ProtectionNoticeAction extends AbstractAction{

	private PtJdbhJDztzdFacadeRemote remote;
	private PtJdbhJDztzd notice;
	private PtJdbhJDzdjb noticeDetail;
	
	public ProtectionNoticeAction()
	{
		remote = (PtJdbhJDztzdFacadeRemote)factory.getFacadeRemote("PtJdbhJDztzdFacade");
	}
	
	/**
	 * 增加，修改操作作
	 * @param map
	 * @param tempNotice
	 * @return
	 * @throws JSONException 
	 */
	public void addAndUpdateRecord() throws JSONException {
		
			String addOrUpdateRecords = request.getParameter("addOrUpdateRecords");
			String dztzdId = request.getParameter("dztzdId");
			PtJdbhJDztzd tempNotice = notice;
			tempNotice.setEnterpriseCode(employee.getEnterpriseCode());
			tempNotice.setSaveMark("N");//未存档
			if (addOrUpdateRecords != null) {
				List<ProtectNoticeForm> list = parseContent(addOrUpdateRecords,tempNotice);
				try {
					remote.modifyRecords(list, dztzdId);
					write("{success:true,msg:'操作成功！'}");
				} catch (CodeRepeatException e) {
					write("{success:true,msg:'" + e.getMessage() + "'}");
				}
			}
	
	}
	
	@SuppressWarnings("unchecked")
	private List<ProtectNoticeForm> parseContent(String records,PtJdbhJDztzd tempNotice) throws JSONException
	{
		List<ProtectNoticeForm> result = new ArrayList<ProtectNoticeForm>();
		Object object = JSONUtil.deserialize(records);
		List list=(List)object;
		int intLen = list.size();
		for(int i=0;i<intLen;i++){
			Map map=(Map)list.get(i);
			ProtectNoticeForm m = this.parseContentModal(map, tempNotice);
			result.add(m);
		}
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	private ProtectNoticeForm parseContentModal(Map map,PtJdbhJDztzd tempNotice)
	{
		ProtectNoticeForm model = new ProtectNoticeForm();
		PtJdbhJDztzd falgNotice = new PtJdbhJDztzd();
		
		Object dztzdId = map.get("notice.dztzdId");
		if(dztzdId != null && !(dztzdId.equals("")))
			falgNotice.setDztzdId(Long.parseLong(dztzdId.toString()));
		Object deviceId = map.get("notice.deviceId");
		if(deviceId != null && !(deviceId.equals("")))
			falgNotice.setDeviceId(Long.parseLong(deviceId.toString()));
		Object dzjssmId = map.get("notice.dzjssmId");
		if(dzjssmId != null && !(dzjssmId.equals("")))
			falgNotice.setDzjssmId(Long.parseLong(falgNotice.toString()));
		Object dztzdCode = map.get("notice.dztzdCode");
		if(dztzdCode != null && !(dztzdCode.equals("")))
			falgNotice.setDztzdCode(dztzdCode.toString());
		Object ctCode = map.get("notice.ctCode");
		if(ctCode != null && !(ctCode.equals("")))
			falgNotice.setCtCode(ctCode.toString());
		Object ptCode= map.get("notice.ptCode");
		if(ptCode != null && !(ptCode.equals("")))
			falgNotice.setPtCode(ptCode.toString());
		Object memo = map.get("notice.memo");
		if(memo != null && !(memo.equals("")))
			falgNotice.setMemo(memo.toString());
		Object fillBy = map.get("notice.fillBy");
		if(fillBy != null && !(fillBy.equals("")))
			falgNotice.setFillBy(fillBy.toString());
		Object useStatus = map.get("notice.useStatus");
		if(useStatus != null && !(useStatus.equals("")))
			falgNotice.setUseStatus(useStatus.toString());
		Object saveBy = map.get("notice.saveBy");
		if(saveBy != null && !(saveBy.equals("")))
			falgNotice.setSaveBy(saveBy.toString());
		Object saveMark = map.get("notice.saveMark");
		if(saveMark != null && !(saveMark.equals("")))
			falgNotice.setSaveMark(saveMark.toString());
		Object enterpriseCode = map.get("notice.enterpriseCode");
		if(enterpriseCode != null && !(enterpriseCode.equals("")))
			falgNotice.setEnterpriseCode(enterpriseCode.toString());
		
		Object effectiveDate = map.get("effectiveDate");
		Object fillName = map.get("fillName");
		Object saveName = map.get("saveName");
		Object equName = map.get("equName");
		Object jssmName = map.get("jssmName");
		
		Object dzdjbId = map.get("dzdjbId");
		Object fixvalueItemId = map.get("fixvalueItemId");
		Object protectTypeId = map.get("protectTypeId");
		Object protectTypeName = map.get("protectTypeName");
		Object fixvalueItemName = map.get("fixvalueItemName");
		Object wholeFixvalue = map.get("wholeFixvalue");
		Object memoDetail = map.get("memo");
		Object enterpriseCodeDetail = map.get("enterpriseCode");
		
		if(falgNotice.getDztzdId() != null && !(falgNotice.getDztzdId().toString().endsWith("")))
		{
			model.setNotice(falgNotice);
		}else{
			model.setNotice(tempNotice);
		}
		if(enterpriseCodeDetail != null)
			model.setEnterpriseCode(employee.getEnterpriseCode());
		if(memoDetail != null)
			model.setMemo(memoDetail.toString());
		if(effectiveDate != null)
			model.setEffectiveDate(effectiveDate.toString());
		if(fillName != null)
			model.setFillName(fillName.toString());
		if(saveName != null)
			model.setSaveName(saveName.toString());
		if(equName != null)
			model.setEquName(equName.toString());
		if(jssmName != null)
			model.setJssmName(jssmName.toString());
	
		if(dzdjbId != null)
			model.setDzdjbId(Long.parseLong(dzdjbId.toString()));
		if(fixvalueItemId != null)
			model.setFixvalueItemId(Long.parseLong(fixvalueItemId.toString()));
		if(protectTypeId != null)
			model.setProtectTypeId(Long.parseLong(protectTypeId.toString()));
		if(protectTypeName != null)
			model.setProtectTypeName(protectTypeName.toString());
		if(fixvalueItemName != null)
			model.setFixvalueItemName(fixvalueItemName.toString());
		if(wholeFixvalue != null)
			model.setWholeFixvalue(wholeFixvalue.toString());
			
		return model;
	}
	
	/**
	 * 继电保护定值通知单列表
	 * @throws JSONException
	 */
	public void findRelayProtectionNoticeList() throws JSONException
	{
		String equName = request.getParameter("equName");
		String sDate = request.getParameter("sDate");
		String eDate =  request.getParameter("eDate");
		
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) 
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findRelayProtectionNoticeList(employee.getEnterpriseCode(), sDate, eDate, equName, start,limit);
		}
		else {
			obj = remote.findRelayProtectionNoticeList(employee.getEnterpriseCode(), sDate, eDate, equName);
		}
		String strOutput = "";
		if (obj == null || obj.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(obj);
		}
		write(strOutput);
	}
	
	/**
	 * 明细列表
	 * @throws JSONException
	 */
	public void findNoticeDetailList() throws JSONException
	{
		String noticeId = request.getParameter("dztzdId");
		
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) 
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findNoticeDetailList(employee.getEnterpriseCode(), noticeId, start,limit);
		}
		else {
			obj = remote.findNoticeDetailList(employee.getEnterpriseCode(), noticeId);
		}
		String strOutput = "";
		strOutput = JSONUtil.serialize(obj);
		write(strOutput);
	}


	/**
	 * 删除一条或多条记录
	 */
	public void deleteNotice()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}"); 
	}
	
	/**
	 * 存档操作
	 * @throws CodeRepeatException
	 */
	public void saveFile() throws CodeRepeatException
	{
		String noticeId = request.getParameter("dztzdId");
		PtJdbhJDztzd model = remote.findById(Long.parseLong(noticeId));
		model.setSaveBy(employee.getWorkerCode());
		model.setSaveMark("Y");
		remote.update(model);
		write("{success:true,msg:'存档成功！'}");
	}
	
	public PtJdbhJDztzd getNotice() {
		return notice;
	}
	public void setNotice(PtJdbhJDztzd notice) {
		this.notice = notice;
	}
	public PtJdbhJDzdjb getNoticeDetail() {
		return noticeDetail;
	}
	public void setNoticeDetail(PtJdbhJDzdjb noticeDetail) {
		this.noticeDetail = noticeDetail;
	}
}

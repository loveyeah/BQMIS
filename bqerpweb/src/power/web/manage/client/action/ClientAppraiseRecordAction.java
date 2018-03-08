package power.web.manage.client.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.client.ConJAppraise;
import power.ejb.manage.client.ConJAppraiseFacadeRemote;
import power.ejb.manage.client.ConJAppraiseRecord;
import power.ejb.manage.client.ConJAppraiseRecordFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ClientAppraiseRecordAction extends AbstractAction{

	
	private ConJAppraiseRecordFacadeRemote appraiseRecordRemote;
	public ClientAppraiseRecordAction()
	{
		appraiseRecordRemote=(ConJAppraiseRecordFacadeRemote)factory.getFacadeRemote("ConJAppraiseRecordFacade");
	}
	
	/**
	 * 查找合作伙伴评价记录列表
	 * @throws JSONException
	 */
	public void findAppraiseRecordList() throws JSONException
	{
		String clientId=request.getParameter("clientId");
		String intervalId=request.getParameter("intervalId");
		
		PageObject obj=	appraiseRecordRemote.findAll(clientId, intervalId, employee.getEnterpriseCode());
		
		String strOutput = "";
		if (obj == null || obj.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(obj);
		}
		write(strOutput);
	}
	
	/**
	 * 保存合作伙伴信息
	 * @throws JSONException
	 */
	public void saveAppraiseRecordInfo() throws JSONException
	{
		String addOrUpdateRecords = request.getParameter("addOrUpdateRecords"); 
		if (addOrUpdateRecords != null) {
			List<ConJAppraiseRecord> list=this.parseRecordList(addOrUpdateRecords);
			appraiseRecordRemote.saveRecord(list);
			if(list!=null&&list.size()>0)
			{
				ConJAppraiseFacadeRemote appraiseRemote=(ConJAppraiseFacadeRemote) factory.getFacadeRemote("ConJAppraiseFacade");
				ConJAppraiseRecord model=list.get(0);
				
				ConJAppraise entity=appraiseRemote.findModelByClientAndInterval(model.getCliendId(), model.getIntervalId(), employee.getEnterpriseCode());
				Double appraisePoint=appraiseRecordRemote.getTotalRecordScore(model.getCliendId(), model.getIntervalId(), employee.getEnterpriseCode());
				if(entity!=null)
				{
					entity.setTotalScore(appraisePoint);
					entity.setAppraiseBy(employee.getWorkerCode());
					entity.setAppraiseDate(new Date());
					appraiseRemote.update(entity);
				}
				else
				{
					entity=new ConJAppraise();
					entity.setCliendId(model.getCliendId());
					entity.setEnterpriseCode(employee.getEnterpriseCode());
					entity.setGatherFlag("N");
					entity.setIntervalId(model.getIntervalId());
					entity.setTotalScore(appraisePoint);
					entity.setAppraiseBy(employee.getWorkerCode());
					entity.setAppraiseDate(new Date());
					appraiseRemote.save(entity);
				}
			}
			write("{success:true,msg:'保存成功！'}");
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<ConJAppraiseRecord>  parseRecordList(String records) throws JSONException
	{
		List<ConJAppraiseRecord> result=new ArrayList<ConJAppraiseRecord>();
		Object object = JSONUtil.deserialize(records);
		List list=(List)object;
		int intLen = list.size();
		for(int i=0;i<intLen;i++){
			Map map=(Map)list.get(i);
			ConJAppraiseRecord model=this.parseRecordModel(map);
			result.add(model);
		}
		return result;
	}
	@SuppressWarnings("unchecked")
	private ConJAppraiseRecord parseRecordModel(Map map)
	{
		ConJAppraiseRecord model=new ConJAppraiseRecord();
		model.setEnterpriseCode(employee.getEnterpriseCode());
		Object recordId= map.get("recordId");
		Object eventId=map.get("eventId");
		Object intervalId=map.get("intervalId");
		Object cliendId=map.get("cliendId");
		Object appraisePoint=map.get("appraisePoint");
		Object memo=map.get("memo");
		if(recordId!=null&&!recordId.equals(""))
		{
			model.setRecordId(Long.parseLong(recordId.toString()));
		}
		if(eventId!=null&&!eventId.equals(""))
		{
			model.setEventId(Long.parseLong(eventId.toString()));
		}
		if(intervalId!=null&&!intervalId.equals(""))
		{
			model.setIntervalId(Long.parseLong(intervalId.toString()));
		}
		if(cliendId!=null&&!cliendId.equals(""))
		{
			model.setCliendId(Long.parseLong(cliendId.toString()));
		}
		if(appraisePoint!=null&&!appraisePoint.equals(""))
		{
			model.setAppraisePoint(Double.parseDouble(appraisePoint.toString()));
		}
		if(memo!=null&&!memo.equals(""))
		{
			model.setMemo(memo.toString());
		}
		return model;
		
		
	}
}

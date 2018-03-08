package power.ejb.workticket.test;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.business.RunJWorktickethis;
import power.ejb.workticket.business.RunJWorktickethisFacadeRemote;
import power.ejb.workticket.business.RunJWorktickets;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.ejb.workticket.form.WorkticketInfo;

public class RunJWorkticketsFacadeQueryTest {

	protected static RunJWorkticketsFacadeRemote remote;
	protected static RunJWorktickethisFacadeRemote hisRemote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (RunJWorkticketsFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("RunJWorkticketsFacade");
		hisRemote = (RunJWorktickethisFacadeRemote) Ejb3Factory.getInstance()
		.getFacadeRemote("RunJWorktickethisFacade");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testQueryWorkticket() throws ParseException {
		WorkticketInfo model= remote.queryWorkticket("hfdc", "S1200811X0001");
		System.out.println("abc");
	}

	
	
	@Test
	public void testGetWorkticketApproveList()
	{
		PageObject result = remote.getWorkticketApproveList("hfdc", null, null, null, null, null, null,null,null,null,null);
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}
	
	//=========================drdu    Start======================================
	
	@Test
	public void testWorkticketChargeBy()
	{
		RunJWorktickethis model = new RunJWorktickethis();

		model.setWorkticketNo("S1200811D0002");
		model.setOldChargeBy("999999");
		model.setNewChargeBy("999999");
		model.setOldApprovedFinishDate(new Date());
		model.setNewApprovedFinishDate(new Date());
		model.setReason("test");
		model.setApproveBy("888888");
		model.setApproveDate(new Date());
		model.setChangeStatus("2");		
		remote.changeWorkticketChargeBy(model,"0101");
		
		RunJWorktickets wmodel = new RunJWorktickets();
		wmodel.setWorkticketNo(model.getWorkticketNo());
		wmodel = remote.findById(wmodel.getWorkticketNo());
		wmodel.setChargeBy(model.getNewChargeBy());
		
		remote.update(wmodel);
	}
	
	@Test
	public void teschangeWorkticket()
	{
		RunJWorktickethis model = new RunJWorktickethis();

		model.setWorkticketNo("S1200811D0002");
		model.setOldChargeBy("999999");
		model.setNewChargeBy("999999");
		model.setOldApprovedFinishDate(new Date());
		model.setNewApprovedFinishDate(new Date());
		model.setReason("test");
		model.setApproveBy("888888");
		model.setApproveDate(new Date());
		model.setChangeStatus("1");		
		
		remote.changeWorktickettime(model);
		
		
		RunJWorktickets wmodel = new RunJWorktickets();
		wmodel.setWorkticketNo(model.getWorkticketNo());
		wmodel = remote.findById(wmodel.getWorkticketNo());
		wmodel.setApprovedFinishDate(model.getNewApprovedFinishDate());
		
		remote.update(wmodel);
	}
	
	
	@Test
	public void testqueryWorkticketetHis()
	{
		PageObject result = remote.queryWorkticketetHis("hfdc", null, null, null, null, null, null);
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}
	
	@Test
	public void testqueryWorkticketForDelayList()
	{
		PageObject result = remote.queryWorkticketForDelayList("hfdc", null, null, null, null, null);
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}
	
	@Test
	public void testfindSecurityMeasureForBreakOutList()
	{
		PageObject result = remote.findSecurityMeasureForBreakOutList("hfdc", null, null, null, null, null, null, null, null,"");
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}
	
	@Test
	public void testfindSecurityMeasureForBreakOutByNo()
	{
		List result = remote.findSecurityMeasureForBreakOutByNo("hfdc", "S1200811Q0001");
		System.out.print(result.size());
	}
	
	@Test
	public void testqueryaddForUrgentTicketList()
	{
		PageObject result = remote.queryaddForUrgentTicketList("hfdc", null, null, null, null, null);
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}
	
	
	@Test
	public void testfindWorkticketHisList()
	{
		List<RunJWorktickethis>list = remote.findWorkticketHisList("S1200811D0002");
		System.out.print(list.size());
		
	}
	
	

	
	@Test
	public void testBreakOutSecurityMeasure()
	{
		HashMap map = new HashMap();
		map.put("2", "N");
		map.put("8", "N");
		map.put("9", "Y"); 
		map.put("7", "N"); 
		remote.breakOutSecurityMeasure("S1200811D0002",map,"test","999999"); 
	}
	
	@Test
	public void testaddForEmergencyTicket()
	{
		RunJWorktickets worktickets = new RunJWorktickets();
		RunJWorktickethis his = new RunJWorktickethis();
		his.setWorkticketNo("H4200812c0002");
		his.setApproveBy("777777");
		his.setApproveDate(new Date());
		his.setApproveStatus("8");
		worktickets = remote.findById(his.getWorkticketNo());
		worktickets.setFireticketFireman("666666");
		remote.addForEmergencyTicket("H4200812c0002", "777777", new Date(), "8", "测试部","666666");
		
	}
	@Test
	public void testWorkticketEndWarn()
	{
		remote.workticketEndWarn("1", "%", "%","hfdc");
	}
	
}

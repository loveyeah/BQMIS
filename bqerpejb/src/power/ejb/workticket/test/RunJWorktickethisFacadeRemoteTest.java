package power.ejb.workticket.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.runlog.shift.RunJEquRunStatusHis;
import power.ejb.workticket.business.RunJWorktickethis;
import power.ejb.workticket.business.RunJWorktickethisFacadeRemote;
import power.ejb.workticket.business.RunJWorktickets;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;

public class RunJWorktickethisFacadeRemoteTest {
	
	protected static RunJWorktickethisFacadeRemote remote;
	protected static RunJWorkticketsFacadeRemote workticketRemote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (RunJWorktickethisFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunJWorktickethisFacade");
		workticketRemote = (RunJWorkticketsFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunJWorkticketsFacade");
	}
	
	
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
		workticketRemote.changeWorkticketChargeBy(model,"0101");
		
		RunJWorktickets wmodel = new RunJWorktickets();
		wmodel.setWorkticketNo(model.getWorkticketNo());
		wmodel = workticketRemote.findById(wmodel.getWorkticketNo());
		wmodel.setChargeBy(model.getNewChargeBy());
		
		workticketRemote.update(wmodel);
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
		
		workticketRemote.changeWorktickettime(model);
		
		
		RunJWorktickets wmodel = new RunJWorktickets();
		wmodel.setWorkticketNo(model.getWorkticketNo());
		wmodel = workticketRemote.findById(wmodel.getWorkticketNo());
		wmodel.setApprovedFinishDate(model.getNewApprovedFinishDate());
		
		workticketRemote.update(wmodel);
	}
	
	@Test
	public void testlist()
	{
		List<RunJWorktickethis>list = workticketRemote.findWorkticketHisList("S1200811D0001");
		System.out.print(list.size());
		
	}
}

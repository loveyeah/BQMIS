package power.ejb.opticket.test;

import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.opticket.RunJOpticket;
import power.ejb.opticket.RunJOpticketFacadeRemote;

public class RunJOpticketFacadeRemoteTest {
	protected static RunJOpticketFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (RunJOpticketFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunJOpticketFacade");
	}

	@Test
	public void testSave() {
		RunJOpticket m = new RunJOpticket();
		m.setCreateBy("999999");
		m.setOperateTaskId(3L);
		m.setAppendixAddr("http://sfsf");
		m.setPlanStartTime(new java.util.Date());
		remote.save("S", m);
	}

	@Test
	public void testDelete() {
		RunJOpticket m = new RunJOpticket();
		m.setCreateBy("999999");
		m.setOperateTaskId(3L);
		m.setAppendixAddr("http://sfsf");
		m.setPlanStartTime(new java.util.Date());
		remote.save("S", m);
	}
	@Test
	public void testFindByWorktickectNo() {
		String str="灞桥未分热控Ⅰ0903001";
		String returnstr=remote.findByWorktickectNo(str);
		System.out.print(returnstr);
	}
	@Test
	public void testUpdate() {
		 //
	}

	@Test
	public void testFindById() {
	//
	}
	@Test
	public void testGetOpticketList(){
//		PageObject list=remote.getOpticketList("hfdc", null, null, null, null, null); 
//		System.out.print(list.getList().size());
	}

}

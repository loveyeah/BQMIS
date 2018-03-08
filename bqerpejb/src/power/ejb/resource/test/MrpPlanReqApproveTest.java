package power.ejb.resource.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.resource.business.MrpPlanReqApprove;

public class MrpPlanReqApproveTest {

	protected static MrpPlanReqApprove remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(MrpPlanReqApprove)Ejb3Factory.getInstance().getFacadeRemote("MrpPlanReqApproveImp");
	}

	@Test
	public void testFindAllPlanBusi() {
		fail("尚未实现"); // TODO
	}

	@Test
	public void testFindPlanBusiForApprove() {
		fail("尚未实现"); // TODO
	}

	@Test
	public void testReportTo() {
		remote.reportTo("JH000009", "hfResourcePlanSC", "999999",
				24l, "test", "", "SB","aaa");
	}

	@Test
	public void testPlanReqSign() {
	//	remote.planReqSign("JH000009", "test", "999999", 46l, null, "", "XC");
	//remote.planReqSign("JH000009", "test", "999999", 67l, null, "", "TY");
	//	remote.planReqSign("JH000009", "test", "999999", 78l, null, "", "TY");
		remote.planReqSign("JH000009", "test", "999999", 83l, null, "", "TY","");
	}

}

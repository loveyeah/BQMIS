package power.ejb.workticket.test;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.RunCWorkticketMarkcard;
import power.ejb.workticket.RunCWorkticketMarkcardFacadeRemote;

public class RunCWorkticketMarkcardFacadeRemoteTest {

	protected static RunCWorkticketMarkcardFacadeRemote remote;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (RunCWorkticketMarkcardFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("RunCWorkticketMarkcardFacade");
	}

	@Test
	public void testSave() {
		RunCWorkticketMarkcard markcard = new RunCWorkticketMarkcard();
		markcard.setEnterpriseCode("hfdc");
		markcard.setMarkcardTypeId(1l);
		markcard.setModifyBy("999999");
		markcard.setMarkcardCode("123");
		try {
			remote.save(markcard);
		} catch (CodeRepeatException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteMulti() {
		remote.deleteMulti("1");
	}

	@Test
	public void testUpdate() {
		RunCWorkticketMarkcard markcard = new RunCWorkticketMarkcard();
		markcard.setMarkcardId(1l);
		markcard.setMarkcardCode("321");
		try {
			remote.update(markcard);
		} catch (CodeRepeatException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFindById() {
		remote.findById(1l);
	}

	@Test
	public void testFindAll() {
		remote.findAll("hfdc", 2l, "");
	}

}

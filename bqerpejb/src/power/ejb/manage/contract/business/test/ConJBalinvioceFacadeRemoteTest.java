package power.ejb.manage.contract.business.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.manage.contract.business.ConJBalinvioceFacadeRemote;

public class ConJBalinvioceFacadeRemoteTest {
	protected static ConJBalinvioceFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(ConJBalinvioceFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("ConJBalinvioceFacade");
	}

	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByBalanceId() {
		List list=remote.findByBalanceId("hfdc", 1l);
		System.out.print(list.size());
	}

}

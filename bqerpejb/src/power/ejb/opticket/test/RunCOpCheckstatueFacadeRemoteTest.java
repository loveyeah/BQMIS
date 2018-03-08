package power.ejb.opticket.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.opticket.RunCOpCheckstatue;
import power.ejb.opticket.RunCOpCheckstatueFacadeRemote;

public class RunCOpCheckstatueFacadeRemoteTest {
	static RunCOpCheckstatueFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(RunCOpCheckstatueFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunCOpCheckstatueFacade");
	}

	@Test
	public void testSave() {
		RunCOpCheckstatue entity=new RunCOpCheckstatue();
		entity.setCheckStatue("abc");
		entity.setEnterpriseCode("hfdc");
		entity.setModifyBy("999999");
		entity.setCheckBefFlag("C");
		remote.save(entity);
		System.out.print(entity.getCheckStatueId());
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
	public void testFindAll() {
		List list=remote.findAll("hfdc");
		System.out.print(list.size());
	}

	@Test
	public void testFindPublic() {
		List list=remote.findPublic("hfdc", "C","N");
		System.out.print(list.size());
	}

}

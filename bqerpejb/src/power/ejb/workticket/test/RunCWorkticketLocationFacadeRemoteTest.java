package power.ejb.workticket.test;

import java.text.ParseException;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.RunCWorkticketLocation;
import power.ejb.workticket.RunCWorkticketLocationFacadeRemote;

public class RunCWorkticketLocationFacadeRemoteTest {
	protected static RunCWorkticketLocationFacadeRemote remote;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (RunCWorkticketLocationFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("RunCWorkticketLocationFacade");
	}

	@Test
	public void testSave() {
		RunCWorkticketLocation markcard = new RunCWorkticketLocation();
		markcard.setEnterpriseCode("hfdc");
		markcard.setModifyBy("999999");
		markcard.setBlockCode("zz");
		markcard.setLocationName("去玩儿去玩儿玩sdssds儿");
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
		RunCWorkticketLocation markcard = new RunCWorkticketLocation();
		markcard.setLocationId(1l);
		markcard.setLocationName("玩儿法师的法师的");
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
	public void testFindAll() throws ParseException {
		remote.findAll("hfdc", "zz", null);
	}

}

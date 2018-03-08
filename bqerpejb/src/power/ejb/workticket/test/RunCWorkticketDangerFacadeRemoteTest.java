package power.ejb.workticket.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketDanger;
import power.ejb.workticket.RunCWorkticketDangerFacadeRemote;
import power.ejb.workticket.RunCWorkticketDangerType;

import com.sun.org.apache.regexp.internal.recompile;

public class RunCWorkticketDangerFacadeRemoteTest {

	private static RunCWorkticketDangerFacadeRemote remote;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (RunCWorkticketDangerFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("RunCWorkticketDangerFacade");
	}

	@Test
	public void testSave() throws CodeRepeatException {
		RunCWorkticketDanger danger = new RunCWorkticketDanger();
		danger.setDangerName("123123");
		danger.setDangerTypeId(1l);
		danger.setEnterpriseCode("hfdc");
		danger.setModifyBy("刘应文");
		danger.setOrderBy(1L);
		danger.setPDangerId(1L);
		remote.save(danger);
	}

	@Test
	public void testDelete() throws CodeRepeatException {
		Long dangerId = 7l;
		remote.delete(dangerId);
	}

	@Test
	public void testDeleteMulti() {
		String ids = "1,2";
		remote.deleteMulti(ids);
	}

	@Test
	public void testUpdate() throws CodeRepeatException {
		RunCWorkticketDanger danger = remote.findById(7l);
		danger.setDangerName("5671");
		danger.setModifyBy("liuyingwen");
		danger.setDangerTypeId(2l);
		danger.setPDangerId(2l);
		danger.setIsUse("Y");
		remote.update(danger);
	}

	@Test
	public void testFindAll() {
		PageObject result=remote.findAll("hfdc", 1l, 1l,"", null);
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}

}

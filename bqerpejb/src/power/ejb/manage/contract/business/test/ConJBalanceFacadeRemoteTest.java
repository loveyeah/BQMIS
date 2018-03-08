package power.ejb.manage.contract.business.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.manage.contract.business.ConJBalance;
import power.ejb.manage.contract.business.ConJBalanceFacadeRemote;
import power.ejb.manage.contract.business.ConJContractInfo;
import power.ejb.manage.contract.business.ConJContractInfoFacadeRemote;
import power.ejb.manage.contract.form.ConBalanceFullForm;
import power.ejb.manage.contract.form.ContractForm;

public class ConJBalanceFacadeRemoteTest {
	protected static ConJBalanceFacadeRemote remote;
	protected static ConJContractInfoFacadeRemote cremote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(ConJBalanceFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("ConJBalanceFacade");
		cremote=(ConJContractInfoFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("ConJContractInfoFacade");
	}

	@Test
	public void testSave() throws CodeRepeatException {
		ConJContractInfo con=cremote.findById(1l);
		ConJBalance model=new ConJBalance();
		model.setCliendId(con.getCliendId());
		model.setConId(con.getConId());
		model.setPaymentId(1L);
		model.setApplicatPrice(100d);
		model.setBalaMethod("xianj");
		model.setItemId(con.getItemId());
		model.setEntryBy(con.getEntryBy());
		model.setEntryDate(con.getEntryDate());
		model.setEnterpriseCode("hfdc");
		remote.save(model);
	}

	@Test
	public void testDelete() throws CodeRepeatException {
		ConJBalance model=remote.findById(1l);
		remote.delete(model);
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
	public void testFindBalanceListByConId() {
		List list=remote.findBalanceListByConId(1l, "hfdc");
		System.out.print(list.size());
	}

	@Test
	public void testFindContractByConId() {
		ContractForm model=remote.findContractByConId(1l);
		System.out.print(model.getAppliedAccount());
	}

	@Test
	public void testFindBalanceByBalanceId() {
		ConBalanceFullForm model=remote.findBalanceByBalanceId(2l);
		System.out.print(model.getApplicatDate());
	}

}

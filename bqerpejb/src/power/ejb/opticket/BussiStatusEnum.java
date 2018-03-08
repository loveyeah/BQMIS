package power.ejb.opticket;

public enum BussiStatusEnum {
	REGIST_STATUS("0"), REPORT_STATUS("1"), WARCH_APPROVE_STATUS("2"), CHARG_APPROVE_STATUS(
			"3"), CLASS_LEAD_APPROVE_STATUS("4"), END_APPROVE_STATUS("5"), BACK_APPROVE_STATUS(
			"T"), INVALID_APPROVE_STATUS("Z"), UNDIF_STATUS("U"), SAFE_APPROVE_STATUS(
			"6"), ENGINEER_APPROVE_STATUS("7"),HEAD_APPROVE_STATUS("H");
	private String value;

	private BussiStatusEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}

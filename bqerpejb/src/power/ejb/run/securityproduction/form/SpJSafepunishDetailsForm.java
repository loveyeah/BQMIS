package power.ejb.run.securityproduction.form;

import power.ejb.run.securityproduction.SpJSafepunishDetails;

public class SpJSafepunishDetailsForm {
	private SpJSafepunishDetails punishDetailsInfo;
	private String punishManName;
	private String punishManDep;

	public SpJSafepunishDetails getPunishDetailsInfo() {
		return punishDetailsInfo;
	}

	public void setPunishDetailsInfo(SpJSafepunishDetails punishDetailsInfo) {
		this.punishDetailsInfo = punishDetailsInfo;
	}

	public String getPunishManName() {
		return punishManName;
	}

	public void setPunishManName(String punishManName) {
		this.punishManName = punishManName;
	}

	public String getPunishManDep() {
		return punishManDep;
	}

	public void setPunishManDep(String punishManDep) {
		this.punishManDep = punishManDep;
	}
}

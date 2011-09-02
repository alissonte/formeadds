package br.com.meadds.session;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityHome;

import br.ufc.meadds.entity.Device;
import br.ufc.meadds.entity.Sample;
import br.ufc.meadds.entity.SampleConsumption;
import br.ufc.meadds.entity.User;

@Name("deviceHome")
public class DeviceHome extends EntityHome<Device> {

	/**
     * 
     */
	private static final long serialVersionUID = 7477119523875525079L;
	@In(create = true)
	UserHome userHome;

	private final User user = (User) Contexts.getSessionContext().get("user");

	public void setDeviceId(Long id) {
		setId(id);
	}

	public Long getDeviceId() {
		return (Long) getId();
	}

	@Override
	protected Device createInstance() {
		Device device = new Device();
		device.setUser(user);
		return device;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		User user = userHome.getDefinedInstance();
		if (user != null) {
			getInstance().setUser(user);
		}
	}

	public boolean isWired() {
		if (getInstance().getUser() == null)
			return false;
		return true;
	}

	public Device getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<SampleConsumption> getSampleconsumptions() {
		return getInstance() == null ? null : new ArrayList<SampleConsumption>(
				getInstance().getSampleconsumptions());
	}

	public List<Sample> getSamples() {
		return getInstance() == null ? null : new ArrayList<Sample>(
				getInstance().getSamples());
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

}

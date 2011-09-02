package br.com.meadds.session;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.framework.EntityHome;

import br.ufc.meadds.entity.AlgorithmConfidentialityDegree;
import br.ufc.meadds.entity.Application;
import br.ufc.meadds.entity.Device;
import br.ufc.meadds.entity.Sample;
import br.ufc.meadds.entity.SampleConsumption;
import br.ufc.meadds.entity.SampleProfile;
import br.ufc.meadds.entity.User;

@Name("userHome")
public class UserHome extends EntityHome<User> {

	/**
     * 
     */
	private static final long serialVersionUID = -919236916805148565L;

	@In(create = true)
	private Renderer renderer;

	@In
	FacesMessages facesMessages;

	public void setUserId(Long id) {
		setId(id);
	}

	public Long getUserId() {
		return (Long) getId();
	}

	@Override
	protected User createInstance() {
		User user = new User();
		return user;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	@Override
	public String persist() {
		String result = null;
		try {
			result = super.persist();
			setInstance(new User());
			renderer.render("/mail.xhtml");
			facesMessages.add("Usuário cadastrado com sucesso!");
		} catch (PersistenceException e) {
			FacesContext.getCurrentInstance().addMessage(
					"Error",
					FacesMessages.createFacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Nome de usuário ou login já existem."));
		}
		return result;
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public User getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<AlgorithmConfidentialityDegree> getAlgorithmconfidentialitydegrees() {
		return getInstance() == null ? null
				: new ArrayList<AlgorithmConfidentialityDegree>(getInstance()
						.getAlgorithmconfidentialitydegrees());
	}

	public List<Application> getApplications() {
		return getInstance() == null ? null : new ArrayList<Application>(
				getInstance().getApplications());
	}

	public List<Device> getDevices() {
		return getInstance() == null ? null : new ArrayList<Device>(
				getInstance().getDevices());
	}

	public List<SampleConsumption> getSampleconsumptions() {
		return getInstance() == null ? null : new ArrayList<SampleConsumption>(
				getInstance().getSampleconsumptions());
	}

	public List<SampleProfile> getSampleprofiles() {
		return getInstance() == null ? null : new ArrayList<SampleProfile>(
				getInstance().getSampleprofiles());
	}

	public List<Sample> getSamples() {
		return getInstance() == null ? null : new ArrayList<Sample>(
				getInstance().getSamples());
	}

}

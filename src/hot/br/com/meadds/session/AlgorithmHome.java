package br.com.meadds.session;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityHome;

import br.ufc.meadds.entity.Algorithm;
import br.ufc.meadds.entity.AlgorithmSampleProfile;
import br.ufc.meadds.entity.AlgorithmType;
import br.ufc.meadds.entity.Library;
import br.ufc.meadds.entity.OperationMode;
import br.ufc.meadds.entity.Sample;
import br.ufc.meadds.entity.SampleConsumption;
import br.ufc.meadds.entity.User;

@Name("algorithmHome")
public class AlgorithmHome extends EntityHome<Algorithm> {

	/**
     * 
     */
	private static final long serialVersionUID = -6165560226420752848L;
	@In(create = true)
	LibraryHome libraryHome;
	@In(create = true)
	OperationModeHome operationModeHome;
	@In(create = true)
	AlgorithmTypeHome algorithmTypeHome;
	@In(create = true)
	private AlgorithmConfidentialityDegreeHome algorithmConfidentialityDegreeHome;

	private final User user = (User) Contexts.getSessionContext().get("user");

	public void setAlgorithmId(Long id) {
		setId(id);
	}

	public Long getAlgorithmId() {
		return (Long) getId();
	}

	@Override
	protected Algorithm createInstance() {
		Algorithm algorithm = new Algorithm();
		algorithmConfidentialityDegreeHome.getInstance().setUser(user);
		algorithmConfidentialityDegreeHome.getInstance()
				.setAlgorithm(algorithm);
		algorithm.getAlgorithmConfidentialityDegrees().add(
				algorithmConfidentialityDegreeHome.getInstance());
		return algorithm;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Library library = libraryHome.getDefinedInstance();
		if (library != null) {
			getInstance().setLibrary(library);
		}
		OperationMode operationMode = operationModeHome.getDefinedInstance();
		if (operationMode != null) {
			getInstance().setOperationMode(operationMode);
		}
		AlgorithmType type = algorithmTypeHome.getDefinedInstance();
		if (type != null) {
			getInstance().setType(type);
		}
		algorithmConfidentialityDegreeHome.setInstance(getInstance()
				.getAlgorithmConfidentialityDegree(user));
	}

	public boolean isWired() {
		if (getInstance().getLibrary() == null)
			return false;
		if (getInstance().getOperationMode() == null)
			return false;
		if (getInstance().getType() == null)
			return false;
		return true;
	}

	public Algorithm getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<AlgorithmSampleProfile> getAlgsampleprofiles() {
		return getInstance() == null ? null
				: new ArrayList<AlgorithmSampleProfile>(getInstance()
						.getSampleProfiles());
	}

	public List<SampleConsumption> getSampleconsumptions() {
		return getInstance() == null ? null : new ArrayList<SampleConsumption>(
				getInstance().getSamplesConsumption());
	}

	public List<Sample> getSamples() {
		return getInstance() == null ? null : new ArrayList<Sample>(
				getInstance().getSamples());
	}

	/**
	 * @return the algorithmConfidentialityDegreeHome
	 */
	public AlgorithmConfidentialityDegreeHome getAlgorithmConfidentialityDegreeHome() {
		return algorithmConfidentialityDegreeHome;
	}

	/**
	 * @param algorithmConfidentialityDegreeHome
	 *            the algorithmConfidentialityDegreeHome to set
	 */
	public void setAlgorithmConfidentialityDegreeHome(
			AlgorithmConfidentialityDegreeHome algorithmConfidentialityDegreeHome) {
		this.algorithmConfidentialityDegreeHome = algorithmConfidentialityDegreeHome;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
}

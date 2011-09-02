package br.com.meadds.session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityHome;

import br.ufc.meadds.entity.Algorithm;
import br.ufc.meadds.entity.AlgorithmSampleProfile;
import br.ufc.meadds.entity.Device;
import br.ufc.meadds.entity.Library;
import br.ufc.meadds.entity.SampleProfile;
import br.ufc.meadds.entity.User;

@Name("sampleProfileHome")
public class SampleProfileHome extends EntityHome<SampleProfile> {

	/**
     * 
     */
	private static final long serialVersionUID = 8273627640896766176L;
	@In(create = true)
	LibraryHome libraryHome;
	@In(create = true)
	UserHome userHome;

	private List<Library> libraries = new LinkedList<Library>();

	private Integer textSize;

	private Set<Integer> textSizes = new HashSet<Integer>();

	private List<Device> devices = new LinkedList<Device>();

	private Set<Device> selectedDevices = new HashSet<Device>();

	private final User user = (User) Contexts.getSessionContext().get("user");

	public void setSampleProfileId(Long id) {
		setId(id);
	}

	public Long getSampleProfileId() {
		return (Long) getId();
	}

	@Override
	protected SampleProfile createInstance() {
		SampleProfile sampleProfile = new SampleProfile();
		return sampleProfile;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	@Override
	@Transactional
	public String persist() {
		getInstance().setUser(user);

		for (Algorithm algorithm : getInstance().getLibrary().getAlgorithms()) {
			AlgorithmSampleProfile alg = new AlgorithmSampleProfile();
			alg.setAlgorithm(algorithm);
			alg.setSamplesTextSize(textSize);
			Query q = getEntityManager()
					.createQuery(
							"select avg(o.encryptTime), avg(o.decryptTime), "
									+ "avg(o.decryptionMemory), avg(o.encryptionMemory) from Sample o "
									+ "where o.user.id = :user "
									+ "and o.algorithm.id = :algorithm "
									+ "and o.textSize = :text "
									+ "and o.device in (:devices)");
			q.setParameter("user", user.getId());
			q.setParameter("algorithm", algorithm.getId());
			q.setParameter("devices", selectedDevices);
			q.setParameter("text", textSize);
			
			Object[] o = (Object[]) q.getSingleResult();
			alg.setAverageEncryptTime(((Double) o[0]).longValue());
			alg.setAverageDecryptTime(((Double) o[1]).longValue());
			alg.setAverageRuntime(((Double) o[0]).longValue()
					+ ((Double) o[1]).longValue());
			alg.setAverageMemoryRequired(Math.max(((Double) o[2]).intValue(),
					((Double) o[3]).intValue()));

			q = getEntityManager().createQuery(
					"select avg(o.averageConsumptionEncrypt), avg(o.averageConsumptionDecrypt) from SampleConsumption o "
							+ "where o.user.id = :user "
							+ "and o.algorithm.id = :algorithm "
							+ "and o.device in (:devices)");
			q.setParameter("user", user.getId());
			q.setParameter("algorithm", algorithm.getId());
			q.setParameter("devices", selectedDevices);
			
			o = (Object[]) q.getSingleResult();
			alg.setAverageConsumption(((Double) o[0]).floatValue() + ((Double) o[1]).floatValue());
			alg.setSampleProfile(instance);
			getInstance().addAlgorithmSampleProfile(alg);
		}

		return super.persist();
	}

	public void wire() {
		getInstance();
		Library library = libraryHome.getDefinedInstance();
		if (library != null) {
			getInstance().setLibrary(library);
		}
		User user = userHome.getDefinedInstance();
		if (user != null) {
			getInstance().setUser(user);
		}
	}

	public boolean isWired() {
		if (getInstance().getLibrary() == null)
			return false;
		if (getInstance().getUser() == null)
			return false;
		return true;
	}

	public SampleProfile getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<AlgorithmSampleProfile> getSampleProfiles() {
		return getInstance() == null ? null
				: new ArrayList<AlgorithmSampleProfile>(getInstance()
						.getSampleProfiles());
	}

	/**
	 * @return the libraries
	 */
	@SuppressWarnings("unchecked")
	public List<Library> getLibraries() {
		if (libraries.isEmpty()) {
			libraries = getEntityManager().createQuery(
					"select o from Library o").getResultList();
		}
		return libraries;
	}

	/**
	 * @param libraries
	 *            the libraries to set
	 */
	public void setLibraries(List<Library> libraries) {
		this.libraries = libraries;
	}

	/**
	 * @return the textSizes
	 */
	@SuppressWarnings("unchecked")
	public Set<Integer> getTextSizes() {
		if (instance.getLibrary() != null
				&& instance.getLibrary().getId() != null) {
			Query query = getEntityManager().createQuery(
					"select distinct o.textSize from Sample o "
							+ "where o.user.id = :user and "
							+ "o.algorithm.library.id = :library");
			query.setParameter("user", user.getId());
			query.setParameter("library", instance.getLibrary().getId());
			textSizes = new HashSet<Integer>(query.getResultList());
		}

		return textSizes;
	}

	/**
	 * @param textSizes
	 *            the textSizes to set
	 */
	public void setTextSizes(Set<Integer> textSizes) {
		this.textSizes = textSizes;
	}

	/**
	 * @return the textSize
	 */
	public Integer getTextSize() {
		return textSize;
	}

	/**
	 * @param textSize
	 *            the textSize to set
	 */
	public void setTextSize(Integer textSize) {
		this.textSize = textSize;
	}

	/**
	 * @return the devices
	 */
	@SuppressWarnings("unchecked")
	public List<Device> getDevices() {
		if (textSize != null
				&& (instance.getLibrary() != null && instance.getLibrary()
						.getId() != null)) {
			Query query = getEntityManager().createQuery(
					"select distinct o.device from Sample o where o.textSize = :text "
							+ "and o.algorithm.library.id = :library "
							+ "and o.user.id = :user");
			query.setParameter("text", textSize);
			query.setParameter("user", user.getId());
			query.setParameter("library", instance.getLibrary().getId());
			devices = query.getResultList();
		}

		return devices;
	}

	/**
	 * @param devices
	 *            the devices to set
	 */
	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	/**
	 * @return the selectedDevices
	 */
	public List<Device> getSelectedDevices() {
		return new LinkedList<Device>(selectedDevices);
	}

	/**
	 * @param selectedDevices
	 *            the selectedDevices to set
	 */
	public void setSelectedDevices(List<Device> selectedDevices) {
		this.selectedDevices = new HashSet<Device>(selectedDevices);
	}

	/*
	 * Map<Algorithm, List<Long>> encriptTime = new HashMap<Algorithm,
	 * List<Long>>(); Map<Algorithm, List<Long>> decryptTime = new
	 * HashMap<Algorithm, List<Long>>(); Map<Algorithm, List<Long>> runtime =
	 * new HashMap<Algorithm, List<Long>>(); Map<Algorithm, List<Double>>
	 * consumption = new HashMap<Algorithm, List<Double>>(); Map<Algorithm,
	 * List<Long>> memory = new HashMap<Algorithm, List<Long>>(); Set<Algorithm>
	 * algorithms = new HashSet<Algorithm>();
	 * 
	 * Algorithm a = null; for (Device dev : selectedDevices) { for (Sample s :
	 * dev.getSamples()) { if (s.getTextSize().equals(textSize)) { a =
	 * s.getAlgorithm(); putValue(encriptTime, a, s.getEncryptTime());
	 * putValue(decryptTime, a, s.getDecryptTime()); putValue(runtime, a,
	 * s.getDecryptTime() + s.getEncryptTime()); putValue( memory, a,
	 * Long.valueOf(s.getDecryptionMemory() + s.getEncryptionMemory()));
	 * algorithms.add(a); } } for (SampleConsumption s :
	 * dev.getSampleconsumptions()) { if (consumption.get(s.getAlgorithm()) ==
	 * null) { consumption.put(s.getAlgorithm(), new LinkedList<Double>()); }
	 * consumption.get(s.getAlgorithm()).add( s.getAverageConsumptionDecrypt() +
	 * s.getAverageConsumptionEncrypt()); algorithms.add(s.getAlgorithm()); } }
	 * 
	 * for (Algorithm o : algorithms) { AlgorithmSampleProfile asp = new
	 * AlgorithmSampleProfile(); long sum = 0; for (Long l : encriptTime.get(o))
	 * { sum += l; } asp.setAverageEncryptTime(sum / encriptTime.get(o).size());
	 * sum = 0; for (Long l : decryptTime.get(o)) { sum += l; }
	 * asp.setAverageDecryptTime(sum / decryptTime.get(o).size()); sum = 0; for
	 * (Long l : runtime.get(o)) { sum += l; } asp.setAverageRuntime(sum /
	 * runtime.get(o).size()); double dd = 0; for (Double l :
	 * consumption.get(o)) { dd += l; }
	 * asp.setAverageConsumption(Double.valueOf( dd /
	 * consumption.get(o).size()).floatValue()); sum = 0; for (Long l :
	 * encriptTime.get(o)) { sum += l; }
	 * asp.setAverageMemoryRequired(Long.valueOf( sum /
	 * encriptTime.get(o).size()).intValue());
	 * asp.setSampleProfile(getInstance());
	 * getInstance().getSampleProfiles().add(asp); } return super.persist();
	 */
}

package br.ufc.meadds.entity;

// Generated 01/06/2011 09:36:02 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

// TODO: Auto-generated Javadoc
/**
 * Algorithm generated by hbm2java.
 */
@Entity
@Table(name = "algorithm", uniqueConstraints = @UniqueConstraint(columnNames = {
		"name", "library_id", "type_id", "operation_mode_id" }))
public class Algorithm implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2888415018784637494L;

	/** The id. */
	private Long id;

	/** The library. */
	private Library library;

	/** The type. */
	private AlgorithmType type;

	/** The operation mode. */
	private OperationMode operationMode;

	/** The name. */
	private String name;

	/** The key lenght default. */
	private Integer keyLenghtDefault;

	/** The sampleconsumptions. */
	private Set<SampleConsumption> samplesConsumption = new HashSet<SampleConsumption>(
			0);

	/** The algorithmconfidentialitydegrees. */
	private Set<AlgorithmConfidentialityDegree> algorithmConfidentialityDegrees = new HashSet<AlgorithmConfidentialityDegree>(
			0);

	/** The algsampleprofiles. */
	private Set<AlgorithmSampleProfile> sampleProfiles = new HashSet<AlgorithmSampleProfile>(
			0);

	/** The samples. */
	private Set<Sample> samples = new HashSet<Sample>(0);

	/**
	 * Instantiates a new algorithm.
	 */
	public Algorithm() {
	}

	/**
	 * Instantiates a new algorithm.
	 * 
	 * @param library
	 *            the library
	 * @param algorithmtype
	 *            the algorithmtype
	 * @param operationmode
	 *            the operationmode
	 * @param name
	 *            the name
	 * @param confidentialityDegree
	 *            the confidentiality degree
	 * @param keyLenghtDefault
	 *            the key lenght default
	 */
	public Algorithm(Library library, AlgorithmType algorithmtype,
			OperationMode operationmode, String name, Integer keyLenghtDefault) {
		this.library = library;
		this.type = algorithmtype;
		this.operationMode = operationmode;
		this.name = name;
		this.keyLenghtDefault = keyLenghtDefault;
	}

	/**
	 * Instantiates a new algorithm.
	 * 
	 * @param library
	 *            the library
	 * @param algorithmtype
	 *            the algorithmtype
	 * @param operationmode
	 *            the operationmode
	 * @param name
	 *            the name
	 * @param confidentialityDegree
	 *            the confidentiality degree
	 * @param keyLenghtDefault
	 *            the key lenght default
	 * @param sampleconsumptions
	 *            the sampleconsumptions
	 * @param algorithmconfidentialitydegrees
	 *            the algorithmconfidentialitydegrees
	 * @param algsampleprofiles
	 *            the algsampleprofiles
	 * @param samples
	 *            the samples
	 */
	public Algorithm(
			Library library,
			AlgorithmType algorithmtype,
			OperationMode operationmode,
			String name,
			Integer keyLenghtDefault,
			Set<SampleConsumption> sampleconsumptions,
			Set<AlgorithmConfidentialityDegree> algorithmconfidentialitydegrees,
			Set<AlgorithmSampleProfile> algsampleprofiles, Set<Sample> samples) {
		this.library = library;
		this.type = algorithmtype;
		this.operationMode = operationmode;
		this.name = name;
		this.keyLenghtDefault = keyLenghtDefault;
		this.samplesConsumption = sampleconsumptions;
		this.algorithmConfidentialityDegrees = algorithmconfidentialitydegrees;
		this.sampleProfiles = algsampleprofiles;
		this.samples = samples;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the library.
	 * 
	 * @return the library
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "library_id", nullable = false)
	@NotNull
	public Library getLibrary() {
		return this.library;
	}

	/**
	 * Sets the library.
	 * 
	 * @param library
	 *            the new library
	 */
	public void setLibrary(Library library) {
		this.library = library;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id", nullable = false)
	@NotNull
	public AlgorithmType getType() {
		return this.type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param algorithmtype
	 *            the new type
	 */
	public void setType(AlgorithmType algorithmtype) {
		this.type = algorithmtype;
	}

	/**
	 * Gets the operation mode.
	 * 
	 * @return the operation mode
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operation_mode_id", nullable = false)
	@NotNull
	public OperationMode getOperationMode() {
		return this.operationMode;
	}

	/**
	 * Sets the operation mode.
	 * 
	 * @param operationmode
	 *            the new operation mode
	 */
	public void setOperationMode(OperationMode operationmode) {
		this.operationMode = operationmode;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	@Column(name = "name", unique = true, nullable = false, length = 45)
	@NotNull
	@Length(max = 45)
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the key lenght default.
	 * 
	 * @return the key lenght default
	 */
	@Column(name = "keyLenghtDefault", nullable = false)
	public Integer getKeyLenghtDefault() {
		return this.keyLenghtDefault;
	}

	/**
	 * Sets the key lenght default.
	 * 
	 * @param keyLenghtDefault
	 *            the new key lenght default
	 */
	public void setKeyLenghtDefault(Integer keyLenghtDefault) {
		this.keyLenghtDefault = keyLenghtDefault;
	}

	/**
	 * Gets the sampleconsumptions.
	 * 
	 * @return the sampleconsumptions
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "algorithm")
	public Set<SampleConsumption> getSamplesConsumption() {
		return this.samplesConsumption;
	}

	/**
	 * Sets the sampleconsumptions.
	 * 
	 * @param sampleconsumptions
	 *            the new sampleconsumptions
	 */
	public void setSamplesConsumption(Set<SampleConsumption> sampleconsumptions) {
		this.samplesConsumption = sampleconsumptions;
	}

	/**
	 * Gets the algorithmconfidentialitydegrees.
	 * 
	 * @return the algorithmconfidentialitydegrees
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "algorithm")
	@NotNull
	public Set<AlgorithmConfidentialityDegree> getAlgorithmConfidentialityDegrees() {
		return this.algorithmConfidentialityDegrees;
	}

	@Transient
	public AlgorithmConfidentialityDegree getAlgorithmConfidentialityDegree(
			User user) {
		AlgorithmConfidentialityDegree object = null;
		for (AlgorithmConfidentialityDegree o : algorithmConfidentialityDegrees) {
			if (o.getUser().getLogin().equals(user.getLogin())
					&& o.getUser().getCpf().equals(user.getCpf())) {
				object = o;
				break;
			}
		}
		return object;
	}

	/**
	 * Sets the algorithmconfidentialitydegrees.
	 * 
	 * @param algorithmconfidentialitydegrees
	 *            the new algorithmconfidentialitydegrees
	 */
	public void setAlgorithmConfidentialityDegrees(
			Set<AlgorithmConfidentialityDegree> algorithmconfidentialitydegrees) {
		this.algorithmConfidentialityDegrees = algorithmconfidentialitydegrees;
	}

	/**
	 * Gets the algsampleprofiles.
	 * 
	 * @return the algsampleprofiles
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "algorithm")
	public Set<AlgorithmSampleProfile> getSampleProfiles() {
		return this.sampleProfiles;
	}

	/**
	 * Sets the algsampleprofiles.
	 * 
	 * @param algsampleprofiles
	 *            the new algsampleprofiles
	 */
	public void setSampleProfiles(Set<AlgorithmSampleProfile> algsampleprofiles) {
		this.sampleProfiles = algsampleprofiles;
	}

	/**
	 * Gets the samples.
	 * 
	 * @return the samples
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "algorithm")
	public Set<Sample> getSamples() {
		return this.samples;
	}

	/**
	 * Sets the samples.
	 * 
	 * @param samples
	 *            the new samples
	 */
	public void setSamples(Set<Sample> samples) {
		this.samples = samples;
	}

	/**
     * 
     */
	@Transient
	public Double getConfidentialityDegree(User user) {
		Double confDegree = null;
		AlgorithmConfidentialityDegree degree = this
				.getAlgorithmConfidentialityDegree(user);
		if (degree != null && degree.getKmin() > 0) {
			confDegree = ((Math.pow(2, (degree.getKtam() / degree.getKmin())) - 1))
					* degree.getP();
		}
		return confDegree;
	}

}

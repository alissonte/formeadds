package br.com.meadds.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityHome;

import br.ufc.meadds.entity.Algorithm;
import br.ufc.meadds.entity.AlgorithmConfidentialityDegree;
import br.ufc.meadds.entity.Application;
import br.ufc.meadds.entity.ConfidentialityDegree;
import br.ufc.meadds.entity.Library;
import br.ufc.meadds.entity.SampleProfile;
import br.ufc.meadds.entity.User;

@Name("libraryHome")
public class LibraryHome extends EntityHome<Library> {

	private static final String ADMIN_QUERY = "select o from User o join o.roles r where r.name = 'admin'";

	/**
     * 
     */
	private static final long serialVersionUID = 493651985853650371L;

	@In(create = true)
	private ConfidentialityDegreeList confidentialityDegreeList;

	@In(create = true)
	private UserHome userHome;

	private final User user = (User) Contexts.getSessionContext().get("user");

	private User admin;

	private Boolean libraryCustomize;

	@In(create = true)
	UserList userList;

	private List<ConfidentialityDegree> list;

	public void setLibraryId(Long id) {
		setId(id);
	}

	public Long getLibraryId() {
		return (Long) getId();
	}

	@Override
	@Transactional
	public String persist() {
		getInstance().getUsers().add(
				userList.getEntityManager().find(User.class, user.getId()));
		return super.persist();
	}

	@Transactional
	public String customize() {
		Set<Algorithm> algoritmos = getInstance().getAlgorithms();
		userList.setUser(user);
		User object = userList.getSingleResult();
		for (Algorithm algorithm : algoritmos) {
			AlgorithmConfidentialityDegree degree = algorithm
					.getAlgorithmConfidentialityDegree(object);
			object.getAlgorithmconfidentialitydegrees().add(degree);
		}
		userHome.setInstance(object);
		userHome.update();
		instance.getUsers().add(object);
		super.update();
		return "customized";
	}

	@Override
	protected Library createInstance() {
		Library library = new Library();
		return library;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public String getLevel(Integer value) {
		String level = null;
		if (list == null) {
			confidentialityDegreeList.setOrderColumn("minimumValue");
			list = confidentialityDegreeList.getResultList();
		}
		for (ConfidentialityDegree o : list) {
			if (o.getMinimumValue() <= value && value < o.getMaximumValue()) {
				level = o.getName();
				break;
			}
		}
		return level;
	}

	public void wire() {
		if (libraryCustomize != null && libraryCustomize) {
			Set<Algorithm> algoritmos = getInstance().getAlgorithms();
			for (Algorithm algorithm : algoritmos) {
				if (algorithm.getAlgorithmConfidentialityDegree(user) == null) {
					AlgorithmConfidentialityDegree degree = algorithm
							.getAlgorithmConfidentialityDegree(getAdmin());
					AlgorithmConfidentialityDegree copy = new AlgorithmConfidentialityDegree();
					copy.setUser(user);
					copy.setAlgorithm(algorithm);
					copy.setKmin(degree.getKmin());
					copy.setKtam(degree.getKtam());
					copy.setP(degree.getP());
					algorithm.getAlgorithmConfidentialityDegrees().add(copy);
				}
			}
		}
		getInstance();
	}

	public boolean canCustomize() {
		boolean can = false;
		for (Algorithm o : getInstance().getAlgorithms()) {
			if (o.getAlgorithmConfidentialityDegree(user) == null) {
				can = true;
				break;
			}
		}
		return can;
	}

	public boolean isWired() {
		return true;
	}

	public Library getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Algorithm> getAlgorithms() {
		return getInstance() == null ? null : new ArrayList<Algorithm>(
				getInstance().getAlgorithms());
	}

	public List<Application> getApplications() {
		return getInstance() == null ? null : new ArrayList<Application>(
				getInstance().getApplications());
	}

	public List<SampleProfile> getSampleprofiles() {
		return getInstance() == null ? null : new ArrayList<SampleProfile>(
				getInstance().getSampleprofiles());
	}

	/**
	 * @return the libraryCustomize
	 */
	public Boolean getLibraryCustomize() {
		return libraryCustomize;
	}

	/**
	 * @param libraryCustomize
	 *            the libraryCustomize to set
	 */
	public void setLibraryCustomize(Boolean libraryCustomize) {
		this.libraryCustomize = libraryCustomize;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return the admin
	 */
	public User getAdmin() {
		if (admin == null) {
			admin = (User) userList.getEntityManager().createQuery(ADMIN_QUERY)
					.getSingleResult();
			System.out.println(admin.getLogin() + admin.getName()
					+ admin.getCpf() + admin.getId());
		}
		return admin;
	}

}

package br.com.meadds.session;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityHome;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import br.ufc.meadds.entity.Algorithm;
import br.ufc.meadds.entity.AlgorithmConfidentialityDegree;
import br.ufc.meadds.entity.AlgorithmSampleProfile;
import br.ufc.meadds.entity.Application;
import br.ufc.meadds.entity.ConfidentialityDegree;
import br.ufc.meadds.entity.Library;
import br.ufc.meadds.entity.SampleProfile;
import br.ufc.meadds.entity.User;

@Name("applicationHome")
public class ApplicationHome extends EntityHome<Application> {

	/**
     * 
     */
	private static final long serialVersionUID = -2517789057477125964L;
	@In(create = true)
	ConfidentialityDegreeHome confidentialityDegreeHome;
	@In(create = true)
	LibraryHome libraryHome;
	@In(create = true)
	UserHome userHome;
	private final User user = (User) Contexts.getSessionContext().get("user");

	private List<User> classification;

	private List<SampleProfile> profiles;

	private SampleProfile profile;

	private User definedUser = new User();

	public void setApplicationId(Long id) {
		setId(id);
	}

	public Long getApplicationId() {
		return (Long) getId();
	}

	@Override
	protected Application createInstance() {
		Application application = new Application();
		application.setUser(user);
		return application;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public String downloadXML() {

		FacesContext ctx = FacesContext.getCurrentInstance();

		Application app = getInstance();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		if (db != null) {
			Document doc = db.newDocument();
			Element application = doc.createElement("application");
			application.setAttribute("confidentialitydegree", app
					.getConfidentialityDegree().getName());
			application.setAttribute("confidentialitydegreemaximum", String
					.valueOf(app.getConfidentialityDegree().getMaximumValue()));
			application.setAttribute("confidentialitydegreeminimum", String
					.valueOf(app.getConfidentialityDegree().getMinimumValue()));

			int count = 0;
			double sum = 0;
			definedUser = (User) getEntityManager()
					.createQuery("select o from User o where o.id = :id")
					.setParameter("id", definedUser.getId()).getSingleResult();
			for (AlgorithmConfidentialityDegree o : definedUser
					.getAlgorithmconfidentialitydegrees()) {
				if (o.getAlgorithm().getLibrary().getId().equals(app.getLibrary().getId())) {
					sum += (Math.pow(2, (o.getKtam() / o.getKmin())) - 1)
							* o.getP();
					count++;
				}
			}
			application.setAttribute("overallaveragealgconfdegree",
					String.valueOf(sum / (double) count));
			count = 0;
			sum = 0;
			for (AlgorithmSampleProfile a : profile.getSampleProfiles()) {
				sum += a.getAverageConsumption();
				count++;
			}

			application.setAttribute("overallaveragealgconsrate",
					String.valueOf(sum / (double) count));

			application.setAttribute("name", app.getName());
			application.setAttribute("version",
					String.valueOf(app.getVersion()));

			Element library = doc.createElement("library");
			application.appendChild(library);
			Library lib = app.getLibrary();
			library.setAttribute("name", lib.getName());
			library.setAttribute("programminglanguage", lib.getLanguage());
			library.setAttribute("platform", lib.getPlataform());
			library.setAttribute("version", String.valueOf(lib.getVersion()));

			for (Algorithm a : lib.getAlgorithms()) {
				if (a.getAlgorithmConfidentialityDegree(user).getP() > 0) {
					Element algorithm = doc.createElement("algorithm");
					algorithm.setAttribute("name", a.getName());
					algorithm.setAttribute("typeofalgorithm", a.getType()
							.getName());
					algorithm.setAttribute("operationmode", a
							.getOperationMode().getName());
					algorithm.setAttribute("keylengthdefault",
							String.valueOf(a.getKeyLenghtDefault()));
					for (AlgorithmSampleProfile o : profile.getSampleProfiles()) {
						if (o.getAlgorithm().equals(a)) {
							algorithm.setAttribute("myconsumptionrate",
									String.valueOf(o.getAverageConsumption()));
							algorithm.setAttribute("encrypttime",
									String.valueOf(o.getAverageEncryptTime()));
							algorithm.setAttribute("decrypttime",
									String.valueOf(o.getAverageDecryptTime()));
							algorithm.setAttribute("memoryrequired", String
									.valueOf(o.getAverageMemoryRequired()));
							break;
						}
					}
					for (AlgorithmConfidentialityDegree o : definedUser
							.getAlgorithmconfidentialitydegrees()) {
						if (o.getAlgorithm().equals(a)
								&& o.getAlgorithm().getLibrary()
										.equals(app.getLibrary())) {
							algorithm.setAttribute(
									"confidentialitydegreeofmyalgorithm",
									String.valueOf((Math.pow(2,
											(o.getKtam() / o.getKmin())) - 1)
											* o.getP()));
							break;
						}
					}
					library.appendChild(algorithm);
				}
			}

			HttpServletResponse response = (HttpServletResponse) ctx
					.getExternalContext().getResponse();

			try {
				Source source = new DOMSource(application);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Result result = new StreamResult(out);
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer();
				transformer.transform(source, result);
				byte[] file = out.toByteArray();

				response.setHeader("Content-disposition",
						"attachment; filename=application.xml");
				response.setContentType("text/xml");
				response.setContentLength(file.length);

				response.getOutputStream().write(file);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		ctx.responseComplete();

		return null;
	}

	public void wire() {
		getInstance();
		ConfidentialityDegree confidentialityDegree = confidentialityDegreeHome
				.getDefinedInstance();
		if (confidentialityDegree != null) {
			getInstance().setConfidentialityDegree(confidentialityDegree);
		}
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
		if (getInstance().getConfidentialityDegree() == null)
			return false;
		if (getInstance().getLibrary() == null)
			return false;
		if (getInstance().getUser() == null)
			return false;
		return true;
	}

	public Application getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	/**
	 * @return the definedUser
	 */
	public User getDefinedUser() {
		return definedUser;
	}

	/**
	 * @param definedUser
	 *            the definedUser to set
	 */
	public void setDefinedUser(User definedUser) {
		this.definedUser = definedUser;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return the classification
	 */
	public List<User> getClassification() {
		if (classification == null) {
			classification = new LinkedList<User>();
			classification.add((User) getEntityManager().createQuery(
					"select o from User o where o.login = 'admin'")
					.getSingleResult());
			User tmp = (User) getEntityManager()
					.createQuery("select o from User o where o.id = :id")
					.setParameter("id", user.getId()).getSingleResult();
			if (tmp.getLibraries().contains(getInstance().getLibrary()) && !classification.contains(tmp)) {
				classification.add(tmp);
			}
		}
		return classification;
	}

	/**
	 * @param classification
	 *            the classification to set
	 */
	public void setClassification(List<User> classification) {
		this.classification = classification;
	}

	/**
	 * @return the profiles
	 */
	public List<SampleProfile> getProfiles() {
		if (profiles == null) {
			profiles = new LinkedList<SampleProfile>();
			User tmp = (User) getEntityManager()
					.createQuery("select o from User o where o.id = :id")
					.setParameter("id", user.getId()).getSingleResult();
			if (tmp.getSampleprofiles() != null
					&& !tmp.getSampleprofiles().isEmpty()) {
				profiles.addAll(tmp.getSampleprofiles());
			}
			tmp = (User) getEntityManager().createQuery(
					"select o from User o where o.login = 'admin'")
					.getSingleResult();
			if (tmp.getSampleprofiles() != null
					&& !tmp.getSampleprofiles().isEmpty()) {
				profiles.addAll(tmp.getSampleprofiles());
			}
		}
		return profiles;
	}

	/**
	 * @param profiles
	 *            the profiles to set
	 */
	public void setProfiles(List<SampleProfile> profiles) {
		this.profiles = profiles;
	}

	/**
	 * @return the profile
	 */
	public SampleProfile getProfile() {
		return profile;
	}

	/**
	 * @param profile
	 *            the profile to set
	 */
	public void setProfile(SampleProfile profile) {
		this.profile = profile;
	}

}

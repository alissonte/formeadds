package br.com.meadds.session;

import br.ufc.meadds.entity.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("algorithmList")
public class AlgorithmList extends EntityQuery<Algorithm> {

    /**
     * 
     */
    private static final long serialVersionUID = -1792996621114490290L;

    private static final String EJBQL = "select algorithm from Algorithm algorithm";

    private static final String[] RESTRICTIONS =
            {"lower(algorithm.name) like lower(concat(#{algorithmList.algorithm.name},'%'))",};

    private Algorithm algorithm = new Algorithm();
    
    private final User user = (User) Contexts.getSessionContext().get("user");

    public AlgorithmList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(25);
        setRestrictionLogicOperator("or");
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

	/**
	 * @return the loggedUser
	 */
	public User getUser() {
		return user;
	}
}

package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("confidentialityDegreeList")
public class ConfidentialityDegreeList extends EntityQuery<ConfidentialityDegree> {

    /**
     * 
     */
    private static final long serialVersionUID = -7459649834455013422L;

    private static final String EJBQL =
            "select confidentialityDegree from ConfidentialityDegree confidentialityDegree";

    private static final String[] RESTRICTIONS =
            {"lower(confidentialityDegree.name) like lower(concat(#{confidentialityDegreeList.confidentialityDegree.name},'%'))",};

    private ConfidentialityDegree confidentialityDegree = new ConfidentialityDegree();

    public ConfidentialityDegreeList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(25);
        setRestrictionLogicOperator("or");
    }

    public ConfidentialityDegree getConfidentialityDegree() {
        return confidentialityDegree;
    }
}

package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("algorithmConfidentialityDegreeList")
public class AlgorithmConfidentialityDegreeList extends EntityQuery<AlgorithmConfidentialityDegree> {

    /**
     * 
     */
    private static final long serialVersionUID = 1156253323137789933L;

    private static final String EJBQL =
            "select algorithmConfidentialityDegree from AlgorithmConfidentialityDegree algorithmConfidentialityDegree";

    private static final String[] RESTRICTIONS = {};

    private AlgorithmConfidentialityDegree algorithmConfidentialityDegree = new AlgorithmConfidentialityDegree();

    public AlgorithmConfidentialityDegreeList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(25);
    }

    public AlgorithmConfidentialityDegree getAlgorithmConfidentialityDegree() {
        return algorithmConfidentialityDegree;
    }
}

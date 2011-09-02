package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("algorithmTypeList")
public class AlgorithmTypeList extends EntityQuery<AlgorithmType> {

    /**
     * 
     */
    private static final long serialVersionUID = -2180141372324594050L;

    private static final String EJBQL = "select algorithmType from AlgorithmType algorithmType";

    private static final String[] RESTRICTIONS =
            {"lower(algorithmType.name) like lower(concat(#{algorithmTypeList.algorithmType.name},'%'))",};

    private AlgorithmType algorithmType = new AlgorithmType();

    public AlgorithmTypeList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(25);
    }

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }
}

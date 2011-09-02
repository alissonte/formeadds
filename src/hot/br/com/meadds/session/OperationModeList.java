package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("operationModeList")
public class OperationModeList extends EntityQuery<OperationMode> {

    /**
     * 
     */
    private static final long serialVersionUID = -5824163557509484858L;

    private static final String EJBQL = "select operationMode from OperationMode operationMode";

    private static final String[] RESTRICTIONS =
            {"lower(operationMode.name) like lower(concat(#{operationModeList.operationMode.name},'%'))",};

    private OperationMode operationMode = new OperationMode();

    public OperationModeList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(25);
        setRestrictionLogicOperator("or");
    }

    public OperationMode getOperationMode() {
        return operationMode;
    }
}

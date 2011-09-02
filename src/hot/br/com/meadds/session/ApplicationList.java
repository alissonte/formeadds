package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("applicationList")
public class ApplicationList extends EntityQuery<Application> {

    /**
     * 
     */
    private static final long serialVersionUID = 6894059274330741155L;

    private static final String EJBQL = "select application from Application application";

    private static final String[] RESTRICTIONS =
            {"lower(application.name) like lower(concat(#{applicationList.application.name},'%'))",};

    private Application application = new Application();

    public ApplicationList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(25);
        setRestrictionLogicOperator("or");
    }

    public Application getApplication() {
        return application;
    }
}

package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("algorithmSampleProfileList")
public class AlgorithmSampleProfileList extends EntityQuery<AlgorithmSampleProfile> {

    /**
     * 
     */
    private static final long serialVersionUID = 139138023347504176L;

    private static final String EJBQL =
            "select algorithmSampleProfile from AlgorithmSampleProfile algorithmSampleProfile";

    private static final String[] RESTRICTIONS = {};

    private AlgorithmSampleProfile algorithmSampleProfile = new AlgorithmSampleProfile();

    public AlgorithmSampleProfileList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(25);
    }

    public AlgorithmSampleProfile getAlgorithmSampleProfile() {
        return algorithmSampleProfile;
    }
}

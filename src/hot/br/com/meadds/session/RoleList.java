package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("roleList")
public class RoleList extends EntityQuery<Role> {

    /**
     * 
     */
    private static final long serialVersionUID = 3953286774867027567L;

    private static final String EJBQL = "select role from Role role";

    private static final String[] RESTRICTIONS = {"lower(role.name) like lower(concat(#{roleList.role.name},'%'))",};

    private Role role = new Role();

    public RoleList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(25);
        setRestrictionLogicOperator("or");
    }

    public Role getRole() {
        return role;
    }
}

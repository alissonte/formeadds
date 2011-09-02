package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("roleHome")
public class RoleHome extends EntityHome<Role> {

    /**
     * 
     */
    private static final long serialVersionUID = -5609555209318319369L;

    public void setRoleId(Long id) {
        setId(id);
    }

    public Long getRoleId() {
        return (Long) getId();
    }

    @Override
    protected Role createInstance() {
        Role role = new Role();
        return role;
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    public Role getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

}

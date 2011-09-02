package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("confidentialityDegreeHome")
public class ConfidentialityDegreeHome extends EntityHome<ConfidentialityDegree> {

    /**
     * 
     */
    private static final long serialVersionUID = -8936200339557947639L;

    public void setConfidentialityDegreeId(Long id) {
        setId(id);
    }

    public Long getConfidentialityDegreeId() {
        return (Long) getId();
    }

    @Override
    protected ConfidentialityDegree createInstance() {
        ConfidentialityDegree confidentialityDegree = new ConfidentialityDegree();
        return confidentialityDegree;
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

    public ConfidentialityDegree getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public List<Application> getApplications() {
        return getInstance() == null ? null : new ArrayList<Application>(getInstance().getApplications());
    }

}

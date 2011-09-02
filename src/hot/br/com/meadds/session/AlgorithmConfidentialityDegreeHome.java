package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("algorithmConfidentialityDegreeHome")
public class AlgorithmConfidentialityDegreeHome extends EntityHome<AlgorithmConfidentialityDegree> {

    /**
     * 
     */
    private static final long serialVersionUID = -3360790657587342109L;
    @In(create = true)
    AlgorithmHome algorithmHome;
    @In(create = true)
    UserHome userHome;

    public void setAlgorithmConfidentialityDegreeId(Long id) {
        setId(id);
    }

    public Long getAlgorithmConfidentialityDegreeId() {
        return (Long) getId();
    }

    @Override
    protected AlgorithmConfidentialityDegree createInstance() {
        AlgorithmConfidentialityDegree algorithmConfidentialityDegree = new AlgorithmConfidentialityDegree();
        return algorithmConfidentialityDegree;
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        getInstance();
        Algorithm algorithm = algorithmHome.getDefinedInstance();
        if (algorithm != null) {
            getInstance().setAlgorithm(algorithm);
        }
        User user = userHome.getDefinedInstance();
        if (user != null) {
            getInstance().setUser(user);
        }
    }

    public boolean isWired() {
        if (getInstance().getAlgorithm() == null)
            return false;
        if (getInstance().getUser() == null)
            return false;
        return true;
    }

    public AlgorithmConfidentialityDegree getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

}

package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("algorithmSampleProfileHome")
public class AlgorithmSampleProfileHome extends EntityHome<AlgorithmSampleProfile> {

    /**
     * 
     */
    private static final long serialVersionUID = -1323912198533355878L;
    @In(create = true)
    AlgorithmHome algorithmHome;
    @In(create = true)
    SampleProfileHome sampleProfileHome;

    public void setAlgorithmSampleProfileId(Long id) {
        setId(id);
    }

    public Long getAlgorithmSampleProfileId() {
        return (Long) getId();
    }

    @Override
    protected AlgorithmSampleProfile createInstance() {
        AlgorithmSampleProfile algorithmSampleProfile = new AlgorithmSampleProfile();
        return algorithmSampleProfile;
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
        SampleProfile sampleProfile = sampleProfileHome.getDefinedInstance();
        if (sampleProfile != null) {
            getInstance().setSampleProfile(sampleProfile);
        }
    }

    public boolean isWired() {
        if (getInstance().getAlgorithm() == null)
            return false;
        if (getInstance().getSampleProfile() == null)
            return false;
        return true;
    }

    public AlgorithmSampleProfile getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

}

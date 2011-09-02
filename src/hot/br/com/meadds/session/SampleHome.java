package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sampleHome")
public class SampleHome extends EntityHome<Sample> {

    /**
     * 
     */
    private static final long serialVersionUID = -3082807046906885973L;
    @In(create = true)
    AlgorithmHome algorithmHome;
    @In(create = true)
    DeviceHome deviceHome;
    @In(create = true)
    UserHome userHome;

    public void setSampleId(Long id) {
        setId(id);
    }

    public Long getSampleId() {
        return (Long) getId();
    }

    @Override
    protected Sample createInstance() {
        Sample sample = new Sample();
        return sample;
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
        Device device = deviceHome.getDefinedInstance();
        if (device != null) {
            getInstance().setDevice(device);
        }
        User user = userHome.getDefinedInstance();
        if (user != null) {
            getInstance().setUser(user);
        }
    }

    public boolean isWired() {
        if (getInstance().getAlgorithm() == null)
            return false;
        if (getInstance().getDevice() == null)
            return false;
        if (getInstance().getUser() == null)
            return false;
        return true;
    }

    public Sample getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

}

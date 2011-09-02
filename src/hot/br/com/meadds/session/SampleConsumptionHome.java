package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sampleConsumptionHome")
public class SampleConsumptionHome extends EntityHome<SampleConsumption> {

    /**
     * 
     */
    private static final long serialVersionUID = -2874296547754507969L;
    @In(create = true)
    AlgorithmHome algorithmHome;
    @In(create = true)
    DeviceHome deviceHome;
    @In(create = true)
    UserHome userHome;

    public void setSampleConsumptionId(Long id) {
        setId(id);
    }

    public Long getSampleConsumptionId() {
        return (Long) getId();
    }

    @Override
    protected SampleConsumption createInstance() {
        SampleConsumption sampleConsumption = new SampleConsumption();
        return sampleConsumption;
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

    public SampleConsumption getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

}

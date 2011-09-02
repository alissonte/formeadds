package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("algorithmTypeHome")
public class AlgorithmTypeHome extends EntityHome<AlgorithmType> {

    /**
     * 
     */
    private static final long serialVersionUID = 2197050166188133000L;

    public void setAlgorithmTypeId(Long id) {
        setId(id);
    }

    public Long getAlgorithmTypeId() {
        return (Long) getId();
    }

    @Override
    protected AlgorithmType createInstance() {
        AlgorithmType algorithmType = new AlgorithmType();
        return algorithmType;
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

    public AlgorithmType getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public List<Algorithm> getAlgorithms() {
        return getInstance() == null ? null : new ArrayList<Algorithm>(getInstance().getAlgorithms());
    }

}

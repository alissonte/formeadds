package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("operationModeHome")
public class OperationModeHome extends EntityHome<OperationMode> {

    /**
     * 
     */
    private static final long serialVersionUID = -1225042073756849156L;

    public void setOperationModeId(Long id) {
        setId(id);
    }

    public Long getOperationModeId() {
        return (Long) getId();
    }

    @Override
    protected OperationMode createInstance() {
        OperationMode operationMode = new OperationMode();
        return operationMode;
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

    public OperationMode getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public List<Algorithm> getAlgorithms() {
        return getInstance() == null ? null : new ArrayList<Algorithm>(getInstance().getAlgorithms());
    }

}

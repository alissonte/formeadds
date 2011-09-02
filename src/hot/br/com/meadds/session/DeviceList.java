package br.com.meadds.session;

import br.ufc.meadds.entity.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("deviceList")
public class DeviceList extends EntityQuery<Device> {

    /**
     * 
     */
    private static final long serialVersionUID = 1381532407540486861L;

    private static final String EJBQL = "select device from Device device join device.user user";

    private static final String[] RESTRICTIONS = {
        "lower(device.additionalInfo) like lower(concat(#{deviceList.device.additionalInfo},'%'))",
        "lower(device.batteryType) like lower(concat(#{deviceList.device.batteryType},'%'))",
        "lower(device.manufaturer) like lower(concat(#{deviceList.device.manufaturer},'%'))",
        "lower(device.name) like lower(concat(#{deviceList.device.name},'%'))",
        "(user.id = #{deviceList.device.user.id} or user.name = 'admin')",
        "lower(device.virtualMachineName) like lower(concat(#{deviceList.device.virtualMachineName},'%'))",};

    private Device device = new Device();

    private final User user = (User) Contexts.getSessionContext().get("user");

    public DeviceList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(25);
        setRestrictionLogicOperator("or");
        device.setUser(user);
    }

    public Device getDevice() {
        return device;
    }
}

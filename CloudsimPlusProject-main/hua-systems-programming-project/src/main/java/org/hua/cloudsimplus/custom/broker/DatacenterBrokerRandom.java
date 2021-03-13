package org.hua.cloudsimplus.custom.broker;

import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.Random;

public class DatacenterBrokerRandom extends DatacenterBrokerSimple
{
    private Random rng;

    public DatacenterBrokerRandom(CloudSim simulation)
    {
        super(simulation);
        this.rng = new Random();
    }

    public DatacenterBrokerRandom(CloudSim simulation, String name)
    {
        super(simulation, name);
        this.rng = new Random();
    }

    public DatacenterBrokerRandom(CloudSim simulation, Random rng)
    {
        super(simulation);
        this.rng = rng;
    }

    public DatacenterBrokerRandom(CloudSim simulation, String name, Random rng)
    {
        super(simulation, name);
        this.rng = rng;
    }

    @Override
    protected Vm defaultVmMapper(Cloudlet cloudlet)
    {
        if (cloudlet.isBoundToVm()) {
            return cloudlet.getVm();
        }

        if (getVmExecList().isEmpty()) {
            return Vm.NULL;
        }

        int vmSize = getVmExecList().size();
        return getVmFromCreatedList(rng.nextInt(vmSize));
    }
}

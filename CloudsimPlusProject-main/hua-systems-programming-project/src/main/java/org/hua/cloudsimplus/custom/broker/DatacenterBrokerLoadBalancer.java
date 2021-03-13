package org.hua.cloudsimplus.custom.broker;

import org.cloudbus.cloudsim.brokers.*;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.*;
import java.util.stream.Collectors;

public class DatacenterBrokerLoadBalancer extends DatacenterBrokerSimple
{

    public DatacenterBrokerLoadBalancer(final CloudSim simulation) {
        this(simulation, "");
    }

    public DatacenterBrokerLoadBalancer(final CloudSim simulation, final String name) {
        super(simulation, name);
    }

    @Override
    protected Vm defaultVmMapper(final Cloudlet cloudlet)
    {
        if (cloudlet.isBoundToVm()) {
            return cloudlet.getVm();
        }

        if (getVmExecList().isEmpty()) {
            return Vm.NULL;
        }

        Map<Vm,Double> mipsPerVM = getCloudletSubmittedList()
                .stream()
                .collect(Collectors.groupingBy(Cloudlet::getVm,Collectors.summingDouble(Cloudlet::getLength)));

        getVmExecList().forEach(vm -> mipsPerVM.putIfAbsent(vm,0D));
        mipsPerVM.remove(Vm.NULL);

        return mipsPerVM
                .entrySet()
                .stream()
                .min(Comparator.comparingDouble(Map.Entry::getValue))
                .get().getKey();

    }
}




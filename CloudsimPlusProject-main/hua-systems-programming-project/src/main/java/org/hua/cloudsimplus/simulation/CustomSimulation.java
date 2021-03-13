package org.hua.cloudsimplus.simulation;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.cloudsimplus.autoscaling.HorizontalVmScaling;
import org.cloudsimplus.autoscaling.HorizontalVmScalingSimple;
import org.hua.cloudsimplus.custom.table.CustomCloudletsTableBuilder;
import org.hua.cloudsimplus.sla.SlaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomSimulation
{

    private int hosts;
    private int hostPEs;
    private int hostPEMIPS;

    private int VMs;
    private int VMPEs;
    private int VMPEMIPS;


    private CloudSim simulation;
    private DatacenterBroker broker;
    private List<Cloudlet> taskList;

    private int vmId = 0;


    public CustomSimulation(DatacenterBroker broker, List<Cloudlet> taskList,
                            int hosts, int hostPEs, int hostPEMIPS,
                            int VMs, int VMPEs, int VMPEMIPS)
    {
        this.hosts = hosts;
        this.hostPEs = hostPEs;
        this.hostPEMIPS = hostPEMIPS;
        this.VMs = VMs;
        this.VMPEs = VMPEs;
        this.VMPEMIPS = VMPEMIPS;
        this.simulation = (CloudSim)broker.getSimulation();
        this.taskList = taskList;
        this.broker = broker;

        createDatacenter();

        this.broker.setVmDestructionDelayFunction(vm -> 10.0);
        this.broker.submitVmList(createListOfScalableVms(VMs));
        this.broker.submitCloudletList(taskList);
    }

    public void startSimulation()
    {
        simulation.start();
        final List<Cloudlet> finishedCloudlets = broker.getCloudletFinishedList();
        new CustomCloudletsTableBuilder(finishedCloudlets).build();
        System.out.printf("Simulation finished in: %.2f seconds.%n",simulation.clock());
        calculateMetrics();

    }

    private void calculateMetrics()
    {
        final List<Cloudlet> finishedCloudlets = broker.getCloudletFinishedList();

        Double averageExecutionLatency = finishedCloudlets
                .stream()
                .mapToDouble(Cloudlet::getActualCpuTime)
                .average()
                .orElse(Double.NaN);

        Double averageMI = finishedCloudlets
                .stream()
                .mapToDouble(Cloudlet::getLength)
                .average()
                .orElse(Double.NaN);

        double mips90thPercentile = new Percentile()
                .evaluate(finishedCloudlets
                        .stream()
                        .mapToDouble(Cloudlet::getLength)
                        .toArray(),90D);

        var tailTasks = finishedCloudlets
                .stream()
                .filter(cloudlet-> cloudlet.getLength() >= mips90thPercentile)
                .collect(Collectors.toList());

        Double tailTasksAverage = tailTasks.stream()
                .mapToDouble(Cloudlet::getActualCpuTime)
                .average()
                .orElse(Double.NaN);

        System.out.printf("Total tasks executed: %d%n",finishedCloudlets.size());
        System.out.printf("Average MI: %.2f%n",averageMI);
        System.out.printf("Average execution latency: %.2f%n",averageExecutionLatency);
        System.out.printf("MI 90th percentile: %.2f%n",mips90thPercentile);
        System.out.printf("Average tail execution latency: %.2f%n",tailTasksAverage);
        System.out.printf("Maximum task execution time violations: %d%n", SlaUtils.getTimeExecutionViolations(finishedCloudlets));

    }


    private Datacenter createDatacenter() {
        final List<Host> hostList = new ArrayList<>(hosts);
        for(int i = 0; i < hosts; i++) {
            Host host = createHost()
                    .setVmScheduler(new VmSchedulerTimeShared());
            hostList.add(host);
        }

        DatacenterSimple dc =  new DatacenterSimple(simulation, hostList);
        dc.setSchedulingInterval(5);
        return dc;
    }

    private Host createHost() {
        final List<Pe> peList = new ArrayList<>();
        for (int i = 0; i < hostPEs ; i++)
            peList.add(new PeSimple(hostPEMIPS));

        return new HostSimple(peList,true);
    }

    private Vm createVm() {
        return new VmSimple(vmId++, VMPEMIPS, VMPEs)
                .setRam(512).setBw(1000).setSize(10000)
                .setCloudletScheduler(new CloudletSchedulerTimeShared());
    }

    private List<Vm> createListOfScalableVms(final int numberOfVms) {
        List<Vm> newList = new ArrayList<>(numberOfVms);
        for (int i = 0; i < numberOfVms; i++) {
            Vm vm = createVm();
            createHorizontalVmScaling(vm);
            newList.add(vm);
        }

        return newList;
    }


    private void createHorizontalVmScaling(Vm vm) {
        HorizontalVmScaling horizontalScaling = new HorizontalVmScalingSimple();
        horizontalScaling
                .setVmSupplier(this::createVm)
                .setOverloadPredicate(this::isVmOverloaded);
        vm.setHorizontalScaling(horizontalScaling);
    }

    private boolean isVmOverloaded(Vm vm) {
        return vm.getCpuPercentUtilization() > 0.6;
    }

}

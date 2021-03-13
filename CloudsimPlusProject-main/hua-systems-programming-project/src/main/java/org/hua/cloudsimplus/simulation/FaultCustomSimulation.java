package org.hua.cloudsimplus.simulation;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.distributions.PoissonDistr;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.cloudsimplus.faultinjection.HostFaultInjection;
import org.cloudsimplus.faultinjection.VmClonerSimple;
import org.hua.cloudsimplus.custom.table.CustomCloudletsTableBuilder;
import org.hua.cloudsimplus.sla.SlaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FaultCustomSimulation
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

    private static final double MEAN_FAILURE_NUMBER_PER_HOUR = 100;

    private HostFaultInjection fault;
    private PoissonDistr poisson;

    public FaultCustomSimulation(DatacenterBroker broker, List<Cloudlet> taskList,
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

        createFaultInjectionForHosts(createDatacenter());

        this.broker.submitVmList(createVms());
        this.broker.submitCloudletList(taskList);

    }

    private void createFaultInjectionForHosts(Datacenter datacenter) {
        this.poisson = new PoissonDistr(MEAN_FAILURE_NUMBER_PER_HOUR);

        fault = new HostFaultInjection(datacenter, poisson);
        fault.setMaxTimeToFailInHours(0);

        fault.addVmCloner(broker, new VmClonerSimple(this::cloneVm, this::cloneCloudlets));
    }

    public void startSimulation()
    {
        simulation.start();
        final List<Cloudlet> finishedCloudlets = broker.getCloudletFinishedList();
        new CustomCloudletsTableBuilder(finishedCloudlets).build();
        System.out.printf("Simulation finished in: %.2f seconds.%n",simulation.clock());
        calculateMetrics();

        System.out.printf("Maximum task execution time violations: %d%n", SlaUtils.getTimeExecutionViolations(finishedCloudlets));

        System.out.printf(
                "%nMean Number of Failures per Hour: %.3f (1 failure expected at each %.2f hours).%n",
                MEAN_FAILURE_NUMBER_PER_HOUR, poisson.getInterArrivalMeanTime());
        System.out.printf("Number of Host faults: %d%n", fault.getNumberOfHostFaults());
        System.out.printf("Number of VM faults (VMs destroyed): %d%n", fault.getNumberOfFaults());
        System.out.printf("Time the simulations finished: %.4f hours%n", simulation.clockInHours());
        System.out.printf("Mean Time To Repair Failures of VMs in minutes (MTTR): %.2f minute%n", fault.meanTimeToRepairVmFaultsInMinutes());
        System.out.printf("Mean Time Between Failures (MTBF) affecting all VMs in minutes: %.2f minutes%n", fault.meanTimeBetweenVmFaultsInMinutes());
        System.out.printf("Hosts MTBF: %.2f minutes%n", fault.meanTimeBetweenHostFaultsInMinutes());
        System.out.printf("Availability: %.2f%%%n%n", fault.availability()*100);

        double maxFaults = SlaUtils.getMaxFaultToleranceLevel();
        System.out.printf("%nMax faults allowed by SLA %.2f%n", maxFaults);

        double faults = fault.getNumberOfHostFaults();
        if(faults > maxFaults){
            System.out.printf("Faults: %.2f. Out of SLA%n",faults);
        } else {
            System.out.printf("Faults: %.2f. SLA contract ok%n",faults);
        }

        System.out.println(getClass().getSimpleName() + " finished!");

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

    }


    private Datacenter createDatacenter() {
        final List<Host> hostList = new ArrayList<>(hosts);
        for(int i = 0; i < hosts; i++) {
            Host host = createHost()
                    .setVmScheduler(new VmSchedulerTimeShared());
            hostList.add(host);
        }

        return new DatacenterSimple(simulation, hostList);
    }

    private Host createHost() {
        final List<Pe> peList = new ArrayList<>(hostPEs);
        for (int i = 0; i < hostPEs ; i++)
            peList.add(new PeSimple(hostPEMIPS));

        return new HostSimple(peList);
    }


    private List<Vm> createVms() {
        final List<Vm> list = new ArrayList<>();
        for (int i = 0; i < VMs; i++) {
            final Vm vm = new VmSimple(VMPEMIPS, VMPEs);
            list.add(vm);
        }
        return list;
    }

    private Vm cloneVm(Vm vm) {
        Vm clone = new VmSimple(vm.getMips(), (int) vm.getNumberOfPes());
        /*It' not required to set an ID for the clone.
        It is being set here just to make it easy to
        relate the ID of the vm to its clone,
        since the clone ID will be 10 times the id of its
        source VM.*/
        clone.setId(vm.getId() * 10);
        clone.setDescription("Clone of VM " + vm.getId());
        clone
                .setSize(vm.getStorage().getCapacity())
                .setBw(vm.getBw().getCapacity())
                .setRam(vm.getBw().getCapacity())
                .setCloudletScheduler(new CloudletSchedulerTimeShared());
        System.out.printf("%n%n# Cloning %s - MIPS %.2f Number of Pes: %d%n", vm, clone.getMips(), clone.getNumberOfPes());

        return clone;
    }

    private List<Cloudlet> cloneCloudlets(Vm sourceVm) {
        final List<Cloudlet> sourceVmCloudlets = sourceVm.getCloudletScheduler().getCloudletList();
        final List<Cloudlet> clonedCloudlets = new ArrayList<>(sourceVmCloudlets.size());
        for (Cloudlet cl : sourceVmCloudlets) {
            Cloudlet clone = cloneCloudlet(cl);
            clonedCloudlets.add(clone);
            System.out.printf("# Created Cloudlet Clone for %s (Cloned Cloudlet Id: %d)%n", sourceVm, clone.getId());
        }

        return clonedCloudlets;
    }

    private Cloudlet cloneCloudlet(Cloudlet source) {
        Cloudlet clone = new CloudletSimple(source.getLength(), source.getNumberOfPes());
        /*It' not required to set an ID for the clone.
        It is being set here just to make it easy to
        relate the ID of the cloudlet to its clone,
        since the clone ID will be 10 times the id of its
        source cloudlet.*/
        clone.setId(source.getId() * 10);
        clone
                .setUtilizationModelBw(source.getUtilizationModelBw())
                .setUtilizationModelCpu(source.getUtilizationModelCpu())
                .setUtilizationModelRam(source.getUtilizationModelRam());
        return clone;
    }
}

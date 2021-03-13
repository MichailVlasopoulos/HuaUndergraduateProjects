package org.hua;

import ch.qos.logback.classic.Level;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.distributions.ContinuousDistribution;
import org.cloudbus.cloudsim.distributions.NormalDistr;
import org.cloudsimplus.util.Log;
import org.hua.cloudsimplus.custom.broker.DatacenterBrokerLoadBalancer;
import org.hua.cloudsimplus.custom.broker.DatacenterBrokerRandom;
import org.hua.cloudsimplus.simulation.CustomSimulation;
import org.hua.cloudsimplus.simulation.Device;
import org.hua.cloudsimplus.simulation.FaultCustomSimulation;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@DisplayName("Simulating task offloading with")
public class DatacenterBrokerFaultSimulationTest
{
    int VMS = 6;
    int VM_PES = 2;
    int VM_PE_MIPS = 50000;

    int HOSTS = 4;
    int HOST_PES = VMS * VM_PES;
    int HOST_PE_MIPS = VM_PE_MIPS;

    int QUANTA = 3;

    DeviceMock deviceMock1;
    DeviceMock deviceMock2;
    DeviceMock deviceMock3;


    @BeforeAll
    void setUp()
    {

        //Sets logs off
        Log.setLevel(Level.OFF);

        deviceMock1 = new DeviceMock(
                new NormalDistr(100,10),
                new NormalDistr(20000,1000),
                QUANTA);

        deviceMock2 = new DeviceMock(
                new NormalDistr(50,5),
                new NormalDistr(50000,5000),
                QUANTA);

        deviceMock3 = new DeviceMock(
                new NormalDistr(25,5),
                new NormalDistr(100000,10000),
                QUANTA);

    }

    @AfterEach
    void tearDown()
    {
        deviceMock1.resetMock();
        deviceMock2.resetMock();
        deviceMock3.resetMock();
    }

    @Test
    @DisplayName("the default DatacenterBrokerSimple.")
    void testSimpleSimulation()
    {
        var sim = new FaultCustomSimulation(
                new DatacenterBrokerSimple(new CloudSim()),
                generateTasks(),
                HOSTS,
                HOST_PES,
                HOST_PE_MIPS,
                VMS,
                VM_PES,
                VM_PE_MIPS);

        sim.startSimulation();
    }

    @Test
    @DisplayName("a custom DatacenterBrokerRandom which selects a VM randomly.")
    void testRandomSimulation()
    {
        var sim = new FaultCustomSimulation(
                new DatacenterBrokerRandom(new CloudSim()),
                generateTasks(),
                HOSTS,
                HOST_PES,
                HOST_PE_MIPS,
                VMS,
                VM_PES,
                VM_PE_MIPS);

        sim.startSimulation();

    }

    @Test
    @DisplayName("a custom DatacenterBrokerLoadBalancer which selects the VM with the least total load.")
    void testLoadBalancerSimulation()
    {
        var sim = new FaultCustomSimulation(
                new DatacenterBrokerLoadBalancer(new CloudSim()),
                generateTasks(),
                HOSTS,
                HOST_PES,
                HOST_PE_MIPS,
                VMS,
                VM_PES,
                VM_PE_MIPS);

        sim.startSimulation();

    }

    private List<Cloudlet> generateTasks()
    {
        List<Cloudlet> tasks = new ArrayList<>();
        tasks.addAll(deviceMock1.getDevice().generateTasks(QUANTA));
        tasks.addAll(deviceMock2.getDevice().generateTasks(QUANTA));
        tasks.addAll(deviceMock3.getDevice().generateTasks(QUANTA));
        tasks.sort(Comparator.comparingDouble(Cloudlet::getSubmissionDelay));
        return tasks;
    }

    class DeviceMock
    {
        private ContinuousDistribution taskQuantityMock = mock(ContinuousDistribution.class);
        private ContinuousDistribution millionInstrMock = mock(ContinuousDistribution.class);
        private Device device;

        private int totalTasks;
        private Double[] taskQuantityDataset;
        private Double[] mipsDataset;

        public DeviceMock(ContinuousDistribution taskQuantityRNG,
                          ContinuousDistribution millionInstrRNG,
                          int quanta)
        {
            this.taskQuantityDataset = generateDistributionDataset(taskQuantityRNG,quanta);

            this.totalTasks = 0;
            for(int i = 0 ; i< quanta ; i++)
                this.totalTasks += taskQuantityDataset[i].intValue();

            this.mipsDataset = generateDistributionDataset(millionInstrRNG, totalTasks);
            this.device = new Device(taskQuantityMock,millionInstrMock);

            when(taskQuantityMock.sample()).thenReturn(taskQuantityDataset[0], taskQuantityDataset);
            when(millionInstrMock.sample()).thenReturn(mipsDataset[0], mipsDataset);
        }

        public Device getDevice()
        {
            return device;
        }

        public Double[] generateDistributionDataset(ContinuousDistribution distribution, int length)
        {
            Double[] dataset = new Double[length];
            for(int i = 0 ; i < length; i++)
                dataset[i] = distribution.sample();
            return dataset;
        }

        private void resetMock()
        {
            reset(taskQuantityMock, millionInstrMock);
            when(taskQuantityMock.sample()).thenReturn(taskQuantityDataset[0], taskQuantityDataset);
            when(millionInstrMock.sample()).thenReturn(mipsDataset[0], mipsDataset);
        }

    }

}

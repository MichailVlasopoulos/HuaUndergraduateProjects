package org.hua.cloudsimplus.simulation;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.distributions.ContinuousDistribution;

import java.util.ArrayList;
import java.util.List;

public class Device
{
    private ContinuousDistribution taskQuantityRNG;
    private ContinuousDistribution millionInstrRNG;
    private static int cloudletIdGenerator = 0;

    public Device(ContinuousDistribution taskQuantityRNG, ContinuousDistribution millionInstrRNG)
    {
        this.taskQuantityRNG = taskQuantityRNG;
        this.millionInstrRNG = millionInstrRNG;
    }

    public List<Cloudlet> generateTasks(int timeQuanta)
    {
        List<Cloudlet> tasks = new ArrayList<>();
        double delay = 0.0;
        if(timeQuanta <= 0) throw new IllegalArgumentException("timeQuanta must be over 0");

        for(int i = 0 ; i < timeQuanta ; i++)
        {
            for(int j = 0; j < (long)taskQuantityRNG.sample(); j++)
            {
                Cloudlet task = new CloudletSimple((long)millionInstrRNG.sample(),2);
                task.setSubmissionDelay(delay);
                delay += 0.25;
                tasks.add(task);
            }
        }
        return tasks;
    }

}

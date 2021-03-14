package org.hua.cloudsimplus.sla;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.List;

public class SlaUtils {

    private static SlaContract slaContract = SlaContract.getInstance("contract.json");

    public static int getTimeExecutionViolations(List<Cloudlet> finishedCloudlets){
        int violations = 0;
        double slaMaxExecTime = slaContract.getTaskCompletionTimeMetric().getMaxDimension().getValue();
        for(Cloudlet cloudlet : finishedCloudlets){
            if(cloudlet.getActualCpuTime()>slaMaxExecTime){
                violations++;
            }
        }
        return violations;
    }

    public static double getMaxFaultToleranceLevel(){
        return slaContract.getFaultToleranceLevel().getMaxDimension().getValue();
    }
}

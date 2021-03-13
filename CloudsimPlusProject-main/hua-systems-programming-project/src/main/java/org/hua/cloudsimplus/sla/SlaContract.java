/*
 * CloudSim Plus: A modern, highly-extensible and easier-to-use Framework for
 * Modeling and Simulation of Cloud Computing Infrastructures and Services.
 * http://cloudsimplus.org
 *
 *     Copyright (C) 2015-2018 Universidade da Beira Interior (UBI, Portugal) and
 *     the Instituto Federal de Educação Ciência e Tecnologia do Tocantins (IFTO, Brazil).
 *
 *     This file is part of CloudSim Plus.
 *
 *     CloudSim Plus is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     CloudSim Plus is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with CloudSim Plus. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hua.cloudsimplus.sla;

import com.google.gson.Gson;
import org.cloudbus.cloudsim.util.ResourceLoader;
import org.cloudsimplus.slametrics.SlaMetric;
import org.cloudsimplus.vmtemplates.AwsEc2Template;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SlaContract {
    private static final String AVAILABILITY = "Availability";
    private static final String TASK_COMPLETION_TIME = "TaskCompletionTime";
    private static final String CPU_UTILIZATION = "CpuUtilization";
    private static final String WAIT_TIME = "WaitTime";
    private static final String PRICE = "Price";
    private static final String FAULT_TOLERANCE_LEVEL = "FaultToleranceLevel";

    private List<SlaMetric> metrics;


    public SlaContract() {
        this.metrics = new ArrayList<>();
    }


    public static SlaContract getInstance(final String jsonFilePath) {
        return getInstanceInternal(ResourceLoader.newInputStream(jsonFilePath, SlaContract.class));
    }


    private static SlaContract getInstanceInternal(final InputStream inputStream) {
        return new Gson().fromJson(new InputStreamReader(inputStream), SlaContract.class);
    }

    public List<SlaMetric> getMetrics() {
        return metrics;
    }

    public void setMetrics(final List<SlaMetric> metrics) {
        this.metrics = metrics == null ? new ArrayList<>() : metrics;
    }

    private SlaMetric getSlaMetric(final String metricName) {
        return metrics
                .stream()
                .filter(metric -> metricName.equals(metric.getName()))
                .findFirst()
                .orElse(SlaMetric.NULL);
    }

    public SlaMetric getAvailabilityMetric() {
        return getSlaMetric(AVAILABILITY);
    }

    public SlaMetric getCpuUtilizationMetric() {
        return getSlaMetric(CPU_UTILIZATION);
    }

    public SlaMetric getPriceMetric() {
        return getSlaMetric(PRICE);
    }

    public SlaMetric getWaitTimeMetric() {
        return getSlaMetric(WAIT_TIME);
    }

    public SlaMetric getTaskCompletionTimeMetric() {
        return getSlaMetric(TASK_COMPLETION_TIME);
    }

    public SlaMetric getFaultToleranceLevel() {
        return getSlaMetric(FAULT_TOLERANCE_LEVEL);
    }

    public double getMaxPrice() {
        return getPriceMetric().getMaxDimension().getValue();
    }

    public double getExpectedMaxPriceForSingleVm() {
        return getMaxPrice() / getFaultToleranceLevel().getMinDimension().getValue();
    }

    public int getMinFaultToleranceLevel() {
        return (int)Math.floor(getFaultToleranceLevel().getMinDimension().getValue());
    }

    @Override
    public String toString() {
        return metrics.toString();
    }
}

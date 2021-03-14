package org.hua.cloudsimplus.custom.table;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.Identifiable;
import org.cloudsimplus.builders.tables.Table;
import org.cloudsimplus.builders.tables.TableBuilderAbstract;
import org.cloudsimplus.builders.tables.TableColumn;

import java.util.List;


public class CustomCloudletsTableBuilder extends TableBuilderAbstract<Cloudlet>
{
    private static final String TIME_FORMAT = "%.2f";
    private static final String SECONDS = "Seconds";
    private static final String CPU_CORES = "CPU cores";

    public CustomCloudletsTableBuilder(List<? extends Cloudlet> list)
    {
        super(list);
    }

    public CustomCloudletsTableBuilder(List<? extends Cloudlet> list, Table table)
    {
        super(list, table);
    }

    @Override
    protected void createTableColumns() {
        final String ID = "ID";
        addColumnDataFunction(getTable().addColumn("Cloudlet", ID), Identifiable::getId);
        addColumnDataFunction(getTable().addColumn("Status "), cloudlet -> cloudlet.getStatus().name());
        addColumnDataFunction(getTable().addColumn("DC", ID), cloudlet -> cloudlet.getVm().getHost().getDatacenter().getId());
        addColumnDataFunction(getTable().addColumn("Host", ID), cloudlet -> cloudlet.getVm().getHost().getId());
        addColumnDataFunction(getTable().addColumn("Host PEs ", CPU_CORES), cloudlet -> cloudlet.getVm().getHost().getWorkingPesNumber());
        addColumnDataFunction(getTable().addColumn("VM", ID), cloudlet -> cloudlet.getVm().getId());
        addColumnDataFunction(getTable().addColumn("VM PEs   ", CPU_CORES), cloudlet -> cloudlet.getVm().getNumberOfPes());
        addColumnDataFunction(getTable().addColumn("CloudletLen", "MI"), Cloudlet::getLength);
        addColumnDataFunction(getTable().addColumn("CloudletPEs", CPU_CORES), Cloudlet::getNumberOfPes);

        TableColumn col = getTable().addColumn("StartTime", SECONDS).setFormat(TIME_FORMAT);
        addColumnDataFunction(col, Cloudlet::getExecStartTime);

        col = getTable().addColumn("FinishTime", SECONDS).setFormat(TIME_FORMAT);
        addColumnDataFunction(col, Cloudlet::getFinishTime);

        col = getTable().addColumn("ExecTime", SECONDS).setFormat(TIME_FORMAT);
        addColumnDataFunction(col, Cloudlet::getActualCpuTime);
    }

}


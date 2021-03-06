package explorekit.operators.UnaryOperators;

import explorekit.data.Column;
import explorekit.data.ColumnInfo;
import explorekit.data.Dataset;
import explorekit.data.DiscreteColumn;

import java.util.Date;
import java.util.List;

/**
 * Created by giladkatz on 14/04/2016.
 */
public class IsWeekendUnaryOperator extends UnaryOperator {
    @Override
    public outputType requiredInputType() {
        return outputType.Date;
    }

    @Override
    public void processTrainingSet(Dataset dataset, List<ColumnInfo> sourceColumns, List<ColumnInfo> targetColumns) throws Exception {

    }

    @Override
    public ColumnInfo generate(Dataset dataset, List<ColumnInfo> sourceColumns, List<ColumnInfo> targetColumns, boolean enforceDistinctVal) {
        DiscreteColumn column = new DiscreteColumn(dataset.getNumOfInstancesPerColumn(), getNumOfBins());
        //this is the number of rows we need to work on - not the size of the vector
        int numOfRows = dataset.getNumberOfRows();
        ColumnInfo columnInfo = sourceColumns.get(0);
        for (int i=0; i<numOfRows; i++) {
            int j = dataset.getIndices().get(i);
            int dayIndex = ((Date) columnInfo.getColumn().getValue(j)).getDay();
            if (dayIndex == 6 || dayIndex == 0) {
                dayIndex = 1;
            }
            else {
                dayIndex = 0;
            }
            column.setValue(j, dayIndex);
        }

        //now we generate the name of the new attribute
        String attString = "IsWeekend(";
        attString = attString.concat(columnInfo.getName());
        attString = attString.concat(")");

        return new ColumnInfo(column, sourceColumns, targetColumns, this.getClass(), attString);
    }

    @Override
    public outputType getOutputType() {
        return outputType.Discrete;
    }

    @Override
    public String getName() {
        return "IsWeekendUnaryOperator";
    }

    public boolean isApplicable(Dataset dataset, List<ColumnInfo> sourceColumns, List<ColumnInfo> targetColumns) {
        if (super.isApplicable(dataset, sourceColumns, targetColumns)) {
            if (sourceColumns.get(0).getColumn().getType().equals(Column.columnType.Date)) {
                return true;
            }
        }
        return false;
    }

    public int getNumOfBins() {
        return 2;
    }
}

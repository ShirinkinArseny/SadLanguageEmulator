package ProcessorModeling.GUI;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class RegistersTable implements TableModel {

    private int[] values=new int[0];

    public void pushValue(int[] data) {
        values=data;
    }

    @Override
    public int getRowCount() {
        return values.length;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0: return "Register";
            case 1: return "Value";
        }
        return "NULL~";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex==0?Integer.class:String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex==0)
        return rowIndex;
        return values[rowIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}

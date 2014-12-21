package ProcessorModeling.GUI;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class MyTabel implements TableModel {

    private int[] values=new int[0];

    public void setSize(int size) {
        values=new int[size];
    }

    public void pushValue(int index, int value) {
        values[index]=value;
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
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex==0)
        return "Register "+rowIndex;
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

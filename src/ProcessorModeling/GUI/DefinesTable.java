package ProcessorModeling.GUI;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class DefinesTable implements TableModel {

    private ArrayList<String> from=new ArrayList<>();
    private ArrayList<String> to=new ArrayList<>();

    public void pushValues(ArrayList<String> from, ArrayList<String> to) {
        this.from=from;
        this.to=to;
    }

    @Override
    public int getRowCount() {
        return from.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0: return "Defined";
            case 1: return "Replacement";
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
        return columnIndex==0?from.get(rowIndex):to.get(rowIndex);
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

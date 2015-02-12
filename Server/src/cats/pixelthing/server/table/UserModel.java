package cats.pixelthing.server.table;

import cats.pixelthing.core.connection.Connection;
import cats.pixelthing.server.user.ServerUser;
import java.util.LinkedList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class UserModel extends AbstractTableModel {

    public enum Column{
        CONNECTED, NAME, ACCESS, X, Y, PIXELS, MUTED, BANNED;

        public String toString(){
            final String s = super.toString();
            if(s.length() == 1)
                return s;
            return s.charAt(0) + s.substring(1).toLowerCase();
        }
    }

    private static final Column[] COLUMNS = Column.values();

    private final List<ServerUser> model;

    protected UserModel(){
        model = new LinkedList<>();
    }

    public ServerUser get(final int i){
        return model.get(i);
    }

    public int indexOf(final ServerUser user){
        return model.indexOf(user);
    }

    public void add(final ServerUser user){
        model.add(user);
        fireTableRowsInserted(model.size()-1, model.size()-1);
    }

    public void remove(final ServerUser user){
        final int i = indexOf(user);
        if(i != -1){
            model.remove(i);
            fireTableRowsDeleted(i, i);
        }
    }

    public int getRowCount() {
        return model.size();
    }

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public void setValueAt(final Object o, final ServerUser user, final Column column){
        setValueAt(o, indexOf(user), column.ordinal());
    }

    public void setValueAt(final Object o, final int r, final int c){
        if(r == -1)
            return;
        final ServerUser user = get(r);
        switch(c){
            case 0:
                user.connection = (Connection) o;
                break;
            case 1:
                user.name = (String) o;
                break;
            case 2:
                user.access = (ServerUser.Access) o;
                break;
            case 3:
                user.x = (Integer) o;
                break;
            case 4:
                user.y = (Integer) o;
                break;
            case 5:
                user.pixelCount = (Long) o;
                break;
            case 6:
                user.muted = (Boolean) o;
                break;
            case 7:
                user.banned = (Boolean) o;
                break;
        }
        fireTableCellUpdated(r, c);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        final ServerUser user = get(rowIndex);
        switch(columnIndex){
            case 0:
                return Boolean.toString(user.connection != null);
            case 1:
                return user.name;
            case 2:
                return user.access.title;
            case 3:
                return Integer.toString(user.x);
            case 4:
                return Integer.toString(user.y);
            case 5:
                return String.format("%,d", user.pixelCount);
            case 6:
                return Boolean.toString(user.muted);
            case 7:
                return Boolean.toString(user.banned);
            default:
                return null;
        }
    }

    public String getColumnName(final int i){
        return COLUMNS[i].toString();
    }
}

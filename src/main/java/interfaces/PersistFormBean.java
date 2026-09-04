package interfaces;

import net.beans.www.ContactFormBean;

public interface PersistFormBean {
    public void openConnection();
    public void writeToDisk(Object obj);
    public Object[] readRecords();
}

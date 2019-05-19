package Record;

import java.util.Date;

public interface IRecord {
    public void setRecordName(String recordName);

    public void setRecordText(String recordText);

    public String getRecordName();

    public String getRecordText();

    public Date getPostDate();

    public String getStringForPrint();
}

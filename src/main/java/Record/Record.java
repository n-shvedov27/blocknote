package Record;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Record implements IRecord {
    private String recordName;
    private String recordText;
    private Date postDate;

    public Record(String recordName, String recordText) {
        if (recordName.equals("")) {
            throw new IllegalArgumentException("Empty name");
        }
        if (recordText.equals("")) {
            throw new IllegalArgumentException("Empty text");
        }
        this.recordName = recordName;
        this.recordText = recordText;
        this.postDate = new Date();
    }

    public Record(String recordName, String recordText, Date postDate) {
        this.recordText = recordText;
        this.recordName = recordName;
        this.postDate = postDate;
    }

    @Override
    public void setRecordName(String recordName) {
        if (recordName.equals("")) {
            throw new IllegalArgumentException("Empty name");
        }
        this.recordName = recordName;
    }

    @Override
    public void setRecordText(String recordText) {
        if (recordText.equals("")) {
            throw new IllegalArgumentException("Empty text");
        }
        this.recordText = recordText;
    }

    @Override
    public String getRecordName() {
        return recordName;
    }

    @Override
    public String getRecordText() {
        return recordText;
    }

    @Override
    public Date getPostDate() {
        return postDate;
    }

    @Override
    public String getStringForPrint() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return this.getRecordName() + "\n" + dt.format(this.getPostDate()) + "\n" + getRecordText();
    }
}

package Record;

import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTest {

    @Test
    public void setRecordName() {
        Record record = new Record("name", "text");
        record.setRecordName("newName");
        assertEquals(record.getRecordName(), "newName");
    }

    @Test
    public void setRecordText() {
        Record record = new Record("name", "text");
        record.setRecordText("newText");
        assertEquals(record.getRecordText(), "newText");
    }

    @Test
    public void getRecordName() {
        Record record = new Record("name", "text");
        assertEquals(record.getRecordName(), "name");
    }

    @Test
    public void getRecordText() {
        Record record = new Record("name", "text");
        assertEquals(record.getRecordText(), "text");
    }

    @Test
    public void getPostDate() {
        Record record = new Record("name", "text");
        assertNotEquals(record.getPostDate(), null);
    }

}
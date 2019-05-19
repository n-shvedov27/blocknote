import DbHandler.DbHandler;
import Notebook.Notebook;
import Record.Record;
import Record.IRecord;
import Record.RecordNotFoundException;
import org.apache.commons.cli.*;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;


public class Main {
    public static void loadCommands(HashMap<String, Runnable> commands, Notebook notebook) {
        Scanner scanner = new Scanner(System.in);

        commands.put("del", () -> {
            System.out.println("Enter id");
            int id = Integer.parseInt(scanner.nextLine());
            try {
                notebook.delRecord(id);
                System.out.println("Done");

            } catch (RecordNotFoundException ex) {
                System.out.println("Record Not found");
            }

        });
        commands.put("add", () -> {
            System.out.println("Enter name record");
            String name = scanner.nextLine();
            System.out.println("Enter text");
            String text = scanner.nextLine();
            notebook.addRecord(name, text);
            System.out.println("Done");


        });
        commands.put("get", () -> {
            System.out.println("Enter id");

            int id = Integer.parseInt(scanner.nextLine());
            try {
                IRecord record = notebook.getRecord(id);
                System.out.println(record.getStringForPrint() + "\n");
                System.out.println("Done");

            } catch (RecordNotFoundException ex) {
                System.out.println("Record Not found");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        });
        commands.put("update", () -> {
            System.out.println("Enter id");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter new name record");
            String name = scanner.nextLine();
            System.out.println("Enter new text");
            String text = scanner.nextLine();
            try {
                notebook.updateRecord(id, name, text);
                System.out.println("Done");

            } catch (RecordNotFoundException ex) {
                System.out.println("Record not found");
            }
        });
        commands.put("getAll", () -> {
            System.out.println("Enter field for filter or -");
            System.out.println("Contained fields:");
            for (Field field : Record.class.getDeclaredFields()) {
                System.out.println("--->" + field.getName());
            }
            String fieldName = scanner.nextLine();
            LinkedList<IRecord> records;
            if (fieldName.equals("-")) {
                records = notebook.getAllRecords();
                if (records.size() == 0) {
                    System.out.println("Record not exists yet");
                }
                for (IRecord record : records) {
                    System.out.println(record.getStringForPrint());
                    System.out.println();
                }
            } else {
                System.out.println("Enter value for field");
                Object value = scanner.nextLine();
                try {
                    records = notebook.getAllRecords(fieldName, value);
                    for (IRecord record : records) {
                        System.out.println(record.getStringForPrint());
                        System.out.println();
                    }
                    if (records.size() == 0) {
                        System.out.println("Record not exists yet");
                    }
                } catch (IllegalAccessException ex) {
                    System.out.println("Field not found");
                    System.out.println();
                }
            }

        });
    }

    public static void main(String[] args) {

        DbHandler dbHandler = DbHandler.getInstance();
        Notebook notebook = new Notebook(dbHandler);

        HashMap<String, Runnable> commands = new HashMap<>();

        loadCommands(commands, notebook);

        System.out.println("Enter command: ");
        for (String command : commands.keySet()) {
            System.out.println(command);
        }
        System.out.println("For exit write:\n/!exit");
        System.out.println();
        while (true) {
            Scanner scanner = new Scanner(System.in);

            String command = scanner.nextLine();

            if (command.equals("/!exit"))
                break;

            if (!commands.keySet().contains(command)) {
                System.out.println("Enter right command");
                continue;
            }
            commands.get(command).run();

            System.out.println("Enter new command");
        }
        notebook.closeHandler();
    }
}

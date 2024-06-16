package src;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import src.FileWork.ReturnValues;

class Utils {
    public static ArrayList<String> split(String str) {
        return split(str, ' ');
    }
    public static ArrayList<String> split(String str, char sep) {
        ArrayList<String> out = new ArrayList<>();
        String tmp_str = new String();
        for (char ch : str.toCharArray()) {
            if (ch == sep) {
                out.add(tmp_str);
                tmp_str = "";
                continue;
            }

            tmp_str += ch;
        }
        if (!tmp_str.equals(""))
            out.add(tmp_str);

        return out;
    }
}

class FileWork {
    private final Reader file_read = new Reader();
    private String filename = null;
    private ArrayList<String> lines = new ArrayList<>();
    private final Scanner IN = new Scanner(System.in);
    private int index = 0;
    private boolean SAVED = true;
    public enum ReturnValues {
        Allright,
        Error,
        Exit
    }
    public void SaveData(String filename) {
        boolean empty_filename = false;
        if (filename == null) {
            empty_filename = true;
            System.out.print("Enter filename for save: ");
            filename = this.IN.nextLine();
            this.filename = filename;
        }
        try (FileWriter filewrite = new FileWriter(filename)) {
            for (int i = 0; i < this.lines.size(); i++) {
                String line = this.lines.get(i);
                if (i == this.lines.size()-1)
                    filewrite.write(line);
                else
                    filewrite.write(line + "\n");
            }
            filewrite.close();
            if (empty_filename) {
                try {
                    this.file_read.open(this.filename);
                } catch (ReadException error) {
                    System.out.println("[!] " + error);
                }
            }
            this.SAVED = true;
        } catch (IOException error) {
            System.out.println(error);
        }
    }

    public void WriteLine(String line) {
        this.lines.set(this.index, line);
        if (this.index == this.lines.size()-1)
            this.lines.add("");
        this.index++;
    }

    public ReturnValues HandlerCommand(String command) {
        ArrayList<String> underscore_split = Utils.split(command, '_');
        if (command.equals(">help<")) {
            System.out.println(">print_all< - show all text");
            System.out.println(">clear< - clear screen");
            System.out.println(">quit< | >exit< - exit from program");
            System.out.println(">save< - save text to file");
            System.out.println(">save_exit< - save text to file and exit from programm");
            System.out.println(">save_as< - save text to other file");
            System.out.println(">gcl< - print current line");
            System.out.println(">rcl< - remove current line ( don't clear line )");
            System.out.println(">set_empty< - remove all lines");
            System.out.println(">nextl< - move cursor to next line");
            System.out.println(">to_INT< - move cursor to line at number INT ( starts at 0, ends at count of lines - 1 )");
            System.out.println(">help< - show help text");
        } else if (command.equals(">print_all<")) {
                System.out.println("--------");
                System.out.println("TEXT");
                System.out.println("--------");
                for (int i = 0; i < this.lines.size(); i++) {
                    String line = this.lines.get(i);
                    if (i == this.index)
                        System.out.print("/>");
                    else
                        System.out.print(i + ") ");
                    System.out.println(line);
                }
                System.out.println("--------");
        } else if (command.equals(">clear<")) {
                System.out.print("\033[H\033[2J");
                System.out.flush();
        } else if (command.equals(">quit<") || command.equals(">exit<")) {
                if (SAVED)
                    return ReturnValues.Exit;
                System.out.print("Are you sure?(y/n): ");
                String choice = this.IN.nextLine();
                if (choice.equals("y"))
                    return ReturnValues.Exit;
        } else if (command.equals(">save<")) {
            SaveData(this.filename);
        } else if (command.equals(">save_exit<")) {
            SaveData(this.filename);
            return ReturnValues.Exit;
        } else if (command.equals(">save_as<")) {
            SaveData(null);
        } else if (command.equals(">gcl<")) { // get_current_line;
            System.out.println("Line: " + this.lines.get(this.index));
        } else if (command.equals(">rcl<")) { // remove_current_line;
            this.lines.remove(this.index);
            if (this.lines.isEmpty()) {
                this.lines.add("");
                this.index = 0;
            } else if (this.index > this.lines.size()-1)
                this.index = this.lines.size()-1;
        } else if (command.equals(">set_empty<")) {
            System.out.print("Are you sure?(y/n): ");
            String choice = IN.nextLine();
            if (choice.equals("y")) {
                this.lines.clear();
                this.lines.add("");
                this.index = 0;
            }
        } else if (command.equals(">nextl<")) {
            this.lines.add("");
            this.index++;
        } else if (underscore_split.size() == 2) {
            switch (underscore_split.get(0)) {
                case ">to" -> {
                    String arg = underscore_split.get(1);
                    if (!(arg.charAt(arg.length()-1) == '<')) {
                        WriteLine(command);
                        break;
                    }
                    arg = arg.replace("<", "");
                    int new_index = Integer.parseInt(arg);
                    if (new_index > this.lines.size()-1 || new_index < 0) {
                        System.out.println("[!] Out of range.");
                        return ReturnValues.Error;
                    }
                    this.index = new_index;
                    System.out.println("Line: " + this.lines.get(this.index));
                    break;
                }
            }
        }
        else {
            WriteLine(command);
            if (this.SAVED)
                this.SAVED = false;
        }
        return ReturnValues.Allright;
    }

    public void Run(String filename) {
        this.filename = filename;
        try {
            if (filename != null) {
                this.file_read.open(this.filename);
                this.lines = this.file_read.read();
            } else
                this.lines.add("");
            this.index = 0;
            System.out.println("Line: " + this.lines.get(this.index));
            boolean exit = false;
            while (!exit) {
                System.out.print(":");
                switch (HandlerCommand(this.IN.nextLine())) {
                    case Allright, Error -> {
                    }
                    default -> exit = true;
                }
            }
        } catch (ReadException | ArrayIndexOutOfBoundsException error) {
            System.out.println(error);
        } finally {
            try {
                this.IN.close();
                this.file_read.close();
            } catch (ReadException error) {
                System.out.println(error);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) throws ReadException {
        FileWork run = new FileWork();
        try {
            if (args.length != 0)
                run.Run(args[0]);
            else
                run.Run(null);
        } catch (Exception error) {
            System.out.println(error);
        }
    }
}
package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reader {
    private String filename;
    private Scanner reader = new Scanner(System.in);
    public void open(String filename) throws ReadException {
        try {
            this.filename = filename;
            this.reader = new Scanner(new File(this.filename));
        } catch(FileNotFoundException error) {
            throw new ReadException(error.getMessage());
        }
    }

    public void reset() throws ReadException { // Recreate object 'Scanner reader';
        this.reader.close();
        try {
            this.reader = new Scanner(new File(this.filename));
        } catch (FileNotFoundException error) {
            throw new ReadException(error.getMessage());
        }
    }

    public void setpointer(int index) throws ReadException { // Set index of this line that will be last;
        this.reader.close();
        try {
            this.reader = new Scanner(new File(this.filename));
            for (int i = 0; i < index; i++) {
                this.reader.nextLine();
            }
        } catch (FileNotFoundException error) {
            throw new ReadException(error.getMessage());
        }
    }

    public String readline(String filename) throws ReadException { // Read ONLY FIRST line from other file;
        String out = null;
        Scanner new_read = new Scanner(System.in);
        try {
            new_read = new Scanner(new File(filename));
            if (new_read.hasNext())
                return new_read.nextLine();
        } catch (FileNotFoundException error) {
            throw new ReadException(error.getMessage());
        } finally {
            new_read.close();
        }

        return out;
    }

    public String readline() {
        String out = null;
        if (this.reader.hasNext())
            out = this.reader.nextLine();

        return out;
    }

    public ArrayList<String> read(String filename) throws ReadException { // Read other file;
        Reader new_read = new Reader();
        try {
            new_read.open(filename);
            return new_read.read();
        } catch (ReadException error) {
            throw new ReadException(error.getMessage());
        } finally {
            new_read.close();
        }
    }

    public ArrayList<String> read() throws ReadException {
        ArrayList<String> out = new ArrayList<>();
        Reader readfile = new Reader();
        readfile.open(this.filename);
        while (true) {
            String line = readfile.readline();
            if (line == null)
                break;
            out.add(line);
        }
        return out;
    }

    public void close() throws ReadException {
        try {
            this.reader.close();
        } catch(NullPointerException error) {
            throw new ReadException(error.getMessage());
        }
    } 
}
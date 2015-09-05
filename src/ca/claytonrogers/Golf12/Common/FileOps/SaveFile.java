package ca.claytonrogers.Golf12.Common.FileOps;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A configuration class which allows configuration setting to be saved to a file. Can also be used
 * to save other information to a file.
 *
 * Created by clayton on 2015-08-29.
 */
public class SaveFile {

    File file;

    private Map<String, String> entries = new HashMap<>(15);

    public SaveFile(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename of a config cannot be null");
        }

        file = new File(filename);
        if (!Files.isReadable(file.toPath()) || !Files.isWritable(file.toPath())) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.close();
            } catch (IOException e) {
                throw new IllegalStateException("There was a problem creating the file: " + file.getAbsolutePath());
            }
        }
        internalRead();
    }

    public void write (String key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Cannot write null keys/value");
        }
        entries.put(key, value);

        internalWrite();
    }

    private void internalWrite() {
        FileWriter fileWriter;
        BufferedWriter out;
        try {
            fileWriter = new FileWriter(file);
            out = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            throw new IllegalStateException("IO error on file: " + file.getAbsolutePath());
        }

        Set<Map.Entry<String, String>> setEntries = entries.entrySet();
        for (Map.Entry<String, String> entry : setEntries) {
            String line = entry.getKey() + '\t' + entry.getValue() + '\n';
            try {
                out.write(line);
            } catch (IOException e) {
                throw new IllegalStateException("There was an issue writing to file: " + e);
            }
        }

        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new IllegalStateException("Problem closing the file after writing: " + file.getAbsolutePath());
        }
    }

    private void internalRead() {
        FileReader fileReader;
        BufferedReader in;
        try {
            fileReader = new FileReader(file);
            in = new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("File could not be found: " + file.getAbsolutePath());
        }

        entries.clear();

        String line;
        int lineNumber = 0;
        while (true) {
            lineNumber++;
            try {
                line = in.readLine();
            } catch (IOException e) {
                break;
            }
            if (line == null) {
                break;
            }

            String[] tokens = line.split("\\t+");
            if (tokens.length != 2) {
                System.out.println("Incorrect number of tokens on line: " + lineNumber);
                System.out.println("Incorrect number of tokens: " + line);
            } else {
                entries.put(tokens[0], tokens[1]);
            }
        }

        try {
            fileReader.close();
        } catch (IOException e) {
            throw new IllegalStateException("Problem closing the file after reading: " + file.getAbsolutePath());
        }
    }

    public int getIntValue (String key, int defaultValue) {
        String value = entries.get(key);
        if (value == null) {
            return defaultValue;
        } else{
            return Integer.parseInt(value);
        }
    }

    public String getStringValue (String key, String defaultValue) {
        String value = entries.get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }
}
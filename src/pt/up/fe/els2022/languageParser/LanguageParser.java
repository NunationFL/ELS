package pt.up.fe.els2022.languageParser;

import pt.up.fe.els2022.languageParser.commands.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LanguageParser {
    FileReader languageFile;
    List<Command> commands = new ArrayList<>();
    public LanguageParser(String filePath) throws FileNotFoundException {
        languageFile = new FileReader(filePath);
    }


    public List<Command> parse() throws IOException {
        BufferedReader reader;
        reader = new BufferedReader(languageFile);
        String line = reader.readLine();
        Command currentCommand = null;

        int lineCounter = 0;

        while (line != null){
            lineCounter++;

            if(line.trim().length() == 0) {
                line = reader.readLine();
                continue;
            }

            try {
                if (line.startsWith("End")) {
                    if(currentCommand == null) {
                        throw new Error("END used out of place,  ");
                    }
                    currentCommand.close();
                    commands.add(currentCommand);
                    currentCommand = null;
                } else if (line.startsWith("Read")) {
                    currentCommand = new Read(line);
                } else if (line.startsWith("Sort")) {
                    currentCommand = new Sort(line);
                    currentCommand.close();
                    commands.add(currentCommand);
                    currentCommand = null;
                }else if (line.startsWith("Merge")) {
                    currentCommand = new Merge(line);
                    currentCommand.close();
                    commands.add(currentCommand);
                    currentCommand = null;
                }else if (line.startsWith("RemoveColumn")) {
                    currentCommand = new RemoveColumn(line);
                    currentCommand.close();
                    commands.add(currentCommand);
                    currentCommand = null;
                }else if (line.startsWith("AddColumn")) {
                    currentCommand = new AddColumn(line);
                    currentCommand.close();
                    commands.add(currentCommand);
                    currentCommand = null;
                }else if (line.startsWith("Write")) {
                    currentCommand = new Write(line);
                    currentCommand.close();
                    commands.add(currentCommand);
                    currentCommand = null;
                } else {
                    if (currentCommand != null) {
                        currentCommand.addLine(line);
                    } else {
                        throw new Error("Command not found around ");
                    }
                }
            }catch (Error e) {
                throw new Error(e.getMessage() +"LINE: " + lineCounter);
            }
            line = reader.readLine();
        }

        reader.close();
        return commands;
    }

    void println(){
        System.out.println("--------------------------------");
        for (var a:commands) {
            a.println();
        }
        System.out.println("--------------------------------");
    }
}


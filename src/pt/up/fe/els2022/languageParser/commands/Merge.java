package pt.up.fe.els2022.languageParser.commands;

import pt.up.fe.els2022.Table;
import pt.up.fe.els2022.TableOperations;
import pt.up.fe.els2022.languageParser.Command;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Merge implements Command {
    List<String> fileIds = new ArrayList<>();
    String newFileId;

    String aggregate;

    String destinyColumn;

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
    }

    public void addFileId(String fileId) {
        this.fileIds.add(fileId);
    }

    public void setNewFileId(String newFileId) {
        this.newFileId = newFileId;
    }

    public void setAggregate(String aggregate) {
        this.aggregate = aggregate;
    }

    public void setDestinyColumn(String destinyColumn) {
        this.destinyColumn = destinyColumn;
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public String getNewFileId() {
        return newFileId;
    }

    public String getAggregate() {
        return aggregate;
    }

    public String getDestinyColumn() {
        return destinyColumn;
    }

    public Merge() {
    }

    @Override
    public void addLine(String line) throws Error{
    }

    @Override
    public void close() {
    }

    @Override
    public void println() {
        String out = "Merge ";

        for(var f: fileIds) {
           out+=f+",";
        }
        out = out.substring(0,out.length()-1);


        if(aggregate != null){
            out += " with "+aggregate + " on "+destinyColumn;
        }
        if(newFileId != null){
            out += " as "+newFileId;
        }
        System.out.println(out);
    }

    @Override
    public void execute(HashMap<String, Table> symbolTable) {
        Table newTable = new Table();

        for(String ID : fileIds){
            Table tableCopy = symbolTable.get(ID).copy();

            if(aggregate != null && aggregate.equals("Name")){
                TableOperations.addColumn(tableCopy,destinyColumn,tableCopy.getOrigin());
            }

            newTable = TableOperations.mergeTables(newTable,tableCopy);
        }

        if(newFileId != null)
            symbolTable.put(newFileId ,newTable);
        else
            symbolTable.put(fileIds.get(0) ,newTable);
    }
}
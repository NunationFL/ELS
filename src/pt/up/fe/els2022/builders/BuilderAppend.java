package pt.up.fe.els2022.builders;

import pt.up.fe.els2022.dslParser.CMDHolder;
import pt.up.fe.els2022.dslParser.Command;
import pt.up.fe.els2022.dslParser.commands.Append;
import pt.up.fe.els2022.dslParser.commands.Write;

public class BuilderAppend implements InterfaceBuilder{
    Append append;
    CMDHolder builder;

    public BuilderAppend(CMDHolder builder) {
        this.append = new Append();
        this.builder = builder;
    }

    public BuilderAppend setFileId(String fileId) {
        append.setFileId(fileId);
        return this;
    }
    public BuilderAppend setSuffix(String suffix) {
        append.setSuffix(suffix);
        return this;
    }
    public BuilderAppend setNewFileId(String newFileId) {
        append.setNewFileId(newFileId);
        return this;
    }


    public CMDHolder close(){
        return builder;
    }

    @Override
    public Command build() {
        return append;
    }
}

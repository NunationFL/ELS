package pt.up.fe.els2022.builders;

import pt.up.fe.els2022.dslParser.CommandHolder;
import pt.up.fe.els2022.dslParser.Command;
import pt.up.fe.els2022.dslParser.commands.RemoveColumn;

public class BuilderRemoveColumn implements InterfaceBuilder{
    RemoveColumn removeColumn;
    CommandHolder builder;

    public BuilderRemoveColumn(CommandHolder builder) {
        this.removeColumn = new RemoveColumn();
        this.builder = builder;
    }

    public BuilderRemoveColumn setFileId(String id){
        removeColumn.setFileId(id);
        return this;
    }
    public BuilderRemoveColumn setColumn(String column){
        removeColumn.setColumn(column);
        return this;
    }
    public BuilderRemoveColumn setNewFileId(String newFileId){
        removeColumn.setNewFileId(newFileId);
        return this;
    }

    public CommandHolder close(){
        return builder;
    }


    @Override
    public Command build() {
        return removeColumn;
    }
}

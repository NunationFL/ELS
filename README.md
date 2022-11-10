# ELS Project

For this project, you need to [install Gradle](https://gradle.org/install/)

## Project setup

Copy your source files to the ``src`` folder, and your JUnit test files to the ``test`` folder.

## Compile and Running

To compile and install the program, run ``gradle installDist``. This will compile your classes and create a launcher script in the folder ``build/install/els2022-g2/bin``. For convenience, there are two script files, one for Windows (``els2022-g2.bat``) and another for Linux (``els2022-g2``), in the root of the repository, that call these scripts.

After compilation, tests will be automatically executed, if any test fails, the build stops. If you want to ignore the tests and build the program even if some tests fail, execute Gradle with flags "-x test".

When creating a Java executable, it is necessary to specify which class that contains a ``main()`` method should be entry point of the application. This can be configured in the Gradle script with the property ``mainClassName``, which by default has the value ``pt.up.fe.els2022.Main``.

## Test

To test the program, run ``gradle test``. This will execute the build, and run the JUnit tests in the ``test`` folder. If you want to see output printed during the tests, use the flag ``-i`` (i.e., ``gradle test -i``).
You can also see a test report by opening ``build/reports/tests/test/index.html``.

## Assignment 1


### Reading from an XML file
```
Read files/vitis-report_1.xml as f1 
    Parent Resources
    Col LUT => LUTs
    Col FF => FFs
    Col DSP48E => DSP48Es
    Col BRAM_18K => BRAM_18Ks
End
```
On the first line we specify what file we want to read from and, after keyword ``as``, give the resulting table an **identifier which is mandatory to use when operating on tables**.

If multiple files are read in the same way, our DSL allows for an easy configuration like: 

- ``Read files/vitis-report_1.xml,files/vitis-report_2.xml,files/vitis-report_3.xml as f1,f2,f3``

Inside the Read block we must first specify the parent from where we will read our values, in case there are multiple tags with the same name.
Then we indicate what tag we will extract the value and right after ``=>`` we can change the name of the resulting column on the table.
We decided that renaming columns right at table creation was more intuitive and user-friendly this way, opposed to having to rename them one by one after reading.

### Adding and removing columns
```
AddColumn f1 TESTCOL TESTE
AddColumn f1 TESTCOL TESTE as f4
RemoveColumn f1 LUTs
RemoveColumn f1 LUTs as f4
```

When adding, we first indicate the target table, then the name of the new column and the default value of that column for all entries on that table. 

When removing, we simply indicate the target table and then what column to remove. 

Similar to some other commands, these two **may** have ``as <identifier>`` at the end. When this is present the original table will remain unaltered and the results of the operations will be reflected on a new table will be identified as ``<identifier>``.

### Sorting a table
```
Sort f3 LUTs desc
Sort f3 LUTs desc as f4
```
Here we first indicate the target table, the column from which we take the values to sort the entries and the order (``asc``or ``desc``).
Again, we **may** have ``as <identifier>`` at the end so that the changes are reflected on a new table and leave the original intact.

**Important** - for this assignment, values read from XML's are all strings so sorting is done according to the unicode value of each character. For example, 432 < 54 is true because char '4' has smaller unicode value than '5'. 

### Merging
```
Merge f1,f2
Merge f1,f2,f3 as f4
Merge f1,f2,f3 with Name on File
Merge f1,f2,f3 with Name on File as f4
```
The merge command can be used in several ways. You can merge more than two tables at a time. If ``as <indentifier>`` is specified the combination of all tables will result in a new table, otherwise it will overwrite the first table in the command (not mandatory).

There is also a ``with <atribute> on <column_name>`` part where you can create a new column for all tables in this merge command preserving some attribute of the table on a column.
For example, in this assignment we want to know the file from where a table/entry originated. The filepath is an attribute of the table originated from the Read command and when merging 2 tables together, instead of adding the column manually to each table, we can use this Merge command to more easily add that column to all tables involved.
We chose this approach because in the future more metadata may be needed from the original file and this gives the language a lot of versatility.

### Writing to a file
```
SetOutput f4
    File
    LUTs
    FFs
    DSP48Es
    BRAM_18Ks
End

Write CSV f4 to test/pt/up/fe/els2022/outFiles/outTestFull.csv
```
To create a .csv from a table we use the Write command where we first specific the type of file (only 'CSV' supported for now), then the target table and finally, after ``to``, the output path.

Before using Write, we can use SetOutput to filter and order the columns for the output file. If SetOutput is done, only columns listed in it will be printed following the order they are presented in the command. Otherwise, everything will be printed, following to order in the Read command.


## Overview of the system

![Imgur](https://imgur.com/eXMSaSi.png)

## Design decisions

Most decisions were made due to the limitations we found on the language at the time.  
We wanted the DSL to feel powerful but yet easy to use.  

Our syntax was chosen to allow for a easier parse of the language. The Beginning of each line starts with a Keyword, that allows us to easily identify which command we are parsing.  
Multiline Commands (SetOutput and Read) follow the same principle but we further include the End token so we can easily decide if the commands bellong inside or not those multile Commands.  

Our Implementation works around the Idea of commands. The language parser decides which command to create, depending on the keyword, and delegates the parsing problem to the command itself.  
This modularity allows for an easy way to create and add more commands as we go.  
The app stores a list of commands, by the order they appear on the Config file, and executes them by order.  
This works similar to a Visitor in the way that each command knows which function of the table to call and execute, depending on the said command. 
The Commands also have access to a global symbolTable, where the tables are stored and it is able to assign reassign tables to identifiers(like variables).

One particular part of the language is the ability to store the result of each command on a new variable or on the same one, allowing for more flexibility.

## Assignment 2

In this assignment we mostly refactored our code and introduced features which did not require many new commands. As such, the new commands are almost all for the reading of TXT files, and one for joining tables without creating new lines

### JOIN
```
Join xml,json as xml
```
We stay consistent in or syntax. This command can join two tables, creating another table. **Warning**: Both tables need to have the same number of lines

To do so, the user just needs to list the two tables he whishes to join

The user can use the keywork as to overwrite one of the tables or just create a new one (just like in commands created in the last assignment).

### READ (JSON)
This read is not an addition. It is instead yet another use case. The user can use the same READ command for XML and JSON. TO use for JSON we do:
```
Read files/decision_tree.json as json
    Parent ROOT params
End
```
In this command we list the "parents" on the JSON file from which we want to read the primitives. In this example, we want to read all primitives that are under the attribute params (which is a composite attribute) and under the root of the file (since root doesn't have a name we choose the keyword "ROOT").

As we can see, it is almost the same as the XML use case. 

## Overview of the system
![Imgur](https://i.imgur.com/zmcq3R2.png)

## Design decisions
In our project specifically, because we started out with a preliminary DSL, a lot of the effourt spent in this assignment was refactoring our project to implement the builder pattern and fluent API which both introduced a whole new level of complexy to our code.

We decided to use the same READ command for XML and JSON because they are so similar in how they work. In the same line of thought, we chose to create a new READ specific for TXT files because they lack any structure and so their interpreting is interely different and requires new functions we didn't have before.

Besides that we also added the possibility to read folders of files. The behaviour will be the same for all files. If a type of file is specificied, the program will only read and create tables from that filetype. The identifier used on the READ will map to all tables originated from the command so when an operation is used it will be used on all of them besides JOIN and MERGE which will result in only 1 (aggregate functions). 

This means that now identifiers map to a list of tables instead of just one. When only one file is read at a time, it will be a list conatining only one table but it was the tradeoff of implementing the folder reading feature.

Now, the tables also have a default output order. Since, internally, Tables are Hashmaps, output order was sort of random before. We changed that so now the order, if not specified will be same as the order they are read from the file.

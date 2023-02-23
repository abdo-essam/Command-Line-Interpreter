import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        File file = new File("D:\\");// start in C directory
        parser.initializeCommands();

        while (true) {
            System.out.print(file.getAbsolutePath() + " ");
            Scanner input = new Scanner(System.in);
            String command;
            command = input.nextLine();


            String[] arrayOfCommands = command.split("\\|");
            for (int i = 0; i < arrayOfCommands.length; i++) {
                command = arrayOfCommands[i];
                if (parser.parse(command)) {

                    if (command.indexOf('>') != -1) { // > found
                        if (command.indexOf('>') != command.lastIndexOf('>') && command.lastIndexOf('>') - command.indexOf('>') != 1) { // not be > and >>
                            System.out.println("Invalid usage of operators (>) and (>>)");
                            continue;
                        }
                        File arr[] = file.listFiles();
                        String fileName = command.substring(command.lastIndexOf(' ') + 1);
                        String checker = Terminal.search(arr, fileName);
                        if (checker.equals("Not found")) {
                            System.out.println("No such file named: " + fileName);
                            continue;
                        }
                        File f2 = new File(checker);
                        if (parser.getCmd().equalsIgnoreCase("pwd")) {
                            if (command.indexOf('>') == command.lastIndexOf('>')) // only one then truncate
                                try {
                                    Files.write(f2.toPath(), file.getAbsolutePath().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            else
                                try {
                                    Files.write(f2.toPath(), file.getAbsolutePath().getBytes(), StandardOpenOption.APPEND);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        } else if (parser.getCmd().equalsIgnoreCase("ls")) {
                            if (command.indexOf('>') == command.lastIndexOf('>'))
                                try {
                                    Files.write(f2.toPath(), Arrays.toString(arr).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            else
                                try {
                                    Files.write(f2.toPath(), Arrays.toString(arr).getBytes(), StandardOpenOption.APPEND);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        } else if (parser.getCmd().equalsIgnoreCase("cat")) {
                            String file1Name = command.substring(command.indexOf(' ') + 1, command.indexOf('>') - 1);
                            String checker1 = Terminal.search(arr, file1Name);
                            if (checker1.equals("Not found")) {
                                System.out.println("No such file named: " + file1Name);
                                continue;
                            }
                            File f1 = new File(checker1);
                            StringBuilder file1Content = new StringBuilder();
                            try {
                                Scanner input1 = new Scanner(f1);
                                while (input1.hasNextLine()) {
                                    file1Content.append(input1.nextLine());
                                    file1Content.append("\n");
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            if (command.indexOf('>') == command.lastIndexOf('>'))
                                try {
                                    Files.write(f2.toPath(), file1Content.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            else
                                try {
                                    Files.write(f2.toPath(), file1Content.toString().getBytes(), StandardOpenOption.APPEND);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }
                    } else if (parser.getCmd().equalsIgnoreCase("cp")) {
                        File array[] = file.listFiles();
                        try {
                            Terminal.cp(array, parser.getArgs());
                            parser.setArgs(null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (parser.getCmd().equalsIgnoreCase("mv")) {
                        File array[] = file.listFiles();
                        try {
                            Terminal.mv(array, parser.getArgs());
                            parser.setArgs(null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (parser.getCmd().equalsIgnoreCase("rm")) {
                        if (!Terminal.rm(parser.getArgs(), file.getAbsolutePath())) {
                            System.out.println("Wrong Parameter!");
                        }
                        parser.setArgs(null);
                    } else if (parser.getCmd().equalsIgnoreCase("rmdir")) {

                        if (!Terminal.rmDir(parser.getArgs(), file.getAbsolutePath())) {
                            System.out.println("Wrong Parameter!");
                        }
                        parser.setArgs(null);
                    } else if (parser.getCmd().equalsIgnoreCase("exit")) {
                        System.out.print("Program terminated on ");
                        Terminal.date();
                        return;
                    } else if (parser.getCmd().equalsIgnoreCase("help")) {
                        Terminal.help();
                    } else if (parser.getCmd().equalsIgnoreCase("args")) {
                        Terminal.args();
                    } else if (parser.getCmd().equalsIgnoreCase("date")) {
                        Terminal.date();
                    } else if (parser.getCmd().equalsIgnoreCase("mkdir")) {
                        if (!Terminal.mkdir(parser.cmd, file.getAbsolutePath())) {
                            System.out.println("already exist! or wrong path!");
                        }
                    } else if (parser.getCmd().equalsIgnoreCase("cat")) {
                        File arr[] = file.listFiles();
                        if (parser.getArgs() == null) {
                            System.out.println("Not enough arguments for cat");
                        } else if (parser.getArgs().length == 1) {
                            Terminal.cat(arr, parser.getArgs()[0]);
                        } else if (parser.getArgs().length == 2) {
                            String file1Name = parser.getArgs()[0];
                            String file2Name = parser.getArgs()[1];
                            Terminal.cat(arr, Terminal.concatenate(arr, file1Name, file2Name));
                        } else {
                            System.out.println("two arguments only!");
                        }
                        parser.setArgs(null);
                    } else if (parser.getCmd().equalsIgnoreCase(("ls"))) {
                        if (parser.getArgs() != null) {
                            System.out.println("ls don't have arguments");
                        } else {
                            Terminal.listAllFiles(file.getAbsolutePath());
                        }
                    } else if (parser.getCmd().equalsIgnoreCase("pwd")) {
                        if (parser.getArgs() != null) // to handle if pwd is entered with argument
                        {
                            System.out.println("pwd takes no args!");
                        } else {
                            Terminal.pwd(file); // display the absolute path of current directory
                        }
                        parser.setArgs(null);
                    } else if (parser.getCmd().equalsIgnoreCase("cd")) {
                        if (parser.getArgs() == null) {
                            file = Terminal.changeDirectory("D:\\", file.getAbsolutePath()); // means that cd doesn't have arguments, so we change to default directory(D:\)
                        } else if (file.equals(Terminal.changeDirectory(parser.getArgs()[0], file.getAbsolutePath())) && parser.getArgs().length > 1)
                            System.out.println("invalid path!");
                        else // argument is valid so change the directory to it
                            file = Terminal.changeDirectory(parser.getArgs()[0], file.getAbsolutePath());

                        parser.setArgs(null);
                    } else if (parser.getCmd().equalsIgnoreCase("clear")) {
                        Terminal.clear();
                    } else if (parser.getCmd().equalsIgnoreCase("more")) {
                        if (parser.getArgs() == null) {
                            System.out.println("Enter one argument");
                        } else if (parser.getArgs().length == 1) {
                            Terminal.more(parser.getArgs()[0], file.getAbsolutePath());
                        } else {
                            System.out.println("Enter one argument only!");
                        }

                    }

                }
            }


        }


    }


}


import java.awt.desktop.SystemSleepEvent;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class CommandLine {
    private static ArrayList<String> listOfCommands = new ArrayList<String>();

    public static void main(String[] args) {
        initializeListOfCommands();
        File file = new File("D:\\");// start in C directory
        Scanner input = new Scanner(System.in);
        String command;
        while (true) {
            System.out.print(file.getAbsolutePath() + " ");
            command = input.nextLine(); // the input
            String[] arrOfCommands = command.split(";");
            for (int j = 0; j < arrOfCommands.length; j++) {
                command = arrOfCommands[j];
                boolean isValid = false;
                int idx = -1;
                String argument = new String(); // for the argument
                String mainCommand = new String(); // for the commands itself(cd, ls, etc.)

                for (int i = 0; i < command.length(); i++) {
                    if (command.charAt(i) == ' ') {
                        mainCommand = command.substring(0, i);
                        if (validate(mainCommand)) {
                            idx = i + 1;
                            argument = command.substring(idx);
                            isValid = true;
                            break;
                        }
                    } else if (i == command.length() - 1) {
                        mainCommand = command.substring(0, i + 1);
                        if (validate(mainCommand)) {
                            isValid = true;
                            break;
                        }
                    }
                }

                if (!isValid) {
                    System.out.println("command not exist!");
                    continue;
                }

                if (command.indexOf('>') != -1) { // > found
                    if (command.indexOf('>') != command.lastIndexOf('>') &&
                            command.lastIndexOf('>') - command.indexOf('>') != 1) { // not be > and >>
                        System.out.println("Invalid usage of operators (>) and (>>)");
                        continue;
                    }
                    File arr[] = file.listFiles();
                    String file2Name = command.substring(command.lastIndexOf(' ') + 1);
                    String checker = lookup(arr, file2Name);
                    if (checker.equals("Not found")) {
                        System.out.println("No such file named: " + file2Name);
                        continue;
                    }
                    File f2 = new File(checker);
                    if (mainCommand.equals("pwd")) {
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
                    } else if (mainCommand.equals("ls")) {
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
                    } else if (mainCommand.equals("cat")) {
                        String file1Name = command.substring(command.indexOf(' ') + 1, command.indexOf('>') - 1);
                        String checker1 = lookup(arr, file1Name);
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
                } else if (mainCommand.equals("cd")) {
                    if (file.equals(changeDirectory(argument,
                            file.getAbsolutePath())) && idx != -1) {
                        System.out.println("invalid path!");
                    } else if (idx == -1) // means that cd doesn't have arguments, so we change to default directory(C:\)
                        file = changeDirectory("C:\\", file.getAbsolutePath());
                    else // argument is valid so change the directory to it
                        file = changeDirectory(argument, file.getAbsolutePath());
                } else if (mainCommand.equals("pwd")) {
                    if (argument.length() > 0) // to handle if pwd is entered with argument
                    {
                        System.out.println("pwd takes no args!");
                    } else {
                        pwd(file); // display the absolute path of current directory
                    }
                } else if (mainCommand.equals("exit")) {
                    terminate();
                    return;
                } else if (command.equals("man ls")) {
                    System.out.println("Name");
                    System.out.println("ls - list directory contents");
                    System.out.println("description");
                    System.out.println("Sort all files and directories in current directory alphabetically "
                            + "and list them\n-a do not ignore enteries starting with .(hidden files)");
                } else if (mainCommand.equals("ls")) {
                    if (argument.length() > 0) {
                        System.out.println("ls don't have arguments");
                    }else {
                        listAllFiles(file.getAbsolutePath());
                    }
                } else if (mainCommand.equals("cp")) {
                    File arr[] = file.listFiles();
                    try {
                        copyFile(arr, argument);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (mainCommand.equals("mv")) {
                    File arr[] = file.listFiles();
                    try {
                        moveFile(arr, argument);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (mainCommand.equals("clear")) {
                    clearConsole();
                } else if (mainCommand.equals("cat")) {
                    File arr[] = file.listFiles();
                    if (command.indexOf(' ') == command.lastIndexOf(' ')) {
                        cat(arr, argument);
                    } else {
                        String file1Name = argument.substring(0, argument.indexOf(' '));
                        String file2Name = argument.substring(argument.indexOf(' ') + 1);
                        cat(arr, concatenate(arr, file1Name, file2Name));
                    }
                } else if (command.equals("date")) {
                    Date date = new Date();
                    System.out.println(date.toString());
                } else if (command.equals("help")) {
                    displayHelp();
                } else if (command.equals("args")) {
                    displayArgs();
                } else if (mainCommand.equals("mkdir")) {
                    if (idx == -1) {
                        System.out.println("missing directory name!");
                    } else {
                        if (!makeDir(argument, file.getAbsolutePath())) {
                            System.out.println("already exist! or wrong path!");
                        }
                    }
                } else if (mainCommand.equals("rm")) {
                    if (!rm(argument, file.getAbsolutePath())) {
                        System.out.println("Wrong parameter!");
                    }
                } else if (mainCommand.equals("rmdir")) {
                    if (!rm("-d " + argument, file.getAbsolutePath())) {
                        System.out.println("Wrong parameter!");
                    }
                } else if (mainCommand.equals("more")) {
                    more(argument, file.getAbsolutePath());
                } else if (mainCommand.equals("less")) {
                    less(argument, file.getAbsolutePath());
                } else if (mainCommand.equals("grep")) {
                    String text = argument.substring(0, argument.indexOf(' '));
                    String fileName = argument.substring(argument.indexOf(' ') + 1, argument.length());
                    File arrTemp[] = file.listFiles();
                    grep(arrTemp, text, fileName);
                }
            }
        }
    }

    public static void initializeListOfCommands() {
        listOfCommands.add("clear");
        listOfCommands.add("cd");
        listOfCommands.add("cp");
        listOfCommands.add("mkdir");
        listOfCommands.add("ls");
        listOfCommands.add("pwd");
        listOfCommands.add("mv");
        listOfCommands.add("rm");
        listOfCommands.add("cat");
        listOfCommands.add("more");
        listOfCommands.add("less");
        listOfCommands.add("rmdir");
        listOfCommands.add("man ls");
        listOfCommands.add("grep");
        listOfCommands.add("help");
        listOfCommands.add("args");
        listOfCommands.add("date");
        listOfCommands.add("exit");
    }

    public static boolean validate(String command) {
        if (listOfCommands.contains(command))
            return true;
        else
            return false;
    }

    public static File changeDirectory(String path, String current) {
        File x = new File(current);
        for (int i = 0; i < x.listFiles().length; i++) {
            if (x.listFiles()[i].getName().equalsIgnoreCase(path))
                return x.listFiles()[i];
        }
        File temp = new File(path);
        if (!temp.exists()) {
            temp = new File(current);
        }
        return temp;
    }

    // The pwd command is a command line utility for printing the current working directory
    public static void pwd(File file) {
        System.out.println(file.getAbsolutePath());
    }


    public static void terminate() {
        Date date = new Date();
        System.out.println("Program terminated on " + date.toString());
    }

    public static void listAllFiles(String currentDirectory) {
        File f = new File(currentDirectory);
        File arr[] = f.listFiles();

            for (int i = 0; i < arr.length; i++) {
                if (arr[i].isHidden())
                    System.out.print("(Hidden)");
                System.out.print(arr[i].getName() + " ");
                System.out.println();
            }
            System.out.println();

    }

    public static void copyFile(File arr[], String command) throws IOException {
        int l = command.lastIndexOf(' ');
        if (l == -1) {
            System.out.println("Not enough arguments for cp");
            return;
        }
        String lastArg = command.substring(l + 1);
        String checker = lookup(arr, lastArg);
        if (checker.equals("Not found")) {
            System.out.println("Error: destination not found");
            return;
        }
        File dest = new File(checker);
        if (dest.isDirectory()) // if its is a directory then copy all other arguments(files) into it
        {
            for (int i = 0, j = 0; i <= l; i++) {
                if (command.charAt(i) == ' ') // loop on all other arguments
                {
                    String arg = command.substring(j, i);
                    checker = lookup(arr, arg);
                    File f = new File(checker);
                    if (!f.exists()) {
                        System.out.println("Error: no such file named " + arg);
                        break;
                    } else if (f.isDirectory()) {
                        System.out.println("Error: can't copy a directory using cp " + arg);
                        break;
                    }
                    //System.out.println(f.getAbsolutePath()+" "+dest.getAbsolutePath());
                    Files.copy(f.toPath(), dest.toPath().resolve(f.toPath().getFileName()), StandardCopyOption.REPLACE_EXISTING);
                    j = i + 1;
                }
            }
        } else if (command.indexOf(' ') == l) // only two arguments given
        {
            String arg = command.substring(0, l);
            checker = lookup(arr, arg);
            if (checker.equals("Not found")) {
                System.out.println("Error: no such file named " + arg);
                return;
            }
            File f = new File(checker);
            InputStream in = new FileInputStream(f);
            OutputStream out = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len); //(bytes array, starting offset, length)
            }
            in.close();
            out.close();
        }
    }

    public static void moveFile(File arr[], String command) throws IOException {
        int l = command.lastIndexOf(' ');
        if (l == -1) {
            System.out.println("Not enough arguments for mv");
            return;
        }
        String lastArg = command.substring(l + 1, command.length());
        String checker = lookup(arr, lastArg);
        if (checker.equals("Not found")) {
            System.out.println("Error: destination not found");
            return;
        }
        File dest = new File(checker);
        if (dest.isDirectory()) {
            for (int i = 0, j = 0; i <= l; i++) {
                if (command.charAt(i) == ' ') {
                    String arg = command.substring(j, i);
                    checker = lookup(arr, arg);
                    File f = new File(checker);
                    if (!f.exists()) {
                        System.out.println("Error: no such file named " + arg);
                        break;
                    } else if (f.isDirectory()) {
                        System.out.println("Error: can't move a directory using mv " + arg);
                        break;
                    }
                    //System.out.println(f.getAbsolutePath()+" "+dest.getAbsolutePath());
                    Files.move(f.toPath(), dest.toPath().resolve(f.toPath().getFileName()), StandardCopyOption.REPLACE_EXISTING);
                    j = i + 1;
                }
            }
        } else if (command.indexOf(' ') == l) // only two arguments are given
        {
            String arg = command.substring(0, l);
            checker = lookup(arr, arg);
            if (checker.equals("Not found")) {
                System.out.println("Error: no such file named " + arg);
                return;
            }
            File f = new File(checker);
            InputStream in = new FileInputStream(f);
            OutputStream out = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            f.delete();
        }
    }

    static String lookup(File arr[], String fileName) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].getName().equalsIgnoreCase(fileName))
                return arr[i].getAbsolutePath();
        }
        return "Not found";
    }

    static void clearConsole() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void cat(File arr[], String fileName) {
        String absPath = lookup(arr, fileName);
        if (absPath.equals("Not found")) {
            System.out.println("No such file named: " + fileName);
            return;
        }
        File f = new File(absPath);
        try {
            Scanner input = new Scanner(f);
            while (input.hasNextLine()) {
                System.out.println(input.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static String concatenate(File arr[], String file1Name, String file2Name) {
        String absPath1 = lookup(arr, file1Name), absPath2 = lookup(arr, file2Name);
        if (absPath1.equals("Not found")) {
            System.out.println("No such file named: " + file1Name);
            return file2Name;
        } else if (absPath2.equals("Not found")) {
            System.out.println("No such file named: " + file2Name);
            return file2Name;
        }
        File file1 = new File(absPath1);
        File file2 = new File(absPath2);
        StringBuilder contentOfFile1 = new StringBuilder();
        try {
            Scanner input = new Scanner(file1);
            while (input.hasNextLine()) {
                contentOfFile1.append(input.nextLine());
                contentOfFile1.append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Files.write(file2.toPath(), contentOfFile1.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file2Name;
    }

    static void displayHelp() {
        System.out.println("date: Displays system date and time");
        System.out.println("help: List all commands and functionalities");
        System.out.println("args: List all commands arguments");
        System.out.println("clear: Clears the console");
        System.out.println("cd: Changes current working directory");
        System.out.println("ls: List all contents of current directory");
        System.out.println("man ls: Displays possible arguments and how to deal with command ls");
        System.out.println("pwd: Displays the absolute path of current directory");
        System.out.println("cp: Copies files");
        System.out.println("mv: Moves files");
        System.out.println("mkdir: Creates a new directory");
        System.out.println("rmdir: Deletes a directory");
        System.out.println("rm: Deletes a file");
        System.out.println("cat: Displays contents of a file and concatenates files and display output");
        System.out.println("more: Let us display and scroll down the output in one direction only");
        System.out.println("less: Like more but more enhanced");
        System.out.println("exit: Terminates the program");
    }

    static void displayArgs() {
        System.out.println("cd: [arg] changes working directory to the given arg");
        System.out.println("cd: [no arg] changes working directory to current directory");
        System.out.println("ls: [no arg] displays contents of a file");
        System.out.println("ls: [-a] displays contents of a file including hidden files");
        System.out.println("cp: [arg1] [arg2] copies contents of arg1(file) to arg2(file)");
        System.out.println("cp: [arg1] [arg2] [argN] copies all given arguments from arg1->argN-1 to"
                + "directory argN");
        System.out.println("mv: [arg1] [arg2] copies contents of arg1(file) to arg2(file) and deletes arg1");
        System.out.println("mv: [arg1] [arg2] [argN] moves all given arguments from arg1->argN-1 to"
                + "directory argN");
        System.out.println("mkdir: [arg] creates a directory with whose name is the given argument");
        System.out.println("rmdir: [arg] deletes a directory whose name is given argument");
        System.out.println("rm: [arg] deletes a file whose name is the given argument");
        System.out.println("cat: [arg1] displays contents of arg1(file)");
        System.out.println("cat: [arg1] [arg2] concatenates contents of arg1 to contents of arg2 and"
                + "displays the result");
    }

    public static void less(String path, String curr) {
        File f = new File(curr + "\\" + path);
        if (f.exists()) {
            try {
                FileInputStream a = new FileInputStream(f);
                BufferedReader br = new BufferedReader(new InputStreamReader(a));
                String l;
                while ((l = br.readLine()) != null) {
                    System.out.println(l);

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        f = new File(path);
        if (f.exists()) {
            try {
                FileInputStream a = new FileInputStream(f);
                BufferedReader br = new BufferedReader(new InputStreamReader(a));
                String l;
                while ((l = br.readLine()) != null) {
                    System.out.println(l);

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
    }

    public static void more(String path, String curr) {
        File f = new File(curr + "\\" + path);
        if (f.exists()) {
            try {
                FileInputStream a = new FileInputStream(f);
                BufferedReader br = new BufferedReader(new InputStreamReader(a));
                String l;
                int c = 0;
                int x;
                Scanner in = new Scanner(System.in);
                while ((l = br.readLine()) != null) {
                    System.out.println(l);
                    c++;
                    if (c % 10 == 0) {
                        System.out
                                .print(".................................MORE press 1 quit press 2 ");
                        x = in.nextInt();
                        if (x == 2)
                            break;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        f = new File(path);
        if (f.exists()) {
            try {
                FileInputStream a = new FileInputStream(f);
                BufferedReader br = new BufferedReader(new InputStreamReader(a));
                String l;
                int c = 0;
                int x;
                Scanner in = new Scanner(System.in);
                while ((l = br.readLine()) != null) {
                    System.out.println(l);
                    c++;
                    if (c % 10 == 0) {
                        System.out
                                .print(".................................MORE press 1 quit press 2 ");
                        x = in.nextInt();
                        if (x == 2)
                            break;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
    }
    public static boolean rm(String path, String curr) {
        String[] command = path.split("\\s+");
        if (command.length > 2) {
            return false;
        }
        if (command.length == 1) {
            File a = new File(curr + "\\" + path);

            if (!a.isDirectory() && a.exists())
                return a.delete();

            a = new File(path);
            if (!a.isDirectory())
                return a.delete();
        }
        if (command.length == 2) {
            if (command[0].equals("-d")) {
                System.out.println(command[0] + " " + command[1]);
                File z = new File(curr + "\\" + command[1]);
                System.out.println(curr + "\\" + command[1]);
                // System.out.println(z.getAbsolutePath());
                if (z.isDirectory()) {
                    return z.delete();
                }
                z = new File(command[1]);
                // System.out.println(z.getAbsolutePath());
                if (z.isDirectory())
                    return z.delete();
            }
        }
        return false;
    }

    public static boolean makeDir(String pathName, String current) {
        // Handling short paths and full paths
        File f = new File(current + "\\" + pathName);
        boolean found = f.mkdir();
        if (found)
            return found;

        File f2 = new File(pathName);
        boolean found2 = f2.mkdir();
        if (found2)
            return found2;

        return false;
    }

    static void grep(File arr[], String text, String fileName) {
        String checker = lookup(arr, fileName);
        if (checker.equals("Not found")) {
            System.out.println("No such file named: " + fileName);
            return;
        }
        File f = new File(checker);
        try {
            Scanner input = new Scanner(f);
            while (input.hasNextLine()) {
                String temp = input.nextLine();
                if (temp.contains(text))
                    System.out.println(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

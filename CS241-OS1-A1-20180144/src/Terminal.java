import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Terminal {

    public static void cp(File arr[], String[] argsList) throws IOException {
        if (argsList == null) {
            System.out.println("Not enough arguments for cp");
            return;
        }
        int l = argsList.length - 1; //directory
        String lastArg = argsList[l]; //directory
        String checker = search(arr, lastArg);
        if (checker.equals("Not found")) {
            System.out.println("Error: destination not found");
            return;
        }
        File dest = new File(checker);
        if (dest.isDirectory()) // if its is a directory then copy all other arguments(files) into it
        {
            for (int i = 0; i < l; i++) {
                // loop on all other arguments
                String arg = argsList[i];
                checker = search(arr, arg);
                File f = new File(checker);
                if (!f.exists()) {
                    System.out.println("Error: no such file named " + arg);
                    break;
                } else if (f.isDirectory()) {
                    System.out.println("Error: can't copy a directory using cp " + arg);
                    break;
                }
                Files.copy(f.toPath(), dest.toPath().resolve(f.toPath().getFileName()), StandardCopyOption.REPLACE_EXISTING);

            }
        }
    }

    static String search(File arr[], String fileName) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].getName().equalsIgnoreCase(fileName))
                return arr[i].getAbsolutePath();
        }
        return "Not found";
    }

    public static void mv(File arr[], String[] argsList) throws IOException {
        if (argsList == null) {
            System.out.println("Not enough arguments for mv");
            return;
        }
        int l = argsList.length - 1; //directory
        String lastArg = argsList[l]; //directory
        String checker = search(arr, lastArg);
        if (checker.equals("Not found")) {
            System.out.println("Error: destination not found");
            return;
        }
        File dest = new File(checker);
        if (dest.isDirectory()) // if its is a directory then copy all other arguments(files) into it
        {
            for (int i = 0; i < l; i++) {
                // loop on all other arguments
                String arg = argsList[i];
                checker = search(arr, arg);
                File f = new File(checker);
                if (!f.exists()) {
                    System.out.println("Error: no such file named " + arg);
                    break;
                } else if (f.isDirectory()) {
                    System.out.println("Error: can't move a directory using mv " + arg);
                    break;
                }
                Files.move(f.toPath(), dest.toPath().resolve(f.toPath().getFileName()), StandardCopyOption.REPLACE_EXISTING);

            }
        }
    }

    public static boolean rm(String[] newPath, String currentPath) {
        if (newPath.length > 2) {
            return false;
        }
        File a = new File(currentPath + "\\" + newPath[0]);

        // Handling short paths and full paths
        if (!a.isDirectory() && a.exists())
            return a.delete();

        a = new File(newPath[0]);
        if (!a.isDirectory())
            return a.delete();

        return false;
    }

    public static boolean rmDir(String[] newPath, String currentPath) {
        if (newPath.length > 2) {
            return false;
        }

        // Handling short paths and full paths
        File z = new File(currentPath + "\\" + newPath[0]);
        if (z.isDirectory()) {
            return z.delete();
        }

        z = new File(newPath[0]);
        if (z.isDirectory())
            return z.delete();

        return false;
    }

    public void cat(File arr[], String[] fileName) throws FileNotFoundException {
        String absPath = Terminal.search(arr, fileName[0]);
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

    public static boolean mkdir( String pathName, String current) {
        // Handling short paths and full paths
        File file = new File(current + "\\" + pathName);
        boolean found = file.mkdir();
        if (found)
            return found;

        File file2 = new File(pathName);
        boolean found2 = file2.mkdir();
        if (found2)
            return found2;

        return false;

    }

    public static void args() {
        System.out.println("cd: [arg] changes working directory to the given arg");
        System.out.println("cd: [no arg] changes working directory to current directory");
        System.out.println("ls: [no arg] displays contents of a file including hidden files");
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

    public static void help() {
        System.out.println("date: Displays system date and time");
        System.out.println("help: List all commands and functionalities");
        System.out.println("args: List all commands arguments");
        System.out.println("clear: Clears the console");
        System.out.println("cd: Changes current working directory");
        System.out.println("ls: List all contents of current directory");
        System.out.println("pwd: Displays the absolute path of current directory");
        System.out.println("cp: Copies files");
        System.out.println("mv: Moves files");
        System.out.println("mkdir: Creates a new directory");
        System.out.println("rmdir: Deletes a directory");
        System.out.println("rm: Deletes a file");
        System.out.println("cat: Displays contents of a file and concatenates files and display output");
        System.out.println("more: Let us display and scroll down the output in one direction only");
        System.out.println("exit: Terminates the program");
    }

    public static void clear() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void Pipe() {

    }

    public static void date() {
        DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(time.format(now));
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
    // The pwd command is a command line utility for printing the current working directory
    public static void pwd(File file) {
        System.out.println(file.getAbsolutePath());
    }

    public static void cat(File arr[], String fileName) {
        String absPath = search(arr, fileName);
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

    public static String concatenate(File arr[], String file1Name, String file2Name) {
        String absPath1 = Terminal.search(arr, file1Name), absPath2 = Terminal.search(arr, file2Name);
        if (absPath1.equals("Not found")) {
            return file1Name;
        } else if (absPath2.equals("Not found")) {
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
}

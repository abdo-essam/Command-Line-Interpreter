import java.util.ArrayList;

public class Parser {
    String[] args; // Will be filled by arguments extracted by parse method
    String cmd; // Will be filled by the command extracted by parse method
    private static ArrayList<String> Commands = new ArrayList<String>();

    /**
     * Return: true if it was able to parse user input correctly. Otherwise false
     * Parameter input: user command
     * In case of success, it should save the extracted command and arguments to
     * args and cmd variables
     * It should also print error messages in case of too few arguments for a
     * commands
     * eg. “cp requires 2 arguments”
     */
    public boolean parse(String input) {

        boolean isValid = false;
        String argument ;
        int idx = -1;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                cmd = input.substring(0, i);
                if (validate(cmd)) {
                    idx = i + 1;
                    argument = input.substring(idx);
                    args = argument.split(" ");
                    isValid = true;
                    break;
                }
            } else if (i == input.length() - 1) {
                cmd = input.substring(0, i + 1);
                if (validate(cmd)) {
                    isValid = true;
                    break;
                }
            }
        }
        if (!isValid) {
            System.out.println("command not exist!");
        }
        return isValid;
    }

    public static boolean validate(String command) {
        if (Commands.contains(command.toLowerCase()))
            return true;
        else
            return false;
    }

    public String[] getArgs() {
        return args;
    }

    public String getCmd() {
        return cmd;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public static void initializeCommands() {
        Commands.add("clear");
        Commands.add("cd");
        Commands.add("cp");
        Commands.add("mkdir");
        Commands.add("ls");
        Commands.add("pwd");
        Commands.add("mv");
        Commands.add("rm");
        Commands.add("cat");
        Commands.add("more");
        Commands.add("rmdir");
        Commands.add("help");
        Commands.add("args");
        Commands.add("date");
        Commands.add("exit");
    }
}

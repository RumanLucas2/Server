package org.example;

import java.util.ArrayList;

public class Commands {
    private static ArrayList<String> commands;

    public static void add(String cmd)
    {
        commands.add(cmd);
    }

    public static void remove(int i)throws Exception {
        if(i+1>commands.size()){
            throw new Exception("Out of bounds");
        }
        commands.remove(i);
    }
    public static void remove(String cmd) throws Exception {
        if (commands.remove(cmd)){
            throw new Exception("Item not found");
        }
    }
    public static ArrayList<String> getCommands() {
        if (commands == null) commands = new ArrayList<>();
        return commands;
    }

    public static String get(int i){
        return commands.get(i);
    }
    public static int size(){
        return commands.size();
    }

    public static String getTop(){
        return Commands.get(0);
    }
}

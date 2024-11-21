package org.example;

import com.mongodb.lang.Nullable;
import com.sun.source.util.TaskEvent;
import jdk.jfr.Unsigned;

import java.util.concurrent.CompletableFuture;

public enum ServerCommand {

    //Normal commands
    End{
        @Override
        public boolean execute(){
            try{
                Servidor.Terminate();
                return true;
            }catch (Exception e){
                return false;
            }
        }
    },
    Start{
        @Override
        public boolean execute() {
            try{
                Servidor.Start();
                return true;
            }catch (Exception e){
                return false;
            }
        }
    },

    Status{
        @Override
        public boolean execute(){
            try{
                Commands.add("status");
                return true;
            }catch (Exception e){
                System.err.println(e.getMessage());
                return false;

            }
        }
    },
    List{
        @Override
        public boolean execute(){
            try{
                Commands.add("list");
                return true;
            }catch (Exception e){
                System.err.println(e.getMessage());
                return false;

            }
        }
    };

    public void execute(@Nullable String obj){
        if (obj == null)execute();
    }
    public abstract boolean execute();
}


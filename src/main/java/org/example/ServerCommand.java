package org.example;

import com.mongodb.lang.Nullable;

public enum ServerCommand {

    //Normal commands
    End{
        @Override
        public Object execute(){
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
        public Object execute() {
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
        public Object execute(){
            try{
                Commands.add("status");
                return "Online";
            }catch (Exception e){
                System.err.println(e.getMessage());
                return e.getMessage();

            }
        }
    },
    List{
        @Override
        public Object execute(){
            try{
                StringBuilder response = new StringBuilder();
                for (var cmd: ServerCommand.values()){
                    response.append(cmd.name()).append("\n");
                }

                return response.toString();
            }catch (Exception e){
                System.err.println(e.getMessage());
                return null;

            }
        }
    };

    public Object execute(@Nullable String obj){
        if (obj == null)execute();
        return null;
    }
    public abstract Object execute();

    public static boolean exists(String obj){
        for(ServerCommand cmd : ServerCommand.values()){
            if (obj.equals(cmd.toString())){
                return true;
            }
        }
        return false;
    };
}


package org.example;

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

    Disconnect {
        @Override
        public String execute(Object obj) throws Exception {
            Parceiro usr = (Parceiro) obj;
            try{
                usr.Terminate();
            }catch (Exception ex){
                System.err.println("Erro ao finalizar objeto: "+ex);
            }

            Servidor.getUsers().remove(usr);
            Servidor.Start();
            return null;
        }

        @Override
        public Object execute(){
            throw new RuntimeException("Not valid");
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

    public abstract Object execute();
    public Object execute(Object obj) throws Exception{
        return null;
    };

    public static boolean exists(Object obj){
        for(ServerCommand cmd : ServerCommand.values()){
            if (obj.equals(cmd.toString())){
                return true;
            }
        }
        return false;
    };
}


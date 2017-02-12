package dk.cphbusiness.virtualcpu;

public class Main {
  
  public static void main(String[] args) {
    System.out.println("Welcome to the awesome CPU program");
    
    Program program = new Program("MOV 5 A", "PUSH A", "ALWAYS","POP A");
    Machine machine = new Machine();
    machine.load(program);
//    machine.print(System.out);
//    machine.tick();
//    machine.print(System.out);

    for (int line : program){
        System.out.println(">>> "+line);
    machine.tick();
    machine.print(System.out);
    }
    }
    
  }

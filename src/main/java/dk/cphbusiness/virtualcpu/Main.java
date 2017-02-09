package dk.cphbusiness.virtualcpu;

public class Main {
  
  public static void main(String[] args) {
    System.out.println("Welcome to the awesome CPU program");
    Machine machine = new Machine();
    machine.print(System.out);
    machine.tick();
    machine.print(System.out);
    }
    
  }

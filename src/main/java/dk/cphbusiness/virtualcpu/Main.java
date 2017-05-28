package dk.cphbusiness.virtualcpu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
  List l;
  
  public static void main(String[] args) {
      
    System.out.println("Welcome to the awesome CPU program");
    File f = new File("fact.txt");
    Program program=null;
    Scanner s;
      try {
          s = new Scanner(f);
      List<String> lines = new ArrayList();
      while(s.hasNext())lines.add(s.nextLine());
      program = new Program(lines);
      } catch (FileNotFoundException ex) {
          Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      }
    
    Machine machine = new Machine();
    machine.load(program);
    machine.run();
//    machine.print(System.out);
//    machine.tick();
//    machine.print(System.out);

//    for (int line : program){
//        System.out.println(">>> "+line);
//    machine.tick();
//    machine.print(System.out);
//    }
    }
    
  }

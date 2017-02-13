package dk.cphbusiness.virtualcpu;

import java.util.Iterator;
import java.util.List;

public class Program implements Iterable<Integer> {
  private String[] lines;
  
  public Program(String... lines) {
    this.lines = lines;
    }
  
  public Program(List<String> lines){
      this.lines= lines.toArray(new String[lines.size()]);
  }
  
  public int get(int index) {
    String line = lines[index];
    if (line.charAt(0) == '0' || line.charAt(0) == '1') {
      return Integer.parseInt(line, 2);
      }
    else return getFromCommand(line);
    
    }
  public String getString(int index){
      return lines[index];
  }
  
  private int getFromCommand(String line){
      // 0000 0000	NOP	
      if (line.equals("NOP")) return 0b0000_0000;
      
      // 0000 0001	ADD	
      if (line.equals("ADD")) return 0b0000_0001;
      // 0000 0010	MUL	
      if (line.equals("MUL")) return 0b0000_0010;
      // 0000 0011	DIV	
      if (line.equals("DIV")) return 0b0000_0011;
      // 0000 0100	ZERO	
      if (line.equals("ZERO")) return 0b0000_0100;
      // 0000 0101	NEG	
      if (line.equals("NEG")) return 0b0000_0101;
      // 0000 0110	POS	
      if (line.equals("POS")) return 0b0000_0110;
      // 0000 0111	NZERO	
      if (line.equals("NZERO")) return 0b0000_0111;
      // 0000 1000	EQ	
      if (line.equals("EQ")) return 0b0000_1000;
      // 0000 1001	LT	
      if (line.equals("LT")) return 0b0000_1001;
      // 0000 1010	GT	
      if (line.equals("GT")) return 0b0000_1010;
      // 0000 1011	NEQ	
      if (line.equals("NEQ")) return 0b0000_1011;
      // 0000 1100	ALWAYS	
      if (line.equals("ALWAYS")) return 0b0000_1100;
      // 0000 1111	HALT	
      if (line.equals("HALT")) return 0b0000_1111;
      // 0001 000r	PUSH r
      if (line.equals("PUSH A")) return 0b0001_0000;
      if (line.equals("PUSH B")) return 0b0001_0001;
      // 0001 001r	POP r
      if (line.equals("POP A")) return 0b0001_0010;
      if (line.equals("POP B")) return 0b0001_0011;
      // 0001 0100	MOV A B	
      if (line.equals("MOV A B")) return 0b0001_0100;
      // 0001 0101	MOV B A
      if (line.equals("MOV B A")) return 0b0001_0101;
      // 0001 0110	INC
      if (line.equals("INC")) return 0b0001_0110;
      // 0001 0111	DEC	
      if (line.equals("DEC")) return 0b0001_0111;
     
      if (line.startsWith("RTN")){
          String[] parts = line.split(" ");
          return 0b0001_1000 | Integer.parseInt(parts[1]);
      }
      
      if (line.startsWith("JMP"))return 0b1000_0000 | Integer.parseInt(line.split(" ")[1]);
      if (line.startsWith("CALL"))return 0b1100_0000 | Integer.parseInt(line.split(" ")[1]);
      
      
      
      if (line.startsWith("MOV ")) {
      String[] parts = line.split(" ");
      if(parts[1].equals("A") || parts[1].equals("B")){
      int r = parts[1].equals("B") ? 1 : 0;
      int o = Integer.parseInt(parts[2]);
      return 0b0010_0000 | (r << 3) | o;
      }
      else if (parts[2].equals("A") || parts[2].equals("B")){
          int r = parts[1].equals("B") ? 1 : 0;
          // 2 interpretations?!
          if (parts[1].startsWith("+")){
              return 0b0011_0000 | Integer.parseInt(parts[1])<<1 | r;
          }
          else return 0b01000000 | convertValue2Complement(parts[1])<<1 | r;
          
      }
      throw new UnsupportedOperationException("Don't know "+line);
      }
      else throw new UnsupportedOperationException("Don't know "+line);
  }
  
  private int convertValue2Complement(String number){
      int num = Integer.parseInt(number);
      if (num<0) return 32-Math.abs(num);
      return num;
  }

  @Override
  public Iterator<Integer> iterator() {
    return new ProgramIterator();
    }
  
  private class ProgramIterator implements Iterator<Integer> {
    private int current = 0;

    
    @Override
    public boolean hasNext() {
      return current < lines.length;
      }

    @Override
    public Integer next() {
      return get(current++);
      }
    
    }
  
  
  
  

  }

package dk.cphbusiness.virtualcpu;

import java.io.PrintStream;

public class Memory {
  public static final int SIZE = 64;
  private final byte[] data = new byte[SIZE];

  public int get(int index) {
    int value = data[index];
    value = value & 255; //0b1111_1111; // & is binary and: and on every bit in value and 0b1111_1111
    return value;
    }
  
  public void set(int index, int value) {
    // TODO
    data[index] = (byte)(value & 0b1111_1111);
    }
  
  public void print(PrintStream out, int index) {
    out.printf("%2d: %4d   ", index, get(index));
    }
  
  public void print(PrintStream out) {
    for (int index = 0; index < 16; index++) {
      print(out, index);
      print(out, 16 + index);
      print(out, 32 + index);
      print(out, 48 + index);
      out.println();
      }
    }
  
  }
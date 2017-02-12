package dk.cphbusiness.virtualcpu;

import java.io.PrintStream;

public class Machine {

    private Cpu cpu = new Cpu();
    private Memory memory = new Memory();
    private boolean halt = false;

    public void load(Program program) {
        int index = 0;
        for (int instr : program) {
            memory.set(index++, instr);
        }
    }

    public void tick() {
        int instr = memory.get(cpu.getIp());

        switch (instr) {
            //0000 0000	NOP	IP++
            case 0b0000_0000:
                cpu.incIp();
                break;
            //0000 0001	ADD	A ← A + B; IP++         
            case 0b0000_0001:
                cpu.setA(cpu.getA() + cpu.getB());
                cpu.incIp();
                break;
            //0000 0010	MUL	A ← A*B; IP++
            case 0b0000_0010:
                cpu.setA(cpu.getA() * cpu.getB());
                cpu.incIp();
                break;
            //0000 0011	DIV	A ← A/B; IP++
            case 0b0000_0011:
                cpu.setA(cpu.getA() / cpu.getB());
                cpu.incIp();
                break;
            //0000 0100	ZERO	F ← A = 0; IP++
            case 0b0000_0100:
                cpu.setFlag(cpu.getA() == 0);
                cpu.incIp();
                break;
            //0000 0101	NEG	F ← A < 0; IP++
            case 0b0000_0101:
                cpu.setFlag(cpu.getA() < 0);
                cpu.incIp();
                break;
            //0000 0110	POS	F ← A > 0; IP++
            case 0b0000_0110:
                cpu.setFlag(cpu.getA() > 0);
                cpu.incIp();
                break;
            //0000 0111	NZERO	F ← A ≠ 0; IP++
            case 0b0000_0111:
                cpu.setFlag(cpu.getA() != 0);
                cpu.incIp();
                break;
            //0000 1000	EQ	F ← A = B; IP++
            case 0b0000_1000:
                cpu.setFlag(cpu.getA() == cpu.getB());
                cpu.incIp();
                break;
            ///0000 1001	LT	F ← A < B; IP++
            case 0b0000_1001:
                cpu.setFlag(cpu.getA() < cpu.getB());
                cpu.incIp();
                break;
            // 0000 1010	GT	F ← A > B; IP++
            case 0b0000_1010:
                cpu.setFlag(cpu.getA() > cpu.getB());
                cpu.incIp();
                break;
            //0000 1011	NEQ	F ← A ≠ B; IP++         
            case 0b0000_1011:
                cpu.setFlag(cpu.getA() != cpu.getB());
                cpu.incIp();
                break;
            //0000 1100	ALWAYS	F ← true; IP++
            case 0b0000_1100:
                cpu.setFlag(true);
                cpu.incIp();
                break;
            //0000 1101		Undefined
            case 0b0000_1101:
                break;
            //0000 1110		Undefined
            case 0b0000_1110:
                break;
            //0000 1111	HALT	Halts execution
            case 0b0000_1111:
                halt = true;
                break;
            //0001 0100	MOV A B	B ← A; IP++
            case 0b0001_0100:
                cpu.setB(cpu.getA());
                cpu.incIp();
                break;
            //0001 0101	MOV B A	A ← B; IP++
            case 0b0001_0101:
                cpu.setA(cpu.getB());
                cpu.incIp();
                break;
            //0001 0110	INC	A++; IP++
            case 0b0001_0110:
                cpu.setA(cpu.getA() + 1);
                cpu.incIp(); // add constraint
                break;
            //0001 0111	DEC	A--; IP++
            case 0b0001_0111:
                cpu.setA(cpu.getA() - 1);
                cpu.incIp(); // add constraint
                break;
            default:
                break;
        }
        // Special cases
//0001 000r	PUSH r	[--SP] ← r; IP++ 
        if ((instr & 0b1111_1110) == 0b0001_0000) {

            cpu.decSp();
            int r = (instr & 0b0000_1000) >> 3;
            if (r == cpu.A) {
                memory.set(cpu.getSp(), cpu.getA());
            } else {
                memory.set(cpu.getSp(), cpu.getB());
            }
            cpu.incIp();
        }
        //0001 001r	POP r	r ← [SP++]; IP++
        if ((instr & 0b1111_1110) == 0b0001_0010) {
            int r = (instr & 0b0000_1000) >> 3;

            if (r == cpu.A) {
                cpu.setA(memory.get(cpu.getSp()));
            } else {
                cpu.setB(memory.get(cpu.getSp()));
            }
            cpu.incSp();
            cpu.incIp();
        }
        // 0001 1ooo	RTN +o	IP ← [SP++]; SP += o; IP++
        if ((instr & 0b1111_1110) == 0b0001_0010) {
            int r = (instr & 0b0000_1000) >> 3;

            if (r == cpu.A) {
                cpu.setA(memory.get(cpu.getSp()));
            } else {
                cpu.setB(memory.get(cpu.getSp()));
            }
            cpu.incSp();
            cpu.incIp();
        }
        

            // 0010 r ooo	MOV r o	   [SP + o] ← r; IP++
        if ((instr & 0b1111_0000) == 0b0010_0000) {

            // 0010 1 011 MOV B (=1) +3  [SP +3] // Move register B to memory position of SP with offset 3
            // 00101011 finding instruction
            //    and
            // 11110000
            // --------
            // 00100000
            // 00101011 finding offset
            //    and
            // 00000111
            // --------
            // 00000011 = 3
            // 00101011 finding register
            //    and
            // 00001000
            // --------
            // 00001000 = 8
            //    >> 3
            // 00000001 = 1
            int o = instr & 0b0000_0111;
            int r = (instr & 0b0000_1000) >> 3;
            if (r == cpu.A) {
                memory.set(cpu.getSp() + o, cpu.getA());
            } else {
                memory.set(cpu.getSp() + o, cpu.getB());
            }
            cpu.setIp(cpu.getIp() + 1);
        }
                // 0011 ooor	MOV o r	r ← [SP + o]; IP++
        if ((instr & 0b1111_0000) == 0b0011_0000) {
            int r = (instr & 0b0000_0001);
            int o = (instr & 0b0000_1110) >> 1;

            if (r == cpu.A) {
                cpu.setA(memory.get(cpu.getSp()+o));
            } else {
                cpu.setB(memory.get(cpu.getSp()+o));
            }
            cpu.incIp();
        }
        //01vv vvvr	MOV v r	r ← v; IP++
        if ((instr & 0b1100_0000) == 0b0100_0000) {
            int r = (instr & 0b0000_0001);
            int v = (instr & 0b0011_1110) >> 1;

            if (r == cpu.A) {
                cpu.setA(v);
            } else {
                cpu.setB(v);
            }
            cpu.incIp();
        }
        // 10aa aaaa	JMP #a	if F then IP ← a else IP++
        if ((instr & 0b1100_0000) == 0b1000_0000) {
            if (cpu.isFlag()){
                cpu.setIp(instr & 0b00111111);
            }
            else cpu.incIp();
        }
        //11aa aaaa	CALL #a	if F then [--SP] ← IP; IP ← a else IP++
        if ((instr & 0b1100_0000) == 0b1100_0000) {
            if (cpu.isFlag()){
                cpu.decSp();
                cpu.setSp(cpu.getIp());
                cpu.setIp(instr & 0b00111111);
            }
            else cpu.incIp();
        }
        
        
        
    }

    public void print(PrintStream out) {
        memory.print(out);
        out.println("-------------");
        cpu.print(out);
    }

}

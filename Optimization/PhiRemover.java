package Optimization;

import java.util.ArrayList;
import java.util.HashMap;
import Composer.*;
import IRSentence.*;

public class PhiRemover {
  HashMap<Integer, HashMap<Integer, ArrayList<PseudoMove>>> moves = new HashMap<>();
  Composer machine = null;

  public PhiRemover(Composer _machine) {
    machine = _machine;
  }

  public void Remove() {
    GenerateMove();
    BuildNewBlocks();
    RemovePhis();
  }

  void GenerateMove() {
    int func = 0;
    int now = 0;
    for (IRCode code : machine.generated) {
      if (code instanceof IRFunc) {
        now = --func;
      }
      if (code instanceof IRLabel) {
        IRLabel label = (IRLabel) code;
        now = label.label;
      }
      if (code instanceof IRPhi) {
        IRPhi phi = (IRPhi) code;
        for (int i = 0; i < phi.labels.size(); i++) {
          FromToPair ftpair = new FromToPair(phi.labels.get(i), now);
          PseudoMove move = new PseudoMove(phi.values.get(i), phi.target);
          if (!moves.containsKey(ftpair.from)) {
            moves.put(ftpair.from, new HashMap<>());
          }
          if (!moves.get(ftpair.from).containsKey(ftpair.to)) {
            moves.get(ftpair.from).put(ftpair.to, new ArrayList<>());
          }
          moves.get(ftpair.from).get(ftpair.to).add(move);
        }
      }
    }
    return;
  }

  void RemovePhis() {
    for (int i = machine.generated.size() - 1; i >= 0; i--) {
      if (machine.generated.get(i) instanceof IRPhi) {
        machine.generated.remove(i);
      }
    }
  }

  void BuildNewBlocks() {
    HashMap<Integer, ArrayList<MoveBlock>> move_buffer = new HashMap<>();
    int func = 0;
    int now = 0;
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      if (code instanceof IRFunc) {
        now = --func;
      }
      if (code instanceof IRLabel) {
        IRLabel label = (IRLabel) code;
        now = label.label;
      }
      if (code instanceof IRjmp) {
        IRjmp jmp = (IRjmp) code;
        FromToPair ftpair = new FromToPair(now, jmp.label);
        if (moves.containsKey(ftpair.from) && moves.get(ftpair.from).containsKey(ftpair.to)) {
          MoveBlock new_block = new MoveBlock();
          MoveBlock new_block2 = new MoveBlock();
          new_block.num = ++machine.label_number;
          new_block2.num = ++machine.label_number;
          new_block.to = machine.label_number;
          new_block2.to = ftpair.to;
          ArrayList<PseudoMove> op2 = new ArrayList<>();
          for (PseudoMove op : moves.get(ftpair.from).get(ftpair.to)) {
            boolean danger = false;
            for(PseudoMove check_op: moves.get(ftpair.from).get(ftpair.to)) {
              if(op == check_op) {
                continue;
              }
              if(op.des.equals(check_op.src)) {
                danger = true;
                break;
              }
            }
            if(danger) {
            String res = "%reg$" + Integer.toString(++machine.tmp_time);
            new_block.moves.add(new PseudoMove(op.src, res));
            op2.add(new PseudoMove(res, op.des));
            } else {
              new_block.moves.add(new PseudoMove(op.src, op.des));
            }
          }
          for (PseudoMove op_v : op2) {
            new_block2.moves.add(op_v);
          }
          jmp.label = new_block.num;
          if(move_buffer.get(ftpair.to) == null) {
            move_buffer.put(ftpair.to, new ArrayList<>());
          }
          move_buffer.get(ftpair.to).add(new_block);
          move_buffer.get(ftpair.to).add(new_block2);
        }
      }
      if (code instanceof Conditionjmp) {
        Conditionjmp condtion_jmp = (Conditionjmp) code;
        FromToPair ftpair = new FromToPair(now, condtion_jmp.label1);
        if (moves.containsKey(ftpair.from) && moves.get(ftpair.from).containsKey(ftpair.to)) {
          MoveBlock new_block = new MoveBlock();
          MoveBlock new_block2 = new MoveBlock();
          new_block.num = ++machine.label_number;
          new_block2.num = ++machine.label_number;
          new_block.to = machine.label_number;
          new_block2.to = ftpair.to;
          ArrayList<PseudoMove> op2 = new ArrayList<>();
          for (PseudoMove op : moves.get(ftpair.from).get(ftpair.to)) {
            boolean danger = false;
            for(PseudoMove check_op: moves.get(ftpair.from).get(ftpair.to)) {
              if(op == check_op) {
                continue;
              }
              if(op.des.equals(check_op.src)) {
                danger = true;
                break;
              }
            }
            if(danger) {
            String res = "%reg$" + Integer.toString(++machine.tmp_time);
            new_block.moves.add(new PseudoMove(op.src, res));
            op2.add(new PseudoMove(res, op.des));
            } else {
              new_block.moves.add(new PseudoMove(op.src, op.des));
            }
          }
          for (PseudoMove op_v : op2) {
            new_block2.moves.add(op_v);
          }
          condtion_jmp.label1 = new_block.num;
          if(move_buffer.get(ftpair.to) == null) {
            move_buffer.put(ftpair.to, new ArrayList<>());
          }
          move_buffer.get(ftpair.to).add(new_block);
          move_buffer.get(ftpair.to).add(new_block2);
        }
        FromToPair ftpair2 = new FromToPair(now, condtion_jmp.label2);
        if (moves.containsKey(ftpair2.from) && moves.get(ftpair2.from).containsKey(ftpair2.to)) {
          MoveBlock new_block = new MoveBlock();
          MoveBlock new_block2 = new MoveBlock();
          new_block.num = ++machine.label_number;
          new_block2.num = ++machine.label_number;
          new_block.to = machine.label_number;
          new_block2.to = ftpair2.to;
          ArrayList<PseudoMove> op2 = new ArrayList<>();
          for (PseudoMove op : moves.get(ftpair2.from).get(ftpair2.to)) {
            boolean danger = false;
            for(PseudoMove check_op: moves.get(ftpair2.from).get(ftpair2.to)) {
              if(op == check_op) {
                continue;
              }
              if(op.des.equals(check_op.src)) {
                danger = true;
                break;
              }
            }
            if(danger) {
            String res = "%reg$" + Integer.toString(++machine.tmp_time);
            new_block.moves.add(new PseudoMove(op.src, res));
            op2.add(new PseudoMove(res, op.des));
            } else {
              new_block.moves.add(new PseudoMove(op.src, op.des));
            }
          }
          for (PseudoMove op_v : op2) {
            new_block2.moves.add(op_v);
          }
          condtion_jmp.label2 = new_block.num;
          if(move_buffer.get(ftpair2.to) == null) {
            move_buffer.put(ftpair2.to, new ArrayList<>());
          }
          move_buffer.get(new_block2.to).add(new_block);
          move_buffer.get(new_block2.to).add(new_block2);
        }
      }
    }
    for(int i = 0; i < machine.generated.size(); i++) {
      if(machine.generated.get(i) instanceof IRLabel) {
        IRLabel code = (IRLabel)machine.generated.get(i);
        if(move_buffer.containsKey(code.label)) {
          for(MoveBlock move_block: move_buffer.get(code.label)) {
            machine.generated.add(i, move_block);
          }
          move_buffer.remove(code.label);
        }
      }
    }
    return;
  }
}
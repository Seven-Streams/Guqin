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
    ArrayList<MoveBlock> move_buffer = new ArrayList<>();
    int func = 0;
    int now = 0;
    int last_label = 0;
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      if (code instanceof IRFunc) {
        now = --func;
      }
      if (code instanceof IRLabel) {
        IRLabel label = (IRLabel) code;
        last_label = i;
        now = label.label;
      }
      if (code instanceof IRFuncend) {
        for (MoveBlock move : move_buffer) {
          machine.generated.add(last_label, move);
        }
        move_buffer.clear();
      }
      if (code instanceof IRjmp) {
        IRjmp jmp = (IRjmp) code;
        FromToPair ftpair = new FromToPair(now, jmp.label);
        if (moves.containsKey(ftpair.from) && moves.get(ftpair.from).containsKey(ftpair.to)) {
          MoveBlock new_block = new MoveBlock();
          new_block.num = ++machine.label_number;
          new_block.to = ftpair.to;
          ArrayList<PseudoMove> op2 = new ArrayList<>();
          for (PseudoMove op : moves.get(ftpair.from).get(ftpair.to)) {
            String res = "%reg" + Integer.toString(++machine.tmp_time);
            new_block.moves.add(new PseudoMove(op.src, res));
            op2.add(new PseudoMove(res, op.des));
          }
          for (PseudoMove op_v : op2) {
            new_block.moves.add(op_v);
          }
          jmp.label = new_block.num;
          move_buffer.add(new_block);
        }
      }
      if (code instanceof Conditionjmp) {
        Conditionjmp condtion_jmp = (Conditionjmp) code;
        FromToPair ftpair = new FromToPair(now, condtion_jmp.label1);
        if (moves.containsKey(ftpair.from) && moves.get(ftpair.from).containsKey(ftpair.to)) {
          MoveBlock new_block = new MoveBlock();
          new_block.num = ++machine.label_number;
          new_block.to = ftpair.to;
          ArrayList<PseudoMove> op2 = new ArrayList<>();
          for (PseudoMove op : moves.get(ftpair.from).get(ftpair.to)) {
            String res = "%reg" + Integer.toString(++machine.tmp_time);
            new_block.moves.add(new PseudoMove(op.src, res));
            op2.add(new PseudoMove(res, op.des));
          }
          for (PseudoMove op_v : op2) {
            new_block.moves.add(op_v);
          }
          condtion_jmp.label1 = new_block.num;
          move_buffer.add(new_block);
        }
        FromToPair ftpair2 = new FromToPair(now, condtion_jmp.label2);
        if (moves.containsKey(ftpair2.from) && moves.get(ftpair2.from).containsKey(ftpair2.to)) {
          MoveBlock new_block = new MoveBlock();
          new_block.num = ++machine.label_number;
          new_block.to = ftpair2.to;
          ArrayList<PseudoMove> op2 = new ArrayList<>();
          for (PseudoMove op : moves.get(ftpair2.from).get(ftpair2.to)) {
            String res = "%reg" + Integer.toString(++machine.tmp_time);
            new_block.moves.add(new PseudoMove(op.src, res));
            op2.add(new PseudoMove(res, op.des));
          }
          for (PseudoMove op_v : op2) {
            new_block.moves.add(op_v);
          }
          condtion_jmp.label2 = new_block.num;
          move_buffer.add(new_block);
        }
      }
    }
    return;
  }
}
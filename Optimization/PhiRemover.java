package Optimization;

import java.util.ArrayList;
import java.util.HashMap;
import Composer.*;
import IRSentence.*;

public class PhiRemover {
  HashMap<FromToPair, ArrayList<PresudeMove>> moves = new HashMap<>();
  Composer machine = null;

  public PhiRemover(Composer _machine) {
    machine = _machine;
  }

  public void GenerateMove() {
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
          PresudeMove move = new PresudeMove(phi.values.get(i), phi.target);
          if (!moves.containsKey(ftpair)) {
            moves.put(ftpair, new ArrayList<>());
          }
          moves.get(ftpair).add(move);
        }
      }
    }
    return;
  }

  public void BuildNewBlocks() {
    ArrayList<MoveBlock> move_buffer = new ArrayList<>();
    int func = 0;
    int now = 0;
    for (int i = 0; i < machine.generated.size(); i++) {
      for (IRCode code : machine.generated) {
        if (code instanceof IRFunc) {
          now = --func;
        }
        if (code instanceof IRLabel) {
          IRLabel label = (IRLabel) code;
          now = label.label;
        }
        if (code instanceof IRFuncend) {
          for (MoveBlock move : move_buffer) {
            machine.generated.add(i, move);
          }
          move_buffer.clear();
        }
        if (code instanceof IRjmp) {
          IRjmp jmp = (IRjmp) code;
          FromToPair ftpair = new FromToPair(now, jmp.label);
          if (moves.containsKey(ftpair)) {
            MoveBlock new_block = new MoveBlock();
            new_block.num = ++machine.label_number;
            new_block.to = ftpair.to;
            for (PresudeMove op : moves.get(ftpair)) {
              new_block.moves.add(op);
            }
            jmp.label = new_block.num;
            move_buffer.add(new_block);
          }
        }
        if (code instanceof Conditionjmp) {
          Conditionjmp condtion_jmp = (Conditionjmp) code;
          FromToPair ftpair = new FromToPair(now, condtion_jmp.label1);
          if (moves.containsKey(ftpair)) {
            MoveBlock new_block = new MoveBlock();
            new_block.num = ++machine.label_number;
            new_block.to = ftpair.to;
            for (PresudeMove op : moves.get(ftpair)) {
              new_block.moves.add(op);
            }
            condtion_jmp.label1 = new_block.num;
            move_buffer.add(new_block);
          }
          FromToPair ftpair2 = new FromToPair(now, condtion_jmp.label2);
          if (moves.containsKey(ftpair2)) {
            MoveBlock new_block = new MoveBlock();
            new_block.num = ++machine.label_number;
            new_block.to = ftpair2.to;
            for (PresudeMove op : moves.get(ftpair2)) {
              new_block.moves.add(op);
            }
            condtion_jmp.label2 = new_block.num;
            move_buffer.add(new_block);
          }
        }
      }
    }
    return;
  }
}
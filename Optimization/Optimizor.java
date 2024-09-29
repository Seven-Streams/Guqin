package Optimization;

import Composer.Composer;

public class Optimizor {
  Composer machine = null;

  public Optimizor(Composer _machine) {
    machine = _machine;
  }

  public void OptimizedCodegen() throws Exception {
    RemoveEmptyCall call_remover = new RemoveEmptyCall(machine);
    call_remover.CheckUnecessaryCalling();
    Mem2Reg M2R = new Mem2Reg(machine);
    M2R.Optim();
    LoopOptimization loop = new LoopOptimization(machine, M2R.idom);
    loop.Optim();
    M2R.clear();
    loop.clear();
    GlobalConst global_check = new GlobalConst(machine);
    global_check.Optim();
    ConstFolding const_check = new ConstFolding(machine);
    const_check.Optim();
    const_check.clear();
    Inline inliner = new Inline(machine);
    inliner.Optim(35);
    SCCP sccp = new SCCP(machine);
    sccp.Optim();
    inliner = new Inline(machine);
    inliner.Optim(35);
    sccp = new SCCP(machine);
    sccp.Optim();
    LocalCSE cse = new LocalCSE(machine);
    cse.Optim();
    PhiRemover eraser = new PhiRemover(machine);
    eraser.Remove();
    NaiveADCE ADCER = new NaiveADCE(machine);
    ADCER.Optim();
    CondUpdate condition = new CondUpdate(machine);
    condition.Optim();
    LinearScan allocator = new LinearScan(machine);
    allocator.Allocator(28);
    RemoveJmp jp = new RemoveJmp(machine);
    jp.Optim();
    Tail tail = new Tail(machine);
    tail.Optim();
    allocator.PrintBuiltIn();
    allocator.Codegen();
    return;
  }
}

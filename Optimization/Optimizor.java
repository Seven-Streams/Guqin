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
    GlobalConst global_check = new GlobalConst(machine);
    global_check.Optim();
    ConstFolding const_check = new ConstFolding(machine);
    const_check.Optim();
    SCCP sccp = new SCCP(machine);
    sccp.Optim();
    PhiRemover eraser = new PhiRemover(machine);
    eraser.Remove();
    NaiveADCE ADCER = new NaiveADCE(machine);
    ADCER.Optim();
    Inline inliner = new Inline(machine);
    inliner.Optim(50);
    CondUpdate condition = new CondUpdate(machine);
    condition.Optim();
    LinearScan allocator = new LinearScan(machine);
    allocator.Allocator(27);
    RemoveJmp jp = new RemoveJmp(machine);
    jp.Optim();
    allocator.PrintBuiltIn();
    allocator.Codegen();
  }
}

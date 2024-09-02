package ASTnodes;

import java.util.ArrayList;

import Composer.*;
import IRSentence.Conditionjmp;
import IRSentence.IRAlloc;
import IRSentence.IRBin;
import IRSentence.IRElement;
import IRSentence.IRFuncall;
import IRSentence.IRIcmp;
import IRSentence.IRLabel;
import IRSentence.IRLoad;
import IRSentence.IRStore;
import IRSentence.IRjmp;

public class NewNode extends ExprNode {
  public ASTNode value = null;
  public ASTNode dims = null;

  @Override
  public Mypair check() throws Exception {
    is_left = true;
    if ((value == null) && (dims == null)) {
      return new Mypair(type, dim);
    }
    if (value != null) {
      Mypair res = value.check();
      if (!res.type.equals(type)) {
        throw new Exception("Type Mismatch");
      }
      return res;
    }
    Mypair res = dims.check();
    return new Mypair(type, res.dim);
  }

  @Override
  public Info GenerateIR(Composer machine) {
    Info return_value = new Info();
    if ((value == null) && (dims == null)) {
      String output = new String("%reg$" + Integer.toString(++machine.tmp_time));
      return_value.reg = new String(output);
      IRFuncall irfuncall = new IRFuncall();
      irfuncall.func_name = "malloc";
      irfuncall.func_type = "ptr";
      irfuncall.target_reg = new String(output);
      irfuncall.reg.add(Integer.toString(size_of_class.get(type)));
      irfuncall.type.add("i32");
      machine.generated.add(irfuncall);
      if ((!type.equals("int")) && (!type.equals("string")) && (!type.equals("bool"))) {
        IRFuncall build = new IRFuncall();
        build.func_type = "void";
        build.func_name = type + "." + type;
        build.reg.add(output);
        build.type.add("ptr");
        machine.generated.add(build);
      }
    } else {
      if (value != null) {
        Info array_info = value.GenerateIR(machine);
        return_value.reg = new String(array_info.reg);
      } else {
        String output = new String("%reg$" + Integer.toString(++machine.tmp_time));
        ArrayList<String> dim_list = new ArrayList<>();
        DimensionNode dim_ = (DimensionNode) (dims);
        for (int i = 0; i < dim; i++) {
          if (dim_.dim_expr.containsKey(i)) {
            Info dims_info = dim_.dim_expr.get(i).GenerateIR(machine);
            dim_list.add(new String(dims_info.reg));
          } else {
            break;
          }
        }
        if (dim_list.size() == 1) {
          IRFuncall alloc_array = new IRFuncall();
          if ((type.equals("int") || type.equals("bool")) && (dim == 1)) {
            alloc_array.func_name = "int_array";
            alloc_array.func_type = "ptr";
            alloc_array.reg.add(dim_list.get(0));
            alloc_array.type.add("i32");
            alloc_array.target_reg = new String(output);
            machine.generated.add(alloc_array);
          } else {
            alloc_array.func_name = "ptr_array";
            alloc_array.func_type = "ptr";
            alloc_array.reg.add(dim_list.get(0));
            alloc_array.type.add("i32");
            alloc_array.target_reg = new String(output);
            machine.generated.add(alloc_array);
            if ((dim == 1) && (!type.equals("string"))) {
              int condition = ++machine.label_number;
              int body = ++machine.label_number;
              int end = ++machine.label_number;
              IRAlloc alloc = new IRAlloc();
              String res = "%reg$" + Integer.toString(++machine.tmp_time);
              alloc.des = new String(res);
              alloc.type = "i32";
              if (!machine.alloc.containsKey(machine.func_name)) {
                machine.alloc.put(machine.func_name, new ArrayList<>());
              }
              machine.alloc.get(machine.func_name).add(alloc);
              IRStore init = new IRStore();
              init.from = "0";
              init.name = new String(res);
              init.type = "i32";
              // i = 0
              machine.generated.add(init);
              machine.generated.add(new IRjmp(condition));
              machine.generated.add(new IRLabel(condition));
              String load_str = "%reg$" + Integer.toString(++machine.tmp_time);
              IRLoad now_value = new IRLoad();
              now_value.des = new String(load_str);
              now_value.src = res;
              now_value.type = "i32";
              machine.generated.add(now_value);
              String judge_str = "%reg$" + Integer.toString(++machine.tmp_time);
              IRIcmp judge = new IRIcmp();
              judge.target_reg = new String(judge_str);
              judge.op1 = load_str;
              judge.op2 = new String(dim_list.get(0));
              judge.symbol = "<";
              judge.type = "i32";
              machine.generated.add(judge);
              Conditionjmp cond = new Conditionjmp(body, end, judge_str);
              // i < x
              machine.generated.add(cond);
              machine.generated.add(new IRLabel(body));
              String alloc_tmp = "%reg$" + Integer.toString(++machine.tmp_time);
              IRElement get_ptr = new IRElement();
              get_ptr.output = new String(alloc_tmp);
              get_ptr.num1 = new String(load_str);
              get_ptr.src = new String(output);
              get_ptr.now_type = "ptr";
              machine.generated.add(get_ptr);
              IRFuncall new_st = new IRFuncall();
              new_st.target_reg = "%reg$" + Integer.toString(++machine.tmp_time);
              new_st.reg.add(Integer.toString(size_of_class.get(type)));
              new_st.type.add("i32");
              new_st.func_name = "malloc";
              new_st.func_type = "ptr";
              machine.generated.add(new_st);
              IRStore to_store = new IRStore();
              to_store.from = new String(new_st.target_reg);
              to_store.name = alloc_tmp;
              to_store.type = "ptr";
              machine.generated.add(to_store);
              IRFuncall funcall = new IRFuncall();
              funcall.func_name = type + "." + type;
              funcall.func_type = "void";
              funcall.reg.add(new String(new_st.target_reg));
              funcall.type.add("ptr");
              machine.generated.add(funcall);
              IRBin iter = new IRBin();
              String new_value = "%reg$" + Integer.toString(++machine.tmp_time);
              iter.op1 = new String(load_str);
              iter.op2 = "1";
              iter.symbol = "+";
              iter.type = "i32";
              iter.target_reg = new String(new_value);
              machine.generated.add(iter);
              IRStore update_i = new IRStore();
              update_i.from = new String(new_value);
              update_i.name = new String(res);
              update_i.type = "i32";
              machine.generated.add(update_i);
              IRjmp back_to_cond = new IRjmp(condition);
              machine.generated.add(back_to_cond);
              machine.generated.add(new IRLabel(end));
            }
          }
        } else {
          IRFuncall alloc_array = new IRFuncall();
          alloc_array.func_name = "ptr_array";
          alloc_array.func_type = "ptr";
          alloc_array.reg.add(dim_list.get(0));
          alloc_array.type.add("i32");
          alloc_array.target_reg = new String(output);
          machine.generated.add(alloc_array);
          // output points at the beginning of the first layer.
          BuildInner(0, dim_list, machine, output);
        }
        return_value.reg = new String(output);
      }
    }
    return return_value;
  }

  void BuildInner(int x, ArrayList<String> config, Composer machine, String beginning) {
    if ((x == (config.size() - 1)) && (x != (dim - 1))) {
      return;
    }
    int condition = ++machine.label_number;
    int body = ++machine.label_number;
    int end = ++machine.label_number;
    IRAlloc alloc = new IRAlloc();
    String res = "%reg$" + Integer.toString(++machine.tmp_time);
    alloc.des = new String(res);
    alloc.type = "i32";
    if (!machine.alloc.containsKey(machine.func_name)) {
      machine.alloc.put(machine.func_name, new ArrayList<>());
    }
    machine.alloc.get(machine.func_name).add(alloc);
    IRStore init = new IRStore();
    init.from = "0";
    init.name = new String(res);
    init.type = "i32";
    // i = 0
    machine.generated.add(init);
    machine.generated.add(new IRjmp(condition));
    machine.generated.add(new IRLabel(condition));
    String load_str = "%reg$" + Integer.toString(++machine.tmp_time);
    IRLoad now_value = new IRLoad();
    now_value.des = new String(load_str);
    now_value.src = res;
    now_value.type = "i32";
    machine.generated.add(now_value);
    String judge_str = "%reg$" + Integer.toString(++machine.tmp_time);
    IRIcmp judge = new IRIcmp();
    judge.target_reg = new String(judge_str);
    judge.op1 = load_str;
    judge.op2 = new String(config.get(x));
    judge.symbol = "<";
    judge.type = "i32";
    machine.generated.add(judge);
    Conditionjmp cond = new Conditionjmp(body, end, judge_str);
    // i < x
    machine.generated.add(cond);
    machine.generated.add(new IRLabel(body));
    String alloc_tmp = "%reg$" + Integer.toString(++machine.tmp_time);
    IRElement get_ptr = new IRElement();
    get_ptr.output = new String(alloc_tmp);
    get_ptr.num1 = new String(load_str);
    get_ptr.src = new String(beginning);
    get_ptr.now_type = "ptr";
    machine.generated.add(get_ptr);
    if (x == (dim - 1)) {
      String real_tmp = "%reg$" + Integer.toString(++machine.tmp_time);
      IRFuncall irfuncall = new IRFuncall();
      irfuncall.func_name = "malloc";
      irfuncall.func_type = "ptr";
      irfuncall.target_reg = new String(real_tmp);
      irfuncall.reg.add(Integer.toString(size_of_class.get(type)));
      irfuncall.type.add("i32");
      machine.generated.add(irfuncall);
      IRFuncall funcall = new IRFuncall();
      funcall.func_name = type + "." + type;
      funcall.func_type = "void";
      funcall.reg.add(new String(real_tmp));
      funcall.type.add("ptr");
      machine.generated.add(funcall);
      IRStore to_store = new IRStore();
      to_store.from = new String(real_tmp);
      to_store.name = alloc_tmp;
      to_store.type = "ptr";
      machine.generated.add(to_store);
    } else {
      if (x == (dim - 2)) {
        if (type.equals("int") || type.equals("bool")) {
          String real_tmp = "%reg$" + Integer.toString(++machine.tmp_time);
          IRFuncall funcall = new IRFuncall();
          funcall.func_name = "int_array";
          funcall.func_type = "ptr";
          funcall.reg.add(config.get(x + 1));
          funcall.type.add("i32");
          funcall.target_reg = new String(real_tmp);
          machine.generated.add(funcall);
          IRStore to_store = new IRStore();
          to_store.from = new String(real_tmp);
          to_store.name = alloc_tmp;
          to_store.type = "ptr";
          machine.generated.add(to_store);
        } else {
          String real_tmp = "%reg$" + Integer.toString(++machine.tmp_time);
          IRFuncall funcall = new IRFuncall();
          funcall.func_name = "ptr_array";
          funcall.func_type = "ptr";
          funcall.reg.add(config.get(x + 1));
          funcall.type.add("i32");
          funcall.target_reg = new String(real_tmp);
          machine.generated.add(funcall);
          IRStore to_store = new IRStore();
          to_store.from = new String(real_tmp);
          to_store.name = alloc_tmp;
          to_store.type = "ptr";
          machine.generated.add(to_store);
          BuildInner(x + 1, config, machine, real_tmp);
        }
      } else {
        String real_tmp = "%reg$" + Integer.toString(++machine.tmp_time);
        IRFuncall funcall = new IRFuncall();
        funcall.func_name = "ptr_array";
        funcall.func_type = "ptr";
        funcall.reg.add(config.get(x + 1));
        funcall.type.add("i32");
        funcall.target_reg = new String(real_tmp);
        machine.generated.add(funcall);
        IRStore to_store = new IRStore();
        to_store.from = new String(real_tmp);
        to_store.name = alloc_tmp;
        to_store.type = "ptr";
        machine.generated.add(to_store);
        BuildInner(x + 1, config, machine, real_tmp);
      }
    }
    // a[i] = new [...], and calling a recursive.
    IRBin iter = new IRBin();
    String new_value = "%reg$" + Integer.toString(++machine.tmp_time);
    iter.op1 = new String(load_str);
    iter.op2 = "1";
    iter.symbol = "+";
    iter.type = "i32";
    iter.target_reg = new String(new_value);
    machine.generated.add(iter);
    IRStore update_i = new IRStore();
    update_i.from = new String(new_value);
    update_i.name = new String(res);
    update_i.type = "i32";
    machine.generated.add(update_i);
    IRjmp back_to_cond = new IRjmp(condition);
    machine.generated.add(back_to_cond);
    machine.generated.add(new IRLabel(end));
    return;
  }
}

package IRSentence;

import java.util.HashMap;

public class IRChararray extends IRCode {
  public String value = null;
  public String reg = null;
  public int size = 0;
  // Watch out! This size contains a /00.

  @Override
  public void CodePrint() {
    System.out.print(reg + " = constant [" + Integer.toString(size) + " x i8] c\"");
    for (int i = 0; i < value.length(); i++) {
      if (value.charAt(i) != '\\') {
        System.out.print(value.charAt(i));
      } else {
        System.out.print('\\');
        i++;
        if (value.charAt(i) == '\\') {
          System.out.print('\\');
        }
        if (value.charAt(i) == '\"') {
          System.out.print("22");
        }
        if (value.charAt(i) == 'n') {
          System.out.print("0A");
        }
      }
    }
    System.out.println("\\00\"");
    return;
  }

  @Override
  public void Codegen() {
    is_global.put(reg, true);
    System.out.println(reg.substring(1) + ".data:");// This part is to avoid "@".
    System.out.print(".asciz \"" + value);
    System.out.println("\"");
    System.out.println(".align 4");
    System.out.println(reg.substring(1) + ":");
    System.out.println(".word 0");
    return;
  }

  public void Init() {
    System.out.println("lui a0, %hi(" + reg.substring(1) + ".data)");
    System.out.println("addi a0, a0, " + "%lo(" + reg.substring(1) + ".data)");
    System.out.println("lui a1, %hi(" + reg.substring(1) + ")");
    System.out.println("addi a1, a1, " + "%lo(" + reg.substring(1) + ")");
    System.out.println("sw a0, 0(a1)");
    return;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    Codegen();
  }

  @Override
  public void UseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    is_global.put(reg, null);
  }
}

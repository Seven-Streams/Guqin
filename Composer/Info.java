package Composer;
import java.util.ArrayList;

import IRSentence.IRCode;


//This func is to pass informations.
public class Info {
  public static ArrayList<LoopInfo> loop = new ArrayList<>();
  public static ArrayList<IRCode> Init = new ArrayList<>();
  public String reg = null;
  public boolean is_const = false;
}
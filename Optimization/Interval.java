package Optimization;

import java.util.Comparator;

public class Interval {
  public int start = 0;
  public int end = 0;
  public String name = null;

  public Interval() {
  }

  public Interval(int _start, int _end, String _name) {
    start = _start;
    end = _end;
    name = new String(_name);
  }

  class IntervalComparator implements Comparator<Interval> {
    @Override
    public int compare(Interval lhs, Interval rhs) {
      if (lhs.start != rhs.start) {
        return (lhs.start > rhs.start) ? 1 : -1;
      }
      if (lhs.end != rhs.end) {
        return (lhs.end < rhs.end) ? 1 : -1;
      }
      return lhs.name.compareTo(rhs.name);
    }
  }
}

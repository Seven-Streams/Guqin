package nodes;
public class Mypair {
	public String type = null;
	public Integer dim = 0;	
	public Mypair(String t, int di) {
		type = new String(t);
		dim = di;
	}
	public Mypair() {
		type = "";
		dim = 0;
	}
}
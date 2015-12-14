package csp_assignmnet1;

public class Pair {
	protected int var;
	protected int val;

	public Pair(int var, int val) {
		this.var = var;
		this.val = val;
	}

	@Override
	public String toString() {
		String ans = "(" + this.var + "," + this.val + ")";
		return ans;

	}
}

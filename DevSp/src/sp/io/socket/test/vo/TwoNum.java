package sp.io.socket.test.vo;

public class TwoNum implements Comparable<TwoNum> {
	private int first;
	private int second;
	
	public TwoNum(int first, int second) {
		this.first = first;
		this.second = second;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public String toPacketString() {
		return first + ","+second;
	}
	
	@Override
	public int compareTo(TwoNum o) {
		int ret = this.second - o.second;
		if(ret == 0) {
			return ret;
		}
		return this.first - o.first;
	}
	
	@Override
	public String toString() {
		return "TwoNum [first=" + first + ", second=" + second + "]";
	}
}

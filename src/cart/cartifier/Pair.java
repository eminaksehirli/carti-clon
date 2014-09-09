package cart.cartifier;

public class Pair implements Comparable<Pair>
{
	public double v;
	public int ix;

	public Pair(double v, int ix)
	{
		this.v = v;
		this.ix = ix;
	}

	@Override
	public String toString()
	{
		return "[" + ix + ":" + v + "]";
	}

	@Override
	public int compareTo(Pair o)
	{
		return Double.compare(v, o.v);
	}
}
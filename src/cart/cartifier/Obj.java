package cart.cartifier;

import java.util.Arrays;

class Obj
{
	int id;
	double[] v;

	public Obj(int id, double[] v)
	{
		this.id = id;
		this.v = v;
	}

	@Override
	public String toString()
	{
		return "Obj [" + id + "," + Arrays.toString(v) + "]";
	}
}

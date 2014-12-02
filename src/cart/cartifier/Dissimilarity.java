package cart.cartifier;

import java.util.Arrays;
import java.util.Collection;

public abstract class Dissimilarity
{
	protected int[] dims;

	public Dissimilarity(int[] dims)
	{
		this.dims = dims;
	}

	public Dissimilarity(Collection<Integer> dims)
	{
		this.dims = new int[dims.size()];
		int ix = 0;
		for (int dim : dims)
		{
			this.dims[ix++] = dim;
		}
	}

	public abstract double between(double[] obj_1, double[] obj_2);

	public double between(Obj obj_1, Obj obj_2)
	{
		return between(obj_1.v, obj_2.v);
	}

	protected abstract String getName();

	@Override
	public String toString()
	{
		return getName() + ": " + Arrays.toString(dims);
	}

	public int[] getDims()
	{
		return dims;
	}
}

package cart.cartifier;

import static java.util.Collections.singletonList;

public class OneDimDissimilarity extends Dissimilarity
{
	public OneDimDissimilarity(int dim)
	{
		super(singletonList(dim));
	}

	public int getDim()
	{
		return dims[0];
	}

	@Override
	public double between(double[] object1, double[] object2)
	{
		double distance = 0;

		for (int d : dims)
		{
			distance += Math.abs(object1[d] - object2[d]);
		}

		return distance;
	}

	@Override
	public String getName()
	{
		return "1 dimensional";
	}
}

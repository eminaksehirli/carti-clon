package cart.cartifier;

import java.util.Collection;

public class EuclidianDistance extends Dissimilarity
{
	public EuclidianDistance(int[] dims)
	{
		super(dims);
	}

	public EuclidianDistance(Collection<Integer> dims)
	{
		super(dims);
	}

	@Override
	public double between(double[] object1, double[] object2)
	{
		double sum = 0;

		for (int d : dims)
		{
			sum += Math.pow(object1[d] - object2[d], 2);
		}
		double distance = Math.sqrt(sum);

		return distance;
	}

	@Override
	public String getName()
	{
		return "Euclidian";
	}
}

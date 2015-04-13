package cart.cartifier;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;

public class OneDimDissimilarity extends Dissimilarity
{
	public static List<Dissimilarity> forEach(int numOfDims)
	{
		List<Dissimilarity> measures = new ArrayList<>(numOfDims);
		for (int i = 0; i < numOfDims; i++)
		{
			measures.add(new OneDimDissimilarity(i));
		}
		return measures;
	}

	@SuppressWarnings("boxing")
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

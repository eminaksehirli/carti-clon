package cart.cartifier;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import mime.plain.PlainItemDB;

import com.google.common.collect.Maps;

public class CartifierInMemory
{

	public static final double Sampling_Ratio = 1;
	protected List<double[]> db;
	public PlainItemDB itemDb;
	private Random r;

	public CartifierInMemory(List<double[]> db)
	{
		this.db = db;
	}

	public void cartifyNumeric(int[] dimensions, int k)
	{
		r = new Random();

		int numOfItems = db.size();
		itemDb = new PlainItemDB();

		if (dimensions.length == 1)
		{
			sameDataRandomizer(dimensions, numOfItems, k);
		} else
		{
			for (int itemIx = 0; itemIx < numOfItems; itemIx++)
			{
				// sampling code
				if (r.nextDouble() > Sampling_Ratio)
				{
					continue;
				}

				double[] object_i = db.get(itemIx);
				Pair[] cart = new Pair[numOfItems];
				for (int j = 0; j < numOfItems; j++)
				{
					double[] object_j = db.get(j);
					double distance;
					double sum = 0;
					for (int d : dimensions)
					{
						sum += Math.pow(object_i[d] - object_j[d], 2);
					}
					distance = Math.sqrt(sum);
					cart[j] = new Pair(distance, j);
				}

				Arrays.sort(cart);

				for (int neighbor = 0; neighbor < Math.min(cart.length, k); neighbor++)
				{
					itemDb.get(cart[neighbor].ix).getTIDs().set(itemIx);
				}
			}
		}
	}

	private void sameDataRandomizer(int[] dimensions, int numOfItems, int k)
	{
		final int d = dimensions[0];

		List<Integer> items = new ArrayList<>(db.size());
		TreeMap<Double, List<Pair>> sortedProj = Maps.newTreeMap();
		for (int i = 0; i < numOfItems; i++)
		{
			final double val = db.get(i)[d];
			// we only care about numeric values
			if (Double.isNaN(val))
			{
				continue;
			}

			Pair pair = new Pair(val, i);
			items.add(i); // this is needed for methods that doesn't cartify all items
			List<Pair> range = sortedProj.get(val);
			if (range == null)
			{
				range = newArrayList();
				sortedProj.put(pair.v, range);
			}
			range.add(pair);
		}

		List<Double> projectList = newArrayList(sortedProj.keySet());

		for (int itemIx : items)
		{
			// sampling code
			if (r.nextDouble() > Sampling_Ratio)
			{
				continue;
			}
			double location = db.get(itemIx)[d];

			List<Pair> equals = sortedProj.get(Double.valueOf(location));

			int cartSize = 0;
			cartSize = addAll(equals, itemIx, cartSize);

			int efK = k; // effective K

			int orderInProj = projectList.indexOf(Double.valueOf(location));

			// // This block is to decrease the artifacts on the borders.
			// if (orderInProj < k / 2)
			// {
			// efK = k / 2 + orderInProj;
			// } else if (projectList.size() - orderInProj < k / 2)
			// {
			// efK = k / 2 + projectList.size() - orderInProj;
			// }

			int leftIx = orderInProj - 1;
			int rightIx = orderInProj + 1;
			while (cartSize < efK && (leftIx >= 0 || rightIx < projectList.size()))
			{
				double leftDist = MAX_VALUE;
				double rightDist = MAX_VALUE;
				if (leftIx >= 0)
				{
					leftDist = abs(location - projectList.get(leftIx));
				}

				if (rightIx < projectList.size())
				{
					rightDist = abs(location - projectList.get(rightIx));
				}

				if (leftDist < rightDist)
				{
					cartSize = addAll(sortedProj.get(projectList.get(leftIx)), itemIx,
							cartSize);
					leftIx--;
				} else if (leftDist > rightDist)
				{
					cartSize = addAll(sortedProj.get(projectList.get(rightIx)), itemIx,
							cartSize);
					rightIx++;
				} else
				{
					List<Pair> toAdd = newArrayList(sortedProj.get(projectList.get(rightIx)));
					toAdd.addAll(sortedProj.get(projectList.get(leftIx)));
					cartSize = addAll(toAdd, itemIx, cartSize);
					rightIx++;
					leftIx--;
				}
			}
		}
	}

	private int addAll(List<Pair> equals, int itemIx, int cartSize)
	{
		for (Pair neighbor : equals)
		{
			itemDb.get(neighbor.ix).getTIDs().set(itemIx);
		}
		return cartSize + equals.size();
	}

	private static void addAll(List<Pair> equals, ArrayList<Pair> cart,
			double distance)
	{
		if (equals.size() > 1)
		{
			Collections.shuffle(equals);
			for (Pair i : equals)
			{
				cart.add(new Pair(distance, i.ix));
			}
		} else
		{
			cart.add(new Pair(distance, equals.get(0).ix));
		}
	}
}

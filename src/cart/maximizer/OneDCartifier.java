package cart.maximizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import cart.cartifier.Pair;

public class OneDCartifier
{
	public static int[] findCartStarts(final double[] dim, int k, boolean extendDim)
	{
		double[] sortedDim = Arrays.copyOf(dim, dim.length);
		Arrays.sort(sortedDim);

		int exStart = 0;
		int exEnd = 0;
		if (extendDim)
		{
			sortedDim = OneDCartifier.expandTheDim(k, sortedDim);
			exStart = k / 2;
			exEnd = k - exStart;
		}
		int[] cartStarts = new int[sortedDim.length];
		int cartStart = 0;

		cartStarts[0] = cartStart;

		// since we add a #exStart objects at the beginning we ofset the indices by
		// exStart
		for (int objIx = 0; objIx < sortedDim.length; objIx++)
		{
			final double obj = sortedDim[objIx];
			int cartEnd = cartStart + k; // cartEnd is exclusive

			while (cartEnd < sortedDim.length
					&& OneDCartifier.dist(obj, sortedDim[cartEnd]) < OneDCartifier.dist(
							obj, sortedDim[cartStart]))
			{
				cartStart++;
				cartEnd++;
			}

			// cartStarts[objIx - exStart] = cartStart;
			cartStarts[objIx] = cartStart;
		}
		return Arrays.copyOfRange(cartStarts, exStart, cartStarts.length - exEnd);
	}

	public static double[] expandTheDim(int k, double[] sortedDim)
	{
		double[] expandedDim = new double[sortedDim.length + k];
		final int expand = k / 2;
		System.arraycopy(sortedDim, 0, expandedDim, expand, sortedDim.length);
		double avg = 0;
		for (int i = 0; i < sortedDim.length - 1; i++)
		{
			avg += sortedDim[i + 1] - sortedDim[i];
		}
		avg /= sortedDim.length - 1;
		for (int i = 0; i < expand; i++)
		{
			expandedDim[i] = sortedDim[0] - ((expand - i) * avg);
		}
		for (int i = sortedDim.length + expand; i < expandedDim.length; i++)
		{
			expandedDim[i] = sortedDim[sortedDim.length - 1]
					+ (i - sortedDim.length - expand + 1) * avg;
		}

		// System.out.println("Dimension extended by: " + expand + ", "
		// + sortedDim.length + " => " + expandedDim.length);
		return expandedDim;
	}

	public static double[][] transpose(ArrayList<double[]> data)
	{
		double[][] dims = new double[data.get(0).length][data.size()];

		for (int dimIx = 0; dimIx < dims.length; dimIx++)
		{
			int rowIx = 0;
			for (double[] row : data)
			{
				dims[dimIx][rowIx] = row[dimIx];
				rowIx++;
			}
		}
		return dims;
	}

	public static ArrayList<double[]> readData(String pathname)
			throws FileNotFoundException
	{
		Scanner sc = new Scanner(new File(pathname));

		ArrayList<double[]> data = new ArrayList<>();

		while (sc.hasNextLine())
		{
			String line = sc.nextLine();
			String[] arr = line.split(" ");

			double[] values = new double[arr.length];
			for (int i = 0; i < arr.length; i++)
			{
				values[i] = Double.parseDouble(arr[i]);
			}
			data.add(values);
		}
		sc.close();
		return data;
	}

	public static Pair[][] toPairs(List<double[]> objects)
	{
		Pair[][] origData = new Pair[objects.size()][];
		int objIx = 0;
		for (double[] obj : objects)
		{
			Pair[] pairObj = new Pair[obj.length];
			for (int i = 0; i < obj.length; i++)
			{
				pairObj[i] = new Pair(obj[i], objIx);
			}
			origData[objIx] = pairObj;
			objIx++;
		}
		return origData;
	}

	public static double dist(double d, double e)
	{
		return Math.abs(d - e);
	}
}

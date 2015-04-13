package be.uantwerpen.adrem.cart.maximizer;

import static java.util.Collections.singletonList;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cart.cartifier.Pair;
import cart.io.InputFile;

import com.google.common.collect.HashMultimap;

public abstract class MaximalMinerCombiner
{
	protected static int skipCount;
	protected int numOfDims;
	protected Pair[][] origData;
	protected Pair[][] orderedDims;
	protected int[][] ids2Orders;
	private Item[][] allItems;
	protected HashMultimap<Integer, int[]> allMineds;
	protected int minLen;
	private double[][] dims;
	private FreqCollector freqCollector;
	private List<Integer> theAllDims;

	public MaximalMinerCombiner(InputFile inputFile)
	{
		try
		{
			ArrayList<double[]> data = inputFile.getData();
			dims = OneDCartifier.transpose(data);
			// System.out.println("Dims data read and transposed");
			origData = OneDCartifier.toPairs(data);
			// System.out.println("Data pairs are created.");
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}

		numOfDims = dims.length;

		orderedDims = new Pair[dims.length][];
		ids2Orders = new int[dims.length][];
		for (int dimIx = 0; dimIx < numOfDims; dimIx++)
		{
			orderedDims[dimIx] = getOrd2Id(origData, dimIx);
			ids2Orders[dimIx] = getId2Ord(orderedDims[dimIx]);
		}
	}

	public List<Freq> mineFor(int k, int minLen)
	{
		FreqCollection freqCollection = new FreqCollection();
		mineFor(k, minLen, freqCollection);
		return freqCollection.freqs;
	}

	public void mineFor(int k, int minLen, FreqCollector a)
	{
		freqCollector = a;
		this.minLen = minLen;

		convertToItems(k);

		CartiFiner miner = new CartiMaximizer(minLen);
		Map<Integer, Map<Integer, Integer>> mineds = miner.mineCarts(dims, k);
		allMineds = mineds2Ids(mineds);

		for (Integer startDimIx : allMineds.keySet())
		{
			List<Integer> dimsToCheck = getAllDims();
			dimsToCheck.remove(startDimIx);

			List<Integer> freqDims = singletonList(startDimIx);

			Collection<int[]> dimMineds = allMineds.get(startDimIx);

			for (int[] aMined : dimMineds)
			{
				foundFreq(aMined, freqDims);
				checkForFreq(dimsToCheck, freqDims, aMined);
			}
		}
	}

	public List<Freq> mineFor(int[] aMined, int k, int minLen, Integer startDimIx)
	{
		final FreqCollection freqCollection = new FreqCollection();
		freqCollector = freqCollection;
		this.minLen = minLen;
		convertToItems(k);
		System.out.println("Items are created!");

		List<Integer> dimsToCheck = getAllDims();
		dimsToCheck.remove(startDimIx);
		List<Integer> freqDims = singletonList(startDimIx);
		checkForFreq(dimsToCheck, freqDims, aMined);
		return freqCollection.freqs;
	}

	private List<Integer> getAllDims()
	{
		if (theAllDims == null)
		{
			List<Integer> allDims = new ArrayList<>(numOfDims);
			for (int i = 0; i < numOfDims; i++)
			{
				allDims.add(i);
			}
			theAllDims = allDims;
		}
		return new ArrayList<Integer>(theAllDims);
	}

	protected void foundFreq(int[] freqSet, List<Integer> freqDims)
	{
		if (freqSet.length < minLen)
		{
			skipCount++;
			System.err.println("[" + this.getClass().getName()
					+ "] Small itemset found! This is an error: "
					+ Arrays.toString(freqSet));
			return;
		}

		freqCollector.foundFreq(new Freq(freqSet, freqDims));
	}

	protected Item[] orderTheItems(int[] aMined, int dimIx)
	{
		int[] ordered = new int[aMined.length];
		int itemIx = 0;
		for (int item : aMined)
		{
			ordered[itemIx++] = ids2Orders[dimIx][item];
		}
		Arrays.sort(ordered);

		Item[] items = new Item[ordered.length];
		for (int i = 0; i < items.length; i++)
		{
			items[i] = allItems[dimIx][orderedDims[dimIx][ordered[i]].ix];
		}
		return items;
	}

	public static Pair[] getOrd2Id(Pair[][] origData, int dimIx)
	{
		Pair[] dimArray = new Pair[origData.length];
		for (int i = 0; i < origData.length; i++)
		{
			dimArray[i] = origData[i][dimIx];
		}
		Arrays.sort(dimArray);
		return dimArray;
	}

	public static int[] getId2Ord(Pair[] orderedDim)
	{
		int[] dimArray = new int[orderedDim.length];

		for (int i = 0; i < orderedDim.length; i++)
		{
			dimArray[orderedDim[i].ix] = i;
		}
		return dimArray;
	}

	protected void convertToItems(int k)
	{
		Item[][] nAllItems = new Item[numOfDims][];
		for (int dimIx = 0; dimIx < numOfDims; dimIx++)
		{
			Item[] dimItems = new Item[dims[dimIx].length];
			for (int i = 0; i < dimItems.length; i++)
			{
				dimItems[i] = new Item(i);
			}
			int[] cartStarts = OneDCartifier.findCartStarts(dims[dimIx], k, false);
			for (int order = 0; order < cartStarts.length; order++)
			{
				for (int itemIx = cartStarts[order]; itemIx < cartStarts[order] + k; itemIx++)
				{
					dimItems[orderedDims[dimIx][itemIx].ix].addTid(order);
				}
			}
			nAllItems[dimIx] = dimItems;
		}
		allItems = nAllItems;
	}

	protected HashMultimap<Integer, int[]> mineds2Ids(
			Map<Integer, Map<Integer, Integer>> mineds)
	{
		HashMultimap<Integer, int[]> allFIs = HashMultimap.create();

		for (Entry<Integer, Map<Integer, Integer>> entry : mineds.entrySet())
		{
			Integer dimIx = entry.getKey();
			Map<Integer, Integer> dimMineds = entry.getValue();

			for (Entry<Integer, Integer> dimLined : dimMineds.entrySet())
			{
				final int start = dimLined.getValue();
				final int end = dimLined.getKey();
				int[] fis = new int[end - start];
				for (int i = start; i < end; i++)
				{
					fis[i - start] = orderedDims[dimIx][i].ix;
				}
				allFIs.put(dimIx, fis);
			}
		}
		return allFIs;
	}

	protected abstract void checkForFreq(List<Integer> dimsToCheck,
			List<Integer> freqDims, int[] aMined);

	protected static class Result
	{
		int a, s, e;

		Result(int a, int s, int e)
		{
			this.a = a;
			this.s = s;
			this.e = e;
		}
	}

	protected static class Item
	{
		int id;
		int txS = Integer.MAX_VALUE, txE;

		public Item(int id)
		{
			this.id = id;
		}

		public void addTid(int tid)
		{
			if (tid < txS)
			{
				txS = tid;
			}
			if (tid > txE)
			{
				txE = tid;
			}
		}

		@Override
		public String toString()
		{
			return "[" + id + ", " + txS + "=>" + txE + "]";
		}
	}

	public interface FreqCollector
	{
		void foundFreq(Freq freq);
	}

	public static class FreqCollection implements FreqCollector
	{
		List<Freq> freqs;

		public FreqCollection()
		{
			this.freqs = new ArrayList<>();
		}

		@Override
		public void foundFreq(Freq freq)
		{
			freqs.add(freq);
		}
	}
}

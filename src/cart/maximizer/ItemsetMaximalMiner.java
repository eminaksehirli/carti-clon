package cart.maximizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class ItemsetMaximalMiner extends MaximalMinerCombiner
{

	private static int area(Item[] dimItems, int start, int end)
	{
		return (dimItems[start].txE - dimItems[end].txS) * (end - start);
	}

	protected abstract int getMinSup();

	@Override
	protected void checkForFreq(List<Integer> dimsToCheck,
			List<Integer> freqDims, Collection<Integer> aMined)
	{
		for (int dimIx : dimsToCheck)
		{
			Item[] items = orderTheItems(aMined, dimIx);

			Map<Integer, Integer> localFreqs = findAllMaxes(items);

			List<List<Integer>> freqSets = new ArrayList<>();
			for (Entry<Integer, Integer> e : localFreqs.entrySet())
			{
				List<Integer> freqSet = new ArrayList<>();
				for (int freqIx = e.getValue(); freqIx < e.getKey(); freqIx++)
				{
					freqSet.add(items[freqIx].id);
				}
				freqSets.add(freqSet);
			}

			if (freqSets.size() > 0)
			{
				List<Integer> newFreqDims = new ArrayList<>(freqDims);
				newFreqDims.add(dimIx);

				for (List<Integer> freqSet : freqSets)
				{
					foundFreq(freqSet, newFreqDims);
					List<Integer> newDimsToCheck = new ArrayList<>(dimsToCheck);
					newDimsToCheck.remove(Integer.valueOf(dimIx));
					checkForFreq(newDimsToCheck, newFreqDims, freqSet);
				}
			}
		}
	}

	protected Map<Integer, Integer> findAllMaxes(Item[] items)
	{
		int minSup = getMinSup();
		Map<Integer, Integer> maxes = new HashMap<>();
		int start = 0;
		int end = start + minLen;

		while (end < items.length - 1)
		{
			while ((items[start].txE - items[end + 1].txS > minSup)
					&& end < items.length - 2)
			{
				end++;
			}

			if (items[start].txE - items[end].txS > minSup)
			{
				maxes.put(end, start);
				end++;
			}

			while (items[start + 1].txE == items[start].txE
					&& start < items.length - minLen)
			{
				start++;
			}

			start++;
			if (end - start < minLen)
			{
				end = start + minLen;
			}
		}
		return maxes;
	}

	public ItemsetMaximalMiner(String pathname)
	{
		super(pathname);
	}

}
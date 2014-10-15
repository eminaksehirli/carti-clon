package cart.maximizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DimBasedMaximalMiner extends MaximalMinerCombiner
{
	public DimBasedMaximalMiner(String pathname)
	{
		super(pathname);
	}

	public static void main(String[] args)
	{
		final String dir = "/home/memin/research/data/synth";
		String pathname = dir + "/6c10d/6c10d.mime";

		DimBasedMaximalMiner cam = new DimBasedMaximalMiner(pathname);
		cam.mineFor(200, 100);
	}

	@Override
	protected void checkForFreq(List<Integer> dimsToCheck,
			List<Integer> freqDims, int[] aMined)
	{
//		for (Integer dimIx : dimsToCheck)
//		{
//			//FIXME: convert array to sets
//			Set<Set<Integer>> dimMineds = allMineds.get(dimIx);
//
//			for (Set<Integer> dimMined : dimMineds)
//			{
//				Set<Integer> intersect = new HashSet<>(aMined);
//				intersect.retainAll(dimMined);
//
//				if (intersect.size() > minLen)
//				{
//					List<Integer> newFreqDims = new ArrayList<>(freqDims);
//					newFreqDims.add(dimIx);
//
//					foundFreq(intersect, newFreqDims);
//					List<Integer> newDimsToCheck = new ArrayList<>(dimsToCheck);
//					newDimsToCheck.remove(dimIx);
//					checkForFreq(newDimsToCheck, newFreqDims, intersect);
//				}
//			}
//		}
	}
}

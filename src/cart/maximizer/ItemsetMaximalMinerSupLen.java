package cart.maximizer;

import java.util.List;

public class ItemsetMaximalMinerSupLen extends ItemsetMaximalMiner
{
	public ItemsetMaximalMinerSupLen(String pathname)
	{
		super(pathname);
	}

	public static void main(String[] args)
	{
		final String dir = "/home/memin/research/data/synth";
		// String pathname = dir + "/6c10d/6c10d.mime";
		String pathname = dir + "/10c24d/10c24d.mime";

		ItemsetMaximalMinerSupLen cam = new ItemsetMaximalMinerSupLen(pathname);
		List<Freq> freqs = cam.mineFor(187, 100);

		for (Freq freq : freqs)
		{
			System.out.println(freq.freqSet.size() + "\t" + freq.freqDims + "\t"
					+ freq.freqSet);
		}
	}

	@Override
	protected int getMinSup()
	{
		return minLen;
	}
}

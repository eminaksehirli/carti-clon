package cart.maximizer;

import java.util.Collection;
import java.util.List;

public class Freq
{
	public Collection<Integer> freqSet;
	public List<Integer> freqDims;
	public final int id;
	static int autoID = 0;

	public Freq(Collection<Integer> freqSet, List<Integer> freqDims)
	{
		this.freqSet = freqSet;
		this.freqDims = freqDims;
		id = autoID++;
	}

	@Override
	public String toString()
	{
		return freqSet.size() + " [" + freqDims.toString() + "] "
				+ freqSet.toString();
	}
}
package be.uantwerpen.adrem.cart.maximizer;

import java.util.List;

public class Freq
{
	public int[] freqSet;
	public List<Integer> freqDims;
	public final int id;
	static int autoID = 0;

	public Freq(int[] freqSet, List<Integer> freqDims)
	{
		this.freqSet = freqSet;
		this.freqDims = freqDims;
		id = autoID++;
	}
}
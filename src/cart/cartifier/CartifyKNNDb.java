package cart.cartifier;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cart.io.InputFile;

public class CartifyKNNDb extends CartifyDb
{
	public static CartifyKNNDb perDim(InputFile inputFile, int k)
	{
		List<Dissimilarity> measures = new ArrayList<>();
		try
		{
			List<double[]> data = inputFile.getData();
			int numOfDims = data.get(0).length;
			for (int i = 0; i < numOfDims; i++)
			{
				measures.add(new OneDimDissimilarity(i));
			}
			return new CartifyKNNDb(inputFile, measures, k);
		} catch (FileNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}

	private int k;

	public CartifyKNNDb(InputFile inputFile, List<Dissimilarity> measures, int k)
	{
		super(inputFile, measures);
		this.k = k;
	}

	public void setK(int k)
	{
		this.k = k;
		createCarts();
	}

	@Override
	protected Cartifier getCartifier(Dissimilarity measure)
	{
		return new KNNCartifier(db, measure, k);
	}
}

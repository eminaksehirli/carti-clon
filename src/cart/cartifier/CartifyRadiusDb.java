package cart.cartifier;

import java.util.List;

import cart.io.InputFile;

public class CartifyRadiusDb extends CartifyDb
{
	private double eps;

	public CartifyRadiusDb(InputFile inputFile, List<Dissimilarity> measures,
			double eps)
	{
		super(inputFile, measures);
		this.eps = eps;
	}

	public void setEps(double eps)
	{
		this.eps = eps;
		createCarts();
	}

	@Override
	protected Cartifier getCartifier(Dissimilarity measure)
	{
		return new RadiusCartifier(db, measure, eps);
	}
}

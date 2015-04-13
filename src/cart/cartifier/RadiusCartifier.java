package cart.cartifier;

import java.util.ArrayList;

public class RadiusCartifier extends ContinuousCartifier
{
	private double eps;

	public RadiusCartifier(Obj[] db, Dissimilarity dist, double eps)
	{
		super(db, dist);
		this.eps = eps;
	}

	@Override
	protected int[][] findContinuousCarts(Obj[] objs)
	{
		final int len = objs.length;
		int[][] carts = new int[len][2];

		for (int objIx = 0; objIx < len; objIx++)
		{
			int cs = objIx;
			int ce = objIx + 1;
			while (cs > 0 && dist.between(objs[objIx], objs[cs - 1]) <= eps)
			{
				cs--;
			}
			while (ce + 1 < len && dist.between(objs[objIx], objs[ce + 1]) <= eps)
			{
				ce++;
			}
			carts[objIx][0] = cs;
			carts[objIx][1] = ce;
		}
		return carts;
	}

	@Override
	protected Obj[] findMultiDCart(Pair[] distances)
	{
		ArrayList<Obj> cart = new ArrayList<Obj>();

		for (Pair obj : distances)
		{
			if (obj.v < eps)
			{
				cart.add(db[obj.ix]);
			}
		}
		return cart.toArray(new Obj[0]);
	}
}

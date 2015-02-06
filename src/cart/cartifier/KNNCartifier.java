package cart.cartifier;

import java.util.Arrays;

public class KNNCartifier extends Cartifier
{
	private int k;

	public KNNCartifier(Obj[] db, Dissimilarity dist, int k)
	{
		super(db, dist);
		this.k = k;
	}

	@Override
	protected int[][] find1DCarts(Obj[] objs)
	{
		int[][] carts = new int[objs.length][2];
		int cs = 0; // cartStart

		carts[0][0] = cs;

		for (int objIx = 0; objIx < objs.length; objIx++)
		{
			final Obj obj = objs[objIx];
			int ce = cs + k; // cartEnd is exclusive

			// obj should always be in the cart
			while (ce <= objIx)
			{
				cs++;
				ce++;
			}
			// optimize the cart
			while (ce < objs.length
					&& dist.between(obj, objs[ce]) < dist.between(obj, objs[cs]))
			{
				cs++;
				ce++;
			}
			// extend the cart(Start)
			while (cs > 0 && dist.between(objs[cs - 1], objs[cs]) == 0)
			{
				cs--;
			}
			// extend the cart(End)
			while (ce < objs.length && dist.between(objs[ce - 1], objs[ce]) == 0)
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
		Arrays.sort(distances);

		int end = k;
		while (end < distances.length)
		{
			if (distances[end - 1] == distances[end])
			{
				end++;
				continue;
			}
			break;
		}

		Obj[] cart = new Obj[end];
		for (int j = 0; j < cart.length; j++)
		{
			cart[j] = db[distances[j].ix];
		}
		return cart;
	}
}

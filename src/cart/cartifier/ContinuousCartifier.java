package cart.cartifier;

/**
 * Cartifies data into continuous carts, i.e., there are not gaps in carts.
 * 
 * @author M. Emin Aksehirli
 * 
 */
public abstract class ContinuousCartifier extends Cartifier
{
	ContinuousCartifier(Obj[] db, Dissimilarity dist)
	{
		super(db, dist);
	}

	@Override
	protected void find1DCarts(final Obj[] sortedDb)
	{
		int[][] carts = findContinuousCarts(sortedDb);
		for (int i = 0; i < carts.length; i++)
		{
			for (int neighborIx = carts[i][0]; neighborIx < carts[i][1]; neighborIx++)
			{
				itemDb.get(sortedDb[neighborIx].id).getTIDs().set(sortedDb[i].id);
			}
		}
	}

	protected abstract int[][] findContinuousCarts(Obj[] sortedObjs);
}

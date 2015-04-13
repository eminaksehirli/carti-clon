package cart.cartifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import be.uantwerpen.adrem.fim.model.PlainItemDB;

public abstract class Cartifier
{
	protected Obj[] db;
	protected Dissimilarity dist;
	protected PlainItemDB itemDb;

	Cartifier(Obj[] db, Dissimilarity dist)
	{
		this.db = db;
		this.dist = dist;
	}

	PlainItemDB cartify()
	{
		int numOfItems = db.length;
		itemDb = new PlainItemDB();

		if (dist instanceof OneDimDissimilarity)
		{
			final int dimIx = ((OneDimDissimilarity) dist).getDim();
			ArrayList<Obj> sortedDbList = new ArrayList<>(Arrays.asList(db));

			Collections.sort(sortedDbList, new Comparator<Obj>() {
				@Override
				public int compare(Obj o1, Obj o2)
				{
					return Double.compare(o1.v[dimIx], o2.v[dimIx]);
				}
			});
			final Obj[] sortedDb = sortedDbList.toArray(new Obj[0]);

			find1DCarts(sortedDb);
		} else
		{
			for (int i = 0; i < db.length; i++)
			{
				Pair[] distances = new Pair[numOfItems];
				for (int j = 0; j < db.length; j++)
				{
					distances[j] = new Pair(dist.between(db[i], db[j]), j);
				}

				Obj[] cart = findMultiDCart(distances);

				for (Obj neighbor : cart)
				{
					itemDb.get(neighbor.id).getTIDs().set(db[i].id);
				}
			}
		}

		return itemDb;
	}

	protected abstract void find1DCarts(final Obj[] sortedDb);

	protected abstract Obj[] findMultiDCart(Pair[] distances);
}
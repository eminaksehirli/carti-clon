package cart.cartifier;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import mime.plain.PlainItem;
import mime.plain.PlainItemDB;
import cart.io.InputFile;

public abstract class CartifyDb
{
	protected Obj[] db;
	protected PlainItemDB[] projectedDbs;
	protected PrintWriter log;
	protected final InputFile inputFile;
	protected List<Dissimilarity> measures;

	public CartifyDb(InputFile inputFile, List<Dissimilarity> measures)
	{
		this.inputFile = inputFile;
		this.measures = new ArrayList<>(measures);
		try
		{
			log = new PrintWriter(File.createTempFile("cartify-log-", ".txt"));
		} catch (IOException e)
		{
			e.printStackTrace();
			log = new PrintWriter(System.out);
		}
	}

	public void cartify()
	{
		try
		{
			cartifyIt();
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	private void cartifyIt() throws IOException
	{
		if (db == null)
		{
			final ArrayList<double[]> dat = inputFile.getData();
			db = new Obj[dat.size()];
			int objId = 0;
			for (double[] data : dat)
			{
				db[objId] = new Obj(objId, data);
				objId++;
			}
		}
		createCarts();
	}

	public void addMeasure(Dissimilarity measure)
	{
		measures.add(measure);
		projectedDbs = Arrays.copyOf(projectedDbs, measures.size());

		RunnableImplementation runner = new RunnableImplementation(projectedDbs.length - 1);
		runner.run();
	}

	public PlainItemDB getBigTDb()
	{
		log("Merging to final db....");
		PlainItemDB completeDb = new PlainItemDB();

		Set<PlainItem> allTheItems = new HashSet<PlainItem>();
		for (PlainItemDB projectedDb : projectedDbs)
		{
			for (PlainItem item : projectedDb)
			{
				allTheItems.add(item);
			}
		}

		// BitSet.ADDRESS_BITS_PER_WORD == 6
		int bitSetWordsPerProjection = db.length >> 6;
		for (PlainItem item : allTheItems)
		{
			long[] tidsArr = new long[projectedDbs.length * bitSetWordsPerProjection];
			int ofset = 0;
			for (PlainItemDB projectedDb : projectedDbs)
			{
				long[] arr = projectedDb.get(item.getId()).getTIDs().toLongArray();
				for (int i = 0; i < arr.length; i++)
				{
					tidsArr[ofset + i] = arr[i];
				}
				ofset += bitSetWordsPerProjection;
			}
			completeDb.get(item.getId(), BitSet.valueOf(tidsArr));
		}

		log("Final db is created.");
		return completeDb;
	}

	protected void log(final String msg)
	{
		log.println(msg);
		log.flush();
	}

	protected void createCarts()
	{
		projectedDbs = new PlainItemDB[measures.size()];
		ExecutorService executor = Executors.newFixedThreadPool(4);

		for (int i = 0; i < projectedDbs.length; i++)
		{
			executor.execute(new RunnableImplementation(i));
		}

		try
		{
			executor.shutdown();
			executor.awaitTermination(1000, TimeUnit.MINUTES);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			executor.shutdownNow();
		}
	}

	public PlainItemDB[] getProjDbs()
	{
		return projectedDbs;
	}

	protected abstract Cartifier getCartifier(Dissimilarity measure);

	final class RunnableImplementation implements Runnable
	{
		private int measureIx;
		private final Dissimilarity measure;

		private RunnableImplementation(int measureIx)
		{
			this.measure = measures.get(measureIx);
			this.measureIx = measureIx;
		}

		@Override
		public void run()
		{
			log("Creating cart for the measure #" + measure);
			Cartifier cartifier = getCartifier(measure);
			projectedDbs[measureIx] = cartifier.cartify();
		}
	}
}
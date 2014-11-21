package cart.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class InputFile
{
	public String fileName;
	public boolean rowsHasNames;
	public boolean colsHasNames;
	public String separator;
	public String[] columnNames;
	public String[] rowNames;
	private ArrayList<double[]> data;

	public InputFile()
	{// for convenience
	}

	public InputFile(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * Creates an inputFile for a space separated file without any headers.
	 * 
	 * @param fileName
	 */
	public static InputFile forMime(String fileName)
	{
		InputFile inputFile = new InputFile(fileName);
		inputFile.separator = " ";
		return inputFile;
	}

	public ArrayList<double[]> getData() throws FileNotFoundException
	{
		if (data == null)
		{
			readData();
		}

		return data;
	}

	private void readData() throws FileNotFoundException
	{
		Scanner sc = new Scanner(new File(fileName));

		data = new ArrayList<>();
		if (colsHasNames && sc.hasNextLine())
		{
			String line = sc.nextLine();
			columnNames = line.split(separator);
		}

		ArrayList<String> rowNamesList = new ArrayList<>();

		while (sc.hasNextLine())
		{
			String line = sc.nextLine();
			String[] arr = line.split(separator);

			int dataLen = arr.length;
			if (rowsHasNames)
			{
				dataLen = arr.length - 1;
			}
			double[] values = new double[dataLen];

			int firstValueAt = 0;
			if (rowsHasNames)
			{
				rowNamesList.add(arr[0]);
				firstValueAt = 1;
			}

			for (int i = firstValueAt; i < dataLen; i++)
			{
				values[i - firstValueAt] = Double.parseDouble(arr[i]);
			}
			data.add(values);
		}
		sc.close();

		if (rowsHasNames)
		{
			this.rowNames = rowNamesList.toArray(new String[0]);
		}
	}
}

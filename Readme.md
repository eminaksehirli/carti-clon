
CLON: Detecting Cluster Structures by Ordered Neighborhoods
===========================================================

This project is an implementation of CLON algorithm. The details can be found in
 http://adrem.ua.ac.be/maxibander .


Application
-----------
CLON is packaged as a runnable .jar file (clon.jar). You can run the application
 on command line with the commands,

```
java -jar clon.jar data-file k minlen
```

Input
-----
CLON accepts parameters as command line arguments in a specified order. 

- `data-file`: Path to the multi dimensional datafile. Please find the 
properties of the data file below.
- `k`: Parameter for the _k_ nearest neighbors. 
- `minsup`: Minimum length for the cluster.


### About the data file:

- it includes space separated real values,
- each row represents an instance,
- each column represents a feature/dimension,
- does not include a(ny) header row(s),
- all instances have the same number of features,
- there are no missing values,
- the real values formatted in the USA locale (use . as decimal separator)


Output
------
CLON outputs the found clusters to the standard output. Each line of output
represents a subspace cluster. Output format:
```
Size of the cluster- Dimensions of the cluster - Objects of the cluster
```
For example, ```10 - 1 2 6 - 0 1 2 3 4 5 6 7 8 9``` means a cluster is
detected at 1st, 2nd and 6th subspaces and it has '10' objects, i.e.,
0 1 2 3 4 5 6 7 8 9


Example
-------

```
java -jar clon.jar example.mime 170 90
```

Datasets
--------
Datasets that are used in the original paper can be found [here][datasets].

Contact
-------
For more information you can visit http://adrem.ua.ac.be/maxibander or send
an email to Emin Aksehirli <emin.aksehirli@uantwerpen.be>.

[data-sets]:http://adrem.ua.ac.be/sites/adrem.ua.ac.be/files/CLON_datasets.tar.bz2

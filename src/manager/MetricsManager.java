package manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetricsManager {
	private ArrayList<List<String>> mfu = new ArrayList<List<String>>();
	private ArrayList<List<String>> pm = new ArrayList<List<String>>();
//	private ArrayList< List<String> > mergedCsv  = new ArrayList< List<String> >();
	private ArrayList< String > mergedMetrics  = new ArrayList< String >();

	public ArrayList< String > mergeMetrics(ArrayList<String> metricsFromUnderstand,
			ArrayList<String> processMetrics) {


		// convert to ArrayLsit< List<String> >
		this.pm = this.convertCsv( processMetrics );
		this.mfu = this.convertCsv( metricsFromUnderstand );
		// remove except to Class module info
		this.mfu = this.extractClassInfo( mfu );


		for ( List<String> list: pm ) {
			String className = list.get(2);
			int index = this.searchSameClass( className );
			if (index != -1 ) {
				// concvert to String
				String str = this.join(list) +
						this.join(this.mfu.get(index));
				this.mergedMetrics.add( str );

			}else {
				System.out.println("Could not class name: " + className );
			}

		}
		return mergedMetrics;

	}

	private ArrayList< List<String> > convertCsv (ArrayList< String > arrayList) {
		ArrayList< List<String> > csv = new ArrayList< List<String> >();
		for (String line: arrayList) {
			String [] array = line.split(",");
			csv.add(Arrays.asList(array));
		}
		return csv;
	}

	private ArrayList<List<String>> extractClassInfo (ArrayList<List<String>> arrayList) {
		ArrayList<List<String>> csv = new ArrayList<List<String>>();
		for ( List<String> list : arrayList ){
			String kind = list.get( 0 );
			if (kind.indexOf("Class") != -1 ) {
				String classname =list.get(1);
				String[] tmp = classname.split(".");
				csv.add(list);
			}
//			if (kind.equals("Class") )	csv.add(list);
		}
		return csv;
	}
	private String join ( List<String> array ) {
		String str = "";
		for ( String element: array ) {
			str += element;
			str += ",";
//			str = str.concat( element );
		}
		return str;
	}

	private int searchSameClass (String className) {
		for ( List<String> list : this.mfu ) {
			if ( list.get(1).indexOf( className ) != -1 ) {
				return this.mfu.indexOf(list);
			}
		}
		return -1;

	}
}

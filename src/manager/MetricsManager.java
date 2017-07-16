package manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetricsManager {
	private ArrayList<List<String>> mfu = new ArrayList<List<String>>();
	private ArrayList<List<String>> pm = new ArrayList<List<String>>();
//	private ArrayList< List<String> > mergedCsv  = new ArrayList< List<String> >();
	private ArrayList< String > mergedMetrics  = new ArrayList< String >();
	private ArrayList< Integer > selectedIndexes  = new ArrayList< Integer >();

	public ArrayList< String > mergeMetrics(ArrayList<String> metricsFromUnderstand,
			ArrayList<Integer> selectedIndexes,
			ArrayList<String> processMetrics) {


		// convert to ArrayLsit< List<String> >
		this.pm = this.convertCsv( processMetrics );
		// put indexes point at CK metrics columns
		this.selectedIndexes = selectedIndexes;
		this.mfu = this.convertCsv( metricsFromUnderstand );
		// remove except to Class module info
		this.mfu = this.extractClassInfo( mfu );


		for ( List<String> list: pm ) {
			String className = list.get(2);
			if(className.equals("CsvHeadderName")) {
				System.out.println();
			}
			int index = this.searchSameClass( className );
			if (index != -1 && !className.equals("")) {
				// convert to String
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
				if(kind.indexOf("Unknown") == -1) {
					csv.add(extractCkMetrics(list));
				}
			}
//			if (kind.equals("Class") )	csv.add(list);
		}
		return csv;
	}

	private List<String> extractCkMetrics(List<String> list) {
		List<String> ckMetrics = new ArrayList<String>();
		ckMetrics.add(list.get(0));
		ckMetrics.add(list.get(1));
		for(int idx: this.selectedIndexes) {
			ckMetrics.add(list.get(idx));

		}
		return ckMetrics;
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
			String classNameMFU = extractClassName(list.get(1));
//			classNameMFU = classNameMFU.substring(1, classNameMFU.length()-1);
			if ( className.equals(classNameMFU)) {
//				if ( classNameMFU.indexOf( className ) != -1 ) {
				return this.mfu.indexOf(list);
			}
		}
		return -1;

	}
// org.apache.derby.authentication.SystemPrincipal
	private String extractClassName(String string) {
		string = string.replaceAll("\"", "");
		String[] array = string.split("\\.");
		return array[ array.length-1];
	}
}

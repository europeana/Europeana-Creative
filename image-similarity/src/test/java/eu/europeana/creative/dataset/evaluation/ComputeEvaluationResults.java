package eu.europeana.creative.dataset.evaluation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import eu.europeana.creative.dataset.evaluation.om.PrecisionTopN;

public class ComputeEvaluationResults extends BaseResultComputation {

	private Map<String, Map<String, PrecisionTopN>> categoryResults = new HashMap<String, Map<String, PrecisionTopN>>();
	private Map<String, Map<String, PrecisionTopN>> subCategoryResults = new HashMap<String, Map<String, PrecisionTopN>>();

	public Map<String, Map<String, PrecisionTopN>> getCategoryResults() {
		return categoryResults;
	}

	public void setCategoryResults(
			Map<String, Map<String, PrecisionTopN>> categoryResults) {
		this.categoryResults = categoryResults;
	}

	public Map<String, Map<String, PrecisionTopN>> getSubCategoryResults() {
		return subCategoryResults;
	}

	public void setSubCategoryResults(
			Map<String, Map<String, PrecisionTopN>> subCategoryResults) {
		this.subCategoryResults = subCategoryResults;
	}

	@Test
	public void computePrecisionAtTopN() throws IOException {
		String dataset = DEMO_DATASET;
		readCategories(dataset);
		computeCategoryLevelPrecision(dataset);
		
		System.out.println("===== category level precision @ top N === ");
		printCategoryLevelPrecision(PrecisionTopN.TYPE_CATEGORY);
		
		System.out.println("===== sub-category level precision @ top N === ");
		printCategoryLevelPrecision(PrecisionTopN.TYPE_SUBCATEGORY);

	}

	private void printCategoryLevelPrecision(String categType) {
		String category;
		Map<String, PrecisionTopN> metrics;
		String outString;

		Set<Entry<String, Map<String, PrecisionTopN>>> entrySet = null;

		if (PrecisionTopN.TYPE_CATEGORY.equals(categType))
			entrySet = getCategoryResults().entrySet();
		else if (PrecisionTopN.TYPE_SUBCATEGORY.equals(categType))
			entrySet = getSubCategoryResults().entrySet();

		for (Map.Entry<String, Map<String, PrecisionTopN>> categoryMetrics : entrySet) {

			category = categoryMetrics.getKey();
			metrics = categoryMetrics.getValue();
			outString = category + ";";

			for (Map.Entry<String, PrecisionTopN> precision : metrics
					.entrySet()) {
				outString += (precision.getValue().toString());
				outString += ";";
			}

			System.out.println(outString);

		}
	}

	

	void readCategories(String dataset) throws IOException {
		BufferedReader reader = getExperimentExecutionResultsReader(dataset);
		String row = null;
		String[] values;
		String category;
		String subCategory;

		while ((row = reader.readLine()) != null) {
			values = row.split(";");
			category = values[2].trim();
			subCategory = values[3].trim();

			if (!categoryResults.containsKey(category))
				categoryResults.put(category,
						new HashMap<String, PrecisionTopN>());

			if (!subCategoryResults.containsKey(subCategory))
				subCategoryResults.put(subCategory,
						new HashMap<String, PrecisionTopN>());

		}

	}
	

	private void computeCategoryLevelPrecision(String dataset)
			throws IOException {
		BufferedReader reader = getExperimentExecutionResultsReader(dataset);
		String row = null;
		String[] values;
		String category;
		String subCategory;
		Map<String, PrecisionTopN> precisionMeasures, precisionMeasuresSc;
		int valtop5, valtop10, valtop15, valtop20, valtop25;
		int valtop5sc, valtop10sc, valtop15sc, valtop20sc, valtop25sc;

		while ((row = reader.readLine()) != null) {
			values = row.split(";");
			category = values[2].trim();
			subCategory = values[3].trim();

			precisionMeasures = getCategoryResults().get(category);
			precisionMeasuresSc = getSubCategoryResults().get(subCategory);

			if (precisionMeasures == null || precisionMeasures.isEmpty()) {
				precisionMeasures = createPrecisionsObjects(PrecisionTopN.TYPE_CATEGORY);
				getCategoryResults().put(category, precisionMeasures);
			}

			if (precisionMeasuresSc == null || precisionMeasuresSc.isEmpty()) {
				precisionMeasuresSc = createPrecisionsObjects(PrecisionTopN.TYPE_SUBCATEGORY);
				getSubCategoryResults().put(subCategory, precisionMeasuresSc);
			}

			// category level
			valtop5 = Integer.valueOf(values[131].trim());
			precisionMeasures.get("P@5c").addToSumm(valtop5);

			valtop10 = Integer.valueOf(values[132].trim());
			precisionMeasures.get("P@10c").addToSumm(valtop10);

			valtop15 = Integer.valueOf(values[133].trim());
			precisionMeasures.get("P@15c").addToSumm(valtop15);

			valtop20 = Integer.valueOf(values[134].trim());
			precisionMeasures.get("P@20c").addToSumm(valtop20);

			valtop25 = Integer.valueOf(values[135].trim());
			precisionMeasures.get("P@25c").addToSumm(valtop25);

			// sub category level
			valtop5sc = Integer.valueOf(values[136].trim());
			precisionMeasuresSc.get("P@5sc").addToSumm(valtop5sc);

			valtop10sc = Integer.valueOf(values[137].trim());
			precisionMeasuresSc.get("P@10sc").addToSumm(valtop10sc);

			valtop15sc = Integer.valueOf(values[138].trim());
			precisionMeasuresSc.get("P@15sc").addToSumm(valtop15sc);

			valtop20sc = Integer.valueOf(values[139].trim());
			precisionMeasuresSc.get("P@20sc").addToSumm(valtop20sc);

			valtop25sc = Integer.valueOf(values[140].trim());
			precisionMeasuresSc.get("P@25sc").addToSumm(valtop25sc);
		}
	}

	private Map<String, PrecisionTopN> createPrecisionsObjects(String type) {
		Map<String, PrecisionTopN> precisionMeasures = new HashMap<String, PrecisionTopN>();

		PrecisionTopN top5 = new PrecisionTopN(type, 5);
		precisionMeasures.put(top5.getMetricName(), top5);

		PrecisionTopN top10 = new PrecisionTopN(type, 10);
		precisionMeasures.put(top10.getMetricName(), top10);

		PrecisionTopN top15 = new PrecisionTopN(type, 15);
		precisionMeasures.put(top15.getMetricName(), top15);

		PrecisionTopN top20 = new PrecisionTopN(type, 20);
		precisionMeasures.put(top20.getMetricName(), top20);

		PrecisionTopN top25 = new PrecisionTopN(type, 25);
		precisionMeasures.put(top25.getMetricName(), top25);

		return precisionMeasures;

	}

	protected BufferedReader getExperimentExecutionResultsReader(String dataset)
			throws FileNotFoundException {
		return new BufferedReader(new FileReader(
				getExperimentExecutionResultsFile(dataset)));
	}

}

package com.trang.uima.ae;

import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.ishidalab.service.uima.types.TranslationText;

import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import com.trang.uima.langrid.ComponentServiceFactoryHolder;
import com.trang.uima.types.IntermediateResult;
import com.trang.uima.types.Target;

public class BackwardTranslationAnnotator extends JCasAnnotator_ImplBase {

	private static final Logger LOGGER = Logger
			.getLogger(BackwardTranslationAnnotator.class);

	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);
		org.apache.log4j.BasicConfigurator.configure();
		LOGGER.info("[BackwardTranslation Annotator] initializing BackwardTranslation Annotator ...");
	}

	public void process(JCas aJCas) {

		try {
			FSIterator translationIterator = aJCas.getAnnotationIndex(
					IntermediateResult.type).iterator();
			FeatureStructure fs = translationIterator.get();
			IntermediateResult translationText = (IntermediateResult) fs;
			String sourceL = translationText.getToLang();
			String targetL = translationText.getFromLang();
			String docText = translationText.getContent();

			int dotIndex = docText.indexOf(".");
			String sentenceID = docText.substring(0, dotIndex);
			String source = docText.substring(dotIndex + 1);

			long before = System.currentTimeMillis();
			System.out.println("	The backward translation of partition "
					+ sentenceID + " starts at: " + before);

			String result = ComponentServiceFactoryHolder
					.get()
					.getService("BackwardTranslationPL",
							TranslationService.class)
					.translate(sourceL, targetL, source);

			String finalResult = sentenceID + "." + result;
			
			Target target = new Target(aJCas);
			target.setBegin(0);
			target.setEnd(finalResult.length());
			target.setFromLang(sourceL);
			target.setToLang(targetL);
			target.setContent(finalResult);
			target.addToIndexes();

			long after = System.currentTimeMillis();
			System.out.println("	The backwards translation of partition "
					+ sentenceID + " finishes in: " + (after - before) + " ms");

		} catch (Exception e) {
			LOGGER.error("BackwardTranslation Error: " + e.getMessage());
		}

	}
}

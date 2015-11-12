package com.trang.uima.ae;

import jp.go.nict.langrid.service_1_2.translation.TranslationService;

import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import com.trang.uima.langrid.ComponentServiceFactoryHolder;
import com.trang.uima.types.IntermediateResult;

public class ForwardTranslationAnnotator extends JCasAnnotator_ImplBase {

	private static final Logger LOGGER = Logger
			.getLogger(ForwardTranslationAnnotator.class);

	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);
		org.apache.log4j.BasicConfigurator.configure();
		LOGGER.info("[ForwardTranslation Annotator] initializing ForwardTranslation Annotator ...");
	}

	public void process(JCas aJCas) {

		try {
			String langPair = aJCas.getDocumentLanguage();
			int seIndex = langPair.indexOf("-");
			String sourceL = langPair.substring(0, seIndex);
			String targetL = langPair.substring(seIndex + 1, langPair.length());
			String docText = aJCas.getDocumentText();

			int dotIndex = docText.indexOf(".");
			String sentenceID = docText.substring(0, dotIndex);
			String source = docText.substring(dotIndex + 1);

			long start = System.currentTimeMillis();
			System.out.println("	The forward translation of partition "
					+ sentenceID + " starts at:" + start);

			String result = ComponentServiceFactoryHolder
					.get()
					.getService("ForwardTranslationPL",
							TranslationService.class)
					.translate(sourceL, targetL, source);

			String finalResult = sentenceID + "." + result;
			
			IntermediateResult transText = new IntermediateResult(aJCas);
			transText.setBegin(0);
			transText.setEnd(finalResult.length());
			transText.setFromLang(sourceL);
			transText.setToLang(targetL);
			transText.setContent(finalResult);
			transText.addToIndexes();


			long finish = System.currentTimeMillis();

			System.out.println("	The forward translation of partition "
					+ sentenceID + " finishes in: " + (finish - start) + " ms");

		} catch (Exception e) {
			LOGGER.error("ForwardTranslation Error: " + e.getMessage());
		}
	}
}

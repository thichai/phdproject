package com.trang.uima.ae;

import java.net.URL;

import jp.go.nict.langrid.client.ws_1_2.ClientFactory;
import jp.go.nict.langrid.client.ws_1_2.TranslationClient;
import jp.go.nict.langrid.language.Language;

import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import com.trang.uima.types.IntermediateResult;

public class EchoTranslationAnnotator extends JCasAnnotator_ImplBase {

	private static final Logger LOGGER = Logger
			.getLogger(EchoTranslationAnnotator.class);

	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);
		// org.apache.log4j.BasicConfigurator.configure();
		// LOGGER.info("[ForwardTranslation Annotator] initializing ForwardTranslation Annotator ...");
	}

	public void process(JCas aJCas) {
		// LOGGER.info("[ForwardTranslation Annotator] processing ForwardTranslation Annotator ...");

		try {

			// Use different values for different case: BPELTwoHopTranslation,
			// UIMATwoHopTranslation, UIMACASPoolTwoHopTranslation
			// TranslationCombinedWithDictionary,
			// UIMATranslationCombinedWithDictionary


			// MockTranslationA, EchoTranslation
			String seviceName = "MockTranslationB";

			// url=http://localhost:8080/jp.go.nict.langrid.webapps.mock/services/,
			// http://localhost:8081/jp.go.trang.langrid.webapps.echo/services/
			//http://localhost:8081/jp.go.nict.langrid.webapps.blank/services/

			String url = "http://localhost:8082/jp.go.trang.langrid.webapps.echo/services/";

			TranslationClient client = ClientFactory
					.createTranslationClient(new URL(url + seviceName));
			
			
			/*TranslationClient client = ClientFactory
					.createTranslationClient(new URL(
							"http://langrid.org/service_manager/invoker/kyoto1.langrid:GoogleTranslate"));
			client.setUserId("ishida.kyoto-u");
			client.setPassword("tWJaakYm");*/
			

			String langPair = aJCas.getDocumentLanguage();
			int seIndex = langPair.indexOf("-");
			String sourceL = langPair.substring(0, seIndex);
			String targetL = langPair.substring(seIndex + 1, langPair.length());

			Language sourceLang = new Language(sourceL);
			Language targetLang = new Language(targetL);

			String docText = aJCas.getDocumentText();

			int dotIndex = docText.indexOf(".");
			String sentenceID = docText.substring(0, dotIndex);
			String source = docText.substring(dotIndex + 1);

			long start = System.currentTimeMillis();
			System.out.println("	Translation of partition " + sentenceID
					+ " starts at:" + start);

			/*
			 * String result = Context.createSoapClient(getClass(),
			 * TranslationService.class, seviceName, gridID).translate(sourceL,
			 * targetL, source); System.out.println(result);
			 */

			String result = client.translate(sourceLang, targetLang, source);
//			System.out.println(result);

			IntermediateResult transText = new IntermediateResult(aJCas);
			transText.setBegin(0);
			transText.setEnd(result.length());
			transText.setFromLang(sourceL);
			transText.setToLang(targetL);
			transText.setContent(result);
			transText.addToIndexes();

			long end = System.currentTimeMillis();
			System.out.println("	Translation of partition " + sentenceID
					+ " finishs in: " + (end - start) + " ms");

		} catch (Exception e) {
			LOGGER.error("Translation Error: " + e.getMessage());
		}
	}
}

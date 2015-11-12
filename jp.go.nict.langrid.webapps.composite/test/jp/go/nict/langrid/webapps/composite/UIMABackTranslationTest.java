package jp.go.nict.langrid.webapps.composite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.impl.protobuf.PbClientFactory;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationResult;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import junit.framework.Assert;

import org.junit.Test;

public class UIMABackTranslationTest {
	@Test
	public void test() throws Exception{
		String datafile = "./testdata/example.txt";
		String document = readFile(datafile);

		BackTranslationService service = new PbClientFactory().create(
				BackTranslationService.class, new URL(
						"http://localhost:8080/jp.go.nict.langrid.webapps.composite/pbServices/"
								+ "UIMABackTranslation"));

		RequestAttributes req = (RequestAttributes) service;
		req.getTreeBindings()
				.add(new BindingNode(
						"ForwardTranslationPL",
						"http://localhost:8080/jp.go.nict.langrid.webapps.composite/pbServices/GoogleTranslation"));
		req.getTreeBindings()
				.add(new BindingNode(
						"BackwardTranslationPL",
						"http://localhost:8080/jp.go.nict.langrid.webapps.composite/pbServices/JServerTranslation"));

		System.out.println("Translating the document...");

		long start = System.currentTimeMillis();
		BackTranslationResult backTransResult = service.backTranslate("en",
				"ja", document);
		long finish = System.currentTimeMillis();

		System.out.println("Finish translation in: " + (finish - start));
		
		System.out.println("Target Result: " + backTransResult.getTarget());
	}
	
	private String readFile(String fileName) {
		String result = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			
			String line = br.readLine();
			
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			result = sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
}

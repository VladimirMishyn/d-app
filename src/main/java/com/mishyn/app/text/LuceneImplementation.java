package com.mishyn.app.text;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Volodymyr on 11.05.2016.
 */
public class LuceneImplementation {
    public static void nGramsPrint(String text) {
        StringReader reader = new StringReader(text);
        StandardTokenizer source = new StandardTokenizer(Version.LUCENE_46, reader);
        TokenStream tokenStream = new StandardFilter(Version.LUCENE_46, source);

        ShingleFilter sf = new ShingleFilter(tokenStream, 2, 3);
        sf.setOutputUnigrams(true);

        CharTermAttribute charTermAttribute = sf.addAttribute(CharTermAttribute.class);
        try {
            sf.reset();
            while (sf.incrementToken()) {
                System.out.println(charTermAttribute.toString());
            }
            sf.end();
            sf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

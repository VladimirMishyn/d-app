package com.mishyn.app.text;

import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;
import com.ibm.icu.text.BreakIterator;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.DocumentPreprocessor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Volodymyr on 12.05.2016.
 */
public class TextAnalysis {

    private static final TokenizerFactory TOKENIZER_FACTORY
            = IndoEuropeanTokenizerFactory.INSTANCE;
    private static final SentenceModel SENTENCE_MODEL
            = new MedlineSentenceModel();

    public static List<String> splitTextSentencesBI(String text) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("splitTextSentencesBI.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(text);
        int start = iterator.first();
        List<String> sentences = new ArrayList<String>();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            writer.println(text.substring(start, end));
            //   System.out.println(text.substring(start, end));
            sentences.add(text.substring(start, end));
        }
        //  System.out.println(sentences.size());
        writer.close();
        return sentences;
    }

    public static List<String> splitTextSentencesSNLP(String text) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("splitTextSentencesSNLP.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Reader reader = new StringReader(text);
        DocumentPreprocessor dp = new DocumentPreprocessor(reader);
        List<String> sentenceList = new ArrayList<String>();

        for (List<HasWord> sentence : dp) {
            String sentenceString = Sentence.listToString(sentence);
            sentenceList.add(sentenceString.toString());
        }
        List<String> sentences = new ArrayList<String>();
        for (String sentence : sentenceList) {
            // System.out.println(sentence);
            sentences.add(sentence);
            writer.println(sentence);
        }
        //  System.out.println(sentences.size());
        writer.close();
        return sentences;
    }

    public static List<String> splitTextSentencesLPNLP(String text) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("splitTextSentencesLPNLP.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        List<String> tokenList = new ArrayList<String>();
        List<String> whiteList = new ArrayList<String>();
        Tokenizer tokenizer
                = TOKENIZER_FACTORY.tokenizer(text.toCharArray(),
                0, text.length());
        tokenizer.tokenize(tokenList, whiteList);
        String[] tokens = new String[tokenList.size()];
        String[] whites = new String[whiteList.size()];
        tokenList.toArray(tokens);
        whiteList.toArray(whites);
        int[] sentenceBoundaries
                = SENTENCE_MODEL.boundaryIndices(tokens, whites);
        int sentStartTok = 0;
        int sentEndTok = 0;
        List<String> sentences = new ArrayList<String>();
        for (int sentenceBoundary : sentenceBoundaries) {
            sentEndTok = sentenceBoundary;
            String sentenceExtracted = "";
            for (int j = sentStartTok; j <= sentEndTok; j++) {
                sentenceExtracted += tokens[j] + whites[j + 1];
            }
            sentStartTok = sentEndTok + 1;
            sentences.add(sentenceExtracted);
            writer.println(sentenceExtracted);
        }
        //  System.out.println(sentences.size());
        writer.close();
        return sentences;
    }
}

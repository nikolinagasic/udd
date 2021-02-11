package com.la.service.publish;

import com.la.model.publish.Plagiat;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CheckPlagiarismService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try {
            System.err.println("SENDING REQUEST FOR PLAGIARISM");
            List<Plagiat> plagiatList = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                Plagiat plagiat = new Plagiat("https://" + generateRandomWords(1)[0] + ".com");
                plagiatList.add(plagiat);
            }
            delegateExecution.setVariable("plagiats", plagiatList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] generateRandomWords(int numberOfWords) {
        String[] randomStrings = new String[numberOfWords];
        Random random = new Random();
        for (int i = 0; i < numberOfWords; i++) {
            char[] word = new char[random.nextInt(8) + 3]; // words of length 3 through 10. (1 and 2 letter words are boring.)
            for (int j = 0; j < word.length; j++) {
                word[j] = (char) ('a' + random.nextInt(26));
            }
            randomStrings[i] = new String(word);
        }
        return randomStrings;
    }
}

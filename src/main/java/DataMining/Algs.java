package DataMining;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

public class Algs {

    private int correct;
    private int incorrect;
    private int tp,tn,fp,fn;
    private String algorithm;

    public Algs(String file, int classIndex, Classifier classifier){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            Instances instances = new Instances(bufferedReader);
            instances.setClassIndex(classIndex);
            Instances tanito, kiertekelo;
            Evaluation evaluation;

            if(false) instances.randomize(new Random());
            tanito = new Instances(instances,0,9*instances.size()/10);
            kiertekelo = new Instances(instances,9*instances.size()/10,instances.size()/10);
            classifier.buildClassifier(tanito);
            evaluation = new Evaluation(kiertekelo);
            double[] eredmeny = evaluation.evaluateModel(classifier, kiertekelo);
            evaluation = new Evaluation(instances);
            evaluation.crossValidateModel( classifier, instances, 10, new Random(1));

            int TP=0, TN=0, FP=0, FN=0;

            for(int i=0;i<kiertekelo.size();i++) {
                if ((((Instances) kiertekelo).get(i)).classValue() == 1 && eredmeny[i] == 1)
                    TP++;
                if ((((Instances) kiertekelo).get(i)).classValue() == 1 && eredmeny[i] == 0)
                    FN++;
                if ((((Instances) kiertekelo).get(i)).classValue() == 0 && eredmeny[i] == 1)
                    FP++;
                if ((((Instances) kiertekelo).get(i)).classValue() == 0 && eredmeny[i] == 0)
                    TN++;
            }

            tp=TP;
            tn=TN;
            fp=FP;
            fn=FN;
            incorrect = (instances.size()-(int)evaluation.correct());
            correct = (int)evaluation.correct();
            String[] alg = String.valueOf(classifier.getClass()).split("\\.");
            algorithm = alg[alg.length-1];
        }
        catch (Exception e) {
        System.out.println("Error Occurred!!!! \n" + e.getMessage());
        }
    }

    public int getCorrect() {
        return correct;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public int getTp() {
        return tp;
    }

    public int getTn() {
        return tn;
    }

    public int getFp() {
        return fp;
    }

    public int getFn() {
        return fn;
    }

    @Override
    public String toString() {
        return algorithm;
    }

}

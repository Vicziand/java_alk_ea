package DataMining;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class CrossValidation {

    private int correct;
    private String algorithm;

   public CrossValidation(String file, int classIndex, Classifier classifier, PrintWriter kiir){	// Classifier classifier: interface
// https://weka.sourceforge.io/doc.dev/weka/classifiers/Classifier.html

        try {

            //System.out.println("\nclassifier.getClass():"+classifier.getClass());
            kiir.println("\nclassifier.getClass():"+classifier.getClass());
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            Instances instances = new Instances(bufferedReader);
            instances.setClassIndex(classIndex);
            Instances tanito, kiertekelo;
            Evaluation evaluation;
            if(false) instances.randomize(new Random());
            tanito = new Instances(instances,0,9*instances.size()/10);
            //System.out.println("tanító.size():"+tanito.size());
            kiertekelo = new Instances(instances,9*instances.size()/10,instances.size()/10);
            //System.out.println("kiértékelő.size():"+kiertekelo.size());
            classifier.buildClassifier(tanito);
            evaluation = new Evaluation(kiertekelo);
            double[] eredmeny = evaluation.evaluateModel(classifier, kiertekelo);
            evaluation = new Evaluation(instances);
            evaluation.crossValidateModel( classifier, instances, 10, new Random(1));

            int TP=0, TN=0, FP=0, FN=0;


            //  TP:TtruePositive, TN:TrueNegative, FP:FalsePositive, FN:FalseNegative
            for(int i=0;i<kiertekelo.size();i++) {
                //System.out.println(i + "\t" + (((Instances) kiertekelo).get(i)).classValue() + "\t" + eredmeny[i]);
                if ((((Instances) kiertekelo).get(i)).classValue() == 1 && eredmeny[i] == 1)
                    TP++;
                if ((((Instances) kiertekelo).get(i)).classValue() == 1 && eredmeny[i] == 0)
                    FN++;
                if ((((Instances) kiertekelo).get(i)).classValue() == 0 && eredmeny[i] == 1)
                    FP++;
                if ((((Instances) kiertekelo).get(i)).classValue() == 0 && eredmeny[i] == 0)
                    TN++;
            }

            kiir.println("TP="+TP+", "+"TN="+TN+", "+"FP="+FP+", "+"FN="+FN);
            kiir.println("TP+TN="+(TP+TN));
            kiir.println("FP+FN="+(FP+FN));

// 10: 10-szeres keresztvalidáció
// new Random(1): véletlenszám generátor
            kiir.println(evaluation.toSummaryString("\nResults", false));
            kiir.println("Correctly Classified Instances:"+(int)evaluation.correct()+"\t"+100*evaluation.correct()/instances.size()+"%");
            kiir.println("Incorrectly Classified Instances:"+(instances.size()-(int)evaluation.correct()));
            kiir.println();

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

}

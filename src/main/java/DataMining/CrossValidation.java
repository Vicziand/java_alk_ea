package DataMining;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;


public class CrossValidation {

   public CrossValidation(String file, int classIndex, Classifier classifier){	// Classifier classifier: interface
// https://weka.sourceforge.io/doc.dev/weka/classifiers/Classifier.html
        System.out.println("\nclassifier.getClass():"+classifier.getClass());
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            Instances instances = new Instances(bufferedReader);
            instances.setClassIndex(classIndex);
            if(false) instances.randomize(new Random());
            Evaluation evaluation = new Evaluation(instances);
            evaluation.crossValidateModel( classifier, instances, 10, new Random(1));
// 10: 10-szeres keresztvalidáció
// new Random(1): véletlenszám generátor
            System.out.println(evaluation.toSummaryString("\nResults", false));
            System.out.println("Correctly Classified Instances:"+(int)evaluation.correct()+"\t"+100*evaluation.correct()/instances.size()+"%");
            System.out.println("Incorrectly Classified Instances:"+(instances.size()-(int)evaluation.correct()));
            System.out.println();
        }
        catch (Exception e) {
            System.out.println("Error Occurred!!!! \n" + e.getMessage());
        }
    }

}

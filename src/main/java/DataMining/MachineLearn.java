package DataMining;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Random;
public class MachineLearn {
   public MachineLearn(String file, int classIndex) {
       try {
           BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
           Instances instances = new Instances(bufferedReader);
           //System.out.println("instances.size():" + instances.size());
           instances.setClassIndex(classIndex);
           Instances tanito, kiertekelo;
           J48 classifier;
           Evaluation evaluation;
           classifier = new J48();
           //System.out.println("\nMachineLearn: Randomize után vagy anélkül: tanító: első 90%, kiértékelő: utolsó 10%");
           if(false) instances.randomize(new Random());
           tanito = new Instances(instances,0,9*instances.size()/10);
           //System.out.println("tanító.size():"+tanito.size());
           kiertekelo = new Instances(instances,9*instances.size()/10,instances.size()/10);
           //System.out.println("kiértékelő.size():"+kiertekelo.size());
           classifier.buildClassifier(tanito);
           evaluation = new Evaluation(kiertekelo);
           double[] eredmeny = evaluation.evaluateModel(classifier, kiertekelo);

          // System.out.println(evaluation.toSummaryString("\nResults", false));
           //System.out.println("Correctly Classified Instances:"+(int)evaluation.correct());
           //System.out.println("Incorrectly Classified Instances:"+(kiertekelo.size()-(int)evaluation.correct()));
           //System.out.println(classifier.toString());

           //System.out.println("\nMachineLearn: Vizsgálat részletesen, egyesével:");
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

           //System.out.println("TP="+TP+", "+"TN="+TN+", "+"FP="+FP+", "+"FN="+FN);
           //System.out.println("TP+TN="+(TP+TN));
           //System.out.println("FP+FN="+(FP+FN));

           PrintWriter kiir = new PrintWriter("Döntési fa.txt");
           kiir.println("tanító.size() :"+tanito.size());
           kiir.println("kiértékelő.size() :"+kiertekelo.size());
           kiir.println(evaluation.toSummaryString("\nResults", false));
           kiir.println("Correctly Classified Instances: "+(int)evaluation.correct());
           kiir.println("Incorrectly Classified Instances: "+(kiertekelo.size()-(int)evaluation.correct()));
           kiir.println(classifier.toString());
           kiir.println("TP="+TP+", "+"TN="+TN+", "+"FP="+FP+", "+"FN="+FN);
           kiir.println("TP+TN="+(TP+TN));
           kiir.println("FP+FN="+(FP+FN));
           kiir.println("\nMachineLearn: Vizsgálat részletesen, egyesével:");

           //  TP:TtruePositive, TN:TrueNegative, FP:FalsePositive, FN:FalseNegative
           for(int i=0;i<kiertekelo.size();i++) {
               kiir.println(i + "\t" + (((Instances) kiertekelo).get(i)).classValue() + "\t" + eredmeny[i]);
           }
           kiir.close();

       } catch (Exception e) {
           System.out.println("Error Occurred!!!! \n" + e.getMessage());
       }
   }


}

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.MathContext;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame{

    static ArrayList<Double> global_xDataArray = new ArrayList<>();
    static ArrayList<Double> global_yDataArray = new ArrayList<>();

    public static double function(BigDecimal sin, BigDecimal v, BigDecimal g, double t){
        //return v*Math.sin(Math.toRadians(angle))*t-g*Math.pow(t,2)/2;

        String sT = String.valueOf(t);
        String sT2 = String.valueOf(Math.pow(t,2));

        BigDecimal bdT = new BigDecimal(sT);
        BigDecimal bdT2 = new BigDecimal(sT2);
        BigDecimal two = new BigDecimal("2");

        BigDecimal y = ((v.multiply(sin)).multiply(bdT)).subtract((g.multiply(bdT2)).divide(two, MathContext.DECIMAL32));
        return y.doubleValue();
    }

    public static double xfunction(BigDecimal cos, BigDecimal v, double t){
        //return v*Math.cos(Math.toRadians(angle))*2*t;

        String sT = String.valueOf(t);

        BigDecimal bdT = new BigDecimal(sT);
        BigDecimal two = new BigDecimal("2");

        BigDecimal y = bdT.multiply(two.multiply(v.multiply(cos)));
        return y.doubleValue();
    }

    public static double tfunction(BigDecimal sin, BigDecimal v, BigDecimal g){
        //return 2*v*Math.sin(Math.toRadians(angle))/g

        BigDecimal two = new BigDecimal("2");

        BigDecimal y = (two.multiply(v.multiply(sin))).divide(g, MathContext.DECIMAL32);
        return y.doubleValue();
    }


    public static void calculate(int angle, int iV, int stack){

        String sSin = String.valueOf(Math.sin(Math.toRadians(angle)));
        String sCos = String.valueOf(Math.cos(Math.toRadians(angle)));
        String sV = String.valueOf(iV);

        BigDecimal sin = new BigDecimal(sSin);
        BigDecimal cos = new BigDecimal(sCos);
        BigDecimal v = new BigDecimal(sV);
        BigDecimal g = new BigDecimal("9.8");

        ArrayList<Double> yDataList = new ArrayList<>();
        ArrayList<Double> xDataList = new ArrayList<>();

        //그래프 연산

        for (double i = 0.0; i <= tfunction(sin, v, g); ) {
            double yData = function(sin, v, g, i);
            double xData = xfunction(cos, v, i);
            //System.out.println(xData);
            xDataList.add(xData);
            yDataList.add(yData);
            String a = String.valueOf(i);
            BigDecimal b = new BigDecimal(a);
            BigDecimal c = new BigDecimal("0.01");
            BigDecimal d = b.add(c);
            i = d.doubleValue();
        }

        double temp = xfunction(cos, v, tfunction(sin, v, g));
        System.out.println(temp);
        System.out.println(function(sin, v, g, tfunction(sin, v, g)));
        xDataList.add(temp);
        yDataList.add(0.0);
        xDataList.add(0.0);
        yDataList.add(0.0);
        global_xDataArray.addAll(xDataList);
        global_yDataArray.addAll(yDataList);

        if (stack == 0) {

            XYChart chart = QuickChart.getChart("Projectile Motion", "distance", "height", "0", global_xDataArray, global_yDataArray);


            JFrame graph = new JFrame("Chart");
            //graph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Dimension res = Toolkit.getDefaultToolkit().getScreenSize();

            graph.setLayout(new BorderLayout());
            graph.setSize(500, 500);
            int x = (int) ((res.width) / 2.0 - 250.0);
            int y = (int) ((res.height) / 2.0 - 250.0);
            graph.setLocation(x, y);

            JPanel chartPanel = new XChartPanel<>(chart);
            graph.add(chartPanel, BorderLayout.CENTER);

            graph.pack();
            graph.setVisible(true);
        }


    }

    public Main(){
        setTitle("Projectile Motion Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension res = Toolkit.getDefaultToolkit().getScreenSize();

        Container c = getContentPane();
        c.setLayout(null);
        setSize(300,300);
        int x = (int) ((res.width)/2.0-150.0);
        int y = (int) ((res.height)/2.0-150.0);
        setLocation(x, y);

        JLabel label1 = new JLabel("Projectile Motion Simulator");
        label1.setLocation(70, 20);
        label1.setSize(200,20);
        c.add(label1);
        JLabel label2 = new JLabel("Angle");
        label2.setLocation(20,60);
        label2.setSize(50,20);
        c.add(label2);
        JTextField text1 = new JTextField("");
        text1.setLocation(70,60);
        text1.setSize(50,20);
        c.add(text1);
        JLabel label3 = new JLabel("Velocity");
        label3.setLocation(20, 85);
        label3.setSize(50,20);
        c.add(label3);
        JTextField text2 = new JTextField("");
        text2.setLocation(70,85);
        text2.setSize(50,20);
        c.add(text2);
        JButton btn1 = new JButton("Simulate");
        btn1.setLocation(20, 120);
        btn1.setSize(100,20);
        c.add(btn1);
        JButton btn2 = new JButton("Stack");
        btn2.setLocation(20, 150);
        btn2.setSize(100,20);
        c.add(btn2);
        JButton btn3 = new JButton("Stack Clear");
        btn3.setLocation(20, 180);
        btn3.setSize(100,20);
        c.add(btn3);

        ActionListener listener1 = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String sAngle = text1.getText();
                String sV = text2.getText();
                int angle = Integer.parseInt(sAngle);
                int v = Integer.parseInt(sV);
                calculate(angle, v, 0);
            }
        };
        btn1.addActionListener(listener1);

        ActionListener listener2 = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String sAngle = text1.getText();
                String sV = text2.getText();
                int angle = Integer.parseInt(sAngle);
                int v = Integer.parseInt(sV);
                calculate(angle, v, 1);
                JOptionPane.showMessageDialog(null, "Chart is stacked", "notice", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        btn2.addActionListener(listener2);

        ActionListener listener3 = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                global_xDataArray = new ArrayList<>();
                global_yDataArray = new ArrayList<>();
                JOptionPane.showMessageDialog(null, "Stacked charts are removed", "notice", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        btn3.addActionListener(listener3);

        setVisible(true);
    }

    public static void main(String[] args){
        new Main();
    }

}





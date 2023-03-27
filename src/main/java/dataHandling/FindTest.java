package dataHandling;

import DataInfo.Point;
import lombok.Data;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author 张璐洋1
 */
@Data
public class FindTest {

    public static final double X_MAX = 10;
    public static final double X_MIN = 0.0;
    public static final double Y_MAX = 12;
    public static final double Y_MIN = 0.0;
    public static final double X = 4;
    public static final double Y = 4.5;
    public static final double PRECISION  = 0.000001;
    public static final int L = 10;
    public static final double K = 0.96;
    public static final double S = 0.2;


    public static Point coordinateProcessing(Float[] distance){
        int[][] uwbPositions = new int[][]{{0, 0}, {0, 8}, {8, 9}, {9, 0}};
        Point points = new Point();
        positionCalculate(distance,uwbPositions,points);
        return points;
    }
    private static double random(){
        return ThreadLocalRandom.current().nextDouble(1);
    }
    public static void positionCalculate(Float[] distance, int[][] uwbPositions, Point points) {
        int i,p;
        double nextX = 0,nextY = 0;
        double changer;
        double p1;
        points.setX(X);
        points.setY(Y);
        double preX = X;
        double preY = Y;
        double preValue = Double.MAX_VALUE;
        double curValue = Double.MAX_VALUE;
        double bestValue = Double.MAX_VALUE;
        double t = 1.0;
        while (t >= PRECISION){

            t *= K;
            for( i = 0; i < L; i++ ){
                p = 0;
                //直到产生一个新解
                while(p == 0) {
                    //扰动，产生一个新解
                    nextX = preX + t*S*(random()-0.5)*(X_MAX-X_MIN);
                    nextY = preY + t*S*(random()-0.5)*(Y_MAX-Y_MIN);
                    //如果在搜索范围
                    if ((nextX >= X_MIN) && (nextX <= X_MAX) && (nextY >= Y_MIN) && (nextY <= Y_MAX)){
                        p = 1;
                    }
                }

                curValue = optimizationFunction(nextX, nextY, distance, uwbPositions);

                //判断最新解与最优解的误差
                if (bestValue > curValue) {
                    //此为新的最优解
				points.setX(nextX);
                points.setY(nextY);
                bestValue = curValue;
                }

                //如果上一次解误差大于新解，则接受新解
                if( preValue > curValue) {
                    //接受新解
                    preX = nextX;
                    preY = nextY;
                    preValue = curValue;
                } else {
                    changer = (preValue - curValue) / (K * t);
                    //计算概率
                    p1 = Math.exp(changer);
                    //以一定概率接受较差的解
                    if (p1 > random() ){
                        preX = nextX;
                        preY = nextY;
                        preValue = curValue;
                    }
                }

            }
        }



    }

    /**
     *
     // optimizationFunction
     // @描述: 解算方法
     //
     //			水平H ,垂直V
     //	     #  -- -- -- H -- -- -- #
     //	     |  B2(0,V)     B3(H,V) |
     //	     |  (10,11)     (20,21) |
     //	     V                      V
     //	     |  (00,01)     (30,31) |
     //	     |  B1(0,0)     B4(H,0) |
     //	     #  -- -- -- H -- -- -- #
     */
    private static Double optimizationFunction(double x, double y, Float[] distance, int[][] uwbPositions) {
            double f21 = 0,f31 = 0,f41 = 0;
        try {
            //基准点与待定点差值开方
            double baseDifference = Math.sqrt(x * x + y * y);
            // B2 - B1
             f21 = -(distance[1] - distance[0]) + Math.sqrt(x * x + (y - uwbPositions[1][1]) * (y - uwbPositions[1][1])) - baseDifference;
            // B3 - B1
             f31 = -(distance[2] - distance[0]) + Math.sqrt((x - uwbPositions[2][0]) * (x - uwbPositions[2][0]) + (y - uwbPositions[2][1]) * (y - uwbPositions[2][1])) - baseDifference;
            // B4 - B1
             f41 = -(distance[3] - distance[0]) + Math.sqrt((x - uwbPositions[3][0]) * (x - uwbPositions[3][0]) + y * y) - baseDifference;
        } catch (Exception e) {
            System.out.println("here!");
            e.printStackTrace();
        }
        return Math.abs(f21) + Math.abs(f31) + Math.abs(f41);

    }
    }


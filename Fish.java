import java.util.ArrayList;
import java.util.Collections;

public class Fish extends MovingGameElement {
    private String color;
    private int min_index;
    private final double RANGE_DISTANCE = 100;
    private int count = 0;

    public Fish(int pos_x, int pos_y, int target_x, int target_y, int speed, String color) {
        super(pos_x, pos_y, target_x, target_y, speed);

        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return getColor() + "Fish";
    }

    public void move(Board board) {
        super.setX_moveOptions(new ArrayList<Integer>());
        super.setY_moveOptions(new ArrayList<Integer>());
        super.setDistances(new ArrayList<Double>());


        calculPossibilities(board, getX_moveOptions(), getY_moveOptions());
        calculDistance(getDistances(), getX_moveOptions(), getY_moveOptions(), getTarget_x(), getTarget_y());

        if (getType().equals("orangeFish")) {
            min_index = getDistances().indexOf(Collections.min(getDistances()));
            setPos_x(getX_moveOptions().get(min_index));
            setPos_y(getY_moveOptions().get(min_index));
            return; //afin d'éviter de rentrer dans le for pour les autres poissons
        }

        for (MovingGameElement mvElemOther : board.movingGameElementList) {

            ArrayList<Integer> tempoX = new ArrayList<Integer>();
            ArrayList<Integer> tempoY = new ArrayList<Integer>();
            ArrayList<Double> tempoDistance = new ArrayList<Double>();

            if (getType().equals("redFish") && !mvElemOther.getType().equals("redFish")) {

                calculPossibilities(board, tempoX, tempoY);
                calculDistance(tempoDistance, tempoX, tempoY, mvElemOther.getPos_x(), mvElemOther.getPos_y());

                if (Collections.min(tempoDistance) < RANGE_DISTANCE && Collections.min(tempoDistance) < Collections.min(getDistances())) {
                    System.out.println("coco ");
                    setDistances(tempoDistance);
                    setX_moveOptions(tempoX);
                    setY_moveOptions(tempoY);
                }
            }
        }

        min_index = getDistances().indexOf(Collections.min(getDistances()));
        setPos_x(getX_moveOptions().get(min_index));
        setPos_y(getY_moveOptions().get(min_index));

//        if(getType().equals("blueFish")){
//            for (MovingGameElement mvElemOther : board.movingGameElementList) {
//                if (mvElemOther != this && mvElemOther.getType().equals("blueFish") || mvElemOther.getType().equals("purpleFish") ) {
//                    System.out.println("salut");
//                    ArrayList<Integer> tempoX = new ArrayList<Integer>();
//                    ArrayList<Integer> tempoY = new ArrayList<Integer>();
//                    ArrayList<Double> tempoDistance = new ArrayList<Double>();
//
//                    for (int i = -1; i <= 1; i++) {
//                        for (int j = -1; j <= 1; j++) {
//                            int test_pos_x = getPos_x() + i * getSpeed();
//                            int test_pos_y = getPos_y() + j * getSpeed();
//                            tempoX.add(test_pos_x);
//                            tempoY.add(test_pos_y);
//
//                        }
//                    }
//                    System.out.println(tempoX);
//                    for (int i = 0; i < tempoX.size(); i++) {
//                        Double distance = getDistance(mvElemOther.getPos_x(), mvElemOther.getPos_y(), tempoX.get(i), tempoY.get(i));
//                        tempoDistance.add(distance);
//                    }
//                    if (getDistances() == null || getDistances().isEmpty()) {
//                        setDistances(tempoDistance);
//                        setX_moveOptions(tempoX);
//                        setY_moveOptions(tempoY);
//
//                    }
//
//
//
//                    if ((getDistances() != null || !getDistances().isEmpty())) {
//                        if (Collections.min(tempoDistance) < Collections.min(getDistances())) {
//                            setDistances(tempoDistance);
//                            setX_moveOptions(tempoX);
//                            setY_moveOptions(tempoY);
//                        }
//                    }
//
//                    double min = Collections.min(getDistances());
//                    min_index = getDistances().indexOf(min);
//                }
//
//            }
//        }
//        else if(getType().equals("purpleFish")){
//            for (MovingGameElement mvElemOther : board.movingGameElementList) {
//                if (mvElemOther != this && mvElemOther.getType().equals("redFish")) {
//
//                    ArrayList<Integer> tempoX = new ArrayList<Integer>();
//                    ArrayList<Integer> tempoY = new ArrayList<Integer>();
//                    ArrayList<Double> tempoDistance = new ArrayList<Double>();
//
//                    for (int i = -1; i <= 1; i++) {
//                        for (int j = -1; j <= 1; j++) {
//                            int test_pos_x = getPos_x() + i * getSpeed();
//                            int test_pos_y = getPos_y() + j * getSpeed();
//                            if (board.isValidPosition(this, test_pos_x, test_pos_y)) {
//                                tempoX.add(test_pos_x);
//                                tempoY.add(test_pos_y);
//
//                            }
//
//                        }
//                    }
//
//                    for (int i = 0; i < tempoX.size(); i++) {
//                        Double distance = getDistance(mvElemOther.getPos_x(), mvElemOther.getPos_y(), tempoX.get(i), tempoY.get(i));
//                        tempoDistance.add(distance);
//                    }
//
//
//                    if (getDistances() == null || getDistances().isEmpty()) {
//                        setDistances(tempoDistance);
//                        setX_moveOptions(tempoX);
//                        setY_moveOptions(tempoY);
//                    }
//
//                    if ((getDistances() != null || !getDistances().isEmpty())) {
//                        if(Collections.min(tempoDistance)  < RANGE_DISTANCE){
//                            if (Collections.max(tempoDistance) < Collections.max(getDistances())) {
//                                setDistances(tempoDistance);
//                                setX_moveOptions(tempoX);
//                                setY_moveOptions(tempoY);
//                            }
//                            double max = Collections.max(getDistances());
//                            min_index = getDistances().indexOf(max);
//                        }
//                        else{
//                            tempoDistance.clear();
//
//                            for (int i = 0; i < getX_moveOptions().size(); i++) {
//                                Double distance = getDistance(getTarget_x(), getTarget_y(), getX_moveOptions().get(i), getY_moveOptions().get(i));
//                                tempoDistance.add(distance);
//                            }
//                            setDistances(tempoDistance);
//                            setX_moveOptions(tempoX);
//                            setY_moveOptions(tempoY);
//
//                            double min = Collections.min(getDistances());
//                            min_index = getDistances().indexOf(min);
//                        }
//                    }
//                }
//            }
//        }
    }

    public double getDistance(int pos_x0, int pos_y0, int pos_x1, int pos_y1) {
        int x_dist = pos_x1 - pos_x0;
        int y_dist = pos_y1 - pos_y0;
        return Math.sqrt(Math.pow(x_dist, 2) + Math.pow(y_dist, 2));
    }

    public void calculPossibilities(Board board, ArrayList<Integer> arrayListX, ArrayList<Integer> arrayListY) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {

                int test_pos_x = getPos_x() + i * getSpeed();
                int test_pos_y = getPos_y() + j * getSpeed();
                if (board.isValidPosition(this, test_pos_x, test_pos_y)) {
                    arrayListX.add(test_pos_x);
                    arrayListY.add(test_pos_y);
                }
            }
        }
    }

    public void calculDistance(ArrayList<Double> arraylistDistance, ArrayList<Integer> arrayListX, ArrayList<Integer> arrayListY, int targetX, int targetY) {
        for (int i = 0; i < arrayListX.size(); i++) {
            double distance = getDistance(targetX, targetY, arrayListX.get(i), arrayListY.get(i));
            arraylistDistance.add(distance);
        }
    }

    public void triggerAction(Board board) {
    }
}















    /*public void move(Board board) {
        super.setX_moveOptions(new ArrayList<Integer>());
        super.setY_moveOptions(new ArrayList<Integer>());
        super.setDistances(new ArrayList<Double>());

        for (MovingGameElement mvElemOther : board.movingGameElementList) {
            ArrayList<Integer> tempoX = new ArrayList<Integer>();
            ArrayList<Integer> tempoY = new ArrayList<Integer>();
            ArrayList<Double> tempoDistance = new ArrayList<Double>();

            calculDistanceWithPositions(board, mvElemOther, tempoX, tempoY, tempoDistance);
            verifyIfDistanceEmpty(tempoX, tempoY, tempoDistance);


            if (getType().equals("redFish")) {
                if (!mvElemOther.getType().equals("redFish")) {
                    if (!getDistances().isEmpty()) {
                        if (Collections.min(tempoDistance) < Collections.min(getDistances())) {
                            setDistances(tempoDistance);
                            setX_moveOptions(tempoX);
                            setY_moveOptions(tempoY);
                        }
                    }
                    double min = Collections.min(getDistances());
                    min_index = getDistances().indexOf(min);
                }
            }
            else if (getType().equals("blueFish")) {
                if (mvElemOther != this && mvElemOther.getType().equals("blueFish") || mvElemOther.getType().equals("purpleFish")) {
                    if (!getDistances().isEmpty()) {
                        if (Collections.min(tempoDistance) < Collections.min(getDistances())) {
                            setDistances(tempoDistance);
                            setX_moveOptions(tempoX);
                            setY_moveOptions(tempoY);
                        }
                    }
                    double min = Collections.min(getDistances());
                    min_index = getDistances().indexOf(min);
                }
            }
            else if (getType().equals("purpleFish")) {
                if (mvElemOther != this && mvElemOther.getType().equals("blueFish") || mvElemOther.getType().equals("purpleFish")) {
                    if (!getDistances().isEmpty()) {
                        if (Collections.min(tempoDistance) < RANGE_DISTANCE) {
                            if (Collections.max(tempoDistance) < Collections.max(getDistances())) {
                                setDistances(tempoDistance);
                                setX_moveOptions(tempoX);
                                setY_moveOptions(tempoY);
                            }
                            double max = Collections.max(getDistances());
                            min_index = getDistances().indexOf(max);
                        } else {
                            tempoDistance.clear();

                            for (int i = 0; i < getX_moveOptions().size(); i++) {
                                Double distance = getDistance(getTarget_x(), getTarget_y(), getX_moveOptions().get(i), getY_moveOptions().get(i));
                                tempoDistance.add(distance);
                            }
                            setDistances(tempoDistance);
                            setX_moveOptions(tempoX);
                            setY_moveOptions(tempoY);

                            double min = Collections.min(getDistances());
                            min_index = getDistances().indexOf(min);
                        }
                    }

                }
            }
            else {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int test_pos_x = getPos_x() + i * getSpeed();
                        int test_pos_y = getPos_y() + j * getSpeed();
                        if (board.isValidPosition(this, test_pos_x, test_pos_y)) {
                            getX_moveOptions().add(test_pos_x);
                            getY_moveOptions().add(test_pos_y);
                        }
                    }
                }

                for (int i = 0; i < getX_moveOptions().size(); i++) {
                    Double distance = getDistance(getTarget_x(), getTarget_y(), getX_moveOptions().get(i), getY_moveOptions().get(i));
                    getDistances().add(distance);
                }

                double min = Collections.min(getDistances());
                min_index = getDistances().indexOf(min);
            }
        }
        setPositionsFromArrays();
    }


    private void verifyIfDistanceEmpty(ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance) {
        if (getDistances().isEmpty()) {
            setX_moveOptions(tempoX);
            setY_moveOptions(tempoY);
            setDistances(tempoDistance);
        }
    }

    private void setPositionsFromArrays() {
        setPos_x(getX_moveOptions().get(min_index));
        setPos_y(getY_moveOptions().get(min_index));
    }

    private void calculDistanceWithPositions(Board board, MovingGameElement mvElemOther, ArrayList<Integer> tempoX, ArrayList<Integer> tempoY, ArrayList<Double> tempoDistance) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int test_pos_x = getPos_x() + i * getSpeed();
                int test_pos_y = getPos_y() + j * getSpeed();
                if (board.isValidPosition(this, test_pos_x, test_pos_y)) {
                    tempoX.add(test_pos_x);
                    tempoY.add(test_pos_y);
                }
            }
        }

        for (int i = 0; i < tempoX.size(); i++) {
            Double distance = getDistance(mvElemOther.getPos_x(), mvElemOther.getPos_y(), tempoX.get(i), tempoY.get(i));
            tempoDistance.add(distance);
        }
    }
     */
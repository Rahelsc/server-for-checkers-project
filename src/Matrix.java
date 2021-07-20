
import java.io.Serializable;
import java.util.*;

public class Matrix implements Serializable {

    int[][] primitiveMatrix; // 2d array
    private static HashMap<Integer, int[]> enemyPositions = new HashMap<>();


    public Matrix(int[][] oArray){
        List<int[]> list = new ArrayList<>();
        for (int[] row : oArray) {
            int[] clone = row.clone();
            list.add(clone);
        }
        primitiveMatrix = list.toArray(new int[0][]);
    }

    public void printMatrix(){
        for (int[] row : primitiveMatrix) {
            String s = Arrays.toString(row);
            System.out.println(s);
        }
    }

    public final int[][] getPrimitiveMatrix() {
        return primitiveMatrix;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] row : primitiveMatrix) {
            stringBuilder.append(Arrays.toString(row));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public Collection<int[]> getNeighbors(final int[] index, int offSet){
        Collection<int[]> list = new ArrayList<>();
        int extracted = -1;
        try{
            extracted = primitiveMatrix[index[0]+offSet][index[1]-1];
            if (extracted == 0)
                list.add(new int[]{index[0]+offSet,index[1]-1});
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = primitiveMatrix[index[0]+offSet][index[1]+1];
            if (extracted == 0)
                list.add(new int[]{index[0]+offSet,index[1]+1});
        }catch (ArrayIndexOutOfBoundsException ignored){}

        return list;
    }

    public Collection<int[]> getNeighborsToEat(final int[] index, int offSet, int enemyOffset){
        Collection<int[]> list = new ArrayList<>();
        enemyPositions.clear();
        int potentialEnemy = -1;
        try{
            if (primitiveMatrix[index[0] + offSet] [index[1] - 2] == 0) {
                potentialEnemy = primitiveMatrix[index[0] + enemyOffset][index[1] - 1];
                if (potentialEnemy != 0 && potentialEnemy != primitiveMatrix[index[0]][index[1]]){
                    list.add(new int[]{index[0] + offSet,index[1] - 2});
                    enemyPositions.put(((index[0] + offSet)*10+(index[1] - 2)), new int[]{index[0] + enemyOffset,index[1] - 1});
                }
            }

        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            if (primitiveMatrix[index[0] + offSet] [index[1] + 2] == 0) {
                potentialEnemy = primitiveMatrix[index[0] + enemyOffset][index[1] + 1];
                if (potentialEnemy != 0 && potentialEnemy != primitiveMatrix[index[0]][index[1]]){
                    list.add(new int[]{index[0] + offSet,index[1] + 2});
                    enemyPositions.put(((index[0] + offSet)*10+(index[1] + 2)), new int[]{index[0] + enemyOffset,index[1] + 1});
                }
            }
        }catch (ArrayIndexOutOfBoundsException ignored){}

        return list;
    }

    public int[] getPossibleEnemiesPosition(int newCalculatedLocation){
        return enemyPositions.get(newCalculatedLocation);
    }

    public int getValue(int[] index) {
        return primitiveMatrix[index[0]][index[1]];
    }
}
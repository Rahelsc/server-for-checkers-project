import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class handles Matrix-related tasks
 */
class MatrixIHandler implements IHandler {
    private Matrix matrix;
    private volatile boolean doWork = true;


    @Override
    public void resetMembers() {
        this.matrix = null;
        this.doWork = true;
    }

    @Override
    public void handle(InputStream fromClient, OutputStream toClient) throws IOException, ClassNotFoundException {
        /*
        Send data as bytes.
        Read data as bytes then transform to meaningful data
        ObjectInputStream and ObjectOutputStream can read and write both primitives and objects
         */
        ObjectInputStream objectInputStream = new ObjectInputStream(fromClient);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(toClient);

        boolean doWork = true;
        // handle client's tasks
        while(doWork){
            /*
            Use-cases:
            if client send the command "matrix"
            - Client sends 2d array -> server will create a Matrix object.
            if client send the command "getNeighbors"
            - Client sends an Index and wishes to get an List of neighbors
            if client send the command "getReachables"
            - Client sends an Index and wishes to get an List of reachable indices
            //TODO:
            - Client sends a start & end index and wishes to get all possible routes between them
             */

            switch (objectInputStream.readObject().toString()){
                case "matrix":{
                    // client will now send a 2d array. handler will create a matrix object
                    int[][] tempArray = (int[][])objectInputStream.readObject();
                    System.out.println("Server: Got 2d array");
                    this.matrix = new Matrix(tempArray);
                    this.matrix.printMatrix();
                    break;
                }

                case "getNeighbors":{
                    // handler will receive an index, then compute its neighbors
                    int [] t = (int[])objectInputStream.readObject();
                    int[] tempIndex = new int[]{t[0], t[1]};
                    int offSet = (int)objectInputStream.readObject();

                    if(this.matrix!=null){
                        List<int[]> neighbors = new ArrayList<>(this.matrix.getNeighbors(tempIndex, offSet));
                        System.out.println("Server: neighbors of "+ Arrays.toString(tempIndex) + ":  ");
                        neighbors.forEach(arr-> System.out.println(Arrays.toString(arr)));
                        // send to socket's outputStream
                        objectOutputStream.writeObject(neighbors);
                    }
                    break;
                }

                case "getNeighborsToEat":{
                    // handler will receive an index, then compute its neighbors
                    int [] t = (int[])objectInputStream.readObject();
                    int[] tempIndex = new int[]{t[0], t[1]};
                    int offSet = (int)objectInputStream.readObject();
                    int enemyOffSet = (int)objectInputStream.readObject();

                    if(this.matrix!=null){
                        List<int[]> neighborsToEat = new ArrayList<>(this.matrix.getNeighborsToEat(tempIndex, offSet, enemyOffSet));
                        System.out.println("Server: NeighboursToEat of "+ Arrays.toString(tempIndex) + ":  ");
                        neighborsToEat.forEach(arr-> System.out.println(Arrays.toString(arr)));

                        // send to socket's outputStream
                        objectOutputStream.writeObject(neighborsToEat);
                    }
                }

                case "getPossibleEnemiesPosition":{
                    Object objectFromUser = objectInputStream.readObject();
                    int newPosition = 0;
                    // only do actions if data sent is int.
                    // this check must be performed because earlier address to this `objectInputStream` include applying the "toString" method to it,
                    // meaning we expect it to be an object
                    // and since int is not an object, we need to check if it is an int before casting it,
                    // otherwise it throws an error
                    if (objectFromUser instanceof Integer) {
                        newPosition = (int) objectFromUser;

                        if (this.matrix != null) {
                            int[] possibleEnemies = this.matrix.getPossibleEnemiesPosition(newPosition);
                            System.out.println("Server: possibleEnemies of: " + newPosition + ":  " + Arrays.toString(possibleEnemies));
                            // send to socket's outputStream
                            objectOutputStream.writeObject(possibleEnemies);
                        }
                    }
                }


                case "stop":{
                    doWork = false;
                    break;
                }
            }
        }





    }


}

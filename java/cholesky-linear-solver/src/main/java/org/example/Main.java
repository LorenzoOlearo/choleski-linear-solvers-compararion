package org.example;

import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.data.DMatrixSparseTriplet;
import org.ejml.interfaces.linsol.LinearSolverSparse;
import org.ejml.ops.DConvertMatrixStruct;
import org.ejml.sparse.FillReducing;
import org.ejml.sparse.csc.decomposition.chol.CholeskyUpLooking_DSCC;
import org.ejml.sparse.csc.factory.LinearSolverFactory_DSCC;
import org.ejml.sparse.csc.linsol.chol.LinearSolverCholesky_DSCC;
import us.hebi.matlab.mat.ejml.Mat5Ejml;
import us.hebi.matlab.mat.format.Mat5;
import us.hebi.matlab.mat.types.Sparse;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

        //Load A

        Sparse value = Mat5.readFromFile(args[0])
                .getStruct("Problem")
                .getSparse("A");


        DMatrixSparseCSC A = new DMatrixSparseCSC(value.getNumRows(), value.getNumCols(), value.getNumNonZero());

        A = Mat5Ejml.convert(value, A);
        A.nz_length = value.getNumNonZero();


/*
        //load fake A

        var workA = new DMatrixSparseTriplet(4, 4, 6);
        workA.addItem(0,0,1);
        workA.addItem(1,0,1);
        workA.addItem(0,1,1);
        workA.addItem(1,1,22);
        workA.addItem(2,2,1);
        workA.addItem(3,3,1);

        DMatrixSparseCSC A = DConvertMatrixStruct.convert(workA, (DMatrixSparseCSC)null);


 */


        //load B
        //TODO: load the correct B
        /*
        var workB = new DMatrixSparseTriplet(A.getNumCols(), 1, A.getNumCols());
        for (int i = 0; i < A.getNumCols(); i++) {
            workB.addItem(i, 0, 1);
        }
        // convert into a format that's easier to perform math with
        DMatrixSparseCSC B = DConvertMatrixStruct.convert(workB, (DMatrixSparseCSC)null);

         */


        DMatrixSparseCSC B = new DMatrixSparseCSC(A.getNumCols(), 1);
        for (int i = 0; i < B.getNumRows(); i++) {
            B.set(i, 0, 1.0);
        }

        DMatrixSparseCSC X = new DMatrixSparseCSC(1, A.getNumCols());

        CholeskyUpLooking_DSCC decomposition = new CholeskyUpLooking_DSCC();
        decomposition.decompose(A);

        LinearSolverCholesky_DSCC solver = new LinearSolverCholesky_DSCC(decomposition, null);

        solver.setA(A);

        solver.solveSparse(B, X);

        X.print();
        System.out.println();

    }
}
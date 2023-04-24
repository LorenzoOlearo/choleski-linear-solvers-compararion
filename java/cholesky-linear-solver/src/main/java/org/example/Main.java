package org.example;

import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

        DMatrixSparseCSC A = Utils.loadMatrix(args[0]);
        DMatrixRMaj B = Utils.computeB(A);
        DMatrixRMaj X = SystemSolver.CholeskySolve(A, B);

        System.out.println(Utils.computeError(X, Utils.computeXe(A)));

    }
}
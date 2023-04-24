package org.example;

import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.sparse.csc.decomposition.chol.CholeskyUpLooking_DSCC;
import org.ejml.sparse.csc.linsol.chol.LinearSolverCholesky_DSCC;

public class SystemSolver {

    public static DMatrixRMaj CholeskySolve(DMatrixSparseCSC A, DMatrixRMaj B) {

        DMatrixRMaj X = new DMatrixRMaj(1, A.getNumCols());

        CholeskyUpLooking_DSCC decomposition = new CholeskyUpLooking_DSCC();

        LinearSolverCholesky_DSCC solver = new LinearSolverCholesky_DSCC(decomposition, null);

        solver.setA(A);

        solver.solve(B, X);

        return X;

    }

    public static DMatrixSparseCSC CholeskySolve(DMatrixSparseCSC A, DMatrixSparseCSC B) {

        DMatrixSparseCSC X = new DMatrixSparseCSC(1, A.getNumCols());

        CholeskyUpLooking_DSCC decomposition = new CholeskyUpLooking_DSCC();

        LinearSolverCholesky_DSCC solver = new LinearSolverCholesky_DSCC(decomposition, null);

        solver.setA(A);

        solver.solveSparse(B, X);

        return X;

    }

}

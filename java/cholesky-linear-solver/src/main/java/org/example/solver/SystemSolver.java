package org.example.solver;

import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.sparse.csc.decomposition.chol.CholeskyUpLooking_DSCC;
import org.ejml.sparse.csc.linsol.chol.LinearSolverCholesky_DSCC;

public class SystemSolver {

    public static DMatrixRMaj CholeskySolve(DMatrixSparseCSC A, DMatrixRMaj B) throws SystemSolverException{

        DMatrixRMaj X = new DMatrixRMaj(1, A.getNumCols());

        CholeskyUpLooking_DSCC decomposition = new CholeskyUpLooking_DSCC();

        LinearSolverCholesky_DSCC solver = new LinearSolverCholesky_DSCC(decomposition, null);

        if(!solver.setA(A))
            throw new SystemSolverException("Matrix can't be decomposed with Cholesky");

        solver.solve(B, X);

        return X;

    }

    public static DMatrixSparseCSC CholeskySolve(DMatrixSparseCSC A, DMatrixSparseCSC B) throws SystemSolverException {

        DMatrixSparseCSC X = new DMatrixSparseCSC(1, A.getNumCols());

        CholeskyUpLooking_DSCC decomposition = new CholeskyUpLooking_DSCC();

        LinearSolverCholesky_DSCC solver = new LinearSolverCholesky_DSCC(decomposition, null);

        if(!solver.setA(A))
            throw new SystemSolverException("Matrix can't be decomposed with Cholesky");

        solver.solveSparse(B, X);

        return X;

    }

}

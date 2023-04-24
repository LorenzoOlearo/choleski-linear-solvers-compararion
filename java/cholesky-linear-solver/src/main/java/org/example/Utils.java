package org.example;

import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.sparse.csc.CommonOps_DSCC;
import us.hebi.matlab.mat.ejml.Mat5Ejml;
import us.hebi.matlab.mat.format.Mat5;
import us.hebi.matlab.mat.types.Sparse;

import java.io.IOException;

public class Utils {

    public static DMatrixSparseCSC loadMatrix(String filename) throws IOException {
        Sparse value = Mat5.readFromFile(filename)
                .getStruct("Problem")
                .getSparse("A");


        DMatrixSparseCSC A = new DMatrixSparseCSC(value.getNumRows(), value.getNumCols(), value.getNumNonZero());

        A = Mat5Ejml.convert(value, A);
        A.nz_length = value.getNumNonZero();
        return A;
    }

    public static DMatrixRMaj computeB(DMatrixSparseCSC A) {
        DMatrixRMaj B = new DMatrixRMaj(A.getNumCols(), 1);

        DMatrixRMaj Xe = new DMatrixRMaj(1, A.getNumRows());
        for (int i = 0; i < Xe.getNumCols(); i++) {
            Xe.set(0, i, 1.0D);
        }
        CommonOps_DSCC.multTransB(A, Xe, B, null);

        return B;
    }

}

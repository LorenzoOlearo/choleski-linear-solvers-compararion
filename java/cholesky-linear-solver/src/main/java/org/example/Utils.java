package org.example;

import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.dense.row.NormOps_DDRM;
import org.ejml.dense.row.SpecializedOps_DDRM;
import org.ejml.sparse.csc.CommonOps_DSCC;
import us.hebi.matlab.mat.ejml.Mat5Ejml;
import us.hebi.matlab.mat.format.Mat5;
import us.hebi.matlab.mat.types.Sparse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

        DMatrixRMaj Xe = computeXe(A);
        CommonOps_DSCC.mult(A, Xe, B);

        return B;
    }

    public static double computeError(DMatrixRMaj X, DMatrixRMaj Target) {
        return SpecializedOps_DDRM.diffNormF(X, Target) / NormOps_DDRM.normF(Target);
    }

    public static DMatrixRMaj computeXe(DMatrixSparseCSC A) {
        DMatrixRMaj Xe = new DMatrixRMaj(A.getNumRows(), 1);
        for (int i = 0; i < Xe.getNumRows(); i++) {
            Xe.set(i, 0, 1.0D);
        }
        return Xe;
    }

    public static void writeProfilesToCSV(ArrayList<Profile> profiles, String filename) throws IOException {
        writeProfilesToCSV(profiles, filename, false);
    }

    public static void writeProfilesToCSV(ArrayList<Profile> profiles, String filename, Boolean append) throws IOException {

        StringBuilder string = new StringBuilder();

        File f = new File(filename);
        if(!f.exists() || !append)
            string.append("A,size,NNZ,time,memory_usage,relative_error,host,platform,runtime_version,library,library_version\n");

        for(Profile profile : profiles) {
            string.append(profile).append("\n");
        }

        try (FileWriter writer = new FileWriter(f, append)) {
            writer.write(string.toString());
        } catch (IOException e) {
            throw e;
        }


    }

}

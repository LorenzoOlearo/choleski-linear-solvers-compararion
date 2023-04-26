package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Main {

    private static String[] matrices = {
            //"Flan_1565",      //Out of memory SIGKILL
            //"StocF-1465",     //Negative array length??
            "cfd1",             //Ok
            "cfd2",             //Ok
            //"apache2",        //Java heap space out of memory
            //"G3_circuit",     //Negative array length??
            //"parabolic_fem",  //Java heap space out of memory
            "shallow_water1",   //Ok
            "ex15"              //Ok
    };

    private static Configuration configuration;

    public static void main(String[] args) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        configuration = objectMapper.readValue(new FileReader(args[0]), Configuration.class);

        ArrayList<Profile> profiles = new ArrayList<>();

        for (String mat : configuration.getMatrices()) {
            System.out.println("---");
            System.out.println(mat);
            DMatrixSparseCSC A = Utils.loadMatrix(configuration.getMatrices_path() + mat + ".mat");
            DMatrixRMaj B = Utils.computeB(A);
            Profile p = profile(A,B);
            p.setName(mat + ".mat");
            profiles.add(p);
        }

        Utils.writeProfilesToCSV(profiles, "report-" + configuration.getHost() + "-" + configuration.getPlatform() + ".csv");

    }

    private static Profile profile(DMatrixSparseCSC A, DMatrixRMaj B) {
        Profile profile = new Profile();
        profile.setPlatform(configuration.getPlatform());
        profile.setHost(configuration.getHost());

        profile.setNumOfNonZero(A.getNonZeroLength());
        profile.setSize(A.getNumCols());

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();

        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();

        DMatrixRMaj X = SystemSolver.CholeskySolve(A, B);

        long endTime = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

        profile.setMemoryUsage(usedMemoryAfter-usedMemoryBefore);
        profile.setRuntime(endTime-startTime);
        profile.setRelativeError(Utils.computeError(X, Utils.computeXe(A)));

        System.out.println("Memory increased: " + profile.getMemoryUsage() + " B");
        System.out.println("Runtime: " + profile.getRuntime() + " ns");
        System.out.println("Relative Error: " + profile.getRelativeError());
        System.out.println("---");
        return profile;
    }
}
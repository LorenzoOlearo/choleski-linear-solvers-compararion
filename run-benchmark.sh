echo RUNNING JAVA --------------------------
echo
FILE=./java/cholesky-linear-solver.jar
if [ ! -f "$FILE" ]; then
    echo "$FILE not found. Generating jar file..."
    cd ./java/cholesky-linear-solver/
    mvn clean compile assembly:single
    cp ./target/cholesky-linear-solver.jar ../cholesky-linear-solver.jar
    cd ../../
fi
java -jar ./java/cholesky-linear-solver.jar ./java/config.json
echo
echo RUNNING PYTHON ------------------------
echo
python3 ./python/main.py --json ./python/config.json
echo
echo RUNNING MATLAB ------------------------
echo
cd matlab
matlab -batch "main('./config.json'); exit;"
cd ..
echo
echo RUNNING JULIA ------------------------
echo
julia ./julia/main.jl ./julia/config.json
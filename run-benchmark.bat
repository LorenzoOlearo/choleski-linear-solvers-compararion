@ echo off
echo RUNNING JAVA --------------------------
echo.
IF NOT EXIST .\java\cholesky-linear-solver.jar (
    cd java\cholesky-linear-solver\
    call mvn -B clean package assembly:single
    copy .\target\cholesky-linear-solver.jar ..\cholesky-linear-solver.jar
    cd ..\..\
)

java -jar .\java\cholesky-linear-solver.jar .\java\config.json
echo.
echo RUNNING PYTHON ------------------------
echo.
python .\python\main.py --json .\python\config.json
echo.
echo RUNNING MATLAB ------------------------
echo.
cd matlab
matlab -batch "main('.\config.json'); exit;"
cd ..
echo.
echo RUNNING JULIA -------------------------
echo.
julia .\julia\main.jl .\julia\config.json
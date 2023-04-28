@ echo off
echo RUNNING JAVA --------------------------
echo.
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
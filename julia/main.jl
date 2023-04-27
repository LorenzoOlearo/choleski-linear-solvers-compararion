using MAT
using JSON
using CSV
using DataFrames
using LinearAlgebra
using Profile
using SparseArrays

function choleskySolve(A, b) 
    R = cholesky(A)
    return R\b
end

configFile = ARGS[1]

config = JSON.parsefile(configFile)

profiles = DataFrame(A = [],size = [],NNZ = [],time = [],memory_usage = [],relative_error = [],host = [],platform = [],runtime_version = [],library = [],library_version = [])

for mat in config["matrices"]
    println("Computing: "*mat)

    file = matopen(joinpath(config["matrices_path"], mat)) 
    A = read(file, "Problem")["A"] 
    
    xe = ones(size(A, 1), 1)
    b = A*xe
    try
        memory_usage = @allocated elapsed_time = @elapsed x = choleskySolve(A, b)

        relativeError = norm(x - xe) / norm(xe)
        push!(profiles, [mat, size(A, 1), nnz(A), elapsed_time, memory_usage/1E6, relativeError, config["host"], config["platform"], VERSION, "LinearAlgebra", VERSION])

    catch error
        println("    └──Skipped")
    end


    
end

CSV.write(joinpath(config["output_path"], "report-"*config["host"]*"-"*config["platform"]*"-Julia.csv"), profiles)

function X = choleskysolve(A, B) 
    R = chol(A);
    X = R\(R'\B);
end
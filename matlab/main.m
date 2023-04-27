function main(config_file)
configuration = jsondecode(fileread(config_file));

matrices = cell(size(configuration.matrices));
elapsed = cell(size(configuration.matrices));
relative_error = cell(size(configuration.matrices));


for i = 1:size(configuration.matrices, 1)
    
    A = loadsparse(append(configuration.matrices_path, filesep, configuration.matrices{i}));
    B = computeB(A);
    tic
    X = choleskysolve(A,B);
    elapsed{i} = toc;
    Xe = computeXe(A);
    relative_error{i} = norm(X-Xe)/norm(Xe);
    
    matrices{i} = configuration.matrices{i};
    clear A B X Xe;

end


end


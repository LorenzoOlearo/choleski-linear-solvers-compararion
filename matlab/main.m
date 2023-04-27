
function main(config_file)
    configuration = jsondecode(fileread(config_file));
    
    matrices = cell(size(configuration.matrices));
    elapsed = cell(size(configuration.matrices));
    relative_error = cell(size(configuration.matrices));
    
    
    for i = 1:size(configuration.matrices, 1)
        disp(append('Computing: ', configuration.matrices{i}))
        A = loadsparse(append(configuration.matrices_path, filesep, configuration.matrices{i}));
        b = computeB(A);
        xe = computeXe(A);
        
        try
            tic
            x = choleskysolve(A,b);
            clear A
            elapsed{i} = toc;
            relative_error{i} = norm(x-xe)/norm(xe);
        
            matrices{i} = configuration.matrices{i};
        catch ME
            disp('    └──Skipped')
        end
    
    
        clear A b x xe ans;
    
    end


end


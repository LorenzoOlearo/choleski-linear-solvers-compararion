
function main(config_file)

    config = jsondecode(fileread(config_file));

    report = table([], [], [], [], [], [], [], [], [], [], [], 'VariableNames', {'A', 'size', 'NNZ', 'time', 'memory_usage', 'relative_error', 'host', 'platform', 'runtime_version', 'library', 'library_version'});
    host = config.host;
    platform = config.platform;
    runtime_version = version();
    library = "MATLAB";
    library_version = version();


    for i = 1:size(config.matrices, 1)
        disp(append('Computing: ', config.matrices{i}))
        A = loadsparse(append(config.matrices_path, filesep, config.matrices{i}));
        b = computeb(A);
        xe = computexe(A);
        
        try
            profile clear;
            profile('-memory','on');
            setpref('profiler','showJitLines',1);

            tic
            x = choleskysolve(A,b);
            elapsed = toc;
            
            matrix_name = config.matrices{i};
            matrix_size = size(A, 1);
            matrix_nnz = nnz(A);
            relative_error = norm(x-xe) / norm(xe);

            profilerInfo = profile('info');
            functionNames = {profilerInfo.FunctionTable.FunctionName};
            functionIndexInNamesTable = find(strcmp(functionNames(:), 'choleskysolve'));
            memory_usage = (profilerInfo.FunctionTable(functionIndexInNamesTable).PeakMem) / 1000000;

            new_line = {matrix_name, matrix_size, matrix_nnz, elapsed, memory_usage, relative_error, host, platform, runtime_version, library, library_version};
            report = [report ; new_line];


        catch ex
            disp('    └──Skipped')
            disp(ex)
        end
    
    
        clear A b x xe;
    
    end

    report_filename = append("report", "-", config.host, "-", config.platform, "-MATLAB.csv");
    report_path = append(config.output_path, filesep, report_filename);
    writetable(report, report_path);

end


import numpy as np
import os
import json
import time
import psutil
import scipy.io as sio
import csv
import pandas as pd
import argparse

from os.path import dirname, join as pjoin
from sksparse.cholmod import cholesky
from memory_profiler import profile
from memory_profiler import memory_usage
from platform import python_version
from scipy import __version__ as scipy_version



@profile 
def sparse_matrix_solver(A, b):
    try:
        start = time.time_ns()
        factor = cholesky(A)
        end = time.time_ns()
        elapsed = round(end - start, 3) / 10e3
        x = factor(b)
    except Exception as e:
        print('Cholesky decomposition failed:' + str(e))
        x = np.zeros(A.shape[0])
        elapsed = -1
    
    return [x, elapsed]



def load_sparse_matrix(config, filename):
    mat = sio.loadmat(os.path.join(config['matrices_path'], filename), struct_as_record=False)
    sparse_mat = mat['Problem'][0][0].A

    return sparse_mat 



def main():

    # Get current working directory and parent directory
    working_dir = os.path.dirname(os.path.abspath(__file__))

    # Load config file
    config_file = os.path.join(working_dir, 'config.json')

    parser = argparse.ArgumentParser()
    parser.add_argument('--json', help='Path to the json config file', default=config_file)
    args = parser.parse_args()
    config_file = args.json

    with open(config_file, 'r') as f:
        config = json.load(f)
        
    # Create report file
    output_file_path = os.path.join(config['output_path'], ('report-' + config['host'] + '-'+ config['platform'] + '-Python.csv'))
    with open(output_file_path, 'w+') as file:
        writer = csv.writer(file)
        writer.writerow(["A",
                         "size",
                         "NNZ",
                         "time",
                         "memory_usage",
                         "relative_error",
                         "host",
                         "platform",
                         "runtime_version",
                         "library",
                         "library_version"])
        
        # For each matrix in the folder, apply the Cholesky decomposition and solve the linear system
        start_total = time.time()
        print('Matrix path: ' + config['matrices_path'])
        print('Available matrices:' + str(os.listdir(config['matrices_path'])))
        print('Starting computation...')

        for filename in config['matrices']:
                print('Computing ' + filename)
                sparse_mat = load_sparse_matrix(config, filename)

                # TEMPORARY: will be later replaced the proper b vector
                b = np.ones(sparse_mat.shape[0])

                # Solve the linear system
                x, elapsed = sparse_matrix_solver(sparse_mat, b)
                mem_usage = memory_usage((sparse_matrix_solver, (sparse_mat, b)))
                mem_usage = round(max(mem_usage))

                # Computes the relative error
                rel_err = np.linalg.norm(sparse_mat.dot(x) - b) / np.linalg.norm(b)
                
                writer.writerow([filename,
                                 sparse_mat.shape[0],
                                 sparse_mat.nnz,
                                 elapsed,
                                 mem_usage,
                                 rel_err,
                                 config['host'],
                                 config['platform'],
                                 python_version(),
                                 "scipy",
                                 scipy_version])
                
        end_total = time.time()
        elapsed_total = round(end_total - start_total, 3)
        print('Total time: ' + str(elapsed_total) + ' seconds')
        
    
    report_df = pd.read_csv(output_file_path, sep=',')
    print(report_df)

    
    
if __name__ == '__main__':
    main()

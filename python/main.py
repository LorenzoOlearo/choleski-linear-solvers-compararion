import numpy as np
import os
import json
import time
import psutil
import scipy.io as sio
import csv
import pandas as pd

from os.path import dirname, join as pjoin
from sksparse.cholmod import cholesky
from memory_profiler import profile
from memory_profiler import memory_usage


@profile 
def sparse_matrix_solver(A, b):
    try: 
        factor = cholesky(A)
        x = factor(b)
    except Exception as e:
        print('Cholesky decomposition not supported: ' + str(e))
        x = np.zeros(A.shape[0]) 
    
    return x


def load_sparse_matrix(config, filename):
    mat = sio.loadmat(os.path.join(config['matrices_path'], filename), struct_as_record=False)
    sparse_mat = mat['Problem'][0][0].A
    
    return sparse_mat 
    
    


def main():
   
    # Get current working directory and parent directory 
    working_dir = os.path.dirname(os.path.abspath(__file__))
    parent_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
   
    # Load config file 
    config_file = os.path.join(parent_dir, 'config.json')
    with open(config_file, 'r') as f:
        config = json.load(f)
        
    # Create report file     
    report_path = os.path.join(working_dir, 'report-' + config['host'] + '-' +config['platform'] + '.csv')
    with open(report_path, 'w+') as file:
        writer = csv.writer(file)
        writer.writerow(["A", "Size", "NNZ", "Time", "MEM", "RelErr", "host", "platform"])
        
        # For each matrix in the folder, apply the Cholesky decomposition and solve the linear system
        start = time.time()
        print('Matrix path: ' + config['matrices_path'])
        print('Available matrices:' + str(os.listdir(config['matrices_path'])))
        print('Starting computation...')
        for filename in os.listdir(config['matrices_path']):
            if filename.endswith(".mat"):
                print('Computing ' + filename)
                sparse_mat = load_sparse_matrix(config, filename)

                # TEMPORARY: will be later replaced the proper b vector
                b = np.ones(sparse_mat.shape[0])

                start_iter = time.time()
                x = sparse_matrix_solver(sparse_mat, b)
                end_iter = time.time()
                elapsed_iter = round(end_iter - start_iter, 3)

                mem_usage = memory_usage(-1, interval=.2, timeout=1, backend="psutil")
                mem_usage = round(np.mean(mem_usage), 2)

                writer.writerow([filename,
                                 sparse_mat.shape[0],
                                 sparse_mat.nnz,
                                 elapsed_iter,
                                 mem_usage,
                                 0,
                                 config['host'],
                                 config['platform']])
        end = time.time()
        elapsed = round(end - start, 3)
        print('Total time: ' + str(elapsed) + ' seconds')
        
    
    
if __name__ == '__main__':
    main()

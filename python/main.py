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


def load_sparse_matrix(config):
    print('matrices folder: ' + config['matrices_path'])
    print(os.listdir(os.path.join(config['matrices_path'])))
    
    # Load the first sample .mat matrix 
    mat = sio.loadmat(os.path.join(config['matrices_path'], 'apache2.mat'), struct_as_record=False)
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
        
        A = load_sparse_matrix(config) 
       
        # TEMPORARY: will be later replaced the proper b vector 
        b = np.ones(A.shape[0])
        
        start = time.time()   
        x = sparse_matrix_solver(A, b)
        end = time.time()
        elapsed = round(end - start, 3)
       
        mem_usage = memory_usage(-1, interval=.2, timeout=1, backend="psutil")
        mem_usage = round(np.mean(mem_usage), 2)
        
        writer.writerow(['ex15', A.shape[0], A.nnz, elapsed, mem_usage, 0, config['host'], config['platform']]) 
    
    
    
if __name__ == '__main__':
    main()
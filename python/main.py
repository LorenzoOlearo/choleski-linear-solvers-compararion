import numpy as np
import os
import json
import scipy.io as sio

from os.path import dirname, join as pjoin
from sksparse.cholmod import cholesky

   
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
   
    # Read config file 
    parent_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
    config_file = os.path.join(parent_dir, 'config.json')
    with open(config_file, 'r') as f:
        config = json.load(f)
        
   
    A = load_sparse_matrix(config) 
    
    b = np.ones(A.shape[0])
    
    x = sparse_matrix_solver(A, b)
    
    print(x)
    
    
    a = 1
    
    
    
if __name__ == '__main__':
    main()
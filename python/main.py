import numpy as np
import os
import json
import scipy.io as sio

from scipy.linalg import cholesky
from os.path import dirname, join as pjoin
    



def main():
   
    # Read config file 
    parent_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
    config_file = os.path.join(parent_dir, 'config.json')
    with open(config_file, 'r') as f:
        config = json.load(f)
    print('matrices folder: ' + config['matrices_path'])
   
    # List all the available matrices
    print(os.listdir(os.path.join(config['matrices_path'])))
    
    # Load the first sample .mat matrix 
    cfd2_mat = sio.loadmat(os.path.join(config['matrices_path'], 'cfd2.mat'), struct_as_record=False)
    sparse_matrix = cfd2_mat['Problem'][0][0].A
    
    print(sparse_matrix)
    
    # cfd2_dec_low = cholesky(cfd2_mat, lower=True)
    
    
    
    
if __name__ == '__main__':
    main()
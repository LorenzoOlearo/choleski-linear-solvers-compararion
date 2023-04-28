import os
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt


def main():

    # Load each report file in the ./report directory into a dataframe
    report_dir = './reports'
    report_files = os.listdir(report_dir)

    reports_df = pd.DataFrame()

    print(report_files)

    for report_file in report_files:
        if report_file != 'report-MACHINE-PLATFORM-Runtime.csv' and report_file != 'summary.csv':
            report = pd.read_csv(os.path.join(report_dir, report_file), sep=',')

            reports_df = pd.concat([reports_df, report], ignore_index=True)

    print(reports_df)

    # Write the resulting dataframe to a csv file, overwrite if already exists
    reports_df.to_csv('./reports/summary.csv', sep=',', index=True)



if __name__ == '__main__':
    main()

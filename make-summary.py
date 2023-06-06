import os
import seaborn as sns
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt


def clean_data(df):
    df = df.sort_values(by=['A', 'memory_usage', 'host', 'platform'], ascending=[True, True, True, True], ignore_index=True)
    df['memory_usage'] = df['memory_usage'].round(3)
    df['time'] = df['time'].round(3)

    return df


def make_plot(df, host):

    sns.set_theme()


    # TODO: filter the dataframe to only include the specified host
    # df = df[df['host'] == host]

    # Group the DataFrame by the "A" column
    grouped = df.groupby("A")

    # Iterate over each group and create a separate plot for each matrix
    for name, group in grouped:
        name = name.replace('.mat', '')
        fig, subfig = plt.subplots(nrows=1, ncols=3, figsize=(20, 5))
        fig.suptitle(f"{name}", fontsize=16)

        if group[group['library'] == 'MATLAB'].empty == True:
            group = pd.concat([group, pd.DataFrame({'library': ['MATLAB'], 'memory_usage': [0], 'time': [0], 'relative_error': [0]})], ignore_index=True)
        if group[group['library'] == 'scipy'].empty == True:
            group = pd.concat([group, pd.DataFrame({'library': ['scipy'], 'memory_usage': [0], 'time': [0], 'relative_error': [0]})], ignore_index=True)
        if group[group['library'] == 'LinearAlgebra'].empty == True:
            group = pd.concat([group, pd.DataFrame({'library': ['LinearAlgebra'], 'memory_usage': [0], 'time': [0], 'relative_error': [0]})], ignore_index=True)
        if group[group['library'] == 'org.ejml'].empty == True:
            group = pd.concat([group, pd.DataFrame({'library': ['org.ejml'], 'memory_usage': [0], 'time': [0], 'relative_error': [0]})], ignore_index=True)


        subfig[0].set_title('Time [seconds]')
        ax = sns.barplot(data=group, x='library', y='time', ax=subfig[0], hue='platform', errorbar=None, palette='hls').set_yscale('log')
        subfig[0].set_xticklabels(['Julia-LinearAlgebra', 'Python-scipy', 'Java-ejml', 'MATLAB'], rotation=15)
        for p in subfig[0].patches:
            subfig[0].annotate(format(p.get_height(), '.2f'), (p.get_x() + p.get_width() / 2., p.get_height()), ha='center', va='center', xytext=(0, 10), textcoords='offset points')

        # Increase top paddigin to make room for the annotation
        subfig[0].set_ylim(top=subfig[0].get_ylim()[1] * 1.2)



        subfig[1].set_title('Memory Usage [MB]')
        ax = sns.barplot(data=group, x='library', y='memory_usage', ax=subfig[1], hue='platform', errorbar=None, palette='hls').set_yscale('log')
        subfig[1].set_xticklabels(['Julia-LinearAlgebra', 'Python-scipy', 'Java-ejml', 'MATLAB'], rotation=15)
        for p in subfig[1].patches:
            subfig[1].annotate(format(p.get_height(), '.0f'), (p.get_x() + p.get_width() / 2., p.get_height()), ha='center', va='center', xytext=(0, 10), textcoords='offset points')

        # Increase top paddigin to make room for the annotation
        subfig[1].set_ylim(top=subfig[1].get_ylim()[1] * 1.2)

        subfig[2].set_title('Relative Error')
        ax = sns.barplot(data=group, x='library', y='relative_error', ax=subfig[2], hue='platform', errorbar=None, palette='hls').set_yscale('log')
        subfig[2].set_xticklabels(['Julia-LinearAlgebra', 'Python-scipy', 'Java-ejml', 'MATLAB'], rotation=15)
        for p in subfig[2].patches:
            subfig[2].annotate(format(p.get_height(), '.1e'), (p.get_x() + p.get_width() / 2., p.get_height()), ha='center', va='center', xytext=(0, 10), textcoords='offset points')
        # Increase top paddigin to make room for the annotation
        subfig[2].set_ylim(top=subfig[2].get_ylim()[1] * 1.2)


        # Rewmove the legend from the subplots
        subfig[0].get_legend().remove()
        subfig[1].get_legend().remove()
        subfig[2].get_legend().remove()

        # Add a legend to the figure
        handles, labels = subfig[0].get_legend_handles_labels()
        fig.legend(handles, labels, loc='lower center', ncol=4, fontsize=12)



        save_path = os.path.join(os.getcwd(), 'reports', 'plots')
        if (os.path.exists(save_path) == False):
            os.makedirs(save_path)

        plt.tight_layout()
        plt.savefig(os.path.join(save_path, name))
        plt.close()



def main():

    # Load each report file in the ./report directory into a dataframe
    report_dir = './reports'
    report_files = os.listdir(report_dir)

    reports_df = pd.DataFrame()

    print(report_files)

    for report_file in report_files:
        if report_file.endswith('.csv') and report_file != 'report-MACHINE-PLATFORM-Runtime.csv' and report_file != 'summary.csv':
            report = pd.read_csv(os.path.join(report_dir, report_file), sep=',')

            reports_df = pd.concat([reports_df, report], ignore_index=True)


    reports_df = clean_data(reports_df)
    print(reports_df)
    reports_df.to_csv('./reports/summary.csv', sep=',', index=True)

    make_plot(reports_df, 'T14')


if __name__ == '__main__':
    main()

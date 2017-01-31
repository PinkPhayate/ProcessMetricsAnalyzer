import csv
import pandas as pd
bug_info_dir = "/Users/phayate/src/ApacheDerby/ApacheDerbyBugList/"
process_metrics_dir = "/Users/phayate/src/ApacheDerby/process-metrics/"
bug_df = pd.DataFrame([])

def transform_buglist(version):
    f=open(bug_info_dir + 'v'+version+'_bugExistFile1119.txt')
    lines2 = f.readlines()
    f.close()
    list = []

    for line in lines2:
        filename = line.split(",")[0]
        filename = filename.replace('jad', 'java')
        list.append( filename )

    with open(bug_info_dir + version+'-buglist.csv', 'w') as f:
        writer = csv.writer(f, lineterminator='\n')
        writer.writerow(list)

    return list


### get process metrics
def func(x):
    filename = x['fileName']
    for bf in bug_df:
        if bf in filename:
            return True

def merge(src_version, bug_version):
    # get bug file list
    global bug_df
    bug_df = pd.read_csv(bug_info_dir + bug_version+'-buglist.csv',header=0)

    # get process metrics
    df = pd.read_csv(process_metrics_dir + "ProcessMetrics" + src_version + ".csv", header=0)
    # merge
    tmp = df.apply(lambda x: 1 if func(x) else 0, axis=1)
    df = pd.concat([df, tmp], axis=1)
    df.to_csv(bug_info_dir + src_version+'-process-bug.csv')




dict = {
# '10.7':'10.8.1.2',
'10.8':'10.9.1.0',
'10.9':'10.10.1.1',
'10.10':'10.11.1.1',
}

for key, value in dict.iteritems():
    print(key,value)
    transform_buglist(value)
    merge(key, value)

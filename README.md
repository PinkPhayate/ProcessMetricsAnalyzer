# DiffMetricsAnalyzer

## Overview
This program works to get software differences metrics between two version.
Software differences metrics is defined by [1].

## Description
diff algorithm references Mayor algorithm[2].
references [this program](http://chat-messenger.net/blog-entry-55.html) that gets list all files from a directory.

## Requirement
java(1.8.0_73)

## Feature
When this program is executed, put 3 files in the same directory.
- processmetrics.csv
- report.txt
- DiffAnalzerLog
	This log show that new files in later version.


## Usage
This repository has jar file and you can use by operating below command in your terminal.

    java -jar PRMA.jar  target_software_repository later_version_directory_name older_version_directory_name suffix


- target_software_repository  ->  that repository must have more two versionds per one software.
- later_version_directory_name
- older_version_directory_name
- prefix  ->  file's suffix about softeare type. Type of programming language.	ex.) java, cs
I have not tested excepting for java and c sharp.

## Reference
1.Nagappan N, Ball T. Use of Relative Code Churn Measures to Predict. Proc. 27th Int. Conf. on Softw. Eng., ICSE’5, 2005, pp284-282.
2.http://www.ne.jp/asahi/hishidama/home/tech/java/google/java-diff-utils.html
3.http://chat-messenger.net/blog-entry-55.html#1
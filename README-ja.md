# DiffMetricsAnalyzer (DIMA)

## 概要
ソフトウェアのバージョン間の差分をメトリクス[1]にして取り出すプログラム

## 機能
ファイル間の差分を取得するアルゴリズムはMayorのものを、そのアルゴリズが実装されたオープンソース[2]のライブラリを使用しました。
また、特定のディレクトリ下に存在するファイルのリストを取得するプログラムには、[3]を用いました。

このプログラムを実行した後、以下の３つのファイルが出力されます。
- processmetrics.csv
	各モジュールのメトリクスが出力されます。
- report.txt
	総ファイル数や、両バージョンに存在したファイル数、バージョンアップに伴って新しくできたファイルなどを見ることができます。
- DiffAnalzerLog
	このログファイルで、新しいファイル、およびモジュール名が取得できなかったファイルを確認することができます。


## 動作環境
java(1.8.0_73)


## 使い方
ターミナル（またはコマンドプロンプト）で以下のコマンドを実行してください。

+ Mac
```terminal
java -jar DIMA.jar later_version_directory_name older_version_directory_name suffix
```

+ Windows
```terminal
java -jar DIMA-win.jar later_version_directory_name older_version_directory_name suffix
```

- later_version_directory_name -> 新しい方のバージョンのソースコードのレポジトリ

- older_version_directory_name -> 古い方ののバージョンのソースコードのレポジトリ

- prefix  ->  ソフトウェアを構成するソースコードのタイプ。現在は１種類のみ対応しています。
    ex.) java, cs	など

* 現在、この二種類のソフトウェアでのみ、動作を確認しています。


## サンプル

望ましいディレクトリ構造を、このレポジトリのsampleディレクトリに示します。その時の実行コマンドは以下になります。

### コマンド
+ mac
```terrminal
java -jar DIMA.jar ./sample/curr ./sample/prev cs
```

+ windows
```terrminal
java -jar DIMA-win.jar ./sample/curr ./sample/prev cs
```


### 実行結果
```report.txt
Number of files in Directory	: 7
Number of current class			: 8
Number of previous class		: 6
Number of exist module			: 6
Number of new module			: 2
```
サンプル実行時に出力されるProcessMetrics.csvが、ディレクトリ上にあるexProcessMetrics.csvと等しければOK

## 参考文献
1.Nagappan N, Ball T. Use of Relative Code Churn Measures to Predict. Proc. 27th Int. Conf. on Softw. Eng., ICSE’5, 2005, pp284-282.

2.http://www.ne.jp/asahi/hishidama/home/tech/java/google/java-diff-utils.html

3.http://chat-messenger.net/blog-entry-55.html#1

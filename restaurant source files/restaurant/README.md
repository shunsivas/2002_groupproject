# Java Source

Java source files are in `src/`.

## Build and run

On macOS or Linux:

```sh
./build.sh
./run.sh
```

On Windows PowerShell:

```powershell
javac -d out (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName })
java -cp out restaurant.Main
```

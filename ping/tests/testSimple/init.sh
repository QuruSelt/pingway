mkdir -p src
mkdir -p src/trashFolder/
touch src/main.c
touch src/trashFolder/trash

echo "src/trashFolder" > .myideignore
echo "src/trashFolder/trash" >> .myideignore
echo "nothing" >> .myideignore

echo "int main() {" > src/main.c
echo "    print(\"Hello World\");" >> src/main.c
echo "}" >> src/main.c

echo "Test project set up!"
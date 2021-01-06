Default: prepare genSourceFiles genClassFiles run clean

prepare:
	rm -rf target javaFiles classFiles *.card *.auth

genSourceFiles:
	find . -name "*.java" > javaFiles
	mkdir target
	javac -cp ".:./lib/java-json.jar:./lib/commons-cli-1.4.jar" -d ./target @javaFiles
genClassFiles:
	find . -name "*.class" > classFiles
run:
	jar cvfm atm.jar atm.mf ./lib -C ./target .
	jar cvfm bank.jar bank.mf ./lib -C ./target .
	cat stub.sh bank.jar > bank && chmod +x bank
	cat stub.sh atm.jar > atm && chmod +x atm
clean:
	rm -rf target javaFiles classFiles *.card *.auth
